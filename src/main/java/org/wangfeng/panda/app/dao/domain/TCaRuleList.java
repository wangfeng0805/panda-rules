package org.wangfeng.panda.app.dao.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.wangfeng.panda.app.common.base.AppBaseModel;
import org.wangfeng.panda.app.model.enums.StatusEnum;
import org.wangfeng.panda.app.model.vo.TCaRuleListVO;
import org.wangfeng.panda.app.service.excel.ExportColumn;
import org.wangfeng.panda.app.validation.group.AddOperation;
import org.wangfeng.panda.app.validation.group.DeleteOperation;
import org.wangfeng.panda.app.validation.group.UpdateOperation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "t_ca_rule_list")
@Data
public class TCaRuleList extends AppBaseModel {
    /**
     * 自增主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    @ApiModelProperty("自增主键ID")
    @NotNull(message="自增主键不能为空" , groups = {DeleteOperation.class, UpdateOperation.class})
    @ExportColumn("自增主键ID")
    private Long id;

    /**
     * 规则集代码
     */
    @Column(name = "rule_list_code")
    @ApiModelProperty("规则集代码")
    @ExportColumn("规则集代码")
    private String ruleListCode;

    /**
     * 规则集名称
     */
    @Column(name = "rule_list_name")
    @ApiModelProperty("规则集名称")
    @NotNull(message="规则集名称不能为空" , groups = {AddOperation.class})
    @ExportColumn("规则集名称")
    private String ruleListName;

    /**
     * （已经废弃）业务线ID
     */
    @Column(name = "business_id")
    @ApiModelProperty("（已经废弃）业务线ID")
//    @NotNull(message="业务线ID不能为空" , groups = {AddOperation.class})
//    @ExportColumn("（已经废弃）业务线ID")
    private Long businessId;

    /**
     * 规则集备注
     */
    @Column(name = "rule_list_remark")
    @ApiModelProperty("规则集备注")
    @ExportColumn("规则集备注")
    private String ruleListRemark;

    /**
     * 状态位（1：启用，0：停用）
     */
    @ApiModelProperty("状态位（1：启用，0：停用）")
    @NotNull(message="状态位不能为空" , groups = {AddOperation.class})
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
    public TCaRuleListVO invokeToVo(){
        TCaRuleListVO vo = new TCaRuleListVO();
        BeanUtils.copyProperties(this,vo);

        //状态名称赋值
        StatusEnum statusEnum = StatusEnum.getByCode(vo.getStatus());
        if(statusEnum!=null){
            vo.setStatusName(statusEnum.getName());
        }

        return vo;
    }

    @Override
    public String toString() {
        return "TCaRuleList{" +
                "id=" + id +
                ", ruleListCode='" + ruleListCode + '\'' +
                ", ruleListName='" + ruleListName + '\'' +
                ", businessId=" + businessId +
                ", ruleListRemark='" + ruleListRemark + '\'' +
                ", status=" + status +
                "} " + super.toString();
    }
}