package org.wangfeng.panda.app.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.wangfeng.panda.app.dao.domain.TCaRuleTree;

import java.util.List;

@Data
public class TCaRuleTreeVO extends TCaRuleTree {

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
     * 该决策树的所有节点
     */
    @ApiModelProperty("该决策树的所有节点")
    private List<TCaRuleTreeNodeVO> nodes;

    /**
     * 该决策树的所有的连接元素
     */
    @ApiModelProperty("该决策树的所有的连接元素")
    private List<TCaRuleTreeMappingVO> connectors;


}
