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

import java.util.List;

import static com.hellom.picker.PickerConstant.*;

/**
 * author:helloM
 * email:1694327880@qq.com
 */
public class BasePicker extends DialogFragment implements View.OnClickListener {

    private PickerParams params;
    private WheelView pickerOne, pickerTwo, pickerThree;
    private TextView tvCancel, tvSure;
    private View btnView;
    private List<String> pickerOneData, pickerTwoData, pickerThreeData;
    private int pickerOneSelectedItemPosition = -1, pickerTwoSelectedItemPosition = -1, pickerThreeSelectedItemPosition = -1;
    private String pickerOneSelectedItem = null, pickerTwoSelectedItem = null, pickerThreeSelectedItem = null;

    /**
     * 监听
     */
    private OnClickListener onClickListener;
    private OnPickerSelectedChangedListener onPickerSelectedChangedListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnPickerSelectedChangedListener(OnPickerSelectedChangedListener onPickerSelectedChangedListener) {
        this.onPickerSelectedChangedListener = onPickerSelectedChangedListener;
    }

    public static BasePicker newInstance(PickerParams params) {
        Bundle args = new Bundle();
        args.putParcelable(PickerConstant.PICKER_PARAMS, params);
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
        initListener();
        initData();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        if (params.getShowMode() == BOTTOM_STYLE) {
            setStyle(DialogFragment.STYLE_NO_TITLE, R.style.bottom_dialog_fragment);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initSize();
    }

    private void getIntentData() {
        Bundle args = getArguments();
        if (args != null) {
            this.params = args.getParcelable(PickerConstant.PICKER_PARAMS);
        }
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

    private void initListener() {
        tvCancel.setOnClickListener(this);
        tvSure.setOnClickListener(this);
        pickerOne.setOnSelectedChangedListener(new WheelView.OnSelectedChangedListener() {
            @Override
            public void onSelectedChanged() {
                if (onPickerSelectedChangedListener != null) {
                    onPickerSelectedChangedListener.onPickerOneSelectedChanged(pickerOne.getSelectedItemData(), pickerOne.getSelectedItemPosition());
                }
            }
        });
        pickerTwo.setOnSelectedChangedListener(new WheelView.OnSelectedChangedListener() {
            @Override
            public void onSelectedChanged() {
                if (onPickerSelectedChangedListener != null) {
                    onPickerSelectedChangedListener.onPickerTwoSelectedChanged(pickerTwo.getSelectedItemData(), pickerTwo.getSelectedItemPosition());
                }
            }
        });
        pickerThree.setOnSelectedChangedListener(new WheelView.OnSelectedChangedListener() {
            @Override
            public void onSelectedChanged() {
                if (onPickerSelectedChangedListener != null) {
                    onPickerSelectedChangedListener.onPickerThreeSelectedChanged(pickerThree.getSelectedItemData(), pickerThree.getSelectedItemPosition());
                }
            }
        });
    }

    private void initData() {
        pickerOne.setData(pickerOneData);
        pickerTwo.setData(pickerTwoData);
        pickerThree.setData(pickerThreeData);
        if (pickerOneSelectedItem != null) {
            pickerOne.setSelectedItem(pickerOneSelectedItem);
        }
        if (pickerTwoSelectedItem != null) {
            pickerTwo.setSelectedItem(pickerTwoSelectedItem);
        }
        if (pickerThreeSelectedItem != null) {
            pickerThree.setSelectedItem(pickerThreeSelectedItem);
        }
        if (pickerOneSelectedItemPosition >= 0) {
            pickerOne.setSelectedItemPosition(pickerOneSelectedItemPosition);
        }
        if (pickerTwoSelectedItemPosition >= 0) {
            pickerTwo.setSelectedItemPosition(pickerTwoSelectedItemPosition);
        }
        if (pickerThreeSelectedItemPosition >= 0) {
            pickerThree.setSelectedItemPosition(pickerThreeSelectedItemPosition);
        }
    }

    @Override
    public void onClick(View v) {
        if (onClickListener != null) {
            int i = v.getId();
            if (i == R.id.tv_cancel) {
                onClickListener.cancel();
            } else if (i == R.id.tv_sure) {
                onClickListener.sure();
            }
        }
    }

    public List<String> getPickerOneData() {
        return pickerOneData;
    }

    public void setPickerOneData(List<String> pickerOneData) {
        this.pickerOneData = pickerOneData;
        if (pickerOne != null) {
            pickerOne.setData(pickerOneData);
        }
    }

    public List<String> getPickerTwoData() {
        return pickerTwoData;
    }

    public void setPickerTwoData(List<String> pickerTwoData) {
        this.pickerTwoData = pickerTwoData;
        if (pickerTwo != null) {
            pickerTwo.setData(pickerTwoData);
        }
    }

    public List<String> getPickerThreeData() {
        return pickerThreeData;
    }

    public void setPickerThreeData(List<String> pickerThreeData) {
        this.pickerThreeData = pickerThreeData;
        if (pickerThree != null) {
            pickerThree.setData(pickerThreeData);
        }
    }

    public void setPickerOneSelectedItemPosition(int pickerOneSelectedItemPosition) {
        this.pickerOneSelectedItemPosition = pickerOneSelectedItemPosition;
        if (pickerOne != null) {
            pickerOne.setSelectedItemPosition(pickerOneSelectedItemPosition);
        }

    }

    public void setPickerTwoSelectedItemPosition(int pickerTwoSelectedItemPosition) {
        this.pickerTwoSelectedItemPosition = pickerTwoSelectedItemPosition;
        if (pickerTwo != null) {
            pickerTwo.setSelectedItemPosition(pickerTwoSelectedItemPosition);
        }
    }

    public void setPickerThreeSelectedItemPosition(int pickerThreeSelectedItemPosition) {
        this.pickerThreeSelectedItemPosition = pickerThreeSelectedItemPosition;
        if (pickerThree != null) {
            pickerThree.setSelectedItemPosition(pickerThreeSelectedItemPosition);
        }
    }

    public void setPickerOneSelectedItem(String pickerOneSelectedItem) {
        this.pickerOneSelectedItem = pickerOneSelectedItem;
        if (pickerOne != null) {
            pickerOne.setSelectedItem(pickerOneSelectedItem);
        }
    }

    public void setPickerTwoSelectedItem(String pickerTwoSelectedItem) {
        this.pickerTwoSelectedItem = pickerTwoSelectedItem;
        if (pickerTwo != null) {
            pickerTwo.setSelectedItem(pickerTwoSelectedItem);
        }
    }

    public void setPickerThreeSelectedItem(String pickerThreeSelectedItem) {
        this.pickerThreeSelectedItem = pickerThreeSelectedItem;
        if (pickerThree != null) {
            pickerThree.setSelectedItem(pickerThreeSelectedItem);
        }
    }

    public int getPickerOneSelectedItemPosition() {
        return pickerOneSelectedItemPosition;
    }

    public int getPickerTwoSelectedItemPosition() {
        return pickerTwoSelectedItemPosition;
    }

    public int getPickerThreeSelectedItemPosition() {
        return pickerThreeSelectedItemPosition;
    }

    public String getPickerOneSelectedItem() {
        return pickerOne == null ? null : pickerOne.getSelectedItemData();
    }

    public String getPickerTwoSelectedItem() {
        return pickerTwo == null ? null : pickerTwo.getSelectedItemData();
    }

    public String getPickerThreeSelectedItem() {
        return pickerThree == null ? null : pickerThree.getSelectedItemData();
    }

    public interface OnClickListener {
        /**
         * 取消按钮
         */
        void cancel();

        /**
         * 确认按钮
         */
        void sure();
    }

    public interface OnPickerSelectedChangedListener {
        /**
         * 一级选择器选项变更监听
         *
         * @param data     选择的数据
         * @param position 选择的数据位于数据源的下标
         */
        void onPickerOneSelectedChanged(String data, int position);

        /**
         * 二级选择器选项变更监听
         *
         * @param data     选择的数据
         * @param position 选择的数据位于数据源的下标
         */
        void onPickerTwoSelectedChanged(String data, int position);

        /**
         * 三级选择器选项变更监听
         *
         * @param data     选择的数据
         * @param position 选择的数据位于数据源的下标
         */
        void onPickerThreeSelectedChanged(String data, int position);
    }
}
