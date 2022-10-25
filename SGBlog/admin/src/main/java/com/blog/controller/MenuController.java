package com.blog.controller;

import com.blog.domain.ResponseResult;
import com.blog.domain.entity.Menu;
import com.blog.domain.vo.MenuVo;
import com.blog.service.MenuService;
import com.blog.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     *  获取菜单列表
     * @param menu
     * @return
     */
    @GetMapping("/list")
    public ResponseResult list(Menu menu){
        List<Menu> menus = menuService.selectMenuList(menu);
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        return ResponseResult.okResult(menuVos);
    }

    /**
     * 获取菜单下拉树列表
     * @return
     */
    @GetMapping("/treeselect")
    public ResponseResult treeselect(){
        return menuService.treeselect();
    }

    /**
     * 加载对应角色菜单列表树
     * @param roleId
     * @return
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public ResponseResult roleMenuTreeSelect(@PathVariable("roleId") Long roleId){
        return menuService.roleMenuTreeSelect(roleId);
    }

    /**
     * 添加菜单
     * @param menu
     * @return
     */
    @PostMapping
    public ResponseResult add(@RequestBody Menu menu){
        return menuService.add(menu);
    }

    /**
     * 根据菜单编号获取详细信息
     * @param menuId
     * @return
     */
    @GetMapping(value = "/{menuId}")
    public ResponseResult getInfo(@PathVariable Long menuId){
        return ResponseResult.okResult(menuService.getById(menuId));
    }

    /**
     * 修改菜单
     * @param menu
     * @return
     */
    @PutMapping
    public ResponseResult edit(@RequestBody Menu menu){
        return menuService.edit(menu);
    }

    /**
     * 删除菜单
     * @param menuId
     * @return
     */
    @DeleteMapping("/{menuId}")
    public ResponseResult remove(@PathVariable("menuId") Long menuId){
        return menuService.delete(menuId);
    }

}
