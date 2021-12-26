package org.wangfeng.panda.app.service.excel.importBiz;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.wangfeng.panda.app.common.Constants;
import org.wangfeng.panda.app.dao.domain.TCaRuleTree;
import org.wangfeng.panda.app.dao.domain.TCaRuleTreeMapping;
import org.wangfeng.panda.app.dao.domain.TCaRuleTreeNode;
import org.wangfeng.panda.app.service.RuleTreeMappingService;
import org.wangfeng.panda.app.service.RuleTreeNodeService;
import org.wangfeng.panda.app.service.RuleTreeService;
import org.wangfeng.panda.app.service.excel.ExportTypeEnum;
import org.wangfeng.panda.app.service.excel.exportBiz.ExportBaseService;
import org.wangfeng.panda.app.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 决策树的导入类
 *
 */
@Service
@Slf4j
public class ImportRuleTreeService extends ExportBaseService {

    @Autowired
    private RuleTreeService ruleTreeService;
    @Autowired
    private RuleTreeNodeService ruleTreeNodeService;
    @Autowired
    private RuleTreeMappingService ruleTreeMappingService;
    @Autowired
    private ImportRuleListService importRuleListService;


    /**
     * 导入决策树
     * @param importMap
     * @param businessCode
     * @param errorRuleTreeCodeList
     */
    @Async
    public void importRuleTree(Map<String,List> importMap,
                               String businessCode,
                               List<String> errorRuleTreeCodeList){
        log.info("import all rule tree，{}，{}，{}", JSON.toJSONString(importMap),JSON.toJSONString(businessCode),JSON.toJSONString(errorRuleTreeCodeList));

        //1、先获取所有的List
        List<TCaRuleTree> treeList = importMap.get(ExportTypeEnum.RULE_TREE_EXPORT.getClassName());
        List<TCaRuleTreeNode> treeNodeList = importMap.get(ExportTypeEnum.RULE_TREE_NODE_EXPORT.getClassName());
        List<TCaRuleTreeMapping> treeMappingList = importMap.get(ExportTypeEnum.RULE_TREE_MAPPING_EXPORT.getClassName());

        //2、插入决策树开始
        //2.1、先进行tree的businessCode和ruleTreeCode的更改
        treeList.parallelStream().forEach(tree -> {
            if(StringUtils.isNotBlank(businessCode) && !tree.getBusinessCode().equals(businessCode)){
                String ruleTreeCode = tree.getRuleTreeCode().replaceAll("RULE_TREE_SEQ_"+tree.getBusinessCode(),"RULE_TREE_SEQ_"+businessCode);
                tree.setBusinessCode(businessCode);
                tree.setRuleTreeCode(ruleTreeCode);
            }
        });

        log.info("import all rule treeList，{}，{}，{}", JSON.toJSONString(importMap),JSON.toJSONString(businessCode),JSON.toJSONString(treeList));

        //2.2、把决策树中的错误的集合，删除掉
        List<TCaRuleTree> finalTreeList = treeList.stream().filter(tree -> !errorRuleTreeCodeList.contains(tree.getRuleTreeCode())).collect(Collectors.toList());

        //2.3、导入剩余的决策树
        StringBuffer treeInfo = new StringBuffer();
        ruleTreeService.batchInsertNotExist(finalTreeList,treeInfo);
        String[] errorTreeCodeArr = treeInfo.toString().split(Constants.DOUHAO);
        for(String s :errorTreeCodeArr){
            if(StringUtils.isNotBlank(s)){
                errorRuleTreeCodeList.add(s);
            }
        }

        //3、批量插入tree_node
        //3.1、先进行node的 rule_tree_code 和 node_rule_code的变更
        treeNodeList.parallelStream().forEach(node -> {
            if(StringUtils.isNotBlank(businessCode) ){
                String oldBusinessCode = node.getRuleTreeCode().substring(14,node.getRuleTreeCode().length()-13);

                String ruleTreeCode = node.getRuleTreeCode().replaceAll("RULE_TREE_SEQ_"+oldBusinessCode,"RULE_TREE_SEQ_"+businessCode);
                node.setRuleTreeCode(ruleTreeCode);
                if(node.getType().equals("规则")){
                    String nodeRuleCode = node.getNodeRuleCode().replaceAll("RULE_SEQ_"+oldBusinessCode,"RULE_LIST_SEQ_"+businessCode);
                    node.setNodeRuleCode(nodeRuleCode);
                }else if(node.getType().equals("规则集")){
                    String nodeRuleCode = node.getNodeRuleCode().replaceAll("RULE_LIST_SEQ_"+oldBusinessCode,"RULE_SEQ_"+businessCode);
                    node.setNodeRuleCode(nodeRuleCode);
                }
            }
        });

        //3.2、把决策树中的错误的node，删除掉
        List<TCaRuleTreeNode> finalTreeNodeList = treeNodeList.stream()
                .filter(node -> !errorRuleTreeCodeList.contains(node.getRuleTreeCode()))
                .collect(Collectors.toList());

        //3.3、把有问题的node的ID整理出来
        List<String> errorRuleTreeNodeCodeList = treeNodeList.stream()
                .filter(node -> errorRuleTreeCodeList.contains(node.getRuleTreeCode()))
                .map(TCaRuleTreeNode::getNodeId)
                .collect(Collectors.toList());


        //3.4、导入剩余的node
        StringBuffer nodeInfo = new StringBuffer();
        ruleTreeNodeService.batchInsertNotExist(finalTreeNodeList,nodeInfo);
        String[] errorNodeCodeArr = treeInfo.toString().split(Constants.DOUHAO);
        for(String s :errorNodeCodeArr){
            if(StringUtils.isNotBlank(s)){
                errorRuleTreeNodeCodeList.add(s);
            }
        }

        //4、批量插入tree_mapping
        //4.1、先进行mapping的rule_tree_code的变更
        treeMappingList.parallelStream().forEach(mapping -> {
            if(StringUtils.isNotBlank(businessCode)  ){
                String oldBusinessCode = mapping.getRuleTreeCode().substring(14,mapping.getRuleTreeCode().length()-13);
                String ruleTreeCode = mapping.getRuleTreeCode().replaceAll("RULE_TREE_SEQ_"+oldBusinessCode,"RULE_TREE_SEQ_"+businessCode);
                mapping.setRuleTreeCode(ruleTreeCode);
            }
        });

        //3.2、把决策树中的错误的mapping，删除掉
        List<TCaRuleTreeMapping> finalTreeMappingList = treeMappingList.stream()
                .filter(mapping -> !errorRuleTreeCodeList.contains(mapping.getRuleTreeCode()))
                .collect(Collectors.toList());

        //3.3、把有问题的node的ID整理出来
        List<String> errorRulkeTreeMappingCodeList = treeMappingList.stream()
                .filter(mapping -> errorRuleTreeCodeList.contains(mapping.getRuleTreeCode()))
                .map(TCaRuleTreeMapping::getSourceNodeId)
                .collect(Collectors.toList());

        //4.4、导入剩余的mapping
        StringBuffer mappingInfo = new StringBuffer();
        ruleTreeMappingService.batchInsertNotExist(finalTreeMappingList,mappingInfo);
        String[] errorMappingCodeArr = treeInfo.toString().split(Constants.DOUHAO);
        for(String s :errorMappingCodeArr){
            if(StringUtils.isNotBlank(s)){
                errorRulkeTreeMappingCodeList.add(s);
            }
        }

        log.info("========================== import rule tree mapping end ==========================");

        //5、打印出有问题的Code
        if(errorRuleTreeCodeList!=null && errorRuleTreeCodeList.size()>0){
            StringBuffer ruleTreeSb = new StringBuffer();
            errorRuleTreeCodeList.stream().forEach(code -> {
                ruleTreeSb.append(code).append(",");
            });
            ruleTreeSb.insert(0,"可能有问题的决策树的Code为：");
            ruleTreeSb.delete(ruleTreeSb.length()-1,ruleTreeSb.length());
            log.error(ruleTreeSb.toString());
        }
        if(errorRuleTreeNodeCodeList!=null && errorRuleTreeNodeCodeList.size()>0){
            StringBuffer ruleTreeNodeSb = new StringBuffer();
            errorRuleTreeNodeCodeList.stream().forEach(code -> {
                ruleTreeNodeSb.append(code).append(",");
            });
            ruleTreeNodeSb.insert(0,"可能有问题的决策树节点ID为：");
            ruleTreeNodeSb.delete(ruleTreeNodeSb.length()-1,ruleTreeNodeSb.length());
            log.error(ruleTreeNodeSb.toString());
        }
        if(errorRulkeTreeMappingCodeList!=null && errorRulkeTreeMappingCodeList.size()>0){
            StringBuffer ruleTreeMappingSb = new StringBuffer();
            errorRulkeTreeMappingCodeList.stream().forEach(code -> {
                ruleTreeMappingSb.append(code).append(",");
            });
            ruleTreeMappingSb.insert(0,"可能有问题的决策树映射的Code为：");
            ruleTreeMappingSb.delete(ruleTreeMappingSb.length()-1,ruleTreeMappingSb.length());
            log.error(ruleTreeMappingSb.toString());
        }


        //5、继续导入规则集合， 因为规则集和决策树没有明确的一对一的对应关系，所以选择全量导入规则
        importRuleListService.importRuleList(importMap,businessCode,new ArrayList<>());

        log.info("========================== import all rule tree end ==========================");


    }



}

