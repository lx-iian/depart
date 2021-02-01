package com.james.depart.domain.VO;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author james
 * @description
 * @date 2021-01-06
 */
@Data
public class DepartmentSortVO implements Serializable {

    private Long id;

    private BigDecimal sort;

    public DepartmentSortVO() {
    }

    public DepartmentSortVO(Long id, BigDecimal sort) {
        this.id = id;
        this.sort = sort;
    }


}
