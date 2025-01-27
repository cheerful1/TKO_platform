package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "员工登录", notes = "登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        // 这里的claims中存放的是员工的一些信息，比如id、username、name等
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value = "员工退出", notes = "退出登录")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 保存
     *
     * @return
     */
    @PostMapping
    @ApiOperation(value = "保存员工", notes = "保存员工")
    public Result<String> saveEmployee(@RequestBody EmployeeDTO  employeeDTO) {
        //{}为占位符，不用填
        log.info("保存员工：{}", employeeDTO);
        employeeService.saveEmployee(employeeDTO);
        return Result.success();
    }

    /**
     * 分页查询员工
     * @return         员工列表
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询员工", notes = "分页查询员工")
    public Result<PageResult>getAllEmployeeByPage(EmployeePageQueryDTO employeePageQuery ) {
        log.info("分页查询员工：{}", employeePageQuery);
        PageResult pageResult = employeeService.getAllEmployeeByPage(employeePageQuery);
        return Result.success(pageResult);
    }

    @PostMapping("/status/{status}")
    public Result startOrStopEmployee(Long id, @PathVariable("status") Integer status) {
        log.info("修改员工状态：id={}, status={}", id, status);
        employeeService.startOrStopEmployee(id, status);
        return Result.success();
    }
}
