package edu.fudan.tbfetcher.pojo;

public class ItemCommentWrapper {

	private ItemComment itemComment;
	private int indicator = 1;// 用来标识当前的评论是否有效，默认为1，1为有效，0为无效(不写入数据库itemComment表中)

	public ItemComment getItemComment() {
		return itemComment;
	}

	public void setItemComment(ItemComment itemComment) {
		this.itemComment = itemComment;
	}

	public int getIndicator() {
		return indicator;
	}

	public void setIndicator(int indicator) {
		this.indicator = indicator;
	}
}
