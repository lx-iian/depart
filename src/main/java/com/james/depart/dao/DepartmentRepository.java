package com.james.depart.dao;

import com.james.depart.domain.Department;
import com.james.depart.domain.Member;
import com.james.depart.domain.VO.DepartmentSortVO;
import com.james.depart.domain.VO.DepartmentVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author james
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    /**
     * 查询所有
     *
     * @return
     */
    // @Query("select id, orgId from Department")
    @Override
    List<Department> findAll();

    @Query("select id from Department where id in ?1")
    Integer findIdById(Long id);

    @Query("select id from Department where parentId in ?1")
    List<Long> findByParentIdIn(List<Long> ids);
    /**
     * 根据id和新排序号更新排序
     *
     * @param id
     * @param sort
     * @return
     */
    // @Query("update Department d set d.orgId = department.orgId where d.id = department.id")
    /*@Query("update Department set orgId = ?2 where id = ?1")
    @Modifying
    @Transactional
    int moveById(@Param("id") Long id, @Param("orgId")Long orgId);*/
    @Query("update Department set sort = ?2 where id = ?1")
    @Modifying
    @Transactional
    Integer saveSortById(long id, BigDecimal sort);

    /**
     * @param parentId   上级部门Id
     * @param sort       原本的排序号
     * @param modifySort 修改后的排序号
     * @return
     */
    @Query("update Department set sort = ?3 where parentId = ?1 and sort = ?2")
    @Modifying
    @Transactional
    Integer moveByParentIdAndSort(Long parentId, BigDecimal sort, BigDecimal modifySort);

    @Query("update Department set sort = ?3 where parentId = ?1 and sort = ?2")
    @Modifying
    @Transactional
    Integer saveOrUpdateDepart(Department department);

    /**
     * 保存
     *
     * @param entity
     * @param <S>
     * @return
     */
    @Override
    <S extends Department> S save(S entity);

    Integer findByLevel(Department department);

    @Query(value = "FROM Department d WHERE d.parentId = ?1 AND d.level = ?2")
    Page<String> findByParentIdAndLevel(@Param("parentId") long parentId, @Param("level") int level, Pageable pageable);

    @Query(value = "FROM Department d WHERE d.parentId = ?1")
    Page<String> findByParentId(long parentId, Pageable pageable);

    @Transactional
    @Modifying
    @Query("DELETE FROM Department d WHERE d.id= ?1 OR d.parentId = ?1")
    Integer deleteByIdOrByParentId(Long id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Department d WHERE d.id IN ?1")
    Integer deleteByIdIn(List<Long> ids);

    @Transactional
    @Modifying
    @Query("UPDATE Department SET parentId = ?2 WHERE id = ?1")
    Integer modifyParentByIdAndParentId(long id, long parentId);

    @Query("SELECT 1 FROM Department WHERE departmentCode = ?1")
    Integer findDepartmentCode(String departmentCode);

    @Query("SELECT 1 FROM Department WHERE departmentCode = ?1 AND id <> ?2")
    Integer findDepartmentCodeNot(String departmentCode, Long id);

    @Query("SELECT 1 FROM Department WHERE departmentCode = ?1 AND id NOT IN ?2")
    Integer findDepartmentCodeNotIn(String departmentCode, List ids);

    @Query(value = "FROM Department d WHERE d.departmentCode = ?1")
    Optional<Department> findByDepartmentCode(String departmentCode);

    @Query("SELECT 1 FROM Department WHERE parentId = ?1 AND departmentName = ?2")
    Integer findByParentIdAndDepartmentName(Long parentId, String departmentName);

    @Query("SELECT 1 FROM Department WHERE parentId = ?1 AND departmentName = ?2 AND id <> ?3")
    Boolean findByParentIdAndDepartmentNameNot(Long parentId, String departmentName, Long id);

    @Query("SELECT 1 FROM Department WHERE parentId = ?1 AND departmentName = ?2 AND id NOT IN ?3")
    Boolean findByParentIdAndDepartmentNameNotIn(Long parentId, String departmentName, List ids);

    @Query("SELECT COUNT(parentId) FROM Department WHERE parentId = ?1")
    Integer countByParentId(Long parentId);

    @Query("SELECT MAX(sort) FROM Department WHERE parentId = ?1")
    BigDecimal maxSortByParentId(Long parentId);

    @Query("SELECT NEW com.james.depart.domain.VO.DepartmentSortVO(d.id, MAX(d.sort)) FROM Department d WHERE d.parentId = ?1 AND d.sort < ?2")
    List<DepartmentSortVO> findByParentIdAndSortLessThanMax(long parentId, BigDecimal sort);

    @Query("SELECT NEW com.james.depart.domain.VO.DepartmentSortVO(d.id, MIN(d.sort)) FROM Department d WHERE d.parentId = ?1 AND d.sort > ?2")
    List<DepartmentSortVO> findByParentIdAndSortGreaterThanMin(long parentId, BigDecimal sort);

    @Transactional
    @Modifying
    @Query("update Department set enable = ?2 where id = ?1")
    Integer modifyEnableById(long id, boolean enable);

    @Query(value = "from Department d where d.departmentCode LIKE ?1%")
    List<Department> findByDepartmentCodeStartingWith(String search);

    @Query(value = "from Department d where d.departmentName LIKE ?1%")
    List<Department> findByDepartmentNameStartingWith(String search);

    @Query(value = "from Department d where d.departmentCode LIKE ?1% OR d.departmentName LIKE ?1%")
    List<Department> findByCodeOrNameStartingWith(String search);
}
