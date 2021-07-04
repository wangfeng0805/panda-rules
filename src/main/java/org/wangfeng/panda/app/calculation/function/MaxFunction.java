package org.wangfeng.panda.app.calculation.function;

import org.springframework.stereotype.Component;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.util.NumberUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 最大值计算函数
 *
 * 要求：
 *  1、objs的长度必须为1个，且为数组类型，并且数组中的所有元素都是数值型
 *
 * 支持：
 *  1、integer
 *  2、double
 */
@Component
public class MaxFunction extends BaseFunction {

    private static final String MAX_ERROR_MESSAGE = "最大值计算函数异常！";

    @Override
    public Object invoke(Object... objs) {


        //1、拼装数组
        List<Object> list = Arrays.asList(objs);

        //3、拼接表达式
        StringBuffer expression = new StringBuffer();
        expression.append("Math.max(");
        for(Object o : list){
            if(!NumberUtils.isCalculationNumber(o)){
                throw new RuleRuntimeException(MAX_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
            }
            expression.append(o.toString()).append(",");
        }
        expression = expression.delete(expression.length()-1,expression.length());
        expression.append(")");

        //4、计算并返回结果
        return calculate(expression.toString(),MAX_ERROR_MESSAGE);
    }


    /**
     * 测试代码
     * @param args
     */
    public static void main(String[] args) {


        List o1 = new ArrayList<>();
        o1.add(1.1);
        o1.add(5);
        o1.add(1231);
        o1.add(412.1);
        o1.add("12ad");

        MaxFunction maxFunction = new MaxFunction();
        System.out.println(maxFunction.invoke(o1));


    }

}
