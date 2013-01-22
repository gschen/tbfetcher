package edu.fudan.tbfetcher.pojo;

import org.apache.log4j.Logger;

import edu.fudan.tbfetcher.constant.SystemConfiguration;
import edu.fudan.tbfetcher.utils.XmlConfUtil;

public class ItemCommentConfig {
	private static final Logger log = Logger.getLogger(ItemCommentConfig.class);
	public static int strategyNum;
	public static int itemCommentQuantityNum;
	public static int itemCommentDateNum;
	public static String itemCommentStartDate;
	public static String itemCommentEndDate;
	public static boolean oldestData;

	public static void initConfig() {
		strategyNum = SystemConfiguration.itemCommentStrategy;

		switch (strategyNum) {
		case 0:
			break;
		case 1:
			itemCommentQuantityNum = SystemConfiguration.itemCommentQuantity;
			break;
		case 2:
			itemCommentDateNum = SystemConfiguration.itemCommentDateNum;

			break;
		case 3:
			itemCommentStartDate = SystemConfiguration.itemCommentStartDate;
			itemCommentEndDate = SystemConfiguration.itemCommentEndDate;
			break;

		default:

		}

		oldestData = SystemConfiguration.oldestData;
	}

	public static void init() {

		strategyNum = Integer.parseInt(XmlConfUtil
				.getValueByName("strategyNum"));

		switch (strategyNum) {
		case 0:
			break;
		case 1:
			itemCommentQuantityNum = Integer.parseInt(XmlConfUtil
					.getValueByName("itemCommentQuantityNum"));
			break;
		case 2:
			itemCommentDateNum = Integer.parseInt(XmlConfUtil
					.getValueByName("itemCommentDateNum"));

			break;
		case 3:
			itemCommentStartDate = XmlConfUtil
					.getValueByName("itemCommentStartDate");
			itemCommentEndDate = XmlConfUtil
					.getValueByName("itemCommentEndDate");
			break;

		default:

		}
	}

}
