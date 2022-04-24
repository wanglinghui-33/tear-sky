package com.tearsky.admin.controller.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.tearsky.common.config.Global;
import com.tearsky.common.core.controller.BaseController;
import com.tearsky.core.util.ShiroUtils;
import com.tearsky.system.domain.SysMenu;
import com.tearsky.system.domain.SysUser;
import com.tearsky.system.service.ISysMenuService;

/**
 * 首页业务处理
 * 
 * @author Administrator
 *
 */
@Controller
public class SysIndexController extends BaseController {

	@Autowired
	private ISysMenuService menuService;

	/**
	 * 系统首页
	 * 
	 * @param mmap
	 * @return
	 */
	@GetMapping("/index")
	public String index(ModelMap mmap) {
		// 获取身份信息
		SysUser user = ShiroUtils.getSysUser();
		// 根据用户ID取出菜单
		List<SysMenu> menus = menuService.selectMenusByUser(user);
		mmap.put("menus", menus);
		mmap.put("user", user);
		mmap.put("copyrightYear", Global.getCopyrightYear());
		mmap.put("demoEnabled", Global.isDemoEnabled());
		return "index";
	}

	// 系统介绍
	@GetMapping("/system/main")
	public String main(ModelMap mmap) {
		mmap.put("version", Global.getVersion());
		return "main";
	}
	
	/**
	 * 切换主题
	 * @param mmap
	 * @return
	 */
	@GetMapping("/system/switchSkin")
	public String switchSkin(ModelMap mmap) {
		return "skin";
	}
}
