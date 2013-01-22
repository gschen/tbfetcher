package edu.fudan.tbfetcher.pojo;

public class FeedRate {

	private int maxPage;        // 返回的json数据中的最大页数
	private int currentPageNum; // 返回的json数据中的当前页数
	private int fetchSum;       // 所获取的所有评论的条数
	private boolean isOver;     //是否停止抓取
	private String firstDate;  // 存储该页的第一条评论的时间
	private String lastDate;   // 存储该页的最后一条评论的时间
	
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

	public int getFetchSum() {
		return fetchSum;
	}

	public boolean isOver() {
		return isOver;
	}

	public void setOver(boolean isOver) {
		this.isOver = isOver;
	}

	public void setFetchSum(int fetchSum) {
		this.fetchSum = fetchSum;
	}

	public String getFirstDate() {
		return firstDate;
	}

	public void setFirstDate(String firstDate) {
		this.firstDate = firstDate;
	}

	public String getLastDate() {
		return lastDate;
	}

	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}
}
