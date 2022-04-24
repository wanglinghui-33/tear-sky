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
import com.tearsky.common.core.domain.Ztree;
import com.tearsky.common.enums.BusinessType;
import com.tearsky.core.util.ShiroUtils;
import com.tearsky.system.domain.SysMenu;
import com.tearsky.system.domain.SysRole;
import com.tearsky.system.service.ISysMenuService;

/**
 * 	菜单信息
 * @author tearsky
 *
 */
@Controller
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController {

	private String prefix = "system/menu";
	
	@Autowired
	private ISysMenuService menuService;

	@RequiresPermissions("system:menu:view")
	@GetMapping()
	public String menu() {
		return prefix + "/menu";
	}
	
	@RequiresPermissions("system:menu:list")
	@PostMapping("/list")
	@ResponseBody
	public List<SysMenu> list(SysMenu menu){
		Long userId = ShiroUtils.getUserId();
		List<SysMenu> menuList = menuService.selectMenuList(menu, userId);
		return menuList;
	}
	
	/**
	 * 新增
	 * @param parentId
	 * @param mmap
	 * @return
	 */
	@GetMapping("/add/{parentId}")
	public String add(@PathVariable("parentId")Long parentId, ModelMap mmap) {
		SysMenu menu = null;
		if(0L != parentId) {
			menu = menuService.selectMenuById(parentId);
		}else {
			menu = new SysMenu();
			menu.setMenuId(0L);
			menu.setMenuName("主目录");
		}
		mmap.put("menu", menu);
		return prefix + "/add";
	}
	
	@Log(title = "菜单管理", businessType = BusinessType.INSERT)
	@RequiresPermissions("system:menu:add")
	@PostMapping("/add")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult addSave(@Validated SysMenu menu) {
		if(UserConstants.MENU_NAME_NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
			return error("新增菜单" + menu.getMenuName() + "失败，菜单名称已存在");
		}
		menu.setCreateBy(ShiroUtils.getLoginName());
		ShiroUtils.clearCachedAuthorizationInfo();
		return toAjax(menuService.insertMenu(menu));
	}
	
	/**
	 * 修改菜单
	 * @param menuId
	 * @param mmap
	 * @return
	 */
	@GetMapping("/edit/{menuId}")
	public String edit(@PathVariable Long menuId, ModelMap mmap) {
		mmap.put("menu", menuService.selectMenuById(menuId));
		return prefix + "/edit";
	}
	
	/**
	 * 修改保存菜单
	 * @param menu
	 * @return
	 */
	@Log(title = "菜单管理", businessType = BusinessType.UPDATE)
	@RequiresPermissions("system:menu:edit")
	@PostMapping("/edit")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult editSave(@Validated SysMenu menu) {
		if(UserConstants.MENU_NAME_NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
			return error("修改菜单" + menu.getMenuName() + "失败,菜单名称已存在");
		}
		menu.setUpdateBy(ShiroUtils.getLoginName());
		ShiroUtils.clearCachedAuthorizationInfo();
		return toAjax(menuService.updateMenu(menu));
	}
	
	@Log(title = "菜单管理", businessType = BusinessType.DELETE)
	@RequiresPermissions("system:menu:remove")
	@GetMapping("/remove/{menuId}")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult remove(@PathVariable("menuId") Long menuId) {
		if(menuService.selectCountMenuByParentId(menuId) > 0) {
			return AjaxResult.warn("存在子菜单，不允许删除");
		}
		if(menuService.selectCountRoleMenuByMenuId(menuId) > 0) {
			return AjaxResult.warn("菜单已分配，不允许删除");
		}
		ShiroUtils.clearCachedAuthorizationInfo();
		return toAjax(menuService.deleteMenuById(menuId));
	}
	
	/**
	 * 选择菜单树
	 * @param menuId
	 * @param mmap
	 * @return
	 */
	@GetMapping("/selectMenuTree/{menuId}")
	public String selectMenuTree(@PathVariable("menuId") Long menuId, ModelMap mmap) {
		mmap.put("menu", menuService.selectMenuById(menuId));
		return prefix + "/tree";
	}
	
	/**
	 * 加载所有菜单列表树
	 * @return
	 */
	@GetMapping("/menuTreeData")
	@ResponseBody
	public List<Ztree> menuTreeData(){
		Long userId = ShiroUtils.getUserId();
		List<Ztree> ztrees = menuService.menuTreeData(userId);
		return ztrees;
	}
	
	/**
	 * 选择菜单图片
	 * @return
	 */
	@GetMapping("/icon")
	public String icon() {
		return prefix + "/icon";
	}
	
	/**
	 * 检查菜单名称唯一性
	 * @param menu
	 * @return
	 */
	@PostMapping("/checkMenuNameUnique")
	@ResponseBody
	public String checkMenuNameUnique(SysMenu menu) {
		return menuService.checkMenuNameUnique(menu);
	}
	
	/**
	 * 加载角色菜单列表树
	 * @param role
	 * @return
	 */
	@GetMapping("/roleMenuTreeData")
	@ResponseBody
	public List<Ztree> roleMenuTreeData(SysRole role){
		Long userId = ShiroUtils.getUserId();
		List<Ztree> ztrees = menuService.roleMenuTreeData(role, userId);
		return ztrees;
	}
}
