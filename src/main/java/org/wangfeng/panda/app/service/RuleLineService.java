package org.wangfeng.panda.app.service;

import org.wangfeng.panda.app.dao.domain.TCaRuleLine;
import org.wangfeng.panda.app.model.vo.TCaRuleLineVO;
import org.wangfeng.panda.app.model.vo.TCaSingleRuleVO;

import java.util.List;

/**
 * 规则行 接口
 */
public interface RuleLineService {

    /**
     * 通过规则CODE查询当前规则对应的所有行
     * @param ruleCode
     * @return
     */
    public List<TCaRuleLineVO> queryLineListByRuleCode(String ruleCode);


    /**
     * 插入单条行
     * @param tCaRuleLineVO
     * @param type
     * @param sort
     */
    public void insertLine(TCaRuleLineVO tCaRuleLineVO , TCaSingleRuleVO tCaSingleRuleVO , String type , Integer sort);


    /**
     * 更新单条行
     * @param tCaRuleLineVO
     * @param tCaSingleRuleVO
     * @param type
     * @param sort
     */
    public void updateLine(TCaRuleLineVO tCaRuleLineVO , TCaSingleRuleVO tCaSingleRuleVO , String type , Integer sort);


    /**
     * 删除行
     * @param ruleCode
     */
    public void deleteLine(String ruleCode);


    /**
     * 停用行
     * @param ruleCode
     */
    public void unUseLine(String ruleCode);


    /**
     * 批量插入，并返回插入结果信息
     * @param tCaRuleLineList
     * @param info
     */
    public void batchInsertNotExist(List<TCaRuleLine> tCaRuleLineList , StringBuffer info);
}
