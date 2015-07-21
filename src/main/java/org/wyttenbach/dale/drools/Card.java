package org.wyttenbach.dale.drools;

public class Card implements Comparable<Card> {
  private Suit suit;

  private Rank rank;

  public Card(Suit suit, Rank rank) {
    this.suit = suit;
    this.rank = rank;
  }

  public Suit getSuit() {
    return suit;
  }

  public Rank getRank() {
    return rank;
  }

  public int getPoints() {
    return Math.max(rank.ordinal() - 8, 0);
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((rank == null) ? 0 : rank.hashCode());
    result = prime * result + ((suit == null) ? 0 : suit.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Card other = (Card) obj;
    if (rank != other.rank)
      return false;
    if (suit != other.suit)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Card [suit=" + suit + ", rank=" + rank + "]";
  }

  @Override
  public int compareTo(Card other) {
    assert other.getSuit().equals(this.getSuit());
    return other.getRank().compareTo(this.getRank());
  }
}
