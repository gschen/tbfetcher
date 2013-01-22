package edu.fudan.tbfetcher.constant;

import java.util.ArrayList;
import java.util.List;

import edu.fudan.tbfetcher.dbaccess.DBManager;
import edu.fudan.tbfetcher.pojo.CategoryInfo;

/**
 * 
 * 连接数据库，从SystemConfiguration表中获取系统启动配置参数信息
 * 
 * @author justin
 * 
 */
public class SystemConfiguration {
	public static int topRankNum;
	public static int pageNumInSearchResult;
	public static int sortOrderInSearchResult;
	public static boolean fetchBuyerInfoFromDealRecord;
	public static boolean fetchItemCommentContent;
	public static boolean oldestData;
	public static List<CategoryInfo> categoryList = new ArrayList<CategoryInfo>();
	public static int itemCommentStrategy;
	public static int itemCommentQuantity;
	public static int itemCommentDateNum;
	public static String itemCommentStartDate;
	public static String itemCommentEndDate;
	public static int shopItemConfig;
	public static boolean haveItemCommentTable;

	private SystemConfiguration() {

	}

	public static void init() {
		DBManager.initSystemConfiguration();
	}

}
