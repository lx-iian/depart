package com.james.depart.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.james.depart.service.DepartmentService;
import com.james.depart.utils.MyResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/**
 * @author james
 * @description
 * @date 2021-01-05
 */
@Api("管理员API")
@RestController
@ResponseBody
@RequestMapping("v1/admin")
public class AdminController {

    final DepartmentService departmentService;

    public AdminController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @ApiOperation("设置部门是否可修改")
    @PutMapping("can_modify")
    public MyResult canModify(@RequestBody String content) {

        return MyResult.success();
    }

}
