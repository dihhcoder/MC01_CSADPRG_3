/*
********************
Last names: ALCANTARA, ANGELES, CONSTANTINO, MATIBAG
Language: C
Paradigm(s): Procedural, Structured (imperative)
********************
*/

// To compile and run the program (requires a C compiler such as GCC):
//   gcc -std=c99 -Wall -Wextra -pedantic MP_3_C.c -o MP_3_C
//   ./MP_3_C        (on Windows: MP_3_C.exe)

#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

/* ----------------------------- constants ------------------------------ */

#define CURRENCY_COUNT        6
#define NAME_LEN              100
#define LINE_LEN              256

#define ANNUAL_INTEREST_PCT   5          /* 5% per annum                    */
#define DAYS_PER_YEAR         365
#define MAX_PROJECTION_DAYS   3650       /* sanity cap for REQ-0007 input   */

#define MAX_TXN_AMOUNT        1000000000.0          /* per transaction      */
#define MAX_RATE              1000000.0             /* PHP per 1 unit       */
#define MAX_BALANCE_CENTAVOS  100000000000000LL     /* 1 trillion PHP cap   */

/* ------------------------------- types -------------------------------- */

/* Menu number shown to the user is (id + 1). */
typedef enum { PHP = 0, USD, JPY, GBP, EUR, CNY } CurrencyId;

typedef struct {
    const char *code;
    const char *name;
    double      rate;      /* PHP per 1 unit of this currency */
} Currency;

typedef struct {
    char      name[NAME_LEN];
    long long balance;     /* in centavos */
    bool      registered;
} Account;

/* ------------------------- input helpers (I/O) ------------------------- */

static void endOfInput(void)
{
    printf("\n\nInput closed. Exiting program.\n");
    exit(0);
}

static void trim(char *s)
{
    size_t len = strlen(s);
    size_t start = 0;

    while (len > 0 && isspace((unsigned char)s[len - 1])) {
        s[--len] = '\0';
    }
    while (s[start] != '\0' && isspace((unsigned char)s[start])) {
        start++;
    }
    if (start > 0) {
        memmove(s, s + start, strlen(s + start) + 1);
    }
}

/* Prints the prompt, reads one full line, strips the newline and trims it. */
static void readLine(const char *prompt, char *buf, size_t size)
{
    size_t len;

    printf("%s", prompt);
    fflush(stdout);

    if (fgets(buf, (int)size, stdin) == NULL) {
        endOfInput();
    }

    len = strlen(buf);
    if (len > 0 && buf[len - 1] == '\n') {
        buf[len - 1] = '\0';
    } else {
        int ch;                       /* line was too long: discard the rest */
        while ((ch = getchar()) != '\n' && ch != EOF) {
            ;
        }
    }
    trim(buf);
}

/* Accepts only digits (a whole number). */
static bool parseWholeNumber(const char *s, long *out)
{
    char *end;

    if (*s == '\0') {
        return false;
    }
    for (const char *p = s; *p != '\0'; p++) {
        if (!isdigit((unsigned char)*p)) {
            return false;
        }
    }
    if (strlen(s) > 9) {              /* keeps the value safely inside long */
        return false;
    }
    *out = strtol(s, &end, 10);
    return true;
}

/* Accepts only digits with at most one '.', e.g. 500, 500.00, .5, 52. */
static bool parsePositiveDecimal(const char *s, double *out)
{
    int digits = 0;
    int dots = 0;

    for (const char *p = s; *p != '\0'; p++) {
        if (isdigit((unsigned char)*p)) {
            digits++;
        } else if (*p == '.') {
            dots++;
        } else {
            return false;
        }
    }
    if (digits == 0 || dots > 1) {
        return false;
    }
    *out = strtod(s, NULL);
    return *out > 0.0;
}

static int promptInt(const char *prompt, int min, int max)
{
    char buf[LINE_LEN];
    long value;

    for (;;) {
        readLine(prompt, buf, sizeof buf);
        if (parseWholeNumber(buf, &value) && value >= min && value <= max) {
            return (int)value;
        }
        printf("Invalid input. Enter a whole number from %d to %d.\n", min, max);
    }
}

static double promptAmount(const char *prompt, double max)
{
    char buf[LINE_LEN];
    double value;

    for (;;) {
        readLine(prompt, buf, sizeof buf);
        if (parsePositiveDecimal(buf, &value) && value <= max) {
            return value;
        }
        printf("Invalid input. Enter a number greater than 0 (up to %.0f).\n", max);
    }
}

