package edu.fudan.tbfetcher.main;

import org.apache.log4j.Logger;

public class DefaultTaobaoClient extends AbstractTaobaoClient {
	private static final Logger log = Logger.getLogger(DefaultTaobaoClient.class);

	@Override
	public void task() {
		topRankProcess();
		searchResultProcess();
		itemDetailProcess();
//		startThreads();
		shopRateProcess();
		shopItemProcess();
		itemCommentProcess();
	}

	public static void main(String[] args) {
		new DefaultTaobaoClient().execute();
	}
}
