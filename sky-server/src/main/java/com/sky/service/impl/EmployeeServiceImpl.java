package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;



import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    @Override
    public void saveEmployee(EmployeeDTO employeeDTO) {
        //1、接收参数
        Employee employee = new Employee();
        //2、对象属性拷贝，从employeeDTO拷贝到employee，属性名必须一致
        BeanUtils.copyProperties(employeeDTO, employee);
        //3、设置账号的状态，默认正常，由管理员手动禁用，使用常量类复用
        employee.setStatus(StatusConstant.ENABLE);
        //4、密码加密，设置密码，默认密码是123456
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        //5、设置当前记录的创建时间和更新时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //6、设置当前记录的创建者id和更新者id，使用BaseContext获取当前线程的id
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());
        //7、保存员工信息
        employeeMapper.insert(employee);
    }


    /**
     * 获取所有员工信息，分页查询
     *
     * @param employeePageQuery 分页查询条件
     * @return
     */
    @Override
    public PageResult getAllEmployeeByPage(EmployeePageQueryDTO employeePageQuery) {
        //1、分页查询 这里使用PageHelper分页插件，底层是使用了thredLocal，所以不需要自己手动设置线程变量
        PageHelper.startPage(employeePageQuery.getPage(), employeePageQuery.getPageSize());
        employeePageQuery.setName(StringUtils.trimToEmpty(employeePageQuery.getName()));
        Page<Employee> page = employeeMapper.selectAllByPage(employeePageQuery);
        long total = page.getTotal();
        List<Employee> records = page.getResult();
        return new PageResult(total, records);
    }

    /**
     * 禁用或者启用员工
     * @param id 员工id
     * @param status 状态 0：禁用 1：启用
     */
    @Override
    public void startOrStopEmployee(Long id, Integer status) {
        Employee employee = new Employee().builder()
                .id(id).status(status)
                .build();
        employeeMapper.update(employee);
    }

    @Override
    public Employee selectById(Long id) {
        return employeeMapper.selectById(id);
    }

    /**
     * 更新员工信息
     * @param employeedto 员工信息
     */
    @Override
    public void updateEmployee(EmployeeDTO employeedto) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeedto, employee);
        employee.setPassword("******");
        //设置更新时间和更新者id
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.update(employee);
    }


}
