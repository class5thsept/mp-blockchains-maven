package edu.grinnell.csc207.blockchains;

public class Node {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+
  Node prev;
  Node next;
  Block block;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+
  public Node(Node prevNode, Block val, Node nextNode) {
    this.prev = prevNode;
    this.block = val;
    this.next = nextNode;
  }


  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+
  public Block getBlock() {
    return this.block;
  }

  


}
