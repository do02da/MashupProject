package com.db.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.annotation.Resource;

import org.springframework.core.io.ClassPathResource;

import com.db.dao.DBDAO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DBServiceImpl implements DBService {
	@Resource(name="DBDAO")
	private DBDAO DBDAO;

	
	// https://www.data.go.kr/tcs/dss/stdFileDown.do?publicDataPk=15012893&publicDataSj=전국무인민원발급정보표준데이터&file=json
	public void PublicData() {
		
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
		URL resource;
		Map<String, Object> EarlyMap = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> DataRecords = new ArrayList<Map<String, Object>>();
		
		
		Scanner sc = new Scanner(System.in);
		
		int cursor;
		
		do {
			// 초기화
			EarlyMap.clear();
			resultMap.clear();
			DataRecords.clear();
			
			System.out.println("선택");
			System.out.println("-1 : 종료, 1 : 전국무인민원발급정보표준데이터, 2 : 전국무인교통단속카메라표준데이터, 3 : 잔국어린이보호구역표준데이터, 4 : 전국CCTV표준데이터");
			cursor = sc.nextInt(); 
			
			switch(cursor){
			case 1 : // 전국무인민원발급정보표준데이터
				resource = new ClassPathResource("/data/전국무인민원발급정보표준데이터.json").getURL();
				
				// "fields"와 "Records"로 이루어진 맵
				EarlyMap = mapper.readValue(resource, new TypeReference<Map<String, Object>>(){});
				
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
			case 2:	// 전국무인교통단속카메라표준데이터
				resource = new ClassPathResource("/data/전국무인교통단속카메라표준데이터.json").getURL();
				
				// "fields"와 "Records"로 이루어진 맵
				EarlyMap = mapper.readValue(resource, new TypeReference<Map<String, Object>>(){});
				
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
			case 3:	// 전국어린이보호구역표준데이터
				resource = new ClassPathResource("/data/전국어린이보호구역표준데이터.json").getURL();
				
				// "fields"와 "Records"로 이루어진 맵
				EarlyMap = mapper.readValue(resource, new TypeReference<Map<String, Object>>(){});
				
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
			
			case 4:
				resource = new ClassPathResource("/data/전국CCTV표준데이터.json").getURL();

				// "fields"와 "Records"로 이루어진 맵
				EarlyMap = mapper.readValue(resource, new TypeReference<Map<String, Object>>(){});
				
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
			}
			
		} while (cursor != -1);
		
		sc.close();
	}
}
