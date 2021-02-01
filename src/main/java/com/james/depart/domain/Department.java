package com.james.depart.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.Proxy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author james
 * @date 2020-12-28
 */
@Entity
@Table(name = "DEPARTMENT")
@Cacheable
@Proxy(lazy = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("部门实体")
// 忽略传值中没有对应字段
// @JsonIgnoreProperties(ignoreUnknown = true)
public class Department implements Serializable {

    @Id
    @ApiModelProperty("部门ID")
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ApiModelProperty("部门代码")
    @Column(name = "DEPARTMENT_CODE")
    private String departmentCode;

    @ApiModelProperty("部门名称")
    @Column(name = "DEPARTMENT_NAME")
    private String departmentName;

    @ApiModelProperty("上级部门ID")
    @Column(name = "PARENT_ID")
    private Long parentId;

    @ApiModelProperty("部门等级")
    @Column(name = "LEVEL")
    private Integer level;

    @ApiModelProperty("部门排序号")
    @Column(name = "SORT")
    private BigDecimal sort;

    @CreatedDate
    @ApiModelProperty("部门创建时间")
    @Column(name = "CREATE_TIME")
    private Date createTime;

    @LastModifiedDate
    @ApiModelProperty("部门修改时间")
    @Column(name = "MODIFY_TIME")
    private Date modifyTime;

    @ApiModelProperty("是否可以修改")
    @Column(name = "CAN_MODIFY")
    private Boolean canModify;

    @ApiModelProperty("部门备注")
    @Column(name = "COMMENT")
    private String comment;

    @ApiModelProperty("是否启用")
    @Column(name = "ENABLE")
    private Boolean enable;

}
