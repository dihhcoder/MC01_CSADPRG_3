/********************
Last names: ALCANTARA, ANGELES, CONSTANTINO, MATIBAG
Language: Java
Paradigm(s): Procedural, Object-Oriented
********************/
import java.util.*;

import javax.swing.JPopupMenu.Separator;

//In order to run the program, you need to have Java installed on your computer. 
//You can download it from the official website: https://www.java.com/en/download/
//Once you have Java installed, you can compile and run the program using the command line or terminal.
//To compile the program, navigate to the directory where the MP_3_Java.java file is located and run the following command:
//javac MP_3_Java.java
//This will create a MP_3_Java.class file in the same directory.
//To run the program, use the following command:
//java MP_3_Java
//Enjoy using the program! If you encounter any issues, please refer to the creators for assistance.

public class MP_3_Java {
    //separator constant string
    public static final String SEPARATOR = "==============================================";
    //constant string for messages
    public static final String MSG00 = "GOODBYE AND THANK YOU FOR USING OUR SYSTEM";
    public static final String MSG01 = "\tWELCOME TO THE BANKING SYSTEM";
    public static final String MSG02 = "Operation Cancelled.";
    public static final String MSG03 = "Type \"Cancel\" to cancel transaction.";
    public static final String MSG04 = "Register Account Name:";
    public static final String MSG05 = "Account for %s Registered Successfully!%n";
    public static final String MSG06 = "Foreign Currency Exchange:";
    public static final String MSG07 = "Source Currency Options:";
    public static final String MSG08 = "Exchanged Currency Options:";
    public static final String MSG09 = "Exchanged Amount: %.2f %s%n";
    public static final String MSG10 = "Record Exchange Rate:";
    public static final String MSG11 = "Exchange Rate for %s Recorded Successfully!%n";
    public static final String MSG12 = "Withdraw Balance:";
    public static final String MSG13 = "Choose Currency For Transaction:";
    public static final String MSG14 = "Current %s Balance: %.2f %n";
    public static final String MSG15 = "Successfully Withdrawn %s %.2f From Your Account!%n";
    public static final String MSG16 = "Updated Account Balance: %s %.2f %n";
    public static final String MSG17 = "Deposit Money To Account:";
    public static final String MSG18 = "Successfully Deposited %s %.2f To Your Account!%n";
    public static final String MSG19 = "Simulate Account Interest Amount:";
    public static final String MSG20 = "Interest Rate: %.2f%% %n";
    public static final String MSG21 = "  Day |  Interest Amount   |  Balance (%s)%n";
    public static final String MSG22 = "%-5d |  %-17.2f |  %.2f %n";
    public static final String MSG23 = "Current Exchange Rate: %.2f %n";

    //constant string for menus
    public static final String MNU00 = """
                                    Select your transaction:
                                    [1] Register Account Name
                                    [2] Deposit Amount
                                    [3] Withdraw Amount
                                    [4] Currency Exchange
                                    [5] Record Exchange Rates
                                    [6] Show Interest Amount
                                    [0] Exit
                                    """;
    public static final String MNU01 = """
                                    [1] Philippine Peso (PHP)
                                    [2] United States Dollar (USD)
                                    [3] Japanese Yen (JPY)
                                    [4] British Pound Sterling (GBP)
                                    [5] Euro (EUR)
                                    [6] Chinese Yuan Renminni (CNY)
                                    """;
    public static final String MNU02 = """
                                    [1] United States Dollar (USD)
                                    [2] Japanese Yen (JPY)
                                    [3] British Pound Sterling (GBP)
                                    [4] Euro (EUR)
                                    [5] Chinese Yuan Renminni (CNY)
                                    """;
    
    //constant string for prompt messages
    public static final String PRM00 = "Select a transaction: ";
    public static final String PRM01 = "Would you like to go back to the menu? If not, you will repeat transaction. (Y/N): ";
    public static final String PRM02 = "Input Account Name: ";
    public static final String PRM03 = "Choose Source Currency: ";
    public static final String PRM04 = "Input Source Amount: ";
    public static final String PRM05 = "Choose Exchanged Currency: ";
    public static final String PRM06 = "Choose Foreign Currency: ";
    public static final String PRM07 = "Exchange Rate: ";
    public static final String PRM08 = "Select Currency: ";
    public static final String PRM09 = "Input Desired Withdraw Amount: ";
    public static final String PRM10 = "Input Desired Deposit Amount: ";
    public static final String PRM11 = "Input Desired Number of Days: ";

