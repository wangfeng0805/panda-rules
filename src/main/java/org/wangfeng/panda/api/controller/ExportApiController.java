package org.wangfeng.panda.api.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.wangfeng.panda.app.common.Constants;
import org.wangfeng.panda.app.common.base.AppBaseController;
import org.wangfeng.panda.app.dao.domain.*;
import org.wangfeng.panda.app.service.excel.ExcelUtil;
import org.wangfeng.panda.app.service.excel.ExportTypeEnum;
import org.wangfeng.panda.app.service.excel.exportBiz.*;
import org.wangfeng.panda.app.service.excel.importBiz.ImportDecisionVariableService;
import org.wangfeng.panda.app.service.excel.importBiz.ImportRuleListService;
import org.wangfeng.panda.app.service.excel.importBiz.ImportRuleTreeService;
import org.wangfeng.panda.app.service.excel.importBiz.ImportSingleRuleService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping(value = "/m/api/excel")
public class ExportApiController extends AppBaseController {

    @Autowired
    private ExportBaseService exportBaseService;
    @Autowired
    private ExportDecisionVariableService exportDecisionVariableService;
    @Autowired
    private ImportDecisionVariableService importDecisionVariableService;
    @Autowired
    private ExportSingleRuleService exportSingleRuleService;
    @Autowired
    private ImportSingleRuleService importSingleRuleService;
    @Autowired
    private ExportRuleListService exportRuleListService;
    @Autowired
    private ImportRuleListService importRuleListService;
    @Autowired
    private ExportRuleTreeService exportRuleTreeService;
    @Autowired
    private ImportRuleTreeService importRuleTreeService;






//    @GetMapping(value = "/exportAll")
//    @ResponseBody
//    public void export2(){
//        try {
//            exportBaseService.exportAll();
//        } catch (ClassNotFoundException e) {
//            log.error("类获取异常！");
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }


    /* ------------------------------------------- 决策变量的导入导出开始 -------------------------------------------  */


    /**
     * 导出决策变量
     * @param businessCode
     */
    @GetMapping(value = "/export/decisionVariable")
    @ResponseBody
    @ApiOperation(value = "导出决策变量", notes = "EXPORT")
    public void exportDecisionVariable(
            @ApiParam(value = "业务线编号", required = false) @RequestParam(required = false) String businessCode
    ){
        try {

            //1、创建EXCEL模板
            String fileName = "导出结果_决策变量_"+System.currentTimeMillis()+".xlsx";
            XSSFWorkbook wb = new XSSFWorkbook();

            //2、组装wb
            exportDecisionVariableService.exportDecisionVariable(businessCode,wb);

            //5、导出
            exportBaseOperation(fileName,wb);

        } catch (Exception e) {
            log.error("导出决策变量异常",e);
        }
    }


    /**
     * 导入决策变量
     * @param multipartFile
     * @return
     */
    @PostMapping(value = "/import/decisionVariable")
    @ResponseBody
    @ApiOperation(value = "导入决策变量", notes = "IMPORT")
    public ResponseEntity importDecisionVariable(MultipartFile multipartFile,String businessCode) {

        if(multipartFile == null){
            return new ResponseEntity(fail("传入文件是空！"), HttpStatus.EXPECTATION_FAILED);
        }

        try {
            //1、得到决策变量的
            List<TCaDecisionVariable> tCaDecisionVariableList = ExcelUtil.getDataFromExcel(multipartFile,TCaDecisionVariable.class,0);



            importDecisionVariableService.importDecisionVariable(tCaDecisionVariableList,businessCode);
            return new ResponseEntity(ok("正在努力导入中，请查看日志"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(fail("导入异常"+e.getMessage()), HttpStatus.EXPECTATION_FAILED);
        }
    }


    /* ------------------------------------------- 决策变量的导入导出结束 -------------------------------------------  */
    /* -------------------------------------------   规则的导入导出开始 --------------------------------------------  */

    /**
     * 导出规则
     * @param
     */
    @GetMapping(value = "/export/singleRule")
    @ResponseBody
    @ApiOperation(value = "导出规则", notes = "EXPORT")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "返回异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "返回异常", response = ResponseEntity.class)

    })
    public ResponseEntity exportSingleRule(
            @ApiParam(value = "规则CODE集合,按照逗号分隔", required = false) @RequestParam(required = false) String codeListStr
    ){

        //1、格式转化，把string转化为list
        List<String> codeList = new ArrayList<>();
        if (codeListStr!=null){
            try{
                String[] codeArr = codeListStr.split(Constants.DOUHAO);
                for (String code : codeArr){
                    codeList.add(code);
                }
            }catch (Exception e){
                return new ResponseEntity(fail("规则ID集合传入失败"),HttpStatus.EXPECTATION_FAILED);
            }

        }

        try {
            //1、创建EXCEL模板
            String fileName = "导出结果_规则_"+System.currentTimeMillis()+".xlsx";
            XSSFWorkbook wb = new XSSFWorkbook();

            //2、组装wb
            exportSingleRuleService.exportSingleRule(codeList,wb);

            //3、导出
            exportBaseOperation(fileName,wb);

            return new ResponseEntity(ok(),HttpStatus.OK);
        } catch (Exception e) {
            log.error("导出规则异常",e);
            return new ResponseEntity(fail("失败"),HttpStatus.EXPECTATION_FAILED);
        }
    }




    /**
     * 导入规则
     * @param multipartFile
     * @return
     */
    @PostMapping(value = "/import/singleRule")
    @ResponseBody
    @ApiOperation(value = "导入规则", notes = "IMPORT")
    public ResponseEntity importSingleRule(MultipartFile multipartFile,String businessCode) {

        if(multipartFile == null){
            return new ResponseEntity(fail("传入文件是空！"), HttpStatus.EXPECTATION_FAILED);
        }
        try {
            //1、获取规则，行，格子
            List<TCaSingleRule> ruleList = ExcelUtil.getDataFromExcel(multipartFile, TCaSingleRule.class,0);
            List<TCaRuleLine> lineList = ExcelUtil.getDataFromExcel(multipartFile, TCaRuleLine.class,1);
            List<TCaCellVariable> cellList = ExcelUtil.getDataFromExcel(multipartFile, TCaCellVariable.class,2);
            Map<String,List> importMap = new HashMap<String,List>(){{
                put(ExportTypeEnum.SINGLE_RULE_EXPORT.getClassName(),ruleList);
                put(ExportTypeEnum.RULE_LINE_EXPORT.getClassName(),lineList);
                put(ExportTypeEnum.CELL_VARIABLE_EXPORT.getClassName(),cellList);
            }};
            importSingleRuleService.importSingleRule(importMap,businessCode,new ArrayList<>());
            return new ResponseEntity(ok("正在努力导入中，请查看日志"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(fail("导入异常"+e.getMessage()), HttpStatus.EXPECTATION_FAILED);
        }
    }




    /* -------------------------------------------   规则的导入导出结束 --------------------------------------------  */
    /* -------------------------------------------  规则集的导入导出开始 --------------------------------------------  */


    /**
     * 导出规则集
     * @param
     */
    @GetMapping(value = "/export/ruleList")
    @ResponseBody
    @ApiOperation(value = "导出规则集合", notes = "EXPORT")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "返回异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "返回异常", response = ResponseEntity.class)

    })
    public ResponseEntity exportRuleList(
            @ApiParam(value = "规则集CODE集合,按照逗号分隔", required = false) @RequestParam(required = false) String codeListStr
    ){

        //1、格式转化，把string转化为list
        List<String> codeList = new ArrayList<>();
        if (codeListStr!=null){
            try{
                String[] codeArr = codeListStr.split(Constants.DOUHAO);
                for (String code : codeArr){
                    codeList.add(code);
                }
            }catch (Exception e){
                return new ResponseEntity(fail("规则集CODE集合传入失败"),HttpStatus.EXPECTATION_FAILED);
            }

        }

        try {
            //1、创建EXCEL模板
            String fileName = "导出结果_规则集_"+System.currentTimeMillis()+".xlsx";
            XSSFWorkbook wb = new XSSFWorkbook();

            //2、组装wb
            Map<String,List> ruleListMap = exportRuleListService.exportRuleList(codeList,wb);
            exportSingleRuleService.exportSingleRule(ruleListMap.get("ruleCodeList"),wb);

            //3、导出
            exportBaseOperation(fileName,wb);

            return new ResponseEntity(ok(),HttpStatus.OK);
        } catch (Exception e) {
            log.error("导出规则异常",e);
            return new ResponseEntity(fail("失败"),HttpStatus.EXPECTATION_FAILED);
        }
    }




    /**
     * 导入规则集
     * @param multipartFile
     * @return
     */
    @PostMapping(value = "/import/ruleList")
    @ResponseBody
    @ApiOperation(value = "导入规则集", notes = "IMPORT")
    public ResponseEntity importRuleList(MultipartFile multipartFile,String businessCode) {

        if(multipartFile == null){
            return new ResponseEntity(fail("传入文件是空！"), HttpStatus.EXPECTATION_FAILED);
        }
        try {
            //1、获取规则，行，格子
            List<TCaRuleList> ruleListList = ExcelUtil.getDataFromExcel(multipartFile, TCaRuleList.class,0);
            List<TCaRuleListMapping> ruleListMappingList = ExcelUtil.getDataFromExcel(multipartFile, TCaRuleListMapping.class,1);
            List<TCaSingleRule> ruleList = ExcelUtil.getDataFromExcel(multipartFile, TCaSingleRule.class,2);
            List<TCaRuleLine> lineList = ExcelUtil.getDataFromExcel(multipartFile, TCaRuleLine.class,3);
            List<TCaCellVariable> cellList = ExcelUtil.getDataFromExcel(multipartFile, TCaCellVariable.class,4);

            Map<String,List> importMap = new HashMap<String,List>(){{
                put(ExportTypeEnum.RULE_LIST_EXPORT.getClassName(),ruleListList);
                put(ExportTypeEnum.RULE_LIST_MAPPING_EXPORT.getClassName(),ruleListMappingList);
                put(ExportTypeEnum.SINGLE_RULE_EXPORT.getClassName(),ruleList);
                put(ExportTypeEnum.RULE_LINE_EXPORT.getClassName(),lineList);
                put(ExportTypeEnum.CELL_VARIABLE_EXPORT.getClassName(),cellList);
            }};

            importRuleListService.importRuleList(importMap,businessCode,new ArrayList<>());

            return new ResponseEntity(ok("正在努力导入中，请查看日志"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(fail("导入异常"+e.getMessage()), HttpStatus.EXPECTATION_FAILED);
        }
    }



    /* -------------------------------------------  规则集的导入导出结束 --------------------------------------------  */
    /* -------------------------------------------  决策树的导入导出开始 --------------------------------------------  */

    /**
     * 导出决策树
     * @param
     */
    @GetMapping(value = "/export/ruleTree")
    @ResponseBody
    @ApiOperation(value = "导出决策树", notes = "EXPORT")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "返回异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "返回异常", response = ResponseEntity.class)

    })
    public ResponseEntity exportRuleTree(
            @ApiParam(value = "决策树CODE集合,按照逗号分隔", required = false) @RequestParam(required = false) String codeListStr
    ){

        //1、格式转化，把string转化为list
        List<String> codeList = new ArrayList<>();
        if (codeListStr!=null){
            try{
                String[] codeArr = codeListStr.split(Constants.DOUHAO);
                for (String code : codeArr){
                    codeList.add(code);
                }
            }catch (Exception e){
                return new ResponseEntity(fail("决策树CODE集合传入失败"),HttpStatus.EXPECTATION_FAILED);
            }

        }

        try {
            //1、创建EXCEL模板
            String fileName = "导出结果_决策树_"+System.currentTimeMillis()+".xlsx";
            XSSFWorkbook wb = new XSSFWorkbook();

            //2、组装wb
            Map<String,List> listAndRuleMap = exportRuleTreeService.exportRuleTree(codeList,wb);
            List<String> ruleCodeList1 = listAndRuleMap.get("ruleCodeList");
            List<String> listCodeList = listAndRuleMap.get("ruleListCodeList");

            Map<String,List> ruleListMap = exportRuleListService.exportRuleList(listCodeList,wb);
            List<String> ruleCodeList2 = ruleListMap.get("ruleCodeList");
            ruleCodeList2.addAll(ruleCodeList1);

            exportSingleRuleService.exportSingleRule(ruleCodeList2,wb);

            //3、导出
            exportBaseOperation(fileName,wb);

            return new ResponseEntity(ok(),HttpStatus.OK);
        } catch (Exception e) {
            log.error("导出规则异常",e);
            return new ResponseEntity(fail("失败"),HttpStatus.EXPECTATION_FAILED);
        }
    }



    /**
     * 导入决策树
     * @param multipartFile
     * @return
     */
    @PostMapping(value = "/import/ruleTree")
    @ResponseBody
    @ApiOperation(value = "导入决策树", notes = "IMPORT")
    public ResponseEntity importRuleTree(MultipartFile multipartFile,String businessCode) {

        if(multipartFile == null){
            return new ResponseEntity(fail("传入文件是空！"), HttpStatus.EXPECTATION_FAILED);
        }
        try {
            //1、获取所有的参数
            List<TCaRuleTree> treeList = ExcelUtil.getDataFromExcel(multipartFile, TCaRuleTree.class,0);
            List<TCaRuleTreeNode> treeNodeList = ExcelUtil.getDataFromExcel(multipartFile, TCaRuleTreeNode.class,1);
            List<TCaRuleTreeMapping> treeMappingList = ExcelUtil.getDataFromExcel(multipartFile, TCaRuleTreeMapping.class,2);
            List<TCaRuleList> ruleListList = ExcelUtil.getDataFromExcel(multipartFile, TCaRuleList.class,3);
            List<TCaRuleListMapping> ruleListMappingList = ExcelUtil.getDataFromExcel(multipartFile, TCaRuleListMapping.class,4);
            List<TCaSingleRule> ruleList = ExcelUtil.getDataFromExcel(multipartFile, TCaSingleRule.class,5);
            List<TCaRuleLine> lineList = ExcelUtil.getDataFromExcel(multipartFile, TCaRuleLine.class,6);
            List<TCaCellVariable> cellList = ExcelUtil.getDataFromExcel(multipartFile, TCaCellVariable.class,7);

            Map<String,List> importMap = new HashMap<String,List>(){{
                put(ExportTypeEnum.RULE_TREE_EXPORT.getClassName(),treeList);
                put(ExportTypeEnum.RULE_TREE_NODE_EXPORT.getClassName(),treeNodeList);
                put(ExportTypeEnum.RULE_TREE_MAPPING_EXPORT.getClassName(),treeMappingList);
                put(ExportTypeEnum.RULE_LIST_EXPORT.getClassName(),ruleListList);
                put(ExportTypeEnum.RULE_LIST_MAPPING_EXPORT.getClassName(),ruleListMappingList);
                put(ExportTypeEnum.SINGLE_RULE_EXPORT.getClassName(),ruleList);
                put(ExportTypeEnum.RULE_LINE_EXPORT.getClassName(),lineList);
                put(ExportTypeEnum.CELL_VARIABLE_EXPORT.getClassName(),cellList);
            }};

            importRuleTreeService.importRuleTree(importMap,businessCode,new ArrayList<>());

            return new ResponseEntity(ok("正在努力导入中，请查看日志"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(fail("导入异常"+e.getMessage()), HttpStatus.EXPECTATION_FAILED);
        }
    }





    /* -------------------------------------------  决策树的导入导出结束 --------------------------------------------  */



}
