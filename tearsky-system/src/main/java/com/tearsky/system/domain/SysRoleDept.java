package com.tearsky.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.tearsky.common.core.domain.BaseEntity;

/**
 * 角色和部门关系表 sys_role_dept
 * 
 * @author tearsky
 * @date 2019-08-22
 */
public class SysRoleDept extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	/** 主键ID */
	private Long roleDeptId;
	/** 角色ID */
	private Long roleId;
	/** 部门ID */
	private Long deptId;

	public void setRoleDeptId(Long roleDeptId){
		this.roleDeptId = roleDeptId;
	}

	public Long getRoleDeptId(){
		return roleDeptId;
	}
	public void setRoleId(Long roleId){
		this.roleId = roleId;
	}

	public Long getRoleId(){
		return roleId;
	}
	public void setDeptId(Long deptId){
		this.deptId = deptId;
	}

	public Long getDeptId(){
		return deptId;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roleDeptId", getRoleDeptId())
            .append("roleId", getRoleId())
            .append("deptId", getDeptId())
            .toString();
    }
}
