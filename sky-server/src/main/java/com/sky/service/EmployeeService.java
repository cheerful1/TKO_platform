package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO 员工登录信息
     * @return 员工信息
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 保存员工信息
     *
     * @param employeeDTO 员工信息
     */
    void saveEmployee(EmployeeDTO employeeDTO);

    /**
     * 获取所有员工信息
     *
     * @param employeePageQuery 分页查询条件
     * @return
     */
    PageResult getAllEmployeeByPage(EmployeePageQueryDTO employeePageQuery);

    /**
     * 禁用或者启用员工
     * @param id 员工id
     * @param status 状态 0：禁用 1：启用
     */
    void startOrStopEmployee(Long id, Integer status);


    /**
     * 根据id查询员工信息
     * @param id 员工id
     * @return 员工信息
     */
    Employee selectById(Long id);

    /**
     * 更新员工信息
     * @param employeedto 员工信息
     *
     */
    void updateEmployee(EmployeeDTO employeedto);
}
