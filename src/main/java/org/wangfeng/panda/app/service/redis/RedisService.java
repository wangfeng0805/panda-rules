package org.wangfeng.panda.app.service.redis;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wangfeng.panda.app.cache.CacheClient;
import org.wangfeng.panda.app.dao.domain.TCaDecisionVariable;
import org.wangfeng.panda.app.dao.domain.TCaRuleList;
import org.wangfeng.panda.app.dao.domain.TCaRuleTree;
import org.wangfeng.panda.app.dao.domain.TCaSingleRule;
import org.wangfeng.panda.app.dao.mapper.TCaDecisionVariableMapper;
import org.wangfeng.panda.app.dao.mapper.TCaRuleListMapper;
import org.wangfeng.panda.app.dao.mapper.TCaRuleTreeMapper;
import org.wangfeng.panda.app.dao.mapper.TCaSingleRuleMapper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 业务上需要的对redis进行的一些处理的代码
 */
@Service
public class RedisService {

    @Autowired
    private CacheClient cacheClient;
    @Autowired
    private TCaSingleRuleMapper tCaSingleRuleMapper;
    @Autowired
    private TCaRuleListMapper tCaRuleListMapper;
    @Autowired
    private TCaRuleTreeMapper tCaRuleTreeMapper;
    @Autowired
    private TCaDecisionVariableMapper tCaDecisionVariableMapper;

    /**
     * 删除所有的缓存
     */
    public void deleteAllCache(Integer type){

        switch (type){
            case 1:
                //1、清除所有的规则
                Example example1 = new Example(TCaSingleRule.class);
                example1.setOrderByClause("id desc");
                List<TCaSingleRule> tCaSingleRuleList = tCaSingleRuleMapper.selectByExampleAndRowBounds(example1,new RowBounds(0, 1));
                if(tCaSingleRuleList!=null){
                    Long singleRuleCount =  tCaSingleRuleList.get(0).getId();
//                    for(int i = 1 ;i<=singleRuleCount;i++){
//                        cacheClient.del(RedisInputEnum.SINGLE_RULE.getName()+i);
//                    }
//                    cacheClient.del("all_"+RedisInputEnum.SINGLE_RULE.getName());
                }
                break;
            case 2:
                //2、清除所有的规则集
                Example example2 = new Example(TCaSingleRule.class);
                example2.setOrderByClause("id desc");
                List<TCaRuleList> tCaRuleListList = tCaRuleListMapper.selectByExampleAndRowBounds(example2,new RowBounds(0, 1));
                if(tCaRuleListList!=null){
                    Long RuleListCount =  tCaRuleListList.get(0).getId();
//                    for(int i = 1 ;i<=RuleListCount;i++){
//                        cacheClient.del(RedisInputEnum.RULE_LIST.getName()+i);
//                    }
//                    cacheClient.del("all_"+RedisInputEnum.RULE_LIST.getName());
                }
                break;
            case 3:
                //3、清除所有的决策树
                Example example3 = new Example(TCaSingleRule.class);
                example3.setOrderByClause("id desc");
                List<TCaRuleTree> tcaRuleTreeList = tCaRuleTreeMapper.selectByExampleAndRowBounds(example3,new RowBounds(0, 1));
                if(tcaRuleTreeList!=null){
                    Long singleRuleCount =  tcaRuleTreeList.get(0).getId();
//                    for(int i = 1 ;i<=singleRuleCount;i++){
//                        cacheClient.del(RedisInputEnum.RULE_TREE.getName()+i);
//                    }
//                    cacheClient.del("all_"+RedisInputEnum.RULE_TREE.getName());
                }
                break;
            case 4:
                //4、清除所有的决策变量
                Example example4 = new Example(TCaSingleRule.class);
                example4.setOrderByClause("id desc");
                List<TCaDecisionVariable> tCaDecisionVariableList = tCaDecisionVariableMapper.selectByExampleAndRowBounds(example4,new RowBounds(0, 1));
                if(tCaDecisionVariableList!=null){
                    Long decisionVariableCount =  tCaDecisionVariableList.get(0).getId();
//                    for(int i = 1 ;i<=decisionVariableCount;i++){
//                        cacheClient.del(RedisInputEnum.DECISION_VARIABLE.getName()+i);
//                    }
//                    cacheClient.del("all_"+RedisInputEnum.DECISION_VARIABLE.getName());
                }
                break;
            default:
                break;
        }

    }


    /**
     * 把不同类型的全量数据，放入redis
     * @param type
     */
    public void putAllInRedis(Integer type){
        switch (type){
            case 1:
                Example singleRuleExample = new Example(TCaSingleRule.class);
                Example.Criteria singleRuleCriteria = singleRuleExample.createCriteria();
                singleRuleCriteria.andEqualTo("delete_flag",0);
                List<TCaSingleRule> tCaSingleRuleList = tCaSingleRuleMapper.selectByExample(singleRuleExample);
//                cacheClient.setObject("all_"+RedisInputEnum.SINGLE_RULE.getName(),tCaSingleRuleList);
                break;
            case 2:
                Example ruleListExample = new Example(TCaSingleRule.class);
                Example.Criteria ruleListCriteria = ruleListExample.createCriteria();
                ruleListCriteria.andEqualTo("delete_flag",0);
                List<TCaRuleList> tCaRuleListList = tCaRuleListMapper.selectByExample(ruleListCriteria);
//                cacheClient.setObject("all_"+RedisInputEnum.RULE_LIST.getName(),tCaRuleListList);
                break;
            case 3:
//                Example ruleListExample = new Example(TCaSingleRule.class);
//                Example.Criteria ruleListCriteria = ruleListExample.createCriteria();
//                ruleListCriteria.andEqualTo("delete_flag",0);
//                List<TCaRuleList> tCaRuleListList = tCaRuleListMapper.selectByExample(ruleListCriteria);
//                cacheClient.setObject("all_"+RedisInputEnum.RULE_LIST.getName(),tCaRuleListList);
                break;
            case 4:
                break;
            default:
                break;
        }







    }


}
