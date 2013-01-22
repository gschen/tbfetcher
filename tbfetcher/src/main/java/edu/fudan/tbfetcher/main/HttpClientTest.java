package edu.fudan.tbfetcher.main;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class HttpClientTest {

	public static void main(String[] args) throws ClientProtocolException, IOException {
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		
		HttpContext localContext = new BasicHttpContext();
		
		HttpGet httpGet = new HttpGet("http://    ");
		
		HttpResponse response = httpClient.execute(httpGet, localContext);
		
		HttpHost targetHost = (HttpHost) localContext.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
		
		System.out.println("Final target is: "+targetHost);
		
		HttpEntity entity = response.getEntity();
		
		EntityUtils.consume(entity);
	}

}
