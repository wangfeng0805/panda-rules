package org.wangfeng.panda.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wangfeng.panda.app.cache.CacheClient;
import org.wangfeng.panda.app.common.Constants;
import org.wangfeng.panda.app.common.Paginate;
import org.wangfeng.panda.app.common.base.AppBaseService;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.dao.domain.TCaRuleList;
import org.wangfeng.panda.app.dao.mapper.TCaRuleListMapper;
import org.wangfeng.panda.app.dao.mapper.TCaRuleListMappingMapper;
import org.wangfeng.panda.app.model.enums.SequenceTypeEnum;
import org.wangfeng.panda.app.model.vo.TCaRuleListVO;
import org.wangfeng.panda.app.model.vo.TCaSingleRuleVO;
import org.wangfeng.panda.app.service.RuleListMappingService;
import org.wangfeng.panda.app.service.RuleListService;
import org.wangfeng.panda.app.service.SequenceService;
import org.wangfeng.panda.app.service.SingleRuleService;
import org.wangfeng.panda.app.util.DateUtils;
import org.wangfeng.panda.app.util.StringUtils;

import java.util.*;

@Service
@Slf4j
public class RuleListServiceImpl extends AppBaseService implements RuleListService {



    @Autowired
    private TCaRuleListMapper tCaRuleListMapper;
    @Autowired
    private TCaRuleListMappingMapper tCaRuleListMappingMapper;

    @Autowired
    private RuleListMappingService ruleListMappingService;
    @Autowired
    private SingleRuleService singleRuleService;
    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private CacheClient cacheClient;


    /**
     * 获取列表页
     * @param tCaRuleListVO
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Paginate queryPagenate(TCaRuleListVO tCaRuleListVO, Integer pageNo, Integer pageSize) {
        //1、增加分页参数
        this.setPage(pageNo, pageSize);
        //2、去除%等特殊字符的影响
        distinguishString(tCaRuleListVO);
        //3、查询出所有符合要求的数据
        List<TCaRuleListVO> tCaRuleListVOList =  tCaRuleListMapper.getList(tCaRuleListVO);
        //4、拼装成需要的格式
        List<TCaRuleListVO> finalTCaRuleListVOList = new Page<>();
        if(tCaRuleListVOList!=null) {
            BeanUtils.copyProperties(tCaRuleListVOList, finalTCaRuleListVOList);
            tCaRuleListVOList.stream().forEach(l -> {
                finalTCaRuleListVOList.add(l.invokeToVo());
            });
        }
        //5、组装成paginate对象
        Paginate paginate = createPaginate(pageNo, pageSize, finalTCaRuleListVOList);
        return paginate;
    }


    /**
     * 通过ID得到对应ID的决策集
     * @param id
     * @return
     */
    @Override
    public TCaRuleListVO getById(Long id) {
        log.info("通过ID得到对应ID的决策集，返回：{}", id);

        //0、查询redis中的值
        TCaRuleListVO tCaRuleListVO = null;
//                (TCaRuleListVO)cacheClient.getObject(RedisKey.RULE_LIST+id);
        if(tCaRuleListVO!=null){
            return tCaRuleListVO;
        }

        //1、查询出对应的规则集对象
        tCaRuleListVO = tCaRuleListMapper.getById(id);
        if(tCaRuleListVO!=null){
            //2、再把规则集对应的所有的规则补齐
            List<TCaSingleRuleVO> tCaSingleRuleVOList = tCaRuleListMappingMapper.getByRuleListCode(tCaRuleListVO.getRuleListCode());
            List<TCaSingleRuleVO> finalTCaSingleRuleVOList = new ArrayList<>();
            if(tCaSingleRuleVOList!=null) {
                BeanUtils.copyProperties(tCaSingleRuleVOList, finalTCaSingleRuleVOList);
                tCaSingleRuleVOList.stream().forEach(rule -> {
                    finalTCaSingleRuleVOList.add(rule.invokeToVo());
                });
            }
            tCaRuleListVO.setTCaSingleRuleVOList(finalTCaSingleRuleVOList);

            //2、放入redis
//            cacheClient.setObject(RedisKey.RULE_LIST+id,tCaRuleListVO);

            return tCaRuleListVO;
        }
       return null;

    }




