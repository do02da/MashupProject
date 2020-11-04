package com.map.service;

import java.util.List;
import java.util.Map;

public interface DataService {

	void DB_init() throws Exception;

	List<Map<String, Object>> getData(String name, String SiDoName, String SiGuGunName) throws Exception;

	List<Map<String, Object>> Search(String search_keyword) throws Exception;
}
