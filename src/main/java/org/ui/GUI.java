package org.ui;

import model.Account;
import model.Holding;
import model.Stock;
import persistence.Reader;
import persistence.Writer;
import webdata.WebScraper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GUI extends JFrame implements ActionListener {
    private static final String ACCOUNT_FILE = "./data/account.txt";
    private Account account;
    private List<Stock> indexStockList;
    private WebScraper webScraper = new WebScraper();
    private int shares;
    private String symbol;
    private JButton depositButton;
    private JButton withdrawButton;
    private JButton buyButton;
    private JButton sellButton;
    private JButton viewPortfolioButton;
    private JButton viewBalanceButton;
    private JButton loadDataButton;
    private JButton saveButton;
    private JButton exitButton;
    private JButton submitButton;
    private JFrame frame;
    private JPanel leftPanel;
    private JPanel rightPanel;
   // private JPanel gifPanel = null;
    private JTextField userInput;
    private JTextField userShareInput;
    private JLabel userPrompt;

    // EFFECTS: creates a new GUI
    public GUI() {
        account = new Account(0.0, 0.0, new ArrayList<>());
        indexStockList = new ArrayList<>();
        repeat();
        frame = new JFrame("Broker Application");
        leftPanel = new JPanel();
        rightPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(350, 550));
        rightPanel.setPreferredSize(new Dimension(350, 550));
        createPanels();
        createButtons();
        addButtons();
        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(rightPanel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: clears the list of stocks every 10 seconds and replaces it with newly-scraped data
    public void repeat() {
        indexStockList.clear();
        initStockList();
        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                webScraper.names.clear();
                webScraper.prices.clear();
                webScraper.symbols.clear();
                webScraper.scrape();
                updateStocks();
                updateMktValue();
                account.sumPortfolio();
                // System.out.println(indexStockList.size());
                // System.out.println(account.getPortfolio().size());
            }
        });
        timer.setInitialDelay(0);
        timer.setRepeats(true);
        timer.start();
    }

    // MODIFIES: this
    // EFFECTS: update market value of holdings with most recent prices
    public void updateMktValue() {
        for (int i = 0; i < indexStockList.size(); i++) {
            for (int j = 0; j < account.getPortfolio().size(); j++) {
                if (account.getPortfolio().get(j).getHoldingSymbol().equals(indexStockList.get(i).getSymbol())) {
                    account.getPortfolio().get(j).setMarketValue(indexStockList.get(i).getPrice()
                            * account.getPortfolio().get(j).shares);
                    account.getPortfolio().get(j).setGainLoss(account.getPortfolio().get(j).marketValue
                            - account.getPortfolio().get(j).bookCost);
                }
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Update each stock in indexStockList with new prices
    public void updateStocks() {
        for (int i = 0; i < indexStockList.size(); i++) {
            indexStockList.get(i).setPrice(webScraper.prices.get(i));
        }
    }

    // MODIFIES: this
    // EFFECTS: scrapes online stock data and creates a new Stock object for each stock and adds it to list
    public void initStockList() {
        webScraper.scrape();
        for (int i = 0; i < webScraper.names.size(); i++) {
            indexStockList.add(new Stock(webScraper.names.get(i),
                    webScraper.symbols.get(i)));
            indexStockList.get(i).setPrice(webScraper.prices.get(i));
        }
    }

    // EFFECTS: creates a deposit button
    public void createDepositButton() {
        depositButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                depositSetup();
            }
        });
    }

    // EFFECTS: creates a withdraw button
    public void createWithdrawButton() {
        withdrawButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //     removeGif();
                withdrawSetup();
            }
        });
    }

    // EFFECTS: creates a buy button
    public void createBuyButton() {
        buyButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //     removeGif();
                buySetup();
            }
        });
    }

    // EFFECTS: creates a sell button
    public void createSellButton() {
        sellButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //     removeGif();
                sellSetup();
            }
        });
    }

    public void createViewPortfolioButton() {
        viewPortfolioButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    viewPortfolioSetup();
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    // EFFECTS: creates a view balance button
    public void createViewBalanceButton() {
        viewBalanceButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewBalanceSetup();
            }
        });
    }

    // EFFECTS: creates a load button
    public void createLoadDataButton() {
        loadDataButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
            }
        });
    }

    // EFFECTS: creates a save button
    public void createSaveButton() {
        saveButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveData();
            }
        });
    }

    // EFFECTS: creates a exit button
    public void createExitButton() {
        exitButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    // EFFECTS: instantiate all buttons and set their names
    public void createButtons() {
        createDepositButton();
        createWithdrawButton();
        createBuyButton();
        createSellButton();
        createViewPortfolioButton();
        createViewBalanceButton();
        createLoadDataButton();
        createSaveButton();
        createExitButton();

        depositButton.setText("Deposit");
        withdrawButton.setText("Withdraw");
        buyButton.setText("Buy Stock");
        sellButton.setText("Sell stock");
        viewPortfolioButton.setText("View Portfolio");
        viewBalanceButton.setText("View current balance");
        loadDataButton.setText("Load Data");
        saveButton.setText("Save data");
        exitButton.setText("Exit");
    }

    // EFFECTS: add buttons to GUI panel
    public void addButtons() {
        leftPanel.add(depositButton);
        leftPanel.add(withdrawButton);
        leftPanel.add(buyButton);
        leftPanel.add(sellButton);
        leftPanel.add(viewPortfolioButton);
        leftPanel.add(viewBalanceButton);
        leftPanel.add(loadDataButton);
        leftPanel.add(saveButton);
        leftPanel.add(exitButton);
    }

    // EFFECTS: instantiate panels
    public void createPanels() {
        leftPanel.setBorder(BorderFactory.createEmptyBorder());
        leftPanel.setLayout(new GridLayout(9, 0));
        leftPanel.setBackground(Color.BLACK);
        rightPanel.setBorder(BorderFactory.createEmptyBorder());
        rightPanel.setBackground(Color.ORANGE);
        rightPanel.setLayout(new GridLayout(15, 0));
    }

    // Taken from CPSC 210 TellerApp
    // EFFECTS: saves state of investment account
    public void saveData() {
        rightPanel.removeAll();
        rightPanel.revalidate();
        rightPanel.repaint();
        try {
            Writer writer = new Writer(new File(ACCOUNT_FILE));
            writer.write(account);
            writer.close();
            rightPanel.add(new JLabel("Saved data to file"));
        } catch (FileNotFoundException e) {
            rightPanel.add(new JLabel("Unable to save to " + ACCOUNT_FILE));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    // Taken from CPSC 210 TellerApp
    // MODIFIES: this
    // EFFECTS: loads account from ACCOUNT_FILE if the file exists. otherwise initalize account with default account
    public void loadData() {
        try {
            List<Holding> holdings = Reader.readHoldings(new File(ACCOUNT_FILE));
            List<Account> accounts = Reader.readAccounts(new File(ACCOUNT_FILE));
            if (!holdings.isEmpty() && !accounts.isEmpty()) {
                this.account.setCash(accounts.get(0).getCash());
                for (int i = 0; i < holdings.size(); i++) {
                    this.account.getPortfolio().add(holdings.get(i));
                    this.account.getPortfolio().get(i).setBookCost(accounts.get(i).getBookCost());
                    this.account.getPortfolio().get(i).setAvgPrice(accounts.get(i).getBookCost()
                            / holdings.get(i).getShares());
                }
                this.account.sumBookCost();
            }
        } catch (IndexOutOfBoundsException | IOException e) {
            new GUI();
        }
    }

    // EFFECTS: Prompts the user to enter a stock symbol
    public void sellSetup() {
        rightPanel.removeAll();
        rightPanel.revalidate();
        rightPanel.repaint();
        userPrompt = new JLabel("Enter stock symbol");
        userPrompt.setFont(new Font("Helvetica", Font.BOLD, 18));
        rightPanel.add(userPrompt);
        userInput = new JTextField();
        rightPanel.add(userInput);
        submitButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                symbol = userInput.getText();
                sellSharesSetup();
                frame.revalidate();
                frame.repaint();
            }
        });
        submitButton.setText("Submit");
        rightPanel.add(submitButton);
    }

    // EFFECTS: prompt the user to enter number of shares
    public void sellSharesSetup() {
        rightPanel.removeAll();
        rightPanel.revalidate();
        rightPanel.repaint();
        userPrompt = new JLabel("How many shares?");
        userPrompt.setFont(new Font("Helvetica", Font.BOLD, 18));
        rightPanel.add(userPrompt);
        userShareInput = new JTextField();
        rightPanel.add(userShareInput);
        submitButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shares = Integer.parseInt(userShareInput.getText());
                submitSell();
                frame.revalidate();
                frame.repaint();
            }
        });
        submitButton.setText("Submit");
        rightPanel.add(submitButton);
    }

    // MODIFIES: this
    // EFFECTS: sells the stock
    public void submitSell() {
        symbol = userInput.getText();
        shares = Integer.parseInt(userShareInput.getText());
        for (int i = 0; i < indexStockList.size(); i++) {
            if (indexStockList.get(i).getSymbol().equals(symbol)) {
                this.account.sellStock(indexStockList.get(i), shares);
            }
        }
    }

    // EFFECTS: display information of every owned stock. If there is a net profit, displays a gif
    public void viewPortfolioSetup() throws MalformedURLException {
        rightPanel.removeAll();
        rightPanel.revalidate();
        rightPanel.repaint();
        rightPanel.add(new JLabel("Total gain/loss "
                + (this.account.getInvestmentsValue() - this.account.getBookCost())));
        for (int i = 0; i < this.account.getPortfolio().size(); i++) {
            rightPanel.add(new JLabel("Stock: " + this.account.getPortfolio().get(i).getHoldingName()));
            rightPanel.add(new JLabel("# of shares: " + this.account.getPortfolio().get(i).getShares()));
            rightPanel.add(new JLabel("Average price per share: " + this.account.getPortfolio().get(i).avgPrice));
            rightPanel.add(new JLabel("Book cost: " + this.account.getPortfolio().get(i).getBookCost()));
            rightPanel.add(new JLabel("Market value: " + this.account.getPortfolio().get(i).marketValue));
            rightPanel.add(new JLabel("Gain/Loss (unrealized): " + this.account.getPortfolio().get(i).gainLoss));
            rightPanel.add(new JLabel("______________________________________"));
//            if ((this.account.getInvestmentsValue() - this.account.getBookCost()) > 0) {
//                addGif();
//            }
        }
    }

