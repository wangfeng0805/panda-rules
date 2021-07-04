package org.wangfeng.panda.app.service;


import org.wangfeng.panda.app.common.Paginate;
import org.wangfeng.panda.app.dao.domain.TCaDecisionVariable;
import org.wangfeng.panda.app.model.vo.TCaDecisionVariableVO;

import java.util.List;

public interface DecisionVariableService {

    /**
     * 查找列表页
     *
     * @param tCaDecisionVariableVO
     * @return
     */
    public Paginate queryPagenate(TCaDecisionVariableVO tCaDecisionVariableVO, Integer pageNo, Integer pageSize);

    /**
     * 根据id查找对应的对象
     *
     * @param id
     * @return
     */
    public TCaDecisionVariableVO getById(Long id);

    /**
     * 批量插入决策变量,其实质是批量更新
     *
     * @param tCaDecisionVariableList
     * @return
     */
    public List<String> batchAddDecisionVariable(List<TCaDecisionVariable> tCaDecisionVariableList);


    /**
     * 更新
     *
     * @param tCaDecisionVariable
     * @return
     */
    public Integer updateByPrimaryKey(TCaDecisionVariable tCaDecisionVariable);

    /**
     * 删除对应的
     *
     * @param id
     * @return
     */
    public Integer delete(Long id);

    /**
     * 通过codeList获取所有的决策变量
     * @param codeList
     * @return
     */
    public List<TCaDecisionVariableVO> queryListByCodeList(List<String> codeList);


    /**
     * 批量插入，并返回插入结果信息
     * @param tCaDecisionVariableList
     * @param info
     */
    public void batchInsertNotExist(List<TCaDecisionVariable> tCaDecisionVariableList , StringBuffer info);


}
