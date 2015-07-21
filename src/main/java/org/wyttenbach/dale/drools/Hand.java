package org.wyttenbach.dale.drools;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class Hand {

  private Map<Suit, SortedSet<Card>> cards = new HashMap<Suit, SortedSet<Card>>();
  
  private Player owner;

  public Hand(Player owner) {
    this.owner = owner;
    for (Suit suit : Suit.values()) {
      cards.put(suit, new TreeSet<Card>());
    }
  }
  
  public Player getOwner() {
    return owner;
  }

  public SortedSet<Card> getCards(Suit suit) {
    return cards.get(suit);
  }
  
  public void add(Card card) {
    SortedSet<Card> bunch = cards.get(card.getSuit());
    bunch.add(card);
  }

  public Suit getLongestSuit() {
    Suit longestSuit = null;
    int length = 0;
    for (Suit suit : Suit.values()) {
      SortedSet<Card> bunch = cards.get(suit);
      if (bunch.size() > length) {
        length = bunch.size();
        longestSuit = suit;
      }
    }
    assert longestSuit != null;
    return longestSuit;
  }

  public int getHighCardPoints() {
    int points = 0;
    for (Suit suit : cards.keySet()) {
      SortedSet<Card> bunch = getCards(suit);
      for (Card card : bunch) {
        // loop optimization: If this card is 0, all the rest are 0 as well
        if (card.getPoints() == 0) {
          break;
        }
        points += card.getPoints();
      }
    }
    return points;
  }

  public int getTotalPoints(Suit trump) {
    int points = getHighCardPoints();
    if (trump != null) {
      for (Suit suit : Suit.values()) {
        if (suit.equals(trump)) {
          continue;
        }
        SortedSet<Card> bunch = getCards(suit);
        switch (bunch.size()) {
        case 0:
          points += 3;
          break;
        case 1:
          points += 2;
          break;
        case 2:
          points += 1;
          break;
        }
      }
    }
    return points;
  }

  public boolean isBalanced() {
    int doubletonCount = 0;
    for (Suit suit : cards.keySet()) {
      SortedSet<Card> bunch = getCards(suit);
      switch (bunch.size()) {
      case 0:
      case 1:
        return false;
      case 2:
        doubletonCount++;
      }
    }
    return doubletonCount <= 1;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Suit suit : cards.keySet()) {
      SortedSet<Card> bunch = getCards(suit);
      sb.append(suit).append('(').append(bunch.size()).append(')').append(": ");
      for (Card card : getCards(suit)) {
        sb.append(card.getRank()).append(' ');
      }
      if (bunch.size() > 0) {
        sb.append('(').append(getTotalPoints(suit)).append(" points)");
      }
      sb.append('\n');
    }
    sb.append("HCP: " + getHighCardPoints());
    return sb.toString();
  }
}
