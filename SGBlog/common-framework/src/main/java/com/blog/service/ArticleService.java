package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.domain.ResponseResult;
import com.blog.domain.dto.AddArticleDto;
import com.blog.domain.dto.ArticleDto;
import com.blog.domain.entity.Article;
import com.blog.domain.vo.ArticleVo;

public interface ArticleService extends IService<Article> {


    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(long id);

    ResponseResult addArticle(AddArticleDto articleDto);

    ResponseResult selectArticlePage(Integer pageNum, Integer pageSize, ArticleVo articleVo);

    ResponseResult getArticleInfo(Long id);


    ResponseResult editArticle(ArticleDto articleDto);

    ResponseResult delete(Long id);
}
