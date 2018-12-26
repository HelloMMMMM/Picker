package com.hellom.wheelview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;


import java.util.ArrayList;
import java.util.List;

public class WheelView extends View {

    private float scrollY = 0;
    private int scrollX = 0;

    private int showSize = 5;             //展示Item的个数
    /**
     * 文字大小，默认14sp
     */
    private float textSize = 14;
    private boolean isCircle = false;     //是否为环形
    private int rate = 120;               //惯性滑动比率，rate越大，速率越快
    private int textColor = 0xFF000000;   //文字颜色
    private int lineColor = 0xFF888888;   //线条的颜色

    private int cacheNowItem = -1;        //预设当前item的位置，负数表示不设定

    private int currentItem = -1;         //当前item位置


    private int width;                  //view的宽度
    private int height;                 //view的高度
    private int itemHeight;             //item的高度
    private int itemX;                  //item的X位置
    private float centerItemTop;        //中心Item的上边距位置
    private float centerItemBottom;     //中心Item的下边距位置
    private float centerItemHeight;     //中心Item的高度

    private int realHeight;             //内容的实际高度
    private int minScrollY;             //最小滚动高度
    private int maxScrollY;             //最大滚动高度

    private ArrayList<HashBean> data;   //数据集合
    private int dataSize = 0;
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

    private float lastY, downY;          //上次操作的坐标以及按下时候的坐标
    private long downTime;              //按下时的时间

    private OverScroller mScroller;

    public boolean isStart = true;

    /**
     * 单位sp大小
     */
    private float scaleDensity;

    /**
     * 数据级联层数
     */
    public static final int LEVEL_ONE = 1;
    public static final int LEVEL_TWO = 2;
    public static final int LEVEL_THREE = 3;
    private int level;

    /**
     * 数据
     */
    private List<?> levelOneData;
    private List<?> levelTwoData;
    private List<?> levelThreeData;

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
        data = new ArrayList<>();
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
    public void setLevelOneData(List<?> levelOneData) {
        this.levelOneData = levelOneData;
    }

    /**
     * 设置二级数据
     *
     * @param levelTwoData 二级数据
     */
    public void setLevelTwoData(List<?> levelTwoData) {
        this.levelTwoData = levelTwoData;
    }

