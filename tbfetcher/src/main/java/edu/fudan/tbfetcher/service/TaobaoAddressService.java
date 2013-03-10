package edu.fudan.tbfetcher.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/*
 * 打开address文件，在内存中构建map<code,addr>，通过getAddress返回addr信息
 * 采用单例模式获得对象
 * author: justin
 */
public class TaobaoAddressService implements AddressService {

	private static TaobaoAddressService taobaoAddressService;

	private Map<String, String> addrMap = new HashMap<String, String>();

	// 私有构造函数，不能外部new其实例对象，只能通过内部来new
	private TaobaoAddressService() {

		try {
			FileInputStream fi = new FileInputStream("address");

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					fi, "utf-8"));

			String line;
			while ((line = reader.readLine()) != null) {
				addrMap.put(line.split("	")[0], line.split("	")[1]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static TaobaoAddressService getInstance() {

		if (taobaoAddressService == null) {
			taobaoAddressService = new TaobaoAddressService();
		}
		return taobaoAddressService;
	}

	@Override
	public String[] getAddress(String addrCode) {
		String[] addr = new String[3];
		addr[0] = addrMap.get(addrCode);
		if(null == addr[0]){
			return null; 
		}
		if(!addrCode.endsWith("000")){
			StringBuilder builder = new StringBuilder(addrCode);
			int len = addrCode.length();
			
			builder.replace(len - 3, len, "000");
			
			addr[1] = addrMap.get(builder.toString());
			addr[2] = addr[0].substring(addr[1].length(), addr[0].length());
		} else {
			addr[1] = addr[0];
			addr[2] = "";
		}
		return addr;
	}

}
