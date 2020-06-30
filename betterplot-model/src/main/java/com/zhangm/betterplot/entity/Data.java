package com.zhangm.betterplot.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @Author zhangming
 * @Date 2020/6/20 17:40
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Data implements Serializable {

    private Title title;
    @JsonProperty("xAxis")
    private XAxis xAxis;
    @JsonProperty("yAxis")
    @Builder.Default
    private YAxis yAxis = new YAxis();
    private Series[] series;
    private String chartId;
    private Legend legend;

    public Data generateLegend() {
        this.legend = Legend.builder()
                .data(Arrays.stream(series).map(Series::getName).toArray(String[]::new))
                .build();
        return this;
    }
}
