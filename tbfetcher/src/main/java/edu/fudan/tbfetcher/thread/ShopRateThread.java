package edu.fudan.tbfetcher.thread;

import edu.fudan.tbfetcher.pojo.ShopIndexInfo;

public class ShopRateThread implements Runnable {

	private ShopIndexInfo shopInfo;

	public ShopRateThread(ShopIndexInfo shopInfo) {
		this.shopInfo = shopInfo;
	}

	@Override
	public void run() {

	}

}
