package org.wangfeng.panda.app.calculation.function;

import org.springframework.stereotype.Component;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.util.NumberUtils;
import org.wangfeng.panda.app.util.StringUtils;

import java.math.RoundingMode;


/**
 * 除法函数
 *
 * 说明：
 *  1、除法计算，计算x与y的乘积
 *
 * 要求：
 *  1、x，y均为数字，可以为浮点数、整数类型；否则页面提示“输入信息类型不匹配”
 *
 * 格式：
 *  1、x*y
 *
 * 返回值类型支持：
 *  1、integer
 *  2、double
 */
@Component
public class DivideFunction extends BaseFunction {

    private static final String DIVIDE_ERROR_MESSAGE = "除法函数异常！";


    @Override
    public Object invoke(Object... objs) {

        //1、校验传入的参数个数是否有问题
        checkArgsCount(2, DIVIDE_ERROR_MESSAGE, objs);

        if(StringUtils.isBlank(objs[0].toString())){
            objs[0] = 0;
        }

        //2、校验传入参数的格式是否正确
//        if(!NumberUtils.isCalculationNumber(objs[0])||!NumberUtils.isCalculationNumber(objs[1])){
//            throw new RuleRuntimeException(DIVIDE_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
//        }

        //3、计算并返回结果
        try {
            return NumberUtils.toNumber(objs[0]).divide(NumberUtils.toNumber(objs[1]),2, RoundingMode.HALF_UP).stripTrailingZeros();
        }catch (Exception e){
            throw new RuleRuntimeException(DIVIDE_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }

    }


    /**
     * 测试代码
     * @param args
     */
    public static void main(String[] args) {

        Object o1 = 12;
        Object o2 = 3.2;

        DivideFunction divideFunction = new DivideFunction();
        System.out.println(divideFunction.invoke(o1,o2));
    }





}
