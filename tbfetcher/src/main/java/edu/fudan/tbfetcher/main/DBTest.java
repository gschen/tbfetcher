package edu.fudan.tbfetcher.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import edu.fudan.tbfetcher.constant.SystemConfiguration;
import edu.fudan.tbfetcher.constant.SystemConstant;
import edu.fudan.tbfetcher.dbaccess.DBManager;
import edu.fudan.tbfetcher.handler.AbstractItemCommentHandler;
import edu.fudan.tbfetcher.handler.TaobaoItemCommentHandler;
import edu.fudan.tbfetcher.handler.TmallItemCommentHandler;
import edu.fudan.tbfetcher.pageparser.ItemDetailPageParser;
import edu.fudan.tbfetcher.pageparser.SearchResultPageParser;
import edu.fudan.tbfetcher.pageparser.ShopRatePageParser;
import edu.fudan.tbfetcher.pageparser.TopRankPageParser;
import edu.fudan.tbfetcher.pojo.CategoryInfo;
import edu.fudan.tbfetcher.pojo.ItemCommentConfig;
import edu.fudan.tbfetcher.pojo.ItemInfo;
import edu.fudan.tbfetcher.pojo.ReviewBaseUrlInfo;
import edu.fudan.tbfetcher.pojo.SellerInSearchResult;
import edu.fudan.tbfetcher.pojo.ShopIndexInfo;
import edu.fudan.tbfetcher.pojo.ShopRate;
import edu.fudan.tbfetcher.pojo.TopRankItemInfo;
import edu.fudan.tbfetcher.service.ShopItemService;
import edu.fudan.tbfetcher.utils.TBBrowser;

public class DBTest {
	private static final Logger log = Logger.getLogger(DBTest.class);
	private List<TopRankItemInfo> topRankItemInfos;
	private List<SellerInSearchResult> sellerInSearchResults;
	private List<ItemInfo> itemInfos;
	private List<ReviewBaseUrlInfo> reviewBaseUrls;
	private List<ShopRate> storeInfos;
	private Map<String, ShopIndexInfo> shopIndexs;
	private List<ShopIndexInfo> shopInfoList = new ArrayList<ShopIndexInfo>();

	public void setUp() {
		DOMConfigurator.configure("log4j.xml");
		SystemConfiguration.init();
		ItemCommentConfig.initConfig();
	}

	public void tearDown() {
		log.info("--------------------------------------------------------------------------------------------------------------");
		log.info("COMPLETE ALL TASKS!");
	}

	public void execute() {
		setUp();
		DBManager.connectDB();
//		TBBrowser.init();
		task();
//		TBBrowser.shutdown();
		DBManager.closeDB();
		tearDown();
	}

	public void task() {
		topRankProcess();
		searchResultProcess();
		itemDetailProcess();
		startThreads();
	}

	private void startThreads() {
		Thread shopItemThread = new Thread(new Runnable() {
			@Override
			public void run() {
				shopItemProcess();
			}
		});
		shopItemThread.start();
		Thread storeInfoThread = new Thread(new Runnable() {

			@Override
			public void run() {
				shopRateProcess();
			}
		});
		storeInfoThread.start();
		Thread itemReviewThread = new Thread(new Runnable() {

			@Override
			public void run() {
				itemCommentProcess();
			}
		});
		itemReviewThread.start();
		try {
			shopItemThread.join();
			storeInfoThread.join();
			itemReviewThread.join();
		} catch (InterruptedException e) {
			log.error("Thread join exception: ", e);
		}
	}

