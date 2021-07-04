package org.wangfeng.panda.app.model.vo;

import lombok.Data;
import org.wangfeng.panda.app.dao.domain.TCaRuleTreeNode;

@Data
public class TCaRuleTreeNodeVO extends TCaRuleTreeNode {



    /**
     * 决策树节点对应的规则或规则集的Name（仅仅是node_type为规则和规则集的时候有值）
     */
    private String nodeRuleName;

}
