package edu.fudan.tbfetcher.service;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.fudan.tbfetcher.constant.SystemConfiguration;
import edu.fudan.tbfetcher.constant.SystemConstant;
import edu.fudan.tbfetcher.dbaccess.DBManager;
import edu.fudan.tbfetcher.pojo.ItemInfoData;
import edu.fudan.tbfetcher.pojo.ShopItemInfo;
import edu.fudan.tbfetcher.utils.TBBrowser;

public class ShopItemService {
	private static final Logger log = Logger.getLogger(ShopItemService.class);
	private static final String SHOP_ITEM_NUM_Table = "CREATE TABLE "
			+ SystemConstant.SHOP_NUM_TABLE + "(id counter primary key, "
			+ "shopId varchar(15), totalItemNum integer)";
	private String shopUrl;
	private String pageUrl;
	private List<ShopItemInfo> itemInfos;
	private boolean isTmall;
	private String shopId;
	private ItemDataService itemDataService;
	private PostageService postageService;
	
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public void setShopUrl(String shopUrl) {
		this.shopUrl = shopUrl;
	}

	public List<ShopItemInfo> getItemInfos() {
		return itemInfos;
	}

	public ShopItemService() {
		itemDataService = new ItemDataService();
		postageService = new PostageService();
	}

	public void init() {
		shopUrl = null;
		pageUrl = null;
		isTmall = false;
		itemInfos = new ArrayList<ShopItemInfo>();
		shopId = null;
	}

	/**
	 * Iterate every page of the url
	 */
	public void execute() {
		if (shopUrl.contains("tmall")) {
			isTmall = true;
		}
		try {
			URL url = new URL(shopUrl);
			String host = url.getHost();
			pageUrl = "http://" + host + "/search.htm";
		} catch (MalformedURLException e) {
			log.error("Shop Item Service Exception: url is not illegal", e);
			return;
		}

		boolean result = TBBrowser.get(pageUrl, null);
		if (!result) {
			return;
		}
		Document doc = TBBrowser.getDoc();
		int shopItemConfig = SystemConfiguration.shopItemConfig;
		Elements itemNumEls = doc.select("div.search-result > span");
		if (itemNumEls.size() > 0) {
			String numStr = itemNumEls.get(0).ownText();
			int itemNum = Integer.valueOf(numStr);
			writeItemNumToDB(shopId, itemNum);

			log.info("Shop Item Service: totle item num: " + itemNum);
		} else {
			writeItemNumToDB(shopId, -1);
		}

		if (2 == shopItemConfig) {
			return;
		}
		// 抓取所有宝贝信息
		Elements pageEls = doc.select("span.page-info");
		int pageSum = 1; // 假定总共1页
		if (pageEls.size() > 0) {
			String sumStr = pageEls.get(0).ownText();
			sumStr = sumStr.substring(sumStr.indexOf("/") + 1, sumStr.length());
			pageSum = Integer.valueOf(sumStr);
		}
		String baseUrl = pageUrl;
		for (int i = 0; i < pageSum; i++) {
			System.out.println("Process page " + (i + 1) + ":----------------------");
			if (i > 0) {
				pageUrl = baseUrl + "?pageNum=" + (i + 1);
				result = TBBrowser.get(pageUrl, null);
				if (!result) {
					break;
				}
				doc = TBBrowser.getDoc();
			}
			Elements itemListEls = doc.select("div.shop-hesper-bd ul.shop-list li div.item");
			for (Element el : itemListEls) {
				ShopItemInfo itemInfo = new ShopItemInfo();
				itemInfo.setShopId(shopId);
				log.info("Shop id is: " + shopId);
				/** item detail url */
				Elements urlEls = el.select("div.pic a");
				if (urlEls.size() > 0) {
					String itemUrl = urlEls.get(0).attr("href");
					if (null != itemUrl && itemUrl.contains("id")) {
						itemInfo.setItemDetailUrl(itemUrl);
						try {
							URI uri = new URI(itemUrl);
							String query = uri.getQuery();
							String[] queryList = query.split("&");
							String itemId = queryList[0].split("=")[1];
							itemInfo.setItemId(itemId);

							log.info("Shop Item Service: ItemId: " + itemId);
						} catch (URISyntaxException e) {
							log.error("Shop Item Service Exception: ", e);
							log.error("Error url: " + itemUrl);
						}

						// 根据itemId判断该宝贝是否下架，如果下架则不做处理
						boolean isDelisting = isDelisting(itemInfo.getItemId());

						if (isDelisting) {
							log.info("该宝贝是否下架， itemId = " + itemInfo.getItemId());
						} else {
							getOtherInfo(itemUrl, itemInfo);
						}

					} else {
						log.error("Error item url: " + itemUrl);
					}
				}

				/** get Item name */
				Elements nameEls = el.select("div.desc a");
				if (nameEls.size() > 0) {
					String itemName = nameEls.get(0).ownText();
					itemInfo.setItemName(itemName);
					log.info("Shop Item Service: itemName: " + itemName);
				}

				/** get price */
				Elements priceEls = el.select("div.price strong");
				if (priceEls.size() > 0) {
					String price = priceEls.get(0).ownText();
					itemInfo.setPrice(price);
					log.info("Shop Item Service: price: " + price);
				}

				/** get sale num */
				Elements saleEls = el.select("div.sales-amount em");
				if (saleEls.size() > 0) {
					String numStr = saleEls.get(0).ownText();
					int saleNum = Integer.valueOf(numStr);

					itemInfo.setTotalDealNum(saleNum);
					log.info("Shop Item Service: Total deal num: " + saleNum);
				}

				/** get review num */
				Elements reviewEls = el.select("div.rating span.small a");
				if (reviewEls.size() > 0) {
					String reviewStr = reviewEls.get(0).ownText();
					reviewStr = reviewStr.substring(2, reviewStr.length() - 3);

					int reviewNum = Integer.valueOf(reviewStr);
					itemInfo.setReviewNum(reviewNum);
					log.info("Shop Item Service: review num: " + reviewNum);
				}
				
				/** set fetch time */
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String fetchTime = format.format(new Date());
				itemInfo.setFetchTime(fetchTime);
				
				itemInfos.add(itemInfo);
				if (itemInfos.size() >= SystemConstant.DB_WRITE_NUM) {
					writeDataToDB(itemInfos);
					itemInfos.clear();
				}
			}
		}
		if (itemInfos.size() > 0) {
			writeDataToDB(itemInfos);
			itemInfos.clear();
		}
	}

