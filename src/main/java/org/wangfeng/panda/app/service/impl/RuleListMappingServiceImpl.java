package org.wangfeng.panda.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wangfeng.panda.app.common.Constants;
import org.wangfeng.panda.app.common.base.AppBaseService;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.dao.domain.TCaRuleListMapping;
import org.wangfeng.panda.app.dao.mapper.TCaRuleListMappingMapper;
import org.wangfeng.panda.app.model.vo.TCaSingleRuleVO;
import org.wangfeng.panda.app.service.RuleListMappingService;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RuleListMappingServiceImpl extends AppBaseService implements RuleListMappingService {

    @Autowired
    private TCaRuleListMappingMapper tCaRuleListMappingMapper;

    /**
     * 根据决策集合ID批量删除对应的关联关系
     * @param ruleListId
     */
    @Override
    public void deleteByRuleListId(Long ruleListId) {
        TCaRuleListMapping tCaRuleListMapping = new TCaRuleListMapping();
        tCaRuleListMapping.setRuleListId(ruleListId);
        tCaRuleListMapping.setDeleteFlag(Constants.SHORT_ONE);
        initUpdateWord(tCaRuleListMapping);
        tCaRuleListMappingMapper.deleteByRuleListId(tCaRuleListMapping);
    }

    @Override
    public void deleteByRuleListCode(String ruleListCode) {
        TCaRuleListMapping tCaRuleListMapping = new TCaRuleListMapping();
        tCaRuleListMapping.setRuleListCode(ruleListCode);
        tCaRuleListMapping.setDeleteFlag(Constants.SHORT_ONE);
        initUpdateWord(tCaRuleListMapping);
        tCaRuleListMappingMapper.deleteByRuleListCode(tCaRuleListMapping);
    }


    /**
     * 从TCaSingleRuleVO中获取对应的mapping，并插入
     * @param tCaSingleRuleVOList
     * @param ruleListCode
     */
    @Override
    public void insertByTCaSingleRuleVOList(List<TCaSingleRuleVO> tCaSingleRuleVOList,String ruleListCode) {
        List<TCaRuleListMapping> ruleListMappingList = new ArrayList<>();
        if(tCaSingleRuleVOList!=null && tCaSingleRuleVOList.size()>0){
            tCaSingleRuleVOList.stream().forEach(t ->{
                TCaRuleListMapping tCaRuleListMapping = new TCaRuleListMapping();
                tCaRuleListMapping.setRuleCode(t.getRuleCode());
                tCaRuleListMapping.setRuleListCode(ruleListCode);
                tCaRuleListMapping.setSort(t.getSort());
                tCaRuleListMapping.setStatus(Constants.SHORT_ONE);
                initSaveWord(tCaRuleListMapping);
                ruleListMappingList.add(tCaRuleListMapping);
            });

            tCaRuleListMappingMapper.insertList(ruleListMappingList);
        }
    }

    /**
     * 批量插入，并返回插入结果信息
     * @param tCaRuleListMappingList
     * @param info
     */
    @Override
    public void batchInsertNotExist(List<TCaRuleListMapping> tCaRuleListMappingList, StringBuffer info) {

        //1、判空
        if(tCaRuleListMappingList==null){
            info.append("待插入的list为空，请查实！");
            return;
        }

        //2、逐条判断，并插入
        //如果存在就不插入，如果不存在则插入
        tCaRuleListMappingList.stream().forEach(rl -> {
            try {
                initSaveWord(rl);

                int count = tCaRuleListMappingMapper.insertNotExist(rl);

                if(count<1){
                    log.error("rule_code为["+rl.getRuleCode()+"]的规则集合mapping重复了，请确认！");
                    throw new RuleRuntimeException("已存在，插入失败");
                }
            }catch (Exception e){
                info.append(rl.getRuleListCode()).append(",");
            }
        });



    }
}
