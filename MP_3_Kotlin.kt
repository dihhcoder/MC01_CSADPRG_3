/* 
********************
Last names: ALCANTARA, ANGELES, CONSTANTINO, MATIBAG
Language: Kotlin
Paradigm(s): Functional, Object-Oriented
********************
*/

// To compile and run Kotlin you need to have a Kotlin compiler 
// and Java Development Kit (JDK) installed and set up
// or use IntelliJ IDEA IDE

// Use this to compile and run the program:
// kotlinc MP_3_Kotlin.kt -include-runtime -d MP_3_Kotlin.jar && java -jar MP_3_Kotlin.jar

// to roundup for interest
import kotlin.math.ceil

// currency to pass
fun getCurrencyList(): List<String> {
    return listOf("PHP", "USD", "JPY", "GBP", "EUR", "CNY")
}

// exchange rates 
fun getExchangeRates(): MutableMap<String, Double> {
    return mutableMapOf(
        "PHP" to 1.00,
        "USD" to 62.00,
        "JPY" to 0.40,
        "GBP" to 84.00,
        "EUR" to 72.00,
        "CNY" to 9.00
    )
}

// Y/N menu loop
fun menu(): Boolean {
    
    var decision: String
    var loop = true
    var verdict = true

    while (loop) {
        print("\nBack to the Main Menu (Y/N): ")
        decision = readln().uppercase()
        print("\n")
        if (decision == "Y") {
            loop = false
            verdict = true
        }
        else if (decision == "N") {
            loop = false
            verdict = false
            println("Thank you for using the Banking and Currency Exchange Application.")
        } else {
            println("Invalid input! Y or N only.")
        }
    }
    return verdict
}

// Currency choices
fun currencyMenu() {
    println("[1] Philippine Peso (PHP)")
    println("[2] United States Dollar (USD)")
    println("[3] Japanese Yen (JPY)")
    println("[4] British Pound Sterling (GBP)")
    println("[5] Euro (EUR)")
    println("[6] Chinese Yuan Renminni (CNY)")
}

// Option 1 (Only 1 Acc)
// Kotlin can use classes to make multiple acc maybe next time
fun register(account: String):String {
    
    var name: String
    var loop = true

    // Acc will be initialized as "N/A"
    // When Acc is no longer "N/A" it can no longer be changed until next run of the program
    do {
        if (account == "N/A") {
            println("Register Account Name")
            print("Account Name: ")
            name = readln()
            println("Thank you for registering!")
            loop = false
        } else {
            println("Account already made!")
            name = account
            loop = false
        }
    } while (loop)
    return name 
}

// Option 2
fun deposit(name: String, balance: Double, currency: String, currencyList: List<String>, rates: MutableMap<String, Double>): Double {
    
    var choice: Int
    var transactionCurrency: String
    var amount: Double
    var conversionRate: Double
    var convertedAmount: Double

    println("Deposit Amount")
    println("Account Name: $name")
    println("Current Balance: %.2f".format(balance))
    println("Currency: $currency")
    println("\nTransaction Currency Options:")
    currencyMenu()
    
    do {
        print("\nTransaction Currency: ")
        choice = readln().toIntOrNull() ?: -1
        if (choice !in 1..currencyList.size)
            println("Invalid choice!")
    } while (choice !in 1..currencyList.size)

    transactionCurrency = currencyList[choice-1]

    do {
        print("\nDeposit Amount in $transactionCurrency: ")
        amount = readln().toDoubleOrNull() ?: 0.00
        if (amount < 0)
            println("Invalid amount!")
    } while (amount < 0)

    conversionRate = rates[transactionCurrency] ?: 1.0
    convertedAmount = amount * conversionRate
    println("Converted Amount: %.2f PHP (1.00 $transactionCurrency = %.2f $currency)".format(convertedAmount, conversionRate))
    return convertedAmount
}

