package edu.fudan.tbfetcher.pojo;

public class FeedRateComment {

	private FeedRateCommentAuction auction;
	private String content;
	private String append;
	private String rate;
	private String tag;
	private String rateId;
	private String award;
	private int userful;
	private String date;
	private FeedRateUserAttr user;
	
	public FeedRateCommentAuction getAuction() {
		return auction;
	}
	public void setAuction(FeedRateCommentAuction auction) {
		this.auction = auction;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAppend() {
		return append;
	}
	public void setAppend(String append) {
		this.append = append;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getRateId() {
		return rateId;
	}
	public void setRateId(String rateId) {
		this.rateId = rateId;
	}
	public String getAward() {
		return award;
	}
	public void setAward(String award) {
		this.award = award;
	}
	public int getUserful() {
		return userful;
	}
	public void setUserful(int userful) {
		this.userful = userful;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public FeedRateUserAttr getUser() {
		return user;
	}
	public void setUser(FeedRateUserAttr user) {
		this.user = user;
	}
}
