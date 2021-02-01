package com.james.depart.domain.VO;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * @author james
 * @description
 * @date 2020-12-28
 */
@Data
/*@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor*/
@ApiModel("部门VO")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepartmentVO {

    private Long id;

    private String departmentCode;

    private String departmentName;

    private Long parentId;

    private Integer level;

    private BigDecimal sort;

    private Date createTime;

    private Date modifyTime;

    private Boolean canModify;

    private String comment;

    private Boolean enable;

    private Integer TotalChildrenMembers;
}
