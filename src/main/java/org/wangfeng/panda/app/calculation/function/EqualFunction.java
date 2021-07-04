package org.wangfeng.panda.app.calculation.function;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;


/**
 * 比较相等函数
 *
 * 说明：
 *  1、x是否等于y
 *
 * 要求：
 *  1、x，y均为数字，可以为浮点数、整数类型；否则页面提示“输入信息类型不匹配”
 *
 * 格式：
 *  1、x==y
 *
 * 返回值类型支持：
 *  1、integer
 *  2、double
 */
@Slf4j
@Component
public class EqualFunction extends BaseFunction {

    private static final String EQUAL_ERROR_MESSAGE = "比较相等函数异常！";


    @Override
    public Object invoke(Object... objs) {

        //1、校验传入的参数个数是否有问题
        checkArgsCount(2, EQUAL_ERROR_MESSAGE, objs);

        //2、校验传入参数的格式是否正确
        if(objs[0]==null|| objs[1]==null){
            throw new RuleRuntimeException(EQUAL_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }

        //3、计算并返回结果
        try{
            String obj1 = checkString(objs[0].toString(),EQUAL_ERROR_MESSAGE);
            String obj2 = checkString(objs[1].toString(),EQUAL_ERROR_MESSAGE);

            return obj1.equals(obj2);

        }catch (RuleRuntimeException e){
            throw new RuleRuntimeException(EQUAL_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }
    }


    /**
     * 测试代码
     * @param args
     */
    public static void main(String[] args) {

        Object o1 = 12;
        Object o2 = 3.2;

        EqualFunction equalFunction = new EqualFunction();
        System.out.println(equalFunction.invoke(o1,o2));
    }





}
