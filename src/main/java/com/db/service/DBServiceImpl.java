package com.db.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.crawler.utils.Crawler;
import com.db.dao.DBDAO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("DBService")
public class DBServiceImpl implements DBService {
	Logger logger = (Logger) LogManager.getLogger(this.getClass());
	
	@Resource(name="DBDAO")
	private DBDAO DBDAO;
	
	private String[] publicDataSjList = {"전국무인민원발급정보표준데이터", "전국무인교통단속카메라표준데이터", "전국어린이보호구역표준데이터", "전국CCTV표준데이터"};
	private String[] publicDataPkList = {"15012893", "15028200", "15012891", "15013094"};
	private final static String DATA_SAVED_URL = "C:/dev/";
	
	@Override
	public void getPublicData() throws Exception{
		List<Map<String, String>> download_list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < publicDataSjList.length; i++) {
			Map<String, String> download_info = new HashMap<String, String>();
			
			String URL = "https://www.data.go.kr/tcs/dss/stdFileDown.do?publicDataPk=" + publicDataPkList[i] + "&publicDataSj=" + publicDataSjList[i] + "&file=json";
			String FileName = publicDataSjList[i] + ".json";
			
			download_info.put("URL", URL);
			download_info.put("filename", FileName);
			
			
			download_list.add(download_info);
			
		}
		run(download_list);
	}
	
	 // Crawler 실행
    public static void run(List<Map<String, String>> download_list) throws Exception {
 
        // 1. Crawler 옵션 설정
        Properties options = new Properties();
        options.put("Content-Type", "application/json;charset=UTF-8");
        options.put("downloads", "C:/dev");
        options.put("timeout", 30*1000);
 
        // 2. Crawler 생성
        Crawler crawler = new Crawler(options);
 
        // Download File
	    crawler.downloads(download_list);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void DB_init() throws Exception {
		/*
		// DB 초기화
		String url = "jdbc:mariadb://127.0.0.1:3306/?useSSL=false&user=root&password=1q2w3e4r";
		String DBName = "mashup_db";
		Connection conn = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    
	    try {
	    	Class.forName("com.mariadb.jdbc.Driver");
	    	logger.debug("드라이버 연결 성공");
	    	
	    	conn = DriverManager.getConnection(url);
	    	logger.debug("데이터베이스 연결 성공");
	    	
	    	stmt = conn.createStatement();
	    	String existsSql = "DROP DATABASE IF EXISTS '" + DBName + "';";
            String createSql = "CREATE DATABASE '" + DBName + "';";
            
            stmt.executeUpdate(existsSql);
            stmt.executeUpdate(createSql);

	    } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
        	if(conn != null) try { conn.close(); } catch(SQLException se) {}
            if(stmt != null) try { stmt.close(); } catch(SQLException se) {}
            if(rs != null) try { rs.close(); } catch(SQLException se) {}
        }
		*/
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> EarlyMap = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> DataRecords = new ArrayList<Map<String, Object>>();
		String url;
		File jsonFile;
		
		Scanner sc = new Scanner(System.in);
		
		int cursor;
		
		do {
			// 초기화
			EarlyMap.clear();
			resultMap.clear();
			DataRecords.clear();
			
			System.out.println("선택");
			System.out.println("-1 : 종료, 0 : 전국무인민원발급정보표준데이터, 1 : 전국무인교통단속카메라표준데이터, 2 : 잔국어린이보호구역표준데이터, 3 : 전국CCTV표준데이터, 4 : 공공데이터포털에서 데이터 다운로드");
			cursor = sc.nextInt(); 
			
			switch(cursor){
			case 0 : // 전국무인민원발급정보표준데이터
				url = DATA_SAVED_URL + publicDataSjList[cursor] + ".json";
				
				jsonFile = new File(url);
				
				// "fields"와 "Records"로 이루어진 맵
				EarlyMap = mapper.readValue(jsonFile, new TypeReference<Map<String, Object>>(){});
				
				// EarlyMap에서 Records만 분리해서 DataRecords에 넣는다.
				DataRecords = (List<Map<String, Object>>) EarlyMap.get("records");
				
				// JSON 파일을 분리해서 resultMap에 넣고 DAO로 DB에 넣는다.
				for (Map<String, Object> data : DataRecords) {
					resultMap.put("PLACE", data.get("설치장소"));
					resultMap.put("LOCATION", data.get("설치위치"));
					resultMap.put("ROADNAMEADDR", data.get("소재지도로명주소"));
					resultMap.put("LOTNUMADDR", data.get("소재지지번주소"));
					resultMap.put("LATITUDE", data.get("위도"));
					resultMap.put("LONGITUDE", data.get("경도"));
					resultMap.put("PHONENUM", data.get("전화번호"));
					resultMap.put("DATABASEDATE", data.get("데이터기준일자"));
					
					DBDAO.insert_CivilAppeal(resultMap);
				}
				break;
			case 1:	// 전국무인교통단속카메라표준데이터
				url = DATA_SAVED_URL + publicDataSjList[cursor] + ".json";
				
				jsonFile = new File(url);
				
				// "fields"와 "Records"로 이루어진 맵
				EarlyMap = mapper.readValue(jsonFile, new TypeReference<Map<String, Object>>(){});
				
				// EarlyMap에서 Records만 분리해서 DataRecords에 넣는다.
				DataRecords = (List<Map<String, Object>>) EarlyMap.get("records");
				
				// JSON 파일을 분리해서 resultMap에 넣고 DAO로 DB에 넣는다.
				for (Map<String, Object> data : DataRecords) {
					resultMap.put("PLACE", data.get("설치장소"));
					resultMap.put("ROADNAMEADDR", data.get("소재지도로명주소"));
					resultMap.put("LOTNUMADDR", data.get("소재지지번주소"));
					resultMap.put("LATITUDE", data.get("위도"));
					resultMap.put("LONGITUDE", data.get("경도"));
					resultMap.put("GOORGNAME", data.get("관리기관명"));
					resultMap.put("PHONENUM", data.get("관리기관전화번호"));
					resultMap.put("DATABASEDATE", data.get("데이터기준일자"));
					
					DBDAO.insert_TrafficCamera(resultMap);
				}
				break;
			case 2:	// 전국어린이보호구역표준데이터
				url = DATA_SAVED_URL + publicDataSjList[cursor] + ".json";
				
				jsonFile = new File(url);
				
				// "fields"와 "Records"로 이루어진 맵
				EarlyMap = mapper.readValue(jsonFile, new TypeReference<Map<String, Object>>(){});
				
				// EarlyMap에서 Records만 분리해서 DataRecords에 넣는다.
				DataRecords = (List<Map<String, Object>>) EarlyMap.get("records");
				
				// JSON 파일을 분리해서 resultMap에 넣고 DAO로 DB에 넣는다.
				for (Map<String, Object> data : DataRecords) {
					resultMap.put("FACILITIES", data.get("대상시설명"));
					resultMap.put("ROADNAMEADDR", data.get("소재지도로명주소"));
					resultMap.put("LOTNUMADDR", data.get("소재지지번주소"));
					resultMap.put("LATITUDE", data.get("위도"));
					resultMap.put("LONGITUDE", data.get("경도"));
					resultMap.put("GOORGNAME", data.get("관리기관명"));
					
					DBDAO.insert_SchoolZone(resultMap);
				}
				break;
			
			case 3:
				url = DATA_SAVED_URL + publicDataSjList[cursor] + ".json";
				
				jsonFile = new File(url);
				
				// "fields"와 "Records"로 이루어진 맵
				EarlyMap = mapper.readValue(jsonFile, new TypeReference<Map<String, Object>>(){});
				
				// EarlyMap에서 Records만 분리해서 DataRecords에 넣는다.
				DataRecords = (List<Map<String, Object>>) EarlyMap.get("records");
				
				// JSON 파일을 분리해서 resultMap에 넣고 DAO로 DB에 넣는다.
				for (Map<String, Object> data : DataRecords) {
					resultMap.put("DIRECTION", data.get("촬영방면정보"));
					resultMap.put("ROADNAMEADDR", data.get("소재지도로명주소"));
					resultMap.put("LOTNUMADDR", data.get("소재지지번주소"));
					resultMap.put("LATITUDE", data.get("위도"));
					resultMap.put("LONGITUDE", data.get("경도"));
					resultMap.put("PHONENUM", data.get("관리기관전화번호"));
					resultMap.put("DATABASEDATE", data.get("데이터기준일자"));
					DBDAO.insert_CCTV(resultMap);
				}
				break;
			
			case 4:
				getPublicData();
				break;
			}
			
		} while (cursor != -1);
		
		sc.close();
	}
}
