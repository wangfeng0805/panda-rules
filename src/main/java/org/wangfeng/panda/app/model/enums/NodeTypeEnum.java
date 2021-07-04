package org.wangfeng.panda.app.model.enums;

public enum  NodeTypeEnum {


    GUI_ZE((short)1,"规则"),
    GUI_ZE_JI((short)2,"规则集"),
    TIAO_JIAN((short)3,"条件"),
    KAI_SHI((short)4,"开始");

    private Short code;
    private String name;

    public Short getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    NodeTypeEnum(Short code, String name) {
        this.code = code;
        this.name = name;
    }

    public static NodeTypeEnum getByCode(Short code){
        if(code == null){
            return null;
        }
        for(NodeTypeEnum i: NodeTypeEnum.values()){
            if(i.getCode() == code.intValue()){
                return i;
            }
        }
        return null;
    }



}
