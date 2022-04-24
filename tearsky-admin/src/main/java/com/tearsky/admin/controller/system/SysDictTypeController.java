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
import com.tearsky.system.domain.SysDictType;
import com.tearsky.system.service.ISysDictTypeService;

/**
 * 数据字典信息
 * @author tearsky
 *
 */
@Controller
@RequestMapping("/system/dict")
public class SysDictTypeController extends BaseController {

	private String prefix = "system/dict/type";
	
	@Autowired
	private ISysDictTypeService dictTypeService;
	
	/**
	 * 数据字典列表页面
	 * @return
	 */
	@RequiresPermissions("system:dict:view")
	@GetMapping()
	public String dictType() {
		return prefix + "/type";
	}
	
	/**
	 * 数据字典列表
	 * @param dictType
	 * @return
	 */
	@RequiresPermissions("system:dict:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SysDictType dictType) {
		startPage();
		List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
		return getDataTable(list);
	}
	
	/**
	 * 新增字典类型
	 * @return
	 */
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}
	
	@Log(title = "字典类型", businessType = BusinessType.INSERT)
	@RequiresPermissions("system:dict:add")
	@PostMapping("/add")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult addSave(@Validated SysDictType dictType) {
		if(UserConstants.DICT_TYPE_NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dictType))) {
			return error("新增字典'" + dictType.getDictName() + "'失败，字典类型已存在");
		}
		dictType.setCreateBy(ShiroUtils.getLoginName());
		return toAjax(dictTypeService.insertDictType(dictType));
	}
	
	/**
	 * 检验字典类型是否唯一
	 * @param dictType
	 * @return
	 */
	@PostMapping("/checkDictTypeUnique")
	@ResponseBody
	public String checkDictTypeUnique(SysDictType dictType) {
		return dictTypeService.checkDictTypeUnique(dictType);
	}
	
	/**
	 * 查询字典详情
	 * @param dictId
	 * @param mmap
	 * @return
	 */
	@RequiresPermissions("system:dict:list")
	@GetMapping("/detail/{dictId}")
	public String detail(@PathVariable("dictId") Long dictId, ModelMap mmap) {
		mmap.put("dict", dictTypeService.selectDictTypeById(dictId));
		mmap.put("dictList", dictTypeService.selectDictTypeAll());
		return "system/dict/data/data";
	}
	
	/**
	 * 修改字典类型
	 * @param dictId
	 * @param mmap
	 * @return
	 */
	@GetMapping("/edit/{dictId}")
	public String edit(@PathVariable("dictId") Long dictId, ModelMap mmap) {
		mmap.put("dict", dictTypeService.selectDictTypeById(dictId));
		return prefix + "/edit";
	}
	
	/**
	 * 修改保存字段类型
	 * @param dictType
	 * @return
	 */
	@Log(title = "字典类型", businessType = BusinessType.UPDATE)
	@RequiresPermissions("system:dict:edit")
	@PostMapping("/edit")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult editSave(@Validated SysDictType dictType) {
		if(UserConstants.DICT_TYPE_NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dictType))) {
			return error("修改字典'" + dictType.getDictName() + "'失败，字典类型已存在");
		}
		dictType.setUpdateBy(ShiroUtils.getLoginName());
		return toAjax(dictTypeService.updateDictType(dictType));
	}
	
	/**
	 * 删除字典类型
	 * @param ids
	 * @return
	 */
	@Log(title = "字典类型", businessType = BusinessType.DELETE)
	@RequiresPermissions("system:dict:remove")
	@PostMapping("/remove")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult remove(String ids) {
		try {
			return toAjax(dictTypeService.deleteDictTypeByIds(ids));
		} catch (Exception e) {
			return error(e.getMessage());
		}
	}
	
	/**
	 * 字典类型导出
	 * @param dictType
	 * @return
	 */
	@Log(title = "字典类型", businessType = BusinessType.EXPORT)
	@RequiresPermissions("system:dict:export")
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(SysDictType dictType) {
		List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
		ExcelUtil<SysDictType> util = new ExcelUtil<SysDictType>(SysDictType.class);
		return util.exportExcel(list, "字典类型");
	}
}
