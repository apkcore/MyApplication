package com.example.charttest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LineChart mLineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLineChart = (LineChart) findViewById(R.id.chart);

        // mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Bold.ttf");
    }


    @Override
    protected void onStart() {
        LineData mLineData = getLineData();
        showChart(mLineChart, mLineData, Color.rgb(114, 188, 223));
        super.onStart();
    }

    // 设置显示的样式
    private void showChart(LineChart lineChart, LineData lineData, int color) {
        lineChart.setDrawBorders(false); // 是否在折线图上添加边框

        // enable / disable grid background
        lineChart.setDrawGridBackground(false); // 是否显示表格颜色
        lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度

        // enable touch gestures
        lineChart.setTouchEnabled(true); // 设置是否可以触摸

        // enable scaling and dragging
        lineChart.setDragEnabled(true);// 是否可以拖拽
        lineChart.setScaleEnabled(true);// 是否可以缩放
        lineChart.getAxisRight().setEnabled(false); // 隐藏右边 的坐标轴
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);//设置横坐标在底部
        lineChart.getXAxis().setGridColor(Color.TRANSPARENT);//去掉网格中竖线的显示
        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false);//

        lineChart.setBackgroundColor(color);// 设置背景

        // add data
        lineChart.setData(lineData); // 设置数据

        // get the legend (only possible after setting data)
        Legend mLegend = lineChart.getLegend(); // 设置比例图标示，就是那个一组y的value的

        // modify the legend ...
        // mLegend.setPosition(LegendPosition.LEFT_OF_CHART);
        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
        mLegend.setFormSize(6f);// 字体
        mLegend.setTextColor(Color.WHITE);// 颜色
        // mLegend.setTypeface(mTf);// 字体

        lineChart.animateX(2500); // 立即执行的动画,x轴
    }

    /**
     * 生成一个数据
     *
     * @return
     */
    private LineData getLineData() {
        String[] xData = new String[]{"a","b","c"};//获得的数据，下同
        Float[] yData = new Float[]{0.1f,0.2f,0.3f};//

        for (int i = 0; i < yData.length; i++) {
            System.out.println("lineChart_yData---:" + yData);
        }
        int dataLength = xData.length;
        List<String> xValues = new ArrayList<String>();
        for (int i = 0; i < dataLength; i++) {
            // x轴显示的数据，这里默认使用数字下标显示
            // xValues.add("" + i);
            xValues.add(xData[i]);
        }

        // y轴的数据
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        for (int i = 0; i < dataLength; i++) {
            // float value = (float) (Math.random() * range) + 3;
            // yValues.add(new Entry(value, i));
            yValues.add(new Entry(yData[i], i));
        }

        // create a dataset and give it a type
        // y轴的数据集合
        LineDataSet lineDataSet = new LineDataSet(yValues, "温度数据一览图" /* 显示在比例图上 */);
        // mLineDataSet.setFillAlpha(110);
        // mLineDataSet.setFillColor(Color.RED);

        // 用y轴的集合来设置参数
        lineDataSet.setLineWidth(1.75f); // 线宽
        lineDataSet.setCircleSize(3f);// 显示的圆形大小
        lineDataSet.setColor(Color.WHITE);// 显示颜色
        lineDataSet.setCircleColor(Color.WHITE);// 圆形的颜色
        lineDataSet.setHighLightColor(Color.WHITE); // 高亮的线的颜色

        List<ILineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(lineDataSet); // add the datasets
        lineDataSet.setDrawCircles(false);
        lineDataSet.setCubicIntensity(0.6f);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(Color.rgb(0, 255, 255));
        // create a data object with the datasets
        LineData lineData = new LineData(lineDataSets);

        return lineData;
    }

//    /**
//     */
//    public Float[] stringTofloat(String[] strings) {
//        Float[] data = new Float[strings.length];
//        for (int i = 0; i < strings.length; i++) {
//            data[i] = Float.valueOf(strings[i].trim().replace("℃", ""));
//            System.out.println("转换后的数据:" + data[i]);
//        }
//        return data;
//    }
}