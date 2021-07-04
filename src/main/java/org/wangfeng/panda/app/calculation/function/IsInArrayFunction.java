package org.wangfeng.panda.app.calculation.function;

import org.springframework.stereotype.Component;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;

import java.util.ArrayList;
import java.util.List;

/**
 * 判断某个元素是否在集合中
 *
 * 要求：
 *  1、objs的长度必须有两个，并且第一个是所需要查看的元素，第二个是List
 *  2、目前支持的只有对字符串的操作，故所有的args[0]都转成字符串操作，传入的list都专程字符串数组
 *
 * 返回值支持：
 *  1、Boolean
 *
 */
@Component
public class IsInArrayFunction extends BaseFunction{

    private static final String IS_IN_ARRAY_ERROR_MESSAGE = "判断某个元素是否在集合中函数异常！";

    @Override
    public Object invoke(Object... objs) {

        //1、校验传入的参数是否有问题
        checkArgsCount(2,IS_IN_ARRAY_ERROR_MESSAGE,objs);

        //2、校验数值类型是否正确
        if(objs[0]==null||!(objs[1] instanceof List)){
            throw new RuleRuntimeException(IS_IN_ARRAY_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }

        //3、组装成string进行比较
        String selStr = checkString(objs[0].toString(),IS_IN_ARRAY_ERROR_MESSAGE);
        List<String> selList = new ArrayList<String>();
        for(Object o : (List)objs[1]){
            selList.add(o.toString());
        }

        //4、计算并返回结果
        return selList.contains(selStr);
    }


    /**
     * 测试代码
     * @param args
     */
    public static void main(String[] args) {

        Object o1 = "1";

        List o2 = new ArrayList<Object>();
        o2.add(1);
        o2.add(2);
        o2.add(3);

        IsInArrayFunction is = new IsInArrayFunction();
        System.out.println(is.invoke(o1,o2));


    }



}
