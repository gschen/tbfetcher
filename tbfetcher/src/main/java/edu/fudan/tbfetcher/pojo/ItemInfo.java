package edu.fudan.tbfetcher.pojo;

import edu.fudan.tbfetcher.constant.SystemConstant;

public class ItemInfo {
	private boolean isTmall; // 是否天猫商城的产品
	private String itemId; // 产品ID
	private String itemName; // 产品名
	private String priceRange; // 价格
	private String freightPrice; // 运费
	private int saleNumIn30Days; // 月销量
	private int totalCommentNum; // 评论数
	private int viewCounter; // not Tmall 浏览数
	private int numInStock; // 库存
	private int numOfLike; // not Tmall 喜欢
	private int numOfShare; // not Tmall 分享
	private int numOfCollectItem; // not Tmall 收藏商品数
	private int numOfCollectShop; // not Tmall 收藏店铺数
	private String payType = "-1"; // 支付类型
	private String serviceType = "-1"; // 服务类型
	private String spec = "-1"; // 商品描述
	private String capacity = "-1"; // 容量
	private String latestCommentDate = "-1"; // 第一条评价时间
	private String yearOfLatest;     //年
	private String monthOfLatest;    //月
	private String dayOfLatest;      //日
	private String timeOfLatest;    //时间
	private String oldestCommentDate = "-1"; // 最后一条评价时间
	private String yearOfOldest;    //年
	private String monthOfOldest;    //月
	private String dayOfOldest;    //日
	private String timeOfOldest;    //时间
	private String shopRateUrl = "-1"; // 店铺动态评分页面url,如果宝贝不属于任何的店铺则店铺信用url默认为-1
	private String itemDetailUrl; // 商品详细信息页面，也就是本页面
	private String itemType; // not Tmall 商品类型：全新等
	private String itemCommentBaseUrl; // 评论页面的base URL
	private String impress; // 买家印象
	private String shopId = "-1"; // 店铺的Id, shopId默认为"-1",有些淘宝item是没有所属店铺的
	private String shopUrl; // 店铺的url地址
	private String fetchTime;
	private int rateNormalCount;// 宝贝评论中评数量
	private int rateBadCount;// 宝贝评论差评数量
	private int additionalCount;// 宝贝评论追加数量
	private int rateGoodCount; //宝贝好评的数量

