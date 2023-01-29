package persistence;

import model.Account;
import model.Holding;
import model.Stock;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Reader {
    public static final String DELIMITER = ",";

    public static List<Holding> readHoldings(File file) throws IOException {
        List<String> fileContent = readFile(file);
        return parseContent(fileContent);
    }

    public static List<Account> readAccounts(File file) throws IOException {
        List<String> fileContent = readFile(file);
        return parseContentAccount(fileContent);
    }


    private static List<Holding> parseContent(List<String> fileContent) throws IOException {
        List<Holding> holdings = new ArrayList<>();

        for (String line : fileContent) {
            ArrayList<String> lineComponents = splitString(line);
            holdings.add(parseHolding(lineComponents));
        }
        return holdings;
    }

    private static List<Account> parseContentAccount(List<String> fileContent) throws IOException {
        List<Account> accounts = new ArrayList<>();

        for (String line : fileContent) {
            ArrayList<String> lineComponents = splitString(line);
            accounts.add(parseAccount(lineComponents));
        }
        return accounts;
    }

    // EFFECTS: returns content of file as a list of strings, each string
    // containing the content of one row of the file
    private static List<String> readFile(File file) throws IOException {
        return Files.readAllLines(file.toPath());
    }

    // EFFECTS: returns a list of strings obtained by splitting line on DELIMITER
    private static ArrayList<String> splitString(String line) {
        String[] splits = line.split(DELIMITER);
        return new ArrayList<>(Arrays.asList(splits));
    }

    private static Holding parseHolding(List<String> components) throws IOException {
        //    double balance = Double.parseDouble(components.get(0));
        String name = components.get(1);
        String symbol = components.get(2);
        int shares = Integer.parseInt(components.get(3));
        //    double bookCost = Double.parseDouble(components.get(4));
        Stock stock = new Stock(name, symbol);
        Holding holding = new Holding(stock, shares);
        return holding;
    }

    private static Account parseAccount(List<String> components) throws IOException {
        double balance = Double.parseDouble(components.get(0));
        double bookCost = Double.parseDouble(components.get(4));
        return new Account(balance, bookCost, new ArrayList<>());
    }

}
