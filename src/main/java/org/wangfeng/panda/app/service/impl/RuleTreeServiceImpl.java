package org.wangfeng.panda.app.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wangfeng.panda.app.calculation.JSEngineCalculation;
import org.wangfeng.panda.app.common.Constants;
import org.wangfeng.panda.app.common.Paginate;
import org.wangfeng.panda.app.common.base.AppBaseService;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.dao.domain.TCaRuleTree;
import org.wangfeng.panda.app.dao.domain.TCaRuleTreeMapping;
import org.wangfeng.panda.app.dao.domain.TCaRuleTreeNode;
import org.wangfeng.panda.app.dao.mapper.TCaRuleTreeMapper;
import org.wangfeng.panda.app.model.enums.NodeTypeEnum;
import org.wangfeng.panda.app.model.enums.SequenceTypeEnum;
import org.wangfeng.panda.app.model.vo.TCaRuleTreeMappingVO;
import org.wangfeng.panda.app.model.vo.TCaRuleTreeNodeVO;
import org.wangfeng.panda.app.model.vo.TCaRuleTreeVO;
import org.wangfeng.panda.app.service.*;
import org.wangfeng.panda.app.util.DateUtils;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("all")
@Service
@Slf4j
public class RuleTreeServiceImpl extends AppBaseService implements RuleTreeService {

    @Autowired
    private TCaRuleTreeMapper tCaRuleTreeMapper;


    @Autowired
    private SingleRuleService singleRuleService;
    @Autowired
    private RuleListService ruleListService;
    @Autowired
    private RuleTreeMappingService ruleTreeMappingService;
    @Autowired
    private RuleTreeNodeService ruleTreeNodeService;
    @Autowired
    private SequenceService sequenceService;


    /**
     * 分页查询数据
     * @param tCaRuleTreeVO
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Paginate queryPagenate(TCaRuleTreeVO tCaRuleTreeVO, Integer pageNo, Integer pageSize) {
        //1、增加分页参数
        this.setPage(pageNo, pageSize);
        //2、去除%等特殊字符的影响
        distinguishString(tCaRuleTreeVO);
        //3、查询出所有符合要求的数据
        List<TCaRuleTreeVO> tCaRuleTreeVOList = tCaRuleTreeMapper.getList(tCaRuleTreeVO);
        //4、拼装成需要的格式
        List<TCaRuleTreeVO> finalTCaRuleTreeVOList = new Page<>();
        if(tCaRuleTreeVOList!=null) {
            BeanUtils.copyProperties(tCaRuleTreeVOList, finalTCaRuleTreeVOList);
            tCaRuleTreeVOList.stream().forEach(variable ->{
                finalTCaRuleTreeVOList.add(variable.invokeToVo());
            });
        }
        //5、组装成paginate对象
        Paginate paginate = createPaginate(pageNo, pageSize, finalTCaRuleTreeVOList);
        return paginate;
    }

    /**
     * 通过ID获取
     * @param id
     * @return
     */
    @Override
    public TCaRuleTreeVO getById(Long id) {
        //1、获得主表数据
        TCaRuleTreeVO tCaRuleTreeVO = tCaRuleTreeMapper.getById(id);

        if(tCaRuleTreeVO==null){
            throw new RuleRuntimeException("没有查到对应的决策树！");
        }

        //2、通过决策树ID去查找其下面的节点和对应的关联关系
        List<TCaRuleTreeMapping> tCaRuleTreeMappingList = ruleTreeMappingService.queryAllMappingByTreeCode(tCaRuleTreeVO.getRuleTreeCode());
        List<TCaRuleTreeNodeVO> tCaRuleTreeNodeList = ruleTreeNodeService.queryAllNodeByTreeCode(tCaRuleTreeVO.getRuleTreeCode());

        //3、把node封装成一个map
        Map<String, TCaRuleTreeNode> nodeMap = tCaRuleTreeNodeList.stream().collect(Collectors.toMap(TCaRuleTreeNode::getNodeId, tCaRuleTreeNode -> tCaRuleTreeNode,(oldValue , newValue) -> oldValue));

        //4、完善connectors
        List<TCaRuleTreeMappingVO> tCaRuleTreeMappingVOList = new ArrayList<>();
        tCaRuleTreeMappingList.stream().forEach(mapping -> {
            TCaRuleTreeMappingVO vo = new TCaRuleTreeMappingVO();
            BeanUtils.copyProperties(mapping,vo);
            vo.setSourceNode(nodeMap.get(mapping.getSourceNodeId()));
            vo.setTargetNode(nodeMap.get(mapping.getTargetNodeId()));
            tCaRuleTreeMappingVOList.add(vo);
        });

        tCaRuleTreeVO.setNodes(tCaRuleTreeNodeList);
        tCaRuleTreeVO.setConnectors(tCaRuleTreeMappingVOList);

        return tCaRuleTreeVO;
    }

