package ui.console;

public interface IConsoleConstants {

    String ANSI_RESET = "\u001B[0m";
    String ANSI_BLACK = "\u001B[30m";
    String ANSI_RED = "\u001B[31m";
    String ANSI_GREEN = "\u001B[32m";
    String ANSI_YELLOW = "\u001B[33m";
    String ANSI_BLUE = "\u001B[34m";
    String ANSI_PURPLE = "\u001B[35m";
    String ANSI_CYAN = "\u001B[36m";
    String ANSI_WHITE = "\u001B[37m";

    String NON_EXISTENT_COMMAND = "The selected command doesn't exist.";

    String LOGIN_TEXT = ANSI_BLUE + "Select execution command id:\n"
            + "1. Login\n"
            + "2. Create user\n"
            + "3. Drop user\n"
            + "0. Exit\n"
            + ">> " + ANSI_RESET;

    String USER_MAIN_MENU_TEXT = ANSI_BLUE + "Select execution command id:\n"
            + "1. Show contacts\n"
            + "2. Sort contacts\n"
            + "3. Find contacts\n"
            + "4. Add contact\n"
            + "5. Drop contact\n"
            + "6. Edit user\n"
            + "7. Edit contact\n"
            + "8. Disconnect\n"
            + "0. Exit\n"
            + ">> " + ANSI_RESET;

    String SORTING_ORDER_TEXT = ANSI_BLUE + "Choose sort order:\n"
            + "1. Ascending\n"
            + "2. Descending\n"
            + "0. Back\n"
            + ">> " + ANSI_RESET;

    String SELECT_SORTING_OPTIONS_TEXT = ANSI_BLUE + "Select sorting options (several can be used):\n"
            + "1. Select all\n"
            + "2. Full name\n"
            + "3. First name\n"
            + "4. Middle name\n"
            + "5. Last name\n"
            + "6. Company\n"
            + "7. Telephone number\n"
            + "8. Email address\n"
            + "0. Back\n"
            + ">> " + ANSI_RESET;

    String SELECT_SEARCHING_OPTION_TEXT = ANSI_BLUE + "Select searching option:\n"
            + "1. Select all\n"
            + "2. Full name\n"
            + "3. Telephone number\n"
            + "4. Email address\n"
            + "5. Company\n"
            + "6. First name\n"
            + "7. Middle name\n"
            + "8. Last name\n"
            + "0. Back\n"
            + ">> " + ANSI_RESET;

}
