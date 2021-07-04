package org.wangfeng.panda.app.calculation.function;

import org.springframework.stereotype.Component;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.model.enums.DateFormatEnum;
import org.wangfeng.panda.app.util.DateUtils;

import java.util.Date;

/**
 * 日期转字符串函数
 *
 * 说明：
 *  1、将日期x转换为格式为y的字符串,y为日期数据可转换为的格式，如yyyyMMdd
 *
 * 要求：
 *  1、x为日期类型，y为格式匹配字符串，否则页面提示“输入信息类型不匹配
 *
 * 格式：
 *  1、dateToString(x,y)
 *
 * 返回值类型支持：
 *  1、string
 */
@Component
public class DateToStringFunction extends BaseFunction{

    private static final String DATE_TO_STRING_ERROR_MESSAGE = "日期转字符串函数异常！";

    @Override
    public Object invoke(Object... objs) {

        //1、校验传入的参数个数是否有问题
        checkArgsCount(2, DATE_TO_STRING_ERROR_MESSAGE, objs);

        //2、校验传入参数的格式是否正确
        if(objs[0] ==null){
            throw new RuleRuntimeException(DATE_TO_STRING_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }

        DateFormatEnum dateFormatEnum = DateFormatEnum.getByFormat(objs[1].toString());
        if(dateFormatEnum == null){
            throw new RuleRuntimeException(DATE_TO_STRING_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }

        //3、计算并返回结果
        Date date ;

        try{
            date = new Date(objs[0].toString());
        }catch (Exception e){
            throw new RuleRuntimeException(DATE_TO_STRING_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }

        String dateString = DateUtils.getString(date,objs[1].toString());

        return new StringBuffer().append(dateString).toString();
    }


    /**
     * 测试代码
     * @param args
     */
    public static void main(String[] args) {
        Object date = new Date("2018/11/02 09:11:32");
        Object pattern = "YYYYMMdd";

        DateToStringFunction dateToStringFunction = new DateToStringFunction();
        System.out.println(dateToStringFunction.invoke(date,pattern));

    }


}
