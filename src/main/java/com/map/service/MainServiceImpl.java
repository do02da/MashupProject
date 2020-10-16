package com.map.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
		Resource[] resources = resolver.getResources("classpath*:/data/*.json") ;

		List<String> resultList = new ArrayList<String>();
		for (Resource resource: resources){
		    resultList.add(resource.getFilename().replace(".json", ""));
		}
		
		return resultList;
	}
	
	@Override
	public List<Object> getData(String Listname) throws Exception {
		URL resource = new ClassPathResource("/data/" + Listname + ".json").getURL();

		ObjectMapper mapper = new ObjectMapper();
		
		List<Object> objList = mapper.readValue(resource, new TypeReference<List<Object>>(){});

		return objList;
	}
}
