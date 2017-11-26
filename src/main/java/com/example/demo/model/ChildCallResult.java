package com.example.demo.model;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Calendar;

import org.jsoup.Connection.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.service.URLConnect;

public class ChildCallResult {

	private static final Logger log = LoggerFactory.getLogger(ChildCallResult.class);

	protected boolean accessible;
	protected boolean internalLink;

	protected String exception;
	protected int httpCode;

	protected URL url;

	protected Response response;

	public ChildCallResult(URL url) {
		Long startTime = Calendar.getInstance().getTimeInMillis();
		this.url = url;
		try {
			this.response = new URLConnect(url).execute();

			this.accessible = true;
			this.httpCode = response.statusCode();

		} catch (UnknownHostException uhe) {
			log.error("UnknownHost Exception for URL " + url + " , " + uhe.getMessage(), uhe);
			this.exception = "UnknownHost";
			this.httpCode = 500;
		} catch (IOException e) {
			log.error("IO Exception for URL " + url + " , " + e.getMessage(), e);
			this.exception = e.getMessage();
			this.httpCode = 500;
		} finally {
			log.error("Time took for URL {} , elapsed time {}", url, (Calendar.getInstance().getTimeInMillis() - startTime));
		}
		if(url.toExternalForm().equals("https://www.google.com.tr/intl/tr/options/")) {
			System.out.println("https://www.google.com.tr/intl/tr/options/");
		}
	}

	public ChildCallResult() {
		// TODO Auto-generated constructor stub
	}

	public void setInternalLink(boolean internalLink) {
		this.internalLink = internalLink;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public boolean isInternalLink() {
		return internalLink;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

}
