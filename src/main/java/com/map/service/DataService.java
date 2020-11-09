package com.map.service;

import java.util.List;
import java.util.Map;

public interface DataService {


	/**
	 * SQL에서 시/도, 시/군/구가 일치하는 내용을 가져오기 위해 map에 SiDoName과 SiGuGunName을 넣어서
	 * name을 판별해서 각 데이터리스트의 데이터를 가져오는 dataDAO를 map을 실어서 보낸다.
	 * @author	김도영
	 * @param	name 데이터리스트이름
	 * @param	SiDoName 선택한 시/도 이름
	 * @param	SiGuGunName 선택한 시/구/군 이름
	 * @return	시/도, 시/군/구에 있는 선택한 데이터리스트의 데이터
	 * @throws	Exception
	 */
	List<Map<String, Object>> getData(String name, String SiDoName, String SiGuGunName) throws Exception;

	/**
	 * 사용자가 입력한 검색어를 받아와서 모든 데이터리스트에서 검색한다.
	 * @author	김도영
	 * @param	search_keyword 검색어
	 * @return	검색결과
	 * @throws	Exception
	 */
	List<Map<String, Object>> Search(String search_keyword) throws Exception;

	
	/**
	 * 
	 * @author	김도영
	 * @return	데이터리스트이름들
	 * @throws	Exception
	 */
	String[] returnListName() throws Exception;
}
