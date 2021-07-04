package org.wangfeng.panda.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wangfeng.panda.app.common.Paginate;
import org.wangfeng.panda.app.common.base.AppBaseService;
import org.wangfeng.panda.app.dao.domain.TCaDecisionVariable;
import org.wangfeng.panda.app.dao.domain.TCaSingleRule;
import org.wangfeng.panda.app.dao.mapper.TCaDecisionVariableMapper;
import org.wangfeng.panda.app.dao.mapper.TCaSingleRuleMapper;
import org.wangfeng.panda.app.model.vo.TCaDecisionVariableVO;
import org.wangfeng.panda.app.model.vo.TCaSingleRuleVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InnerService extends AppBaseService {

    @Autowired
    private TCaSingleRuleMapper tCaSingleRuleMapper;
    @Autowired
    private TCaDecisionVariableMapper tCaDecisionVariableMapper;

    @Autowired
    private DealOldDataService dealOldDataService;


    /**
     * 处理老数据，一次性的接口
     */
//    @Async
    @Transactional(rollbackFor = Exception.class)
    public void dealOldData(){
        //1、查询出所有的表达式
        Map<String, TCaSingleRule> singleRuleMap = tCaSingleRuleMapper.selectAll().stream().filter(sr -> sr.getDeleteFlag()==0).collect(Collectors.toMap(TCaSingleRule::getOutPutName, sr -> sr,(oldValue , newValue) -> newValue));
        Map<String, TCaDecisionVariable> decisionVariableMap =tCaDecisionVariableMapper.selectAll().stream().filter(dv -> dv.getDeleteFlag()==0).collect(Collectors.toMap(TCaDecisionVariable::getVariableCode, dv -> dv,(oldValue , newValue) -> newValue));



        //2、遍历所有的规则
        List<TCaSingleRule> ruleList = tCaSingleRuleMapper.queryAll(new TCaSingleRule());

        for(TCaSingleRule rule : ruleList){
            try{
                //单个处理
                dealOldDataService.dealSingleRule(rule,singleRuleMap,decisionVariableMap);
            }catch (Exception e){
                log.error("出错的ID为："+rule.getId());
            }

        }
    }


    @Autowired
    private DecisionVariableService decisionVariableService;

    @Autowired
    private SingleRuleService singleRuleService;



    /**
     * 对于所有的变量的获取
     * 包括:
     * 1、决策变量（5条）
     * 2、输出结果（5条）
     * @param businessCode
     * @param keyword
     * @return
     */
    public Map<String,Object> queryAllVariable(String businessCode,String keyword){
        Map<String,Object> finalMap= new HashMap<>();

        //1、根据关键字查询对应的决策变量
        TCaDecisionVariableVO queryDecisionVariableVO = new TCaDecisionVariableVO();
        queryDecisionVariableVO.setBusinessCode(businessCode);
        queryDecisionVariableVO.setVariableCode(keyword);
        Paginate paginate1 = decisionVariableService.queryPagenate(queryDecisionVariableVO,1,5);
        finalMap.put("decisionVariable",paginate1);

        //2、根据关键字查询对应的输出结果
        TCaSingleRuleVO querySingleRuleVO = new TCaSingleRuleVO();
        querySingleRuleVO.setBusinessCode(businessCode);
        querySingleRuleVO.setOutPutName(keyword);
        Paginate paginate2 = singleRuleService.queryPagenate(querySingleRuleVO,1,5);
        finalMap.put("singleRule",paginate2);

        return finalMap;
    }
}
