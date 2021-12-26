package org.wangfeng.panda.app.service.excel.importBiz;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.wangfeng.panda.app.common.Constants;
import org.wangfeng.panda.app.dao.domain.TCaCellVariable;
import org.wangfeng.panda.app.dao.domain.TCaRuleLine;
import org.wangfeng.panda.app.dao.domain.TCaSingleRule;
import org.wangfeng.panda.app.service.CellVariableService;
import org.wangfeng.panda.app.service.RuleLineService;
import org.wangfeng.panda.app.service.SingleRuleService;
import org.wangfeng.panda.app.service.excel.ExportTypeEnum;
import org.wangfeng.panda.app.service.excel.exportBiz.ExportBaseService;
import org.wangfeng.panda.app.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 规则的导入类
 *
 */
@Service
@Slf4j
public class ImportSingleRuleService extends ExportBaseService {


    @Autowired
    private SingleRuleService singleRuleService;
    @Autowired
    private RuleLineService ruleLineService;
    @Autowired
    private CellVariableService cellVariableService;

    /**
     * 导入规则（异步）
     * @param importMap
     * @param businessCode
     * @param errorCodeList
     */
    @Async
    public void importSingleRule(Map<String,List> importMap,
                                 String businessCode,
                                 List<String> errorCodeList) {

        log.info("import all single rule start，{}，{}，{}", JSON.toJSONString(importMap),JSON.toJSONString(businessCode),JSON.toJSONString(errorCodeList));

        List<TCaSingleRule> ruleList = importMap.get(ExportTypeEnum.SINGLE_RULE_EXPORT.getClassName());
        List<TCaRuleLine> lineList = importMap.get(ExportTypeEnum.RULE_LINE_EXPORT.getClassName());
        List<TCaCellVariable> cellList = importMap.get(ExportTypeEnum.CELL_VARIABLE_EXPORT.getClassName());

        //2、批量插入规则
        //2.1、先批量处理rule_code，businessCode，out_put_code
        ruleList.parallelStream().forEach(rule -> {
            if(StringUtils.isNotBlank(businessCode) && !rule.getBusinessCode().equals(businessCode) ){
                String ruleCode = rule.getRuleCode().replaceAll("RULE_SEQ_"+rule.getBusinessCode(),"RULE_SEQ_"+businessCode);
                String outPutCode = ruleCode + Constants._OUT_PUT;

                rule.setBusinessCode(businessCode);
                rule.setRuleCode(ruleCode);
                rule.setOutPutCode(outPutCode);

            }
        });
        //2.2、过滤掉报错的规则
        List<TCaSingleRule> finalRuleList = ruleList.parallelStream().filter( r -> !errorCodeList.contains(r.getRuleCode())).collect(Collectors.toList());

        //2.3、取出对应的报错的rule_code
        List<String> errorRuleCodeList = new ArrayList<>();
        errorRuleCodeList.addAll(errorCodeList);

        //2.4、导入剩余的规则
        StringBuffer ruleInfo = new StringBuffer();
        singleRuleService.batchInsertNotExist(finalRuleList,ruleInfo);
        String[] errorRuleCodeArr = ruleInfo.toString().split(Constants.DOUHAO);
        for(String s :errorRuleCodeArr){
            if(StringUtils.isNotBlank(s)){
                errorRuleCodeList.add(s);
            }
        }

        //3、批量插入行
        //3.1、先批量处理line_code,business_code,rule_code
        lineList.parallelStream().forEach(line -> {
            if(businessCode != null && !line.getBusinessCode().equals(businessCode) ){
                String lineCode = line.getLineCode().replaceAll("RULE_LINE_RULE_SEQ_"+line.getBusinessCode(),"RULE_LINE_RULE_SEQ_"+businessCode);
                String ruleCode = line.getRuleCode().replaceAll("RULE_SEQ_"+line.getBusinessCode(),"RULE_SEQ_"+businessCode);

                line.setLineCode(lineCode);
                line.setRuleCode(ruleCode);
                line.setBusinessCode(businessCode);
            }
        });
        //3.2、过滤掉报错的规则
        List<TCaRuleLine> finalLineList = lineList.parallelStream().filter( l -> !errorRuleCodeList.contains(l.getRuleCode())).collect(Collectors.toList());
        //3.3、取出对应的报错的line_code
        List<String> errorLineCodeList = new ArrayList<>();
        errorLineCodeList.addAll(lineList.parallelStream()
                .filter(l -> errorRuleCodeList.contains(l.getRuleCode()))
                .map(TCaRuleLine::getLineCode)
                .collect(Collectors.toList())
        );

        log.info("import all single rule finalLineList，{}，{}，{}", JSON.toJSONString(importMap),JSON.toJSONString(businessCode),JSON.toJSONString(finalLineList));

        //3.4、导入所有的剩余的行
        StringBuffer lineInfo = new StringBuffer();
        ruleLineService.batchInsertNotExist(finalLineList,lineInfo);
        String[] errorLineCodeArr = lineInfo.toString().split(Constants.DOUHAO);
        for(String s :errorLineCodeArr){
            if(StringUtils.isNotBlank(s)){
                errorLineCodeList.add(s);
            }
        }

        //4、批量插入格子
        //4.1、先批量处理cell_variable_code、rule_line_cdoe、business_code
        cellList.parallelStream().forEach(cell -> {
            if(businessCode != null && !cell.getBusinessCode().equals(businessCode) ) {
                String cellVariableCode = cell.getCellVariableCode().replaceAll("CELL_RULE_SEQ_"+cell.getBusinessCode(),"CELL_RULE_SEQ_"+businessCode);
                String ruleLineCode = cell.getRuleLineCode().replaceAll("RULE_LINE_RULE_SEQ_"+cell.getBusinessCode(),"CELL_RULE_SEQ_"+businessCode);

                cell.setBusinessCode(businessCode);
                cell.setCellVariableCode(cellVariableCode);
                cell.setRuleLineCode(ruleLineCode);
            }
        });

        log.info("import all single rule cellList，{}，{}，{}", JSON.toJSONString(importMap),JSON.toJSONString(businessCode),JSON.toJSONString(cellList));

        //4.2、过滤掉报错的行
        List<TCaCellVariable> finalCellList = cellList.stream().filter(c -> !errorLineCodeList.contains(c.getRuleLineCode())).collect(Collectors.toList());

        //4.3、取出对应的报错的line_code
        List<String> errorCellCodeList = new ArrayList<>();
        errorCellCodeList.addAll(cellList.parallelStream()
                .filter(cell -> errorLineCodeList.contains(cell.getRuleLineCode()))
                .map(TCaCellVariable::getCellVariableCode)
                .collect(Collectors.toList())
        );

        //4.4、导入所有的剩余的格子
        StringBuffer cellInfo = new StringBuffer();
        cellVariableService.batchInsertNotExist(finalCellList,cellInfo);

        //5、报错信息，打印到日志
        if(errorRuleCodeList!=null && errorRuleCodeList.size()>0){
            StringBuffer ruleSb = new StringBuffer();
            errorRuleCodeList.stream().forEach(code -> {
                ruleSb.append(code).append(",");
            });
            ruleSb.insert(0,"可能有问题的规则的Code为：");
            ruleSb.delete(ruleSb.length()-1,ruleSb.length());
            log.error(ruleSb.toString());

        }

        if(errorLineCodeList!=null && errorLineCodeList.size()>0){
            StringBuffer lineSb = new StringBuffer();
            errorLineCodeList.stream().forEach(code -> {
                lineSb.append(code).append(",");
            });
            lineSb.insert(0,"可能有问题的行的Code为：");
            lineSb.delete(lineSb.length()-1,lineSb.length());
            log.error(lineSb.toString());

        }


        if(errorCellCodeList!=null && errorCellCodeList.size()>0){
            StringBuffer cellSb = new StringBuffer();
            errorCellCodeList.stream().forEach(code -> {
                cellSb.append(code).append(",");
            });
            cellSb.insert(0,"可能有问题的格子的Code为：");
            cellSb.delete(cellSb.length()-1,cellSb.length());
            log.error(cellSb.toString());

        }

    }




}
