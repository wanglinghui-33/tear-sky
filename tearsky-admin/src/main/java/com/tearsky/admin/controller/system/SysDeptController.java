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
import com.tearsky.common.core.domain.Ztree;
import com.tearsky.common.enums.BusinessType;
import com.tearsky.core.util.ShiroUtils;
import com.tearsky.system.domain.SysDept;
import com.tearsky.system.domain.SysRole;
import com.tearsky.system.service.ISysDeptService;

/**
 * 部门信息
 * @author tearsky
 *
 */
@Controller
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController{

	private String prefix = "system/dept";
	
	@Autowired
	private ISysDeptService deptService;
	
	/**
	 * 部门信息列表页面
	 * @return
	 */
	@RequiresPermissions("system:dept:view")
	@GetMapping()
	public String dept() {
		return prefix + "/dept";
	}
	
	/**
	 * 部门信息列表
	 * @param dept
	 * @return
	 */
	@RequiresPermissions("system:dept:list")
	@PostMapping("/list")
	@ResponseBody
	public List<SysDept> list(SysDept dept){
		List<SysDept> list = deptService.selectDeptList(dept);
		return list;
	}
	
	/**
	 * 新增部门
	 * @param parentId
	 * @param mmap
	 * @return
	 */
	@GetMapping("/add/{parentId}")
	public String add(@PathVariable("parentId") Long parentId, ModelMap mmap) {
		mmap.put("dept", deptService.selectDeptById(parentId));
		return prefix + "/add";
	}
	
	/**
	 * 新增部门保存
	 * @param dept
	 * @return
	 */
	@Log(title = "部门管理", businessType = BusinessType.INSERT)
	@RequiresPermissions("system:dept:add")
	@PostMapping("/add")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult addSave(@Validated SysDept dept) {
		if(UserConstants.DEPT_NAME_NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
			return error("新增部门 ' " + dept.getDeptName() + " ' 失败，部门名称已存在");
		}
		dept.setCreateBy(ShiroUtils.getLoginName());
		return toAjax(deptService.insertDept(dept));
	}
	
	/**
	 * 修改部门信息
	 * @param deptId
	 * @param mmap
	 * @return
	 */
	@GetMapping("/edit/{deptId}")
	public String edit(@PathVariable("deptId") Long deptId, ModelMap mmap) {
		SysDept dept = deptService.selectDeptById(deptId);
		mmap.put("dept", dept);
		return prefix + "/edit";
	}
	
	/**
	 * 修改部门信息保存
	 * @param dept
	 * @return
	 */
	@Log(title = "部门管理", businessType = BusinessType.UPDATE)
	@RequiresPermissions("system:dept:edit")
	@PostMapping("/edit")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult eidtSave(@Validated SysDept dept) {
		if(UserConstants.DEPT_NAME_NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
			return error("修改部门 ' " + dept.getDeptName() + " ' 失败，部门名称已存在");
		}else if(dept.getParentId().equals(dept.getDeptId())) {
			return error("修改部门 ' " + dept.getDeptName() + " ' 失败，上级部门不能是自己");
		}
		dept.setUpdateBy(ShiroUtils.getLoginName());
		return toAjax(deptService.updateDept(dept));
	}
	
	/**
	 * 删除部门
	 * @param deptId
	 * @return
	 */
	@Log(title = "部门管理", businessType = BusinessType.DELETE)
	@RequiresPermissions("system:dept:remove")
	@GetMapping("/remove/{deptId}")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult remove(@PathVariable("deptId") Long deptId) {
		if(deptService.selectDeptCount(deptId) > 0) {
			return AjaxResult.warn("存在下级部门,不能删除");
		}else if(deptService.checkDeptExistUser(deptId)) {
			return AjaxResult.warn("部门存在用户,不能删除");
		}
		return toAjax(deptService.deleteDeptById(deptId));
	}
	
	/**
	 * 加载部门列表树
	 * @return
	 */
	@GetMapping("/treeData")
	@ResponseBody
	public List<Ztree> treeData(){
		List<Ztree> ztrees = deptService.selectDeptTree(new SysDept());
		return ztrees;
	}
	
	/**
	 * 选择部门树
	 * @param deptId
	 * @param mmap
	 * @return
	 */
	@GetMapping("/selectDeptTree/{deptId}")
	public String selectDeptTree(@PathVariable("deptId") Long deptId, ModelMap mmap) {
		mmap.put("dept", deptService.selectDeptById(deptId));
		return prefix + "/tree";
	}
	
	/**
	 * 加载角色部门（数据权限）列表树
	 * @param role
	 * @return
	 */
	@GetMapping("/roleDeptTreeData")
	@ResponseBody
	public List<Ztree> deptTreeData(SysRole role){
		List<Ztree> ztrees = deptService.roleDeptTreeData(role);
		return ztrees;
	}
	
	/**
	 * 检验部门名称
	 * @param dept
	 * @return
	 */
	@PostMapping("/checkDeptNameUnique")
	@ResponseBody
	public String checkDeptNameUnique(SysDept dept) {
		return deptService.checkDeptNameUnique(dept);
	}
}
