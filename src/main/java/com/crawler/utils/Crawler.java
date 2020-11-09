package com.crawler.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

/**
 * 쓰레드를 이용한 크롤링 메소드
 * @packagename	com.crawler.utils
 * @filename	Crawler.java
 * @see			https://heodolf.tistory.com/102?category=887835
 */
public class Crawler {
	Logger logger = (Logger) LogManager.getLogger(this.getClass());
	
	private static ExecutorService executorService = null;
    private static Connection conn = null;
    private static Properties options = null;
	
    // 생성자()
    public Crawler() {
        // 1. Thread Pool 설정
        executorService = Executors.newFixedThreadPool( Const.DEFAULT_THREADS );
    }
    // 생성자( URL )
    public Crawler(String URL) {
        // 1. Thread Pool 설정
        executorService = Executors.newFixedThreadPool( Const.DEFAULT_THREADS );
        
        conn = Jsoup.connect(URL);
    }
    
    // 생성자( options)
    public Crawler(Properties options) throws Exception {
        // 1. Thread Pool 설정
        executorService = Executors.newFixedThreadPool( Const.DEFAULT_THREADS );
        
        setOptions(options);
    }
    
    // 생성자( URL, props );
    public Crawler(String URL, Properties options) throws Exception {
        // 1. Thread Pool 설정
        executorService = Executors.newFixedThreadPool( Const.DEFAULT_THREADS );
 
        conn = Jsoup.connect(URL);
        setOptions(options);
    }
    
    @SuppressWarnings("unchecked")
    public void setOptions(Properties _options) throws Exception {
        options = _options;
        
        // Download 폴더 설정, 경로에 파일이 없으면 생성
        final String downloads = options.getProperty("downloads", Const.DEFAULT_DOWNSLOADS);
        File file = new File( downloads );
        if( !file.exists() || !file.isDirectory() ) {
            file.mkdirs();
        }
        
        if( conn == null ) return;
        
        // Headers 설정
        if( _options.get("headers") != null ) {
            conn.headers((Map<String, String>)_options.get("headers"));
        }
        // Header: Content-Type 설정
        conn.header("Content-Type", (String)_options.getProperty("Content-Type", Const.DEFAULT_CONTENT_TYPE));
 
        // User-Agent 설정
        conn.userAgent((String)_options.getProperty("User-Agent", Const.DEFAULT_USER_AGENT));
 
        // Connection Timeout 설정
        int timeout = Const.DEFAULT_TIMEOUT;
        if( _options.get("timeout") != null ) {
            timeout = (Integer) _options.get("timeout");
        }
        conn.timeout(timeout);
    }
    
	public Runnable download(final String URL, final String filename) {
		// Thread(Runnable) 객체 생성
	    Runnable runnable = new Runnable() {
	        public void run() {
	        	// 파일을 저장할 경로
	            String downloads = options.getProperty("downloads", Const.DEFAULT_DOWNSLOADS);
	            
	            // 현재 Thread의 이름
                String threadName = Thread.currentThread().getName();
 
	            logger.debug(downloads);
	        	// Jsoup Connection 객체 생성
				Connection conn = Jsoup.connect(URL).maxBodySize(1024*1024*30).ignoreContentType(true);

				// 저정할 File 객체 생성
				File SaveFile = null;
				
				// File에 데이터를 저장할 Stream 객체 생성
				FileOutputStream out = null;
				
			
				Response response;
				try {
					response = conn.execute();
					
					SaveFile = new File(downloads, filename);
					
					out = new FileOutputStream(SaveFile, false);
					out.write( response.bodyAsBytes() );
					out.close();
					
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
                    // 저장된 File 확인
                    if( SaveFile.exists() ) {
                        System.out.println( "["+threadName+"][SAVED] "+ SaveFile.getPath() );
                    } else {
                        System.out.println( "["+threadName+"][SAVE FAILED] "+ SaveFile.getPath() );
                    }
                }
	
		
	        	}
	        };
	        
	        executorService.execute(runnable);
	        
	        // executorService.submit(runnable);
	        // execute: Runnable을 인자로 받으며 반환값이 없음, void.
	        // submit: Runnable과 Callable을 인자로 받으며 반환값을 받을 수 있음, return Future.
	        return runnable;
	    
	}
	public void downloads(List<Map<String, String>> download_list) throws Exception{
		logger.debug("downloads");
		
		for (Map<String, String> download_info : download_list) {
			// URL 지정
			String url = download_info.get("URL");
			String filename = download_info.get("filename");
			
			// 1. Download Thread 실행
			download(url, filename);
		}
		
		// 2. Thread Pool 종료
        executorService.shutdown(); // Task Queue에 남아있는 Thread와 실행중인 Thread 처리된 뒤 종료
        
        try {
            // 3. 5분 후에도 종료가 되지 않으면 강제 종료
            if ( !executorService.awaitTermination(5, TimeUnit.MINUTES) ) {
                executorService.shutdownNow();
            }
            executorService = null;
        } catch (Exception e) {
            executorService.shutdownNow();
            executorService = null;
        } finally {
            if ( executorService != null ) {
                executorService.shutdownNow();
            }
        }
	}

}
