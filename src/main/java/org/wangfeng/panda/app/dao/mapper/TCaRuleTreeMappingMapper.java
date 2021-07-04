package org.wangfeng.panda.app.dao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.wangfeng.panda.app.dao.MyMapper;
import org.wangfeng.panda.app.dao.domain.TCaRuleTreeMapping;

import java.util.List;

public interface TCaRuleTreeMappingMapper extends MyMapper<TCaRuleTreeMapping> {


    @Select("select * from t_ca_rule_tree_mapping where rule_tree_code = #{ruleTreeCode} and delete_flag = 0 and status = 1")
    public List<TCaRuleTreeMapping> queryAllMappingByTreeCode(String treeCode);


    @Update("<script>"
            + "update t_ca_rule_tree_mapping "
            + "<set> "
            + " delete_flag = #{deleteFlag}, "
            + " modified_time = #{modifiedTime}, "
            + " modified_by = #{modifiedBy}"
            + "</set>"
            + "where tree_id = #{treeId} "
            + "</script>")
    public void deleteByRuleTreeId(TCaRuleTreeMapping tCaRuleTreeMapping);


    @Select("select * from t_ca_rule_tree_mapping where delete_flag = 0")
    public List<TCaRuleTreeMapping> queryAll(TCaRuleTreeMapping tCaRuleTreeMapping);


    @Select(" <script> " +
            " select *" +
            " from t_ca_rule_tree_mapping  " +
            " where rule_tree_code in " +
            " <foreach collection=\"list\" item=\"item\" index=\"index\" open=\"(\" close=\")\" separator=\",\"> "+
            "  #{item}" +
            " </foreach> "+
            "  and delete_flag = 0 " +
            "</script>")
    public List<TCaRuleTreeMapping> queryListByTreeCodeList(List<String> RuleTreeCodeList);



    @Insert("<script>" +
            "insert into t_ca_rule_tree_mapping " +
            "(source_node_id,target_node_id,tree_id,status,modified_time,modified_by,created_time,created_by,delete_flag,rule_tree_code) " +
            "select #{sourceNodeId},#{targetNodeId},#{treeId},#{status},#{modifiedTime},#{modifiedBy},#{createdTime},#{createdBy},#{deleteFlag},#{ruleTreeCode} " +
            "from dual " +
            "where not exists ( " +
            "select * from t_ca_rule_tree_mapping where source_node_id = #{sourceNodeId} and target_node_id = #{targetNodeId} and rule_tree_code = #{ruleTreeCode} and delete_flag = 0) " +
            "</script>")
    public int insertNotExist(TCaRuleTreeMapping tCaRuleTreeMapping);


}