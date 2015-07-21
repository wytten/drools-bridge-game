package org.wyttenbach.dale.drools;

public enum Player {
  SOUTH,
  WEST,
  NORTH,
  EAST;

  private Hand hand;

  public Player next() {
    int index = (this.ordinal() + 1) % values().length;
    return values()[index];
  }

  public Hand getHand() {
    return hand;
  }

  void setHand(Hand hand) {
    this.hand = hand;
  }
}
