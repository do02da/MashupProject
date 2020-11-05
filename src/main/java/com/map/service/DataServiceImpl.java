package com.map.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.map.dao.DataDAO;

@Service("dataService")
public class DataServiceImpl implements DataService{
	Logger logger = (Logger) LogManager.getLogger(this.getClass());
	
	@Resource(name="dataDAO")
	private DataDAO dataDAO;

	@Override
	public List<Map<String, Object>> getData(String name, String SiDoName, String SiGuGunName) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("SiDoName", SiDoName);
		map.put("SiGuGunName", SiGuGunName);
		
		if (name.equals("전국무인민원발급정보표준데이터")) {
			return dataDAO.getData_CivilAppeal(map);
		} else if (name.equals("전국무인교통단속카메라표준데이터")) {
			return dataDAO.getData_TrafficCamera(map);
		} else if (name.equals("전국어린이보호구역표준데이터")) {
			return dataDAO.getData_SchoolZone(map);
		} else if (name.equals("전국CCTV표준데이터")) {
			return dataDAO.getData_CCTV(map);
		} else {
			return null;
		}
	}
	
	@Override
	public List<Map<String, Object>> Search(String search_keyword) throws Exception {
		List<Map<String, Object>> resultList = dataDAO.Search(search_keyword);
		
		return resultList;
	}
}
