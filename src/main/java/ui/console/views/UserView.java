package ui.console.views;

import backend.entity.User;
import backend.service.ContactService;
import backend.service.UserService;
import ui.console.IConsoleConstants;

import java.util.ArrayList;
import java.util.List;

import static ui.console.utils.ConsoleUtils.SCANNER;
import static ui.console.utils.ConsoleUtils.getIntCommand;


public class UserView {

    private UserService userService;
    private ContactService contactService;
    private Long userId;

    public UserView() {
        this.userService = new UserService();
    }

    public boolean showLoginMenu() {
        String username;
        String password;
        System.out.println();
        int command = getIntCommand(IConsoleConstants.LOGIN_TEXT);
        if (command == 1) {
            while (true) {
                System.out.println();
                System.out.print("Enter username: ");
                username = SCANNER.nextLine();
                System.out.print("Enter password: ");
                password = SCANNER.nextLine();
                System.out.println();
                if (userService.login(username, password)) {
                    System.err.println("Connected.");
                    userId = userService.getUser().getId();
                    contactService = new ContactService(userId);
                    return true;
                }
                System.err.println("User hasn't been found.");
            }
        } else if (command == 2) {
            createUser();
            return showLoginMenu();
        } else if (command == 3) {
            dropUser();
            return showLoginMenu();
        } else if (command == 0) {
            System.out.println("\nGoodbye!");
        } else {
            System.out.println("\n" + IConsoleConstants.NON_EXISTENT_COMMAND);
            showLoginMenu();
        }
        return false;
    }


    public void showLoggedMenu(ContactView contactView) {
        System.out.println();
        int command = getIntCommand(IConsoleConstants.USER_MAIN_MENU_TEXT);
        while (command != 0) {
            System.out.println();
            if (command == 1) {
                // show contacts
                contactView.printContacts(contactService.getAllContacts());
            } else if (command == 2) {
                // sort contacts
                contactView.showSortContactsMenu();
                System.out.println();
                contactView.printContacts(contactService.getAllContacts());
            } else if (command == 3) {
                // find contacts
                contactView.printFoundContacts(contactView.findContacts(), contactView.getKeyWord());
            } else if (command == 4) {
                // add contact
                contactView.addContact();
            } else if (command == 5) {
                // drop contact
                contactView.dropContact();
            } else if (command == 6) {
                // edit user
                editUser();
            } else if (command == 7) {
                // edit user
                contactView.editContact();
            } else if (command == 8) {
                // disconnect
                userService.logOut();
                contactService = null;
                System.err.println("Disconnected.");
                showLoginMenu();
            } else {
                System.out.println(IConsoleConstants.NON_EXISTENT_COMMAND);
            }
            System.out.println();
            command = getIntCommand(IConsoleConstants.USER_MAIN_MENU_TEXT);
        }
        System.out.println("\nGoodbye!");
    }


    private void createUser() {
        System.out.println();
        List<String> usernames = new ArrayList<>();
        userService.getAllUsers().forEach(user -> usernames.add(user.getUsername()));
        System.out.print("Enter parameters:\nUsername: ");
        String username = SCANNER.nextLine();
        while (usernames.contains(username)) {
            System.err.println("User with the same name already exists.");
            System.out.print("Username: ");
            username = SCANNER.nextLine();
        }
        System.out.print("Enter password: ");
        String password = SCANNER.nextLine();
        System.out.print("Confirm the password: ");
        String passwordConf = SCANNER.nextLine();
        while (!password.equals(passwordConf)) {
            System.err.println("Password mismatch.");
            System.out.print("Enter password: ");
            password = SCANNER.nextLine();
            System.out.print("Confirm the password: ");
            passwordConf = SCANNER.nextLine();
        }
        System.out.print("Enter telephone number: ");
        String telephoneNumber = SCANNER.nextLine();
        System.out.print("Enter email: ");
        String email = SCANNER.nextLine();
        userService.createUser(username, password, telephoneNumber, email);
        System.out.println(String.format("User <%s> has been created.", username));
    }

    private void editUser() {
        User currentUser = userService.getUser();

        System.out.print("Enter new password: ");
        String password = SCANNER.nextLine();
        if (password.isEmpty()) {
            password = currentUser.getPassword();
        }

        System.out.print("Enter new telephone number: ");
        String telNumber = SCANNER.nextLine();
        if (telNumber.isEmpty()) {
            telNumber = currentUser.getTelNumber();
        }

        System.out.print("Enter new email: ");
        String email = SCANNER.nextLine();
        if (email.isEmpty()) {
            email = currentUser.getEmail();
        }

        User user = new User(
                currentUser.getId(),
                currentUser.getUsername(),
                password,
                telNumber,
                email);
        if (userService.editUser(user)) {
            System.out.println(String.format("User <%s> has been updated.", currentUser.getUsername()));
        }

    }

    private void dropUser() {
        System.out.println();
        System.out.print("Enter username: ");
        String username = SCANNER.nextLine();
        System.out.print("Enter password: ");
        String password = SCANNER.nextLine();
        if (userService.deleteByCredentials(username, password)) {
            System.out.println(String.format("User <%s> has been dropped.", username));
            return;
        }
        System.err.println("Wrong username or password.");
    }

    public Long getUserId() {
        return userId;
    }
}