    //constant string for error messages
    public static final String ERR00 = "ERROR: Input cannot be left blank!";
    public static final String ERR01 = "ERROR: Input is not valid!";
    public static final String ERR02 = "ERROR: Input should be between %d and %d!%n";
    public static final String ERR03 = "ERROR: Input should be between %.2f and %.2f!%n";
    public static final String ERR04 = "ERROR: Input should be at least %.2f!%n";
    public static final String ERR05 = "ERROR: Input should be 'Y' or 'N'!";
    public static final String ERR06 = "ERROR: Account already exists!";
    public static final String ERR07 = "ERROR: Currency currently not available!";
    public static final String ERR08 = "ERROR: Account doesn't exist!";
    public static final String ERR09 = "ERROR: Insufficient Balance!";

    public static final double INTEREST_RATE = 0.05; //Interest Rate

    public static void main(String[] args){
        System.out.println(SEPARATOR);
        System.out.println(MSG01);
        System.out.println(SEPARATOR);
        try(Scanner keyboard = new Scanner(System.in)){
            int choice = -1;
            boolean backToMenu;
            ArrayList<Account> accs = new ArrayList<Account>();
            Map<String, Currency> exchangeRates = new LinkedHashMap<String, Currency>();    
            setUp(exchangeRates);
            while(choice == -1){
                backToMenu = false;
                while(!backToMenu){
                    try{
                        if(choice == -1){
                            choice = printMenu(keyboard);
                        }
                        caller(choice, accs, keyboard, exchangeRates);
                        if(choice != 0){
                            backToMenu = promptBack(keyboard);
                            if(backToMenu){
                                choice = -1;
                            }
                        } 
                    } catch(CancelledException _){
                        System.out.println(MSG02);
                        backToMenu = promptBack(keyboard);
                        if(backToMenu){
                            choice = -1;
                        }
                    }
                }
            }
        } 
    }

    //sets up default exchange rates
    public static void setUp(Map<String, Currency> exchangeRates){
        exchangeRates.put("PHP", new Currency("Philippine Peso", 1.0));
        exchangeRates.put("USD", new Currency("United States Dollar", 52.00));
        exchangeRates.put("JPY", new Currency("Japanese Yen", 0.40));
        exchangeRates.put("GBP", new Currency("British Pound", 84.00));
        exchangeRates.put("EUR", new Currency("Euro", 72.00));
        exchangeRates.put("CNY", new Currency("Chinese Yuan Reminni", 9.00));
    }

    //calls functions based on menu input
    public static void caller(int choice, ArrayList<Account> accs, Scanner keyboard, Map<String, Currency> exchangeRates){
        switch(choice){
            case 1:
                regAccName(accs, keyboard);
                break;
            case 2:
                depositAmount(accs, keyboard, exchangeRates);
                break;
            case 3:
                withdrawAmount(accs, keyboard, exchangeRates);
                break;
            case 4:
                currEx(keyboard, exchangeRates, accs);
                break;
            case 5:
                recordExRate(keyboard, exchangeRates);
                break;
            case 6:
                showInterestAmount(accs, keyboard, exchangeRates);
                break;
            case 0:
                System.out.println(MSG00);
                System.out.println(SEPARATOR);
                System.exit(0);
                break;
            default:
                System.out.println(ERR01);
        }
    }

    //prints menu and takes transaction choice
    public static int printMenu(Scanner keyboard){
        int choice = -1;
        while(choice == -1){
            System.out.println(MNU00);
            choice = getInt(PRM00, keyboard, 0, 6, true);
            System.out.println(SEPARATOR);
        }
        return choice;
    }

    //prints prompt to go back to menu and takes boolean input
    public static boolean promptBack(Scanner keyboard){
        System.out.println(SEPARATOR);
        String input = getString(PRM01, keyboard, true);
        System.out.println(SEPARATOR);
        if(input.equalsIgnoreCase("Y")){
            return true;
        } else if (input.equalsIgnoreCase("N")){
            return false;
        } else {
            System.out.println(ERR05);
            return promptBack(keyboard);
        }
    }

    //function definition for registering account name
    public static void regAccName(ArrayList<Account> list, Scanner keyboard){
        System.out.println(MSG04);
        String name = getString(PRM02, keyboard, false);
        boolean exists = false;
        for(Account acc: list){
            if(name.equalsIgnoreCase(acc.getName())){
                exists = true;
            }
        }
        if(exists){
            System.out.println(ERR06);
        } else {
            list.add(new Account(name, 0.0, INTEREST_RATE));
            System.out.printf(MSG05, name);
        }
    }

