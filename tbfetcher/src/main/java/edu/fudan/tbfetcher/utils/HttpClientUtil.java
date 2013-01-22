package edu.fudan.tbfetcher.utils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.xml.DOMConfigurator;

public class HttpClientUtil {

	private static HttpClient httpClient;
	private static HttpContext context;

	public static void init() {

		if (httpClient == null) {
			try {
				httpClient = createHttpClient();
			} catch (KeyManagementException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}

		if (context == null) {
			context = new BasicHttpContext();
		}
	}

	private static HttpClient createHttpClient()
			throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext sslContext = SSLContext.getInstance("SSL");
		// TrustManager[] trustAllCerts = new TrustManager[] { new
		// TrustAllTrustManager() };
		// sslContext.init(null, trustAllCerts, new
		// java.security.SecureRandom());

		// SSLSocketFactory sslSocketFactory = new SSLSocketFactory(sslContext);
		// SchemeRegistry schemeRegistry = new SchemeRegistry();
		// schemeRegistry.register(new Scheme("https", 443, sslSocketFactory));
		// schemeRegistry
		// .register(new Scheme("http", 80, new PlainSocketFactory()));

		HttpParams params = new BasicHttpParams();
		// ClientConnectionManager cm = new
		// org.apache.http.impl.conn.SingleClientConnManager(
		// schemeRegistry);

		// some pages require a user agent
		AbstractHttpClient httpClient = new DefaultHttpClient(params);
		HttpProtocolParams
				.setUserAgent(
						httpClient.getParams(),
						"Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:13.0) Gecko/20100101 Firefox/13.0.1");

		// httpClient.setRedirectStrategy(new RedirectStrategy());

		httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
			@Override
			public void process(HttpResponse response, HttpContext context)
					throws HttpException, IOException {
				if (response.containsHeader("Location")) {
					Header[] locations = response.getHeaders("Location");
					if (locations.length > 0)
						context.setAttribute(LAST_REDIRECT_URL,
								locations[0].getValue());
				}
			}
		});

		return httpClient;
	}

	public static void test() {

		try {
			HttpClient httpClient = createHttpClient();
			HttpContext context = new BasicHttpContext();
			HttpGet httpGet = new HttpGet(
					"http://detail.tmall.com/item.htm?id=21355564170&ad_id=&am_id=&cm_id=&pm_id=1500206164949e8479ce");
			try {
				HttpResponse response = httpClient.execute(httpGet, context);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String url = getUrlAfterRedirects(context);
			System.out.println(url);
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	// 如果get请经过多次redirect,获取get请求中最后一个url
	public static String getTargetUrl(String url) {

		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = httpClient.execute(httpGet, context);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String targetUrl = getUrlAfterRedirects(context);
		return targetUrl;
	}

	private static String getUrlAfterRedirects(HttpContext context) {
		String lastRedirectUrl = (String) context
				.getAttribute(LAST_REDIRECT_URL);
		if (lastRedirectUrl != null)
			return lastRedirectUrl;
		else {
			HttpUriRequest currentReq = (HttpUriRequest) context
					.getAttribute(ExecutionContext.HTTP_REQUEST);
			HttpHost currentHost = (HttpHost) context
					.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
			String currentUrl = (currentReq.getURI().isAbsolute()) ? currentReq
					.getURI().toString() : (currentHost.toURI() + currentReq
					.getURI());
			return currentUrl;
		}
	}

	public static final String LAST_REDIRECT_URL = "last_redirect_url";

	public static void main(String[] args) {
		DOMConfigurator.configure("log4j.xml");
		HttpClientUtil.test();
	}

}
