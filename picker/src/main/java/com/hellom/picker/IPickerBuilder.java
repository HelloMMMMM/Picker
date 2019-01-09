package com.hellom.picker;

/**
 * author:helloM
 * email:1694327880@qq.com
 */
public interface IPickerBuilder<K, T> {

    /**
     * 显示位置
     *
     * @param mode 1:bottom 2:center
     * @return 指定builder
     */
    K setShowMode(int mode);

    /**
     * 偏移
     *
     * @param offsetX 负左偏，正右偏
     * @return 指定builder
     */
    K setOffsetX(int offsetX);

    /**
     * 文字大小
     *
     * @param size 文字大小
     * @return 指定builder
     */
    K setTextSize(int size);

    /**
     * 文字颜色
     *
     * @param color 颜色
     * @return 指定builder
     */
    K setTextColor(int color);

    /**
     * 分割线颜色
     *
     * @param color 颜色
     * @return 指定builder
     */
    K setLineColor(int color);

    /**
     * 创建dialog
     *
     * @return 指定的dialog
     */
    T build();
}
