package org.wangfeng.panda.app.dao.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.wangfeng.panda.app.common.Constants;
import org.wangfeng.panda.app.common.base.AppBaseModel;
import org.wangfeng.panda.app.model.enums.DecisionVariableTypeEnum;
import org.wangfeng.panda.app.model.enums.StatusEnum;
import org.wangfeng.panda.app.model.enums.VariableParamSourceEnum;
import org.wangfeng.panda.app.model.vo.TCaDecisionVariableVO;
import org.wangfeng.panda.app.service.excel.ExportColumn;
import org.wangfeng.panda.app.validation.group.AddOperation;
import org.wangfeng.panda.app.validation.group.DeleteOperation;
import org.wangfeng.panda.app.validation.group.UpdateOperation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Table(name = "t_ca_decision_variable")
@Data
public class TCaDecisionVariable extends AppBaseModel {
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
     * 字段名称
     */
    @Column(name = "variable_code")
    @ApiModelProperty("字段名称")
    @Pattern(message = "仅允许输入字母、数字、下划线", regexp = "^[_a-zA-Z0-9]+$", groups = {AddOperation.class,UpdateOperation.class})
    @ExportColumn("字段名称")
    private String variableCode;

    /**
     * 决策变量名称
     */
    @Column(name = "decision_variable_name")
    @ApiModelProperty("决策变量名称")
    @Pattern(message = "仅允许输入汉字、字母、数字、下划线", regexp = "^[\\u4e00-\\u9fa5_a-zA-Z0-9]+$", groups = {AddOperation.class,UpdateOperation.class})
    @ExportColumn("决策变量名称")
    private String decisionVariableName;

    /**
     * 变量类型（1-字符串；2-整数；3-布尔；4-浮点数；5-日期；6-时间；7-数组；8-枚举值）
     */
    @Column(name = "variable_type")
    @ApiModelProperty("变量类型（1-字符串；2-整数；3-布尔；4-浮点数；5-日期；6-时间；7-数组；8-枚举值）")
    @ExportColumn("变量类型（1-字符串；2-整数；3-布尔；4-浮点数；5-日期；6-时间；7-数组；8-枚举值）")
    private Short variableType;

    /**
     * 枚举值的数据范围(如果数据类型是5，则数据范围以这个为准，逗号分隔)
     */
    @Column(name = "enum_data_range")
    @ApiModelProperty("枚举值的数据范围(如果数据类型是5，则数据范围以这个为准，逗号分隔)")
    @ExportColumn("枚举值的数据范围")
    private String enumDataRange;

    /**
     * 变量的长度（字符串长度，整数的长度，浮点数带小数点的长度）
     */
    @Column(name = "variable_length")
    @ApiModelProperty("变量的长度（字符串长度，整数的长度，浮点数带小数点的长度）")
    @ExportColumn("变量的长度")
    private Integer variableLength;

    /**
     * 变量类别(1-系统内置；2-自定义)
     */
    @Column(name = "variable_category")
    @ApiModelProperty("变量类别(1-系统内置；2-自定义)")
    @ExportColumn("变量类别(1-系统内置；2-自定义)")
    private Short variableCategory;

    /**
     * 决策变量默认值
     */
    @Column(name = "decision_variable_default_value")
    @ApiModelProperty("决策变量默认值")
    @ExportColumn("决策变量默认值")
    private String decisionVariableDefaultValue;

    /**
     * 数据来源（暂时未定，详见枚举）
     */
    @Column(name = "param_source")
    @ApiModelProperty("数据来源（暂时未定，详见枚举）")
    @ExportColumn("数据来源（暂时未定，详见枚举）")
    private Short paramSource;

    /**
     * 决策变量备注
     */
    @Column(name = "decision_variable_remark")
    @ApiModelProperty("决策变量备注")
    @ExportColumn("决策变量备注")
    private String decisionVariableRemark;

    /**
     * （已经废弃）业务线ID
     */
    @Column(name = "business_id")
    @ApiModelProperty("（已经废弃）业务线ID")
//    @ExportColumn("（已经废弃）业务线ID")
    private Long businessId;

    /**
     * 是否是决策变量（0-否；1-是）
     */
    @Column(name = "decision_variable_flag")
    @ApiModelProperty("是否是决策变量（0-否；1-是）")
    @ExportColumn("是否是决策变量（0-否；1-是）")
    private Short decisionVariableFlag;

    /**
     * 状态位（1：启用，0：停用）
     */
    @ApiModelProperty("状态位（1：启用，0：停用）")
    @ExportColumn("状态位（1：启用，0：停用）")
    private Short status;

    /**
     * 业务线的code
     */
    @Column(name = "business_code")
    @ApiModelProperty("业务线的code")
    @ExportColumn("业务线的code")
    private String businessCode;

    /**
     * 实体类向vo的转换方法
     * @return
     */
    public TCaDecisionVariableVO invokeToVo(){
        if(this == null){
            return null;
        }
        TCaDecisionVariableVO vo = new TCaDecisionVariableVO();
        BeanUtils.copyProperties(this,vo);

        vo.setDecisionVariableFlagName(vo.getDecisionVariableFlag().equals(Constants.SHORT_ONE)?Constants.SHI:Constants.FOU);

        //状态名称赋值
        StatusEnum statusEnum = StatusEnum.getByCode(vo.getStatus());
        if(statusEnum!=null){
            vo.setStatusName(statusEnum.getName());
        }

        //变量类型名称赋值
        DecisionVariableTypeEnum decisionVariableTypeEnum = DecisionVariableTypeEnum.getByCode(vo.getVariableType());
        if(decisionVariableTypeEnum!=null){
            vo.setVariableTypeName(decisionVariableTypeEnum.getName());
        }

        //数据来源名称赋值
        VariableParamSourceEnum variableParamSourceEnum = VariableParamSourceEnum.getByCode(vo.getParamSource());
        if(variableParamSourceEnum!=null){
            vo.setParamSourceName(variableParamSourceEnum.getName());
        }

        return vo;
    }

    @Override
    public String toString() {
        return "TCaDecisionVariable{" +
                "id=" + id +
                ", variableCode='" + variableCode + '\'' +
                ", decisionVariableName='" + decisionVariableName + '\'' +
                ", variableType=" + variableType +
                ", enumDataRange='" + enumDataRange + '\'' +
                ", variableLength=" + variableLength +
                ", variableCategory=" + variableCategory +
                ", decisionVariableDefaultValue='" + decisionVariableDefaultValue + '\'' +
                ", paramSource=" + paramSource +
                ", decisionVariableRemark='" + decisionVariableRemark + '\'' +
                ", businessId=" + businessId +
                ", decisionVariableFlag=" + decisionVariableFlag +
                ", status=" + status +
                "} " + super.toString();
    }
}