	/**
	 * Top rank Process
	 * */
	public void topRankProcess() {
		log.info("Top Rank Process:------------------------------------------------------------");
		topRankItemInfos = new ArrayList<TopRankItemInfo>();
		// List<CategoryInfo> categoryInfos = XmlConfUtil.getCategoryList();
		List<CategoryInfo> categoryInfos = SystemConfiguration.categoryList;

		// get top rank item info
		log.info("The num of category list is: " + categoryInfos.size());
		TopRankPageParser topRankPageParser = new TopRankPageParser();
		boolean getPageFlag = false;
		for (CategoryInfo c : categoryInfos) {
			log.info("Top rank url is: " + c.getCategoryHref());
			String pageUrl = c.getCategoryHref();
			if (null == pageUrl || 0 == pageUrl.length()) {
				log.error("Top rank page url is null. Skip it!");
				continue;
			}
			String categoryName = c.getCategoryName();
			try {
				topRankPageParser.init();
				topRankPageParser.setPageUrl(pageUrl);
				topRankPageParser.setCategoryName(categoryName);
				getPageFlag = topRankPageParser.getPage();
				if (getPageFlag) {
					topRankPageParser.parsePage();
				} else {
					log.error("Top rank process error: get page failed. Skip it!");
					continue;
				}
			} catch (Exception e) {
				// TODO: handle exception
				log.error("Exception happened [Top Rank Process], url ["
						+ pageUrl + "]. Skip it!");
				log.error("Exception: ", e);
				continue;
			}
			topRankItemInfos.addAll(topRankPageParser.getTopTenItemInfos());
		}
	}

	/**
	 * Search result process
	 */
	public void searchResultProcess() {
		log.info("Search Result Process:--------------------------------------------------");
		sellerInSearchResults = new ArrayList<SellerInSearchResult>();
		List<SellerInSearchResult> tempList = new ArrayList<SellerInSearchResult>(); // 用来写数据库的暂时性的list
		SearchResultPageParser searchResultPageParser = new SearchResultPageParser();
		boolean getPageFlag = false;
		for (int i = 0; i < topRankItemInfos.size(); i++) {
			log.info("This is the item process no: " + i);
			TopRankItemInfo itemInfo = topRankItemInfos.get(i);
			String href = itemInfo.getHref();
			String categoryName = itemInfo.getCategoryName();
			String productName = itemInfo.getItemName();

			if (null == href || 0 == href.length()) {
				log.error("Search result page url is null. Skip it!");
				continue;
			}
			try {
				int sort = SystemConfiguration.sortOrderInSearchResult;
				// int sort = Integer.parseInt(XmlConfUtil
				// .getValueByName("sortOrderInSearchResult")); // 设置搜索出来页面的排序方式
				String sortKind = SearchResultPageParser.sortKind[sort];
				href += "&sort=" + sortKind;

				searchResultPageParser.init();
				searchResultPageParser.setPageUrl(href);
				searchResultPageParser.setCategoryName(categoryName);
				searchResultPageParser.setProductName(productName);
				getPageFlag = searchResultPageParser.getPage();
				if (getPageFlag) {
					searchResultPageParser.getSellerNumAndWeekSaleNum();
					itemInfo.setWeekSaleNum(searchResultPageParser.getSaleNum());
					itemInfo.setWeekSellerNum(searchResultPageParser
							.getSellerNum());

					searchResultPageParser.parsePage();
				} else {
					log.error("Search result process error: get page failed. Skip it!");
					continue;
				}
			} catch (Exception e) {
				// TODO: handle exception
				log.error("Exception happened [Search Result Process], url ["
						+ href + "]. Skip it!");
				log.error("Exception: " + e);
				continue;
			}
			tempList.addAll(searchResultPageParser.getSellerResultList());
			if (tempList.size() >= SystemConstant.DB_WRITE_NUM) {
				SearchResultPageParser.writeDataToDB(tempList);
				sellerInSearchResults.addAll(tempList);
				tempList.clear();
			}
		}
		TopRankPageParser.writeDataToDB(topRankItemInfos);
		if (tempList.size() > 0) {
			SearchResultPageParser.writeDataToDB(tempList);
			sellerInSearchResults.addAll(tempList);
			tempList.clear();
		}
		tempList = null;
		topRankItemInfos.clear();
		topRankItemInfos = null;
	}