// Option 3
fun withdraw(name: String, balance: Double, currency: String, currencyList: List<String>, rates: MutableMap<String, Double>): Double {
    
    var choice: Int
    var transactionCurrency: String
    var amount: Double
    var conversionRate: Double
    var convertedAmount: Double

    println("Withdraw Amount")
    println("Account Name: $name")
    println("Current Balance: %.2f".format(balance))
    println("Currency: $currency")
    println("\nTransaction Currency Options:")
    currencyMenu()

    do {
        print("\nTransaction Currency: ")
        choice = readln().toIntOrNull() ?: -1
        if (choice !in 1..currencyList.size)
            println("Invalid choice!")
    } while (choice !in 1..currencyList.size)

    transactionCurrency = currencyList[choice-1]
    conversionRate = rates[transactionCurrency] ?: 1.0

    do {
        print("\nWithdraw Amount in $transactionCurrency: ")
        amount = readln().toDoubleOrNull() ?: 0.00
        convertedAmount = amount * conversionRate
        if (convertedAmount < 0 || convertedAmount > balance)
            println("Invalid amount!")
    } while (convertedAmount < 0 || convertedAmount > balance)

    println("Converted Amount: %.2f PHP (1.00 $transactionCurrency = %.2f $currency)".format(convertedAmount, conversionRate))
    return convertedAmount
}

// Option 4
fun exchange(currencyList: List<String>, rates: MutableMap<String, Double>) {
    
    var currencyLoop = true
    var choiceLoop = true
    var choiceCurrency: String
    var choice: Int
    var sourceCurrency: String
    var sourceRate: Double
    var sourceAmount: Double
    var exchangeCurrency: String
    var exchangeRate: Double
    var exchangeAmount: Double

    while (currencyLoop) {
        println("Foreign Currency Exchange")
        println("Source Currency Option:")
        currencyMenu()
        
        do {
            print("\nSource Currency: ")
            choice = readln().toIntOrNull() ?: -1
            if (choice !in 1..currencyList.size)
                println("Invalid choice!")
        } while (choice !in 1..currencyList.size)
        
        sourceCurrency = currencyList[choice-1]
        sourceRate = rates[sourceCurrency] ?: 1.0

        do {
            print("Source Amount: ")
            sourceAmount = readln().toDoubleOrNull() ?: 0.00
        } while (sourceAmount <= 0)

        println("\nExchange Currency Option:")
        currencyMenu()

        do {
            print("\nExchange Currency: ")
            choice = readln().toIntOrNull() ?: -1
            if (choice !in 1..currencyList.size)
                println("Invalid choice!")
        } while (choice !in 1..currencyList.size)

        exchangeCurrency = currencyList[choice-1]
        exchangeRate = rates[exchangeCurrency] ?: 1.0

        exchangeAmount = sourceAmount * (sourceRate/exchangeRate)
        print("Exchange Amount: %.2f\n".format(exchangeAmount))
        
        // Convert again loop
        do {
            print("\nConvert another currency (Y/N) ... ")
            choiceCurrency = readln().uppercase()
            if (choiceCurrency == "Y") {
                print("\n")
                choiceLoop = false
            }
            else if (choiceCurrency == "N")
                currencyLoop = false
                choiceLoop = false
        } while (choiceLoop)
}
}

// Option 5
fun record(currencyList: List<String>, oldRates: MutableMap<String, Double>) {
    
    var choice: Int
    var exchangeCurrency: String
    var exchangeRate: Double
    
    // to circumvent immutability of parameters reassign it lol
    var rates = oldRates

    println("Record Exchange Rates\n")
    currencyMenu()
    
    do {
        print("\nSelect Currency: ")
        choice = readln().toIntOrNull() ?: -1
        if (choice !in 1..currencyList.size)
            println("Invalid choice!")
    } while (choice !in 1..currencyList.size)
                
    do {
        print("Exchange Rate: ")
        exchangeRate = readln().toDoubleOrNull() ?: 0.00
    } while (exchangeRate <= 0)
                
    exchangeCurrency = currencyList[choice-1]
    rates[exchangeCurrency] = exchangeRate
}

