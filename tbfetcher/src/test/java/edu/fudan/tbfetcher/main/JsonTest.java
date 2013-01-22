package edu.fudan.tbfetcher.main;

import net.sf.json.JSONObject;

public class JsonTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String json = "{\"watershed\":100,\"maxPage\":0,\"currentPageNum\":1,\"comments\":null}";
		JSONObject jsonObj = JSONObject.fromObject(json);

		String obj = jsonObj.getString("comments");
		// if (null == obj) {
		// System.out.print("null");
		// } else {
		// System.out.println(" not null:" + obj);
		// }

		if (obj.equals("null")) {
			System.out.print("null");
		} else {
			// JsonArr
			System.out.print("null1111111");
		}
	}
}
