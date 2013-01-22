package edu.fudan.tbfetcher.service;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import edu.fudan.tbfetcher.pojo.MonthServiceEntity;
import edu.fudan.tbfetcher.utils.TBBrowser;

public class MonthService {

	private static final Logger log = Logger.getLogger(MonthService.class);
	private Document doc;
	private String monthuserid;
	private String userTag;
	private String isB2C;
	private List<MonthServiceEntity> monthServieEntities;

	private long userNumberId = 0L;

	public void setUserNumberId(long userNumberId) {
		this.userNumberId = userNumberId;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public MonthService() {
		monthServieEntities = new ArrayList<MonthServiceEntity>();
	}

	public void init() {
		doc = null;
		monthuserid = null;
		userTag = null;
		isB2C = null;
		monthServieEntities.clear();
	}

	public List<MonthServiceEntity> getMonthServieEntities() {
		return monthServieEntities;
	}

	public void execute() {
		String ajaxUrl = "";
		String jsonStr = "";
		try {
			ajaxUrl = "http://rate.taobao.com/ShopService4C.htm?callback=monthinfo_shoprate&userNumId="
					+ String.valueOf(userNumberId);

			jsonStr = getJson(ajaxUrl);
			getData(jsonStr);
		} catch (Exception e) {
			log.error("Month Service Error: [userNumberId]: " + userNumberId);
			log.error("Ajax Url: " + ajaxUrl);
			log.error("JsonString: " + jsonStr);
			log.error("Exception: ", e);
		}
	}

	public String getJson(String ajaxUrl) {
		boolean result = TBBrowser.get(ajaxUrl, null);
		String jsonStr = null;
		if (result) {
			jsonStr = TBBrowser.getRawText();
			int begin = jsonStr.indexOf("{");
			int end = jsonStr.lastIndexOf("}") + 1;
			jsonStr = jsonStr.substring(begin, end);
		}
		if (jsonStr.startsWith("{") && jsonStr.endsWith("}")) {
			return jsonStr;
		} else {
			log.error("This is not a json string.");
			log.error("Error info is:");
			log.error("Ajax url is: " + ajaxUrl);
			log.error("Response string from server is: "
					+ TBBrowser.getRawText());
			log.error("------------------------------------------");
		}
		return null;
	}

	public String getComparison(String first, String last) {
		String comparison = null;

		float f = Float.parseFloat(first);
		float l = Float.parseFloat(last);

		if (f > l) {
			comparison = ">";
		} else {
			comparison = "<";
		}
		return comparison;

	}

	private void getData(String json) {
		JSONObject obj = JSONObject.fromObject(json);

		if (obj != null) {
			JSONObject avgRefund = obj.getJSONObject("avgRefund");
			JSONObject ratRefund = obj.getJSONObject("ratRefund");
			JSONObject complaints = obj.getJSONObject("complaints");
			JSONObject punish = obj.getJSONObject("punish");

			MonthServiceEntity avgEntity = new MonthServiceEntity();
			MonthServiceEntity ratEntity = new MonthServiceEntity();
			MonthServiceEntity comEntity = new MonthServiceEntity();
			MonthServiceEntity punEntity = new MonthServiceEntity();

			if (avgRefund != null) {
				avgEntity.setNativeValue(avgRefund.getString("localVal"));
				avgEntity.setAvgValue(avgRefund.getString("indVal"));
				avgEntity.setComparison(getComparison(
						avgRefund.getString("localVal"),
						avgRefund.getString("indVal")));
			} else {
				log.error("There is no avgRefund field in the json string.");
				log.error("Error info is: ");
				log.error("Json string is: " + json);
			}
			if (ratRefund != null) {
				ratEntity.setNativeValue(ratRefund.getString("localVal"));
				ratEntity.setAvgValue(ratRefund.getString("indVal"));
				ratEntity.setComparison(getComparison(
						ratRefund.getString("localVal"),
						ratRefund.getString("indVal")));
			} else {
				log.error("There is no ratRefund field in the json string.");
				log.error("Error info is: ");
				log.error("Json string is: " + json);
			}
			if (complaints != null) {
				comEntity.setNativeValue(complaints.getString("localVal"));
				comEntity.setAvgValue(complaints.getString("indVal"));
				comEntity.setComparison(getComparison(
						complaints.getString("localVal"),
						complaints.getString("indVal")));
			} else {
				log.error("There is no complaints field in the json string.");
				log.error("Error info is: ");
				log.error("Json string is: " + json);
			}
			if (punish != null) {
				punEntity.setNativeValue(punish.getString("localVal"));
				punEntity.setAvgValue(punish.getString("indVal"));
				punEntity.setComparison(getComparison(
						punish.getString("localVal"),
						punish.getString("indVal")));
			} else {
				log.error("There is no punish field in the json string.");
				log.error("Error info is: ");
				log.error("Json string is: " + json);
			}

			monthServieEntities.add(avgEntity);
			monthServieEntities.add(ratEntity);
			monthServieEntities.add(comEntity);
			monthServieEntities.add(punEntity);
		} else {
			log.error("Json str error.");
			log.error("Error info is: ");
			log.error("Json string is: " + json);
		}
	}

	public String getJsonString(String str) {
		int begin = str.indexOf("{");
		int end = str.lastIndexOf("}");

		log.info("Json string is: " + str);
		return str.substring(begin, end + 1);
	}

	public String getUserId() {
		String userId = null;

		if (doc.select("input#monthuserid").size() == 0) {
			log.info("There is no monthuserid in the page.");
		} else {
			Element monthuseridEle = doc.select("input#monthuserid").get(0);
			monthuserid = monthuseridEle.attr("value");
		}
		log.info("Monthuserid is: " + monthuserid);

		userId = monthuserid;
		return userId;
	}

	public void getFieldsFromPage() {
		if (doc.select("input#monthuserid").size() == 0) {
			log.info("There is no monthuserid in the page.");
		} else {
			Element monthuseridEle = doc.select("input#monthuserid").get(0);
			monthuserid = monthuseridEle.attr("value");
		}
		log.info("Monthuserid is: " + monthuserid);

		if (doc.select("input#userTag").size() == 0) {
			log.info("There is no user tag in the page.");
		} else {
			Element userTagEle = doc.select("input#userTag").get(0);
			userTag = userTagEle.attr("value");

		}
		log.info("UserTag is: " + userTag);

		if (doc.select("input#isB2C").size() == 0) {
			log.info("There is no B2C in the page.");
		} else {
			Element isB2CEle = doc.select("input#isB2C").get(0);
			isB2C = isB2CEle.attr("value");
		}
		log.info("IsB2C is: " + isB2C);
	}

	public String constructMonthServiceAjaxUrl() {
		String ajaxUrl = null;
		String baseUrl = "http://ratehis.taobao.com/monthServiceAjax.htm?";

		ajaxUrl = baseUrl + "monthuserid=" + monthuserid + "&userTag="
				+ userTag + "&isB2C=" + isB2C;

		log.info("Month service ajax url is: " + ajaxUrl);
		return ajaxUrl;
	}

	public void parseJson(String jsonStr) {
		JSONObject jsonObj = JSONObject.fromObject(jsonStr);
		log.info("tips is: " + jsonObj.getString("tips"));
		log.info("monthServiceData is: "
				+ jsonObj.getString("monthServiceData"));

		JSONArray array = jsonObj.getJSONArray("alldata");

		List alldata = (List) JSONSerializer.toJava(array);

		if (alldata.size() == 0) {
			log.info("Alldata list size is 0");
		} else {
			for (Object o : alldata) {
				JSONObject jo = JSONObject.fromObject(o);

				MonthServiceEntity entity = new MonthServiceEntity();
				entity.setNativeValue(jo.getString("native"));
				entity.setComparison(jo.getString("comparison"));
				entity.setAvgValue(jo.getString("avg"));

				log.info(entity.getLineString());
				monthServieEntities.add(entity);
			}
		}
	}
}
