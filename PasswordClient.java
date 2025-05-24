// Miguel Ocque - mocque@bu.edu

import java.util.Scanner;

public class PasswordClient {
    // a wrapper file that will run the initial call to the methods in the Password.java
    // file
    public static void main(String args[]) {

        // a scanner to detect user input
        Scanner scanner = new Scanner(System.in);

        // a while loop that continually runs if the user doesn't select the 'Quit' option
        while (true) {

            //print out the Menu for the user
            printMenu();

            // ask the user for a selection with an int
            System.out.print("Please select your option from the list: ");
            int input = scanner.nextInt();

            switch(input) {
                case 1:

                case 2:

                case 3:
                
                case 4:

                case 5:

                case 6:
                    break;
                
                default:
                    System.out.println("Invalid Input. Please Select a Valid Option.");
            }


            if (input == 6) {
                break;
            }

        }

        // ask the user if they want to save accounts to a file
        System.out.println("Would you like to Save your Accounts to a File? (y/n)");

        String choice = scanner.nextLine();

        if (choice.equals("y")) {
            // save file process
        }
        // if it's not a yes, there's no need to check, we can just quit.


        // once we're out we Quit, so we must close scanner
        scanner.close();
    }

    // method that lists the menu options for the user
    public static void printMenu() {
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
