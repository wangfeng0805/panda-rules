package org.wangfeng.panda.app.calculation.function;

import org.springframework.stereotype.Component;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;

/**
 * 比较不相等函数
 *
 * 说明：
 *  1、x是否不等于y
 *
 * 要求：
 *  1、x，y均为数字，可以为浮点数、整数类型；否则页面提示“输入信息类型不匹配”
 *
 * 格式：
 *  1、x <> y
 *
 * 返回值类型支持：
 *  1、integer
 *  2、double
 */
@Component
public class NotEqualFunction extends BaseFunction {

    private static final String NOT_EQYUAL_ERROR_MESSAGE = "比较不相等函数异常！";


    @Override
    public Object invoke(Object... objs) {

        //1、校验传入的参数个数是否有问题
        checkArgsCount(2, NOT_EQYUAL_ERROR_MESSAGE, objs);

        //2、校验传入参数的格式是否正确
        if(objs[0]==null|| objs[1]==null){
            throw new RuleRuntimeException(NOT_EQYUAL_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }
//        if(!NumberUtils.isCalculationNumber(objs[0])||!NumberUtils.isCalculationNumber(objs[1])){
//            throw new RuleRuntimeException(EQUAL_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
//        }

        //3、计算并返回结果
        try{
            String obj1 = checkString(objs[0].toString(),NOT_EQYUAL_ERROR_MESSAGE);
            String obj2 = checkString(objs[1].toString(),NOT_EQYUAL_ERROR_MESSAGE);
            return !obj1.equals(obj2);
        }catch (RuleRuntimeException e){
            throw new RuleRuntimeException(NOT_EQYUAL_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }
    }


    /**
     * 测试代码
     * @param args
     */
    public static void main(String[] args) {

        Object o1 = 12;
        Object o2 = 3.2;

        NotEqualFunction notEqualFunction = new NotEqualFunction();
        System.out.println(notEqualFunction.invoke(o1,o2));
    }





}
