package codingblackfemales.gettingstarted;

import codingblackfemales.algo.AlgoLogic;
import codingblackfemales.sotw.ChildOrder;
import codingblackfemales.sotw.SimpleAlgoState;
import codingblackfemales.sotw.marketdata.AskLevel;
import codingblackfemales.sotw.marketdata.BidLevel;
import messages.order.Side;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * This test is designed to check your algo behavior in isolation from the order book.
 * 
 * You can tick in market data messages by creating new versions of createTick() 
 * and testing your algo's responses to the simulated data.
 */
public class MyAlgoTest extends AbstractAlgoTest {

    @Override
    public AlgoLogic createAlgoLogic() {
        // Add your algo logic to the test environment
        return new MyAlgoLogic();
    }

    /**
     * Test basic algo behavior with a single tick of market data.
     */
    @Test
    public void testSingleTickBehavior() throws Exception {
        // Create a sample market data tick
        SimpleAlgoState tick = createTick(100.0, 95.0, 10, 15);

        // Send the tick to the algo
        send(tick);
        
                // Validate behavior: no orders should be created for a neutral tick
                assertEquals("No child orders should be created initially.", 
                             container.getState().getChildOrders().size(), 0);
            }
        
            private void send(SimpleAlgoState tick) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'send'");
            }
        
            /**
     * Test algo response to significant price movements.
     */
    @Test
    public void testAlgoResponseToPriceMovements() throws Exception {
        // Create market data with a low ask price, triggering a buy order
        SimpleAlgoState tick = createTick(85.0, 95.0, 10, 15);
        send(tick);

        // Validate that a buy order is created
        List<ChildOrder> childOrders = container.getState().getChildOrders();
        assertEquals("One child order should be created for a buy.", 1, childOrders.size());
        assertEquals("The created order should be a BUY order.", Side.BUY, childOrders.get(0).getSide());

        // Create market data with a high bid price, triggering a sell order
        tick = createTick(100.0, 115.0, 10, 15);
        send(tick);

        // Validate that a sell order is created
        childOrders = container.getState().getChildOrders();
        assertEquals("Two child orders should now exist.", 2, childOrders.size());
        assertEquals("The second order should be a SELL order.", Side.SELL, childOrders.get(1).getSide());
    }

    /**
     * Helper method to simulate a tick of market data.
     */
    private SimpleAlgoState createTick(double askPrice, double bidPrice, long askQuantity, long bidQuantity) {
        return new SimpleAlgoState() {
            private final List<AskLevel> askLevels = List.of(new AskLevel(askPrice, askQuantity));
            private final List<BidLevel> bidLevels = List.of(new BidLevel(bidPrice, bidQuantity));
            private final List<ChildOrder> childOrders = new ArrayList<>();

            @Override
            public String getSymbol() {
                return "TEST_SYMBOL";
            }

            @Override
            public int getBidLevels() {
                return bidLevels.size();
            }

            @Override
            public int getAskLevels() {
                return askLevels.size();
            }

            @Override
            public BidLevel getBidAt(int index) {
                return bidLevels.get(index);
            }

            @Override
            public AskLevel getAskAt(int index) {
                return askLevels.get(index);
            }

            @Override
            public List<ChildOrder> getChildOrders() {
                return childOrders;
            }

            @Override
            public List<ChildOrder> getActiveChildOrders() {
                return childOrders;
            }

            @Override
            public long getInstrumentId() {
                return 12345L;
            }

            @Override
            public long getHighestBid() {
                return bidLevels.stream().mapToLong(BidLevel::getPrice).max().orElse(0L);
            }
        };
    }
}
