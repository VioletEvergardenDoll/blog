package com.blog.controller;

import com.blog.domain.ResponseResult;
import com.blog.domain.dto.AddTagDto;
import com.blog.domain.dto.EditTagDto;
import com.blog.domain.dto.TagListDto;
import com.blog.domain.entity.Tag;
import com.blog.domain.vo.PageVo;
import com.blog.service.TagService;
import com.blog.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 标签列表
     * @param pageNum
     * @param pageSize
     * @param tagListDto
     * @return
     */
    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }

    /**
     * 添加标签
     * @param addTagDto
     * @return
     */
    @PostMapping
    public ResponseResult addTag(@RequestBody AddTagDto addTagDto){
        Tag tag = BeanCopyUtils.copyBean(addTagDto,Tag.class);
        return tagService.addTag(tag);
    }


    /**
     * 获取标签信息 然后通过信息修改标签
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getTagInfo(@PathVariable(value = "id")Long id){
        return tagService.getTagInfo(id);
    }

    /**
     * 修改标签
     * @param tagDto
     * @return
     */
    @PutMapping
    public ResponseResult editTag(@RequestBody EditTagDto tagDto){
        Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);
        return tagService.editTag(tag);
    }

    /**
     * 删除标签
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult delectTag(@PathVariable Long id){
        return tagService.delectTag(id);
    }

    /**
     * 查询所有标签
     * @return
     */
    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        return tagService.listAllTag();
    }
}