    /**
     * 通过ID得到对应ID的决策集
     * @param ruleListCode
     * @return
     */
    @Override
    public TCaRuleListVO getByCode(String ruleListCode) {

        //0、查询redis中的值
        TCaRuleListVO tCaRuleListVO = null;
//        (TCaRuleListVO)cacheClient.getObject(RedisKey.RULE_LIST+ruleListCode);
        if(tCaRuleListVO!=null){
            return tCaRuleListVO;
        }

        //1、查询出对应的规则集对象
        tCaRuleListVO = tCaRuleListMapper.getByCode(ruleListCode);

        if(tCaRuleListVO!=null){
            //2、再把规则集对应的所有的规则补齐
            List<TCaSingleRuleVO> tCaSingleRuleVOList = tCaRuleListMappingMapper.getByRuleListCode(tCaRuleListVO.getRuleListCode());
            List<TCaSingleRuleVO> finalTCaSingleRuleVOList = new ArrayList<>();
            if(tCaSingleRuleVOList!=null) {
                BeanUtils.copyProperties(tCaSingleRuleVOList, finalTCaSingleRuleVOList);
                tCaSingleRuleVOList.stream().forEach(rule -> {
                    finalTCaSingleRuleVOList.add(rule.invokeToVo());
                });
            }
            tCaRuleListVO.setTCaSingleRuleVOList(finalTCaSingleRuleVOList);

            //2、放入redis
//            cacheClient.setObject(RedisKey.RULE_LIST+ruleListCode,tCaRuleListVO);

            return tCaRuleListVO;
        }
        return null;

    }

    /**
     * 插入一条规则集数据
     * @param tCaRuleListVO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(TCaRuleListVO tCaRuleListVO) {
        //0、判空
        if(StringUtils.isBlank(tCaRuleListVO.getBusinessCode())){
            throw new RuntimeException("业务线编号不能为空！");
        }

        //1、填充预留字段
        complementProperty(tCaRuleListVO);
        initSaveWord(tCaRuleListVO);

        //2、插入对应的规则集
        tCaRuleListMapper.insert(tCaRuleListVO);

        //3、插入对应的映射关系
        ruleListMappingService.insertByTCaSingleRuleVOList(tCaRuleListVO.getTCaSingleRuleVOList(),tCaRuleListVO.getRuleListCode());
    }


    /**
     * 更新规则集的数据
     * @param tCaRuleListVO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByPrimaryKey(TCaRuleListVO tCaRuleListVO) {

        //删除redis中的值
//        cacheClient.del(RedisKey.RULE_LIST+tCaRuleListVO.getId());

        //1、填充预留字段
        initUpdateWord(tCaRuleListVO);

        //2、更新规则集表
        Integer count = tCaRuleListMapper.updateByPrimaryKey(tCaRuleListVO);
        if(count==0){
            throw new RuleRuntimeException("更新失败，没有对应的数据！");
        }

        //3、删除已有的所有的映射关系
        ruleListMappingService.deleteByRuleListCode(tCaRuleListVO.getRuleListCode());

        //4、增加新增的映射关系
        ruleListMappingService.insertByTCaSingleRuleVOList(tCaRuleListVO.getTCaSingleRuleVOList(),tCaRuleListVO.getRuleListCode());
    }


    /**
     * 删除规则集的数据
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {

        //0、查询出对应的对象
        TCaRuleListVO deleteRuleList = tCaRuleListMapper.getById(id);


        //删除redis中的值
//        cacheClient.del(RedisKey.RULE_LIST+id);

        //1、删除规则集表中的那条数据
        TCaRuleList tCaRuleList = new TCaRuleList();
        tCaRuleList.setId(id);
        tCaRuleList.setDeleteFlag(Constants.SHORT_ONE);
        initUpdateWord(tCaRuleList);
        tCaRuleListMapper.updateByPrimaryKey(tCaRuleList);

        //2、删除规则集映射表中关联的数据
        ruleListMappingService.deleteByRuleListCode(deleteRuleList.getRuleListCode());
    }



    /**
     * 填充计算表达式，规则代码，输出项代码
     *
     * @param tCaRuleList
     */
    public void complementProperty( TCaRuleList tCaRuleList){
        //生成规则集编号
        String businessCode = tCaRuleList.getBusinessCode();
        //获取日期戳
        String YYYYMMDD = DateUtils.getString(new Date(), DateUtils.YYYYMMDD);
        //自增并得到对应的结果
        sequenceService.incr(YYYYMMDD, SequenceTypeEnum.RULE_LIST_SEQ.getTypeName());
        Integer num = sequenceService.queryLatestNum(YYYYMMDD, SequenceTypeEnum.RULE_LIST_SEQ.getTypeName());
        //拼接出ruleListCode
        String ruleListCode =SequenceTypeEnum.RULE_LIST_SEQ+"_"+businessCode+"_"+YYYYMMDD+"_"+String.format("%03d", num);

        tCaRuleList.setRuleListCode(ruleListCode);
    }





