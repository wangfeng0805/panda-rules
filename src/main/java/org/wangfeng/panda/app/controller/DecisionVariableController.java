/**
 *
 *
 *
 *
 *
 *
 */
package org.wangfeng.panda.app.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.wangfeng.panda.app.common.Paginate;
import org.wangfeng.panda.app.common.base.AppBaseController;
import org.wangfeng.panda.app.dao.domain.TCaDecisionVariable;
import org.wangfeng.panda.app.model.enums.VariableParamSourceEnum;
import org.wangfeng.panda.app.model.vo.TCaDecisionVariableVO;
import org.wangfeng.panda.app.service.DecisionVariableService;
import org.wangfeng.panda.app.validation.group.SelectOperation;
import org.wangfeng.panda.app.validation.group.UpdateOperation;

import java.util.List;


/**
 * <p>
 * Title: [子系统名称]_[模块名]
 * </p>
 * <p>
 * Description: 决策变量表controller
 * </p>
 *
 * @author: wangfeng
 * @version: v1.0
 * @author: (lastest modification by wangfeng)
 * @since: 2020-09-10 19:09:43
 */
@Slf4j
@RestController
@RequestMapping(value = "/m/decision/variable")
@Api(value = "panda-decision-variable", description = "决策变量API")
public class DecisionVariableController extends AppBaseController {


    @Autowired
    private DecisionVariableService decisionVariableService;


    /**
     * 列表页查询
     *
     * @param tCaDecisionVariableVO
     * @return
     */
    @RequestMapping(value = "/list", produces = {"application/json"}, method = RequestMethod.POST)
    @ApiOperation(value = "查询决策变量列表页", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "返回异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "返回异常", response = ResponseEntity.class)

    })
    public ResponseEntity getList(@ApiParam(value = "查询条件，是对象", required = true) @RequestBody @Validated(value = SelectOperation.class) TCaDecisionVariableVO tCaDecisionVariableVO,
                                  BindingResult bindingResult) {
        checkBindingResult(bindingResult);
        Paginate paginate = decisionVariableService.queryPagenate(tCaDecisionVariableVO, getPageIndex(), getPageSize());
        return new ResponseEntity(ok("获取成功",paginate),HttpStatus.OK);
    }


    /**
     * 通过ID单个查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/get", produces = {"application/json"}, method = RequestMethod.GET)
    @ApiOperation(value = "根据Id查找对应的决策变量", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "返回异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "返回异常", response = ResponseEntity.class)

    })
    public ResponseEntity getById(@ApiParam(value = "决策变量的主键ID", required = true) @RequestParam Long id) {
        TCaDecisionVariableVO tCaDecisionVariableVO = decisionVariableService.getById(id);
        if(tCaDecisionVariableVO==null){
            return new ResponseEntity(fail("没有查到对应数据！"),HttpStatus.EXPECTATION_FAILED);
        }
        return new ResponseEntity(ok("获取成功",tCaDecisionVariableVO), HttpStatus.OK);
    }


    /**
     * 新增决策变量
     *
     * @param tCaDecisionVariableList
     * @return
     */
    @RequestMapping(value = "/add", produces = {"application/json"}, method = RequestMethod.POST)
    @ApiOperation(value = "决策变量新增", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "返回异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "返回异常", response = ResponseEntity.class)

    })
    public ResponseEntity add(@ApiParam(value = "新增对象", required = true)
                              @RequestBody @Validated List<TCaDecisionVariable> tCaDecisionVariableList) {
        List<String> errorVariableCodeList = decisionVariableService.batchAddDecisionVariable(tCaDecisionVariableList);
        Integer errorCount = errorVariableCodeList != null && errorVariableCodeList.size() > 0 ? errorVariableCodeList.size() : 0;

        JSONObject result = new JSONObject();
        result.put("successCount",tCaDecisionVariableList.size() - errorCount);
        result.put("failCount",errorCount);

        return new ResponseEntity(ok("新增完成",result), HttpStatus.OK);
    }


    /**
     * 修改决策变量
     *
     * @param tCaDecisionVariable
     * @return
     */
    @RequestMapping(value = "/modify", produces = {"application/json"}, method = RequestMethod.POST)
    @ApiOperation(value = "查询决策变量编辑", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "返回异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "返回异常", response = ResponseEntity.class)

    })
    public ResponseEntity modify(@ApiParam(value = "编辑对象", required = true)
                                 @RequestBody @Validated(value = UpdateOperation.class) TCaDecisionVariable tCaDecisionVariable,
                                 BindingResult bindingResult) {
        checkBindingResult(bindingResult);
        decisionVariableService.updateByPrimaryKey(tCaDecisionVariable);
        return new ResponseEntity(ok(), HttpStatus.OK);
    }


    /**
     * 删除决策变量
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/delete", produces = {"application/json"})
    @ApiOperation(value = "决策变量删除", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "返回异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "返回异常", response = ResponseEntity.class)

    })
    public ResponseEntity delete(@ApiParam(value = "待删除的ID", required = true) @RequestParam Long id) {
        decisionVariableService.delete(id);
        return new ResponseEntity(ok("删除成功！"), HttpStatus.OK);
    }





    /**
     * 获取所有的决策变量数据来源
     *
     * @return
     */
    @GetMapping(value = "/queryAllEnums", produces = {"application/json"})
    @ApiOperation(value = "获取所有的决策变量数据来源", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "返回异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "返回异常", response = ResponseEntity.class)

    })
    public ResponseEntity queryAllParamSourceEnums() {
        List<JSONObject> list =  VariableParamSourceEnum.queryAllParamSourceEnums();
        return new ResponseEntity(ok("调用成功！",list), HttpStatus.OK);
    }

}

