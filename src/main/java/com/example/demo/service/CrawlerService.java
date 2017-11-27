package com.example.demo.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.model.ChildCallResult;
import com.example.demo.model.MainCallResult;
import com.google.common.net.InternetDomainName;

@Service
public class CrawlerService {

	private static final Logger log = LoggerFactory.getLogger(CrawlerService.class);

	private static ExecutorService threadPool = Executors.newFixedThreadPool(50);

	public MainCallResult crawl(URL url) {
		try {
			MainCallResult callResult = new MainCallResult(url);
			findInnerAnchorsAndGetTheirAccessStatuses(callResult);
			return callResult;
		} catch (InterruptedException e) {
			log.error("Interrupted Exception: " + e.getMessage(), e);
			Thread.currentThread().interrupt();
			MainCallResult cr = new MainCallResult();
			cr.setException("Interrupted Exception: : " + e.getMessage());
			return cr;
		}
	}

	private void findInnerAnchorsAndGetTheirAccessStatuses(MainCallResult mainCallResult) throws InterruptedException {
		Document doc = mainCallResult.getDocument();
		if (doc == null) {
			return;
		}
		List<LinkCaller> innerLinkTasks = new ArrayList<>();
		Set<String> anchors = getAnchorsFromDocument(doc);
		for (String hrefLink : anchors) {
			log.debug("hrefLink: ", hrefLink);
			try {
				URL url2 = new URL(mainCallResult.getUrl(), hrefLink);
				innerLinkTasks.add(new LinkCaller(url2));
			} catch (MalformedURLException e1) {
				log.error("MalformedURLExceptionn occured for URL " + hrefLink);
				log.error(e1.getMessage(), e1);
			}
		}
		threadPool.invokeAll(innerLinkTasks);
		for (LinkCaller call : innerLinkTasks) {
			mainCallResult.addChild(getInnerCallResult(call, mainCallResult.getUrl()));
		}
	}

	private Set<String> getAnchorsFromDocument(Document doc) {
		return doc.getElementsByTag("a").stream().filter((e) -> {
			String href = e.attr("href");
			return href.startsWith("/") || href.toLowerCase().startsWith("http://")
					|| href.toLowerCase().startsWith("https://");
		}).map(e -> e.attr("href")).collect(Collectors.toSet());
	}

	private ChildCallResult getInnerCallResult(LinkCaller call, URL parentUrl) {
		try {
			ChildCallResult callResult = call.call();
			callResult.setInternalLink(isSameDomain(parentUrl, call.getUrl()));
			return callResult;
		} catch (Exception e) {
			log.error("Exception occured in callig URL " + call.getUrl(), e);
			ChildCallResult interiorCall = new ChildCallResult();

			interiorCall.setException(e.getMessage());
			interiorCall.setUrl(call.getUrl());
			interiorCall.setInternalLink(isSameDomain(parentUrl, call.getUrl()));

			return interiorCall;
		}
	}

	private boolean isSameDomain(URL parentURL, URL childURL) {
		InternetDomainName childDomain = InternetDomainName.from(childURL.getHost()).topPrivateDomain();
		InternetDomainName parentDomain = InternetDomainName.from(parentURL.getHost()).topPrivateDomain();
		return childDomain.equals(parentDomain);
	}

}
