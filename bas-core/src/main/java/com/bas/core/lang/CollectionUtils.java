package com.bas.core.lang;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Lucio on 2021/7/21.
 */
public class CollectionUtils {

    @NotNull
    public static <T> List<T> newList(@NotNull T... obj) {
        ArrayList<T> list =  new ArrayList<>();
        if(obj.length>0){
            list.addAll(Arrays.asList(obj));
        }
        return list;
    }
}
