/*
 * created by: Miguel Ocque - mocque@bu.edu
 *
 * implementing a password manager:
 * - first asks the user how many total accounts they want to create
 * - asks the user if they want to create their own password or generate one
 * - asks the user what the username they want to save is
 * - asks the user what account they're saving the password for
 * - utilizes hash table chaining to save the password: linked list form
 * - passwords will be kept in a queue, such that if any duplicate accounts, it will show all relevant saved passwords
 * - will be done in a while loop format, such that there are options to do each thing every time an action is performed
 * - must indicate when they want to quit
 */

import java.util.*;
import java.util.Scanner;

// encryption imports

import javax.crypto.Cipher;                  // Core encryption engine
import javax.crypto.spec.SecretKeySpec;      // Allows us to build a key object from bytes
import java.security.MessageDigest;          // Used for SHA-256 hashing of the PIN
import java.util.Arrays;                     // For trimming the hashed key to 16 bytes
import java.util.Base64;                     // Lets us turn binary into printable string


public class Password {

    // **************************** CORRECT BELOW THESE LINES *******************************

    //  a global variable that initializes our hash table to fit 50 different accounts
    private final int DEFAULT_NUM_PASSWORDS = 50;

    // a scanner to detect user input
    Scanner scanner = new Scanner(System.in);

    // a Node class that will contain all the fields of a Linked Password Node
    private class Node {
        private String account_type;
        private String username;
        private LLQueue<Object> PassValues;
        private Node next;
        
        private Node(String acct, String usr, Object pswrd) {
            this.account_type = acct;
            this.username = usr;
            PassValues = new LLQueue<Object>();
            PassValues.insert(pswrd);
            this.next = null;
        }
    }

    /* hash function */
    public int h1(Object key) {
        int h1 = key.hashCode() % table.length;
        if (h1 < 0) {
            h1 += table.length;
        }
        return h1;
    }
    
    // hash table with a set size
    private Node[] table = new Node[DEFAULT_NUM_PASSWORDS];

    // total number of accounts we've added to the hash table; updates within methods
    private int numKeys; 

    // Password needs a constructor, set the numKeys to 0 since we haven't added anything
    public Password () {

        // the numkeys tells us how many accounts we have (0 at construction)
        numKeys = 0;
    }

    // ** not going to include any other constructors **

    // **************************** CORRECT ABOVE THESE LINES ********************************

    // wrapper method to call the private method 
    public void createAccount(String acct, String usrnm, Object pswrd) {
        insertPass(acct, usrnm, pswrd);
    }

