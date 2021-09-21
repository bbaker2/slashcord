package com.bbaker.slashcord.handler;

import static com.bbaker.slashcord.util.CommonsUtil.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.interaction.callback.InteractionFollowupMessageBuilder;
import org.javacord.api.interaction.callback.InteractionImmediateResponseBuilder;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;

import com.bbaker.slashcord.handler.annotation.Slash;
import com.bbaker.slashcord.handler.annotation.SlashException;
import com.bbaker.slashcord.handler.annotation.SlashMeta;
import com.bbaker.slashcord.handler.annotation.SlashOption;
import com.bbaker.slashcord.handler.args.AlwaysNullArgument;
import com.bbaker.slashcord.handler.args.ApiMetaArgument;
import com.bbaker.slashcord.handler.args.BooleanOptionArgument;
import com.bbaker.slashcord.handler.args.ChannelMetaArgument;
import com.bbaker.slashcord.handler.args.ChannelOptionArgument;
import com.bbaker.slashcord.handler.args.FollowupResponseMetaArgument;
import com.bbaker.slashcord.handler.args.ImmediateResponseMetaArgument;
import com.bbaker.slashcord.handler.args.IntegerOptionArgument;
import com.bbaker.slashcord.handler.args.InteractionMetaArgument;
import com.bbaker.slashcord.handler.args.InteractionOptionArgument;
import com.bbaker.slashcord.handler.args.RoleOptionArgument;
import com.bbaker.slashcord.handler.args.StringOptionArgument;
import com.bbaker.slashcord.handler.args.UserMetaArgument;
import com.bbaker.slashcord.handler.args.UserOptionArgument;
import com.bbaker.slashcord.handler.response.AutoSuccess;
import com.bbaker.slashcord.handler.response.StringResponse;
import com.bbaker.slashcord.handler.response.ToStringResponse;
import com.bbaker.slashcord.handler.response.VoidResponse;
import com.bbaker.slashcord.handler.throwable.ExceptionHandler;
import com.bbaker.slashcord.handler.throwable.SuppressException;

public class SlashCommandListener implements SlashCommandCreateListener {

