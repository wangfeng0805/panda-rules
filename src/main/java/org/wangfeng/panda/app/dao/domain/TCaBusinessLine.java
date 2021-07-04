package org.wangfeng.panda.app.dao.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.wangfeng.panda.app.common.base.AppBaseModel;
import org.wangfeng.panda.app.model.enums.StatusEnum;
import org.wangfeng.panda.app.model.vo.TCaBusinessLineVO;
import org.wangfeng.panda.app.service.excel.ExportColumn;
import org.wangfeng.panda.app.validation.group.AddOperation;
import org.wangfeng.panda.app.validation.group.DeleteOperation;
import org.wangfeng.panda.app.validation.group.UpdateOperation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Table(name = "t_ca_business_line")
@Data
public class TCaBusinessLine extends AppBaseModel {
    /**
     * 自增主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    @ApiModelProperty("自增主键ID")
    @NotNull(message="自增主键不能为空" , groups = {DeleteOperation.class, UpdateOperation.class})
    @ExportColumn("自增主键ID")
    private Long id;

    /**
     * 业务线代码
     */
    @Column(name = "business_code")
    @ApiModelProperty("业务线代码")
    @NotNull(message="业务线代码不能为空" , groups = {AddOperation.class})
    @Pattern(message = "业务线代码仅允许输入字母、数字、下划线，需以字母开头", regexp = "^[a-zA-Z][_a-zA-Z0-9]+$", groups = {AddOperation.class})
    @ExportColumn("业务线代码")
    private String businessCode;

    /**
     * 业务线名称
     */
    @Column(name = "business_name")
    @ApiModelProperty("业务线名称")
    @Pattern(message = "业务线名称仅允许输入汉字、字母、数字、下划线", regexp = "^[\\u4e00-\\u9fa5_a-zA-Z0-9]+$", groups = {AddOperation.class,UpdateOperation.class})
    @ExportColumn("业务线名称")
    private String businessName;

    /**
     * 业务线备注
     */
    @Column(name = "business_remark")
    @ApiModelProperty("业务线备注")
    @ExportColumn("业务线备注")
    private String businessRemark;

    /**
     * 状态位（1：启用，0：停用）
     */
    @ApiModelProperty("状态位（1：启用，0：停用）")
    @ExportColumn("状态位（1：启用，0：停用）")
    private Short status;


    /**
     * 实体类向vo的转换方法
     * @return
     */
    public TCaBusinessLineVO invokeToVo(){
        TCaBusinessLineVO vo = new TCaBusinessLineVO();
        BeanUtils.copyProperties(this,vo);

        //状态名称赋值
        StatusEnum statusEnum = StatusEnum.getByCode(vo.getStatus());
        if(statusEnum!=null){
            vo.setStatusName(statusEnum.getName());
        }

        return vo;
    }


    @Override
    public String toString() {
        return "TCaBusinessLine{" +
                "id=" + id +
                ", businessCode='" + businessCode + '\'' +
                ", businessName='" + businessName + '\'' +
                ", businessRemark='" + businessRemark + '\'' +
                ", status=" + status +
                "} " + super.toString();
    }
}