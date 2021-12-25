package org.wangfeng.panda.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wangfeng.panda.app.cache.CacheClient;
import org.wangfeng.panda.app.calculation.JSEngineCalculation;
import org.wangfeng.panda.app.common.Constants;
import org.wangfeng.panda.app.common.Paginate;
import org.wangfeng.panda.app.common.base.AppBaseService;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.dao.domain.TCaSingleRule;
import org.wangfeng.panda.app.dao.mapper.TCaSingleRuleMapper;
import org.wangfeng.panda.app.model.enums.OutPutTypeEnum;
import org.wangfeng.panda.app.model.enums.SequenceTypeEnum;
import org.wangfeng.panda.app.model.vo.TCaRuleLineVO;
import org.wangfeng.panda.app.model.vo.TCaSingleRuleVO;
import org.wangfeng.panda.app.service.DecisionVariableService;
import org.wangfeng.panda.app.service.RuleLineService;
import org.wangfeng.panda.app.service.SequenceService;
import org.wangfeng.panda.app.service.SingleRuleService;
import org.wangfeng.panda.app.util.DateUtils;
import org.wangfeng.panda.app.util.concurrent.CallableTemplate;
import org.wangfeng.panda.app.util.concurrent.ICallableTaskFrameWork;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@SuppressWarnings("all")
public class SingleRuleServiceImpl extends AppBaseService implements SingleRuleService {


    @Autowired
    private TCaSingleRuleMapper tCaSingleRuleMapper;


    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private DecisionVariableService decisionVariableService;
    @Autowired
    private RuleLineService ruleLineService;

    @Autowired
    private ICallableTaskFrameWork callableTaskFrameWork;

    @Autowired
    private CacheClient cacheClient;


    /**
     * 查找规则列表页
     * @param tCaSingleRuleVO
     * @param pageNo
     * @param pageSize
     * @return
     *
     */
    @Override
    public Paginate queryPagenate(TCaSingleRuleVO tCaSingleRuleVO, Integer pageNo, Integer pageSize) {
        //1、增加分页参数
        this.setPage(pageNo, pageSize);
        //2、去除%等特殊字符的影响
        distinguishString(tCaSingleRuleVO);
        //3、查询出所有符合要求的数据
        List<TCaSingleRuleVO> tCaSingleRuleVOList =  tCaSingleRuleMapper.getList(tCaSingleRuleVO);
        //4、拼装成需要的格式
        List<TCaSingleRuleVO> finalTCaSingleRuleVOList = new Page<>();
        if(tCaSingleRuleVOList!=null) {
            BeanUtils.copyProperties(tCaSingleRuleVOList, finalTCaSingleRuleVOList);
            tCaSingleRuleVOList.stream().forEach(rule -> {
                finalTCaSingleRuleVOList.add(rule.invokeToVo());
            });
        }
        //5、组装成paginate对象
        Paginate paginate = createPaginate(pageNo, pageSize, finalTCaSingleRuleVOList);
        return paginate;
    }

    /**
     * 根据ID查询对应的规则对象
     * @param id
     * @return
     *
     */
    @Override
    public TCaSingleRuleVO getById(Long id) {
        log.info("根据ID查询对应的规则对象，开始：{}", id);

        //0、从redis中取值
        TCaSingleRuleVO tCaSingleRuleVO = null;
//                (TCaSingleRuleVO)cacheClient.getObject(RedisKey.SINGLE_RULE+id);
        if(tCaSingleRuleVO!=null){
            return tCaSingleRuleVO;
        }

        //1、从库中取出对应的规则
        TCaSingleRule tCaSingleRule = tCaSingleRuleMapper.getById(id);
        //判空
        if(tCaSingleRule==null){
            return null;
        }

        //2、通过规则，查出所有的行
        List<TCaRuleLineVO> tCaRuleLineVOList = ruleLineService.queryLineListByRuleCode(tCaSingleRule.getRuleCode());


        //3、转换并插入对应的line
        tCaSingleRuleVO = tCaSingleRule.invokeToVo();
        tCaSingleRuleVO.setIfModule(
                tCaRuleLineVOList.stream().filter(line -> "IF".equals(line.getRuleLineModule())).collect(Collectors.toList())
        );
        tCaSingleRuleVO.setThenModule(
                tCaRuleLineVOList.stream().filter(line -> "THEN".equals(line.getRuleLineModule())).collect(Collectors.toList())
        );
        tCaSingleRuleVO.setElseModule(
                tCaRuleLineVOList.stream().filter(line -> "ELSE".equals(line.getRuleLineModule())).collect(Collectors.toList())
        );
        tCaSingleRuleVO.setSingleModule(
                tCaRuleLineVOList.stream().filter(line -> "SINGLE".equals(line.getRuleLineModule())).collect(Collectors.toList())
        );

        //4、保存到redis中
//        cacheClient.setObject(RedisKey.SINGLE_RULE+id,tCaSingleRuleVO);
//        cacheClient.expire(RedisKey.SINGLE_RULE+id,Constants.INTEGER_3600);
        log.info("根据ID查询对应的规则对象，返回：{}", JSON.toJSONString(tCaSingleRuleVO));

        return tCaSingleRuleVO;
    }


