package org.wangfeng.panda.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wangfeng.panda.app.cache.CacheClient;
import org.wangfeng.panda.app.common.Constants;
import org.wangfeng.panda.app.common.base.AppBaseService;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.dao.domain.TCaCellVariable;
import org.wangfeng.panda.app.dao.mapper.TCaCellVariableMapper;
import org.wangfeng.panda.app.model.vo.TCaRuleLineVO;
import org.wangfeng.panda.app.service.CellVariableService;

import java.util.List;

@Service
@Slf4j
public class CellVariableServiceImpl extends AppBaseService implements CellVariableService {


    @Autowired
    private CacheClient cacheClient;

    @Autowired
    private TCaCellVariableMapper tCaCellVariableMapper;



    /**
     * 通过lineCode查询所有的变量格子
     * @param lineCode
     * @return
     */
    @Override
    public List<TCaCellVariable> queryCellListByLineCode(String lineCode) {
        //1、查询redis内是否有对应的结果
        List<TCaCellVariable> tCaCellVariableList = null;
//                (List<TCaCellVariable>)cacheClient.getObject("all_"+RedisInputEnum.CELL_VARIABLE+"_by_line_code_"+lineCode);
        if(tCaCellVariableList!=null){
            return tCaCellVariableList;
        }

        //2、如果没有则去数据库查询
        tCaCellVariableList = tCaCellVariableMapper.queryCellListByLineCode(lineCode);


        //3、插入redis
//        cacheClient.setObject("all_"+RedisInputEnum.CELL_VARIABLE+"_by_line_code_"+lineCode,tCaCellVariableList);
//        cacheClient.expire("all_"+RedisInputEnum.CELL_VARIABLE+"_by_line_code_"+lineCode, Constants.INTEGER_3600);

        return tCaCellVariableList;
    }

    /**
     * 插入格子
     * @param tCaCellVariable
     * @param tCaRuleLineVO
     * @param sort
     */
    @Override
    public Integer insertCellVariable(TCaCellVariable tCaCellVariable, TCaRuleLineVO tCaRuleLineVO, Integer sort) {

        //定义一些初步的数据
        if(tCaCellVariable.getSort()==null){
            tCaCellVariable.setSort(sort);
        }
        tCaCellVariable.setCellVariableValue(tCaCellVariable.getCellVariableSource()==1?tCaCellVariable.getCellVariableKey():"");
        tCaCellVariable.setBusinessCode(tCaRuleLineVO.getBusinessCode());
        tCaCellVariable.setRuleLineCode(tCaRuleLineVO.getLineCode());
        tCaCellVariable.setCellVariableCode("CELL_"+tCaRuleLineVO.getRuleCode()+"_"+tCaRuleLineVO.getSort()+"_"+sort+"_"+tCaRuleLineVO.getRuleLineModule()+"_"+tCaCellVariable.getCellVariableKey());
        tCaCellVariable.setStatus(Constants.SHORT_ONE);
        tCaCellVariable.setId(null);

        initSaveWord(tCaCellVariable);

        return tCaCellVariableMapper.insert(tCaCellVariable);

    }

    /**
     * 更新格子
     * @param tCaCellVariable
     * @param tCaRuleLineVO
     * @param sort
     * @return
     */
    @Override
    public Integer updateCellVariable(TCaCellVariable tCaCellVariable, TCaRuleLineVO tCaRuleLineVO, Integer sort) {
//        cacheClient.del("all_"+RedisInputEnum.CELL_VARIABLE+"_by_line_code_"+tCaRuleLineVO.getLineCode());

        //1、先查询是否存在
        TCaCellVariable queryCellVariable = new TCaCellVariable();
        queryCellVariable.setCellVariableCode("CELL_"+tCaRuleLineVO.getRuleCode()+"_"+tCaRuleLineVO.getSort()+"_"+sort+"_"+tCaRuleLineVO.getRuleLineModule()+"_"+tCaCellVariable.getCellVariableKey());
        TCaCellVariable queryTCaCellVariable = tCaCellVariableMapper.selectOne(queryCellVariable);

        //2、如果存在则更新，如果不存在则插入
        if(queryTCaCellVariable!=null){
            //定义一些初步的数据
            if(queryTCaCellVariable.getSort()==null){
                queryTCaCellVariable.setSort(sort);
            }
            queryTCaCellVariable.setSort(sort);
            queryTCaCellVariable.setCellVariableValue(tCaCellVariable.getCellVariableSource()==1?tCaCellVariable.getCellVariableKey():"");
            queryTCaCellVariable.setBusinessCode(tCaRuleLineVO.getBusinessCode());
            queryTCaCellVariable.setRuleLineCode(tCaRuleLineVO.getLineCode());
            queryTCaCellVariable.setStatus(Constants.SHORT_ONE);
            queryTCaCellVariable.setCellVariableKey(tCaCellVariable.getCellVariableKey());
            initUpdateWord(queryTCaCellVariable);
            return tCaCellVariableMapper.updateByPrimaryKey(queryTCaCellVariable);

        }else {
            //定义一些初步的数据
            return insertCellVariable(tCaCellVariable,tCaRuleLineVO,sort);
        }

    }

    /**
     * 根据lineCode删除
     * @param lineCode
     */
    @Override
    public void deleteCell(String lineCode) {
//        cacheClient.del("all_"+RedisInputEnum.CELL_VARIABLE+"_by_line_code_"+lineCode);
        tCaCellVariableMapper.deleteByLineCode(lineCode);
    }


    /**
     * 停用格子
     * @param lineCode
     */
    @Override
    public void unUseCell(String lineCode) {
//        cacheClient.del("all_"+RedisInputEnum.CELL_VARIABLE+"_by_line_code_"+lineCode);
        tCaCellVariableMapper.unUseByLineCode(lineCode);
    }

    @Override
    public void batchInsertNotExist(List<TCaCellVariable> tCaCellVariableList, StringBuffer info) {
        //1、判空
        if(tCaCellVariableList==null){
            info.append("待插入的list为空，请查实！");
            return;
        }

        //2、逐条判断，并插入
        //如果存在就不插入，如果不存在则插入
        tCaCellVariableList.stream().forEach(cv -> {
            try {

                //定义一些初步的数据
                initSaveWord(cv);
                int count = tCaCellVariableMapper.insertNotExist(cv);
                if(count<1){
                    log.error("code为["+cv.getCellVariableKey()+"]的规则重复了，请确认！");
                    throw new RuleRuntimeException("已存在，插入失败");
                }
            }catch (Exception e){
                info.append(cv.getCellVariableKey()).append(",");
            }
        });
    }
}
