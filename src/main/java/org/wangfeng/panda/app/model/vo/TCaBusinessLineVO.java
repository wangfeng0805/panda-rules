package org.wangfeng.panda.app.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.wangfeng.panda.app.dao.domain.TCaBusinessLine;

@Data
public class TCaBusinessLineVO extends TCaBusinessLine {


    /**
     * 状态位（1：启用，0：停用）
     */
    @ApiModelProperty("状态位（1：启用，0：停用）")
    private String statusName;


    /**
     * 关键字
     */
    @ApiModelProperty("关键字")
    private String keyword;


    @Override
    public String toString() {
        return "TCaBusinessLineVO{" +
                "statusName='" + statusName + '\'' +
                ", keyword='" + keyword + '\'' +
                "} " + super.toString();
    }
}
