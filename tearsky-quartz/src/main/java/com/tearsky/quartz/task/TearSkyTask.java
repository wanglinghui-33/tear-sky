package com.tearsky.quartz.task;

import org.springframework.stereotype.Component;

import com.tearsky.common.utils.StringUtils;

/**
 * 定时任务调度
 * 
 * @author tearsky
 *
 */
@Component("tearSkyTask")
public class TearSkyTask {

	public void noParams() {
		System.out.println("执行无参方法");
	}

	public void params(String params) {
		System.out.println("执行有参方法：" + params);
	}

	public void multipleParams(String s, Boolean b, Long l, Double d, Integer i) {
		System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
	}

}