// Option 6
fun interest(name: String, balance: Double, currency: String, interest: Double, daily: Double, years: Double) {
    
    var days: Int
    
    // reassigned
    var newDaily = daily
    var newBalance = balance

    var rawDaily: Double
    var roundedBalance: Double

    println("Show Interest Amount")
    println("Account Name: $name")
    println("Current Balance: %.2f".format(newBalance))
    println("Currency: $currency")
    println("Interest Rate: %.2f %%".format(interest*100))

    do {
        print("\nTotal Number of Days: ")
        days = readln().toIntOrNull() ?: -1
        if (days < 1 || days > 30)
            println("Invalid number of days!")
    } while (days < 1 || days > 30) 

    for (i in 1..days) {
        rawDaily = newBalance * (interest / years)
        newDaily = ceil(rawDaily * 100.0) / 100.0
        newBalance += newDaily
        roundedBalance = ceil(newBalance * 100.0) / 100.0
        println("$i \t| ${"%.2f".format(newDaily)} \t| ${"%.2f".format(roundedBalance)} \t|")
    }
}

fun main() {
    var loop = true
    var nameLoop = true
    var choice: Int
    var accountName: String = "N/A"
    var currentBalance: Double = 00.00
    var newBalance: Double = 0.00
    var depositAmount: Double = 0.00
    var withdrawAmount: Double = 0.00
    var defaultCurrency: String = "PHP"
    var interest: Double = 0.05
    var daysYear: Double = 365.00
    var dailyInterest: Double = 0.00

    var myCurrencies = getCurrencyList()
    var myRates = getExchangeRates()
    
    while (loop) {
    println("Select Transaction:")
    println("[1] Register Account Name")
    println("[2] Deposit Amount")
    println("[3] Withdraw Amount")
    println("[4] Currency Exchange")
    println("[5] Record Exchange Rates")
    println("[6] Show Interest Amount")
    print("\nChoice: ")
    choice = readln().toIntOrNull() ?: -1
    print("\n")

    when (choice) {
        1 -> {
                accountName = register(accountName)
                loop = menu()
            }
        
        2 -> {
                // loop when accountName is "N/A"
                do {
                if (accountName == "N/A") {
                    println("Register First!")
                    nameLoop = false
                } else {
                    depositAmount = deposit(accountName,currentBalance,defaultCurrency,myCurrencies,myRates)
                    currentBalance += depositAmount
                    println("Updated Balance: %.2f".format(currentBalance))
                    nameLoop = false
                }
                } while (nameLoop)
                loop = menu()

            }

        3 -> {
                // loop when accountName is "N/A"
                do {
                if (accountName == "N/A") {
                    println("Register First!")
                    nameLoop = false
                } else if (currentBalance <= 0.00){
                    println("Account has no balance!")
                    nameLoop = false
                } else {
                    withdrawAmount = withdraw(accountName,currentBalance,defaultCurrency,myCurrencies,myRates)
                    currentBalance -= withdrawAmount
                    println("Updated Balance: %.2f".format(currentBalance))
                    nameLoop = false
                }
                } while (nameLoop)
                loop = menu()
            }

        4 -> {
                exchange(myCurrencies,myRates)
                loop = menu()
            }

        5 -> {
                record(myCurrencies,myRates)
                loop = menu()
            }

        6 -> {
                // loop when accountName is "N/A"
                do {
                if (accountName == "N/A") {
                    println("Register First!")
                    nameLoop = false
                } else if (currentBalance <= 0.00){
                    println("Account has no balance!")
                    nameLoop = false
                } else {
                    interest(accountName,currentBalance,defaultCurrency,interest,dailyInterest,daysYear)
                    nameLoop = false
                }
                } while (nameLoop)
                loop = menu()
            }

        else -> println("Invalid choice!\n")
    }
    }
}