//    // EFFECTS: adds visual component to GUI
//    public void addGif() throws MalformedURLException {
//        URL url = new URL("https://media.tenor.com/images/b55bff5a514d97b0397ba494038b07d4/tenor.gif");
//        ImageIcon icon = new ImageIcon(url);
//        JLabel gif = new JLabel(icon);
//        gifPanel = new JPanel();
//        gifPanel.add(gif);
//        gifPanel.setPreferredSize(new Dimension(220, 550));
//        gifPanel.setBackground(Color.ORANGE);
//        frame.add(gifPanel, BorderLayout.EAST);
//        frame.pack();
//        frame.revalidate();
//        frame.repaint();
//    }

//    // EFFECTS: removes visual component from GUI
//    public void removeGif() {
//        if (gifPanel != null) {
//            frame.remove(gifPanel);
//            frame.pack();
//            frame.revalidate();
//            frame.repaint();
//        }
//    }

    // EFFECTS: displays current balance
    public void viewBalanceSetup() {
        rightPanel.removeAll();
        rightPanel.revalidate();
        rightPanel.repaint();
        rightPanel.add(new JLabel("Your current balance is: " + this.account.getCash()));
    }

    // EFFECTS: Prompts the user to enter a cash amount to deposit
    public void depositSetup() {
        rightPanel.removeAll();
        rightPanel.revalidate();
        rightPanel.repaint();
        userPrompt = new JLabel("How much cash do you want to deposit?");
        userPrompt.setFont(new Font("Helvetica", Font.BOLD, 18));
        rightPanel.add(userPrompt);
        userInput = new JTextField();
        rightPanel.add(userInput);
        submitButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitDeposit();
                frame.revalidate();
                frame.repaint();
            }
        });
        submitButton.setText("Submit");
        rightPanel.add(submitButton);
    }

    // MODIFIES: this
    // EFFECTS: deposits cash and displays it in the GUI
    public void submitDeposit() {
        double cash;
        cash = Double.parseDouble(userInput.getText());
        this.account.depositCash(cash);
        rightPanel.add(new JLabel("Your current balance is: " + this.account.getCash()));
        frame.revalidate();
        frame.repaint();
    }

    // MODIFIES: this
    // EFFECTS: withdraws cash and displays it in the GUI
    public void submitWithdraw() {
        double cash;
        cash = Double.parseDouble(userInput.getText());
        this.account.withdrawCash(cash);
        System.out.println(account.getCash());
        rightPanel.add(new JLabel("Your current balance is: " + this.account.getCash()));
        frame.revalidate();
        frame.repaint();
    }



    // EFFECTS: Prompts the user to enter a cash amount
    public void withdrawSetup() {
        rightPanel.removeAll();
        rightPanel.revalidate();
        rightPanel.repaint();
        userPrompt = new JLabel("How much cash do you want to withdraw?");
        userPrompt.setFont(new Font("Helvetica", Font.BOLD, 18));
        rightPanel.add(userPrompt);
        userInput = new JTextField();
        rightPanel.add(userInput);
        submitButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitWithdraw();
                frame.revalidate();
                frame.repaint();
            }
        });
        submitButton.setText("Submit");
        rightPanel.add(submitButton);
    }

    // EFFECTS: Prompts the user to enter a stock symbol
    public void buySetup() {
        rightPanel.removeAll();
        rightPanel.revalidate();
        rightPanel.repaint();
        userPrompt = new JLabel("Enter stock symbol");
        userPrompt.setFont(new Font("Helvetica", Font.BOLD, 18));
        rightPanel.add(userPrompt);
        userInput = new JTextField();
        rightPanel.add(userInput);
        submitButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                symbol = userInput.getText();
                buySharesSetup();
                frame.revalidate();
                frame.repaint();
            }
        });
        submitButton.setText("Submit");
        rightPanel.add(submitButton);
    }

    // EFFECTS: prompt the user to enter number of shares
    public void buySharesSetup() {
        rightPanel.removeAll();
        rightPanel.revalidate();
        rightPanel.repaint();
        userPrompt = new JLabel("How many shares?");
        userPrompt.setFont(new Font("Helvetica", Font.BOLD, 18));
        rightPanel.add(userPrompt);
        userShareInput = new JTextField();
        rightPanel.add(userShareInput);
        submitButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shares = Integer.parseInt(userShareInput.getText());
                submitBuy();
                frame.revalidate();
                frame.repaint();
            }
        });
        submitButton.setText("Submit");
        rightPanel.add(submitButton);
    }

    // MODIFIES: this
    // EFFECTS: purchases the stock
    public void submitBuy() {
        symbol = userInput.getText();
        shares = Integer.parseInt(userShareInput.getText());
        for (int i = 0; i < indexStockList.size(); i++) {
            if (indexStockList.get(i).getSymbol().equals(symbol)) {
                this.account.buyStock(indexStockList.get(i), shares);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
