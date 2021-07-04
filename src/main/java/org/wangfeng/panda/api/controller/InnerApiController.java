package org.wangfeng.panda.api.controller;

import org.wangfeng.panda.app.common.base.AppBaseController;
import org.wangfeng.panda.app.service.InnerService;
import org.wangfeng.panda.app.service.redis.RedisService;
import org.wangfeng.panda.app.util.StringUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping(value = "/m/api/inner")
public class InnerApiController extends AppBaseController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private InnerService innerService;

    @GetMapping(value = "/redis/delete")
    public ResponseEntity delete(Integer type){
        redisService.deleteAllCache(type);
        return new ResponseEntity("完成", HttpStatus.OK);

    }


    /**
     * 处理老数据
     * @return
     */
    @GetMapping(value = "/dealOldData")
    public ResponseEntity dealOldData(){
        innerService.dealOldData();
        return new ResponseEntity("正在处理中,请查看日志！",HttpStatus.OK);
    }


    /**
     * 查询所有的下拉选中的内容
     * @return
     */
    @RequestMapping(value = "/queryAllVariable", produces = {"application/json"}, method = RequestMethod.GET)
    @ApiOperation(value = "查询所有的下拉选中的内容", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回正常", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "返回异常", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "返回异常", response = ResponseEntity.class)

    })
    public ResponseEntity queryAllVariable(@ApiParam(value = "决策变量的主键ID", required = true) @RequestParam String businessCode,
                                           @ApiParam(value = "决策变量的主键ID", required = true) @RequestParam String keyword){
        if(StringUtils.isBlank(businessCode)){
            return new ResponseEntity(fail("请先选择业务线"),HttpStatus.EXPECTATION_FAILED);
        }
//        if(StringUtils.isBlank(keyword)){
//            return new ResponseEntity(fail("请输入关键字"),HttpStatus.EXPECTATION_FAILED);
//        }
        Map<String,Object> variableMap = innerService.queryAllVariable(businessCode,keyword);
        return new ResponseEntity(ok("获取成功",variableMap),HttpStatus.OK);
    }

}
