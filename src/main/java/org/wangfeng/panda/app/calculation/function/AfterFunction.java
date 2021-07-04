package org.wangfeng.panda.app.calculation.function;

import org.springframework.stereotype.Component;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;

import java.util.Date;

/**
 * 日期比较函数
 *
 * 说明：
 *  1、日期x是否在日期y之后
 *
 * 要求：
 *  1、x，y均为日期或时间类型，且类型必须一致，否则页面提示“输入信息类型不匹配”
 *
 * 格式：
 *  1、x after y
 *
 * 返回值类型支持：
 *  1、boolean
 */
@Component
public class AfterFunction extends BaseFunction {

    private static final String AFTER_ERROR_MESSAGE = "日期比较函数异常！";


    @Override
    public Object invoke(Object... objs) {

        //1、校验传入的参数个数是否有问题
        checkArgsCount(2, AFTER_ERROR_MESSAGE, objs);

        //2、校验传入参数的格式是否正确
        if(objs[0] == null || objs[1] == null){
            throw new RuleRuntimeException(AFTER_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }

        //3、计算并返回结果
        Date date1 ;
        Date date2 ;

        try{
            date1 = new Date(objs[0].toString());
            date2 = new Date(objs[1].toString());
        }catch (Exception e){
            throw new RuleRuntimeException(AFTER_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }

        return date1.getTime()-date2.getTime()>0;
    }


    /**
     * 测试代码
     * @param args
     */
    public static void main(String[] args) {

        Object o1 = new Date("2018/09/11 09:11:23");
        Object o2 = new Date("2018/09/11 09:11:23");

        AfterFunction afterFunction = new AfterFunction();
        System.out.println(afterFunction.invoke(o1,o2));
    }





}