    /**
     * 根据CODE查询对应的规则对象
     * @param id
     * @return
     *
     */
    @Override
    public TCaSingleRuleVO getByCode(String ruleCode) {

        //0、从redis中取值
        TCaSingleRuleVO tCaSingleRuleVO = null;
//                (TCaSingleRuleVO)cacheClient.getObject(RedisKey.SINGLE_RULE+ruleCode);
        if(tCaSingleRuleVO!=null){
            return tCaSingleRuleVO;
        }

        //1、从库中取出对应的规则
        TCaSingleRule tCaSingleRule = tCaSingleRuleMapper.getByCode(ruleCode);
        //判空
        if(tCaSingleRule==null){
            return null;
        }

        //2、通过规则，查出所有的行
        List<TCaRuleLineVO> tCaRuleLineVOList = ruleLineService.queryLineListByRuleCode(tCaSingleRule.getRuleCode());


        //3、转换并插入对应的line
        tCaSingleRuleVO = tCaSingleRule.invokeToVo();
        tCaSingleRuleVO.setIfModule(
                tCaRuleLineVOList.stream().filter(line -> "IF".equals(line.getRuleLineModule())).collect(Collectors.toList())
        );
        tCaSingleRuleVO.setThenModule(
                tCaRuleLineVOList.stream().filter(line -> "THEN".equals(line.getRuleLineModule())).collect(Collectors.toList())
        );
        tCaSingleRuleVO.setElseModule(
                tCaRuleLineVOList.stream().filter(line -> "ELSE".equals(line.getRuleLineModule())).collect(Collectors.toList())
        );
        tCaSingleRuleVO.setSingleModule(
                tCaRuleLineVOList.stream().filter(line -> "SINGLE".equals(line.getRuleLineModule())).collect(Collectors.toList())
        );

        //4、保存到redis中
//        cacheClient.setObject(RedisKey.SINGLE_RULE+ruleCode,tCaSingleRuleVO);
//        cacheClient.expire(RedisKey.SINGLE_RULE+ruleCode,Constants.INTEGER_3600);

        return tCaSingleRuleVO;
    }


