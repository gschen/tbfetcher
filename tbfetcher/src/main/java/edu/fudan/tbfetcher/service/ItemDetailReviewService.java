package edu.fudan.tbfetcher.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import edu.fudan.tbfetcher.formfields.GetMethod;
import edu.fudan.tbfetcher.pojo.ItemReview;
import edu.fudan.tbfetcher.pojo.ItemReviewAuction;
import edu.fudan.tbfetcher.pojo.ItemReviewComment;
import edu.fudan.tbfetcher.pojo.ItemReviewUser;
import edu.fudan.tbfetcher.utils.GetWaitUtil;

//根据指定的item detail page url获取所有评论信息
public class ItemDetailReviewService {
	private static final Logger log = Logger.getLogger(ItemDetailReviewService.class);
	
	
	private ItemReview itemReview;
	private String sellerId ;
	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	
	private String itemPageUrl;


	private int reviewSum = 0;

	private String reviewUrl;

	public int getReviewSum() {
		return reviewSum;
	}

	public void setReviewSum(int reviewSum) {
		this.reviewSum = reviewSum;
	}

	public String getItemPageUrl() {
		return itemPageUrl;
	}

	public void setItemPageUrl(String itemPageUrl) {
		this.itemPageUrl = itemPageUrl;
	}


	public HttpClient getHttpClient() {
		return httpClient;
	}


	private HttpClient httpClient = new DefaultHttpClient();

	public ItemDetailReviewService() {

	}

	/**
	 * 
	 * 
	 * 1. get review url from page; 2. construct specified review url; 3. get
	 * specified review page; 4. parse json data;
	 */
	public void execute() {

		reviewUrl = getFeedRateListUrl();
		
		if(reviewUrl == null){
			return ;
		}
		int pageSize = 20;

		if (reviewSum == 0) {
			return ;
		} else {
			int pageSum = (reviewSum % pageSize == 0) ? reviewSum / pageSize
					: (reviewSum / pageSize + 1);
			log.info("Total page num is: " + pageSum);
			for (int pageNum = 1; pageNum <= pageSum; ++pageNum) {
				log.info("--------------------------------------------------------------------------------------");
				log.info("The review of Page NO is: " + pageNum);
				parseReview(pageNum);

			}
		}

		this.httpClient.getConnectionManager().shutdown();
		
	}


	public void parseReview(int pageNum) {
		
		String ajaxUrl = null;
		ajaxUrl = buildFeedRateAjaxUrl(reviewUrl, pageNum);
		
		if(ajaxUrl == null){
			return ;
		}
		
		GetMethod get = new GetMethod(httpClient, ajaxUrl);
		GetWaitUtil.get(get);
		String jsonStr = getFeedRateListJsonString(get.getResponseAsString()
				.trim());
		
		if(jsonStr == null){
			return ;
		}
		parseFeedRateListJson(jsonStr);
		get.shutDown();
	}


	public String getFeedRateListUrl() {
		String baseFeedRateListUrl = "";

		String tmpStr = "";
		GetMethod getMethod = new GetMethod(httpClient, itemPageUrl);
		GetWaitUtil.get(getMethod);
		tmpStr = getMethod.getResponseAsString();
		getMethod.shutDown();

		//if there is no "data-listApi" string in the page source, then return null.
		if(tmpStr.contains("data-listApi") == false){
			return null;
		}
		int base = tmpStr.indexOf("data-listApi=");
		int begin = tmpStr.indexOf("\"", base);
		int end = tmpStr.indexOf("\"", begin + 1);
		baseFeedRateListUrl = tmpStr.substring(begin + 1, end);
		log.info("Base feed url is: " + baseFeedRateListUrl);

		return baseFeedRateListUrl;

	}

	public String buildFeedRateAjaxUrl(String baseFeedRateListUrl,
			int currentPageNum) {
		String append = "&currentPageNum="
				+ currentPageNum
				+ "&rateType=&orderType=feedbackdate&showContent=1&attribute=&callback=jsonp_reviews_list";
		StringBuffer sb = new StringBuffer();
		sb.append(baseFeedRateListUrl);
		sb.append(append);

		return sb.toString();
	}

