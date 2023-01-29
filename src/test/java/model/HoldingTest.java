package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class HoldingTest {
    private Account testAccount;
    private Stock s1;
    private Stock s2;

    @BeforeEach
    void runBefore() {
        testAccount = new Account(0, 0, new ArrayList<>());
        testAccount.depositCash(20000);
        s1 = new Stock("Tesla", "TSLA");
        s2 = new Stock("Apple", "AAPL");
        s1.setPrice(1550);
        s2.setPrice(380);
    }

    @Test
    void testHolding() {
        Holding h = new Holding(s1, 10);
        h.setMarketValue(15500);
        h.setGainLoss(0);
        h.setAvgPrice(1550);
        testAccount.getPortfolio().add(h);
        Assertions.assertEquals(10, testAccount.getPortfolio().get(0).getShares());
        h.updateShares(5);
        Assertions.assertEquals(15, testAccount.getPortfolio().get(0).getShares());
    }
}
