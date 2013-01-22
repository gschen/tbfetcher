package edu.fudan.tbfetcher.pageparser;

import java.util.List;

import jxl.write.WritableSheet;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.fudan.tbfetcher.dbaccess.DBManager;
import edu.fudan.tbfetcher.pojo.MonthServiceEntity;
import edu.fudan.tbfetcher.pojo.ShopRate;
import edu.fudan.tbfetcher.service.MonthService;
import edu.fudan.tbfetcher.utils.DateTimeUtil;

/**
 * 
 * 店铺信用页面解析
 * 
 * @author JustinChen
 * 
 */
public class ShopRatePageParser extends BasePageParser {

	private static final Logger log = Logger.getLogger(ShopRatePageParser.class);
	private ShopRate shopRateInfo;
	private boolean isTmall;
	private MonthService monthService;
	private long userNumberId = 0L;

	public void setUserNumberId(long userNumberId) {
		this.userNumberId = userNumberId;
	}

	public void setTmall(boolean isTmall) {
		this.isTmall = isTmall;
	}

	public ShopRate getStoreInfo() {
		return shopRateInfo;
	}

	@Override
	public void init() {
		super.init();
		monthService = new MonthService();
		isTmall = false;
		shopRateInfo = new ShopRate();
	}

	@Override
	public void parsePage() {
		log.info("Start to parse page " + ShopRatePageParser.class);
		shopRateInfo.setSellerRateHref(pageUrl);
		shopRateInfo.setTmall(isTmall);
		shopRateInfo.setFetchTime(DateTimeUtil.getCurrentDateTime());
		if (isTmall) {
			tmallPageParse();
		} else {
			taobaoPageParse();
		}
	}

