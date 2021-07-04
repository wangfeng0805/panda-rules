package org.wangfeng.panda.app.service.excel.exportBiz;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wangfeng.panda.app.common.Constants;
import org.wangfeng.panda.app.dao.domain.TCaCellVariable;
import org.wangfeng.panda.app.dao.domain.TCaRuleLine;
import org.wangfeng.panda.app.dao.domain.TCaSingleRule;
import org.wangfeng.panda.app.dao.mapper.TCaCellVariableMapper;
import org.wangfeng.panda.app.dao.mapper.TCaRuleLineMapper;
import org.wangfeng.panda.app.dao.mapper.TCaSingleRuleMapper;
import org.wangfeng.panda.app.service.excel.ExcelUtil;
import org.wangfeng.panda.app.service.excel.ExportTypeEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 规则的导出类
 */
@Service
@Slf4j
public class ExportSingleRuleService extends ExportBaseService{


    @Autowired
    private TCaSingleRuleMapper tCaSingleRuleMapper;
    @Autowired
    private TCaRuleLineMapper tCaRuleLineMapper;
    @Autowired
    private TCaCellVariableMapper tCaCellVariableMapper;


    /**
     * 导出规则，如果传入id集合则就导出特定的规则，否则全量导出
     * @param codeList
     * @param wb
     */
    public void exportSingleRule(List<String> codeList,XSSFWorkbook wb){

        List<TCaSingleRule> exportRuleList = new ArrayList<>();
        List<TCaRuleLine> exportRuleLineList = new ArrayList<>();
        List<TCaCellVariable> exportCellList = new ArrayList<>();

        //1、找出所有的规则
        if(codeList == null || codeList.size() == 0){
            exportRuleList =  tCaSingleRuleMapper.queryAll(new TCaSingleRule());
        }else {
            exportRuleList = tCaSingleRuleMapper.queryByIdList(codeList);
        }

        //2、找出所有的行
        Set<String> ruleCodeSet = new TreeSet<>();
        exportRuleList.stream().forEach(rule -> {
            ruleCodeSet.add(rule.getRuleCode());
        });
        List<String> ruleCodeList = new ArrayList<>(ruleCodeSet);


        Integer ruleCount = ruleCodeList.size()%500==0?
                ruleCodeList.size()/Constants.BATCH_INSERT_SIZE_500:
                ruleCodeList.size()/Constants.BATCH_INSERT_SIZE_500+1;

        for(int i=0 ; i< ruleCount ; i++){
            Integer start = i*Constants.BATCH_INSERT_SIZE_500;
            Integer end = (i+1)*Constants.BATCH_INSERT_SIZE_500>ruleCodeList.size()?ruleCodeList.size():(i+1)*Constants.BATCH_INSERT_SIZE_500;

            List<String> subRuleCodeList = ruleCodeList.subList(start,end);

            List<TCaRuleLine> subRuleLineList = tCaRuleLineMapper.queryLineListByRuleCodeList(subRuleCodeList);

            exportRuleLineList.addAll(subRuleLineList);
        }


        //3、找出所有的格子
        Set<String> lineCodeSet = new TreeSet<>();
        exportRuleLineList.stream().forEach(line -> {
            lineCodeSet.add(line.getLineCode());
        });
        List<String> lineCodeList = new ArrayList<>(lineCodeSet);
        Integer lineCount = lineCodeList.size()%500==0?
                lineCodeList.size()/Constants.BATCH_INSERT_SIZE_500:
                lineCodeList.size()/Constants.BATCH_INSERT_SIZE_500+1;

        for(int i=0 ; i< lineCount ; i++){
            Integer start = i*Constants.BATCH_INSERT_SIZE_500;
            Integer end = (i+1)*Constants.BATCH_INSERT_SIZE_500>lineCodeList.size()?lineCodeList.size():(i+1)*Constants.BATCH_INSERT_SIZE_500;

            List<String> subLineCodeList = lineCodeList.subList(start,end);

            List<TCaCellVariable> subCellVaiableList = tCaCellVariableMapper.queryCellListByLineCodeList(subLineCodeList);

            exportCellList.addAll(subCellVaiableList);
        }


        //4、得到sheet页面的header，并生成excel
        String ruleSheetName = ExportTypeEnum.SINGLE_RULE_EXPORT.getCNName();
        List<String> ruleHeaders = ExcelUtil.getHeaders(TCaSingleRule.class);
        wb = ExcelUtil.getXSSFWorkbook(ruleSheetName, ruleHeaders, exportRuleList, wb,TCaSingleRule.class);

        String lineSheetName = ExportTypeEnum.RULE_LINE_EXPORT.getCNName();
        List<String> lineHeaders = ExcelUtil.getHeaders(TCaRuleLine.class);
        wb = ExcelUtil.getXSSFWorkbook(lineSheetName, lineHeaders, exportRuleLineList, wb,TCaRuleLine.class);

        String cellSheetName = ExportTypeEnum.CELL_VARIABLE_EXPORT.getCNName();
        List<String> cellHeaders = ExcelUtil.getHeaders(TCaCellVariable.class);
        wb = ExcelUtil.getXSSFWorkbook(cellSheetName, cellHeaders, exportCellList, wb,TCaCellVariable.class);

    }

}
