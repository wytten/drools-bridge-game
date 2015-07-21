package org.wyttenbach.dale.drools;

public class Contract {
  private Player declarer;
  
  private Bid bid;
  
  private Boolean doubled;
  
  private Boolean redoubled;

  public Contract(Player declarer, Bid bid) {
    super();
    this.declarer = declarer;
    this.bid = bid;
  }

  public Player getDeclarer() {
    return declarer;
  }

  public void setDeclarer(Player declarer) {
    this.declarer = declarer;
  }

  public Bid getBid() {
    return bid;
  }

  public void setBid(Bid bid) {
    this.bid = bid;
  }

  public Boolean getDoubled() {
    return doubled;
  }

  public void setDoubled(Boolean doubled) {
    this.doubled = doubled;
  }

  public Boolean getRedoubled() {
    return redoubled;
  }

  public void setRedoubled(Boolean redoubled) {
    this.redoubled = redoubled;
  }

  @Override
  public String toString() {
    return "Contract [declarer=" + declarer + ", bid=" + bid + ", doubled=" + doubled + ", redoubled=" + redoubled
        + "]";
  }
  
}
