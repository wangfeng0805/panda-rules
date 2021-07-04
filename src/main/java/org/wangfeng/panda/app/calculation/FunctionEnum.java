package org.wangfeng.panda.app.calculation;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public enum FunctionEnum {

    ADD("加上","+","AddFunction","加法","${XXX}+${XXX}","FUNC_ADD"),
    SUBSTRACT("减去","-","SubstractFunction","减法","${XXX}-${XXX}","FUNC_SUBSTRACT"),
    MULTIPLY("乘以","*","MultiplyFunction","乘法","${XXX}*${XXX}","FUNC_MULTIPLY"),
    DIVIDE("除以","/","DivideFunction","除法","${XXX}/${XXX}","FUNC_DIVIDE"),
    REMAINDER("取余","%","RemainderFunction","取余","${XXX}%${XXX}","FUNC_REMAINDER"),
    GREATER_THAN("大于",">","GreaterThanFunction","大于","${XXX}>${XXX}","FUNC_GREATER_THAN"),
    GREATER_THAN_EQUAL("大于等于",">=","GreaterThanEqualFunction","大于等于","${XXX}>=${XXX}","FUNC_GREATER_THAN_EQUAL"),
    LESS_THAN("小于","<","LessThanFunction","小于","${XXX}<${XXX}","FUNC_LESS_THAN"),
    LESS_THAN_EQUAL("小于等于","<=","LessThanEqualFunction","小于等于","${XXX}<=${XXX}","FUNC_LESS_THAN_EQUAL"),
    EQUAL("等于","==","EqualFunction","等于","${XXX}==${XXX}","FUNC_EQUAL"),
    NOT_EQUAL("不等于","!=","NotEqualFunction","不等于","${XXX}!=${XXX}","FUNC_NOT_EQUAL"),
    AFTER("after","after","AfterFunction","日期在另一日期之后","${XXX}>${XXX}","FUNC_AFTER"),
    BEFORE("before","before","BeforeFunction","日期在另一日期之前","${XXX}<${XXX}","FUNC_BEFORE"),




    MAX_FUNCTION("较大值","max","MaxFunction","最大值函数","max ${XXX} ${XXX} ","FUNC_MAX")
    ,MIN_FUNCTION("较小值","min","MinFunction","最小值函数","min ${XXX} ${XXX} ","FUNC_MIN")
    ,POW_FUNCTION("pow","pow","PowFunction","指数计算函数","pow ${XXX} ${XXX} ","FUNC_POW")
    ,ARRAY_SIZE_FUNCTION("arraySize","arraySize","ArraySizeFunction","数组长度函数","arraySize ${XXX} ","FUNC_ARRAY_SIZE")
    ,SQRT_FUNCTION("sqrt","sqrt","SqrtFunction","开方计算函数","sqrt ${XXX} ","FUNC_SQRT")
    ,STR_LENGTH_FUNCTION("strLength","strLength","StrLengthFunction","字符串长度函数","strLength ${XXX} ","FUNC_STR_LENGTH")
    ,IS_IN_ARRAY_FUNCTION("isInArray","isInArray","IsInArrayFunction","判断某个元素是否在集合中","${XXX} isInArray ${XXX}","FUNC_IS_IN_ARRAY")
    ,CEIL_FUNCTION("ceil","ceil","CeilFunction","向上取整函数","ceil ${XXX} ","FUNC_CEIL")
    ,FLOOR_FUNCTION("floor","floor","FloorFunction","向下取整函数","floor ${XXX} ","FUNC_FLOOR")
    ,ROUND_FUNCTION("round","round","RoundFunction","四舍五入函数","round ${XXX} ${XXX} ","FUNC_ROUND")
    ,GET_ELEMENT_FUNCTION("getElement","getElement","GetElementFunction","获取数组中的某个元素的函数","getElement ${XXX} ${XXX} ","FUNC_GET_ELEMENT")
    ,DAY_FUNCTION("day","day","GetDayFunction","返回日期中的日","day ${XXX} ","FUNC_DAY")
    ,MONTH_FUNCTION("month","month","GetMonthFunction","返回日期中的月份","month ${XXX} ","FUNC_MONTH")
    ,YEAR_FUNCTION("year","year","GetYearFunction","返回日期中的年份","year ${XXX} ","FUNC_YEAR")
    ,INDEX_FUNCTION("index","index","IndexFunction","获取字符串y在字符串x中的位置","index ${XXX} ${XXX} ","FUNC_INDEX")
    ,DAYS_BETWEEN_FUNCTION("daysBetween","daysBetween","DaysBetweenFunction","两个日期之间间隔的天数","${XXX} daysBetween ${XXX}","FUNC_DAYS_BETWEEN")
    ,MONTHS_BETWEEN_FUNCTION("monthsBetween","monthsBetween","MonthsBetweenFunction","两个日期之间间隔的月数","${XXX} monthsBetween ${XXX}","FUNC_MONTHS_BETWEEN")
    ,STR_TO_NUMBER_FUNCTION("strToNumber","strToNumber","StrToNumberFunction","字符串转换为integer数据类型","strToNumber ${XXX} ","FUNC_STR_TO_NUMBER")
    ,CONCAT_FUNCTION("concat","concat","ConcatFunction","字符串x，y拼接为新的字符串xy","${XXX} concat ${XXX}","FUNC_CONCAT")
    ,TRIM_FUNCTION("trim","trim","TrimFunction","去除字符串x前后空格","trim ${XXX} ","FUNC_TRIM")
    ,DATE_TO_STRING_FUNCTION("dateToString","dateToString","DateToStringFunction","将日期x转换为格式为y的字符串,y为日期数据可转换为的格式，如yyyyMMdd","dateToString ${XXX} ${XXX} ","FUNC_DATE_TO_STRING")
    ,NUMBER_TO_STRING_FUNCTION("numberToString","numberToString","NumberToStringFunction","将数字x转换为字符串","numberToString ${XXX} ","FUNC_NUMBER_TO_STRING")
    ,REPLACE_ALL_FUNCTION("replaceAll","replaceAll","ReplaceAllFunction","将字符串x中符合y格式的部分全部替换为z，函数返回完成替换的新的字符串","replaceAll ${XXX} ${XXX} ${XXX} ","FUNC_REPLACE_ALL")
    ,REPLACE_FIRST_FUNCTION("replaceFirst","replaceFirst","ReplaceFirstFunction","将字符串x中第一处符合y格式的部分替换为z，函数返回完成替换的新的字符串","replaceFirst ${XXX} ${XXX} ${XXX} ","FUNC_REPLACE_FIRST")
    ,SUBST_LEFT_FUNCTION("substLeft","substLeft","SubstLeftFunction","将字符串x从左边起截取y位，函数返回截取后的新字符串","substLeft ${XXX} ${XXX} ","FUNC_SUBST_LEFT")
    ,SUBST_RIGHT_FUNCTION("substRight","substRight","SubstRightFunction","将字符串x从右边起截取y位，函数返回截取后的新字符串","substRight ${XXX} ${XXX} ","FUNC_SUBST_RIGHT")
    ,SUBST_MID_FUNCTION("substMid","substMid","SubstMidFunction","截取字符串x中从第y位到z位的部分，函数返回截取后的新字符串","substMid ${XXX} ${XXX} ${XXX} ","FUNC_SUBST_MID")
    ,TO_UPPER_CASE_FUNCTION("toUpperCase","toUpperCase","ToUpperCaseFunction","将字符串x中的所有字母转换为大写，函数返回大小写转换完成的新字符串","toUpperCase ${XXX} ","FUNC_TO_UPPER_CASE")
    ,TO_LOWER_CASE_FUNCTION("toLowerCase","toLowerCase","ToLowerCaseFunction","将字符串x中的所有字母转换为小写，函数返回大小写转换完成的新字符串","toLowerCase ${XXX} ","FUNC_TO_LOWER_CASE")
    ,STRING_TO_DATE_FUNCTION("stringToDate","stringToDate","StringToDateFunction","将字符串x转换为格式为y的日期数据","stringToDate ${XXX} ${XXX} ","FUNC_STRING_TO_DATE")
    ,ADD_DAYS_FUNCTION("addDays","addDays","AddDaysFunction","日期天数加法计算函数，函数返回日期x加y天后的日期","addDays ${XXX} ${XXX} ","FUNC_ADD_DAYS")
    ,ADD_TIME_FUNCTION("addTime","addTime","AddTimeFunction","日期时间加法计算函数，返回日期x增加y小时z分钟s秒后的日期","addTime ${XXX} ${XXX} ${XXX} ${XXX} ","FUNC_ADD_TIME")
    ,SPLIT_STR_FUNCTION("splitStr","splitStr","SplitStrFunction","拆分字符串的函数，按照后面的分隔符去拆分字符串，返回一个字符串数组","splitStr ${XXX} ${XXX} ","FUNC_SPLIT_STR")
    ;


    /**
     * 页面展示的名字
     */
    private String functionName;

    /**
     * 后台存储的名字，应当保持唯一
     */
    private String functionCode;

    /**
     * 对应的具体的类的名字
     */
    private String className;

    /**
     * 描述
     */
    private String description;

    /**
     * 格式
     */
    private String functionTemplate;

    /**
     * 1.0版本的函数表达式（待删除）
     */
    private String functionExpression;

    public String getFunctionName() {
        return functionName;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public String getClassName() {
        return className;
    }

    public String getDescription() {
        return description;
    }

    public String getFunctionTemplate() {
        return functionTemplate;
    }

    public String getFunctionExpression() {
        return functionExpression;
    }

    FunctionEnum(String functionName, String functionCode, String className, String description, String functionTemplate, String functionExpression) {
        this.functionName = functionName;
        this.functionCode = functionCode;
        this.className = className;
        this.description = description;
        this.functionTemplate = functionTemplate;
        this.functionExpression = functionExpression;
    }

    /**
     * 通过functionName获取对应的枚举
     * @param functionName
     * @return
     */
    public static FunctionEnum getByFunctionName(String functionName){
        if(functionName == null){
            return null;
        }
        for(FunctionEnum i: FunctionEnum.values()){
            if(i.getFunctionName().equals(functionName)){
                return i;
            }
        }
        return null;
    }

    /**
     * 通过functionCode获取对应的枚举
     * @param showName
     * @return
     */
    public static FunctionEnum getByFunctionCode(String functionCode){
        if(functionCode == null){
            return null;
        }
        for(FunctionEnum i: FunctionEnum.values()){
            if(i.getFunctionCode().equals(functionCode)){
                return i;
            }
        }
        return null;
    }


    /**
     * 通过functionExpression获取对应的枚举
     * @param functionExpression
     * @return
     */
    public static FunctionEnum getByFunctionExpression(String functionExpression){
        if(functionExpression == null){
            return null;
        }
        for(FunctionEnum i: FunctionEnum.values()){
            if(i.getFunctionExpression().equals(functionExpression)){
                return i;
            }
        }
        return null;
    }


    /**
     * 获取所有的枚举类
     * @return
     */
    public static List<JSONObject> queryAllFunctionEnum(){
        List<JSONObject> functionList = new ArrayList();
        for(FunctionEnum i: FunctionEnum.values()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("functionCode",i.getFunctionCode());
            jsonObject.put("functionName",i.getFunctionName());
            jsonObject.put("className",i.getClassName());
            jsonObject.put("description",i.getDescription());
            jsonObject.put("functionTemplate",i.getFunctionTemplate());
            functionList.add(jsonObject);
        }
        return functionList;

    }

    /**
     * 单个枚举转化为json
     * @param functionEnum
     * @return
     */
    public static JSONObject functionEnumToJSON(FunctionEnum functionEnum){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("functionCode",functionEnum.getFunctionCode());
        jsonObject.put("functionName",functionEnum.getFunctionName());
        jsonObject.put("functionTemplate",functionEnum.getFunctionTemplate());
        jsonObject.put("description",functionEnum.getDescription());
        jsonObject.put("className",functionEnum.getClassName());
        return jsonObject;
    }


}
