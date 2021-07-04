package org.wangfeng.panda.app.model.enums;


/**
 * 状态枚举
 */
public enum StatusEnum {

    QI_YONG((short)1,"启用"),
    TING_YONG((short)0,"停用");


    private Short code;
    private String name;

    public Short getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    StatusEnum(Short code, String name) {
        this.code = code;
        this.name = name;
    }

    public static StatusEnum getByCode(Short code){
        if(code == null){
            return null;
        }
        for(StatusEnum i: StatusEnum.values()){
            if(i.getCode() == code.intValue()){
                return i;
            }
        }
        return null;
    }

}
