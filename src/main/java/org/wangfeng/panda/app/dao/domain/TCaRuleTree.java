package org.wangfeng.panda.app.dao.domain;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.wangfeng.panda.app.common.base.AppBaseModel;
import org.wangfeng.panda.app.model.enums.StatusEnum;
import org.wangfeng.panda.app.model.vo.TCaRuleTreeVO;
import org.wangfeng.panda.app.service.excel.ExportColumn;
import org.wangfeng.panda.app.validation.group.AddOperation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "t_ca_rule_tree")
@Data
public class TCaRuleTree extends AppBaseModel {
    /**
     * 自增主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    @ExportColumn("自增主键ID")
    private Long id;

    /**
     * 决策树代码
     */
    @Column(name = "rule_tree_code")
    @ExportColumn("决策树代码")
    private String ruleTreeCode;

    /**
     * 规则集名称
     */
    @Column(name = "rule_tree_name")
    @NotNull(message="决策树名称不能为空" , groups = {AddOperation.class})
    @ExportColumn("决策树名称")
    private String ruleTreeName;

    /**
     * （已经废弃）业务线ID
     */
    @Column(name = "business_id")
//    @NotNull(message="业务线ID不能为空" , groups = {AddOperation.class})
//    @ExportColumn("（已经废弃）业务线ID")
    private Long businessId;

    /**
     * 决策树备注
     */
    @Column(name = "rule_tree_remark")
    @ExportColumn("决策树备注")
    private String ruleTreeRemark;

    /**
     * 根节点的节点ID
     */
    @Column(name = "root_rule_node_id")
    @NotNull(message="rootRuleNodeId不能为空" , groups = {AddOperation.class})
    @ExportColumn("根节点的节点ID")
    private String rootRuleNodeId;

    /**
     * 状态位（1：启用，0：停用）
     */
    @NotNull(message="状态不能为空" , groups = {AddOperation.class})
    @ExportColumn("状态位（1：启用，0：停用）")
    private Short status;

    /**
     * 业务线的code
     */
    @Column(name = "business_code")
    @NotNull(message="业务线的编号不能为空" , groups = {AddOperation.class})
    @ExportColumn("业务线的code")
    private String businessCode;

    /**
     * 实体类向vo的转换方法
     * @return
     */
    public TCaRuleTreeVO invokeToVo(){
        TCaRuleTreeVO vo = new TCaRuleTreeVO();
        BeanUtils.copyProperties(this,vo);

        //状态名称赋值
        StatusEnum statusEnum = StatusEnum.getByCode(vo.getStatus());
        if(statusEnum!=null){
            vo.setStatusName(statusEnum.getName());
        }

        return vo;
    }
}