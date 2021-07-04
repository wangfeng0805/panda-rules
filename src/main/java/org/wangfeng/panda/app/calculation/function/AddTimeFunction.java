package org.wangfeng.panda.app.calculation.function;

import org.springframework.stereotype.Component;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.util.DateUtils;
import org.wangfeng.panda.app.util.NumberUtils;

import java.util.Date;

/**
 * 日期增加时间函数
 *
 * 说明：
 *  1、日期时间加法计算函数，返回日期x增加y小时z分钟s秒后的日期
 *
 * 要求：
 *  1、x为日期类型，y、z、s为0或正负整数，否则页面提示“输入信息类型不匹配”
 *
 * 格式：
 *  1、addTime(x,y,z,s)
 *
 * 返回值类型支持：
 *  1、date
 */
@Component
public class AddTimeFunction extends BaseFunction{

    private static final String ADD_TIME_ERROR_MESSAGE = "日期增加时间函数异常！";

    @Override
    public Object invoke(Object... objs) {

        //1、校验传入的参数个数是否有问题
        checkArgsCount(4, ADD_TIME_ERROR_MESSAGE, objs);

        //2、校验传入参数的格式是否正确
        if(!(objs[0] instanceof Date)){
            throw new RuleRuntimeException(ADD_TIME_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }
        if(!NumberUtils.isCalculationNumber(objs[1]) || !NumberUtils.IsInteger(Double.valueOf(objs[1].toString()))){
            throw new RuleRuntimeException(ADD_TIME_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }
        if(!NumberUtils.isCalculationNumber(objs[2]) || !NumberUtils.IsInteger(Double.valueOf(objs[1].toString()))){
            throw new RuleRuntimeException(ADD_TIME_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }
        if(!NumberUtils.isCalculationNumber(objs[3]) || !NumberUtils.IsInteger(Double.valueOf(objs[1].toString()))){
            throw new RuleRuntimeException(ADD_TIME_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }

        //3、计算并返回结果
        Date date = (Date)objs[0];
        Integer hour = Integer.valueOf(objs[1].toString());
        Integer minute = Integer.valueOf(objs[2].toString());
        Integer second = Integer.valueOf(objs[3].toString());

        Date finalDate = DateUtils.getRelativeDateByHMS(date,hour,minute,second);

        return finalDate;
    }


    /**
     * 测试代码
     * @param args
     */
    public static void main(String[] args) {

        Object date = new Date("2018/09/11");
        Object hour = 25;
        Object minute = 1;
        Object second = 1;


        AddTimeFunction addTimeFunction = new AddTimeFunction();
        System.out.println(addTimeFunction.invoke(date,hour,minute,second));

    }



}
