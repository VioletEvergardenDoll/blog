package com.blog.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "修改角色状态dto")
public class ChangeRoleStatusDto {

    //角色ID@TableId
    private Long id;
    //角色状态（0正常 1停用）
    private String status;
}
