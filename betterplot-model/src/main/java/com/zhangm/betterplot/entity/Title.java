package com.zhangm.betterplot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author zhangming
 * @Date 2020/6/21 18:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Title implements Serializable {

    private String text;

}
