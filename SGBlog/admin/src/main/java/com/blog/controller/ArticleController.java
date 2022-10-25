package com.blog.controller;

import com.blog.domain.ResponseResult;
import com.blog.domain.dto.AddArticleDto;

import com.blog.domain.dto.ArticleDto;
import com.blog.domain.vo.ArticleVo;
import com.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 文章列表
     * @param pageNum
     * @param pageSize
     * @param articleVo
     * @return
     */
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, ArticleVo articleVo){
        return articleService.selectArticlePage(pageNum,pageSize,articleVo);
    }



    /**
     * 添加博文
     * @param articleDto
     * @return
     */
    @PostMapping
    public ResponseResult addArticle(@RequestBody AddArticleDto articleDto){
        return articleService.addArticle(articleDto);
    }

    /**
     * 查询文章详情 获取博文信息 然后通过信息修改博文
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public ResponseResult getArticleInfo(@PathVariable(value = "id")Long id){
        return articleService.getArticleInfo(id);
    }

    /**
     * 修改文章
     * @param articleDto
     * @return
     */
    @PutMapping
    public ResponseResult editArticle(@RequestBody ArticleDto articleDto){
        return articleService.editArticle(articleDto);
    }

    /**
     * 删除文章
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id){
        return articleService.delete(id);
    }
}
