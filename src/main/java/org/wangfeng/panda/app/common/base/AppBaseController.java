package org.wangfeng.panda.app.common.base;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.service.excel.ExcelUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@Slf4j
public class AppBaseController {

    private static final int PAGE_INDEX_DEFAULT = 1;
    private static final int PAGE_SIZE_DEFAULT = 20;

    public int getPageIndex() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String pageIndex = request.getParameter("pageNo");
        if (StringUtils.isEmpty(pageIndex)) {
            return PAGE_INDEX_DEFAULT;
        }
        try {
            return Integer.parseInt(pageIndex);
        } catch (NumberFormatException e){
            return PAGE_INDEX_DEFAULT;
        }
    }

    /**
     * <p>
     * description:获取每页查询的数量，默认为20条
     * </p>
     *
     * @return 查询的数量，默认20
     */
    public int getPageSize() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String pageSize = request.getParameter("pageSize");
        if (StringUtils.isEmpty(pageSize)) {
            return PAGE_SIZE_DEFAULT;
        }
        try{
            return Integer.parseInt(pageSize);
        } catch (NumberFormatException e){
            return PAGE_SIZE_DEFAULT;
        }

    }

    /**
     * 把校验的错误结果以异常抛出
     * @param bindingResult
     */
    protected void checkBindingResult(BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldError().getDefaultMessage();
            throw new RuleRuntimeException(message);
        }
    }

    /**
     * 操作成功，返回数据中含有三个key；code：状态：见枚举类；message：反馈的消息;data:表示需要传达的参数，默认data为null
     * @return 成功
     */
    public JSONObject ok(){
        return ok("操作成功");
    }

    /**
     * 操作成功，返回数据中含有三个key；code：状态：见枚举类；message：反馈的消息;data:表示需要传达的参数，默认data为null
     * @param msg 反馈的消息
     * @return 成功附带信息
     */
    public JSONObject ok(String msg){
        return ok(msg,null);
    }

    /**
     * 操作成功，返回数据中含有三个key；code：状态：见枚举类；message：反馈的消息;data:表示需要传达的参数，默认data为null
     * @param msg 反馈的消息
     * @param data 返回数据
     * @return 成功附带信息与数据, 如数据为null则不返回数据字段
     */
    public JSONObject ok(String msg,Object data){
        return rs(msg,1,data);
    }

    /**
     * 操作成功，返回数据中含有三个key；code：状态：见枚举类；message：反馈的消息;data:表示需要传达的参数，默认data为null
     * @param msg 反馈的消息
     * @param code 状态码
     * @param data  反馈所带的内容
     * @return 成功附带信息
     */
    public JSONObject ok(String msg,Integer code,Object data){
        return rs(msg,code,data);
    }


    /**
     * 操作成功，返回数据中含有三个key；code：状态：见枚举类；message：反馈的消息;data:表示需要传达的参数，默认data为null
     * @param msg 反馈的消息
     * @param code 状态码
     * @param data  反馈所带的内容
     * @return 成功附带信息
     */
    public JSONObject ok(String msg,String code,Object data){
        return rs(msg,code,data);
    }

    /**
     * 操作失败，返回数据中含有三个key；code：状态：见枚举类；message：反馈的消息;data:表示需要传达的参数，默认data为null
     * @return 失败
     */
    public JSONObject fail(){
        return fail("操作失败");
    }

    /**
     * 操作失败，返回数据中含有三个key；code：状态：见枚举类；message：反馈的消息;data:表示需要传达的参数，默认data为null
     * @param msg 反馈的消息
     * @return 失败附带信息
     */
    public JSONObject fail(String msg){
        return fail(msg,null);
    }

    /**
     * 操作失败，返回数据中含有三个key；code：状态：见枚举类；message：反馈的消息;data:表示需要传达的参数，默认data为null
     * @param msg 反馈的消息
     * @param data 返回数据
     * @return 失败附带信息, 如数据为null则不返回数据字段
     */
    public JSONObject fail(String msg,Object data){
        return rs(msg,-1,data);
    }

    /**
     * 操作失败，返回数据中含有三个key；code：状态：见枚举类；message：反馈的消息;data:表示需要传达的参数，默认data为null
     * @param msg 反馈的消息
     * @param code 状态码
     * @param data 返回数据
     * @return 失败附带信息, 如数据为null则不返回数据字段
     */
    public JSONObject fail(String msg,Integer code,Object data){
        return rs(msg,code,data);
    }


    /**
     * 操作失败，返回数据中含有三个key；code：状态：见枚举类；message：反馈的消息;data:表示需要传达的参数，默认data为null
     * @param msg 反馈的消息
     * @param code 状态码
     * @param data 返回数据
     * @return 失败附带信息, 如数据为null则不返回数据字段
     */
    public JSONObject fail(String msg,String code,Object data){
        return rs(msg,code,data);
    }

    /**
     * 拼接成统一的json
     * @param msg
     * @param code
     * @param data
     * @return
     */
    private JSONObject rs(String msg,int code,Object data){
        JSONObject rs = new JSONObject();
        rs.put("code", code);
        rs.put("message", msg);
        if(data != null){
            try{
                Object obj = JSONObject.toJSON(data);
                rs.put("data", obj);
            }catch(Exception e){
                rs.put("data", data);
            }
        }else{
            rs.put("data", "");
        }
        return rs;
    }




    /**
     * 拼接成统一的json（此处的code为String类型，防止有些系统用gson出问题）
     * @param msg
     * @param code
     * @param data
     * @return
     */
    private JSONObject rs(String msg,String code,Object data){
        JSONObject rs = new JSONObject();
        rs.put("code", code);
        rs.put("message", msg);
        if(data != null){
            try{
                Object obj = JSONObject.toJSON(data);
                rs.put("data", obj);
            }catch(Exception e){
                rs.put("data", data);
            }
        }else{
            rs.put("data", "");
        }
        return rs;
    }


    /**
     * 导出的基本操作
     * @param fileName
     * @param wb
     */
    protected void exportBaseOperation(String fileName , XSSFWorkbook wb){
        //导出到页面
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        try {
            ExcelUtil.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            log.error("导出到页面出错");
            e.printStackTrace();
        }
    }

}
