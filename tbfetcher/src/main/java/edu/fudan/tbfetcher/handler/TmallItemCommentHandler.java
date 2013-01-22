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

public class TmallItemCommentHandler extends AbstractItemCommentHandler {
	public TmallItemCommentHandler(String itemId, String baseCommentSUrl) {
		super(itemId, baseCommentSUrl);
	}

	private static final Logger log = Logger.getLogger(TmallItemCommentHandler.class);

	@Override
	public String buildAjaxUrl(int currentPageNum) {
		log.info("Tmall comment url is: " + baseCommentUrl + currentPageNum);
		return baseCommentUrl + currentPageNum;
	}

	@Override
	public void parseJson(String jsonStr) {
		int begin = jsonStr.indexOf("{");
		int end = jsonStr.lastIndexOf("}") + 1;

		jsonStr = jsonStr.substring(begin, end);
		JSONObject json = JSONObject.fromObject(jsonStr);
		json = json.getJSONObject("rateDetail");
		JSONObject paginator = json.getJSONObject("paginator");

		int maxPage = paginator.getInt("lastPage");
		int currentPage = paginator.getInt("page");

		feedRate.setCurrentPageNum(currentPage);
		feedRate.setMaxPage(maxPage);

		JSONArray reviews = json.getJSONArray("rateList");
		boolean fetchContet = SystemConfiguration.fetchItemCommentContent; // 是否抓取评论内容
		List<ItemComment> comments = new ArrayList<ItemComment>();
		int count = 0;
		for (Object o : reviews) {
			JSONObject item = JSONObject.fromObject(o);
			String date = item.getString("rateDate");
			boolean[] result = checkComment(date, true);
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
			String content = item.getString("rateContent");
			String nick = item.getString("displayUserNick");
			String nickUrl = item.getString("displayUserLink");

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
