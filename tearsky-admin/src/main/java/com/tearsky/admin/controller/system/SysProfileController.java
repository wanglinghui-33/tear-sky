package com.tearsky.admin.controller.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.tearsky.common.annotation.Log;
import com.tearsky.common.config.Global;
import com.tearsky.common.core.controller.BaseController;
import com.tearsky.common.core.domain.AjaxResult;
import com.tearsky.common.enums.BusinessType;
import com.tearsky.common.utils.StringUtils;
import com.tearsky.common.utils.file.FileUploadUtils;
import com.tearsky.core.shiro.service.SysPasswordService;
import com.tearsky.core.util.ShiroUtils;
import com.tearsky.system.domain.SysUser;
import com.tearsky.system.service.ISysUserService;

/**
 * 个人信息业务处理
 * @author tearsky
 *
 */
@Controller
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController{
	
	private static final Logger log = LoggerFactory.getLogger(SysProfileController.class);

	private String prefix = "system/user/profile";
	
	@Autowired
	private ISysUserService userService;
	
	@Autowired
	private SysPasswordService sysPasswordService;
	
	/**
	 *  个人信息
	 * @param mmap
	 * @return
	 */
	@GetMapping()
	public String profile(ModelMap mmap) {
		SysUser user = ShiroUtils.getSysUser();
		mmap.put("user", user);
		mmap.put("roleGroup", userService.selectUserRoleGroup(user.getUserId()));
		mmap.put("postGroup", userService.selectUserPostGroup(user.getUserId()));
		return prefix + "/profile";
	}
	
	/**
	 * 验证旧密码是否正确
	 * @param password
	 * @return
	 */
	@GetMapping("/checkPassword")
	@ResponseBody
	public boolean checkPassword(String password) {
		SysUser user = ShiroUtils.getSysUser();
		if(sysPasswordService.matches(user, password)) {
			return true;
		}
		return false;
	}
	
	@GetMapping("/resetPwd")
	public String resetPwd(ModelMap mmap) {
		SysUser user = ShiroUtils.getSysUser();
		mmap.put("user", userService.selectUserById(user.getUserId()));
		return prefix + "/resetPwd";
	}
	
	/**
	 * 重置密码
	 * @param oldPasswor 旧密码
	 * @param newPassword 新密码
	 * @return
	 */
	@Log(title = "重置密码", businessType = BusinessType.UPDATE)
	@PostMapping("/resetPwd")
	@ResponseBody
	public AjaxResult resetPwd(String oldPassword, String newPassword) {
		SysUser user = ShiroUtils.getSysUser();
		if(StringUtils.isNotEmpty(newPassword) && sysPasswordService.matches(user, oldPassword)) {
			user.setSalt(ShiroUtils.randomSalt());
			user.setPassword(sysPasswordService.encryptPassword(user.getLoginName(), newPassword, user.getSalt()));
			if(userService.resetUserPwd(user) > 0) {
				ShiroUtils.setSysUser(userService.selectUserById(user.getUserId()));
				return success();
			}
			return error();
		}else {
			return error("修改密码失败，旧密码错误");
		}
	}
	
	/**
	 * 修改用户
	 * @return
	 */
	@Log(title = "个人信息", businessType = BusinessType.UPDATE)
	@PostMapping("/update")
	@ResponseBody
	public AjaxResult update(SysUser sysUser) {
		SysUser currentUser = ShiroUtils.getSysUser();
		currentUser.setUserName(sysUser.getUserName());
		currentUser.setEmail(sysUser.getEmail());
		currentUser.setPhonenumber(sysUser.getPhonenumber());
		currentUser.setSex(sysUser.getSex());
		if(userService.updateUserInfo(currentUser) > 0) {
			ShiroUtils.setSysUser(userService.selectUserById(currentUser.getUserId()));
			return success();
		}
		return error();
	}
	
	/**
	 * 修改头像
	 * @param mmap
	 * @return
	 */
	@GetMapping("/avatar")
	public String avatar(ModelMap mmap) {
		SysUser sysUser = ShiroUtils.getSysUser();
		mmap.put("user", userService.selectUserById(sysUser.getUserId()));
		return prefix + "/avatar";
	}
	
	@Log(title = "个人信息", businessType = BusinessType.UPDATE)
	@PostMapping("/updateAvatar")
	@ResponseBody
	public AjaxResult updateAvatar(@RequestParam("avatarfile") MultipartFile file) {
		SysUser user = ShiroUtils.getSysUser();
		try {
			if(!file.isEmpty()) {
				String avatar = FileUploadUtils.upload(Global.getAvatarPath(), file);
				user.setAvatar(avatar);
				if(userService.updateUserInfo(user) > 0) {
					ShiroUtils.setSysUser(userService.selectUserById(user.getUserId()));
					return success();
				}
			}
			return error();
		} catch (Exception e) {
			log.error("修改头像失败！", e);
			return error(e.getMessage());
		}
	}
}
