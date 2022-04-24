package com.tearsky.admin.controller.monitor;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tearsky.core.web.domain.Server;

/**
 * 服务器监控
 * @author tearsky
 *
 */
@Controller
@RequestMapping("/monitot/server")
public class ServerController {

	private String prefix = "monitor/server";
	
	@RequiresPermissions("monitor:server:view")
	@GetMapping()
	public String server(ModelMap mmap) throws Exception {
		Server server = new Server();
		server.copyTo();
		mmap.put("server", server);
        return prefix + "/server";
	}
}
