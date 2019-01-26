package com.hellom.picker.base;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import static com.hellom.picker.base.PickerConstant.*;

/**
 * author:helloM
 * email:1694327880@qq.com
 */
public class PickerParams implements Parcelable {
    /**
     * 显示位置
     */
    private int showMode = BOTTOM_STYLE;
    /**
     * 偏移(负左偏,正右偏)
     */
    private int mOffsetX = DEFAULT_OFFSET_X;
    /**
     * 文字相关
     */
    private int textSize = DEFAULT_TEXT_SIZE;
    private int textColor = DEFAULT_TEXT_COLOR;
    /**
     * 分割线颜色
     */
    private int lineColor = DEFAULT_LINE_COLOR;

    /**
     * 按钮样式设置
     */
    private int btnStyle = BOTTOM_BTN_STYLE;
    private String title;
    private String leftText = DEFAULT_LEFT_TEXT;
    private String rightText = DEFAULT_RIGHT_TEXT;
    private int titleTextColor = DEFAULT_BTN_TEXT_COLOR;
    private int leftTextColor = DEFAULT_BTN_TEXT_COLOR;
    private int rightTextColor = DEFAULT_BTN_TEXT_COLOR;
    private int titleTextSize = DEFAULT_BTN_TITLE_TEXT_SIZE;
    private int leftTextSize = DEFAULT_BTN_TEXT_SIZE;
    private int rightTextSize = DEFAULT_BTN_TEXT_SIZE;
    private int leftBtnBackgroundColor;
    private int rightBtnBackgroundColor;
    private int leftBtnResource;
    private int rightBtnResource;
    private Drawable leftBtnDrawable;
    private Drawable rightBtnDrawable;
    private int alignMode;
    private boolean isCircle;
    private int showSize = DEFAULT_SHOW_SIZE;
    private float velocityRate = DEFAULT_VELOCITY_RATE;

    protected PickerParams() {

    }

    private PickerParams(Parcel in) {
        showMode = in.readInt();
        mOffsetX = in.readInt();
        textSize = in.readInt();
        textColor = in.readInt();
        lineColor = in.readInt();
        btnStyle = in.readInt();
        title = in.readString();
        leftText = in.readString();
        rightText = in.readString();
        titleTextColor = in.readInt();
        leftTextColor = in.readInt();
        rightTextColor = in.readInt();
        titleTextSize = in.readInt();
        leftTextSize = in.readInt();
        rightTextSize = in.readInt();
        leftBtnBackgroundColor = in.readInt();
        rightBtnBackgroundColor = in.readInt();
        leftBtnResource = in.readInt();
        rightBtnResource = in.readInt();
        alignMode = in.readInt();
        isCircle = in.readByte() != 0;
        showSize = in.readInt();
        velocityRate = in.readFloat();
    }

    public static final Creator<PickerParams> CREATOR = new Creator<PickerParams>() {
        @Override
        public PickerParams createFromParcel(Parcel in) {
            return new PickerParams(in);
        }

        @Override
        public PickerParams[] newArray(int size) {
            return new PickerParams[size];
        }
    };

    int getShowMode() {
        return showMode;
    }

    void setShowMode(int showMode) {
        this.showMode = showMode;
    }

    int getOffsetX() {
        return mOffsetX;
    }

    void setOffsetX(int mOffsetX) {
        this.mOffsetX = mOffsetX;
    }

    int getTextSize() {
        return textSize;
    }

    void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    int getTextColor() {
        return textColor;
    }

    void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    int getLineColor() {
        return lineColor;
    }

    void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    int getBtnStyle() {
        return btnStyle;
    }

    void setBtnStyle(int btnStyle) {
        this.btnStyle = btnStyle;
    }

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    String getLeftText() {
        return leftText;
    }

    void setLeftText(String leftText) {
        this.leftText = leftText;
    }

    String getRightText() {
        return rightText;
    }

