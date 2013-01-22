package edu.fudan.tbfetcher.constant;

public class SystemConstant {
	public static final int MAX_SHEET_ROWS = 65536;

	public static final int ADDR_THRESHHOLD = 100;// 地址门阀值

	public static final int CONNECTION_TIMEOUT = 30000;

	public static final int SO_TIMEOUT = 60000;
	public static final String dbFilePath = "D:\\tbfetcher.accdb";

	public static final int THREAD_NUM = 20;

	public static final String TOP_RANK_TABLE = "TopRankTable";
	public static final String SEARCH_RESULT_TABLE = "SearchResultTable";
	public static final String ITEM_DETAIL_TABLE = "ItemDetailTable";
	public static final String SHOP_RATE_TABLE = "ShopRateTable";
	public static final String SHOP_ITEM_TABLE = "ShopItemTable";
	public static final String ITEM_COMMENT_TABLE = "ItemCommentTable";
	public static final String SHOP_NUM_TABLE = "ShopNumTable";

	public static final int DB_WRITE_NUM = 10; // 每隔多少条顺序
}
