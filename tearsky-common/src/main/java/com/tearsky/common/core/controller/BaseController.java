package com.tearsky.common.core.controller;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tearsky.common.core.domain.AjaxResult;
import com.tearsky.common.core.domain.AjaxResult.Type;
import com.tearsky.common.core.page.PageDomain;
import com.tearsky.common.core.page.TableDataInfo;
import com.tearsky.common.core.page.TableSupport;
import com.tearsky.common.utils.DateUtils;
import com.tearsky.common.utils.ServletUtils;
import com.tearsky.common.utils.StringUtils;
import com.tearsky.common.utils.sql.SqlUtil;

/**
 * web 层通用数据处理
 * 
 * @author tearsky
 *
 */
public class BaseController {

	protected final Logger logger = LoggerFactory.getLogger(BaseController.class);

	/**
	 * 将前台传递过来的日期格式的字符串，自动转化为Date类型
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateUtils.parseDate(text));
			}
		});
	}

	/**
	 * 设置请求分页数据
	 */
	protected void startPage() {
		PageDomain pageDomain = TableSupport.buildPageRequest();
		Integer pageNum = pageDomain.getPageNum();
		Integer pageSize = pageDomain.getPageSize();
		if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
			String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
			PageHelper.startPage(pageNum, pageSize, orderBy);
		}
	}

	/**
	 * 获取request
	 */
	public HttpServletRequest getRequest() {
		return ServletUtils.getRequest();
	}

	/**
	 * 获取response
	 */
	public HttpServletResponse getResponse() {
		return ServletUtils.getResponse();
	}

	/**
	 * 获取session
	 */
	public HttpSession getSession() {
		return getRequest().getSession();
	}

	/**
	 * 响应请求分页数据
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected TableDataInfo getDataTable(List<?> list) {
		TableDataInfo rspData = new TableDataInfo();
		rspData.setCode(0);
		rspData.setRows(list);
		rspData.setTotal(new PageInfo(list).getTotal());
		return rspData;
	}
	
	/**
	 * 返回成功
	 * @return
	 */
	public AjaxResult success() {
		return AjaxResult.success();
	}
	
	/**
	 * 返回失败
	 * @return
	 */
	public AjaxResult error() {
		return AjaxResult.error();
	}
	
	/**
	 * 返回成功消息
	 * @param message
	 * @return
	 */
	public AjaxResult success(String message) {
		return AjaxResult.success(message);
	}
	
	/**
	 * 返回失败消息
	 * @param message
	 * @return
	 */
	public AjaxResult error(String message) {
		return AjaxResult.error(message);
	}
	
	/**
	 * 返回错误码消息
	 * @param type
	 * @param message
	 * @return
	 */
	public AjaxResult error(Type type, String message) {
		return new AjaxResult(type, message);
	}
	
	/**
	 * 响应返回结果
	 * @param rows
	 * @return
	 */
	protected AjaxResult toAjax(int rows) {
		return rows > 0 ? success() : error(); 
	}
	
	/**
	 * 响应返回结果
	 * @param result
	 * @return
	 */
	protected AjaxResult toAjax(boolean result) {
		return result ? success() : error();
	}
	
	/**
	 * 页面跳转
	 * @param url
	 * @return
	 */
	public String redirect(String url) {
		return StringUtils.format("redirect:{}", url);
	}
}
