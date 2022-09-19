package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


//public interface EmployeeService extends IService<Employee> {
//}
public interface EmployeeService extends IService<Employee> {
//    public R<String> delSession(HttpServletRequest request);
    R<Employee> login(HttpServletRequest request, @RequestBody Employee employee);

     R<String> logout(HttpServletRequest request);

    public R<String> addEmployee(HttpServletRequest request, @RequestBody Employee employee);

    List<Employee> selectAll();

    R<Page> getMember(int page, int pageSize, String name);

    public R<String> update(HttpServletRequest request,@RequestBody Employee employee);


}