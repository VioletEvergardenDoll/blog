package com.blog.controller;

import com.blog.domain.ResponseResult;
import com.blog.domain.dto.ChangeRoleStatusDto;
import com.blog.domain.entity.Role;
import com.blog.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 后台角色列表
     * @param role
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public ResponseResult list(Role role,Integer pageNum,Integer pageSize){
        return roleService.selectRolePage(role,pageNum,pageSize);
    }

    /**
     * 修改角色状态
     * @param roleStatusDto
     * @return
     */
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody ChangeRoleStatusDto roleStatusDto){
        return roleService.ChangeRoleStatus(roleStatusDto);
    }

    /**
     * 新增角色
     * @param role
     * @return
     */
    @PostMapping
    public ResponseResult add(@RequestBody Role role){
        return roleService.insertRole(role);
    }

    /**
     * 根据角色编号获取详细信息
     * @param roleId
     * @return
     */
    @GetMapping(value = "/{roleId}")
    public ResponseResult getInfo(@PathVariable Long roleId){
        return roleService.getInfo(roleId);
    }

    /**
     * 修改保存角色
     * @return
     */
    @PutMapping
    public ResponseResult edit(@RequestBody Role role){
        return roleService.updateRole(role);
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult remove(@PathVariable(name = "id") Long id){
        roleService.removeById(id);
        return ResponseResult.okResult();
    }

    /**
     * 新增用户获取角色菜单
     * @return
     */
    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        List<Role> roles = roleService.selectRoleAll();
        return ResponseResult.okResult(roles);
    }
}
