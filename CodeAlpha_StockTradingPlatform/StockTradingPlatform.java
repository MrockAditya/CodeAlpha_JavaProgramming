package codealpha_tasks.CodeAlpha_StockTradingPlatform;

import java.io.*;
import java.util.*;

class Stock {
    private String symbol;
    private String name;
    private double price;

    public Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }

    public String getSymbol() { return symbol; }
    public String getName() { return name; }
    public double getPrice() { return price; }

    public void updatePrice() {
        // Random fluctuation between -3% and +3%
        double changePercent = (Math.random() * 0.06) - 0.03;
        this.price = Math.max(1.0, this.price * (1 + changePercent));
    }
}

class Transaction {
    private String type;
    private String symbol;
    private int quantity;
    private double price;

    public Transaction(String type, String symbol, int quantity, double price) {
        this.type = type;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("%-5s | %-6s | Qty: %-4d | Price: $%-8.2f | Total: $%-8.2f",
                type, symbol, quantity, price, (quantity * price));
    }
}

class Portfolio {
    private double balance;
    private Map<String, Integer> holdings;
    private List<Transaction> history;

    public Portfolio(double initialBalance) {
        this.balance = initialBalance;
        this.holdings = new HashMap<>();
        this.history = new ArrayList<>();
    }

    public double getBalance() { return balance; }

    public void buyStock(Stock stock, int quantity) {
        double cost = stock.getPrice() * quantity;
        if (cost > balance) {
            System.out.println("Insufficient funds! Required: $" + String.format("%.2f", cost));
            return;
        }
        balance -= cost;
        holdings.put(stock.getSymbol(), holdings.getOrDefault(stock.getSymbol(), 0) + quantity);
        history.add(new Transaction("BUY", stock.getSymbol(), quantity, stock.getPrice()));
        System.out.printf("Successfully bought %d shares of %s for $%.2f\n", quantity, stock.getSymbol(), cost);
    }

    public void sellStock(Stock stock, int quantity) {
        int owned = holdings.getOrDefault(stock.getSymbol(), 0);
        if (owned < quantity) {
            System.out.println("Not enough shares to sell! You own: " + owned);
            return;
        }
        double proceeds = stock.getPrice() * quantity;
        balance += proceeds;
        int remaining = owned - quantity;
        if (remaining == 0) {
            holdings.remove(stock.getSymbol());
        } else {
            holdings.put(stock.getSymbol(), remaining);
        }
        history.add(new Transaction("SELL", stock.getSymbol(), quantity, stock.getPrice()));
        System.out.printf("Successfully sold %d shares of %s for $%.2f\n", quantity, stock.getSymbol(), proceeds);
    }

    public void displayPortfolio(Map<String, Stock> market) {
        System.out.println("\n--- PORTFOLIO SUMMARY ---");
        System.out.printf("Cash Balance: $%.2f\n", balance);
        double totalPortfolioValue = balance;

        System.out.printf("%-8s %-10s %-12s %-12s\n", "Symbol", "Shares", "Current Price", "Value");
        System.out.println("-----------------------------------------------");
        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
            String sym = entry.getKey();
            int qty = entry.getValue();
            double curPrice = market.get(sym).getPrice();
            double val = qty * curPrice;
            totalPortfolioValue += val;
            System.out.printf("%-8s %-10d $%-11.2f $%-11.2f\n", sym, qty, curPrice, val);
        }
        System.out.println("-----------------------------------------------");
        System.out.printf("Total Account Value: $%.2f\n", totalPortfolioValue);
    }

    public void displayHistory() {
        System.out.println("\n--- TRANSACTION HISTORY ---");
        if (history.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }
        for (Transaction t : history) {
            System.out.println(t);
        }
    }

    public void saveToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println(balance);
            for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
                writer.println(entry.getKey() + "," + entry.getValue());
            }
            System.out.println("Portfolio saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving portfolio: " + e.getMessage());
        }
    }
}

public class StockTradingPlatform {
    private static final Map<String, Stock> market = new LinkedHashMap<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static final Portfolio userPortfolio = new Portfolio(10000.00);

    public static void main(String[] args) {
        initializeMarket();

        boolean running = true;
        while (running) {
            updateMarketPrices();
            System.out.println("\n=== STOCK TRADING PLATFORM ===");
            System.out.println("1. View Market Data");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. View Transaction History");
            System.out.println("6. Save & Exit");
            System.out.print("Select option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    displayMarket();
                    break;
                case 2:
                    handleTrade(true);
                    break;
                case 3:
                    handleTrade(false);
                    break;
                case 4:
                    userPortfolio.displayPortfolio(market);
                    break;
                case 5:
                    userPortfolio.displayHistory();
                    break;
                case 6:
                    userPortfolio.saveToFile("portfolio.txt");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid selection.");
            }
        }
    }

    private static void initializeMarket() {
        market.put("AAPL", new Stock("AAPL", "Apple Inc.", 175.50));
        market.put("GOOGL", new Stock("GOOGL", "Alphabet Inc.", 140.25));
        market.put("TSLA", new Stock("TSLA", "Tesla Inc.", 210.80));
        market.put("AMZN", new Stock("AMZN", "Amazon.com", 178.90));
    }

    private static void updateMarketPrices() {
        for (Stock stock : market.values()) {
            stock.updatePrice();
        }
    }

    private static void displayMarket() {
        System.out.println("\n---------------- MARKET WATCH ----------------");
        System.out.printf("%-8s %-20s %-10s\n", "Symbol", "Company", "Price ($)");
        System.out.println("----------------------------------------------");
        for (Stock s : market.values()) {
            System.out.printf("%-8s %-20s $%-9.2f\n", s.getSymbol(), s.getName(), s.getPrice());
        }
        System.out.println("----------------------------------------------");
    }

    private static void handleTrade(boolean isBuy) {
        displayMarket();
        System.out.print("Enter Stock Symbol: ");
        String symbol = scanner.nextLine().trim().toUpperCase();

        if (!market.containsKey(symbol)) {
            System.out.println("Stock symbol not found!");
            return;
        }

        System.out.print("Enter Quantity: ");
        int qty = scanner.nextInt();
        scanner.nextLine();

        if (qty <= 0) {
            System.out.println("Quantity must be greater than zero.");
            return;
        }

        if (isBuy) {
            userPortfolio.buyStock(market.get(symbol), qty);
        } else {
            userPortfolio.sellStock(market.get(symbol), qty);
        }
    }
}
