package org.wangfeng.panda.app.service;

import org.wangfeng.panda.app.dao.domain.TCaRuleListMapping;
import org.wangfeng.panda.app.model.vo.TCaSingleRuleVO;

import java.util.List;

public interface RuleListMappingService {

    /**
     * 根据规则集ID去删除对应的映射关系
     * @param ruleListId
     */
    public void deleteByRuleListId(Long ruleListId);

    /**
     * 根据规则集CODE去删除对应的映射关系
     * @param ruleListCode
     */
    public void deleteByRuleListCode(String ruleListCode);


    /**
     * 批量插入映射关系
     * @param tCaSingleRuleVOList
     */
    public void insertByTCaSingleRuleVOList(List<TCaSingleRuleVO> tCaSingleRuleVOList,String ruleListCode);

    /**
     * 批量插入，并返回插入结果信息
     * @param tCaRuleListMappingList
     * @param info
     */
    public void batchInsertNotExist(List<TCaRuleListMapping> tCaRuleListMappingList , StringBuffer info);
}
