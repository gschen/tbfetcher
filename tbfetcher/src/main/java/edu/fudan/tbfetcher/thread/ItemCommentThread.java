//package edu.fudan.tbfetcher.thread;
//
//import org.apache.log4j.Logger;
//
//import edu.fudan.tbfetcher.handler.AbstractItemCommentHandler;
//import edu.fudan.tbfetcher.handler.TaobaoItemCommentHandler;
//import edu.fudan.tbfetcher.handler.TmallItemCommentHandler;
//import edu.fudan.tbfetcher.pageparser.ItemDetailPageParser;
//import edu.fudan.tbfetcher.pojo.ReviewBaseUrlInfo;
//
//public class ItemCommentThread implements Runnable {
//	private static final Logger log = Logger.getLogger(ItemCommentThread.class);
//	private ReviewBaseUrlInfo baseUrlInfo;
//
//	public ItemCommentThread(ReviewBaseUrlInfo baseUrlInfo) {
//		this.baseUrlInfo = baseUrlInfo;
//	}
//
//	@Override
//	public void run() {
//		String itemId = baseUrlInfo.getItemId();
//		String baseUrl = baseUrlInfo.getBaseUrl();
//		boolean isTmall = baseUrlInfo.isTmall();
//		if (null == baseUrl || "".equals(baseUrl)) {
//			log.info("The " + itemId
//					+ " page's review base url is null. Skip it!");
//		}
//		itemCommentHandler(isTmall, itemId, baseUrl);
//	}
//
////	public void itemCommentHandler(boolean isTmall, String itemId,
////			String baseCommentUrl) {
////		AbstractItemCommentHandler handler;
////
////		if (isTmall) {
////			handler = new TmallItemCommentHandler(itemId, baseCommentUrl);
////		} else {
////			handler = new TaobaoItemCommentHandler(itemId, baseCommentUrl);
////		}
////
////		handler.handle();
////
////		String lastReviewDate = handler.getFirstCommentDate();
////		String firstReviewDate = handler.getLastCommentDate();
////		ItemDetailPageParser.setReviewDate(itemId, firstReviewDate,
////				lastReviewDate);
////		handler.printMetrics();
////	}
//}
