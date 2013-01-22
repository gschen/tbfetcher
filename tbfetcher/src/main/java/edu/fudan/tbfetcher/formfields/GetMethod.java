package edu.fudan.tbfetcher.formfields;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.log4j.Logger;


/**
 * 
 * 负责构造一次get请求
 * 
 * @author JustinChen
 * 
 */
public class GetMethod {
	private static final Logger log = Logger.getLogger(GetMethod.class);
	private HttpClient httpclient;
	private String getUrl;
	private HttpResponse response;
	private HttpGet httpget;
	private String responseStr;

	public void setGetUrl(String getUrl) {
		this.getUrl = getUrl;
	}
	
	public String getGetUrl() {
		return getUrl;
	}

	public HttpResponse getResponse() {
		return response;
	}

	public GetMethod(HttpClient httpClient, String getUrl) {
		if (httpClient == null || getUrl.equals(null)) {
			System.out.println("ERROR: httpclient is null. - " + GetMethod.class);
		}
		this.httpclient = httpClient;
		this.getUrl = getUrl;
	}

	public boolean doGet(List<NameValuePair> headers){
		log.info("GetUrl is: " + getUrl);
		httpget = new HttpGet(this.getUrl);
		if(null != headers){
			for(NameValuePair nvp : headers){
				httpget.setHeader(nvp.getName(), nvp.getValue());
			}
		}
		httpget.setHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.92 Safari/537.4");
		try {
			response = httpclient.execute(httpget);
			StatusLine status = response.getStatusLine();
			if(HttpStatus.SC_OK != status.getStatusCode()){
				log.info("get response header failed.");
				return false;
			}
			log.info("get response header success.");
			return setResponseString();
		} catch (ClientProtocolException e) {
			//如果是i.taobao.com中的非法用户访问，则直接返回
			if(getUrl.contains("http://i.taobao.com")){
				return true;
			}
			log.error("Exception happened[doGet:Client protocol exception], url [" + getUrl + "]");
			log.error(e.getMessage());
			httpget.abort();      
		} catch (IOException e) {
			log.error("Exception happened[doGet:IO exception], url [" + getUrl + "]");
			log.error(e.getMessage());
			httpget.abort();
		} catch (Exception e) {
			// TODO: handle exception
			log.error("Exception happened[doGet: exception], url [" + getUrl + "]");
			log.error(e.getMessage());
			log.error(e.getStackTrace());
			httpget.abort();
		} finally{
			shutDown();
		}
		return false;
	}
	
	public boolean doGet() {
		return doGet(null);
	}

	public void write2File() {

	}

	// java不支持函数参数默认值这种做法，所以我们只有通过函数重载来解决这个问题
	public void printResponse() {
		printResponse("utf-8");
	}

	public boolean setResponseString(){
		if(null == response){
			return false;
		}
		HttpEntity entity = response.getEntity();
		log.info("entity length: " + entity.getContentLength());
		try {
			if(null != entity){
				byte[] contentBuf = EntityUtils.toByteArray(entity);
				this.responseStr = new String(contentBuf, "GBK");
				return true;
			}
		} catch (Exception e) {
			httpget.abort();
			log.error("Exception happened [Set Response String], url [" + getUrl + "]");
			log.error(e.getMessage());
			log.error(e.getStackTrace());
		}
		return false;
	}
	
	public String getResponseAsString() {
		return this.responseStr;
	}

	public void printResponse(String charset) {

		if (this.getResponse().getEntity() == null) {
			log.error("The entity of the response is null.");
		} else {
			try {
				System.out.println(EntityUtils.toString(
						this.response.getEntity(), charset));
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void shutDown() {
		if(response == null){
			return ;
		}
		try {
			// Ensures that the entity content is fully consumed and the content
			// stream, if exists, is closed.
			if(response.getEntity() == null)
				return ;
			EntityUtils.consume(this.response.getEntity());
			// release connection
			httpget.releaseConnection();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