    /**
     * 插入规则
     * @param tCaSingleRuleVO
     *
     */
    @Override
    public void insert(TCaSingleRuleVO tCaSingleRuleVO) {
        //1、校验：同一业务线下规则名称不允许重复
        TCaSingleRule queryTCaSingleRule = new TCaSingleRule();
        queryTCaSingleRule.setBusinessCode(tCaSingleRuleVO.getBusinessCode()==null?"":tCaSingleRuleVO.getBusinessCode());
        queryTCaSingleRule.setRuleName(tCaSingleRuleVO.getRuleName()==null?"":tCaSingleRuleVO.getRuleName());
        List<TCaSingleRuleVO> queryList = tCaSingleRuleMapper.getList(queryTCaSingleRule.invokeToVo());
        if(queryList!=null&&queryList.size()>0){
            throw new RuleRuntimeException("同一业务线下规则名称不允许重复");
        }

        //2、分部分插入
        //得到IF
        List<TCaRuleLineVO> ifModule = tCaSingleRuleVO.getIfModule()==null?null:tCaSingleRuleVO.getIfModule();
        //得到THEN
        List<TCaRuleLineVO> thenModule = tCaSingleRuleVO.getThenModule()==null?null:tCaSingleRuleVO.getThenModule();
        //得到ELSE
        List<TCaRuleLineVO> elseModule = tCaSingleRuleVO.getElseModule()==null?null:tCaSingleRuleVO.getElseModule();
        //得到SINGLE
        List<TCaRuleLineVO> singleModule = tCaSingleRuleVO.getSingleModule()==null?null:tCaSingleRuleVO.getSingleModule();

        if(singleModule == null && (ifModule == null || thenModule == null || elseModule == null)){
            throw new RuleRuntimeException("参数模块格式错误！请确认后再插入");
        }

        //填充预留字段
        complementProperty(tCaSingleRuleVO);

        if(singleModule != null ){
            //当是single的时候,插入single
//            List<TCaRuleLineVO> singleLineList = tCaSingleRuleVO.getSingleModule();
            Integer singleSort = 0;
            for(TCaRuleLineVO line : singleModule){
                singleSort = singleSort+1;
                ruleLineService.insertLine(line,tCaSingleRuleVO,"SINGLE",singleSort);
            }
        }else{
            //当时if表达式的时候
            //插入IF
            Integer ifSort = 0;
            for(TCaRuleLineVO line : ifModule){
                ifSort = ifSort+1;
                ruleLineService.insertLine(line,tCaSingleRuleVO,"IF",ifSort);
            }
            //插入THEN
            Integer thenSort = 0;
            for(TCaRuleLineVO line : thenModule){
                thenSort = thenSort+1;
                ruleLineService.insertLine(line,tCaSingleRuleVO,"THEN",thenSort);
            }
            //插入ELSE
            Integer elseSort = 0;
            for(TCaRuleLineVO line : elseModule){
                elseSort = elseSort+1;
                ruleLineService.insertLine(line,tCaSingleRuleVO,"ELSE",elseSort);
            }
        }


        //插入对应的规则
        initSaveWord(tCaSingleRuleVO);
        Integer count = tCaSingleRuleMapper.insert(tCaSingleRuleVO);
        if(count==0){
            throw new RuleRuntimeException("新增失败，请稍后再试！");
        }
    }




    /**
     * 更新规则
     * @param tCaSingleRuleVO
     *
     */
    @Override
    public void updateByPrimaryKey(TCaSingleRuleVO tCaSingleRuleVO) {

        //删除redis中的值
//        cacheClient.del(RedisKey.SINGLE_RULE+tCaSingleRuleVO.getId());

        //同一业务线下规则名称不允许重复
        if(tCaSingleRuleVO.getRuleCode()!=null){
            TCaSingleRule queryTCaSingleRule = new TCaSingleRule();
            queryTCaSingleRule.setBusinessCode(tCaSingleRuleVO.getBusinessCode()==null?"":tCaSingleRuleVO.getBusinessCode());
            queryTCaSingleRule.setRuleCode(tCaSingleRuleVO.getRuleCode()==null?"":tCaSingleRuleVO.getRuleCode());
            List<TCaSingleRuleVO> queryList = tCaSingleRuleMapper.getList(queryTCaSingleRule.invokeToVo());
            if(queryList!=null&&queryList.size()>0){
                queryList.stream().forEach(l -> {
                    if(!l.getId().equals(tCaSingleRuleVO.getId())){
                        throw new RuleRuntimeException("同一业务线下规则名称不允许重复");
                    }
                });
            }
        }

        //2、分部分插入
        //得到IF
        List<TCaRuleLineVO> ifModule = tCaSingleRuleVO.getIfModule()==null?null:tCaSingleRuleVO.getIfModule();
        //得到THEN
        List<TCaRuleLineVO> thenModule = tCaSingleRuleVO.getThenModule()==null?null:tCaSingleRuleVO.getThenModule();
        //得到ELSE
        List<TCaRuleLineVO> elseModule = tCaSingleRuleVO.getElseModule()==null?null:tCaSingleRuleVO.getElseModule();
        //得到SINGLE
        List<TCaRuleLineVO> singleModule = tCaSingleRuleVO.getSingleModule()==null?null:tCaSingleRuleVO.getSingleModule();

        //3、更新
        if(ifModule!=null || thenModule!=null || elseModule!=null || singleModule!=null){
            //当表达式有变化的时候，会把原有的表达式全部删除，并且加入新的表达式
            if(singleModule == null && (ifModule == null || thenModule == null || elseModule == null)){
                throw new RuleRuntimeException("参数模块格式错误！请确认后再插入");
            }

            //把所有行置为无效
            ruleLineService.unUseLine(tCaSingleRuleVO.getRuleCode());

            //然后走更新的逻辑
            if(tCaSingleRuleVO.getSingleModule() != null ){
                //当是single的时候,插入single
                Integer singleSort = 0;
                for(TCaRuleLineVO line : singleModule){
                    singleSort = singleSort+1;
                    ruleLineService.updateLine(line,tCaSingleRuleVO,"SINGLE",singleSort);
                }
            }else{
                //当时if表达式的时候
                //插入IF
                Integer ifSort = 0;
                for(TCaRuleLineVO line : ifModule){
                    ifSort = ifSort+1;
                    ruleLineService.updateLine(line,tCaSingleRuleVO,"IF",ifSort);
                }
                //插入THEN
                Integer thenSort = 0;
                for(TCaRuleLineVO line : thenModule){
                    thenSort = thenSort+1;
                    ruleLineService.updateLine(line,tCaSingleRuleVO,"THEN",thenSort);
                }
                //插入ELSE
                Integer elseSort = 0;
                for(TCaRuleLineVO line : elseModule){
                    elseSort = elseSort+1;
                    ruleLineService.updateLine(line,tCaSingleRuleVO,"ELSE",elseSort);
                }
            }
        }

        //填充预留字段
        complementProperty(tCaSingleRuleVO);

        //修改对应的规则
        initUpdateWord(tCaSingleRuleVO);
        Integer count = tCaSingleRuleMapper.updateByPrimaryKey(tCaSingleRuleVO);
        if(count==0){
            throw new RuleRuntimeException("更新失败，没有对应的数据！");
        }
    }

