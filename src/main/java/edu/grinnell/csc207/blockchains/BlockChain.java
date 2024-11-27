package edu.grinnell.csc207.blockchains;

import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.grinnell.csc207.util.AssociativeArray;
import edu.grinnell.csc207.util.KeyNotFoundException;
import java.util.ArrayList;

/**
 * A full blockchain.
 *
 * @author Leonardo Alves Nunes
 * @author Natalie Nardone
 */
public class BlockChain implements Iterable<Transaction> {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The number of blocks in the BlockChain.
   */
  int size;

  /**
   * The first node in the BlockChain.
   */
  Node first;

  /**
   * The last node in the BlockChain.
   */
  Node last;

  /**
   * The validator used to validate the blocks in the BlockChain.
   */
  HashValidator validator;

  /**
   * An AssociativeArray of all of the people in the BlockChain and their current balances.
   */
  AssociativeArray<String, Integer> balances;

  /**
   * An ArrayList of all of the people in the BlockChain.
   */
  ArrayList<String> userList;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new blockchain using a validator to check elements.
   *
   * @param check
   *   The validator used to check elements.
   */
  public BlockChain(HashValidator check) {
    Block newBlock = new Block(0, new Transaction("", "", 0), new Hash(new byte[] {}), check);
    Node newNode = new Node(null, newBlock, null);
    this.size = 1;
    this.first = newNode;
    this.last = newNode;
    this.validator = check;
    this.balances = new AssociativeArray<String, Integer>();
    this.userList = new ArrayList<String>();
  } // BlockChain(HashValidator)

  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Mine for a new valid block for the end of the chain, returning that
   * block.
   *
   * @param t
   *   The transaction that goes in the block.
   *
   * @return a new block with correct number, hashes, and such.
   */
  public Block mine(Transaction t) {
    return new Block(this.size + 1, t, this.getHash(), validator);
  } // mine(Transaction)

  /**
   * Get the number of blocks curently in the chain.
   *
   * @return the number of blocks in the chain, including the initial block.
   */
  public int getSize() {
    return this.size;
  } // getSize()

  /**
   * Gets the list of all of the users in the chain.
   *
   * @return a list of the users in the chain.
   */
  public ArrayList<String> getUserList() {
    return this.userList;
  } // getSize()

  /**
   * Add a block to the end of the chain.
   *
   * @param blk
   *   The block to add to the end of the chain.
   *
   * @throws IllegalArgumentException if (a) the hash is not valid, (b)
   *   the hash is not appropriate for the contents, or (c) the previous
   *   hash is incorrect.
   */
  public void append(Block blk) {
    // need to handle case where didn't mine a block or invalid nonce, etc
    Hash testHash;
    try {
      testHash = blk.computeHash();
      if (validator.isValid(blk.getHash()) && (blk.getHash().equals(testHash))
           && (blk.getPrevHash().equals(this.getHash()))) {
        Node newNode = new Node(this.last, blk, null);
        this.last.next = newNode;
        this.last = newNode;
        size++;

        // update transactionss
        String source = blk.getTransaction().getSource();
        String target = blk.getTransaction().getTarget();
        int amount = blk.getTransaction().getAmount();
        if (!source.equals("")) {
          if (balances.hasKey(source)) {
            try {
              balances.set(source, balances.get(source) - amount);
            } catch (Exception e) {
              System.err.println("Key exception");
            } // try/catch
          } else {
            // not valid
            this.userList.add(source);
            try {
              balances.set(source, 0 - amount);
            } catch (Exception e) {
              System.err.println("Key exception");
            } // try/catch
          } // if/else
        } // if
        if (balances.hasKey(target)) {
          try {
            balances.set(target, balances.get(target) + amount);
          } catch (Exception e) {
            System.err.println("Key exception");
          } // try/catch
        } else {
          this.userList.add(target);
          try {
            balances.set(target, amount);
          } catch (Exception e) {
            System.err.println("Key exception");
          } // try/catch
        } // if/else
      } else {
        throw new IllegalArgumentException();
      } // if/else
    } catch (NoSuchAlgorithmException e) {
      System.err.println("Invalid algorithm");
    } // try/catch
  } // append()

  /**
   * Attempt to remove the last block from the chain.
   *
   * @return false if the chain has only one block (in which case it's
   *   not removed) or true otherwise (in which case the last block
   *   is removed).
   */
  public boolean removeLast() {
    if (this.size == 1) {
      return false;
    } else {
      Node removed = this.last;
      this.last = this.last.prev;
      this.last.next = null;
      this.size--;

      String source = removed.getBlock().getTransaction().getSource();
      String target = removed.getBlock().getTransaction().getTarget();
      int amount = removed.getBlock().getTransaction().getAmount();

      try {
        if (!source.equals("")) {
          balances.set(source, balances.get(source) + amount);
          balances.set(target, balances.get(target) - amount);
        } else {
          balances.set(target, balances.get(target) - amount);
        } // if/else
      } catch (Exception e) {
        System.err.println("Key exception");
      } // try/catch

      return true;
    } // if/else
  } // removeLast()

  /**
   * Get the hash of the last block in the chain.
   *
   * @return the hash of the last sblock in the chain.
   */
  public Hash getHash() {
    return this.last.getBlock().getHash();
  } // getHash()

