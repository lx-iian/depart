package com.james.depart.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.Proxy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "MEMBER")
@Cacheable
@Proxy(lazy = false)
@Data
@ApiModel("员工实体")
// 忽略传值中没有对应字段
@JsonIgnoreProperties(ignoreUnknown = true)
public class Member implements Serializable {
    @Id
    @ApiModelProperty("员工ID")
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ApiModelProperty("员工号")
    @Column(name = "MEMBER_CODE")
    private String memberCode;

    @ApiModelProperty("姓名")
    @Column(name = "MEMBER_NAME")
    private String memberName;

    @ApiModelProperty("性别")
    @Column(name = "GENDER")
    private Byte gender;

    @ApiModelProperty("年龄")
    @Column(name = "AGE")
    private Integer age;

    @ApiModelProperty("入职时间")
    @Column(name = "ENTRY_DATE")
    private Date entryTime;

    @ApiModelProperty("联系电话")
    @Column(name = "PHONE")
    private String phone;

    @ApiModelProperty("所属部门ID")
    @Column(name = "DEPARTMENT_ID")
    private Long departmentId;

    @ApiModelProperty("排序号")
    @Column(name = "SORT")
    private Integer sort;

    @ApiModelProperty("员工备注")
    @Column(name = "COMMENT")
    private String comment;

    @CreatedDate
    @ApiModelProperty("员工创建时间")
    @Column(name = "CREATE_TIME")
    private Date createTime;

    @LastModifiedDate
    @ApiModelProperty("员工修改时间")
    @Column(name = "MODIFY_TIME")
    private Date modifyTime;

    @ApiModelProperty("是否启用")
    @Column(name = "ENABLE")
    private Boolean enable;
}
