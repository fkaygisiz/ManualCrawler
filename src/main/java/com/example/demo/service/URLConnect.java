package com.example.demo.service;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

public class URLConnect {

	private URL url;
	public URLConnect(URL url) {
		this.url = url;
	}
	
	public Response execute() throws IOException {
		return Jsoup.connect(url.toExternalForm())
		        .followRedirects(true) //to follow redirects
		        .timeout(10000)
		        .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")//to get into pages preventing automated calls
		        .execute();
	}
		
}
