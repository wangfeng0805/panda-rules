package org.wangfeng.panda.app.model.enums;

/**
 * 规则类别名称(1-系统内置；2-自定义)
 */
public enum RuleCategoryEnum {

    SYSTEM((short)1,"系统内置"),
    CUSTOM((short)2,"自定义");

    private Short code;
    private String name;

    public Short getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    RuleCategoryEnum(Short code, String name) {
        this.code = code;
        this.name = name;
    }

    public static RuleCategoryEnum getByCode(Short code){
        if(code == null){
            return null;
        }
        for(RuleCategoryEnum i: RuleCategoryEnum.values()){
            if(i.getCode() == code.intValue()){
                return i;
            }
        }
        return null;
    }
}
