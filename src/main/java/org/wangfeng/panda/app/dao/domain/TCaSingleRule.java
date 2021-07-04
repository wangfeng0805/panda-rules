package org.wangfeng.panda.app.dao.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.wangfeng.panda.app.common.base.AppBaseModel;
import org.wangfeng.panda.app.model.enums.OutPutTypeEnum;
import org.wangfeng.panda.app.model.enums.RuleCategoryEnum;
import org.wangfeng.panda.app.model.enums.StatusEnum;
import org.wangfeng.panda.app.model.vo.TCaSingleRuleVO;
import org.wangfeng.panda.app.service.excel.ExportColumn;
import org.wangfeng.panda.app.validation.group.AddOperation;
import org.wangfeng.panda.app.validation.group.UpdateOperation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "t_ca_single_rule")
@Data
public class TCaSingleRule extends AppBaseModel{

    /**
     * 自增主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    @ApiModelProperty("自增主键ID")
    @NotNull(message="自增主键ID不能为空" , groups = {UpdateOperation.class})
    private Long id;

    /**
     * （已经废弃）业务线ID
     */
    @Column(name = "business_id")
    @ApiModelProperty("（已经废弃）业务线ID")
    private Long businessId;

    /**
     * 规则代码
     */
    @Column(name = "rule_code")
    @ApiModelProperty("规则代码")
    @ExportColumn("规则代码")
    private String ruleCode;

    /**
     * 规则名称
     */
    @Column(name = "rule_name")
    @ApiModelProperty("规则名称")
    @NotNull(message="规则名称不能为空" , groups = {AddOperation.class})
    @ExportColumn("规则名称")
    private String ruleName;

    /**
     * 规则备注
     */
    @Column(name = "rule_remark")
    @ApiModelProperty("规则备注")
    @ExportColumn("规则备注")
    private String ruleRemark;

    /**
     * 规则类别(1-系统内置；2-自定义)
     */
    @Column(name = "rule_category")
    @ApiModelProperty("规则类别(1-系统内置；2-自定义)")
    @ExportColumn("规则类别(1-系统内置；2-自定义)")
    private Short ruleCategory;

    /**
     * 状态位（1：启用，0：停用）
     */
    @ApiModelProperty("状态位（1：启用，0：停用）")
    @NotNull(message="状态位不能为空" , groups = {AddOperation.class})
    @ExportColumn("状态位（1：启用，0：停用）")
    private Short status;

    /**
     * 输出项代码
     */
    @Column(name = "out_put_code")
    @ApiModelProperty("输出项代码")
    @ExportColumn("输出项代码")
    private String outPutCode;

    /**
     * 输出项名称
     */
    @Column(name = "out_put_name")
    @ApiModelProperty("输出项名称")
    @NotNull(message="输出项不能为空" , groups = {AddOperation.class})
    @ExportColumn("输出项名称")
    private String outPutName;

    /**
     * 输出类型（1-字符串；2-整数；3-布尔；4-浮点数；5-日期；6-时间；7-数组；8-枚举值）
     */
    @Column(name = "out_put_type")
    @ApiModelProperty("输出类型（1-字符串；2-整数；3-布尔；4-浮点数；5-日期；6-时间；7-数组；8-枚举值）")
    @NotNull(message="输出类型不能为空" , groups = {AddOperation.class})
    @ExportColumn("输出类型（1-字符串；2-整数；3-布尔；4-浮点数；5-日期；6-时间；7-数组；8-枚举值）")
    private Short outPutType;

    /**
     * 输出结果的格式（输出类型是4浮点数的时候，显示的是小数位数；输出类型是5日期或者6时间的时候，显示的是日期的format）
     */
    @Column(name = "out_put_format")
    @ApiModelProperty("输出结果的格式（输出类型是4浮点数的时候，显示的是小数位数；输出类型是5日期或者6时间的时候，显示的是日期的format）")
    @ExportColumn("输出结果的格式")
    private String outPutFormat;

    /**
     * 是否是条件表达式（1：是条件表达式，0：不是条件表达式）
     */
    @Column(name = "conditional_expression")
    @ApiModelProperty("是否是条件表达式（1：是条件表达式，0：不是条件表达式）")
    @ExportColumn("是否是条件表达式（1：是条件表达式，0：不是条件表达式）")
    private Short conditionalExpression;

    /**
     * （已经废弃）是否是单值操作（1：常量，0：复杂）
     */
    @Column(name = "single_value_operation")
    @ApiModelProperty("（已经废弃）是否是单值操作（1：常量，0：复杂）")
    private Short singleValueOperation;

    /**
     * （已经废弃）页面展示的规则表达式
     */
    @Column(name = "show_rule_expression")
    @ApiModelProperty("（已经废弃）页面展示的规则表达式")
//    @NotNull(message="规则表达式不能为空" , groups = {AddOperation.class})
    private String showRuleExpression;

    /**
     * （已经废弃）递归完成后参与运算的规则表达式
     */
    @Column(name = "real_rule_expression")
    @ApiModelProperty("（已经废弃）递归完成后参与运算的规则表达式")
    private String realRuleExpression;

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
    public TCaSingleRuleVO invokeToVo(){

        TCaSingleRuleVO vo = new TCaSingleRuleVO();
        BeanUtils.copyProperties(this,vo);

        //状态名称赋值
        StatusEnum statusEnum = StatusEnum.getByCode(vo.getStatus());
        if(statusEnum!=null){
            vo.setStatusName(statusEnum.getName());
        }


        //输出类型名称赋值
        OutPutTypeEnum outPutTypeEnum = OutPutTypeEnum.getByCode(vo.getOutPutType());
        if(outPutTypeEnum!=null){
            vo.setOutPutTypeName(outPutTypeEnum.getName());
        }


        //规则类别名称赋值
        RuleCategoryEnum ruleCategoryEnum = RuleCategoryEnum.getByCode(vo.getRuleCategory());
        if(ruleCategoryEnum!=null){
            vo.setRuleCategoryName(ruleCategoryEnum.getName());
        }

        return vo;
    }

    @Override
    public String toString() {
        return "TCaSingleRule{" +
                "id=" + id +
                ", businessCode=" + businessCode +
                ", ruleCode='" + ruleCode + '\'' +
                ", ruleName='" + ruleName + '\'' +
                ", ruleRemark='" + ruleRemark + '\'' +
                ", ruleCategory=" + ruleCategory +
                ", status=" + status +
                ", outPutCode='" + outPutCode + '\'' +
                ", outPutName='" + outPutName + '\'' +
                ", outPutType=" + outPutType +
                ", outPutFormat='" + outPutFormat + '\'' +
                ", conditionalExpression=" + conditionalExpression +
                ", singleValueOperation=" + singleValueOperation +
                ", ShowRuleExpression='" + showRuleExpression + '\'' +
                ", RealRuleExpression='" + realRuleExpression + '\'' +
                "} " + super.toString();
    }
}