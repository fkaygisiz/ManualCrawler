package com.example.demo.service;

import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.TestHelper;
import com.example.demo.model.ChildCallResult;

@RunWith(PowerMockRunner.class)
@SpringBootTest
@PowerMockRunnerDelegate(SpringRunner.class)
public class CrawlerServiceTest {

	@Autowired
	private CrawlerService crawlerService;

	@Test
	public void shouldGetTrueFromdoesDocHaveFormContainingLoginActionWhenNoFormContainsLoginAction() throws Exception {
		Document docWithAnchors = TestHelper.getDocFromHtmlFile("com/example/model/with_anchors.html");
		Set<String> anchors = Whitebox.invokeMethod(crawlerService, "getAnchorsFromDocument", docWithAnchors);
		assertTrue(anchors.size() == 3);
	}

	@Test
	public void shouldGet3InternalLinksFromisSameDomain() throws Exception {
		Set<String> anchors = new HashSet<String>() {
			{
				add("/1");
				add("/2");
				add("http://example.com/adasdd");
				add("http://facebook.com");
			}
		};
		URL parentURL = new URL("http://example.com");
		int sameDomainCounter = 0;
		for (String anchor : anchors) {
			URL url = new URL(parentURL, anchor);
			Boolean isSame = Whitebox.invokeMethod(crawlerService, "isSameDomain", parentURL, url);
			if (isSame) {
				sameDomainCounter++;
			}
		}
		assertTrue(sameDomainCounter == 3);

	}

	@Test
	public void shouldgetInnerCallResult() throws Exception {

		URL parentURL = new URL("http://example.com");
		LinkCaller linkCaller = new LinkCaller(new URL("http://facebook.com"));
		ChildCallResult childCallResult = Whitebox.invokeMethod(crawlerService, "getInnerCallResult", linkCaller,
				parentURL);
		assertTrue(!childCallResult.isInternalLink());

		URL parentURL2 = new URL("https://github.com/login");
		LinkCaller linkCaller2 = new LinkCaller(new URL("https://github.com/"));
		ChildCallResult childCallResult2 = Whitebox.invokeMethod(crawlerService, "getInnerCallResult", linkCaller2,
				parentURL2);
		assertTrue(childCallResult2.isInternalLink());
		assertTrue(childCallResult2.isAccessible());
	}

	@Test
	public void shouldGetAccessibleFalseFromgetInnerCallResultWhenNonexistentURLGiven() throws Exception {

		URL parentURL = new URL("http://example.com");
		LinkCaller linkCaller = new LinkCaller(new URL("http://askdaksdklhdhklashdahskdhkkaslksakkdsaakdkladskshkhasdd.com"));
		ChildCallResult childCallResult = Whitebox.invokeMethod(crawlerService, "getInnerCallResult", linkCaller,
				parentURL);
		assertTrue(!childCallResult.isAccessible());
	}
}
