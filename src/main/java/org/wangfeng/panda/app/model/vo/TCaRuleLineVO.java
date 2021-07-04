package org.wangfeng.panda.app.model.vo;

import lombok.Data;
import org.wangfeng.panda.app.dao.domain.TCaCellVariable;
import org.wangfeng.panda.app.dao.domain.TCaRuleLine;

import java.util.List;

@Data
public class TCaRuleLineVO extends TCaRuleLine {

    /**
     * 当前行所有的变量格子
     */
    private List<TCaCellVariable> tCaCellVariableList;


    /**
     * 当前行计算的结果
     */
    private Object lineResult;

}