    /**
     * 设置三级数据
     *
     * @param levelThreeData 三级数据
     */
    public void setLevelThreeData(List<?> levelThreeData) {
        this.levelThreeData = levelThreeData;
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

    /**
     * 增加数据
     *
     * @param show     显示数据
     * @param backData 选中时的返回数据
     */
    public void addData(String show, Object backData) {
        data.add(new HashBean(show, backData));
        dataSize++;
    }

    /**
     * 增加数据
     *
     * @param data 显示数据和选中时的返回数据
     */
    public void addData(String data) {
        addData(data, data);
    }

    public void clearData() {
        data.clear();
    }

    public void setCircle(boolean isCircle) {
        this.isCircle = isCircle;
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

    public void setRate(int rate) {
        this.rate = rate;
    }

    public void notifyDataSetChanged() {
        isStart = true;
        invalidate();
    }

    private void measureData() {
        if (isStart) {
            width = getWidth();
            itemX = width / 2;
            height = getHeight();
            itemHeight = (height - getPaddingTop() - getPaddingBottom()) / showSize;
            realHeight = dataSize * itemHeight;
            minScrollY = -(getRealHeight() - (showSize + 1) / 2 * itemHeight);
            maxScrollY = (showSize - 1) / 2 * itemHeight;
            centerItemHeight = itemHeight;
            centerItemTop = (height - getPaddingTop() - getPaddingBottom()) / 2 + getPaddingTop() - centerItemHeight / 2;
            centerItemBottom = (height - getPaddingTop() - getPaddingBottom()) / 2 + getPaddingTop() + centerItemHeight / 2;
            shader = new LinearGradient(0, 0, 0, height, new int[]{0xFFFFFFFF, 0xAAFFFFFF, 0x00FFFFFF, 0x00FFFFFF, 0xAAFFFFFF, 0xFFFFFFFF}, new float[]{0.0f, centerItemTop / height, centerItemTop / height, centerItemBottom / height, centerItemBottom / height, 1.0f}, Shader.TileMode.REPEAT);
            coverPaint.setShader(shader);
            isStart = false;
        }
    }

    @Override
    public void computeScroll() {
        //scroller的滚动是否完成
        if (mScroller.computeScrollOffset()) {
            scrollY = mScroller.getCurrY();
            invalidate();
        }
        super.computeScroll();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        measureData();
        //如果设置了当前选中
        if (cacheNowItem >= 0) {
            scrollY = -(cacheNowItem - (showSize - 1) / 2) * itemHeight;
            cacheNowItem = -1;
        }
        int startItemPos = (int) -scrollY / itemHeight;      //绘制的数据的起始位置
        paint.setColor(textColor);
        for (int i = startItemPos, j = 0; i < startItemPos + showSize + 2; j++, i++) {
            float topY = j * itemHeight + scrollY % itemHeight;
            if (i >= 0 && i < dataSize) {
                canvas.drawText(data.get(i).showStr, itemX, getBaseLine(paint, topY, itemHeight), paint);
            } else {
                if (isCircle) {
                    int pos = i % dataSize;
                    canvas.drawText(data.get(pos < 0 ? pos + dataSize : pos).showStr, itemX, getBaseLine(paint, topY, itemHeight), paint);
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

    /**
     * 获取数据集合的大小
     *
     * @param isRefresh 是否重新计算数据集合大小
     * @return
     */
    public int getDataSize(boolean isRefresh) {
        if (isRefresh) {
            dataSize = data.size();
        }
        return data.size();
    }

    /**
     * 设置当前Item的位置
     *
     * @param position
     */
    public void setCenterItem(int position) {
        if (position >= 0 && position < dataSize) {
            cacheNowItem = position;
        }
        invalidate();
    }

    /**
     * 设置选中内容
     *
     * @param showData
     */
    public void setCenterItem(String showData) {
        int size = data.size();
        for (int i = 0; i < size; i++) {
            if (showData.equals(data.get(i).showStr)) {
                cacheNowItem = i;
                invalidate();
                return;
            }
        }
    }

    /**
     * 获取选中内容的数据
     *
     * @return
     */
    public Object getCenterItem() {
        if (cacheNowItem >= 0) {
            return data.get(cacheNowItem).backData;
        } else {
            int dy = (int) scrollY % itemHeight;         //不足一个Item高度的部分
            if (Math.abs(dy) > itemHeight / 2) {          //如果偏移大于item的一半，
                if (scrollY < 0) {
                    scrollY = scrollY - itemHeight - dy;
                } else {
                    scrollY = scrollY + itemHeight - dy;
                }
            } else {
                scrollY = scrollY - dy;
            }
            mScroller.forceFinished(true);
            invalidate();
            int nowChecked;
            if (!isCircle) {
                if (scrollY < minScrollY) {
                    nowChecked = dataSize - 1;
                } else if (scrollY > maxScrollY) {
                    nowChecked = 0;
                } else {
                    nowChecked = (int) (-scrollY / itemHeight + (showSize - 1) / 2);
                }
            } else {
                //滚轮时，重置scrollY位置，使它出现在限定范围的等效位置
                //以minScroll为相对0点，进行调整
                if (scrollY < minScrollY || scrollY >= maxScrollY) {
                    int mid = (int) ((scrollY - minScrollY) % realHeight);
                    if (mid < 0) {
                        mid += realHeight;
                    }
                    scrollY = mid + minScrollY;
                }
                nowChecked = (int) (-scrollY / itemHeight + (showSize - 1) / 2);
            }
            return dataSize > 0 ? data.get(nowChecked).backData : null;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downTime = System.currentTimeMillis();
                downY = event.getRawY();
                lastY = downY;
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
        }
        return true;
    }

    private int getRealHeight() {
        if (realHeight == 0) {
            realHeight = dataSize * itemHeight;
        }
        return realHeight;
    }

    private void checkStateAndPosition() {
        //上拉超出
        if (!isCircle && scrollY < -(getRealHeight() - (showSize + 1) / 2 * itemHeight)) {
            mScroller.startScroll(0, (int) scrollY, 0, (showSize + 1) / 2 * itemHeight - getRealHeight() - (int) scrollY, 400);
//            mScroller.springBack(0,(int)scrollY,0,0,minScrollY,maxScrollY);
        } else if (!isCircle && scrollY > (showSize - 1) / 2 * itemHeight) {  //下拉超出
            mScroller.startScroll(0, (int) scrollY, 0, (showSize - 1) / 2 * itemHeight - (int) scrollY, 400);
//            mScroller.springBack(0,(int)scrollY,0,0,minScrollY,maxScrollY);
        } else {
            long endTime = System.currentTimeMillis();
            //超出滑动时间或者不足滑动距离
            if (endTime - downTime > 250 || Math.abs(lastY - downY) < itemHeight / 2) {
                int dy = (int) scrollY % itemHeight;         //不足一个Item高度的部分
                if (Math.abs(dy) > itemHeight / 2) {          //如果偏移大于item的一半，
                    if (scrollY < 0) {
                        mScroller.startScroll(0, (int) scrollY, 0, -itemHeight - dy);
                    } else {
                        mScroller.startScroll(0, (int) scrollY, 0, itemHeight - dy);
                    }
                } else {
                    mScroller.startScroll(0, (int) scrollY, 0, -dy);
                }
            } else {
                //滑动距离，和手指滑动距离成正比，和滑动时间成反比
                int finalY = (int) ((scrollY + rate * (lastY - downY) / (endTime - downTime))) / itemHeight * itemHeight;
                if (!isCircle) {
                    if (finalY < minScrollY) {
                        finalY = minScrollY;
                    } else if (finalY > maxScrollY) {
                        finalY = maxScrollY;
                    }
                }
                mScroller.startScroll(0, (int) scrollY, 0, (int) (finalY - scrollY), 400);
            }
        }
    }

    private void pretendScrollY(float dy) {
        scrollY += dy;
        invalidate();
    }

    private float getBaseLine(Paint paint, float top, float height) {
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        return (2 * top + height - fontMetrics.bottom - fontMetrics.top) / 2;
    }

    private float getBaseLine(int position) {
        return getBaseLine(paint, itemHeight * position, itemHeight);
    }

    private class HashBean {

        public HashBean(String showStr, Object backData) {
            this.showStr = showStr;
            this.backData = backData;
        }

        public String showStr;
        public Object backData;
    }

}
