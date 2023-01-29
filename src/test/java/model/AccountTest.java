package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
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
    void testBuyStock() {
        assertEquals(1550, s1.getPrice());
        assertEquals("TSLA", s1.getSymbol());
        assertEquals(20000, testAccount.getCash());
        testAccount.buyStock(s1, 2);
        assertEquals(16900, testAccount.getCash());
        assertEquals(1, testAccount.getPortfolio().size());
        testAccount.buyStock(s2, 3);
        assertEquals(380, testAccount.getPortfolio().get(1).avgPrice);
        assertEquals(1550, testAccount.getPortfolio().get(0).avgPrice);
        assertTrue(testAccount.getBookCost() == 4240);
        assertEquals("Tesla", testAccount.getPortfolio().get(0).getHoldingName());
        assertEquals("TSLA", testAccount.getPortfolio().get(0).getHoldingSymbol());
    }

    @Test
    void testBuySameStockMultipleTimes() {
        testAccount.buyStock(s1, 1);
        testAccount.buyStock(s1, 1);
        assertEquals(1, testAccount.getPortfolio().size());
        assertEquals(16900, testAccount.getCash());
        assertEquals(2, testAccount.getPortfolio().get(0).getShares());
        testAccount.setCash(0);
        testAccount.buyStock(s1, 1);
        assertEquals(2, testAccount.getPortfolio().get(0).getShares());
    }

    @Test
    void testSellStockPartialShares() {
        testAccount.buyStock(s2, 10);
        assertEquals(16200, testAccount.getCash());
        testAccount.sellStock(s2, 5);
        assertEquals(18100, testAccount.getCash());
        assertEquals(5, testAccount.getPortfolio().get(0).shares);
        assertEquals(380, testAccount.getPortfolio().get(0).avgPrice);
    }

    @Test
    void testSellStockNotOwned() {
        testAccount.buyStock(s1, 1);
        testAccount.sellStock(s2, 1);
        assertEquals(1550, testAccount.getPortfolio().get(0).bookCost);
    }

    @Test
    void testSellSharesNotOwned() {
        testAccount.sellStock(s2, 10);
        assertEquals(0, testAccount.getPortfolio().size());
        assertEquals(20000, testAccount.getCash());
    }

    @Test
    void testSellStockAllShares() {
        testAccount.buyStock(s2, 10);
        assertEquals(16200, testAccount.getCash());
        assertTrue(testAccount.getPortfolio().size() == 1);
        testAccount.sellStock(s2, 10);
        assertEquals(20000, testAccount.getCash());
        assertTrue(testAccount.getPortfolio().size() == 0);
    }

    @Test
    void testSellAll() {
        testAccount.buyStock(s2, 10);
        assertEquals(10, testAccount.getPortfolio().get(0).getShares());
        assertEquals(1, testAccount.getPortfolio().size());
        testAccount.sellStock(s2, 10);
        assertEquals(0, testAccount.getPortfolio().size());
    }

    @Test
    void testSellSomeThenAll() {
        testAccount.buyStock(s2, 10);
        assertEquals(16200, testAccount.getCash());
        assertEquals(3800, testAccount.getBookCost());
        testAccount.sellStock(s2, 5);
        testAccount.sellStock(s2, 5);
        assertEquals(0, testAccount.getPortfolio().size());
        assertEquals(20000, testAccount.getCash());
    }

    @Test
    void testCantAffordStock() {
        testAccount.withdrawCash(19000);
        assertEquals(1000, testAccount.getCash());
        testAccount.buyStock(s2, 10);
        assertEquals(0, testAccount.getPortfolio().size());
    }
    @Test
    void testBuyMultipleStocks() {
        testAccount.buyStock(s1, 5);
        testAccount.buyStock(s2, 20);
        assertEquals(2, testAccount.getPortfolio().size());
        assertEquals(4650, testAccount.getCash());
        assertEquals(15350, testAccount.getBookCost());
        assertEquals(7750, testAccount.getPortfolio().get(0).bookCost);
        assertEquals(7600, testAccount.getPortfolio().get(1).bookCost);
    }
}