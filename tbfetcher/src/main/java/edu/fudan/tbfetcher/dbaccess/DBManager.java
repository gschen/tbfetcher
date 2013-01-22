package edu.fudan.tbfetcher.dbaccess;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;

import edu.fudan.tbfetcher.constant.SystemConfiguration;
import edu.fudan.tbfetcher.constant.SystemConstant;
import edu.fudan.tbfetcher.pojo.CategoryInfo;
import edu.fudan.tbfetcher.pojo.ItemComment;
import edu.fudan.tbfetcher.pojo.ItemInfo;
import edu.fudan.tbfetcher.pojo.SellerInSearchResult;
import edu.fudan.tbfetcher.pojo.ShopItemInfo;
import edu.fudan.tbfetcher.pojo.ShopRate;
import edu.fudan.tbfetcher.pojo.TopRankItemInfo;

public class DBManager {

	private static Connection connection;
	private final static String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
	private final static String dbUrlString = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ="
			+ SystemConstant.dbFilePath;
	private final static Logger log = Logger.getLogger(DBManager.class);

	// 创建所有的table
	public static void createTable() {
		// create toptanktable
		String result[] = TopRankItemInfo.getCreateSQLStr();
		String tableName = result[0];
		String createTableStr = result[1];

		if (!DBManager.isConnected()) {
			DBManager.connectDB();
		}
		if (!DBManager.isTableExist(tableName)) {
			log.info("create table " + tableName);
			DBManager.createTable(createTableStr);
			log.info("create table success!");
		}

		// create search result table
		result = SellerInSearchResult.getCreateSQLStr();
		tableName = result[0];
		createTableStr = result[1];

		if (!DBManager.isConnected()) {
			DBManager.connectDB();
		}
		if (!DBManager.isTableExist(tableName)) {
			log.info("create table " + tableName);
			DBManager.createTable(createTableStr);
			log.info("create table success!");
		}

		// create item detail table
		result = ItemInfo.getCreateSQLStr();
		tableName = result[0];
		createTableStr = result[1];

		if (!DBManager.isConnected()) {
			DBManager.connectDB();
		}
		if (!DBManager.isTableExist(tableName)) {
			log.info("create table " + tableName);
			DBManager.createTable(createTableStr);
			log.info("create table success!");
		}
		// create shop rate table
		result = ShopRate.getCreateSQLStr();
		tableName = result[0];
		createTableStr = result[1];

		if (!DBManager.isConnected()) {
			DBManager.connectDB();
		}
		if (!DBManager.isTableExist(tableName)) {
			log.info("create table " + tableName);
			DBManager.createTable(createTableStr);
			log.info("create table success!");
		}
		// create shop item table
		result = ShopItemInfo.getCreateSQLStr();
		tableName = result[0];
		createTableStr = result[1];

		if (!DBManager.isConnected()) {
			DBManager.connectDB();
		}
		if (!DBManager.isTableExist(tableName)) {
			log.info("create table " + tableName);
			DBManager.createTable(createTableStr);
			log.info("create table success!");
		}

		// create item comment table
		result = ItemComment.getCreateSQLStr();
		tableName = result[0];
		createTableStr = result[1];

		if (!DBManager.isConnected()) {
			DBManager.connectDB();
		}
		if (!DBManager.isTableExist(tableName)) {
			log.info("create table " + tableName);
			DBManager.createTable(createTableStr);
			log.info("create table success!");
		}
	}

