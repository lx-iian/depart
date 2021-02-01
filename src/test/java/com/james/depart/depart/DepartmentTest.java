package com.james.depart.depart;

import com.james.depart.dao.DepartmentRepository;
import com.james.depart.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author james
 * @description
 * @date 2021-01-05
 */
@AutoConfigureMockMvc
@SpringBootTest
public class DepartmentTest {

    @Resource
    private MockMvc mockMvc;

    final DepartmentRepository departmentRepository;

    final MemberService memberService;

    public DepartmentTest(DepartmentRepository departmentRepository, MemberService memberService) {
        this.departmentRepository = departmentRepository;
        this.memberService = memberService;
    }

    @Test
    public void getSortCountByParentId() throws Exception {


        String result = mockMvc.perform(
                get("/testGetJson")
                        .content("{\"id\":1,\"name\":\"张三\",\"sex\":\"男\"}")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println("result"+result);

    }

    @Test
    public void findByIdAndLtThisSortMaxTest() {
        Long id = 111L;
        BigDecimal sort = BigDecimal.valueOf(113);
        Assert.isNull(id, "aa");
       /* Map byIdAndLtThisSortMax = departmentRepository.findByIdAndLtThisSortMax(id, sort);
        System.out.println(byIdAndLtThisSortMax);*/
    }
}
