package com.james.depart.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.james.depart.dao.DepartmentRepository;
import com.james.depart.domain.Department;
import com.james.depart.domain.VO.DepartmentSortVO;
import com.james.depart.domain.VO.DepartmentVO;
import com.james.depart.service.DepartmentService;
import com.james.depart.service.MemberService;
import com.james.depart.utils.IResult;
import com.james.depart.utils.RegexUtil;
import com.james.depart.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

/**
 * @author james
 * @description
 * @date 2020-12-28
 */
@Service
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    final DepartmentRepository departmentRepository;

    final MemberService memberService;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, MemberService memberService) {
        this.departmentRepository = departmentRepository;
        this.memberService = memberService;
    }

    @Override
    public String initialize() throws IOException {
        boolean exists = departmentRepository.existsById(1L);
        if (!exists) {
            ObjectMapper objMapper = new ObjectMapper();
            URL resource = DepartmentServiceImpl.class.getClassLoader().getResource("META-INF/department-initialize.json");
            Department department = objMapper.readValue(resource, Department.class);
            department.setCreateTime(new Date());
            departmentRepository.save(department);
            return "初始化部门完成！";
        }
        return "已存在部门根节点，继续执行";
    }

    @Override
    public Integer saveSortById(long id, BigDecimal sort) {
        return departmentRepository.saveSortById(id, sort);
    }

    @Override
    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public Optional<String> findAll() {
        List<Department> all = departmentRepository.findAll();
        return Optional.ofNullable(all.toString());
    }

    @Override
    public List<Department> listAll() {
        return departmentRepository.findAll();
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Optional<Department> findById(Long id) {
        return departmentRepository.findById(id);
    }

    @Override
    public DepartmentVO getDepartmentAndTotalMembers(Long id) throws Exception {
        Optional<Department> department = findById(id);
        Integer departmentAndChildrenMembers = getDepartmentAndChildrenMembers(department.get().getId());
        /*DepartmentVO departmentVO = new DepartmentVO();
        MyBeanUtils.copyBean1(department,departmentVO);
        DepartmentVO departmentVO2 = MyBeanUtils.copyProperties(department.toString(), DepartmentVO::new);

        try {

            BeanUtils.copyProperties(department, departmentVO);
        } catch (Exception e) {
            System.out.println(e);
        }*/

        /*ObjectMapper objectMapper12 = new ObjectMapper();
        String departmentStr = department.toString();
        String substring = "{" + departmentStr.substring(20, departmentStr.length() - 2) + "}";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        DepartmentVO departmentVO = objectMapper.readValue(department.toString(), DepartmentVO.class);
           */
        DepartmentVO departmentVO = new DepartmentVO();
        departmentVO.setId(department.get().getId());
        departmentVO.setDepartmentCode(department.get().getDepartmentCode());
        departmentVO.setDepartmentName(department.get().getDepartmentName());
        departmentVO.setCreateTime(department.get().getCreateTime());
        departmentVO.setParentId(department.get().getParentId());
        departmentVO.setLevel(department.get().getLevel());
        departmentVO.setComment(department.get().getComment());
        departmentVO.setEnable(department.get().getEnable());
        departmentVO.setSort(department.get().getSort());
        departmentVO.setTotalChildrenMembers(departmentAndChildrenMembers);
        return departmentVO;
    }

    @Override
    public Integer getDepartmentAndChildrenMembers(Long id) {
        Optional<Department> department = departmentRepository.findById(id);
        List<Long> totalDepartment = findByParentIdIn(department.get().getId());
        List<Long> members = memberService.findIdByDepartmentIdIn(totalDepartment);
        return members == null ? 0 : members.size();
    }

    @Transactional
    @Override
    public Integer exchangeSortByIdAndTagSort(long id, BigDecimal tagSort) {
        Optional<Department> department = findById(id);
        BigDecimal sort = department.get().getSort();
        Long parentId = department.get().getParentId();
        Integer modify = departmentRepository.moveByParentIdAndSort(parentId, tagSort, sort);
        if (0 != modify) {
            modify = departmentRepository.saveSortById(id, tagSort);
            if (modify == IResult.MSG_MODIFY_ONE) {
                return IResult.MSG_MODIFY_ONE;
            }
            return IResult.MSG_MODIFY_ZERO;
        }
        return IResult.MSG_ALREADY_LAST;
    }

    @Override
    @Transactional
    public Integer modifyOneSort(long id, boolean moveUp) {
        Optional<Department> department = findById(id);
        if (!department.isPresent()) {
            return 0;
        }
        // Assert.notEmpty(,"修改失败，无此部门");
        BigDecimal sort = department.get().getSort();
        /*if (sort == IResult.FIRST) {
            return IResult.MSG_ALREADY_FIRST;
        }*/
        Long parentId = department.get().getParentId();
        Assert.notNull(parentId, "修改失败，没有上级部门");
        List<DepartmentSortVO> departmentSortVOList;
        if (moveUp) {
            departmentSortVOList = departmentRepository.findByParentIdAndSortLessThanMax(parentId, sort);
        } else {
            departmentSortVOList = departmentRepository.findByParentIdAndSortGreaterThanMin(parentId, sort);
        }
        if (departmentSortVOList.isEmpty()) {
            return IResult.MSG_MODIFY_ZERO;
        }
        DepartmentSortVO departmentSortVO = departmentSortVOList.get(0);
        Long moveId = departmentSortVO.getId();
        if (moveId == null) {
            return IResult.MSG_MODIFY_ZERO;
        }
        BigDecimal tagSort = departmentSortVO.getSort();
        // 先将被调整的排序修改
        int modify = departmentRepository.saveSortById(moveId, sort);
        //Integer modify = departmentRepository.moveByParentIdAndSort(parentId, modifySort, sort);
        if (IResult.MSG_MODIFY_ONE == modify) {
            // 修改排序
            modify = departmentRepository.saveSortById(id, tagSort);
            if (modify == IResult.MSG_MODIFY_ONE) {
                return IResult.MSG_MODIFY_ONE;
            }
        }
        return IResult.MSG_MODIFY_ZERO;
    }


    /* 1
    @Override
    public Department saveOrUpdateDepart(Department department) {
        Department save = departmentRepository.save(department);
        return save;
    }*/
    @Override
    public String saveOrUpdateDepart(Department department, boolean modify) {

        Boolean codeLegal = validateDepartmentCode(department.getDepartmentCode());
        if (!codeLegal) {
            return "部门代码不合法！";
        }

        if (!modify) { // 新建
            if (validateDepartmentCodeRepeat(department.getDepartmentCode())) {
                return "部门代码重复！";
            }
            if (validateDepartmentName(department.getParentId(), department.getDepartmentName())) {
                return "同级部门名称重复！";
            }
            long mathUUID = UUIDUtil.getMathUUID();
            department.setId(mathUUID);
            department.setCreateTime(new Date());
            BigDecimal bigDecimal = departmentRepository.maxSortByParentId(department.getParentId());
            department.setSort(bigDecimal.add(BigDecimal.valueOf(1)));

        } else { // 修改
            Optional<Department> resultDepartment = departmentRepository.findById(department.getId());
            Boolean canModify = resultDepartment.get().getCanModify();
            department.setCanModify(canModify);
            if (!canModify) {
                return "该部门不允许修改！";
            }
            if (validateDepartmentCodeRepeatNot(department.getDepartmentCode(), department.getId())) {
                return "部门代码重复！";
            }
            if (!validateDepartmentNameNot(department.getParentId(), department.getDepartmentName(), department.getId())) {
                return "同级部门名称重复！";
            }
        }
        // 不启用则将排序置0
        if (!department.getEnable()) {
            department.setSort(BigDecimal.valueOf(0));
        }
        departmentRepository.save(department);
        return modify ? "修改部门成功" : "新建部门成功";
    }

    @Override
    public Boolean validateDepartmentCodeRepeat(String departmentCode) {
        return departmentRepository.findDepartmentCode(departmentCode) != null;
    }

    public Boolean validateDepartmentCodeRepeatNot(String departmentCode, Long id) {
        return departmentRepository.findDepartmentCodeNot(departmentCode, id) != null;
    }

    @Override
    public Integer modifyByIdAndSort(long id, boolean moveUp, BigDecimal smallTag, BigDecimal bigTag) {
        Optional<Department> department = findById(id);
        if (!department.isPresent()) {
            return 0;
        }
        BigDecimal tagSort;
        if (bigTag.equals(BigDecimal.valueOf(0))) {
            // 特例x到4（最末，最后一个）：smallTag=4的sort,bigTag=0
            tagSort = smallTag.add(BigDecimal.valueOf(1));
        } else {
            // 特例x到1（最前，第一个）：smallTag=0,bigTag=1的sort
            tagSort = smallTag.add(bigTag).divide(BigDecimal.valueOf(2));
        }
        return saveSortById(id, tagSort);
    }

    @Override
    public int modifyEnableById(long id, boolean enable) {
        if (id == 1L) {
            return -1;
        }
        Integer modify = departmentRepository.modifyEnableById(id, enable);
        return modify;
    }

    @Override
    public List<Department> findByCodeOrNameStartingWith(String search) {
        return departmentRepository.findByCodeOrNameStartingWith(search);
    }

    @Override
    public Page<String> findByParentId(long parentId, Pageable pageable) {

        return departmentRepository.findByParentId(parentId, pageable);
    }

    /**
     * 删除部门同时删除子部门
     *
     * @param departmentId
     * @return
     */
    @Transactional
    @Override
    public int deleteById(long departmentId) {
        // Optional<Department> department = findById(departmentId);
        if (departmentId == 1L) {
            return 0;
        }
        boolean exists = departmentRepository.existsById(departmentId);
        if (!exists) {
            return -1;
        }
        List<Long> children = findByParentIdIn(departmentId);
        List<Long> memberIds = memberService.findIdByDepartmentIdIn(children);
        if (!memberIds.isEmpty()) {
            return -2;
        }
        Integer integer = departmentRepository.deleteByIdIn(children);
        return integer;
    }

    @Override
    public List<Long> findByParentIdIn(long departmentId) {
        List<Long> childrenIds = new ArrayList<>();
        List<Long> searchChildrenId = new ArrayList<>();
        searchChildrenId.add(departmentId);
        while (!searchChildrenId.isEmpty()) {
            childrenIds.addAll(searchChildrenId);
            searchChildrenId = departmentRepository.findByParentIdIn(searchChildrenId);
        }
        return childrenIds;
    }

    @Override
    public Optional<Department> findByDepartmentCode(String departmentCode) {
        return departmentRepository.findByDepartmentCode(departmentCode);
    }

    @Override
    public List<Department> findByDepartmentCodeStartingWith(String search) {
        return departmentRepository.findByDepartmentCodeStartingWith(search);
    }

    @Override
    public List<Department> findByDepartmentNameStartingWith(String search) {
        return departmentRepository.findByDepartmentNameStartingWith(search);
    }

    @Transactional
    @Override
    public String modifyParentByIdAndParentId(long id, long parentId) {
        if (id == 1L) {
            return "根节点不允许修改！";
        }
        if (id == parentId) {
            return "上级部门不允许为自己！";
        }
        List<Long> children = findByParentIdIn(id);
        for (long child : children) {
            if (child == parentId) {
                return "上级部门不允许转为原下级部门！";
            }
        }
        return departmentRepository.modifyParentByIdAndParentId(id, parentId) == 0 ? "修改失败" : "修改成功";
    }

    @Override
    public Boolean validateDepartmentCode(String departmentCode) {
        return RegexUtil.LetterAndDigit(departmentCode);
    }

    @Override
    public Boolean validateDepartmentName(Long parentId, String departmentName) {
        return departmentRepository.findByParentIdAndDepartmentName(parentId, departmentName) != null;
    }

    @Override
    public Boolean validateDepartmentNameNot(Long parentId, String departmentName, Long id) {
        Boolean byParentIdAndDepartmentNameNot = departmentRepository.findByParentIdAndDepartmentNameNot(parentId, departmentName, id);
        return byParentIdAndDepartmentNameNot == null;
    }

}
