import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import java.io.FileWriter;
import com.opencsv.CSVWriter;


public class Controller {
	public static void main(String[] args) throws Exception {
		String crawlStorageFolder = "C:/Users/cleve/eclipse-workspace/MyFirstCrawler/data/crawl";
		
		FileWriter fetchFileWriter = new FileWriter("fetch_foxnews.csv",true);
		FileWriter visitFileWriter = new FileWriter("visit_foxnews.csv",true);
		FileWriter urlsFileWriter = new FileWriter("urls_foxnews.csv",true);
		
		CSVWriter fetchWriter = new CSVWriter(fetchFileWriter);
		CSVWriter visitWriter = new CSVWriter(visitFileWriter);
		CSVWriter urlsWriter = new CSVWriter(urlsFileWriter);
		
		fetchWriter.writeNext(new String[] {"URL","Status"});
		fetchWriter.close();
		visitWriter.writeNext(new String[] {"URL","Size","Number of Outlinks","Content-Type"});
		visitWriter.close();
		urlsWriter.writeNext(new String[] {"URL","OK/N_OK"});
		urlsWriter.close();
		
		int numberOfCrawlers = 7;
		
		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setMaxPagesToFetch(20000);
		config.setMaxDepthOfCrawling(16);
		config.setIncludeBinaryContentInCrawling(true);
		config.setIncludeHttpsPages(true);
		config.setPolitenessDelay(2);
		
		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
		
		/*
		 * For each crawl, you need to add some seed URLs. These are the first
		 * URLs that are fetched and then the crawler starts following links
		 * which are found in these pages.
		 */
		controller.addSeed("https://www.foxnews.com");
		
		/*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		controller.start(MyCrawler.class, numberOfCrawlers);
		
		System.out.println("FINAL UNIQUE URL INSIDE: " + MyCrawler.getUniqueUrlInside());
		System.out.println("FINAL UNIQUE URL OUTSIDE: " + MyCrawler.getUniqueUrlOutside());
		System.out.println("FINAL TOTAL URL: " + MyCrawler.getTotalUrl());
;	}
}