    //function definition for currency exchange feature
    public static void currEx(Scanner keyboard, Map<String, Currency> exRate, ArrayList<Account> list){
        System.out.println(MSG06);
        System.out.println(MSG07);
        System.out.println(MNU01);
        int sourceCurrency = getInt(PRM03, keyboard, 1, 6, false);
        System.out.println(SEPARATOR);
        double sourceAmount = getDouble(PRM04, keyboard, 0.0, Double.POSITIVE_INFINITY, false);
        System.out.println(SEPARATOR);
        System.out.println(MSG08);
        System.out.println(MNU01);
        int exchangedCurrency = getInt(PRM05, keyboard, 1, 6, false);
        System.out.println(SEPARATOR);
        double exchangedAmount = convertCurrency(sourceAmount, exRate.get(getCurrencyCode(sourceCurrency)).getValue(), exRate.get(getCurrencyCode(exchangedCurrency)).getValue());
        System.out.printf(MSG09, exchangedAmount, getCurrencyCode(exchangedCurrency));
    }

    //function definition for recording/modifying exchange rates
    public static void recordExRate(Scanner keyboard, Map<String, Currency> exRate){
        System.out.println(MSG10);
        System.out.println(MNU02);
        int foreignCurrency = getInt(PRM06, keyboard, 0, 5, false);
        foreignCurrency++;
        System.out.println(SEPARATOR);
        System.out.printf(MSG23, exRate.get(getCurrencyCode(foreignCurrency)).getValue());
        double exchangeRate = getDouble(PRM07, keyboard, 0, Double.POSITIVE_INFINITY, false);
        System.out.println(SEPARATOR);
        if(exRate.containsKey(getCurrencyCode(foreignCurrency))){
            exRate.get(getCurrencyCode(foreignCurrency)).setValue(exchangeRate);
            System.out.printf(MSG11, getCurrencyCode(foreignCurrency));
            displayExRates(exRate);
        } else {
            System.out.println(ERR07);
        }
    }

    //helper function for getting currency code(e.g. "PHP", "USD", "JPY")
    public static String getCurrencyCode(int choice){
        switch(choice){
            case 1:
                return "PHP";
            case 2:
                return "USD";
            case 3:
                return "JPY";
            case 4:
                return "GBP";
            case 5:
                return "EUR";
            case 6:
                return "CNY";
            default:
                return "";
        }
    }

    //function definition for displaying current exchange rates set
    public static void displayExRates(Map<String, Currency> exchangeRates){
        System.out.println("Current Exchange Rates:");
        for(Map.Entry<String, Currency> entry : exchangeRates.entrySet()){
           System.out.printf("%-30s: %.2f\n", entry.getValue().getName() + " (" + entry.getKey() + ")", entry.getValue().getValue());
        }
    }

