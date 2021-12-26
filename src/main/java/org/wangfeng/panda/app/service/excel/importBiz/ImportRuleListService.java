package org.wangfeng.panda.app.service.excel.importBiz;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.wangfeng.panda.app.common.Constants;
import org.wangfeng.panda.app.dao.domain.TCaRuleList;
import org.wangfeng.panda.app.dao.domain.TCaRuleListMapping;
import org.wangfeng.panda.app.service.RuleListMappingService;
import org.wangfeng.panda.app.service.RuleListService;
import org.wangfeng.panda.app.service.excel.ExportTypeEnum;
import org.wangfeng.panda.app.service.excel.exportBiz.ExportBaseService;
import org.wangfeng.panda.app.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 规则集的导入类
 *
 */
@Service
@Slf4j
public class ImportRuleListService extends ExportBaseService {

    @Autowired
    private RuleListService ruleListService;
    @Autowired
    private RuleListMappingService ruleListMappingService;

    @Autowired
    private ImportSingleRuleService importSingleRuleService;


    /**
     * 导入规则集（异步）
     * @param importMap             需要导入的map
     * @param businessCode          需要导入的业务线CODE
     * @param errorRuleListCodeList         有异常的规则集编号List
     */
    @Async
    public void importRuleList(Map<String,List> importMap,
                               String businessCode,
                               List<String> errorRuleListCodeList){

        log.info("import all rule rulelist，{}，{}，{}", JSON.toJSONString(importMap),JSON.toJSONString(businessCode),JSON.toJSONString(errorRuleListCodeList));

        //1、先获取所有的List
        List<TCaRuleList> ruleListList = importMap.get(ExportTypeEnum.RULE_LIST_EXPORT.getClassName());
        List<TCaRuleListMapping> ruleListMappingList = importMap.get(ExportTypeEnum.RULE_LIST_MAPPING_EXPORT.getClassName());

        //2、批量插入list
        //2.1、先批量处理businessCode，rule_list_code
        ruleListList.parallelStream().forEach(list -> {
            if(StringUtils.isNotBlank(businessCode)  && !list.getBusinessCode().equals(businessCode) ){
                String ruleListCode = list.getRuleListCode().replaceAll("RULE_LIST_SEQ_"+list.getBusinessCode(),"RULE_LIST_SEQ_"+businessCode);

                list.setBusinessCode(businessCode);
                list.setRuleListCode(ruleListCode);
            }
        });

        //2.2、把规则集中的错误的集合，删除掉
        List<TCaRuleList> finalList = ruleListList.stream().filter(list -> !errorRuleListCodeList.contains(list.getRuleListCode())).collect(Collectors.toList());

        //2.4、导入剩余的规则集
        StringBuffer listInfo = new StringBuffer();
        ruleListService.batchInsertNotExist(finalList,listInfo);
        String[] errorListCodeArr = listInfo.toString().split(Constants.DOUHAO);
        for(String s :errorListCodeArr){
            if(StringUtils.isNotBlank(s)){
                errorRuleListCodeList.add(s);
            }
        }

        log.info("import all rule rulelist，{}，{}，{}", JSON.toJSONString(importMap),JSON.toJSONString(businessCode),JSON.toJSONString(listInfo));


        //3、批量插入list_mapping
        //3.1、先批量处理rule_code，rule_list_code
        ruleListMappingList.parallelStream().forEach(mapping -> {
            if(StringUtils.isNotBlank(businessCode)  ){
                String oldBusinessCode = mapping.getRuleCode().substring(9,mapping.getRuleCode().length()-13);
                String ruleListCode = mapping.getRuleListCode().replaceAll("RULE_LIST_SEQ_"+oldBusinessCode,"RULE_LIST_SEQ_"+businessCode);
                String ruleCode = mapping.getRuleCode().replaceAll("RULE_SEQ_"+oldBusinessCode,"RULE_SEQ_"+businessCode);
                mapping.setRuleListCode(ruleListCode);
                mapping.setRuleCode(ruleCode);
            }
        });

        //3.2、把规则集中的错误的集合，删除掉
        List<TCaRuleListMapping> finalMapppingList = ruleListMappingList.stream().filter(mapping -> !errorRuleListCodeList.contains(mapping.getRuleListCode())).collect(Collectors.toList());

        //3.3、把有问题的mapping的CODE整理出来
        List<String> errorRuleCodeList = ruleListMappingList.stream()
                .filter(mapping -> errorRuleListCodeList.contains(mapping.getRuleListCode()))
                .map(TCaRuleListMapping::getRuleCode)
                .collect(Collectors.toList());


        //3.4、导入所有的剩余的映射
        StringBuffer mappingInfo = new StringBuffer();
        ruleListMappingService.batchInsertNotExist(finalMapppingList,mappingInfo);
        String[] errorRuleCodeArr = mappingInfo.toString().split(Constants.DOUHAO);
        for(String s :errorRuleCodeArr){
            if(StringUtils.isNotBlank(s)){
                errorRuleCodeList.add(s);
            }
        }

        log.info("========================== import rule list mapping end ==========================");


        //4、打印出有问题的Code
        if(errorRuleListCodeList!=null && errorRuleListCodeList.size()>0){
            StringBuffer ruleListSb = new StringBuffer();
            errorRuleListCodeList.stream().forEach(code -> {
                ruleListSb.append(code).append(",");
            });
            ruleListSb.insert(0,"可能有问题的规则集的Code为：");
            ruleListSb.delete(ruleListSb.length()-1,ruleListSb.length());
            log.error(ruleListSb.toString());
        }

        if(errorRuleCodeList!=null && errorRuleCodeList.size()>0){
            StringBuffer ruleCodeSb = new StringBuffer();
            errorRuleCodeList.stream().forEach(code -> {
                ruleCodeSb.append(code).append(",");
            });
            ruleCodeSb.insert(0,"规则集映射中可能有问题的规则的Code为：");
            ruleCodeSb.delete(ruleCodeSb.length()-1,ruleCodeSb.length());
            log.error(ruleCodeSb.toString());
        }

        //5、继续导入规则， 因为规则和规则集没有明确的一对一的对应关系，所以选择全量导入规则
        importSingleRuleService.importSingleRule(importMap,businessCode,new ArrayList<>());

        log.info("========================== import all rule list end ==========================");

    }

}
