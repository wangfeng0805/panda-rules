package org.wangfeng.panda.app.service.excel.exportBiz;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wangfeng.panda.app.common.Constants;
import org.wangfeng.panda.app.dao.domain.TCaRuleList;
import org.wangfeng.panda.app.dao.domain.TCaRuleListMapping;
import org.wangfeng.panda.app.dao.mapper.TCaRuleListMapper;
import org.wangfeng.panda.app.dao.mapper.TCaRuleListMappingMapper;
import org.wangfeng.panda.app.service.excel.ExcelUtil;
import org.wangfeng.panda.app.service.excel.ExportTypeEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExportRuleListService extends ExportBaseService{

    @Autowired
    private TCaRuleListMapper tCaRuleListMapper;
    @Autowired
    private TCaRuleListMappingMapper tCaRuleListMappingMapper;

    /**
     * 导出所有的ruleList
     * @param codeList
     * @param wb
     */
    public Map<String,List> exportRuleList(List<String> codeList, XSSFWorkbook wb){
        log.info("exportRuleList，开始，{}，{}", JSON.toJSONString(codeList),JSON.toJSONString(wb));

        //1、创建新的对象
        List<TCaRuleList> exportRuleListList = new ArrayList<>();
        List<TCaRuleListMapping> exportRuleListMappingList = new ArrayList<>();


        if(codeList == null || codeList.size()==0 ){

            exportRuleListList = tCaRuleListMapper.queryAll(new TCaRuleList());
            exportRuleListMappingList = tCaRuleListMappingMapper.queryAll(new TCaRuleListMapping());
        }else{

            //2、计算次数
            Integer ruleListCount = codeList.size()%500==0?
                    codeList.size()/ Constants.BATCH_INSERT_SIZE_500:
                    codeList.size()/Constants.BATCH_INSERT_SIZE_500+1;

            //3、循环获取list
            for(int i=0 ; i< ruleListCount ; i++){
                Integer start = i*Constants.BATCH_INSERT_SIZE_500;
                Integer end = (i+1)*Constants.BATCH_INSERT_SIZE_500>codeList.size()?codeList.size():(i+1)*Constants.BATCH_INSERT_SIZE_500;

                List<String> subRuleListCodeList = codeList.subList(start,end);

                List<TCaRuleList> subRuleLineList = tCaRuleListMapper.queryListByCodeList(subRuleListCodeList);
                List<TCaRuleListMapping> subMappingList = tCaRuleListMappingMapper.queryListByCodeList(subRuleListCodeList);


                exportRuleListList.addAll(subRuleLineList);
                exportRuleListMappingList.addAll(subMappingList);
            }

        }
        log.info("exportRuleList，中间数据，{}，{}，{}", JSON.toJSONString(codeList),JSON.toJSONString(exportRuleListMappingList),JSON.toJSONString(exportRuleListList));

        //4、得到sheet页面的header，并生成excel
        String ruleListSheetName = ExportTypeEnum.RULE_LIST_EXPORT.getCNName();
        List<String> ruleListHeaders = ExcelUtil.getHeaders(TCaRuleList.class);
        wb = ExcelUtil.getXSSFWorkbook(ruleListSheetName, ruleListHeaders, exportRuleListList, wb,TCaRuleList.class);

        String ruleListMappingSheetName = ExportTypeEnum.RULE_LIST_MAPPING_EXPORT.getCNName();
        List<String> ruleListMappingHeaders = ExcelUtil.getHeaders(TCaRuleListMapping.class);
        wb = ExcelUtil.getXSSFWorkbook(ruleListMappingSheetName, ruleListMappingHeaders, exportRuleListMappingList, wb,TCaRuleListMapping.class);


        //5、组装返回结果
        List<String> ruleCodeList = exportRuleListMappingList.stream().map(TCaRuleListMapping::getRuleCode).collect(Collectors.toList());
        Map<String,List> resultMap = new HashMap<String,List>(){{
            put("ruleCodeList",ruleCodeList);
        }};

        log.info("exportRuleList，返回，{}，{}", JSON.toJSONString(codeList),JSON.toJSONString(resultMap));
        return resultMap;

    }

}