	public void taobaoPageParse() {
		Elements sellerInfoEls = doc.select("div.personal-info div.left-box div.bd");
		if (sellerInfoEls.size() > 0) {
			Element sellerInfoEl = sellerInfoEls.get(0);

			/** seller name:卖家姓名 */
			Elements nameEls = sellerInfoEl.select("div.title > a");
			if (nameEls.size() > 0) {
				String sellerName = nameEls.get(0).ownText();
				shopRateInfo.setSellerName(sellerName);
				log.info("seller name: " + sellerName);
			}

			/** main sale:当前主营 */
			Elements saleEls = sellerInfoEl.select("ul:not(.sep) > li > a");
			if (saleEls.size() > 0) {
				String mainSale = saleEls.get(0).ownText();
				char spa = (char) 160;
				mainSale = mainSale.replace(spa, ' ');
				shopRateInfo.setMainSale(mainSale.trim());
				log.info("main sale: " + mainSale);
			}

			/** location:所在地区 */
			Elements locEls = sellerInfoEl.select("ul li");
			if (locEls.size() > 1) {
				String location = locEls.get(1).ownText();
				location = location.substring(location.indexOf("：") + 1).trim();
				location = location.replace((char) 160, ' ');
				shopRateInfo.setLocation(location.trim());
				log.info("seller location info: " + location);
			}

			/** create time: 创店时间 */
			Element createShopEl = doc.getElementById("J_showShopStartDate");
			if (null != createShopEl) {
				String createDate = createShopEl.val();
				shopRateInfo.setCreateShopDate(createDate);
				log.info("create shop time: " + createDate);
			}

			/** seller credit：卖家信用 */
			Elements creditEls = sellerInfoEl.select("ul.sep li");
			if (creditEls.size() > 0) {
				String sellerCredit = creditEls.get(0).ownText();
				sellerCredit = sellerCredit.substring(
						sellerCredit.indexOf("：") + 1, sellerCredit.length());
				shopRateInfo.setSellerRate(sellerCredit);
				log.info("seller credit: " + sellerCredit);
			}

			/** buyer credit:买家信用 */
			if (creditEls.size() > 1) {
				String buyerCredit = creditEls.get(1).ownText();
				buyerCredit = buyerCredit.substring(
						buyerCredit.indexOf("：") + 1, buyerCredit.length());
				shopRateInfo.setBuyerRate(buyerCredit);
				log.info("buyer credit: " + buyerCredit);
			}
		}

		/** store service: 店铺服务 */
		String serviceType = "";
		Elements storeDescEls = doc.select("div.desc ul li a");
		for (Element el : storeDescEls) {
			serviceType += el.text() + ",";
		}
		if (serviceType.contains(",")) {
			serviceType = serviceType
					.substring(0, serviceType.lastIndexOf(","));
		}
		shopRateInfo.setServiceType(serviceType);
		log.info("service type: " + serviceType);

		/** charge num: 保证金 */
		Elements chargeEls = doc.select("div.charge span");
		if (chargeEls.size() > 0) {
			String chargeNum = chargeEls.get(0).ownText().trim();
			shopRateInfo.setChargeNum(chargeNum);
			log.info("store charge num is:" + chargeNum);
		}

		/** store half year score: 店铺半年内动态评分 */
		Elements dynamicRateEls = doc
				.select("div#sixmonth ul li div.item-scrib em.count");
		if (dynamicRateEls.size() > 0) {
			Element matchEl = dynamicRateEls.get(0);
			String matchScore = matchEl.ownText();
			shopRateInfo.setMatchScore(matchScore);
			log.info("match rate score: " + matchScore);

			Element matchPaEl = matchEl.parent();
			if (matchPaEl.select("strong.over").size() > 0) {
				shopRateInfo.setMatchCompToAvg("高");
			} else if (matchPaEl.select("strong.lower").size() > 0) {
				shopRateInfo.setMatchCompToAvg("低");
			} else {
				shopRateInfo.setMatchCompToAvg("平");
			}
		}
		if (dynamicRateEls.size() > 1) {
			Element serviceEl = dynamicRateEls.get(1);
			String serviceScore = serviceEl.ownText();
			shopRateInfo.setServiceScore(serviceScore);
			log.info("service rate score: " + serviceScore);

			Element servicePaEl = serviceEl.parent();
			if (servicePaEl.select("strong.over").size() > 0) {
				shopRateInfo.setServiceCompToAvg("高");
			} else if (servicePaEl.select("strong.lower").size() > 0) {
				shopRateInfo.setServiceCompToAvg("低");
			} else {
				shopRateInfo.setServiceCompToAvg("平");
			}
		}
		if (dynamicRateEls.size() > 2) {
			Element consignEl = dynamicRateEls.get(2);
			String consignmentScore = consignEl.ownText();
			shopRateInfo.setConsignmentScore(consignmentScore);
			log.info("consignment rate score: " + consignmentScore);

			Element consignPaEl = consignEl.parent();
			if (consignPaEl.select("strong.over").size() > 0) {
				shopRateInfo.setConsignCompToAvg("高");
			} else if (consignPaEl.select("strong.lower").size() > 0) {
				shopRateInfo.setConsignCompToAvg("低");
			} else {
				shopRateInfo.setConsignCompToAvg("平");
			}
		}

		/** month service: 店铺30天内服务情况 */
		monthService.init();
		monthService.setUserNumberId(userNumberId);
		monthService.setDoc(doc);
		monthService.execute();
		List<MonthServiceEntity> serviceEntities = monthService
				.getMonthServieEntities();
		shopRateInfo.setRefundmentScore(serviceEntities.get(0).getNativeValue());
		shopRateInfo.setAvgOfRefund(serviceEntities.get(0).getAvgValue());
		shopRateInfo.setRefundmentRateScore(serviceEntities.get(1)
				.getNativeValue());
		shopRateInfo.setAvgOfRefundRate(serviceEntities.get(1).getAvgValue());
		shopRateInfo.setComplaintScore(serviceEntities.get(2).getNativeValue());
		shopRateInfo.setAvgOfComplaint(serviceEntities.get(2).getAvgValue());
		shopRateInfo.setPunishmentScore(serviceEntities.get(3).getNativeValue());
		shopRateInfo.setAvgOfPunish(serviceEntities.get(3).getAvgValue());

		/** seller good rate:好评率 */
		Elements goodRateEls = doc.select("h4.seller em.gray");
		if (goodRateEls.size() > 0) {
			String goodRate = goodRateEls.get(0).ownText();
			goodRate = goodRate.substring(goodRate.indexOf("：") + 1,
					goodRate.length());
			shopRateInfo.setSellerGoodRate(goodRate);
			log.info("seller good rate: " + goodRate);
		}

		Elements creditEls = doc.select("div#J_show_list ul li");
		/** week review data: 最近一周 */
		if (creditEls.size() > 0) {
			Elements weekRateEls = creditEls.get(0).select("table tbody tr");
			if (weekRateEls.size() > 1) {
				Element weekSumEl = weekRateEls.get(1);
				String weekSumRateOk = weekSumEl.select("td.rateok").text();
				String weekSumRateNormal = weekSumEl.select("td.ratenormal")
						.text();
				String weekSumRateBad = weekSumEl.select("td.ratebad").text();
				shopRateInfo.setWeekSumRateOk(weekSumRateOk);
				shopRateInfo.setWeekSumRateNormal(weekSumRateNormal);
				shopRateInfo.setWeekSumRateBad(weekSumRateBad);
				log.info("Week sum rate ok is: " + weekSumRateOk);
				log.info("Week sum rate normal is: " + weekSumRateNormal);
				log.info("Week sum rate bad is: " + weekSumRateBad);
			}
			if (weekRateEls.size() > 2) {
				Elements weekMainEl = weekRateEls.get(2).select("td");
				String weekMainRateOk = weekMainEl.get(1).text();
				String weekMainRateNormal = weekMainEl.get(2).text();
				String weekMainRateBad = weekMainEl.get(3).text();
				shopRateInfo.setWeekMainRateOk(weekMainRateOk);
				shopRateInfo.setWeekMainRateNormal(weekMainRateNormal);
				shopRateInfo.setWeekMainRateBad(weekMainRateBad);
				log.info("Week main rate ok is: " + weekMainRateOk);
				log.info("Week main rate normal is: " + weekMainRateNormal);
				log.info("Week main rate bad is: " + weekMainRateBad);
			}
			if (weekRateEls.size() > 3) {
				Elements weekNotMainEl = weekRateEls.get(3).select("td");
				String weekNotMainRateOk = weekNotMainEl.get(1).text();
				String weekNotMainRateNormal = weekNotMainEl.get(2).text();
				String weekNotMainRateBad = weekNotMainEl.get(3).text();
				shopRateInfo.setWeekNotmainRateOk(weekNotMainRateOk);
				shopRateInfo.setWeekNotmainRateNormal(weekNotMainRateNormal);
				shopRateInfo.setWeekNotmainRateBad(weekNotMainRateBad);
				log.info("Week not main rate ok is: " + weekNotMainRateOk);
				log.info("Week not main rate normal is: "
						+ weekNotMainRateNormal);
				log.info("Week not main rate bad is: " + weekNotMainRateBad);
			}
		}

		/** month review data: 最近一月 */
		if (creditEls.size() > 1) {
			Elements monthRateEls = creditEls.get(1).select("table tbody tr");
			if (monthRateEls.size() > 1) {
				Element monthSumEl = monthRateEls.get(1);
				String monthSumRateOk = monthSumEl.select("td.rateok").text();
				String monthSumRateNormal = monthSumEl.select("td.ratenormal")
						.text();
				String monthSumRateBad = monthSumEl.select("td.ratebad").text();
				shopRateInfo.setMonthSumRateBad(monthSumRateBad);
				shopRateInfo.setMonthSumRateNormal(monthSumRateNormal);
				shopRateInfo.setMonthSumRateOk(monthSumRateOk);
				log.info("Month sum rate ok is: " + monthSumRateOk);
				log.info("Month sum rate normal is: " + monthSumRateNormal);
				log.info("Month sum rate bad is: " + monthSumRateBad);
			}
			if (monthRateEls.size() > 2) {
				Elements monthMainEl = monthRateEls.get(2).select("td");
				String monthMainRateOk = monthMainEl.get(1).text();
				String monthMainRateNormal = monthMainEl.get(2).text();
				String monthMainRateBad = monthMainEl.get(3).text();
				shopRateInfo.setMonthMainRateBad(monthMainRateBad);
				shopRateInfo.setMonthMainRateNormal(monthMainRateNormal);
				shopRateInfo.setMonthMainRateOk(monthMainRateOk);
				log.info("Month main rate ok is: " + monthMainRateOk);
				log.info("Month main rate normal is: " + monthMainRateNormal);
				log.info("Month main rate bad is: " + monthMainRateBad);
			}

			if (monthRateEls.size() > 3) {
				Elements monthNotMainEl = monthRateEls.get(3).select("td");
				String monthNotMainRateOk = monthNotMainEl.get(1).text();
				String monthNotMainRateNormal = monthNotMainEl.get(2).text();
				String monthNotMainRateBad = monthNotMainEl.get(3).text();
				shopRateInfo.setMonthNotmainRateBad(monthNotMainRateBad);
				shopRateInfo.setMonthNotmainRateNormal(monthNotMainRateNormal);
				shopRateInfo.setMonthNotmainRateOk(monthNotMainRateOk);
				log.info("Month not main rate ok: " + monthNotMainRateOk);
				log.info("Month not main rate normal: "
						+ monthNotMainRateNormal);
				log.info("Month not main rate bad: " + monthNotMainRateBad);
			}
		}
		/** half year data: 最近半年 */
		if (creditEls.size() > 2) {
			Elements halfYearRateEls = creditEls.get(2)
					.select("table tbody tr");
			if (halfYearRateEls.size() > 1) {
				Element halfYearSumEl = halfYearRateEls.get(1);
				String halfYearSumRateOk = halfYearSumEl.select("td.rateok")
						.text();
				String halfYearSumRateNormal = halfYearSumEl.select(
						"td.ratenormal").text();
				String halfYearSumRateBad = halfYearSumEl.select("td.ratebad")
						.text();

				shopRateInfo.setHalfYearSumRateBad(halfYearSumRateBad);
				shopRateInfo.setHalfYearSumRateOk(halfYearSumRateOk);
				shopRateInfo.setHalfYearSumRateNormal(halfYearSumRateNormal);
				log.info("Half year sum rate ok: " + halfYearSumRateOk);
				log.info("Half year sum rate normal: " + halfYearSumRateNormal);
				log.info("Half year sum rate bad: " + halfYearSumRateBad);
			}
			if (halfYearRateEls.size() > 2) {
				Elements halfYearMainEl = halfYearRateEls.get(2).select("td");
				String halfYearMainRateOk = halfYearMainEl.get(1).text();
				String halfYearMainRateNormal = halfYearMainEl.get(2).text();
				String halfYearMainRateBad = halfYearMainEl.get(3).text();
				shopRateInfo.setHalfYearMainRateBad(halfYearMainRateBad);
				shopRateInfo.setHalfYearMainRateNormal(halfYearMainRateNormal);
				shopRateInfo.setHalfYearMainRateOk(halfYearMainRateOk);
				log.info("Half year main rate ok: " + halfYearMainRateOk);
				log.info("Half year main rate normal: "
						+ halfYearMainRateNormal);
				log.info("Half year main rate bad: " + halfYearMainRateBad);
			}

			if (halfYearRateEls.size() > 3) {
				Elements halfYearNotMainEl = halfYearRateEls.get(3)
						.select("td");
				String halfYearNotMainRateOk = halfYearNotMainEl.get(1).text();
				String halfYearNotMainRateNormal = halfYearNotMainEl.get(2)
						.text();
				String halfYearNotMainRateBad = halfYearNotMainEl.get(3).text();
				shopRateInfo.setHalfYearNotmainRateBad(halfYearNotMainRateBad);
				shopRateInfo
						.setHalfYearNotmainRateNormal(halfYearNotMainRateNormal);
				shopRateInfo.setHalfYearNotmainRateOk(halfYearNotMainRateOk);
				log.info("Half year not main rate ok: " + halfYearNotMainRateOk);
				log.info("Half year not main rate normal: "
						+ halfYearNotMainRateNormal);
				log.info("Half year not main rate bad: "
						+ halfYearNotMainRateBad);
			}
		}

		/** before year data: 半年以前 */
		if (creditEls.size() > 3) {
			Elements beforeHalfYearRateEls = creditEls.get(3).select(
					"table tbody tr");
			Element beforeHalfYearSumEl = beforeHalfYearRateEls.get(1);
			String beforeHalfYearSumRateOk = beforeHalfYearSumEl.select(
					"td.rateok").text();
			String beforeHalfYearSumRateNormal = beforeHalfYearSumEl.select(
					"td.ratenormal").text();
			String beforeHalfYearSumRateBad = beforeHalfYearSumEl.select(
					"td.ratebad").text();
			shopRateInfo.setBeforeHalfYearSumRateBad(beforeHalfYearSumRateBad);
			shopRateInfo
					.setBeforeHalfYearSumRateNormal(beforeHalfYearSumRateNormal);
			shopRateInfo.setBeforeHalfYearSumRateOk(beforeHalfYearSumRateOk);
			log.info("Before half year sum rate ok: " + beforeHalfYearSumRateOk);
			log.info("Before half year sum rate normal: "
					+ beforeHalfYearSumRateNormal);
			log.info("Before half year sum rate bad: "
					+ beforeHalfYearSumRateBad);
		}

		Elements sellerHistoryEls = doc
				.select("div.seller-rate-info div.frame div.list");

		if (sellerHistoryEls.size() > 2) {
			String mainBusiness = sellerHistoryEls.get(1).ownText();
			mainBusiness = mainBusiness
					.substring(mainBusiness.indexOf("：") + 1);
			String mainBusinessPercentage = sellerHistoryEls.get(2).ownText();
			mainBusinessPercentage = mainBusinessPercentage
					.substring(mainBusinessPercentage.indexOf("：") + 1);
			int blankIndex = mainBusiness.indexOf(160);
			if (-1 != blankIndex) {
				mainBusiness = mainBusiness.substring(0, blankIndex);
			}
			shopRateInfo.setMainBusiness(mainBusiness);
			shopRateInfo.setMainBusinessPercentage(mainBusinessPercentage);
			log.info("Main biz is: " + mainBusiness);
			log.info("Main biz percentage is: " + mainBusinessPercentage);

		}
	}

