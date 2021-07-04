package org.wangfeng.panda.app.calculation.function;

import org.springframework.stereotype.Component;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.util.NumberUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取数组中特定下标的元素的
 *
 * 要求：
 *  1、objs中必须有两个参数
 *  2、第一个参数为数组（数组如何传递），第二个参数为指定的位置，必须是整数
 *
 * 返回值类型支持：
// *  1、integer
// *  2、double
 *  3、string （目前只支持字符串）
// *  4、date
// *  5、time
// *  6、boolean
 */
@Component
public class GetElementFunction extends BaseFunction{

    private static final String GET_ELEMENT_ERROR_MESSAGE = "获取数组中特定下标的元素的函数异常！";

    @Override
    public Object invoke(Object... objs) {
        //1、校验传入的参数是否有问题
        checkArgsCount(2,GET_ELEMENT_ERROR_MESSAGE,objs);

        //2、校验第一个参数是否是数组,第二个参数是否是正整数
        try {
            if(!(objs[0] instanceof List) || !NumberUtils.NonNegativeInteger(Double.valueOf(objs[1].toString()))){
                throw new RuleRuntimeException(GET_ELEMENT_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
            }
        }catch (Exception e){
            throw new RuleRuntimeException(GET_ELEMENT_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }

        //3、计算并返回结果
        try{
            List list = (List)objs[0];
            if(list!=null){
                return list.get(Integer.valueOf(objs[1].toString()));
//                return new StringBuffer().append("\"").append(list.get(Integer.valueOf(objs[1].toString())).toString()).append("\"").toString();
            }
        }catch (IndexOutOfBoundsException e){
            return "";
        }catch (Exception e){
            return null;
        }
        return null;
    }


    /**
     * 测试代码
     * @param args
     */
    public static void main(String[] args) {

        List o1 = new ArrayList();
        o1.add(1);
        o1.add("这是第二个");
        o1.add(true);
        o1.add('f');

        Object o2 = 111;

        GetElementFunction getElementFunction = new GetElementFunction();
        System.out.println(getElementFunction.invoke(o1,o2));

    }




}
