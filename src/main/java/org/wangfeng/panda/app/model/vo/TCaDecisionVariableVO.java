package org.wangfeng.panda.app.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.wangfeng.panda.app.dao.domain.TCaDecisionVariable;

@Data
public class TCaDecisionVariableVO extends TCaDecisionVariable {

    /**
     * 变量类型名称（1-字符串；2-整数；3-布尔；4-浮点数；5-日期；6-时间；7-数组；8-枚举值）
     */
    @ApiModelProperty("变量类型名称（1-字符串；2-整数；3-布尔；4-浮点数；5-日期；6-时间；7-数组；8-枚举值）")
    private String variableTypeName;


    /**
     * 业务线名称
     */
    @ApiModelProperty("业务线名称")
    private String businessName;

    /**
     * 是否是决策变量（0-否；1-是）
     */
    @ApiModelProperty("是否是决策变量（0-否；1-是）")
    private String decisionVariableFlagName;


    /**
     * 状态位（1：启用，0：停用）
     */
    @ApiModelProperty("状态位（1：启用，0：停用）")
    private String statusName;


    /**
     * 数据来源名称（暂时未定，详见枚举）
     */
    @ApiModelProperty("数据来源名称（暂时未定，详见枚举）")
    private String paramSourceName;

    /**
     * 关键字
     */
    @ApiModelProperty("关键字")
    private String keyword;

    @Override
    public String toString() {
        return "TCaDecisionVariableVO{" +
                "variableTypeName='" + variableTypeName + '\'' +
                ", businessName='" + businessName + '\'' +
                ", decisionVariableFlagName='" + decisionVariableFlagName + '\'' +
                ", statusName='" + statusName + '\'' +
                ", paramSourceName='" + paramSourceName + '\'' +
                "} " + super.toString();
    }
}
