package org.wangfeng.panda.app.service.impl;

import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wangfeng.panda.app.cache.CacheClient;
import org.wangfeng.panda.app.common.Constants;
import org.wangfeng.panda.app.common.Paginate;
import org.wangfeng.panda.app.common.base.AppBaseService;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.dao.domain.TCaDecisionVariable;
import org.wangfeng.panda.app.dao.mapper.TCaDecisionVariableMapper;
import org.wangfeng.panda.app.model.vo.TCaDecisionVariableVO;
import org.wangfeng.panda.app.service.DecisionVariableService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DecisionVariableServiceImpl extends AppBaseService implements DecisionVariableService {


    @Autowired
    private TCaDecisionVariableMapper tCaDecisionVariableMapper;

    @Autowired
    private CacheClient cacheClient;

    /**
     * 分页查询
     *
     * @param tCaDecisionVariableVO
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Paginate queryPagenate(TCaDecisionVariableVO tCaDecisionVariableVO, Integer pageNo, Integer pageSize) {
        //1、增加分页参数
        this.setPage(pageNo, pageSize);
        //2、去除%等特殊字符的影响
        distinguishString(tCaDecisionVariableVO);
        //3、查询出所有符合要求的数据
        List<TCaDecisionVariableVO> tCaDecisionVariableVOList = tCaDecisionVariableMapper.getList(tCaDecisionVariableVO);
        //4、拼装成需要的格式
        List<TCaDecisionVariableVO> finalTCaDecisionVariableVOList = new Page<>();
        if(tCaDecisionVariableVOList!=null) {
            BeanUtils.copyProperties(tCaDecisionVariableVOList, finalTCaDecisionVariableVOList);
            tCaDecisionVariableVOList.stream().forEach(variable ->{
                finalTCaDecisionVariableVOList.add(variable.invokeToVo());
            });
        }
        //5、组装成paginate对象
        Paginate paginate = createPaginate(pageNo, pageSize, finalTCaDecisionVariableVOList);
        return paginate;
    }

    /**
     * 根据id查找对应的对象
     *
     * @param id
     * @return
     */
    @Override
    public TCaDecisionVariableVO getById(Long id) {

        TCaDecisionVariableVO tCaDecisionVariableVO = null;
//                = (TCaDecisionVariableVO)cacheClient.getObject(RedisKey.DECISION_VARIABLE+id);
        if(tCaDecisionVariableVO!=null){
            return tCaDecisionVariableVO;
        }

        TCaDecisionVariable tCaDecisionVariable = tCaDecisionVariableMapper.getById(id);
        tCaDecisionVariableVO = tCaDecisionVariable.invokeToVo();
//        cacheClient.setObject(RedisKey.DECISION_VARIABLE+id,tCaDecisionVariableVO);

        return tCaDecisionVariableVO;
    }

    /**
     * 批量插入决策变量,其实质是批量更新
     *
     * @param tCaDecisionVariableList
     * @return
     */
    @Override
    public List<String> batchAddDecisionVariable(List<TCaDecisionVariable> tCaDecisionVariableList) {
        List<String> errorVariableCodeList = new ArrayList<>();

        tCaDecisionVariableList.stream().forEach(tCaDecisionVariable -> {
            try {
                //1、校验相同业务线下，相同数据来源的字段，决策变量名称是否有重复
                TCaDecisionVariableVO query = new TCaDecisionVariableVO();
                query.setBusinessId(tCaDecisionVariable.getBusinessId());
                query.setParamSource(tCaDecisionVariable.getParamSource());
                query.setDecisionVariableName(tCaDecisionVariable.getDecisionVariableName());
                List<TCaDecisionVariableVO> queryList = tCaDecisionVariableMapper.getList(query);
                if(queryList.size()>0){
                    throw new RuleRuntimeException("相同业务线下，相同数据来源的字段，决策变量名称有重复！");
                }
                //2、进行更新操作
                initUpdateWord(tCaDecisionVariable);
                tCaDecisionVariable.setDecisionVariableFlag((short) 1);
                tCaDecisionVariable.setStatus((short) 1);
                tCaDecisionVariableMapper.updateByPrimaryKey(tCaDecisionVariable);
            } catch (Exception e) {
                TCaDecisionVariable errorTCaDecisionVariable = tCaDecisionVariableMapper.getById(tCaDecisionVariable.getId());
                if (errorTCaDecisionVariable != null) {
                    errorVariableCodeList.add(errorTCaDecisionVariable.getVariableCode());
                }
            }
        });

        return errorVariableCodeList;
    }

    /**
     * 按照主键更新决策变量
     *
     * @param tCaDecisionVariable
     * @return
     */
    @Override
    public Integer updateByPrimaryKey(TCaDecisionVariable tCaDecisionVariable) {
        //0、删除对应的redis的值
//        cacheClient.del(RedisKey.DECISION_VARIABLE+tCaDecisionVariable.getId());

        //1、校验相同业务线下，相同数据来源的字段，决策变量名称是否有重复
        TCaDecisionVariableVO query = new TCaDecisionVariableVO();
        query.setBusinessId(tCaDecisionVariable.getBusinessId());
        query.setParamSource(tCaDecisionVariable.getParamSource());
        query.setDecisionVariableName(tCaDecisionVariable.getDecisionVariableName());
        List<TCaDecisionVariableVO> queryList = tCaDecisionVariableMapper.getList(query);
        if(queryList.size()>0){
            queryList.stream().forEach(q -> {
                if(!q.getId().equals(tCaDecisionVariable.getId())){
                    throw new RuleRuntimeException("相同业务线下，相同数据来源的字段，决策变量名称有重复！");
                }
            });
        }

        //2、更新决策变量
        initUpdateWord(tCaDecisionVariable);
        return tCaDecisionVariableMapper.updateByPrimaryKey(tCaDecisionVariable);
    }

    /**
     * 删除决策变量
     *
     * @param id
     * @return
     */
    @Override
    public Integer delete(Long id) {

        //0、删除对应的redis的值
//        cacheClient.del(RedisKey.DECISION_VARIABLE+id);

        TCaDecisionVariable tCaDecisionVariable = new TCaDecisionVariable();
        tCaDecisionVariable.setId(id);
        tCaDecisionVariable.setDecisionVariableName(Constants.EMPTY);
        tCaDecisionVariable.setDecisionVariableFlag(Constants.SHORT_ZERO);
        tCaDecisionVariable.setDecisionVariableRemark(Constants.EMPTY);
        initUpdateWord(tCaDecisionVariable);
        return tCaDecisionVariableMapper.updateByPrimaryKey(tCaDecisionVariable);
    }


    /**
     * 通过codeList获取所有的决策变量
     * @param codeList
     * @return
     */
    @Override
    public List<TCaDecisionVariableVO> queryListByCodeList(List<String> codeList) {
        if(codeList==null||codeList.size()==0){
            return null;
        }
        return tCaDecisionVariableMapper.queryListByCodeList(codeList);
    }


    /**
     * 批量插入，并返回插入结果信息
     * @param tCaDecisionVariableList
     * @param info
     */
    @Override
    public void batchInsertNotExist(List<TCaDecisionVariable> tCaDecisionVariableList, StringBuffer info) {
        //1、判空
        if(tCaDecisionVariableList==null){
            info.append("待插入的list为空，请查实！");
            return;
        }

        //2、逐条判断，并插入
        //如果存在就不插入，如果不存在则插入
        tCaDecisionVariableList.stream().forEach(dv -> {
            try {
                initSaveWord(dv);
                int count = tCaDecisionVariableMapper.insertNotExist(dv);
                if(count<1){
                    log.error("code为["+dv.getVariableCode()+"]的决策变量重复了，请确认！");
                    throw new RuleRuntimeException("已存在，插入失败");
                }
            }catch (Exception e){
                info.append(dv.getVariableCode()).append(",");
            }
        });



    }
}
