package com.tearsky.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.tearsky.common.core.domain.BaseEntity;

/**
 * 角色和菜单关联表 sys_role_menu
 * 
 * @author tearsky
 * @date 2019-08-22
 */
public class SysRoleMenu extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	/** 主键ID */
	private Long roleMenuId;
	/** 角色ID */
	private Long roleId;
	/** 菜单ID */
	private Long menuId;

	public void setRoleMenuId(Long roleMenuId){
		this.roleMenuId = roleMenuId;
	}

	public Long getRoleMenuId(){
		return roleMenuId;
	}
	public void setRoleId(Long roleId){
		this.roleId = roleId;
	}

	public Long getRoleId(){
		return roleId;
	}
	public void setMenuId(Long menuId){
		this.menuId = menuId;
	}

	public Long getMenuId(){
		return menuId;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roleMenuId", getRoleMenuId())
            .append("roleId", getRoleId())
            .append("menuId", getMenuId())
            .toString();
    }
}
