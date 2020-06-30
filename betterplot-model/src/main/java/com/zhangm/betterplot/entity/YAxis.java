package com.zhangm.betterplot.entity;

import lombok.*;

import java.io.Serializable;

/**
 * @Author zhangming
 * @Date 2020/6/21 13:14
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class YAxis implements Serializable {

    private String type;

    YAxis() {
        this.type = "value";
    }
}
