package com.hellom.picker.base;

import android.support.v4.app.FragmentManager;

/**
 * author:helloM
 * email:1694327880@qq.com
 */
public interface IBasePicker {
    /**
     * 显示dialogFragment
     *
     * @param fragmentManager fragment管理
     * @param tag             fragment标签
     */
    void show(FragmentManager fragmentManager, String tag);
}
