package edu.fudan.tbfetcher.pojo;

public class ItemReviewComment {

	private ItemReviewAuction auction;
	private String content;
	private String append;
	public String getAppend() {
		return append;
	}
	public void setAppend(String append) {
		this.append = append;
	}
	private String rate;
	private String tag;
	private int rateId;
	private String award;
	private String reply;
	private int useful;
	private String date;
	private ItemReviewUser user;
	
	public ItemReviewAuction getAuction() {
		return auction;
	}
	public void setAuction(ItemReviewAuction auction) {
		this.auction = auction;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public int getRateId() {
		return rateId;
	}
	public void setRateId(int rateId) {
		this.rateId = rateId;
	}
	public String getAward() {
		return award;
	}
	public void setAward(String award) {
		this.award = award;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
	public int getUseful() {
		return useful;
	}
	public void setUseful(int useful) {
		this.useful = useful;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public ItemReviewUser getUser() {
		return user;
	}
	public void setUser(ItemReviewUser user) {
		this.user = user;
	}
	
	
	
}
