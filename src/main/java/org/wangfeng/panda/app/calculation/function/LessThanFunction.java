package org.wangfeng.panda.app.calculation.function;

import org.springframework.stereotype.Component;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.util.NumberUtils;

/**
 * 小于函数
 *
 * 说明：
 *  1、x是否小于y
 *
 * 要求：
 *  1、x，y为整数或者浮点数中的任意一种类型，否则页面提示“输入信息类型不匹配”
 *
 * 格式：
 *  1、x < y
 *
 * 返回值类型支持：
 *  1、integer
 *  2、double
 */
@Component
public class LessThanFunction extends BaseFunction {

    private static final String LESS_THAN_ERROR_MESSAGE = "小于函数异常！";


    @Override
    public Object invoke(Object... objs) {

        //1、校验传入的参数个数是否有问题
        checkArgsCount(2, LESS_THAN_ERROR_MESSAGE, objs);

        if("".equals(objs[0])){
            objs[0] = 0;
        }
        if("".equals(objs[1])){
            objs[1] = 0;
        }

        //2、校验传入参数的格式是否正确
//        if(!NumberUtils.isCalculationNumber(objs[0])||!NumberUtils.isCalculationNumber(objs[1])){
//            throw new RuleRuntimeException(LESS_THAN_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
//        }

        //3、计算并返回结果
        try {
            return NumberUtils.toNumber(objs[0]).compareTo(NumberUtils.toNumber(objs[1])) == -1;
        }catch (Exception e){
            throw new RuleRuntimeException(LESS_THAN_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }
    }


    /**
     * 测试代码
     * @param args
     */
    public static void main(String[] args) {

        Object o1 = 12;
        Object o2 = 3.2;

        LessThanFunction lessThanFunction = new LessThanFunction();
        System.out.println(lessThanFunction.invoke(o1,o2));
    }





}
