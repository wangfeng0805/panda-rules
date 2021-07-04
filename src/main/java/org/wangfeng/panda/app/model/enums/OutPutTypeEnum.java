package org.wangfeng.panda.app.model.enums;


import com.alibaba.fastjson.JSONObject;
import org.wangfeng.panda.app.calculation.FunctionEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * 输出类型枚举类（1-字符串；2-整数；3-布尔；4-浮点数；5-日期；6-时间；7-数组；8-枚举值）
 */
public enum OutPutTypeEnum {
    //对于字符串的函数，因为目前来说为了区分数字1和字符串1，我们进行了字符串加上双引号的特殊处理，
    //但是这样对于部分函数会有问题，所以对于字符串操作的函数需要进行先出去双引号计算的逻辑
    STRING((short)1,"字符串",new ArrayList<FunctionEnum>(){{
        add(FunctionEnum.CONCAT_FUNCTION);
        add(FunctionEnum.TRIM_FUNCTION);
        add(FunctionEnum.DATE_TO_STRING_FUNCTION);
        add(FunctionEnum.NUMBER_TO_STRING_FUNCTION);
        add(FunctionEnum.GET_ELEMENT_FUNCTION);
        add(FunctionEnum.REPLACE_ALL_FUNCTION);
        add(FunctionEnum.REPLACE_FIRST_FUNCTION);
        add(FunctionEnum.SUBST_LEFT_FUNCTION);
        add(FunctionEnum.SUBST_RIGHT_FUNCTION);
        add(FunctionEnum.SUBST_MID_FUNCTION);
        add(FunctionEnum.TO_UPPER_CASE_FUNCTION);
        add(FunctionEnum.TO_LOWER_CASE_FUNCTION);
    }}),

    INTEGER((short)2,"整数",new ArrayList<FunctionEnum>(){{
        add(FunctionEnum.ADD);
        add(FunctionEnum.SUBSTRACT);
        add(FunctionEnum.MULTIPLY);
        add(FunctionEnum.DIVIDE);
        add(FunctionEnum.REMAINDER);
        add(FunctionEnum.POW_FUNCTION);
        add(FunctionEnum.SQRT_FUNCTION);
        add(FunctionEnum.ROUND_FUNCTION);
        add(FunctionEnum.STR_TO_NUMBER_FUNCTION);
        add(FunctionEnum.MAX_FUNCTION);
        add(FunctionEnum.MIN_FUNCTION);
        add(FunctionEnum.GET_ELEMENT_FUNCTION);
        add(FunctionEnum.ARRAY_SIZE_FUNCTION);
        add(FunctionEnum.CEIL_FUNCTION);
        add(FunctionEnum.FLOOR_FUNCTION);
        add(FunctionEnum.DAY_FUNCTION);
        add(FunctionEnum.MONTH_FUNCTION);
        add(FunctionEnum.YEAR_FUNCTION);
        add(FunctionEnum.INDEX_FUNCTION);
        add(FunctionEnum.DAYS_BETWEEN_FUNCTION);
        add(FunctionEnum.MONTHS_BETWEEN_FUNCTION);
        add(FunctionEnum.STR_LENGTH_FUNCTION);
    }}),

    BOOLEAN((short)3,"布尔",new ArrayList<FunctionEnum>(){{
        add(FunctionEnum.GREATER_THAN);
        add(FunctionEnum.GREATER_THAN_EQUAL);
        add(FunctionEnum.LESS_THAN);
        add(FunctionEnum.LESS_THAN_EQUAL);
        add(FunctionEnum.EQUAL);
        add(FunctionEnum.NOT_EQUAL);
        add(FunctionEnum.AFTER);
        add(FunctionEnum.BEFORE);
        add(FunctionEnum.IS_IN_ARRAY_FUNCTION);
    }}),

    DOUBLE((short)4,"浮点数",new ArrayList<FunctionEnum>(){{
        add(FunctionEnum.ADD);
        add(FunctionEnum.SUBSTRACT);
        add(FunctionEnum.MULTIPLY);
        add(FunctionEnum.DIVIDE);
        add(FunctionEnum.REMAINDER);
        add(FunctionEnum.POW_FUNCTION);
        add(FunctionEnum.SQRT_FUNCTION);
        add(FunctionEnum.ROUND_FUNCTION);
        add(FunctionEnum.STR_TO_NUMBER_FUNCTION);
        add(FunctionEnum.MAX_FUNCTION);
        add(FunctionEnum.MIN_FUNCTION);
        add(FunctionEnum.GET_ELEMENT_FUNCTION);
    }}),

    DATE((short)5,"日期",new ArrayList<FunctionEnum>(){{
        add(FunctionEnum.GET_ELEMENT_FUNCTION);
        add(FunctionEnum.STRING_TO_DATE_FUNCTION);
        add(FunctionEnum.ADD_DAYS_FUNCTION);
        add(FunctionEnum.ADD_TIME_FUNCTION);
    }}),

    TIME((short)6,"时间",new ArrayList<FunctionEnum>(){{
        add(FunctionEnum.GET_ELEMENT_FUNCTION);
    }}),

    LIST((short)7,"数组",new ArrayList<FunctionEnum>(){{
        add(FunctionEnum.SPLIT_STR_FUNCTION);
    }})
    ;

    private Short code;
    private String name;
    /**
     * 可以接受的运算符
     */
    private List<FunctionEnum> functionList;


    public Short getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public List<FunctionEnum> getFunctionList() {
        return functionList;
    }


    OutPutTypeEnum(Short code, String name, List<FunctionEnum> functionList) {
        this.code = code;
        this.name = name;
        this.functionList = functionList;
    }

    /**
     * 通过Code查询
     * @param code
     * @return
     */
    public static OutPutTypeEnum getByCode(Short code){
        if(code == null){
            return null;
        }
        for(OutPutTypeEnum i: OutPutTypeEnum.values()){
            if(i.getCode() == code.intValue()){
                return i;
            }
        }
        return null;
    }


    /**
     * 查询所有的规则输出类型
     * @return
     */
    public static List<JSONObject> queryAllOutPutType(){
        List<JSONObject> outPutTypeList = new ArrayList();
        for(OutPutTypeEnum i: OutPutTypeEnum.values()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code",i.getCode());
            jsonObject.put("name",i.getName());

            List<JSONObject> functionEnumList = new ArrayList<>();
            i.getFunctionList().stream().forEach(functionEnum -> {
                JSONObject json =  FunctionEnum.functionEnumToJSON(functionEnum);
                functionEnumList.add(json);
            });

            jsonObject.put("functionList",functionEnumList);
            outPutTypeList.add(jsonObject);
        }
        return outPutTypeList;
    }






}
