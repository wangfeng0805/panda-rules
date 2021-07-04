package org.wangfeng.panda.app.calculation.function;


import org.springframework.stereotype.Component;

/**
 * 字符串拼接函数
 *
 * 说明：
 *  1、字符串x，y拼接为新的字符串xy
 *
 * 要求：
 *  1、x，y均为字符串类型，否则页面提示“输入信息类型不匹配”
 *
 * 格式：
 *  1、x concat y
 *
 * 返回值类型支持：
 *  1、string
 */
@Component
public class ConcatFunction extends BaseFunction{

    private static final String CONCAT_ERROR_MESSAGE = "字符串拼接函数异常！";

    @Override
    public Object invoke(Object... objs) {

        //1、校验传入的参数个数是否有问题
        checkArgsCount(2, CONCAT_ERROR_MESSAGE, objs);

        //2、处理传入的参数
        String startStr = checkString(objs[0].toString(),CONCAT_ERROR_MESSAGE);
        String endStr = checkString(objs[1].toString(),CONCAT_ERROR_MESSAGE);

        //3 计算并返回结果
        return new StringBuffer().append(startStr).append(endStr).toString();
    }


    /**
     * 测试代码
     * @param args
     */
    public static void main(String[] args) {
        String str1 = "str1";
        String str2 = "\"str2";
        String str3 = "str3\"";
        String str4 = "\"str4\"";

        ConcatFunction concatFunction = new ConcatFunction();

        System.out.println(concatFunction.invoke(str1,str2));
        System.out.println(concatFunction.invoke(str1,str3));
        System.out.println(concatFunction.invoke(str1,str4));
        System.out.println(concatFunction.invoke(str2,str3));
        System.out.println(concatFunction.invoke(str2,str4));
        System.out.println(concatFunction.invoke(str3,str4));

    }




}
