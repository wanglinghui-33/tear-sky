package com.tearsky.common.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.tearsky.common.utils.ServletUtils;

/**
 * 服务相关配置
 * @author tearsky
 *
 */
@Component
public class ServerConfig {

	/**
	 * 获取完整的请求路径，包括：域名，端口号，上下文访问路径
	 * @return
	 */
	public String getUrl() {
		HttpServletRequest request = ServletUtils.getRequest();
		return getDomain(request);
	}
	
	public String getDomain(HttpServletRequest request) {
		StringBuffer url = request.getRequestURL();
		String contextPath = request.getServletContext().getContextPath();
		return url.delete(url.length() - request.getRequestURI().length(), url.length()).append(contextPath).toString();
	}
}
