package org.wangfeng.panda.app.dao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.wangfeng.panda.app.dao.MyMapper;
import org.wangfeng.panda.app.dao.domain.TCaDecisionVariable;
import org.wangfeng.panda.app.model.vo.TCaDecisionVariableVO;

import java.util.List;

public interface TCaDecisionVariableMapper extends MyMapper<TCaDecisionVariable> {


    @Select("<script>"
            + "SELECT variable.*,business.business_name as businessName "
            + "FROM t_ca_decision_variable variable "
            + "LEFT JOIN t_ca_business_line business ON variable.business_code = business.business_code "
            + "<where>"
            + "<if test='id!=null' >"
            + "    and variable.id = #{id} "
            + "</if>"
            + "<if test='variableCode!=null ' >"
            + "    and variable.variable_code like  CONCAT('%', #{variableCode}, '%') "
            + "</if>"
            + "<if test='decisionVariableName!=null' >"
            + "    and variable.decision_variable_name like  CONCAT('%', #{decisionVariableName}, '%') "
            + "</if>"
            + "<if test='variableType!=null' >"
            + "    and variable.variable_type = #{variableType} "
            + "</if>"
            + "<if test='enumDataRange!=null' >"
            + "    and variable.enum_data_range = #{enumDataRange} "
            + "</if>"
            + "<if test='variableLength!=null' >"
            + "    and variable.variable_length = #{variableLength} "
            + "</if>"
            + "<if test='variableCategory!=null' >"
            + "    and variable.variable_category = #{variableCategory} "
            + "</if>"
            + "<if test='decisionVariableDefaultValue!=null' >"
            + "    and variable.decision_variable_default_value = #{decisionVariableDefaultValue} "
            + "</if>"
            + "<if test='paramSource!=null' >"
            + "    and variable.param_source = #{paramSource} "
            + "</if>"
            + "<if test='decisionVariableRemark!=null' >"
            + "    and variable.decision_variable_remark = #{decisionVariableRemark} "
            + "</if>"
            + "<if test='businessCode!=null' >"
            + "    and variable.business_code = #{businessCode} "
            + "</if>"
            + "<if test='decisionVariableFlag!=null' >"
            + "    and variable.decision_variable_flag = #{decisionVariableFlag} "
            + "</if>"
            + "<if test='modifiedTime!=null' >"
            + "    and variable.modified_time = #{modifiedTime} "
            + "</if>"
            + "<if test='modifiedBy!=null' >"
            + "    and variable.modified_by = #{modifiedBy} "
            + "</if>"
            + "<if test='createdTime!=null' >"
            + "    and variable.created_time = #{createdTime} "
            + "</if>"
            + "<if test='createdBy!=null' >"
            + "    and variable.created_by = #{createdBy} "
            + "</if>"
            + "<if test='status!=null' >"
            + "    and variable.status = #{status} "
            + "</if>"
            + "<if test='keyword!=null' >"
            + "    and (variable.decision_variable_name like  CONCAT('%', #{keyword}, '%') OR variable.variable_code like  CONCAT('%', #{keyword}, '%') ) "
            + "</if>"
            + "    and variable.delete_flag = 0 "
            + "</where>"
            + "order by variable.status desc , variable.id desc "
            + "</script>")
    public List<TCaDecisionVariableVO> getList(TCaDecisionVariableVO tCaDecisionVariableVO);


    @Select("<script>"
            + "SELECT * "
            + "FROM t_ca_decision_variable "
            + "WHERE id = #{id} and delete_flag=0"
            + "</script>")
    public TCaDecisionVariable getById(Long id);


    @Update("<script>"
            + "update t_ca_decision_variable"
            + "<set>"
            + "<if test='variableCode!=null ' >"
            + "    variable_code = #{variableCode}, "
            + "</if>"
            + "<if test='decisionVariableName!=null' >"
            + "    decision_variable_name = #{decisionVariableName}, "
            + "</if>"
            + "<if test='variableType!=null' >"
            + "    variable_type = #{variableType}, "
            + "</if>"
            + "<if test='enumDataRange!=null' >"
            + "    enum_data_range = #{enumDataRange}, "
            + "</if>"
            + "<if test='variableLength!=null' >"
            + "    variable_length = #{variableLength}, "
            + "</if>"
            + "<if test='variableCategory!=null' >"
            + "    variable_category = #{variableCategory}, "
            + "</if>"
            + "<if test='decisionVariableDefaultValue!=null' >"
            + "    decision_variable_default_value = #{decisionVariableDefaultValue}, "
            + "</if>"
            + "<if test='paramSource!=null' >"
            + "    param_source = #{paramSource}, "
            + "</if>"
            + "<if test='decisionVariableRemark!=null' >"
            + "    decision_variable_remark = #{decisionVariableRemark}, "
            + "</if>"
            + "<if test='decisionVariableFlag!=null' >"
            + "    decision_variable_flag = #{decisionVariableFlag}, "
            + "</if>"
            + "<if test='createdTime!=null' >"
            + "    created_time = #{createdTime}, "
            + "</if>"
            + "<if test='createdBy!=null' >"
            + "    created_by = #{createdBy}, "
            + "</if>"
            + "<if test='status!=null' >"
            + "    status = #{status}, "
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
    public int updateByPrimaryKey(TCaDecisionVariable tCaDecisionVariable);




    @Select("<script>"
            + "SELECT * "
            + "FROM t_ca_decision_variable "
            + "where variable_code in "
            + "<foreach collection=\"list\" item=\"item\" index=\"index\" open=\"(\" close=\")\" separator=\",\"> "
            + " #{item}"
            + "</foreach> "
            + "</script>")
    public List<TCaDecisionVariableVO> queryListByCodeList(List<String> codeList);


    @Select("<script>" +
            "select * " +
            "from t_ca_decision_variable " +
            "where delete_flag = 0 " +
            "<if test='businessCode!=null' > " +
            "   and business_code = #{businessCode} " +
            "</if>" +
            "</script>")
    public List<TCaDecisionVariable> queryAll(TCaDecisionVariable tCaDecisionVariable);



    @Insert("<script>" +
            "insert into t_ca_decision_variable " +
            "(variable_code,decision_variable_name,variable_type,enum_data_range,variable_length,variable_category,decision_variable_default_value,param_source,decision_variable_remark,decision_variable_flag,status,modified_time,modified_by,created_time,created_by,delete_flag,business_code) " +
            "select #{variableCode},#{decisionVariableName},#{variableType},#{enumDataRange},#{variableLength},#{variableCategory},#{decisionVariableDefaultValue},#{paramSource},#{decisionVariableRemark},#{decisionVariableFlag},#{status},#{modifiedTime},#{modifiedBy},#{createdTime},#{createdBy},#{deleteFlag},#{businessCode} " +
            "from dual " +
            "where not exists ( " +
            "select * from t_ca_decision_variable where variable_code = #{variableCode} and business_code = #{businessCode} and delete_flag = 0) " +
            "</script>")
    public int insertNotExist(TCaDecisionVariable list);

}