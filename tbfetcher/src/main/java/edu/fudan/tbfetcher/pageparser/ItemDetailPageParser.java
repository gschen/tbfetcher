package edu.fudan.tbfetcher.pageparser;

import java.util.List;

import javax.xml.soap.Detail;

import jxl.write.WritableSheet;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.fudan.tbfetcher.constant.SystemConstant;
import edu.fudan.tbfetcher.dbaccess.DBManager;
import edu.fudan.tbfetcher.pojo.ItemInfo;
import edu.fudan.tbfetcher.pojo.ItemInfoData;
import edu.fudan.tbfetcher.service.ImpressService;
import edu.fudan.tbfetcher.service.ItemDataService;
import edu.fudan.tbfetcher.service.PostageService;
import edu.fudan.tbfetcher.service.TmallReviewCountSevice;
import edu.fudan.tbfetcher.utils.DateTimeUtil;

public class ItemDetailPageParser extends BasePageParser {
	private static final Logger log = Logger.getLogger(ItemDetailPageParser.class);

	private ItemInfo itemInfo;
	private String itemId;
	private boolean isTmall;
	private ImpressService impressService;
	private PostageService postageService;
	private TmallReviewCountSevice tmallReviewCountSevice;
	private ItemDataService itemDataService;

	public void setTmall(boolean isTmall) {
		this.isTmall = isTmall;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public ItemInfo getItemInfo() {
		return itemInfo;
	}

	@Override
	public void init() {
		super.init();
		impressService = new ImpressService();
		postageService = new PostageService();
		tmallReviewCountSevice = new TmallReviewCountSevice();
		itemDataService = new ItemDataService();
		itemId = null;
		isTmall = false;
	}

	/**
	 * 针对各种不同的页面进行不同的处理 1.天猫页面；2.竞拍页面；3.正常页面；
	 * 增价拍页面如：http://item.taobao.com/item.htm?id=15577727894 如何区分不同的页面类型？
	 */
	@Override
	public void parsePage() {
		log.info("Start to parse page: " + ItemDetailPageParser.class);

		itemInfo = new ItemInfo();
		itemInfo.setViewCounter(-2); // 初始化为-2，即宝贝已下架

		itemInfo.setTmall(isTmall);
		itemInfo.setItemId(itemId);
		itemInfo.setItemDetailUrl(pageUrl);
		itemInfo.setFetchTime(DateTimeUtil.getCurrentDateTime());
		if (isTmall) {
			itemInfo.setViewCounter(-1);
			log.info("Tmall page parse.");
			if (pageUrl.contains("chaoshi.tmall.com")) {
				// tmallMarketPageParse();
			} else {
				tmallPageParser();
			}
		} else if (rawText.contains("tbid-container")) {
			log.info("Start to parse 增价拍 page.");
			bidTypePageParser();
		} else if (rawText.contains("此宝贝已下架")) {
			log.info("此宝贝已下架!");
		} else {
			normalPageParser();
		}
	}

	private void tmallPageParser() {
		/** 先查看是否出错 */
		if (doc.select("div.errorPage").size() > 0) {
			log.error("Item Detail Page Parse Error: Return the wrong page.");
			return;
		}

		/** get shopId */
		Elements metaEls = doc.select("meta[name=microscope-data]");
		if (metaEls.size() > 0) {
			Element metaEl = metaEls.get(0);
			String content = metaEl.attr("content");
			int begin = content.indexOf("shopId=");
			int end = content.indexOf(";", begin);

			String shopId = content.substring(begin + 7, end);
			itemInfo.setShopId(shopId);
			log.info("Item detail: shopId:" + shopId);
		}

		/** get shop url */
		Elements shopEls = doc.select("a.enter");
		if (shopEls.size() > 0) {
			Element shopEl = shopEls.get(0);
			String href = shopEl.attr("href");
			itemInfo.setShopUrl(href);
			log.info("Item detail: shopUrl:" + href);
		}

		/** Item name */
		// 两种情况获取item name
		Elements nameEls = doc.select("div.tb-detail-hd h3 a");
		if (null != nameEls && nameEls.size() > 0) {
			String itemName = nameEls.get(0).ownText();
			log.info("item detail: item name : " + itemName);
			itemInfo.setItemName(itemName);
		}
		Elements itemNameEles2 = doc.select("h3.detail-hd");
		if (null != itemNameEles2 && itemNameEles2.size() > 0) {
			String itemName = itemNameEles2.first().ownText();
			log.info("item detail: item name : " + itemName);
			itemInfo.setItemName(itemName);
		}

		/** pay type */
		String payType = "";
		Elements links = doc.select("div.J_Paylist a");
		if (links.size() > 0) {
			for (Element link : links) {
				if (!"#".equals(link.attr("href"))) {
					payType += link.ownText() + ", ";
				}
			}
			payType = payType.substring(0, payType.lastIndexOf(","));
			itemInfo.setPayType(payType);
			log.info("payType: " + payType);
		}

		/** get price range */
		// 两种情况获取price range
		Elements priceEls = doc.select("strong.J_originalPrice");
		if (priceEls.size() > 0) {
			String price = priceEls.get(0).ownText();
			itemInfo.setPriceRange(price);
			log.info("price range: " + price);
		}
		Elements priceEls2 = doc.select("strong#J_Price");
		if (priceEls2.size() > 0) {
			String price = priceEls2.get(0).ownText();
			price = price.substring(1);
			itemInfo.setPriceRange(price);
			log.info("price range: " + price);
		}

		/** get num in stock */
		Element stockEl = doc.getElementById("J_SpanStock");
		if (null != stockEl) {
			String stockStr = stockEl.ownText();
			int numInStock = Integer.parseInt(stockStr);
			itemInfo.setNumInStock(numInStock);
			log.info("num in stock: " + numInStock);
		}

		/** get capacity && spec */
		Elements elements = doc.select("div#J_AttrList ul li");
		if (null != elements) {
			log.info("Element size is: " + elements.size());
			if (elements.size() >= 2) {
				// 化妆品规格: 正常规格
				Element specEl = elements.get(1);
				String text = specEl.ownText();
				if (text.contains("规格")) {
					String spec = elements.get(1).attr("title").trim();
					spec = spec.substring(1, spec.length());
					log.info("spec: " + spec);
					itemInfo.setSpec(spec);
				}
			}
			if (elements.size() >= 3) {
				// 化妆品容量: 其它容量
				Element capacityEl = elements.get(2);
				String text = capacityEl.ownText();
				if (text.contains("容量")) {
					String capacity = capacityEl.attr("title").trim();
					capacity = capacity.substring(1, capacity.length());
					log.info("capacity: " + capacity);
					itemInfo.setCapacity(capacity);
				}
			}
		}

		itemInfo.setNumOfCollectItem(-1);
		itemInfo.setNumOfCollectShop(-1);
		itemInfo.setNumOfLike(-1);
		itemInfo.setNumOfShare(-1);

		try {
			itemDataService.init();
			itemDataService.setItemId(itemId);
			itemDataService.setTmall(true);
			itemDataService.execute();
			int numOfItemCollect = itemDataService.getData().getItemCount();
			itemInfo.setNumOfCollectItem(numOfItemCollect);
		} catch (Exception e) {
			log.error("Item Detail Page Parse Error, item data service error[tmall]");
			log.error("itemId: " + itemId);
			log.error("Exception: ", e);
		}

		/** service type */
		Elements serviceEls = doc.select("span.tb-serPromise-name");
		if (serviceEls.size() > 0) {
			String serviceStr = "";
			for (Element el : serviceEls) {
				String text = el.ownText();
				serviceStr += text + ",";
			}
			serviceStr = serviceStr.substring(0, serviceStr.length() - 1);
			log.info("support service: " + serviceStr);
			itemInfo.setServiceType(serviceStr);
		}

		/** get impress */
		try {
			impressService.init();
			impressService.setTmall(true);
			impressService.setItemId(itemId);
			impressService.execute();
			String impress = impressService.getImpress();
			log.info("impress: " + impress);
			itemInfo.setImpress(impress);
		} catch (Exception e) {
			log.error("Item Detail Page Parse Error, impress service error[tmall]");
			log.error("itemId: " + itemId);
			log.error("Exception: ", e);
		}

		/** get postage & month sale */
		try {
			postageService.init();
			postageService.setRawText(doc.toString());
			postageService.setTmall(true);
			postageService.setDoc(doc);
			postageService.setItemUrl(pageUrl);
			postageService.setReferer(pageUrl);
			postageService.execute();
			String freight = postageService.getFreight();
			int saleSum = postageService.getSaleSum();

			itemInfo.setFreightPrice(freight);
			itemInfo.setSaleNumIn30Days(saleSum);
			log.info("freight price: " + freight);
			log.info("month sale: " + saleSum);
		} catch (Exception e) {
			log.error("Item Detail Page Parse Error, postage service error[tmall]");
			log.error("RawText:");
			log.error(doc.toString());
			log.error("itemUrl: " + pageUrl);
			log.error("Exception: ", e);
		}

		/** get review count */
		String sellerId = "";
		try {
			tmallReviewCountSevice.init();
			tmallReviewCountSevice.setItemId(itemId);
			tmallReviewCountSevice.setMarket(false);
			if (metaEls.size() > 0) {
				String content = metaEls.get(0).attr("content");
				int begin = content.indexOf("userid=") + 7;
				int end = content.indexOf(";", begin);
				sellerId = content.substring(begin, end);

				tmallReviewCountSevice.setSellerId(sellerId);
				tmallReviewCountSevice.execute();
				int reviewCount = tmallReviewCountSevice.getReviewCount();
				itemInfo.setTotalCommentNum(reviewCount);
				log.info("reviews count: " + reviewCount);
			}
		} catch (Exception e) {
			log.error("Item Detail Page Parse Error, tmall review count service error[tmall]");
			log.error("itemId: " + itemId);
			log.error("sellerId: " + sellerId);
			log.error("Exception: ", e);
		}

		/** user rate href */
		Elements rateEls = doc.select("div.shop-rate ul li a");
		if (rateEls.size() > 0) {
			Element rateEl = rateEls.get(0);
			String href = rateEl.attr("href");
			itemInfo.setShopRateUrl(href);

			log.info("user rate href: " + href);
		}

		/** item review base url */
		String reviewBaseUrl = "";
		reviewBaseUrl += "http://rate.tmall.com/list_detail_rate.htm?callback=jsonp1351840852205&itemId="
				+ itemId;
		reviewBaseUrl += "&order=1&forShop=1&append=0&currentPage=";
		itemInfo.setItemCommentBaseUrl(reviewBaseUrl);
		log.info("review base url: " + reviewBaseUrl);
	}

	private void normalPageParser() {
		/** 先查看是否出错 */
		if (doc.select("div.error-notice").size() > 0) {
			log.error("Item Detail Page Parse Error: Return the wrong page.");
			return;
		}
		/** get shopId */
		Elements metaEls = doc.select("meta[name=microscope-data]");
		if (metaEls.size() > 0) {
			Element metaEl = metaEls.get(0);
			String content = metaEl.attr("content");
			int begin = content.indexOf("shopId=");
			int end = content.indexOf(";", begin);

			String shopId = content.substring(begin + 7, end);
			itemInfo.setShopId(shopId);
			log.info("Item detail: shopId:" + shopId);
		}

		/** get shop url */
		Elements shopEls = doc.select("a.tb-enter-shop");
		if (shopEls.size() > 0) {
			Element shopEl = shopEls.get(0);
			String href = shopEl.attr("href");
			itemInfo.setShopUrl(href);
			log.info("Item detail: shopUrl:" + href);
		}

		/** Get base review url */
		if (rawText.contains("data-listApi")) {
			int base = rawText.indexOf("data-listApi=");
			int begin = rawText.indexOf("\"", base);
			int end = rawText.indexOf("\"", begin + 1);
			String reviewBaseUrl = rawText.substring(begin + 1, end);

			itemInfo.setItemCommentBaseUrl(reviewBaseUrl);
			log.info("Item Review Base url is: " + reviewBaseUrl);
		}

		/** Item name */
		Elements nameEls = doc.select("div.tb-detail-hd h3");
		if (null != nameEls && nameEls.size() > 0) {
			String itemName = nameEls.get(0).ownText();
			log.info("item detail: item name : " + itemName);
			itemInfo.setItemName(itemName);
		}

		// get price etc.
		Elements tbProEls = doc.select("div.tb-property");
		if (null != tbProEls && tbProEls.size() > 0) {
			Element itemPro = tbProEls.get(0);
			// price range
			String priceRange = null;
			Elements priceEls = itemPro
					.select("strong#J_StrPrice em.tb-rmb-num");
			if (priceEls.size() > 0) {
				priceRange = priceEls.get(0).ownText();
			}
			log.info("priceRange: " + priceRange);
			itemInfo.setPriceRange(priceRange);

			// impress
			try {
				impressService.init();
				impressService.setTmall(false);
				impressService.setDoc(doc);
				impressService.execute();
				String impress = impressService.getImpress();
				itemInfo.setImpress(impress);
				itemInfo.setRateGoodCount(impressService.getRateGoodCount());
				itemInfo.setRateNormalCount(impressService.getRateNormalCount());
				itemInfo.setRateBadCount(impressService.getRateBadCount());
				itemInfo.setAdditionalCount(impressService.getAdditionalCount());

				log.info("impress: " + impress);
			} catch (Exception e) {
				log.error("Item Detail Page Parse, impress service[taobao]");
				log.error("RawText:");
				log.error(doc);
				log.error("Exception: ", e);
			}
			

			// view count
			try {
				String shopId = itemInfo.getShopId();
				if(null == shopId || 0 == shopId.length()){
					log.error("Item Detail Page Parse[taobao][url]: " + pageUrl);
					log.error("ShopId is null");
				}else{
					itemDataService.init();
					itemDataService.setTmall(false);
					itemDataService.setItemId(itemId);
					itemDataService.setShopId(itemInfo.getShopId());
					itemDataService.execute();
					ItemInfoData data = itemDataService.getData();

					itemInfo.setViewCounter(data.getViewCount());
					itemInfo.setTotalCommentNum(data.getFeedCount());
					itemInfo.setNumOfCollectItem(data.getItemCount());
					itemInfo.setNumOfCollectShop(data.getStoreCount());
					itemInfo.setNumOfLike(data.getLikeCount());
					itemInfo.setNumOfShare(data.getShareCount());

					log.info("View counter is: " + itemInfo.getViewCounter());
				}
			} catch (Exception e) {
				log.error("Item Detail Page Parse Error, item data service[taobao]");
				log.error("itemId: " + itemId);
				log.error("shopId: " + itemInfo.getShopId());
				log.error("Exception: " + e);
			}

			// pay type
			String payType = "";
			Elements links = itemPro.select("dl.tb-paymethods a");
			for (Element link : links) {
				if (!"#".equals(link.attr("href"))) {
					payType += link.ownText() + ", ";
				}
			}
			log.info("PayType is: " + payType);
			payType = payType.substring(0, payType.lastIndexOf(","));
			log.info("payType: " + payType);
			itemInfo.setPayType(payType);

			/** service type */
			String serviceType = "";
			links = itemPro.select("dl.tb-featured-services a");
			int i = 0;
			for (Element link : links) {
				serviceType += link.attr("title");
				if (i == (links.size() - 1)) {

				} else {
					serviceType += ",";
				}
				++i;
			}
			log.info("serviceType: " + serviceType);
			itemInfo.setServiceType(serviceType);

			/** stock num */
			Element inStockEl = doc.getElementById("J_SpanStock");
			if (null != inStockEl) {
				String inStockStr = inStockEl.ownText().trim();
				int numStock = Integer.parseInt(inStockStr);
				itemInfo.setNumInStock(numStock);
				log.info("num in stock: " + numStock);
			}

			/** item type: 全新 */
			Elements typeEls = doc.select("li.tb-item-type em");
			if (typeEls.size() > 0) {
				Element typeEl = typeEls.get(0);
				String type = typeEl.ownText();
				itemInfo.setItemType(type);
				log.info("Item type: " + type);
			}
		}

		/** user rate href */
		Element rankEl = doc.getElementById("shop-rank");
		if (null != rankEl) {
			String href = rankEl.attr("href");
			itemInfo.setShopRateUrl(href);
			log.info("user rate href: " + href);
		}

		/** postage service */
		try {
			postageService.init();
			postageService.setRawText(doc.toString());
			postageService.setTmall(false);
			postageService.execute();
			itemInfo.setSaleNumIn30Days(postageService.getSaleSum());

			String freightPrice = postageService.getFreight();
			itemInfo.setFreightPrice(freightPrice);
			log.info("freightPrice: " + freightPrice);
		} catch (Exception e) {
			log.error("Item Detail Page Parse, postage service error[taobao]");
			log.error("Rawtext:");
			log.error(doc);
			log.error("Exception: ", e);
		}

		Elements elements = doc.select("div#attributes ul.attributes-list li");
		if (null != elements) {
			log.info("Element size is: " + elements.size());
			if (elements.size() >= 2) {
				// 化妆品规格: 正常规格
				Element specEl = elements.get(1);
				String text = specEl.ownText();
				if (text.contains("规格")) {
					String spec = elements.get(1).attr("title").trim();
					spec = spec.substring(1, spec.length());
					log.info("spec: " + spec);
					itemInfo.setSpec(spec);
				}
			}
			if (elements.size() >= 3) {
				// 化妆品容量: 其它容量
				Element capacityEl = elements.get(2);
				String text = capacityEl.ownText();
				if (text.contains("容量")) {
					String capacity = capacityEl.attr("title").trim();
					capacity = capacity.substring(1, capacity.length());
					log.info("capacity: " + capacity);
					itemInfo.setCapacity(capacity);
				}
			}
		}
	}

	/**
	 * 增价拍页面处理模块 http://item.taobao.com/item.htm?id=14730950078
	 */
	private void bidTypePageParser() {
		log.info("bid type page parse: nothing to do.");
	}

	public static void writeDataToDB(List<ItemInfo> list) {
		try {
			String result[] = ItemInfo.getCreateSQLStr();
			String tableName = result[0];
			String createTableStr = result[1];

			DBManager.connectDB();
			if (!DBManager.isTableExist(tableName)) {
				log.info("create table " + tableName);
				DBManager.createTable(createTableStr);
				log.info("create table success!");
			}

			for (int i = 0; i < list.size(); i++) {
				ItemInfo data = list.get(i);
				DBManager.insertData(data, tableName);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("Item Detail Page Parse Error: Write into DB exception.");
			log.error("Exception: ", e);
		} finally {
			DBManager.closeDB();
		}
	}

	public static void setReviewDate(String itemId, String firstDate,
			String lastDate) {
		String sqlStr = "update " + SystemConstant.ITEM_DETAIL_TABLE
				+ " set oldestCommentDate='" + firstDate
				+ "', latestCommentDate='" + lastDate + "' where itemId='"
				+ itemId + "'";
		try {
			DBManager.connectDB();
			DBManager.executeSQL(sqlStr);
		} catch (Exception e) {
			log.error("Item Detail Page Parse setReviewDate to DB Error");
			log.error("Exception: ", e);
		}
	}
}
