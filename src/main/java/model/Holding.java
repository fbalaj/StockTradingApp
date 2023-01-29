package model;

// Represents holding of a stock with number of shares, book price, and average price per share
public class Holding {
    protected Stock stock;
    public int shares;
    public double bookCost;
    public double avgPrice;
    public double marketValue;
    public double gainLoss;


    // EFFECTS: Constructs a holding with stock s, sh number of shares, book cost and average price per share
    public Holding(Stock s, int sh) {
        this.stock = s;
        this.shares = sh;
        this.bookCost = s.getPrice() * sh;
        this.avgPrice = this.bookCost / sh;
        this.gainLoss = marketValue - bookCost;
    }

    // MODIFIES: this
    // EFFECTS: add s to shares
    public void updateShares(int s) {
        this.shares += s;
    }

    public void setMarketValue(double marketValue) {
        this.marketValue = marketValue;
    }

    public void setGainLoss(double gainLoss) {
        this.gainLoss = gainLoss;
    }

    public int getShares() {
        return shares;
    }

    public void setBookCost(double bookCost) {
        this.bookCost = bookCost;
    }

    public void setAvgPrice(double avgPrice) {
        this.avgPrice = avgPrice;
    }

    public double getBookCost() {
        return bookCost;
    }

    public String getHoldingName() {
        return this.stock.getName();
    }

    public String getHoldingSymbol() {
        return this.stock.getSymbol();
    }
}
