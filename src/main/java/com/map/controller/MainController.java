package com.map.controller;

import java.net.URL;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.map.common.CommandMap;

@Controller
public class MainController {
	Logger logger = (Logger) LogManager.getLogger(this.getClass());
	
	@RequestMapping(value="/map/openMapMain.do")
	public ModelAndView openSampleList(CommandMap commandMap) throws Exception {
		ModelAndView mv = new ModelAndView("/openMapMain");

		return mv;
	}
	
	@RequestMapping(value="/test/test.do")
	public @ResponseBody List<Object> test() throws Exception {
		URL resource = new ClassPathResource("/data/전국무인민원발급정보표준데이터.json").getURL();

		ObjectMapper mapper = new ObjectMapper();
		
		List<Object> objList = mapper.readValue(resource, new TypeReference<List<Object>>(){});
		
		return objList;
    }
}
