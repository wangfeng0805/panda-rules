package org.wangfeng.panda.app.service;

import com.alibaba.fastjson.JSONObject;
import org.wangfeng.panda.app.common.Paginate;
import org.wangfeng.panda.app.dao.domain.TCaRuleTree;
import org.wangfeng.panda.app.model.vo.TCaRuleTreeVO;

import java.util.List;

public interface RuleTreeService {

    /**
     * 查找列表页
     * @param tCaRuleTreeVO
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Paginate queryPagenate(TCaRuleTreeVO tCaRuleTreeVO, Integer pageNo, Integer pageSize);


    /**
     * 根据id查找对应的对象
     *
     * @param id
     * @return
     */
    public TCaRuleTreeVO getById(Long id);


    /**
     * 插入决策树
     * @param tCaRuleTreeVO
     */
    public void insert(TCaRuleTreeVO tCaRuleTreeVO);


    /**
     * 按照主键更新决策树
     * @param tCaRuleTreeVO
     */
    public void updateByPrimaryKey(TCaRuleTreeVO tCaRuleTreeVO);


    /**
     * 根据ID删除对应的决策集
     * @param id
     */
    public void delete(Long id);


    /**
     * 通过ID计算对应的决策树的结果
     * @param id
     * @return
     */
    public JSONObject calculateRuleTreeById(Long id,JSONObject jsonObject);

    /**
     * 批量插入，并返回插入结果信息
     * @param tCaRuleTreeList
     * @param info
     */
    public void batchInsertNotExist(List<TCaRuleTree> tCaRuleTreeList , StringBuffer info);
}
