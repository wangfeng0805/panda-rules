package org.wangfeng.panda.app.service.excel.importBiz;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.wangfeng.panda.app.dao.domain.TCaDecisionVariable;
import org.wangfeng.panda.app.service.DecisionVariableService;
import org.wangfeng.panda.app.service.excel.exportBiz.ExportBaseService;
import org.wangfeng.panda.app.util.StringUtils;

import java.util.List;


/**
 * 决策变量的导入类
 *
 */
@Service
@Slf4j
public class ImportDecisionVariableService extends ExportBaseService {

    @Autowired
    private DecisionVariableService decisionVariableService;

    /**
     * 导入决策变量（异步）
     * @param tCaDecisionVariableList
     */
    @Async
    public void importDecisionVariable(List<TCaDecisionVariable> tCaDecisionVariableList, String businessCode) {

        log.info("import all rule DecisionVariable，{}，{}", JSON.toJSONString(tCaDecisionVariableList),JSON.toJSONString(businessCode));

        //1、转换businessCode
        if(StringUtils.isNotBlank(businessCode)){
            tCaDecisionVariableList.parallelStream().forEach(dv -> {
                dv.setBusinessCode(businessCode);
            });
        }

        //2、批量插入
        StringBuffer info = new StringBuffer();
        decisionVariableService.batchInsertNotExist(tCaDecisionVariableList,info);

        //3、报错信息，打印到日志
        if(info!=null && info.length()>0){
            log.info("import all rule with info，{}，{}", JSON.toJSONString(tCaDecisionVariableList),JSON.toJSONString(info));
            info.insert(0,"有问题的决策变量的Code为：");
            info.delete(info.length()-1,info.length());
            log.info(info.toString());
        }else{
            log.info("import all rule no info，{}，{}", JSON.toJSONString(tCaDecisionVariableList),JSON.toJSONString(businessCode));
        }

    }


}
