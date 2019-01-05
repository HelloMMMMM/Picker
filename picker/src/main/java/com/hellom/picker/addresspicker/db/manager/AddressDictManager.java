package com.hellom.picker.addresspicker.db.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.hellom.picker.addresspicker.bean.AdressBean;
import com.hellom.picker.addresspicker.bean.City;
import com.hellom.picker.addresspicker.bean.County;
import com.hellom.picker.addresspicker.bean.Province;
import com.hellom.picker.addresspicker.bean.Street;
import com.hellom.picker.addresspicker.db.AssetsDatabaseManager;
import com.hellom.picker.addresspicker.db.TableField;

import java.util.ArrayList;
import java.util.List;

import static com.hellom.picker.addresspicker.db.TableField.ADDRESS_DICT_FIELD_CODE;
import static com.hellom.picker.addresspicker.db.TableField.ADDRESS_DICT_FIELD_ID;
import static com.hellom.picker.addresspicker.db.TableField.ADDRESS_DICT_FIELD_PARENTID;

public class AddressDictManager {
    private SQLiteDatabase db;

    public AddressDictManager(Context context) {
        // 初始化，只需要调用一次
        AssetsDatabaseManager.initManager(context);
        // 获取管理对象，因为数据库需要通过管理对象才能够获取
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        // 通过管理对象获取数据库
        db = mg.getDatabase("address.db");
    }

    /**
     * 增加一个地址
     */
    public void insertAddress(AdressBean.ChangeRecordsBean address) {
        if (address != null) {
            //手动设置开启事务
            db.beginTransaction();
            try {
                ContentValues values = new ContentValues();
                values.put(ADDRESS_DICT_FIELD_CODE, address.code);
                values.put(TableField.ADDRESS_DICT_FIELD_NAME, address.name);
                values.put(ADDRESS_DICT_FIELD_PARENTID, address.parentId);
                values.put(ADDRESS_DICT_FIELD_ID, address.id);
                db.insert(TableField.TABLE_ADDRESS_DICT, null, values);
                db.setTransactionSuccessful(); //设置事务处理成功
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction(); //事务终止
            }
        }
    }

    /**
     * 增加地址集合
     */
    public void insertAddress(List<AdressBean.ChangeRecordsBean> list) {
        if (list != null) {
            //手动设置开启事务
            db.beginTransaction();
            try {
                for (AdressBean.ChangeRecordsBean adress : list) {
                    ContentValues values = new ContentValues();
                    values.put(ADDRESS_DICT_FIELD_CODE, adress.code);
                    values.put(TableField.ADDRESS_DICT_FIELD_NAME, adress.name);
                    values.put(ADDRESS_DICT_FIELD_PARENTID, adress.parentId);
                    values.put(ADDRESS_DICT_FIELD_ID, adress.id);
                    db.insert(TableField.TABLE_ADDRESS_DICT, null, values);
                }
                //设置事务处理成功
                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //事务终止
                db.endTransaction();
            }
        }
    }

    /**
     * 更新地址
     */
    public void updateAddressInfo(AdressBean.ChangeRecordsBean address) {
        if (address != null) {
            //手动设置开启事务
            db.beginTransaction();
            try {
                ContentValues values = new ContentValues();
                values.put(ADDRESS_DICT_FIELD_CODE, address.code);
                values.put(TableField.ADDRESS_DICT_FIELD_NAME, address.name);
                values.put(ADDRESS_DICT_FIELD_PARENTID, address.parentId);
                values.put(ADDRESS_DICT_FIELD_ID, address.id);
                String[] args = {String.valueOf(address.id)};
                db.update(TableField.TABLE_ADDRESS_DICT, values, TableField.FIELD_ID + "=?", args);
                //设置事务处理成功
                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //事务终止
                db.endTransaction();
            }
        }
    }

