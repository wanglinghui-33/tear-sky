package com.tearsky.admin.controller.monitor;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tearsky.common.annotation.Log;
import com.tearsky.common.core.controller.BaseController;
import com.tearsky.common.core.domain.AjaxResult;
import com.tearsky.common.core.page.TableDataInfo;
import com.tearsky.common.enums.BusinessType;
import com.tearsky.common.utils.poi.ExcelUtil;
import com.tearsky.core.shiro.service.SysPasswordService;
import com.tearsky.system.domain.SysLogininfor;
import com.tearsky.system.service.ISysLogininforService;

/**
 * 系统访问记录
 * @author tearsky
 *
 */
@Controller
@RequestMapping("/monitor/logininfor")
public class SysLogininforController extends BaseController{

	private String prefix = "monitor/logininfor";
	
	@Autowired
	private ISysLogininforService logininforService;
	
	@Autowired
	private SysPasswordService passwordService;
	
	/**
	 * 登录日志页面
	 * @return
	 */
	@RequiresPermissions("monitor:logininfor:view")
	@GetMapping()
	public String logininfor() {
		return prefix + "/logininfor";
	}
	
	/**
	 * 登录日志列表
	 * @param logininfor
	 * @return
	 */
	@RequiresPermissions("monitor:logininfor:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SysLogininfor logininfor) {
		startPage();
		List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
		return getDataTable(list);
	}
	
	/**
	 * 移除登录日志
	 * @param ids
	 * @return
	 */
	@Log(title = "登录日志", businessType = BusinessType.DELETE)
	@RequiresPermissions("monitor:logininfor:remove")
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		return toAjax(logininforService.deleteLogininforByIds(ids));
	}
	
	/**
	 * 清空登录日志
	 * @return
	 */
	@Log(title = "登录日志", businessType = BusinessType.DELETE)
	@RequiresPermissions("monitor:logininfor:remove")
	@PostMapping("/clean")
	@ResponseBody
	public AjaxResult clean() {
		logininforService.cleanLogininfor();
		return success();
	}
	
	/**
	 * 解锁用户
	 * @param loginName
	 * @return
	 */
	@Log(title = "登录日志", businessType = BusinessType.OTHER)
	@RequiresPermissions("monitor:logininfor:unlock")
	@PostMapping("/unlock")
	@ResponseBody
	public AjaxResult unlock(String loginName) {
		passwordService.unlock(loginName);
		return success();
	}
	
	/**
	 * 导出登录日志
	 * @param logininfor
	 * @return
	 */
	@Log(title = "登录日志", businessType = BusinessType.EXPORT)
	@RequiresPermissions("monitot:logininfor:export")
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(SysLogininfor logininfor) {
		List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
		ExcelUtil<SysLogininfor> util = new ExcelUtil<SysLogininfor>(SysLogininfor.class);
		return util.exportExcel(list, "登录日志");
	}
}
