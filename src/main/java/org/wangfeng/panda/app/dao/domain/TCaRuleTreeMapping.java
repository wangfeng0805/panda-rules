package org.wangfeng.panda.app.dao.domain;

import lombok.Data;
import org.wangfeng.panda.app.common.base.AppBaseModel;
import org.wangfeng.panda.app.service.excel.ExportColumn;

import javax.persistence.*;

@Table(name = "t_ca_rule_tree_mapping")
@Data
public class TCaRuleTreeMapping extends AppBaseModel {
    /**
     * 自增主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    @ExportColumn("自增主键ID")
    private Long id;

    /**
     * 源节点的nodeID，前端页面自动生成的
     */
    @Column(name = "source_node_id")
    @ExportColumn("源节点的nodeID")
    private String sourceNodeId;

    /**
     * 目标节点的nodeID，前端页面自动生成的
     */
    @Column(name = "target_node_id")
    @ExportColumn("目标节点的nodeID")
    private String targetNodeId;

    /**
     * (已经废弃）决策树ID
     */
    @Column(name = "tree_id")
//    @ExportColumn("(已经废弃）决策树ID")
    private Long treeId;

    /**
     * 状态位（1：启用，0：停用）
     */
    @ExportColumn("状态位（1：启用，0：停用）")
    private Short status;

    /**
     * 规则集代码
     */
    @Column(name = "rule_tree_code")
    @ExportColumn("规则集代码")
    private String ruleTreeCode;

}