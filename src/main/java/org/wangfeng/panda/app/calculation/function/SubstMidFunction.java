package org.wangfeng.panda.app.calculation.function;


import org.springframework.stereotype.Component;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.util.NumberUtils;

/**
 * 字符串中部分截取函数
 *
 * 说明：
 *  1、截取字符串x中从第y位到z位的部分，函数返回截取后的新字符串
 *
 * 要求：
 *  1、x为字符串类型，y、z为大于等于0的整数；否则页面提示“输入信息类型不匹配”
 *
 * 格式：
 *  1、substMid(x,y,z)
 *
 * 返回值类型支持：
 *  1、string
 */
@Component
public class SubstMidFunction extends BaseFunction{

    private static final String SUBST_MID_ERROR_MESSAGE = "字符串中部分截取函数异常！";


    @Override
    public Object invoke(Object... objs) {

        //1、校验传入的参数个数是否有问题
        checkArgsCount(3, SUBST_MID_ERROR_MESSAGE, objs);

        //2、处理传入的参数
        String string = checkString(objs[0].toString(),SUBST_MID_ERROR_MESSAGE);
        if(!NumberUtils.isCalculationNumber(objs[1]) || !NumberUtils.NonNegativeInteger(Double.valueOf(objs[1].toString()))){
            throw new RuleRuntimeException(SUBST_MID_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }
        Integer startIndex = Integer.valueOf(objs[1].toString());
        if(!NumberUtils.isCalculationNumber(objs[2]) || !NumberUtils.NonNegativeInteger(Double.valueOf(objs[2].toString()))){
            throw new RuleRuntimeException(SUBST_MID_ERROR_MESSAGE+IMPORT_UNITE_ERROR_MESSAGE);
        }
        Integer endIndex = Integer.valueOf(objs[2].toString());


        //3、当剩余长度小于
        if(string.length()<endIndex){
            return string.substring(startIndex);
        }




        //4、计算并返回
        return new StringBuffer().append(string.substring(startIndex,endIndex)).toString();
    }

    /**
     * 测试代码
     * @param args
     */
    public static void main(String[] args) {

        Object o1 = "sdqwewqf";
        Object o2 = 3;
        Object o3 = 6;


        SubstMidFunction substLeftFunction = new SubstMidFunction();
        System.out.println(substLeftFunction.invoke(o1,o2,o3));

    }


}
