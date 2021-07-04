package org.wangfeng.panda.app.dao.domain;

import lombok.Data;
import org.wangfeng.panda.app.common.base.AppBaseModel;
import org.wangfeng.panda.app.service.excel.ExportColumn;

import javax.persistence.*;

@Table(name = "t_ca_cell_variable")
@Data
public class TCaCellVariable extends AppBaseModel {
    /**
     * 自增主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    @ExportColumn("自增主键ID")
    private Long id;

    /**
     * 格子对应的编码
     */
    @Column(name = "cell_variable_code")
    @ExportColumn("格子对应的编码")
    private String cellVariableCode;

    /**
     * 格子对应的key
     */
    @Column(name = "cell_variable_key")
    @ExportColumn("格子对应的key")
    private String cellVariableKey;

    /**
     * 格子对应的value
     */
    @Column(name = "cell_variable_value")
    @ExportColumn("格子对应的value")
    private String cellVariableValue;

    /**
     * 该格子对应的数据类型（1-字符串；2-整数；3-布尔；4-浮点数；5-日期；6-时间；7-数组；8-枚举值）
     */
    @Column(name = "cell_variable_type")
    @ExportColumn("该格子对应的数据类型（1-字符串；2-整数；3-布尔；4-浮点数；5-日期；6-时间；7-数组；8-枚举值）")
    private Short cellVariableType;

    /**
     * 该格子的数据来源（1、手动输入；2、决策变量；3、规则输出结果；4、本规则中的行数）
     */
    @Column(name = "cell_variable_source")
    @ExportColumn("该格子的数据来源（1、手动输入；2、决策变量；3、规则输出结果；4、本规则中的行数）")
    private Short cellVariableSource;

    /**
     * 行的编码
     */
    @Column(name = "rule_line_code")
    @ExportColumn("行的编码")
    private String ruleLineCode;

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
    @ExportColumn("态位（1：启用，0：停用）")
    private Short status;

}