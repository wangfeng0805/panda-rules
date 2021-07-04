package org.wangfeng.panda.app.calculation.function;

import org.springframework.stereotype.Component;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.util.NumberUtils;

import java.math.BigDecimal;

/**
 * 开方计算函数
 *
 * 要求：
 *  1、所有的参数都是数值型
 *  2、objs的长度必须只有一个
 *
 * 返回值类型支持：
 *  1、integer
 *  2、double
 */
@Component
public class SqrtFunction extends BaseFunction {

    private static final String SQRT_ERROR_MESSAGE = "开方计算函数异常！";

    @Override
    public Object invoke(Object... objs) {

        //1、校验传入的参数个数是否有问题
        checkArgsCount(1,SQRT_ERROR_MESSAGE,objs);

        //2、拼接表达式
        StringBuffer expression = new StringBuffer();
        expression.append("Math.sqrt(");
        if(!NumberUtils.isCalculationNumber(objs[0]) || !NumberUtils.NonNegative(new BigDecimal(objs[0].toString()))){
            throw new RuleRuntimeException(SQRT_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }
        expression.append(objs[0].toString()).append(")");

        //3、计算结果
        return calculate(expression.toString(),SQRT_ERROR_MESSAGE);
    }


    /**
     * 测试代码
     * @param args
     */
    public static void main(String[] args) {
        Object o1 = 25;

        SqrtFunction sqrtFunction = new SqrtFunction();
        System.out.println(sqrtFunction.invoke(o1));
    }

}
