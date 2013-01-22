package edu.fudan.tbfetcher.handler;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import edu.fudan.tbfetcher.constant.SystemConfiguration;
import edu.fudan.tbfetcher.pojo.ItemComment;
import edu.fudan.tbfetcher.utils.DateTimeUtil;
import edu.fudan.tbfetcher.utils.TBBrowser;

public class TaobaoItemCommentHandler extends AbstractItemCommentHandler {
	
	public TaobaoItemCommentHandler(String itemId, String baseCommentSUrl) {
		super(itemId, baseCommentSUrl);
	}

	private static final Logger log = Logger.getLogger(TaobaoItemCommentHandler.class);

	@Override
	public String buildAjaxUrl(int currentPageNum) {

		return baseCommentUrl
				+ "&currentPageNum="
				+ currentPageNum
				+ "&rateType=&orderType=feedbackdate&showContent=&attribute=&callback=jsonp_reviews_list";
	}

	@Override
	public void parseJson(String jsonStr) {
		int begin = jsonStr.indexOf("{");
		int end = jsonStr.lastIndexOf("}") + 1;

		jsonStr = jsonStr.substring(begin, end);
		JSONObject json = JSONObject.fromObject(jsonStr);

		int maxPage = json.getInt("maxPage");
		int currentPage = json.getInt("currentPageNum");

		feedRate.setCurrentPageNum(currentPage);
		feedRate.setMaxPage(maxPage);

		// 先判断评论是否为空
		if (json.getString("comments").equals("null")) {
			feedRate.setOver(true);
			return;
		}

		JSONArray reviews = json.getJSONArray("comments");
		boolean fetchContet = SystemConfiguration.fetchItemCommentContent; // 是否抓取评论内容
		List<ItemComment> comments = new ArrayList<ItemComment>();
		// 依次解析从服务器端返回json数据的评论信息
		int count = 0;
		for (Object o : reviews) {
			JSONObject item = JSONObject.fromObject(o);
			String date = item.getString("date");
			boolean[] result = checkComment(date, false);
			boolean writeDB = result[0];
			boolean stopFetch = result[1];

			if (stopFetch) {
				feedRate.setOver(true);
				break;
			} else {
				if (0 == count) {
					feedRate.setFirstDate(date);
				}
				feedRate.setLastDate(date);
			}
			count++;
			if (!writeDB) {
				continue;
			}
			String content = item.getString("content");
			JSONObject user = item.getJSONObject("user");
			String nick = user.getString("nick");
			String nickUrl = user.getString("nickUrl");

			ItemComment reviewInfo = new ItemComment();

			reviewInfo.setItemId(itemId);
			reviewInfo.setBuyerNick(nick);
			reviewInfo.setBuyerHref(nickUrl);
			reviewInfo.setFetchTime(DateTimeUtil.getCurrentDateTime());
			if (fetchContet) {
				reviewInfo.setReviewContent(content);
			} else {
				reviewInfo.setReviewContent("");
			}
			reviewInfo.setReviewDate(date);
			reviewInfo.setReviewIndicator(getCommentContentIndicator(content));
			if (null != nickUrl && nickUrl.length() > 0) {
				handleUserHomePage(nickUrl, reviewInfo);
			}
			comments.add(reviewInfo);
			feedRate.setFetchSum(feedRate.getFetchSum() + 1); // 抓到并写入数据库的评论数加1
		}
		writeDataToDB(comments);
	}

}
