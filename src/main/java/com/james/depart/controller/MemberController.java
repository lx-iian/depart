package com.james.depart.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.james.depart.domain.Member;
import com.james.depart.service.MemberService;
import com.james.depart.utils.IResult;
import com.james.depart.utils.MyResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Api("员工API")
@RequestMapping("v1/member")
@RestController
@ResponseBody
public class MemberController {

    final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @ApiOperation("增加、修改员工信息")
    @ApiImplicitParam(
            name = "username", value = "用户的用户名-String", required = true, paramType="body"
    )
    @PatchMapping("save")
    public MyResult save(@RequestBody String content) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(content);
        boolean modify = jsonNode.path("modify").asBoolean();
        /*ObjectNode object = (ObjectNode) jsonNode;
        object.remove("modify");*/
        Member member = objectMapper.readValue(jsonNode.toString(), Member.class);
        int success = memberService.save(member, modify);
        return MyResult.success((modify ? "修改员工信息" : "新增员工信息") + (success == 1 ? "成功" : "失败"));
    }

    @ApiOperation("查看员工信息")
    @PostMapping("id")
    public MyResult getById(@RequestBody String content) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(content);
        long id = jsonNode.path("id").asLong();
        Optional<Member> member = memberService.findById(id);
        return MyResult.success(member);

    }

    @ApiOperation("根据部门ID获取员工信息")
    @PostMapping("department_id")
    public MyResult findByDepartmentId(@RequestBody String content) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(content);
        long departmentId = jsonNode.path("departmentId").asLong();
        int pageNumber = jsonNode.path("pageNumber").asInt();
        int pageSize = jsonNode.path("pageSize").asInt();
        if (pageNumber <= 0) {
            pageNumber = 1;
        }
        if (pageSize <= 1) {
            pageSize = 1;
        }
        Sort sort = Sort.by(Sort.Direction.ASC, "sort");
        // PageRequest中page从0开始，将用户习惯（从1开始）转换传入
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        Page<String> showMemberByDepartmentId = memberService.findByDepartmentId(departmentId, pageable);
        return MyResult.success(showMemberByDepartmentId);
    }

    @ApiOperation("根据员工ID交换相邻两个排序")
    @PatchMapping("move/id")
    public MyResult moveById(@RequestBody String content) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(content);
        long id = jsonNode.path("id").asLong();
        int tagSort = jsonNode.path("tagSort").asInt();
        Integer moveResult = memberService.moveById(id, tagSort);

        if (moveResult == IResult.MSG_ALREADY_FIRST) {
            return MyResult.error(IResult.MSG_FIRST);
        } else if (moveResult == IResult.MSG_ALREADY_LAST) {
            return MyResult.error(IResult.MSG_LAST);
        }
        return MyResult.success(String.format("修改%s条数据成功", moveResult));
    }

    @ApiOperation("根据员工ID和移动个数修改排序，1->3 个数为2，3->1 个数为-2")
    @PatchMapping("modify/sort")
    public MyResult modifyByIdAndSort(@RequestBody String content) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(content);
        long id = jsonNode.path("id").asLong();
        int modifyNum = jsonNode.path("modifyNum").asInt();
        Integer moveResult = memberService.modifyByIdAndSort(id, modifyNum);

        switch (moveResult) {
            case IResult.MSG_FAIL:
                return MyResult.error(IResult.MSG_MODIFY_FAIL);
            case IResult.MSG_ALREADY_FIRST:
                return MyResult.error(IResult.MSG_FIRST);
            case IResult.MSG_ALREADY_LAST:
                return MyResult.error(IResult.MSG_LAST);
        }
        return MyResult.success(String.format("修改%s条数据成功", moveResult));
    }

    @ApiOperation("搜索员工")
    @PostMapping("search")
    public MyResult searchByCodeOrName(@RequestBody String content) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(content);
        String search = jsonNode.path("search").asText();
        return MyResult.success(memberService.findByCodeOrNameStartingWith(search));
    }

    @ApiOperation("根据ID删除员工")
    @DeleteMapping("id")
    public MyResult deleteById(@RequestBody String id) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(id);
        long departmentId = jsonNode.path("id").asLong();

        int result = memberService.deleteById(departmentId);
        return result > 0 ? MyResult.success(IResult.MSG_DELETE_SUCCESS) : MyResult.error(result, IResult.MSG_DELETE_FAIL);
    }

}