    /**
     * 查找 地址 数据
     */
    public List<AdressBean.ChangeRecordsBean> getAddressList() {
        List<AdressBean.ChangeRecordsBean> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TableField.TABLE_ADDRESS_DICT + " order by sort asc", null);
        while (cursor.moveToNext()) {
            AdressBean.ChangeRecordsBean addressInfo = new AdressBean.ChangeRecordsBean();
            addressInfo.id = cursor.getInt(cursor.getColumnIndex(TableField.FIELD_ID));
            addressInfo.parentId = cursor.getInt(cursor.getColumnIndex(ADDRESS_DICT_FIELD_PARENTID));
            addressInfo.code = cursor.getString(cursor.getColumnIndex(ADDRESS_DICT_FIELD_CODE));
            addressInfo.name = cursor.getString(cursor.getColumnIndex(TableField.ADDRESS_DICT_FIELD_NAME));
            list.add(addressInfo);
        }
        cursor.close();
        return list;
    }

    /**
     * 获取省份列表
     */
    public List<Province> getProvinceList() {
        List<Province> provinceList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TableField.TABLE_ADDRESS_DICT + " where " + ADDRESS_DICT_FIELD_PARENTID + "=?", new String[]{String.valueOf(0)});
        while (cursor.moveToNext()) {
            Province province = new Province();
            province.id = cursor.getInt(cursor.getColumnIndex(ADDRESS_DICT_FIELD_ID));
            province.code = cursor.getString(cursor.getColumnIndex(ADDRESS_DICT_FIELD_CODE));
            province.name = cursor.getString(cursor.getColumnIndex(TableField.ADDRESS_DICT_FIELD_NAME));
            provinceList.add(province);
        }
        cursor.close();
        return provinceList;
    }

    /**
     * 获取省份
     */
    public String getProvince(String provinceCode) {
        Cursor cursor = db.rawQuery("select * from " + TableField.TABLE_ADDRESS_DICT + " where " + ADDRESS_DICT_FIELD_CODE + "=?", new String[]{provinceCode});
        if (cursor != null && cursor.moveToFirst()) {
            Province province = new Province();
            province.id = cursor.getInt(cursor.getColumnIndex(ADDRESS_DICT_FIELD_ID));
            province.code = cursor.getString(cursor.getColumnIndex(ADDRESS_DICT_FIELD_CODE));
            province.name = cursor.getString(cursor.getColumnIndex(TableField.ADDRESS_DICT_FIELD_NAME));
            cursor.close();
            return province.name;
        } else {
            return "";
        }
    }

    /**
     * 获取省份
     */
    public Province getProvinceBean(String provinceCode) {
        Cursor cursor = db.rawQuery("select * from " + TableField.TABLE_ADDRESS_DICT + " where " + ADDRESS_DICT_FIELD_CODE + "=?", new String[]{provinceCode});
        if (cursor != null && cursor.moveToFirst()) {
            Province province = new Province();
            province.id = cursor.getInt(cursor.getColumnIndex(ADDRESS_DICT_FIELD_ID));
            province.code = cursor.getString(cursor.getColumnIndex(ADDRESS_DICT_FIELD_CODE));
            province.name = cursor.getString(cursor.getColumnIndex(TableField.ADDRESS_DICT_FIELD_NAME));
            cursor.close();
            return province;
        } else {
            return null;
        }
    }

    /**
     * 获取省份对应的城市列表
     */
    public List<City> getCityList(int provinceId) {
        List<City> cityList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TableField.TABLE_ADDRESS_DICT + " where " + ADDRESS_DICT_FIELD_PARENTID + "=?", new String[]{String.valueOf(provinceId)});
        while (cursor.moveToNext()) {
            City city = new City();
            city.id = cursor.getInt(cursor.getColumnIndex(ADDRESS_DICT_FIELD_ID));
            city.code = cursor.getString(cursor.getColumnIndex(ADDRESS_DICT_FIELD_CODE));
            city.name = cursor.getString(cursor.getColumnIndex(TableField.ADDRESS_DICT_FIELD_NAME));
            cityList.add(city);
        }
        cursor.close();
        return cityList;
    }

    /**
     * 获取城市
     */
    public String getCity(String cityCode) {
        Cursor cursor = db.rawQuery("select * from " + TableField.TABLE_ADDRESS_DICT + " where " + ADDRESS_DICT_FIELD_CODE + "=?", new String[]{cityCode});
        if (cursor != null && cursor.moveToFirst()) {
            City city = new City();
            city.id = cursor.getInt(cursor.getColumnIndex(ADDRESS_DICT_FIELD_ID));
            city.code = cursor.getString(cursor.getColumnIndex(ADDRESS_DICT_FIELD_CODE));
            city.name = cursor.getString(cursor.getColumnIndex(TableField.ADDRESS_DICT_FIELD_NAME));
            cursor.close();
            return city.name;
        } else {
            return "";
        }
    }

    /**
     * 获取城市
     */
    public City getCityBean(String cityCode) {
        Cursor cursor = db.rawQuery("select * from " + TableField.TABLE_ADDRESS_DICT + " where " + ADDRESS_DICT_FIELD_CODE + "=?", new String[]{cityCode});
        if (cursor != null && cursor.moveToFirst()) {
            City city = new City();
            city.id = cursor.getInt(cursor.getColumnIndex(ADDRESS_DICT_FIELD_ID));
            city.code = cursor.getString(cursor.getColumnIndex(ADDRESS_DICT_FIELD_CODE));
            city.name = cursor.getString(cursor.getColumnIndex(TableField.ADDRESS_DICT_FIELD_NAME));
            cursor.close();
            return city;
        } else {
            return null;
        }
    }

    /**
     * 获取城市对应的区，乡镇列表
     */
    public List<County> getCountyList(int cityId) {
        List<County> countyList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TableField.TABLE_ADDRESS_DICT + " where " + ADDRESS_DICT_FIELD_PARENTID + "=?", new String[]{String.valueOf(cityId)});
        while (cursor.moveToNext()) {
            County county = new County();
            county.id = cursor.getInt(cursor.getColumnIndex(ADDRESS_DICT_FIELD_ID));
            county.code = cursor.getString(cursor.getColumnIndex(ADDRESS_DICT_FIELD_CODE));
            county.name = cursor.getString(cursor.getColumnIndex(TableField.ADDRESS_DICT_FIELD_NAME));
            countyList.add(county);
        }
        cursor.close();
        return countyList;
    }

    public String getCounty(String countyCode) {
        Cursor cursor = db.rawQuery("select * from " + TableField.TABLE_ADDRESS_DICT + " where " + ADDRESS_DICT_FIELD_CODE + "=?", new String[]{countyCode});
        if (cursor != null && cursor.moveToFirst()) {
            County county = new County();
            county.id = cursor.getInt(cursor.getColumnIndex(ADDRESS_DICT_FIELD_ID));
            county.code = cursor.getString(cursor.getColumnIndex(ADDRESS_DICT_FIELD_CODE));
            county.name = cursor.getString(cursor.getColumnIndex(TableField.ADDRESS_DICT_FIELD_NAME));
            cursor.close();
            return county.name;
        } else {
            return "";
        }
    }

    public County getCountyBean(String countyCode) {
        Cursor cursor = db.rawQuery("select * from " + TableField.TABLE_ADDRESS_DICT + " where " + ADDRESS_DICT_FIELD_CODE + "=?", new String[]{countyCode});
        if (cursor != null && cursor.moveToFirst()) {
            County county = new County();
            county.id = cursor.getInt(cursor.getColumnIndex(ADDRESS_DICT_FIELD_ID));
            county.code = cursor.getString(cursor.getColumnIndex(ADDRESS_DICT_FIELD_CODE));
            county.name = cursor.getString(cursor.getColumnIndex(TableField.ADDRESS_DICT_FIELD_NAME));
            cursor.close();
            return county;
        } else {
            return null;
        }
    }

    /**
     * 获取区，乡镇对应的街道列表
     */
    public List<Street> getStreetList(int countyId) {
        List<Street> streetList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TableField.TABLE_ADDRESS_DICT + " where " + ADDRESS_DICT_FIELD_PARENTID + "=?", new String[]{String.valueOf(countyId)});
        while (cursor.moveToNext()) {
            Street street = new Street();
            street.id = cursor.getInt(cursor.getColumnIndex(ADDRESS_DICT_FIELD_ID));
            street.code = cursor.getString(cursor.getColumnIndex(ADDRESS_DICT_FIELD_CODE));
            street.name = cursor.getString(cursor.getColumnIndex(TableField.ADDRESS_DICT_FIELD_NAME));
            streetList.add(street);
        }
        cursor.close();
        return streetList;
    }

    public String getStreet(String streetCode) {
        Cursor cursor = db.rawQuery("select * from " + TableField.TABLE_ADDRESS_DICT + " where " + ADDRESS_DICT_FIELD_CODE + "=?", new String[]{streetCode});
        if (cursor != null && cursor.moveToFirst()) {
            Street street = new Street();
            street.id = cursor.getInt(cursor.getColumnIndex(ADDRESS_DICT_FIELD_ID));
            street.code = cursor.getString(cursor.getColumnIndex(ADDRESS_DICT_FIELD_CODE));
            street.name = cursor.getString(cursor.getColumnIndex(TableField.ADDRESS_DICT_FIELD_NAME));
            cursor.close();
            return street.name;
        } else {
            return "";
        }
    }

    public Street getStreetBean(String streetCode) {
        Cursor cursor = db.rawQuery("select * from " + TableField.TABLE_ADDRESS_DICT + " where " + ADDRESS_DICT_FIELD_CODE + "=?", new String[]{streetCode});
        if (cursor != null && cursor.moveToFirst()) {
            Street street = new Street();
            street.id = cursor.getInt(cursor.getColumnIndex(ADDRESS_DICT_FIELD_ID));
            street.code = cursor.getString(cursor.getColumnIndex(ADDRESS_DICT_FIELD_CODE));
            street.name = cursor.getString(cursor.getColumnIndex(TableField.ADDRESS_DICT_FIELD_NAME));
            cursor.close();
            return street;
        } else {
            return null;
        }
    }

    /**
     * 查找消息临时列表中是否存在这一条记录
     */
    public int isExist(AdressBean.ChangeRecordsBean bannerInfo) {
        int count = 0;
        Cursor cursor = db.rawQuery("select count(*) from " + TableField.TABLE_ADDRESS_DICT + " where " + TableField.FIELD_ID + "=?", new String[]{String.valueOf(bannerInfo.id)});
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

}
