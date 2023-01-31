package persistence;

import model.Account;
import model.Holding;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class ReaderTest {
    @Test
    void parseHoldingsTest() {
        try {
            List<Holding> holdings = Reader.readHoldings(new File("./data/testAccountFile.txt"));
            List<Account> accounts = Reader.readAccounts(new File("./data/testAccountFile.txt"));
            Holding h = holdings.get(0);
            Account a = accounts.get(0);
            h.setBookCost(a.getBookCost());
            Assertions.assertEquals("Tesla", h.getHoldingName());
            Assertions.assertEquals("TSLA", h.getHoldingSymbol());
            Assertions.assertEquals(25, h.getShares());
            Assertions.assertEquals(37500.0, h.getBookCost());
            Assertions.assertEquals(62500.0, a.getCash());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testIOException() {
        try {
            Reader.readHoldings(new File("./data/nonexistingfile.txt"));
            fail("Should have thrown exception");
        } catch (IOException e) {
            // expected
        }
    }
}
