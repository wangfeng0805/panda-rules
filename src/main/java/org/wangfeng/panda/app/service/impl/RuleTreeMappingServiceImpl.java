package org.wangfeng.panda.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wangfeng.panda.app.common.Constants;
import org.wangfeng.panda.app.common.base.AppBaseService;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.dao.domain.TCaRuleTreeMapping;
import org.wangfeng.panda.app.dao.mapper.TCaRuleTreeMappingMapper;
import org.wangfeng.panda.app.model.vo.TCaRuleTreeMappingVO;
import org.wangfeng.panda.app.service.RuleTreeMappingService;
import org.wangfeng.panda.app.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RuleTreeMappingServiceImpl extends AppBaseService implements RuleTreeMappingService {

    @Autowired
    private TCaRuleTreeMappingMapper tCaRuleTreeMappingMapper;


    /**
     * 通过treeId去查找所有的关联关系
     * @param treeCode
     * @return
     */
    @Override
    public List<TCaRuleTreeMapping> queryAllMappingByTreeCode(String treeCode) {

        List<TCaRuleTreeMapping> tCaRuleTreeMappingList = tCaRuleTreeMappingMapper.queryAllMappingByTreeCode(treeCode);

        return tCaRuleTreeMappingList;
    }


    /**
     * 通过TCaRuleTreeVO去插入对应的connectors数据
     * @param connectors
     * @param ruleTreeCode
     */
    @Override
    public void insertByTCaRuleTreeVO(List<TCaRuleTreeMappingVO> connectors, String ruleTreeCode) {

        List<TCaRuleTreeMapping> insertConnectors = new ArrayList<>();

        //循环增加对应的treeId
        connectors.stream().forEach(tCaRuleTreeMappingVO -> {
            tCaRuleTreeMappingVO.setRuleTreeCode(ruleTreeCode);
            if(StringUtils.isBlank(tCaRuleTreeMappingVO.getTargetNodeId())){
                tCaRuleTreeMappingVO.setTargetNodeId(tCaRuleTreeMappingVO.getTargetNode().getNodeId());
            }
            if(StringUtils.isBlank(tCaRuleTreeMappingVO.getSourceNodeId())){
                tCaRuleTreeMappingVO.setSourceNodeId(tCaRuleTreeMappingVO.getSourceNode().getNodeId());
            }

            initSaveWord(tCaRuleTreeMappingVO);
            TCaRuleTreeMapping mapping = new TCaRuleTreeMapping();
            BeanUtils.copyProperties(tCaRuleTreeMappingVO,mapping);
            insertConnectors.add(mapping);
        });

        //批量插入
        if(insertConnectors!=null&&insertConnectors.size()>0){
            tCaRuleTreeMappingMapper.insertList(insertConnectors);
        }

    }

    /**
     * 通过treeId删除所有对应的mapping关系
     * @param ruleTreeCode
     */
    @Override
    public void deleteByRuleTreeCode(String ruleTreeCode) {
        TCaRuleTreeMapping tCaRuleTreeMapping = new TCaRuleTreeMapping();
        tCaRuleTreeMapping.setRuleTreeCode(ruleTreeCode);
        tCaRuleTreeMapping.setDeleteFlag(Constants.SHORT_ONE);
        initUpdateWord(tCaRuleTreeMapping);
        tCaRuleTreeMappingMapper.deleteByRuleTreeId(tCaRuleTreeMapping);
    }

    /**
     * 批量插入，并返回插入结果信息
     * @param tCaRuleTreeMappingList
     * @param info
     */
    @Override
    public void batchInsertNotExist(List<TCaRuleTreeMapping> tCaRuleTreeMappingList, StringBuffer info) {
        //1、判空
        if(tCaRuleTreeMappingList==null){
            info.append("待插入的list为空，请查实！");
            return;
        }

        //2、逐条判断，并插入
        //如果存在就不插入，如果不存在则插入
        tCaRuleTreeMappingList.stream().forEach(tm -> {
            try {
                initSaveWord(tm);

                int count = tCaRuleTreeMappingMapper.insertNotExist(tm);

                if(count<1){
                    log.error("sourceNodeId为["+tm.getSourceNodeId()+"]，targetNodeId为["+tm.getTargetNodeId()+"]的决策树映射重复了，请确认！");
                    throw new RuleRuntimeException("已存在，插入失败");
                }
            }catch (Exception e){
                info.append(tm.getRuleTreeCode()).append(",");
            }
        });
    }


}