	/* write the data to the access database */
	public static void writeDataToDB(TopRankItemInfo data) {
		try {
			DBManager.insertData(data, SystemConstant.TOP_RANK_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeDataToDB(SellerInSearchResult data) {
		try {
			DBManager.insertData(data, SystemConstant.SEARCH_RESULT_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeDataToDB(ItemInfo data) {
		try {
			DBManager.insertData(data, SystemConstant.ITEM_DETAIL_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeDataToDB(ShopRate data) {
		try {
			DBManager.insertData(data, SystemConstant.SHOP_RATE_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeDataToDB(ShopItemInfo data) {
		try {
			DBManager.insertData(data, SystemConstant.SHOP_ITEM_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeDataToDB(ItemComment data) {
		try {
			DBManager.insertData(data, SystemConstant.ITEM_COMMENT_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void init() {
		connectDB();
		createTable();
	}

	public static void shutdown() {

		closeDB();
	}

	public static void connectDB() {
		try {
			Class.forName(driver);
			Properties prop = new Properties();
			prop.put("charSet", "GBK"); // 解决中文乱码
			// String dbPath = XmlConfUtil.getValueByName("DBFilePath");
			// String url = urlPre + dbPath;
			connection = DriverManager.getConnection(dbUrlString, prop);
		} catch (Exception e) {
			log.error(e.getStackTrace());
			e.printStackTrace();
		}
	}

	public static boolean isConnected() {
		if (null == connection) {
			return false;
		}
		return true;
	}

	public static boolean createTable(String sqlStr) {
		// TODO Auto-generated method stub
		if (null == connection) {
			connectDB();
		}
		try {
			Statement stat = connection.createStatement();
			stat.execute(sqlStr);
			stat.close();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getStackTrace());
			e.printStackTrace();
		}
		return false;
	}

	public static boolean insertData(Object data, String tableName) {
		String sqlStr = SQLFactory.getInsertSQLStr(data, tableName);
		log.info("insert SQL: \n" + sqlStr);
		if (null == connection) {
			connectDB();
		}
		try {
			Statement stat = connection.createStatement();
			int result = stat.executeUpdate(sqlStr);

			if (1 == result) {
				stat.close();
				log.info("insert success!");
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getStackTrace());
			e.printStackTrace();
			log.debug("Exception happened at insert SQL:" + sqlStr);
		}
		return false;
	}

	public static boolean closeDB() {
		// TODO Auto-generated method stub
		if (null != connection) {
			try {
				connection.close();
				connection = null;
				return true;
			} catch (Exception e) {
				// TODO: handle exception
				log.error(e.getStackTrace());
				e.printStackTrace();
			}
		}

		return false;
	}

	public static boolean isTableExist(String tableName) {
		if (null == connection) {
			connectDB();
		}
		try {
			DatabaseMetaData meta = connection.getMetaData();
			ResultSet set = meta.getTables(null, null, tableName, null);
			while (set.next()) {
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getStackTrace());
			e.printStackTrace();
		}
		return false;
	}

	public static long getCountOfTable(String tableName) {
		if (null == connection) {
			connectDB();
		}
		try {
			String SQLStr = "select count(*) as cnt from " + tableName;
			Statement stat = connection.createStatement();
			ResultSet rs = stat.executeQuery(SQLStr);
			rs.next();
			return rs.getLong(1);
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getStackTrace());
			e.printStackTrace();
		}
		return 0;
	}

	public static void initSystemConfiguration() {
		if (null == connection) {
			connectDB();
		}
		try {
			String sql = "select * from SystemConfigurationTable";
			Statement stat = connection.createStatement();
			ResultSet rs = stat.executeQuery(sql);
			rs.next();

			SystemConfiguration.pageNumInSearchResult = rs
					.getInt("pageNumInSearchResult");
			SystemConfiguration.fetchBuyerInfoFromDealRecord = rs
					.getBoolean("fetchBuyerInfoFromDealRecord");
			SystemConfiguration.oldestData = rs.getBoolean("oldestData");
			SystemConfiguration.fetchItemCommentContent = rs
					.getBoolean("fetchItemCommentContent");
			SystemConfiguration.haveItemCommentTable = rs
					.getBoolean("haveItemCommentTable");
			SystemConfiguration.topRankNum = rs.getInt("topRankNum");
			SystemConfiguration.sortOrderInSearchResult = rs
					.getInt("sortOrderInSearchResult");
			SystemConfiguration.itemCommentDateNum = rs
					.getInt("itemCommentDateNum");
			SystemConfiguration.itemCommentQuantity = rs
					.getInt("itemCommentQuantity");
			SystemConfiguration.itemCommentStrategy = rs
					.getInt("itemCommentStrategy");
			SystemConfiguration.itemCommentStartDate = rs
					.getString("itemCommentStartDate");
			SystemConfiguration.itemCommentEndDate = rs
					.getString("itemCommentEndDate");
			SystemConfiguration.shopItemConfig = rs.getInt("shopItemConfig");

			String categoryStr = rs.getString("categoryList");

			String[] categoryArray = categoryStr.split(";");

			log.info(categoryArray.length);

			for (int i = 0; i < categoryArray.length; ++i) {

				CategoryInfo ca = new CategoryInfo();
				ca.setCategoryName(categoryArray[i].split(":")[0]);
				ca.setCategoryHref(categoryArray[i].split("::")[1]);
				SystemConfiguration.categoryList.add(ca);
			}

			for (CategoryInfo info : SystemConfiguration.categoryList) {
				log.info(info.getCategoryName());
				log.info(info.getCategoryHref());
			}

		} catch (Exception e) {
			log.error(e.getStackTrace());
			e.printStackTrace();
		}

	}

	public static void executeSQL(String sqlStr) {
		if (null == connection) {
			connectDB();
		}
		if (null == sqlStr || 0 == sqlStr.length()) {
			return;
		}
		try {
			Statement stat = connection.createStatement();
			int result = stat.executeUpdate(sqlStr);
			stat.close();
			System.out.println(result);
			if (result > 0) {
				log.info("Execute SQL Success.");
			} else {
				log.error("Execute SQL Failed.");
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("DBManager Error: Exception happeded.");
			log.error("Exception: ", e);
		}
	}
}
