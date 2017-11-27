package com.example.demo.model;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.powermock.reflect.Whitebox;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.TestHelper;
import com.example.demo.service.LinkCaller;

/*
@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest(MainCallResult.class)*/
@RunWith(PowerMockRunner.class)
@SpringBootTest
@PowerMockRunnerDelegate(SpringRunner.class)
public class MainCallResultTest {

	private static Document docWithHeaders;
	private static Document docWithPassword;
	private static Document docWithLoginAction;
	private static Document docWithSignInAction;

	@BeforeClass
	public static void initDoc() throws URISyntaxException, IOException {
		docWithHeaders = TestHelper.getDocFromHtmlFile("com/example/model/with_headers.html");
		docWithPassword = TestHelper.getDocFromHtmlFile("com/example/model/with_password.html");
		docWithLoginAction = TestHelper.getDocFromHtmlFile("com/example/model/with_login_action.html");
		docWithSignInAction = TestHelper.getDocFromHtmlFile("com/example/model/with_signin_action.html");

	}

	@Test
	public void shouldGetFalseFromdoesDocHavePasswordFieldWhenHtmlContainsNoPasswordFiled() throws Exception {
		MainCallResult mainCallResult = new MainCallResult();
		Boolean doesDocHavePasswordField = Whitebox.invokeMethod(mainCallResult, "doesDocHavePasswordField",
				docWithHeaders);
		assertTrue(!doesDocHavePasswordField);
	}

	@Test
	public void shouldGetTrueFromdoesDocHavePasswordFieldWhenHtmlContainsPasswordFiled() throws Exception {
		MainCallResult mainCallResult = new MainCallResult();
		Boolean doesDocHavePasswordField = Whitebox.invokeMethod(mainCallResult, "doesDocHavePasswordField",
				docWithPassword);
		assertTrue(doesDocHavePasswordField);
	}

	@Test
	public void shouldGetFalseFromdoesDocHaveFormContainingLoginActionWhenNoFormExistsinHtml() throws Exception {
		MainCallResult mainCallResult = new MainCallResult();
		Boolean doesDocHaveFormContainingLoginAction = Whitebox.invokeMethod(mainCallResult,
				"doesDocHaveFormContainingLoginAction", docWithHeaders);
		assertTrue(!doesDocHaveFormContainingLoginAction);
	}

	@Test
	public void shouldGetTrueFromdoesDocHaveFormContainingLoginActionWhenNoFormContainsLoginAction() throws Exception {
		MainCallResult mainCallResult = new MainCallResult();
		Boolean doesDocHaveFormContainingLoginAction = Whitebox.invokeMethod(mainCallResult,
				"doesDocHaveFormContainingLoginAction", docWithLoginAction);
		assertTrue(doesDocHaveFormContainingLoginAction);
	}

	@Test
	public void shouldGetTrueFromdoesDocHaveFormContainingLoginActionWhenNoFormContainsSignInAction() throws Exception {
		MainCallResult mainCallResult = new MainCallResult();
		Boolean doesDocHaveFormContainingLoginAction = Whitebox.invokeMethod(mainCallResult,
				"doesDocHaveFormContainingLoginAction", docWithSignInAction);
		assertTrue(doesDocHaveFormContainingLoginAction);
	}

	@Test
	public void shouldGetHTML5FromgetHtmlVersion() throws Exception {
		MainCallResult mainCallResult = new MainCallResult();
		String htmlVersion = Whitebox.invokeMethod(mainCallResult, "getHtmlVersion", docWithSignInAction);
		assertTrue("HTML 5".equals(htmlVersion));
	}

	@Test
	public void shouldGetHTML401FromgetHtmlVersion() throws Exception {
		MainCallResult mainCallResult = new MainCallResult();
		String htmlVersion = Whitebox.invokeMethod(mainCallResult, "getHtmlVersion", docWithHeaders);
		assertTrue("HTML 4".equals(htmlVersion));
	}

	@Test
	public void shouldGetCorrectHeaderCounts() throws Exception {
		MainCallResult mainCallResult = new MainCallResult();
		Whitebox.invokeMethod(mainCallResult, "processHeaderCounts", docWithHeaders);
		assertTrue(mainCallResult.getHeadLevel1() == 1);
		assertTrue(mainCallResult.getHeadLevel2() == 2);
		assertTrue(mainCallResult.getHeadLevel3() == 0);
		assertTrue(mainCallResult.getHeadLevel4() == 1);
		assertTrue(mainCallResult.getHeadLevel5() == 0);
		assertTrue(mainCallResult.getHeadLevel6() == 0);
	}

	@Test
	public void shouldGetMainCallResultFromGithubURL() throws MalformedURLException {
		URL parentURL2 = new URL("https://github.com/login");
		MainCallResult mainCallResult = new MainCallResult(parentURL2);
		assertTrue(mainCallResult.getInternalLinks().size() == 0);
		assertTrue(mainCallResult.getExternalLinks().size() == 0);
		assertTrue(mainCallResult.getTitle().equals("Sign in to GitHub · GitHub"));
		assertTrue(mainCallResult.isHasLogin());
	}

}
