package com.tearsky.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.tearsky.common.core.domain.BaseEntity;

/**
 * 用户和角色关联表 sys_user_role
 * 
 * @author tearsky
 * @date 2019-08-22
 */
public class SysUserRole extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	/** 主键ID */
	private Long userRoleId;
	/** 用户ID */
	private Long userId;
	/** 角色ID */
	private Long roleId;

	public void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Long getUserRoleId(){
		return userRoleId;
	}
	public void setUserId(Long userId){
		this.userId = userId;
	}

	public Long getUserId(){
		return userId;
	}
	public void setRoleId(Long roleId){
		this.roleId = roleId;
	}

	public Long getRoleId(){
		return roleId;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userRoleId", getUserRoleId())
            .append("userId", getUserId())
            .append("roleId", getRoleId())
            .toString();
    }
}
