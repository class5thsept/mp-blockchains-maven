package edu.grinnell.csc207.blockchains;

/**
 * Nodes to contain Blocks in a BlockChain.
 *
 * @author Leonardo Alves Nunes
 * @author Natalie Nardone
 */
public class Node {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The previous node in the chain.
   */
  Node prev;

  /**
   * The next node in the chain.
   */
  Node next;

  /**
   * The block stored in the node.
   */
  Block block;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Creates a new node from the specified block, previous node, and next node.
   *
   * @param prevNode
   *   The previous node in the chain.
   * @param val
   *   The block to be stored in the node.
   * @param nextNode
   *   The next node in the chain.
   */
  public Node(Node prevNode, Block val, Node nextNode) {
    this.prev = prevNode;
    this.block = val;
    this.next = nextNode;
  } // Node(Node, Block, Node)


  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Gets the block in the node.
   *
   * @return the block in the node.
   */
  public Block getBlock() {
    return this.block;
  } // getBlock()

  /**
   * Gets the next node in the chain.
   *
   * @return the next node.
   */
  public Node getNext() {
    return this.next;
  } // getNext()

  /**
   * Gets the previous node in the chain.
   *
   * @return the previous node.
   */
  public Node getPrev() {
    return this.prev;
  } // getPrev()
} // class Node
