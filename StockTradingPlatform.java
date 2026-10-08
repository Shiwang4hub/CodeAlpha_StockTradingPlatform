import java.util.ArrayList;
import java.util.Scanner;

class Stock {
    private String symbol;
    private String name;
    private double price;

    public Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}

class Transaction {
    private String type;
    private String stockSymbol;
    private int quantity;
    private double price;

    public Transaction(String type, String stockSymbol, int quantity, double price) {
        this.type = type;
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.price = price;
    }

    public void display() {
        System.out.printf("%-8s %-10s %-8d Rs. %.2f%n",
                type, stockSymbol, quantity, price);
    }
}

class User {
    private String name;
    private double balance;
    private ArrayList<Stock> portfolioStocks;
    private ArrayList<Integer> portfolioQuantities;
    private ArrayList<Transaction> transactions;

    public User(String name, double balance) {
        this.name = name;
        this.balance = balance;
        portfolioStocks = new ArrayList<>();
        portfolioQuantities = new ArrayList<>();
        transactions = new ArrayList<>();
    }

    public void buyStock(Stock stock, int quantity) {
        if (quantity <= 0) {
            System.out.println("Invalid quantity.");
            return;
        }

        double totalCost = stock.getPrice() * quantity;

        if (totalCost > balance) {
            System.out.println("Insufficient balance.");
            return;
        }

        int index = findStock(stock.getSymbol());

        if (index == -1) {
            portfolioStocks.add(stock);
            portfolioQuantities.add(quantity);
        } else {
            int newQuantity = portfolioQuantities.get(index) + quantity;
            portfolioQuantities.set(index, newQuantity);
        }

        balance -= totalCost;

        transactions.add(
                new Transaction("BUY", stock.getSymbol(), quantity, stock.getPrice())
        );

        System.out.printf("Successfully bought %d shares of %s.%n",
                quantity, stock.getSymbol());
        System.out.printf("Amount paid: Rs. %.2f%n", totalCost);
    }

    public void sellStock(Stock stock, int quantity) {
        if (quantity <= 0) {
            System.out.println("Invalid quantity.");
            return;
        }

        int index = findStock(stock.getSymbol());

        if (index == -1) {
            System.out.println("You do not own this stock.");
            return;
        }

        int ownedQuantity = portfolioQuantities.get(index);

        if (quantity > ownedQuantity) {
            System.out.println("You do not have enough shares to sell.");
            return;
        }

        double totalValue = stock.getPrice() * quantity;
        balance += totalValue;

        int remainingQuantity = ownedQuantity - quantity;

        if (remainingQuantity == 0) {
            portfolioStocks.remove(index);
            portfolioQuantities.remove(index);
        } else {
            portfolioQuantities.set(index, remainingQuantity);
        }

        transactions.add(
                new Transaction("SELL", stock.getSymbol(), quantity, stock.getPrice())
        );

        System.out.printf("Successfully sold %d shares of %s.%n",
                quantity, stock.getSymbol());
        System.out.printf("Amount received: Rs. %.2f%n", totalValue);
    }

    private int findStock(String symbol) {
        for (int i = 0; i < portfolioStocks.size(); i++) {
            if (portfolioStocks.get(i).getSymbol().equalsIgnoreCase(symbol)) {
                return i;
            }
        }
        return -1;
    }

    public void displayPortfolio() {
        System.out.println("\n========== MY PORTFOLIO ==========");

        if (portfolioStocks.isEmpty()) {
            System.out.println("Your portfolio is empty.");
            return;
        }

        double totalPortfolioValue = 0;

        System.out.printf("%-10s %-8s %-12s %-15s%n",
                "Symbol", "Qty", "Price", "Total Value");
        System.out.println("-----------------------------------------------");

        for (int i = 0; i < portfolioStocks.size(); i++) {
            Stock stock = portfolioStocks.get(i);
            int quantity = portfolioQuantities.get(i);

            double value = stock.getPrice() * quantity;
            totalPortfolioValue += value;

            System.out.printf("%-10s %-8d Rs. %-9.2f Rs. %-10.2f%n",
                    stock.getSymbol(),
                    quantity,
                    stock.getPrice(),
                    value);
        }

        System.out.println("-----------------------------------------------");
        System.out.printf("Total Portfolio Value: Rs. %.2f%n", totalPortfolioValue);
    }

