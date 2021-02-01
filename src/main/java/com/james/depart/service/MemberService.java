package com.james.depart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.james.depart.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author james
 */
public interface MemberService {

    /**
     * 新建或修改员工信息
     * @param member
     * @return
     */
    Integer save(String content) throws JsonProcessingException;

    Integer save(Member member, Boolean modify);

    Boolean validateAge(Integer age);

    Boolean validateEntryDate(Date date);

    Boolean validatePhoneLegal(String phoneNumber);

    Optional<Member> findById(Long id);

    List<Long> findByIdIn(List ids);

    String findMemberCode(String findMemberCode);

    Page<String> findByDepartmentId(long departmentId, Pageable pageable);

    /**
     * 直接交换排序号
     * @param id
     * @param tagSort
     * @return
     */
    Integer moveById(long id, Integer tagSort);

    Integer saveById(long id, Integer tagSort);

    List<Long> findIdByDepartmentIdIn(List<Long> children);

    Integer modifyByIdAndSort(long id, int modifyNum);

    Integer findIdByDepartmentIdAndSort(long departmentId, int tagSort);

    int deleteById(long departmentId);

    List<Member> findByCodeOrNameStartingWith(String search);
}
