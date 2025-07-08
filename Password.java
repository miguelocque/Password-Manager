/**
 * Password.java
 *
 * The Engine that runs the password manager, utilizing libraries, OOP, and correct privacy methodology
 *
 * @author Miguel Ocque
 * @version 1.0
 * @since May 2025
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
        private LLQueue<String> PassValues;
        private Node next;
        
        private Node(String acct, String usr, String pswrd) {
            this.account_type = acct;
            this.username = usr;
            PassValues = new LLQueue<String>();
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
    public void createAccount(String acct, String usrnm, String pswrd) {
        // we do an error check before calling the method, to ensure we're saving valid credentials
        if (acct.equals(null) || usrnm.equals(null) || pswrd.equals(null)) {
            throw new IllegalArgumentException("Credentials cannot be NULL. Please try again");
        }

        insertPass(acct, usrnm, pswrd);
    }

    // method that will be called whenever a new password wants to be inserted
    private boolean insertPass(String acct, String usrnm, String pswrd) {

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
            while (trav != null && (!trav.username.equals(usrnm) && !trav.account_type.equals(acct))) {
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

                // this points the head of the linked list to the new Node created
                table[position] = newAccount;

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
                correctUsername = correctUsername.trim();

                if (correctUsername.equals("y") || correctUsername.equals("Y")) {
                    // in this case we know that the user meant the existing username

                    // first we have to change the credentials to 

                    // first we check if the password already exists in this queue
                    // if we got null back from the searcher method, that means the password does not exist and we can insert
                    if (findPassInUsername(trav.account_type, trav.username, pswrd) == null) {
                        trav.PassValues.insert(pswrd);

                        //confirmation message for the insert
                        System.out.print("Password for the ");
                        System.out.print(trav.account_type);
                        System.out.print(" account with username ");
                        System.out.print(trav.username);
                        System.out.print(" was saved!");
                        System.out.println();
                    }

                    else {
                        //confirmation message for the already existing password
                        System.out.print("Password for the ");
                        System.out.print(trav.account_type);
                        System.out.print(" account with username ");
                        System.out.print(trav.username);
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

                    // this points the head of the linked list to the new Node created
                    table[position] = newAccount;

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
                if (findPassInUsername(acct, usrnm, pswrd) == null) {
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
                    
                    // we do nothing, everything is handled in the find method
                }

                // ** we DONT update numKeys because we're inserting an additional password to an already existing key **
            }
        }

        // we will always return true since we can always add to a Linked Node table
        return true;
    }

    // delete method
    public String deletePass(String acct, String usrnm, Object pswrdToDelete) {
        // get the hash value of the username
        int position = h1(acct);

        // make a trav Node so that we can go through the linked list
        Node trav = table[position];

        // must check to see if there's anything at the current trav, so make the trav != null check first

        // now we go through the linked list until our account and username match or we reach the end
        trav = rightAccount(trav, acct, usrnm);
        
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
        String passToDelete = null;

        // holder object for passwords in queue
        String holder = null;

        // create an additional LLQueue so that we can insert everything EXCEPT the password to delete
        LLQueue<String> newPassList = new LLQueue<String>();

        // while the current LLqueue of passwords is NOT empty, do the following:
        while (!trav.PassValues.isEmpty()) {
            // insert the first password in the queue into the other as long as it's not the password we delete
            holder = trav.PassValues.remove();

            if (!holder.equals(pswrdToDelete)) {
                // if the current password in the queue is not what we're looking for
                // insert into queue 
                newPassList.insert(holder);

            }
            else if (holder.equals(passToDelete) && passToDelete != null) {
                // this case is to check for duplicate passwords; we can only delete one password at a time, 
                // so here, we found the password we want to delete, but it's a duplicate since our passToDelete isn't null
                // (which it should be null if we haven't found the password.)
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

        // once we exit this first loop, we know that the original queue is empty and our new one should have everything except 
        // the password we wanted to delete

        // return the original linked list to its form without the password we 'deleted' 
        while (!newPassList.isEmpty()) {

            // remove the first item in our new queue and place it into original queue, which inherently restores correct order
            trav.PassValues.insert(newPassList.remove());
        }

        // at the end we return whatever is in passToDelete - null or not
        if (passToDelete == null) {
            System.out.println("The Password you seeked to delete does not exist for the " + acct + " account with the " + usrnm + " username.");
        }

        return passToDelete;

    }

    public String findPass(String acct, String user, String pass) {
        // null check for invalid credentials
        if (acct.equals(null) || user.equals(null) || pass.equals(null)) {
            throw new IllegalArgumentException("Credentials cannot be NULL. Please try again");
        }

        return findPassInUsername(acct, user, pass);
    }


    // search method  
    private String findPassInUsername(String acct, String user, String pass) {
        // get the hash value of the username
        int position = h1(acct);

        // make a trav Node so that we can go through the linked list
        Node trav = table[position];

        // make an extra queue to go through the linked list passwords
        LLQueue<String> passSearch = new LLQueue<>();

        // an additional holder variable to store null to check if we have the correct password to return at the end
        String foundPass = null;

        // now we go through the linked list until our account and username match or we reach the end
        trav = rightAccount(trav, acct, user);

        // now trav is either null or pointing to the one of the right things, let's do a null check
        if (trav == null) {
            // this means that we don't have a valid account under the given credentials; let's print that
            System.out.println("Account/Username not Found. Try again.");
            return null;
        }

        // getting here assumes we are pointing to the correct account

        // while the password queue is not empty
        while (!trav.PassValues.isEmpty()) {
            // get the first item in the queue and store it in a variable
            String holder = trav.PassValues.remove();
            
            // if the password we got is the password we're looking for, so we return it
            if (holder.equals(pass)) {
                foundPass = holder;
            }

            // insert the password into the temp queue
            passSearch.insert(holder);

        }

        // one more while loop to reset the queue
        while (!passSearch.isEmpty()) {
            // get the first item in the NEW queue
            String movePassBack = passSearch.remove();

            // place the password back into original queue
            trav.PassValues.insert(movePassBack);
        }

        // first let's check if foundPass is null, which if it is we should print an error message and say not found
        if (foundPass == null) {
            System.out.println("Password not located for your " + acct + " account.");
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


    // method to retrieve passwords from one username into an array
    public String[] retrievePasswords(String acct, String user, String pin) {
        // call the private method
        return placePasswordsIntoArray(acct, user, pin);
    }

    private String[] placePasswordsIntoArray(String acct, String user, String pin) {
        // private method that will be called by the wrapper method to retrieve credentials

        // error check for null credentials
        if (acct == null || user == null || user.equals("") || acct.equals("")) {
            return null;
        }
        
        // first obtain the correct position on the table to look up the account
        int position = h1(acct);

        // create an arrayList to add to from our queue of passwords when we find the account
        List<String> passwords = new ArrayList<>();

        // now we loop through the linked list at the position with a trav var
        Node trav = table[position];

        // until we're either null or at the correct user/acct
        trav = rightAccount(trav, acct, user);

        // now that we're out of the loop we should check if our trav is null, meaning we didnt find anything
        if (trav == null) {
            return null;
        }

        // if we're here, that means that we should have both the correct username and password, so we can
        // go through the queue of passwords and place them into the arraylist
        // a holder queue to place the passwords in correct order and then back into the original queue
        LLQueue<String> holder = new LLQueue<>();

        // loop through the trav queue until empty
        while (!trav.PassValues.isEmpty()) {
            // get the first password in the queue
            String curPass = trav.PassValues.remove();

            // add the current pasword to the array
            passwords.add(decrypt(curPass, pin));

            // and insert the current password into our new holder queue
            holder.insert(curPass);
        }

        // now we've reached the point where our passwords are all in our arraylist, and decrypted

        // now we should restore our original queue 
        while (!holder.isEmpty()) {
            // remove the first item from the queue
            String curPass = holder.remove();

            // and insert it back into the original queue
            trav.PassValues.insert(curPass);
        }

        // now we have to turn our arraylist into a string array
        String [] result = new String[passwords.size()];

        // loop through our arrays and update the correct result array
        for (int i = 0; i < result.length; i++) {
            result[i] = passwords.get(i);
        }

        // and we can safely return our array
        return result;
    }

    // method that calls the method to delete an account;
    public boolean deleteSpecificAccount(String acct, String user) {
        return deleteAccount(acct, user);
    }

    private boolean deleteAccount(String acct, String user) {
        // get our correct position
        int position = h1(acct);

        // now a traverse and prev variable
        Node trav = table[position];
        Node prev = null;

        while (trav != null) {
            if (trav != null && trav.account_type.equals(user) && trav.username.equals(user)) {
                break;
            }
            prev = trav;
            trav = trav.next;

        }

        if (trav == null) {
            // we didnt find the account to delete in this case
            return false;
        }

        // we also want to check if the account we want to delete is the first thing in the list
        if (prev == null && trav != null) {
            // just edit the head of the list
            table[position] = trav.next;

            // and return true
            return true;
        }

        // now we do the removal, prev points to the correct previous node, and trav to the node we want to delete

        // so first we check to see if there's anything to the right of trav.
        if (trav.next != null) {
            // just point prev to the next node of trav. 
            prev.next = trav.next;

            // and we can return true here
            return true;
        }
        else { // this means that trav has nothing to the next of it (i.e. the end of the list)
            
            // so we can simply edit the prev's next to null and make it the end of the list
            prev.next = null;
            
            // and we return true since we deleted
            return true;

        }



    }

    // helper method that finds the correct account
    private Node rightAccount(Node trav, String acct, String user) {
        while (trav != null) {
            if (trav != null && trav.account_type.equals(acct) && trav.username.equals(user)) {
                break;
            } 
            trav = trav.next;
        }

        return trav;
    }

    // method that returns the number of accounts the user has saved
    public int numAccounts() {
        return numKeys;
    }

}
