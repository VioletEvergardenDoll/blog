package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.domain.ResponseResult;
import com.blog.domain.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2022-09-11 15:44:57
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);
    

    ResponseResult treeselect();

    ResponseResult roleMenuTreeSelect(Long roleId);

    ResponseResult add(Menu menu);

    ResponseResult edit(Menu menu);

    ResponseResult delete(Long menuId);

    List<Menu> selectMenuList(Menu menu);
}
