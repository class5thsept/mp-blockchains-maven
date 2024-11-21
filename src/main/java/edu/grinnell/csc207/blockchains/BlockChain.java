package edu.grinnell.csc207.blockchains;

import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.NoSuchElementException;

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
  int size;
  Node first;
  Node last;
  HashValidator validator;

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
    if (this.balance(t.getSource()) >= t.getAmount()) {
      return new Block(this.size + 1, t, this.getHash(), validator);
    } else {
      return new Block(-1, t, this.getHash(), 0);
    }
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
    Hash testHash;
    try {
      testHash = blk.computeHash();
      if (validator.isValid(blk.getHash()) && (blk.getHash().equals(testHash)) && (blk.getPrevHash().equals(this.getHash()))) {
        Node newNode = new Node(this.last, blk, null);
        this.last.next = newNode;
        this.last = newNode;
        size++;
        // update users and balances
      } else {
        throw new IllegalArgumentException();
      }
    } catch (NoSuchAlgorithmException e) {
      System.err.println("Invalid algorithm");
    }
  } // append()

  /**
   * Attempt to remove the last block from the chain.
   *
   * @return false if the chain has only one block (in which case it's
   *   not removed) or true otherwise (in which case the last block
   *   is removed).
   */
  public boolean removeLast() {
    return true;        // STUB
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
    return true;        // STUB
  } // isCorrect()

  /**
   * Determine if the blockchain is correct in that (a) the balances are
   * legal/correct at every step, (b) that every block has a correct
   * previous hash field, (c) that every block has a hash that is correct
   * for its contents, and (d) that every block has a valid hash.
   *
   * @throws Exception
   *   If things are wrong at any block.
   */
  public void check() throws Exception {
    // STUB
  } // check()

  /**
   * Return an iterator of all the people who participated in the
   * system.
   *
   * @return an iterator of all the people in the system.
   */
  public Iterator<String> users() {
    return new Iterator<String>() {
      public boolean hasNext() {
        return false;   // STUB
      } // hasNext()

      public String next() {
        throw new NoSuchElementException();     // STUB
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
    return 0;   // STUB
  } // balance()

  /**
   * Get an interator for all the blocks in the chain.
   *
   * @return an iterator for all the blocks in the chain.
   */
  public Iterator<Block> blocks() {
    return new Iterator<Block>() {
      public boolean hasNext() {
        return false;   // STUB
      } // hasNext()

      public Block next() {
        throw new NoSuchElementException();     // STUB
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
      public boolean hasNext() {
        return false;   // STUB
      } // hasNext()

      public Transaction next() {
        throw new NoSuchElementException();     // STUB
      } // next()
    };
  } // iterator()

} // class BlockChain
