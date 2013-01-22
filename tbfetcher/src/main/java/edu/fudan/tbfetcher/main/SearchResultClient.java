package edu.fudan.tbfetcher.main;

/*
 * 
 * 
 * 根据预先配置的search_result_urls,进行数据抓取工作
 */
public class SearchResultClient extends AbstractTaobaoClient {

	@Override
	public void task() {

	}

	public static void main(String[] args) {
		new DefaultTaobaoClient().execute();
	}

}
