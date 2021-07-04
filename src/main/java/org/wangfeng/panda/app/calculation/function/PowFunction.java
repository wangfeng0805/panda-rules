package org.wangfeng.panda.app.calculation.function;

import org.springframework.stereotype.Component;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.util.NumberUtils;

import java.math.BigDecimal;

/**
 * 指数计算函数
 *
 * 要求：
 *  1、objs中必须有两个参数，第一个是底数，第二个是指数
 *  2、第一个参数为非负数，第二个参数为数字
 *
 * 返回值类型支持：
 *  1、integer
 *  2、double
 */
@Component
public class PowFunction extends BaseFunction{

    private static final String POW_ERROR_MESSAGE = "指数计算函数异常！";

    @Override
    public Object invoke(Object... objs) {

        //1、校验传入的参数个数是否有问题
        checkArgsCount(2,POW_ERROR_MESSAGE,objs);

        //2、拼接表达式
        StringBuffer expression = new StringBuffer();
        expression.append("Math.pow(");

        //2.1 拼接第一个参数
        if(!NumberUtils.isCalculationNumber(objs[0]) || !NumberUtils.NonNegative(new BigDecimal(objs[0].toString()))){
            throw new RuleRuntimeException(POW_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }
        expression.append(objs[0].toString()).append(",");

        //2.2 拼接第二个参数
        if(!NumberUtils.isCalculationNumber(objs[1])){
            throw new RuleRuntimeException(POW_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }
        expression.append(objs[1].toString()).append(")");

        //3 计算结果
        return calculate(expression.toString(),POW_ERROR_MESSAGE);
    }


    /**
     * 测试代码
     * @param args
     */
    public static void main(String[] args) {

        Object o1 = 3;
        Object o2 = 2;

        PowFunction powFunction = new PowFunction();
        System.out.println(powFunction.invoke(o1,o2));

    }
}
