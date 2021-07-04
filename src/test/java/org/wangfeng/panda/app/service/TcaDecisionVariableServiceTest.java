package org.wangfeng.panda.app.service;

import org.wangfeng.panda.app.common.Paginate;
import org.wangfeng.panda.app.dao.domain.TCaDecisionVariable;
import org.wangfeng.panda.app.model.vo.TCaDecisionVariableVO;
import org.wangfeng.panda.support.BaseDecisionVariableUT;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TcaDecisionVariableServiceTest extends BaseDecisionVariableUT {

    @Test
    public void queryPagenate() {
        TCaDecisionVariableVO tCaDecisionVariable = new TCaDecisionVariableVO();
//        tCaDecisionVariable.setId(1L);
        Integer pageNo = 1;
        Integer pageSize = 10;

        Paginate paginate = decisionVariableService.queryPagenate(tCaDecisionVariable, pageNo, pageSize);
        System.out.println("=====================================开始打印结果啦=====================================");
        System.out.println(paginate);
        System.out.println("=====================================结束打印结果啦=====================================");
    }


    @Test
    public void batchAdd() {
        List<TCaDecisionVariable> tCaDecisionVariableList = new ArrayList<>();
        for (Short s = 11; s < 15; s++) {
            TCaDecisionVariable tCaDecisionVariable = new TCaDecisionVariable();
            tCaDecisionVariable.setParamSource(s);
            tCaDecisionVariable.setEnumDataRange("数据范围！");
            initSaveWord(tCaDecisionVariable);
            tCaDecisionVariableList.add(tCaDecisionVariable);
        }

        List<String> errorCodeList = decisionVariableService.batchAddDecisionVariable(tCaDecisionVariableList);
        System.out.println(String.format("一共插入了%d条数据！其中错误了%d条！", tCaDecisionVariableList.size(), (errorCodeList != null && errorCodeList.size() > 0 ? errorCodeList.size() : 0)));
    }


    @Test
    public void updateByPrimaryKey() {
        TCaDecisionVariable tCaDecisionVariable = new TCaDecisionVariable();
        tCaDecisionVariable.setId(20L);
        tCaDecisionVariable.setDeleteFlag((short) 2);
        tCaDecisionVariable.setDecisionVariableName("决策变量名称(新改的)");
        Integer count = decisionVariableService.updateByPrimaryKey(tCaDecisionVariable);
        System.out.println(String.format("一共更新了%d条数据！", count));
    }

    @Test
    public void delete() {
        Integer count = decisionVariableService.delete(7L);
        System.out.println(String.format("一共删除了%d条数据！", count));
    }


}
