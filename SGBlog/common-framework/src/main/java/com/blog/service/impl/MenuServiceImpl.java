package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.constants.SystemConstants;
import com.blog.domain.ResponseResult;
import com.blog.domain.entity.Menu;
import com.blog.domain.vo.MenuTreeVo;
import com.blog.domain.vo.MenuVo;
import com.blog.domain.vo.RoleMenuTreeSelectVo;
import com.blog.mapper.MenuMapper;
import com.blog.service.MenuService;
import com.blog.utils.BeanCopyUtils;
import com.blog.utils.SecurityUtils;
import com.blog.utils.SystemConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2022-09-11 15:44:58
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    /**
     * 权限Perms
     * @param id
     * @return
     */
    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员，返回所有的权限
        if (SecurityUtils.isAdmin()){
            //判断条件
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            //判断权限类型是C或F
            wrapper.in(Menu::getMenuType,SystemConstants.MENU,SystemConstants.BUTTON);
            //判断权限状态是否为正常
            wrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);

            List<Menu> menus = list(wrapper);
            //处理   如果是权限字段，则都返回
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        //否则返回其所具有的的权限
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        //判断是否是管理员
        if (SecurityUtils.isAdmin()){
            //如果是 返回所有符合要求的Menu
           menus = menuMapper.selectAllRouterMenu();
        }else {
            //否则获取当前用户所具有得Menu
           menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }
        //构建tree
        //先找出第一层的菜单 然后去找他们的子菜单 设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus,0L);
        return menuTree;
    }

    /**
     * 后台菜单列表
     * @param menu
     * @return
     */
    @Override
    public List<Menu> selectMenuList(Menu menu) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        //menuName模糊查询
        queryWrapper.like(StringUtils.hasText(menu.getMenuName()),Menu::getMenuName,menu.getMenuName());
        queryWrapper.eq(StringUtils.hasText(menu.getStatus()),Menu::getStatus,menu.getStatus());
        //排序 parent_id和order_num
        queryWrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
        List<Menu> menus = list(queryWrapper);;
        return menus;
    }


    private List<Menu> builderMenuTree(List<Menu> menus, long parentId) {
        //找到parentid为0的返回    filter过滤
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .peek(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }



    /**
     * 获取存入参数的子Menu集合
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {

        //对集合进行stream
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .peek(m -> m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }


    @Override
    public ResponseResult treeselect() {
        //复用之前的selectMenuList方法。方法需要参数，参数可以用来进行条件查询，而这个方法不需要条件，所以直接new Menu()传入
        List<Menu> menus = selectMenuList(new Menu());
        List<MenuTreeVo> options =  SystemConverter.buildMenuSelectTree(menus);
        return ResponseResult.okResult(options);
    }

    @Override
    public ResponseResult roleMenuTreeSelect(Long roleId) {
        List<Menu> menus = selectMenuList(new Menu());
        List<Long> checkedKeys = selectMenuListByRoleId(roleId);
        List<MenuTreeVo> menuTreeVos = SystemConverter.buildMenuSelectTree(menus);
        RoleMenuTreeSelectVo vo = new RoleMenuTreeSelectVo(checkedKeys,menuTreeVos);
        return ResponseResult.okResult(vo);
    }



    private List<Long> selectMenuListByRoleId(Long roleId) {
        return getBaseMapper().selectMenuListByRoleId(roleId);
    }

    /**
     * 添加菜单
     * @param menu
     * @return
     */
    @Override
    public ResponseResult add(Menu menu) {
        save(menu);
        return ResponseResult.okResult();
    }

    /**
     * 修改菜单
     * @param menu
     * @return
     */
    @Override
    public ResponseResult edit(Menu menu) {
        if (menu.getId().equals(menu.getParentId())){
            return ResponseResult.errorResult(500,"修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        updateById(menu);
        return ResponseResult.okResult();
    }

    /**
     * 删除菜单
     * @param menuId
     * @return
     */
    @Override
    public ResponseResult delete(Long menuId) {
        if (hasChild(menuId)){
            return ResponseResult.errorResult(500,"存在子菜单不允许删除");
        }
        removeById(menuId);
        return ResponseResult.okResult();
    }



    private boolean hasChild(Long menuId) {
        //判断ParentId不等于menuId
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId,menuId);
        return count(queryWrapper) != 0;
    }
}