	public boolean isTmall() {
		return isTmall;
	}
	public void setTmall(boolean isTmall) {
		this.isTmall = isTmall;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getPriceRange() {
		return priceRange;
	}
	public void setPriceRange(String priceRange) {
		this.priceRange = priceRange;
	}
	public String getFreightPrice() {
		return freightPrice;
	}
	public void setFreightPrice(String freightPrice) {
		this.freightPrice = freightPrice;
	}
	public int getSaleNumIn30Days() {
		return saleNumIn30Days;
	}
	public void setSaleNumIn30Days(int saleNumIn30Days) {
		this.saleNumIn30Days = saleNumIn30Days;
	}
	public int getTotalCommentNum() {
		return totalCommentNum;
	}
	public void setTotalCommentNum(int totalCommentNum) {
		this.totalCommentNum = totalCommentNum;
	}
	public int getViewCounter() {
		return viewCounter;
	}
	public void setViewCounter(int viewCounter) {
		this.viewCounter = viewCounter;
	}
	public int getNumInStock() {
		return numInStock;
	}
	public void setNumInStock(int numInStock) {
		this.numInStock = numInStock;
	}
	public int getNumOfLike() {
		return numOfLike;
	}
	public void setNumOfLike(int numOfLike) {
		this.numOfLike = numOfLike;
	}
	public int getNumOfShare() {
		return numOfShare;
	}
	public void setNumOfShare(int numOfShare) {
		this.numOfShare = numOfShare;
	}
	public int getNumOfCollectItem() {
		return numOfCollectItem;
	}
	public void setNumOfCollectItem(int numOfCollectItem) {
		this.numOfCollectItem = numOfCollectItem;
	}
	public int getNumOfCollectShop() {
		return numOfCollectShop;
	}
	public void setNumOfCollectShop(int numOfCollectShop) {
		this.numOfCollectShop = numOfCollectShop;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getCapacity() {
		return capacity;
	}
	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}
	public String getLatestCommentDate() {
		return latestCommentDate;
	}
	public void setLatestCommentDate(String latestCommentDate) {
		this.latestCommentDate = latestCommentDate;
	}
	public String getMonthOfLatest() {
		return monthOfLatest;
	}
	public void setMonthOfLatest(String monthOfLatest) {
		this.monthOfLatest = monthOfLatest;
	}
	public String getDayOfLatest() {
		return dayOfLatest;
	}
	public void setDayOfLatest(String dayOfLatest) {
		this.dayOfLatest = dayOfLatest;
	}
	public String getDayOfOldest() {
		return dayOfOldest;
	}
	public void setDayOfOldest(String dayOfOldest) {
		this.dayOfOldest = dayOfOldest;
	}
	public String getTimeOfLatest() {
		return timeOfLatest;
	}
	public void setTimeOfLatest(String timeOfLatest) {
		this.timeOfLatest = timeOfLatest;
	}
	public String getOldestCommentDate() {
		return oldestCommentDate;
	}
	public void setOldestCommentDate(String oldestCommentDate) {
		this.oldestCommentDate = oldestCommentDate;
	}
	public String getYearOfLatest() {
		return yearOfLatest;
	}
	public void setYearOfLatest(String yearOfLatest) {
		this.yearOfLatest = yearOfLatest;
	}
	public String getYearOfOldest() {
		return yearOfOldest;
	}
	public void setYearOfOldest(String yearOfOldest) {
		this.yearOfOldest = yearOfOldest;
	}
	public String getMonthOfOldest() {
		return monthOfOldest;
	}
	public void setMonthOfOldest(String monthOfOldest) {
		this.monthOfOldest = monthOfOldest;
	}
	public String getTimeOfOldest() {
		return timeOfOldest;
	}
	public void setTimeOfOldest(String timeOfOldest) {
		this.timeOfOldest = timeOfOldest;
	}
	public String getShopRateUrl() {
		return shopRateUrl;
	}
	public void setShopRateUrl(String shopRateUrl) {
		this.shopRateUrl = shopRateUrl;
	}
	public String getItemDetailUrl() {
		return itemDetailUrl;
	}
	public void setItemDetailUrl(String itemDetailUrl) {
		this.itemDetailUrl = itemDetailUrl;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getItemCommentBaseUrl() {
		return itemCommentBaseUrl;
	}
	public void setItemCommentBaseUrl(String itemCommentBaseUrl) {
		this.itemCommentBaseUrl = itemCommentBaseUrl;
	}
	public String getImpress() {
		return impress;
	}
	public void setImpress(String impress) {
		this.impress = impress;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getShopUrl() {
		return shopUrl;
	}
	public void setShopUrl(String shopUrl) {
		this.shopUrl = shopUrl;
	}
	public String getFetchTime() {
		return fetchTime;
	}
	public void setFetchTime(String fetchTime) {
		this.fetchTime = fetchTime;
	}
	public int getRateNormalCount() {
		return rateNormalCount;
	}
	public void setRateNormalCount(int rateNormalCount) {
		this.rateNormalCount = rateNormalCount;
	}
	public int getRateGoodCount() {
		return rateGoodCount;
	}
	public void setRateGoodCount(int rateGoodCount) {
		this.rateGoodCount = rateGoodCount;
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


	public static String[] getCreateSQLStr() {
		StringBuffer createSqlStr = new StringBuffer("CREATE TABLE " + SystemConstant.ITEM_DETAIL_TABLE + "(");

		createSqlStr.append("id counter primary key, ");
		createSqlStr.append("isTmall char(1), ");
		createSqlStr.append("itemId varchar(20), ");
		createSqlStr.append("shopId varchar(20), ");
		createSqlStr.append("shopUrl varchar(100), ");
		createSqlStr.append("itemName varchar(100), ");
		createSqlStr.append("impress varchar(100), ");
		createSqlStr.append("priceRange varchar(20), ");
		createSqlStr.append("freightPrice varchar(20), ");
		createSqlStr.append("saleNumIn30Days integer, ");
		createSqlStr.append("totalCommentNum integer, ");
		createSqlStr.append("viewCounter integer, ");
		createSqlStr.append("numInStock integer, ");
		createSqlStr.append("numOfLike integer, ");
		createSqlStr.append("numOfShare integer, ");
		createSqlStr.append("numOfCollectItem integer, ");
		createSqlStr.append("numOfCollectShop integer, ");
		createSqlStr.append("payType varchar(10), ");
		createSqlStr.append("serviceType varchar(20), ");
		createSqlStr.append("spec varchar(200), ");
		createSqlStr.append("capacity varchar(20), ");
		createSqlStr.append("oldestCommentDate varchar(20), ");
		createSqlStr.append("yearOfOldest varchar(5), ");
		createSqlStr.append("monthOfOldest varchar(5), ");
		createSqlStr.append("dayOfOldest varchar(5), ");
		createSqlStr.append("timeOfOldest varchar(10), ");
		createSqlStr.append("latestCommentDate varchar(20), ");
		createSqlStr.append("yearOfLatest varchar(5), ");
		createSqlStr.append("monthOfLatest varchar(5), ");
		createSqlStr.append("dayOfLatest varchar(5), ");
		createSqlStr.append("timeOfLatest varchar(10), ");
		createSqlStr.append("shopRateUrl varchar(200), ");
		createSqlStr.append("itemDetailUrl varchar(200), ");
		createSqlStr.append("itemType varchar(20), ");
		createSqlStr.append("itemCommentBaseUrl varchar(200), ");
		createSqlStr.append("fetchTime varchar(50), ");
		createSqlStr.append("rateGoodCount integer, ");
		createSqlStr.append("rateNormalCount integer, ");
		createSqlStr.append("rateBadCount integer, ");
		createSqlStr.append("additionalCount integer)");

		return new String[] { SystemConstant.ITEM_DETAIL_TABLE,
				createSqlStr.toString() };
	}
}
