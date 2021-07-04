package org.wangfeng.panda.app.calculation.function;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 字符串转数字的函数
 *
 * 要求：
 *  1、objs中必须有两个参数
 *  2、第一个参数为字符串，第二个参数为精度（int类型）
 *
 * 返回值类型支持：
 *  1、integer
 *  2、double
 */
@Component
public class StrToNumberFunction extends BaseFunction{

    private static final String STR_TO_NUMBER_ERROR_MESSAGE = "字符串转数字的函数异常！";

    @Override
    public Object invoke(Object... objs) {

        //1、校验传入的参数是否有问题
        checkArgsCount(2,STR_TO_NUMBER_ERROR_MESSAGE,objs);

        //2、计算字符串转数字
        try{
            int scale = Integer.valueOf(objs[1].toString());
            BigDecimal number = new BigDecimal(objs[0].toString()).setScale(scale,BigDecimal.ROUND_HALF_UP);
            return number;
        }catch (Exception e){
            throw new RuntimeException(STR_TO_NUMBER_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }

    }


    /**
     * 测试代码
     * @param args
     */
    public static void main(String[] args) {

        Object o1 = "12.213213";
        Object o2 = 1;

        StrToNumberFunction strToNumberFunction = new StrToNumberFunction();
        System.out.println(strToNumberFunction.invoke(o1,o2));
    }
}