    // method that will be called whenever a new password wants to be inserted
    private boolean insertPass(String acct, String usrnm, Object pswrd) {
        // first we get the hash value of the account type
        int position = h1(acct);

        // then we look in the position and see if there's anything there to begin with
        // we also want to check if the position is 'removed' - there used to be an account that we chose to delete
        if (table[position] == null || (table[position].account_type == null && table[position].username == null && table[position].next == null)) {
            // we can simply point the position to the new node we create
            Node newAccount = new Node(acct, usrnm, pswrd);
            table[position] = newAccount;
            numKeys++;

            //confirmation message
            System.out.print("Saved Username/Password for the ");
            System.out.print(acct);
            System.out.print(" account!");
            System.out.println();
        }
        else { // otherwise, there's something at the position, we must look for duplicates

            // so we make a traverse Node at the correct table position
            Node trav = table[position];

            // traverse through the things at the position until either we reach the end or find a dupe
            while (trav != null && !trav.username.equals(usrnm) && !trav.account_type.equals(acct)) {
                trav = trav.next;
            }

            // here if trav is null, there are no duplicates, so we can add the new 'account' to the FRONT of the list
            if (trav == null) {
                // make the Node
                Node newAccount = new Node(acct, usrnm, pswrd);

                // point the next field of the new Node to the previous head 
                newAccount.next = table[position];
                
                // make the new account the new head
                table[position] = newAccount;

                // update numKeys
                numKeys++;

                //confirmation message
                System.out.print("Saved Username/Password for the ");
                System.out.print(acct);
                System.out.print(" account!");
                System.out.println();
            }

            else if (trav.username.equals(usrnm) && !trav.account_type.equals(acct)) {
                // here we have the case where the username equals but not the account, meaning we have to make a complete new Node
                Node newAccount = new Node(acct, usrnm, pswrd);

                // add the new account to the front of the list; it's fine to edit trav since we're gonna exit the conditional
                trav = table[position];
                newAccount.next = trav;
                trav = newAccount;

                // ** we DO update numKeys because we're creating a new account **
                numKeys++;

                //confirmation message
                System.out.print("Saved Username/Password for the ");
                System.out.print(acct);
                System.out.print(" account!");
                System.out.println();
            }

            else if (!trav.username.equals(usrnm) && trav.account_type.equals(acct)) {
                // here we have the case where the username doesn't match the already existing account with a username, 
                // so we have to ask if they meant the existing username
                System.out.print("Did you mean this existing username? - ");
                System.out.print(trav.username);
                System.out.print(" (y/n) - ");
                String correctUsername = scanner.nextLine();

                if (correctUsername.equals("y") || correctUsername.equals("Y")) {
                    // in this case we know that the user meant the existing username

                    // first we check if the password already exists in this queue
                    // if we got null back from the searcher method, that means the password does not exist and we can insert
                    if (findPassInUsername(usrnm, pswrd) == null) {
                        trav.PassValues.insert(pswrd);

                        //confirmation message for the insert
                        System.out.print("Password for the ");
                        System.out.print(acct);
                        System.out.print(" account with username ");
                        System.out.print(usrnm);
                        System.out.print(" was saved!");
                        System.out.println();
                    }

                    else {
                        //confirmation message for the already existing password
                        System.out.print("Password for the ");
                        System.out.print(acct);
                        System.out.print(" account with username ");
                        System.out.print(usrnm);
                        System.out.print(" already exists.");
                        System.out.println();
                    }
                    
                    // ** we DONT update numKeys because we're inserting an additional password to an already existing key **
                }

                else { // otherwise, they meant a different username, so we can make a new Node and place it at the front of the list
                    Node newAccount = new Node(acct, usrnm, pswrd);

                    // add the new account to the front of the list; it's fine to edit trav since we're gonna exit the conditional
                    trav = table[position];
                    newAccount.next = trav;
                    trav = newAccount;

                    // ** we DO update numKeys because we're creating a new account **
                    numKeys++;

                    //confirmation message
                    System.out.print("Saved new Username and Password for the ");
                    System.out.print(acct);
                    System.out.print(" account!");
                    System.out.println();
                }

            }

            else {  // otherwise, there is a duplicate (both account type and username), and trav is pointing to it
            
                // thus we simply check if the password already exists and insert accordingly
                if (findPassInUsername(usrnm, pswrd) == null) {
                    trav.PassValues.insert(pswrd);

                    //confirmation message for the insert
                    System.out.print("Password for the ");
                    System.out.print(acct);
                    System.out.print(" account with username ");
                    System.out.print(usrnm);
                    System.out.print(" was saved!");
                    System.out.println();
                }

                else {
                    //confirmation message for the already existing password
                    System.out.print("Password for the ");
                    System.out.print(acct);
                    System.out.print(" account with username ");
                    System.out.print(usrnm);
                    System.out.print(" already exists.");
                    System.out.println();
                }

                // ** we DONT update numKeys because we're inserting an additional password to an already existing key **
            }
        }

        // we will always return true since we can always add to a Linked Node table
        return true;
    }

    // delete method
    public Object deletePass(String acct, String usrnm, Object pswrdToDelete) {
        // get the hash value of the username
        int position = h1(acct);

        // make a trav Node so that we can go through the linked list
        Node trav = table[position];

        // now we go through the linked list until our account and username match or we reach the end
        while ((!trav.username.equals(usrnm) && !trav.account_type.equals(acct)) && trav != null) {
            trav = trav.next;
        }
        
        // if we reach the end of the list and trav is null, that means we can't find an account/username under which the password
        // is located
        if (trav == null) {
            // so we can return null and an error message
            System.out.println("Account not found. Could not delete password. Try again.");
            return null;
        }

        // if we don't go into the if statement, that means we have a match; 

        // first we should check to see if the current list is empty, implying that no passwords even exist for this account/username
        if (trav.PassValues.isEmpty()) {
            // if we go into here, that means that we have no passwords. Return null and an error message
            System.out.println("Account found. No Passwords Exist.");
            return null;
        }
        
        // if we're here that means we have password(s) saved.
        // we must go through the password(s) until we 
        // find the correct one and proceed to delete it
        Object passToDelete = null;

        // holder object for passwords in queue
        Object holder = null;

        // create an additional LLQueue so that we can insert everything EXCEPT the password to delete
        LLQueue<Object> newPassList = new LLQueue<Object>();

        // while the current LLqueue of passwords 
        while (!trav.PassValues.isEmpty()) {
            // insert the first password in the queue into the other as long as it's not the password we delete
            holder = trav.PassValues.remove();

            if (holder != pswrdToDelete) {
                // if the current password in the queue is not what we're looking for
                // insert into queue 
                newPassList.insert(holder);

            }
            else if (holder == pswrdToDelete && passToDelete != null) {
                // this case is to check for duplicate passwords; we can only delete one password at a time, so let's say that
                System.out.println("Found password more than once. Deleting only the first found password.");
                
                // and we continue the loop
            }
            else {
                // this case matches the password, and passToDelete is null so we know we found our password we want to delete

                // holder currently holds the removed password, so we don't insert into the new queue, but we update passToDelete
                passToDelete = holder;

                // and continue through the loop
            }
            

        }

        // return the original linked list to its form without the password we 'deleted' - ** TODO **

        // at the end we return whatever is in passToDelete
        return passToDelete;

    }

