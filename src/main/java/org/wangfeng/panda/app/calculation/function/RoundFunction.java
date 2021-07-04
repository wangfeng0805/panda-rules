package org.wangfeng.panda.app.calculation.function;

import org.springframework.stereotype.Component;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.util.NumberUtils;

/**
 * 四舍五入计算函数
 *
 * 要求：
 *  1、objs中必须有两个参数
 *  2、第一个参数为数字，第二个参数为保留的小数位数
 *
 * 返回值类型支持：
 *  1、integer
 *  2、double
 */
@Component
public class RoundFunction extends BaseFunction {

    private static final String ROUND_ERROR_MESSAGE = "四舍五入计算函数异常！";

    @Override
    public Object invoke(Object... objs) {

        //1、校验传入的参数个数是否有问题
        checkArgsCount(2,ROUND_ERROR_MESSAGE,objs);

        //2、校验第一个参数是否是数值型，第二个参数是否是非负整数
        if(!NumberUtils.isCalculationNumber(objs[0]) || !NumberUtils.NonNegativeInteger(Double.valueOf(objs[1].toString()))){
            throw new RuleRuntimeException(ROUND_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }

        //3、拼接表达式
        double precision = Math.pow(10d,Double.parseDouble(objs[1].toString()));
        StringBuffer expression = new StringBuffer();
        expression.append("Math.round(");
        expression.append(objs[0].toString()).append("*").append(precision).append(")/").append(precision);

        //4、计算结果
        return calculate(expression.toString(),ROUND_ERROR_MESSAGE);
    }


    public static void main(String[] args) {

        Object o1 = 2.12321321;
        Object o2 = 3;

        RoundFunction roundFunction = new RoundFunction();
        System.out.println(roundFunction.invoke(o1,o2));
    }




}
