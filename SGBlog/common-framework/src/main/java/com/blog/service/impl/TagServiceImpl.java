package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.domain.ResponseResult;
import com.blog.domain.dto.TagListDto;
import com.blog.domain.entity.Tag;
import com.blog.domain.vo.PageVo;
import com.blog.domain.vo.TagVo;
import com.blog.enums.AppHttpCodeEnum;
import com.blog.exception.SystemException;
import com.blog.mapper.TagMapper;
import com.blog.service.TagService;
import com.blog.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2022-09-05 19:14:00
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    /**
     * 标签列表
     * @param pageNum
     * @param pageSize
     * @param tagListDto
     * @return
     */
    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {

        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        //StringUtils.hasText(tagListDto.getName()) 判断 tagListDto中Name是否有值。有值返回true，执行后边语句，没有值返回false，不继续执行
        queryWrapper.eq(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
        queryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());

        //分页查询
        Page<Tag> page =new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,queryWrapper);

        //封装数据返回
        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    /**
     * 添加标签
     * @param tag
     * @return
     */
    @Override
    public ResponseResult addTag(Tag tag) {
        if (!StringUtils.hasText(tag.getName())){
            throw new SystemException(AppHttpCodeEnum.TAGNAME_NOT_NULL);
        }
        save(tag);
        return ResponseResult.okResult();
    }

    /**
     * 逻辑删除标签
     * @param id
     * @return
     */
    @Override
    public ResponseResult delectTag(Long id) {
        //按id删除
        removeById(id);
        return ResponseResult.okResult();
    }

    /**
     * 获取标签信息
     * @param id
     * @return
     */
    @Override
    public ResponseResult getTagInfo(Long id) {
        Tag tag = getById(id);
        return ResponseResult.okResult(tag);
    }

    /**
     * 修改标签
     * @param tag
     * @return
     */
    @Override
    public ResponseResult editTag(Tag tag) {
        updateById(tag);
        return ResponseResult.okResult();
    }

    /**
     * 查询所有标签
     * @return
     */
    @Override
    public ResponseResult listAllTag() {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        //select 查询id和name字段
        wrapper.select(Tag::getId,Tag::getName);
        List<Tag> list = list(wrapper);
        List<TagVo> tagVOS = BeanCopyUtils.copyBeanList(list, TagVo.class);
        return ResponseResult.okResult(tagVOS);
    }


}
