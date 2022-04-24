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
import com.tearsky.common.core.controller.BaseController;
import com.tearsky.common.core.domain.AjaxResult;
import com.tearsky.common.core.page.TableDataInfo;
import com.tearsky.common.enums.BusinessType;
import com.tearsky.common.utils.poi.ExcelUtil;
import com.tearsky.core.util.ShiroUtils;
import com.tearsky.system.domain.SysDictData;
import com.tearsky.system.service.ISysDictDataService;

/**
 * 数据字典信息
 * @author tearsky
 *
 */
@Controller
@RequestMapping("/system/dict/data")
public class SysDictDataController extends BaseController {

	private String prefix = "system/dict/data";
	
	@Autowired
	private ISysDictDataService dictDataService;
	
	/**
	 * 字典数据列表页面
	 * @return
	 */
	@RequiresPermissions("system:dict:view")
	@GetMapping()
	public String dictData() {
		return prefix + "/data";
	}
	
	/**
	 * 字典数据列表
	 * @param dictData
	 * @return
	 */
	@RequiresPermissions("system:dict:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SysDictData dictData) {
		startPage();
		List<SysDictData> list = dictDataService.selectDictDataList(dictData);
		return getDataTable(list);
	}
	
	/**
	 * 新增字典类型
	 * @param dictType
	 * @param mmap
	 * @return
	 */
	@GetMapping("/add/{dictType}")
	public String add(@PathVariable("dictType") String dictType, ModelMap mmap) {
		mmap.put("dictType", dictType);
		return prefix + "/add";
	}
	
	@Log(title = "字典数据", businessType = BusinessType.INSERT)
	@RequiresPermissions("system:dict:add")
	@PostMapping("/add")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult addSave(@Validated SysDictData dictData) {
		dictData.setCreateBy(ShiroUtils.getLoginName());
		return toAjax(dictDataService.insertDictData(dictData));
	}
	
	/**
	 * 修改字典类型
	 * @param dictCode
	 * @param mmap
	 * @return
	 */
	@GetMapping("/edit/{dictCode}")
	public String edit(@PathVariable Long dictCode, ModelMap mmap) {
		mmap.put("dict", dictDataService.selectDictDataById(dictCode));
		return prefix + "/edit";
	}
	
	/**
	 * 保存字典类型
	 * @param dictData
	 * @return
	 */
	@Log(title = "字典数据", businessType = BusinessType.UPDATE)
	@RequiresPermissions("system:dict:edit")
	@PostMapping("/edit")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult editSave(@Validated SysDictData dictData) {
		dictData.setUpdateBy(ShiroUtils.getLoginName());
		return toAjax(dictDataService.updateDictData(dictData));
	}
	
	/**
	 * 删除字典数据信息
	 * @param ids
	 * @return
	 */
	@Log(title = "字典数据", businessType = BusinessType.DELETE)
	@RequiresPermissions("system:dict:remove")
	@PostMapping("/remove")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult remove(String ids) {
		return toAjax(dictDataService.deleteDictDataByIds(ids));
	}
	
	/**
	 * 字典数据导出
	 * @param dictData
	 * @return
	 */
	@Log(title = "字典数据", businessType = BusinessType.EXPORT)
	@RequiresPermissions("system:dict:export")
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(SysDictData dictData) {
		List<SysDictData> list = dictDataService.selectDictDataList(dictData);
		ExcelUtil<SysDictData> util = new ExcelUtil<SysDictData>(SysDictData.class);
		return util.exportExcel(list, "字典数据");
	}
}
