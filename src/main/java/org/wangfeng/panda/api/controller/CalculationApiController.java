package org.wangfeng.panda.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.wangfeng.panda.api.enums.ResponseStatusEnum;
import org.wangfeng.panda.app.common.base.AppBaseController;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.service.RuleListService;
import org.wangfeng.panda.app.service.RuleTreeService;
import org.wangfeng.panda.app.service.SingleRuleService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/m/api")
@Api(value = "panda-calculation-api", description = "计算API", tags = "api")
public class CalculationApiController extends AppBaseController {


    @Autowired
    private SingleRuleService singleRuleService;
    @Autowired
    private RuleListService ruleListService;
    @Autowired
    private RuleTreeService ruleTreeService;

    /**
     * 单个规则的运算逻辑
     * 传入对应的规则的ID和所需json格式的参数
     *
     * @param id
     * @param jsonObject
     * @return
     */
    @PostMapping(value = "/calculateByRuleId", produces = {"application/json"})
    @ApiOperation(value = "单个规则的运算", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 417, message = "业务异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "系统异常", response = ResponseEntity.class)

    })
    public ResponseEntity calculateByRuleId(
            @ApiParam(value = "规则ID", required = true) @RequestParam Long id,
            @ApiParam(value = "参数", required = true) @RequestBody JSONObject jsonObject
    ) {
        //1、判断，当传入参数不满足要求的时候，返回错误
        if (id == null || jsonObject == null) {
            return new ResponseEntity(fail(ResponseStatusEnum.CALCULATE_ARGS_ERROR.getMessage(), ResponseStatusEnum.CALCULATE_ARGS_ERROR.getCode(), null), HttpStatus.OK);
        }

        //2、计算结果，如果有异常，则返回错误
        try {
            log.info("单个规则的运算，传入的参数是：{}，{}",id,JSON.toJSONString(jsonObject));
            JSONObject result = singleRuleService.calculateByIdORCode(id, null,jsonObject);
            log.info("单个规则的运算，计算的结果是：{}", JSON.toJSONString(result));
            return new ResponseEntity(ok(ResponseStatusEnum.CALCULATE_SUCCESS.getMessage(), ResponseStatusEnum.CALCULATE_SUCCESS.getCode(), result), HttpStatus.OK);
        } catch (RuleRuntimeException ruleException) {
            log.error("单个规则的运算，计算异常：{}，{}",id,JSON.toJSONString(jsonObject),ruleException);
            return new ResponseEntity(fail(ruleException.getMessage(), ResponseStatusEnum.CALCULATE_RESULT_ERROR.getCode(), null), HttpStatus.OK);
        } catch (Exception exception) {
            log.error("单个规则的运算，系统异常：{}，{}",id,JSON.toJSONString(jsonObject),exception);
            return new ResponseEntity(fail(ResponseStatusEnum.CALCULATE_RESULT_ERROR.getMessage(), ResponseStatusEnum.CALCULATE_RESULT_ERROR.getCode(), null), HttpStatus.OK);
        }

    }


    /**
     * 多个规则的运算逻辑
     * 传入对应的规则的多个ID的字符串，以逗号分隔，以及所需json格式的参数
     *
     * @param idList
     * @param jsonObject
     * @return
     */
    @PostMapping(value = "/calculateByRuleIdList", produces = {"application/json"})
    @ApiOperation(value = "多个规则并行运算", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 417, message = "业务异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "系统异常", response = ResponseEntity.class)

    })
    public ResponseEntity calculateByRuleIdList(
            @ApiParam(value = "规则ID", required = true) @RequestParam String idList,
            @ApiParam(value = "参数", required = true) @RequestBody JSONObject jsonObject
    ) {

        //1、判断，当传入参数不满足要求的时候，返回错误
        if (idList == null || jsonObject == null) {
            return new ResponseEntity(fail(ResponseStatusEnum.CALCULATE_ARGS_ERROR.getMessage(), ResponseStatusEnum.CALCULATE_ARGS_ERROR.getCode(), null), HttpStatus.OK);
        }

        try {
            log.info("多个规则并行运算，传入的参数是：{}，{}",idList,JSON.toJSONString(jsonObject));

            //2、拆分对应的idList获取所有的规则的ID
            String[] arr = idList.split(",");
            List<Long> list = new ArrayList<>();
            for (String s : arr) {
                list.add(Long.parseLong(s));
            }
            //3、计算结果，如果有异常，则返回错误
            JSONObject result = singleRuleService.calculateByIdList(list, jsonObject);

            log.info("多个规则并行运算，计算的结果是：{}，{}，{}",idList,JSON.toJSONString(jsonObject),JSON.toJSONString(result));

            return new ResponseEntity(ok(ResponseStatusEnum.CALCULATE_SUCCESS.getMessage(), ResponseStatusEnum.CALCULATE_SUCCESS.getCode(), result), HttpStatus.OK);
        } catch (RuleRuntimeException ruleException) {
            log.info("多个规则并行运算，计算异常：{}，{}",idList,JSON.toJSONString(jsonObject),ruleException);
            return new ResponseEntity(fail(ruleException.getMessage(), ResponseStatusEnum.CALCULATE_RESULT_ERROR.getCode(), null), HttpStatus.OK);
        } catch (Exception exception) {
            log.info("多个规则并行运算，系统异常：{}，{}",idList,JSON.toJSONString(jsonObject),exception);
            return new ResponseEntity(fail(ResponseStatusEnum.CALCULATE_RESULT_ERROR.getMessage(), ResponseStatusEnum.CALCULATE_RESULT_ERROR.getCode(), null), HttpStatus.OK);
        }
    }


    /**
     * 单个规则集的运算逻辑
     * 传入对应的规则集的ID和所需json格式的参数
     *
     * @param id
     * @param jsonObject
     * @return
     */
    @PostMapping(value = "/calculateByRuleListId", produces = {"application/json"})
    @ApiOperation(value = "单个规则集的运算", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 417, message = "业务异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "系统异常", response = ResponseEntity.class)

    })
    public ResponseEntity calculateByRuleListId(
            @ApiParam(value = "规则ID", required = true) @RequestParam Long id,
            @ApiParam(value = "参数", required = true) @RequestBody JSONObject jsonObject
    ) {

        //1、判断，当传入参数不满足要求的时候，返回错误
        if (id == null || jsonObject == null) {
            return new ResponseEntity(fail(ResponseStatusEnum.CALCULATE_ARGS_ERROR.getMessage(), ResponseStatusEnum.CALCULATE_ARGS_ERROR.getCode(), null), HttpStatus.OK);
        }

        //2、计算结果，如果有异常，则返回错误
        try {
            log.info("单个规则集的运算，传入的参数是：{}，{}",id,JSON.toJSONString(jsonObject));
            JSONObject result = ruleListService.calculateRuleListByIdORCode(id,null, jsonObject);
            log.info("单个规则集的运算，计算的结果是：{}，{}",id,JSON.toJSONString(jsonObject));
            return new ResponseEntity(ok(ResponseStatusEnum.CALCULATE_SUCCESS.getMessage(), ResponseStatusEnum.CALCULATE_SUCCESS.getCode(), result), HttpStatus.OK);
        } catch (RuleRuntimeException ruleException) {
            log.info("单个规则集的运算，计算异常：{}，{}",id,JSON.toJSONString(jsonObject),ruleException);
            return new ResponseEntity(fail(ruleException.getMessage(), ResponseStatusEnum.CALCULATE_RESULT_ERROR.getCode(), null), HttpStatus.OK);
        } catch (Exception exception) {
            log.info("单个规则集的运算，系统异常：{}，{}",id,JSON.toJSONString(jsonObject),exception);
            return new ResponseEntity(fail(ResponseStatusEnum.CALCULATE_RESULT_ERROR.getMessage(), ResponseStatusEnum.CALCULATE_RESULT_ERROR.getCode(), null), HttpStatus.OK);
        }
    }


    /**
     * 单个规则树的运算逻辑
     * 传入对应的规则树的ID和所需json格式的参数
     *
     * @param id
     * @param jsonObject
     * @return
     */
    @PostMapping(value = "/calculateByRuleTreeId", produces = {"application/json"})
    @ApiOperation(value = "单个规则树的运算", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 417, message = "业务异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "系统异常", response = ResponseEntity.class)})
    public ResponseEntity calculateByRuleTreeId(
            @ApiParam(value = "规则ID", required = true) @RequestParam Long id,
            @ApiParam(value = "参数", required = true) @RequestBody JSONObject jsonObject) {

        //1、判断，当传入参数不满足要求的时候，返回错误
        if (id == null || jsonObject == null) {
            return new ResponseEntity(fail(ResponseStatusEnum.CALCULATE_ARGS_ERROR.getMessage(), ResponseStatusEnum.CALCULATE_ARGS_ERROR.getCode(), null), HttpStatus.OK);
        }

        //2、计算结果，如果有异常，则返回错误
        try {
            log.info("单个规则树的运算，传入的参数是：{}，{}",id,JSON.toJSONString(jsonObject));
            JSONObject result = ruleTreeService.calculateRuleTreeById(id, jsonObject);
            log.info("单个规则树的运算，计算的结果是：{}，{}",id,JSON.toJSONString(jsonObject));
            return new ResponseEntity(ok(ResponseStatusEnum.CALCULATE_SUCCESS.getMessage(), ResponseStatusEnum.CALCULATE_SUCCESS.getCode(), result), HttpStatus.OK);
        } catch (RuleRuntimeException ruleException) {
            log.info("单个规则树的运算，计算异常：{}，{}",id,JSON.toJSONString(jsonObject),ruleException);
            return new ResponseEntity(fail(ruleException.getMessage(), ResponseStatusEnum.CALCULATE_RESULT_ERROR.getCode(), null), HttpStatus.OK);
        } catch (Exception exception) {
            log.info("单个规则树的运算，系统异常：{}，{}",id,JSON.toJSONString(jsonObject),exception);
            return new ResponseEntity(fail(ResponseStatusEnum.CALCULATE_RESULT_ERROR.getMessage(), ResponseStatusEnum.CALCULATE_RESULT_ERROR.getCode(), null), HttpStatus.OK);
        }
    }
}
