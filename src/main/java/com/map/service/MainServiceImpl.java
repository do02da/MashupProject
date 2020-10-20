package com.map.service;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("mainService")
public class MainServiceImpl implements MainService {
	Logger logger = (Logger) LogManager.getLogger(this.getClass());
	
	@Override
	public List<String> getDataList() throws Exception {
		ClassLoader cl = this.getClass().getClassLoader(); 
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
		Resource[] resources = resolver.getResources("classpath*:/data/*.json") ;	// src/main/resources/data/ 밑에 json 파일들을 가져옴

		List<String> resultList = new ArrayList<String>();
		for (Resource resource: resources){
		    resultList.add(resource.getFilename().replace(".json", ""));			// ".json"을 없애고 데이터리스트이름들만 리스트에 추가
		}
		
		// API여서 임의로 추가. (개발중)
		// resultList.add("전국교통사고다발지역표준데이터");
		
		return resultList;
	}

	@Override
	public List<Object> getData(String Listname) throws Exception {
		if (Listname.equals("전국교통사고다발지역표준데이터")) {		// 전국교통사고다발지역표준데이터(개발중)
			String Servicekey = "czFVYd2rvx65d10VQv3jW8gHxTLJKR8VXF6bUR5U%2BAhBiSs%2BuhRiPVFbuiarpOz8%2F3g%2FRybQL2XTcE6hOQUq8Q%3D%3D";
			 StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552061/frequentzoneLg/getRestFrequentzoneLg"); 			/*URL*/
		        urlBuilder.append("&" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + URLEncoder.encode(Servicekey, "UTF-8")); 			/*공공데이터포털에서 발급받은 인증키*/
		        urlBuilder.append("&" + URLEncoder.encode("searchYearCd","UTF-8") + "=" + URLEncoder.encode("2018", "UTF-8")); 				/*검색을 원하는 연도*/
		        urlBuilder.append("&" + URLEncoder.encode("siDo","UTF-8") + "=" + URLEncoder.encode("11", "UTF-8")); 						/*법정동 시도 코드*/
		        urlBuilder.append("&" + URLEncoder.encode("guGun","UTF-8") + "=" + URLEncoder.encode("200", "UTF-8")); 						/*법정동 시군구 코드*/
		        urlBuilder.append("&" + URLEncoder.encode("type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); 						/*결과형식(xml/json)*/
		        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); 					/*검색건수*/
		        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); 						/*페이지 번호*/
			return null;
		} else {											// resources/data에 json 파일로 존재하는 데이터들
			URL resource = new ClassPathResource("/data/" + Listname + ".json").getURL();
			ObjectMapper mapper = new ObjectMapper();
			List<Object> objList = mapper.readValue(resource, new TypeReference<List<Object>>(){});
	
			return objList;
		}
	}
	
	@Override
	public List<Map<String, Object>> Search(String search_keyword) throws Exception {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		
		List<String> DataList = getDataList();
		
		for (String datalist : DataList) {
			List<Object> Data = getData(datalist);
			
			for (Object tmpData : Data) {
				String[] StrList = tmpData.toString().split(",");

				for (String tmpStr : StrList) {
					if (tmpStr.contains("설치장소") || tmpStr.contains("대상시설명") || tmpStr.contains("촬영방면정보"))	{		// 제목찾기
						if (tmpStr.contains(search_keyword)) {															// 검색어가 포함되어있으면
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("DataListName", datalist);
							map.put("data", tmpData);
							resultList.add(map);
							continue;
						}
					}
				}
			}
		}

		return resultList;
	}
}
