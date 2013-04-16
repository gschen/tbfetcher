package edu.fudan.tbfetcher.handler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import edu.fudan.tbfetcher.constant.SystemConfiguration;
import edu.fudan.tbfetcher.constant.SystemConstant;
import edu.fudan.tbfetcher.dbaccess.DBManager;
import edu.fudan.tbfetcher.pageparser.ItaobaoPageParser;
import edu.fudan.tbfetcher.pojo.FeedRate;
import edu.fudan.tbfetcher.pojo.ItemComment;
import edu.fudan.tbfetcher.pojo.ItemCommentConfig;
import edu.fudan.tbfetcher.utils.TBBrowser;

public abstract class AbstractItemCommentHandler implements ItemCommentHandler {
	private static final Logger log = Logger.getLogger(AbstractItemCommentHandler.class);

	private static final long DAY_TIME = 24 * 60 * 60 * 1000;
	protected String baseCommentUrl; // 宝贝评论基本地址
	protected String itemId; // 宝贝编号信息
	protected FeedRate feedRate; // 抓取的评论信息保存在feedRate.reviewList中
	protected String firstCommentDate;
	protected String lastCommentDate;
	protected ItaobaoPageParser itaobaoPageParser;
	
	public String getFirstCommentDate() {
		return firstCommentDate;
	}

	public String getLastCommentDate() {
		return lastCommentDate;
	}

	public AbstractItemCommentHandler(String itemId, String baseCommentSUrl) {
		this.itemId = itemId;
		this.baseCommentUrl = baseCommentSUrl;
		itaobaoPageParser = new ItaobaoPageParser();
		feedRate = new FeedRate();
		firstCommentDate = "";
		lastCommentDate = "";
	}

	@Override
	public void handle() {
		int currentPageNum = 1;
		int maxPageNum = 1;
		boolean isOver = false;
		do {
			log.info("Comment url is: " + buildAjaxUrl(currentPageNum));
			boolean result = TBBrowser.get(buildAjaxUrl(currentPageNum), null);
			if (result) {
				String jsonStr = TBBrowser.getRawText();
				parseJson(jsonStr);
				if(feedRate.getCurrentPageNum() < currentPageNum){
					break;
				}
				currentPageNum = feedRate.getCurrentPageNum();
				maxPageNum = feedRate.getMaxPage();
				isOver = feedRate.isOver();
			} else {
				log.error("Item comment handler Error: Get page failed.");
			}
			if (1 == currentPageNum) {
				lastCommentDate = feedRate.getFirstDate(); // 设置最后一条评论时间
			}
			currentPageNum++;
		} while (currentPageNum <= maxPageNum && !isOver); // 当当前页>最大页时，跳出循环
		firstCommentDate = feedRate.getLastDate();
	}

	protected abstract String buildAjaxUrl(int currentPageNum);

	protected abstract void parseJson(String jsonStr);

	/**
	 * 根据评价内容获取评价类型
	 * 
	 * 0 好评！ 1 评价方未及时做出评价，系统默认好评！ 2 此用户没有填写评论! 3 用户自己做出的评价
	 * 
	 * */
	protected int getCommentContentIndicator(String content) {
		int indicator = 3;
		// 评价方未及时做出评价,系统默认好评!
		if (content.equals("评价方未及时做出评价,系统默认好评!")) {
			indicator = 1;
		} else if (content.equals("此用户没有填写评论!")) {
			indicator = 2;
		} else if (content.equals("好评！")) {
			indicator = 0;
		}
		return indicator;
	}

	protected void handleUserHomePage(String nickUrl, ItemComment reviewInfo) {
		itaobaoPageParser.setPageUrl(nickUrl);
		itaobaoPageParser.setBuyerInfo(reviewInfo);

		boolean result = itaobaoPageParser.getPage();
		if (result) {
			try {
				itaobaoPageParser.parsePage();
			} catch (Exception e) {
				log.error("ItaobaoPageParser page parse error, [url]: " + nickUrl);
				log.error("Exception: ", e);
			}
		} else {
			log.error("ItaobaoPageParse Error: Get page failed.");
		}
	}

	protected boolean needFetchAllComment() {
		// 需要获取宝贝评论历史上第一条评论时间，所以需要抓取所有的评论
		if (ItemCommentConfig.oldestData == true
				|| ItemCommentConfig.strategyNum == 0) {
			return true;
		}
		return false;
	}