    /**
     * 插入对应的决策树
     * @param tCaRuleTreeVO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(TCaRuleTreeVO tCaRuleTreeVO) {

        //1、填充预留字段
        complementProperty(tCaRuleTreeVO);
        initSaveWord(tCaRuleTreeVO);

        //2、插入对应的决策树
        tCaRuleTreeMapper.insert(tCaRuleTreeVO);

        //3、插入对应的节点
        ruleTreeNodeService.insertByTCaRuleTreeVO(tCaRuleTreeVO.getNodes(),tCaRuleTreeVO.getRuleTreeCode());

        //4、插入对应的节点关联关系
        ruleTreeMappingService.insertByTCaRuleTreeVO(tCaRuleTreeVO.getConnectors(),tCaRuleTreeVO.getRuleTreeCode());

    }

    /**
     * 更新对应的决策树
     * @param tCaRuleTreeVO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByPrimaryKey(TCaRuleTreeVO tCaRuleTreeVO) {
        //1、填充预留字段
        initUpdateWord(tCaRuleTreeVO);

        //2、更新规则树表
        Integer count = tCaRuleTreeMapper.updateByPrimaryKey(tCaRuleTreeVO);
        if(count==0){
            throw new RuleRuntimeException("更新失败，没有对应的数据！");
        }

        //3、当node不为空时，更新node表
        if(tCaRuleTreeVO.getNodes()!=null && tCaRuleTreeVO.getNodes().size()>0){
            //删除已有的所有的节点
            ruleTreeNodeService.deleteByRuleTreeCode(tCaRuleTreeVO.getRuleTreeCode());

            //增加新增的节点
            ruleTreeNodeService.insertByTCaRuleTreeVO(tCaRuleTreeVO.getNodes(),tCaRuleTreeVO.getRuleTreeCode());
        }


        //4、当Connectors不为空时，更新mapping关系表
        if(tCaRuleTreeVO.getConnectors()!=null && tCaRuleTreeVO.getConnectors().size()>0){
            //删除已有的所有的映射关系
            ruleTreeMappingService.deleteByRuleTreeCode(tCaRuleTreeVO.getRuleTreeCode());

            //增加新增的映射关系
            ruleTreeMappingService.insertByTCaRuleTreeVO(tCaRuleTreeVO.getConnectors(),tCaRuleTreeVO.getRuleTreeCode());
        }


    }

    /**
     * 按照ID删除对应的决策树
     * @param id
     */
    @Override
    public void delete(Long id) {

        //0、查询对应的树
        TCaRuleTreeVO tCaRuleTreeVO = tCaRuleTreeMapper.getById(id);


        //1、删除决策树表中的数据
        TCaRuleTree tree = new TCaRuleTree();
        tree.setId(id);
        tree.setDeleteFlag(Constants.SHORT_ONE);
        initUpdateWord(tree);

        tCaRuleTreeMapper.updateByPrimaryKey(tree);

        //2、删除决策树节点表，决策树节点映射表中的数据
        ruleTreeNodeService.deleteByRuleTreeCode(tCaRuleTreeVO.getRuleTreeCode());
        ruleTreeMappingService.deleteByRuleTreeCode(tCaRuleTreeVO.getRuleTreeCode());

    }


    /**
     * 批量插入，并返回插入结果信息
     * @param tCaRuleTreeList
     * @param info
     */
    @Override
    public void batchInsertNotExist(List<TCaRuleTree> tCaRuleTreeList, StringBuffer info) {
        //1、判空
        if(tCaRuleTreeList==null){
            info.append("待插入的list为空，请查实！");
            return;
        }

        //2、逐条判断，并插入
        //如果存在就不插入，如果不存在则插入
        tCaRuleTreeList.stream().forEach(rt -> {
            try {
                initSaveWord(rt);

                int count = tCaRuleTreeMapper.insertNotExist(rt);

                if(count<1){
                    log.error("code为["+rt.getRuleTreeCode()+"]的决策树重复了，请确认！");
                    throw new RuleRuntimeException("已存在，插入失败");
                }
            }catch (Exception e){
                info.append(rt.getRuleTreeCode()).append(",");
            }
        });


    }



