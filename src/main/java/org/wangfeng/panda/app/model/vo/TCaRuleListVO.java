package org.wangfeng.panda.app.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.wangfeng.panda.app.dao.domain.TCaRuleList;

import java.util.List;

@Data
public class TCaRuleListVO extends TCaRuleList {

    /**
     * 业务线名称
     */
    @ApiModelProperty("业务线名称")
    private String BusinessName;

    /**
     * 状态位（1：启用，0：停用）
     */
    @ApiModelProperty("状态位（1：启用，0：停用）")
    private String StatusName;


    /**
     * 关键字
     */
    @ApiModelProperty("关键字")
    private String keyword;


    /**
     * 规则集下所有的规则VO的集合
     */
    @ApiModelProperty("规则集下所有的规则VO的集合")
    private List<TCaSingleRuleVO> tCaSingleRuleVOList;

    @Override
    public String toString() {
        return "TCaRuleListVO{" +
                "BusinessName='" + BusinessName + '\'' +
                ", StatusName='" + StatusName + '\'' +
                ", keyword='" + keyword + '\'' +
                ", tCaSingleRuleVOList=" + tCaSingleRuleVOList +
                "} " + super.toString();
    }
}
