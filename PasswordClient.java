/**
 * PasswordClient.java
 *
 * A command-line password manager with AES-128 encryption and SHA-256 authentication.
 *
 * @author Miguel Ocque
 * @version 1.0
 * @since May 2025
 */

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.*;

public class PasswordClient {
    // a wrapper file that will run the initial call to the methods in the Password.java file

    // a final check file and string for our PIN validation
    private static final String FILE_NAME = "vault.check";
    private static final String CHECK_STRING = "check";

    // string objects that will be reused for space optimization for CLI options
    private static String acct;
    private static String user;
    private static String pass;

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
            int input;

            // loop until we have an integer
            while (true) {
                try {
                    input = scanner.nextInt(); // Try to read an int
                    break; // Exit the loop if successful
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid integer from the list.");
                    scanner.nextLine(); // Clear the invalid input
                }
            }

            scanner.nextLine(); // this gets rid of the invisible newline
            System.out.println();

            switch(input) {
                // "Store New Username/Password for Specific Account"
                case 1:
                    // get the account type
                    System.out.print("What account are you saving? ");
                    acct = scanner.nextLine();
                    acct = acct.trim();
                    acct = acct.toLowerCase();
                    System.out.println();

                    // get the username
                    System.out.print("Please type your username: ");
                    user = scanner.nextLine();
                    user = user.trim();
                    user = user.toLowerCase();
                    System.out.println();

                    // get the password
                    System.out.print("Please enter your Password (must be at least 8 characters long): ");
                    pass = scanner.nextLine();

                    while (pass.length() < 8) {
                        System.out.print("Password must be at least 8 characters long. Try again: ");
                        pass = scanner.nextLine();
                        System.out.println();
                    }
                    pass = pass.trim();
                    System.out.println();

                    // here we want to call a method to encrypt the password before saving it.
                    pass = newAccountsWithPasswords.encrypt(pass, pin);

                    // now we can store it
                    newAccountsWithPasswords.createAccount(acct, user, pass);

                    // skip the remaining cases
                    continue;


                // "Search for Existing Password with Account and Username"
                case 2:
                    // get the account type
                    System.out.print("What account are you searching for? ");
                    acct = scanner.nextLine();
                    acct = acct.trim();
                    acct = acct.toLowerCase();
                    System.out.println();

                    // get the username
                    System.out.print("Please type your username: ");
                    user = scanner.nextLine();
                    user = user.trim();
                    user = user.toLowerCase();
                    System.out.println();

                    // obtain password they want to find
                    System.out.print("Please enter the Password you would like to find: ");
                    pass = scanner.nextLine();
                    pass = pass.trim();
                    System.out.println();

                    // encrypt the typed in password so that we can accurately compare
                    pass = newAccountsWithPasswords.encrypt(pass, pin);

                    // create a String to show the user their found Password
                    String found = newAccountsWithPasswords.findPass(acct, user, pass);

                    // decrypt the password to display
                    if (found != null) {
                        found = newAccountsWithPasswords.decrypt(found, pin);
                        System.out.println("Your " + acct + " password was found and exists!");
                        System.out.println("This is your password: " + found);
                    }

                    // continue with the loop
                    continue;


                // "List out All Saved Usernames and Passwords for a Specific Account Type"
                case 3:
                    // this obtains the account
                    System.out.print("From what account would you like your passwords? ");
                    acct = scanner.nextLine();
                    acct = acct.trim();
                    acct = acct.toLowerCase();
                    System.out.println();

                    // obtains the username
                    System.out.print("What's the username for that account? ");
                    user = scanner.nextLine();
                    user = user.trim();
                    user = user.toLowerCase();
                    System.out.println();

                    // now we call the method, with the pin, and if we get null we print an error message
                    String [] passwordsObtained = newAccountsWithPasswords.retrievePasswords(acct, user, pin);

                    if (passwordsObtained != null) {
                        // here we have an array that we can print to the user
                        System.out.println("Here are your passwords for your " + acct + " account: " + Arrays.toString(passwordsObtained));
                    }
                    else {
                        System.out.println("Error. Account was not found, thus no passwords were obtained. Try again.");
                    }

                    // and continue with our loop
                    continue;


                // "Delete Specific Password from an Account/Username"
                case 4:
                    // get the account type
                    System.out.print("What account are deleting from? ");
                    acct = scanner.nextLine();
                    acct = acct.trim();
                    acct = acct.toLowerCase();
                    System.out.println();

                    // get the username
                    System.out.print("Please type your username: ");
                    user = scanner.nextLine();
                    user = user.trim();
                    user = user.toLowerCase();
                    System.out.println();

                    // obtain password they want to delete
                    System.out.print("Please enter the Password you would like to delete: ");
                    pass = scanner.nextLine();
                    pass = pass.trim();

                    // encrypt the typed in password so that we can accurately compare
                    pass = newAccountsWithPasswords.encrypt(pass, pin);

                    // create a String to hold the result
                    String deletedPass = newAccountsWithPasswords.deletePass(acct, user, pass);

                    if (deletedPass != null) {
                        // this means we obtained the deleted password; let's decrypt it and print it
                        deletedPass = newAccountsWithPasswords.decrypt(deletedPass, pin);

                        System.out.println();
                        System.out.print("You have successfully deleted the password '");
                        System.out.print(deletedPass);
                        System.out.print("' for your ");
                        System.out.print(acct);
                        System.out.print(" account with username ");
                        System.out.print(user);
                        System.out.print(".");
                        System.out.println();
                    }
                    else { // being here means that we have a returned null from the method, meaning we didn't delete anything

                        // we already printed an error message, so we continue
                        
                    }

                    continue;

                // "Erase all Passwords Created for a Specific Account Type"
                case 5:

                // "How Many Accounts have you created?"
                case 6:
                    System.out.println("You have created " + newAccountsWithPasswords.numAccounts() + " accounts!");
                    
                // Delete Account
                case 7:
                    // get the account type
                    System.out.print("What account are you deleting? ");
                    acct = scanner.nextLine();
                    acct = acct.trim();
                    acct = acct.toLowerCase();
                    System.out.println();

                    // get the username
                    System.out.print("Please type the username associated with the account: ");
                    user = scanner.nextLine();
                    user = user.trim();
                    user = user.toLowerCase();
                    System.out.println();

                    // now call the method; if false, state error message, if true, print confirmation
                    if (newAccountsWithPasswords.deleteSpecificAccount(acct, user)) {
                        System.out.println("Successfully deleted your " + acct + " account!");
                    }

                    else { // this means no account found
                        System.out.println("No account was located with the given credentials. Try again.");
                    }

                // "Quit"
                case 8:
                    break;
                
                default:
                    System.out.println("Invalid Input. Please Select a Valid Option.");
            }

            // checking if we have our quit command
            if (input == 8) {
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
        scanner.close();

    }

    // method that lists the menu options for the user
    public static void printMenu() {
        System.out.println();
        System.out.println("------- Password Manager -------");
        System.out.println();
        System.out.println("1. Store New Username/Password for Specific Account");
        System.out.println("2. Search for Existing Password with Account Type and Username");
        System.out.println("3. List out All Saved Usernames and Passwords for a Specific Account Type");
        System.out.println("4. Delete Specific Password from an Account/Username");
        System.out.println("5. Erase all Passwords Created for a Specific Account Type");
        System.out.println("6. How Many Accounts Have you Created?");
        System.out.println("7. Delete Account");
        System.out.println("8. Quit");
        System.out.println();
        System.out.println("-----------------------------------");
        System.out.println();
    }
}
