package org.wangfeng.panda.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wangfeng.panda.app.common.Constants;
import org.wangfeng.panda.app.common.base.AppBaseService;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.dao.domain.TCaRuleTreeNode;
import org.wangfeng.panda.app.dao.mapper.TCaRuleTreeNodeMapper;
import org.wangfeng.panda.app.model.vo.TCaRuleTreeNodeVO;
import org.wangfeng.panda.app.service.RuleTreeNodeService;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RuleTreeNodeServiceImpl extends AppBaseService implements RuleTreeNodeService {

    @Autowired
    private TCaRuleTreeNodeMapper tCaRuleTreeNodeMapper;


    /**
     * 通过treeId获取所有的相关的节点信息
     * @param ruleTreeCode
     * @return
     */
    @Override
    public List<TCaRuleTreeNodeVO> queryAllNodeByTreeCode(String ruleTreeCode) {

        List<TCaRuleTreeNodeVO> tCaRuleTreeNodeList = tCaRuleTreeNodeMapper.queryAllNodeByTreeCode(ruleTreeCode);

        return tCaRuleTreeNodeList;
    }

    /**
     * 通过TCaRuleTreeVO去插入对应的node数据
     * @param nodes
     * @param ruleTreeCode
     */
    @Override
    public void insertByTCaRuleTreeVO(List<TCaRuleTreeNodeVO> nodes, String ruleTreeCode) {

        List<TCaRuleTreeNode> finalNodes = new ArrayList<>();

        //为节点增加决策树ID的属性
        nodes.stream().forEach(tCaRuleTreeNode -> {
            tCaRuleTreeNode.setRuleTreeCode(ruleTreeCode);
            initSaveWord(tCaRuleTreeNode);

            TCaRuleTreeNode finalNode = new TCaRuleTreeNode();
            BeanUtils.copyProperties(tCaRuleTreeNode,finalNode);
            finalNodes.add(finalNode);
        });
        //批量插入
        if(finalNodes!=null&& finalNodes.size()>0){
            tCaRuleTreeNodeMapper.insertList(finalNodes);
        }
    }


    /**
     * 通过treeId删除所有对应的node
     * @param ruleTreeCode
     */
    @Override
    public void deleteByRuleTreeCode(String ruleTreeCode) {
        TCaRuleTreeNode tCaRuleTreeNode = new TCaRuleTreeNode();
        tCaRuleTreeNode.setRuleTreeCode(ruleTreeCode);
        tCaRuleTreeNode.setDeleteFlag(Constants.SHORT_ONE);
        initUpdateWord(tCaRuleTreeNode);
        tCaRuleTreeNodeMapper.deleteByRuleTreeId(tCaRuleTreeNode);
    }

    /**
     * 批量插入，并返回插入结果信息
     * @param tCaRuleTreeNodeList
     * @param info
     */
    @Override
    public void batchInsertNotExist(List<TCaRuleTreeNode> tCaRuleTreeNodeList, StringBuffer info) {
        //1、判空
        if(tCaRuleTreeNodeList==null){
            info.append("待插入的list为空，请查实！");
            return;
        }

        //2、逐条判断，并插入
        //如果存在就不插入，如果不存在则插入
        tCaRuleTreeNodeList.stream().forEach(tn -> {
            try {
                initSaveWord(tn);

                int count = tCaRuleTreeNodeMapper.insertNotExist(tn);

                if(count<1){
                    log.error("code为["+tn.getNodeId()+"]的决策树节点重复了，请确认！");
                    throw new RuleRuntimeException("已存在，插入失败");
                }
            }catch (Exception e){
                info.append(tn.getRuleTreeCode()).append(",");
            }
        });
    }


}
