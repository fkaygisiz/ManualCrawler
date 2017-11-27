package com.example.demo.service;

import java.net.URL;
import java.util.concurrent.Callable;

import com.example.demo.model.ChildCallResult;

public class LinkCaller implements Callable<ChildCallResult> {

	private URL url;

	public LinkCaller(URL url) {
		this.url = url;
	}

	@Override
	public ChildCallResult call() throws Exception {
		return new ChildCallResult(url);
	}

	public URL getUrl() {
		return url;
	}

}
