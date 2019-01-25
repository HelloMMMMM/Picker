package com.hellom.picker;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import static com.hellom.picker.PickerConstant.*;

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
    private float velocityRate = DEFAULT_VELOCITYRATE;

    protected PickerParams() {

    }

    protected PickerParams(Parcel in) {
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

    public int getShowMode() {
        return showMode;
    }

    public void setShowMode(int showMode) {
        this.showMode = showMode;
    }

    public int getOffsetX() {
        return mOffsetX;
    }

    public void setOffsetX(int mOffsetX) {
        this.mOffsetX = mOffsetX;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public int getBtnStyle() {
        return btnStyle;
    }

    public void setBtnStyle(int btnStyle) {
        this.btnStyle = btnStyle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLeftText() {
        return leftText;
    }

    public void setLeftText(String leftText) {
        this.leftText = leftText;
    }

    public String getRightText() {
        return rightText;
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
    }

    public int getTitleTextColor() {
        return titleTextColor;
    }

    public void setTitleTextColor(int titleTextColor) {
        this.titleTextColor = titleTextColor;
    }

    public int getLeftTextColor() {
        return leftTextColor;
    }

    public void setLeftTextColor(int leftTextColor) {
        this.leftTextColor = leftTextColor;
    }

    public int getRightTextColor() {
        return rightTextColor;
    }

    public void setRightTextColor(int rightTextColor) {
        this.rightTextColor = rightTextColor;
    }

    public int getTitleTextSize() {
        return titleTextSize;
    }

    public void setTitleTextSize(int titleTextSize) {
        this.titleTextSize = titleTextSize;
    }

    public int getLeftTextSize() {
        return leftTextSize;
    }

    public void setLeftTextSize(int leftTextSize) {
        this.leftTextSize = leftTextSize;
    }

    public int getRightTextSize() {
        return rightTextSize;
    }

    public void setRightTextSize(int rightTextSize) {
        this.rightTextSize = rightTextSize;
    }

    public int getLeftBtnBackgroundColor() {
        return leftBtnBackgroundColor;
    }

    public void setLeftBtnBackgroundColor(int leftBtnBackgroundColor) {
        this.leftBtnBackgroundColor = leftBtnBackgroundColor;
    }

    public int getRightBtnBackgroundColor() {
        return rightBtnBackgroundColor;
    }

    public void setRightBtnBackgroundColor(int rightBtnBackgroundColor) {
        this.rightBtnBackgroundColor = rightBtnBackgroundColor;
    }

    public Drawable getLeftBtnDrawable() {
        return leftBtnDrawable;
    }

    public void setLeftBtnDrawable(Drawable leftBtnDrawable) {
        this.leftBtnDrawable = leftBtnDrawable;
    }

    public Drawable getRightBtnDrawable() {
        return rightBtnDrawable;
    }

    public void setRightBtnDrawable(Drawable rightBtnDrawable) {
        this.rightBtnDrawable = rightBtnDrawable;
    }

    public int getLeftBtnResource() {
        return leftBtnResource;
    }

    public void setLeftBtnResource(int leftBtnResource) {
        this.leftBtnResource = leftBtnResource;
    }

    public int getRightBtnResource() {
        return rightBtnResource;
    }

    public void setRightBtnResource(int rightBtnResource) {
        this.rightBtnResource = rightBtnResource;
    }

    public int getAlignMode() {
        return alignMode;
    }

    public void setAlignMode(int alignMode) {
        this.alignMode = alignMode;
    }

    public boolean isCircle() {
        return isCircle;
    }

    public void setCircle(boolean circle) {
        isCircle = circle;
    }

    public int getShowSize() {
        return showSize;
    }

    public void setShowSize(int showSize) {
        this.showSize = showSize;
    }

    public float getVelocityRate() {
        return velocityRate;
    }

    public void setVelocityRate(float velocityRate) {
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
