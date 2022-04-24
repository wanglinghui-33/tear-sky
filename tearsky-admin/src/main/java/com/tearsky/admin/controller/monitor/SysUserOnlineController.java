package com.tearsky.admin.controller.monitor;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tearsky.common.annotation.Log;
import com.tearsky.common.core.controller.BaseController;
import com.tearsky.common.core.domain.AjaxResult;
import com.tearsky.common.core.page.TableDataInfo;
import com.tearsky.common.enums.BusinessType;
import com.tearsky.common.enums.OnlineStatus;
import com.tearsky.core.shiro.session.OnlineSession;
import com.tearsky.core.shiro.session.OnlineSessionDAO;
import com.tearsky.core.util.ShiroUtils;
import com.tearsky.system.domain.SysUserOnline;
import com.tearsky.system.service.ISysUserOnlineService;

/**
 * 在线用户监控
 * @author tearsky
 *
 */
@Controller
@RequestMapping("/monitor/online")
public class SysUserOnlineController extends BaseController{

	private String prefix = "monitor/online";
	
	@Autowired
	private ISysUserOnlineService userOnlineService;
	
	@Autowired
	private OnlineSessionDAO onlineSessionDAO;
	
	/**
	 * 在线用户列表页面
	 * @return
	 */
	@RequiresPermissions("monitor:online:view")
	@GetMapping()
	public String online() {
		return prefix + "/online";
	}
	
	/**
	 * 在线用户列表
	 * @param userOnline
	 * @return
	 */
	@RequiresPermissions("monitor:online:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SysUserOnline userOnline) {
		startPage();
		List<SysUserOnline> list = userOnlineService.selectUserOnlineList(userOnline);
		return getDataTable(list);
	}
	
	/**
	 * 批量用户强退
	 * @param ids
	 * @return
	 */
	@Log(title = "在线用户", businessType = BusinessType.FORCE)
	@RequiresPermissions("monitor:online:batchForceLogout")
	@PostMapping("/batchForceLogout")
	@ResponseBody
	public AjaxResult batchForceLogout(@RequestParam("ids[]") String[] ids) {
		for(String sessionId : ids) {
			SysUserOnline online = userOnlineService.selectOnlineById(sessionId);
			if(online == null) {
				return error("用户已下线");
			}
			OnlineSession onlineSession = (OnlineSession) onlineSessionDAO.readSession(online.getSessionId());
			if(onlineSession == null) {
				return error("用户已下线");
			}
			if(sessionId.equals(ShiroUtils.getSessionId())) {
				return error("当前登录用户无法强退");
			}
			onlineSession.setStatus(OnlineStatus.off_line);
			onlineSessionDAO.update(onlineSession);
			online.setStatus(OnlineStatus.off_line);
			userOnlineService.saveOnline(online);
		}
		return success();
	}
	
	/**
	 * 强退用户
	 * @param sessionId
	 * @return
	 */
	@Log(title = "在线用户", businessType = BusinessType.FORCE)
	@RequiresPermissions("monitor:online:forceLogout")
	@PostMapping("/forceLogout")
	@ResponseBody
	public AjaxResult forceLogout(String sessionId) {
		SysUserOnline online = userOnlineService.selectOnlineById(sessionId);
		if(sessionId.equals(ShiroUtils.getSessionId())) {
			return error("当前登录用户无法强退");
		}
		if(online == null) {
			return error("用户已下线");
		}
		OnlineSession onlineSession = (OnlineSession) onlineSessionDAO.readSession(online.getSessionId());
		if(onlineSession == null) {
			return error("用户已下线");
		}
		onlineSession.setStatus(OnlineStatus.off_line);
        onlineSessionDAO.update(onlineSession);
        online.setStatus(OnlineStatus.off_line);
        userOnlineService.saveOnline(online);
        return success();
	}
}
