package com.blog.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "修改标签dto")
public class EditTagDto {
    private Long id;
    //标签名
    private String name;

    //备注
    private String remark;
}
