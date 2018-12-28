package com.hellom.wheelview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.OverScroller;

import java.util.List;

/**
 * @author mx
 */
public class WheelView extends View {
    /**
     * 滚动Y值
     */
    private float scrollY = 0;
    /**
     * 显示的item个数，默认为5
     */
    private int showSize = 5;
    /**
     * 文字大小，默认14sp
     */
    private float textSize = 14;
    /**
     * 是否可以循环滚动
     */
    private boolean isCircle = false;
    /**
     * 文字颜色
     */
    private int textColor = 0xFF000000;
    /**
     * 线条的颜色
     */
    private int lineColor = 0xFF888888;

    /**
     * view总宽度
     */
    private int width;
    /**
     * view总高度
     */
    private int height;
    /**
     * item行高度
     */
    private int itemHeight;
    /**
     * item的X位置
     */
    private int itemX;
    /**
     * 可滚动的最小Y值,用于判断上拉超出
     */
    private int minScrollY;
    /**
     * 可滚动最大Y值，用于判断下拉超出
     */
    private int maxScrollY;
    /**
     * 数据真实高度
     */
    private int realHeight;
    /**
     * 中心item上下边距
     */
    private float centerItemTop;
    private float centerItemBottom;
    /**
     * 内容绘制paint
     */
    private Paint paint;
    /**
     * 遮罩paint
     */
    private Paint coverPaint;
    /**
     * 遮罩shader
     */
    private Shader shader;
    /**
     * 上次操作的坐标以及按下时候的坐标
     */
    private float lastY;
    /**
     * 滚动器
     */
    private OverScroller mScroller;
    /**
     * 用于标识一些基本参数是否已经初始化
     */
    public boolean isStart = true;
    /**
     * 单位sp大小
     */
    private float scaleDensity;
    /**
     * 数据
     */
    private List<String> data;
    /**
     * 各级联数据数量
     */
    private int dataSize = 0;
    /**
     * 速度检测器
     */
    private VelocityTracker mVelocityTracker;
    /**
     * 滚动速率单位，用于fling动作时获取速率(单位时间滚动的像素)
     */
    private static final int VELOCITY_UNITS = 300;
    /**
     * 滚动速率最大最小值
     */
    private static final int MIN_VELOCITY = 1000;
    private static final int MAX_VELOCITY = 6000;
    /**
     * 手势模式
     */
    private int mode = MODE_NONE;
    private static final int MODE_NONE = 0;
    private static final int MODE_DRAG = 1;
    private static final int MODE_FLING = 2;

    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        scaleDensity = getResources().getDisplayMetrics().scaledDensity;
        mScroller = new OverScroller(getContext());
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(scaleDensity * textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        coverPaint = new Paint();
        int evenNUmber2 = 2;
        if (showSize % evenNUmber2 == 0) {
            showSize += 1;
        }
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        invalidate();
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
        invalidate();
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        paint.setTextSize(scaleDensity * textSize);
        invalidate();
    }

    public void setData(List<String> data) {
        this.data = data;
        dataSize = data == null ? 0 : data.size();
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        isStart = true;
        invalidate();
    }

