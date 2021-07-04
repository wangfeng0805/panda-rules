package org.wangfeng.panda.app.calculation.function;


import org.springframework.stereotype.Component;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.util.NumberUtils;

/**
 * 数字转字符串函数
 *
 * 说明：
 *  1、将数字x转换为字符串
 *
 * 要求：
 *  1、x为数字类型，否则页面提示“输入信息类型不匹配”
 *
 * 格式：
 *  1、numberToString(x)
 *
 * 返回值类型支持：
 *  1、string
 */
@Component
public class NumberToStringFunction extends BaseFunction{

    private static final String NUMBER_TO_STRING_ERROR_MESSAGE = "数字转字符串函数异常！";

    @Override
    public Object invoke(Object... objs) {

        //1、校验传入的参数个数是否有问题
        checkArgsCount(1, NUMBER_TO_STRING_ERROR_MESSAGE, objs);

        //2、校验传入参数的格式是否正确
        if(!NumberUtils.isCalculationNumber(objs[0])){
            throw new RuleRuntimeException(NUMBER_TO_STRING_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }

        //3、计算并返回结果
        return new StringBuffer().append(objs[0].toString()).toString();
    }


    /**
     * 测试代码
     * @param args
     */
    public static void main(String[] args) {

        Object o1 = "1";
        Object o2 = 2;
        Object o3 = 3.1;
        Object o4 = 5.2d;




        NumberToStringFunction numberToStringFunction = new NumberToStringFunction();
        System.out.println(numberToStringFunction.invoke(o1));
        System.out.println(numberToStringFunction.invoke(o2));
        System.out.println(numberToStringFunction.invoke(o3));
        System.out.println(numberToStringFunction.invoke(o4));

    }



}
