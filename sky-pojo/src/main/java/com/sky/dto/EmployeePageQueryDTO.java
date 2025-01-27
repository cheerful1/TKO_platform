package com.sky.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeePageQueryDTO implements Serializable {

    //员工姓名
    @ApiModelProperty(value = "员工姓名")
    private String name;

    //页码
    @ApiModelProperty(value = "页码")
    private int page;

    //每页显示记录数
    @ApiModelProperty(value = "每页显示记录数")
    private int pageSize;

}
