package org.wangfeng.panda.app.dao.domain;

import lombok.Data;
import org.wangfeng.panda.app.common.base.AppBaseModel;
import org.wangfeng.panda.app.service.excel.ExportColumn;

import javax.persistence.*;

@Table(name = "t_ca_rule_tree_node")
@Data
public class TCaRuleTreeNode extends AppBaseModel {
    /**
     * 自增主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    @ExportColumn("自增主键ID")
    private Long id;

    /**
     * 该节点的名字
     */
    @ExportColumn("该节点的名字")
    private String name;

    /**
     * 该节点的类型（规则，规则集，判断条件）
     */
    @ExportColumn("该节点的类型")
    private String type;

    /**
     * （已经废弃）决策树节点对应的规则或规则集的ID
     */
    @Column(name = "node_rule_id")
//    @ExportColumn("（已经废弃）决策树节点对应的规则或规则集的ID")
    private Long nodeRuleId;

    /**
     * 该节点的nodeID，前端页面自动生成的
     */
    @Column(name = "node_id")
    @ExportColumn("该节点的nodeID")
    private String nodeId;

    /**
     * 该节点的x坐标
     */
    @ExportColumn("该节点的x坐标")
    private Integer x;

    /**
     * 该节点的y坐标
     */
    @ExportColumn("该节点的y坐标")
    private Integer y;

    /**
     * 该节点的icon名称
     */
    @ExportColumn("该节点的icon名称")
    private String icon;

    /**
     * 该节点是否上方有连线（0：否，1：是）
     */
    @Column(name = "is_left_connect_show")
    @ExportColumn("该节点是否上方有连线（0：否，1：是）")
    private Boolean isLeftConnectShow;

    /**
     * 该节点是否下方有连线（0：否，1：是）
     */
    @Column(name = "is_right_connect_show")
    @ExportColumn("该节点是否下方有连线（0：否，1：是）")
    private Boolean isRightConnectShow;

    /**
     * 判断条件（同规则的表达式）
     */
    @Column(name = "judgement_condition")
    @ExportColumn("判断条件")
    private String judgementCondition;

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
     * 决策树节点对应的规则或规则集的ID（仅仅是node_type为规则和规则集的时候有值）
     */
    @Column(name = "node_rule_code")
    @ExportColumn("决策树节点对应的规则或规则集的ID")
    private String nodeRuleCode;

    /**
     * 规则集代码
     */
    @Column(name = "rule_tree_code")
    @ExportColumn("规则集代码")
    private String ruleTreeCode;
}