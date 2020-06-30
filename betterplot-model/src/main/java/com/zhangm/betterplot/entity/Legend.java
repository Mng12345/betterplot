package com.zhangm.betterplot.entity;

import lombok.*;

import java.io.Serializable;

/**
 * @Author zhangming
 * @Date 2020/6/22 20:37
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Legend implements Serializable {

    private String[] data;

}
