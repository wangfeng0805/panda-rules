package org.wangfeng.panda.app.dao;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * User: wangfeng
 * Date: 2020/8/16
 * 被继承的Mapper，一般业务Mapper继承它
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {

}