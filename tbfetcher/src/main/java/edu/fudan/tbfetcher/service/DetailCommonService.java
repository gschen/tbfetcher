package edu.fudan.tbfetcher.service;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.http.client.HttpClient;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import edu.fudan.tbfetcher.formfields.GetMethod;
import edu.fudan.tbfetcher.pojo.ItemImpress;
import edu.fudan.tbfetcher.utils.GetWaitUtil;


//item impress
public class DetailCommonService {
	private static final Logger log = Logger.getLogger(DetailCommonService.class);

	private Document doc;
	private HttpClient httpClient;
	private List<ItemImpress> impresses;
	private String pageUrl;
	
	public DetailCommonService(final HttpClient client, final Document doc, final String pageUrl){
		this.httpClient = client;
		this.doc = doc;
		this.pageUrl = pageUrl;
		this.impresses = new ArrayList<ItemImpress>();
	}
	
	public String getJsonString(String ajaxUrl) {
		String json;
		GetMethod get = new GetMethod(httpClient, ajaxUrl);
		GetWaitUtil.get(get);
		json = get.getResponseAsString();
		get.shutDown();

		int base = json.indexOf("(");
		int end = json.lastIndexOf(")");
		return json.substring(base + 1, end);
	}


	public String getAjaxUrl() {
		if(null == doc){
			GetMethod get = new GetMethod(httpClient, pageUrl);
			GetWaitUtil.get(get);
			String docRawText = get.getResponseAsString();
			doc = Jsoup.parse(docRawText);
		}
		StringBuilder ajaxUrl = new StringBuilder();
		String datacommonApi;
		if (doc.select("em#J_RateStar").size() != 0) {
			Element e = doc.select("em#J_RateStar").get(0);
			datacommonApi = e.attr("data-commonApi");
			log.info("data-commonApi is: " + datacommonApi);
			ajaxUrl.append(datacommonApi);
		} else {
			log.info("There is no detail section in the page.");
			return null;
		}

		String app = "&callback=jsonp_reviews_summary";

		ajaxUrl.append(app);
		log.info("Ajax url is: " + ajaxUrl.toString());
		return ajaxUrl.toString();
	}

	public void execute() {
		String ajaxUrl = getAjaxUrl();
		if (ajaxUrl == null) {

			log.info("There is no impressess.");
		} else {
			String json = getJsonString(ajaxUrl);
			log.info("Json from server is: " + json);
			parseJson(json);
		}
	}

	
/*			{"watershed":100,
 * 			 "data":{
 * 						"correspond":"4.8","correspondCount":38700,"correspondList":["87.34","10.95","1.28","0.14","0.28"],
 * 						"count":{"additional":2,"bad":0,"correspond":0,"good":88,"goodFull":234,"hascontent":0,"normal":0,"total":88},
 * 						"impress":[{"attribute":"2324-11","count":12,"title":"温和不刺激","value":1},
 * 								   {"attribute":"2524-11","count":8,"title":"清洁度强","value":1},
 * 								   {"attribute":"420-11","count":5,"title":"物流快","value":1},
 * 								   {"attribute":"824-11","count":5,"title":"保湿滋润","value":1},
 * 								   {"attribute":"620-11","count":4,"title":"质量不错","value":1}],
 *  					"links":null,"refundTime":0,"spuRatting":[]}}
	
	*/
	public void parseJson(String json) {
		JSONObject jsonObj = JSONObject.fromObject(json);

		JSONObject obj = jsonObj.getJSONObject("data");
		JSONArray array = obj.getJSONArray("impress");

		List list = (List) JSONSerializer.toJava(array);

		if(list.size() == 0){
			log.info("There is no impressess.");
		}else{
			for (Object o : list) {

				JSONObject j = JSONObject.fromObject(o);
				
				ItemImpress impress;
				
				impress = (ItemImpress) JSONObject.toBean(j, ItemImpress.class);
				impresses.add(impress);
			}
		}
		
	}
	
	public String getImpress(){
		StringBuilder sb = new StringBuilder();
		for(ItemImpress ii : impresses){
			sb.append(ii.getTitle());
			sb.append("("+ii.getCount()+")");
		}
		return sb.toString();
	}
}
