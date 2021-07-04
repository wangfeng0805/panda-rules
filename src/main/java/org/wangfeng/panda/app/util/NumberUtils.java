package org.wangfeng.panda.app.util;

import org.wangfeng.panda.app.calculation.JSEngineCalculation;
import org.wangfeng.panda.app.common.Constants;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtils {

    private static Pattern DOUBLE_PATTERN = Pattern.compile("^-?[0-9]+(\\.[0-9]+)?$");


    /**
     * 把一个对象转成数字
     * @param obj
     * @return
     */
    public static BigDecimal toNumber(Object obj){
        if(obj == null){
            throw new RuleRuntimeException("该对象不是数字！");
        }
        try {
            Double d ;
            if (isNumber(obj)) {
                d = Double.parseDouble(obj.toString());
            }else {
                d = Double.parseDouble(JSEngineCalculation.jsCalculate(obj.toString()).toString());
            }

            if(d%1.0==0){
                return new BigDecimal(d/1.0);
            }
            return new BigDecimal(d);

        }catch (Exception e) {
            throw new RuleRuntimeException("该对象不是数字！");
        }
    }


    /**
     * 判断一个一个对象是否是数字
     * @param obj
     * @return
     */
    public static Boolean isNumber(Object obj){
        if (obj == null){
            return false;
        }

        if (obj instanceof Number) {
            return true;
        }else if (obj instanceof String){
            Matcher isNum = DOUBLE_PATTERN.matcher(obj.toString());
            return isNum.matches();
        }
        return false;
    }



    /**
     * 判断一个一个对象是否是数字或者计算是否是数字
     * @param obj
     * @return
     */
    public static Boolean isCalculationNumber (Object obj) {

        if(obj==null){
            return false;
        }
        return isNumber(obj) || isNumber(JSEngineCalculation.jsCalculate(obj.toString()).toString());
    }


    /**
     * 判断一个数字是否非负
     * @return
     */
    public static Boolean NonNegative(BigDecimal obj){
        if(obj==null){
            return false;
        }

        return obj.compareTo(new BigDecimal(0))!=-1;
    }

    /**
     * 判断一个数字是否非负整数
     * @return
     */
    public static Boolean NonNegativeInteger(Double obj){
        if(obj == null){
            return false;
        }
        if(Constants.DOUBLE_ZERO.equals(obj%1) && obj >=0){
            return true;
        }
        return false;
    }

    /**
     * 判断一个数字是否为整数
     * @return
     */
    public static Boolean IsInteger(Double obj){
        if(obj == null){
            return false;
        }
        if(Constants.DOUBLE_ZERO.equals(obj%1)||Constants.NEGATIVE_DOUBLE_ZERO.equals(obj%1)){
            return true;
        }
        return false;
    }
}
