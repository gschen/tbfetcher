package edu.fudan.tbfetcher.pojo;

public class ShopItemInfo {
	public static final String TABLE_NAME = "ShopItemTable";

	private String shopId;
	private String itemId;
	private String itemName;
	private String price;
	private int monthSaleNum;
	private int viewNum;
	private int numOfCollectItem;
	private int numOfLike;
	private int numOfShare;
	private int reviewNum;
	private int numInStock;
	private boolean isSoldout;
	private int totalDealNum;
	private String fetchTime;

	public String getFetchTime() {
		return fetchTime;
	}

	public void setFetchTime(String fetchTime) {
		this.fetchTime = fetchTime;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public int getMonthSaleNum() {
		return monthSaleNum;
	}

	public void setMonthSaleNum(int monthSaleNum) {
		this.monthSaleNum = monthSaleNum;
	}

	public int getViewNum() {
		return viewNum;
	}

	public void setViewNum(int viewNum) {
		this.viewNum = viewNum;
	}

	public int getNumOfCollectItem() {
		return numOfCollectItem;
	}

	public void setNumOfCollectItem(int numOfCollectItem) {
		this.numOfCollectItem = numOfCollectItem;
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

	public int getReviewNum() {
		return reviewNum;
	}

	public void setReviewNum(int reviewNum) {
		this.reviewNum = reviewNum;
	}

	public int getNumInStock() {
		return numInStock;
	}

	public void setNumInStock(int numInStock) {
		this.numInStock = numInStock;
	}

	public boolean isSoldout() {
		return isSoldout;
	}

	public void setSoldout(boolean isSoldout) {
		this.isSoldout = isSoldout;
	}

	public int getTotalDealNum() {
		return totalDealNum;
	}

	public void setTotalDealNum(int totalDealNum) {
		this.totalDealNum = totalDealNum;
	}

	public static String[] getCreateSQLStr() {
		StringBuffer createSqlStr = new StringBuffer("CREATE TABLE "
				+ TABLE_NAME + "(");

		createSqlStr.append("id counter primary key, ");
		createSqlStr.append("itemId varchar(20), ");
		createSqlStr.append("shopId varchar(20), ");
		createSqlStr.append("itemName varchar(100), ");
		createSqlStr.append("price varchar(20), ");
		createSqlStr.append("monthSaleNum integer, ");
		createSqlStr.append("viewNum integer, ");
		createSqlStr.append("numInStock integer, ");
		createSqlStr.append("numOfLike integer, ");
		createSqlStr.append("numOfShare integer, ");
		createSqlStr.append("numOfCollectItem integer, ");
		createSqlStr.append("reviewNum integer, ");
		createSqlStr.append("isSoldout char(1), ");
		createSqlStr.append("fetchTime varchar(50), ");
		createSqlStr.append("totalDealNum integer)");

		return new String[] { TABLE_NAME, createSqlStr.toString() };
	}
}
