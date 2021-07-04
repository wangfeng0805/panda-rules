package org.wangfeng.panda.app.common.base;


import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * 为所有的实体操作类基类，提供更新时必要的操作控制和创建时的公用字段信息
 *
 */
public abstract class AppBaseModel implements Serializable {

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty("修改时间")
    private Date modifiedTime;

    /**
     * 修改人
     */
    @Column(name = "modified_by")
    @ApiModelProperty("修改人")
    private String modifiedBy;

    /**
     * 插入时间
     */
    @Column(name = "created_time")
    @ApiModelProperty("插入时间")
    private Date createdTime;

    /**
     * 插入人
     */
    @Column(name = "created_by")
    @ApiModelProperty("插入人")
    private String createdBy;

    /**
     * 逻辑删除标识（1:正常状态，0:删除状态）
     */
    @Column(name = "delete_flag")
    @ApiModelProperty("逻辑删除标识（1:正常状态，0:删除状态）")
    private Short deleteFlag;

    /**
     * 获取修改时间
     *
     * @return modified_time - 修改时间
     */
    public Date getModifiedTime() {
        return modifiedTime;
    }

    /**
     * 设置修改时间
     *
     * @param modifiedTime 修改时间
     */
    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    /**
     * 获取修改人
     *
     * @return modified_by - 修改人
     */
    public String getModifiedBy() {
        return modifiedBy;
    }

    /**
     * 设置修改人
     *
     * @param modifiedBy 修改人
     */
    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    /**
     * 获取插入时间
     *
     * @return created_time - 插入时间
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * 设置插入时间
     *
     * @param createdTime 插入时间
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * 获取插入人
     *
     * @return created_by - 插入人
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * 设置插入人
     *
     * @param createdBy 插入人
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * 获取逻辑删除标识（1:正常状态，0:删除状态）
     *
     * @return delete_flag - 逻辑删除标识（1:正常状态，0:删除状态）
     */
    public Short getDeleteFlag() {
        return deleteFlag;
    }

    /**
     * 设置逻辑删除标识（1:正常状态，0:删除状态）
     *
     * @param deleteFlag 逻辑删除标识（1:正常状态，0:删除状态）
     */
    public void setDeleteFlag(Short deleteFlag) {
        this.deleteFlag = deleteFlag;
    }


    @Override
    public String toString() {
        return "AppBaseModel{" +
                "modifiedTime=" + modifiedTime +
                ", modifiedBy='" + modifiedBy + '\'' +
                ", createdTime=" + createdTime +
                ", createdBy='" + createdBy + '\'' +
                ", deleteFlag=" + deleteFlag +
                '}';
    }
}
