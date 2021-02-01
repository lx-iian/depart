package com.james.depart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.james.depart.domain.Department;
import com.james.depart.domain.VO.DepartmentVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * @author james
 */
public interface DepartmentService {

    String initialize() throws IOException;

    Department save(Department department);

    Integer saveSortById(long id, BigDecimal sort);

    /**
     *
     * @return string
     */
    Optional<String> findAll();

    /**
     *
     * @return string
     */
    List<Department> listAll();

    /**
     * 根据Id查找
     * @return
     * @param id
     */
    Optional<Department> findById(Long id);

   // DepartmentVO findDepartmentVOById(Long id);

    /**
     * 获取部门信息和员工总人数：本部门+子部门
     * @param id
     * @return
     * @throws Exception
     */
    DepartmentVO getDepartmentAndTotalMembers(Long id) throws Exception;

    Integer getDepartmentAndChildrenMembers(Long id);

    /**
     * 直接交换排序号
     * @param id
     * @param tagSort 更改之后的序号
     * @return
     */
    Integer exchangeSortByIdAndTagSort(long id, BigDecimal tagSort);

    /**
     * @param id
     * @param moveUp：是否是上(前）移 ps:1->2->3 将3上移 1->3->2
     * @return
     */
    Integer modifyOneSort(long id, boolean moveUp);
    /**
     * 新建或修改部门
     * @param department
     * @param modify
     * @return
     */
    //Department saveOrUpdateDepart(Department department);
    String saveOrUpdateDepart(Department department, boolean modify) throws JsonProcessingException;

    /**
     * 根据上级部门Id和等级查询下一级部门
     * @param parentId
     * @return
     */
    Page<String> findByParentId(long parentId, Pageable pageable);

    int deleteById(long departmentId);

    /**
     * 查询所有下级部门（包括下级部门的下级部门）
     * @param departmentId
     * @return
     */
    List<Long> findByParentIdIn(long departmentId);

    Optional<Department> findByDepartmentCode(String departmentCode);

    List<Department> findByDepartmentCodeStartingWith(String search);

    List<Department> findByDepartmentNameStartingWith(String search);

    String modifyParentByIdAndParentId(long id, long parentId);

    Boolean validateDepartmentCode(String departmentCode);

    Boolean validateDepartmentCodeRepeat(String departmentCode);

    Boolean validateDepartmentCodeRepeatNot(String departmentCode, Long id);

    Boolean validateDepartmentName(Long parentId, String departmentName);

    Boolean validateDepartmentNameNot(Long parentId, String departmentName, Long id);



    /**
     * smallTag=2的sort,bigTag=3的sort ，1移动到3后，则smallTag=3的sort,bigTag=4的sort.
     * @param id
     * @param moveUp
     * @param smallTag
     * @param bigTag
     * @return
     */
    Integer modifyByIdAndSort(long id, boolean moveUp, BigDecimal smallTag, BigDecimal bigTag);

    /**
     * 启、停用部门
     * @param id 部门id
     * @param enable 设置状态
     * @return
     */
    int modifyEnableById(long id, boolean enable);

    List<Department> findByCodeOrNameStartingWith(String search);
}