	public void tmallPageParse() {
		/** shop Id */
		// Element shopIdEl = doc.getElementById("J_ShopIdHidden");
		// if (null != shopIdEl) {
		// String shopId = shopIdEl.attr("value");
		// storeInfo.setShopId(shopId);
		//
		// log.info("shop id: " + shopId);
		// }
		/** 卖家信息 */
		Elements sellerInfoEls = doc
				.select("div.personal-info div.left-box div.bd");
		if (sellerInfoEls.size() > 0) {
			Element sellerInfoEl = sellerInfoEls.get(0);

			/** seller name:卖家姓名 */
			Elements nameEls = sellerInfoEl.select("div.title > a");
			if (nameEls.size() > 0) {
				String sellerName = nameEls.get(0).ownText();
				shopRateInfo.setSellerName(sellerName);
				log.info("seller name: " + sellerName);
			}

			/** main sale:当前主营 */
			Elements saleEls = sellerInfoEl.select("ul:not(.company) > li > a");
			if (saleEls.size() > 0) {
				String mainSale = saleEls.get(0).ownText();
				char spa = (char) 160;
				mainSale = mainSale.replace(spa, ' ');
				shopRateInfo.setMainSale(mainSale.trim());
				log.info("main sale: " + mainSale);
			}

			/** location:所在地区 */
			Elements locEls = sellerInfoEl.select("ul li");
			if (locEls.size() > 2) {
				String location = locEls.get(2).ownText();
				location = location.substring(location.indexOf("：") + 1).trim();
				location = location.replace((char) 160, ' ');
				shopRateInfo.setLocation(location.trim());
				log.info("seller location info: " + location);
			}

			/** create time: 创店时间 */
			Element createShopEl = doc.getElementById("J_showShopStartDate");
			if (null != createShopEl) {
				String createDate = createShopEl.val();
				shopRateInfo.setCreateShopDate(createDate);
				log.info("create shop time: " + createDate);
			}
		}

		/** store service: 店铺服务 */
		String serviceType = "";
		Elements storeDescEls = doc.select("div.promise a");
		for (Element el : storeDescEls) {
			serviceType += el.attr("title") + ",";
		}
		if (storeDescEls.size() > 0) {
			serviceType = serviceType
					.substring(0, serviceType.lastIndexOf(","));
		} else {
			log.error("There is no div.promise in the page.");
			serviceType = "-1";
		}

		shopRateInfo.setServiceType(serviceType);
		log.info("service type: " + serviceType);

		/** charge num: 保证金 */
		Elements chargeEls = doc.select("div.charge span");
		if (chargeEls.size() > 0) {
			String chargeNum = chargeEls.get(0).ownText().trim();
			shopRateInfo.setChargeNum(chargeNum);
			log.info("store charge num is:" + chargeNum);
		}

		/** store half year score: 店铺半年内动态评分 */
		Elements dynamicRateEls = doc
				.select("div#sixmonth ul li div.item-scrib em.count");
		if (dynamicRateEls.size() > 0) {
			Element matchEl = dynamicRateEls.get(0);
			String matchScore = matchEl.ownText();
			shopRateInfo.setMatchScore(matchScore);
			log.info("match rate score: " + matchScore);

			Element matchPaEl = matchEl.parent();
			if (matchPaEl.select("strong.over").size() > 0) {
				shopRateInfo.setMatchCompToAvg("高");
			} else if (matchPaEl.select("strong.lower").size() > 0) {
				shopRateInfo.setMatchCompToAvg("低");
			} else {
				shopRateInfo.setMatchCompToAvg("平");
			}
		}
		if (dynamicRateEls.size() > 1) {
			Element serviceEl = dynamicRateEls.get(1);
			String serviceScore = serviceEl.ownText();
			shopRateInfo.setServiceScore(serviceScore);
			log.info("service rate score: " + serviceScore);

			Element servicePaEl = serviceEl.parent();
			if (servicePaEl.select("strong.over").size() > 0) {
				shopRateInfo.setServiceCompToAvg("高");
			} else if (servicePaEl.select("strong.lower").size() > 0) {
				shopRateInfo.setServiceCompToAvg("低");
			} else {
				shopRateInfo.setServiceCompToAvg("平");
			}
		}
		if (dynamicRateEls.size() > 2) {
			Element consignEl = dynamicRateEls.get(2);
			String consignmentScore = consignEl.ownText();
			shopRateInfo.setConsignmentScore(consignmentScore);
			log.info("consignment rate score: " + consignmentScore);

			Element consignPaEl = consignEl.parent();
			if (consignPaEl.select("strong.over").size() > 0) {
				shopRateInfo.setConsignCompToAvg("高");
			} else if (consignPaEl.select("strong.lower").size() > 0) {
				shopRateInfo.setConsignCompToAvg("低");
			} else {
				shopRateInfo.setConsignCompToAvg("平");
			}
		}

		/** month service: 店铺30天内服务情况 */
		monthService.init();
		monthService.setDoc(doc);
		monthService.execute();
		List<MonthServiceEntity> serviceEntities = monthService
				.getMonthServieEntities();
		shopRateInfo.setRefundmentScore(serviceEntities.get(0).getNativeValue());
		shopRateInfo.setAvgOfRefund(serviceEntities.get(0).getAvgValue());
		shopRateInfo.setRefundmentRateScore(serviceEntities.get(1)
				.getNativeValue());
		shopRateInfo.setAvgOfRefundRate(serviceEntities.get(1).getAvgValue());
		shopRateInfo.setComplaintScore(serviceEntities.get(2).getNativeValue());
		shopRateInfo.setAvgOfComplaint(serviceEntities.get(2).getAvgValue());
		shopRateInfo.setPunishmentScore(serviceEntities.get(3).getNativeValue());
		shopRateInfo.setAvgOfPunish(serviceEntities.get(3).getAvgValue());

	}

	/* write the data to the access database */
	public static void writeDataToDB(List<ShopRate> list) {
		try {
			String result[] = ShopRate.getCreateSQLStr();
			String tableName = result[0];
			String createTableStr = result[1];

			DBManager.connectDB();
			if (!DBManager.isTableExist(tableName)) {
				log.info("create table " + tableName);
				DBManager.createTable(createTableStr);
				log.info("create table success!");
			}
			for (int i = 0; i < list.size(); i++) {
				ShopRate data = list.get(i);
				DBManager.insertData(data, tableName);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("ShopRatePageParse Error: writeDataToDB exception");
			log.error("Exception: ", e);
		} finally{
			DBManager.closeDB();
		}
	}
}
