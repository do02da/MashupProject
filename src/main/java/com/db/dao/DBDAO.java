package com.db.dao;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.common.dao.AbstractDAO;

@Repository("DBDAO")
public class DBDAO extends AbstractDAO {
		Logger logger = (Logger) LogManager.getLogger(this.getClass());
		
		// 전국무인민원발급정보표준데이터
		public void insert_CivilAppeal(Map<String, Object> map) throws Exception {
			insert_NoLog("insert_CivilAppeal", map);
		}
		
		// 전국무인교통단속카메라표준데이터
		public void insert_TrafficCamera(Map<String, Object> map) throws Exception {
			insert_NoLog("insert_TrafficCamera", map);
		}
		
		// 전국어린이보호구역표준데이터
		public void insert_SchoolZone(Map<String, Object> map) throws Exception {
			insert_NoLog("insert_SchoolZone", map);
		}

		// 전국CCTV표준데이터
		public void insert_CCTV(Map<String, Object> map) throws Exception {
			insert_NoLog("insert_CCTV", map);
		}
}
