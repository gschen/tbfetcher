package edu.fudan.tbfetcher.thread;

import edu.fudan.tbfetcher.pojo.SellerInSearchResult;

public class ItemDetailThread implements Runnable {

	private SellerInSearchResult sellerInSearchResult;

	public ItemDetailThread(SellerInSearchResult sellerInSearchResult) {
		this.sellerInSearchResult = sellerInSearchResult;
	}

	@Override
	public void run() {

	}

}
