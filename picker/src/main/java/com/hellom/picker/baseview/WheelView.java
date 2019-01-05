package com.hellom.picker.baseview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

import com.hellom.picker.R;

import java.util.List;

/**
 * author:helloM
 * email:1694327880@qq.com
 */
public class WheelView extends View {
    /**
     * 滚动Y值
     */
    private float scrollY = 0;
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
    private int itemHeight = 0;
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
     * 数据数量
     */
    private int dataSize = 0;
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
     * 显示的item个数，默认为5
     */
    private int showSize;
    /**
     * 文字大小，默认16sp
     */
    private float textSize;
    /**
     * 是否可以循环滚动
     */
    private boolean isCircle;
    /**
     * 文字颜色
     */
    private int textColor;
    /**
     * 线条的颜色
     */
    private int lineColor;
    /**
     * 选中item文字位置偏移
     */
    private int mOffsetX;
    /**
     * 文字对齐方式,默认为居中
     */
    private int alignMode = CENTER_ALIGN_MODE;
    public static final int CENTER_ALIGN_MODE = 1;
    public static final int LEFT_ALIGN_MODE = 2;
    public static final int RIGHT_ALIGN_MODE = 3;
    /**
     * 数据
     */
    private List<String> data;
    /**
     * 速率比率,默认0.3
     */
    private float velocityRate;
    private static final float MAX_VELOCITY_RATE = 1.0f;
    private static final float MIN_VELOCITY_RATE = 0.1f;
    /**
     * 选中item变化监听
     */
    private OnSelectedChangedListener mOnSelectedChangedListener;

    /**
     * 获取选中item数据
     */
    public String getSelectedItemData() {
        return selectedItemPosition == -1 ? "" : data.get(selectedItemPosition);
    }

    /**
     * 获取选中item下标
     */
    public int getSelectedItemPosition() {
        return selectedItemPosition;
    }

    /**
     * 设置选中item监听
     */
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
     * 根据所在数据源的位置设置选中的item位置
     */
    public void setSelectedItemPosition(int position) {
        if (position >= 0 && position < dataSize) {
            selectedItemPosition = position;
            notifyDataSetChanged();
        }
    }

    /**
     * 根据内容设置选中item位置
     */
    public void setSelectedItem(String value) {
        if (dataSize > 0) {
            int position = data.indexOf(value);
            if (position >= 0) {
                selectedItemPosition = position;
                notifyDataSetChanged();
            }
        }
    }

    /**
     * 设置显示出来的条目个数
     */
    public void setShowSize(int showSize) {
        int evenNUmber2 = 2;
        if (showSize % evenNUmber2 == 0) {
            showSize += 1;
        }
        this.showSize = showSize;
        notifyDataSetChanged();
    }

    /**
     * 文字颜色设置
     */
    public void setTextColor(int textColor) {
        this.textColor = textColor;
        invalidate();
    }

    /**
     * 设置选中item文字位置偏移,负数为向左偏移,正数为向右偏移(单位dp)
     */
    public void setOffsetX(int offsetX) {
        this.mOffsetX = (int) (offsetX * density);
        notifyDataSetChanged();
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
     * 设置文字大小(单位sp)
     */
    public void setTextSize(float textSize) {
        this.textSize = scaleDensity * textSize;
        paint.setTextSize(this.textSize);
        invalidate();
    }

    /**
     * 设置对齐方式
     */
    public void setAlignMode(int alignMode) {
        if (alignMode >= CENTER_ALIGN_MODE && alignMode <= RIGHT_ALIGN_MODE) {
            this.alignMode = alignMode;
            setPaintAlign(alignMode);
            invalidate();
        }
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

    /**
     * 通知重新计算
     */
    private void notifyDataSetChanged() {
        isStart = true;
        invalidate();
    }

    public interface OnSelectedChangedListener {
        /**
         * 选中条目变化监听
         */
        void onSelectedChanged();
    }


    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initStyle(context, attrs);
        init();
    }

    private void initStyle(Context context, AttributeSet attrs) {
        scaleDensity = getResources().getDisplayMetrics().scaledDensity;
        density = getResources().getDisplayMetrics().density;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WheelView);
        textSize = typedArray.getDimension(R.styleable.WheelView_wl_textSize, 16 * scaleDensity);
        textColor = typedArray.getColor(R.styleable.WheelView_wl_textColor, 0xFF333333);
        lineColor = typedArray.getColor(R.styleable.WheelView_wl_lineColor, 0xFFd1d1d1);
        isCircle = typedArray.getBoolean(R.styleable.WheelView_wl_isCircle, false);
        showSize = typedArray.getInteger(R.styleable.WheelView_wl_showSize, 5);
        mOffsetX = (int) typedArray.getDimension(R.styleable.WheelView_wl_offsetX, 0);
        alignMode = typedArray.getInt(R.styleable.WheelView_wl_alignMode, CENTER_ALIGN_MODE);
        velocityRate = typedArray.getFloat(R.styleable.WheelView_wl_velocityRate, 0.3f);
        typedArray.recycle();
    }

