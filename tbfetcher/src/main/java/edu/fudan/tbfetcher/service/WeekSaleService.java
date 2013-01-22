package edu.fudan.tbfetcher.service;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import edu.fudan.tbfetcher.formfields.GetMethod;
import edu.fudan.tbfetcher.utils.GetWaitUtil;


/**
 * 
 * Search result page 页面上的周销量和周销量的卖家数量
 * @author JustinChen
 *
 */
public class WeekSaleService {
	private static final Logger log = Logger.getLogger(WeekSaleService.class);
	
	private int weekSaleNum;
	private int weekSellerNum;
	private Document doc;
	
	public int getWeekSaleNum() {
		return weekSaleNum;
	}
	
	public int getWeekSellerNum() {
		return weekSellerNum;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public void init(){
		weekSaleNum = 0;
		weekSellerNum = 0;
		doc = null;
	}
	
	public void execute(){
		if( doc.select("dl.product-shortcut dd").size() < 2){
			log.info("There is no product shotcut.");
		}else{
			if( doc.select("dl.product-shortcut dd").get(1).select("strong").size() < 2){
				log.info("There is no week sale num and week seller num");
			}else{
				weekSaleNum = Integer.parseInt(doc.select("dl.product-shortcut dd").get(1).select("strong").get(0).ownText());
				weekSellerNum = Integer.parseInt(doc.select("dl.product-shortcut dd").get(1).select("strong").get(1).ownText());

			}
		}
		log.info("Week sale num is: " + weekSaleNum);
		log.info("Week seller num is: "+weekSellerNum);
	}
}
