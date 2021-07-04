package org.wangfeng.panda.app.model.enums;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 决策变量数据来源枚举
 */
public enum VariableParamSourceEnum {


    paramSource1((short)1,"系统获取"),
    paramSource2((short)2,"申请书信息"),
    paramSource3((short)3,"实名认证信息"),
    paramSource4((short)4,"OCR"),
    paramSource5((short)5,"申请信息整合"),
    paramSource6((short)6,"设备栏位"),
    paramSource7((short)7,"行为信息"),
    paramSource8((short)8,"业务系统"),
    paramSource9((short)9,"菁卡评分"),
    paramSource10((short)10,"统一身份认证"),
    paramSource11((short)11,"商城信息"),
    paramSource12((short)12,"实名"),
    paramSource13((short)13,"地址一致性"),
    paramSource14((short)14,"银河标签中心"),
    paramSource15((short)15,"距离计算"),
    paramSource16((short)16,"中诚信四要素验证"),
    paramSource17((short)17,"运营商常驻位置距离"),
    paramSource18((short)18,"企业照面信息"),
    paramSource19((short)19,"企业固话验证结果"),
    paramSource20((short)20,"公共风险评分"),
    paramSource21((short)21,"在网时长"),
    paramSource22((short)22,"实名核验"),
    paramSource23((short)23,"手机号归属地"),
    paramSource24((short)24,"网贷信息"),
    paramSource25((short)25,"多头信息"),
    paramSource26((short)26,"申请统计"),
    paramSource27((short)27,"百融特殊名单"),
    paramSource28((short)28,"企业信用基本信息"),
    paramSource29((short)29,"企业失信被执行"),
    paramSource30((short)30,"电信实名认证"),
    paramSource31((short)31,"学历"),
    paramSource32((short)32,"高法"),
    paramSource33((short)33,"互金协会"),
    paramSource34((short)34,"聚信立公积金"),
    paramSource35((short)35,"聚信立蜜罐"),
    paramSource36((short)36,"敬众"),
    paramSource37((short)37,"清算协会"),
    paramSource38((short)38,"同盾信贷保镖"),
    paramSource39((short)39,"腾讯反欺诈"),
    paramSource40((short)40,"亿美"),
    paramSource41((short)41,"ZR地址画像"),
    paramSource42((short)42,"ZR小白信用"),
    paramSource43((short)43,"ZR黑名单"),
    paramSource44((short)44,"ZR消费画像"),
    paramSource45((short)45,"ZR多头共债"),
    paramSource46((short)46,"ZR金融画像"),
    paramSource47((short)47,"ZR欺诈评分"),
    paramSource48((short)48,"ZR手机号画像"),
    paramSource49((short)49,"ZR盘古分"),
    paramSource50((short)50,"ZR网物画像"),
    paramSource51((short)51,"智数消费等级"),
    paramSource52((short)52,"智数经济稳定性"),
    paramSource53((short)53,"智数职业稳定性"),
    paramSource54((short)54,"银行标签中心"),
    paramSource55((short)55,"京东反欺诈黑名单"),
    paramSource56((short)56,"ZR多头借贷查询"),
    paramSource57((short)57,"聚合三要素"),
    paramSource58((short)58,"天盾"),
    paramSource59((short)59,"plus资格"),
    paramSource60((short)60,"多头撞库")
    ;


    private Short code;
    private String name;


    public Short getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    VariableParamSourceEnum(Short code, String name) {
        this.code = code;
        this.name = name;
    }

    public static VariableParamSourceEnum getByCode(Short code){
        if(code == null){
            return null;
        }
        for(VariableParamSourceEnum i: VariableParamSourceEnum.values()){
            if(i.getCode() == code.intValue()){
                return i;
            }
        }
        return null;
    }

    public static List<JSONObject> queryAllParamSourceEnums(){
        List<JSONObject> paramSourceEnumList = new ArrayList();
        for(VariableParamSourceEnum i: VariableParamSourceEnum.values()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code",i.getCode());
            jsonObject.put("name",i.getName());
            paramSourceEnumList.add(jsonObject);
        }
        return paramSourceEnumList;
    }
}
