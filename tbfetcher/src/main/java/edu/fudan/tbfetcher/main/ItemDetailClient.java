package edu.fudan.tbfetcher.main;

/*
 * 
 * 根据预先定义的一批Item_detail_urls来完成数据抓取
 */
public class ItemDetailClient extends AbstractTaobaoClient {

	@Override
	public void task() {

	}

	public static void main(String[] args) {
		new DefaultTaobaoClient().execute();
	}

}
