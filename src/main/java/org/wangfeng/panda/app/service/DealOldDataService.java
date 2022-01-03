package org.wangfeng.panda.app.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.wangfeng.panda.app.calculation.FunctionEnum;
import org.wangfeng.panda.app.common.Constants;
import org.wangfeng.panda.app.common.base.AppBaseService;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.dao.domain.TCaCellVariable;
import org.wangfeng.panda.app.dao.domain.TCaDecisionVariable;
import org.wangfeng.panda.app.dao.domain.TCaRuleLine;
import org.wangfeng.panda.app.dao.domain.TCaSingleRule;
import org.wangfeng.panda.app.dao.mapper.TCaCellVariableMapper;
import org.wangfeng.panda.app.dao.mapper.TCaRuleLineMapper;
import org.wangfeng.panda.app.util.StringUtils;

import java.util.*;

/**
 * @author: wangfeng
 * @version: v1.0
 * @author: (lastest modification by wangfeng)
 * @since: 2021-09-26 11:22:43
 */
@Service
@Slf4j
public class DealOldDataService extends AppBaseService {

    @Autowired
    private TCaRuleLineMapper tCaRuleLineMapper;


    @Autowired
    private TCaCellVariableMapper tCaCellVariableMapper;



    @Transactional(rollbackFor = Exception.class,propagation= Propagation.REQUIRES_NEW)
    public void dealSingleRule(TCaSingleRule rule , Map<String, TCaSingleRule> singleRuleMap, Map<String, TCaDecisionVariable> decisionVariableMap){
        //3、得到表达式
        String showExpression = rule.getShowRuleExpression();
        if(showExpression == null){
            log.info("id为"+rule.getId()+"的规则没有表达式！");
            return;
        }

        JSONObject jsonObject = JSON.parseObject(showExpression);
        //得到IF
        String IF = jsonObject.get("IF")==null?null:jsonObject.get("IF").toString();
        //得到THEN
        String THEN = jsonObject.get("THEN")==null?null:jsonObject.get("THEN").toString();
        //得到ELSE
        String ELSE = jsonObject.get("ELSE")==null?null:jsonObject.get("ELSE").toString();
        //得到SINGLE
        String SINGLE = jsonObject.get("SINGLE")==null?null:jsonObject.get("SINGLE").toString();


        //4、如果if是null，则
        if(IF != null && THEN != null && ELSE != null){
            //4.1、先处理IF的逻辑
            dealIF(IF,rule,singleRuleMap,decisionVariableMap);
            //4.2、再处理THEN的逻辑
            dealOTHERS(THEN,rule,singleRuleMap,decisionVariableMap,"THEN");
            //4.3、最后处理ELSE的逻辑
            dealOTHERS(ELSE,rule,singleRuleMap,decisionVariableMap,"ELSE");

        }else if(SINGLE != null){
            //4.4、处理SINGLE的逻辑
            dealOTHERS(SINGLE,rule,singleRuleMap,decisionVariableMap,"SINGLE");

        }else{
            log.info("id为"+1+"的规则表达式有问题！");
        }
    }












    /**
     * IF 的 处理逻辑
     * @param IF
     * @param rule
     * @param singleRuleMap
     * @param decisionVariableMap
     */
    private void dealIF(String IF, TCaSingleRule rule , Map<String, TCaSingleRule> singleRuleMap,Map<String, TCaDecisionVariable> decisionVariableMap){

        //开启IF操作
        System.out.println(IF);
        String[] lineArray = IF.split("[\\|\\|,\\&\\&]");

        //遍历所有的行，并组成对象
        Integer lineSort = 0;
        for (String lineExp: lineArray){
            if(StringUtils.isNotBlank(lineExp)){
                lineSort += 1;

                IF = IF.substring(lineExp.length(),IF.length());

                //构建行对象
                TCaRuleLine tCaRuleLine = new TCaRuleLine();
                tCaRuleLine.setRuleCode(rule.getRuleCode());
                tCaRuleLine.setRuleLineModule("IF");
                tCaRuleLine.setSort(lineSort);
                tCaRuleLine.setStatus((short)1);
                tCaRuleLine.setBusinessCode(rule.getBusinessCode());

                tCaRuleLine.setLineCode("RULE_LINE_"+rule.getRuleCode()+"_IF_"+lineSort);
                if(lineExp.startsWith("(")){
                    lineExp = lineExp.substring(1,lineExp.length());
                    tCaRuleLine.setLineLeftBracket((short)1);
                }
                if(lineExp.endsWith(")")){
                    lineExp = lineExp.substring(0,lineExp.length()-1);
                    tCaRuleLine.setLineRightBracket((short)1);
                }
                if(IF.startsWith("&&")){
                    tCaRuleLine.setLineConnector("&&");
                    IF = IF.substring(2,IF.length());
                }
                if(IF.startsWith("||")){
                    tCaRuleLine.setLineConnector("||");
                    IF = IF.substring(2,IF.length());
                }

                tCaRuleLine.setLineWhetherSimple((short)0);


                //拆到对应的cell字符串
                String[] cellArray = lineExp.split(">=|>|<=|<|==|!=|after|before|isInArray");
                List<String> cellList = new ArrayList<>();
                String functionCode = lineExp;
                for(String cellCode : cellArray){
                    if(StringUtils.isNotBlank(cellCode)){
                        functionCode = functionCode.replace(cellCode,"");
                        cellList.add(cellCode);
                    }
                }
                if(cellList.size()!=2){
                    throw new RuleRuntimeException("IF的参数个数不正确");
                }

                //验证是否得到对应的符号
                if(FunctionEnum.getByFunctionCode(functionCode)==null){
                    throw new RuleRuntimeException("操作符有问题");
                }

                tCaRuleLine.setLineFunctionCode(functionCode);

                initSaveWord(tCaRuleLine);

                //这里获取到了对应的格子对象
                tCaRuleLineMapper.insert(tCaRuleLine);
                System.out.println(tCaRuleLine);


                //遍历所有的cell_variable格子，并组成对象
                Integer cellSort = 0;
                for(String cellCode : cellList){
                    cellSort += 1;
                    dealCell(cellCode,lineSort,cellSort,rule,tCaRuleLine,singleRuleMap,decisionVariableMap,"IF");
                }
            }
        }

        System.out.println("-----------------------------------------------------------------");

    }


