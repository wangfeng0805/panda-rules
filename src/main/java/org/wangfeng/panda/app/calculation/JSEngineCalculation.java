package org.wangfeng.panda.app.calculation;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.wangfeng.panda.app.cache.SpringUtil;
import org.wangfeng.panda.app.common.Constants;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.dao.domain.TCaCellVariable;
import org.wangfeng.panda.app.model.vo.TCaRuleLineVO;
import org.wangfeng.panda.app.util.DateUtils;
import org.wangfeng.panda.app.util.StringUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Slf4j
@SuppressWarnings("all")
public class JSEngineCalculation {

    private static volatile ScriptEngine engine;

    private static void init() {
        ScriptEngineManager manager = new ScriptEngineManager();
        engine = manager.getEngineByName("javascript");
    }


    /**
     * 执行计算的方法（目前仅用在决策树中的判断中会用到，先不去掉，后期再看）
     *
     * @param expressionStr
     * @param jsonObject
     * @return
     */

    public static Object calculate(String expressionStr, JSONObject jsonObject) {

        //1、第一步解析，转化内置操作，即把自定义的方法（FUNC开头的方法）执行，并且返回结果替换到表达式内。
        //方法都是[[]]包起来，并且执行。

        while (getFunctionCount(expressionStr) > 0) {
            String[] rules = expressionStr.split("\\[\\[|\\]\\]");
            List<String> functions = new ArrayList<>();
            for (String rule : rules) {
                if (rule.startsWith("FUNC_") && rule.endsWith(")")) {
                    functions.add(rule);
                }
            }

            for (String function : functions) { //遍历所有的内置方法

                Object result = null;
                String[] elements = function.split("[\\(\\)\\,]");

                String className = "";
                List<Object> args = new ArrayList();//参数列表
                for (String element : elements) {
                    if (element.startsWith("FUNC_")) {
                        className = element;
                    } else if (element.startsWith("{{") && element.endsWith("}}")) {
                        String key = element.replaceAll("\\{\\{", "").replaceAll("}}", "");
                        args.add(jsonObject.get(key));
                    } else if (element.length() > 0) {
                        args.add(element);
                    }
                }
                try {
                    //按照反射获取对应的类
                    String fullClassName = Constants.PRE_FUNCTION_CLASS_NAME + FunctionEnum.getByFunctionName(className).getClassName();
                    Class clz = Class.forName(fullClassName);
                    Object obj =  SpringUtil.getBean(clz);
                    //获得对应的方法
                    Method m = obj.getClass().getDeclaredMethod(Constants.FUNCTION_METHOD_NAME, Object[].class);
                    Object[] strings = new Object[args.size()];
                    args.toArray(strings);
                    result = m.invoke(obj, (Object) strings);
                } catch(RuleRuntimeException e){
                    log.error("单个计算错误，错误信息：{}，表达式是：{}",e.getMessage(),expressionStr);
                    throw new RuleRuntimeException(e.getMessage());
                }catch (InvocationTargetException e){
                    String errorMsg = ((InvocationTargetException) e).getTargetException().getMessage();
                    log.error("反射计算错误！" + errorMsg);
                    throw new RuleRuntimeException(errorMsg);
                }catch (Exception e) {
                    log.error("获取方法和类出错！" + e.getMessage());
                    throw new RuleRuntimeException("获取方法和类出错！");
                }

                if(result instanceof Date){
                    result = "\""+ DateUtils.getString((Date) result,DateUtils.DEFAULT_FORMAT)+"\"";
                }
                expressionStr = expressionStr.replace("[["+function+"]]",result.toString());

            }

        }

        //第二步解析，传入参数
        String[] arr = expressionStr.split("[{}]");
        for(int i=0;i<arr.length;i++){
            if(jsonObject.containsKey(arr[i])){
                Object o = jsonObject.get(arr[i]);
                String before = "\\{\\{" + arr[i] + "\\}\\}";
                String after = o.toString();
                //如果是字符串类型的，需要前后加上引号
                if (o instanceof String) {
                    after = "\"" + after + "\"";
                }

                expressionStr = expressionStr.replaceAll(before, after);
            }
        }

        try {
            Object calculateResult = jsCalculate(expressionStr);
            return calculateResult;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuleRuntimeException("规则运算异常!");
        }

    }


    /**
     * 得到方法的数量（同上，暂时不去掉）
     *
     * @param expressionStr
     * @return
     */
    private static Integer getFunctionCount(String expressionStr) {
        String[] rules = expressionStr.split("\\[\\[|\\]\\]");
        List<String> functions = new ArrayList<>();
        for (String rule : rules) {
            if (rule.startsWith("FUNC_") && rule.endsWith(")")) {
                functions.add(rule);
            }
        }
        return functions.size();
    }

    /**
     * JS计算
     *
     * @param script
     * @return
     */
    public static Object jsCalculate(String script) {
        if (engine == null) {
            log.info("进入了init方法，表达式是{}", script);
            init();
        }
        try {
            if("".equals(script)){
                return "";
            }
            return (Object) engine.eval(script);
        } catch (ScriptException ex) {
            log.error(ex.getMessage());
            throw new RuleRuntimeException(ex.getMessage());
        }
    }

