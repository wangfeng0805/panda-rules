package org.wangfeng.panda.app.model.enums;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public enum DateFormatEnum {
    YYYY_MM_DD_FORMAT("年月日","yyyy-MM-dd"),
    YYYY_MM_FORMAT("年月","yyyy-MM"),
    YYYY_MM_DD_HH_MM_SS("年月日时分秒","yyyy-MM-dd HH:mm:ss"),
    YYYYMMDD_FORMAT("年月日","yyyyMMdd"),
    DEFAULT_FORMAT_YMD("默认年月日","yyyy/MM/dd"),
    DEFAULT_FORMAT_YMDHMS("默认年月日时分秒","yyyy/MM/dd HH:mm:ss");


    private String name;
    private String format;


    public String getName() {
        return name;
    }

    public String getFormat() {
        return format;
    }

    DateFormatEnum(String name, String format) {
        this.name = name;
        this.format = format;
    }


    public static DateFormatEnum getByFormat(String format){
        if(format == null){
            return null;
        }
        for(DateFormatEnum i: DateFormatEnum.values()){
            if(i.getFormat().equals(format)){
                return i;
            }
        }
        return null;
    }



    /**
     * 得到所有的日期格式
     * @return
     */
    public static List<JSONObject> queryAllDateFormatEnums(){
        List<JSONObject> dateFormatEnumList = new ArrayList();
        for(DateFormatEnum i: DateFormatEnum.values()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name",i.getName());
            jsonObject.put("format",i.getFormat());
            dateFormatEnumList.add(jsonObject);
        }
        return dateFormatEnumList;
    }


}
