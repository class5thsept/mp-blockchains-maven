package edu.grinnell.csc207.blockchains;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Blocks to be stored in blockchains.
 *
 * @author Leonardo Alves Nunes
 * @author Natalie Nardone
 * @author Samuel A. Rebelsky
 */
public class Block {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The number of the block in the blockchain.
   */
  int num;

  /**
   * The transaction stored in the block.
   */
  Transaction transaction;

  /**
   * The hash of the previous block in the chain.
   */
  Hash prevHash;

  /**
   * The nonce of this block.
   */
  long nonce;

  /**
   * The hash of this block.
   */
  Hash hash;

  /**
   * The MessageDigest to calculate the hash for each block.
  */
  MessageDigest md;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new block from the specified block number, transaction, and
   * previous hash, mining to choose a nonce that meets the requirements
   * of the validator.
   *
   * @param num
   *   The number of the block.
   * @param transaction
   *   The transaction for the block.
   * @param prevHash
   *   The hash of the previous block.
   * @param check
   *   The validator used to check the block.
   */
  public Block(int num, Transaction transaction, Hash prevHash,
      HashValidator check) {
    this.num = num;
    this.transaction = transaction;
    this.prevHash = prevHash;
    long count = 0;
    do {
      this.nonce = count;
      try {
        this.hash = this.computeHash();
      } catch (NoSuchAlgorithmException e) {
        System.err.println("Invalid algorithm");
      } // try/catch
      count++;
    } while (!check.isValid(hash));
  } // Block(int, Transaction, Hash, HashValidator)

  /**
   * Create a new block, computing the hash for the block.
   *
   * @param num
   *   The number of the block.
   * @param transaction
   *   The transaction for the block.
   * @param prevHash
   *   The hash of the previous block.
   * @param nonce
   *   The nonce of the block.
   */
  public Block(int num, Transaction transaction, Hash prevHash, long nonce) {
    this.num = num;
    this.transaction = transaction;
    this.prevHash = prevHash;
    this.nonce = nonce;
    try {
      this.hash = this.computeHash();
    } catch (NoSuchAlgorithmException e) {
      System.err.println("Invalid algorithm");
    } // try/catch
  } // Block(int, Transaction, Hash, long)

  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

  /**
   * Compute the hash of the block given all the other info already
   * stored in the block.
   * @return the calculated hash of the block.
   */
  public Hash computeHash() throws NoSuchAlgorithmException {
    this.md = MessageDigest.getInstance("sha-256");
    byte[] numbytes = ByteBuffer.allocate(Integer.BYTES).putInt(num).array();
    byte[] sourcebytes = transaction.getSource().getBytes();
    byte[] targetbytes = transaction.getTarget().getBytes();
    byte[] amountbytes = ByteBuffer.allocate(Integer.BYTES).putInt(transaction.getAmount()).array();
    byte[] prevbytes = prevHash.getBytes();
    byte[] noncebytes = ByteBuffer.allocate(Long.BYTES).putLong(nonce).array();
    md.update(numbytes);
    md.update(sourcebytes);
    md.update(targetbytes);
    md.update(amountbytes);
    md.update(prevbytes);
    md.update(noncebytes);
    byte[] hash = md.digest();
    return new Hash(hash);
  } // computeHash()

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Get the number of the block.
   *
   * @return the number of the block.
   */
  public int getNum() {
    return num;
  } // getNum()

  /**
   * Get the transaction stored in this block.
   *
   * @return the transaction.
   */
  public Transaction getTransaction() {
    return transaction;
  } // getTransaction()

  /**
   * Get the nonce of this block.
   *
   * @return the nonce.
   */
  public long getNonce() {
    return nonce;
  } // getNonce()

  /**
   * Get the hash of the previous block.
   *
   * @return the hash of the previous block.
   */
  Hash getPrevHash() {
    return prevHash;
  } // getPrevHash

  /**
   * Get the hash of the current block.
   *
   * @return the hash of the current block.
   */
  Hash getHash() {
    return hash;
  } // getHash

  /**
   * Get a string representation of the block.
   *
   * @return a string representation of the block.
   */
  public String toString() {
    return
      "Block " + this.num + " (Transaction: "
      + transaction.toString() + " , Nonce: "
      + this.nonce
       + " , prevHash: "
        + this.prevHash
         + " , hash: "
          + this.hash + " )";
  } // toString()
} // class Block
