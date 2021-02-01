package com.james.depart.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.james.depart.dao.MemberRepository;
import com.james.depart.domain.Department;
import com.james.depart.domain.Member;
import com.james.depart.service.MemberService;
import com.james.depart.utils.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MemberServiceImpl implements MemberService {

    final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Value("${member-code.prefix}")
    private String memberCodePrefix;

    @Value("${age-between.lte}")
    private int ageLess;

    @Value("${age-between.gte}")
    private int ageMore;

    private static final ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap(200);

    @Override
    public Integer save(String content) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(content);
        int modify = jsonNode.path("modify").asInt();

        ObjectNode object = (ObjectNode) jsonNode;
        object.remove("modify");
        Member member = objectMapper.readValue(object.toString(), Member.class);
        Long memberId = member.getId();
        boolean memberExists = memberRepository.existsById(memberId);
        if (modify == 0 && memberExists) {
            return 0;
        }
        memberRepository.save(member);
        return 1;
    }

    @Override
    public Integer save(Member member, Boolean modify) {
        String memberCode = null;
        Integer id;
        Optional<Member> optionalMember = Optional.empty();
        // 新增
        if (!modify) {
            id = (Integer) concurrentHashMap.get("id");
            Integer cacheMonth = (Integer) concurrentHashMap.get("month");
            if (id == null) {
                id = 0;
                concurrentHashMap.put("id", 2);
            }
            // 每月更新 重置id序号
            if (cacheMonth == null) {
                cacheMonth = TimeUtil.getMonth();
                concurrentHashMap.put("month", cacheMonth);
            }
            int nowMonth = TimeUtil.getMonth();

            if (nowMonth != cacheMonth) {
                concurrentHashMap.put("month", nowMonth);
                concurrentHashMap.put("id", 1);
            }
            /*memberCode = memberCodePrefix + TimeUtil.getFullYearAndMonth() + id;
            String hasMemberCode = findMemberCode(memberCode);
            while (hasMemberCode != null) {
                memberCode = memberCodePrefix + TimeUtil.getFullYearAndMonth() + ++id;
                hasMemberCode = findMemberCode(memberCode);
            }*/
            String hasMemberCode;
            do {
                memberCode = memberCodePrefix + TimeUtil.getFullYearAndMonth() + ++id;
                hasMemberCode = findMemberCode(memberCode);
            } while (hasMemberCode != null);
            long mathUUID = UUIDUtil.getMathUUID();
            member.setId(mathUUID);
        } else {
            optionalMember = findById(member.getId());
            if (!optionalMember.isPresent()) {
                return 0;
            }
        }
        member.setMemberCode(modify ? optionalMember.get().getMemberCode() : memberCode);
        if (!validateAge(member.getAge())) {
            return 0;
        }
        if (!validateEntryDate(member.getEntryTime())) {
            return 0;
        }
        if (member.getPhone() != null) {
            if (!validatePhoneLegal(member.getPhone())) {
                return 0;
            }
        }
        memberRepository.save(member);
        return 1;
    }

    @Override
    public Boolean validateAge(Integer age) {
        return age >= ageMore && age <= ageLess;
    }

    @Override
    public Boolean validateEntryDate(Date date) {
        Date nowDate = new Date();
        int i = nowDate.compareTo(date);
        /*LocalDate nowDate = LocalDate.now();
        int i = nowDate.compareTo(localDate);*/
        return i >= 0;
    }

    @Override
    public Boolean validatePhoneLegal(String phoneNumber) {
        return RegexUtil.PhoneLegal(phoneNumber);
    }

    @Override
    public Optional<Member> findById(Long id) {
        Optional<Member> member = memberRepository.findById(id);
        return member;
    }

    @Override
    public List<Long> findByIdIn(List ids) {
        List allById = memberRepository.findAllById(ids);
        return allById;
    }

    @Override
    public String findMemberCode(String findMemberCode) {
        return memberRepository.findMemberCode(findMemberCode);
    }

    @Override
    public Page<String> findByDepartmentId(long departmentId, Pageable pageable){
        return memberRepository.findByDepartmentId(departmentId, pageable);
    }

    @Transactional
    @Override
    public Integer moveById(long id, Integer tagSort) {
        Optional<Member> member = memberRepository.findById(id);
        if (!member.isPresent()) {
            return 0;
        }
        Integer sort = member.get().getSort();
        Long departmentId = member.get().getDepartmentId();
        // 先将被替换数据修改
        Integer modify = memberRepository.moveByDepartmentIdAndSort(departmentId, tagSort, sort);
        if (0 != modify) {
            modify = saveById(id, tagSort);
            if (modify == IResult.MSG_MODIFY_ONE) {
                return IResult.MSG_MODIFY_ONE;
            }

        }
        return IResult.MSG_MODIFY_ZERO;
    }

    @Override
    public Integer saveById(long id, Integer tagSort) {
        return memberRepository.saveById(id, tagSort);
    }

    @Override
    public List<Long> findIdByDepartmentIdIn(List<Long> children) {
        return memberRepository.findIdByDepartmentIdIn(children);
    }

    @Override
    public Integer modifyByIdAndSort(long id, int modifyNum) {
        Optional<Member> member = findById(id);

        if (!member.isPresent()) {
            return -1;
        }
        Integer sort  = member.get().getSort();
        if (sort == 1) {
            return IResult.MSG_ALREADY_FIRST;
        }
        Long departmentId = member.get().getDepartmentId();
        int tagSort = sort + modifyNum;
        if (tagSort < 1) {
            return 0;
        }
        Integer idByDepartmentIdAndSort = findIdByDepartmentIdAndSort(departmentId, tagSort);
        if (idByDepartmentIdAndSort == null) {
            int i = memberRepository.maxSortByDepartmentId(departmentId);
            if (i == sort) {
                return IResult.MSG_ALREADY_LAST;
            }
            return 0;
        }
        int isModify;
        // 位移向前（起始为1） 中间排序号增大
        if (modifyNum > 0) {
            isModify = memberRepository.saveSortsSubByDepartmentIdAndSort(departmentId, sort, tagSort);
        } else {
            isModify = memberRepository.saveSortsAddByDepartmentIdAndSort(departmentId, sort, tagSort);
        }
        if (isModify == 0) {
            return 0;
        }
        return saveById(id, sort + modifyNum);
    }

    @Override
    public Integer findIdByDepartmentIdAndSort(long departmentId, int tagSort) {
        return memberRepository.findIdDepartmentIdAndSort(departmentId, tagSort);
    }


    @Override
    public List<Member> findByCodeOrNameStartingWith(String search) {
        return memberRepository.findByCodeOrNameStartingWith(search);
    }

    @Transactional
    @Override
    public int deleteById(long id) {
        Optional<Member> Member = findById(id);
        if (!Member.isPresent()) {
            return -1;
        }
        memberRepository.deleteById(id);
        return 0;
    }


}
