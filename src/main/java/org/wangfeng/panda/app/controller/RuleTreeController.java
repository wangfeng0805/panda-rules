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
import org.wangfeng.panda.app.model.vo.TCaRuleTreeVO;
import org.wangfeng.panda.app.service.RuleTreeService;
import org.wangfeng.panda.app.validation.group.AddOperation;
import org.wangfeng.panda.app.validation.group.UpdateOperation;


@Slf4j
@RestController
@RequestMapping(value = "/m/rule/tree")
@Api(value = "panda-rule-tree", description = "决策树API")
public class RuleTreeController extends AppBaseController {

    @Autowired
    private RuleTreeService ruleTreeService;

    /**
     * 列表页查询
     *
     * @param tCaRuleTreeVO
     * @return
     */
    @RequestMapping(value = "/list", produces = {"application/json"}, method = RequestMethod.POST)
    @ApiOperation(value = "决策树列表页", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "返回异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "返回异常", response = ResponseEntity.class)

    })
    public ResponseEntity getList(@ApiParam(value = "查询条件，是对象", required = true) @RequestBody @Validated TCaRuleTreeVO tCaRuleTreeVO) {
        Paginate paginate = ruleTreeService.queryPagenate(tCaRuleTreeVO,getPageIndex(),getPageSize());
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
    public ResponseEntity getById(@ApiParam(value = "决策树的主键ID", required = true) @RequestParam Long id) {
        TCaRuleTreeVO tCaRuleTreeVO = ruleTreeService.getById(id);
        if(tCaRuleTreeVO==null){
            return new ResponseEntity(fail("没有查到对应数据！"),HttpStatus.EXPECTATION_FAILED);
        }
        return new ResponseEntity(ok("获取成功",tCaRuleTreeVO), HttpStatus.OK);
    }



    /**
     * 新增决策树
     *
     * @param tCaRuleTreeVO
     * @return
     */
    @RequestMapping(value = "/add", produces = {"application/json"}, method = RequestMethod.POST)
    @ApiOperation(value = "决策树新增", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "返回异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "返回异常", response = ResponseEntity.class)

    })
    public ResponseEntity add(@ApiParam(value = "新增的决策树对象", required = true)
                              @RequestBody @Validated(value = AddOperation.class) TCaRuleTreeVO tCaRuleTreeVO,
                              BindingResult bindingResult) {
        checkBindingResult(bindingResult);
        ruleTreeService.insert(tCaRuleTreeVO);
        return new ResponseEntity(ok(), HttpStatus.OK);
    }





    /**
     * 编辑决策树
     *
     * @param tCaRuleTreeVO
     * @return
     */
    @RequestMapping(value = "/modify", produces = {"application/json"}, method = RequestMethod.POST)
    @ApiOperation(value = "编辑决策树", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "返回异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "返回异常", response = ResponseEntity.class)

    })
    public ResponseEntity modify(@ApiParam(value = "编辑的规则集对象", required = true)
                                 @RequestBody @Validated(value = UpdateOperation.class) TCaRuleTreeVO tCaRuleTreeVO,
                                 BindingResult bindingResult) {
        checkBindingResult(bindingResult);
        ruleTreeService.updateByPrimaryKey(tCaRuleTreeVO);
        return new ResponseEntity(ok(), HttpStatus.OK);
    }



    /**
     * 删除决策树
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/delete", produces = {"application/json"})
    @ApiOperation(value = "删除决策树", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "返回异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "返回异常", response = ResponseEntity.class)

    })
    public ResponseEntity delete(@ApiParam(value = "待删除的ID", required = true) @RequestParam Long id) {
        ruleTreeService.delete(id);
        return new ResponseEntity(ok("删除成功"), HttpStatus.OK);
    }


}
