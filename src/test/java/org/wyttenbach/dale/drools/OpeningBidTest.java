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

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.conf.SequentialOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpeningBidTest {
  private final static Logger LOG = LoggerFactory.getLogger(OpeningBidTest.class);
  
  static KieServices kieServices;

  static StatelessKieSession session;

  private Deal deal;

  private Auction auction;

  @BeforeClass
  public static void setUpClass() throws Exception {
    kieServices = KieServices.Factory.get();
    KieFileSystem fileSystem = kieServices.newKieFileSystem();
    fileSystem.write("src/main/resources/org/wyttenbach/dale/drools/bidding.drl",
        kieServices.getResources().newClassPathResource("bidding.drl", OpeningBidTest.class));
    KieBuilder builder = kieServices.newKieBuilder(fileSystem).buildAll();
    if (builder.getResults().hasMessages(Message.Level.ERROR)) {
      throw new RuntimeException(builder.getResults().toString());
    }

    KieBaseConfiguration configuration = kieServices.newKieBaseConfiguration();
    configuration.setOption(SequentialOption.YES);

    KieContainer container = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
    KieBase knowledgeBase = container.newKieBase(configuration);

    session = knowledgeBase.newStatelessKieSession();
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
    KieCommands kieCommands = kieServices.getCommands();
    List<Command<?>> commands = new ArrayList<Command<?>>();
    commands.add(kieCommands.newInsertElements(getFacts(hand)));
    session.execute(kieCommands.newBatchExecution(commands));
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
