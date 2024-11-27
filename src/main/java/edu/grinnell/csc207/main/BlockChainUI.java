package edu.grinnell.csc207.main;

import edu.grinnell.csc207.blockchains.Block;
import edu.grinnell.csc207.blockchains.BlockChain;
import edu.grinnell.csc207.blockchains.HashValidator;
import edu.grinnell.csc207.blockchains.Transaction;

import edu.grinnell.csc207.util.IOUtils;

import java.io.PrintWriter;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * A simple UI for our BlockChain class.
 *
 * @author Leonardo Alves Nunes
 * @author Natalie Nardone
 * @author Samuel A. Rebelsky
 */
public class BlockChainUI {
  // +-----------+---------------------------------------------------
  // | Constants |
  // +-----------+

  /**
   * The number of bytes we validate. Should be set to 3 before submitting.
   */
  static final int VALIDATOR_BYTES = 3;

  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

  /**
   * Print out the instructions.
   *
   * @param pen
   *   The pen used for printing instructions.
   */
  public static void instructions(PrintWriter pen) {
    pen.println("""
      Valid commands:
        mine: discovers the nonce for a given transaction
        append: appends a new block onto the end of the chain
        remove: removes the last block from the end of the chain
        check: checks that the block chain is valid
        users: prints a list of users
        balance: finds a user's balance
        transactions: prints out the chain of transactions
        blocks: prints out the chain of blocks (for debugging only)
        help: prints this list of commands
        quit: quits the program""");
  } // instructions(PrintWriter)

  // +------+--------------------------------------------------------
  // | Main |
  // +------+

  /**
   * Run the UI.
   *
   * @param args
   *   Command-line arguments (currently ignored).
   */
  public static void main(String[] args) throws Exception {
    PrintWriter pen = new PrintWriter(System.out, true);
    BufferedReader eyes = new BufferedReader(new InputStreamReader(System.in));

    // Set up our blockchain.
    HashValidator validator =
        (h) -> {
          if (h.length() < VALIDATOR_BYTES) {
            return false;
          } // if
          for (int v = 0; v < VALIDATOR_BYTES; v++) {
            if (h.get(v) != 0) {
              return false;
            } // if
          } // for
          return true;
        };
    BlockChain chain = new BlockChain(validator);

    instructions(pen);

    boolean done = false;

    String source;
    String target;
    int amount;

    while (!done) {
      pen.print("\nCommand: ");
      pen.flush();
      String command = eyes.readLine();
      if (command == null) {
        command = "quit";
      } // if

      switch (command.toLowerCase()) {
        case "append":
          source = IOUtils.readLine(pen, eyes, "Source (return for deposit): ");
          target = IOUtils.readLine(pen, eyes, "Target: ");
          amount = IOUtils.readInt(pen, eyes, "Amount: ");
          long nonce = Long.parseUnsignedLong(IOUtils.readLine(pen, eyes, "Nonce: "));
          Block newBlock = new Block(chain.getSize() + 1, new Transaction(source, target, amount),
              chain.getHash(), nonce);
          try {
            chain.append(newBlock);
            pen.println("Appended: " + newBlock.toString());
          } catch (IllegalArgumentException e) {
            pen.printf("Could not append - invalid hash for contents.");
          } // try/catch
          break;

        case "balance":
          String user = IOUtils.readLine(pen, eyes, "User: ");
          pen.printf("%s's balance is %d ", user, chain.balance(user));
          break;

        case "blocks":
          Iterator<Block> blocks = chain.blocks();
          while (blocks.hasNext()) {
            pen.printf(blocks.next().toString());
            pen.printf("\n");
          } // while
          break;

        case "check":
          if (chain.isCorrect()) {
            pen.printf("The blockchain checks out.");
          } else {
            try {
              chain.check();
            } catch (Exception e) {
              pen.printf("%s", e);
            } // try/catch
          } // if/else
          break;

        case "help":
          instructions(pen);
          break;

        case "mine":
          source = IOUtils.readLine(pen, eyes, "Source (return for deposit): ");
          target = IOUtils.readLine(pen, eyes, "Target: ");
          amount = IOUtils.readInt(pen, eyes, "Amount: ");
          Block b = chain.mine(new Transaction(source, target, amount));
          pen.println("\nUse nonce: " + b.getNonce());
          break;

        case "quit":
          done = true;
          break;

        case "remove":
          chain.removeLast();
          pen.printf("Removed last element");
          break;

        case "transactions":
          Iterator<Transaction> transactions = chain.iterator();
          transactions.next();
          while (transactions.hasNext()) {
            pen.printf(transactions.next().toString());
            pen.printf("\n");
          } // while
          break;

        case "users":
          for (int i = 0; i < chain.getUserList().size(); i++) {
            pen.printf(chain.getUserList().get(i));
            pen.printf("\n");
          } // for
          break;

        default:
          pen.printf("invalid command: '%s'. Try again.\n", command);
          break;
      } // switch
    } // while

    pen.printf("\nGoodbye\n");
    eyes.close();
    pen.close();
  } // main(String[])
} // class BlockChainUI
