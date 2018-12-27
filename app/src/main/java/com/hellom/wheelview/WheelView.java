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
import android.widget.Scroller;


import java.util.ArrayList;
import java.util.List;

/**
 * @author mx
 */
public class WheelView extends View {
    /**
     * 各级联区域滚动Y值
     */
    private float scrollY1 = 0;
    private float scrollY2 = 0;
    private float scrollY3 = 0;
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
     * item宽度
     */
    private int itemWidth;
    /**
     * 各级联item的X位置
     */
    private int itemX1;
    /**
     * 各级联数据区域可滚动的最小Y值,用于判断上拉超出
     */
    private int minScrollY1;
    /**
     * 可滚动最大Y值，用于判断下拉超出
     */
    private int maxScrollY;
    /**
     * 各级联区域数据真实高度
     */
    private int realHeight1;
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
    private float lastY, downY;
    /**
     * 按下时的时间
     */
    private long downTime;
    /**
     * 各级联区域滚动器
     */
    private OverScroller mScroller1;
    /**
     * 用于标识一些基本参数是否已经初始化
     */
    public boolean isStart = true;
    /**
     * 单位sp大小
     */
    private float scaleDensity;
    /**
     * 数据级联层数,默认一级
     */
    public static final int LEVEL_ONE = 1;
    public static final int LEVEL_TWO = 2;
    public static final int LEVEL_THREE = 3;
    private int level = LEVEL_ONE;
    /**
     * 数据
     */
    private List<String> levelOneData;
    /**
     * 各级联数据数量
     */
    private int levelDataSize1 = 0;
    /**
     * 当前触摸级联区域
     */
    private int currentTouchLevel = LEVEL_ONE;
    /**
     * 速度检测器
     */
    private VelocityTracker mVelocityTracker;

    /**
     * 滚动速率单位，用于fling动作时获取速率(单位时间滚动的像素)
     */
    private final int VELOCITY_UNITS = 300;

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
        mScroller1 = new OverScroller(getContext());
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

    /**
     * 设置一级数据
     *
     * @param levelOneData 一级数据
     */
    public void setLevelOneData(List<String> levelOneData) {
        if (level < LEVEL_ONE) {
            return;
        }
        this.levelOneData = levelOneData;
        levelDataSize1 = levelOneData == null ? 0 : levelOneData.size();
        notifyDataSetChanged();
    }

