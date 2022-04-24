package com.tearsky.common.enums;

/**
 * 	用户回话
 * @author tearsky
 *
 */
public enum OnlineStatus {

	on_line("在线"), off_line("离线");
	
	private final String info;

	private OnlineStatus(String info) {
		this.info = info;
	}
	
	public String getInfo() {
		return info;
	}
}
