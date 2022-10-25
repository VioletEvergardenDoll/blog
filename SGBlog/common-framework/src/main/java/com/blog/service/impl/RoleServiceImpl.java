package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.constants.SystemConstants;
import com.blog.domain.ResponseResult;
import com.blog.domain.dto.ChangeRoleStatusDto;
import com.blog.domain.entity.Role;
import com.blog.domain.entity.RoleMenu;
import com.blog.domain.vo.PageVo;
import com.blog.mapper.RoleMapper;
import com.blog.service.RoleMenuService;
import com.blog.service.RoleService;
import com.mysql.cj.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2022-09-11 15:51:09
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMenuService roleMenuService;
    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员，如果是返回集合中只需要有admin
        if(id == 1L){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        //否则查询用户所具有的角色信息

        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    /**
     * 后台角色列表
     * @param role
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult selectRolePage(Role role, Integer pageNum, Integer pageSize) {

        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        //查询
        queryWrapper.like(StringUtils.hasText(role.getRoleName()),Role::getRoleName,role.getRoleName());
        queryWrapper.eq(StringUtils.hasText(role.getStatus()),Role::getStatus,role.getStatus());
        //排序
        queryWrapper.orderByAsc(Role::getRoleSort);

        Page<Role> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,queryWrapper);
        //转换成Vo
        List<Role> roles = page.getRecords();

        PageVo pageVo = new PageVo();
        pageVo.setTotal(page.getTotal());
        pageVo.setRows(roles);

        return ResponseResult.okResult(pageVo);
    }

    /**
     * 修改角色状态
     * @param roleStatusDto
     * @return
     */
    @Override
    public ResponseResult ChangeRoleStatus(ChangeRoleStatusDto roleStatusDto) {
        Role role = new Role();
        role.setId(roleStatusDto.getId());
        role.setRoleName(roleStatusDto.getStatus());
        updateById(role);
        return ResponseResult.okResult();
    }

    /**
     * 新增角色
     * @param role
     * @return
     */
    @Override
    @Transactional
    public ResponseResult insertRole(Role role) {
        save(role);
        System.out.println(role.getId());
        if(role.getMenuIds()!=null&&role.getMenuIds().length>0){
            insertRoleMenu(role);
        }
        return ResponseResult.okResult();
    }



    /**
     * 关联角色菜单表，新增数据
     * @param role
     */
    private void insertRoleMenu(Role role) {
        List<RoleMenu> roleMenuList = Arrays.stream(role.getMenuIds())
                .map(memuId -> new RoleMenu(role.getId(), memuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenuList);
    }


    /**
     * 获取详细信息
     * @param roleId
     * @return
     */
    @Override
    public ResponseResult getInfo(Long roleId) {
        Role role= getById(roleId);
        return ResponseResult.okResult(role);
    }

    /**
     * 修改角色
     * @param role
     * @return
     */
    @Override
    public ResponseResult updateRole(Role role) {
        updateById(role);
        roleMenuService.deleteRoleMenuByRoleId(role.getId());
        insertRoleMenu(role);
        return ResponseResult.okResult();
    }

    @Override
    public List<Role> selectRoleAll() {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getStatus, SystemConstants.NORMAL);
        return list(queryWrapper);
    }

    @Override
    public List<Long> selectRoleIdByUserId(Long userId) {
        return getBaseMapper().selectRoleIdByUserId(userId);
    }


}
