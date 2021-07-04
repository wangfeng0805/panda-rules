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
import org.wangfeng.panda.app.dao.domain.TCaBusinessLine;
import org.wangfeng.panda.app.model.vo.TCaBusinessLineVO;
import org.wangfeng.panda.app.service.BusinessLineService;
import org.wangfeng.panda.app.validation.group.AddOperation;
import org.wangfeng.panda.app.validation.group.UpdateOperation;


/**
 * <p>
 * Title: [子系统名称]_[模块名]
 * </p>
 * <p>
 * Description: 业务线表controller
 * </p>
 *
 * @author: wangfeng
 * @version: v1.0
 * @author: (lastest modification by wangfeng)
 * @since: 2020-09-26 11:22:43
 */
@SuppressWarnings("all")
@Slf4j
@RestController
@RequestMapping(value = "/m/business/line")
@Api(value = "panda-business-line", description = "业务线API")
public class BusinessLineController extends AppBaseController {

    @Autowired
    private BusinessLineService businessLineService;

    /**
     * 列表页查询
     *
     * @param tCaBusinessLine
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list", produces = {"application/json"}, method = RequestMethod.POST)
    @ApiOperation(value = "业务线列表页", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "返回异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "返回异常", response = ResponseEntity.class)

    })
    public ResponseEntity getList(@ApiParam(value = "查询条件，是对象", required = true) @RequestBody @Validated TCaBusinessLineVO tCaBusinessLineVO) {
        Paginate paginate = businessLineService.queryPagenate(tCaBusinessLineVO,getPageIndex(),getPageSize());
        return new ResponseEntity(ok("获取成功",paginate),HttpStatus.OK);
    }



    /**
     * 通过ID单个查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/get", produces = {"application/json"}, method = RequestMethod.GET)
    @ApiOperation(value = "根据Id查找对应的业务线", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "返回异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "返回异常", response = ResponseEntity.class)

    })
    public ResponseEntity getById(@ApiParam(value = "业务线的主键ID", required = true) @RequestParam Long id) {
        TCaBusinessLine tCaBusinessLine = businessLineService.getById(id);
        if(tCaBusinessLine==null){
            return new ResponseEntity(fail("没有查到对应数据！"),HttpStatus.EXPECTATION_FAILED);
        }
        return new ResponseEntity(ok("获取成功",tCaBusinessLine.invokeToVo()), HttpStatus.OK);
    }




    /**
     * 新增业务线
     *
     * @param tCaBusinessLine
     * @return
     */
    @RequestMapping(value = "/add", produces = {"application/json"}, method = RequestMethod.POST)
    @ApiOperation(value = "业务线新增", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "返回异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "返回异常", response = ResponseEntity.class)

    })
    public ResponseEntity add(@ApiParam(value = "新增的业务线对象", required = true)
                              @RequestBody @Validated(value = AddOperation.class) TCaBusinessLine tCaBusinessLine,
                              BindingResult bindingResult) {
        checkBindingResult(bindingResult);
        businessLineService.insert(tCaBusinessLine);
        return new ResponseEntity(ok(), HttpStatus.OK);
    }


    /**
     * 修改业务线
     *
     * @param tCaBusinessLine
     * @return
     */
    @RequestMapping(value = "/modify", produces = {"application/json"}, method = RequestMethod.POST)
    @ApiOperation(value = "业务线编辑", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "返回异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "返回异常", response = ResponseEntity.class)

    })
    public ResponseEntity modify(@ApiParam(value = "编辑的业务线对象", required = true)
                                 @RequestBody @Validated(value = UpdateOperation.class) TCaBusinessLine tCaBusinessLine,
                                 BindingResult bindingResult) {
        checkBindingResult(bindingResult);
        if(tCaBusinessLine.getBusinessCode()!=null){
            return new ResponseEntity(fail("业务线代码不能被编辑!"), HttpStatus.EXPECTATION_FAILED);
        }
        businessLineService.updateByPrimaryKey(tCaBusinessLine);
        return new ResponseEntity(ok(), HttpStatus.OK);
    }


    /**
     * 删除业务线
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/delete", produces = {"application/json"})
    @ApiOperation(value = "业务线删除", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "返回异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "返回异常", response = ResponseEntity.class)

    })
    public ResponseEntity delete(@ApiParam(value = "待删除的ID", required = true) @RequestParam Long id) {
        businessLineService.delete(id);
        return new ResponseEntity(ok("删除成功"), HttpStatus.OK);
    }

}
