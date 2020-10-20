package com.map.service;

import java.util.List;
import java.util.Map;

public interface MainService {

	List<String> getDataList() throws Exception;

	List<Object> getData(String Listname) throws Exception;

	List<Map<String, Object>> Search(String search_keyword) throws Exception;

}
