package com.tearsky.quartz.controller;

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

import com.tearsky.common.annotation.Log;
import com.tearsky.common.core.controller.BaseController;
import com.tearsky.common.core.domain.AjaxResult;
import com.tearsky.common.core.page.TableDataInfo;
import com.tearsky.common.enums.BusinessType;
import com.tearsky.common.utils.poi.ExcelUtil;
import com.tearsky.quartz.domain.SysJobLog;
import com.tearsky.quartz.service.ISysJobLogService;

/**
 * 调度日志操作记录
 * @author tearsky
 *
 */
@Controller
@RequestMapping("/monitor/jobLog")
public class SysJobLogController extends BaseController {

	private String prefix = "monitor/job";
	
	@Autowired
	private ISysJobLogService jobLogService;
	
	@RequiresPermissions("monitor:job:view")
	@GetMapping()
	public String jobLog() {
		return prefix + "/jobLog";
	}
	
	@RequiresPermissions("monitor:job:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SysJobLog jobLog) {
		startPage();
		List<SysJobLog> list = jobLogService.selectJobLogList(jobLog);
		return getDataTable(list);
	}
	
	/**
	 * 调度日志详情
	 * @param jobLogId
	 * @param mmap
	 * @return
	 */
	@RequiresPermissions("monitor:job:detail")
	@GetMapping("/detail/{jobLogId}")
	public String detail(@PathVariable("jobLogId") Long jobLogId, ModelMap mmap) {
		mmap.put("name", "jobLog");
        mmap.put("jobLog", jobLogService.selectJobLogById(jobLogId));
        return prefix + "/detail";
	}
	
	/**
	 * 调度日志删除
	 * @param ids
	 * @return
	 */
	@Log(title = "调度日志", businessType = BusinessType.DELETE)
	@RequiresPermissions("monitor:job:remove")
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		return toAjax(jobLogService.deleteJobLogByIds(ids));
	}
	
	/**
	 * 调度日志清除
	 * @return
	 */
	@Log(title = "调度日志", businessType = BusinessType.CLEAN)
    @RequiresPermissions("monitor:job:remove")
    @PostMapping("/clean")
    @ResponseBody
	public AjaxResult clean() {
		jobLogService.cleanJobLog();
        return success();
	}
	
	/**
	 * 调度日志导出
	 * @param jobLog
	 * @return
	 */
	@Log(title = "调度日志", businessType = BusinessType.EXPORT)
    @RequiresPermissions("monitor:job:export")
    @PostMapping("/export")
    @ResponseBody
	public AjaxResult export(SysJobLog jobLog) {
		List<SysJobLog> list = jobLogService.selectJobLogList(jobLog);
		ExcelUtil<SysJobLog> util = new ExcelUtil<SysJobLog>(SysJobLog.class);
		return util.exportExcel(list, "调度日志");
	}
}
