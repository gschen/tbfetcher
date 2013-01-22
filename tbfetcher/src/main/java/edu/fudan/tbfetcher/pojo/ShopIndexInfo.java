package edu.fudan.tbfetcher.pojo;

public class ShopIndexInfo {
	private String shopId;
	private boolean isTmall;
	private String shopUrl;
	private String shopRateUrl;
	private long userNumberId = 0L;// 店铺用户id

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

	public boolean isTmall() {
		return isTmall;
	}

	public void setTmall(boolean isTmall) {
		this.isTmall = isTmall;
	}

	public String getShopUrl() {
		return shopUrl;
	}

	public void setShopUrl(String shopUrl) {
		this.shopUrl = shopUrl;
	}

	public String getShopRateUrl() {
		return shopRateUrl;
	}

	public void setShopRateUrl(String shopRateUrl) {
		this.shopRateUrl = shopRateUrl;
	}
}
