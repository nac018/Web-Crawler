import java.util.Set;
import java.util.regex.Pattern;
//import java.io.IOException;
import java.io.FileWriter;
import com.opencsv.CSVWriter;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
//import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css||css?|js"  + "|mp3|mp4|zip|gz|json))$");
	private int count = 0;
	private static int unique_url_inside = 0;
	private static int unique_url_outside = 0;
	private static int total_url = 0;
	
	public static int getUniqueUrlInside() {
		return unique_url_inside;
	}
	
	public static int getUniqueUrlOutside() {
		return unique_url_outside;
	}
	
	public static int getTotalUrl() {
		return total_url;
	}
	
	/*
	 * This method received two parameters. The first parameteris the page 
	 * in which we have discovered this new URL and the second parameter is
	 * the new URL. You should implement this function to specify whether 
	 * the given URL should be crawled or not (based on your crawling logic).
	 * In this example, we are instructing the crawler to ignore URLs that 
	 * have css, js, mp3, ... extensions and to only accept URLs that start
	 * with "https://www.foxnews.com/". In this case, we didn't need the 
	 * referringPage parameter to make the decision.
	 */
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		boolean filter = !FILTERS.matcher(href).matches() 
				&& (href.startsWith("https://www.foxnews.com/") || href.startsWith("http://www.foxnews.com/"));
		
		if(href.startsWith("https://www.foxnews.com/") || href.startsWith("http://www.foxnews.com/")) {
			++unique_url_inside;
		}
		else {
			++unique_url_outside;
		}
		
		try {
			FileWriter urlsFileWriter = new FileWriter("urls_foxnews.csv",true);
			CSVWriter urlsWriter = new CSVWriter(urlsFileWriter);
			if(filter) {
				urlsWriter.writeNext(new String[] {href,"OK"});
			}
			else {
				urlsWriter.writeNext(new String[] {href,"N_OK"});
			}
			urlsWriter.close();
		}
		catch(Exception e) {
			System.out.println("Error encountered when writing to urls csv");
		}
		return filter;
	}
	
	@Override
	protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
		try {
			FileWriter fetchFileWriter = new FileWriter("fetch_foxnews.csv",true);
			CSVWriter fetchWriter = new CSVWriter(fetchFileWriter);
			fetchWriter.writeNext(new String[] {webUrl.getURL(),Integer.toString(statusCode)});
			fetchWriter.close();
		}
		catch(Exception e) {
			System.out.println("Error encountered when writing to fetch csv");
		}
	}

	/*
	 * This function is called when a page is fetched and ready to be 
	 * processed by your program.
	 */
	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL().replaceAll(",", "-");
		String contentType = page.getContentType().split(";")[0];
		boolean contentTypeCheck = 
				contentType.equals("text/html") ||
				contentType.startsWith("image/") ||
				contentType.equals("application/pdf") || 
				contentType.equals("application/msword") || 
				contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		
		if(page.getParseData() != null && contentTypeCheck) {
			//HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			Set<WebURL> links = page.getParseData().getOutgoingUrls();
			try {
				FileWriter visitFileWriter = new FileWriter("visit_foxnews.csv",true);
				CSVWriter visitWriter = new CSVWriter(visitFileWriter);
				visitWriter.writeNext(new String[] {url,Integer.toString(page.getContentData().length),Integer.toString(links.size()),page.getContentType()});
				visitWriter.close();
				
				++count;
				total_url += links.size();
				
				System.out.println("URL " + Integer.toString(count) + " DONE");
				System.out.println("UNIQUE URL INSIDE: " + Integer.toString(unique_url_inside));
				System.out.println("UNIQUE URL OUTSIDE: " + Integer.toString(unique_url_outside));
				System.out.println("TOTAL URL: " + Integer.toString(total_url));
			}
			catch(Exception e) {
				System.out.println("Error encountered when writing to visit csv");
			}
		}
		
	}
}
