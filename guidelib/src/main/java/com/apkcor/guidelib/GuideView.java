package com.apkcor.guidelib;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.ArrayList;

/**
 * des:“应用新功能”的用户指引view
 */
public class GuideView extends View {
    //高亮类型：矩形、圆形、椭圆形
    public static final int VIEWSTYLE_RECT = 0;
    public static final int VIEWSTYLE_CIRCLE = 1;
    public static final int VIEWSTYLE_OVAL = 2;
    //画笔类型，圆滑、默认
    public static final int MASKBLURSTYLE_SOLID = 0;
    public static final int MASKBLURSTYLE_NORMAL = 1;

    private View rootView;//activity的contentview,是FrameLayout
    private Bitmap jtUpLeft, jtUpRight, jtDownRight, jtDownLeft;// 指示箭头
    private Bitmap fgBitmap;// 前景
    private Canvas mCanvas;// 绘制蒙版层的画布
    private Paint mPaint;// 绘制蒙版层画笔
    private int screenW, screenH;// 屏幕宽高
    private int margin;
    private int radius;//圆半径
    private OnDismissListener onDismissListener;//关闭监听
    private Activity activity;

    /*******************
     * 可配置属性
     *****************************/
    private boolean touchOutsideCancel = true;//外部点击是否可关闭
    private int highLightStyle = VIEWSTYLE_RECT;//高亮类型默认圆形
    public int maskblurstyle = MASKBLURSTYLE_SOLID;//画笔类型默认
    private ArrayList<Bitmap> tipBitmaps;//显示图片
    private ArrayList<String> tipStr;//显示文字
    private ArrayList<View> targetViews;//高亮目标view
    private int maskColor = 0x99000000;// 蒙版层颜色
    private int borderWitdh = 10;
    private int highLisghtPadding = 0;// 高亮控件padding
    private int textSize = 15;
    private int textColor = Color.WHITE;
    private Paint paint;
    private PaintFlagsDrawFilter pfd;


    private GuideView(Activity activity) {
        super(activity);
        this.activity = activity;
        // 计算参数
        cal(activity);
        // 初始化对象
        init(activity);
    }

    public static GuideView builder(Activity activity) {
        return new GuideView(activity);
    }

    /**
     * 计算参数
     *
     * @param context 上下文环境引用
     */
    private void cal(Context context) {
        // 获取屏幕尺寸数组
        int[] screenSize = UiUtil.getScreenSize((Activity) context);
        // 获取屏幕宽高
        screenW = screenSize[0];
        screenH = screenSize[1];
    }

