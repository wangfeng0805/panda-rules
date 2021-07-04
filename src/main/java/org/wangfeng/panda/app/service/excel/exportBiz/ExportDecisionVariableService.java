package org.wangfeng.panda.app.service.excel.exportBiz;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.wangfeng.panda.app.cache.SpringUtil;
import org.wangfeng.panda.app.dao.MyMapper;
import org.wangfeng.panda.app.dao.domain.TCaDecisionVariable;
import org.wangfeng.panda.app.service.excel.ExcelUtil;
import org.wangfeng.panda.app.service.excel.ExportTypeEnum;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 决策变量的导出类
 */
@Service
@Slf4j
public class ExportDecisionVariableService extends ExportBaseService{


    /**
     *  导出决策变量
     * @param businessCode
     * @throws Exception
     */
    public void exportDecisionVariable(String businessCode,XSSFWorkbook wb) throws Exception {

        //1、查询所有的deleteflag为0的数据
        String mapperFullPath = ExportTypeEnum.mapperPrePath+ExportTypeEnum.DECISION_VARIABLE_EXPORT.getMapperName();

        Class mapperClz = Class.forName(mapperFullPath);
        MyMapper myMapper = (MyMapper) SpringUtil.getBean(mapperClz);

        Method m = mapperClz.getDeclaredMethod(ExportTypeEnum.queryAllMethod, TCaDecisionVariable.class);

        TCaDecisionVariable tCaDecisionVariable = new TCaDecisionVariable();
        tCaDecisionVariable.setBusinessCode(businessCode);

        List exportList = (List) m.invoke(myMapper, tCaDecisionVariable);

        //2、得到sheet页面的header
        String sheetName = ExportTypeEnum.DECISION_VARIABLE_EXPORT.getCNName();
        List<String> headers = ExcelUtil.getHeaders(TCaDecisionVariable.class);

        //3、生成excel
        wb = ExcelUtil.getXSSFWorkbook(sheetName, headers, exportList, wb,TCaDecisionVariable.class);

    }


}
