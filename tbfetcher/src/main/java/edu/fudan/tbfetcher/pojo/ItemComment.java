package edu.fudan.tbfetcher.pojo;

public class ItemComment {
	public static final String TABLE_NAME = "ItemCommentTable";

	private String itemId;
	private String buyId = "-1";
	private String buyerNick = "-1";
	private String reviewContent = "-1";
	private String reviewDate = "-1";
	private int reviewIndicator;
	private String buyerName;
	private int creditScore = -1;
	private String buyerDesc = "-1"; // "小有所成"等
	private String gender = "-1"; // 男、女
	private String buyerAddress = "-1";
	private String buyerHomePageUrl;
	private String fetchTime;

	public String getFetchTime() {
		return fetchTime;
	}

	public void setFetchTime(String fetchTime) {
		this.fetchTime = fetchTime;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getBuyerNick() {
		return buyerNick;
	}

	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	public String getBuyId() {
		return buyId;
	}

	public void setBuyId(String buyId) {
		this.buyId = buyId;
	}

	public String getReviewContent() {
		return reviewContent;
	}

	public void setReviewContent(String reviewContent) {
		this.reviewContent = reviewContent;
	}

	public String getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(String reviewDate) {
		this.reviewDate = reviewDate;
	}

	public int getReviewIndicator() {
		return reviewIndicator;
	}

	public void setReviewIndicator(int reviewIndicator) {
		this.reviewIndicator = reviewIndicator;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public int getCreditScore() {
		return creditScore;
	}

	public void setCreditScore(int creditScore) {
		this.creditScore = creditScore;
	}

	public String getBuyerDesc() {
		return buyerDesc;
	}

	public void setBuyerDesc(String buyerDesc) {
		this.buyerDesc = buyerDesc;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBuyerAddress() {
		return buyerAddress;
	}

	public void setBuyerAddress(String buyerAddress) {
		this.buyerAddress = buyerAddress;
	}

	public String getBuyerHref() {
		return buyerHomePageUrl;
	}

	public void setBuyerHref(String buyerHref) {
		this.buyerHomePageUrl = buyerHref;
	}

	public static String[] getCreateSQLStr() {
		StringBuffer createSqlStr = new StringBuffer("CREATE TABLE "
				+ TABLE_NAME + "(");

		createSqlStr.append("id counter primary key, ");
		createSqlStr.append("itemId varchar(30), ");
		createSqlStr.append("buyId varchar(20), ");
		createSqlStr.append("buyerNick varchar(20), ");
		createSqlStr.append("reviewContent memo, ");
		createSqlStr.append("reviewDate varchar(10), ");
		createSqlStr.append("reviewIndicator integer, ");
		createSqlStr.append("buyerName varchar(10), ");
		createSqlStr.append("creditScore integer, ");
		createSqlStr.append("buyerDesc varchar(100), ");
		createSqlStr.append("gender char(1), ");
		createSqlStr.append("buyerAddress varchar(100), ");
		createSqlStr.append("fetchTime varchar(50), ");
		createSqlStr.append("buyerHomePageUrl varchar(200))");

		return new String[] { TABLE_NAME, createSqlStr.toString() };
	}
}
