package org.wyttenbach.dale.drools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck {
  
  private Random random = new Random();
  
  private List<Card> cards = new ArrayList<Card>();

  public Deck() {
    for (Rank rank : Rank.values()) {
      for (Suit suit : Suit.values()) {
        cards.add(new Card(suit, rank));
      }
    }
  }

  public Deck(long seed) {
    this();
    this.random = new Random(seed);
    shuffle();
  }
  
  public void shuffle() {
    Collections.shuffle(cards, random);
  }

  public Card draw() {
    Card card = cards.remove(0);
    return card;
  }
  
  public List<Card> getCards() {
    return cards;
  }
}
