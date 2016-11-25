package com.apkcore.dropsofwater.weight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class WaveView extends View {
    /**
     * 绘制一条曲线，然后通过wavehelper动画来控制刷新
     * +------------------------+
     * |<--wave length->        |______
     * |   /\          |   /\   |  |
     * |  /  \         |  /  \  | amplitude
     * | /    \        | /    \ |  |
     * |/      \       |/      \|__|____
     * |        \      /        |  |
     * |         \    /         |  |
     * |          \  /          |  |
     * |           \/           | water level
     * |                        |  |
     * |                        |  |
     * +------------------------+__|____
     */
    private static final float DEFAULT_AMPLITUDE_RATIO = 0.05f;
    private static final float DEFAULT_WATER_LEVEL_RATIO = 0.5f;
    private static final float DEFAULT_WAVE_LENGTH_RATIO = 1.0f;
    private static final float DEFAULT_WAVE_SHIFT_RATIO = 0.0f;

    public static final int DEFAULT_BEHIND_WAVE_COLOR = Color.parseColor("#28FFFFFF");
    public static final int DEFAULT_FRONT_WAVE_COLOR = Color.parseColor("#3CFFFFFF");
    private Paint textPaint;

    private boolean mShowWave;

    private BitmapShader mWaveShader;
    private Matrix mShaderMatrix;
    private Paint mViewPaint;
    private Paint mBorderPaint;

    private float mDefaultAmplitude;
    private float mDefaultWaterLevel;
    private float mDefaultWaveLength;
    private double mDefaultAngularFrequency;

    private float mAmplitudeRatio = DEFAULT_AMPLITUDE_RATIO;
    private float mWaveLengthRatio = DEFAULT_WAVE_LENGTH_RATIO;
    private float mWaterLevelRatio = DEFAULT_WATER_LEVEL_RATIO;
    private float mWaveShiftRatio = DEFAULT_WAVE_SHIFT_RATIO;

//    private int mBehindWaveColor = DEFAULT_BEHIND_WAVE_COLOR;
    private int mFrontWaveColor = DEFAULT_FRONT_WAVE_COLOR;
    private Path dropPath;
    private float dropCircle;
    private float dropRatio;
    private float flyCircle;
    private float positionFlyXspeed;
    private float positionFlyYspeed;
    private Paint wavePaint;

    public WaveView(Context context) {
        super(context);
        init();
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (mBorderPaint == null) {
            mBorderPaint = new Paint();
            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setStyle(Style.STROKE);
        }
        mBorderPaint.setColor(Color.parseColor("#ADADAD"));
        mBorderPaint.setStrokeWidth(dpToPx(3));

        //画波浪水面
        wavePaint = new Paint();
        mShaderMatrix = new Matrix();
        mViewPaint = new Paint();
        mViewPaint.setAntiAlias(true);

        //画水滴与气泡
        dropPath = new Path();
        dropCircle = dpToPx(4);
        flyCircle = dpToPx(3);
        positionFlyXspeed = dpToPx(6);
        positionFlyYspeed = dpToPx(15);
        //画文字
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(dpToPx(15));
    }

    private int dpToPx(float dp) {
        return (int) (dp * getContext().getResources().getDisplayMetrics().density);
    }

    public void setDropRatio(float dropRatio) {
        if (this.dropRatio != dropRatio) {
            this.dropRatio = dropRatio;
//            invalidate();
        }
    }

    public float getWaveShiftRatio() {
        return mWaveShiftRatio;
    }

    public void setWaveShiftRatio(float waveShiftRatio) {
        if (mWaveShiftRatio != waveShiftRatio) {
            mWaveShiftRatio = waveShiftRatio;
            invalidate();
        }
    }

    public float getWaterLevelRatio() {
        return mWaterLevelRatio;
    }

    public void setWaterLevelRatio(float waterLevelRatio) {
        if (mWaterLevelRatio != waterLevelRatio) {
            mWaterLevelRatio = waterLevelRatio;
            invalidate();
        }
    }

    public float getAmplitudeRatio() {
        return mAmplitudeRatio;
    }

    public void setAmplitudeRatio(float amplitudeRatio) {
        if (mAmplitudeRatio != amplitudeRatio) {
            mAmplitudeRatio = amplitudeRatio;
            invalidate();
        }
    }

    public float getWaveLengthRatio() {
        return mWaveLengthRatio;
    }

    public void setWaveLengthRatio(float waveLengthRatio) {
        mWaveLengthRatio = waveLengthRatio;
    }

    public boolean isShowWave() {
        return mShowWave;
    }

    public void setShowWave(boolean showWave) {
        mShowWave = showWave;
    }

    public void setBorder(int width, int color) {
        if (mBorderPaint == null) {
            mBorderPaint = new Paint();
            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setStyle(Style.STROKE);
        }
        mBorderPaint.setColor(color);
        mBorderPaint.setStrokeWidth(width);

        invalidate();
    }

    public void setWaveColor(int frontWaveColor) {
        mFrontWaveColor = frontWaveColor;
        // need to recreate shader when color changed
        if (getHeight() > 0 && getWidth() > 0) {
            mWaveShader = null;
            createShader();
            invalidate();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        createShader();
    }

    /**
     * 创建具有重复水平的默认波的阴影
     */
    private void createShader() {
        mDefaultAngularFrequency = 2.0f * Math.PI / DEFAULT_WAVE_LENGTH_RATIO / getWidth();
        mDefaultAmplitude = getHeight() * DEFAULT_AMPLITUDE_RATIO;
        mDefaultWaterLevel = getHeight() * DEFAULT_WATER_LEVEL_RATIO;
        mDefaultWaveLength = getWidth();

        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        wavePaint.setStrokeWidth(2);
        wavePaint.setAntiAlias(true);

        // Draw default waves into the bitmap
        // y=Asin(ωx+φ)+h
        final int endX = getWidth() + 1;
        final int endY = getHeight() + 1;

        float[] waveY = new float[endX];

        wavePaint.setColor(mFrontWaveColor);
        wavePaint.setAlpha(40);
        for (int beginX = 0; beginX < endX; beginX++) {
            double wx = beginX * mDefaultAngularFrequency;
            float beginY = (float) (mDefaultWaterLevel + mDefaultAmplitude * Math.sin(wx));
            canvas.drawLine(beginX, beginY, beginX, endY, wavePaint);

            waveY[beginX] = beginY;
        }

        wavePaint.setAlpha(100);
        final int wave2Shift = (int) (mDefaultWaveLength / 4);
        for (int beginX = 0; beginX < endX; beginX++) {
            canvas.drawLine(beginX, waveY[(beginX + wave2Shift) % endX], beginX, endY, wavePaint);
        }

        mWaveShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        mViewPaint.setShader(mWaveShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mShowWave && mWaveShader != null) {
            if (mViewPaint.getShader() == null) {
                mViewPaint.setShader(mWaveShader);
            }

            mShaderMatrix.setScale(
                    mWaveLengthRatio / DEFAULT_WAVE_LENGTH_RATIO,
                    mAmplitudeRatio / DEFAULT_AMPLITUDE_RATIO,
                    0,
                    mDefaultWaterLevel);
            mShaderMatrix.postTranslate(
                    mWaveShiftRatio * getWidth(),
                    (DEFAULT_WATER_LEVEL_RATIO - mWaterLevelRatio) * getHeight());

            mWaveShader.setLocalMatrix(mShaderMatrix);

            float borderWidth = mBorderPaint == null ? 0f : mBorderPaint.getStrokeWidth();

            //画圆
            if (borderWidth > 0) {
                canvas.drawCircle(getWidth() / 2f, getHeight() / 2f,
                        (getWidth() - borderWidth) / 2f - 1f, mBorderPaint);
            }
            float radius = getWidth() / 2f - borderWidth;
            canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius, mViewPaint);

            //画水滴
            float positionDrop = dropRatio * getHeight() * (1 - mWaterLevelRatio);
            if (positionDrop < getHeight() - borderWidth + dropRatio) {
                dropPath.reset();
                dropPath.moveTo(getWidth() / 2f, positionDrop);
                dropPath.lineTo(getWidth() / 2f - dropCircle * 0.9f, positionDrop + dropCircle);
                dropPath.lineTo(getWidth() / 2f + dropCircle * 0.9f, positionDrop + dropCircle);
                dropPath.lineTo(getWidth() / 2f, positionDrop);
                dropPath.addCircle(getWidth() / 2f, positionDrop + 1.5f * dropCircle, dropCircle, Path.Direction.CCW);
                dropPath.close();
                canvas.drawPath(dropPath, wavePaint);
            }
            //画气泡
            float positionFly = (1 - dropRatio) * getHeight() * (1 - mWaterLevelRatio);
            if (positionFly > borderWidth + flyCircle) {
                canvas.drawCircle(getWidth() / 2f, positionFly, flyCircle, wavePaint);
            }
            if ((positionFly - positionFlyYspeed) > borderWidth + flyCircle) {
                canvas.drawCircle(getWidth() / 2f - positionFlyXspeed, positionFly - positionFlyYspeed, flyCircle, wavePaint);
            }
            if ((positionFly - 2 * positionFlyYspeed) > borderWidth + flyCircle) {
                canvas.drawCircle(getWidth() / 2f, positionFly - 2 * positionFlyYspeed, flyCircle, wavePaint);
            }
            if ((positionFly - 3 * positionFlyYspeed) > borderWidth + flyCircle) {
                canvas.drawCircle(getWidth() / 2f + positionFlyXspeed, positionFly - 3 * positionFlyYspeed, flyCircle, wavePaint);
            }

            //画文字
            textPaint.setColor(Color.BLACK);
            String flowNum = (int) Math.floor(mWaterLevelRatio * 100) + "%";
            float num = textPaint.measureText(flowNum);
            canvas.drawText(flowNum, getWidth() / 2f - num / 2, getHeight() / 2f + dpToPx(15 / 2.5f), textPaint);
        } else {
            mViewPaint.setShader(null);
        }
    }
}
