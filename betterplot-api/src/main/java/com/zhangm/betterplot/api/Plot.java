package com.zhangm.betterplot.api;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Streams;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.zhangm.betterplot.entity.Data;
import com.zhangm.betterplot.entity.Series;
import com.zhangm.betterplot.entity.Title;
import com.zhangm.betterplot.entity.XAxis;
import com.zhangm.betterplot.util.DataUtil;
import com.zhangm.betterplot.util.Lists;
import com.zhangm.easyutil.Tuple;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author zhangming
 * @Date 2020/6/28 17:18
 */
public class Plot {

    static Config config = ConfigFactory.load("conf");

    // 控制httpClient不打印日志信息
    static {
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "fatal");
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class InnerPlot<E> {

        private List<? extends List<? extends Object>> x;
        private List<? extends List<? extends Number>> y;
        private String[] legend;
        private String[] lineTypes;
        private String title;
        private String xLabel;

        public InnerPlot x(List<? extends List<? extends Object>> x) {
            this.x = x;
            return this;
        }

        public InnerPlot x(double[]... x) {
            this.x = Arrays.stream(x).map(xItem -> Arrays.stream(xItem).boxed().collect(Collectors.toList()))
                    .collect(Collectors.toList());
            return this;
        }

        public InnerPlot x(String[]... x) {
            this.x = Arrays.stream(x).map(xItem -> Arrays.stream(xItem).collect(Collectors.toList())).collect(Collectors.toList());
            return this;
        }

        public final InnerPlot x(List<?>... x) {
            this.x = Arrays.stream(x).collect(Collectors.toList());
            return this;
        }

        public InnerPlot y(List<? extends List<? extends Number>> y) {
            this.y = y;
            return this;
        }

        public InnerPlot y(double[]... y) {
            this.y = Arrays.stream(y).map(xItem -> Arrays.stream(xItem).boxed().collect(Collectors.toList()))
                    .collect(Collectors.toList());
            return this;
        }

        public InnerPlot y(int[]... y) {
            this.y = Arrays.stream(y).map(xItem -> Arrays.stream(xItem).boxed().collect(Collectors.toList())).collect(Collectors.toList());
            return this;
        }

        @SafeVarargs
        public final InnerPlot y(List<? extends Number>... y) {
            this.y = Arrays.stream(y).collect(Collectors.toList());
            return this;
        }

        public InnerPlot legend(String... legend) {
            if (legend.length != y.size()) {
                throw new RuntimeException("the length of lengend must equals to the length of y");
            }
            this.legend = legend;
            return this;
        }

        public InnerPlot lineTypes(String... lineTypes) {
            if (lineTypes.length != y.size()) {
                throw new RuntimeException("the length of lineTypes must equals to the length of y");
            }
            this.lineTypes = lineTypes;
            return this;
        }

        public InnerPlot title(String title) {
            this.title = title;
            return this;
        }

        public InnerPlot xLabel(String xLabel) {
            this.xLabel = xLabel;
            return this;
        }

        /**
         * 检测所有参数是否全部设置完成
         */
        private void checkArgs() {
            if (x == null || y == null || legend == null || lineTypes == null || "".equals(title) || "".equals(xLabel)) {
                throw new RuntimeException("请完成所有参数配置，title和xLabel不能为空字符串");
            }
        }

        public void show() {
            checkArgs();
            String serverHost = config.getString("betterplot-server.host");
            String port = config.getString("betterplot-server.port");
            // 合并x和y
            Object[][] arrayX = x.stream().map(item -> item.toArray(Object[]::new)).toArray(Object[][]::new);
            Number[][] arrayY = y.stream().map(item -> item.toArray(Number[]::new)).toArray(Number[][]::new);
            Tuple<Object[], Number[][]> mergedXY = DataUtil.merge(arrayX, arrayY);
            Data data = Data.builder()
                    .title(Title.builder().text(title).build())
                    .xAxis(XAxis.builder().data(mergedXY.getV1()).type("category").name(xLabel).build())
                    .series(Streams.mapWithIndex(Arrays.stream(mergedXY.getV2()), (Number[] item, long index) ->
                            Series.builder().data(item).type(lineTypes[(int)index]).name(legend[(int)index]).build()
                    ).toArray(Series[]::new))
                    .build()
                    .generateLegend();
            try {
                pushData(data);
                System.out.println(String.format("click http://%s:%s/ to show the chart", serverHost, port));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    public static InnerPlot of() {
        return new InnerPlot();
    }

    private static void pushData(Data data) throws IOException {
        String serverHost = config.getString("betterplot-server.host");
        String port = config.getString("betterplot-server.port");
        String plotUrl = String.format("http://%s:%s/api/plot", serverHost, port);
        HttpEntity entity = new StringEntity(JSON.toJSONString(data));
        HttpPost post = new HttpPost(plotUrl);
        post.addHeader("Content-Type", "application/json");
        post.setEntity(entity);
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        closeableHttpClient.execute(post);
    }

    /**
     * matplotlib plot([1, 2, 3, 4], [1, 4, 2, 3])
     * @param x
     * @param y
     * @return
     */
    public static void plot(List<? extends Object> x, List<? extends Number> y) {
        try {
            String serverHost = config.getString("betterplot-server.host");
            String port = config.getString("betterplot-server.port");
            Data data = Data.builder()
                    .title(Title.builder().text("x-y").build())
                    .xAxis(XAxis.builder().name("x").type("category").data(x.toArray(Object[]::new)).build())
                    .series(new Series[]{
                            Series.builder().name("y").type("line").data(y.toArray(Number[]::new)).build()
                        })
                    .build()
                    .generateLegend();
            pushData(data);
            System.out.println(String.format("click http://%s:%s/ to show the chart", serverHost, port));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * multi line
     * @param x
     * @param y
     * @param legend
     * @param lineTypes
     * @param title
     * @param xLabel
     */
    public static void plot(List<? extends List<? extends Object>> x, List<? extends List<? extends Number>> y, String[] legend,
                            String[] lineTypes, String title, String xLabel) {
        of().x(x).y(y).legend(legend).lineTypes(lineTypes).xLabel(xLabel).show();
    }

    public static void main(String[] args) {
//        Plot.plot(Lists.listOf(2, 3, 4), Lists.listOf(4, 5, 6));
//        Plot.plot(Lists.listOf(Lists.listOf(1, 2, 3), Lists.listOf(4, 5, 6)),
//                Lists.listOf(Lists.listOf(3.2, 3.2, 4.5), Lists.listOf(3.2, 4.7, 9.9)),
//                new String[] {"y1", "y2"}, new String[] {"line", "scatter"},
//                "multi-line-plot", "x");
        Plot.plot(Lists.listOf(Lists.listOf(1, 2, 3), Lists.listOf(2, 4, 5, 6), Lists.listOf(4, 5, 6, 8, 12)),
                Lists.listOf(Lists.listOf(3.2, 3.2, 4.5), Lists.listOf(1.1, 3.2, 4.7, 9.9), Lists.listOf(2.1, 3.2, 9.8, 12.2, 10.1)),
                new String[] {"y1", "y2", "y3"}, new String[] {"line", "scatter", "line"},
                "multi-line-plot", "x");
        Plot.of()
                .x(Lists.listOf(Lists.listOf(1, 2, 3), Lists.listOf(2, 4, 5, 6), Lists.listOf(4, 5, 6, 8, 12)))
                .y(Lists.listOf(Lists.listOf(3.2, 3.2, 4.5), Lists.listOf(1.1, 3.2, 4.7, 9.9), Lists.listOf(2.1, 3.2, 9.8, 12.2, 10.1)))
                .legend("y1", "y2", "y3")
                .lineTypes("line", "scatter", "line")
                .title("mulit-line-stream-api")
                .xLabel("x")
                .show();
    }
}