    private void measureData() {
        if (isStart) {
            width = getWidth();
            height = getHeight();

            int temp1 = height - getPaddingTop() - getPaddingBottom();
            itemHeight = temp1 / showSize;
            centerItemTop = temp1 / 2 + getPaddingTop() - itemHeight / 2;
            centerItemBottom = temp1 / 2 + getPaddingTop() + itemHeight / 2;

            itemX = width / 2;

            realHeight = dataSize * itemHeight;
            int temp2 = (showSize + 1) / 2 * itemHeight;
            minScrollY = temp2 - realHeight;
            maxScrollY = (showSize - 1) / 2 * itemHeight;

            int[] colors = new int[]{0xFFFFFFFF, 0xAAFFFFFF, 0x00FFFFFF, 0x00FFFFFF, 0xAAFFFFFF, 0xFFFFFFFF};
            float[] positions = new float[]{0.0f, centerItemTop / height, centerItemTop / height, centerItemBottom / height, centerItemBottom / height, 1.0f};
            shader = new LinearGradient(0, 0, 0, height, colors, positions, Shader.TileMode.REPEAT);
            coverPaint.setShader(shader);

            isStart = false;
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollY = mScroller.getCurrY();
        } else {
            if (scrollY >= minScrollY && scrollY <= maxScrollY && mode == MODE_FLING) {
                correctScrollY();
            }
        }
        invalidate();
        super.computeScroll();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        measureData();
        //设置绘制数据画笔
        paint.setColor(textColor);
        //绘制一级数据
        if (dataSize > 0) {
            int startItemPos = (int) -scrollY / itemHeight;
            for (int i = startItemPos, j = 0; i < startItemPos + showSize + 2; j++, i++) {
                float topY = j * itemHeight + scrollY % itemHeight;
                if (i >= 0 && i < dataSize) {
                    canvas.drawText(data.get(i), itemX, getBaseLine(paint, topY, itemHeight), paint);
                } else {
                    if (isCircle) {
                        int pos = i % dataSize;
                        canvas.drawText(data.get(pos < 0 ? pos + dataSize : pos), itemX, getBaseLine(paint, topY, itemHeight), paint);
                    }
                }
            }
        }
        //绘制中间的线条和遮罩层
        paint.setColor(lineColor);
        canvas.drawLine(getPaddingLeft(), centerItemTop, width - getPaddingRight(), centerItemTop, paint);
        canvas.drawLine(getPaddingLeft(), centerItemBottom, width - getPaddingRight(), centerItemBottom, paint);
        coverPaint.setShader(shader);
        canvas.drawRect(0, 0, width, height, coverPaint);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float y = event.getRawY();
                float dy = y - lastY;
                updateScrollY(dy);
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:
                performClick();
                checkStateAndPosition();
                break;
            default:
                break;
        }
        return true;
    }

    private void checkStateAndPosition() {
        if (!isCircle) {
            if (scrollY < minScrollY) {
                //上拉超出
                mScroller.startScroll(0, (int) scrollY, 0, minScrollY - (int) scrollY, 400);
            } else if (scrollY > maxScrollY) {
                //下拉超出
                mScroller.startScroll(0, (int) scrollY, 0, maxScrollY - (int) scrollY, 400);
            } else {
                //正常范围内，判断拖拽和抛出的手势，分开处理
                mVelocityTracker.computeCurrentVelocity(VELOCITY_UNITS, MAX_VELOCITY);
                int speed = (int) mVelocityTracker.getYVelocity();
                if (Math.abs(speed) < MIN_VELOCITY) {
                    mode = MODE_DRAG;
                    correctScrollY();
                } else {
                    mode = MODE_FLING;
                    mScroller.fling(0, (int) scrollY, 0, speed, 0, 0, minScrollY, maxScrollY);
                }
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            }
        }
    }

    private void correctScrollY() {
        int dy = (int) (scrollY % itemHeight);
        int correctGuide = itemHeight / 2;
        if (Math.abs(dy) > correctGuide) {
            //滚动超出item一半的修正
            if (scrollY < 0) {
                mScroller.startScroll(0, (int) scrollY, 0, -itemHeight - dy);
            } else {
                mScroller.startScroll(0, (int) scrollY, 0, itemHeight - dy);
            }
        } else {
            //滚动剩余部分不超出item高度一半的修正
            mScroller.startScroll(0, (int) scrollY, 0, -dy);
        }
        mode = MODE_NONE;
    }

    private void updateScrollY(float dy) {
        scrollY += dy;
    }

    private float getBaseLine(Paint paint, float top, float height) {
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        return (2 * top + height - fontMetrics.bottom - fontMetrics.top) / 2;
    }

    private float getBaseLine(int position) {
        return getBaseLine(paint, itemHeight * position, itemHeight);
    }
}
