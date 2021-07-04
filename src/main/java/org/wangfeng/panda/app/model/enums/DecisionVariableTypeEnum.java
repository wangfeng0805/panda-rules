package org.wangfeng.panda.app.model.enums;

/**
 * 决策变量数据类型枚举
 */
public enum DecisionVariableTypeEnum {

    STRING((short)1,"字符串"),
    INTEGER((short)2,"整数"),
    BOOLEAN((short)3,"布尔"),
    DOUBLE((short)4,"浮点数"),
    ENUM((short)5,"枚举值");

    private Short code;
    private String name;

    public Short getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    DecisionVariableTypeEnum(Short code, String name) {
        this.code = code;
        this.name = name;
    }

    public static DecisionVariableTypeEnum getByCode(Short code){
        if(code == null){
            return null;
        }
        for(DecisionVariableTypeEnum i: DecisionVariableTypeEnum.values()){
            if(i.getCode() == code.intValue()){
                return i;
            }
        }
        return null;
    }
}
