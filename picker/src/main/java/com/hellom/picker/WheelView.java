package com.hellom.picker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
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
     * 文字大小，默认16sp
     */
    private float textSize = 16;
    /**
     * 是否可以循环滚动
     */
    private boolean isCircle = false;
    /**
     * 文字颜色
     */
    private int textColor = 0xFF333333;
    /**
     * 线条的颜色
     */
    private int lineColor = 0xFFd1d1d1;

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
     * item的X位置(文字)
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
     * 滚动器
     */
    private OverScroller mScroller;
    /**
     * 用于标识一些基本参数是否已经初始化
     */
    private boolean isStart = true;
    /**
     * 单位sp大小
     */
    private float scaleDensity;
    /**
     * 单位dp大小
     */
    private float density;
    /**
     * 数据
     */
    private List<String> data;
    /**
     * 数据数量
     */
    private int dataSize = 0;
    /**
     * 速率比率,默认0.3
     */
    private float velocityRate = 0.3f;
    private static final float MAX_VELOCITY_RATE = 1.0f;
    private static final float MIN_VELOCITY_RATE = 0.1f;
    /**
     * 手势模式
     */
    private int mode = MODE_NONE;
    private static final int MODE_NONE = 0;
    private static final int MODE_DRAG = 1;
    private static final int MODE_FLING = 2;
    /**
     * 手势检测器
     */
    private GestureDetector mGestureDetector;
    /**
     * 上一次选中的和此次选中item的position
     */
    private int lastSelectedItemPosition = -1;
    private int selectedItemPosition = -1;
    /**
     * 是否需要更新选中item位置
     */
    private boolean isNeedUpdateSelectedItemPosition = false;
    /**
     * 选中item变化监听
     */
    private OnSelectedChangedListener mOnSelectedChangedListener;

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
        density = getResources().getDisplayMetrics().density;
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
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                //滚动未结束再次触碰，手动结束上次触碰
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                //按下时触发,false不传递无法触发scroll和fling
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                //单击动作
                performClick();
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                //滑动模式(多次触发),返回值不影响手势检测器onTouchEvent的返回值
                mode = MODE_DRAG;
                scrollY += -distanceY;
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //抛模式(手指抬起时且速度大于一定值触发),返回false时手势检测器onTouchEvent返回false
                mode = MODE_FLING;
                mScroller.fling(0, (int) scrollY, 0, (int) (velocityRate * velocityY), 0, 0, minScrollY, maxScrollY);
                return false;
            }
        });
    }

    public String getSelectedItemData() {
        return selectedItemPosition == -1 ? "" : data.get(selectedItemPosition);
    }

    public void setOnSelectedChangedListener(OnSelectedChangedListener mOnSelectedChangedListener) {
        this.mOnSelectedChangedListener = mOnSelectedChangedListener;
    }

    /**
     * 设置抛的速率比例，越大越快
     *
     * @param velocityRate 速率比例
     */
    public void setVelocityRate(float velocityRate) {
        if (velocityRate > MAX_VELOCITY_RATE) {
            this.velocityRate = MAX_VELOCITY_RATE;
        } else if (velocityRate < MIN_VELOCITY_RATE) {
            this.velocityRate = MIN_VELOCITY_RATE;
        } else {
            this.velocityRate = velocityRate;
        }
    }

    /**
     * 设置选中的item位置
     */
    public void setSelectedItem(int position) {
        if (position >= 0 && position < dataSize) {
            selectedItemPosition = position;
            notifyDataSetChanged();
        }
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        invalidate();
    }

    /**
     * 设置选中区域的线的颜色
     */
    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
        invalidate();
    }

    /**
     * 设置是否循环滚动
     */
    public void setCircle(boolean circle) {
        isCircle = circle;
        notifyDataSetChanged();
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(float textSize) {
        this.textSize = textSize;
        paint.setTextSize(scaleDensity * textSize);
        invalidate();
    }

    /**
     * 设置数据
     */
    public void setData(List<String> data) {
        reset();
        this.data = data;
        dataSize = data == null ? 0 : data.size();
        notifyDataSetChanged();
    }

    private void notifyDataSetChanged() {
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
            //滚动范围设置
            int temp2 = (showSize + 1) / 2 * itemHeight;
            int realHeight = dataSize * itemHeight;
            if (isCircle) {
                minScrollY = Integer.MIN_VALUE;
                maxScrollY = Integer.MAX_VALUE;
            } else {
                minScrollY = temp2 - realHeight;
                maxScrollY = (showSize - 1) / 2 * itemHeight;
            }
            //设置选中position
            int halfShowSize = showSize / 2;
            if (dataSize == 0) {
                lastSelectedItemPosition = selectedItemPosition = -1;
            } else if (selectedItemPosition == -1) {
                lastSelectedItemPosition = selectedItemPosition = halfShowSize;
            } else {
                lastSelectedItemPosition = selectedItemPosition;
                scrollY = (halfShowSize - selectedItemPosition) * itemHeight;
            }
            //遮罩层相关
            int[] colors = new int[]{0xFFFFFFFF, 0xAAFFFFFF, 0x00FFFFFF, 0x00FFFFFF, 0xAAFFFFFF, 0xFFFFFFFF};
            float[] positions = new float[]{0.0f, centerItemTop / height, centerItemTop / height, centerItemBottom / height, centerItemBottom / height, 1.0f};
            shader = new LinearGradient(0, 0, 0, height, colors, positions, Shader.TileMode.REPEAT);
            coverPaint.setShader(shader);
            //重新计算的标识
            isStart = false;
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollY = mScroller.getCurrY();
            isNeedUpdateSelectedItemPosition = true;
        } else {
            //抛模式结束后的修正
            if (mode == MODE_FLING) {
                correctScrollY();
            }
            //计算选中item位置
            if (isNeedUpdateSelectedItemPosition) {
                computeSelectedItemPosition();
                onSelectedChanged();
            }
        }
        invalidate();
        super.computeScroll();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        measureData();
        canvas.drawColor(Color.WHITE);
        //绘制数据
        paint.setColor(textColor);
        if (dataSize > 0) {
            int startItemPos = (int) -scrollY / itemHeight;
            int halfShowSize = showSize / 2;
            for (int i = startItemPos, j = 0; i < startItemPos + showSize + halfShowSize; j++, i++) {
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
        paint.setStrokeWidth(density);
        canvas.drawLine(getPaddingLeft(), centerItemTop, width - getPaddingRight(), centerItemTop, paint);
        canvas.drawLine(getPaddingLeft(), centerItemBottom, width - getPaddingRight(), centerItemBottom, paint);
        coverPaint.setShader(shader);
        canvas.drawRect(0, 0, width, height, coverPaint);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //手势检测器传递了事件且是手指抬起动作，进行位置修正，针对滑动情况
        boolean detectedUp = event.getAction() == MotionEvent.ACTION_UP;
        if (!mGestureDetector.onTouchEvent(event) && detectedUp) {
            checkStateAndPosition();
        }
        return true;
    }

    private void checkStateAndPosition() {
        if (!isCircle) {
            if (scrollY < minScrollY) {
                //上拉超出
                mScroller.startScroll(0, (int) scrollY, 0, (int) (minScrollY - scrollY), 400);
            } else if (scrollY > maxScrollY) {
                //下拉超出
                mScroller.startScroll(0, (int) scrollY, 0, (int) (maxScrollY - scrollY), 400);
            } else {
                //滑动情况下的位置修正
                if (mode == MODE_DRAG) {
                    correctScrollY();
                }
            }
        } else {
            //滑动情况下的位置修正
            if (mode == MODE_DRAG) {
                correctScrollY();
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

    private float getBaseLine(Paint paint, float top, float height) {
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        return (2 * top + height - fontMetrics.bottom - fontMetrics.top) / 2;
    }

    private void computeSelectedItemPosition() {
        //若修正未完成，不算选中
        if (scrollY % itemHeight == 0) {
            int halfShowSize = showSize / 2;
            if (scrollY >= 0) {
                //向下滑
                int scrollItemCount = (int) (scrollY / itemHeight);
                if (scrollItemCount > halfShowSize) {
                    //下滑条目数量大于显示数量的一半
                    int temp = (scrollItemCount - halfShowSize) % dataSize;
                    selectedItemPosition = temp == 0 ? temp : dataSize - temp;
                } else {
                    //下滑条目数不足显示数量的一半
                    selectedItemPosition = halfShowSize - scrollItemCount;
                }
            } else {
                //向上滑
                selectedItemPosition = (halfShowSize - (int) (scrollY / itemHeight)) % dataSize;
            }
        }
        isNeedUpdateSelectedItemPosition = false;
    }

    private void onSelectedChanged() {
        if (selectedItemPosition != lastSelectedItemPosition) {
            if (mOnSelectedChangedListener != null) {
                mOnSelectedChangedListener.onSelectedChanged();
            }
            lastSelectedItemPosition = selectedItemPosition;
        }
    }

    private void reset() {
        selectedItemPosition = lastSelectedItemPosition = -1;
        isNeedUpdateSelectedItemPosition = false;
        scrollY = 0;
        if (mScroller != null && !mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
        data = null;
        dataSize = 0;
    }

    public interface OnSelectedChangedListener {
        /**
         * 选中条目变化监听
         */
        void onSelectedChanged();
    }
}
