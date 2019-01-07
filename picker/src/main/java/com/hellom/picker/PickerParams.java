package com.hellom.picker;

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
}