    Map<String, Consumer<SlashCommandInteraction>> listeners = new HashMap<>();

    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent event) {
        SlashCommandInteraction slashEvent = event.getSlashCommandInteraction();

        Optional<SlashCommandInteractionOption> first = slashEvent.getFirstOption();

        // construct the command hash which could be
        // cmd
        // cmd_sub
        // cmd_group_sub
        String hash = slashEvent.getCommandName();

        if(first.isPresent()) {
            SlashCommandInteractionOption gos = first.get();
            if(gos.isSubcommandOrGroup()) {
                hash += "_" + gos.getName();
            }

            Optional<SlashCommandInteractionOption> second = gos.getFirstOption();

            if(second.isPresent()) {
                SlashCommandInteractionOption sub = second.get();
                if(sub.isSubcommandOrGroup()) {
                    hash += "_" + sub.getName();
                }
            }
        }

        Consumer<SlashCommandInteraction> subscriber = listeners.get(hash);
        if(subscriber != null) {
            try {
                subscriber.accept(slashEvent);
            } catch (Throwable e) {
                // prevent the exception from propagating
                e.printStackTrace();
            }
        }
    }

    public void addListener(Object handler) {
        Method[] methods = handler.getClass().getMethods();

        for(Method cmdMethod : methods) {

            Slash slashCmd = cmdMethod.getAnnotation(Slash.class);

            // skip methods missing the SlashCommand annotation
            if(slashCmd == null) {
                continue;
            }

            // construct the command hash which could be
            // cmd
            // cmd_sub
            // cmd_group_sub
            String hash = slashCmd.command();

            if(isNotBlank(slashCmd.group()) && isNotBlank(slashCmd.sub())) {
                hash += "_" + slashCmd.group() + "_" + slashCmd.sub();
            } else if(isNotBlank(slashCmd.sub())) {
                hash += "_" + slashCmd.sub();
            }


            Parameter[] params = cmdMethod.getParameters();
            Function<SlashCommandInteraction, Object>[] argHandlers = generateParamHandlers(params);
            BiConsumer<Object, SlashCommandInteraction> resultHandler = generateResultHandler(cmdMethod.getReturnType());
            Function<Throwable, String> exceptionHandler = generateExceptionHandler(cmdMethod);

            SlashCommandHandler h = new SlashCommandHandler(handler, cmdMethod, resultHandler, argHandlers, exceptionHandler);
            listeners.put(hash, h);
        }

    }

    private Function<Throwable, String> generateExceptionHandler(Method method){
        SlashException[] slashExceptions = method.getAnnotationsByType(SlashException.class);
        if(slashExceptions.length == 0) {
            return new SuppressException();
        } else {
            List<Class<? extends Throwable>> whitelist = new ArrayList<>();
            for(SlashException se : slashExceptions) {
                for(Class<? extends Throwable> allowed : se.value()) {
                    whitelist.add(allowed);
                }
            }
            return new ExceptionHandler(whitelist);
        }

    }

    private BiConsumer<Object, SlashCommandInteraction> generateResultHandler(Class<?> returnType) {
        if(returnType.equals(Void.TYPE)){
            return new VoidResponse();
        } else if(returnType.isAssignableFrom(String.class)){
            return new StringResponse();
        } else if(hasCustomToString(returnType)){
            return new ToStringResponse();
        } else {
            return new AutoSuccess();
        }
    }

    private static boolean hasCustomToString(Class<?> returnType) {
        try {
            String fullyQualifiedName = returnType.getMethod("toString").getDeclaringClass().toString();
            return !fullyQualifiedName.equals(Object.class.toString());
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            return false; // should never be called. All methods have toString
        }
    }

    private Function<SlashCommandInteraction, Object>[] generateParamHandlers(Parameter[] params){
        Function<SlashCommandInteraction, Object>[] args = new Function[params.length];

        for(int i = 0; i < params.length; i++) {
            Parameter param = params[i];
            String name = param.getName();

            SlashOption nameOverride = param.getAnnotation(SlashOption.class);
            if(nameOverride != null) {
                name = nameOverride.value();
            }

            boolean isMeta = param.getAnnotation(SlashMeta.class) != null;

            Class<?> type = param.getType();
            // Meta Arguments
            if(type.isAssignableFrom(DiscordApi.class)) {
                args[i] = new ApiMetaArgument();

            } else if (type.isAssignableFrom(TextChannel.class)) {
                args[i] = new ChannelMetaArgument();

            } else if (type.isAssignableFrom(SlashCommandInteraction.class)) {
                args[i] = new InteractionMetaArgument();

            } else if (type.isAssignableFrom(InteractionImmediateResponseBuilder.class)) {
                args[i] = new ImmediateResponseMetaArgument();

            } else if (type.isAssignableFrom(InteractionFollowupMessageBuilder.class)) {
                args[i] = new FollowupResponseMetaArgument();

            // Meta AND Option Arguments
            } else if (type.isAssignableFrom(User.class)) {
                if(isMeta) {
                    args[i] = new UserMetaArgument();
                } else {
                    args[i] = new UserOptionArgument(name);
                }

            // Option Arguments
            } else if (type.isAssignableFrom(Integer.class)) {
                args[i] = new IntegerOptionArgument(name);

            } else if (type.isAssignableFrom(String.class)) {
                args[i] = new StringOptionArgument(name);

            } else if (type.isAssignableFrom(Boolean.class)) {
                args[i] = new BooleanOptionArgument(name);

            } else if (type.isAssignableFrom(ServerChannel.class)) {
                args[i] = new ChannelOptionArgument(name);

            } else if (type.isAssignableFrom(Role.class)) {
                args[i] = new RoleOptionArgument(name);

            } else if (type.isAssignableFrom(SlashCommandInteractionOption.class)) {
                args[i] = new InteractionOptionArgument(name);
            } else {
                args[i] = new AlwaysNullArgument();
            }

        }
        return args;
    }


}
