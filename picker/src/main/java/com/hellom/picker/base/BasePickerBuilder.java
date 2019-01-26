package com.hellom.picker.base;

import android.graphics.drawable.Drawable;

/**
 * author:helloM
 * email:1694327880@qq.com
 * desc:picker基础接口，提供设置picker接口，其中一部分为picker内容设置，
 * 一部分为picker按钮设置，按钮设置分为按钮在上和按钮在下两种方式，在上
 * 可设置标题，不可设置按钮背景，在下不可设置标题，可设置按钮背景，其他为
 * 两种方式皆可设置
 */
public abstract class BasePickerBuilder<K extends BasePickerBuilder, T> {

    private PickerParams params;

    public BasePickerBuilder(PickerParams params) {
        this.params = params;
    }

    /**
     * 必须重写，以顺利转换为子builder类
     *
     * @return 子builder类
     */
    protected abstract K castBuilder();

    /**
     * 显示位置
     *
     * @param mode 1:bottom 2:center
     * @return 指定builder
     */
    public K setShowMode(int mode) {
        params.setShowMode(mode);
        return castBuilder();
    }

    /**
     * 偏移
     *
     * @param offsetX 负左偏，正右偏
     * @return 指定builder
     */
    public K setOffsetX(int offsetX) {
        params.setOffsetX(offsetX);
        return castBuilder();
    }

    /**
     * 文字大小
     *
     * @param size 文字大小
     * @return 指定builder
     */
    public K setTextSize(int size) {
        params.setTextSize(size);
        return castBuilder();
    }

    /**
     * 文字颜色
     *
     * @param color 颜色
     * @return 指定builder
     */
    public K setTextColor(int color) {
        params.setTextColor(color);
        return castBuilder();
    }

    /**
     * 分割线颜色
     *
     * @param color 颜色
     * @return 指定builder
     */
    public K setLineColor(int color) {
        params.setLineColor(color);
        return castBuilder();
    }

    /**
     * 设置按钮位置样式
     *
     * @param style 特定的按钮位置样式
     * @return 指定builder
     */
    public K setBtnStyle(int style) {
        params.setBtnStyle(style);
        return castBuilder();
    }

    /**
     * 按钮样式在上时，可以设置标题
     *
     * @param title 标题内容
     * @return 指定builder
     */
    public K setTitle(String title) {
        params.setTitle(title);
        return castBuilder();
    }

    /**
     * 设置左侧按钮文字内容
     *
     * @param leftText 左侧按钮文本
     * @return 指定builder
     */
    public K setLeftText(String leftText) {
        params.setLeftText(leftText);
        return castBuilder();
    }

    /**
     * 设置右侧按钮文本
     *
     * @param rightText 按钮文本
     * @return 指定builder
     */
    public K setRightText(String rightText) {
        params.setRightText(rightText);
        return castBuilder();
    }

    /**
     * 设置标题颜色
     *
     * @param color 颜色
     * @return 指定builder
     */
    public K setTitleTextColor(int color) {
        params.setTitleTextColor(color);
        return castBuilder();
    }

    /**
     * 设置左侧文本颜色
     *
     * @param color 颜色
     * @return 指定builder
     */
    public K setLeftTextColor(int color) {
        params.setLeftTextColor(color);
        return castBuilder();
    }

    /**
     * 设置右侧文本颜色
     *
     * @param color 颜色
     * @return 指定builder
     */
    public K setRightTextColor(int color) {
        params.setRightTextColor(color);
        return castBuilder();
    }

    /**
     * 设置标题文字大小
     *
     * @param size 文字大小
     * @return 指定builder
     */
    public K setTitleTextSize(int size) {
        params.setTitleTextSize(size);
        return castBuilder();
    }

    /**
     * 设置左侧文字大小
     *
     * @param size 文字大小
     * @return 指定builder
     */
    public K setLeftTextSize(int size) {
        params.setLeftTextSize(size);
        return castBuilder();
    }

    /**
     * 设置右侧文字大小
     *
     * @param size 右侧文字大小
     * @return 指定builder
     */
    public K setRightTextSize(int size) {
        params.setRightTextSize(size);
        return castBuilder();
    }

    /**
     * 设置左侧按钮背景颜色
     *
     * @param color 颜色
     * @return 指定builder
     */
    public K setLeftBtnBackgroundColor(int color) {
        params.setLeftBtnBackgroundColor(color);
        return castBuilder();
    }

    /**
     * 设置右侧按钮背景颜色
     *
     * @param color 颜色
     * @return 指定builder
     */
    public K setRightBtnBackgroundColor(int color) {
        params.setRightBtnBackgroundColor(color);
        return castBuilder();
    }

    /**
     * 以资源id方式设置左侧按钮背景
     *
     * @param resId 资源id
     * @return 指定builder
     */
    public K setLeftBtnResource(int resId) {
        params.setLeftBtnResource(resId);
        return castBuilder();
    }

    /**
     * 以资源id方式设置右侧按钮背景
     *
     * @param resId 资源id
     * @return 指定builder
     */
    public K setRightBtnResource(int resId) {
        params.setRightBtnResource(resId);
        return castBuilder();
    }

    /**
     * 以drawable方式设置左侧按钮背景
     *
     * @param drawable drawable
     * @return 指定builder
     */
    public K setLeftBtnDrawable(Drawable drawable) {
        params.setLeftBtnDrawable(drawable);
        return castBuilder();
    }

    /**
     * 以drawable方式设置右侧按钮背景
     *
     * @param drawable drawable
     * @return 指定builder
     */
    public K setRightBtnDrawable(Drawable drawable) {
        params.setRightBtnDrawable(drawable);
        return castBuilder();
    }

    /**
     * 设置内容的对齐方式
     *
     * @param alignMode 对齐方式
     * @return 指定builder
     */
    public K setAlignMode(int alignMode) {
        params.setAlignMode(alignMode);
        return castBuilder();
    }

    /**
     * 设置是否循环滚动
     *
     * @param circle 是否循环滚动
     * @return 指定builder
     */
    public K setCircle(boolean circle) {
        params.setCircle(circle);
        return castBuilder();
    }

    /**
     * 设置显示个数
     *
     * @param showSize 显示个数
     * @return 指定builder
     */
    public K setShowSize(int showSize) {
        params.setShowSize(showSize);
        return castBuilder();
    }

    /**
     * 设置滚动速率比例
     *
     * @param velocityRate 速率比例
     * @return 指定builder
     */
    public K setVelocityRate(float velocityRate) {
        params.setVelocityRate(velocityRate);
        return castBuilder();
    }

    /**
     * 创建dialog,必须实现
     *
     * @return 指定的dialog
     */
    public abstract T build();
}
