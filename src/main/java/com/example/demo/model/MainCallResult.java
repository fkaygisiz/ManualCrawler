package com.example.demo.model;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MainCallResult extends ChildCallResult {

	private static final Logger log = LoggerFactory.getLogger(MainCallResult.class);

	private boolean hasLogin;

	private String title;
	private int headLevel1, headLevel2, headLevel3, headLevel4, headLevel5, headLevel6;

	private List<ChildCallResult> children = new ArrayList<>();

	@JsonIgnore
	private Document document;

	public MainCallResult() {
		super();
	}

	public MainCallResult(URL url) {
		super(url);
		try {
			this.document = this.response.parse();
			if (!StringUtils.isEmpty(this.document.title())) {
				this.title = this.document.title();
			}
			this.hasLogin = doesPageHaveALogin(this.document);
		} catch (IOException e) {
			log.error("IO Exception: " + e.getMessage(), e);
			this.exception = e.getMessage();
			this.httpCode = 500;
		}
	}

	private boolean doesPageHaveALogin(Document doc) {
		return doesTitleIndicateLoginPage(doc.title()) || doesDocHaveFormContainingLoginAction(doc)
				|| doesDocHavePasswordField(doc);
	}

	private boolean doesDocHavePasswordField(Document doc) {
		return !CollectionUtils.isEmpty(doc.getElementsByAttribute("password"));
	}

	private boolean doesDocHaveFormContainingLoginAction(Document doc) {
		return doc.getElementsByAttribute("action").stream()
				.filter(e -> e.attr("action").toLowerCase().contains("login")).findAny().isPresent();
	}

	private boolean doesTitleIndicateLoginPage(String title) {
		if (!StringUtils.isEmpty(title) && title.toLowerCase().contains("login")) {
			return true;
		}
		return false;
	}

	public boolean hasLogin() {
		return hasLogin;
	}

	public boolean isAccessible() {
		return accessible;
	}

	public int getHttpCode() {
		return httpCode;
	}

	public String getTitle() {
		return title;
	}

	public int getHeadLevel1() {
		return headLevel1;
	}

	public int getHeadLevel2() {
		return headLevel2;
	}

	public int getHeadLevel3() {
		return headLevel3;
	}

	public int getHeadLevel4() {
		return headLevel4;
	}

	public int getHeadLevel5() {
		return headLevel5;
	}

	public int getHeadLevel6() {
		return headLevel6;
	}

	public static void main(String[] args) throws IOException {
		String urlStr = "https://www.spiegel.de/meinspiegel/login.html";
		// String url = "http://fatihkaygisiz.com";
		URL url = new URL(urlStr);
		MainCallResult cr = new MainCallResult(url);
		System.out.println(cr.hasLogin());
	}
	public Document getDocument() {
		return document;
	}


	public List<ChildCallResult> getChildren() {
		return children;
	}


}
