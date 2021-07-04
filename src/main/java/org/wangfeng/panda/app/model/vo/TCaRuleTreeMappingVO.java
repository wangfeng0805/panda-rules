package org.wangfeng.panda.app.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.wangfeng.panda.app.dao.domain.TCaRuleTreeMapping;
import org.wangfeng.panda.app.dao.domain.TCaRuleTreeNode;

@Data
public class TCaRuleTreeMappingVO extends TCaRuleTreeMapping {

    /**
     * 目标节点（子级节点）
     */
    @ApiModelProperty("目标节点")
    private TCaRuleTreeNode targetNode;

    /**
     * 源节点（父级节点）
     */
    @ApiModelProperty("源节点")
    private TCaRuleTreeNode sourceNode;

}
