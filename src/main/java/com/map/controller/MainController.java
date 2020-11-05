package com.map.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.map.service.MainService;

@Controller
public class MainController {
	Logger logger = (Logger) LogManager.getLogger(this.getClass());
	
	@Resource(name="mainService")
	private MainService mainService;
	

	/**
	 * index.jsp에서 메인화면으로 보내준다.
	 * @author	김도영
	 * @return	메인화면
	 * @throws	Exception
	 */
	@RequestMapping(value="/map/openMapMain.do")
	public ModelAndView openSampleList() throws Exception {
		ModelAndView mv = new ModelAndView("/openMapMain");

		return mv;
	}
	
	/**
	 * src/main/resources/data에 있는 json 파일들의 이름을 가져온다
	 * @author	김도영
	 * @return	데이터리스트 이름
	 * @throws	Exception
	 */
	@RequestMapping(value="/map/getDataList.do")
	public @ResponseBody List<String> getDataList() throws Exception {
		return mainService.getDataList();
    }
}