  /**
   * Determine if the blockchain is correct in that (a) the balances are
   * legal/correct at every step, (b) that every block has a correct
   * previous hash field, (c) that every block has a hash that is correct
   * for its contents, and (d) that every block has a valid hash.
   *
   * @return true if the blockchain is correct and false otherwise.
   */
  public boolean isCorrect() {
    // source is in balances and has >= money than the amount
    // compare current prevHash to prev hash
    // compare computeHash to hash
    // check hash with validator
    Block current;
    Block previous;

    Iterator<Block> blocks = this.blocks();

    if (this.size == 1) {
      return true;
    } // if
    previous = blocks.next();
    //blocks.next();

    while (blocks.hasNext()) {
      current = blocks.next();
      String source = current.getTransaction().getSource();
      if (current.getTransaction().getAmount() < 0) {
        return false;
      } // if
      if (!source.equals("")) {
        try {
          if ((!balances.hasKey(source)) || (balances.get(source) < 0)) {
            return false;
          } // if
        } catch (KeyNotFoundException e) {
          System.err.println("Could not find the key");
        } // try/catch
      } // if

      if (!previous.getHash().equals(current.getPrevHash())) {
        return false;
      } // if

      try {
        if (!current.getHash().equals(current.computeHash())) {
          return false;
        } // if
      } catch (NoSuchAlgorithmException e) {
        System.err.println("Invalid Algorithm");
      } // try/catch

      if (!validator.isValid(current.getHash())) {
        return false;
      } // if
      previous = current;
    } // while
    return true;
  } // isCorrect()

  /**
   * Determine if the blockchain is correctif (current.getTransaction().getAmount() < 0) {
        return false;
      } in that (a) the balances are
   * legal/correct at every step, (b) that every block has a correct
   * previous hash field, (c) that every block has a hash that is correct
   * for its contents, and (d) that every block has a valid hash.
   *
   * @throws Exception
   *   If things are wrong at any block.
   */
  public void check() throws Exception {
    Block current;
    Block previous;

    Iterator<Block> blocks = this.blocks();

    if (this.size > 1) {
      previous = blocks.next();
      //blocks.next();

      while (blocks.hasNext()) {
        current = blocks.next();
        String source = current.getTransaction().getSource();

        if (current.getTransaction().getAmount() < 0) {
          throw new Exception("Negative amount in transaction: "
           + current.getTransaction().getAmount());
        } // if

        if (!source.equals("")) {
          try {
            if ((!balances.hasKey(source))) {
              throw new Exception("Unknown source of transaction: " + source);
            } // if
            if ((balances.get(source) < 0)) {
              throw new Exception("Negative balance for: " + source);
            } // if
          } catch (KeyNotFoundException e) {
            System.err.println("Could not find the key");
          } // try/catch
        } // if

        if (!previous.getHash().equals(current.getPrevHash())) {
          throw new Exception("Previous hash does not match with prevHash of the current one."
            + "prevHash of current: " + current.getPrevHash()
            + ". hash of previous: " + previous.getHash());
        } // if

        try {
          if (!current.getHash().equals(current.computeHash())) {
            throw new Exception("The provided hash does not match the hash generated by "
             + "the contents");
          } // if
        } catch (NoSuchAlgorithmException e) {
          System.err.println("Invalid Algorithm");
        } // try/catch

        if (!validator.isValid(current.getHash())) {
          throw new Exception("Invalid Hash. Hash: " + current.getHash());
        } // if
        previous = current;
      } // while
    } // if
  } // check()

  /**
   * Return an iterator of all the people who participated in the
   * system.
   *
   * @return an iterator of all the people in the system.
   */
  public Iterator<String> users() {
    return new Iterator<String>() {

      int cur = 0;
      String update = "";

      public boolean hasNext() {
        return (this.cur < BlockChain.this.userList.size());
      } // hasNext()

      public String next() {
        if (!this.hasNext()) {
          throw new NoSuchElementException();
        } else {
          this.update = BlockChain.this.userList.get(this.cur);
          this.cur++;
          return this.update;
        } // if/else
      } // next()
    };
  } // users()

  /**
   * Find one user's balance.
   *
   * @param user
   *   The user whose balance we want to find.
   *
   * @return that user's balance (or 0, if the user is not in the system).
   */
  public int balance(String user) {
    if (this.balances.hasKey(user)) {
      Iterator<Block> blocks = this.blocks();
      Block current;
      int userBal = 0;
      while (blocks.hasNext()) {
        current = blocks.next();
        if (current.getTransaction().getSource().equals(user)) {
          userBal = userBal - current.getTransaction().getAmount();
        } // if
        if (current.getTransaction().getTarget().equals(user)) {
          userBal = userBal + current.getTransaction().getAmount();
        } // if
      } // while
      return userBal;
    } else {
      return 0;
    } // if/else
  } // balance()

  /**
   * Get an interator for all the blocks in the chain.
   *
   * @return an iterator for all the blocks in the chain.
   */
  public Iterator<Block> blocks() {
    return new Iterator<Block>() {
      // FIELDS
      Node next = BlockChain.this.first;
      Node update = null;

      public boolean hasNext() {
        return (this.next != null);
      } // hasNext()

      public Block next() {
        if (!this.hasNext()) {
          throw new NoSuchElementException();
        } else {
          this.update = this.next;
          this.next = this.next.getNext();
          return this.update.getBlock();
        } // if/else
      } // next()
    };
  } // blocks()

  /**
   * Get an interator for all the transactions in the chain.
   *
   * @return an iterator for all the blocks in the chain.
   */
  public Iterator<Transaction> iterator() {
    return new Iterator<Transaction>() {
      // FIELDS
      Node next = BlockChain.this.first;
      Node update = null;

      public boolean hasNext() {
        return (this.next != null);
      } // hasNext()

      public Transaction next() {
        if (!this.hasNext()) {
          throw new NoSuchElementException();
        } else {
          this.update = this.next;
          this.next = this.next.getNext();
          return this.update.getBlock().getTransaction();
        } // if/else
      } // next()
    };
  } // iterator()

} // class BlockChain
