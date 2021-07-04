package org.wangfeng.panda.app.dao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.wangfeng.panda.app.dao.MyMapper;
import org.wangfeng.panda.app.dao.domain.TCaRuleListMapping;
import org.wangfeng.panda.app.model.vo.TCaSingleRuleVO;

import java.util.List;

public interface TCaRuleListMappingMapper extends MyMapper<TCaRuleListMapping> {


    /**
     * 通过规则集的code去查询所有的规则列表
     * @param ruleListCode
     * @return
     */
    @Select("<script>"
            + "SELECT rule.*,mapping.sort as sort "
            + "FROM  t_ca_rule_list_mapping mapping "
            + "LEFT JOIN t_ca_single_rule rule ON mapping.rule_code = rule.rule_code "
            + "WHERE mapping.rule_list_code = #{ruleListCode} AND mapping.delete_flag = 0 AND rule.delete_flag = 0 "
            + "ORDER BY mapping.sort asc"
            + "</script>")
    public List<TCaSingleRuleVO> getByRuleListCode(String ruleListCode);



    @Update("<script>"
            + "update t_ca_rule_list_mapping "
            + "<set> "
            + " delete_flag = 1, "
            + " modified_time = #{modifiedTime}, "
            + " modified_by = #{modifiedBy}"
            + "</set>"
            + "where rule_list_id = #{ruleListId} "
            + "</script>")
    public int deleteByRuleListId(TCaRuleListMapping tCaRuleListMapping);


    @Update("<script>"
            + "update t_ca_rule_list_mapping "
            + "<set> "
            + " delete_flag = 1, "
            + " modified_time = #{modifiedTime}, "
            + " modified_by = #{modifiedBy}"
            + "</set>"
            + "where rule_list_code = #{ruleListCode} "
            + "</script>")
    public int deleteByRuleListCode(TCaRuleListMapping tCaRuleListMapping);


    @Select("select * from t_ca_rule_list_mapping where delete_flag = 0")
    public List<TCaRuleListMapping> queryAll(TCaRuleListMapping tCaRuleListMapping);

    @Select(" <script> " +
            " select *" +
            " from t_ca_rule_list_mapping  " +
            " where rule_list_code in " +
            " <foreach collection=\"list\" item=\"item\" index=\"index\" open=\"(\" close=\")\" separator=\",\"> "+
            "  #{item}" +
            " </foreach> "+
            " and delete_flag = 0 " +
            "</script>")
    public List<TCaRuleListMapping> queryListByCodeList(List<String> RuleListCodeList);

    @Insert("<script>" +
            "insert into t_ca_rule_list_mapping " +
            "(rule_list_id,rule_id,sort,status,modified_time,modified_by,created_time,created_by,delete_flag,rule_list_code,rule_code) " +
            "select #{ruleListId},#{ruleId},#{sort},#{status},#{modifiedTime},#{modifiedBy},#{createdTime},#{createdBy},#{deleteFlag},#{ruleListCode},#{ruleCode} " +
            "from dual " +
            "where not exists ( " +
            "select * from t_ca_rule_list_mapping where rule_list_code = #{ruleListCode} and rule_code = #{ruleCode} and delete_flag = 0) " +
            "</script>")
    public int insertNotExist(TCaRuleListMapping tCaRuleListMapping);
}