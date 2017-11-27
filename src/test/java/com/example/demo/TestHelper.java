package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.example.demo.model.MainCallResult;

public class TestHelper {
	public static Document getDocFromHtmlFile(String filePath) throws URISyntaxException, IOException {
		ClassLoader classLoader = MainCallResult.class.getClassLoader();
		String decodedPath = new URI(classLoader.getResource(filePath).toString()).getPath();
		File file = new File(decodedPath);
		return Jsoup.parse(file, "UTF-8", "http://example.com/");
	}
}
