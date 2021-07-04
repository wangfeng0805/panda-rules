package org.wangfeng.panda.app.service.excel;


public enum ExportTypeEnum {



    BUSINESS_LINE_EXPORT(1,"TCaBusinessLine","业务线","TCaBusinessLineMapper","t_ca_business_line"),
    DECISION_VARIABLE_EXPORT(2,"TCaDecisionVariable","决策变量","TCaDecisionVariableMapper","t_ca_decision_variable"),
    SINGLE_RULE_EXPORT(3,"TCaSingleRule","规则","TCaSingleRuleMapper","t_ca_single_rule"),
    RULE_LIST_EXPORT(4,"TCaRuleList","规则集","TCaRuleListMapper","t_ca_rule_list"),
    RULE_LIST_MAPPING_EXPORT(5,"TCaRuleListMapping","规则集关系","TCaRuleListMappingMapper","t_ca_rule_list_mapping"),
    RULE_TREE_EXPORT(6,"TCaRuleTree","决策树","TCaRuleTreeMapper","t_ca_rule_tree"),
    RULE_TREE_NODE_EXPORT(7,"TCaRuleTreeNode","决策树节点","TCaRuleTreeNodeMapper","t_ca_rule_tree_node"),
    RULE_TREE_MAPPING_EXPORT(8,"TCaRuleTreeMapping","决策树关系","TCaRuleTreeMappingMapper","t_ca_rule_tree_mapping"),
    RULE_SEQUENCE_EXPORT(9,"TCaSequence","自增量记录表","TCaSequenceMapper","t_ca_sequence"),
    RULE_LINE_EXPORT(10,"TCaRuleLine","规则行","TCaRuleLineMapper","t_ca_rule_line"),
    CELL_VARIABLE_EXPORT(11,"TCaCellVariable","规则格子","TCaCellVariableMapper","t_ca_cell_variable");




    public static final String classPrePath = "org.wangfeng.panda.app.dao.domain.";
    public static final String mapperPrePath = "org.wangfeng.panda.app.dao.mapper.";
    public static final String queryAllMethod = "queryAll";


    private Integer id;

    private String className;

    private String CNName;

    private String mapperName;

    private String tableName;

    ExportTypeEnum(Integer id, String className, String CNName, String mapperName, String tableName) {
        this.id = id;
        this.className = className;
        this.CNName = CNName;
        this.mapperName = mapperName;
        this.tableName = tableName;
    }

    public Integer getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public String getCNName() {
        return CNName;
    }

    public String getMapperName() {
        return mapperName;
    }

    public String getTableName() {
        return tableName;
    }

    /**
     * 通过中文名字查询对应的导出的数据
     * @param CNName
     * @return
     */
    public static ExportTypeEnum getByCNName(String CNName){
        if(CNName == null){
            return null;
        }
        for(ExportTypeEnum i: ExportTypeEnum.values()){
            if(i.getCNName().equals(CNName)){
                return i;
            }
        }
        return null;
    }

}
