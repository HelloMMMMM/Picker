package com.hellom.picker.addresspicker.db;

public class TableField {
    /**
     * 地址库字典表-表名
     */
    public static final String TABLE_ADDRESS_DICT = "address_dict";
    /**
     * 公用的id
     */
    public static final String FIELD_ID = "id";
    /**
     * 父id，自关联id主键
     */
    public static final String ADDRESS_DICT_FIELD_PARENTID = "parentId";
    public static final String ADDRESS_DICT_FIELD_ID = "id";
    /**
     * 地址编号
     */
    public static final String ADDRESS_DICT_FIELD_CODE = "code";
    /**
     * 中文名
     */
    public static final String ADDRESS_DICT_FIELD_NAME = "name";
    /**
     * 创建地址库字典表sql语句
     */
    static final String CREATE_ADDRESS_DICT_SQL = "create table " + TABLE_ADDRESS_DICT + "(" + ADDRESS_DICT_FIELD_ID + " integer not null," + ADDRESS_DICT_FIELD_PARENTID + " integer not null," + ADDRESS_DICT_FIELD_CODE + " text," + ADDRESS_DICT_FIELD_NAME + " text)";
}
