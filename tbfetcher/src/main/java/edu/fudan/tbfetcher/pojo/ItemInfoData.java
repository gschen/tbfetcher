package edu.fudan.tbfetcher.pojo;

public class ItemInfoData {
	private int feedCount;
	private int storeCount;
	private int likeCount;
	private int shareCount;
	private int itemCount;
	private int viewCount;
	
	
	public int getFeedCount() {
		return feedCount;
	}
	public void setFeedCount(int feedCount) {
		this.feedCount = feedCount;
	}
	public int getStoreCount() {
		return storeCount;
	}
	public void setStoreCount(int storeCount) {
		this.storeCount = storeCount;
	}
	public int getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}
	public int getShareCount() {
		return shareCount;
	}
	public void setShareCount(int shareCount) {
		this.shareCount = shareCount;
	}
	public int getItemCount() {
		return itemCount;
	}
	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
	public int getViewCount() {
		return viewCount;
	}
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
	
	public void clearData(){
		this.feedCount = 0;
		this.shareCount = 0;
		this.likeCount = 0;
		this.shareCount = 0;
		this.itemCount = 0;
		this.viewCount = 0;
	}
}
