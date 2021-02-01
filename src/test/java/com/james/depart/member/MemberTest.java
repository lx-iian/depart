package com.james.depart.member;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.james.depart.dao.MemberRepository;
import com.james.depart.domain.Member;
import com.james.depart.domain.VO.DepartmentVO;
import com.james.depart.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author james
 * @description
 * @date 2021-01-04
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MemberTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void findByIdTest() {
        Optional<Member> byId = memberRepository.findById(1L);
        System.out.println(byId);
    }

    @Test
    public void idTest() {
        String s = "1.00000000000000088817841970012523233890533447265625";
        System.out.println(s.length());
    }

    @Test
    public void change2BigDecimalTest() {
        String s = "1.00000000000000088817841970012523233890533447265625";
        BigDecimal b1 = new BigDecimal(s);
        System.out.println(b1);
    }


    @Test
    public void halfTest() {
        BigDecimal b1 = new BigDecimal("1");
        BigDecimal b2 = new BigDecimal("2");
        BigDecimal b3 = new BigDecimal("3");
        BigDecimal b4 = new BigDecimal("4");

        for (int i = 0; i < 50; i++) {
            b3 = b2;
            b2 = b1.add(b2).divide(new BigDecimal("2"));

            System.out.println("b2=" + b2 + " b3=" + b3);
        }

    }

    @Test
    public void doubleTest() {
        BigDecimal b1 = new BigDecimal("1");
        BigDecimal b2 = new BigDecimal("2");
        BigDecimal b3 = new BigDecimal("3");
        BigDecimal b4 = new BigDecimal("4");

        for (int i = 0; i < 50; i++) {
            b1 = b2;
            b2 = b2.add(b3).divide(new BigDecimal("2"));

            System.out.println("b2=" + b2 + " b1=" + b1);
        }

    }

    @Test
    public void MemberToJSON() throws JsonProcessingException {
        String a = "Optional[Department{id=108, departmentCode='109', departmentName='null', parentId=null, level=null, sort=108, createTime=null, modifyTime=null, canModify=true, comment='', isEnable=true}]";
        String substring = a.substring(9, a.length() - 1);
        System.out.println(substring);

        String c = "{id=108, departmentCode='109', departmentName='null', parentId=null, level=null, sort=108, createTime=null, modifyTime=null, canModify=true, comment='', isEnable=true}";
        String b = "{\n" +
                "  \"id\": 5377966949807939126,\n" +
                "  \"departmentName\": \"123456\",\n" +
                "  \"departmentCode\": \"123456123\",\n" +
                "  \"parentId\": 121,\n" +
                "  \"level\": 1,\n" +
                "  \"sort\": 1,\n" +
                "  \"canModify\": 1,\n" +
                "  \"comment\": null\n" +
                "}";

        String listTxt = JSONArray.toJSONString("{id=108, departmentCode='109', departmentName='null', parentId=null, level=null, sort=108, createTime=null, modifyTime=null, canModify=true, comment='', isEnable=true}");
        DepartmentVO DepartmentVO123 = JSONObject.parseObject(listTxt, DepartmentVO.class);
        System.out.println(DepartmentVO123.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        DepartmentVO departmentVO = objectMapper.readValue(b, DepartmentVO.class);
        System.out.println(departmentVO);
       /* ObjectMapper objectMapper = new ObjectMapper();
        DepartmentVO departmentVO = objectMapper.readValue(a, DepartmentVO.class);
        System.out.println(departmentVO);*/

    }
}
