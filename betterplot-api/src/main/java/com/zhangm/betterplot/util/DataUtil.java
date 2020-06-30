package com.zhangm.betterplot.util;

import com.google.common.collect.Streams;
import com.zhangm.easyutil.RangeUtil;
import com.zhangm.easyutil.Tuple;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author zhangming
 * @Date 2020/6/29 14:01
 */
public interface DataUtil {

    /**
     * 将多个x合并为一个x，将缺失的y以null填充
     * @param x
     * @param y
     * @return
     */
    static Tuple<Object[], Number[][]> merge(Object[][] x, Number[][] y) {
        int pairNum = x.length;
        // 将x排序，并将y按照x的顺序再排序
        // 将x进行合并
        Object[] mergedX;
        Set<Object> xSets = new HashSet<>();
        for (int i=0; i<pairNum; i++) {
            xSets.addAll(Arrays.stream(x[i]).collect(Collectors.toList()));
            Object[] sortedX = new Object[x[i].length];
            int[] xIndex = RangeUtil.sortToIndex(x[i], sortedX);
            Number[] sortedY = RangeUtil.reindex(y[i], xIndex, new Number[y[i].length]);
            x[i] = sortedX;
            y[i] = sortedY;
        }
        mergedX = xSets.stream().sorted().toArray();
        Number[][] mergedY = new Number[pairNum][];
        // 初始化mergedY
        for (int i=0; i<pairNum; i++) {
            mergedY[i] = new Number[mergedX.length];
        }
        for (int xi=0; xi<pairNum; xi++) {
            int count = 0;
            Object[] xItem = x[xi];
            for (int i=0; i<mergedX.length; i++) {
                if (count >= xItem.length) {
                    mergedY[xi][i] = null;
                    continue;
                }
                if (mergedX[i].equals(xItem[count])) {
                    mergedY[xi][i] = y[xi][count++];
                } else {
                    mergedY[xi][i] = null;
                }
            }
        }
        return new Tuple<>(mergedX, mergedY);
    }
}
