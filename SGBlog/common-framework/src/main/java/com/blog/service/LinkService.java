package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.domain.ResponseResult;
import com.blog.domain.entity.Link;



/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2022-04-22 23:48:36
 */
public interface LinkService extends IService<Link> {


    ResponseResult getAllLink();

    ResponseResult selectLinkPage(Link link, Integer pageNum, Integer pageSize);

    ResponseResult add(Link link);

    ResponseResult getInfo(Long id);

    ResponseResult edit(Link link);

    ResponseResult changeLinkStatus(Link link);
}
