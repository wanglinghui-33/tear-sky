package com.tearsky.generator.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tearsky.common.annotation.Log;
import com.tearsky.common.core.controller.BaseController;
import com.tearsky.common.core.page.TableDataInfo;
import com.tearsky.common.core.text.Convert;
import com.tearsky.common.enums.BusinessType;
import com.tearsky.generator.domain.TableInfo;
import com.tearsky.generator.service.GenService;

/**
 * 代码生成操作处理
 * @author tearsky
 *
 */
@Controller
@RequestMapping("/tool/gen")
public class GenController extends BaseController{

	private String prefix = "tool/gen";
	
	@Autowired
	private GenService genService;
	
	@RequiresPermissions("tool:gen:view")
	@GetMapping()
	public String gen() {
		return prefix + "/gen";
	}
	
	@RequiresPermissions("tool:gen:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(TableInfo tableInfo) {
		startPage();
		List<TableInfo> list = genService.selectTableList(tableInfo);
		return getDataTable(list);
	}
	
	@Log(title = "代码生成", businessType = BusinessType.GENCODE)
	@RequiresPermissions("tool:gen:code")
	@GetMapping("genCode/{tableName}")
	public void genCode(HttpServletResponse response, @PathVariable("tableName") String tableName) throws IOException {
		byte[] data = genService.generatorCode(tableName);
		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename=\"tearSky.zip\"");
		response.addHeader("Content-Length", "" + data.length);
		response.setContentType("application/octet-stream; charset=UTF-8");
		IOUtils.write(data, response.getOutputStream());
	}
	
	@Log(title = "代码生成", businessType = BusinessType.GENCODE)
	@RequiresPermissions("tool:gen:code")
	@GetMapping("/batchGenCode")
	public void batchGenCode(HttpServletResponse response, String tables) throws IOException {
		String[] tableNames = Convert.toStrArray(tables);
		byte[] data = genService.generatorCode(tableNames);
		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename=\"tearSky.zip\"");
		response.addHeader("Content-Length", "" + data.length);
		response.setContentType("application/octet-stream; charset=UTF-8");
		IOUtils.write(data, response.getOutputStream());
	}
}
