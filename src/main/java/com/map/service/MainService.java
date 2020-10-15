package com.map.service;

import java.util.List;

public interface MainService {

	List<String> getDataList() throws Exception;

	List<Object> getData(String Listname) throws Exception;

}
