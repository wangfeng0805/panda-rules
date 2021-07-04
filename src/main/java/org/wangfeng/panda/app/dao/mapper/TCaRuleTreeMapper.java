package org.wangfeng.panda.app.dao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.wangfeng.panda.app.dao.MyMapper;
import org.wangfeng.panda.app.dao.domain.TCaRuleTree;
import org.wangfeng.panda.app.model.vo.TCaRuleTreeVO;

import java.util.List;

public interface TCaRuleTreeMapper extends MyMapper<TCaRuleTree> {

    @Select("<script>"
            + "SELECT tree.*,business.business_name as businessName "
            + "FROM t_ca_rule_tree tree "
            + "LEFT JOIN t_ca_business_line business ON tree.business_code = business.business_code "
            + "<where>"
            + "<if test='id!=null' >"
            + "    and tree.id = #{id} "
            + "</if>"
            + "<if test='ruleTreeCode!=null' >"
            + "    and tree.rule_tree_code = #{ruleTreeCode} "
            + "</if>"
            + "<if test='ruleTreeName!=null' >"
            + "    and tree.rule_tree_name = #{ruleTreeName} "
            + "</if>"
            + "<if test='ruleTreeRemark!=null' >"
            + "    and tree.rule_tree_remark = #{ruleTreeRemark} "
            + "</if>"
            + "<if test='rootRuleNodeId!=null' >"
            + "    and tree.root_rule_node_id = #{rootRuleNodeId} "
            + "</if>"
            + "<if test='modifiedTime!=null' >"
            + "    and tree.modified_time = #{modifiedTime} "
            + "</if>"
            + "<if test='modifiedBy!=null' >"
            + "    and tree.modified_by = #{modifiedBy} "
            + "</if>"
            + "<if test='createdTime!=null' >"
            + "    and tree.created_time = #{createdTime} "
            + "</if>"
            + "<if test='createdBy!=null' >"
            + "    and tree.created_by = #{createdBy} "
            + "</if>"
            + "<if test='status!=null' >"
            + "    and tree.status = #{status} "
            + "</if>"
            + "<if test='keyword!=null' >"
            + "    and (tree.rule_tree_code like CONCAT('%', #{keyword}, '%') or tree.rule_tree_name like CONCAT('%', #{keyword}, '%')) "
            + "</if>"
            + "<if test='businessCode!=null' >"
            + "    and tree.business_code = #{businessCode} "
            + "</if>"
            + "    and tree.delete_flag = 0 "
            + "</where>"

            + "order by tree.status desc , tree.id desc "
            + "</script>")
    public List<TCaRuleTreeVO> getList(TCaRuleTreeVO tCaRuleTreeVO);





    @Select("<script>"
            + "SELECT * "
            + "FROM  t_ca_rule_tree "
            + "WHERE id = #{id} AND delete_flag = 0 "
            + "</script>")
    public TCaRuleTreeVO getById(Long id);




    /**
     * 保存一个实体，null的属性也会保存，不会使用数据库默认值
     *
     * @param tCaRuleTree
     * @return
     */
    @Override
    @Insert("<script>"
            + "insert into t_ca_rule_tree (rule_tree_code,rule_tree_name,business_code,rule_tree_remark,root_rule_node_id,status,modified_time,modified_by,created_time,created_by,delete_flag)"
            + " values "
            + "(#{ruleTreeCode},#{ruleTreeName},#{businessCode},#{ruleTreeRemark},#{rootRuleNodeId},#{status},#{modifiedTime},#{modifiedBy},#{createdTime},#{createdBy},#{deleteFlag})"
            + "</script>"
    )
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(TCaRuleTree tCaRuleTree);





    @Update("<script>"
            + "update t_ca_rule_tree "
            + "<set>"
            + "<if test='ruleTreeCode!=null' >"
            + "    rule_tree_code = #{ruleTreeCode}, "
            + "</if>"
            + "<if test='ruleTreeName!=null' >"
            + "    rule_tree_name = #{ruleTreeName}, "
            + "</if>"
            + "<if test='ruleTreeRemark!=null' >"
            + "    rule_tree_remark = #{ruleTreeRemark}, "
            + "</if>"
            + "<if test='rootRuleNodeId!=null' >"
            + "    root_rule_node_id = #{rootRuleNodeId}, "
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
    public int updateByPrimaryKey(TCaRuleTree tCaRuleTree);


    @Select("select * from t_ca_rule_tree where delete_flag = 0")
    public List<TCaRuleTree> queryAll(TCaRuleTree tCaRuleTree);


    @Select(" <script> " +
            " select *" +
            " from t_ca_rule_tree  " +
            " where rule_tree_code in " +
            " <foreach collection=\"list\" item=\"item\" index=\"index\" open=\"(\" close=\")\" separator=\",\"> "+
            "  #{item}" +
            " </foreach> "+
            " and delete_flag = 0 " +
            "</script>")
    public List<TCaRuleTree> queryListByTreeCodeList(List<String> RuleTreeCodeList);


    @Insert("<script>" +
            "insert into t_ca_rule_tree " +
            "(rule_tree_code,rule_tree_name,business_id,rule_tree_remark,root_rule_node_id,status,modified_time,modified_by,created_time,created_by,delete_flag,business_code) " +
            "select #{ruleTreeCode},#{ruleTreeName},#{businessId},#{ruleTreeRemark},#{rootRuleNodeId},#{status},#{modifiedTime},#{modifiedBy},#{createdTime},#{createdBy},#{deleteFlag},#{businessCode} " +
            "from dual " +
            "where not exists ( " +
            "select * from t_ca_rule_tree where rule_tree_code = #{ruleTreeCode} and delete_flag = 0) " +
            "</script>")
    public int insertNotExist(TCaRuleTree tCaRuleTree);
}