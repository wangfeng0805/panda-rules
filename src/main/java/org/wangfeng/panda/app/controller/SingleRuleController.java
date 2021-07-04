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
import org.wangfeng.panda.app.dao.domain.TCaSingleRule;
import org.wangfeng.panda.app.model.enums.OutPutTypeEnum;
import org.wangfeng.panda.app.model.vo.TCaSingleRuleVO;
import org.wangfeng.panda.app.service.SingleRuleService;
import org.wangfeng.panda.app.validation.group.AddOperation;
import org.wangfeng.panda.app.validation.group.SelectOperation;
import org.wangfeng.panda.app.validation.group.UpdateOperation;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * Title: [子系统名称]_[模块名]
 * </p>
 * <p>
 * Description: 规则controller
 * </p>
 *
 * @author: wangfeng
 * @version: v1.0
 * @author: (lastest modification by wangfeng)
 * @since: 2020-09-10 16:55:43
 */
@Slf4j
@RestController
@RequestMapping(value = "/m/single/rule")
@Api(value = "panda-single-rule", description = "规则API")
public class SingleRuleController extends AppBaseController {


    @Autowired
    private SingleRuleService singleRuleService;

    /**
     * 列表页查询
     *
     * @param tCaSingleRuleVO
     * @return
     */
    @RequestMapping(value = "/list", produces = {"application/json"}, method = RequestMethod.POST)
    @ApiOperation(value = "规则列表页查询", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 417, message = "业务异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "系统异常", response = ResponseEntity.class)

    })
    public ResponseEntity getList(@ApiParam(value = "查询条件，是对象", required = true)
                                  @RequestBody @Validated(value = SelectOperation.class) TCaSingleRuleVO tCaSingleRuleVO) {
        Paginate paginate = singleRuleService.queryPagenate(tCaSingleRuleVO,getPageIndex(),getPageSize());
        return new ResponseEntity(ok("获取成功",paginate),HttpStatus.OK);
    }


    /**
     * 通过ID单个查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/get", produces = {"application/json"}, method = RequestMethod.GET)
    @ApiOperation(value = "根据Id查找对应的规则", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 417, message = "业务异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "系统异常", response = ResponseEntity.class)

    })
    public ResponseEntity getById(@ApiParam(value = "规则的主键ID", required = true) @RequestParam Long id) {
        TCaSingleRuleVO tCaSingleRuleVO = singleRuleService.getById(id);
        if(tCaSingleRuleVO==null){
            return new ResponseEntity(fail("没有查到对应数据！"),HttpStatus.EXPECTATION_FAILED);
        }
        return new ResponseEntity(ok("获取成功",tCaSingleRuleVO), HttpStatus.OK);
    }


    /**
     * 新增规则
     *
     * @param tCaSingleRuleVO
     * @return
     */
    @RequestMapping(value = "/add", produces = {"application/json"}, method = RequestMethod.POST)
    @ApiOperation(value = "规则新增", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 417, message = "业务异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "系统异常", response = ResponseEntity.class)

    })
    public ResponseEntity add(@ApiParam(value = "新增对象", required = true)
                              @RequestBody @Validated(value = AddOperation.class) TCaSingleRuleVO tCaSingleRuleVO,
                              BindingResult bindingResult) {
        checkBindingResult(bindingResult);
        singleRuleService.insert(tCaSingleRuleVO);
        return new ResponseEntity(ok(), HttpStatus.OK);
    }

    /**
     * 规则编辑
     *
     * @param tCaSingleRuleVO
     * @return
     */
    @RequestMapping(value = "/modify", produces = {"application/json"}, method = RequestMethod.POST)
    @ApiOperation(value = "规则编辑", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 417, message = "业务异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "系统异常", response = ResponseEntity.class)

    })
    public ResponseEntity modify(@ApiParam(value = "编辑对象", required = true)
                                 @RequestBody @Validated(value = UpdateOperation.class) TCaSingleRuleVO tCaSingleRuleVO,
                                 BindingResult bindingResult) {
        checkBindingResult(bindingResult);
        singleRuleService.updateByPrimaryKey(tCaSingleRuleVO);
        return new ResponseEntity(ok(), HttpStatus.OK);
    }


    /**
     * 删除规则
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/delete", produces = {"application/json"})
    @ApiOperation(value = "规则删除", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 417, message = "业务异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "系统异常", response = ResponseEntity.class)

    })
    public ResponseEntity delete(@ApiParam(value = "待删除的ID", required = true) @RequestParam Long id) {
        singleRuleService.delete(id);
        return new ResponseEntity(ok("删除成功！"), HttpStatus.OK);
    }


    /**
     * 全量运算符的查询
     *
     * @return
     */
    @GetMapping(value = "/queryAllOutPutType", produces = {"application/json"})
    @ApiOperation(value = "全量输出类型查询", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 417, message = "业务异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "系统异常", response = ResponseEntity.class)

    })
    public ResponseEntity queryAllOutPutType() {
        List<JSONObject> jsonObjectList = OutPutTypeEnum.queryAllOutPutType();
        return new ResponseEntity(ok("操作成功",jsonObjectList), HttpStatus.OK);
    }


    /**
     * 测试表达式写的是否正确的接口
     *
     * @return
     */
    @PostMapping(value = "/testRuleExpression", produces = {"application/json"})
    @ApiOperation(value = "测试表达式写的是否正确的接口", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 417, message = "业务异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "系统异常", response = ResponseEntity.class)

    })
    public ResponseEntity testRuleExpression(
            @ApiParam(value = "参数", required = true) @RequestBody Map<String,Object> params,
            @ApiParam(value = "表达式对象", required = true) @RequestBody TCaSingleRule tCaSingleRule
    ) {
        Object obj = singleRuleService.testRuleExpression(tCaSingleRule,params);
        return new ResponseEntity(ok(obj.toString()), HttpStatus.OK);
    }

}
