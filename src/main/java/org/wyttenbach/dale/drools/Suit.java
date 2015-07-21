package org.wyttenbach.dale.drools;

public enum Suit {
  CLUBS('\u2663'), DIAMONDS('\u2666'), HEARTS('\u2665'), SPADES('\u2660');
  
  private Character symbol;
  
  Suit(Character symbol) {
    this.symbol = symbol;
  }
  
  public Character getSymbol() {
    return symbol;
  }
}