    /**
     *
     * @param OTHERS
     * @param rule
     * @param singleRuleMap
     * @param decisionVariableMap
     */
    private void dealOTHERS(String OTHERS, TCaSingleRule rule , Map<String, TCaSingleRule> singleRuleMap,Map<String, TCaDecisionVariable> decisionVariableMap,String type) {

        JSONObject jsonObject = null;

        //如果是简单的，走这里，其他走下面
        try{
            jsonObject = JSONObject.parseObject(OTHERS);
        }catch (JSONException e){
            //是简单操作
            TCaRuleLine tCaRuleLine = new TCaRuleLine();

            tCaRuleLine.setLineLeftBracket((short)0);
            tCaRuleLine.setLineRightBracket((short)0);
            tCaRuleLine.setSort(1);
            tCaRuleLine.setRuleLineModule(type);
            tCaRuleLine.setRuleCode(rule.getRuleCode());
            tCaRuleLine.setLineConnector("");
            tCaRuleLine.setBusinessCode(rule.getBusinessCode());
            tCaRuleLine.setStatus((short)1);
            tCaRuleLine.setLineCode("RULE_LINE_"+rule.getRuleCode()+"_"+type+"_"+1);

            String lineExp = OTHERS;

            tCaRuleLine.setLineFunctionCode("");
            tCaRuleLine.setLineWhetherSimple((short)1);
            initSaveWord(tCaRuleLine);

            //插入对应的行
            tCaRuleLineMapper.insert(tCaRuleLine);

            //循环遍历插入
            List<String> cellList = new ArrayList<String>(){{
                add(lineExp);
            }};

            //遍历所有的cell_variable格子，并组成对象
            Integer cellSort = 0;
            for(String cellCode : cellList){
                cellSort += 1;
                dealCell(cellCode,1,cellSort,rule,tCaRuleLine,singleRuleMap,decisionVariableMap,type);
            }

            return;
        }


        //复杂的走这里，获取所有的key
        Set<String> keys =  jsonObject.keySet();
        for(String key : keys){
            Integer lineSort = Integer.parseInt(key.replace("#Line",""));
            TCaRuleLine tCaRuleLine = new TCaRuleLine();

            tCaRuleLine.setLineLeftBracket((short)0);
            tCaRuleLine.setLineRightBracket((short)0);
            tCaRuleLine.setSort(lineSort);
            tCaRuleLine.setRuleLineModule(type);
            tCaRuleLine.setRuleCode(rule.getRuleCode());
            tCaRuleLine.setLineConnector("");
            tCaRuleLine.setBusinessCode(rule.getBusinessCode());
            tCaRuleLine.setStatus((short)1);
            tCaRuleLine.setLineCode("RULE_LINE_"+rule.getRuleCode()+"_"+type+"_"+lineSort);

            String lineExp = jsonObject.getString(key);
            if(lineExp.startsWith("[[FUNC_") && lineExp.endsWith("]]")){

                String[] arr = lineExp.split("\\(|\\)");
                for(String str : arr){
                    if(StringUtils.isBlank(str)){
                        continue;
                    }
                    if(str.startsWith("[[FUNC_")){
                        String funcCode = str.substring(2,str.length());

                        FunctionEnum functionEnum = FunctionEnum.getByFunctionExpression(funcCode);
                        if(functionEnum ==null){
                            throw new RuleRuntimeException("没有对应的函数！！");
                        }
                        tCaRuleLine.setLineFunctionCode(functionEnum.getFunctionCode());
                    }else if(!str.endsWith("]]")){
                        //这里是最终的cell
                        String[] cellArr = str.split(",");

                        //遍历所有的cell_variable格子，并组成对象
                        Integer cellSort = 0;
                        for(String cellCode : cellArr){
                            if(StringUtils.isNotBlank(cellCode)){
                                cellSort += 1;
                                dealCell(cellCode,lineSort,cellSort,rule,tCaRuleLine,singleRuleMap,decisionVariableMap,type);
                            }
                        }
                    }else if(str.endsWith("]]")){
                        log.info("结束了");
                    }else{
                        throw new RuleRuntimeException("多到了奇奇怪怪的东西~~");

                    }
                }

                tCaRuleLine.setLineWhetherSimple((short)2);
                initSaveWord(tCaRuleLine);

                //插入对应的行
                tCaRuleLineMapper.insert(tCaRuleLine);

            }else{
                tCaRuleLine.setLineFunctionCode("");
                tCaRuleLine.setLineWhetherSimple((short)1);
                initSaveWord(tCaRuleLine);

                //插入对应的行
                tCaRuleLineMapper.insert(tCaRuleLine);

                //循环遍历插入
                List<String> cellList = new ArrayList<String>(){{
                    add(lineExp);
                }};

                //遍历所有的cell_variable格子，并组成对象
                Integer cellSort = 0;
                for(String cellCode : cellList){
                    cellSort += 1;
                    dealCell(cellCode,lineSort,cellSort,rule,tCaRuleLine,singleRuleMap,decisionVariableMap,type);
                }

            }

        }

    }


