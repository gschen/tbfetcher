package edu.fudan.tbfetcher.pojo;

public class ShopRate {
	public static final String TABLE_NAME = "ShopRateTable";

	private boolean isTmall;
	private String shopId;
	private String sellerRateHref;
	private String sellerName;
	private String mainSale;
	private String location;
	private String createShopDate;
	private String sellerRate; // not tmall 卖家信用
	private String buyerRate; // not tmall 买家信用
	private String chargeNum; // 商铺当前保证金金额
	private String serviceType; // 商铺提供的各项服务
	private String matchScore;
	private String serviceScore;
	private String consignmentScore;
	private String matchCompToAvg; // 与平均相比高还是低
	private String serviceCompToAvg; // 与平均相比高还是低
	private String consignCompToAvg; // 与平均相比高还是低
	private String refundmentScore;
	private String refundmentRateScore;
	private String complaintScore;
	private String punishmentScore;
	private String avgOfRefund; // 平均
	private String avgOfRefundRate; // 平均
	private String avgOfComplaint; // 平均
	private String avgOfPunish; // 平均
	private String sellerGoodRate;

	private String weekSumRateOk;
	private String weekMainRateOk;
	private String weekNotmainRateOk;
	private String weekSumRateNormal;
	private String weekMainRateNormal;
	private String weekNotmainRateNormal;
	private String weekSumRateBad;
	private String weekNotmainRateBad;

	private String monthSumRateOk;
	private String monthMainRateOk;
	private String monthNotmainRateOk;
	private String monthSumRateNormal;
	private String monthMainRateNormal;
	private String weekMainRateBad;
	private String monthNotmainRateNormal;
	private String monthSumRateBad;
	private String monthMainRateBad;
	private String monthNotmainRateBad;

	private String halfYearSumRateOk;
	private String halfYearMainRateOk;
	private String halfYearNotmainRateOk;
	private String halfYearSumRateNormal;
	private String halfYearMainRateNormal;
	private String halfYearNotmainRateNormal;
	private String halfYearSumRateBad;
	private String halfYearMainRateBad;
	private String halfYearNotmainRateBad;

	private String beforeHalfYearSumRateOk;
	private String beforeHalfYearSumRateNormal;
	private String beforeHalfYearSumRateBad;

	private String mainBusiness;
	private String mainBusinessPercentage;

	private String fetchTime;

	public String getFetchTime() {
		return fetchTime;
	}

	public void setFetchTime(String fetchTime) {
		this.fetchTime = fetchTime;
	}

	public boolean isTmall() {
		return isTmall;
	}

