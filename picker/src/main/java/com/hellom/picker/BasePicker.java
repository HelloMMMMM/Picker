package com.hellom.picker;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hellom.picker.baseview.WheelView;

import static com.hellom.picker.PickerConstant.*;

/**
 * author:helloM
 * email:1694327880@qq.com
 */
public class BasePicker extends DialogFragment {

    private PickerParams params;
    protected WheelView pickerOne, pickerTwo, pickerThree;
    private TextView tvCancel, tvSure;
    private View btnView;

    public BasePicker() {

    }

    public static BasePicker newInstance() {

        Bundle args = new Bundle();

        BasePicker fragment = new BasePicker();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initStyle();
        View view = inflater.inflate(R.layout.dialog_fragment_base_picker, container, false);
        initView(view);
        setAttribute();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (params.getShowMode() == BOTTOM_STYLE) {
            setStyle(DialogFragment.STYLE_NO_TITLE, R.style.bottom_dialog_fragment);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initSize();
    }

    private void initStyle() {
        Dialog dialog = getDialog();
        dialog.setCanceledOnTouchOutside(true);
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            if (params.getShowMode() == BOTTOM_STYLE) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                layoutParams.gravity = Gravity.BOTTOM;
            } else if (params.getShowMode() == CENTER_STYLE) {
                layoutParams.gravity = Gravity.CENTER;
            }
        }
    }

    private void initSize() {
        Window window = getDialog().getWindow();
        if (window != null) {
            if (params.getShowMode() == BOTTOM_STYLE) {
                window.setLayout(-1, -2);
            } else if (params.getShowMode() == CENTER_STYLE) {
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                window.setLayout((int) (displayMetrics.widthPixels * 0.9f), -2);
            }
        }
    }

    private void initView(View view) {
        pickerOne = view.findViewById(R.id.picker_one);
        pickerTwo = view.findViewById(R.id.picker_two);
        pickerThree = view.findViewById(R.id.picker_three);
        if (params.getBtnStyle() == PickerConstant.BOTTOM_BTN_STYLE) {
            ViewStub bottomBtn = view.findViewById(R.id.bottom_btn);
            btnView = bottomBtn.inflate();
        } else if (params.getBtnStyle() == PickerConstant.TOP_BTN_STYLE) {
            ViewStub topBtn = view.findViewById(R.id.top_btn);
            btnView = topBtn.inflate();
        }
    }

    private void setAttribute() {
        tvCancel = btnView.findViewById(R.id.tv_cancel);
        tvSure = btnView.findViewById(R.id.tv_sure);
        if (params.getBtnStyle() == PickerConstant.BOTTOM_BTN_STYLE) {
            if (params.getLeftBtnBackgroundColor() != 0) {
                tvCancel.setBackgroundColor(params.getLeftBtnBackgroundColor());
            } else if (params.getLeftBtnResource() != 0) {
                tvCancel.setBackgroundResource(params.getLeftBtnResource());
            } else if (params.getLeftBtnDrawable() != null) {
                tvCancel.setBackground(params.getLeftBtnDrawable());
            }
            if (params.getRightBtnBackgroundColor() != 0) {
                tvSure.setBackgroundColor(params.getRightBtnBackgroundColor());
            } else if (params.getRightBtnResource() != 0) {
                tvSure.setBackgroundResource(params.getRightBtnResource());
            } else if (params.getRightBtnDrawable() != null) {
                tvSure.setBackground(params.getRightBtnDrawable());
            }
        } else if (params.getBtnStyle() == PickerConstant.TOP_BTN_STYLE) {
            TextView title = btnView.findViewById(R.id.tv_title);
            title.setText(params.getTitle());
            title.setTextColor(params.getTitleTextColor());
            title.setTextSize(params.getTitleTextSize());
        }
        tvCancel.setText(params.getLeftText());
        tvCancel.setTextSize(params.getLeftTextSize());
        tvCancel.setTextColor(params.getLeftTextColor());
        tvSure.setText(params.getRightText());
        tvSure.setTextSize(params.getRightTextSize());
        tvSure.setTextColor(params.getRightTextColor());
        pickerOne.setTextSize(params.getTextSize());
        pickerTwo.setTextSize(params.getTextSize());
        pickerThree.setTextSize(params.getTextSize());
        pickerOne.setTextColor(params.getTextColor());
        pickerTwo.setTextColor(params.getTextColor());
        pickerThree.setTextColor(params.getTextColor());
        pickerOne.setOffsetX(params.getOffsetX());
        pickerTwo.setOffsetX(params.getOffsetX());
        pickerThree.setOffsetX(params.getOffsetX());
        pickerOne.setLineColor(params.getLineColor());
        pickerTwo.setLineColor(params.getLineColor());
        pickerThree.setLineColor(params.getLineColor());
        pickerOne.setAlignMode(params.getAlignMode());
        pickerTwo.setAlignMode(params.getAlignMode());
        pickerThree.setAlignMode(params.getAlignMode());
        pickerOne.setCircle(params.isCircle());
        pickerTwo.setCircle(params.isCircle());
        pickerThree.setCircle(params.isCircle());
        pickerOne.setShowSize(params.getShowSize());
        pickerTwo.setShowSize(params.getShowSize());
        pickerThree.setShowSize(params.getShowSize());
        pickerOne.setVelocityRate(params.getVelocityRate());
        pickerTwo.setVelocityRate(params.getVelocityRate());
        pickerThree.setVelocityRate(params.getVelocityRate());
    }
}
