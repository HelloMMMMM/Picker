package com.hellom.picker;

import android.graphics.drawable.Drawable;

import static com.hellom.picker.PickerConstant.*;

/**
 * author:helloM
 * email:1694327880@qq.com
 */
public class PickerParams {
    /**
     * 显示位置
     */
    private int showMode = BOTTOM_STYLE;
    /**
     * 偏移(负左偏,正右偏)
     */
    private int mOffsetX = 0;
    /**
     * 文字相关
     */
    private int textSize = 16;
    private int textColor = 0xFF333333;
    /**
     * 分割线颜色
     */
    private int lineColor = 0xFFffffff;

    /**
     * 按钮样式设置
     */
    private int btnStyle = PickerConstant.BOTTOM_BTN_STYLE;
    private String title;
    private String leftText;
    private String rightText;
    private int titleTextColor = 0xFF000000;
    private int leftTextColor = 0xFF000000;
    private int rightTextColor = 0xFF000000;
    private int titleTextSize = 18;
    private int leftTextSize = 16;
    private int rightTextSize = 16;
    private int leftBtnBackgroundColor;
    private int rightBtnBackgroundColor;
    private int leftBtnResource;
    private int rightBtnResource;
    private Drawable leftBtnDrawable;
    private Drawable rightBtnDrawable;
    private int alignMode;
    private boolean isCircle;
    private int showSize;
    private float velocityRate;

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
}
