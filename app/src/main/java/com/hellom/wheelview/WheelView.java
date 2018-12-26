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


    private int rate = 120;               //惯性滑动比率，rate越大，速率越快


    private int cacheNowItem = -1;        //预设当前item的位置，负数表示不设定

    private int currentItem = -1;         //当前item位置

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
    private int itemX2;
    private int itemX3;
    /**
     * 各级联数据区域可滚动的最小Y值,用于判断上拉超出
     */
    private int minScrollY1;
    private int minScrollY2;
    private int minScrollY3;
    /**
     * 可滚动最大Y值，用于判断下拉超出
     */
    private int maxScrollY;
    /**
     * 各级联区域数据真实高度
     */
    private int realHeight1;
    private int realHeight2;
    private int realHeight3;
    /**
     * 中心item上下边距
     */
    private float centerItemTop;
    private float centerItemBottom;

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
    /**
     * 上次操作的坐标以及按下时候的坐标
     */
    private float lastY, downY, downX;
    /**
     * 按下时的时间
     */
    private long downTime;
    /**
     * 各级联区域滚动器
     */
    private OverScroller mScroller1;
    private OverScroller mScroller2;
    private OverScroller mScroller3;
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
    private List<String> levelTwoData;
    private List<String> levelThreeData;
    /**
     * 各级联数据数量
     */
    private int levelDataSize1 = 0;
    private int levelDataSize2 = 0;
    private int levelDataSize3 = 0;
    /**
     * 当前触摸级联区域
     */
    private int currentTouchLevel = LEVEL_ONE;

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
        mScroller2 = new OverScroller(getContext());
        mScroller3 = new OverScroller(getContext());
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
    public void setLevelOneData(List<String> levelOneData) {
        if (level != LEVEL_ONE) {
            return;
        }
        this.levelOneData = levelOneData;
        levelDataSize1 = levelOneData == null ? 0 : levelOneData.size();
        notifyDataSetChanged();
    }

    /**
     * 设置二级数据
     *
     * @param levelTwoData 二级数据
     */
    public void setLevelTwoData(List<String> levelTwoData) {
        if (level != LEVEL_TWO) {
            return;
        }
        this.levelTwoData = levelTwoData;
        levelDataSize2 = levelTwoData == null ? 0 : levelTwoData.size();
        notifyDataSetChanged();
    }

    /**
     * 设置三级数据
     *
     * @param levelThreeData 三级数据
     */
    public void setLevelThreeData(List<String> levelThreeData) {
        if (level != LEVEL_THREE) {
            return;
        }
        this.levelThreeData = levelThreeData;
        levelDataSize3 = levelThreeData == null ? 0 : levelThreeData.size();
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
            height = getHeight();

            itemHeight = (height - getPaddingTop() - getPaddingBottom()) / showSize;

            itemWidth = width / level;
            itemX1 = itemWidth / 2;
            itemX2 = itemX1 + itemWidth;
            itemX3 = itemX2 + itemWidth;

            minScrollY1 = -(getRealHeight1() - (showSize + 1) / 2 * itemHeight);
            minScrollY2 = -(getRealHeight2() - (showSize + 1) / 2 * itemHeight);
            minScrollY3 = -(getRealHeight3() - (showSize + 1) / 2 * itemHeight);
            maxScrollY = (showSize - 1) / 2 * itemHeight;

            centerItemTop = (height - getPaddingTop() - getPaddingBottom()) / 2 + getPaddingTop() - itemHeight / 2;
            centerItemBottom = (height - getPaddingTop() - getPaddingBottom()) / 2 + getPaddingTop() + itemHeight / 2;

            shader = new LinearGradient(0, 0, 0, height, new int[]{0xFFFFFFFF, 0xAAFFFFFF, 0x00FFFFFF, 0x00FFFFFF, 0xAAFFFFFF, 0xFFFFFFFF}, new float[]{0.0f, centerItemTop / height, centerItemTop / height, centerItemBottom / height, centerItemBottom / height, 1.0f}, Shader.TileMode.REPEAT);
            coverPaint.setShader(shader);

            isStart = false;
        }
    }

    @Override
    public void computeScroll() {
        //scroller的滚动是否完成
        switch (currentTouchLevel) {
            case LEVEL_ONE:
                if (mScroller1.computeScrollOffset()) {
                    scrollY1 = mScroller1.getCurrY();
                }
                break;
            case LEVEL_TWO:
                if (mScroller2.computeScrollOffset()) {
                    scrollY2 = mScroller2.getCurrY();
                }
                break;
            case LEVEL_THREE:
                if (mScroller3.computeScrollOffset()) {
                    scrollY3 = mScroller3.getCurrY();
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
       /* //如果设置了当前选中
        if (cacheNowItem >= 0) {
            scrollY = -(cacheNowItem - (showSize - 1) / 2) * itemHeight;
            cacheNowItem = -1;
        }*/
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
        //绘制二级数据
        if (levelDataSize2 > 0) {
            int startItemPos = (int) -scrollY2 / itemHeight;
            for (int i = startItemPos, j = 0; i < startItemPos + showSize + 2; j++, i++) {
                float topY = j * itemHeight + scrollY2 % itemHeight;
                if (i >= 0 && i < levelDataSize2) {
                    canvas.drawText(levelTwoData.get(i), itemX2, getBaseLine(paint, topY, itemHeight), paint);
                } else {
                    if (isCircle) {
                        int pos = i % levelDataSize2;
                        canvas.drawText(levelTwoData.get(pos < 0 ? pos + levelDataSize2 : pos), itemX2, getBaseLine(paint, topY, itemHeight), paint);
                    }
                }
            }
        }
        //绘制三级数据
        if (levelDataSize3 > 0) {
            int startItemPos = (int) -scrollY3 / itemHeight;
            for (int i = startItemPos, j = 0; i < startItemPos + showSize + 2; j++, i++) {
                float topY = j * itemHeight + scrollY3 % itemHeight;
                if (i >= 0 && i < levelDataSize3) {
                    canvas.drawText(levelThreeData.get(i), itemX3, getBaseLine(paint, topY, itemHeight), paint);
                } else {
                    if (isCircle) {
                        int pos = i % levelDataSize3;
                        canvas.drawText(levelThreeData.get(pos < 0 ? pos + levelDataSize3 : pos), itemX3, getBaseLine(paint, topY, itemHeight), paint);
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
        /*if (cacheNowItem >= 0) {
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
        }*/
        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downTime = System.currentTimeMillis();
                downY = event.getRawY();
                downX = event.getX();
                lastY = downY;
                whereIsTouch();
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

    private void whereIsTouch() {
        if (downX < itemWidth) {
            currentTouchLevel = LEVEL_ONE;
        } else if (downX < itemWidth * 2 && downX > itemWidth) {
            currentTouchLevel = LEVEL_TWO;
        } else {
            currentTouchLevel = LEVEL_THREE;
        }
    }

    private int getRealHeight1() {
        if (realHeight1 == 0) {
            realHeight1 = levelDataSize1 * itemHeight;
        }
        return realHeight1;
    }

    private int getRealHeight2() {
        if (realHeight2 == 0) {
            realHeight2 = levelDataSize2 * itemHeight;
        }
        return realHeight2;
    }

    private int getRealHeight3() {
        if (realHeight3 == 0) {
            realHeight3 = levelDataSize3 * itemHeight;
        }
        return realHeight3;
    }

    private void checkStateAndPosition() {
        int minScrollY = 0;
        float scrollY = 0;
        OverScroller mScroller;
        switch (currentTouchLevel) {
            case LEVEL_ONE:
                minScrollY = minScrollY1;
                scrollY = scrollY1;
                mScroller = mScroller1;
                break;
            case LEVEL_TWO:
                minScrollY = minScrollY2;
                scrollY = scrollY2;
                mScroller = mScroller2;
                break;
            case LEVEL_THREE:
                minScrollY = minScrollY3;
                scrollY = scrollY3;
                mScroller = mScroller3;
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
            //超出滑动时间或者不足滑动距离
            //endTime - downTime > 250 ||
            if (Math.abs(lastY - downY) < itemHeight / 2) {
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

    private class HashBean {

        public HashBean(String showStr, Object backData) {
            this.showStr = showStr;
            this.backData = backData;
        }

        public String showStr;
        public Object backData;
    }

}
