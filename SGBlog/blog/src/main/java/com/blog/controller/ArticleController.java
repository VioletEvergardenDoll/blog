package com.blog.controller;


import com.blog.domain.ResponseResult;
import com.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * controller方法响应都在响应体当中
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

/*
//    请求映射规则
    @GetMapping("/list")
    public List<Article> test(){
        return articleService.list();
    }
*/

    /**
     * 热门文章
     * @return
     */
    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList(){
        //查询热门文章
        ResponseResult result = articleService.hotArticleList();
        return result;
    }

    /**
     * 列表
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @return
     */
    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum,Integer pageSize,Long categoryId){
        return articleService.articleList(pageNum,pageSize,categoryId);

    }

    /**
     * 详情  路径形式参数要加注解标识@PathVariable("ID")
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){

        return articleService.getArticleDetail(id);
    }

    /**
     * 更新浏览量     @PathVariable 将id获取过来
     * @param id
     * @return
     */
    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") long id){
        return articleService.updateViewCount(id);
    }

}
