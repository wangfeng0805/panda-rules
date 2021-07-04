package org.wangfeng.panda.app.service;

public interface SequenceService {

    /**
     * 根据code和type获取对应的sequence
     * @param code
     * @param type
     * @return
     */
    public Integer queryLatestNum(String code,String type);


    /**
     * 如果存在则加一；否则，新增一条记录
     * @param code
     * @param type
     */
    public void incr (String code,String type);

}
