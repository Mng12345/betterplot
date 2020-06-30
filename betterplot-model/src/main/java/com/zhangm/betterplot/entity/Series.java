package com.zhangm.betterplot.entity;

import lombok.*;

import java.io.Serializable;

/**
 * @Author zhangming
 * @Date 2020/6/20 17:38
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Series implements Serializable {

    private Number[] data;
    private String name;
    private String type;

}
