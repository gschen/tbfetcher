package edu.fudan.tbfetcher.pojo;

import java.util.List;

public class ItemReview {
	int watershed;
	int maxPage;
	int currentPageNum;
	List<ItemReviewComment> comments;
	
	
	public ItemReview() {
		//comments = new ArrayList<ItemReviewComment>();
	}
	
	public int getWatershed() {
		return watershed;
	}
	public void setWatershed(int watershed) {
		this.watershed = watershed;
	}
	public int getMaxPage() {
		return maxPage;
	}
	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}
	public int getCurrentPageNum() {
		return currentPageNum;
	}
	public void setCurrentPageNum(int currentPageNum) {
		this.currentPageNum = currentPageNum;
	}
	public List<ItemReviewComment> getComments() {
		return comments;
	}
	public void setComments(List<ItemReviewComment> comments) {
		this.comments = comments;
	}
}
