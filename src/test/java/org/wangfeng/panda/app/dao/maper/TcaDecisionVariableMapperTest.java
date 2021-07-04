package org.wangfeng.panda.app.dao.maper;

import org.wangfeng.panda.app.dao.domain.TCaDecisionVariable;
import org.wangfeng.panda.app.model.vo.TCaDecisionVariableVO;
import org.wangfeng.panda.support.BaseDecisionVariableUT;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TcaDecisionVariableMapperTest extends BaseDecisionVariableUT {

    /**
     * dao层的查找list
     */
    @Test
    public void getList() {
        TCaDecisionVariableVO tCaDecisionVariable = new TCaDecisionVariableVO();
        tCaDecisionVariable.setBusinessId(0L);
        tCaDecisionVariable.setDecisionVariableName("string");

        List<TCaDecisionVariableVO> tCaDecisionVariableList = tCaDecisionVariableMapper.getList(tCaDecisionVariable);
        System.out.println("=====================================开始打印结果啦=====================================");
        tCaDecisionVariableList.stream().forEach(t -> System.out.println(t));
        System.out.println("=====================================结束打印结果啦=====================================");
    }

    /**
     * dao层的插入
     */
    @Test
    public void batchInsert() {
        List<TCaDecisionVariable> tCaDecisionVariableList = new ArrayList<>();
        for (Long i = 5L; i < 10L; i++) {
            TCaDecisionVariable tCaDecisionVariable = new TCaDecisionVariable();
            tCaDecisionVariable.setBusinessId(i);
            tCaDecisionVariable.setStatus((short) 0);
            initSaveWord(tCaDecisionVariable);
            tCaDecisionVariableList.add(tCaDecisionVariable);
        }
        Integer count = tCaDecisionVariableMapper.insertList(tCaDecisionVariableList);
        System.out.println(String.format("一共插入了%d条数据！", count));
    }


    /**
     * dao层的更新
     */
    @Test
    public void updateByPrimaryKey() {
        TCaDecisionVariable tCaDecisionVariable = new TCaDecisionVariable();
        tCaDecisionVariable.setId(17L);
        tCaDecisionVariable.setDeleteFlag((short) 6);
        tCaDecisionVariable.setDecisionVariableName("决策变量名称");
        initUpdateWord(tCaDecisionVariable);
        tCaDecisionVariableMapper.updateByPrimaryKey(tCaDecisionVariable);
    }

    /**
     * dao层的根据ID去获取详情
     */
    public void getById() {
        Long id = 3L;
        TCaDecisionVariable tCaDecisionVariable = tCaDecisionVariableMapper.getById(id);
        System.out.println(tCaDecisionVariable);
    }

}
