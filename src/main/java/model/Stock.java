package model;

public class Stock {
    private String name;
    private String symbol;
    private double price;

    // REQUIRES: symbol is all uppercase
    // EFFECTS: stock name is set to n, symbol is set to parameter symbol, and price is initially set to 0
    public Stock(String companyName, String symbol) {
        this.name = companyName;
        this.symbol = symbol;
        this.price = 0;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
