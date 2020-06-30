package com.zhangm.betterplot.entity;

import lombok.*;

import java.io.Serializable;

/**
 * @Author zhangming
 * @Date 2020/6/21 12:49
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class XAxis implements Serializable {

    private Object[] data;
    private String name;
    private String type;

}
