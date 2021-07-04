package org.wangfeng.panda.app.dao.domain;

import lombok.Data;
import org.wangfeng.panda.app.common.base.AppBaseModel;
import org.wangfeng.panda.app.service.excel.ExportColumn;

import javax.persistence.*;

@Table(name = "t_ca_rule_list_mapping")
@Data
public class TCaRuleListMapping extends AppBaseModel {
    /**
     * 自增主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    @ExportColumn("自增主键ID")
    private Long id;

    /**
     * （已经废弃）规则集ID
     */
    @Column(name = "rule_list_id")
//    @ExportColumn("（已经废弃）规则集ID")
    private Long ruleListId;

    /**
     * （已经废弃）规则ID
     */
    @Column(name = "rule_id")
//    @ExportColumn("（已经废弃）规则ID")
    private Long ruleId;

    /**
     * 同一个规则集下的规则ID的排序
     */
    @ExportColumn("同一个规则集下的规则ID的排序")
    private Integer sort;

    /**
     * 状态位（1：启用，0：停用）
     */
    @ExportColumn("状态位（1：启用，0：停用）")
    private Short status;

    /**
     * 规则集代码
     */
    @Column(name = "rule_list_code")
    @ExportColumn("规则集代码")
    private String ruleListCode;

    /**
     * 规则代码
     */
    @Column(name = "rule_code")
    @ExportColumn("规则代码")
    private String ruleCode;

    @Override
    public String toString() {
        return "TCaRuleListMapping{" +
                "id=" + id +
                ", ruleListId=" + ruleListId +
                ", ruleId=" + ruleId +
                ", sort=" + sort +
                ", status=" + status +
                "} " + super.toString();
    }

}