    /**
     * 运行JS基本函数
     */
    public static Object jsFunction(Object... o) {
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine se = sem.getEngineByName("javascript");
        try {
            String script = "function say(name){ return 'hello,'+name; }";
            se.eval(script);
            Invocable inv2 = (Invocable) se;
            String res = (String) inv2.invokeFunction("say", o);
            if (res instanceof String) {
                res = "\"" + res + "\"";
            }
            return res;
        } catch (Exception e) {
            log.error("运行JS基本函数错误，" + e.getMessage(), e);
            throw new RuleRuntimeException("运行JS基本函数错误!");
        }
    }

/*-------------------------------------------以下是2.0之后的计算逻辑，以对象的形式，而不是以字符串的形式---------------------------------------------*/

    /**
     * 计算多行
     * 用在single/then/else
     */
    public static Object calculateLines(List<TCaRuleLineVO> lineModleList , JSONObject originJSONObject){

        Object finalResult = null;

        //定义一个规则内含line的json
        JSONObject lineInObject = new JSONObject();

        for(int i=0;i<lineModleList.size();i++){

            //计算该行
            TCaRuleLineVO singleLine = lineModleList.get(i);
            calculateLine(singleLine,originJSONObject,lineInObject);
            //得到结果
            Object result = singleLine.getLineResult();
            //如果不是最后一行，则放入json中
            if(i< lineModleList.size()-1){
                lineInObject.put("#Line"+singleLine.getSort(),result);
            }else if(i == lineModleList.size()-1){
                //如果是最后一行，则输出
                finalResult =  result;
            }
        }

//        if(finalResult instanceof String){
//            finalResult = "\""+finalResult+"\"";
//        }

        return finalResult;
    }


    /**
     * 计算单行
     * @param lineModle
     * @param originJSONObject
     * @param lineInObject
     */
    public static void calculateLine(TCaRuleLineVO lineModle,JSONObject originJSONObject,JSONObject lineInJSONObject){

        Object lineResult = null;

        //获取入参
        List<TCaCellVariable> variableCellModelList = lineModle.getTCaCellVariableList();
        if (variableCellModelList==null || variableCellModelList.size() < 1){
            throw new RuntimeException("入参个数有问题");
        }
        //获取运算符
        String functionCode = lineModle.getLineFunctionCode();

        if(StringUtils.isBlank(functionCode)){
            //没有运算符的时候时简单计算
            //简单计算的时候应当只有一个参数进来
            if(variableCellModelList.size()!=1){
                throw new RuntimeException("在single模块内，简单计算时候，参数数量不正确！");
            }
            //得到value
            lineResult = getValue(variableCellModelList.get(0),originJSONObject,lineInJSONObject);

            lineModle.setLineResult(lineResult);
        }else{
            FunctionEnum functionEnum = FunctionEnum.getByFunctionCode(functionCode);

            if(functionEnum == null){
                throw new RuleRuntimeException("没有对应的表达式！");
            }

            //有运算符的时候进行复杂计算
            Object[] strings = new Object[variableCellModelList.size()];
            try {
                //按照反射获取对应的类
                String fullClassName = Constants.PRE_FUNCTION_CLASS_NAME + functionEnum.getClassName();
                Class clz = Class.forName(fullClassName);
                Object obj =  SpringUtil.getBean(clz);
                //获得对应的方法
                Method m = obj.getClass().getDeclaredMethod(Constants.FUNCTION_METHOD_NAME, Object[].class);

                for(int i=0 ; i<variableCellModelList.size() ;i++){
                    strings[i] = getValue(variableCellModelList.get(i),originJSONObject,lineInJSONObject);
                }
                lineResult = m.invoke(obj, (Object) strings);
                lineModle.setLineResult(lineResult);
            } catch (InvocationTargetException e){
                String errorMsg = ((InvocationTargetException) e).getTargetException().getMessage();
                StringBuffer sb = new StringBuffer();
                for(Object s : strings){
                    sb.append(s==null?null:s.toString());
                    sb.append(",");
                }
                errorMsg = String.format("单行计算错误，规则code为：%s，行code为：%s，错误信息：%s，入参为：%s ", lineModle.getRuleCode(),lineModle.getLineCode(), errorMsg,sb);
                log.error(errorMsg);
                throw new RuleRuntimeException(errorMsg);
            }catch (Exception e) {
                log.error("获取方法和类出错！" + e.getMessage());
                throw new RuleRuntimeException("获取方法和类出错！");
            }

        }

    }


    public static Object getValue(TCaCellVariable variableCellModel, JSONObject originJSONObject, JSONObject lineInJSONObject){

        Object value = null;
        if(StringUtils.isNotBlank(variableCellModel.getCellVariableValue())
                || variableCellModel.getCellVariableValue().equals(variableCellModel.getCellVariableKey())
                || ("\""+variableCellModel.getCellVariableValue()+"\"").equals(variableCellModel.getCellVariableKey())){
            //如果当前对象有value,则取当前value
            value = variableCellModel.getCellVariableValue();
        }else if(lineInJSONObject.containsKey(variableCellModel.getCellVariableKey())){
            //如果当前key在lineInJSONObject中
            value = lineInJSONObject.get(variableCellModel.getCellVariableKey());

        }else if(originJSONObject.containsKey(variableCellModel.getCellVariableKey())){
            //如果当前key在originJSONObject中
            value = originJSONObject.get(variableCellModel.getCellVariableKey());
        }else {
            throw new RuntimeException(String.format("参数 %s 没有值，请确认",variableCellModel.getCellVariableKey()));
        }

        return value;
    }











}