	// 检测一条评论是否符合配置条件，返回的是一个bool数组，含有两个值，第一个是是否要写入数据库的，第二个是是否停止
	protected boolean[] checkComment(String dateStr, boolean isTmall) {
		boolean[] result = new boolean[2];
		// 抓取所有，并写入数据库
		if (0 == ItemCommentConfig.strategyNum) {
			result[0] = true;
			result[1] = false;
			return result;
		}

		// 获取最近多少条评论
		if (1 == ItemCommentConfig.strategyNum) {
			int reviewSize = feedRate.getFetchSum();

			log.info("Review size : " + reviewSize);
			if (reviewSize >= ItemCommentConfig.itemCommentQuantityNum) {
				log.info("Fetching enough reviews, review num is: "
						+ reviewSize);
				result[0] = false;
			} else {
				result[0] = true;
			}
		}
		// 最近多少天评论
		if (2 == ItemCommentConfig.strategyNum) {
			String dateFormatStr = "";
			if (isTmall) {
				dateFormatStr = SystemConstant.COMMENT_TM_DATE_FORMAT;
			} else {
				dateFormatStr = SystemConstant.COMMENT_TB_DATE_FORMAT;
			}
			SimpleDateFormat format = new SimpleDateFormat(dateFormatStr);
			try {
				Date currentDate = format.parse(dateStr); // 当前评论日期
				Date today = new Date();
				long interval = today.getTime() - currentDate.getTime();
				long numOfDays = interval / DAY_TIME;

				if (numOfDays > ItemCommentConfig.itemCommentDateNum) {
					result[0] = false;
				} else {
					result[0] = true;
				}
			} catch (ParseException e) {
				log.error("Check comment exception: ", e);
			}
		}
		result[1] = !result[0];
		// 获取从××到××之间的评论
		if (3 == ItemCommentConfig.strategyNum) {
			String dateFormatStr = "";
			if (isTmall) {
				dateFormatStr = "yyyy-MM-dd HH:mm:ss";
			} else {
				dateFormatStr = "yyyy.MM.dd";
			}
			// log.info("data format is: " + dateFormatStr);
			SimpleDateFormat format = new SimpleDateFormat(dateFormatStr);
			try {
				// log.info("date str is: " + dateStr);
				Date currentDate = format.parse(dateStr); // 当前评论日期
				Date startDate = format
						.parse(isTmall ? ItemCommentConfig.itemCommentStartDate
								+ " 0:0:0"
								: ItemCommentConfig.itemCommentStartDate
										.replaceAll("-", "."));
				Date endDate = format
						.parse(isTmall ? ItemCommentConfig.itemCommentEndDate
								+ " 0:0:0"
								: ItemCommentConfig.itemCommentEndDate
										.replaceAll("-", "."));

				// 如果当前日期不在配置文件中的日期里面，则返回true
				if (currentDate.getTime() > endDate.getTime()) {
					result[0] = false;
					result[1] = false;
				} else if (currentDate.getTime() < startDate.getTime()) {
					result[0] = false;
					result[1] = true;
				} else {
					result[0] = true;
					result[1] = false;
				}
			} catch (ParseException e) {
				log.error("Check comment exception: ", e);
			}
		}
		// 是否抓取所有评论，但是不一定写入数据库
		if (ItemCommentConfig.oldestData == true) {
			result[1] = false;
		}
		return result;
	}

	public void printMetrics() {
		log.info("------------------------------------------------");
		log.info("Item comment info summary is as below: ");
		log.info("Item id: " + itemId);
		log.info("Item comment strategy num: " + ItemCommentConfig.strategyNum);
		log.info("The sum of the item comment is: " + feedRate.getFetchSum());
		log.info("Oldest data is: " + ItemCommentConfig.oldestData);
		log.info("Oldest comment date: " + getFirstCommentDate());
		log.info("Lastest comment date: " + getLastCommentDate());

	}

	public static void writeDataToDB(List<ItemComment> list) {
		try {
			String result[] = ItemComment.getCreateSQLStr();
			String tableName = result[0];
			String createTableStr = result[1];

			DBManager.connectDB();
			if (!DBManager.isTableExist(tableName)) {
				log.info("create table " + tableName);
				DBManager.createTable(createTableStr);
				log.info("create table success!");
			}

			for (int i = 0; i < list.size(); i++) {
				ItemComment data = list.get(i);
				DBManager.insertData(data, tableName);
			}
		} catch (Exception e) {
			log.error("CommentHandler Write to DB Error");
			log.error("Exception: ", e);
		} finally{
			DBManager.closeDB();
		}
	}

}
