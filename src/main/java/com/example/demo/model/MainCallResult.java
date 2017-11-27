package com.example.demo.model;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MainCallResult extends CallResult {

	private static final Logger log = LoggerFactory.getLogger(MainCallResult.class);

	private boolean hasLogin;

	private String title;
	private int headLevel1;
	private int headLevel2;
	private int headLevel3;
	private int headLevel4;
	private int headLevel5;
	private int headLevel6;

	private List<ChildCallResult> internalLinks = new ArrayList<>();
	private List<ChildCallResult> externalLinks = new ArrayList<>();

	@JsonIgnore
	private Document document;

	private String documentType;

	public MainCallResult() {
		super();
	}

	public MainCallResult(URL url) {
		super(url);
		try {
			if(this.response == null) {
				throw new IOException("Unable To Connect");
			}
			this.document = this.response.parse();
			this.documentType = getHtmlVersion(this.document);
			if (!StringUtils.isEmpty(this.document.title())) {
				this.title = this.document.title();
			}
			this.hasLogin = doesPageHaveALogin(this.document);
			processHeaderCounts(this.document);
		} catch (IOException e) {
			log.error("IO Exception: " + e.getMessage(), e);
			this.exception = e.getMessage();
			this.httpCode = 500;
		}
	}

	private String getHtmlVersion(Document doc) {
		List<Node>nods = doc.childNodes();
        for (Node node : nods) {
           if (node instanceof DocumentType) {
               return getHTMLTypeFromDocumentType(node);
                 
           }
       }
        return "HTML";
	}

	private String getHTMLTypeFromDocumentType(Node node) {
		DocumentType documentType = (DocumentType)node;
		   String publicId = documentType.attr("publicid").replaceAll(" ", "").toLowerCase();
		     if(documentType.toString().replaceAll(" ", "").equalsIgnoreCase("<!DOCTYPEhtml>")) {//https://www.w3.org/QA/2002/04/valid-dtd-list.html and https://www.w3schools.com/html/html_intro.asp
		    	 return "HTML 5";
		     }else if(publicId.contains("xhtml1.0")) {
		    	 return "XHTML 1.0";
		     }else if(publicId.contains("xhtml1.1")) {
		    	 return "XHTML 1.1";
		     }else if(publicId.contains("xhtmlbasic1.0")) {
		    	 return "XHTML Basic 1.0";
		     }else if(publicId.contains("4.01")) {
		    	 return "HTML 4";
		     }else if(publicId.contains("3.2")) {
		    	 return "HTML 3.2";
		     }else if(publicId.contains("2.0")) {
		    	 return "HTML 2.0";
		     }else {
		    	 return "HTML";
		     }
	}

	private void processHeaderCounts(Document doc) {
		this.headLevel1 = doc.getElementsByTag("H1").size();
		this.headLevel2 = doc.getElementsByTag("H2").size();
		this.headLevel3 = doc.getElementsByTag("H3").size();
		this.headLevel4 = doc.getElementsByTag("H4").size();
		this.headLevel5 = doc.getElementsByTag("H5").size();
		this.headLevel6 = doc.getElementsByTag("H6").size();

	}

	private boolean doesPageHaveALogin(Document doc) {
		return doesTitleIndicateLoginPage(doc.title()) || doesDocHaveFormContainingLoginAction(doc)
				|| doesDocHavePasswordField(doc);
	}

	private boolean doesDocHavePasswordField(Document doc) {
		return !doc.getElementsByAttribute("type").stream().filter(e ->"password".equalsIgnoreCase(e.attr("type"))).collect(Collectors.toList()).isEmpty() ;
	}

	private boolean doesDocHaveFormContainingLoginAction(Document doc) {
		return doc.getElementsByAttribute("action").stream()
				.filter(e -> (e.attr("action").toLowerCase().contains("login")
						|| e.attr("action").toLowerCase().contains("signin")))
				.findAny().isPresent();
	}

	private boolean doesTitleIndicateLoginPage(String title) {
		if (!StringUtils.isEmpty(title) && title.toLowerCase().contains("login")) {
			return true;
		}
		return false;
	}

	public boolean isHasLogin() {
		return hasLogin;
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

	public Document getDocument() {
		return document;
	}

	public void addChild(ChildCallResult childCallResult) {
		if (childCallResult.isInternalLink()) {
			internalLinks.add(childCallResult);
		} else {
			externalLinks.add(childCallResult);
		}

	}

	public List<ChildCallResult> getInternalLinks() {
		return internalLinks;
	}

	public List<ChildCallResult> getExternalLinks() {
		return externalLinks;
	}

	public String getDocumentType() {
		return documentType;
	}

}
