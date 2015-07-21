package org.wyttenbach.dale.drools;

import static org.junit.Assert.assertEquals;
import static org.wyttenbach.dale.drools.Player.NORTH;
import static org.wyttenbach.dale.drools.Suit.CLUBS;
import static org.wyttenbach.dale.drools.Suit.DIAMONDS;
import static org.wyttenbach.dale.drools.Suit.HEARTS;
import static org.wyttenbach.dale.drools.Suit.SPADES;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.conf.SequentialOption;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatelessKnowledgeSession;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpeningBidTest {
  private final static Logger LOG = LoggerFactory.getLogger(OpeningBidTest.class);
  
  static StatelessKnowledgeSession session;

  private Deal deal;

  private Auction auction;

  @BeforeClass
  public static void setUpClass() throws Exception {
    KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
    builder.add(ResourceFactory.newClassPathResource("bidding.drl"), ResourceType.DRL);
    if (builder.hasErrors()) {
      throw new RuntimeException(builder.getErrors().toString());
    }

    KnowledgeBaseConfiguration configuration = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
    configuration.setOption(SequentialOption.YES);

    KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase(configuration);
    knowledgeBase.addKnowledgePackages(builder.getKnowledgePackages());

    session = knowledgeBase.newStatelessKnowledgeSession();
  }

  @Before
  public void setUp() {
    LOG.info("-----------------------------------------------------------------------");
  }
  
  @Test
  public void testPass() {
    deal = new Deal(new Deck(1));
    Hand hand = deal.getHand(NORTH);
    assertBid(hand, new Pass());
  }

  @Test
  public void testBid1Club() {
    deal = new Deal(new Deck(8));
    Hand hand = deal.getHand(NORTH);
    assertBid(hand, new Bid(1, CLUBS));
  }

  @Test
  public void testBid1Diamond() {
    deal = new Deal(new Deck(74));
    Hand hand = deal.getHand(NORTH);
    assertBid(hand, new Bid(1, DIAMONDS));
  }
  
  @Test
  public void testBid1Spade() {
    deal = new Deal(new Deck(23));
    Hand hand = deal.getHand(NORTH);
    assertBid(hand, new Bid(1, SPADES));
  }

  @Test
  public void testBid1Heart() {
    deal = new Deal(new Deck(41));
    Hand hand = deal.getHand(NORTH);
    assertBid(hand, new Bid(1, HEARTS));
  }

  @Test
  public void testBid1NoTrump() {
    deal = new Deal(new Deck(11));
    Hand hand = deal.getHand(NORTH);
    assertBid(hand, new Bid(1, null));
  }

  @Test
  public void testLocate() {
    for (long seed = 1; seed < Long.MAX_VALUE; seed++) {
      deal = new Deal(new Deck(seed));

      Hand hand = deal.getHand(NORTH);
      if (hand.getHighCardPoints() == 13 && hand.getLongestSuit().equals(DIAMONDS)) {
        LOG.info(""+hand);
        LOG.info("Seed: " + seed);
        break;
      }
    }
  }

  private void execute(Hand hand) {
    LOG.info(""+hand);
    @SuppressWarnings("rawtypes")
    List<Command> commands = new ArrayList<Command>();
    commands.add(CommandFactory.newInsertElements(getFacts(hand)));
    session.execute(CommandFactory.newBatchExecution(commands));
  }

  private void assertBid(Hand hand, Bid bid) {
    execute(hand);
    assertEquals(bid, auction.getLastPlayerBid().getBid());
    assertEquals(hand.getOwner(), auction.getLastPlayerBid().getPlayer());
    LOG.info(""+auction.getLastPlayerBid());
    LOG.info(""+auction.getContract());
  }

  private Collection<Object> getFacts(Hand hand) {
    ArrayList<Object> facts = new ArrayList<Object>();
    for (Player player: Player.values()) {
      facts.add(deal.getHand(player));
    }
    facts.add(auction = new Auction(hand.getOwner()));
    // TODO: Why did Michal's example add attributes as facts?
    // Because it works equally well with the next line commented out.
    //facts.add(hand.getHighCardPoints());
    return facts;
  }

}
