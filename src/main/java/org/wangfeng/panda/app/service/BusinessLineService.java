package org.wangfeng.panda.app.service;

import org.wangfeng.panda.app.common.Paginate;
import org.wangfeng.panda.app.dao.domain.TCaBusinessLine;
import org.wangfeng.panda.app.model.vo.TCaBusinessLineVO;

public interface BusinessLineService {

    /**
     * 查找列表页
     * @param tCaBusinessLineVO
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Paginate queryPagenate(TCaBusinessLineVO tCaBusinessLineVO, Integer pageNo, Integer pageSize);

    /**
     * 根据id查找对应的对象
     *
     * @param id
     * @return
     */
    public TCaBusinessLine getById(Long id);

    /**
     * 插入业务线
     *
     * @param tCaBusinessLine
     * @return
     */
    public void insert(TCaBusinessLine tCaBusinessLine);


    /**
     * 更新
     *
     * @param tCaBusinessLine
     * @return
     */
    public void updateByPrimaryKey(TCaBusinessLine tCaBusinessLine);

    /**
     * 删除对应的
     *
     * @param id
     * @return
     */
    public void delete(Long id);

}
