package com.james.depart.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.james.depart.domain.Department;
import com.james.depart.service.DepartmentService;
import com.james.depart.utils.IResult;
import com.james.depart.utils.JSONUtil;
import com.james.depart.utils.MyResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.io.DataInput;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

/**
 * 部门管理
 *
 * @author james
 * @date 2020-12-28
 */
@Api("部门API")
@RestController
@ResponseBody
@RequestMapping("v1/department")
public class DepartmentController {

    final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @ApiOperation("获取根部门信息")
    @PostMapping("list/root")
    public MyResult listRoot() {
        Optional<Department> root = departmentService.findById(1L);
        return MyResult.success(root);
    }

    @ApiOperation("获取根部门下子部门信息")
    @PostMapping("list/root/children")
    public MyResult listRootChildren() {
        Page<String> rootChildren = departmentService.findByParentId(1L, null);
        return MyResult.success(rootChildren);
    }

/*    @ApiOperation("获取所有部门")
    @PostMapping("list/all")
    public MyResult listAll() {
        // List<Department> departments = departmentService.listAll();
        return MyResult.success(departmentService.listAll());
    }*/

    @ApiOperation("根据ID获取部门信息")
    @PostMapping("id")
    public MyResult getById(@RequestBody String content) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(content);
        long id = jsonNode.path("id").asLong();
        return MyResult.success(departmentService.getDepartmentAndTotalMembers(id));
    }

    @ApiOperation("更改此部门排位 ps:1、2、3、4 如4移动到3前，则" +
            "smallTag=2的sort,bigTag=3的sort ，1移动到3后，则smallTag=3的sort,bigTag=4的sort." +
            "特例x到1（最前，第一个）：smallTag=0,bigTag=1的sort，特例x到4（最末，最后一个）：smallTag=4的sort,bigTag=0")
    @PatchMapping("modify/sort")
    public MyResult modifyByIdAndSort(@RequestBody String content) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(content);
        long id = jsonNode.path("id").asLong();
        boolean moveUp = jsonNode.path("moveUp").asBoolean();
        String smallTagStr = jsonNode.path("smallTag").asText();
        Assert.notNull(smallTagStr, "smallTag不存在");
        String bigTagStr = jsonNode.path("bigTag").asText();
        Assert.notNull(bigTagStr, "bigTag不存在");
        BigDecimal smallTag = new BigDecimal(smallTagStr);
        BigDecimal bigTag = new BigDecimal(bigTagStr);
        Integer moveResult = departmentService.modifyByIdAndSort(id, moveUp, smallTag, bigTag);
        if (moveResult == IResult.MSG_ALREADY_FIRST) {
            return MyResult.error(IResult.MSG_FIRST);
        } else if (moveResult == IResult.MSG_ALREADY_LAST) {
            return MyResult.error(IResult.MSG_LAST);
        }
        return MyResult.success(String.format("修改%s条数据成功", moveResult));
    }

    @ApiOperation("部门排序上调、下调一位")
    @PatchMapping("modify/one_sort")
    public MyResult modifyOneSort(@RequestBody String content) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(content);
        long id = jsonNode.path("id").asLong();
        boolean moveUp = jsonNode.path("moveUp").asBoolean();
        Integer moveResult = departmentService.modifyOneSort(id, moveUp);
        if (moveResult == IResult.MSG_ALREADY_FIRST) {
            return MyResult.error(IResult.MSG_FIRST);
        } else if (moveResult == IResult.MSG_ALREADY_LAST) {
            return MyResult.error(IResult.MSG_LAST);
        }
        return MyResult.success(String.format("修改%s条数据成功", moveResult));
    }

    @ApiOperation("根据ID修改上级部门")
    @PatchMapping("modify/id")
    public MyResult modifyParentByIdAndParentId(@RequestBody String content) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(content);
        long id = jsonNode.path("id").asLong();
        long parentId = jsonNode.path("parentById").asLong();
        String s = departmentService.modifyParentByIdAndParentId(id, parentId);
        return MyResult.success(s);
    }

    @ApiOperation("新增、修改部门")
    @PutMapping("save/department")
    public MyResult saveOrUpdateDepart(@RequestBody String content) throws JsonProcessingException {
        // jackson
/*        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(content);
         // ObjectNode object = (ObjectNode) jsonNode;
        // object.remove("modify");
        // Department department = objectMapper.readValue(jsonNode.toString(), Department.class);*/
        Department department = JSONUtil.objectMapper.readValue(content, Department.class);
        JsonNode jsonNode = JSONUtil.getJsonNode(content);
        boolean modify = jsonNode.path("modify").asBoolean();


        // fastjson
        /*Byte modify = JSON.parseObject(content).getByte("modify");
        //jsonObject.remove("modify", content);
        Department department = JSON.parseObject(content, Department.class);*/
        String modifyMSG = departmentService.saveOrUpdateDepart(department, modify);
        return MyResult.success(modifyMSG);
    }

    @ApiOperation(value = "查询子部门信息", notes = "子部门")
    @PostMapping("children")
    public MyResult listChildren(@RequestBody String department) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(department);
        long parentId = jsonNode.path("parentId").asLong();
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

        Page<String> showDepartmentByParentIdAndLevel = departmentService.findByParentId(parentId, pageable);
        /*if (moveResult == IResult.MSG_ALREADY_FIRST) {
            return MyResult.error(IResult.MSG_FIRST);
        } else if (moveResult == IResult.MSG_ALREADY_LAST) {
            return MyResult.error(IResult.MSG_LAST);
        }*/
        return MyResult.success(showDepartmentByParentIdAndLevel);
    }

    @ApiOperation("启用、停用部门")
    @PatchMapping("modify/enable")
    public MyResult modifyEnableById(@RequestBody String content) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(content);
        long id = jsonNode.path("id").asLong();
        boolean enable = jsonNode.path("enable").asBoolean();
        int result = departmentService.modifyEnableById(id, enable);
        return result > 0 ? MyResult.success(IResult.MSG_MODIFY_SUCCESS) : MyResult.error(result, IResult.MSG_MODIFY_FAIL);
    }

    @ApiOperation("搜索部门")
    @PostMapping("search")
    public MyResult searchByCodeOrName(@RequestBody String content) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(content);
        String search = jsonNode.path("search").asText();
        return MyResult.success(departmentService.findByCodeOrNameStartingWith(search));
    }

    @ApiOperation("根据ID删除部门")
    @DeleteMapping("id")
    @ApiImplicitParam(name = "id", value = "部门ID", paramType = "body", dataType = "String")
    public MyResult deleteById(@RequestBody String id) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(id);
        long departmentId = jsonNode.path("id").asLong();

        int result = departmentService.deleteById(departmentId);
        return result > 0 ? MyResult.success(IResult.MSG_DELETE_SUCCESS) : MyResult.error(result, IResult.MSG_DELETE_FAIL);
    }
}
