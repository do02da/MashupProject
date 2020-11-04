package com.map.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.common.dao.AbstractDAO;

@Repository("dataDAO")
public class DataDAO extends AbstractDAO{
	Logger logger = (Logger) LogManager.getLogger(this.getClass());
	
	// 전국무인민원발급정보표준데이터
	public void insert_CivilAppeal(Map<String, Object> map) throws Exception {
		insert_NoLog("insert_CivilAppeal", map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getData_CivilAppeal(Map<String, String> map) throws Exception {
		return (List<Map<String, Object>>) selectList("getData_CivilAppeal", map);
	}

	// 전국무인교통단속카메라표준데이터
	public void insert_TrafficCamera(Map<String, Object> map) throws Exception {
		insert_NoLog("insert_TrafficCamera", map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getData_TrafficCamera(Map<String, String> map) throws Exception {
		return (List<Map<String, Object>>) selectList("getData_TrafficCamera", map);
	}

	// 전국어린이보호구역표준데이터
	public void insert_SchoolZone(Map<String, Object> map) throws Exception {
		insert_NoLog("insert_SchoolZone", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getData_SchoolZone(Map<String, String> map) {
		return (List<Map<String, Object>>) selectList("getData_SchoolZone", map);
	}

	// 전국CCTV표준데이터
	public void insert_CCTV(Map<String, Object> map) throws Exception {
		insert_NoLog("insert_CCTV", map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getData_CCTV(Map<String, String> map) {
		return (List<Map<String, Object>>) selectList("getData_CCTV", map);	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> Search(String search_keyword) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		
		String[] DataListName = {"전국무인민원발급정보표준데이터", "전국무인교통단속카메라표준데이터", "전국어린이보호구역표준데이터", "전국CCTV표준데이터"};
		String[] SQL_Name = {"Search_CivilAppeal", "Search_TrafficCamera", "Search_SchoolZone", "Search_CCTV"};
		
		for (int i = 0; i < DataListName.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("data", (List<Map<String, Object>>) selectList(SQL_Name[i], search_keyword));

			if (map.get("data").toString().length() > 2) {	// 빈 데이터를 toString() 하면 []가 되서 length가 2가 되므로 
				map.put("DataListName", DataListName[i]);
				resultList.add(map);
			}
		}
		
		return resultList;
	}
}
