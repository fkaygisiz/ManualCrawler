package com.example.demo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Connection.Response;

import com.google.common.net.InternetDomainName;

import org.jsoup.Jsoup;

public class URLConnect {

	private URL url;
	public URLConnect(URL url) {
		this.url = url;
	}
	
	public Response execute() throws IOException {
		return Jsoup.connect(url.toExternalForm())
		        .followRedirects(true) //to follow redirects
		        .timeout(1000)
		        .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
		        .execute();
	}
	
	public static void main(String[] args) {
		try {
			// Generate absolute URL
			// Base URL = www.gnu.org
			
			InternetDomainName childDomain = InternetDomainName.from((new URL("https://help.github.com/articles/github-terms-of-service/")).getHost()).topPrivateDomain();
			InternetDomainName childDomain2 = InternetDomainName.from((new URL("https://github.com/login")).getHost()).topPrivateDomain();

			System.out.println("childDomain : " + childDomain);
			System.out.println("childDomain2 : " + childDomain2);
			System.out.println(childDomain.equals(childDomain2));
			URL url1 = new URL("http://www.gnu.org/ADAD");
			System.out.println("URL1: " + url1.toString());

			// Generate URL for pages with a common base
			URL url2 = new URL(url1, "http://www.milliyet/ADAD");
			System.out.println("URL2: " + url2.toString());

			// Generate URLs from different pieces of data
			URL url3 = new URL("http", "www.gnu.org", "/licenses/gpl.txt");
			System.out.println("URL3: " + url3.toString());	
			
			URL url4 = new URL("http", "www.gnu.org", 80, "/licenses/gpl.txt");
			System.out.println("URL4: " + url4.toString() + "\n");

			// Open URL stream as an input stream and print contents to command line
			try (BufferedReader in = new BufferedReader(new InputStreamReader(url4.openStream()))) {
				String inputLine;

				// Read the "gpl.txt" text file from its URL representation
				System.out.println("/***** File content (URL4) *****/n");
				while((inputLine = in.readLine()) != null) {
					System.out.println(inputLine);
				}
			} catch (IOException ioe) {
				ioe.printStackTrace(System.err);
			}
		} catch (MalformedURLException mue) {
			mue.printStackTrace(System.err);
		}
	}
	
}
