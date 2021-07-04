package org.wangfeng.panda.app.controller;

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
import org.wangfeng.panda.app.model.vo.TCaRuleListVO;
import org.wangfeng.panda.app.service.RuleListService;
import org.wangfeng.panda.app.validation.group.AddOperation;
import org.wangfeng.panda.app.validation.group.UpdateOperation;

@Slf4j
@RestController
@RequestMapping(value = "/m/rule/list")
@Api(value = "panda-rule-list", description = "规则集API")
public class RuleListController extends AppBaseController {


    @Autowired
    private RuleListService ruleListService;


    /**
     * 列表页查询
     *
     * @param tCaRuleListVO
     * @return
     */
    @RequestMapping(value = "/list", produces = {"application/json"}, method = RequestMethod.POST)
    @ApiOperation(value = "规则集列表页", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "返回异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "返回异常", response = ResponseEntity.class)

    })
    public ResponseEntity getList(@ApiParam(value = "查询条件，是对象", required = true) @RequestBody @Validated TCaRuleListVO tCaRuleListVO) {
        Paginate paginate = ruleListService.queryPagenate(tCaRuleListVO,getPageIndex(),getPageSize());
        return new ResponseEntity(ok("获取成功",paginate), HttpStatus.OK);
    }



    /**
     * 通过ID单个查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/get", produces = {"application/json"}, method = RequestMethod.GET)
    @ApiOperation(value = "根据Id查找对应的规则集", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "返回异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "返回异常", response = ResponseEntity.class)

    })
    public ResponseEntity getById(@ApiParam(value = "规则集的主键ID", required = true) @RequestParam Long id) {
        TCaRuleListVO tCaRuleListVO = ruleListService.getById(id);
        if(tCaRuleListVO==null){
            return new ResponseEntity(fail("没有查到对应数据！"),HttpStatus.EXPECTATION_FAILED);
        }
        return new ResponseEntity(ok("获取成功",tCaRuleListVO), HttpStatus.OK);
    }




    /**
     * 新增
     *
     * @param tCaRuleListVO
     * @return
     */
    @RequestMapping(value = "/add", produces = {"application/json"}, method = RequestMethod.POST)
    @ApiOperation(value = "规则集新增", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "返回异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "返回异常", response = ResponseEntity.class)

    })
    public ResponseEntity add(@ApiParam(value = "新增的规则集对象", required = true)
                              @RequestBody @Validated(value = AddOperation.class) TCaRuleListVO tCaRuleListVO,
                              BindingResult bindingResult) {
        checkBindingResult(bindingResult);
        ruleListService.insert(tCaRuleListVO);
        return new ResponseEntity(ok(), HttpStatus.OK);
    }


    /**
     * 编辑
     *
     * @param tCaRuleListVO
     * @return
     */
    @RequestMapping(value = "/modify", produces = {"application/json"}, method = RequestMethod.POST)
    @ApiOperation(value = "编辑规则集", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "返回异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "返回异常", response = ResponseEntity.class)

    })
    public ResponseEntity modify(@ApiParam(value = "编辑的规则集对象", required = true)
                                 @RequestBody @Validated(value = UpdateOperation.class) TCaRuleListVO tCaRuleListVO,
                                 BindingResult bindingResult) {
        checkBindingResult(bindingResult);
        ruleListService.updateByPrimaryKey(tCaRuleListVO);
        return new ResponseEntity(ok(), HttpStatus.OK);
    }


    /**
     * 删除规则集
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/delete", produces = {"application/json"})
    @ApiOperation(value = "删除规则集", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "返回异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "返回异常", response = ResponseEntity.class)

    })
    public ResponseEntity delete(@ApiParam(value = "待删除的ID", required = true) @RequestParam Long id) {
        ruleListService.delete(id);
        return new ResponseEntity(ok("删除成功"), HttpStatus.OK);
    }
}
