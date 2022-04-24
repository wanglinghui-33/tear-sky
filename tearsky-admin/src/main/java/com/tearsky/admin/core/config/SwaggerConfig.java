package com.tearsky.admin.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tearsky.common.config.Global;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2的接口配置
 * @author tearsky
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	/**
	 * 创建API
	 */
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				// 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
				.apiInfo(apiInfo())
				// 设置哪些接口暴露给Swagger展示
				.select()
				// 扫描所有有注解的api，用这种方式更灵活
				.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
				// 扫描所有 .apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build();
	}

	private ApiInfo apiInfo() {
		//用于ApiInfoBuilder进行定制
		return new ApiInfoBuilder()
				//设置标题
				.title("标题：管理系统接口文档")
				//描述
				.description("描述：用于管理集团旗下公司的人员信息,具体包括XXX,XXX模块...")
				//作者信息
				.contact(new Contact(Global.getName(), null, null))
				//版本
				.version("版本号:" + Global.getVersion()).build();
	}
}
