package com.example.demo.model;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;

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
		this.url = url;

		try {
			this.response = new URLConnect(url).execute();

			this.accessible = true;
			this.httpCode = response.statusCode();

		} catch (UnknownHostException uhe) {
			log.error("UnknownHost Exception: " + uhe.getMessage(), uhe);
			this.exception = "UnknownHost";
			this.httpCode = 500;
		} catch (IOException e) {
			log.error("IO Exception: " + e.getMessage(), e);
			this.exception = e.getMessage();
			this.httpCode = 500;
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
