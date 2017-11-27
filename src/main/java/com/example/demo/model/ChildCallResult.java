package com.example.demo.model;

import java.net.URL;

public class ChildCallResult extends CallResult {

	private boolean internalLink;

	
	public ChildCallResult(URL url) {
		super(url);
	}

	public ChildCallResult() {
		super();
	}
	
	public void setInternalLink(boolean internalLink) {
		this.internalLink = internalLink;
	}

	public boolean isInternalLink() {
		return internalLink;
	}


}
