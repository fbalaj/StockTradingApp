package persistence;

import model.Account;
import model.Holding;
import model.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class WriterTest {
    private static final String TEST_FILE = "./data/testAccountFile.txt";
    private Writer testWriter;
    private Account account;
    private Stock s1;

    @BeforeEach
    void runBefore() throws FileNotFoundException, UnsupportedEncodingException {
        testWriter = new Writer(new File(TEST_FILE));
        account = new Account(0, 0, new ArrayList<>());
        s1 = new Stock("Tesla", "TSLA");
        s1.setPrice(1500);
    }

    @Test
    void testWriteAccount() {
        account.depositCash(100000);
        account.buyStock(s1, 25);
        testWriter.write(account);
        testWriter.close();

        try {
            List<Holding> holdings = Reader.readHoldings(new File(TEST_FILE));
            Holding account = holdings.get(0);
            //       assertEquals(62500.0, account.getCash());
            assertEquals("Tesla", holdings.get(0).getHoldingName());
            assertEquals("TSLA", holdings.get(0).getHoldingSymbol());
            assertEquals(25, holdings.get(0).getShares());
            //       assertEquals(37500.0, account.getBookCost());


        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