	/**
	 * 进入到具体的每一个页面获取额外的信息
	 */
	private void getOtherInfo(String url, ShopItemInfo itemInfo) {
		if (null == url) {
			return;
		}
		boolean result = TBBrowser.get(url, null);
		if (!result) {
			return;
		}
		Document doc = TBBrowser.getDoc();
		/** get num in stock */
		Element stockEl = doc.getElementById("J_SpanStock");
		if (null != stockEl) {
			String stockStr = stockEl.ownText();
			int numInStock = Integer.parseInt(stockStr);
			itemInfo.setNumInStock(numInStock);
			log.info("num in stock: " + numInStock);

			if (numInStock <= 0) {
				itemInfo.setSoldout(true);
			}
		}

		/** get view num and other */
		if (isTmall) {
			itemInfo.setViewNum(-1);
			itemInfo.setNumOfCollectItem(-1);
			itemInfo.setNumOfLike(-1);
			itemInfo.setNumOfShare(-1);

			itemDataService.init();
			itemDataService.setItemId(itemInfo.getItemId());
			itemDataService.setTmall(true);
			itemDataService.execute();
			int numOfItemCollect = itemDataService.getData().getItemCount();
			itemInfo.setNumOfCollectItem(numOfItemCollect);
			log.info("num of Item collect: " + numOfItemCollect);
		} else {
			itemDataService.init();
			itemDataService.setTmall(false);
			itemDataService.setItemId(itemInfo.getItemId());
			itemDataService.setShopId(itemInfo.getShopId());
			itemDataService.execute();
			ItemInfoData data = itemDataService.getData();

			itemInfo.setViewNum(data.getViewCount());
			itemInfo.setNumOfCollectItem(data.getItemCount());
			itemInfo.setNumOfLike(data.getLikeCount());
			itemInfo.setNumOfShare(data.getShareCount());
		}

		postageService.init();
		postageService.setRawText(doc.toString());
		postageService.setTmall(isTmall);
		postageService.setReferer(pageUrl);
		postageService.execute();
		int monthSaleSum = postageService.getSaleSum();

		itemInfo.setMonthSaleNum(monthSaleSum);
		log.info("month sale: " + monthSaleSum);
	}

	public static void writeDataToDB(List<ShopItemInfo> list) {
		try {
			String result[] = ShopItemInfo.getCreateSQLStr();
			String tableName = result[0];
			String createTableStr = result[1];

			DBManager.connectDB();
			if (!DBManager.isTableExist(tableName)) {
				log.info("create table " + tableName);
				DBManager.createTable(createTableStr);
				log.info("create table success!");
			}

			for (int i = 0; i < list.size(); i++) {
				ShopItemInfo data = list.get(i);
				DBManager.insertData(data, tableName);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("Shop Item Service Error: Write into DB exception.");
			log.error("Exception: ", e);
		} finally{
			DBManager.closeDB();
		}
	}

	private void writeItemNumToDB(String shopId, int itemNum) {
		try {
			String tableName = SystemConstant.SHOP_NUM_TABLE;

			DBManager.connectDB();
			if (!DBManager.isTableExist(tableName)) {
				log.info("create table " + tableName);
				DBManager.createTable(SHOP_ITEM_NUM_Table);
				log.info("create table success!");
			}

			String sqlStr = "INSERT INTO " + tableName
					+ "(shopId, totalItemNum)VALUES('" + shopId + "', "
					+ itemNum + ")";
			DBManager.executeSQL(sqlStr);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("Shop Item Num Error: Write into DB exception.");
			log.error("Exception: ", e);
		} finally{
			DBManager.closeDB();
		}
	}
	
	private boolean isDelisting(String itemId) {
		TBBrowser.get("http://item.taobao.com/item.htm?id=" + itemId);
		String rtn = TBBrowser.getRawText();
		if (rtn != null && rtn.contains("该宝贝可能已经下架")) {
			log.info(itemId + "该宝贝可能已经下架.");
			return true;
		}
		return false;
	}
}
