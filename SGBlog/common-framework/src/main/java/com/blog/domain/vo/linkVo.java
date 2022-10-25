package com.blog.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class linkVo {
    private Long id;


    private String name;

    private String logo;

    private String description;
    //网站地址
    private String address;

}
