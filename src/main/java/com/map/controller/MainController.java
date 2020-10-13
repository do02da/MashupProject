package com.map.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
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
	@ResponseBody
	public void test(CommandMap commandMap) throws Exception {
		ObjectMapper mapper = new ObjectMapper();

		URL resource = new ClassPathResource("/data/전국무인민원발급정보표준데이터.json").getURL();
		

		try { 
			Map<String, String> map = mapper.readValue(resource, Map.class);
			
            logger.debug(map.get("설치장소"));
        } catch (IOException e) {
            e.printStackTrace();
        }
 
    }
}
