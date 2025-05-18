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
    // a Node class that will contain all the fields of a Linked Password Node
    private class Node {
        private Object username;
        private LLQueue<Object> PassValues;
        private Node next;
        
        private Node(Object usr, Object pswrd) {
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
    
    private Node[] table;      // the hash table itself
    private int numKeys;       // the total number of keys in the table

    // Password needs a constructor: we only need a number so that we can initialize the table
    public Password (int numPasswords) {
        if (numPasswords <= 0) {
            throw new IllegalArgumentException("Must have a number of total passwords greater than 0");
        }

        // initializes the table to the size given
        table = new Node[numPasswords];

        // the numkeys tells us how many passwords we have
        numKeys = 0;
    }

    // default constructor, no size given, so we assume 0 
    public Password() {
        this(0);
    }

    // ** not going to include any other constructors because we either need a number or none so that we can throw an exception **

    // method that will be called whenever a new password wants to be inserted
    public boolean insertPass(String usrnm, Object pswrd) {
        // first we get the hash value of the username
        int position = h1(usrnm);

        // then we look in the position and see if there's anything there to begin with
        // we also want to check if the position is 'removed' - there used to be a password that
        // we chose to delete
        if (table[position] == null || (table[position].username == null && table[position].next == null)) {
            // we can simply point the position to the new node we create
            Node newAccount = new Node((String)usrnm, pswrd);
            table[position] = newAccount;
            numKeys++;
        }
        else { // otherwise, there's something at the position, we must look for duplicates
            Node trav = table[position];
            while (trav != null && !trav.username.equals(usrnm)) {
                trav = trav.next;
            }

            // here if trav is null, there are no duplicates, so we can add the new 'account' to the front of the list
            if (trav == null) {
                // make the Node
                Node newAccount = new Node((String)usrnm, pswrd);
                // point the next field of the new Node to the previous head 
                newAccount.next = table[position];
                // make the new account the new head
                table[position] = newAccount;

                // update numKeys
                numKeys++;
            }
            else {  // otherwise, there is a duplicate, and trav is pointing to it
                trav.PassValues.insert(pswrd);

                // ** we DONT update numKeys because we're inserting an additional password to an already existing key **
            }
        }

        // we will always return true since we can always add to a Linked Node table
        return true;
    }

    // delete method
    public boolean deletePass(Object usrnm, Object pswrdToDelete) {
        // get the hash value of the username
        int position = h1(usrnm);

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
