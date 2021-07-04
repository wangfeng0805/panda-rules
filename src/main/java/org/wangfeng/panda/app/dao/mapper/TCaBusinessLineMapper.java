package org.wangfeng.panda.app.dao.mapper;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.wangfeng.panda.app.dao.MyMapper;
import org.wangfeng.panda.app.dao.domain.TCaBusinessLine;
import org.wangfeng.panda.app.model.vo.TCaBusinessLineVO;

import java.util.List;

public interface TCaBusinessLineMapper extends MyMapper<TCaBusinessLine> {

    @Select("<script>"
            + "SELECT * "
            + "FROM t_ca_business_line "
            + "<where>"
            + "<if test='id!=null' >"
            + "    and id = #{id} "
            + "</if>"
            + "<if test='businessCode!=null' >"
            + "    and business_code  like CONCAT('%', #{businessCode}, '%') "
            + "</if>"
            + "<if test='businessName!=null' >"
            + "    and business_name  like CONCAT('%', #{businessName}, '%') "
            + "</if>"
            + "<if test='businessRemark!=null' >"
            + "    and business_remark = #{businessRemark} "
            + "</if>"
            + "<if test='modifiedTime!=null' >"
            + "    and modified_time = #{modifiedTime} "
            + "</if>"
            + "<if test='modifiedBy!=null' >"
            + "    and modified_by = #{modifiedBy} "
            + "</if>"
            + "<if test='createdTime!=null' >"
            + "    and created_time = #{createdTime} "
            + "</if>"
            + "<if test='createdBy!=null' >"
            + "    and created_by = #{createdBy} "
            + "</if>"
            + "<if test='status!=null' >"
            + "    and status = #{status} "
            + "</if>"
            + "<if test='keyword!=null' >"
            + "    and (business_code like CONCAT('%', #{keyword}, '%') or business_name like CONCAT('%', #{keyword}, '%')) "
            + "</if>"
            + "    and delete_flag = 0 "
            + "</where>"
            + "order by status desc , id desc "
            + "</script>")
    public List<TCaBusinessLine> getList(TCaBusinessLineVO tCaBusinessLineVO);






    @Select("<script>"
            + "SELECT * "
            + "FROM t_ca_business_line "
            + "WHERE id = #{id} and delete_flag=0 "
            + "</script>")
    public TCaBusinessLine getById(Long id);






    @Update("<script>"
            + "update t_ca_business_line "
            + "<set>"
            + "<if test='businessCode!=null ' >"
            + "    business_code = #{businessCode}, "
            + "</if>"
            + "<if test='businessName!=null' >"
            + "    business_name = #{businessName}, "
            + "</if>"
            + "<if test='businessRemark!=null' >"
            + "    business_remark = #{businessRemark}, "
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
    public int updateByPrimaryKey(TCaBusinessLine tCaBusinessLine);



    @Select("select * from t_ca_business_line where delete_flag = 0")
    public List<TCaBusinessLine> queryAll(TCaBusinessLine tCaBusinessLine);
}