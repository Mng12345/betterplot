package com.zhangm.betterplot.entity;

import lombok.*;

/**
 * @Author zhangming
 * @Date 2020/6/21 0:00
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GlobalResult<T> {

    private Integer status;
    private String message;
    private T data;

}
