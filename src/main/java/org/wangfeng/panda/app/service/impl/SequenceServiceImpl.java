package org.wangfeng.panda.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wangfeng.panda.app.common.Constants;
import org.wangfeng.panda.app.common.base.AppBaseService;
import org.wangfeng.panda.app.dao.domain.TCaSequence;
import org.wangfeng.panda.app.dao.mapper.TCaSequenceMapper;
import org.wangfeng.panda.app.service.SequenceService;

import java.util.List;


@Service
public class SequenceServiceImpl  extends AppBaseService implements SequenceService {

    @Autowired
    private TCaSequenceMapper tCaSequenceMapper;

    /**
     * 根据code和type获取对应的sequence
     * @param code
     * @param type
     * @return
     */
    @Override
    public Integer queryLatestNum(String code, String type) {
        Integer num = tCaSequenceMapper.queryLatestNum(code,type);
        Integer finalNum = 0;
        if(num!=null) {
            finalNum = num;
        }
        return finalNum;

    }


    /**
     * 如果存在则加一；否则，新增一条记录
     * @param code
     * @param type
     */
    @Override
    public void incr(String code, String type) {
        TCaSequence tCaSequence = new TCaSequence();
        tCaSequence.setCode(code);
        tCaSequence.setType(type);

        List<TCaSequence> list = tCaSequenceMapper.select(tCaSequence);

        if(list==null||list.size()==0){
            tCaSequence.setSeq(Constants.INTEGER_ONE);
            initSaveWord(tCaSequence);
            tCaSequenceMapper.insert(tCaSequence);
        }else{
            tCaSequenceMapper.incr(code,type);
        }
    }


}
