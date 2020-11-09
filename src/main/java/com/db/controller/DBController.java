package com.db.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.db.service.DBService;

@Controller
public class DBController {

	@Resource(name="DBService")
	private DBService dbService;
	
	@RequestMapping(value="/db/admin.do")
	public void test2() throws Exception {
		dbService.DB_init();
	}
}
