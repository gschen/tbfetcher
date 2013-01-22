package edu.fudan.tbfetcher.utils;

import java.util.List;

import org.apache.log4j.Logger;

import edu.fudan.tbfetcher.formfields.GetMethod;

public class GetWaitUtil {

	private static final Logger log = Logger.getLogger(GetWaitUtil.class);
	
	
	public static void get(GetMethod get){
		int base = 1 *1000;
		int cnt = 1;
		while(get.doGet() == false){
			log.info("Waiting for " + base*cnt + "ms.");
			log.info("Page url is: "+ get.getGetUrl());
			try {
				Thread.sleep(base*cnt ++);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void get(GetMethod get, List headers){
		int base = 1 *1000;
		int cnt = 1;
		while(get.doGet(headers) == false){
			log.info("Waiting for "+ base*cnt + "ms.");
			try {
				Thread.sleep(base*cnt ++);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
