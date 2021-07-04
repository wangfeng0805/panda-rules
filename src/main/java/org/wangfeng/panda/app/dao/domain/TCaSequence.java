package org.wangfeng.panda.app.dao.domain;

import lombok.Data;
import org.wangfeng.panda.app.common.base.AppBaseModel;
import org.wangfeng.panda.app.service.excel.ExportColumn;

import javax.persistence.*;


@Table(name = "t_ca_sequence")
@Data
public class TCaSequence extends AppBaseModel {
    /**
     * 自增主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    @ExportColumn("自增主键ID")
    private Long id;

    /**
     * 编号
     */
    @Column(name = "code")
    @ExportColumn("编号")
    private String code;

    /**
     * 序号
     */
    @ExportColumn("序号")
    private Integer seq;

    /**
     * 类型
     */
    @ExportColumn("类型")
    private String type;

    /**
     * 状态位（1：启用，0：停用）
     */
    @ExportColumn("状态位（1：启用，0：停用）")
    private Short status;


    @Override
    public String toString() {
        return "TCaSequence{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", seq=" + seq +
                ", type='" + type + '\'' +
                ", status=" + status +
                "} " + super.toString();
    }
}