package edu.fudan.tbfetcher.pojo;

public class SellerInSearchResult {
	public static final String TABLE_NAME = "SearchResultTable";

	private String itemId;
	private String shopId;
	private String productName; // top rank的产品名
	private String itemName;
	private String itemUrl;
	private String categoryName;
	private String sellerName;
	private boolean isGlobalBuy;
	private boolean isGoldSeller;
	private double price;
	private double freightPrice;
	private boolean creditCardPay;
	private String sellerAddress;
	private int saleNum;
	private int reviews;
	private boolean isConsumerPromise;
	private boolean isSevenDayReturn;
	private boolean isQualityItem;
	private boolean is30DaysMaintain;
	private int page;
	private int rank;
	private boolean isLeaveACompensableThree;
	private boolean isTmall;
	private String fetchTime;
	private long userNumberId; // 搜索结果页面中，店铺卖家id

	public long getUserNumberId() {
		return userNumberId;
	}

	public void setUserNumberId(long userNumberId) {
		this.userNumberId = userNumberId;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getFetchTime() {
		return fetchTime;
	}

	public void setFetchTime(String fetchTime) {
		this.fetchTime = fetchTime;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

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

	public String getHref() {
		return itemUrl;
	}

	public void setHref(String href) {
		this.itemUrl = href;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public boolean isGlobalBuy() {
		return isGlobalBuy;
	}

	public void setGlobalBuy(boolean isGlobalBuy) {
		this.isGlobalBuy = isGlobalBuy;
	}

	public boolean isGoldSeller() {
		return isGoldSeller;
	}

	public void setGoldSeller(boolean isGoldSeller) {
		this.isGoldSeller = isGoldSeller;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getFreightPrice() {
		return freightPrice;
	}

	public void setFreightPrice(double freightPrice) {
		this.freightPrice = freightPrice;
	}

	public boolean isCreditCardPay() {
		return creditCardPay;
	}

	public void setCreditCardPay(boolean creditCardPay) {
		this.creditCardPay = creditCardPay;
	}

	public String getSellerAddress() {
		return sellerAddress;
	}

	public void setSellerAddress(String sellerAddress) {
		this.sellerAddress = sellerAddress;
	}

	public int getSaleNum() {
		return saleNum;
	}

	public void setSaleNum(int saleNum) {
		this.saleNum = saleNum;
	}

	public int getReviews() {
		return reviews;
	}

	public void setReviews(int reviews) {
		this.reviews = reviews;
	}

	public boolean isConsumerPromise() {
		return isConsumerPromise;
	}

	public void setConsumerPromise(boolean isConsumerPromise) {
		this.isConsumerPromise = isConsumerPromise;
	}

	public boolean isSevenDayReturn() {
		return isSevenDayReturn;
	}

	public void setSevenDayReturn(boolean isSevenDayReturn) {
		this.isSevenDayReturn = isSevenDayReturn;
	}

	public boolean isQualityItem() {
		return isQualityItem;
	}

	public void setQualityItem(boolean isQualityItem) {
		this.isQualityItem = isQualityItem;
	}

	public boolean isIs30DaysMaintain() {
		return is30DaysMaintain;
	}

	public void setIs30DaysMaintain(boolean is30DaysMaintain) {
		this.is30DaysMaintain = is30DaysMaintain;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public boolean isLeaveACompensableThree() {
		return isLeaveACompensableThree;
	}

	public void setLeaveACompensableThree(boolean isLeaveACompensableThree) {
		this.isLeaveACompensableThree = isLeaveACompensableThree;
	}

	public static String[] getCreateSQLStr() {
		StringBuffer createSqlStr;

		createSqlStr = new StringBuffer("CREATE TABLE " + TABLE_NAME + "(");
		createSqlStr.append("id counter primary key, ");
		createSqlStr.append("shopId varchar(20), ");
		createSqlStr.append("itemId varchar(20), ");
		createSqlStr.append("productName varchar(50), ");
		createSqlStr.append("itemName varchar(100), ");
		createSqlStr.append("itemUrl varchar(100), ");
		createSqlStr.append("categoryName varchar(10), ");
		createSqlStr.append("sellerName varchar(10), ");
		createSqlStr.append("isGlobalBuy char(1), ");
		createSqlStr.append("isGoldSeller char(1), ");
		createSqlStr.append("price double, ");
		createSqlStr.append("freightPrice double, ");
		createSqlStr.append("creditCardPay char(1), ");
		createSqlStr.append("sellerAddress varchar(100), ");
		createSqlStr.append("saleNum integer, ");
		createSqlStr.append("reviews integer, ");
		createSqlStr.append("isConsumerPromise char(1), ");
		createSqlStr.append("isSevenDayReturn char(1), ");
		createSqlStr.append("isQualityItem char(1), ");
		createSqlStr.append("is30DaysMaintain char(1), ");
		createSqlStr.append("page integer, ");
		createSqlStr.append("rank integer, ");
		createSqlStr.append("userNumberId long, ");
		createSqlStr.append("isLeaveACompensableThree char(1), ");
		createSqlStr.append("fetchTime varchar(50), ");
		createSqlStr.append("isTmall char(1))");

		return new String[] { TABLE_NAME, createSqlStr.toString() };
	}
}
