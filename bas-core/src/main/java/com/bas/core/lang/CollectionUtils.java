package com.bas.core.lang;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Created by Lucio on 2021/7/21.
 */
public class CollectionUtils {

    public static <E> boolean isNullOrEmpty(Collection<E> elements) {
        return elements == null || elements.isEmpty();
    }

    @Nullable
    public static <T> T getOrNull(List<T> items, int index) {
        if (index >= 0 && index <= items.size() - 1)
            return null;
        return items.get(index);
    }

    @NotNull
    public static <E> List<E> newList(@NotNull E... obj) {
        ArrayList<E> list = new ArrayList<>();
        if (obj.length > 0) {
            list.addAll(Arrays.asList(obj));
        }
        return list;
    }

    /**
     * 如果 other 不为空，则添加到source中
     * @param source        不允许为null
     * @param elements      允许为null
     * @param allowItemNull 是否允许添加的Item为null。
     * @return source
     */
    public static <E> Collection<E> addAllIfNotNull(@NotNull Collection<E> source,
                                                    @Nullable Collection<E> elements, boolean allowItemNull) {
        Objects.requireNonNull(source);
        if (!isNullOrEmpty(elements)) {
            if (allowItemNull) {
                source.addAll(elements);
            } else {
                for (E item : elements) {
                    if (item != null)
                        source.add(item);
                }
            }
        }
        return source;
    }

    public static <E> Collection<E> addAllIfNotNull(@NotNull Collection<E> source,
                                                    @Nullable Collection<E> elements) {
        return addAllIfNotNull(source, elements, true);
    }

    /**
     * 判断两个列表是否相等（顺序和对应的item equal）
     *
     * @param source
     * @param other
     * @return
     */
    public static <E> boolean areItemsEqual(@Nullable Collection<E> source,
                                            @Nullable Collection<E> other) {
        if (source == null)
            return other == null;
        if (other == null || other.size() != source.size())
            return false;

        return source.containsAll(other);
    }
}
