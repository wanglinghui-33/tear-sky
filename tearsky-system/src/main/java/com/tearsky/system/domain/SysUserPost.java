package com.tearsky.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.tearsky.common.core.domain.BaseEntity;

/**
 * 用户与岗位关系表 sys_user_post
 * 
 * @author tearsky
 * @date 2019-08-22
 */
public class SysUserPost extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	/** 主键ID */
	private Long userPostId;
	/** 用户ID */
	private Long userId;
	/** 岗位ID */
	private Long postId;

	public void setUserPostId(Long userPostId){
		this.userPostId = userPostId;
	}

	public Long getUserPostId(){
		return userPostId;
	}
	public void setUserId(Long userId){
		this.userId = userId;
	}

	public Long getUserId(){
		return userId;
	}
	public void setPostId(Long postId){
		this.postId = postId;
	}

	public Long getPostId(){
		return postId;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userPostId", getUserPostId())
            .append("userId", getUserId())
            .append("postId", getPostId())
            .toString();
    }
}