    /**
     * 删除规则
     * @param id
     *
     */
    @Override
    public void delete(Long id) {
        //删除redis中的值
//        cacheClient.del(RedisKey.SINGLE_RULE+id);

        TCaSingleRule deltCaSingleRule = tCaSingleRuleMapper.getById(id);


        TCaSingleRule tCaSingleRule = new TCaSingleRule();
        tCaSingleRule.setId(id);
        tCaSingleRule.setDeleteFlag(Constants.SHORT_ONE);
        initUpdateWord(tCaSingleRule);
        Integer count = tCaSingleRuleMapper.updateByPrimaryKey(tCaSingleRule);
        if(count==0){
            throw new RuleRuntimeException("删除失败，没有对应的数据！");
        }

        //删除行
        ruleLineService.deleteLine(deltCaSingleRule.getRuleCode());
    }



    /**
     * 填充计算表达式，规则代码，输出项代码
     */
    public void complementProperty( TCaSingleRule tCaSingleRule){

        //生成对应的可计算的表达式
//        if(!StringUtils.isEmpty(tCaSingleRule.getShowRuleExpression())){
//            String realRuleExpression = ConvertExpression.convertExpression(JSON.parseObject(tCaSingleRule.getShowRuleExpression()));
//            tCaSingleRule.setRealRuleExpression(realRuleExpression);
//        }

        //生成规则代码 和 输出项代码(只有在新增的时候需要生成，编辑的时候不需要变动)
        if(tCaSingleRule.getId()==null){
            //获取日期戳
            String YYYYMMDD = DateUtils.getString(new Date(), DateUtils.YYYYMMDD);
            //自增并得到对应的结果
            sequenceService.incr(YYYYMMDD, SequenceTypeEnum.RULE_SEQ.getTypeName());
            Integer num = sequenceService.queryLatestNum(YYYYMMDD, SequenceTypeEnum.RULE_SEQ.getTypeName());
            //拼接出ruleCode
            String ruleCode = SequenceTypeEnum.RULE_SEQ.getTypeName()+"_"+tCaSingleRule.getBusinessCode()+"_"+YYYYMMDD+"_"+String.format("%03d", num);
            String outPutCode = ruleCode+ Constants._OUT_PUT;
            tCaSingleRule.setRuleCode(ruleCode);
            tCaSingleRule.setOutPutCode(outPutCode);
        }

    }



