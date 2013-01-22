package edu.fudan.tbfetcher.service;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import edu.fudan.tbfetcher.utils.HttpClientUtil;
import edu.fudan.tbfetcher.utils.TBBrowser;

public class PostageService {
	private static final Logger log = Logger.getLogger(PostageService.class);

	private String freight;
	private int saleSum;
	private String rawText;
	private boolean isTmall;
	private String referer;
	private Document doc;
	private String itemUrl;

	public void setItemUrl(String itemUrl) {
		this.itemUrl = itemUrl;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public void setTmall(boolean isTmall) {
		this.isTmall = isTmall;
	}

	public void setRawText(String rawText) {
		this.rawText = rawText;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public String getFreight() {
		return freight;
	}

	public int getSaleSum() {
		return saleSum;
	}

	/**
	 * init the data
	 * */
	public void init() {
		saleSum = 0;
		rawText = null;
		isTmall = false;
		referer = null;
		freight = null;
	}

	private void itemDomainPostageParser() {
		int base = rawText.indexOf("apiItemInfo");
		String ajaxUrl = "";
		String jsonStr = "";
		if (-1 != base) {
			try {
				int begin = rawText.indexOf("http", base);
				int end = rawText.indexOf("\"", begin);
				ajaxUrl = rawText.substring(begin, end);
				log.info("The postage ajax url is: " + ajaxUrl);
				boolean result = TBBrowser.get(ajaxUrl, null);
				if (result) {
					jsonStr = TBBrowser.getRawText();
					itemJsonParse(jsonStr);
				}
			} catch (Exception e) {
				log.error("Postage Sevice Error, [taobao]");
				log.error("Ajax url:" + ajaxUrl);
				log.error("JsonSting: " + jsonStr);
				log.error("Exception: ", e);
			}
			
		} else {
			log.error("Postage Service Error: No postage base url has found.");
			return;
		}
	}

	private void tmDomainPostageParser() {
		int base = rawText.indexOf("initApi");
		String ajaxUrl = "";
		String jsonStr = "";
		if (base > 0) {
			try {
				int begin = rawText.indexOf("http", base);
				int end = rawText.indexOf("\"", begin);
				ajaxUrl = rawText.substring(begin, end)
						+ "&callback=jsonp1356709653822_0&ip=&campaignId=&key=&abt=&cat_id=&q=&u_channel=&ref=";
				List<NameValuePair> headers = new ArrayList<NameValuePair>();
				NameValuePair nvp = new BasicNameValuePair("referer", referer);
				headers.add(nvp);
				boolean result = TBBrowser.get(ajaxUrl, headers);
				if (result) {
					jsonStr = TBBrowser.getRawText();
					tmallJsonParse(jsonStr);
				}
			} catch (Exception e) {
				log.error("Postage Sevice Error, [tmall]");
				log.error("Ajax url:" + ajaxUrl);
				log.error("JsonSting: " + jsonStr);
				log.error("Exception: ", e);
			}
		} else {
			log.info("卖家自己承担运费");
			freight = "卖家自己承担运费";
			saleSum = getSaleOutNum(getSaleOutNumUrl());
			return;
		}
	}

	private String getSaleOutNumUrl() {

		String saleOutNumUrl = "";
		String host = HttpClientUtil.getTargetUrl(itemUrl).split("[?]")[0];
		log.info("host is: " + host);
		String base = host.substring(0, host.length() - "detail.htm".length());
		Elements saleOutEles = doc.select("li.sale-out > em");
		if (saleOutEles.size() > 0) {
			String append = saleOutEles.first().attr("data-url");

			saleOutNumUrl = base + append;
		}
		log.info("Sale num url is: " + saleOutNumUrl);
		return saleOutNumUrl;
	}

	private int getSaleOutNum(String str) {
		TBBrowser.get(str);
		JSONObject obj = JSONObject.fromObject(TBBrowser.getRawText());

		return obj.getInt("soldNum");
	}

	/**
	 * 
	 * 调用此函数后可以得到邮费的起始地址和邮费
	 * 
	 * @param getUrl
	 * @return
	 */
	public void execute() {
		try {
			if (isTmall) {
				tmDomainPostageParser();
			} else {
				itemDomainPostageParser();
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("Postage service error: exception happened.");
			log.error("Exception: ", e);
		}
	}

	/*
	 * Parse json object from string for normal $callback({ postage:{
	 * type:'applyPostage', location:'广东深圳', destination:'上海',
	 * carriage:'快递:7.00元 EMS:21.00元 ', dataUrl:
	 * 'http://delivery.taobao.com/detail/detail.do?itemCount=1&amp;itemId=5656200607&amp;itemValue=39.00&amp;isSellerPay=false&amp;templateId=12699&amp;userId=934381&amp;unifiedPost=6.00&amp;unifiedExpress=6.00&amp;unifiedEms=21.00&amp;weight=0&amp;size=0',
	 * cityId:'310000' } , quantity:{ quanity: 76, interval: 30 } });
	 */
	private void itemJsonParse(String jsonStr) {
		int begin = jsonStr.indexOf("{");
		int end = jsonStr.lastIndexOf("}") + 1;
		jsonStr = jsonStr.substring(begin, end);
		JSONObject json = JSONObject.fromObject(jsonStr);
		JSONObject postJson = json.getJSONObject("postage");
		JSONObject saleJson = json.getJSONObject("quantity");
		String location = postJson.getString("location");
		String carriage = postJson.getString("carriage");
		carriage = carriage.replaceAll("<span>", "");
		carriage = carriage.replaceAll("<span/>", "");

		freight = location + " : " + carriage;

		if (saleJson.toString().equals("null")) {// 但返回的json数据不包含quannity时
			saleSum = 0;
		} else {
			saleSum = saleJson.getInt("quanity");
		}

		log.info("Location: " + location);
		log.info("Carriage: " + carriage);
		log.info("Sale sum is: " + saleSum);

	}

	/** get the postage and sale num from the json string */
	private void tmallJsonParse(String jsonStr) {
		int base = jsonStr.lastIndexOf("postage");
		int begin = jsonStr.indexOf(":\"", base);
		int end = jsonStr.indexOf("\"", begin + 2);

		String carriage = jsonStr.substring(begin + 2, end);

		freight = "上海" + " : " + carriage;

		begin = jsonStr.indexOf("sellCount\":");
		end = jsonStr.indexOf("}", begin);
		String saleNumStr = jsonStr.substring(begin + 11, end);
		saleSum = Integer.parseInt(saleNumStr);
	}
}
