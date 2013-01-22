package edu.fudan.tbfetcher.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.client.HttpClient;
import org.apache.log4j.Logger;

import edu.fudan.tbfetcher.formfields.GetMethod;
import edu.fudan.tbfetcher.utils.FileUtil;

public class TaobaoDsDataService {
	private static final Logger log = Logger.getLogger(TaobaoDsDataService.class);
	private JSONObject dsData;

	public void execute(){
		getDsData();
	}
	public void getDsData() {
		dsData = JSONObject.fromObject(FileUtil.read("tbds.json"));
	}
	
	public String getData(String str){
		if(str.equals(null)||str.equals("")||(dsData.toString().contains(str)==false)){
			return null;
		}else{
			String myStr;
			myStr = dsData.getString(str).split("\"")[1];
			return myStr;
		}
	}
	
	public String getAddress(String addressCode){
		if(null == dsData){
			dsData = JSONObject.fromObject(FileUtil.read("tbds.json"));
		}
		JSONArray array = dsData.getJSONArray(addressCode);
		String dist = array.getString(0);
		String provCode = array.getString(1);
		
		if(!provCode.equals("1")){
			array = dsData.getJSONArray(provCode);
			String prov = array.getString(0);
			dist = prov + dist;
		}
		return dist;
	}
}
