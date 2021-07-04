package org.wangfeng.panda.app.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.wangfeng.panda.app.dao.MyMapper;
import org.wangfeng.panda.app.dao.domain.TCaSequence;

import java.util.List;


public interface TCaSequenceMapper extends MyMapper<TCaSequence> {



    @Select("SELECT seq " +
            "FROM t_ca_sequence " +
            "WHERE code = #{code} and type = #{type} ")
    public Integer queryLatestNum (@Param("code") String code, @Param("type") String type);



    @Update("UPDATE t_ca_sequence " +
            "SET seq = seq+1 " +
            "WHERE code = #{code} and type = #{type} ")
    public void incr(@Param("code") String code, @Param("type") String type);



    @Select("select * from t_ca_sequence where delete_flag = 0")
    public List<TCaSequence> queryAll(TCaSequence tCaSequence);

}