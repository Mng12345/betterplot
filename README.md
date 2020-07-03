# betterplot
An matplotlib like api for java, continuous improving! This project was built just because plotting chart is not too easy and clear in java than in python, so i copyed the plot api in matplotlib and converted it in java.

# steps
## step1.
Download the prebuild packages(betterplot-api, betterplot-model, betterplot-server). Add the betterplot-model and betterplot-api packages into your own local maven repository.
## step2.
Start the springboot application. java -jar betterplot-server[...].jar
## step3.
Use the api supported by betterplot-api package to plot something.

# examples

## example01
### api
`public static void plot(List<? extends Object> x, List<? extends Number> y)`
### code
`Plot.plot(Lists.listOf(2, 3, 4), Lists.listOf(4, 5, 6));`
### chart
![image](https://github.com/Mng12345/betterplot/blob/master/examples/example01.jpg)

## example02
### api
`public static void plot(List<? extends List<? extends Object>> x, List<? extends List<? extends Number>> y, String[] legend,
                            String[] lineTypes, String title, String xLabel)`
### code
`Plot.plot(Lists.listOf(Lists.listOf(1, 2, 3), Lists.listOf(2, 4, 5, 6), Lists.listOf(4, 5, 6, 8, 12)),
                Lists.listOf(Lists.listOf(3.2, 3.2, 4.5), Lists.listOf(1.1, 3.2, 4.7, 9.9), Lists.listOf(2.1, 3.2, 9.8, 12.2, 10.1)),
                new String[] {"y1", "y2", "y3"}, new String[] {"line", "scatter", "line"},
                "multi-line-plot", "x");`
### chart       
![image](https://github.com/Mng12345/betterplot/blob/master/examples/example02.jpg)

## example03 Stream api `of` for easy use

### api
`public static InnerPlot of()`

### code
`
Plot.of()<br>
    .x(Lists.listOf(Lists.listOf(1, 2, 3), Lists.listOf(2, 4, 5, 6), Lists.listOf(4, 5, 6, 8, 12)))<br>
    .y(Lists.listOf(Lists.listOf(3.2, 3.2, 4.5), Lists.listOf(1.1, 3.2, 4.7, 9.9), Lists.listOf(2.1, 3.2, 9.8, 12.2, 10.1)))<br>
    .legend("y1", "y2", "y3")<br>
    .lineTypes("line", "scatter", "line")<br>
    .title("mulit-line-stream-api")<br>
    .xLabel("x")<br>
    .show();<br>
`

### chart 
![image](https://github.com/Mng12345/betterplot/blob/master/examples/example03.jpg)


