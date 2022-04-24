package com.tearsky.admin.controller.system;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tearsky.common.annotation.Log;
import com.tearsky.common.annotation.RepeatSubmit;
import com.tearsky.common.constant.UserConstants;
import com.tearsky.common.core.controller.BaseController;
import com.tearsky.common.core.domain.AjaxResult;
import com.tearsky.common.core.page.TableDataInfo;
import com.tearsky.common.enums.BusinessType;
import com.tearsky.common.utils.poi.ExcelUtil;
import com.tearsky.core.util.ShiroUtils;
import com.tearsky.system.domain.SysRole;
import com.tearsky.system.domain.SysUser;
import com.tearsky.system.domain.SysUserRole;
import com.tearsky.system.service.ISysRoleService;
import com.tearsky.system.service.ISysUserService;

/**
 * 角色信息
 * @author tearsky
 *
 */
@Controller
@RequestMapping("/system/role")
public class SysRoleController extends BaseController {

	private String prefix = "system/role";
	
	@Autowired
	private ISysRoleService roleService;
	@Autowired
	private ISysUserService userService;
	
	/**
	 * 角色列表页面
	 * @return
	 */
	@RequiresPermissions("system:role:view")
	@GetMapping
	public String role() {
		return prefix + "/role";
	}
	
	/**
	 * 角色列表
	 * @param role
	 * @return
	 */
	@RequiresPermissions("system:role:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SysRole role) {
		startPage();
		List<SysRole> list = roleService.selectRoleList(role);
		return getDataTable(list);
	}
	