    /**
     * 填充计算表达式，规则代码，输出项代码
     *
     * @param tCaRuleTree
     */
    public void complementProperty( TCaRuleTree tCaRuleTree){
        //生成规则集编号
        String businessCode = tCaRuleTree.getBusinessCode();
        //获取日期戳
        String YYYYMMDD = DateUtils.getString(new Date(), DateUtils.YYYYMMDD);
        //自增并得到对应的结果
        sequenceService.incr(YYYYMMDD, SequenceTypeEnum.RULE_TREE_SEQ.getTypeName());
        Integer num = sequenceService.queryLatestNum(YYYYMMDD, SequenceTypeEnum.RULE_TREE_SEQ.getTypeName());
        //拼接出ruleListCode
        String ruleTreeCode = SequenceTypeEnum.RULE_TREE_SEQ+"_"+businessCode+"_"+YYYYMMDD+"_"+String.format("%03d", num);

        tCaRuleTree.setRuleTreeCode(ruleTreeCode);
    }



    /* --------------------- 以上是前后端交互的业务代码 ------------------------------ */

    /* ----------------------以下是计算的逻辑代码-------------------------------------*/


    /**
     * 通过决策树ID进行计算
     * @param id
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject calculateRuleTreeById(Long id,JSONObject jsonObject) {
        //1、获取对应的决策树
        TCaRuleTreeVO tCaRuleTreeVO = getById(id);
        log.info("获取到对应的决策树：{}",new Gson().toJson(tCaRuleTreeVO));

        //2、把所有的node拼装成一个map
        List<TCaRuleTreeNodeVO> tCaRuleTreeNodeList = tCaRuleTreeVO.getNodes();
        List<TCaRuleTreeMappingVO> tCaRuleTreeMappingVOList = tCaRuleTreeVO.getConnectors();
        //转换成nodemap方便获取
        Map<String,TCaRuleTreeNode> nodeMap = tCaRuleTreeNodeList.stream().collect(Collectors.toMap(TCaRuleTreeNode::getNodeId, tCaRuleTreeNode -> tCaRuleTreeNode,(oldValue , newValue) -> oldValue));


        Map<String, List<TCaRuleTreeNode>> params = new HashMap<>();
        tCaRuleTreeMappingVOList.stream().forEach(t->{
            String key = t.getSourceNodeId();
            List<TCaRuleTreeNode> list = new ArrayList<>();
            if(params.containsKey(key)) {
                list = params.get(key);
            }
            TCaRuleTreeNode node = nodeMap.get(t.getTargetNodeId());
            list.add(node);
            params.put(key,list);
        });

        //3、开始递归计算，直到获取不到子节点为止
        log.info("开始进行递归运算====================================");
        String key = tCaRuleTreeVO.getRootRuleNodeId();

        Long sourceId = nodeMap.get(key).getNodeRuleId();
        String nodeType = nodeMap.get(key).getType();

        JSONObject result = new JSONObject();
        while(params.containsKey(key)){
            //判断是否进有分支命中的标志位
            Boolean hasChanged = false;

            //获取所有的子分支
            List<TCaRuleTreeNode> sonList = params.get(key);

            //遍历所有的子节点
            for(TCaRuleTreeNode son : sonList){

                key = son.getNodeId();
                sourceId = son.getNodeRuleId();
                nodeType = son.getType();

                //如果是开始节点，或者是条件节点，则直接跳过，不参与计算
                if((NodeTypeEnum.KAI_SHI.getName().equals(nodeType)||NodeTypeEnum.TIAO_JIAN.getName().equals(nodeType))){
                    hasChanged = true;
                    break;
                }

                //计算规则，规则集的结果
                if(son.getNodeRuleId()==null){
                    throw new RuleRuntimeException(String.format("该节点没有配置对应规则，节点node_id是%d",son.getNodeId()));
                }

                //获取节点上的表达式，来判断是否命中该分支
                String expression = son.getJudgementCondition();
                //当没有对应的表达式的时候，计算并到下一层
                if((expression==null||expression.length()==0)){
                    hasChanged = true;
                    result =  realCalculate(son.getNodeRuleCode(),son.getType(),jsonObject);
                    jsonObject.putAll(result);
                    break;
                }

                //如果有对应的表达式的时候，计算决策树判断逻辑，如果命中则退出循环
                Boolean obj = (Boolean) JSEngineCalculation.calculate(expression,jsonObject);
                if(obj){
                    result =  realCalculate(son.getNodeRuleCode(),son.getType(),jsonObject);
                    jsonObject.putAll(result);
                    hasChanged = true;
                    break;
                }
            }

            //如果全部没有被命中，则抛出异常告知！
            if(!hasChanged){
                throw new RuleRuntimeException("存在没有命中的分支，请查实！");
            }

        }
        log.info("结束进行递归运算====================================");

        //4、返回最后的计算的返回的值
        return result;
    }




    /**
     * 按照是规则还是规则集来进行计算
     * @param id
     * @param nodeType
     * @param jsonObject
     * @return
     */
    private JSONObject realCalculate(String code , String nodeType, JSONObject jsonObject){
        JSONObject result = new JSONObject();
        if(code==null){
            throw new RuleRuntimeException("该节点没有配置对应规则");
        }
        if(NodeTypeEnum.GUI_ZE.getName().equals(nodeType)){
            log.info("开始计算决策树中的规则，规则CODE是：{}",code);
            result = singleRuleService.calculateByIdORCode(null,code,jsonObject);
        }else if(NodeTypeEnum.GUI_ZE_JI.getName().equals(nodeType)){
            log.info("开始计算决策树中的规则集，规则集CODE是：{}",code);
            result = ruleListService.calculateRuleListByIdORCode(null,code,jsonObject);
        }
        return result;
    }





//
//    @Autowired
//    private TCaDecisionVariableMapper tCaDecisionVariableMapper;
//    @Autowired
//    private TCaSingleRuleMapper tCaSingleRuleMapper;

//    /**
//     * 第二版根据决策树ID去运算
//     * @param id
//     * @param jsonObject
//     * @return
//     */
//    @Override
//    public JSONObject calculateRuleTreeById2(Long id,JSONObject jsonObject) {
//
//        //1、获取对应的决策树
//        TCaRuleTreeVO tCaRuleTreeVO = getById(id);
//        log.info("获取到对应的决策树：{}",new Gson().toJson(tCaRuleTreeVO));
//
//        //4、获取树的那条线
//        List<TCaRuleTreeNodeVO> nodes = tCaRuleTreeVO.getNodes();
//        Map<String,TCaRuleTreeNodeVO> nodeVOMap = nodes.stream().collect(Collectors.toMap(TCaRuleTreeNodeVO::getNodeId,tCaRuleTreeNodeVO -> tCaRuleTreeNodeVO,(oldValue,newValue) -> newValue));
//
//        List<TCaRuleTreeMappingVO> connectors = tCaRuleTreeVO.getConnectors();
//        Map<String,List<TCaRuleTreeNode>> connectorVOMap = new HashMap<>();
//        connectors.stream().forEach(connector->{
//            List<TCaRuleTreeNode> list = new ArrayList<>();
//            if(connectorVOMap.containsKey(connector.getSourceNodeId())){
//                list = connectorVOMap.get(connector.getSourceNodeId());
//            }
//            list.add(connector.getTargetNode());
//            connectorVOMap.put(connector.getSourceNodeId(),list);
//        });
//
//
//        //生成一条线
//        StringBuffer sb = new StringBuffer();
//        //所有的规则和决策变量的输出
//        JSONObject finalJson = jsonObject;
//        TCaRuleTreeNodeVO finalNode = new TCaRuleTreeNodeVO();
//        //当前节点的ID，如果是空，则结束了！
//        TCaRuleTreeNodeVO currentNode = nodeVOMap.get(tCaRuleTreeVO.getRootRuleNodeId());
//        while (currentNode!=null&&!StringUtils.isBlank(currentNode.getNodeId())){
//
//            if(NodeTypeEnum.GUI_ZE_JI.getName().equals(currentNode.getType())){ //如果节点是规则集
//                //获取对应的规则集合详情
//                TCaRuleListVO tCaRuleListVO = ruleListService.getById(currentNode.getNodeRuleId());
//                //获取下面所有的规则
//                List<TCaSingleRuleVO> SRVOList = tCaRuleListVO.getTCaSingleRuleVOList();
//                //依次进行转换
//                SRVOList.stream().forEach(vo ->{
//                    //获取对应的表达式
//                    String realRuleExpression = vo.getRealRuleExpression();
//                    //把所有的决策变量和输出结果全部替换掉
//                    realRuleExpression = convertExpression(realRuleExpression,finalJson);
//                    if(realRuleExpression.contains("{{")){
//                        throw new RuleRuntimeException(String.format("规则ID为：%s，表达式为：%s的决策变量或者输出结果，没有上下文，请查实！",vo.getId(),realRuleExpression));
//                    }
//                    //再放回finalJson
//                    finalJson.put(vo.getOutPutName(),realRuleExpression);
//                });
//
//
//                List<TCaRuleTreeNode> sonNodes = connectorVOMap.get(currentNode.getNodeId());
//                if(sonNodes!=null&& sonNodes.size()>1){
//                    TCaRuleTreeNodeVO newCurrentNode = new TCaRuleTreeNodeVO();
//                    BeanUtils.copyProperties(sonNodes.get(0), newCurrentNode);
//                    currentNode = newCurrentNode;
//                }else {
//                    finalNode = currentNode;
//                    currentNode = null;
//                }
//
//
//
//            }else if(NodeTypeEnum.GUI_ZE.getName().equals(currentNode.getType())){ //如果节点是规则
//                //获取对应的规则详情
//                TCaSingleRuleVO tCaSingleRuleVO = singleRuleService.getById(currentNode.getNodeRuleId());
//                //获取对应的表达式
//                String realRuleExpression = tCaSingleRuleVO.getRealRuleExpression();
//                //把所有的决策变量和输出结果全部替换掉
//                realRuleExpression = convertExpression(realRuleExpression,finalJson);
//
//                if(realRuleExpression.contains("{{")){
//                    throw new RuleRuntimeException(String.format("规则ID为：%s，表达式为：%s的决策变量或者输出结果，没有上下文，请查实！",tCaSingleRuleVO.getId(),realRuleExpression));
//                }
//
//                //再放回finalJson
//                finalJson.put(tCaSingleRuleVO.getOutPutName(),realRuleExpression);
//
//                List<TCaRuleTreeNode> sonNodes = connectorVOMap.get(currentNode.getNodeId());
//                if(sonNodes!=null&& sonNodes.size()>1){
//                    TCaRuleTreeNodeVO newCurrentNode = new TCaRuleTreeNodeVO();
//                    BeanUtils.copyProperties(sonNodes.get(0), newCurrentNode);
//                    currentNode = newCurrentNode;
//                }else {
//                    finalNode = currentNode;
//                    currentNode = null;
//                }
//
//
//
//            }else if(NodeTypeEnum.TIAO_JIAN.getName().equals(currentNode.getType())){  //如果节点是判断条件
//
//                List<TCaRuleTreeNode> sonNodes = connectorVOMap.get(currentNode.getNodeId());
//
//                for(TCaRuleTreeNode node : sonNodes) {
//                    if (NodeTypeEnum.GUI_ZE.getName().equals(node.getType())
//                            || NodeTypeEnum.GUI_ZE_JI.getName().equals(node.getType())) {
//                        String sonExpression = node.getJudgementCondition();
//                        Boolean booleanObj = (Boolean)JSEngineCalculation.calculate(sonExpression, jsonObject);
//                        if (booleanObj){
//                            TCaRuleTreeNodeVO newCurrentNode = new TCaRuleTreeNodeVO();
//                            BeanUtils.copyProperties(node, newCurrentNode);
//                            currentNode = newCurrentNode;
//                            break;
//                        }else{
//                            continue;
//                        }
//                    }
//                }
//            }else{
//
//                List<TCaRuleTreeNode> sonNodes = connectorVOMap.get(currentNode.getNodeId());
//                if(sonNodes!=null&& sonNodes.size()>0){
//                    TCaRuleTreeNodeVO newCurrentNode = new TCaRuleTreeNodeVO();
//                    BeanUtils.copyProperties(sonNodes.get(0), newCurrentNode);
//                    currentNode = newCurrentNode;
//                }else {
//                    currentNode = null;
//                    throw new RuleRuntimeException("最后一个节点是判断节点，错误！");
//                }
//
//            }
//
//        }
//
//        //5、对最后一个节点计算
//        JSONObject result = new JSONObject();
//        if(NodeTypeEnum.GUI_ZE.getName().equals(finalNode.getType())){
//            result = singleRuleService.calculateById(finalNode.getNodeRuleId(), finalJson);
//        }else if(NodeTypeEnum.GUI_ZE_JI.getName().equals(finalNode.getType())){
//            result = ruleListService.calculateRuleListById(finalNode.getNodeRuleId(), finalJson);
//        }
//
//        return result;
//    }


//    /**
//     * 转换expression
//     * @param expression
//     * @param jsonObject
//     * @return
//     */
//    public static String convertExpression(String expression, JSONObject jsonObject){
//        for(String s : jsonObject.keySet()){
//            String value = String.valueOf(jsonObject.get(s));
//            if(value==""){
//                value = "\"\"";
//            }
//            expression = expression.replaceAll("\\{\\{"+s+"}}", "("+value+")");
//        }
//        return expression;
//    }





}
