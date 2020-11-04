package com.map.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.map.service.DataService;

@Controller
public class DataController {
	Logger logger = (Logger) LogManager.getLogger(this.getClass());
	
	@Resource(name="dataService")
	private DataService dataService;
	

	@RequestMapping(value="/data/db_init.do")
	public void db_init() throws Exception {
		dataService.DB_init();
	}
	
	@RequestMapping(value="/data/getData.do")
	@ResponseBody
	public List<Map<String, Object>> getData(@RequestParam String name, @RequestParam String SiDoName, @RequestParam String SiGuGunName) throws Exception {
		return dataService.getData(name, SiDoName, SiGuGunName);
	}
	
	@RequestMapping(value="/data/search.do")
	public @ResponseBody List<Map<String, Object>> Search(HttpServletRequest request) throws Exception {
		String search_keyword = request.getParameter("search_keyword");
		return dataService.Search(search_keyword);
	}
}
