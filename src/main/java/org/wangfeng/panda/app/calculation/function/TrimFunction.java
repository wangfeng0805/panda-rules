package org.wangfeng.panda.app.calculation.function;

import org.springframework.stereotype.Component;

/**
 * 字符串去除空格
 *
 * 说明：
 *  1、去除字符串x前后空格
 *
 * 要求：
 *  1、x为字符串类型，否则页面提示“输入信息类型不匹配”
 *
 * 格式：
 *  1、trim(x)
 *
 * 返回值类型支持：
 *  1、string
 */
@Component
public class TrimFunction extends BaseFunction{

    private static final String TRIM_ERROR_MESSAGE = "字符串去除两端空格函数异常！";


    @Override
    public Object invoke(Object... objs) {

        //1、校验传入的参数个数是否有问题
        checkArgsCount(1, TRIM_ERROR_MESSAGE, objs);

        //2、处理传入的参数
        String str = checkString(objs[0].toString(),TRIM_ERROR_MESSAGE);

        //3 计算并返回结果
        return new StringBuffer().append(str.trim()).toString();

    }


    /**
     * 测试代码
     * @param args
     */
    public static void main(String[] args) {
        Object str1 = "str1";
        Object str2 = "  str2";
        Object str3 = "str3  ";
        Object str4 = "  str4  ";

        Object str5 = "\"  str5  ";
        Object str6 = "  str6  \"";
        Object str7 = "\"a  str7  \"";


        TrimFunction trimFunction = new TrimFunction();

        System.out.println(trimFunction.invoke(str1));
        System.out.println(trimFunction.invoke(str2));
        System.out.println(trimFunction.invoke(str3));
        System.out.println(trimFunction.invoke(str4));
        System.out.println(trimFunction.invoke(str5));
        System.out.println(trimFunction.invoke(str6));
        System.out.println(trimFunction.invoke(str7));


    }


}
