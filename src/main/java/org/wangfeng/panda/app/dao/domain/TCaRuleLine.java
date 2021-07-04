package org.wangfeng.panda.app.dao.domain;

import lombok.Data;
import org.wangfeng.panda.app.common.base.AppBaseModel;
import org.wangfeng.panda.app.service.excel.ExportColumn;

import javax.persistence.*;

@Table(name = "t_ca_rule_line")
@Data
public class TCaRuleLine extends AppBaseModel {
    /**
     * 自增主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    @ExportColumn("自增主键ID")
    private Long id;

    /**
     * 行的编码，用于唯一识别（“rule_line” + business_code + 时间戳 + 序列号）
     */
    @Column(name = "line_code")
    @ExportColumn("行的编码，用于唯一识别")
    private String lineCode;

    /**
     * 是否有左括号
     */
    @Column(name = "line_left_bracket")
    @ExportColumn("是否有左括号")
    private Short lineLeftBracket;

    /**
     * 是否有右括号
     */
    @Column(name = "line_right_bracket")
    @ExportColumn("是否有右括号")
    private Short lineRightBracket;

    /**
     * 该行中的运算符，小于等于一个。
     */
    @Column(name = "line_function_code")
    @ExportColumn("该行中的运算符")
    private String lineFunctionCode;

    /**
     * 该行末尾是否有AND或者是OR，仅仅在if的行中会有
     */
    @Column(name = "line_connector")
    @ExportColumn("行末尾是否有AND或者是OR")
    private String lineConnector;

    /**
     * 判断该行是简单操作/复杂操作；1：是简单操作符 2：是复杂操作符 0 :不存在
     */
    @Column(name = "line_whether_simple")
    @ExportColumn("判断该行是简单操作/复杂操作；1：是简单操作符 2：是复杂操作符 0 :不存在")
    private Short lineWhetherSimple;

    /**
     * 行所属的规则的模块
     */
    @Column(name = "rule_line_module")
    @ExportColumn("行所属的规则的模块")
    private String ruleLineModule;


    /**
     * 规则代码
     */
    @Column(name = "rule_code")
    @ExportColumn("规则代码")
    private String ruleCode;


    /**
     * 同一个规则集下的规则ID的排序
     */
    @ExportColumn("同一个规则集下的规则ID的排序")
    private Integer sort;

    /**
     * 业务线的code
     */
    @Column(name = "business_code")
    @ExportColumn("业务线的code")
    private String businessCode;

    /**
     * 状态位（1：启用，0：停用）
     */
    @ExportColumn("状态位（1：启用，0：停用）")
    private Short status;

}