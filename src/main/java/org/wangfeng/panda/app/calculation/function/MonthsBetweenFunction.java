package org.wangfeng.panda.app.calculation.function;

import org.springframework.stereotype.Component;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.util.DateUtils;

import java.util.Date;

/**
 * 两个日期之间间隔的月数
 *
 * 要求：
 *  1、所有的参数都是date类型
 *  2、objs的长度必须有两个，第一个参数是起始日期，第二个参数是结束日期
 *
 * 返回值类型支持：
 *  1、integer
 */
@Component
public class MonthsBetweenFunction extends BaseFunction{

    private static final String MONTHS_BETWEEN_ERROR_MESSAGE = "计算两个日期之间间隔月数的函数异常！！";

    @Override
    public Object invoke(Object... objs) {
        //1、校验传入的参数个数是否有问题
        checkArgsCount(2,MONTHS_BETWEEN_ERROR_MESSAGE,objs);

        //2、校验这两个参数是否都是date类型
        if(objs[0] ==null || objs[1] == null){
            throw new RuleRuntimeException(MONTHS_BETWEEN_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }

        //3、计算并返回结果
        Date fromDate ;
        Date endDate ;
        try{
            fromDate = new Date(objs[0].toString());
            endDate = new Date(objs[1].toString());
        }catch (Exception e){
            throw new RuleRuntimeException(MONTHS_BETWEEN_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }

        return DateUtils.monthsBetween(fromDate,endDate);
    }


    public static void main(String[] args) {

        Object o1 = new Date("2018/08/09 22:10:09");
        Object o2 = new Date("2019/09/10 09:00:22");

        MonthsBetweenFunction monthsBetweenFunction = new MonthsBetweenFunction();
        System.out.println(monthsBetweenFunction.invoke(o1,o2));

    }


}