    private void init() {
        mScroller = new OverScroller(getContext());
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
        setPaintAlign(alignMode);
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
                //onTouchEvent手动返回true时，手势检测所有回调返回值都不影响事件的接收
                //若使用gestureDetector的onTouchEvent作为返回值，才受其影响
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                //单击动作
                performClick();
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                //滑动模式(多次触发)
                mode = MODE_DRAG;
                scrollY += -distanceY;
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //抛模式(手指抬起时且速度大于一定值触发)
                mode = MODE_FLING;
                mScroller.fling(0, (int) scrollY, 0, (int) (velocityRate * velocityY), 0, 0, minScrollY, maxScrollY);
                return false;
            }
        });
    }

    private void setPaintAlign(int alignMode) {
        switch (alignMode) {
            case CENTER_ALIGN_MODE:
                paint.setTextAlign(Paint.Align.CENTER);
                break;
            case LEFT_ALIGN_MODE:
                paint.setTextAlign(Paint.Align.LEFT);
                break;
            case RIGHT_ALIGN_MODE:
                paint.setTextAlign(Paint.Align.RIGHT);
                break;
            default:
                throw new IllegalArgumentException("unknown argument:" + alignMode);
        }
    }

    private void measureData() {
        if (isStart) {
            width = getWidth();
            height = getHeight();
            int temp1 = height - getPaddingTop() - getPaddingBottom();
            itemHeight = temp1 / showSize;
            centerItemTop = temp1 / 2 + getPaddingTop() - itemHeight / 2;
            centerItemBottom = temp1 / 2 + getPaddingTop() + itemHeight / 2;
            //文字绘制x位置以及选中item的偏移
            int halfWidth = width / 2;
            if (mOffsetX < -halfWidth) {
                mOffsetX = -halfWidth;
            } else if (mOffsetX > halfWidth) {
                mOffsetX = halfWidth;
            }
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
            int halfShowSize = showSize / 2;
            //初始化scrollY
            scrollY = halfShowSize * itemHeight;
            //设置选中position
            if (dataSize == 0) {
                lastSelectedItemPosition = selectedItemPosition = -1;
            } else if (selectedItemPosition == -1) {
                lastSelectedItemPosition = selectedItemPosition = 0;
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
                //获取绘制内容
                String drawString = null;
                if (i >= 0 && i < dataSize) {
                    drawString = data.get(i);
                } else if (isCircle) {
                    int pos = i % dataSize;
                    drawString = data.get(pos < 0 ? pos + dataSize : pos);
                }
                if (drawString == null) {
                    continue;
                }
                //测量文字长度，若太长，缩小文字大小
                paint.setTextSize(textSize);
                float textWidth = paint.measureText(drawString);
                if (textWidth > width) {
                    paint.setTextSize(textSize * width / textWidth);
                }
                //在设置了偏移时,在可偏移区域计算偏移量
                float topY = j * itemHeight + scrollY % itemHeight;
                float baseLineY = getBaseLine(paint, topY, itemHeight);
                int offsetX = 0;
                if (mOffsetX != 0) {
                    int centerItemCenter = (int) (centerItemTop + itemHeight / 2);
                    if (baseLineY > (centerItemTop - itemHeight / 2) && baseLineY < (centerItemBottom + itemHeight / 2)) {
                        offsetX = (int) ((1 - Math.abs(baseLineY - centerItemCenter) / itemHeight) * mOffsetX);
                    }
                }
                canvas.drawText(drawString, itemX + offsetX, baseLineY, paint);
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
        //item高度为0或者无数据不处理事件
        if (itemHeight == 0 || dataSize == 0) {
            return false;
        }
        //处理滑动抬起时的位置修正、抛模式超出边界时的平滑回弹
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
}
