package org.wangfeng.panda.app.dao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.wangfeng.panda.app.dao.MyMapper;
import org.wangfeng.panda.app.dao.domain.TCaRuleLine;
import org.wangfeng.panda.app.model.vo.TCaRuleLineVO;

import java.util.List;

public interface TCaRuleLineMapper extends MyMapper<TCaRuleLine> {


    @Select(" <script> " +
            " select line.*" +
            " from t_ca_rule_line line " +
            " where line.rule_code = #{ruleCode} and line.status = 1 and line.delete_flag = 0 " +
            "</script>")
    public List<TCaRuleLineVO> queryLineListByRuleCode(String ruleCode);



    @Select(" <script> " +
            " select line.*" +
            " from t_ca_rule_line line " +
            " where line.rule_code in " +
            " <foreach collection=\"list\" item=\"item\" index=\"index\" open=\"(\" close=\")\" separator=\",\"> "+
            "  #{item}" +
            " </foreach> "+
            " and line.status = 1 and line.delete_flag = 0 " +
            "</script>")
    public List<TCaRuleLine> queryLineListByRuleCodeList(List<String> ruleCodeList);



    @Update(" <script> " +
            " update t_ca_rule_line line " +
            " set  line.delete_flag = 1 " +
            " where line.rule_code = #{ruleCode} and line.delete_flag = 0 " +
            "</script>")
    public void deleteByRuleCode(String ruleCode);


    @Update(" <script> " +
            " update t_ca_rule_line line " +
            " set  line.status = 0 " +
            " where line.rule_code = #{ruleCode} " +
            "</script>")
    public void unUseByRuleCode(String ruleCode);


    @Insert("<script>" +
            "insert into t_ca_rule_line " +
            "(line_code,line_left_bracket,line_right_bracket,line_function_code,line_connector,line_whether_simple,rule_line_module,rule_code,sort,business_code,status,modified_time,modified_by,created_time,created_by,delete_flag) " +
            "select #{lineCode},#{lineLeftBracket},#{lineRightBracket},#{lineFunctionCode},#{lineConnector},#{lineWhetherSimple},#{ruleLineModule},#{ruleCode},#{sort},#{businessCode},#{status},#{modifiedTime},#{modifiedBy},#{createdTime},#{createdBy},#{deleteFlag} " +
            "from dual " +
            "where not exists ( " +
            "select * from t_ca_rule_line where line_code = #{lineCode} and delete_flag = 0) " +
            "</script>")
    public int insertNotExist(TCaRuleLine tCaRuleLine);
}