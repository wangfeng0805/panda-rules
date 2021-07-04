package org.wangfeng.panda.app.calculation.function;

import org.springframework.stereotype.Component;
import org.wangfeng.panda.app.common.Constants;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.model.enums.DateFormatEnum;
import org.wangfeng.panda.app.util.DateUtils;
import org.wangfeng.panda.app.util.StringUtils;

import java.util.Date;

/**
 * 字符串转日期函数
 *
 * 说明：
 *  1、将字符串x转换为格式为y的日期数据
 *
 * 要求：
 *  1、x为字符串类型，否则页面提示“输入信息类型不匹配”
 *
 * 格式：
 *  1、stringToDate(x,y)
 *
 * 返回值类型支持：
 *  1、date
 */
@Component
public class StringToDateFunction extends BaseFunction{

    private static final String STRING_TO_DATE_ERROR_MESSAGE = "字符串转日期函数异常！";

    @Override
    public Object invoke(Object... objs) {

        //1、校验传入的参数个数是否有问题
        checkArgsCount(2, STRING_TO_DATE_ERROR_MESSAGE, objs);

        //2、处理传入的参数
        String dateString = checkString(objs[0].toString(),STRING_TO_DATE_ERROR_MESSAGE);
        if(StringUtils.isBlank(dateString)){
            //如果传入的日期是空字符串，那直接返回默认值，防止整体检测报错，最后的结果
            return Constants.DEFAULT_DATE_STRING;
        }

        String formatString = checkString(objs[1].toString(),STRING_TO_DATE_ERROR_MESSAGE);

        DateFormatEnum dateFormatEnum = DateFormatEnum.getByFormat(formatString);
        if(dateFormatEnum == null){
            throw new RuleRuntimeException(STRING_TO_DATE_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }

        //3 计算并返回结果
        Date date = DateUtils.getDate(dateString,formatString);
        if(date == null){
            throw new RuleRuntimeException(STRING_TO_DATE_ERROR_MESSAGE);
        }
        return date;
    }


    /**
     * 测试代码
     * @param args
     */
    public static void main(String[] args) {

        Object dateString = "20180910";
        Object pattern = "YYYYMMdd";

        StringToDateFunction stringToDateFunction = new StringToDateFunction();
        System.out.println(stringToDateFunction.invoke(dateString,pattern));

    }


}
