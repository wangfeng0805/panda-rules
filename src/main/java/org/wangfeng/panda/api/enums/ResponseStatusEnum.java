package org.wangfeng.panda.api.enums;

public enum  ResponseStatusEnum {

    CALCULATE_SUCCESS("1000","计算成功"),
    CALCULATE_ARGS_ERROR("1001","传入参数异常"),
    CALCULATE_RESULT_ERROR("1002","计算结果错误");


    private String code;
    private String message;

    ResponseStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }


    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


    /**
     * 通过code查询出对应的response状态枚举
     * @param code
     * @return
     */
    public static ResponseStatusEnum getByCode(String code){
        if(code == null){
            return null;
        }
        for(ResponseStatusEnum i: ResponseStatusEnum.values()){
            if(i.getCode().equals(code)){
                return i;
            }
        }
        return null;
    }





}