static bool promptYesNo(const char *prompt)
{
    char buf[LINE_LEN];

    for (;;) {
        readLine(prompt, buf, sizeof buf);
        if (strlen(buf) == 1 && (toupper((unsigned char)buf[0]) == 'Y' ||
                                 toupper((unsigned char)buf[0]) == 'N')) {
            return toupper((unsigned char)buf[0]) == 'Y';
        }
        printf("Invalid input. Enter Y or N.\n");
    }
}

/* --------------------------- money helpers ----------------------------- */

static void formatCentavos(char *out, size_t size, long long centavos)
{
    snprintf(out, size, "%lld.%02lld", centavos / 100, centavos % 100);
}

/* amount (in some currency) -> PHP centavos, rounded to the nearest centavo */
static long long toPhpCentavos(double amount, double rate)
{
    return (long long)(amount * rate * 100.0 + 0.5);
}

/* Daily interest in centavos, rounded half up:
 * balance x (ANNUAL_INTEREST_PCT / 100) / DAYS_PER_YEAR, in integer math. */
static long long dailyInterest(long long balanceCentavos)
{
    const long long divisor = 100LL * DAYS_PER_YEAR;
    return (balanceCentavos * ANNUAL_INTEREST_PCT + divisor / 2) / divisor;
}

/* ---------------------------- menu helpers ----------------------------- */

static void printMainMenu(void)
{
    printf("\nSelect Transaction:\n");
    printf("[1] Register Account Name\n");
    printf("[2] Deposit Amount\n");
    printf("[3] Withdraw Amount\n");
    printf("[4] Currency Exchange\n");
    printf("[5] Record Exchange Rates\n");
    printf("[6] Show Interest Amount\n\n");
}

static void printCurrencyMenu(const Currency currencies[])
{
    for (int i = 0; i < CURRENCY_COUNT; i++) {
        printf("[%d] %s (%s)\n", i + 1, currencies[i].name, currencies[i].code);
    }
    printf("\n");
}

static CurrencyId promptCurrency(const char *prompt)
{
    return (CurrencyId)(promptInt(prompt, 1, CURRENCY_COUNT) - 1);
}

static void printAccountHeader(const Account *acc)
{
    char money[40];

    formatCentavos(money, sizeof money, acc->balance);
    printf("Account Name: %s\n", acc->name);
    printf("Current Balance: %s\n", money);
    printf("Currency: %s\n", "PHP");
}

static bool requireAccount(const Account *acc)
{
    if (!acc->registered) {
        printf("No account registered yet. Please use [1] Register Account Name first.\n");
        return false;
    }
    return true;
}

/* -------------------------- feature functions -------------------------- */

/* REQ-0009 */
static void registerAccount(Account *acc)
{
    char name[LINE_LEN];

    printf("\nRegister Account Name\n");

    if (acc->registered) {
        printf("An account is already registered: %s\n", acc->name);
        if (!promptYesNo("Replace it with a new account (balance resets to 0.00)? (Y/N): ")) {
            printf("Registration cancelled.\n");
            return;
        }
    }

    for (;;) {
        readLine("Account Name: ", name, sizeof name);
        if (name[0] == '\0') {
            printf("Account name cannot be empty.\n");
        } else if (strlen(name) >= NAME_LEN) {
            printf("Account name is too long (max %d characters).\n", NAME_LEN - 1);
        } else {
            break;
        }
    }

    strcpy(acc->name, name);
    acc->balance = 0;
    acc->registered = true;
    printf("Account registered.\n");
}

/* REQ-0004, REQ-0005, REQ-0008 */
static void transact(Account *acc, const Currency currencies[], bool isDeposit)
{
    const char *title = isDeposit ? "Deposit Amount" : "Withdraw Amount";
    char label[LINE_LEN];
    char money[40];
    CurrencyId id;
    double amount;
    long long phpCentavos;

    printf("\n%s\n", title);
    if (!requireAccount(acc)) {
        return;
    }
    printAccountHeader(acc);

    printf("\nTransaction Currency Options:\n");
    printCurrencyMenu(currencies);
    id = promptCurrency("Transaction Currency: ");

    snprintf(label, sizeof label, "%s (%s): ", title, currencies[id].code);
    amount = promptAmount(label, MAX_TXN_AMOUNT);
    phpCentavos = toPhpCentavos(amount, currencies[id].rate);

    if (phpCentavos == 0) {
        printf("Amount is too small (less than 0.01 PHP). Transaction cancelled.\n");
        return;
    }
    if (id != PHP) {
        formatCentavos(money, sizeof money, phpCentavos);
        printf("Converted Amount: %s PHP (1 %s = %.4f PHP)\n",
               money, currencies[id].code, currencies[id].rate);
    }

    if (isDeposit) {
        if (acc->balance + phpCentavos > MAX_BALANCE_CENTAVOS) {
            printf("Deposit would exceed the maximum allowed balance. Transaction cancelled.\n");
            return;
        }
        acc->balance += phpCentavos;
    } else {
        if (phpCentavos > acc->balance) {
            printf("Insufficient balance. Transaction cancelled.\n");
            return;
        }
        acc->balance -= phpCentavos;
    }

    formatCentavos(money, sizeof money, acc->balance);
    printf("Updated Balance: %s\n", money);
}

