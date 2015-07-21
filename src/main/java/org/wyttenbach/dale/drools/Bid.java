package org.wyttenbach.dale.drools;

public class Bid {
  private int level;

  private Suit trump;

  public Bid(int level, Suit trump) {
    this.level = level;
    this.trump = trump;
  }

  @Override
  public String toString() {
    return "Bid [level=" + level + ", trump=" + trump + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + level;
    result = prime * result + ((trump == null) ? 0 : trump.hashCode());
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
    Bid other = (Bid) obj;
    if (level != other.level)
      return false;
    if (trump != other.trump)
      return false;
    return true;
  }
}
