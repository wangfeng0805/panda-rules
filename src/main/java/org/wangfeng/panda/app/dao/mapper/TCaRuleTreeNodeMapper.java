package org.wangfeng.panda.app.dao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.wangfeng.panda.app.dao.MyMapper;
import org.wangfeng.panda.app.dao.domain.TCaRuleTreeNode;
import org.wangfeng.panda.app.model.vo.TCaRuleTreeNodeVO;

import java.util.List;

public interface TCaRuleTreeNodeMapper extends MyMapper<TCaRuleTreeNode> {

    @Select("select tree.* ,IFNULL(rule.rule_name,list.rule_list_name) as nodeRuleName " +
            "from t_ca_rule_tree_node tree " +
            "left join t_ca_single_rule rule on tree.node_rule_code = rule.rule_code AND tree.type='规则' " +
            "left join t_ca_rule_list list on tree.node_rule_code = list.rule_list_code AND tree.type='规则集' " +
            "where tree.rule_tree_code = #{ruleTreeCode} and tree.delete_flag = 0 and tree.status = 1")
    public List<TCaRuleTreeNodeVO> queryAllNodeByTreeCode(String ruleTreeCode);

    @Update("<script>"
            + "update t_ca_rule_tree_node "
            + "<set> "
            + " delete_flag = #{deleteFlag}, "
            + " modified_time = #{modifiedTime}, "
            + " modified_by = #{modifiedBy}"
            + "</set>"
            + "where tree_id = #{treeId} "
            + "</script>")
    public void deleteByRuleTreeId(TCaRuleTreeNode tCaRuleTreeNode);



    @Select("select * from t_ca_rule_tree_node where delete_flag = 0")
    public List<TCaRuleTreeNode> queryAll(TCaRuleTreeNode tCaRuleTreeNode);


    @Select(" <script> " +
            " select *" +
            " from t_ca_rule_tree_node  " +
            " where rule_tree_code in " +
            " <foreach collection=\"list\" item=\"item\" index=\"index\" open=\"(\" close=\")\" separator=\",\"> "+
            "  #{item}" +
            " </foreach> "+
            " and delete_flag = 0 " +
            "</script>")
    public List<TCaRuleTreeNode> queryListByTreeCodeList(List<String> RuleTreeCodeList);


    @Insert("<script>" +
            "insert into t_ca_rule_tree_node " +
            "(name,type,node_rule_id,node_id,x,y,icon,is_left_connect_show,is_right_connect_show,judgement_condition,tree_id,status,modified_time,modified_by,created_time,created_by,delete_flag,node_rule_code,rule_tree_code) " +
            "select #{name},#{type},#{nodeRuleId},#{nodeId},#{x},#{y},#{icon},#{isLeftConnectShow},#{isRightConnectShow},#{judgementCondition},#{treeId},#{status},#{modifiedTime},#{modifiedBy},#{createdTime},#{createdBy},#{deleteFlag},#{nodeRuleCode},#{ruleTreeCode} " +
            "from dual " +
            "where not exists ( " +
            "select * from t_ca_rule_tree_node where node_id = #{nodeId} and rule_tree_code = #{ruleTreeCode} and delete_flag = 0) " +
            "</script>")
    public int insertNotExist(TCaRuleTreeNode tCaRuleTreeNode);
}