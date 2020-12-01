package ui.console.utils;

import java.util.Scanner;

public class ConsoleUtils {

    public static final Scanner SCANNER = new Scanner(System.in);

    public static int getIntCommand(String text) {
        int command;
        while (true) {
            System.out.print(text);
            try {
                command = Integer.valueOf(SCANNER.nextLine());
                break;
            } catch (NumberFormatException | NullPointerException e) {
                System.err.println("Incorrect input.");
                System.out.println();
            }
        }
        return command;
    }
}