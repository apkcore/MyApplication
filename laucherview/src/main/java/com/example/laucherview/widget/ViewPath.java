package com.example.laucherview.widget;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 为了实现跟绘图时连续画线的效果，我们需要一个路径信息Path将路径的操作先后保存起来：
 */
public class ViewPath {

    // 四种路径操作
    public static final int MOVE = 0;
    public static final int LINE = 1;
    public static final int QUAD = 2;
    public static final int CURVE = 3;

    private ArrayList<ViewPoint> mPoints;


    public ViewPath() {
        mPoints = new ArrayList<>();
    }

    // 添加重置起点的路径
    public void moveTo(float x, float y) {
        mPoints.add(ViewPoint.moveTo(x, y, MOVE));
    }

    // 添加直线移动的路径
    public void lineTo(float x, float y) {
        mPoints.add(ViewPoint.lineTo(x, y, LINE));
    }

    // 添加三阶贝塞尔移动的路径
    public void curveTo(float x, float y, float x1, float y1, float endX, float endY) {
        mPoints.add(ViewPoint.curveTo(x, y, x1, y1, endX, endY, CURVE));
    }

    // 添加二阶贝塞尔移动的路径
    public void quadTo(float x, float y, float endX, float endY) {
        mPoints.add(ViewPoint.quadTo(x, y, endX, endY, QUAD));
    }

    // 获取路径集合
    public Collection<ViewPoint> getPoints() {
        return mPoints;
    }


}
