package edu.fudan.tbfetcher.pageparser;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.fudan.tbfetcher.constant.SystemConfiguration;
import edu.fudan.tbfetcher.constant.SystemConstant;
import edu.fudan.tbfetcher.dbaccess.DBManager;
import edu.fudan.tbfetcher.pojo.SellerInSearchResult;
import edu.fudan.tbfetcher.utils.DateTimeUtil;
import edu.fudan.tbfetcher.utils.TBBrowser;

public class SearchResultPageParser extends BasePageParser {

	private static final Logger log = Logger.getLogger(SearchResultPageParser.class);

	public static final String[] sortKind = new String[] { 
			"commend", // 默认
			"renqi-desc", // 人气从高到低
			"sale-desc", // 销量从高到低
			"credit-desc", // 信用从高到低
			"price-desc", // 价格从高到低
			"price-asc", // 价格从低到高
			"total-desc", // 总价从高到低
			"total-asc" // 总价从低到高
	};

	private String categoryName;
	private List<SellerInSearchResult> sellerResultList;
	private int saleNum;
	private int sellerNum;
	private String productName;

	public SearchResultPageParser(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public SearchResultPageParser() {
	}

	public List<SellerInSearchResult> getSellerResultList() {
		return sellerResultList;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getSaleNum() {
		return saleNum;
	}

	public int getSellerNum() {
		return sellerNum;
	}

	@Override
	public void init() {
		super.init();
		categoryName = null;
		saleNum = 0;
		sellerNum = 0;
		sellerResultList = new ArrayList<SellerInSearchResult>();
	}

	/**
	 * 获取周销量和卖家数
	 * */
	public void getSellerNumAndWeekSaleNum() {
		if (doc == null) {
			getPage();
		}
		Elements saleEls = doc.select("dl.product-shortcut dd strong.sale-num");
		if (saleEls.size() > 0) {
			Element saleEl = saleEls.get(0);
			String saleNumStr = saleEl.ownText();
			saleNum = Integer.parseInt(saleNumStr);

			Element sellerEl = saleEl.nextElementSibling();
			String sellerNumStr = sellerEl.ownText();
			sellerNum = Integer.parseInt(sellerNumStr);

			log.info("week sale num: " + saleNum);
			log.info("week seller num: " + sellerNum);
		}
	}

	public void parsePage() {
		log.info("Start to parse page " + SearchResultPageParser.class);

		int pageNum = 0;
		int maxPageNum = SystemConfiguration.pageNumInSearchResult;
		while (pageNum < maxPageNum) {
			log.info("Parsing page " + (pageNum + 1) + " ... ");
			Elements itemList = doc.select("form#bid-form li.list-item");
			for (int i = 0; i < itemList.size(); i++) {
				Element item = itemList.get(i);
				Elements tmall = item.select("div.icon-area > a");

				boolean isTmall = false;
				if (tmall.size() > 0 && "天猫".equals(tmall.get(0).attr("title"))) {
					isTmall = true;
				}
				Element itemLink = item.select("h3.summary a").get(0);
				String itemName = itemLink.attr("title");
				String itemHref = itemLink.attr("href");
				String itemId = null;
				int begin = 0, end = 0;
				if (isTmall) {
					itemHref = itemHref.substring(itemHref.lastIndexOf("http"));
				}
				String queryStr = itemHref.substring(itemHref.indexOf("?") + 1);
				String[] queries = queryStr.split("&");
				for (String str : queries) {
					String[] pair = str.split("=");
					if ("id".equals(pair[0]) || "item_id".equals(pair[0])) {
						itemId = pair[1];
					}
				}
				if(null != itemId){
					if(isInserted(itemId)){
						continue;
					}
				}else{
					continue;
				}
				String sellerName = item.select("p.seller > a").get(0).text();
				Elements globalBuy = item.select("p.seller > a.globalbuy");
				boolean isGlobalBuy = false;
				if (globalBuy.size() > 0) {
					isGlobalBuy = true;
				}
				boolean isGoldSeller = false;
				Elements brandSeller = item
						.select("div.icon-area > a.brand-seller");
				if (brandSeller.size() > 0) {
					isGoldSeller = true;
				}
				Elements itemAttr = item.select("ul.attribute");
				Elements priceList = itemAttr.select("li.price");
				String priceStr = priceList.select("em").get(0).text();
				double price = Double.valueOf(priceStr);
				String freightStr = priceList.select("span.shipping").get(0)
						.text();
				freightStr = freightStr.substring(3);
				double freight = Double.valueOf(freightStr);
				Elements creditcard = priceList.select("a.creditcard");
				boolean isCreditcard = false;
				if (creditcard.size() > 0) {
					isCreditcard = true;
				}
				Element placeEl = itemAttr.select("li.place").get(0);
				String sellerAddress = placeEl.ownText();
				String addrProvince = "", addrCity = "";
				if(sellerAddress.contains(" ")){
					String[] addrs = sellerAddress.split(" ");
					addrProvince = addrs[0];
					addrCity = addrs[1];
				}else {
					addrProvince = addrCity = sellerAddress;
				}
				String saleNumStr = itemAttr.select("li.sale").get(0).ownText();
				int saleNum = -1;
				if (null == saleNumStr || saleNumStr.length() == 0) {
					saleNum = 0;
				} else {
					begin = 4;
					end = saleNumStr.lastIndexOf("笔");
					saleNumStr = saleNumStr.substring(begin, end);
					saleNum = Integer.valueOf(saleNumStr);
				}
				String reviews = itemAttr.select("li.sale > a.reviews").text();
				int reviewsNum = -1;
				if (null == reviews || 0 == reviews.length()) {
					reviewsNum = 0;
				} else {
					end = reviews.indexOf("条");
					reviews = reviews.substring(0, end);
					reviewsNum = Integer.valueOf(reviews);
				}

				Element legend2 = itemAttr.select("li.legend2").get(0);
				boolean isConsumerPromise = false;
				boolean isLeaveACompensableThree = false;// 假一赔三
				boolean isSevenDayReturn = false;
				boolean isQualityItem = false;
				boolean is30DaysMaintain = false;
				if (legend2.select("a.xb-as-fact").size() > 0) {
					isConsumerPromise = true;
				}
				if (legend2.select("a.xb-sevenday-return").size() > 0) {
					isSevenDayReturn = true;
				}
				if (legend2.select("a.xb-quality_item").size() > 0) {
					isQualityItem = true;
				}
				if (legend2.select("a.xb-jia1-pei3").size() > 0) {
					isLeaveACompensableThree = true;
				}
				if (legend2.select("a.xb-thirtyday-repair").size() > 0) {
					is30DaysMaintain = true;
				}

				// get shop user_number_id
				long sellerId = 0L;
				Elements sellerInfo = item.select("p.seller,.lister,.hCard");
				if (sellerInfo.size() > 0) {

					if (sellerInfo.select("a").size() > 0) {
						Element seller = sellerInfo.select("a").first();
						sellerId = Long.parseLong(seller.attr("href")
								.split("=")[1]);
					} else {
						log.error("There is no seller  in the search result page.");
					}
				} else {
					log.error("There is no seller info in the search result page.");
				}

				log.info("Seller id is: " + sellerId);

				int rank = i + 1;

				SellerInSearchResult sellerItemInfo = new SellerInSearchResult();
				sellerItemInfo.setUserNumberId(sellerId);
				sellerItemInfo.setProductName(productName);
				sellerItemInfo.setItemId(itemId);
				sellerItemInfo.setTmall(isTmall);
				sellerItemInfo.setItemName(itemName);
				sellerItemInfo.setHref(itemHref);
				sellerItemInfo.setCategoryName(categoryName);
				sellerItemInfo.setSellerName(sellerName);
				sellerItemInfo.setGlobalBuy(isGlobalBuy);
				sellerItemInfo.setGoldSeller(isGoldSeller);
				sellerItemInfo.setPrice(price);
				sellerItemInfo.setFreightPrice(freight);
				sellerItemInfo.setCreditCardPay(isCreditcard);
				sellerItemInfo.setSellerAddress(sellerAddress);
				sellerItemInfo.setAddrProvince(addrProvince);
				sellerItemInfo.setAddrCity(addrCity);
				sellerItemInfo.setSaleNum(saleNum);
				sellerItemInfo.setReviews(reviewsNum);
				sellerItemInfo.setConsumerPromise(isConsumerPromise);
				sellerItemInfo.setSevenDayReturn(isSevenDayReturn);
				sellerItemInfo.setQualityItem(isQualityItem);
				sellerItemInfo.setIs30DaysMaintain(is30DaysMaintain);
				sellerItemInfo.setPage(pageNum + 1);
				sellerItemInfo.setRank(rank);
				sellerItemInfo.setLeaveACompensableThree(isLeaveACompensableThree);
				sellerItemInfo.setFetchTime(DateTimeUtil.getCurrentDateTime());

				log.info("----------------------------------------");
				log.info("seller name is: " + sellerName);
				log.info("isConsumerPromise: " + isConsumerPromise);
				log.info("is30DaysMaintain: " + is30DaysMaintain);
				log.info("isCreditcard: " + isCreditcard);
				log.info("isQualityItem: " + isQualityItem);
				log.info("isLeaveACompensableThree: "
						+ isLeaveACompensableThree);
				log.info("isSevenDayReturn: " + isSevenDayReturn);

				sellerResultList.add(sellerItemInfo);
			}
			Elements pageUrlEls = doc.select("div.page-bottom > a.page-next");
			if (pageUrlEls.size() > 0) {
				Element link = pageUrlEls.get(0);
				String href = link.attr("href");
				href = "http://s.taobao.com" + href;
				boolean result = getNextPage(href);
				if (!result) {
					break;
				}
				pageNum++;
			} else {
				break;
			}
		}
	}

	private boolean getNextPage(String href) {
		boolean result = TBBrowser.get(href, null);
		if (result) {
			setPageUrl(href);
			Document doc = TBBrowser.getDoc();
			if(null != doc){
				setDoc(doc);
				return true;
			}
		}
		return false;
	}

	/**
	 * check the item id is inserted or not
	 * @param itemId
	 * */
	private boolean isInserted(String itemId){
		for(SellerInSearchResult seller : sellerResultList){
			if(seller.getItemId().equals(itemId)){
				return true;
			}
		}
		return false;
	}
	
	public static void writeDataToDB(List<SellerInSearchResult> list) {
		try {
			String result[] = SellerInSearchResult.getCreateSQLStr();
			String tableName = result[0];
			String createTableStr = result[1];

			DBManager.connectDB();
			if (!DBManager.isTableExist(tableName)) {
				log.info("create table " + tableName);
				DBManager.createTable(createTableStr);
				log.info("create table success!");
			}

			for (int i = 0; i < list.size(); i++) {
				SellerInSearchResult data = list.get(i);
				DBManager.insertData(data, tableName);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("Search Result Write to DB Error");
			log.error("Exception: ", e);
		} finally{
			DBManager.closeDB();
		}
	}

	public static void updateShopId(String itemId, String shopId) {
		String sqlStr = "update " + SystemConstant.SEARCH_RESULT_TABLE
				+ " set shopId='" + shopId + "' where itemId='" + itemId + "'";
		try {
			DBManager.connectDB();
			DBManager.executeSQL(sqlStr);
		} catch (Exception e) {
			log.error("Search Result updateShopId to DB Error");
			log.error("Exception: ", e);
		} finally{
			DBManager.closeDB();
		}
	}
}
