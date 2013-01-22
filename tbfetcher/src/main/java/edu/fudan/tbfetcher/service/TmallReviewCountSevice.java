package edu.fudan.tbfetcher.service;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import edu.fudan.tbfetcher.utils.TBBrowser;

public class TmallReviewCountSevice {
	private final static Logger log = Logger.getLogger(TmallReviewCountSevice.class);
	private int reviewCount;
	private String itemId;
	private String sellerId;
	private boolean isMarket;
	private String ajaxUrl;
	
	public void setAjaxUrl(String ajaxUrl) {
		this.ajaxUrl = ajaxUrl;
	}
	public void setMarket(boolean isMarket) {
		this.isMarket = isMarket;
	}
	public int getReviewCount() {
		return reviewCount;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	public void init(){
		reviewCount = 0;
		itemId = null;
		sellerId = null;
		isMarket = false;
		ajaxUrl = null;
	}
	
	public void execute(){
		reviewCount = 0;
		try {
			if(!isMarket){
				ajaxUrl = "http://rate.tmall.com/list_dsr_info.htm?callback=jsonp20&itemId=" + itemId + "&sellerId=" + sellerId;
			}
			log.info("Tmall view count ajax url is: " + ajaxUrl);
			boolean result = TBBrowser.get(ajaxUrl, null);
			if(result){
				if(isMarket){
					String rawText = TBBrowser.getRawText();
					int base = rawText.indexOf("TB.Detail.MallReviews");
					int begin = rawText.indexOf("'", base) + 1;
					int end = rawText.indexOf("'", begin);
					
					String html = rawText.substring(begin, end);
					Document doc = Jsoup.parseBodyFragment(html);
					String numStr = doc.select("em").get(0).ownText();
					reviewCount = Integer.parseInt(numStr);
				}else{
					String jsonStr = TBBrowser.getRawText();
					int begin = jsonStr.indexOf("{");
					int end = jsonStr.lastIndexOf("}") + 1;
					jsonStr = jsonStr.substring(begin, end);
					JSONObject json = JSONObject.fromObject(jsonStr);
					json = json.getJSONObject("dsr");
					
					reviewCount = json.getInt("rateTotal");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("Tmall Review Count Service Error: Exception happened. [itemId]: " + itemId);
			log.error("Exception: ", e);
		}
	}
}
