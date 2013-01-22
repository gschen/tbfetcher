package edu.fudan.tbfetcher.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.fudan.tbfetcher.pojo.CategoryInfo;


import org.jsoup.parser.Parser;

public class XmlConfUtil {

	private static final Logger log = Logger.getLogger(XmlConfUtil.class);
	private static final String PATH = "settings.xml";
	private static Document doc = null;

	public static void openXml() {
		String str = FileUtil.read(PATH);
		//由于jsoup类库在解析xml文件时，会把&le转义为<=，所以先替换，后再还原
		doc = Jsoup.parse(str.replace("&le", "&1le"), "utf-8", Parser.xmlParser());
	}

	public static String getValueByName(String name) {
		return doc.select(name).first().text();
	}

	public static List<CategoryInfo> getCategoryList() {

		List<CategoryInfo> categoryList = new ArrayList<CategoryInfo>();

		if (doc.select("categories").size() != 0) {
			Element categories = doc.select("categories").get(0);

			if (categories.select("category").size() != 0) {
				Elements es = categories.select("category");

				for (Element e : es) {
					CategoryInfo ci = new CategoryInfo();
					ci.setCategoryName(e.select("name").get(0).ownText());
					ci.setCategoryHref(e.select("url").get(0).text().replaceAll("&1le", "&le"));
					categoryList.add(ci);
				}
			}
		}
		log.info("Read category info from settings.xml files:");
		for(CategoryInfo ci : categoryList){
			
			log.info("Category name: "+ ci.getCategoryName());
			log.info("Category url: "+ci.getCategoryHref());
		}

		return categoryList;
	}
}
