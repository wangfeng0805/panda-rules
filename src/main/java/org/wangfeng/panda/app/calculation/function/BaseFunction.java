package org.wangfeng.panda.app.calculation.function;

import lombok.extern.slf4j.Slf4j;
import org.wangfeng.panda.app.calculation.JSEngineCalculation;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;

@Slf4j
public abstract class BaseFunction {


    protected static final String IMPORT_UNITE_ERROR_MESSAGE = "输入信息类型不匹配！";

    private static final String CHECK_ARGS_COUNT_ERROR_MESSAGE = "输入参数数量不正确！";

    private static final String CALCULATE_ERROR_MESSAGE = "计算结果异常！";

    private static final String STRING_FORMAT_ERROR_MESSAGE = "字符串处理异常！";

    /**
     * 计算方法
     * @param objs
     * @return
     */
    public Object invoke(Object... objs){
        return null;
    }

    /**
     * 判断传入参数的个数是否正确，这里强制规定传参数量
     */
    protected void checkArgsCount(Integer count , String errorMessage , Object... objs){
        if(objs==null || objs.length!=count){
            throw new RuleRuntimeException(errorMessage+CHECK_ARGS_COUNT_ERROR_MESSAGE);
        }
        for(Object o : objs){
            if(o == null){
                throw new RuleRuntimeException(errorMessage+CHECK_ARGS_COUNT_ERROR_MESSAGE);
            }
        }


    }

    /**
     * 去除字符串两边的双引号
     * @param str
     * @param errorMessage
     */
    protected String checkString(String str , String errorMessage){
        if(str==null){
            throw new RuleRuntimeException(errorMessage + STRING_FORMAT_ERROR_MESSAGE);
        }

        if(str.startsWith("\"")){
            str = str.substring(1,str.length());
        }
        if(str.endsWith("\"")){
            str = str.substring(0,str.length()-1);
        }

        return str;
    }


    /**
     * 计算
     * @param expression
     * @return
     */
    protected Object calculate(String expression , String errorMessage){

        //计算结果并返回
        try {
            return JSEngineCalculation.jsCalculate(expression);
        } catch (Exception ex) {
            log.error("计算错误！"+ex.getMessage(),ex);
            throw new RuleRuntimeException(errorMessage+CALCULATE_ERROR_MESSAGE);
        }
    }
}
