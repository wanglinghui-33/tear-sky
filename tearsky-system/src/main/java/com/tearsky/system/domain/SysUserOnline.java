package com.tearsky.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.tearsky.common.core.domain.BaseEntity;
import com.tearsky.common.enums.OnlineStatus;

import java.util.Date;

/**
 * 在线用户记录表 sys_user_online
 * 
 * @author tearsky
 * @date 2019-08-22
 */
public class SysUserOnline extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	/** 用户会话id */
	private String sessionId;
	/** 登录账号 */
	private String loginName;
	/** 部门名称 */
	private String deptName;
	/** 登录IP地址 */
	private String ipaddr;
	/** 登录地点 */
	private String loginLocation;
	/** 浏览器类型 */
	private String browser;
	/** 操作系统 */
	private String os;
	/** 在线状态（on_line在线 off_line离线） */
	private OnlineStatus status = OnlineStatus.on_line;
	/** session创建时间 */
	private Date startTimestamp;
	/** session最后访问时间 */
	private Date lastAccessTime;
	/** 超时时间，单位为分钟 */
	private Long expireTime;

	public void setSessionId(String sessionId){
		this.sessionId = sessionId;
	}

	public String getSessionId(){
		return sessionId;
	}
	public void setLoginName(String loginName){
		this.loginName = loginName;
	}

	public String getLoginName(){
		return loginName;
	}
	public void setDeptName(String deptName){
		this.deptName = deptName;
	}

	public String getDeptName(){
		return deptName;
	}
	public void setIpaddr(String ipaddr){
		this.ipaddr = ipaddr;
	}

	public String getIpaddr(){
		return ipaddr;
	}
	public void setLoginLocation(String loginLocation){
		this.loginLocation = loginLocation;
	}

	public String getLoginLocation(){
		return loginLocation;
	}
	public void setBrowser(String browser){
		this.browser = browser;
	}

	public String getBrowser(){
		return browser;
	}
	public void setOs(String os){
		this.os = os;
	}

	public String getOs(){
		return os;
	}
	public void setStatus(OnlineStatus status){
		this.status = status;
	}

	public OnlineStatus getStatus(){
		return status;
	}
	public void setStartTimestamp(Date startTimestamp){
		this.startTimestamp = startTimestamp;
	}

	public Date getStartTimestamp(){
		return startTimestamp;
	}
	public void setLastAccessTime(Date lastAccessTime){
		this.lastAccessTime = lastAccessTime;
	}

	public Date getLastAccessTime(){
		return lastAccessTime;
	}
	public void setExpireTime(Long expireTime){
		this.expireTime = expireTime;
	}

	public Long getExpireTime(){
		return expireTime;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("sessionId", getSessionId())
            .append("loginName", getLoginName())
            .append("deptName", getDeptName())
            .append("ipaddr", getIpaddr())
            .append("loginLocation", getLoginLocation())
            .append("browser", getBrowser())
            .append("os", getOs())
            .append("status", getStatus())
            .append("startTimestamp", getStartTimestamp())
            .append("lastAccessTime", getLastAccessTime())
            .append("expireTime", getExpireTime())
            .toString();
    }
}
