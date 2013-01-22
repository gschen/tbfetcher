package edu.fudan.tbfetcher.utils;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import edu.fudan.tbfetcher.constant.SystemConstant;

public class TBBrowser {
	private static final Logger log = Logger.getLogger(TBBrowser.class);
	private static final int RETRY_TIME = 10; // 一个httpget失败后，重试次数，默认10次
	private static final int RETRY_INTERVAL = 1000; // 重试的间隔时间，默认1000ms
	private static DefaultHttpClient httpClient; // Httpclient 客户端
	private static String rawText; // 用于存储一次请求返回的纯文本
	private static Document doc; // 用于存储一次请求返回的DOM结构
	
	public static DefaultHttpClient getHttpClient() {
		return httpClient;
	}

	public static String getRawText() {
		return rawText;
	}

	public static Document getDoc() {
		return doc;
	}

	/**
	 * 初始化httpClient
	 * */
	public static void init() {
		if (null == httpClient) {
			final HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					SystemConstant.CONNECTION_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams,
					SystemConstant.SO_TIMEOUT);
			HttpConnectionParams.setTcpNoDelay(httpParams, true);
			HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);
			// httpParams.setBooleanParameter(ClientPNames.HANDLE_REDIRECTS,
			// false); //禁止自动重定向
			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory
					.getSocketFactory()));
			schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory
					.getSocketFactory()));
			PoolingClientConnectionManager cm = new PoolingClientConnectionManager(
					schemeRegistry);
			cm.setMaxTotal(2000);
			cm.setDefaultMaxPerRoute(200);
			httpClient = new DefaultHttpClient(cm, httpParams);
		}
	}

	public static boolean get(String url) {
		if (url.equals(null) || url.equals("")) {
			log.error("Http get request url is null.");
		} else {
			return get(url, null);
		}
		return false;
	}

	/**
	 * 负责发送一次请求,返回true则请求成功，通过调用getRawText()获取返回的纯文本，调用getDoc()获取返回的DOM
	 * 在这个方法中，我们会做足够多次的尝试（10次），如果请求失败，则调用程序应该跳过这个请求URL 调用该方法的之前，并确保传入参数的合法性
	 * */
	public static boolean get(String url, List<NameValuePair> headers) {
		/** 初始化为空 */
		rawText = null;
		doc = null;

		if (null == url) {
			log.error("Request url is null!");
			return false;
		}
		log.info("The request Url is: " + url);
		HttpGet httpget = new HttpGet(url);
		if (null != headers && headers.size() > 0) {
			for (NameValuePair nvp : headers) {
				httpget.setHeader(nvp.getName(), nvp.getValue());
			}
		}
		/** 伪装浏览器 */
		httpget.setHeader("User-Agent",
				"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");

		/** 最多重试10次，来发送请求 */
		int cnt = 0;
		boolean result = false;
		do {
			if (cnt > 0) {
				try {
					int time = RETRY_INTERVAL * cnt;
					log.error("Waiting for " + time + "ms.....");
					Thread.sleep(time);
				} catch (InterruptedException e) {
					log.error("Exception: ", e);
				}
			}
			result = tryToSendGetRequest(httpget);
			cnt++;
		} while (!result && cnt <= RETRY_TIME);

		if (cnt <= RETRY_TIME) {
			return true;
		} else {
			log.error("Get response failed after retrying" + RETRY_TIME
					+ " times. URL: " + url);
		}
		return false;
	}

	/**
	 * 试图去发送一次http 请求，如果最终成功获取，则返回true，如果因为任何原因，没有成功，则返回false
	 * */
	private static boolean tryToSendGetRequest(HttpGet httpget) {
		if (null == httpget) {
			log.error("Http get request is null.");
			return false;
		}
		try {
			HttpResponse response = httpClient.execute(httpget);
			HttpEntity entity = null;
			StatusLine status = response.getStatusLine();
			int statusCode = status.getStatusCode();
			if (HttpStatus.SC_OK != statusCode) {
				log.error("Get response header failed. Reason is: Status Code is not OK!");
			} else {
				entity = response.getEntity();
				if (null != entity) {
					rawText = EntityUtils.toString(entity, "GBK");
					if (null != rawText) {
						doc = Jsoup.parse(rawText);
						if(null == doc){
							return false;
						}
						if (doc.select("div.notice").size() > 0 && doc.select("div.safe").size() > 0) {
							log.error("The page has been redirected because of Taobao's safety refuse !");
							Thread.sleep(2 * 60 * 1000);
							return false;
						}
						return true;
					}
				}
			}
			EntityUtils.consume(entity);
		} catch (Exception e) { // 这里捕获所有可能的异常，统一处理
			log.error("Exception happened[doGet: exception], url ["
					+ httpget.getURI() + "]");
			log.error("Exception: ", e);
			httpget.abort();
		} finally {
			httpget.reset();
		}
		return false;
	}

	/**
	 * 释放掉资源
	 * */
	public static void shutdown() {
		if (null != httpClient) {
			httpClient.getConnectionManager().shutdown();
			httpClient = null;
		}
	}

}
