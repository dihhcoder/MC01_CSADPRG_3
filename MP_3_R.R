#********************
# Last names: ALCANTARA, ANGELES, CONSTANTINO, MATIBAG
# Language: R
# Paradigm(s): Procedural, Functional
#********************

# to run the program (requires R 4.0 or newer)
#   Rscript MP_3_R.R
# or open it in RStudio and click Source



# constants

INTEREST_RATE <- 0.05
DAYS_PER_YEAR <- 365

# php is the base currency so its rate is always 1
CURRENCY_CODES <- c("PHP", "USD", "JPY", "GBP", "EUR", "CNY")
CURRENCY_NAMES <- c("Philippine Peso", "United States Dollar", "Japanese Yen",
                    "British Pound Sterling", "Euro", "Chinese Yuan Renminni")




# input helpers

# stdin connection so it can work in rscript
input_con <- if (interactive()) NULL else file("stdin", open = "r")

# reads one line tapos exit na pag wala ng input
read_input <- function(prompt) {
  if (interactive()) {
    line <- readline(prompt)
  } else {
    cat(prompt)
    line <- readLines(input_con, n = 1)
    if (length(line) == 0) {
      cat("\n\nInput closed. Exiting program.\n")
      quit(save = "no")
    }
  }
  trimws(line)
}

# ask again until valid whole number between min and max
read_int <- function(prompt, min, max) {
  repeat {
    input <- read_input(prompt)
    if (grepl("^[0-9]{1,9}$", input)) {
      value <- as.integer(input)
      if (value >= min && value <= max) return(value)
    }
    cat(sprintf("Invalid input. Enter a whole number from %d to %d.\n", min, max))
  }
}

# ask again hanggang greater than zero yung number
read_amount <- function(prompt) {
  repeat {
    input <- read_input(prompt)
    if (grepl("^[0-9]*\\.?[0-9]+$|^[0-9]+\\.$", input)) {
      value <- as.numeric(input)
      if (value > 0) return(value)
    }
    cat("Invalid input. Enter a number greater than 0.\n")
  }
}

# only accepts y or n then true pag y
read_yes_no <- function(prompt) {
  repeat {
    input <- toupper(read_input(prompt))
    if (input %in% c("Y", "N")) return(input == "Y")
    cat("Invalid input. Enter Y or N.\n")
  }
}



# display helpers

# formats a number with two decimal places
money <- function(x) formatC(x, format = "f", digits = 2, big.mark = "")

print_main_menu <- function() {
  cat("\nSelect Transaction:\n")
  cat("[1] Register Account Name\n")
  cat("[2] Deposit Amount\n")
  cat("[3] Withdraw Amount\n")
  cat("[4] Currency Exchange\n")
  cat("[5] Record Exchange Rates\n")
  cat("[6] Show Interest Amount\n\n")
}

print_currency_menu <- function() {
  cat(sprintf("[%d] %s (%s)\n", seq_along(CURRENCY_CODES), CURRENCY_NAMES, CURRENCY_CODES), sep = "")
  cat("\n")
}

print_account_header <- function(account) {
  cat("Account Name: ", account$name, "\n", sep = "")
  cat("Current Balance: ", money(account$balance), "\n", sep = "")
  cat("Currency: PHP\n")
}

# returns false when no account is registered yet
has_account <- function(account) {
  if (is.null(account$name)) {
    cat("No account registered yet. Please use [1] Register Account Name first.\n")
    return(FALSE)
  }
  TRUE
}

# features

# req-0009 register the user's account name
register_account <- function(account) {
  cat("\nRegister Account Name\n")
  if (!is.null(account$name)) {
    cat("An account is already registered: ", account$name, "\n", sep = "")
    if (!read_yes_no("Replace it with a new account (balance resets to 0.00)? (Y/N): ")) {
      cat("Registration cancelled.\n")
      return(account)
    }
  }
  repeat {
    name <- read_input("Account Name: ")
    if (nchar(name) > 0) break
    cat("Account name cannot be empty.\n")
  }
  list(name = name, balance = 0)
}

# req-0004 req-0005 req-0008 deposit or withdraw in any currency
transact <- function(account, rates, is_deposit) {
  title <- if (is_deposit) "Deposit Amount" else "Withdraw Amount"
  cat("\n", title, "\n", sep = "")
  if (!has_account(account)) return(account)
  print_account_header(account)

  cat("\nTransaction Currency Options:\n")
  print_currency_menu()
  id <- read_int("Transaction Currency: ", 1, length(CURRENCY_CODES))
  code <- CURRENCY_CODES[id]

  cat("\n")
  amount <- read_amount(sprintf("%s (%s): ", title, code))

  # todo convert first to php because php yung balance

  # todo add the amount to the balance for deposit
  # subtract the amount for withdraw if the balance is enough
  # print the updated balance
  account
}

# req-0003 convert source amount to php then to the target currency
currency_exchange <- function(rates) {
  repeat {
    cat("\nForeign Currency Exchange\n")
    cat("Source Currency Option:\n")
    print_currency_menu()
    src <- CURRENCY_CODES[read_int("Source Currency: ", 1, length(CURRENCY_CODES))]
    amount <- read_amount("Source Amount: ")

    cat("\nExchanged Currency Options:\n")
    print_currency_menu()
    dst <- CURRENCY_CODES[read_int("Exchange Currency: ", 1, length(CURRENCY_CODES))]

    # todo compute the exchange amount
    # print the exchange amount

    if (!read_yes_no("\nConvert another currency (Y/N)? ")) break
  }
}

# req-0001 req-0002 record how many php equal one unit of a foreign currency
record_exchange_rate <- function(rates) {
  cat("\nRecord Exchange Rate\n\n")
  print_currency_menu()
  code <- CURRENCY_CODES[read_int("Select Foreign Currency: ", 1, length(CURRENCY_CODES))]

  # php cannot be changed since it is the base currency
  if (code == "PHP") {
    cat("PHP is the base currency. Its rate is fixed at 1.00 and cannot be changed.\n")
    return(rates)
  }

  # todo ask for the new exchange rate
  # save the new rate too
  rates
}

# req-0006 req-0007 show the daily interest for the entered number of days
show_interest <- function(account) {
  cat("\nShow Interest Amount\n")
  if (!has_account(account)) return(invisible(NULL))
  print_account_header(account)
  cat("Interest Rate: ", INTEREST_RATE * 100, "%\n\n", sep = "")

  # todo ask for the total number of days

  # this is only a projection so the real balance is not changed
  balance <- account$balance
  cat("\nDay  | Interest | Balance |\n")

  # todo compute the daily interest and balance for each day
  # print each day as a row in the table
}





# main

main <- function() {
  account <- list(name = NULL, balance = 0)

  # default rates in php per one unit and can be changed in option 5
  rates <- list(PHP = 1.00, USD = 52.00, JPY = 0.40, GBP = 84.00, EUR = 72.00, CNY = 9.00)

  repeat {
    print_main_menu()
    choice <- read_int("Choice: ", 1, 6)

    if (choice == 1) account <- register_account(account)
    if (choice == 2) account <- transact(account, rates, TRUE)
    if (choice == 3) account <- transact(account, rates, FALSE)
    if (choice == 4) currency_exchange(rates)
    if (choice == 5) rates <- record_exchange_rate(rates)
    if (choice == 6) show_interest(account)

    # currency exchange already asks its own y or n question
    if (choice != 4 && !read_yes_no("\nBack to the Main Menu (Y/N): ")) break
  }

  cat("\nThank you for using the Banking and Currency Exchange Application.\n")
}

main()
