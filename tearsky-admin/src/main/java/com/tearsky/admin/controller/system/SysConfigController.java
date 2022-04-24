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
import com.tearsky.system.domain.SysConfig;
import com.tearsky.system.service.ISysConfigService;

/**
 * 参数配置
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/system/config")
public class SysConfigController extends BaseController{

	private String prefix = "system/config";
	
	@Autowired
	private ISysConfigService configService;
	
	/**
	 * 参数配置列表页面
	 * @return
	 */
	@RequiresPermissions("system:config:view")
	@GetMapping()
	public String config() {
		return prefix + "/config";
	}
	
	/**
	 * 参数配置列表
	 * @param config
	 * @return
	 */
	@RequiresPermissions("system:config:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SysConfig config) {
		startPage();
		List<SysConfig> list = configService.selectConfigList(config);
		return getDataTable(list);
	}
	
	/**
	 * 添加参数设置
	 * @return
	 */
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}
	
	/**
	 * 添加参数设置保存
	 * @param config
	 * @return
	 */
	@Log(title = "参数管理", businessType = BusinessType.INSERT)
	@RequiresPermissions("system:config:add")
	@PostMapping("/add")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult addSave(@Validated SysConfig config) {
		if(UserConstants.CONFIG_KEY_NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
			return error("新增参数'" + config.getConfigName() + "'失败，参数名称已存在");
		}
		config.setCreateBy(ShiroUtils.getLoginName());
		return toAjax(configService.insertConfig(config));
	}
	
	/**
	 * 编辑参数设置
	 * @param config
	 * @return
	 */
	@GetMapping("/edit/{configId}")
	public String edit(@PathVariable("configId") Long configId, ModelMap mmap) {
		mmap.put("config", configService.selectConfigById(configId));
		return prefix + "/edit";
	}
	
	/**
	 * 编辑参数设置保存
	 * @param config
	 * @return
	 */
	@Log(title = "参数管理", businessType = BusinessType.UPDATE)
	@RequiresPermissions("system:config:edit")
	@PostMapping("/edit")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult editSave(@Validated SysConfig config) {
		if(UserConstants.CONFIG_KEY_NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
			return error("修改参数'" + config.getConfigName() + "'失败，参数名称已存在");
		}
		config.setUpdateBy(ShiroUtils.getLoginName());
		return toAjax(configService.updateConfig(config));
	}
	
	/**
	 * 删除参数设置
	 * @param ids
	 * @return
	 */
	@Log(title = "参数管理", businessType = BusinessType.DELETE)
	@RequiresPermissions("system:config:remove")
	@PostMapping("/remove")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult remove(String ids) {
		return toAjax(configService.deleteConfigByIds(ids));
	}
	
	/**
	 * 导出参数设置
	 * @param config
	 * @return
	 */
	@Log(title = "参数管理", businessType = BusinessType.EXPORT)
	@RequiresPermissions("system:config:export")
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(SysConfig config) {
		List<SysConfig> list = configService.selectConfigList(config);
		ExcelUtil<SysConfig> util = new ExcelUtil<SysConfig>(SysConfig.class);
		return util.exportExcel(list, "参数管理");
	}
	
	/**
	 * 校验参数键名
	 * @param config
	 * @return
	 */
	@PostMapping("/checkConfigKeyUnique")
	@ResponseBody
	public String checkConfigKeyUnique(SysConfig config) {
		return configService.checkConfigKeyUnique(config);
	}
}