    //function definition for simulating interest amount and total for given day value
    public static void showInterestAmount(ArrayList<Account> list, Scanner keyboard, Map<String, Currency> exchangeRates){
        int accKey = -1;
        System.out.println(MSG19);
        String nameKey = getString(PRM02, keyboard, false);
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getName().equalsIgnoreCase(nameKey)){
                accKey = i;
                break;
            }
        }
        if(accKey != -1){
            System.out.println(MSG13);
            System.out.println(MNU01);
            int currency = getInt(PRM08, keyboard, 1, 6, false);
            System.out.printf(MSG14, getCurrencyCode(currency), list.get(accKey).getBalance());
            System.out.printf(MSG20, list.get(accKey).getInterestRate() * 100);
            System.out.println(SEPARATOR);
            int days = getInt(PRM11, keyboard, 1, Integer.MAX_VALUE, false);
            System.out.println(SEPARATOR);
            System.out.printf(MSG21, getCurrencyCode(currency));
            System.out.println(SEPARATOR);
            double displayBalance = list.get(accKey).getBalance() * exchangeRates.get(getCurrencyCode(currency)).getValue();
            for(int i = 0; i < days; i++){
                //Daily Interest = (End-of-Day Balance) x (Annual Interest Rate / 365)
                double dailyInterest = displayBalance * (list.get(accKey).getInterestRate() / 365);
                dailyInterest = Math.round(dailyInterest * 100.0) / 100.0;
                displayBalance += dailyInterest;
                System.out.printf(MSG22, i + 1, dailyInterest, displayBalance);
            }
        } else {
            System.out.println(ERR08);
        }

    }

    //function definition for depositing php amounts through various currencies
    public static void depositAmount(ArrayList<Account> list, Scanner keyboard, Map<String, Currency> exRate){
        int accKey = -1;
        System.out.println(MSG17);
        String nameKey = getString(PRM02, keyboard, false);
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getName().equalsIgnoreCase(nameKey)){
                accKey = i;
                break;
            }
        }
        if(accKey != -1){
            System.out.println(MSG13);
            System.out.println(MNU01);
            int currency = getInt(PRM08, keyboard, 1, 6, false);
            double depositAmount = getDouble(PRM10, keyboard, 0.0, Double.POSITIVE_INFINITY, false);
            double toAdd = convertCurrency(depositAmount, exRate.get(getCurrencyCode(currency)).getValue(), 1.0);
            double newBalance = list.get(accKey).getBalance() + toAdd;
            list.get(accKey).setBalance(newBalance);
            System.out.println(SEPARATOR);
            System.out.printf(MSG18, getCurrencyCode(currency), depositAmount);
            System.out.printf(MSG16, getCurrencyCode(1), newBalance);
        } else {
            System.out.println(ERR08);
        }
    }

    //function definition for withdrawing php amounts through various currencies
    public static void withdrawAmount(ArrayList<Account> list, Scanner keyboard, Map<String, Currency> exRate){
        int accKey = -1;
        System.out.println(MSG12);
        String nameKey = getString(PRM02, keyboard, false);
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getName().equalsIgnoreCase(nameKey)){
                accKey = i;
                break;
            }
        }
        if(accKey != -1){
            System.out.println(MSG13);
            System.out.println(MNU01);
            int currency = getInt(PRM08, keyboard, 1, 6, false);
            double currBal = list.get(accKey).getBalance() * exRate.get(getCurrencyCode(currency)).getValue();
            System.out.printf(MSG14, getCurrencyCode(currency), currBal);
            double withdrawAmount = getDouble(PRM09, keyboard, 0.0, currBal, false);
            if(withdrawAmount > currBal){
                System.out.println(ERR09);
            } else {
                double withdrawAmountInPHP = convertCurrency(withdrawAmount, 1.0, exRate.get(getCurrencyCode(currency)).getValue());
                double newBalance = list.get(accKey).getBalance() - withdrawAmountInPHP;
                list.get(accKey).setBalance(newBalance);
                System.out.println(SEPARATOR);
                System.out.printf(MSG15, getCurrencyCode(currency), withdrawAmount);
                System.out.printf(MSG16, getCurrencyCode(1), newBalance);
            }
        } else {
            System.out.println(ERR08);
        }
    }
    
    //helper function for converting amount values given source and target currency rates
    public static double convertCurrency(double amount, double sourceRate, double targetRate){
        return (amount * sourceRate) / targetRate;
    }

    //helper function handling string input capture
    public static String getString(String prompt, Scanner keyboard, boolean isMenu){
        String input = "";
        boolean isValid = false;
        while(!isValid){
            if(!isMenu){
                System.out.println(MSG03);
            }
            System.out.print(prompt);
            input = keyboard.nextLine();
            if(input.trim().isEmpty()){
                System.out.println(ERR00);
                continue;
            }
            if(!isMenu && input.trim().equalsIgnoreCase("cancel")){
                throw new CancelledException();
            }
            isValid = true;
        }
        return input.trim();
    }

    //helper function handling integer input capture
    public static int getInt(String prompt, Scanner keyboard, int min, int max, boolean isMenu){
        String input = "";
        int choice = -1;
        boolean isValid = false;
        while(!isValid){
            try{
                input = getString(prompt, keyboard, isMenu);
                choice = Integer.parseInt(input);
                if(choice >= min && choice <= max){
                    isValid = true;
                } else {
                    System.out.printf(ERR02, min, max);
                }
            } catch(NumberFormatException _){
                System.out.println(ERR01);
            }   
        }
        return choice;
    }

    //helper function handling double input capture
    public static double getDouble(String prompt, Scanner keyboard, double min, double max, boolean isMenu){
        double input = -1.0;
        String scanned = "";
        boolean isValid = false;
        while(!isValid){    
            try{
                scanned = getString(prompt, keyboard, isMenu);
                input = Double.parseDouble(scanned);
                if(input >= min && input <= max){
                    isValid = true;
                } else {
                    if(max == Double.POSITIVE_INFINITY){
                        System.out.printf(ERR04, min);
                    } else {
                        System.out.printf(ERR03, min, max);
                    }   
                }
            } catch(NumberFormatException _){
                System.out.println(ERR01);
            }  
        }
        return input;
    }

    //nested class for account object
    public static class Account {
        private String name;
        private double balance;
        private double interestRate;

        public Account(String name, double balance, double interestRate) {
            this.name = name;
            this.balance = balance;
            this.interestRate = interestRate;
        }

        public String getName() {
            return name;
        }

        public double getBalance() {
            return balance;
        }

        public double getInterestRate() {
            return interestRate;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }
    }

    //nested class for currency object
    public static class Currency{
        private String name;
        private double value;

        public Currency(String name, double value){
            this.name = name;
            this.value = value;
        }

        public String getName(){
            return this.name;
        }

        public double getValue(){
            return this.value;
        }

        public void setValue(double value){
            this.value = value;
        }
    }

    //nested class for operation cancelling
    public static class CancelledException extends RuntimeException {}
}