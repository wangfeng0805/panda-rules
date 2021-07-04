package org.wangfeng.panda.app.service.excel.exportBiz;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wangfeng.panda.app.common.Constants;
import org.wangfeng.panda.app.dao.domain.TCaRuleTree;
import org.wangfeng.panda.app.dao.domain.TCaRuleTreeMapping;
import org.wangfeng.panda.app.dao.domain.TCaRuleTreeNode;
import org.wangfeng.panda.app.dao.mapper.TCaRuleTreeMapper;
import org.wangfeng.panda.app.dao.mapper.TCaRuleTreeMappingMapper;
import org.wangfeng.panda.app.dao.mapper.TCaRuleTreeNodeMapper;
import org.wangfeng.panda.app.service.excel.ExcelUtil;
import org.wangfeng.panda.app.service.excel.ExportTypeEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExportRuleTreeService extends ExportBaseService{

    @Autowired
    private TCaRuleTreeMapper tCaRuleTreeMapper;
    @Autowired
    private TCaRuleTreeNodeMapper tCaRuleTreeNodeMapper;
    @Autowired
    private TCaRuleTreeMappingMapper tCaRuleTreeMappingMapper;


    /**
     * 导出所有的ruleTree
     * @param codeList
     * @param wb
     */
    public Map<String,List> exportRuleTree(List<String> codeList, XSSFWorkbook wb){

        //1、创建新的对象
        List<TCaRuleTree> exportTreeList = new ArrayList<>();
        List<TCaRuleTreeNode> exportNodeList = new ArrayList<>();
        List<TCaRuleTreeMapping> exportMappingList = new ArrayList<>();


        if(codeList == null || codeList.size() == 0){
            exportTreeList = tCaRuleTreeMapper.queryAll(new TCaRuleTree());
            exportNodeList = tCaRuleTreeNodeMapper.queryAll(new TCaRuleTreeNode());
            exportMappingList = tCaRuleTreeMappingMapper.queryAll(new TCaRuleTreeMapping());
        }else {
            //2、计算次数
            Integer treeCount = codeList.size()%500==0?
                    codeList.size()/ Constants.BATCH_INSERT_SIZE_500:
                    codeList.size()/Constants.BATCH_INSERT_SIZE_500+1;


            //3、循环获取list
            for(int i=0 ; i< treeCount ; i++){
                Integer start = i*Constants.BATCH_INSERT_SIZE_500;
                Integer end = (i+1)*Constants.BATCH_INSERT_SIZE_500>codeList.size()?codeList.size():(i+1)*Constants.BATCH_INSERT_SIZE_500;

                List<String> subCodeList = codeList.subList(start,end);

                List<TCaRuleTree> subTreeList = tCaRuleTreeMapper.queryListByTreeCodeList(subCodeList);
                List<TCaRuleTreeNode> subNodeList = tCaRuleTreeNodeMapper.queryListByTreeCodeList(subCodeList);
                List<TCaRuleTreeMapping> subMappingList = tCaRuleTreeMappingMapper.queryListByTreeCodeList(subCodeList);

                exportTreeList.addAll(subTreeList);
                exportNodeList.addAll(subNodeList);
                exportMappingList.addAll(subMappingList);

            }

        }

        //4、得到sheet页面的header，并生成excel
        String ruleTreeSheetName = ExportTypeEnum.RULE_TREE_EXPORT.getCNName();
        List<String> ruleTreeHeaders = ExcelUtil.getHeaders(TCaRuleTree.class);
        wb = ExcelUtil.getXSSFWorkbook(ruleTreeSheetName, ruleTreeHeaders, exportTreeList, wb,TCaRuleTree.class);

        String ruleTreeNodeSheetName = ExportTypeEnum.RULE_TREE_NODE_EXPORT.getCNName();
        List<String> ruleTreeNodeHeaders = ExcelUtil.getHeaders(TCaRuleTreeNode.class);
        wb = ExcelUtil.getXSSFWorkbook(ruleTreeNodeSheetName, ruleTreeNodeHeaders, exportNodeList, wb,TCaRuleTreeNode.class);

        String ruleTreeMappingSheetName = ExportTypeEnum.RULE_TREE_MAPPING_EXPORT.getCNName();
        List<String> ruleTreeMappingHeaders = ExcelUtil.getHeaders(TCaRuleTreeMapping.class);
        wb = ExcelUtil.getXSSFWorkbook(ruleTreeMappingSheetName, ruleTreeMappingHeaders, exportMappingList, wb,TCaRuleTreeMapping.class);

        //5、组装返回结果
        List<String> ruleCodeList = exportNodeList.stream().filter(node -> node.getType().equals("规则")).map(TCaRuleTreeNode::getNodeRuleCode).collect(Collectors.toList());
        List<String> ruleListCodeList = exportNodeList.stream().filter(node -> node.getType().equals("规则集")).map(TCaRuleTreeNode::getNodeRuleCode).collect(Collectors.toList());
        Map<String,List> resultMap = new HashMap<String,List>(){{
            put("ruleCodeList",ruleCodeList);
            put("ruleListCodeList",ruleListCodeList);
        }};

        return resultMap;

    }



}
