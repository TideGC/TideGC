package com.example.employee.web.controller;

import com.example.employee.entity.Department;
import com.example.employee.entity.Employee;
import com.example.employee.http.DepartmentFeignClient;
import com.example.employee.util.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final DepartmentFeignClient departmentFeignClient;

    @PreAuthorize("hasAuthority('admin:write')")
    @GetMapping("/employee/get")
    public ResponseResult<Employee> getEmployee(@RequestParam("id") Long id) {
        Employee employee = new Employee(1L, "tommy", 10000, 2000, null);

        // 发起 http 请求
        ResponseResult<Department> responseResult = departmentFeignClient.getDepartment(1L);
        employee.setDepartment(responseResult.getData());

        ResponseResult<Employee> result = new ResponseResult<>(200, "success", employee);

        return result ;
    }

}