    /**
     * 批量插入，并返回插入结果信息
     * @param tCaSingleRuleList
     * @param info
     */
    @Override
    public void batchInsertNotExist(List<TCaSingleRule> tCaSingleRuleList, StringBuffer info) {
        //1、判空
        if(tCaSingleRuleList==null){
            info.append("待插入的list为空，请查实！");
            return;
        }

        //2、逐条判断，并插入
        //如果存在就不插入，如果不存在则插入
        tCaSingleRuleList.stream().forEach(sr -> {
            try {
                initSaveWord(sr);

                int count = tCaSingleRuleMapper.insertNotExist(sr);

                if(count<1){
                    log.error("code为["+sr.getRuleCode()+"]的规则重复了，请确认！");
                    throw new RuleRuntimeException("已存在，插入失败");
                }
            }catch (Exception e){
                info.append(sr.getRuleCode()).append(",");
            }
        });



    }







    /* --------------------- 以上是前后端交互的业务代码 ------------------------------ */

    /* ----------------------以下是计算的逻辑代码-------------------------------------*/

    /**
     * 测试规则表达式
     * @param tCaSingleRule
     * @param params
     */
    @Override
    public Object testRuleExpression(TCaSingleRule tCaSingleRule, Map<String,Object> params) {

        // todo
        return null;

    }


    /**
     * 计算对应的表达式
     * @param ruleId        规则ID
     * @param ruleCode      规则CODE
     * @param jsonObject    传入的参数
     * @return
     */
    @Override
    public JSONObject calculateByIdORCode(Long ruleId,String ruleCode, JSONObject jsonObject) {
        log.info("开始计算规则，规则ID是{}，规则CODE是{}",ruleId,ruleCode);

        //1、获取对应的规则
        TCaSingleRuleVO tCaSingleRuleVO = null;
        if(ruleId!=null){
            tCaSingleRuleVO = getById(ruleId);
        }else{
            tCaSingleRuleVO = getByCode(ruleCode);
        }



        if(tCaSingleRuleVO == null){
            throw new RuleRuntimeException("没有查询到对应的规则！");
        }

        //2、开始计算逻辑
        JSONObject resultJson = new JSONObject();
        Object finalResult = null;
        //其次，按照顺序，每行LineModle 分别计算
        StringBuffer sb = new StringBuffer();

        //当有if表达式，则说明是条件表达式
        if(tCaSingleRuleVO.getIfModule()!=null && tCaSingleRuleVO.getIfModule().size()>0){
            List<TCaRuleLineVO> ifLineList = tCaSingleRuleVO.getIfModule();
            List<TCaRuleLineVO> thenLineList = tCaSingleRuleVO.getThenModule().stream()
                    .sorted(Comparator.comparing(TCaRuleLineVO::getSort))
                    .collect(Collectors.toList());
            List<TCaRuleLineVO> elseLineList = tCaSingleRuleVO.getElseModule().stream()
                    .sorted(Comparator.comparing(TCaRuleLineVO::getSort))
                    .collect(Collectors.toList());
            if(ifLineList==null||thenLineList==null||elseLineList==null){
                throw new RuntimeException("ifLineList或者thenLineList或者elseLineList为空");
            }

            //拼接if
            sb.append("(");

            ifLineList.stream().forEach(ifLine -> {
                String leftBracket = ifLine.getLineLeftBracket()==(short)1?"(":"";
                String rightBracket = ifLine.getLineRightBracket()==(short)1?")":"";
                String connector = ifLine.getLineConnector()==null?"":ifLine.getLineConnector();

                JSEngineCalculation.calculateLine(ifLine,jsonObject,new JSONObject());

                sb.append(leftBracket).append(ifLine.getLineResult()).append(rightBracket).append(connector);
            });

            sb.append(")");

            //计算if的结果，如果为true，则走then ，反之则走else
            if((Boolean) JSEngineCalculation.jsCalculate(sb.toString())){
                //拼接then
                finalResult = JSEngineCalculation.calculateLines(thenLineList,jsonObject);
            }else{
                //拼接else
                finalResult = JSEngineCalculation.calculateLines(elseLineList,jsonObject);
            }
        }else if(tCaSingleRuleVO.getSingleModule()!=null && tCaSingleRuleVO.getSingleModule().size()>0){
            //说明不是条件表达式
            List<TCaRuleLineVO> singleLineList = tCaSingleRuleVO.getSingleModule().stream()
                    .sorted(Comparator.comparing(TCaRuleLineVO::getSort))
                    .collect(Collectors.toList());
            if(singleLineList==null){
                throw new RuntimeException("singleLineList为空");
            }
            finalResult = JSEngineCalculation.calculateLines(singleLineList,jsonObject);

        }else{
            //如果两个都是空，则表明规则错误
            throw new RuntimeException("规则错误！");
        }


        log.info("这里计算完成了，结果是{}",finalResult);

        try{

            //2.3、把结果转换成指定的格式
            Object result = null;
            OutPutTypeEnum outPutTypeEnum = OutPutTypeEnum.getByCode(tCaSingleRuleVO.getOutPutType());
            if(OutPutTypeEnum.INTEGER.equals(outPutTypeEnum)){
                result =  Integer.parseInt(finalResult.toString());
            }else if(OutPutTypeEnum.DOUBLE.equals(outPutTypeEnum)){
                result = new BigDecimal(finalResult.toString()).setScale(Integer.parseInt(tCaSingleRuleVO.getOutPutFormat()),BigDecimal.ROUND_HALF_UP);
            }else if(OutPutTypeEnum.STRING.equals(outPutTypeEnum)){
                result =  finalResult == null ? "" : finalResult.toString();
            }else if(OutPutTypeEnum.DATE.equals(outPutTypeEnum)){
                result = DateUtils.getString((Date) finalResult,tCaSingleRuleVO.getOutPutFormat());
            }else if(OutPutTypeEnum.TIME.equals(outPutTypeEnum)){
                result =  DateUtils.getString((Date)finalResult,tCaSingleRuleVO.getOutPutFormat());
            }else if(OutPutTypeEnum.BOOLEAN.equals(outPutTypeEnum)){
                result =  (Boolean) finalResult;
            }else if(OutPutTypeEnum.LIST.equals(outPutTypeEnum)){
//                AbstractJSObject json = (AbstractJSObject)finalResult;
//                result = (List) json.values();
                result = (List) finalResult;
            }
            resultJson.put(tCaSingleRuleVO.getOutPutName(),result);
            log.info("这里转换完成了，结果是{}",result);

        }catch (RuleRuntimeException ruleException){
            log.error(String.format("ID为%s的规则计算失败，错误信息是：\""+ruleException.getMessage()+"\"",ruleId));
            resultJson.put(tCaSingleRuleVO.getOutPutName(),ruleException.getMessage());
            throw new RuleRuntimeException(String.format("ID为%s的规则计算失败，请查实后重试！",ruleId));
        } catch(Exception e){
            log.error(String.format("ID为%s的规则计算失败，错误信息是：\""+e.getMessage()+"\"",ruleId),e);
            resultJson.put(tCaSingleRuleVO.getOutPutName(),"计算失败，请查实后重试！");
            throw new RuleRuntimeException("计算失败，请查实后重试！");
        }

        return resultJson;
    }