    public void displayBalance() {
        System.out.printf("Available Balance: Rs. %.2f%n", balance);
    }

    public void displayTransactions() {
        System.out.println("\n========== TRANSACTION HISTORY ==========");

        if (transactions.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }

        System.out.printf("%-8s %-10s %-8s %-10s%n",
                "Type", "Stock", "Quantity", "Price");
        System.out.println("------------------------------------------");

        for (Transaction transaction : transactions) {
            transaction.display();
        }
    }
}

public class StockTradingPlatform {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Stock[] market = {
                new Stock("TCS", "Tata Consultancy Services", 3500.00),
                new Stock("INFY", "Infosys", 1800.00),
                new Stock("RELIANCE", "Reliance Industries", 2900.00),
                new Stock("HDFC", "HDFC Bank", 1650.00)
        };

        System.out.println("======================================");
        System.out.println("       STOCK TRADING PLATFORM");
        System.out.println("======================================");

        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        double initialBalance;

        while (true) {
            System.out.print("Enter initial balance (Rs.): ");
            initialBalance = sc.nextDouble();

            if (initialBalance >= 0) {
                break;
            }

            System.out.println("Balance cannot be negative.");
        }

        User user = new User(name, initialBalance);

        int choice;

        do {
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. View Market");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. View Balance");
            System.out.println("6. View Transaction History");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.println("\n========== MARKET DATA ==========");
                    System.out.printf("%-10s %-30s %-12s%n",
                            "Symbol", "Company", "Price");
                    System.out.println("--------------------------------------------------");

                    for (Stock stock : market) {
                        System.out.printf("%-10s %-30s Rs. %.2f%n",
                                stock.getSymbol(),
                                stock.getName(),
                                stock.getPrice());
                    }
                    break;

                case 2:
                    System.out.println("\nSelect a stock to buy:");

                    for (int i = 0; i < market.length; i++) {
                        System.out.println((i + 1) + ". "
                                + market[i].getSymbol()
                                + " - Rs. "
                                + String.format("%.2f", market[i].getPrice()));
                    }

                    System.out.print("Enter stock number: ");
                    int buyChoice = sc.nextInt();

                    if (buyChoice < 1 || buyChoice > market.length) {
                        System.out.println("Invalid stock selection.");
                        break;
                    }

                    System.out.print("Enter quantity: ");
                    int buyQuantity = sc.nextInt();

                    user.buyStock(market[buyChoice - 1], buyQuantity);
                    break;

                case 3:
                    System.out.println("\nSelect a stock to sell:");

                    for (int i = 0; i < market.length; i++) {
                        System.out.println((i + 1) + ". "
                                + market[i].getSymbol()
                                + " - Rs. "
                                + String.format("%.2f", market[i].getPrice()));
                    }

                    System.out.print("Enter stock number: ");
                    int sellChoice = sc.nextInt();

                    if (sellChoice < 1 || sellChoice > market.length) {
                        System.out.println("Invalid stock selection.");
                        break;
                    }

                    System.out.print("Enter quantity: ");
                    int sellQuantity = sc.nextInt();

                    user.sellStock(market[sellChoice - 1], sellQuantity);
                    break;

                case 4:
                    user.displayPortfolio();
                    break;

                case 5:
                    user.displayBalance();
                    break;

                case 6:
                    user.displayTransactions();
                    break;

                case 7:
                    System.out.println("\nThank you for using Stock Trading Platform!");
                    break;

                default:
                    System.out.println("Invalid choice. Please select 1-7.");
            }

        } while (choice != 7);

        sc.close();
    }
}