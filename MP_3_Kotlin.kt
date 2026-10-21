/* 
********************
Last names: ALCANTARA, ANGELES, CONSTANTINO, MATIBAG
Language: Kotlin
Paradigm(s): Functional, Object-Oriented
********************
*/

fun menu(): Boolean {
    var decision: String
    print("\nBack to the Main Menu (Y/N): ")
    decision = readln()
    print("\n")
    if (decision == "Y" || decision == "y")
        return true
    else
        return false
}

fun register():String {
    println("Register Account Name")
    print("Account Name: ")
    return readln() 
}

fun deposit(name: String, balance: Double, currency: String): Double {
    println("Deposit Amount")
    println("Account Name: $name")
    println("Current Balance: %.2f".format(balance))
    println("Currency: $currency")
    print("\nDeposit Amount: ")
    return readln().toDouble()
}

fun withdraw(name: String, balance: Double, currency: String): Double {
    println("Withdraw Amount")
    println("Account Name: $name")
    println("Current Balance: %.2f".format(balance))
    println("Currency: $currency")
    print("\nWithdraw Amount: ")
    return readln().toDouble()
}

fun main() {
    var loop = true
    var currencyLoop = true
    var choice: Int
    var choiceCurrency: String
    var choiceRecord: Int
    var accountName: String = "N/A"
    var defaultBalance: Double = 1000.00
    var currentBalance: Double = 1000.00
    var newBalance: Double = 0.00
    var depositAmount: Double = 0.00
    var withdrawAmount: Double = 0.00
    var defaultCurrency: String = "PHP"
    var sourceCurrency: Int
    var exchangeCurrency: Int
    var sourceRate: Double = 0.0
    var exchangeRate: Double = 0.00
    var sourceAmount: Double = 0.00
    var exchangeAmount: Double = 0.0
    var interest: Int = 5
    var interestDays: Int
    var dailyInterest: Double

    var php: Double = 1.00
    var usd: Double = 62.00
    var jpy: Double = 0.40
    var gbp: Double = 84.00
    var eur: Double = 72.00
    var cny: Double = 9.00
    
    while (loop) {
    println("Select Transaction:")
    println("[1] Register Account Name")
    println("[2] Deposit Amount")
    println("[3] Withdraw Amount")
    println("[4] Currency Exchange")
    println("[5] Record Exchange Rates")
    println("[6] Show Interest Amount")
    print("\nChoice: ")
    choice = readln().toInt()
    print("\n")

    when (choice) {
        1 -> {
                accountName = register()
                loop = menu()
            }
        
        2 -> {
                depositAmount = deposit(accountName,defaultBalance,defaultCurrency)
                while (depositAmount < 0) {
                    println("Invalid amount!")
                    print("\nDeposit Amount: ")
                    depositAmount = readln().toDouble()
                }
                newBalance = defaultBalance + depositAmount
                println("Updated Balance: %.2f".format(newBalance))
                loop = menu()

            }

        3 -> {
                withdrawAmount = withdraw(accountName,defaultBalance,defaultCurrency)
                while (withdrawAmount > defaultBalance || withdrawAmount < 0) {
                    println("Invalid amount!")
                    print("\nWithdraw Amount: ")
                    withdrawAmount = readln().toDouble()
                }
                newBalance = defaultBalance - withdrawAmount
                println("Updated Balance: %.2f".format(newBalance))
                loop = menu()
            }

        4 -> {
                currencyLoop = true
                while (currencyLoop) {
                    println("Foreign Currency Exchange")
                    println("Source Currency Option:")
                    println("[1] Philippine Peso (PHP)")
                    println("[2] United States Dollar (USD)")
                    println("[3] Japanese Yen (JPY)")
                    println("[4] British Pound Sterling (GBP)")
                    println("[5] Euro (EUR)")
                    println("[6] Chinese Yuan Renminni (CNY)")
                    print("\nSource Currency: ")
                    sourceCurrency = readln().toInt()

                    while (sourceCurrency < 1 || sourceCurrency > 6) {
                        println("Invalid choice!")
                        print("\nSource Currency: ")
                        sourceCurrency = readln().toInt()
                    }

                        sourceRate = when (sourceCurrency) {
                            1 -> php
                            2 -> usd
                            3 -> jpy
                            4 -> gbp
                            5 -> eur
                            6 -> cny
                            else -> 0.0
                        }

                    print("Source Amount: ")
                    sourceAmount = readln().toDouble()

                    while (sourceAmount <= 0) {
                        println("Invalid amount!")
                        print("\nSource Amount: ")
                        sourceAmount = readln().toDouble()
                    }

                    println("\nExchanged Currency Option:")
                    println("[1] Philippine Peso (PHP)")
                    println("[2] United States Dollar (USD)")
                    println("[3] Japanese Yen (JPY)")
                    println("[4] British Pound Sterling (GBP)")
                    println("[5] Euro (EUR)")
                    println("[6] Chinese Yuan Renminni (CNY)")
                    print("\nExchange Currency: ")
                    exchangeCurrency = readln().toInt()

                    while (exchangeCurrency == sourceCurrency) {
                        println("Source Currency and Exchange Currency is the SAME!")
                        print("\nExchange Currency: ")
                        exchangeCurrency = readln().toInt()
                    }

                    while (exchangeCurrency < 1 || exchangeCurrency > 6) {
                        println("Invalid choice!")
                        print("\nExchange Currency: ")
                        exchangeCurrency = readln().toInt()
                    }

                        exchangeRate = when (exchangeCurrency) {
                            1 -> php
                            2 -> usd
                            3 -> jpy
                            4 -> gbp
                            5 -> eur
                            6 -> cny
                            else -> 0.0
                        }

                    exchangeAmount = sourceAmount * (sourceRate/exchangeRate)
                    print("Exchange Amount: %.2f".format(exchangeAmount))
                    print("\nConvert another currency (Y/N) ... ")
                    choiceCurrency = readln()
                    if (choiceCurrency == "Y" || choiceCurrency == "y") {
                        currencyLoop = true
                        print("\n")
                    }
                    else
                        currencyLoop = false
                }
                loop = menu()
            }

        5 -> {
                println("Record Exchange Rates\n")
                println("[1] Philippine Peso (PHP)")
                println("[2] United States Dollar (USD)")
                println("[3] Japanese Yen (JPY)")
                println("[4] British Pound Sterling (GBP)")
                println("[5] Euro (EUR)")
                println("[6] Chinese Yuan Renminni (CNY)")
                print("\nSelect Foreign Currency: ")
                choiceRecord = readln().toInt()

                while (choiceRecord < 1 || choiceRecord > 6) {
                    println("Invalid Foreign Currency!")
                    print("\nSelect Foreign Currency: ")
                    choiceRecord = readln().toInt()
                }

                print("Exchange Rate: ")
                exchangeRate = readln().toDouble()

                while (exchangeRate <= 0) {
                    println("Invalid Exchange Rate!")
                    print("\nExchange Rate: ")
                    exchangeRate = readln().toDouble()
                }
                
                when (choiceRecord) {
                    1 -> php = exchangeRate
                    2 -> usd = exchangeRate
                    3 -> jpy = exchangeRate
                    4 -> gbp = exchangeRate
                    5 -> eur = exchangeRate
                    6 -> cny = exchangeRate
                    else -> println("Invalid choice!\n")
                }

                loop = menu()
            }

        6 -> {
                println("Show Interest Amount")
                println("Account Name: $accountName")
                println("Current Balance: %.2f".format(currentBalance))
                println("Currency: $defaultCurrency")
                println("Interest Rate: $interest%")
                print("\nTotal Number of Days: ")
                interestDays = readln().toInt()

                while (interestDays < 1 || interestDays > 30) {
                    println("Invalid number of days!")
                    print("\nTotal Number of Days: ")
                    interestDays = readln().toInt()
                }

                for (i in 1..interestDays) {
                    dailyInterest = currentBalance * (0.05/365.00)
                    currentBalance += dailyInterest
                    println("$i \t| ${"%.2f".format(dailyInterest)} \t| ${"%.2f".format(currentBalance)} \t|")
                }
                loop = menu()
            }

        else -> println("Invalid choice!\n")
    }
    }
}


// Use this to run the program:
// kotlinc MP_3_Kotlin.kt -include-runtime -d MP_3_Kotlin.jar && java -jar MP_3_Kotlin.jar
