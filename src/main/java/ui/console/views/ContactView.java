package ui.console.views;

import backend.entity.Address;
import backend.entity.Contact;
import backend.entity.Phone;
import backend.entity.PhoneType;
import backend.service.ContactService;
import backend.utils.ContactComparator;
import ui.console.IConsoleConstants;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ui.console.utils.ConsoleUtils.SCANNER;
import static ui.console.utils.ConsoleUtils.getIntCommand;

public class ContactView {

    private ContactService contactService;
    private UserView userView;
    private String keyWord;

    public ContactView(UserView userView) {
        this.userView = userView;
        this.contactService = new ContactService(userView.getUserId());
    }

    public void printContacts(List<Contact> contacts) {
        if (contacts == null) {
            return;
        }
        if (contacts.isEmpty()) {
            System.err.println("No contacts have been found.");
            return;
        }
        contactsToAlignContactLines(contacts).forEach(strings -> {
            Arrays.stream(strings).forEach(param -> System.out.print(param + "    "));
            System.out.println();
        });
    }

    public void printFoundContacts(List<Contact> contacts, String kewWord) {
        if (contacts == null) {
            return;
        }
        if (kewWord == null || kewWord.isEmpty()) {
            printContacts(contacts);
            return;
        }
        final Pattern regex = Pattern.compile("(" + kewWord + ")",
                Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        contactsToAlignContactLines(contacts).forEach(strings -> {
            Arrays.stream(strings).forEach(param -> {
                StringBuilder resParam = new StringBuilder();
                Matcher matcher = regex.matcher(param);
                if (matcher.find()) {
                    while (true) {
                        int start = matcher.start();
                        int end = matcher.end();
                        resParam.append(param, 0, start)
                                .append(IConsoleConstants.ANSI_GREEN)
                                .append(param, start, end)
                                .append(IConsoleConstants.ANSI_RESET);
                        param = param.substring(end);
                        matcher = regex.matcher(param);
                        if (!matcher.find()) {
                            resParam.append(param);
                            break;
                        }
                    }
                }
                System.out.print(((resParam.length() == 0) ? param : resParam.toString()) + "    ");
            });
            System.out.println();
        });
    }

    private List<String[]> contactsToAlignContactLines(List<Contact> contacts) {
        List<String[]> contactStrings = new ArrayList<>();
        for (Contact contact : contacts) {
            contactStrings.add(contact.toString().split(" {4}"));
        }
        for (int i = 0; i < contactStrings.get(0).length; i++) {
            int maxLength = 0;
            for (String[] strings : contactStrings) {
                if (maxLength < strings[i].length()) {
                    maxLength = strings[i].length();
                }
            }
            for (String[] strings : contactStrings) {
                while (strings[i].length() < maxLength) {
                    strings[i] += " ";
                }
            }
        }
        return contactStrings;
    }

    public void showSortContactsMenu() {
        System.out.println();
        int command = getIntCommand(IConsoleConstants.SORTING_ORDER_TEXT);
        boolean ascending = true;
        System.out.println();
        if (command == 0) {
            // back
            userView.showLoggedMenu(this);
            return;
        } else if (command == 2) {
            ascending = false;
        } else if (command != 1) {
            System.out.println(IConsoleConstants.NON_EXISTENT_COMMAND + "\n");
            showSortContactsMenu();
            return;
        }
        List<ContactComparator> sortParamsList = getSortParams();
        ContactComparator[] sortParams = new ContactComparator[Objects.requireNonNull(sortParamsList).size()];
        if (!sortParamsList.isEmpty()) {
            for (int i = 0; i < sortParams.length; i++) {
                sortParams[i] = sortParamsList.get(i);
            }
            contactService.sortContacts(
                    contactService.getAllContacts(),
                    ascending,
                    sortParams);
        }
    }

    private List<ContactComparator> getSortParams() {
        System.out.println();
        int command = getIntCommand(IConsoleConstants.SELECT_SORTING_OPTIONS_TEXT);
        char[] commandsId = String.valueOf(command).toCharArray();
        List<ContactComparator> params = new ArrayList<>();
        for (char c : commandsId) {
            if (c == '0') {
                showSortContactsMenu();
                return null;
            } else if (c == '1') {
                params.add(ContactComparator.FULL_NAME);
                params.add(ContactComparator.COMPANY);
                params.add(ContactComparator.TELEPHONE_NUMBER);
                params.add(ContactComparator.EMAIL_ADDRESS);
            } else if (c == '2') {
                params.add(ContactComparator.FULL_NAME);
            } else if (c == '3') {
                params.add(ContactComparator.FIRST_NAME);
            } else if (c == '4') {
                params.add(ContactComparator.MIDDLE_NAME);
            } else if (c == '5') {
                params.add(ContactComparator.LAST_NAME);
            } else if (c == '6') {
                params.add(ContactComparator.COMPANY);
            } else if (c == '7') {
                params.add(ContactComparator.TELEPHONE_NUMBER);
            } else if (c == '8') {
                params.add(ContactComparator.EMAIL_ADDRESS);
            } else {
                System.out.println("\n" + IConsoleConstants.NON_EXISTENT_COMMAND);
                getSortParams();
                return null;
            }
        }
        return params;
    }

    public List<Contact> findContacts() {
        System.out.println();
        int command = getIntCommand(IConsoleConstants.SELECT_SEARCHING_OPTION_TEXT);
        ContactComparator searchKey;
        if (command == 0) {
            userView.showLoggedMenu(this);
            return null;
        } else if (command == 1) {
            searchKey = ContactComparator.ALL;
        } else if (command == 2) {
            searchKey = ContactComparator.FULL_NAME;
        } else if (command == 3) {
            searchKey = ContactComparator.TELEPHONE_NUMBER;
        } else if (command == 4) {
            searchKey = ContactComparator.EMAIL_ADDRESS;
        } else if (command == 5) {
            searchKey = ContactComparator.COMPANY;
        } else if (command == 6) {
            searchKey = ContactComparator.FIRST_NAME;
        } else if (command == 7) {
            searchKey = ContactComparator.MIDDLE_NAME;
        } else if (command == 8) {
            searchKey = ContactComparator.LAST_NAME;
        } else {
            System.out.println("\n" + IConsoleConstants.NON_EXISTENT_COMMAND);
            findContacts();
            return null;
        }
        System.out.print("\nEnter key words: ");
        keyWord = SCANNER.nextLine();
        return contactService.findContactsBy(searchKey, keyWord);
    }

    public void addContact() {
        if (contactService.createContact(readContact())) {
            System.out.println("New contact has been added.");
            System.out.println();
        }
    }

    public Contact readContact() {
        System.out.println();
        System.out.print("Enter parameters:\nFirst name: ");
        String firstName = SCANNER.nextLine();
        System.out.print("Middle name: ");
        String middleName = SCANNER.nextLine();
        System.out.print("Last name: ");
        String lastName = SCANNER.nextLine();
        Phone phone = readPhone();
        System.out.print("Company: ");
        String company = SCANNER.nextLine();
        System.out.print("Email address: ");
        String email = SCANNER.nextLine();
        Address address = readAddress();
        System.out.print("Note: ");
        String note = SCANNER.nextLine();
        return new Contact(-1L, firstName, middleName, lastName, company, phone, email, address, note);
    }

    private Phone readPhone() {
        System.out.print("Telephone number: ");
        String telNumber = SCANNER.nextLine();
        System.out.print("Telephone model: ");
        String model = SCANNER.nextLine();

        int command = 0;
        while (command < 1 || command > 3) {
            System.out.println("Telephone type:\n1. Mobile\n2. Home\n3. Work");
            command = Integer.valueOf(SCANNER.nextLine());
        }
        PhoneType type = null;
        switch (command) {
            case 1:
                type = PhoneType.MOBILE;
                break;
            case 2:
                type = PhoneType.HOME;
                break;
            case 3:
                type = PhoneType.WORK;
                break;
        }
        return new Phone(null, telNumber, model, type);
    }

    private Address readAddress() {
        System.out.print("Country: ");
        String country = SCANNER.nextLine();
        System.out.print("City: ");
        String city = SCANNER.nextLine();
        System.out.print("Street: ");
        String street = SCANNER.nextLine();
        System.out.print("Building: ");
        String building = SCANNER.nextLine();
        return new Address(null, country, city, street, building);
    }

    public void dropContact() {
        System.out.print("Enter contact id: ");
        long id = Long.valueOf(SCANNER.nextLine());
        if (contactService.dropContact(id)) {
            System.out.println("Contact has been dropped.");
        }
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void editContact() {
        List<Contact> contacts = contactService.getAllContacts();
        printContacts(contacts);

        System.out.print("Enter contact id: ");
        long id = Long.valueOf(SCANNER.nextLine());

        Contact editedContact = contacts.stream()
                .filter(c -> c.getId() == id)
                .findAny()
                .orElse(null);
        if (editedContact == null) {
            System.out.println("Selected is doesn't exist.");
            return;
        }

        Contact readContact = readContact();
        readContact.setId(editedContact.getId());
        if (readContact.getFirstName().isEmpty()) {
            readContact.setFirstName(editedContact.getFirstName());
        }
        if (readContact.getMiddleName().isEmpty()) {
            readContact.setMiddleName(editedContact.getMiddleName());
        }
        if (readContact.getLastName().isEmpty()) {
            readContact.setLastName(editedContact.getLastName());
        }
        if (readContact.getCompany().isEmpty()) {
            readContact.setCompany(editedContact.getCompany());
        }

        Phone editedPhone = editedContact.getPhone();
        Phone readPhone = readContact.getPhone();
        readPhone.setId(editedPhone.getId());
        if (readPhone.getTelNumber().isEmpty()) {
            readPhone.setTelNumber(editedPhone.getTelNumber());
        }
        if (readPhone.getModel().isEmpty()) {
            readPhone.setModel(editedPhone.getModel());
        }

        Address editedAddress = editedContact.getAddress();
        Address readAddress = readContact.getAddress();
        readAddress.setId(editedAddress.getId());
        if (readAddress.getCity().isEmpty()) {
            readAddress.setCity(editedAddress.getCity());
            readAddress.setCountry(editedAddress.getCountry());
        }
        if (readAddress.getStreet().isEmpty()) {
            readAddress.setStreet(editedAddress.getStreet());
        }
        if (readAddress.getBuilding().isEmpty()) {
            readAddress.setBuilding(editedAddress.getBuilding());
        }

        if (readContact.getEmail().isEmpty()) {
            readContact.setEmail(editedContact.getEmail());
        }
        if (readContact.getNote().isEmpty()) {
            readContact.setNote(editedContact.getNote());
        }

        if (contactService.editContact(readContact)) {
            System.out.println("Contact has been edited.");
            System.out.println();
        }
    }
}
