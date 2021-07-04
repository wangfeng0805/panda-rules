package org.wangfeng.panda.app.service;

import com.alibaba.fastjson.JSONObject;
import org.wangfeng.panda.app.common.Paginate;
import org.wangfeng.panda.app.dao.domain.TCaSingleRule;
import org.wangfeng.panda.app.model.vo.TCaSingleRuleVO;

import java.util.List;
import java.util.Map;

public interface SingleRuleService {

    /**
     * 查找列表页
     *
     * @param tCaSingleRuleVO
     * @return
     */
    public Paginate queryPagenate(TCaSingleRuleVO tCaSingleRuleVO, Integer pageNo, Integer pageSize);

    /**
     * 根据id查找对应的对象
     *
     * @param id
     * @return
     */
    public TCaSingleRuleVO getById(Long id);


    /**
     * 根据code查找对应的对象
     *
     * @param code
     * @return
     */
    public TCaSingleRuleVO getByCode(String code);

    /**
     * 批量插入决策变量,其实质是批量更新
     *
     * @param tCaSingleRuleVO
     * @return
     */
    public void insert (TCaSingleRuleVO tCaSingleRuleVO);


    /**
     * 更新
     *
     * @param tCaSingleRuleVO
     * @return
     */
    public void updateByPrimaryKey(TCaSingleRuleVO tCaSingleRuleVO);

    /**
     * 删除对应的
     *
     * @param id
     * @return
     */
    public void delete(Long id);

    /**
     * 测试对应的规则表达式是否正确
     * @param tCaSingleRule
     * @param params
     */
    public Object testRuleExpression(TCaSingleRule tCaSingleRule, Map<String,Object> params);


    /**
     * 计算对应的表达式
     * @param ruleId        规则ID
     * @param ruleCode      规则CODE
     * @param jsonObject    传入的参数
     * @return
     */
    public JSONObject calculateByIdORCode(Long ruleId, String ruleCode , JSONObject jsonObject);

    /**
     * 批量计算表达式
     * @param ruleIdList
     * @param jsonObject
     * @return
     */
    public JSONObject calculateByIdList(List<Long> ruleIdList, JSONObject jsonObject);


    /**
     * 批量插入，并返回插入结果信息
     * @param tCaSingleRuleList
     * @param info
     */
    public void batchInsertNotExist(List<TCaSingleRule> tCaSingleRuleList , StringBuffer info);

}
