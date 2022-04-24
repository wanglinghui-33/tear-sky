package com.tearsky.admin.controller.monitor;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tearsky.common.core.controller.BaseController;
import com.tearsky.common.core.domain.AjaxResult;
import com.tearsky.common.core.page.TableDataInfo;
import com.tearsky.common.utils.poi.ExcelUtil;
import com.tearsky.system.domain.SysOperLog;
import com.tearsky.system.service.ISysOperLogService;

/**
 * 操作日志记录
 * @author tearsky
 *
 */
@Controller
@RequestMapping("/monitor/operlog")
public class SysOperlogController extends BaseController{

	private String prefix = "monitor/operlog";
	
	@Autowired
	private ISysOperLogService operLogService;
	
	/**
	 * 操作日志列表页面
	 * @return
	 */
	@RequiresPermissions("monitor:operlog:view")
	@GetMapping()
	public String operlog() {
		return prefix + "/operlog";
	}
	
	/**
	 * 操作日志列表
	 * @param operLog
	 * @return
	 */
	@RequiresPermissions("monitor:operlog:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SysOperLog operLog) {
		startPage();
		List<SysOperLog> list = operLogService.selectOperLogList(operLog);
		return getDataTable(list);
	}
	
	/**
	 * 操作日志详情
	 * @param operId
	 * @param mmap
	 * @return
	 */
	@RequiresPermissions("monitor:operlog:detail")
	@GetMapping("/detail/{operId}")
	public String detail(@PathVariable("operId") Long operId, ModelMap mmap) {
		mmap.put("operLog", operLogService.selectOperLogById(operId));
		return prefix + "/detail";
	}
	
	/**
	 * 删除操作日志
	 * @param ids
	 * @return
	 */
	@RequiresPermissions("monitor:operlog:remove")
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		return toAjax(operLogService.deleteOperLogByIds(ids));
	}
	
	/**
	 * 清除操作日志
	 * @return
	 */
	@RequiresPermissions("monitor:operlog:remove")
	@PostMapping("/clean")
	@ResponseBody
	public AjaxResult clean() {
		operLogService.cleanOperLog();
		return success();
	}
	
	/**
	 * 导出操作日志
	 * @param operlog
	 * @return
	 */
	@RequiresPermissions("monitor:operlog:export")
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(SysOperLog operLog) {
		List<SysOperLog> list = operLogService.selectOperLogList(operLog);
		ExcelUtil<SysOperLog> util = new ExcelUtil<SysOperLog>(SysOperLog.class);
		return util.exportExcel(list, "操作日志");
	}
}
