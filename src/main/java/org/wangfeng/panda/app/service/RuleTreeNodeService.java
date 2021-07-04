package org.wangfeng.panda.app.service;

import org.wangfeng.panda.app.dao.domain.TCaRuleTreeNode;
import org.wangfeng.panda.app.model.vo.TCaRuleTreeNodeVO;

import java.util.List;

public interface RuleTreeNodeService {

    /**
     * 通过treeId去查找所有的节点
     * @param ruleTreeCode
     * @return
     */
    public List<TCaRuleTreeNodeVO> queryAllNodeByTreeCode(String ruleTreeCode);


    /**
     * 通过TCaRuleTreeVO去插入对应的node数据
     * @param nodes
     * @param ruleTreeCode
     */
    public void insertByTCaRuleTreeVO(List<TCaRuleTreeNodeVO> nodes,String ruleTreeCode);


    /**
     * 通过treeId删除所有对应的node
     * @param ruleTreeCode
     */
    public void deleteByRuleTreeCode(String ruleTreeCode);

    /**
     * 批量插入，并返回插入结果信息
     * @param tCaRuleTreeNodeList
     * @param info
     */
    public void batchInsertNotExist(List<TCaRuleTreeNode> tCaRuleTreeNodeList , StringBuffer info);
}
