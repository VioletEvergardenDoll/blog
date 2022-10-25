package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.constants.SystemConstants;
import com.blog.domain.ResponseResult;
import com.blog.domain.dto.AddArticleDto;
import com.blog.domain.dto.ArticleDto;
import com.blog.domain.entity.Article;
import com.blog.domain.entity.ArticleTag;
import com.blog.domain.entity.Category;
import com.blog.domain.vo.*;
import com.blog.mapper.ArticleMapper;
import com.blog.service.ArticleService;
import com.blog.service.ArticleTagService;
import com.blog.service.CategoryService;
import com.blog.utils.BeanCopyUtils;
import com.blog.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("artilceService")

//ServiceImpl<泛型，实体类>

public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleTagService articleTagService;

    /**
     * 查询热门文章实现
     * @return
     */
    @Override
    public ResponseResult hotArticleList() {

        //封装查询条件 封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();

        //必须是正式文章,   Article::getStatus方法引用方式
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);

        //按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);

        //最多只查询十条
        Page<Article> page = new Page<>(1, 10);
        page(page, queryWrapper);

        //获取查询列表数据
        List<Article> articles = page.getRecords();

        //bean拷贝 BeanUtils.copyProperties(原数据对象，目标对象)
/*        List<HotArticleVo> articleVos = new ArrayList<>();
        for (Article article : articles) {
            HotArticleVo vo = new HotArticleVo();
            BeanUtils.copyProperties(article,vo);
            articleVos.add(vo);
        }*/
        List<HotArticleVo> vs = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);

        //封装成ResponseResult返回
        return ResponseResult.okResult(vs);
    }

    /**
     * 文章列表
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @return
     */
    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件  指定泛型
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //如果 有categoryId 就要 查询时和传入的相同
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId) && categoryId > 0, Article::getCategoryId, categoryId);
        //状态是正式发布的
        lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //对isTop进行降序
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);
        //分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, lambdaQueryWrapper);


        List<Article> articles = page.getRecords();
        //查询categoryName
        articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());

/*        articles.stream()
                .map(new Function<Article, Article>() {
                    @Override
                    public Article apply(Article article) {
                        //获取分类id，查询分类信息，获取分类名称
                        //把分类名称设置给article
                        return article.setCategoryName(categoryService.getById(article.getCategoryId()).getName());
                    }
                })*/
        //categoryId去查询categoryName进行设置
/*        for (Article article : articles) {
            Category category = categoryService.getById(article.getCategoryId());
            article.setCategoryName(category.getName());

        }*/

        //封装查询结果 VO
        List<ArticleListVo> articleListVo = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);


        PageVo pageVo = new PageVo(articleListVo, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 文章详情
     * @param id
     * @return
     */
    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);

        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());

        //转换成VO
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);

        //根据分类id查询分类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if (category != null) {
            articleDetailVo.setCategoryName(category.getName());

        }
        //封装响应返回
        return ResponseResult.okResult(articleDetailVo);
    }


    /**
     * 更新浏览量
     * @param id
     * @return
     */
    @Override
    public ResponseResult updateViewCount(long id) {
        //更新redis中对应的浏览量
        redisCache.incrementCacheMapValue("article:viewCount", String.valueOf(id), 1);
        return ResponseResult.okResult();
    }

    /**
     * 添加文章
     * @param articleDto
     * @return
     */
    @Override
    //保证事物操作，方便回滚
    @Transactional
    public ResponseResult addArticle(AddArticleDto articleDto) {
        //添加 博客
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);

        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        //添加 博客和标签的关联
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    /**
     * 后台文章列表
     * @param pageNum
     * @param pageSize
     * @param articleVo
     * @return
     */
    @Override
    public ResponseResult selectArticlePage(Integer pageNum, Integer pageSize, ArticleVo articleVo) {
        //分页查询
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //StringUtils.hasText(tagListDto.getName()) 判断 tagListDto中Name是否有值。有值返回true，执行后边语句，没有值返回false，不继续执行
        queryWrapper.like(StringUtils.hasText(articleVo.getTitle()), Article::getTitle,articleVo.getTitle());
        queryWrapper.like(StringUtils.hasText(articleVo.getSummary()),Article::getSummary,articleVo.getSummary());

        Page<Article> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,queryWrapper);

        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    /**
     *获取文章信息
     * @param id
     * @return
     */
    @Override
    public ResponseResult getArticleInfo(Long id) {
        Article article = getById(id);
        //获取关联标签
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId,article.getId());
        List<ArticleTag> articleTags = articleTagService.list(articleTagLambdaQueryWrapper);
        List<Long> tags = articleTags.stream()
                .map(articleTag -> articleTag.getTagId())
                .collect(Collectors.toList());

        ArticleVo articleVo = BeanCopyUtils.copyBean(article, ArticleVo.class);
        articleVo.setTag(tags);

        return ResponseResult.okResult(articleVo);
    }

    /**
     * 修改文章
     * @param articleDto
     * @return
     */
    @Override
    public ResponseResult editArticle(ArticleDto articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        //更新博客信息
        updateById(article);
        //删除原有的 标签和博客的关联
        LambdaQueryWrapper<ArticleTag> articleTaglambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTaglambdaQueryWrapper.eq(ArticleTag::getArticleId,article.getId());
        articleTagService.remove(articleTaglambdaQueryWrapper);
        //添加新的博客和标签的关联信息
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(articleDto.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);

        return ResponseResult.okResult();
    }

    /**
     * 删除文章
     * @param id
     * @return
     */
    @Override
    public ResponseResult delete(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }


}