    /* --------------------- 以上是前后端交互的业务代码 ------------------------------ */

    /* ----------------------以下是计算的逻辑代码-------------------------------------*/




    /**
     * 通过规则集ID去计算对应的结果
     *
     * @param id
     * @param ruleListCode
     * @param jsonObject
     */
    @Override
    public JSONObject calculateRuleListByIdORCode(Long id, String ruleListCode ,JSONObject jsonObject) {
        //1、拿到所有的规则的ID
        Map<Integer,List<Long>> groupSortMap = new HashMap<>();
        TCaRuleListVO tCaRuleListVO = null;
        if(id!=null){
            tCaRuleListVO = getById(id);
        }else{
            tCaRuleListVO = getByCode(ruleListCode);
        }

        if(tCaRuleListVO==null){
            return null;
        }
        List<TCaSingleRuleVO> tCaSingleRuleVOList =tCaRuleListVO.getTCaSingleRuleVOList();
        if(tCaSingleRuleVOList==null){
            return null;
        }

        tCaSingleRuleVOList.stream().forEach(t -> {
            List<Long> list = new ArrayList<>();
            if(groupSortMap.containsKey(t.getSort())){
                list = groupSortMap.get(t.getSort());
            }
            list.add(t.getId());
            groupSortMap.put(t.getSort(),list);
        });

        //2、遍历这个Map，执行对应的结果
        Set<Integer> keys = groupSortMap.keySet();
        List<Integer> keyList = new ArrayList<>(keys);
        Collections.sort(keyList);
        JSONObject finalResult = new JSONObject();
        for(Integer i : keyList){
            List<Long> ruleIds = groupSortMap.get(i);
            JSONObject result = singleRuleService.calculateByIdList(ruleIds,jsonObject);
            finalResult.putAll(result);
        }

        return finalResult;
    }


    /**
     * 批量插入，并返回插入结果信息
     * @param tCaRuleListList
     * @param info
     */
    @Override
    public void batchInsertNotExist(List<TCaRuleList> tCaRuleListList, StringBuffer info) {
        //1、判空
        if(tCaRuleListList==null){
            info.append("待插入的list为空，请查实！");
            return;
        }

        //2、逐条判断，并插入
        //如果存在就不插入，如果不存在则插入
        tCaRuleListList.stream().forEach(rl -> {
            try {
                initSaveWord(rl);

                int count = tCaRuleListMapper.insertNotExist(rl);

                if(count<1){
                    log.error("code为["+rl.getRuleListCode()+"]的规则集合重复了，请确认！");
                    throw new RuleRuntimeException("已存在，插入失败");
                }
            }catch (Exception e){
                info.append(rl.getRuleListCode()).append(",");
            }
        });
    }
}