    // search method -- ** TODO ** 
    private Object findPassInUsername(Object usrnm, Object pswrd) {
        // get the hash value of the username
        int position = h1(usrnm);

        // make a trav Node so that we can go through the linked list
        Node trav = table[position];

        // make an extra queue to go through the linked list passwords
        LLQueue<Object> passSearch = new LLQueue<Object>();

        // an additional holder variable to store null to check if we have the correct password to return at the end
        Object foundPass = null;

        // go through the linked list, and empty the LLqueue in a nested loop, by pushing it onto a different queue
        // then back to the original queue
        while (trav != null) {
            // while the password queue is not empty
            while (!trav.PassValues.isEmpty()) {
                // get the first item in the queue and store it in a variable
                Object holder = trav.PassValues.remove();
                
                // if the password we got is the password we're looking for, so we return it
                if (holder.equals(pswrd)) {
                    foundPass = holder;
                }
 
                // insert the password into the temp queue
                passSearch.insert(holder);

            }
            // one more while loop to reset the queue
            while (!passSearch.isEmpty()) {
                // get the first item in the NEW queue
                Object movePassBack = passSearch.remove();

                // place the password back into original queue
                trav.PassValues.insert(movePassBack);
            }

            // move trav to the next value
            trav = trav.next;
        } 

        // we return 'foundPass' which should either have the correct password or null
        return foundPass;
    }

    // **************************** CORRECT BELOW THESE LINES *******************************

    // encrypt methods 
    private static SecretKeySpec deriveKeyFromPIN(String pin) throws Exception {
        // Obtains the "Secure Hash Algorithm" (SHA-256) 
        MessageDigest sha = MessageDigest.getInstance("SHA-256");

         // Hash the PIN into 32 bytes
        byte[] hashed = sha.digest(pin.getBytes("UTF-8"));

        // Use the first 16 bytes (128 bits)
        byte[] key = Arrays.copyOf(hashed, 16);

        // Wrap it into a key object that is then returned to the correct object to be utilized in the encrypt method
        return new SecretKeySpec(key, "AES");                           
    }

    public String encrypt(String pass, String pin) {
        try {
            // obtains the correct derived key utilizing the given pin
            SecretKeySpec key = deriveKeyFromPIN(pin);

            // the encryptor method using "Advanced Encryption Standard" (AES)
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            // this encrypts our passed in password
            byte[] encrypted = cipher.doFinal(pass.getBytes("UTF-8"));

            // turns and returns our encrypted password into a readable string
            return Base64.getEncoder().encodeToString(encrypted);            
        } catch (Exception e) {

            // error handler
            System.err.println("Encryption error: " + e.getMessage());
            return null;
        }
    }

    public String decrypt(String encryptedText, String pin) {
        try {
            // use our pin to derive the correct key
            SecretKeySpec key = deriveKeyFromPIN(pin);

            // getting the AES encryption method 
            Cipher cipher = Cipher.getInstance("AES");

            // we are now DECRYPTING, not ENCRYPTING
            cipher.init(Cipher.DECRYPT_MODE, key);

            // decodes our string into bytes so that the AES algorithm can decrypt using matrices
            byte[] decoded = Base64.getDecoder().decode(encryptedText);      
            byte[] decrypted = cipher.doFinal(decoded);                      
            
            // returns a String that should be the correct decrypted password
            return new String(decrypted, "UTF-8");
        } catch (Exception e) {
            System.err.println("Decryption error: " + e.getMessage());
            return null;
        }
    }

    // **************************** CORRECT ABOVE THESE LINES ********************************


    // method to retrieve passwords from one username into an array - ** TODO **

    // 

}
