package org.wyttenbach.dale.drools;

import java.util.HashMap;
import java.util.Map;

public class Deal {
  private Map<Player, Hand> hands = new HashMap<Player, Hand>();

  public Deal(Deck deck) {
    for (Player player : Player.values()) {
      Hand hand = new Hand(player);
      hands.put(player, hand);
      player.setHand(hand);
    }
    for (int i = 0; i < 13; i++) {
      for (Player player : Player.values()) {
        getHand(player).add(deck.draw());
      }
    }
  }

  public Hand getHand(Player player) {
    return hands.get(player);
  }
}
