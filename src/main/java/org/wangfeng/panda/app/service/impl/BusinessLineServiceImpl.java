package org.wangfeng.panda.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wangfeng.panda.app.common.Constants;
import org.wangfeng.panda.app.common.Paginate;
import org.wangfeng.panda.app.common.base.AppBaseService;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.dao.domain.TCaBusinessLine;
import org.wangfeng.panda.app.dao.mapper.TCaBusinessLineMapper;
import org.wangfeng.panda.app.model.vo.TCaBusinessLineVO;
import org.wangfeng.panda.app.service.BusinessLineService;

import java.util.List;

@Service
@Slf4j
public class BusinessLineServiceImpl extends AppBaseService implements BusinessLineService {

    @Autowired
    private TCaBusinessLineMapper tCaBusinessLineMapper;

    /**
     * 查询列表页
     * @param tCaBusinessLineVO
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Paginate queryPagenate(TCaBusinessLineVO tCaBusinessLineVO, Integer pageNo, Integer pageSize) {
        log.info("查询列表页，开始：{}，{}，{}", JSON.toJSONString(tCaBusinessLineVO),JSON.toJSONString(pageNo),JSON.toJSONString(pageSize));
        //1、增加分页参数
        this.setPage(pageNo, pageSize);
        //2、去除%等特殊字符的影响
        distinguishString(tCaBusinessLineVO);
        //3、查询出所有符合要求的数据
        List<TCaBusinessLine> tCaBusinessLineList = tCaBusinessLineMapper.getList(tCaBusinessLineVO.invokeToVo());
        //4、拼装成需要的格式
        List<TCaBusinessLineVO> tCaBusinessLineVOList = new Page<>();
        if(tCaBusinessLineList!=null){
            BeanUtils.copyProperties(tCaBusinessLineList,tCaBusinessLineVOList);
            tCaBusinessLineList.stream().forEach(business ->{
                tCaBusinessLineVOList.add(business.invokeToVo());
            });
        }
        //5、组装成paginate对象
        Paginate paginate = createPaginate(pageNo, pageSize, tCaBusinessLineVOList);
        return paginate;
    }

    /**
     * 通过ID查询对应的业务线
     * @param id
     * @return
     */
    @Override
    public TCaBusinessLine getById(Long id) {
        log.info("通过ID查询对应的业务线，开始：{}", id);
        TCaBusinessLine tCaBusinessLine = tCaBusinessLineMapper.getById(id);
        log.info("通过ID查询对应的业务线，返回：{}", JSON.toJSONString(tCaBusinessLine));
        return tCaBusinessLine;
    }

    /**
     * 新增业务线
     * @param tCaBusinessLine
     */
    @Override
    public void insert(TCaBusinessLine tCaBusinessLine) {
        log.info("新增业务线，开始：{}", JSON.toJSONString(tCaBusinessLine));

        TCaBusinessLine queryBusinessCode = new TCaBusinessLine();
        queryBusinessCode.setBusinessCode(tCaBusinessLine.getBusinessCode());
        List<TCaBusinessLine> queryBusinessCodeList = tCaBusinessLineMapper.getList(queryBusinessCode.invokeToVo());
        if(queryBusinessCodeList!=null&&queryBusinessCodeList.size()>0){
            throw new RuleRuntimeException("业务线代码不能重复！");
        }


        TCaBusinessLine queryBusinessName = new TCaBusinessLine();
        queryBusinessName.setBusinessCode(tCaBusinessLine.getBusinessName());
        List<TCaBusinessLine> queryBusinessNameList = tCaBusinessLineMapper.getList(queryBusinessName.invokeToVo());
        if(queryBusinessNameList!=null&&queryBusinessNameList.size()>0){
            throw new RuleRuntimeException("业务线名称不能重复！");
        }

        initSaveWord(tCaBusinessLine);
        Integer count = tCaBusinessLineMapper.insert(tCaBusinessLine);
        if(count==0){
            throw new RuleRuntimeException("新增失败，请稍后再试！");
        }
    }

    /**
     * 更新业务线
     * @param tCaBusinessLine
     */
    @Override
    public void updateByPrimaryKey(TCaBusinessLine tCaBusinessLine) {
        initUpdateWord(tCaBusinessLine);
        Integer count = tCaBusinessLineMapper.updateByPrimaryKey(tCaBusinessLine);
        if(count==0){
            throw new RuleRuntimeException("更新失败，没有对应的数据！");
        }
    }

    /**
     * 通过ID逻辑删除对应的业务线
     * @param id
     */
    @Override
    public void delete(Long id) {
        log.info("通过ID逻辑删除对应的业务线，开始：{}", id);

        TCaBusinessLine tCaBusinessLine = new TCaBusinessLine();
        tCaBusinessLine.setId(id);
        tCaBusinessLine.setDeleteFlag(Constants.SHORT_ONE);
        initUpdateWord(tCaBusinessLine);
        Integer count = tCaBusinessLineMapper.updateByPrimaryKey(tCaBusinessLine);
        if(count==0){
            throw new RuleRuntimeException("删除失败，没有对应的数据！");
        }
    }
}
