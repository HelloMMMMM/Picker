package com.hellom.picker.addresspicker.bean;

import java.util.ArrayList;
import java.util.List;

public class AdressBean {
    public int changeCount;
    public String code;
    public String message;

    public List<ChangeRecordsBean> changeRecords = new ArrayList<>();

    public static class ChangeRecordsBean {
        /*** id*/
        public int id;
        /*** 地址编号*/
        public String code;
        /*** 中文名*/
        public String name;
        /*** 父id*/
        public int parentId;
    }
}