	/**
	 * Item detail process
	 * */
	public void itemDetailProcess() {
		log.info("Item Detail Process:------------------------------------------------------------");
		itemInfos = new ArrayList<ItemInfo>();
		reviewBaseUrls = new ArrayList<ReviewBaseUrlInfo>();
		shopIndexs = new HashMap<String, ShopIndexInfo>();
		ItemDetailPageParser itemDetailPageParser = new ItemDetailPageParser();
		boolean getPageFlag = false;
		while (sellerInSearchResults.size() > 0) {
			for (int i = 0; i < sellerInSearchResults.size(); ++i) {
				SellerInSearchResult sellerInSearchResult = sellerInSearchResults
						.get(i);
				String href = sellerInSearchResult.getHref();
				if (null == href || 0 == href.length()) {
					log.info("Item detail page url is null. Skip it!");
					sellerInSearchResults.remove(i);
					continue;
				}
				try {
					itemDetailPageParser.init();
					itemDetailPageParser.setPageUrl(href);
					itemDetailPageParser.setItemId(sellerInSearchResult
							.getItemId());
					itemDetailPageParser.setTmall(sellerInSearchResult
							.isTmall());
					getPageFlag = itemDetailPageParser.getPage();
					if (getPageFlag) {
						itemDetailPageParser.parsePage();
						ItemInfo itemInfo = itemDetailPageParser.getItemInfo();
						String shopUrl = itemInfo.getShopUrl();
						String shopId = itemInfo.getShopId();
						String shopRateUrl = itemInfo.getShopRateUrl();

						if (null != shopId && !shopIndexs.containsKey(shopId)) {
							ShopIndexInfo shopIndexInfo = new ShopIndexInfo();
							shopIndexInfo.setShopId(shopId);
							shopIndexInfo.setShopRateUrl(shopRateUrl);
							shopIndexInfo.setShopUrl(shopUrl);
							shopIndexInfo.setTmall(itemInfo.isTmall());

							shopIndexs.put(shopId, shopIndexInfo);
							shopInfoList.add(shopIndexInfo);
						}

						SearchResultPageParser.updateShopId(
								itemInfo.getItemId(), itemInfo.getShopId());
						itemInfos.add(itemInfo);

						ReviewBaseUrlInfo baseUrlInfo = new ReviewBaseUrlInfo();
						baseUrlInfo.setItemId(itemInfo.getItemId());
						baseUrlInfo.setBaseUrl(itemInfo.getItemCommentBaseUrl());
						baseUrlInfo.setTmall(itemInfo.isTmall());
						reviewBaseUrls.add(baseUrlInfo);

						if (itemInfos.size() >= SystemConstant.DB_WRITE_NUM) {
							ItemDetailPageParser.writeDataToDB(itemInfos);
							itemInfos.clear();
						}
						sellerInSearchResults.remove(i);
					}
				} catch (Exception e) {
					log.error("Exception happened [Item Detail Process], url ["
							+ href + "]. Skip it!");
					log.error("Exception: ", e);
					continue;
				}

				--i;
			}
		}
		sellerInSearchResults.clear();
		sellerInSearchResults = null;
		if (itemInfos.size() > 0) {
			ItemDetailPageParser.writeDataToDB(itemInfos);
			itemInfos.clear();
		}
		itemInfos = null;
	}

	/**
	 * Shop item process
	 * */
	public void shopItemProcess() {
		log.info("Shop Item Process:------------------------------------------------------");
		int shopItemConfig = SystemConfiguration.shopItemConfig;
		if (1 == shopItemConfig) {
			log.info("Shop Item Process Finish: Config: not to fetch any info of shop item.");
			return;
		}
		ShopItemService shopItemService = new ShopItemService();
		for (ShopIndexInfo shopInfo : shopInfoList) {
			String shopUrl = shopInfo.getShopUrl();

			if (null == shopUrl || 0 == shopUrl.length()) {
				log.info("Shop item page url is null. Skip it!");
				continue;
			}
			try {
				shopItemService.init();
				shopItemService.setShopUrl(shopUrl);
				shopItemService.execute();
			} catch (Exception e) {
				// TODO: handle exception
				log.error("Exception happened [Shop Item Process], url ["
						+ shopUrl + "]. Skip it!");
				log.error("Exception: ", e);
				continue;
			}
		}
	}

