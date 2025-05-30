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
import java.util.Random;



public class Password {

    // **************************** CORRECT BELOW THESE LINES *******************************

    //  a global variable that initializes our hash table to fit 50 different accounts
    private final int DEFAULT_NUM_PASSWORDS = 50;

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

    // total number of passwords and accounts we've added to the hash table; updates within methods
    private int numKeys; 

    // Password needs a constructor, set the numKeys to 0 since we haven't added anything
    public Password () {

        // the numkeys tells us how many passwords we have (0 at construction)
        numKeys = 0;
    }

    // ** not going to include any other constructors **

    // **************************** CORRECT ABOVE THESE LINES ********************************

    // method that will be called whenever a new password wants to be inserted
    public boolean insertPass(String acct, String usrnm, Object pswrd) {
        // first we get the hash value of the username
        int position = h1(acct);

        // then we look in the position and see if there's anything there to begin with
        // we also want to check if the position is 'removed' - there used to be a password that
        // we chose to delete
        if (table[position] == null || table[position].account_type == null && (table[position].username == null && table[position].next == null)) {
            // we can simply point the position to the new node we create
            Node newAccount = new Node(acct, usrnm, pswrd);
            table[position] = newAccount;
            numKeys++;
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
            }

            // ** CHECK IF THE ACCT EQUALS BUT NOT THE USERNAME AND VICE VERSA

            else {  // otherwise, there is a duplicate, and trav is pointing to it
                trav.PassValues.insert(pswrd);

                // ** we DONT update numKeys because we're inserting an additional password to an already existing key **
            }
        }

        // we will always return true since we can always add to a Linked Node table
        return true;
    }

    // delete method
    public boolean deletePass(String acct, String usrnm, Object pswrdToDelete) {
        // get the hash value of the username
        int position = h1(acct);

        // make a trav Node so that we can go through the linked list
        Node trav = table[position];

        // 


        // temporary
        return true;
    }

    // search method
    public Object findPassInUsername(Object usrnm, Object pswrd) {
        // get the hash value of the username
        int position = h1(usrnm);

        // make a trav Node so that we can go through the linked list
        Node trav = table[position];

        // make an extra queue to go through the linked list passwords
        LLQueue<Object> passSearch = new LLQueue<Object>();

        // go through the linked list, and empty the LLqueue in a nested loop, by pushing it onto a different queue
        // then back to the original queue
        while (trav != null) {
            // while the password queue is not empty
            while (!trav.PassValues.isEmpty()) {
                // get the first item in the queue and store it in a variable
                Object holder = trav.PassValues.remove();
                
                // if the password we got is the password we're looking for, so we return it
                if (holder.equals(pswrd)) {
                    return holder;
                }

                // insert the password into the temp queue
                passSearch.insert(holder);

            }
            // one more while loop to reset the queue
            while (!passSearch.isEmpty()) {
                // get the first item in the queue
                Object movePassBack = passSearch.remove();

                // place the password back into original queue
                trav.PassValues.insert(movePassBack);
            }

            // move trav to the next value
            trav = trav.next;

        } 

        // if we're here, we never found the password we wanted
        return null;
    }

    // retrieve passwords from one username into an array

    // 

}
