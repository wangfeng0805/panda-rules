package org.wangfeng.panda.app.service.redis;

/**
 * redis 中存入的类型枚举
 */
public enum RedisInputEnum {


    SINGLE_RULE((short)1,"single_rule"),
    RULE_LIST((short)2,"rule_list"),
    RULE_TREE((short)3,"rule_tree"),
    DECISION_VARIABLE((short)4,"decision_variable"),
    CELL_FUNCTION((short)5,"cell_function"),
    CELL_VARIABLE((short)6,"cell_variable"),
    RULE_LINE((short)7,"rule_line");

    private Short code;

    private String name;

    public Short getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    RedisInputEnum(Short code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 通过Code查询
     * @param code
     * @return
     */
    public static RedisInputEnum getByCode(Short code){
        if(code == null){
            return null;
        }
        for(RedisInputEnum i: RedisInputEnum.values()){
            if(i.getCode() == code.intValue()){
                return i;
            }
        }
        return null;
    }
}
