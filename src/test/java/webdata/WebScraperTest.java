package webdata;

import model.Account;
import model.Stock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.fail;

public class WebScraperTest {
    private WebScraper scraper;
    private Account account;
    private Stock s1;

    @BeforeEach
    void runBefore() {
        scraper = new WebScraper();
        account = new Account(0, 0, new ArrayList<>());
        s1 = new Stock("Apple Inc.", "AAPL");
        account.depositCash(250000);
    }

    @Test
    void testsScraper() {
        try {
            scraper.scrape();
            s1.setPrice(scraper.prices.get(0));
            account.buyStock(s1, 25);
            Assertions.assertEquals(1, account.getPortfolio().size());
            account.getPortfolio().get(0).setMarketValue(scraper.prices.get(0) * 25.0);
            Assertions.assertEquals(scraper.prices.get(0) * 25.0, account.getPortfolio().get(0).marketValue);
        } catch (Exception e) {
            fail("Should not have thrown exception");
        }
    }

    @Test
    void testException() {

    }
}
