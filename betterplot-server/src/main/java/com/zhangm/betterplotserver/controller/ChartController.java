package com.zhangm.betterplotserver.controller;

import com.alibaba.fastjson.JSON;

import com.zhangm.betterplot.entity.Data;
import com.zhangm.betterplot.entity.Series;
import com.zhangm.betterplot.entity.Title;
import com.zhangm.betterplot.entity.XAxis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayDeque;
import java.util.Iterator;

/**
 * @Author zhangming
 * @Date 2020/6/19 16:13
 */
@RequestMapping("/api")
@RestController
@Slf4j
public class ChartController {

    private ArrayDeque<Data> chartQueue = new ArrayDeque<>(10);

    @GetMapping("/getchart")
    public Data getChart(@RequestParam("chartId") String chartId) {
        try {
            int chartIdNum = Integer.parseInt(chartId);
            if (0 <= chartIdNum && chartIdNum <= 9) {
                Iterator<Data> iterator = chartQueue.iterator();
                int count = 0;
                while (iterator.hasNext()) {
                    if (count == chartIdNum) {
                        return iterator.next();
                    }
                    iterator.next();
                    count++;
                }
                throw new RuntimeException("wrong chartId: " + chartId);
            } else {
                throw new RuntimeException("wrong chartId: " + chartId);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @PostMapping(value = "/plot", consumes = "application/json")
    public void plot(@RequestBody Data data) {
        if (chartQueue.size() >= 10) {
            chartQueue.poll();
        }
        chartQueue.add(data);
        log.info("received data: " + JSON.toJSONString(data));
    }

}
