package org.wangfeng.panda.app.service;

import org.wangfeng.panda.app.dao.domain.TCaCellVariable;
import org.wangfeng.panda.app.model.vo.TCaRuleLineVO;

import java.util.List;

/**
 * 格子-参数 接口
 */
public interface CellVariableService {

    /**
     * 通过lineCode查询所有的变量格子
     * @return
     */
    public List<TCaCellVariable> queryCellListByLineCode(String lineCode);


    /**
     * 插入对应的格子
     * @param tCaCellVariable
     * @param tCaRuleLineVO
     * @param sort
     * @return
     */
    public Integer insertCellVariable(TCaCellVariable tCaCellVariable , TCaRuleLineVO tCaRuleLineVO , Integer sort);


    /**
     * 更新对应的格子
     * @param tCaCellVariable
     * @param tCaRuleLineVO
     * @param sort
     * @return
     */
    public Integer updateCellVariable(TCaCellVariable tCaCellVariable , TCaRuleLineVO tCaRuleLineVO , Integer sort);


    /**
     * 根据lineCode删除对应的格子
     * @param lineCode
     */
    public void deleteCell(String lineCode);



    /**
     * 停用对应的格子
     * @param lineCode
     */
    public void unUseCell(String lineCode);


    /**
     * 批量插入，并返回插入结果信息
     * @param tCaCellVariableList
     * @param info
     */
    public void batchInsertNotExist(List<TCaCellVariable> tCaCellVariableList , StringBuffer info);
}
