package org.wangfeng.panda.app.service;


import com.alibaba.fastjson.JSONObject;
import org.wangfeng.panda.app.common.Paginate;
import org.wangfeng.panda.app.dao.domain.TCaRuleList;
import org.wangfeng.panda.app.model.vo.TCaRuleListVO;

import java.util.List;

public interface RuleListService {

    /**
     * 查找列表页
     * @param tCaRuleListVO
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Paginate queryPagenate(TCaRuleListVO tCaRuleListVO, Integer pageNo, Integer pageSize);

    /**
     * 根据id查找对应的对象
     *
     * @param id
     * @return
     */
    public TCaRuleListVO getById(Long id);

    /**
     * 根据code查找对应的对象
     *
     * @param code
     * @return
     */
    public TCaRuleListVO getByCode(String code);
    /**
     * 批量插入决策变量,其实质是批量更新
     *
     * @param tCaRuleListVO
     * @return
     */
    public void insert (TCaRuleListVO tCaRuleListVO);


    /**
     * 更新
     *
     * @param tCaRuleListVO
     * @return
     */
    public void updateByPrimaryKey(TCaRuleListVO tCaRuleListVO);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public void delete(Long id);


    /**
     * 通过规则集ID去计算对应的结果
     *
     * @param id
     * @param ruleListCode
     * @param jsonObject
     */
    public JSONObject calculateRuleListByIdORCode(Long id,String ruleListCode ,JSONObject jsonObject);


    /**
     * 批量插入，并返回插入结果信息
     * @param tCaRuleListList
     * @param info
     */
    public void batchInsertNotExist(List<TCaRuleList> tCaRuleListList , StringBuffer info);
}
