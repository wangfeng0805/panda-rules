package org.wangfeng.panda.app.dao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.wangfeng.panda.app.dao.MyMapper;
import org.wangfeng.panda.app.dao.domain.TCaSingleRule;
import org.wangfeng.panda.app.model.vo.TCaSingleRuleVO;

import java.util.List;


public interface TCaSingleRuleMapper extends MyMapper<TCaSingleRule> {


    @Select("<script>"
            + "SELECT rule.*,business.business_name as businessName "
            + "FROM t_ca_single_rule rule "
            + "LEFT JOIN t_ca_business_line business ON rule.business_code = business.business_code "
            + "<where>"
            + "<if test='id!=null' >"
            + "    and rule.id = #{id} "
            + "</if>"
            + "<if test='businessCode!=null' >"
            + "    and rule.business_code = #{businessCode} "
            + "</if>"
            + "<if test='ruleCode!=null' >"
            + "    and rule.rule_code  like CONCAT('%', #{ruleCode}, '%') "
            + "</if>"
            + "<if test='ruleName!=null' >"
            + "    and rule.rule_name  like CONCAT('%', #{ruleName}, '%') "
            + "</if>"
            + "<if test='ruleRemark!=null' >"
            + "    and rule.rule_remark = #{ruleRemark} "
            + "</if>"
            + "<if test='ruleCategory!=null' >"
            + "    and rule.rule_category = #{ruleCategory} "
            + "</if>"
            + "<if test='status!=null' >"
            + "    and rule.status = #{status} "
            + "</if>"
            + "<if test='outPutCode!=null' >"
            + "    and rule.out_put_code = #{outPutCode} "
            + "</if>"
            + "<if test='outPutName!=null' >"
            + "    and rule.out_put_name like CONCAT('%', #{outPutName}, '%') "
            + "</if>"
            + "<if test='outPutType!=null' >"
            + "    and rule.out_put_type = #{outPutType} "
            + "</if>"
            + "<if test='outPutFormat!=null' >"
            + "    and rule.out_put_format = #{outPutFormat} "
            + "</if>"
            + "<if test='conditionalExpression!=null' >"
            + "    and rule.conditional_expression = #{conditionalExpression} "
            + "</if>"
            + "<if test='singleValueOperation!=null' >"
            + "    and rule.single_value_operation = #{singleValueOperation} "
            + "</if>"
            + "<if test='showRuleExpression!=null' >"
            + "    and rule.show_rule_expression = #{showRuleExpression} "
            + "</if>"
            + "<if test='realRuleExpression!=null' >"
            + "    and rule.real_rule_expression = #{realRuleExpression} "
            + "</if>"
            + "<if test='modifiedTime!=null' >"
            + "    and rule.modified_time = #{modifiedTime} "
            + "</if>"
            + "<if test='modifiedBy!=null' >"
            + "    and rule.modified_by = #{modifiedBy} "
            + "</if>"
            + "<if test='createdTime!=null' >"
            + "    and rule.created_time = #{createdTime} "
            + "</if>"
            + "<if test='createdBy!=null' >"
            + "    and rule.created_by = #{createdBy} "
            + "</if>"
            + "<if test='keyword!=null' >"
            + "    and (rule.rule_code like CONCAT('%', #{keyword}, '%') or rule.rule_name like CONCAT('%', #{keyword}, '%')) "
            + "</if>"
            + "    and rule.delete_flag = 0 "
            + "</where>"
            + "order by rule.status desc , rule.id desc "
            + "</script>")
    public List<TCaSingleRuleVO> getList(TCaSingleRuleVO tCaSingleRuleVO);






    @Select("<script>"
            + "SELECT * "
            + "FROM t_ca_single_rule "
            + "WHERE id = #{id} and delete_flag=0 "
            + "</script>")
    public TCaSingleRule getById(Long id);



    @Select("<script>"
            + "SELECT * "
            + "FROM t_ca_single_rule "
            + "WHERE rule_code = #{ruleCode} and delete_flag=0 "
            + "</script>")
    public TCaSingleRule getByCode(String ruleCode);


    @Update("<script>"
            + "update t_ca_single_rule "
            + "<set>"
            + "<if test='ruleCode!=null' >"
            + "    rule_code = #{ruleCode} , "
            + "</if>"
            + "<if test='ruleName!=null' >"
            + "    rule_name = #{ruleName} , "
            + "</if>"
            + "<if test='ruleRemark!=null' >"
            + "    rule_remark = #{ruleRemark} , "
            + "</if>"
            + "<if test='ruleCategory!=null' >"
            + "    rule_category = #{ruleCategory} , "
            + "</if>"
            + "<if test='status!=null' >"
            + "    status = #{status} , "
            + "</if>"
            + "<if test='outPutCode!=null' >"
            + "    out_put_code = #{outPutCode} , "
            + "</if>"
            + "<if test='outPutName!=null' >"
            + "    out_put_name = #{outPutName} , "
            + "</if>"
            + "<if test='outPutType!=null' >"
            + "    out_put_type = #{outPutType} , "
            + "</if>"
            + "<if test='outPutFormat!=null' >"
            + "    out_put_format = #{outPutFormat} , "
            + "</if>"
            + "<if test='conditionalExpression!=null' >"
            + "    conditional_expression = #{conditionalExpression} , "
            + "</if>"
            + "<if test='singleValueOperation!=null' >"
            + "    single_value_operation = #{singleValueOperation} , "
            + "</if>"
            + "<if test='showRuleExpression!=null' >"
            + "    show_rule_expression = #{showRuleExpression} , "
            + "</if>"
            + "<if test='realRuleExpression!=null' >"
            + "    real_rule_expression = #{realRuleExpression} , "
            + "</if>"
            + "<if test='deleteFlag!=null' >"
            + "    delete_flag = #{deleteFlag}, "
            + "</if>"
            + " modified_time = #{modifiedTime}, "
            + " modified_by = #{modifiedBy} "
            + "</set>"
            + "where id = #{id} "
            + "</script>")
    @Override
    public int updateByPrimaryKey(TCaSingleRule tCaSingleRule);



    @Select("select * from t_ca_single_rule where delete_flag = 0")
    public List<TCaSingleRule> queryAll(TCaSingleRule tCaSingleRule);

    @Select("<script>" +
            "select * " +
            "from t_ca_single_rule " +
            "where delete_flag = 0 " +
            "and rule_code in " +
            "<foreach collection=\"list\" item=\"item\" index=\"index\" open=\"(\" close=\")\" separator=\",\"> "+
            " #{item}" +
            "</foreach> "+
            "</script>")
    public List<TCaSingleRule> queryByIdList(List<String> ids);



    @Insert("<script>" +
            "insert into t_ca_single_rule " +
            "(rule_code,rule_name,rule_remark,rule_category,status,out_put_code,out_put_name,out_put_type,out_put_format,conditional_expression,modified_time,modified_by,created_time,created_by,delete_flag,business_code) " +
            "select #{ruleCode},#{ruleName},#{ruleRemark},#{ruleCategory},#{status},#{outPutCode},#{outPutName},#{outPutType},#{outPutFormat},#{conditionalExpression},#{modifiedTime},#{modifiedBy},#{createdTime},#{createdBy},#{deleteFlag},#{businessCode} " +
            "from dual " +
            "where not exists ( " +
            "select * from t_ca_single_rule where rule_code = #{ruleCode} and delete_flag = 0) " +
            "</script>")
    public int insertNotExist(TCaSingleRule tCaSingleRule);





}