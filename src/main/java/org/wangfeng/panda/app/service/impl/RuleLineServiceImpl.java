package org.wangfeng.panda.app.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wangfeng.panda.app.cache.CacheClient;
import org.wangfeng.panda.app.common.Constants;
import org.wangfeng.panda.app.common.base.AppBaseService;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.dao.domain.TCaCellVariable;
import org.wangfeng.panda.app.dao.domain.TCaRuleLine;
import org.wangfeng.panda.app.dao.mapper.TCaRuleLineMapper;
import org.wangfeng.panda.app.model.vo.TCaRuleLineVO;
import org.wangfeng.panda.app.model.vo.TCaSingleRuleVO;
import org.wangfeng.panda.app.service.CellVariableService;
import org.wangfeng.panda.app.service.RuleLineService;

import java.util.List;

@Service
@Slf4j
public class RuleLineServiceImpl extends AppBaseService implements RuleLineService {


    @Autowired
    private CacheClient cacheClient;

    @Autowired
    private CellVariableService cellVariableService;

    @Autowired
    private TCaRuleLineMapper tCaRuleLineMapper;

    /**
     * 通过规则CODE查询当前规则对应的所有行
     * @param ruleCode
     * @return
     */
    @Override
    public List<TCaRuleLineVO> queryLineListByRuleCode(String ruleCode) {
        log.info("通过规则CODE查询当前规则对应的所有行，开始：{}", ruleCode);
        //1、查询redis内是否有对应的结果
        List<TCaRuleLineVO> tCaRuleLineVOList = null;
//        (List<TCaRuleLineVO>)cacheClient.getObject("all_"+RedisInputEnum.RULE_LINE+"_by_rule_code_"+ruleCode);
        if(tCaRuleLineVOList!=null){
            return tCaRuleLineVOList;
        }

        //2、如果redis内没有，则查询数据库
        tCaRuleLineVOList = tCaRuleLineMapper.queryLineListByRuleCode(ruleCode);
        if(tCaRuleLineVOList == null){
            throw new RuleRuntimeException("该条规则没有对应的行！");
        }

        tCaRuleLineVOList.stream().forEach(line -> {
            //3、查出该行对应的变量格子
            List<TCaCellVariable> cellVariableVOList = cellVariableService.queryCellListByLineCode(line.getLineCode());
            line.setTCaCellVariableList(cellVariableVOList);
        });

        //4、存入redis
//        cacheClient.setObject("all_"+RedisInputEnum.RULE_LINE+"_by_rule_code_"+ruleCode,tCaRuleLineVOList);
//        cacheClient.expire("all_"+RedisInputEnum.RULE_LINE+"_by_rule_code_"+ruleCode, Constants.INTEGER_3600);
        log.info("通过规则CODE查询当前规则对应的所有行，返回：{}", ruleCode, JSON.toJSONString(tCaRuleLineVOList));

        return tCaRuleLineVOList;
    }


    /**
     * 插入单条行
     * @param tCaRuleLineVO
     * @param type
     * @param sort
     */
    @Override
    public void insertLine(TCaRuleLineVO tCaRuleLineVO, TCaSingleRuleVO tCaSingleRuleVO , String type, Integer sort) {

        //插入行
        if(tCaRuleLineVO.getSort()==null){
            tCaRuleLineVO.setSort(sort);
        }
        tCaRuleLineVO.setBusinessCode(tCaSingleRuleVO.getBusinessCode());
        tCaRuleLineVO.setLineCode("RULE_LINE_"+tCaSingleRuleVO.getRuleCode()+"_"+type+"_"+sort);
        tCaRuleLineVO.setRuleLineModule(type);
        tCaRuleLineVO.setStatus(Constants.SHORT_ONE);
        tCaRuleLineVO.setRuleCode(tCaSingleRuleVO.getRuleCode());
        tCaRuleLineVO.setId(null);
        initSaveWord(tCaRuleLineVO);
        tCaRuleLineMapper.insertSelective(tCaRuleLineVO);

        //循环插入格子数据
        List<TCaCellVariable> tCaCellVariableList = tCaRuleLineVO.getTCaCellVariableList();
        Integer cellSort = 0;
        for(TCaCellVariable cell : tCaCellVariableList){
            cellSort += 1;
            cellVariableService.insertCellVariable(cell,tCaRuleLineVO,cellSort);
        }


    }


