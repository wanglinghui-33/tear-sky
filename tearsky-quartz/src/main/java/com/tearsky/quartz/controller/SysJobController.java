package com.tearsky.quartz.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.quartz.SchedulerException;
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
import com.tearsky.common.core.controller.BaseController;
import com.tearsky.common.core.domain.AjaxResult;
import com.tearsky.common.core.page.TableDataInfo;
import com.tearsky.common.enums.BusinessType;
import com.tearsky.common.exception.job.TaskException;
import com.tearsky.common.utils.poi.ExcelUtil;
import com.tearsky.quartz.domain.SysJob;
import com.tearsky.quartz.service.ISysJobService;

/**
 * 调度任务信息处理
 * @author tearsky
 *
 */
@Controller
@RequestMapping("/monitor/job")
public class SysJobController extends BaseController{

	private String prefix = "monitor/job";
	
	@Autowired
	private ISysJobService jobService;
	
	/**
	 * 列表页面
	 * @return
	 */
	@RequiresPermissions("monitor:job:view")
	@GetMapping()
	public String job() {
		return prefix + "/job";
	}
	
	/**
	 * 定时任务列表
	 * @param job
	 * @return
	 */
	@RequiresPermissions("monitor:job:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SysJob job) {
		startPage();
		List<SysJob> list = jobService.selectJobList(job);
		return getDataTable(list);
	}
	
	/**
	 * 定时任务添加
	 * @return
	 */
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}
	
	/**
	 * 定时任务添加保存
	 * @param job
	 * @return
	 * @throws TaskException 
	 * @throws SchedulerException 
	 */
	@Log(title = "定时任务", businessType = BusinessType.INSERT)
	@RequiresPermissions("monitor:job:add")
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(SysJob job) throws SchedulerException, TaskException {
		if(!jobService.checkCronExpressionIsValid(job.getCronExpression())) {
			return error("添加定时任务 '" + job.getJobName() + "'失败，cron表达式不正确");
		}
		return toAjax(jobService.insertJob(job));
	}
	
	/**
	 * 定时任务编辑
	 * @param jobId
	 * @param mmap
	 * @return
	 */
	@GetMapping("/edit/{jobId}")
	public String edit(@PathVariable("jobId") Long jobId, ModelMap mmap) {
		mmap.put("job", jobService.selectJobById(jobId));
		return prefix + "/edit";
	}
	
	/**
	 * 定时任务编辑保存
	 * @param job
	 * @return
	 * @throws TaskException 
	 * @throws SchedulerException 
	 */
	@Log(title = "定时任务", businessType = BusinessType.UPDATE)
	@RequiresPermissions("monitor:job:edit")
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(@Validated SysJob job) throws SchedulerException, TaskException {
		if(!jobService.checkCronExpressionIsValid(job.getCronExpression())) {
			return error("修改定时任务 '" + job.getJobName() + "'失败，cron表达式不正确");
		}
		return toAjax(jobService.updateJob(job));
	}
	
	/**
	 * 任务调度状态修改
	 * @param job
	 * @return
	 * @throws SchedulerException 
	 */
	@Log(title = "定时任务", businessType = BusinessType.UPDATE)
	@RequiresPermissions("monitor:job:changeStatus")
	@PostMapping("/changeStatus")
	@ResponseBody
	public AjaxResult changeStatus(SysJob job) throws SchedulerException {
		SysJob newJob = jobService.selectJobById(job.getJobId());
        newJob.setStatus(job.getStatus());
        return toAjax(jobService.changeStatus(newJob));
	}
	
	/**
	 * 任务调度执行一次
	 * @param job
	 * @return
	 * @throws SchedulerException 
	 */
	@Log(title = "定时任务", businessType = BusinessType.UPDATE)
	@RequiresPermissions("monitor:job:changeStatus")
	@PostMapping("/run")
	@ResponseBody
	public AjaxResult run(SysJob job) throws SchedulerException {
		jobService.run(job);
        return success();
	}
	
	/**
	 * 定时任务详情
	 * @param jobId
	 * @param mmap
	 * @return
	 */
	@RequiresPermissions("monitot:job:detail")
	@GetMapping("/detail/{jobId}")
	public String detail(@PathVariable("jobId") Long jobId, ModelMap mmap) {
		mmap.put("name", "job");
        mmap.put("job", jobService.selectJobById(jobId));
        return prefix + "/detail";
	}
	
	/**
	 * 移除任务调度
	 * @param ids
	 * @return
	 * @throws SchedulerException 
	 */
	@Log(title = "定时任务", businessType = BusinessType.DELETE)
	@RequiresPermissions("monitor:job:remove")
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) throws SchedulerException {
		jobService.deleteJobByIds(ids);
        return success();
	}
	
	/**
	 * 导出任务调度
	 * @param job
	 * @return
	 */
	@Log(title = "定时任务", businessType = BusinessType.EXPORT)
	@RequiresPermissions("monitor:job:export")
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(SysJob job) {
		List<SysJob> list = jobService.selectJobList(job);
		ExcelUtil<SysJob> util = new ExcelUtil<SysJob>(SysJob.class);
		return util.exportExcel(list, "定时任务");
	}
	
	/**
	 * 验证cron表达式是否正确
	 * @param job
	 * @return
	 */
	@PostMapping("/checkCronExpressionIsValid")
    @ResponseBody
	public boolean checkCronExpressionIsValid(SysJob job) {
		return jobService.checkCronExpressionIsValid(job.getCronExpression());
	}
}
