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
import org.springframework.web.multipart.MultipartFile;

import com.tearsky.common.annotation.Log;
import com.tearsky.common.annotation.RepeatSubmit;
import com.tearsky.common.constant.UserConstants;
import com.tearsky.common.core.controller.BaseController;
import com.tearsky.common.core.domain.AjaxResult;
import com.tearsky.common.core.page.TableDataInfo;
import com.tearsky.common.enums.BusinessType;
import com.tearsky.common.utils.poi.ExcelUtil;
import com.tearsky.core.shiro.service.SysPasswordService;
import com.tearsky.core.util.ShiroUtils;
import com.tearsky.system.domain.SysUser;
import com.tearsky.system.service.ISysPostService;
import com.tearsky.system.service.ISysRoleService;
import com.tearsky.system.service.ISysUserService;

/**
 * 用户信息
 * @author tearsky
 *
 */
@Controller
@RequestMapping("/system/user")
public class SysUserController extends BaseController {
	
	private String prefix = "system/user";
	
	@Autowired
	private ISysUserService userService;

	@Autowired
	private ISysRoleService roleService;
	
	@Autowired
	private ISysPostService postService;
	
	@Autowired
	private SysPasswordService passwordService;
	
	/**
	 * 用户列表页面
	 */
	@RequiresPermissions("system:user:view")
	@GetMapping()
	public String user() {
		return prefix + "/user";
	}
	
	/**
	 * 用户列表
	 * @param user
	 * @return
	 */
	@RequiresPermissions("system:user:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SysUser user) {
		startPage();
		List<SysUser> list = userService.selectUserList(user);
		return getDataTable(list);
	}
	
	/**
	 * 新增用户
	 * @param mmap
	 * @return
	 */
	@GetMapping("/add")
	public String add(ModelMap mmap) {
		mmap.put("roles", roleService.selectRoleAll());
		mmap.put("posts", postService.selectPostAll());
		return prefix + "/add";
	}
	
	/**
	 * 新增用户保存
	 * @param user
	 * @return
	 */
	@Log(title = "用户管理", businessType = BusinessType.INSERT)
	@RequiresPermissions("system:user:add")
	@PostMapping("/add")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult addSave(@Validated SysUser user) {
		if(UserConstants.USER_NAME_NOT_UNIQUE.equals(userService.checkLoginNameUnique(user.getLoginName()))) {
			return error("新增用户 ' " + user.getLoginName() + " ' 失败，登录账号已存在");
		}else if(UserConstants.USER_PHONE_NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
			return error("新增用户 ' " + user.getPhonenumber() + " ' 失败，手机号码已存在");
		}else if(UserConstants.USER_EMAIL_NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
			return error("新增用户 ' " + user.getPhonenumber() + " ' 失败，邮箱账号已存在");
		}
		user.setSalt(ShiroUtils.randomSalt());
		user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
		user.setCreateBy(ShiroUtils.getLoginName());
		return toAjax(userService.insertUser(user));
	}
	
	/**
	 * 修改用户
	 * @param userId
	 * @param mmap
	 * @return
	 */
	@GetMapping("/edit/{userId}")
	public String edit(@PathVariable("userId") Long userId, ModelMap mmap) {
		mmap.put("user", userService.selectUserById(userId));
		mmap.put("roles", roleService.selectRolesByUserId(userId));
		mmap.put("posts", postService.selectPostsByUserId(userId));
		return prefix + "/edit";
	}
	
	/**
	 * 修改用户保存
	 * @param user
	 * @return
	 */
	@Log(title = "用户管理", businessType = BusinessType.UPDATE)
	@RequiresPermissions("system:user:edit")
	@PostMapping("/edit")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult editSave(@Validated SysUser user) {
		userService.checkUserAllowed(user);
		if(UserConstants.USER_PHONE_NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
			return error("修改用户 ' " + user.getPhonenumber() + " ' 失败，手机号码已存在");
		}else if(UserConstants.USER_EMAIL_NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
			return error("修改用户 ' " + user.getPhonenumber() + " ' 失败，邮箱账号已存在");
		}
		user.setUpdateBy(ShiroUtils.getLoginName());
		
		return toAjax(userService.updateUser(user));
	}
	
