package org.wangfeng.panda.app.service;

import org.wangfeng.panda.app.dao.domain.TCaRuleTreeMapping;
import org.wangfeng.panda.app.model.vo.TCaRuleTreeMappingVO;

import java.util.List;

public interface RuleTreeMappingService {


    /**
     * 通过treeId去查找所有的关联关系
     * @param treeCode
     * @return
     */
    public List<TCaRuleTreeMapping> queryAllMappingByTreeCode(String treeCode);


    /**
     * 通过TCaRuleTreeVO去插入对应的connectors数据
     * @param connectors
     * @param ruleTreeCode
     */
    public void insertByTCaRuleTreeVO(List<TCaRuleTreeMappingVO> connectors, String ruleTreeCode);


    /**
     * 通过treeId删除所有对应的mapping关系
     * @param ruleTreeCode
     */
    public void deleteByRuleTreeCode(String ruleTreeCode);


    /**
     * 批量插入，并返回插入结果信息
     * @param tCaRuleTreeMappingList
     * @param info
     */
    public void batchInsertNotExist(List<TCaRuleTreeMapping> tCaRuleTreeMappingList , StringBuffer info);
}
