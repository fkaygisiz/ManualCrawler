package com.example.demo.service;

import java.net.URL;
import java.util.concurrent.Callable;

import com.example.demo.model.ChildCallResult;
import com.example.demo.model.MainCallResult;

public class LinkCaller implements Callable<ChildCallResult> {

	private URL url;
	private boolean isChildLink;

	public LinkCaller(URL url, boolean isChildLink) {
		this.url = url;
		this.isChildLink = isChildLink;
	}

	@Override
	public ChildCallResult call() throws Exception {
		
		if(isChildLink) {
			return new ChildCallResult(url);
		}else {
			return new MainCallResult(url);
		}
	}

	public URL getUrl() {
		return url;
	}

}
