package com.bbaker.slashcord.structure.util;

import java.util.Comparator;
import java.util.List;

public class CompareUtil {

    public static <T> boolean equalLists(List<T> a, List<T> b, Comparator<T> comp) {

        if(a == null && b == null) {
            return true;
        } else if (a == null ^ b == null){
            return false;
        } else if (a.size() != b.size()){
            return false;
        } else {
            for(int i = 0; i < a.size(); i++) {
                if(comp.compare(a.get(i), b.get(i)) != 0) {
                    return false;
                }
            }
            return true;
        }

    }

}
