package org.wangfeng.panda.support;

import org.wangfeng.panda.app.dao.mapper.TCaDecisionVariableMapper;
import org.wangfeng.panda.app.service.DecisionVariableService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 决策变量基础测试类
 */
public class BaseDecisionVariableUT extends BaseUT {

    @Autowired
    protected DecisionVariableService decisionVariableService;

    @Autowired
    protected TCaDecisionVariableMapper tCaDecisionVariableMapper;


}
