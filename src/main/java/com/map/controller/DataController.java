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
	
	/**
	 * 사용자가 선택한 시/도, 시/군/구와 선택한 데이터리스트 이름을 서비스로 넘겨서 데이터를 받아온다.
	 * @author	김도영
	 * @param	name 데이터리스트이름
	 * @param	SiDoName 선택한 시/도 이름
	 * @param	SiGuGunName 선택한 시/구/군 이름
	 * @return	시/도, 시/군/구에 있는 선택한 데이터리스트의 데이터
	 * @throws	Exception
	 */
	@RequestMapping(value="/data/getData.do")
	@ResponseBody
	public List<Map<String, Object>> getData(@RequestParam String name, @RequestParam String SiDoName, @RequestParam String SiGuGunName) throws Exception {
		return dataService.getData(name, SiDoName, SiGuGunName);
	}
	
	/**
	 * 사용자가 입력한 검색어를 받아와서 모든 데이터리스트에서 검색한다.
	 * @author	김도영
	 * @param	request request에서 search_keyword 파라미터를 가져온다.
	 * @return	검색결과
	 * @throws	Exception
	 */
	@RequestMapping(value="/data/search.do")
	@ResponseBody
	public List<Map<String, Object>> Search(HttpServletRequest request) throws Exception {
		String search_keyword = request.getParameter("search_keyword");
		return dataService.Search(search_keyword);
	}
}
