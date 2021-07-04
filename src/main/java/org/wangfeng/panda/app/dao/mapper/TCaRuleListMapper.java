package org.wangfeng.panda.app.dao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.wangfeng.panda.app.dao.MyMapper;
import org.wangfeng.panda.app.dao.domain.TCaRuleList;
import org.wangfeng.panda.app.model.vo.TCaRuleListVO;

import java.util.List;

public interface TCaRuleListMapper extends MyMapper<TCaRuleList> {


    @Select("<script>"
            + "SELECT list.*,business.business_name as businessName "
            + "FROM t_ca_rule_list list "
            + "LEFT JOIN t_ca_business_line business ON list.business_code = business.business_code "
            + "<where>"
            + "<if test='id!=null' >"
            + "    and list.id = #{id} "
            + "</if>"
            + "<if test='ruleListCode!=null' >"
            + "    and list.rule_list_code = #{ruleListCode} "
            + "</if>"
            + "<if test='ruleListName!=null' >"
            + "    and list.rule_list_name = #{ruleListName} "
            + "</if>"
            + "<if test='ruleListRemark!=null' >"
            + "    and list.rule_list_remark = #{ruleListRemark} "
            + "</if>"
            + "<if test='modifiedTime!=null' >"
            + "    and list.modified_time = #{modifiedTime} "
            + "</if>"
            + "<if test='modifiedBy!=null' >"
            + "    and list.modified_by = #{modifiedBy} "
            + "</if>"
            + "<if test='createdTime!=null' >"
            + "    and list.created_time = #{createdTime} "
            + "</if>"
            + "<if test='createdBy!=null' >"
            + "    and list.created_by = #{createdBy} "
            + "</if>"
            + "<if test='status!=null' >"
            + "    and list.status = #{status} "
            + "</if>"
            + "<if test='keyword!=null' >"
            + "    and (list.rule_list_code like CONCAT('%', #{keyword}, '%') or list.rule_list_name like CONCAT('%', #{keyword}, '%')) "
            + "</if>"
            + "<if test='businessCode!=null' >"
            + "    and list.business_code = #{businessCode} "
            + "</if>"
            + "    and list.delete_flag = 0 "
            + "</where>"
            + "order by list.status desc , id desc "
            + "</script>")
    public List<TCaRuleListVO> getList(TCaRuleListVO tCaRuleListVO);


    /**
     * 保存一个实体，null的属性也会保存，不会使用数据库默认值
     *
     * @param tCaRuleList
     * @return
     */
    @Override
    @Insert("<script>"
            + "insert into t_ca_rule_list (rule_list_code,rule_list_name,business_code,rule_list_remark,status,modified_time,modified_by,created_time,created_by,delete_flag)"
            + " values "
            + "(#{ruleListCode},#{ruleListName},#{businessCode},#{ruleListRemark},#{status},#{modifiedTime},#{modifiedBy},#{createdTime},#{createdBy},#{deleteFlag})"
            + "</script>"
    )
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(TCaRuleList tCaRuleList);


    @Select("<script>"
            + "SELECT * "
            + "FROM  t_ca_rule_list "
            + "WHERE id = #{id} AND delete_flag = 0 "
            + "</script>")
    public TCaRuleListVO getById(Long id);



    @Select("<script>"
            + "SELECT * "
            + "FROM  t_ca_rule_list "
            + "WHERE rule_list_code = #{ruleListCode} AND delete_flag = 0 "
            + "</script>")
    public TCaRuleListVO getByCode(String ruleListCode);


    @Update("<script>"
            + "update t_ca_rule_list "
            + "<set>"
            + "<if test='ruleListCode!=null' >"
            + "    rule_list_code = #{ruleListCode}, "
            + "</if>"
            + "<if test='ruleListName!=null' >"
            + "    rule_list_name = #{ruleListName}, "
            + "</if>"
            + "<if test='ruleListRemark!=null' >"
            + "    rule_list_remark = #{ruleListRemark}, "
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
            + " modified_by = #{modifiedBy}"
            + "</set>"
            + "where id = #{id} "
            + "</script>")
    @Override
    public int updateByPrimaryKey(TCaRuleList tCaRuleList);


    @Select("select * from t_ca_rule_list where delete_flag = 0")
    public List<TCaRuleList> queryAll(TCaRuleList tCaRuleList);



    @Select(" <script> " +
            " select list.*" +
            " from t_ca_rule_list list " +
            " where list.rule_list_code in " +
            " <foreach collection=\"list\" item=\"item\" index=\"index\" open=\"(\" close=\")\" separator=\",\"> "+
            "  #{item}" +
            " </foreach> "+
            " and list.delete_flag = 0 " +
            "</script>")
    public List<TCaRuleList> queryListByCodeList(List<String> RuleListCodeList);



    @Insert("<script>" +
            "insert into t_ca_rule_list " +
            "(rule_list_code,rule_list_name,business_id,rule_list_remark,status,modified_time,modified_by,created_time,created_by,delete_flag,business_code) " +
            "select #{ruleListCode},#{ruleListName},#{businessId},#{ruleListRemark},#{status},#{modifiedTime},#{modifiedBy},#{createdTime},#{createdBy},#{deleteFlag},#{businessCode} " +
            "from dual " +
            "where not exists ( " +
            "select * from t_ca_rule_list where rule_list_code = #{ruleListCode} and delete_flag = 0) " +
            "</script>")
    public int insertNotExist(TCaRuleList tCaRuleList);
}