package org.wangfeng.panda.app.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.wangfeng.panda.app.dao.domain.TCaSingleRule;

import java.util.List;
import java.util.Map;

@Data
public class TCaSingleRuleVO extends TCaSingleRule {


    /**
     * 业务线名称
     */
    @ApiModelProperty("业务线名称")
    private String businessName;

    /**
     * 规则类别名称(1-系统内置；2-自定义)
     */
    @ApiModelProperty("规则类别名称(1-系统内置；2-自定义)")
    private String ruleCategoryName;


    /**
     * 状态位名称（1：启用，0：停用）
     */
    @ApiModelProperty("状态位名称（1：启用，0：停用）")
    private String statusName;


    /**
     * 输出类型（1-字符串；2-整数；3-布尔；4-浮点数；5-日期；6-时间；7-数组；8-枚举值）
     */
    @ApiModelProperty("输出类型（1-字符串；2-整数；3-布尔；4-浮点数；5-日期；6-时间；7-数组；8-枚举值）")
    private String outPutTypeName;

    /**
     * 关键字
     */
    @ApiModelProperty("关键字")
    private String keyword;

    /**
     * 同一个规则集下的规则ID的排序
     */
    @ApiModelProperty("同一个规则集下的规则ID的排序")
    private Integer sort;

    /**
     * key未决策变量的code，value是决策变量
     */
    private Map<String, TCaDecisionVariableVO> decisionVariableVOMap;

    /**
     * if模块
     */
    private List<TCaRuleLineVO> ifModule;

    /**
     * then模块
     */
    private List<TCaRuleLineVO> thenModule;

    /**
     * else模块
     */
    private List<TCaRuleLineVO> elseModule;

    /**
     * single模块
     */
    private List<TCaRuleLineVO> singleModule;

}