    /**
     * 初始化对象
     */
    private void init(Context context) {
        margin = UiUtil.dp2px(context,14);
        pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        paint = new Paint();
        paint.setFilterBitmap(true);
        paint.setAntiAlias(true);
        tipBitmaps = new ArrayList<>();
        tipStr = new ArrayList<>();
        targetViews = new ArrayList<>();
        rootView = ((Activity) getContext()).findViewById(android.R.id.content);
        // 实例化画笔并开启其抗锯齿和抗抖动
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        // 设置画笔透明度为0是关键！
        mPaint.setARGB(0, 255, 0, 0);
        // 设置混合模式为DST_IN
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        BlurMaskFilter.Blur blurStyle = null;
        switch (maskblurstyle) {
            case MASKBLURSTYLE_SOLID:
                blurStyle = BlurMaskFilter.Blur.SOLID;
                break;
            case MASKBLURSTYLE_NORMAL:
                blurStyle = BlurMaskFilter.Blur.NORMAL;
                break;
        }
        mPaint.setMaskFilter(new BlurMaskFilter(15, blurStyle));
        // 生成前景图Bitmap
        fgBitmap = Bitmap.createBitmap(screenW, screenH, Bitmap.Config.ARGB_4444);
        // 将其注入画布
        mCanvas = new Canvas(fgBitmap);
        // 绘制前景画布颜色
        mCanvas.drawColor(maskColor);
        // 实例化箭头图片
        jtDownRight = BitmapFactory.decodeResource(getResources(), R.mipmap.hl_down_right);
        jtDownLeft = BitmapFactory.decodeResource(getResources(), R.mipmap.hl_down_left);
        jtUpLeft = BitmapFactory.decodeResource(getResources(), R.mipmap.hl_up_left);
        jtUpRight = BitmapFactory.decodeResource(getResources(), R.mipmap.hl_up_right);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(textColor);
        paint.setTextSize(UiUtil.dp2px(this.getContext(), textSize));
        if (targetViews == null && tipBitmaps == null && tipStr == null)
            return;
        // 绘制前景
        canvas.drawBitmap(fgBitmap, 0, 0, null);
        //有高亮控件
        if (targetViews.size() > 0 && (tipBitmaps.size() > 0 || tipStr.size() > 0)) {
            for (int i = 0; i < targetViews.size(); i++) {
                //高亮控件宽高
                int vWidth = targetViews.get(i).getWidth();
                int vHeight = targetViews.get(i).getHeight();
                //获取获取高亮控件坐标
                int left = 0;
                int top = 0;
                int right = 0;
                int bottom = 0;
                try {
                    Rect rtLocation = ViewUtils.getLocationInView(((ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT)).getChildAt(0), targetViews.get(i));
                    left = rtLocation.left;
                    top = rtLocation.top;
                    right = rtLocation.right;
                    bottom = rtLocation.bottom;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //绘制高亮形状
                switch (highLightStyle) {
                    case VIEWSTYLE_OVAL:
                        RectF rectf = new RectF(left - highLisghtPadding, top - highLisghtPadding, right + highLisghtPadding, bottom + highLisghtPadding);
                        mCanvas.drawOval(rectf, mPaint);
                        break;
                    case VIEWSTYLE_RECT:
                        RectF rect = new RectF(left - borderWitdh - highLisghtPadding, top - borderWitdh - highLisghtPadding, right + borderWitdh + highLisghtPadding, bottom + borderWitdh + highLisghtPadding);
                        mCanvas.drawRoundRect(rect, 20, 20, mPaint);
                        break;
                    case VIEWSTYLE_CIRCLE:
                    default:
                        radius = vWidth > vHeight ? vWidth / 2 + highLisghtPadding / 2 : vHeight / 2 + highLisghtPadding / 2;
                        if (radius < UiUtil.dp2px(this.getContext(),15)) {
                            radius = UiUtil.dp2px(this.getContext(),33);
                        }
                        mCanvas.drawCircle(left + vWidth / 2, top + vHeight / 2, radius, mPaint);
                        break;

                }
                canvas.setDrawFilter(pfd);
                //绘制箭头和提示图
                if (bottom < screenH / 2 || (screenH / 2 - top > bottom - screenH / 2)) {// 偏上
                    int jtTop = highLightStyle == VIEWSTYLE_CIRCLE ? top + vHeight / 2 + highLisghtPadding + margin + radius : bottom + highLisghtPadding + margin;
                    if (right < screenW / 2 || (screenW / 2 - left > right - screenW / 2)) {//偏左
                        canvas.drawBitmap(jtUpLeft, left + vWidth / 2, jtTop, paint);
                        if (tipBitmaps.size() > i && tipBitmaps.get(i) != null) {
                            canvas.drawBitmap(tipBitmaps.get(i), left + vWidth / 2, jtTop + jtUpLeft.getHeight(), paint);
                        } else if (tipStr.size() > i && tipStr.get(i) != null) {
                            String text = tipStr.get(i);
                            int height = ViewUtils.getTextHeight(paint, text);
                            drawString(canvas, text, left + vWidth / 2, jtTop + height + jtUpLeft.getHeight(), paint);
                        }
                    } else {
                        canvas.drawBitmap(jtUpRight, left + vWidth / 2 - 100 - margin, jtTop, null);
                        if (tipBitmaps.size() > i && tipBitmaps.get(i) != null) {
                            canvas.drawBitmap(tipBitmaps.get(i), left + vWidth / 2 - 100 - tipBitmaps.get(i).getWidth() / 2, jtTop + jtUpRight.getHeight(), null);
                        } else if (tipStr.size() > i && tipStr.get(i) != null) {
                            String text = tipStr.get(i);
                            int height = ViewUtils.getTextHeight(paint, text);
                            int maxW = 0;
                            if (text.contains("\n")) {
                                String[] strs = text.split("\\n");
                                for (String str : strs) {
                                    maxW = maxW < ViewUtils.getTextWidth(paint, str) ? ViewUtils.getTextWidth(paint, str) : maxW;
                                }
                            } else {
                                maxW = ViewUtils.getTextWidth(paint, text);
                            }
                            drawString(canvas, text, right - maxW, jtTop + jtUpRight.getHeight() + height, paint);
                        }
                    }
                } else {
                    int jtTop = highLightStyle == VIEWSTYLE_CIRCLE ? top + vHeight / 2 - jtDownLeft.getHeight() - radius - highLisghtPadding - margin : top - jtDownLeft.getHeight() - margin - highLisghtPadding;
                    if (right < screenW / 2 || (screenW / 2 - left > right - screenW / 2)) {
                        canvas.drawBitmap(jtDownLeft, left + vWidth / 2, jtTop, null);
                        if (tipBitmaps.size() > i && tipBitmaps.get(i) != null) {
                            canvas.drawBitmap(tipBitmaps.get(i), left + vWidth / 2, jtTop - tipBitmaps.get(i).getHeight(), null);
                        } else if (tipStr.size() > i && tipStr.get(i) != null) {
                            String text = tipStr.get(i);
                            int height = ViewUtils.getTextHeight(paint, text);
                            int num = 0;
                            if (text.contains("\n")) {
                                String[] strs = text.split("\\n");
                                num = strs.length - 1;
                            }
                            drawString(canvas, text, left + vWidth / 2, jtTop - num * height - margin, paint);
                        }
                    } else {
                        canvas.drawBitmap(jtDownRight, left + vWidth / 2 - 100 - margin, jtTop, null);
                        if (tipBitmaps.size() > i && tipBitmaps.get(i) != null) {
                            canvas.drawBitmap(tipBitmaps.get(i), left + vWidth / 2 - 100 - tipBitmaps.get(i).getWidth() / 2 - margin, jtTop - tipBitmaps.get(i).getHeight(), null);
                        } else if (tipStr.size() > i && tipStr.get(i) != null) {
                            String text = tipStr.get(i);
                            int height = ViewUtils.getTextHeight(paint, text);
                            int num = 0;
                            int maxW = 0;
                            if (text.contains("\n")) {
                                String[] strs = text.split("\\n");
                                for (String str : strs) {
                                    maxW = maxW < ViewUtils.getTextWidth(paint, str) ? ViewUtils.getTextWidth(paint, str) : maxW;
                                }
                                num = strs.length - 1;
                            } else {
                                maxW = ViewUtils.getTextWidth(paint, text);
                            }
                            drawString(canvas, text, right - maxW, jtTop - num * height - margin, paint);
                        }
                    }
                }
            }
        }
        //无高亮控件，只是显示提示图片（仅能提示一张）
        else if (tipBitmaps.size() > 0) {
            canvas.drawBitmap(tipBitmaps.get(0), (screenW - tipBitmaps.get(0).getWidth()) / 2, (screenH - tipBitmaps.get(0).getHeight()) / 2, null);
        }
    }

    protected RectF drawString(Canvas canvas, String text, float x, float y, Paint paint) {
        int len = text.length();
        ViewUtils.getTextWidth(paint,text);
        RectF fullRect = new RectF();
        fullRect.top = y;
        fullRect.bottom = fullRect.top;
        fullRect.left = x;
        fullRect.right = fullRect.left;

        if (text != null) {
            String[] lines = text.split("\\n");
            Rect rect = new Rect();
            int yOff = 0;
            for (int i = 0; i < lines.length; ++i) {
                canvas.drawText(lines[i], x, y + yOff, paint);
                paint.getTextBounds(lines[i], 0, lines[i].length(), rect);
                yOff = yOff + rect.height() + 5;
                fullRect.bottom = fullRect.bottom + (float) rect.height() + 5.0f;
                if (paint.getTextAlign() == Paint.Align.LEFT) {
                    fullRect.right = Math.max(fullRect.right, fullRect.left + (float) rect.width());
                } else {
                    fullRect.left = Math.min(fullRect.left, fullRect.right - (float) rect.width());
                }
            }
        }

        fullRect.top -= 10.0f;
        fullRect.bottom += 5.0f;

        return fullRect;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP://
                if (touchOutsideCancel) {
                    this.setVisibility(View.GONE);
                    //移除view
                    if (rootView != null) {
                        ((ViewGroup) rootView).removeView(this);
                    }
                    //返回监听
                    if (this.onDismissListener != null) {
                        onDismissListener.onDismiss();
                    }
                    return true;
                }
                break;
        }
        return true;
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    /********************builder模式设置属性******************************/

    /**
     * 绘制前景画布颜色
     *
     * @param bgColor
     */
    public GuideView setMaskColor(int bgColor) {
        try {
            this.maskColor = ContextCompat.getColor(getContext(), bgColor);
            // 重新绘制前景画布
            mCanvas.drawColor(maskColor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 设置高亮显示类型
     *
     * @param style
     */
    public GuideView setHighLightStyle(int style) {
        this.highLightStyle = style;
        return this;
    }

    /**
     * 设置高亮画笔类型
     *
     * @param maskblurstyle
     */
    public GuideView setMaskblurstyle(int maskblurstyle) {
        this.maskblurstyle = maskblurstyle;
        return this;
    }

    /**
     * 设置需要高亮的View和提示的图片
     *
     * @param targetView
     * @param res
     */
    public GuideView addHighLightGuidView(View targetView, int res) {
        targetViews.add(targetView);
        tipBitmaps.add(BitmapFactory.decodeResource(getResources(), res));
        return this;
    }

    public GuideView setTextSize(int textSize) {
        this.textSize = textSize;
        return this;
    }

    public GuideView setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    public GuideView addHighLightGuidView(View targetView, String text) {
        StringBuilder sb = new StringBuilder();
        text = text.trim();
        sb.append(text);
        if (text.endsWith("\n")) {
            sb.deleteCharAt(text.length() - 1);
        }
        targetViews.add(targetView);
        tipStr.add(sb.toString());
        return this;
    }

    /**
     * 设置不需要高亮的View，只是提示图片
     *
     * @param res
     */
    public GuideView addNoHighLightGuidView(int res) {
        tipBitmaps.add(BitmapFactory.decodeResource(getResources(), res));
        return this;
    }

    /**
     * 设置外部是否关闭，默认关闭
     *
     * @param cancel
     */
    public GuideView setTouchOutsideDismiss(boolean cancel) {
        this.touchOutsideCancel = cancel;
        return this;
    }

    /**
     * 设置额外的边框宽度
     *
     * @param borderWidth
     */
    public GuideView setBorderWidth(int borderWidth) {
        this.borderWitdh = borderWidth;
        return this;
    }

    /**
     * 设置状态栏高度 默认是减去了一个状态栏高度 如果主题设置android:windowTranslucentStatus=true
     * 需要设置状态栏高度为0
     *
     * @param highLisghtPadding
     */
    public GuideView setHighLisghtPadding(int highLisghtPadding) {
        this.highLisghtPadding = highLisghtPadding;
        return this;
    }

    /**
     * 设置关闭监听
     *
     * @param listener
     */
    public GuideView setOnDismissListener(OnDismissListener listener) {
        this.onDismissListener = listener;
        return this;
    }

    /**
     * 清空画布
     */
    public GuideView clearBg() {
        if (mCanvas != null) {
            Paint paint = new Paint();
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            mCanvas.drawPaint(paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        }
        // 将其注入画布
        mCanvas = new Canvas(fgBitmap);
        // 绘制前景画布
        mCanvas.drawColor(maskColor);
        return this;
    }

    /**
     * 显示
     */
    public void show() {
        if (rootView != null) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ((ViewGroup) rootView).addView(this, ((ViewGroup) rootView).getChildCount(), lp);
        }
    }

}