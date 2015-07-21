package org.wyttenbach.dale.drools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.wyttenbach.dale.drools.Player.EAST;
import static org.wyttenbach.dale.drools.Player.NORTH;
import static org.wyttenbach.dale.drools.Player.SOUTH;
import static org.wyttenbach.dale.drools.Player.WEST;
import static org.wyttenbach.dale.drools.Rank.ACE;
import static org.wyttenbach.dale.drools.Rank.EIGHT;
import static org.wyttenbach.dale.drools.Rank.KING;
import static org.wyttenbach.dale.drools.Rank.NINE;
import static org.wyttenbach.dale.drools.Rank.QUEEN;
import static org.wyttenbach.dale.drools.Rank.SIX;
import static org.wyttenbach.dale.drools.Suit.CLUBS;
import static org.wyttenbach.dale.drools.Suit.HEARTS;
import static org.wyttenbach.dale.drools.Suit.SPADES;

import java.util.SortedSet;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelTest {
  
  @SuppressWarnings("unused")
  private final static Logger LOG = LoggerFactory.getLogger(ModelTest.class);
  
  @Test
  public void testRank() {
    assertTrue(ACE.ordinal() > EIGHT.ordinal());
    assertTrue(QUEEN.ordinal() < KING.ordinal());
  }

  @Test
  public void testCard() {
    Card card1 = new Card(SPADES, ACE);
    assertEquals(4, card1.getPoints());

    Card card2 = new Card(SPADES, ACE);
    assertEquals(4, card2.getPoints());

    Card card3 = new Card(SPADES, KING);
    assertEquals(3, card3.getPoints());

    Card card4 = new Card(SPADES, NINE);
    assertEquals(0, card4.getPoints());

    assertEquals(card1, card2);
    assertNotEquals(card3, card2);
  }

  @Test
  public void testDeck() {
    Deck deck = new Deck(1);
    assertEquals(52, deck.getCards().size());
    Card card = new Card(CLUBS, KING);
    assertEquals(card, deck.getCards().get(0));
  }

  @Test
  public void testDeal() {
    Deck deck = new Deck();
    Deal deal = new Deal(deck);
    
    int totalPoints = 0;
    for (Player player : Player.values()) {
      totalPoints += deal.getHand(player).getHighCardPoints();
    }
    assertEquals(40, totalPoints);
  }
  
  @Test
  public void testDeal1() {
    Deck deck = new Deck(1);
    Deal deal = new Deal(deck);

    Hand south = deal.getHand(SOUTH);
    assertEquals(6, south.getHighCardPoints());

    Hand east = deal.getHand(EAST);
    assertEquals(12, east.getHighCardPoints());

    Hand west = deal.getHand(WEST);
    assertEquals(12, west.getHighCardPoints());

    Hand north = deal.getHand(NORTH);
    assertEquals(10, north.getHighCardPoints());
    SortedSet<Card> bunch = north.getCards(HEARTS);
    assertEquals(2, bunch.size());
    Card card = new Card(HEARTS, SIX);
    assertEquals(card, bunch.first());
  }

  @Test
  public void testPlayer() {
    assertEquals(WEST, SOUTH.next());
    assertEquals(NORTH, WEST.next());
    assertEquals(EAST, NORTH.next());
    assertEquals(SOUTH, EAST.next());
  }
  
  @Test
  public void testSymbols() {
    for (Suit suit: Suit.values()) {
      System.out.println(suit + " " + suit.getSymbol());
    }
  }
}
