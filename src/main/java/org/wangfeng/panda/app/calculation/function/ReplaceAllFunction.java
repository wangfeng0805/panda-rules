package org.wangfeng.panda.app.calculation.function;

import org.springframework.stereotype.Component;

/**
 * 字符串全量替换函数
 *
 * 说明：
 *  1、将字符串x中符合y格式的部分全部替换为z，函数返回完成替换的新的字符串
 *
 * 要求：
 *  1、x，y，z均为字符串类型，否则页面提示“输入信息类型不匹配”
 *
 * 格式：
 *  1、replaceAll(x,y,z)
 *
 * 返回值类型支持：
 *  1、string
 */
@Component
public class ReplaceAllFunction extends BaseFunction {

    private static final String REPLACE_ALL_ERROR_MESSAGE = "字符串全量替换函数异常！";

    @Override
    public Object invoke(Object... objs) {

        //1、校验传入的参数个数是否有问题
        checkArgsCount(3, REPLACE_ALL_ERROR_MESSAGE, objs);

        //2、校验传入参数的类型是否有问题
        String totalStr = checkString(objs[0].toString(),REPLACE_ALL_ERROR_MESSAGE);
        String originStr = checkString(objs[1].toString(),REPLACE_ALL_ERROR_MESSAGE);
        String replaceStr = checkString(objs[2].toString(),REPLACE_ALL_ERROR_MESSAGE);

        //3、计算并返回结果
        return new StringBuffer().append(totalStr.replaceAll(originStr,replaceStr)).toString();

    }


    /**
     * 测试代码
     * @param args
     */
    public static void main(String[] args) {

        Object o1 = "\"asdfasdf";
        Object o2 = "s\"";
        Object o3 = "\"666\"";

        ReplaceAllFunction replaceAllFunction = new ReplaceAllFunction();
        System.out.println(replaceAllFunction.invoke(o1,o2,o3));

    }




}
