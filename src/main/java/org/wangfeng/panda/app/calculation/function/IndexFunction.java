package org.wangfeng.panda.app.calculation.function;

import org.springframework.stereotype.Component;

/**
 * 获取字符串的位置函数
 *
 * 要求：
 *  1、获取字符串y在字符串x中的位置，仅返回y第一次出现的位置，返回值从1开始，未找到结果返回0
 *  2、objs的长度必须有两个，第一个是原始字符串，第二个是需要匹配的字符串
 *
 * 返回值类型支持：
 *  1、integer
 */
@Component
public class IndexFunction extends BaseFunction {

    private static final String INDEX_ERROR_MESSAGE = "获取字符串的位置函数异常！";

    @Override
    public Object invoke(Object... objs) {
        //1、校验传入的参数是否有问题
        checkArgsCount(2,INDEX_ERROR_MESSAGE,objs);
        String string = checkString(objs[0].toString(),INDEX_ERROR_MESSAGE);
        String indexStr = checkString(objs[1].toString(),INDEX_ERROR_MESSAGE);

        return string.indexOf(indexStr);
    }


    /**
     * 测试代码
     * @param args
     */
    public static void main(String[] args) {

        Object o1 = "adsadhauidwhq13\"";
        Object o2 = "s";

        IndexFunction indexFunction = new IndexFunction();
        System.out.println(indexFunction.invoke(o1,o2));

    }

}
