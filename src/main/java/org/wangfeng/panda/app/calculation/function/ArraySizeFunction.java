package org.wangfeng.panda.app.calculation.function;

import org.springframework.stereotype.Component;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;

import java.util.List;

/**
 * 数组长度统计函数
 *
 * 要求：
 *  1、objs的长度必须只有一个，并且是数组类型
 *
 * 支持：
 *  1、integer
 */
@Component
public class ArraySizeFunction extends BaseFunction{

    private static final String ARRAY_SIZE_ERROR_MESSAGE = "数组长度统计函数异常！";

    @Override
    public Object invoke(Object... objs) {

        //1、校验传入的参数个数是否有问题
        checkArgsCount(1,ARRAY_SIZE_ERROR_MESSAGE,objs);

        //2、校验数值类型是否正确
        if(!(objs[0] instanceof List)){
            throw new RuleRuntimeException(ARRAY_SIZE_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }

        //3、计算并返回
        List list = (List)objs[0];
        if(list!=null){
            return list.size();
        }

        return 0;
    }


}
