package com.tearsky.common.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.tearsky.common.utils.spring.SpringUtils;

/**
 * 获取i18n资源文件
 * @author tearsky
 *
 */
public class MessageUtils {

	/**
	 * 更加消息键和参数，获取消息 委托给spring messageSource
	 * @param code
	 * @param args
	 * @return
	 */
	public static String message(String code, Object...args) {
		MessageSource messageSource = SpringUtils.getBean(MessageSource.class);
		return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
	}
}
