package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.domain.ResponseResult;
import com.blog.domain.entity.Category;
import com.blog.domain.vo.CategoryVo;

import java.util.List;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2022-04-22 18:12:13
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult listAllCategory();

    ResponseResult selectCategoryPage(Category category, Integer pageNum, Integer pageSize);

    ResponseResult add(Category category);

    ResponseResult getInfo(Long id);

    ResponseResult edit(Category category);


//    List<CategoryVo> listAllCategory();
}
