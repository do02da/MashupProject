package com.map.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

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
		
		return resultList;
	}
}
