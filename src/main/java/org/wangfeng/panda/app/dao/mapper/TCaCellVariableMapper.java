package org.wangfeng.panda.app.dao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.wangfeng.panda.app.dao.MyMapper;
import org.wangfeng.panda.app.dao.domain.TCaCellVariable;

import java.util.List;


public interface TCaCellVariableMapper extends MyMapper<TCaCellVariable> {


    @Select("<script>" +
            " select cell.* " +
            " from t_ca_cell_variable cell " +
            " where cell.rule_line_code = #{lineCode} and cell.status = 1 and cell.delete_flag = 0 " +
            "</script>")
    public List<TCaCellVariable> queryCellListByLineCode(String lineCode);


    @Select("<script>" +
            " select cell.* " +
            " from t_ca_cell_variable cell " +
            " where cell.rule_line_code in " +
            " <foreach collection=\"list\" item=\"item\" index=\"index\" open=\"(\" close=\")\" separator=\",\"> "+
            "  #{item}" +
            " </foreach> "+
            " and cell.status = 1 and cell.delete_flag = 0 " +
            "</script>")
    public List<TCaCellVariable> queryCellListByLineCodeList(List<String> lineCodeList);




    @Update(" <script> " +
            " update t_ca_cell_variable cell " +
            " set  cell.delete_flag = 1 " +
            " where cell.rule_line_code = #{lineCode} and cell.delete_flag = 0 " +
            "</script>")
    public void deleteByLineCode(String lineCode);


    @Update(" <script> " +
            " update t_ca_cell_variable cell " +
            " set  cell.status = 0 " +
            " where cell.rule_line_code = #{lineCode}" +
            "</script>")
    public void unUseByLineCode(String lineCode);


    @Insert("<script>" +
            "insert into t_ca_cell_variable " +
            "(cell_variable_code,cell_variable_key,cell_variable_value,cell_variable_type,cell_variable_source,rule_line_code,sort,business_code,status,modified_time,modified_by,created_time,created_by,delete_flag) " +
            "select #{cellVariableCode},#{cellVariableKey},#{cellVariableValue},#{cellVariableType},#{cellVariableSource},#{ruleLineCode},#{sort},#{businessCode},#{status},#{modifiedTime},#{modifiedBy},#{createdTime},#{createdBy},#{deleteFlag} " +
            "from dual " +
            "where not exists ( " +
            "select * from t_ca_cell_variable where cell_variable_code = #{cellVariableCode} and delete_flag = 0) " +
            "</script>")
    public int insertNotExist(TCaCellVariable tCaCellVariable);

}