    void setRightText(String rightText) {
        this.rightText = rightText;
    }

    int getTitleTextColor() {
        return titleTextColor;
    }

    void setTitleTextColor(int titleTextColor) {
        this.titleTextColor = titleTextColor;
    }

    int getLeftTextColor() {
        return leftTextColor;
    }

    void setLeftTextColor(int leftTextColor) {
        this.leftTextColor = leftTextColor;
    }

    int getRightTextColor() {
        return rightTextColor;
    }

    void setRightTextColor(int rightTextColor) {
        this.rightTextColor = rightTextColor;
    }

    int getTitleTextSize() {
        return titleTextSize;
    }

    void setTitleTextSize(int titleTextSize) {
        this.titleTextSize = titleTextSize;
    }

    int getLeftTextSize() {
        return leftTextSize;
    }

    void setLeftTextSize(int leftTextSize) {
        this.leftTextSize = leftTextSize;
    }

    int getRightTextSize() {
        return rightTextSize;
    }

    void setRightTextSize(int rightTextSize) {
        this.rightTextSize = rightTextSize;
    }

    int getLeftBtnBackgroundColor() {
        return leftBtnBackgroundColor;
    }

    void setLeftBtnBackgroundColor(int leftBtnBackgroundColor) {
        this.leftBtnBackgroundColor = leftBtnBackgroundColor;
    }

    int getRightBtnBackgroundColor() {
        return rightBtnBackgroundColor;
    }

    void setRightBtnBackgroundColor(int rightBtnBackgroundColor) {
        this.rightBtnBackgroundColor = rightBtnBackgroundColor;
    }

    Drawable getLeftBtnDrawable() {
        return leftBtnDrawable;
    }

    void setLeftBtnDrawable(Drawable leftBtnDrawable) {
        this.leftBtnDrawable = leftBtnDrawable;
    }

    Drawable getRightBtnDrawable() {
        return rightBtnDrawable;
    }

    void setRightBtnDrawable(Drawable rightBtnDrawable) {
        this.rightBtnDrawable = rightBtnDrawable;
    }

    int getLeftBtnResource() {
        return leftBtnResource;
    }

    void setLeftBtnResource(int leftBtnResource) {
        this.leftBtnResource = leftBtnResource;
    }

    int getRightBtnResource() {
        return rightBtnResource;
    }

    void setRightBtnResource(int rightBtnResource) {
        this.rightBtnResource = rightBtnResource;
    }

    int getAlignMode() {
        return alignMode;
    }

    void setAlignMode(int alignMode) {
        this.alignMode = alignMode;
    }

    boolean isCircle() {
        return isCircle;
    }

    void setCircle(boolean circle) {
        isCircle = circle;
    }

    int getShowSize() {
        return showSize;
    }

    void setShowSize(int showSize) {
        this.showSize = showSize;
    }

    float getVelocityRate() {
        return velocityRate;
    }

    void setVelocityRate(float velocityRate) {
        this.velocityRate = velocityRate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(showMode);
        dest.writeInt(mOffsetX);
        dest.writeInt(textSize);
        dest.writeInt(textColor);
        dest.writeInt(lineColor);
        dest.writeInt(btnStyle);
        dest.writeString(title);
        dest.writeString(leftText);
        dest.writeString(rightText);
        dest.writeInt(titleTextColor);
        dest.writeInt(leftTextColor);
        dest.writeInt(rightTextColor);
        dest.writeInt(titleTextSize);
        dest.writeInt(leftTextSize);
        dest.writeInt(rightTextSize);
        dest.writeInt(leftBtnBackgroundColor);
        dest.writeInt(rightBtnBackgroundColor);
        dest.writeInt(leftBtnResource);
        dest.writeInt(rightBtnResource);
        dest.writeInt(alignMode);
        dest.writeByte((byte) (isCircle ? 1 : 0));
        dest.writeInt(showSize);
        dest.writeFloat(velocityRate);
    }
}
