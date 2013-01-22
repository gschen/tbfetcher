package edu.fudan.tbfetcher.pageparser;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;

import edu.fudan.tbfetcher.utils.TBBrowser;

/**
 * Steps to parse page is as following; 1. get the specified page; 2.If get page successfully, 
 * then parse the page and put the specified data to the collection; 
 *
 * The derived class must to override the parsePage method in order to get the
 * specified data.
 * 
 * @author JustinChen
 * 
 */
public class BasePageParser implements PageParser {
	private static final Logger log = Logger.getLogger(BasePageParser.class);
	
	protected String pageUrl;    //页面的url   
	protected Document doc;      //请求返回的页面的DOM结构
	protected String rawText;    //请求返回的页面的纯文本
	
	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public String getRawText() {
		return rawText;
	}

	public void setRawText(String rawText) {
		this.rawText = rawText;
	}

	/**
	 * initialize the field
	 * */
	public void init(){
		setPageUrl(null);
		setRawText(null);
		setDoc(null);
	}
	
	@Override
	public boolean getPage() {
		if(null == pageUrl || 0 == pageUrl.length()){
			log.error("Url is null when get page.");
			return false;
		}
		boolean result = TBBrowser.get(pageUrl, null);
		if(result){
			Document doc = TBBrowser.getDoc();
			String rawText = TBBrowser.getRawText();
			
			if(null != doc && null != rawText && rawText.length() > 0){
				setDoc(TBBrowser.getDoc());
				setRawText(TBBrowser.getRawText());
				log.info("Get the page successfully, url: " + pageUrl);
				return true;
			}
		}
		log.error("Get the page failed, url: " + pageUrl);
		return false;
	}

	@Override
	public void parsePage() {}
}
