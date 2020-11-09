package com.db.service;

public interface DBService {

	void DB_init() throws Exception;

	/**
	 * 공공데이터포털에서 가져온 데이터이름과 Pk로 URL을 만들어서 FileName과 함께 Map으로 만들어 List를 만들어서
	 * run 메소드로 보내 크롤러를 실행한다.
	 * @throws Exception
	 */
	void getPublicData() throws Exception;

}
