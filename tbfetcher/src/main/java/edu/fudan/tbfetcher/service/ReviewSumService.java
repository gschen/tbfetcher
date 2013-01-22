//package edu.fudan.tbfetcher.service;
//
//import net.sf.json.JSONObject;
//
//import org.apache.http.client.HttpClient;
//import org.apache.log4j.Logger;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//
//import edu.fudan.tbfetcher.formfields.GetMethod;
//import edu.fudan.tbfetcher.utils.GetWaitUtil;
//
//public class ReviewSumService {
//	private static final Logger log = Logger.getLogger(ReviewSumService.class);
//	private String itemPageUrl;
//	private HttpClient httpClient;
//	private Document doc;
//	private int reviewSum = 0;
//
//	public int getReviewSum() {
//		return reviewSum;
//	}
//
//	public void setReviewSum(int reviewSum) {
//		this.reviewSum = reviewSum;
//	}
//
//	public ReviewSumService(HttpClient client, Document doc, String pageUrl) {
//		this.httpClient = client;
//		this.doc = doc;
//		this.itemPageUrl = pageUrl;
//	}
//
//	public void execute() {
//		String ajaxUrl = getAjaxUrl();
//		if (ajaxUrl == null) {
//			log.info("There is no review sum url.");
//		} else {
//			String strFromServer = getPlainJson(ajaxUrl);
//			String json = null;
//			json = getJsonStr(strFromServer);
//
//			if (json != null) {
//				parseJson(json);
//			}
//		}
//	}
//
//	public String getAjaxUrl() {
//		if(null == doc){
//			GetMethod get = new GetMethod(httpClient, itemPageUrl);
//			GetWaitUtil.get(get);
//			String docRawText = get.getResponseAsString();
//			doc = Jsoup.parse(docRawText);
//		}
//		String ajaxUrl = "";
//		String baseUrl = "";
//		String appendStr = "&callback=jsonp_reviews_summary";
//
//		if (doc.select("em#J_RateStar").size() == 0) {
//			log.error("There is no review sum url in the page.");
//			ajaxUrl = null;
//		} else {
//			Element rateStar = doc.select("em#J_RateStar").get(0);
//			baseUrl = rateStar.attr("data-commonApi");
//			ajaxUrl = baseUrl + appendStr;
//		}
//
//		return ajaxUrl;
//	}
//
//	public String getPlainJson(String ajaxUrl) {
//		String strFromServer = null;
//		GetMethod get = new GetMethod(httpClient, ajaxUrl);
//		GetWaitUtil.get(get);
//
//		strFromServer = get.getResponseAsString();
//		get.shutDown();
//		return strFromServer;
//
//	}
//
//	public String getJsonStr(String plainJson) {
//		String jsonStr = null;
//		if (plainJson.contains("{") && plainJson.contains("}")) {
//			int begin = plainJson.indexOf("{");
//			int end = plainJson.lastIndexOf("}");
//			jsonStr = plainJson.substring(begin, end + 1);
//		}
//
//		return jsonStr;
//	}
//	public void parseJson(String jsonStr) {
//		// log.info("Json str from server is: "+jsonStr);
//
//		JSONObject jsonObj = JSONObject.fromObject(jsonStr);
//
//		if (jsonStr.contains("data") && jsonStr.contains("count")
//				&& jsonStr.contains("total")) {
//			reviewSum = jsonObj.getJSONObject("data").getJSONObject("count")
//					.getInt("total");
//		}
//
//		log.info("The sum of reviews is: " + reviewSum);
//	}
//
//}
