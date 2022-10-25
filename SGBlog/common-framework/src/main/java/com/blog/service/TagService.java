package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.domain.ResponseResult;
import com.blog.domain.dto.EditTagDto;
import com.blog.domain.dto.TagListDto;
import com.blog.domain.entity.Tag;
import com.blog.domain.vo.PageVo;
import org.apache.poi.ss.formula.functions.T;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2022-09-05 19:13:59
 */
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(Tag tag);

    ResponseResult delectTag(Long id);

    ResponseResult editTag(Tag tag);

    ResponseResult getTagInfo(Long id);

    ResponseResult listAllTag();
}
