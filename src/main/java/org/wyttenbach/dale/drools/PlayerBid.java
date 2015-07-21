package org.wyttenbach.dale.drools;

public class PlayerBid {
  private Player player;

  private Bid bid;

  public PlayerBid(Player player, Bid bid) {
    this.player = player;
    this.bid = bid;
  }

  public Player getPlayer() {
    return player;
  }

  public Bid getBid() {
    return bid;
  }

  @Override
  public String toString() {
    return "PlayerBid [player=" + player + ", bid=" + bid + "]";
  }
}
