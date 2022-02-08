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
 * 集合工具类
 */
public class CollectionUtils {

    public static <E> boolean isNullOrEmpty(@Nullable Collection<E> elements) {
        return elements == null || elements.isEmpty();
    }

    @Nullable
    public static <E> E getOrNull(@NotNull List<E> source, int index) {
        Objects.requireNonNull(source);
        if (index >= 0 && index <= source.size() - 1)
            return null;
        return source.get(index);
    }

    @SafeVarargs
    @NotNull
    public static <E> List<E> newList(@NotNull E... elements) {
        return new ArrayList<>(Arrays.asList(elements));
    }

    /**
     * 如果 other 不为空，则添加到source中
     *
     * @param destination    不允许为null
     * @param elements       允许为null
     * @param filterNotNulls 是否允许添加的Item为null。
     */
    public static <E> void mergeTo(@NotNull Collection<E> destination,
                                   @Nullable Collection<E> elements,
                                   boolean filterNotNulls) {
        Objects.requireNonNull(destination);
        if (isNullOrEmpty(elements))
            return;
        if (filterNotNulls) {
            for (E item : elements) {
                if (item != null)
                    destination.add(item);
            }
        } else {
            destination.addAll(elements);
        }
    }

    public static <E> void mergeTo(@NotNull Collection<E> source,
                                   @Nullable Collection<E> elements) {
        mergeTo(source, elements, false);
    }

    /**
     * 判断两个列表是否相等（顺序和对应的item equal）
     *
     * @param source 源数据集合
     * @param other  其他数据源
     * @return 如果{@link source}与@{link other}数据集合中的元素相同，则返回true，其他情况返回false。
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
