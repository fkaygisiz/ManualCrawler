package com.example.demo.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.model.ChildCallResult;
import com.example.demo.model.MainCallResult;
import com.google.common.net.InternetDomainName;

@Service
public class CrawlerService {

	private static final Logger log = LoggerFactory.getLogger(CrawlerService.class);

	private static ExecutorService threadPool = Executors.newFixedThreadPool(30);

	public MainCallResult crawl(URL url) {
		try {
			//Response response = new URLConnect(url).execute();
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

	private void findInnerAnchorsAndGetTheirAccessStatuses(MainCallResult callResult) throws InterruptedException {
		Document doc = callResult.getDocument();
		if(doc == null) {
			return;
		}
		List<LinkCaller> innerLinkTasks = new ArrayList<>();
		Set<String> anchors = doc.getElementsByTag("a").stream().filter((e) -> {
			String href = e.attr("href");
			return href.startsWith("/") || href.toLowerCase().startsWith("http://")
					|| href.toLowerCase().startsWith("https://");
		}).map(e->e.attr("href")).collect(Collectors.toSet());
		for (String hrefLink : anchors) {
			log.debug("hrefLink: ", hrefLink);
			try {
				URL url2 = new URL(callResult.getUrl(), hrefLink);
				innerLinkTasks.add(new LinkCaller(url2, true));
			} catch (MalformedURLException e1) {
				log.error("MalformedURLExceptionn occured for URL " + hrefLink);
				log.error(e1.getMessage(), e1);
			}
		}
		threadPool.invokeAll(innerLinkTasks);
		for (LinkCaller call : innerLinkTasks) {
			callResult.getChildren().add(getInnerCallResult(call, callResult.getUrl()));
		}
	}

	private ChildCallResult getInnerCallResult(LinkCaller call, URL parentUrl) {
		try {
			ChildCallResult callResult = call.call();
			InternetDomainName childDomain = InternetDomainName.from(call.getUrl().getHost()).topPrivateDomain();
			InternetDomainName parentDomain = InternetDomainName.from(parentUrl.getHost()).topPrivateDomain();
			callResult.setInternalLink(childDomain.equals(parentDomain));
			return callResult;
		} catch (Exception e) {
			log.error("Exception occured in callig URL " + call.getUrl(), e);
			MainCallResult interiorCall = new MainCallResult();

			interiorCall.setException(e.getMessage());
			interiorCall.setUrl(call.getUrl());
			InternetDomainName childDomain = InternetDomainName.from(call.getUrl().getHost()).topPrivateDomain();
			InternetDomainName parentDomain = InternetDomainName.from(parentUrl.getHost()).topPrivateDomain();
			interiorCall.setInternalLink(childDomain.equals(parentDomain));

			return interiorCall;
		}
	}

}