	/**
	 * shop rate process
	 * */
	public void shopRateProcess() {
		log.info("Start to process store info(user rate).");
		storeInfos = new ArrayList<ShopRate>();
		ShopRatePageParser storeInfoPageParser = new ShopRatePageParser();
		for (ShopIndexInfo shopInfo : shopInfoList) {
			boolean isTmall = shopInfo.isTmall();
			String shopRateUrl = shopInfo.getShopRateUrl();

			if (null == shopRateUrl || 0 == shopRateUrl.length()) {
				log.info("Shop item page url is null. Skip it!");
				continue;
			}
			try {
				storeInfoPageParser.init();
				storeInfoPageParser.setPageUrl(shopRateUrl);
				storeInfoPageParser.setTmall(isTmall);
				boolean result = storeInfoPageParser.getPage();
				if (result) {
					storeInfoPageParser.parsePage();
					ShopRate storeInfo = storeInfoPageParser.getStoreInfo();
					if (null != storeInfo) {
						storeInfos.add(storeInfo);
						if (storeInfos.size() >= SystemConstant.DB_WRITE_NUM) {
							ShopRatePageParser.writeDataToDB(storeInfos);
							storeInfos.clear();
						}
					}
				}
			} catch (Exception e) {
				log.error("Exception happened [Store Info Process], url ["
						+ shopRateUrl + "]. Skip it!");
				log.error("Exception: ", e);
				continue;
			}
		}
		if (storeInfos.size() > 0) {
			ShopRatePageParser.writeDataToDB(storeInfos);
			storeInfos.clear();
		}
	}

	public void itemCommentHandler(boolean isTmall, String itemId,
			String baseCommentUrl) {
		AbstractItemCommentHandler handler;

		if (isTmall) {
			handler = new TmallItemCommentHandler(itemId, baseCommentUrl);
		} else {
			handler = new TaobaoItemCommentHandler(itemId, baseCommentUrl);
		}

		handler.handle();

		String lastReviewDate = handler.getFirstCommentDate();
		String firstReviewDate = handler.getLastCommentDate();
		ItemDetailPageParser.setReviewDate(itemId, firstReviewDate,
				lastReviewDate);
		log.info("first date: " + firstReviewDate);
		log.info("last date: " + lastReviewDate);
		handler.printMetrics();
	}

	public void itemCommentProcess() {
		log.info("Item Review Process:----------------------------------------------------------");
		for (int i = 0; i < reviewBaseUrls.size(); i++) {
			log.info("This is the item process no: " + i);
			ReviewBaseUrlInfo baseUrlInfo = reviewBaseUrls.get(i);
			String itemId = baseUrlInfo.getItemId();
			String baseUrl = baseUrlInfo.getBaseUrl();
			boolean isTmall = baseUrlInfo.isTmall();
			if (null == baseUrl || "".equals(baseUrl)) {
				log.info("The " + itemId
						+ " page's review base url is null. Skip it!");
				continue;
			}
			itemCommentHandler(isTmall, itemId, baseUrl);
		}
		reviewBaseUrls = null;
	}

	public static void main(String[] args) {
		new DBTest().execute();
		// test();
	}

	public static void test() {
		DBTest dbTest = new DBTest();
		dbTest.setUp();
		TBBrowser.init();
		DBManager.connectDB();
		// ItemDetailPageParser itemDetailPageParser = new
		// ItemDetailPageParser();
		// itemDetailPageParser.init();
		// itemDetailPageParser
		// .setPageUrl("http://item.taobao.com/item.htm?spm=a230r.1.10.17.FiwGIF&id=15544167282");
		// itemDetailPageParser.setItemId("15544167282");
		// itemDetailPageParser.setTmall(false);
		// boolean getPageFlag = itemDetailPageParser.getPage();
		// if (getPageFlag) {
		// itemDetailPageParser.parsePage();
		// ItemInfo itemInfo = itemDetailPageParser.getItemInfo();
		//
		// dbTest.itemCommentHandler(false, "15544167282",
		// itemInfo.getItemReviewsHref());
		// }
		ShopItemService shopItemService = new ShopItemService();
		shopItemService.init();
		shopItemService
				.setShopUrl("http://jinyucao.taobao.com/?spm=2013.1.1000126.21.Ojk82K");
		shopItemService.execute();

		DBManager.closeDB();
		TBBrowser.shutdown();
		dbTest.tearDown();
	}
}
