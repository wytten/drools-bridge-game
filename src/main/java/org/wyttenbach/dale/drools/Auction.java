package org.wyttenbach.dale.drools;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Auction {
  private final static Logger LOG = LoggerFactory.getLogger(Auction.class);

  private Player current;

  private List<PlayerBid> bids = new ArrayList<PlayerBid>();

  public Auction(Player current) {
    this.current = current;
  }

  public Player getCurrentBidder() {
    return current;
  }

  public void bid(Player player, Bid bid) {
    assert player.equals(current);
    LOG.info("size="+bids.size());
    LOG.info("player="+player+", bid="+bid+", hand="+player.getHand());
    bids.add(new PlayerBid(player, bid));
    current = player.next();
  }

  public List<PlayerBid> getPlayerBids() {
    return bids;
  }

  public PlayerBid getLastPlayerBid() {
    PlayerBid lastBid = null;
    if (bids.size() > 0) {
      lastBid = bids.get(bids.size() - 1);
    }
    return lastBid;
  }

  public Contract getContract() {
    Contract contract = null;
    @SuppressWarnings("unused")
    int pass = 0;
    int doubled = 0;
    // Iterate bids in reverse.
    for (ListIterator<PlayerBid> li = bids.listIterator(bids.size()); li.hasPrevious();) {
      PlayerBid bid = li.previous();
      if (bid.getBid() instanceof Pass) {
        pass++;
        continue;
      } else if (bid.getBid() instanceof Doubled) {
        doubled++;
        continue;
      } else {
        // TODO assert pass == 3;
        assert doubled < 3;
        contract = new Contract(bid.getPlayer(), bid.getBid());
        switch (doubled) {
        case 0:
          break;
        case 1:
          contract.setDoubled(true);
        case 2:
          contract.setRedoubled(true);
        }
      }
    }

    return contract;
  }
}
