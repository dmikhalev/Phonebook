package ui.console;

import ui.console.views.ContactView;
import ui.console.views.UserView;


public class ConsoleApp {

    public static void main(String[] args) {
        UserView userView = new UserView();

        if (userView.showLoginMenu()) {
            userView.showLoggedMenu(new ContactView(userView));
        }
    }

}
