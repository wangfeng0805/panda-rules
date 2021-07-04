package org.wangfeng.panda.app.calculation.function;

import org.springframework.stereotype.Component;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.util.DateUtils;
import org.wangfeng.panda.app.util.NumberUtils;

import java.util.Date;

/**
 * 日期增加天数函数
 *
 * 说明：
 *  1、日期天数加法计算函数，函数返回日期x加y天后的日期
 *
 * 要求：
 *  1、x为日期类型，y为0或正负整数，否则页面提示“输入信息类型不匹配”
 *
 * 格式：
 *  1、addDays(x,y)
 *
 * 返回值类型支持：
 *  1、date
 */
@Component
public class AddDaysFunction extends BaseFunction{

    private static final String ADD_DATE_ERROR_MESSAGE = "日期增加天数函数异常！";

    @Override
    public Object invoke(Object... objs) {

        //1、校验传入的参数个数是否有问题
        checkArgsCount(2, ADD_DATE_ERROR_MESSAGE, objs);

        //2、校验传入参数的格式是否正确
        if(!(objs[0] instanceof Date)){
            throw new RuleRuntimeException(ADD_DATE_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }
        Date date = (Date)objs[0];
        if(!NumberUtils.isCalculationNumber(objs[1]) || !NumberUtils.IsInteger(Double.valueOf(objs[1].toString()))){
            throw new RuleRuntimeException(ADD_DATE_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }

        Date finalDate = DateUtils.getRelativeDateByDay(date,Integer.valueOf(objs[1].toString()));

        return finalDate;
    }


    /**
     * 测试代码
     * @param args
     */
    public static void main(String[] args) {

        Object o1 = new Date("2018/09/11");
        Object o2 = -3;

        AddDaysFunction addDaysFunction = new AddDaysFunction();
        System.out.println(addDaysFunction.invoke(o1,o2));

    }


}
