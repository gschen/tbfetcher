package edu.fudan.tbfetcher.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import edu.fudan.tbfetcher.utils.TBBrowser;

public class ImpressService {
	private final static Logger log = Logger.getLogger(ImpressService.class);

	private Document doc; // 从父页面传过来的DOM结果，也用来查找ajax url
	private String impress; // 返回的impress，以String形式
	private boolean isTmall; // 是否是天猫的处理
	private String itemId; // 当前处理的商品ID

	private int rateGoodCount;
	private int rateNormalCount;
	private int rateBadCount;
	private int additionalCount;

	public int getRateGoodCount() {
		return rateGoodCount;
	}

	public void setRateGoodCount(int rateGoodCount) {
		this.rateGoodCount = rateGoodCount;
	}

	public int getRateNormalCount() {
		return rateNormalCount;
	}

	public void setRateNormalCount(int rateNormalCount) {
		this.rateNormalCount = rateNormalCount;
	}

	public int getRateBadCount() {
		return rateBadCount;
	}

	public void setRateBadCount(int rateBadCount) {
		this.rateBadCount = rateBadCount;
	}

	public int getAdditionalCount() {
		return additionalCount;
	}

	public void setAdditionalCount(int additionalCount) {
		this.additionalCount = additionalCount;
	}

	public String getImpress() {
		return impress;
	}

	public void setImpress(String impress) {
		this.impress = impress;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public void setTmall(boolean isTmall) {
		this.isTmall = isTmall;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	/**
	 * init the data
	 * */
	public void init() {
		doc = null;
		impress = null;
		isTmall = false;
		itemId = null;
	}

	public void execute() {
		impress = ""; // initialize the impress
		if (isTmall) {
			tmallProcess();
		} else {
			normalProcess();
		}
	}

	/**
	 * 天猫商城的获取用户印象的处理方法 一般的ajax
	 * url:http://rate.tmall.com/listTagClouds.htm?itemId
	 * =13627473564&isAll=true&callback=loadtagjsoncallback
	 * 
	 * */
	private void tmallProcess() {
		String baseUrl = "http://rate.tmall.com/listTagClouds.htm?itemId=";
		String suffix = "&isAll=true&callback=loadtagjsoncallback";
		String ajaxUrl = baseUrl + itemId + suffix;
		String jsonStr = "";
		log.info("Tmall impress ajax url is: " + ajaxUrl);

		boolean result = TBBrowser.get(ajaxUrl, null);
		if (result) {
			try {
				jsonStr = TBBrowser.getRawText();
				int begin = jsonStr.indexOf("{");
				int end = jsonStr.lastIndexOf("}");
				jsonStr = jsonStr.substring(begin, end + 1);
				JSONObject json = JSONObject.fromObject(jsonStr);
				json = json.getJSONObject("tags");
				JSONArray array = json.getJSONArray("tagClouds");
				if (null != array && array.size() > 0) {
					StringBuilder sb = new StringBuilder();
					for (Object o : array) {
						JSONObject item = JSONObject.fromObject(o);
						String tag = item.getString("tag");
						sb.append(tag + ",");
					}
					impress = sb.substring(0, sb.length() - 1);
				}
			} catch (Exception e) {
				log.error("Impress Service Error: Exception happened. [tmall][itemId]: " + itemId);
				log.error("ajax url: " + ajaxUrl);
				log.error("Json String: ");
				log.error(jsonStr);
				log.error("Exception: ", e);
			}
		}
	}

	/**
	 * 通常一般的获取用户印象的处理方法 一般的ajax
	 * url：http://rate.taobao.com/detailCommon.htm?userNumId
	 * =13245409&auctionNumId
	 * =14357354663&siteID=7&callback=jsonp_reviews_summary
	 * */
	private void normalProcess() {
		String suffix = "&callback=jsonp_reviews_summary";
		Element apiEl = doc.getElementById("J_RateStar");
		if (null != apiEl) {
			String baseUrl = apiEl.attr("data-commonApi");
			String ajaxUrl = baseUrl + suffix;
			String jsonStr = "";
			boolean result = TBBrowser.get(ajaxUrl, null);
			if (result) {
				try {
					jsonStr = TBBrowser.getRawText();
					int begin = jsonStr.indexOf("{");
					int end = jsonStr.lastIndexOf("}");

					jsonStr = jsonStr.substring(begin, end + 1);
					JSONObject json = JSONObject.fromObject(jsonStr);
					json = json.getJSONObject("data");

					// get item count
					JSONObject count = json.getJSONObject("count");
					int goodFull = count.getInt("goodFull");
					int normal = count.getInt("normal");
					int bad = count.getInt("bad");
					int additional = count.getInt("additional");

					this.rateGoodCount = goodFull;
					this.rateNormalCount = normal;
					this.rateBadCount = bad;
					this.additionalCount = additional;

					log.info("goodFull: " + goodFull);
					log.info("normal: " + normal);
					log.info("bad: " + bad);
					log.info("additional: " + additional);
					// get item impress
					JSONArray array = json.getJSONArray("impress");
					if (null != array && array.size() > 0) {
						StringBuilder sb = new StringBuilder();
						for (Object o : array) {
							JSONObject item = JSONObject.fromObject(o);
							String tag = item.getString("title");
							sb.append(tag + ",");
						}
						impress = sb.substring(0, sb.length() - 1);
					}
				} catch (Exception e) {
					log.error("Impress Service Error: Exception happened. [taobao][itemId]: " + itemId);
					log.error("ajax url: " + ajaxUrl);
					log.error("Json String: ");
					log.error(jsonStr);
					log.error("Exception: ", e);
				}
			}
		}
	}
}
