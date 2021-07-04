package org.wangfeng.panda.app.calculation.function;

import org.springframework.stereotype.Component;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.util.NumberUtils;

/**
 * 向上取整函数
 *
 * 要求：
 *  1、所有的参数都是数值型
 *  2、objs的长度必须只有一个
 *
 * 返回值类型支持：
 *  1、integer
 */
@Component
public class CeilFunction extends BaseFunction{

    private static final String CEIL_ERROR_MESSAGE = "向上取整函数异常！";


    @Override
    public Object invoke(Object... objs) {

        //1、校验传入的参数个数是否有问题
        checkArgsCount(1,CEIL_ERROR_MESSAGE,objs);

        //2、拼接表达式
        StringBuffer expression = new StringBuffer();
        expression.append("Math.ceil(");
        if(!NumberUtils.isCalculationNumber(objs[0])){
            throw new RuleRuntimeException(CEIL_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }
        expression.append(objs[0].toString()).append(")");

        //3 计算结果
        return calculate(expression.toString(),CEIL_ERROR_MESSAGE);
    }



    /**
     * 测试代码
     * @param args
     */
    public static void main(String[] args) {

        Object o1 = 1.213;
        Object o2 = -1.213;


        CeilFunction ceilFunction = new CeilFunction();
        System.out.println(ceilFunction.invoke(o1));
        System.out.println(ceilFunction.invoke(o2));

    }

}