    /**
     *
     */
    public void dealCell(String cellCode,
                         Integer lineSort,
                         Integer cellSort,
                         TCaSingleRule rule,
                         TCaRuleLine tCaRuleLine,
                         Map<String, TCaSingleRule> singleRuleMap,
                         Map<String, TCaDecisionVariable> decisionVariableMap,
                         String type){
        TCaCellVariable tCaCellVariable = new TCaCellVariable();

        try {
            tCaCellVariable.setBusinessCode(rule.getBusinessCode());
            tCaCellVariable.setStatus((short)1);
            tCaCellVariable.setSort(cellSort);
            tCaCellVariable.setRuleLineCode(tCaRuleLine.getLineCode());

            //判断是否是决策变量
            if(cellCode.startsWith("{{") && cellCode.endsWith("}}")){
                String finalCellCode = cellCode.substring(2,cellCode.length()-2);

                tCaCellVariable.setCellVariableCode("CELL_"+rule.getRuleCode()+"_"+lineSort+"_"+cellSort+"_"+type+"_"+finalCellCode);
                tCaCellVariable.setCellVariableKey(finalCellCode);
                tCaCellVariable.setCellVariableValue(Constants.EMPTY);

                //1）优先是决策变量
                if(decisionVariableMap.containsKey(finalCellCode)){
                    tCaCellVariable.setCellVariableSource((short)2);
                    tCaCellVariable.setCellVariableType(decisionVariableMap.get(finalCellCode).getVariableType());

                }else if(singleRuleMap.containsKey(finalCellCode)){
                    tCaCellVariable.setCellVariableSource((short)3);
                    tCaCellVariable.setCellVariableType(singleRuleMap.get(finalCellCode).getOutPutType());
                }else if(finalCellCode.startsWith("#Line")){
                    tCaCellVariable.setCellVariableSource((short)4);
                    tCaCellVariable.setCellVariableType((short)0);
                }else{
                    log.info("这里三不管");
                }
            }else{
                String finalCellCode = cellCode;
                tCaCellVariable.setCellVariableCode("CELL_"+rule.getRuleCode()+"_"+lineSort+"_"+cellSort+"_"+type+"_"+finalCellCode);
                //其他的都当作手动输入
                if(cellCode.startsWith("\"") && cellCode.endsWith("\"")){
                    finalCellCode = cellCode.substring(1,cellCode.length()-1);
                    tCaCellVariable.setCellVariableType((short)1);
                }else if (cellCode.contains("\\.")){
                    tCaCellVariable.setCellVariableType((short)4);
                }else{
                    tCaCellVariable.setCellVariableType((short)2);
                }
                tCaCellVariable.setCellVariableKey(finalCellCode);
                tCaCellVariable.setCellVariableValue(finalCellCode);
                tCaCellVariable.setCellVariableSource((short)1);
            }


            tCaCellVariable.setCreatedTime(new Date());
            tCaCellVariable.setCreatedBy(Constants.SYS);
            tCaCellVariable.setModifiedTime(new Date());
            tCaCellVariable.setModifiedBy(Constants.SYS);
            tCaCellVariable.setDeleteFlag(Constants.SHORT_ZERO);

            //这里获取到了对应的格子对象
            tCaCellVariableMapper.insertSelective(tCaCellVariable);
            //这里获取到了line_cell的mapping对象
            System.out.println(tCaCellVariable);
        }catch (Exception e){
            log.error("抛异常啦",e);
        }


    }


}
