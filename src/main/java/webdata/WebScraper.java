package webdata;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

// Represents a web scraper with a list of names, symbols, and prices of S&P 500 stocks
public class WebScraper {
    final String url = "https://www.slickcharts.com/sp500";
    public ArrayList<String> names = new ArrayList<>();
    public ArrayList<String> symbols = new ArrayList<>();
    public ArrayList<Double> prices = new ArrayList<>();
    NumberFormat format = NumberFormat.getInstance(Locale.US);
    Document doc;

    // EFFECTS: scrape names, symbols, and market prices of each stock in the S&P 500 index from the url
    public void scrape() {
        try {
            doc = Jsoup.connect(url).get();
            int numEntries = doc.select(".col-lg-7 > div:nth-child(1) > div:nth-child(2) > " +
                    "table:nth-child(1) > tbody:nth-child(2)").select("tr").size();
            for(int i = 0; i < numEntries; i++) {
                String name = doc.select(".col-lg-7 > div:nth-child(1) > div:nth-child(2) > " +
                        "table:nth-child(1) > tbody:nth-child(2)").select("tr").get(i).children().get(1).text();
                String ticker = doc.select(".col-lg-7 > div:nth-child(1) > div:nth-child(2) > " +
                        "table:nth-child(1) > tbody:nth-child(2)").select("tr").get(i).children().get(2).text();
                Number price = format.parse(doc.select(".col-lg-7 > div:nth-child(1) > div:nth-child(2) > " +
                        "table:nth-child(1) > tbody:nth-child(2)").select("tr").get(0).children().get(4).text());
                names.add(name);
                symbols.add(ticker);
                prices.add(price.doubleValue());
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
