// Miguel Ocque - mocque@bu.edu

import java.util.Scanner;
import java.io.*;

public class PasswordClient {
    // a wrapper file that will run the initial call to the methods in the Password.java file

    // a final check file and string for our PIN validation
    private static final String FILE_NAME = "vault.check";
    private static final String CHECK_STRING = "check";

    public static void main(String args[]) {

        // instance of a Password class
        Password newAccountsWithPasswords = new Password();

        // a scanner to detect user input
        Scanner scanner = new Scanner(System.in);

        // we're seeing if the PIN has been created or not
        if (!new File(FILE_NAME).exists()) {
            // getting the new PIN from the user
            System.out.print("Please enter your new 4-Digit PIN: ");
            String newPin = getValidPIN(scanner);

            // now we can encrypt the check using that pin
            String encryptedChecker = newAccountsWithPasswords.encrypt(CHECK_STRING, newPin);

            // and we save that string to a file
            writeToFile(FILE_NAME, encryptedChecker);

            // after which we can proceed to the CLI
            Manager(scanner, newAccountsWithPasswords, newPin);

        }
        else { // if we're here, that means a PIN exists and we must ask the user for it
            System.out.print("Enter your 4 digit PIN to unlock your Password Manager: ");
            String enteredPin = getValidPIN(scanner);

            // read the encrypted check password from the file 
            String encryptedCheck = readFromFile(FILE_NAME);

            // decrypt the string with the entered PIN
            String decryptCheck = newAccountsWithPasswords.decrypt(encryptedCheck, enteredPin);

            // and we check to see if the decrypted string matches
            if (CHECK_STRING.equals(decryptCheck)) {
                System.out.println("Access Granted. Welcome to your Password Manager!");
                Manager(scanner, newAccountsWithPasswords, enteredPin);
            }
            else { 
                System.out.println("Incorrect PIN. Try again later.");
                return;
            }

        }


        // once we're out we Quit, so we must close scanner
        scanner.close();
    }

    // method that writes to a file
    private static void writeToFile(String filename, String content) {
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write(content);
            writer.close();
        } catch (Exception e) {
            System.err.println("I/O Error: " + e.getMessage());
        }
        
    }

    // method that reads from a file
    private static String readFromFile(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String content = reader.readLine();
            reader.close();
            return content;
        } catch (Exception e) {
            System.err.println("I/O Error: " + e.getMessage());
            return null;
        }
        
    }

    // method to obtain a PIN from the user
    private static String getValidPIN(Scanner scanner) {
        String pin;
        while (true) {
            // gets user input
            pin = scanner.nextLine();
            // checking to see if the entered PIN is 4 digits and there are no characters
            if (pin.matches("\\d{4}")) {
                // if it does we can return it
                return pin;
            }
            else { // if not we ask the user for another correct PIN
                System.out.print("Invalid PIN. Please enter a 4 digit PIN: ");
            }
        }
    }

    // method that runs the Password Manager
    public static void Manager(Scanner scanner, Password newAccountsWithPasswords, String pin) {
        // a while loop that continually runs if the user doesn't select the 'Quit' option
        while (true) {

            //print out the Menu for the user
            printMenu();

            // ask the user for a selection with an int
            System.out.print("Please select your option from the list: ");
            int input = scanner.nextInt();
            scanner.nextLine(); // this gets rid of the invisible newline
            System.out.println();

            switch(input) {
                // "Store New Username/Password for Specific Account"
                case 1:
                    // get the account type
                    System.out.print("What account are you saving? ");
                    String acct = scanner.nextLine();
                    System.out.println();

                    // get the username
                    System.out.print("Please type your username: ");
                    String user = scanner.nextLine();
                    System.out.println();

                    // get the password
                    System.out.print("Please enter your Password (must be at least 8 characters long): ");
                    String pass = scanner.nextLine();

                    while (pass.length() < 8) {
                        System.out.print("Password must be at least 8 characters long. Try again: ");
                        pass = scanner.nextLine();
                        System.out.println();
                    }
                    System.out.println();

                    // here we want to call a method to encrypt the password before saving it.
                    pass = newAccountsWithPasswords.encrypt(pass, pin);

                    // now we can store it
                    newAccountsWithPasswords.createAccount(acct, user, pass);


                // "Search for Existing Password with Account Type and Username"
                case 2:


                // "List out All Saved Usernames and Passwords for a Specific Account Type"
                case 3:
                

                // "Delete Specific Password from an Account/Username"
                case 4:


                // "Erase all Accounts Created for a Specific Account Type"
                case 5:


                // "Quit"
                case 6:
                    break;
                
                default:
                    System.out.println("Invalid Input. Please Select a Valid Option.");
            }

            // checking if we have our quit command
            if (input == 6) {
                break;
            }

        }

        // ask the user if they want to save accounts to a file
        System.out.print("Would you like to Save your Accounts to a File? (y/n) ");

        String choice = scanner.next();

        if (choice.equals("y") || choice.equals("Y")) {
            // save file process
        }
        // if it's not a yes, there's no need to check, we can just quit.
    }

    // method that lists the menu options for the user
    public static void printMenu() {
        System.out.println();
        System.out.println("------- Password Manager -------");
        System.out.println();
        System.out.println("1. Store New Username/Password for Specific Account");
        System.out.println("2. Search for Existing Password with Account Type and Username");
        System.out.println("3. List out All Saved Usernames and Passwords for a Specific Account Type");
        System.out.println("4. Delete Specific Username and Password from an Account");
        System.out.println("5. Erase all Accounts Created for a Specific Account Type");
        System.out.println("6. Quit");
        System.out.println();
        System.out.println("-----------------------------------");
        System.out.println();
    }
}
