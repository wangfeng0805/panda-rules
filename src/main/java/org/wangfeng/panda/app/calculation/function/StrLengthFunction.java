package org.wangfeng.panda.app.calculation.function;

import org.springframework.stereotype.Component;

/**
 * 字符串长度函数
 *
 * 要求：
 *  1、objs的长度必须只有一个，并且是字符串类型
 *
 * 支持：
 *  1、integer
 *
 */
@Component
public class StrLengthFunction extends BaseFunction {

    private static final String STR_LENGTH_ERROR_MESSAGE = "字符串长度函数异常！";

    @Override
    public Object invoke(Object... objs) {

        //1、校验传入的参数是否有问题
        checkArgsCount(1,STR_LENGTH_ERROR_MESSAGE,objs);

        //2、计算并返回结果
        return objs[0].toString().length();
    }


    /**
     * 测试代码
     * @param args
     */
    public static void main(String[] args) {

        Object o1 = "asdasdsad";

        StrLengthFunction strLengthFunction = new StrLengthFunction();
        System.out.println(strLengthFunction.invoke(o1));
    }

}