	/**
	 * 用户状态修改
	 * @param user
	 * @return
	 */
	@Log(title = "用户管理", businessType = BusinessType.UPDATE)
	@RequiresPermissions("system:user:edit")
	@PostMapping("/changeStatus")
	@ResponseBody
	public AjaxResult changeStatus(SysUser user) {
		userService.checkUserAllowed(user);
		return toAjax(userService.changeStatus(user));
	}
	
	/**
	 * 用户删除
	 * @param ids
	 * @return
	 */
	@Log(title = "用户管理", businessType = BusinessType.DELETE)
	@RequiresPermissions("system:user:remove")
	@PostMapping("/remove")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult remove(String ids) {
		try {
			return toAjax(userService.deleteUserByIds(ids));
		} catch (Exception e) {
			return error(e.getMessage());
		}
	}
	
	/**
	 * 重置密码
	 * @param userId
	 * @param mmap
	 * @return
	 */
	@RequiresPermissions("system:user:resetPwd")
	@GetMapping("/resetPwd/{userId}")
	public String resetPwd(@PathVariable("userId") Long userId, ModelMap mmap) {
		mmap.put("user", userService.selectUserById(userId));
		return prefix + "/resetPwd";
	}
	
	/**
	 * 重置密码保存
	 * @param user
	 * @return
	 */
	@Log(title = "用户管理", businessType = BusinessType.UPDATE)
	@RequiresPermissions("system:user:resetPwd")
	@PostMapping("/resetPwd")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult resetPwdSave(SysUser user) {
		userService.checkUserAllowed(user);
		user.setSalt(ShiroUtils.randomSalt());
		user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
		if(userService.resetUserPwd(user) > 0) {
			if(ShiroUtils.getUserId() == user.getUserId()) {
				ShiroUtils.setSysUser(userService.selectUserById(user.getUserId()));
			}
			return success();
		}
		return error();
	}
	
	/**
	 * 用户导出
	 * @param user
	 * @return
	 */
	@Log(title = "用户管理", businessType = BusinessType.EXPORT)
	@RequiresPermissions("system:user:export")
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(SysUser user) {
		List<SysUser> list = userService.selectUserList(user);
		ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
		return util.exportExcel(list, "用户数据");
	}
	
	/**
	 * 用户导入模板下载
	 * @return
	 */
	@RequiresPermissions("system:user:import")
	@GetMapping("/importTemplate")
	@ResponseBody
	public AjaxResult importTemplate() {
		ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
		return util.importTemplateExcel("用户数据");
	}
	
	/**
	 * 导入用户信息
	 * @param file
	 * @param updateSupport
	 * @return
	 * @throws Exception 
	 */
	@Log(title = "用户管理", businessType = BusinessType.IMPORT)
	@RequiresPermissions("system:user:import")
	@PostMapping("/importData")
	@ResponseBody
	public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
		ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
		List<SysUser> userList = util.importExcel(file.getInputStream());
		String operName = ShiroUtils.getSysUser().getLoginName();
		String message = userService.importUser(userList, updateSupport, operName);
		return AjaxResult.success(message);
	}
	
	/**
	 * 检验用户名
	 * @param sysUser
	 * @return
	 */
	@PostMapping("/checkLoginNameUnique")
	@ResponseBody
	public String checkLoginNameUnique(SysUser sysUser) {
		return userService.checkLoginNameUnique(sysUser.getLoginName());
	}
	
	/**
	 * 检验手机号码
	 * @param sysUser
	 * @return
	 */
	@PostMapping("/checkPhoneUnique")
	@ResponseBody
	public String checkPhoneUnique(SysUser sysUser) {
		return userService.checkPhoneUnique(sysUser);
	}
	
	/**
	 * 检验email邮箱
	 * @param sysUser
	 * @return
	 */
	@PostMapping("/checkEmailUnique")
	@ResponseBody
	public String checkEmailUnique(SysUser sysUser) {
		return userService.checkEmailUnique(sysUser);
	}
}
