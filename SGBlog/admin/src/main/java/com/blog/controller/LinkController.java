package com.blog.controller;

import com.blog.domain.ResponseResult;
import com.blog.domain.entity.Category;
import com.blog.domain.entity.Link;
import com.blog.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Autowired
    private LinkService linkService;


    /**
     * 获取友链列表
     * @param link
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public ResponseResult list(Link link,Integer pageNum,Integer pageSize){
        return linkService.selectLinkPage(link,pageNum,pageSize);
    }

    /**
     * 新增友链
     * @param link
     * @return
     */
    @PostMapping
    public ResponseResult add(@RequestBody Link link){
        return linkService.add(link);
    }

    /**
     * 获取友链信息
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public ResponseResult getInfo(@PathVariable(value = "id")Long id){
        return linkService.getInfo(id);
    }

    /**
     * 修改友链信息
     * @param link
     * @return
     */
    @PutMapping
    public ResponseResult edit(@RequestBody Link link){
        return linkService.edit(link);
    }

    @PutMapping("/changeLinkStatus")
    public ResponseResult changeLinkStatus(@RequestBody Link link){
        return linkService.changeLinkStatus(link);
    }

    /**
     * 删除友链
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id){
        linkService.removeById(id);
        return ResponseResult.okResult();
    }
}
