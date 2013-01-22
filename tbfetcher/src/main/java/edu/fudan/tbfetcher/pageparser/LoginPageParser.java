package edu.fudan.tbfetcher.pageparser;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class LoginPageParser extends BasePageParser {

	private final static Logger log = Logger.getLogger(LoginPageParser.class); 
//	@Override
//	public boolean parsePage() {
//		return false;
//	}
//
//	public LoginPageParser(HttpClient httpClient, String pageUrl) {
//		super(httpClient, pageUrl);
//		// TODO Auto-generated constructor stub
//	}

	/**
	 * 
	 * loginPage页面根据指定的formId获得表单字段并构造map
	 * 
	 * @param response
	 * @param formId
	 * @param formFieldMap
	 */
	public Map<String, String> getFormFieldsMap(String loginPageUrl,
			String formId) {
		Map<String, String> formFieldMap = new HashMap<String, String>();

		try {
//			this.getPage(loginPageUrl);
			Elements eles = this.getDoc().select("form#" + formId + " input");
			log.info("The size of input tag in the specified form is:"
					+ eles.size());
			log.info("The details of the input tag [name] [value] is:");
			for (Element e : eles) {
				formFieldMap.put(
						e.attr("name") == "" ? e.attr("id") : e.attr("name"),
						e.attr("value"));
				log.info("[id]" + e.attr("id") + "			[name]" + e.attr("name")
						+ "				[value]" + e.attr("value"));
			}
		} catch (IllegalStateException e1) {
			e1.printStackTrace();
		}
		return formFieldMap;
	}

}
