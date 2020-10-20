package com.map.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.map.common.CommandMap;
import com.map.service.MainService;

@Controller
public class MainController {
	Logger logger = (Logger) LogManager.getLogger(this.getClass());
	
	@Resource(name="mainService")
	private MainService mainService;
	
	@RequestMapping(value="/map/openMapMain.do")
	public ModelAndView openSampleList(CommandMap commandMap) throws Exception {
		ModelAndView mv = new ModelAndView("/openMapMain");

		return mv;
	}
	
	@RequestMapping(value="/map/getDataList.do")
	public @ResponseBody List<String> getDataList() throws Exception {
		return mainService.getDataList();
    }
	
	@RequestMapping(value="/map/getData.do")
	public @ResponseBody List<Object> getData(HttpServletRequest request) throws Exception {
		return mainService.getData(request.getParameter("name"));
    }
	
	@RequestMapping(value="/map/search.do")
	public @ResponseBody List<Map<String, Object>> Search(HttpServletRequest request) throws Exception {
		String search_keyword = request.getParameter("search_keyword");
		
		return mainService.Search(search_keyword);
	}
}