    /**
     * 批量计算表达式
     * @param ruleIdList
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject calculateByIdList(List<Long> ruleIdList,JSONObject jsonObject) {

        List<CallableTemplate<JSONObject>> tasks = new ArrayList<CallableTemplate<JSONObject>>();

        // 将需要计算的表达式，放在一个集合中
        for (int i = 0; i < ruleIdList.size(); i++) {
            QueryByCompanyCodeThred queryByCompanyCodeThred = new QueryByCompanyCodeThred(ruleIdList.get(i),jsonObject);
            tasks.add(queryByCompanyCodeThred);
        }
        //通过多线程一次性计算表达式，并拿到返回结果集
        List<JSONObject> results = null;
        try {
            results = callableTaskFrameWork.submitsAll(tasks);
            // 解析返回结果集
            JSONObject finalJson = new JSONObject();
            for (JSONObject json : results) {
                //放入返回的对象中
                finalJson.putAll(json);
                //放入初始的json对象中，后面的计算可能会用到
                jsonObject.putAll(json);
            }
            return finalJson;
        } catch (Exception e) {
            log.error("批量执行异常！"+e.getMessage());
            throw new RuleRuntimeException(e.getMessage());
        }
    }




    /**
     * 执行计算的线程
     */
    class QueryByCompanyCodeThred extends CallableTemplate<JSONObject>  {

        private Long ruleId;
        private JSONObject jsonObject;

        public QueryByCompanyCodeThred(Long ruleId, JSONObject jsonObject) {
            this.ruleId = ruleId;
            this.jsonObject = jsonObject;
        }

        @Override
        public JSONObject process() {
            JSONObject result = calculateByIdORCode(ruleId,null,jsonObject);
            return result;
        }

    }

}
