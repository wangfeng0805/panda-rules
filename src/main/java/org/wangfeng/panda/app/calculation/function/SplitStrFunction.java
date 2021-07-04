package org.wangfeng.panda.app.calculation.function;

import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 拆分字符串函数
 *
 * 说明：
 *  1、按照第二个参数来进行拆分第一个参数
 *
 * 要求：
 *  1、x为字符串，y为字符串，否则页面提示“输入信息类型不匹配”
 *
 * 格式：
 *  1、splitStr(x,y)
 *
 * 返回值类型支持：
 *  1、list<String>
 */
@Component
public class SplitStrFunction extends BaseFunction{


    private static final String SPLIT_STR_ERROR_MESSAGE = "拆分字符串函数异常！";

    @Override
    public Object invoke(Object... objs) {

        //1、校验传入的参数个数是否有问题
        String regex = "";
        if(objs.length==1 || "undefined".equals(objs[1])){
            regex = ",";
        }else{
            regex = checkString(objs[1].toString(),SPLIT_STR_ERROR_MESSAGE);
        }

//        checkArgsCount(2, SPLIT_STR_ERROR_MESSAGE, objs);

        //2、校验传入参数的格式是否正确
        String originStr = checkString(objs[0].toString(),SPLIT_STR_ERROR_MESSAGE);
//        String regex = checkString(objs[1].toString(),SPLIT_STR_ERROR_MESSAGE);

        //3、转化成List并且返回
        return Arrays.asList(originStr.split(regex,-1));
    }


    /**
     * 测试代码
     * @param args
     */
    public static void main(String[] args) {

        Object o1 = "a,4,g,asdf1q,123cv,1231f,as";
        Object o2 = ",";

        SplitStrFunction splitStrFunction = new SplitStrFunction();
        System.out.println(splitStrFunction.invoke(o1,o2));

    }

}
