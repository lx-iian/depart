package com.james.depart.dao;

import com.james.depart.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(value = "FROM Member WHERE memberCode = ?1")
    Optional<Member> findByMemberCode(String memberCode);

    @Query(value = "SELECT memberCode FROM Member WHERE memberCode = ?1")
    String findMemberCode(String memberCode);

    @Query(value = "FROM Member d WHERE d.departmentId = ?1")
    Page<String> findByDepartmentId(long departmentId, Pageable pageable);

    @Query("update Member SET sort = ?3 WHERE departmentId = ?1 AND sort = ?2")
    @Modifying
    @Transactional
    Integer moveByDepartmentIdAndSort(Long departmentId, int sort, Integer tagSort);

    @Query("update Member SET sort = ?2 WHERE id = ?1")
    @Modifying
    @Transactional
    Integer saveById(long id, int modifySort);

    @Query(value = "SELECT id FROM Member WHERE departmentId IN ?1")
    List<Long> findIdByDepartmentIdIn(List<Long> children);

    @Query("UPDATE Member SET sort = sort + 1 WHERE departmentId = ?1 AND sort >= ?3 AND sort < ?2")
    @Modifying
    @Transactional
    int saveSortsAddByDepartmentIdAndSort(Long departmentId, Integer sort, int tagSort);

    @Query("UPDATE Member SET sort = sort - 1 WHERE departmentId = ?1 AND sort > ?2 AND sort <= ?3")
    @Modifying
    @Transactional
    int saveSortsSubByDepartmentIdAndSort(Long departmentId, Integer sort, int tagSort);

    @Query(value = "SELECT 1 FROM Member WHERE departmentId = ?1 AND sort = ?2")
    Integer findIdDepartmentIdAndSort(long departmentId, int tagSort);

    @Query("select MAX(sort) from Member WHERE departmentId = ?1")
    int maxSortByDepartmentId(Long departmentId);

    @Query(value = "FROM Member d WHERE d.memberCode LIKE ?1% OR d.memberName  LIKE ?1%")
    List<Member> findByCodeOrNameStartingWith(String search);
}