	/**
	 * 新增角色
	 * @return
	 */
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}
	
	@Log(title = "角色管理", businessType = BusinessType.INSERT)
	@RequiresPermissions("system:role:add")
	@PostMapping("/add")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult addSave(@Validated SysRole role) {
		if(UserConstants.ROLE_NAME_NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
			return error("新增角色 ' " + role.getRoleName() + " '失败，角色名称已存在");
		}else if(UserConstants.ROLE_KEY_NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
			return error("新增角色 ' " + role.getRoleName() + " '失败，角色权限已存在");
		}
		role.setCreateBy(ShiroUtils.getLoginName());
		ShiroUtils.clearCachedAuthorizationInfo();
		return toAjax(roleService.insertRole(role));
	}
	
	/**
	 * 修改角色
	 * @param roleId
	 * @param mmap
	 * @return
	 */
	@GetMapping("/edit/{roleId}")
	public String edit(@PathVariable("roleId") Long roleId, ModelMap mmap) {
		mmap.put("role", roleService.selectRoleById(roleId));
		return prefix + "/edit";
	}
	
	@Log(title = "角色管理", businessType = BusinessType.UPDATE)
	@RequiresPermissions("system:role:edit")
	@PostMapping("/edit")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult editSave(@Validated SysRole role) {
		roleService.checkRoleAllowed(role);
		if(UserConstants.ROLE_NAME_NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
			return error("修改角色 ' " + role.getRoleName() + " ' 失败，角色名称已存在");
		}else if(UserConstants.ROLE_KEY_NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))){
			return error("修改角色 ' " + role.getRoleName() + " ' 失败，角色权限已存在");
		}
		role.setUpdateBy(ShiroUtils.getLoginName());
		ShiroUtils.clearCachedAuthorizationInfo();
		return toAjax(roleService.updateRole(role));
	}
	
	/**
	 * 删除角色
	 * @param ids
	 * @return
	 */
	@Log(title = "角色管理", businessType = BusinessType.DELETE)
	@RequiresPermissions("system:role:remove")
	@PostMapping("/remove")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult remove(String ids) {
		try {
			return toAjax(roleService.deleteRoleByIds(ids));
		} catch (Exception e) {
			return error(e.getMessage());
		}
	}
	
	/**
	 * 导出角色管理
	 * @param role
	 * @return
	 */
	@Log(title = "角色管理", businessType = BusinessType.EXPORT)
	@RequiresPermissions("system:role:export")
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(SysRole role) {
		List<SysRole> list = roleService.selectRoleList(role);
		ExcelUtil<SysRole> util = new ExcelUtil<SysRole>(SysRole.class);
		return util.exportExcel(list, "角色数据");
	}
	
	/**
	 * 角色状态修改
	 * @param role
	 * @return
	 */
	@Log(title = "角色管理", businessType = BusinessType.UPDATE)
	@RequiresPermissions("system:role:edit")
	@PostMapping("changeStatus")
	@ResponseBody
	public AjaxResult changeStatus(SysRole role) {
		roleService.checkRoleAllowed(role);
		return toAjax(roleService.changeStatus(role));
	}
	
	/**
	 * 角色分配数据权限
	 * @param roleId
	 * @param mmap
	 * @return
	 */
	@RequiresPermissions("system:role:authData")
	@GetMapping("/authDataScope/{roleId}")
	public String authDataScope(@PathVariable("roleId") Long roleId, ModelMap mmap) {
		mmap.put("role", roleService.selectRoleById(roleId));
		return prefix + "/dataScope";
	}
	
	/**
	 * 保存角色分配的数据权限
	 * @param role
	 * @return
	 */
	@Log(title = "角色管理", businessType = BusinessType.UPDATE)
	@RequiresPermissions("system:role:authData")
	@PostMapping("/authDataScope")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult authDataScopeSave(SysRole role) {
		roleService.checkRoleAllowed(role);
		role.setUpdateBy(ShiroUtils.getLoginName());
		if(roleService.authDataScope(role) > 0) {
			ShiroUtils.setSysUser(userService.selectUserById(ShiroUtils.getSysUser().getUserId()));
			return success();
		}
		return error();
	}
	
	/**
	 * 分配用户
	 * @param roleId
	 * @param mmap
	 * @return
	 */
	@RequiresPermissions("system:role:authUser")
	@GetMapping("/authUser/{roleId}")
	public String authUser(@PathVariable("roleId") Long roleId, ModelMap mmap) {
		mmap.put("role", roleService.selectRoleById(roleId));
		return prefix + "/authUser";
	}
	
	/**
	 * 查询已分配用户角色列表
	 * @param user
	 * @return
	 */
	@RequiresPermissions("system:role:list")
	@PostMapping("/authUser/allocatedList")
	@ResponseBody
	public TableDataInfo allocatedList(SysUser user) {
		startPage();
		List<SysUser> list = userService.selectAllocatedList(user);
		return getDataTable(list);
	}
	
	/**
	 * 选择用户
	 * @param roleId
	 * @param mmap
	 * @return
	 */
	@GetMapping("/authUser/selectUser/{roleId}")
	public String selectUser(@PathVariable("roleId") Long roleId, ModelMap mmap) {
		mmap.put("role", roleService.selectRoleById(roleId));
		return prefix + "/selectUser";
	}
	
	/**
	 * 查询未分配的用户角色列表
	 * @param user
	 * @return
	 */
	@RequiresPermissions("system:role:list")
	@PostMapping("/authUser/unallocatedList")
	@ResponseBody
	public TableDataInfo unallocatedList(SysUser user) {
		startPage();
		List<SysUser> list = userService.selectUnallocatedList(user);
		return getDataTable(list);
	}
	
	/**
	 * 批量选择用户授权
	 * @param roleId
	 * @param userIds
	 * @return
	 */
	@Log(title = "角色管理", businessType = BusinessType.GRANT)
	@PostMapping("authUser/selectAll")
	@ResponseBody
	public AjaxResult selectAuthUserAll(Long roleId, String userIds) {
		return toAjax(roleService.insertAuthUsers(roleId, userIds));
	}
	
	/**
	 * 取消授权
	 * @param userRole
	 * @return
	 */
	@Log(title = "角色管理", businessType = BusinessType.GRANT)
	@PostMapping("/authUser/cancel")
	@ResponseBody
	public AjaxResult cancelAuthUser(SysUserRole userRole) {
		return toAjax(roleService.deleteAuthUser(userRole));
	}
	
	/**
	 * 批量取消授权
	 * @param roleId
	 * @param userIds
	 * @return
	 */
	@Log(title = "角色管理", businessType = BusinessType.GRANT)
	@PostMapping("/authUser/cancelAll")
	@ResponseBody
	public AjaxResult cancelAuthUserAll(Long roleId, String userIds) {
		return toAjax(roleService.deleteAuthUsers(roleId, userIds));
	}
	
	/**
	 * 校验角色名称
	 * @param role
	 * @return
	 */
	@PostMapping("/checkRoleNameUnique")
	@ResponseBody
	public String checkRoleNameUnique(SysRole role) {
		return roleService.checkRoleNameUnique(role);
	}
	
	/**
	 * 检验角色权限
	 * @param role
	 * @return
	 */
	@PostMapping("/checkRoleKeyUnique")
	@ResponseBody
	public String checkRoleKeyUnique(SysRole role) {
		return roleService.checkRoleKeyUnique(role);
	}
}