	public void setTmall(boolean isTmall) {
		this.isTmall = isTmall;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getSellerRateHref() {
		return sellerRateHref;
	}

	public void setSellerRateHref(String sellerRateHref) {
		this.sellerRateHref = sellerRateHref;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public String getMainSale() {
		return mainSale;
	}

	public void setMainSale(String mainSale) {
		this.mainSale = mainSale;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCreateShopDate() {
		return createShopDate;
	}

	public void setCreateShopDate(String createShopDate) {
		this.createShopDate = createShopDate;
	}

	public String getBuyerRate() {
		return buyerRate;
	}

	public void setBuyerRate(String buyerRate) {
		this.buyerRate = buyerRate;
	}

	public String getChargeNum() {
		return chargeNum;
	}

	public void setChargeNum(String chargeNum) {
		this.chargeNum = chargeNum;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getMatchScore() {
		return matchScore;
	}

	public void setMatchScore(String matchScore) {
		this.matchScore = matchScore;
	}

	public String getServiceScore() {
		return serviceScore;
	}

	public void setServiceScore(String serviceScore) {
		this.serviceScore = serviceScore;
	}

	public String getConsignmentScore() {
		return consignmentScore;
	}

	public void setConsignmentScore(String consignmentScore) {
		this.consignmentScore = consignmentScore;
	}

	public String getRefundmentScore() {
		return refundmentScore;
	}

	public void setRefundmentScore(String refundmentScore) {
		this.refundmentScore = refundmentScore;
	}

	public String getRefundmentRateScore() {
		return refundmentRateScore;
	}

	public void setRefundmentRateScore(String refundmentRateScore) {
		this.refundmentRateScore = refundmentRateScore;
	}

	public String getComplaintScore() {
		return complaintScore;
	}

	public void setComplaintScore(String complaintScore) {
		this.complaintScore = complaintScore;
	}

	public String getPunishmentScore() {
		return punishmentScore;
	}

	public void setPunishmentScore(String punishmentScore) {
		this.punishmentScore = punishmentScore;
	}

	public String getSellerGoodRate() {
		return sellerGoodRate;
	}

	public void setSellerGoodRate(String sellerGoodRate) {
		this.sellerGoodRate = sellerGoodRate;
	}

	public String getWeekSumRateOk() {
		return weekSumRateOk;
	}

	public void setWeekSumRateOk(String weekSumRateOk) {
		this.weekSumRateOk = weekSumRateOk;
	}

	public String getWeekMainRateOk() {
		return weekMainRateOk;
	}

	public void setWeekMainRateOk(String weekMainRateOk) {
		this.weekMainRateOk = weekMainRateOk;
	}

	public String getWeekNotmainRateOk() {
		return weekNotmainRateOk;
	}

	public void setWeekNotmainRateOk(String weekNotmainRateOk) {
		this.weekNotmainRateOk = weekNotmainRateOk;
	}

	public String getWeekSumRateNormal() {
		return weekSumRateNormal;
	}

	public void setWeekSumRateNormal(String weekSumRateNormal) {
		this.weekSumRateNormal = weekSumRateNormal;
	}

	public String getWeekMainRateNormal() {
		return weekMainRateNormal;
	}

	public void setWeekMainRateNormal(String weekMainRateNormal) {
		this.weekMainRateNormal = weekMainRateNormal;
	}

	public String getWeekNotmainRateNormal() {
		return weekNotmainRateNormal;
	}

	public void setWeekNotmainRateNormal(String weekNotmainRateNormal) {
		this.weekNotmainRateNormal = weekNotmainRateNormal;
	}

	public String getWeekSumRateBad() {
		return weekSumRateBad;
	}

	public void setWeekSumRateBad(String weekSumRateBad) {
		this.weekSumRateBad = weekSumRateBad;
	}

	public String getWeekMainRateBad() {
		return weekMainRateBad;
	}

	public void setWeekMainRateBad(String weekMainRateBad) {
		this.weekMainRateBad = weekMainRateBad;
	}

	public String getWeekNotmainRateBad() {
		return weekNotmainRateBad;
	}

	public void setWeekNotmainRateBad(String weekNotmainRateBad) {
		this.weekNotmainRateBad = weekNotmainRateBad;
	}

	public String getMonthSumRateOk() {
		return monthSumRateOk;
	}

	public void setMonthSumRateOk(String monthSumRateOk) {
		this.monthSumRateOk = monthSumRateOk;
	}

	public String getMonthMainRateOk() {
		return monthMainRateOk;
	}

	public void setMonthMainRateOk(String monthMainRateOk) {
		this.monthMainRateOk = monthMainRateOk;
	}

	public String getMonthNotmainRateOk() {
		return monthNotmainRateOk;
	}

	public void setMonthNotmainRateOk(String monthNotmainRateOk) {
		this.monthNotmainRateOk = monthNotmainRateOk;
	}

	public String getMonthSumRateNormal() {
		return monthSumRateNormal;
	}

	public void setMonthSumRateNormal(String monthSumRateNormal) {
		this.monthSumRateNormal = monthSumRateNormal;
	}

	public String getMonthMainRateNormal() {
		return monthMainRateNormal;
	}

	public void setMonthMainRateNormal(String monthMainRateNormal) {
		this.monthMainRateNormal = monthMainRateNormal;
	}

	public String getMonthNotmainRateNormal() {
		return monthNotmainRateNormal;
	}

	public void setMonthNotmainRateNormal(String monthNotmainRateNormal) {
		this.monthNotmainRateNormal = monthNotmainRateNormal;
	}

	public String getMonthSumRateBad() {
		return monthSumRateBad;
	}

	public void setMonthSumRateBad(String monthSumRateBad) {
		this.monthSumRateBad = monthSumRateBad;
	}

	public String getMonthMainRateBad() {
		return monthMainRateBad;
	}

	public void setMonthMainRateBad(String monthMainRateBad) {
		this.monthMainRateBad = monthMainRateBad;
	}

	public String getMonthNotmainRateBad() {
		return monthNotmainRateBad;
	}

	public void setMonthNotmainRateBad(String monthNotmainRateBad) {
		this.monthNotmainRateBad = monthNotmainRateBad;
	}

	public String getHalfYearSumRateOk() {
		return halfYearSumRateOk;
	}

	public void setHalfYearSumRateOk(String halfYearSumRateOk) {
		this.halfYearSumRateOk = halfYearSumRateOk;
	}

	public String getHalfYearMainRateOk() {
		return halfYearMainRateOk;
	}

	public void setHalfYearMainRateOk(String halfYearMainRateOk) {
		this.halfYearMainRateOk = halfYearMainRateOk;
	}

	public String getHalfYearNotmainRateOk() {
		return halfYearNotmainRateOk;
	}

	public void setHalfYearNotmainRateOk(String halfYearNotmainRateOk) {
		this.halfYearNotmainRateOk = halfYearNotmainRateOk;
	}

	public String getHalfYearSumRateNormal() {
		return halfYearSumRateNormal;
	}

	public void setHalfYearSumRateNormal(String halfYearSumRateNormal) {
		this.halfYearSumRateNormal = halfYearSumRateNormal;
	}

	public String getHalfYearMainRateNormal() {
		return halfYearMainRateNormal;
	}

	public void setHalfYearMainRateNormal(String halfYearMainRateNormal) {
		this.halfYearMainRateNormal = halfYearMainRateNormal;
	}

	public String getHalfYearNotmainRateNormal() {
		return halfYearNotmainRateNormal;
	}

	public void setHalfYearNotmainRateNormal(String halfYearNotmainRateNormal) {
		this.halfYearNotmainRateNormal = halfYearNotmainRateNormal;
	}

	public String getHalfYearSumRateBad() {
		return halfYearSumRateBad;
	}

	public void setHalfYearSumRateBad(String halfYearSumRateBad) {
		this.halfYearSumRateBad = halfYearSumRateBad;
	}

	public String getHalfYearMainRateBad() {
		return halfYearMainRateBad;
	}

	public void setHalfYearMainRateBad(String halfYearMainRateBad) {
		this.halfYearMainRateBad = halfYearMainRateBad;
	}

	public String getHalfYearNotmainRateBad() {
		return halfYearNotmainRateBad;
	}

	public void setHalfYearNotmainRateBad(String halfYearNotmainRateBad) {
		this.halfYearNotmainRateBad = halfYearNotmainRateBad;
	}

	public String getBeforeHalfYearSumRateOk() {
		return beforeHalfYearSumRateOk;
	}

	public void setBeforeHalfYearSumRateOk(String beforeHalfYearSumRateOk) {
		this.beforeHalfYearSumRateOk = beforeHalfYearSumRateOk;
	}

	public String getBeforeHalfYearSumRateNormal() {
		return beforeHalfYearSumRateNormal;
	}

	public void setBeforeHalfYearSumRateNormal(
			String beforeHalfYearSumRateNormal) {
		this.beforeHalfYearSumRateNormal = beforeHalfYearSumRateNormal;
	}

	public String getBeforeHalfYearSumRateBad() {
		return beforeHalfYearSumRateBad;
	}

	public void setBeforeHalfYearSumRateBad(String beforeHalfYearSumRateBad) {
		this.beforeHalfYearSumRateBad = beforeHalfYearSumRateBad;
	}

	public String getSellerRate() {
		return sellerRate;
	}

	public void setSellerRate(String sellerRate) {
		this.sellerRate = sellerRate;
	}

	public String getMainBusiness() {
		return mainBusiness;
	}

	public void setMainBusiness(String mainBusiness) {
		this.mainBusiness = mainBusiness;
	}

	public String getMainBusinessPercentage() {
		return mainBusinessPercentage;
	}

	public void setMainBusinessPercentage(String mainBusinessPercentage) {
		this.mainBusinessPercentage = mainBusinessPercentage;
	}

	public String getMatchCompToAvg() {
		return matchCompToAvg;
	}

	public void setMatchCompToAvg(String matchCompToAvg) {
		this.matchCompToAvg = matchCompToAvg;
	}

	public String getServiceCompToAvg() {
		return serviceCompToAvg;
	}

	public void setServiceCompToAvg(String serviceCompToAvg) {
		this.serviceCompToAvg = serviceCompToAvg;
	}

	public String getConsignCompToAvg() {
		return consignCompToAvg;
	}

	public void setConsignCompToAvg(String consignCompToAvg) {
		this.consignCompToAvg = consignCompToAvg;
	}

	public String getAvgOfRefund() {
		return avgOfRefund;
	}

	public void setAvgOfRefund(String avgOfRefund) {
		this.avgOfRefund = avgOfRefund;
	}

	public String getAvgOfRefundRate() {
		return avgOfRefundRate;
	}

	public void setAvgOfRefundRate(String avgOfRefundRate) {
		this.avgOfRefundRate = avgOfRefundRate;
	}

	public String getAvgOfComplaint() {
		return avgOfComplaint;
	}

	public void setAvgOfComplaint(String avgOfComplaint) {
		this.avgOfComplaint = avgOfComplaint;
	}

	public String getAvgOfPunish() {
		return avgOfPunish;
	}

	public void setAvgOfPunish(String avgOfPunish) {
		this.avgOfPunish = avgOfPunish;
	}

	public static String[] getCreateSQLStr() {
		StringBuffer createSqlStr = new StringBuffer("CREATE TABLE "
				+ TABLE_NAME + "(");

		createSqlStr.append("id counter primary key, ");
		createSqlStr.append("isTmall char(1), ");
		createSqlStr.append("shopId varchar(20), ");
		createSqlStr.append("sellerRateHref varchar(100), ");
		createSqlStr.append("sellerName varchar(10), ");
		createSqlStr.append("mainSale varchar(10), ");
		createSqlStr.append("location varchar(10), ");
		createSqlStr.append("createShopDate varchar(10), ");
		createSqlStr.append("sellerRate varchar(10), ");
		createSqlStr.append("buyerRate varchar(10), ");
		createSqlStr.append("chargeNum varchar(15), ");
		createSqlStr.append("serviceType varchar(30), ");
		createSqlStr.append("matchScore varchar(5), ");
		createSqlStr.append("serviceScore varchar(5), ");
		createSqlStr.append("consignmentScore varchar(5), ");
		createSqlStr.append("matchCompToAvg char(1), ");
		createSqlStr.append("serviceCompToAvg char(1), ");
		createSqlStr.append("consignCompToAvg char(1), ");
		createSqlStr.append("refundmentScore varchar(5), ");
		createSqlStr.append("refundmentRateScore varchar(5), ");
		createSqlStr.append("complaintScore varchar(5), ");
		createSqlStr.append("punishmentScore varchar(5), ");
		createSqlStr.append("avgOfRefund varchar(5), ");
		createSqlStr.append("avgOfRefundRate varchar(5), ");
		createSqlStr.append("avgOfComplaint varchar(5), ");
		createSqlStr.append("avgOfPunish varchar(5), ");
		createSqlStr.append("sellerGoodRate varchar(10), ");
		createSqlStr.append("weekSumRateOk varchar(10), ");
		createSqlStr.append("weekSumRateNormal varchar(10), ");
		createSqlStr.append("weekSumRateBad varchar(10), ");
		createSqlStr.append("weekMainRateOk varchar(10), ");
		createSqlStr.append("weekMainRateNormal varchar(10), ");
		createSqlStr.append("weekMainRateBad varchar(10), ");
		createSqlStr.append("weekNotMainRateOk varchar(10), ");
		createSqlStr.append("weekNotMainRateNormal varchar(10), ");
		createSqlStr.append("weekNotMainRateBad varchar(10), ");
		createSqlStr.append("monthSumRateOk varchar(10), ");
		createSqlStr.append("monthSumRateNormal varchar(10), ");
		createSqlStr.append("monthSumRateBad varchar(10), ");
		createSqlStr.append("monthMainRateOk varchar(10), ");
		createSqlStr.append("monthMainRateNormal varchar(10), ");
		createSqlStr.append("monthMainRateBad varchar(10), ");
		createSqlStr.append("monthNotMainRateOk varchar(10), ");
		createSqlStr.append("monthNotMainRateNormal varchar(10), ");
		createSqlStr.append("monthNotMainRateBad varchar(10), ");

		createSqlStr.append("halfYearSumRateOk varchar(10), ");
		createSqlStr.append("halfYearSumRateNormal varchar(10), ");
		createSqlStr.append("halfYearSumRateBad varchar(10), ");
		createSqlStr.append("halfYearMainRateOk varchar(10), ");
		createSqlStr.append("halfYearMainRateNormal varchar(10), ");
		createSqlStr.append("halfYearMainRateBad varchar(10), ");
		createSqlStr.append("halfYearNotMainRateOk varchar(10), ");
		createSqlStr.append("halfYearNotMainRateNormal varchar(10), ");
		createSqlStr.append("halfYearNotMainRateBad varchar(10), ");

		createSqlStr.append("beforeHalfYearSumRateOk varchar(10), ");
		createSqlStr.append("beforeHalfYearSumRateNormal varchar(10), ");
		createSqlStr.append("beforeHalfYearSumRateBad varchar(10), ");

		createSqlStr.append("mainBusiness varchar(10), ");
		createSqlStr.append("fetchTime varchar(50), ");
		createSqlStr.append("mainBusinessPercentage varchar(10))");

		return new String[] { TABLE_NAME, createSqlStr.toString() };
	}
}
