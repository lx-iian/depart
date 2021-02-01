package com.james.depart;

import com.james.depart.dao.DepartmentRepository;
import com.james.depart.domain.Department;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class DepartApplicationTests {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    void contextLoads() {
    }

    /**
     * 添加用户
     */
    @Test
    // 在测试类对于事务提交方式默认的是回滚。
    // @Transactional
    public void saveOrUpdateDepartmentTest(){
        Department department = new Department();
        department.setId(111L);
        department.setLevel(1);
        department.setDepartmentName("111测试");
        Department save = this.departmentRepository.save(department);
        System.out.println("save = " + save);
    }

    @Test
    public void getDepartmentTest(){
        Department one = this.departmentRepository.getOne(11L);
        System.out.println("De=" + one);
    }

    @Test
    public void getByIdOTest(){
        Optional<Department> byId = departmentRepository.findById(1L);
        System.out.println("getByIdTest = " + byId);
    }

}
