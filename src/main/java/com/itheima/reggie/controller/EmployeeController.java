package com.itheima.reggie.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeServiceImpl;

    /**
     * 员工登录
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        return employeeServiceImpl.login(request, employee);
    }

    /**
     * 员工退出
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        return employeeServiceImpl.logout(request);
    }

    /**
     * 添加员工
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> addEmployee(HttpServletRequest request, @RequestBody Employee employee) {
        return employeeServiceImpl.addEmployee(request, employee);
    }

    /**
     * 员工信息分页查询
     *
     * @param page     多少页
     * @param pageSize 页数
     * @param name     查询的名字
     * @return
     */
    @GetMapping("/page")
    public R<Page> getMember(int page, int pageSize, String name) {
        return employeeServiceImpl.getMember(page, pageSize, name);
    }

    /**
     * 对员工状态进行修改 禁用/启用
     *
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> statusHandle(HttpServletRequest request, @RequestBody Employee employee) {
        log.info(employee.toString());
        return employeeServiceImpl.update(request, employee);
    }


    /**
     * 根据ID查询员工信息
     * @param id
     * @return
     */
    @GetMapping ("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        return R.success(employeeServiceImpl.getById(id));
    }



}