	/**
	 * 将从服务器端返回的字符串转化为json字符串
	 * 
	 * @return
	 */
	/*
	 * 
	 * 评论的格式如下: { "watershed":100, "maxPage":167, "currentPageNum":166,
	 * "comments":[ { "auction":{
	 * "title":"Apple/苹果 iPhone 4S 无锁版/港版 16G 32G 64G可装软件有未激活",
	 * "aucNumId":13599064573, "link":"", "sku":"机身颜色:港版16G白色现货  手机套餐:官方标配" },
	 * "content":"hao", "append":null, "rate":"好评！", "tag":"",
	 * "rateId":16249892723, "award":"", "reply":null, "useful":0,
	 * "date":"2012.03.08", "user":{ "vip":"", "rank":136,
	 * "nick":"771665176_44", "userId":410769781,
	 * "displayRatePic":"b_red_4.gif", "nickUrl":
	 * "http://wow.taobao.com/u/NDEwNzY5Nzgx/view/ta_taoshare_list.htm?redirect=fa"
	 * , "vipLevel":2, "avatar":
	 * "http://img.taobaocdn.com/sns_logo/i1/T1VxqHXa4rXXb1upjX.jpg_40x40.jpg",
	 * "anony":false,
	 * "rankUrl":"http://rate.taobao.com/rate.htm?user_id=410769781&rater=1"} },
	 */
	public String getFeedRateListJsonString(String str) {
		log.info("Plain json string of feed rate review from server is: " + str);
		if(str.contains("(")==false || str.contains(")")==false){
			return null;
		}
		
		int begin = str.indexOf("(");
		int end = str.lastIndexOf(")");
		log.info("Json string of feed rate review is: "
				+ str.substring(begin + 1, end));
		// return str.substring("jsonp_reviews_list(".length(), str.length() -
		// 1);//if the str has no jsonp_reviews_list, what should we can do?
		return str.substring(begin + 1, end);

		/*
		 * The substring begins at the specified beginIndex and extends to the
		 * character at the index endIndex - 1
		 */
	}

	public boolean parseFeedRateListJson(String str) {
		itemReview = new ItemReview();
		
		JSONObject jsonObj = JSONObject.fromObject(str);

		//解析json并赋值
		itemReview.setWatershed(jsonObj.getInt("watershed"));
		itemReview.setCurrentPageNum(jsonObj.getInt("currentPageNum"));
		itemReview.setMaxPage(jsonObj.getInt("maxPage"));
		
		List<ItemReviewComment> itemReviewComments = new ArrayList<ItemReviewComment>();
		itemReview.setComments(itemReviewComments);
		
		if (jsonObj.get("comments").equals(null)) {
			log.info("There is no comment.");
			return false;
		} else {
			JSONArray comments = jsonObj.getJSONArray("comments");

			List list = (List) JSONSerializer.toJava(comments);

			for (Object o : list) {
				
				JSONObject j = JSONObject.fromObject(o);
				ItemReviewComment itemReviewComment = new ItemReviewComment();
				itemReviewComment.setContent(j.getString("content"));
				itemReviewComment.setAppend(j.getString("append"));
				itemReviewComment.setRate(j.getString("rate"));
				itemReviewComment.setTag(j.getString("tag"));
				itemReviewComment.setRateId(j.getInt("rateId"));
				itemReviewComment.setAward(j.getString("award"));
				itemReviewComment.setReply(j.getString("reply"));
				itemReviewComment.setUseful(j.getInt("useful"));
				itemReviewComment.setDate(j.getString("date"));
				
				JSONObject auction = j.getJSONObject("auction");
				ItemReviewAuction itemReviewAuction = new ItemReviewAuction();
				itemReviewAuction.setAucNumId(auction.getInt("aucNumId"));
				itemReviewAuction.setLink(auction.getString("link"));
				itemReviewAuction.setSku(auction.getString("sku"));
				itemReviewAuction.setTitle(auction.getString("title"));
				itemReviewComment.setAuction(itemReviewAuction);
				
				JSONObject user = j.getJSONObject("user");
				ItemReviewUser itemReviewUser = new ItemReviewUser();
				itemReviewUser.setVip(user.getString("vip"));
				itemReviewUser.setUserId(user.getInt("userId"));
				itemReviewUser.setAnony(user.getBoolean("anony"));
				itemReviewUser.setVipLevel(user.getInt("vipLevel"));
				itemReviewUser.setRank(user.getInt("rank"));
				itemReviewUser.setRankUrl(user.getString("rankUrl"));
				itemReviewUser.setNick(user.getString("nick"));
				itemReviewUser.setNickUrl(user.getString("nickUrl"));
				itemReviewUser.setAvatar(user.getString("avatar"));
				itemReviewUser.setDisplayRatePic(user.getString("displayRatePic"));
				itemReviewComment.setUser(itemReviewUser);
				
				itemReviewComments.add(itemReviewComment);
			}
			return true;
		}
	}

}
