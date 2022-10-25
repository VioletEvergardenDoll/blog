package com.blog.controller;

import com.blog.constants.SystemConstants;
import com.blog.domain.ResponseResult;
import com.blog.domain.dto.AddCommentDto;
import com.blog.domain.entity.Comment;
import com.blog.service.CommentService;
import com.blog.utils.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
//swagger api注解
@Api(tags = "评论",description = "评论相关接口")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId,Integer pageNum,Integer pageSize){

        return commentService.commentList(SystemConstants.ARTICLE_COMMENT,articleId,pageNum,pageSize);
    }

    /**
     * 添加评论
     * @param addCommentDto
     * @return
     */
    @PostMapping
    public ResponseResult addComment(@RequestBody AddCommentDto addCommentDto){
        //将dto AddCommentDto转换成 Comment
        Comment comment = BeanCopyUtils.copyBean(addCommentDto, Comment.class);
        return commentService.addComment(comment);
    }

    /**
     * 友链评论列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/linkCommentList")
    //swagger 中文注解   value属性  notes描述信息
    @ApiOperation(value = "友链评论列表",notes = "获取一页友链评论")

    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页号"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小"),
    })
    public ResponseResult linkCommentList(Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.LINK_COMMENT, null,pageNum,pageSize);
    }
}
