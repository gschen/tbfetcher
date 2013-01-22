package edu.fudan.tbfetcher.pojo;

import edu.fudan.tbfetcher.constant.SystemConstant;

public class TopRankItemInfo {
	private int topRank;
	private String productName;
	private String href;
	private String categoryName;
	private int weekSaleNum;
	private int weekSellerNum;

	public int getWeekSaleNum() {
		return weekSaleNum;
	}

	public void setWeekSaleNum(int weekSaleNum) {
		this.weekSaleNum = weekSaleNum;
	}

	public int getWeekSellerNum() {
		return weekSellerNum;
	}

	public void setWeekSellerNum(int weekSellerNum) {
		this.weekSellerNum = weekSellerNum;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getTopRank() {
		return topRank;
	}

	public void setTopRank(int topRank) {
		this.topRank = topRank;
	}

	public String getItemName() {
		return productName;
	}

	public void setItemName(String itemName) {
		this.productName = itemName;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public static String[] getCreateSQLStr() {
		StringBuffer createSqlStr = new StringBuffer("CREATE TABLE "
				+ SystemConstant.TOP_RANK_TABLE + "(");

		createSqlStr.append("id counter primary key, ");
		createSqlStr.append("topRank integer, ");
		createSqlStr.append("productName varchar(20), ");
		createSqlStr.append("href memo, ");
		createSqlStr.append("categoryName varchar(10), ");
		createSqlStr.append("weekSaleNum integer, ");
		createSqlStr.append("weekSellerNum integer)");

		return new String[] { SystemConstant.TOP_RANK_TABLE, createSqlStr.toString() };
	}

}
