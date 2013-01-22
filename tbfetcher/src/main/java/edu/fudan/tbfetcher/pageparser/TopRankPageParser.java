package edu.fudan.tbfetcher.pageparser;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.fudan.tbfetcher.constant.SystemConfiguration;
import edu.fudan.tbfetcher.dbaccess.DBManager;
import edu.fudan.tbfetcher.pojo.TopRankItemInfo;

/**
 * 根据提供的页面的url，也就是根据提供的category name获取top rank的item的信息
 * 
 * */
public class TopRankPageParser extends BasePageParser {

	private static final Logger log = Logger.getLogger(TopRankPageParser.class);

	private List<TopRankItemInfo> topTenItemInfos;
	private String categoryName;

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public List<TopRankItemInfo> getTopTenItemInfos() {
		return topTenItemInfos;
	}

	@Override
	public void init() {
		super.init();
		categoryName = null;
		topTenItemInfos = new ArrayList<TopRankItemInfo>();
	}

	@Override
	public void parsePage() {
		log.info("Start to parse page " + TopRankPageParser.class);

		int topRank = SystemConfiguration.topRankNum;
		int rankCount = 0;
		while (rankCount < topRank) {
			Elements itemDiv = doc.select("div.items, div.imagelists");
			Elements itemList = itemDiv.select("ol > li");

			for (int i = 0; i < itemList.size() && rankCount < topRank; i++) {
				Element item = itemList.get(i);

				String rankStr = item.child(0).text();
				int rank = Integer.valueOf(rankStr);
				Element link = item.select("a.name").get(0);
				String href = link.attr("href");
				href = href.substring(href.lastIndexOf("http"));
				String itemName = link.text();

				TopRankItemInfo itemInfo = new TopRankItemInfo();
				itemInfo.setTopRank(rank);
				itemInfo.setHref(href);
				itemInfo.setItemName(itemName);
				itemInfo.setCategoryName(categoryName);

				topTenItemInfos.add(itemInfo);
				rankCount ++;

				log.info("Top Rank Page - rank: " + itemInfo.getTopRank());
				log.info("Top Rank Page - href:" + itemInfo.getHref());
				log.info("Top Rank Page - itemName:" + itemInfo.getItemName());
			}

			Elements nextPageEls = doc.select("a.page-next");
			if (nextPageEls.size() > 0) {
				Element nextPageLink = nextPageEls.get(0);
				String pageUrl = nextPageLink.attr("href");
				setPageUrl(pageUrl);
				boolean result = getPage();
				if (!result) {
					log.error("Top Rank Page Parser Error: Get Page failed. [url]: "
							+ pageUrl);
					break;
				}
			} else {
				break;
			}
		}
	}

	/* write the data to the access database */
	public static void writeDataToDB(List<TopRankItemInfo> list) {
		try {
			String result[] = TopRankItemInfo.getCreateSQLStr();
			String tableName = result[0];
			String createTableStr = result[1];

			DBManager.connectDB();
			if (!DBManager.isTableExist(tableName)) {
				log.info("create table " + tableName);
				DBManager.createTable(createTableStr);
				log.info("create table success!");
			}
			for (int i = 0; i < list.size(); i++) {
				TopRankItemInfo data = list.get(i);
				DBManager.insertData(data, tableName);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("Top Rank Page Parse Write to DB Error");
			log.error("Exception: ", e);
		} finally{
			DBManager.closeDB();
		}
	}
}
