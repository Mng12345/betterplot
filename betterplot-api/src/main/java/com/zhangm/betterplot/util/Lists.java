package com.zhangm.betterplot.util;

import java.util.ArrayList;
import java.util.Collections;


/**
 * @Author zhangming
 * @Date 2020/6/30 9:27
 */
public interface Lists {

    @SafeVarargs
    static <E> ArrayList<E> listOf(E... elements) {
        return com.google.common.collect.Lists.newArrayList(elements);
    }
}
