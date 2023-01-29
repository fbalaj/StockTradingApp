package model;

import persistence.Reader;
import persistence.Saveable;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

// Represents a trading account with cash, book cost, value of investments, and list of holdings
public class Account implements Saveable {
    private double cash;
    private double bookCost;
    private double investmentsValue;
    private List<Holding> portfolio;

    // EFFECTS: creates a new account with 0 cash, 0 book cost, 0 investment value and no holdings
    public Account(double cash, double bookCost, ArrayList<Holding> portfolio) {
        this.cash = cash;
        this.bookCost = bookCost;
        this.investmentsValue = 0;
        this.portfolio = portfolio;
    }

    // REQUIRES: this.cash >= shares * stock price
    // MODIFIES: this
    // EFFECTS: create new holding with stock s and shares, and add it to portfolio. Subtract cost from available cash
    public void buyStock(Stock s, int shares) {
        double cost = s.getPrice() * shares;
        if (portfolio.isEmpty() && this.cash >= cost) {
            portfolio.add(new Holding(s, shares));
            this.cash -= cost;
            this.bookCost += cost;
        } else {
            for (Holding h : portfolio) {
                if (h.getHoldingSymbol().equals(s.getSymbol()) && this.cash >= cost) {
                    h.updateShares(shares);
                    h.bookCost += cost;
                    this.cash -= cost;
                    return;
                }
            }
            if (this.cash >= cost) {
                portfolio.add(new Holding(s, shares));
                this.cash -= cost;
                this.bookCost += cost;
            }
        }
    }

    //
    // https://stackoverflow.com/questions/18448671/how-to-avoid-concurrentmodificationexception-while-removing-elements-from-arr
    // REQUIRES: s is in portfolio, shares <= # of shares owned, price <= market price
    // MODIFIES: this
    // EFFECTS: if stock is in portfolio, sell it, and if user sells all shares, remove stock from portfolio
    public void sellStock(Stock s, int shares) {
        List<Holding> toRemove = new ArrayList<>();
        for (Holding h : portfolio) {
            if (h.stock.getName().equals(s.getName())) {
                if (h.shares > shares) {
                    this.cash += shares * s.getPrice();
                    this.bookCost -= shares * s.getPrice();
                    h.bookCost -= shares * s.getPrice();
                    this.investmentsValue -= shares * s.getPrice();
                    h.shares -= shares;
                } else if (h.shares == shares) {
                    this.cash += shares * s.getPrice();
                    this.bookCost -= shares * s.getPrice();
                    h.bookCost -= shares * s.getPrice();
                    this.investmentsValue -= shares * s.getPrice();
                    h.shares -= shares;
                    toRemove.add(h);
                }
            }
        }
        portfolio.removeAll(toRemove);
    }

    // MODIFIES: this
    // EFFECTS: update investmentsValue to reflect the sum of the market price of the entire portfolio
    public void sumPortfolio() {
        this.investmentsValue = 0;
        for (int i = 0; i < portfolio.size(); i++) {
            this.investmentsValue += portfolio.get(i).marketValue;
        }
    }

    // MODIFIES: this
    // EFFECTS: update bookCost to reflect the sum of the book cost of the entire portfolio
    public void sumBookCost() {
        for (int i = 0; i < portfolio.size(); i++) {
            this.bookCost += portfolio.get(i).getBookCost();
        }
    }

    public double getInvestmentsValue() {
        return investmentsValue;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    // MODIFIES: this
    // EFFECTS: adds amount to cash balance
    public void depositCash(double amount) {
        this.cash += amount;
    }

    // MODIFIES: this
    // EFFECTS: subtracts amount from cash balance
    public void withdrawCash(double amount) {
        this.cash -= amount;
    }

    public double getCash() {
        return cash;
    }


    public double getBookCost() {
        return bookCost;
    }

    public List<Holding> getPortfolio() {
        return portfolio;
    }

    // EFFECTS: saves Account fields to file
    @Override
    public void save(PrintWriter printWriter) {
        for (int i = 0; i < this.portfolio.size(); i++) {
            printWriter.print(this.cash);
            printWriter.print(Reader.DELIMITER);
            printWriter.print(this.portfolio.get(i).getHoldingName());
            printWriter.print(Reader.DELIMITER);
            printWriter.print(this.portfolio.get(i).getHoldingSymbol());
            printWriter.print(Reader.DELIMITER);
            printWriter.print(this.portfolio.get(i).getShares());
            printWriter.print(Reader.DELIMITER);
            printWriter.println(this.portfolio.get(i).getBookCost());
        }
    }
}
