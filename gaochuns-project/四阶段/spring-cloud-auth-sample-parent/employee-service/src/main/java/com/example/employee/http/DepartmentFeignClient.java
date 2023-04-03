package com.example.employee.http;

import com.example.employee.entity.Department;
import com.example.employee.util.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("department-service")
public interface DepartmentFeignClient {

    @GetMapping("/department/get")
    public ResponseResult<Department> getDepartment(@RequestParam("id") Long id);

}
