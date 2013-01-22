package edu.fudan.tbfetcher.pojo;

public class ReviewBaseUrlInfo {
	private String itemId;
	private String baseUrl;
	private boolean isTmall;
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	public boolean isTmall() {
		return isTmall;
	}
	public void setTmall(boolean isTmall) {
		this.isTmall = isTmall;
	}
}	
