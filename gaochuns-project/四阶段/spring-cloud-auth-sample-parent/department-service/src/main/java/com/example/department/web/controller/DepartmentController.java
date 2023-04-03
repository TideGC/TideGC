package com.example.department.web.controller;

import com.example.department.entity.Department;
import com.example.department.util.ResponseResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DepartmentController {

    @PreAuthorize("hasAuthority('admin:write')")
    @GetMapping("/hello-world")
    public String adminCanDo() {
        System.out.println("如果当前登录用户具有 admin:write 权限，你才应该看到这这行文字。");
        return "hello world";
    }

    @PreAuthorize("hasAuthority('admin:write')")
    @GetMapping("/department/get")
    public ResponseResult<Department> getDepartment(@RequestParam("id") Long id) {

        Department department = new Department(1L, "hello", "world");
        ResponseResult<Department> success = new ResponseResult<>(200, "success", department);

        return success;
    }
}
