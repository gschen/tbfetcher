package edu.fudan.tbfetcher.service;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import edu.fudan.tbfetcher.pojo.ItemInfoData;
import edu.fudan.tbfetcher.utils.TBBrowser;

public class ItemDataService {
	private static final Logger log = Logger.getLogger(ItemDataService.class);

	private static final String VIEW_PREFIX = "ICVT_7_";
	private static final String FEED_PREFIX = "ICE_3_feedcount-";
	private static final String LIKE_PREFIX = "ZAN_27_2_";
	private static final String SHARE_PREFIX = "DFX_200_1_";
	private static final String ITEM_PREFIX = "ICCP_1_";
	private static final String SHOP_PREFIX = "SCCP_2_";
	private String itemId;
	private String shopId;
	private boolean isTmall;
	private ItemInfoData data;

	public void setTmall(boolean isTmall) {
		this.isTmall = isTmall;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public ItemInfoData getData() {
		return data;
	}

	/**
	 * init the data
	 * */
	public void init() {
		itemId = null;
		shopId = null;
		isTmall = false;
		data = new ItemInfoData();
	}

	public void execute() {
		if (isTmall) {
			tmallDataParse();
		} else {
			String ajaxUrl = "";
			String jsonStr = "";
			try {
				ajaxUrl = getCounterApiUrl();
				jsonStr = getJsonFromServer(ajaxUrl);
				parseJson(jsonStr);
			} catch (Exception e) {
				// TODO: handle exception
				log.error("Item data service error: [taobao][itemId]: " + itemId);
				log.error("AjaxUrl: " + ajaxUrl);
				log.error("JsonString: " + jsonStr);
				log.error("Exception: ", e);
			}
		}
	}

	private void tmallDataParse() {
		String ajaxUrl = "http://count.tbcdn.cn/counter3?keys=ICCP_1_" + itemId
				+ "&callback=TShop.mods.SKU.Stat.setCollectCount";
		boolean result = TBBrowser.get(ajaxUrl, null);
		String jsonStr = "";
		if (result) {
			try {
				jsonStr = TBBrowser.getRawText();
				int begin = jsonStr.indexOf("{");
				int end = jsonStr.lastIndexOf("}") + 1;

				jsonStr = jsonStr.substring(begin, end);
				JSONObject json = JSONObject.fromObject(jsonStr);
				int itemNum = json.getInt(ITEM_PREFIX + itemId);
				log.info("Json string from Tmall data is: " + jsonStr);
				data.setItemCount(itemNum);
			} catch (Exception e) {
				log.error("Item Data Service Error, [tmall][itemId]: " + itemId);
				log.error("AjaxUrl: " + ajaxUrl);
				log.error("JsonStr: ");
				log.error(jsonStr);
			}
		} else {
			log.error("Item Data Service Error: Get page failed.[Tmall ajaxUrl: ]"
					+ ajaxUrl);
		}
	}

	/*
	 * {"DFX_200_1_13738559526":100,
	 * "ICVT_7_13738559526":295616,"ICCP_1_13738559526":5473,
	 * "ICE_3_feedcount-13738559526":528,
	 * "ZAN_27_2_13738559526":15,"SCCP_2_35555431":11658}
	 */
	private void parseJson(String jsonStr) {
		JSONObject jsonObj = JSONObject.fromObject(jsonStr);

		String itemViewCounterFLag = VIEW_PREFIX + itemId;
		String itemFeedCounterFlag = FEED_PREFIX + itemId;
		String likeCountFlag = LIKE_PREFIX + itemId;
		String shareCountFlag = SHARE_PREFIX + itemId;
		String itemColCountFlag = ITEM_PREFIX + itemId;
		String storeColCountFlag = SHOP_PREFIX + shopId;

		int viewCount = jsonObj.getInt(itemViewCounterFLag);
		int feedCount = jsonObj.getInt(itemFeedCounterFlag);
		int likeCount = jsonObj.getInt(likeCountFlag);
		int shareCount = jsonObj.getInt(shareCountFlag);
		int itemCount = jsonObj.getInt(itemColCountFlag);
		int storeCount = jsonObj.getInt(storeColCountFlag);

		data.setFeedCount(feedCount);
		data.setItemCount(itemCount);
		data.setLikeCount(likeCount);
		data.setShareCount(shareCount);
		data.setStoreCount(storeCount);
		data.setViewCount(viewCount);
	}

	private String getJsonFromServer(String ajaxUrl) {
		String jsonStr = null;

		boolean result = TBBrowser.get(ajaxUrl, null);
		if (result) {
			jsonStr = TBBrowser.getRawText();
			int begin = jsonStr.indexOf("{");
			int end = jsonStr.lastIndexOf("}") + 1;
			jsonStr = jsonStr.substring(begin, end);
		} else {
			log.error("Item Data Service Error: Get page failed.");
		}
		return jsonStr;
	}

	private String getCounterApiUrl() {
		StringBuffer sb = new StringBuffer();
		String appendStr = "&callback=DT.mods.SKU.CounterCenter.saveCounts";

		sb.append("http://count.tbcdn.cn/counter3?keys=");
		sb.append(VIEW_PREFIX + itemId + ",");
		sb.append(LIKE_PREFIX + itemId + ",");
		sb.append(SHARE_PREFIX + itemId + ",");
		sb.append(ITEM_PREFIX + itemId + ",");
		sb.append(FEED_PREFIX + itemId);
		if (null != shopId && shopId.length() > 0) {
			sb.append("," + SHOP_PREFIX + shopId);
		}
		sb.append(appendStr);

		log.info("Ajax url is: " + sb.toString());

		return sb.toString();
	}
}