/* REQ-0001, REQ-0002 */
static void recordExchangeRates(Currency currencies[])
{
    CurrencyId id;
    char label[LINE_LEN];

    printf("\nRecord Exchange Rate\n\n");
    printCurrencyMenu(currencies);
    id = promptCurrency("Select Foreign Currency: ");

    if (id == PHP) {
        printf("PHP is the base currency; its rate is fixed at 1.00 and cannot be changed.\n");
        return;
    }

    printf("Enter how many PHP equal 1 %s.\n", currencies[id].code);
    snprintf(label, sizeof label, "Exchange Rate: ");
    currencies[id].rate = promptAmount(label, MAX_RATE);
    printf("Recorded: 1 %s = %.4f PHP\n", currencies[id].code, currencies[id].rate);
}

/* REQ-0003: source -> PHP -> destination */
static void currencyExchange(const Currency currencies[])
{
    do {
        CurrencyId src;
        CurrencyId dst;
        double amount;
        double result;

        printf("\nForeign Currency Exchange\n");
        printf("Source Currency Option:\n");
        printCurrencyMenu(currencies);
        src = promptCurrency("Source Currency: ");
        amount = promptAmount("Source Amount: ", MAX_TXN_AMOUNT);

        printf("\nExchanged Currency Options:\n");
        printCurrencyMenu(currencies);
        dst = promptCurrency("Exchange Currency: ");

        if (src == dst) {
            printf("Source and exchange currency are the same (%s). Nothing to convert.\n",
                   currencies[src].code);
        } else {
            result = amount * currencies[src].rate / currencies[dst].rate;
            printf("Exchange Amount: %.2f\n", result);
        }
    } while (promptYesNo("\nConvert another currency (Y/N)? "));
}

/* REQ-0006, REQ-0007: projection only, the real balance is not modified */
static void showInterest(const Account *acc)
{
    long long balance;
    long long interest;
    int days;
    char interestStr[40];
    char balanceStr[40];

    printf("\nShow Interest Amount\n");
    if (!requireAccount(acc)) {
        return;
    }
    printAccountHeader(acc);
    printf("Interest Rate: %d%%\n\n", ANNUAL_INTEREST_PCT);

    days = promptInt("Total Number of Days: ", 1, MAX_PROJECTION_DAYS);

    printf("\nDay  | Interest | Balance |\n");
    balance = acc->balance;
    for (int day = 1; day <= days; day++) {
        interest = dailyInterest(balance);
        balance += interest;
        formatCentavos(interestStr, sizeof interestStr, interest);
        formatCentavos(balanceStr, sizeof balanceStr, balance);
        printf("%-4d | %-8s | %-7s |\n", day, interestStr, balanceStr);
    }
}

/* -------------------------------- main -------------------------------- */

int main(void)
{
    Account account = { "", 0, false };
    Currency currencies[CURRENCY_COUNT] = {
        { "PHP", "Philippine Peso",          1.0 },   /* base currency; the rest are default rates */
        { "USD", "United States Dollar",     52.0 },
        { "JPY", "Japanese Yen",             0.40 },
        { "GBP", "British Pound Sterling",   84.0 },
        { "EUR", "Euro",                     72.0 },
        { "CNY", "Chinese Yuan Renminni",    9.0 }
    };
    bool running = true;

    while (running) {
        bool askBack = true;

        printMainMenu();
        switch (promptInt("Choice: ", 1, 6)) {
            case 1: registerAccount(&account);                 break;
            case 2: transact(&account, currencies, true);      break;
            case 3: transact(&account, currencies, false);     break;
            case 4: currencyExchange(currencies); askBack = false; break;
            case 5: recordExchangeRates(currencies);           break;
            case 6: showInterest(&account);                    break;
        }

        if (askBack) {
            running = promptYesNo("\nBack to the Main Menu (Y/N): ");
        }
    }

    printf("\nThank you for using the Banking and Currency Exchange Application.\n");
    return 0;
}