    /**
     * 更新单条行
     * @param tCaRuleLineVO
     * @param tCaSingleRuleVO
     * @param type
     * @param sort
     */
    @Override
    public void updateLine(TCaRuleLineVO tCaRuleLineVO, TCaSingleRuleVO tCaSingleRuleVO, String type, Integer sort) {

//        cacheClient.del("all_"+RedisInputEnum.RULE_LINE+"_by_rule_code_"+tCaSingleRuleVO.getRuleCode());

        //1、先查询是否存在
        TCaRuleLine queryRuleLine = new TCaRuleLine();
        queryRuleLine.setLineCode("RULE_LINE_"+tCaSingleRuleVO.getRuleCode()+"_"+type+"_"+sort);
        TCaRuleLine tCaRuleLine = tCaRuleLineMapper.selectOne(queryRuleLine);

        //2、如果存在则更新，如果不存在则新增
        if(tCaRuleLine!=null){

            if(tCaRuleLineVO.getSort()==null){
                tCaRuleLineVO.setSort(sort);
            }

            tCaRuleLineVO.setSort(sort);
            tCaRuleLineVO.setBusinessCode(tCaSingleRuleVO.getBusinessCode());
            tCaRuleLineVO.setLineCode("RULE_LINE_"+tCaSingleRuleVO.getRuleCode()+"_"+type+"_"+sort);
            tCaRuleLineVO.setRuleLineModule(type);
            tCaRuleLineVO.setRuleCode(tCaSingleRuleVO.getRuleCode());
            tCaRuleLineVO.setStatus(Constants.SHORT_ONE);
            tCaRuleLineVO.setId(tCaRuleLine.getId());
            initSaveWord(tCaRuleLineVO);
            tCaRuleLineMapper.updateByPrimaryKey(tCaRuleLineVO);


            //把所有cell置为无效
            cellVariableService.unUseCell(tCaRuleLine.getLineCode());

            //循环更新格子数据
            List<TCaCellVariable> tCaCellVariableList = tCaRuleLineVO.getTCaCellVariableList();
            Integer cellSort = 0;
            for(TCaCellVariable cell : tCaCellVariableList){
                cellSort += 1;
                cellVariableService.updateCellVariable(cell,tCaRuleLineVO,cellSort);
            }


        }else{
            insertLine(tCaRuleLineVO, tCaSingleRuleVO, type, sort);
        }

    }



    /**
     * 删除行
     * @param ruleCode
     */
    @Override
    public void deleteLine(String ruleCode) {

//        cacheClient.del("all_"+RedisInputEnum.RULE_LINE+"_by_rule_code_"+ruleCode);

        List<TCaRuleLineVO> tCaRuleLineVOList = tCaRuleLineMapper.queryLineListByRuleCode(ruleCode);
        tCaRuleLineMapper.deleteByRuleCode(ruleCode);


        tCaRuleLineVOList.stream().forEach(line -> {
            cellVariableService.deleteCell(line.getLineCode());
        });
    }


    /**
     * 停用行
     * @param ruleCode
     */
    @Override
    public void unUseLine(String ruleCode) {
//        cacheClient.del("all_"+RedisInputEnum.RULE_LINE+"_by_rule_code_"+ruleCode);

        List<TCaRuleLineVO> tCaRuleLineVOList = tCaRuleLineMapper.queryLineListByRuleCode(ruleCode);
        tCaRuleLineMapper.unUseByRuleCode(ruleCode);


        tCaRuleLineVOList.stream().forEach(line -> {
            cellVariableService.unUseCell(line.getLineCode());
        });
    }


    /**
     * 批量插入，并返回插入结果信息
     * @param tCaRuleLineList
     * @param info
     */
    @Override
    public void batchInsertNotExist(List<TCaRuleLine> tCaRuleLineList, StringBuffer info) {
        //1、判空
        if(tCaRuleLineList==null){
            info.append("待插入的list为空，请查实！");
            return;
        }

        //2、逐条判断，并插入
        //如果存在就不插入，如果不存在则插入
        tCaRuleLineList.stream().forEach(rl -> {
            try {
                initSaveWord(rl);
                int count = tCaRuleLineMapper.insertNotExist(rl);
                if(count<1){
                    log.error("code为["+rl.getLineCode()+"]的行重复了，请确认！");
                    throw new RuleRuntimeException("已存在，插入失败");
                }
            }catch (Exception e){
                info.append(rl.getLineCode()).append(",");
            }
        });


    }


}
