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
import com.tearsky.system.domain.SysPost;
import com.tearsky.system.service.ISysPostService;

/**
 * 岗位 信息
 * @author tearsky
 *
 */
@Controller
@RequestMapping("/system/post")
public class SysPostController extends BaseController{

	private String prefix = "system/post";
	
	@Autowired
	private ISysPostService postService;
	
	
	@RequiresPermissions("system:post:view")
	@GetMapping()
	public String post() {
		return prefix + "/post";
	}
	
	@RequiresPermissions("system:post:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SysPost post) {
		startPage();
		List<SysPost> list = postService.selectPostList(post);
		return getDataTable(list);
	}
	
	/**
	 * 岗位添加
	 */
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}
	
	/**
	 * 岗位添加保存
	 * @param post
	 * @return
	 */
	@Log(title = "岗位管理", businessType = BusinessType.INSERT)
	@RequiresPermissions("system:post:add")
	@PostMapping("/add")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult addSave(SysPost post) {
		if(UserConstants.POST_NAME_NOT_UNIQUE.equals(postService.checkPostNameUnique(post))) {
			return error("新增岗位 ' " + post.getPostName() + " ' 失败，岗位名称已存在");
		}else if(UserConstants.POST_CODE_NOT_UNIQUE.equals(postService.checkPostCodeUnique(post))) {
			return error("新增岗位 ' " + post.getPostName() + " ' 失败，岗位编码已存在");
		}
		post.setCreateBy(ShiroUtils.getLoginName());
		return toAjax(postService.insertPost(post));
	}
	
	/**
	 * 编辑岗位
	 * @param postId
	 * @param mmap
	 * @return
	 */
	@GetMapping("/edit/{postId}")
	public String edit(@PathVariable("postId") Long postId, ModelMap mmap) {
		mmap.put("post", postService.selectPostById(postId));
		return prefix + "/edit";
	}
	
	/**
	 * 修改保存岗位
	 * @param post
	 * @return
	 */
	@Log(title = "岗位管理", businessType = BusinessType.UPDATE)
	@RequiresPermissions("system:post:edit")
	@PostMapping("/edit")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult editSave(@Validated SysPost post) {
		if(UserConstants.POST_NAME_NOT_UNIQUE.equals(postService.checkPostNameUnique(post))) {
			return error("修改岗位 ' " + post.getPostName() + " ' 失败，岗位名称已存在");
		}else if(UserConstants.POST_CODE_NOT_UNIQUE.equals(postService.checkPostCodeUnique(post))) {
			return error("修改岗位 ' " + post.getPostName() + " ' 失败，岗位编码已存在");
		}
		post.setUpdateBy(ShiroUtils.getLoginName());
		return toAjax(postService.updatePost(post));
	}
	
	/**
	 * 删除岗位信息
	 * @param ids
	 * @return
	 */
	@Log(title = "岗位管理", businessType = BusinessType.DELETE)
	@RequiresPermissions("system:post:remove")
	@PostMapping("/remove")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult remove(String ids) {
		try {
			return toAjax(postService.deletePostByIds(ids));
		} catch (Exception e) {
			return error(e.getMessage());
		}
	}
	
	/**
	 * 导出岗位
	 * @param post
	 * @return
	 */
	@Log(title = "岗位管理", businessType = BusinessType.EXPORT)
	@RequiresPermissions("system:post:export")
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(SysPost post) {
		List<SysPost> list = postService.selectPostList(post);
		ExcelUtil<SysPost> util = new ExcelUtil<SysPost>(SysPost.class);
		return util.exportExcel(list, "岗位管理");
	}
	
	/**
	 * 校验岗位名称
	 * @param post
	 * @return
	 */
	@PostMapping("/checkPostNameUnique")
	@ResponseBody
	public String checkPostNameUnique(SysPost post) {
		return postService.checkPostNameUnique(post);
	}
	
	/**
	 * 检验岗位编码
	 * @param post
	 * @return
	 */
	@PostMapping("/checkPostCodeUnique")
	@ResponseBody
	public String checkPostCodeUnique(SysPost post) {
		return postService.checkPostCodeUnique(post);
	}
}
