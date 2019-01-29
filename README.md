### 远程依赖
**android studio 添加如下代码**
```
allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}
```
```
dependencies {
	        implementation 'com.github.HelloMMMMM:Picker:versionCode'
	}
```

### WheelView
基础滚轮view为WheelView，可使用其自行创建选择器

### 日期选择器设置
```
DatePicker datePicker = new DatePickerBuilder(new DatePickerParams())
                    .setAlignMode(PickerConstant.CENTER_ALIGN_MODE)
                    .setBtnStyle(PickerConstant.TOP_BTN_STYLE)
                    .setCircle(false)
                    .setLeftText("取消")
                    .setRightText("确定")
                    .setLeftTextColor(0xff000000)
                    .setRightTextColor(0xff000000)
                    .setLeftTextSize(16)
                    .setRightTextSize(16)
                    .setLineColor(0xffadadad)
                    .setOffsetX(-16)
                    .setShowMode(PickerConstant.BOTTOM_STYLE)
                    .setShowSize(5)
                    .setTitle("选择日期")
                    .setTitleTextColor(0xff666666)
                    .setTitleTextSize(20)
                    .setVelocityRate(0.7f)
                    .setCurrentDate(1111, 11, 11)
                    .setOnDateSelectedListener(new DatePicker.OnDateSelectedListener() {
                        @Override
                        public void onDateSelected(String year, String month, String day) {
                            Toast.makeText(MainActivity.this, year + "-" + month + "-" + day, Toast.LENGTH_SHORT).show();
                        }
                    }).build();
            datePicker.show(fragmentManager, "datePicker");
```
### 地址选择器设置
```
AddressPicker addressPicker = new AddressPickerBuilder(this, new AddressPickerParams())
                    .setAlignMode(PickerConstant.LEFT_ALIGN_MODE)
                    .setBtnStyle(PickerConstant.BOTTOM_BTN_STYLE)
                    .setCircle(true)
                    .setLeftText("取消")
                    .setRightText("确定")
                    .setLeftTextColor(0xff000000)
                    .setRightTextColor(0xff000000)
                    .setLeftTextSize(16)
                    .setRightTextSize(16)
                    .setLineColor(0xffadadad)
                    .setOffsetX(-16)
                    .setShowMode(PickerConstant.BOTTOM_STYLE)
                    .setShowSize(5)
                    .setVelocityRate(0.7f)
                    .setLeftBtnBackgroundColor(Color.GREEN)
                    .setRightBtnBackgroundColor(Color.BLUE)
                    .setCurrentAddress("湖北", "襄阳市", "枣阳市")
                    .setOnAddressSelectedListener(new AddressPicker.OnAddressSelectedListener() {
                        @Override
                        public void onAddressSelected(String year, String month, String day) {
                            Toast.makeText(MainActivity.this, year + "-" + month + "-" + day, Toast.LENGTH_SHORT).show();
                        }
                    }).build();
            addressPicker.show(fragmentManager, "addressPicker");
```

# 欢迎提交issues