    /**
     * 设置级联层数
     *
     * @param level 级联层数
     */
    public void setLevel(int level) {
        if (level < LEVEL_ONE || level > LEVEL_THREE) {
            throw new IllegalArgumentException("only support 1~3 level,please use static constant:LEVEL_ONE,LEVEL_TWO,LEVEL_THREE");
        }
        this.level = level;
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

            itemWidth = width / level;
            itemX1 = itemWidth / 2;

            int temp2 = (showSize + 1) / 2 * itemHeight;
            minScrollY1 = temp2 - getRealHeight1();
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
        switch (currentTouchLevel) {
            case LEVEL_ONE:
                if (mScroller1.computeScrollOffset()) {
                    scrollY1 = mScroller1.getCurrY();
                } else {
                    if (scrollY1 >= minScrollY1 && scrollY1 <= maxScrollY) {
                        correctScrollY();
                    }
                }
                break;
            default:
                break;
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
        if (levelDataSize1 > 0) {
            int startItemPos = (int) -scrollY1 / itemHeight;
            for (int i = startItemPos, j = 0; i < startItemPos + showSize + 2; j++, i++) {
                float topY = j * itemHeight + scrollY1 % itemHeight;
                if (i >= 0 && i < levelDataSize1) {
                    canvas.drawText(levelOneData.get(i), itemX1, getBaseLine(paint, topY, itemHeight), paint);
                } else {
                    if (isCircle) {
                        int pos = i % levelDataSize1;
                        canvas.drawText(levelOneData.get(pos < 0 ? pos + levelDataSize1 : pos), itemX1, getBaseLine(paint, topY, itemHeight), paint);
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
                downTime = System.currentTimeMillis();
                downY = event.getRawY();
                lastY = downY;
                performClick();
                break;
            case MotionEvent.ACTION_MOVE:
                float y = event.getRawY();
                float dy = y - lastY;
                pretendScrollY(dy);
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:
                checkStateAndPosition();
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }

    /*private void whereIsTouch() {
        //记录当前点击区域，若前一次滚动未结束，手动结束
        if (downX < itemWidth) {
            if (!mScroller1.isFinished()) {
                mScroller1.abortAnimation();
                scrollY1 = mScroller1.getCurrY();
            }
            currentTouchLevel = LEVEL_ONE;
        } else if (downX < itemWidth * 2 && downX > itemWidth) {
            if (!mScroller2.isFinished()) {
                mScroller2.abortAnimation();
                scrollY2 = mScroller2.getCurrY();
            }
            currentTouchLevel = LEVEL_TWO;
        } else {
            if (!mScroller3.isFinished()) {
                mScroller3.abortAnimation();
                scrollY3 = mScroller3.getCurrY();
            }
            currentTouchLevel = LEVEL_THREE;
        }
    }*/

    private int getRealHeight1() {
        if (realHeight1 == 0) {
            realHeight1 = levelDataSize1 * itemHeight;
        }
        return realHeight1;
    }

    private void checkStateAndPosition() {
        //获取对应区域的变量
        int minScrollY;
        float scrollY;
        OverScroller mScroller;
        switch (currentTouchLevel) {
            case LEVEL_ONE:
                minScrollY = minScrollY1;
                scrollY = scrollY1;
                mScroller = mScroller1;
                break;
            default:
                minScrollY = minScrollY1;
                scrollY = scrollY1;
                mScroller = mScroller1;
                break;
        }
        if (!isCircle && scrollY < minScrollY) {
            //上拉超出
            mScroller.startScroll(0, (int) scrollY, 0, minScrollY - (int) scrollY, 400);
        } else if (!isCircle && scrollY > maxScrollY) {
            //下拉超出
            mScroller.startScroll(0, (int) scrollY, 0, maxScrollY - (int) scrollY, 400);
        } else {
            long endTime = System.currentTimeMillis();

            long ds = endTime - downTime;
            mVelocityTracker.computeCurrentVelocity(300);
            int speed = (int) mVelocityTracker.getYVelocity();

            //正常情况(滑动距离，和手指滑动距离成正比，和滑动时间成反比)
            //int finalY = (int) ((scrollY + dy * speed));
            if (!isCircle) {

                /*int dy = (int) (scrollY + speed) % itemHeight;
                if (dy != 0) {
                    speed += correctScrollY(dy);
                }*/
                mScroller.fling(0, (int) scrollY, 0, speed, 0, 0, minScrollY1, maxScrollY);

              /*  if (finalY < minScrollY) {
                    finalY = minScrollY;
                } else if (finalY > maxScrollY) {
                    finalY = maxScrollY;
                }
                 mScroller.startScroll(0, (int) scrollY, 0, (int) (finalY - scrollY), 400);*/
/*
                //超出startScroll方法250ms滑动持续时间且滑动距离大于item高度的一半
                if (Math.abs(dy) > itemHeight / 2) {
                    if (scrollY < 0) {
                        //向上滑，dy为负，(-itemHeight-dy)为负，向下弹回
                        mScroller.startScroll(0, (int) scrollY, 0, -itemHeight - dy);
                    } else {
                        //向下滑，dy为正，(itemHeight-dy)为正，向上弹回
                        mScroller.startScroll(0, (int) scrollY, 0, itemHeight - dy);
                    }
                } else if (Math.abs(dy) <= itemHeight / 2) {
                    //不足滑动距离
                    mScroller.startScroll(0, (int) scrollY, 0, -dy);
                }*/
            }
        }
    }

    private void correctScrollY() {
        int dy = (int) (scrollY1 % itemHeight);
        if (Math.abs(dy) > itemHeight / 2) {
            if (scrollY1 < 0) {
                mScroller1.startScroll(0, (int) scrollY1, 0, -itemHeight - dy);
            } else {
                mScroller1.startScroll(0, (int) scrollY1, 0, itemHeight - dy);
            }
        } else {
            mScroller1.startScroll(0, (int) scrollY1, 0, -dy);
        }
    }

    private void pretendScrollY(float dy) {
        switch (currentTouchLevel) {
            case LEVEL_ONE:
                scrollY1 += dy;
                break;
            case LEVEL_TWO:
                scrollY2 += dy;
                break;
            case LEVEL_THREE:
                scrollY3 += dy;
                break;
            default:
                break;
        }
        invalidate();
    }

    private float getBaseLine(Paint paint, float top, float height) {
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        return (2 * top + height - fontMetrics.bottom - fontMetrics.top) / 2;
    }

    private float getBaseLine(int position) {
        return getBaseLine(paint, itemHeight * position, itemHeight);
    }
}
