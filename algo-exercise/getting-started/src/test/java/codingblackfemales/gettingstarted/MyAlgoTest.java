package codingblackfemales.gettingstarted;

import codingblackfemales.algo.AlgoLogic;
import codingblackfemales.sotw.SimpleAlgoState;
import codingblackfemales.sotw.marketdata.AskLevel;
import codingblackfemales.sotw.marketdata.BidLevel;
import messages.order.Side;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test is designed to check your algo behavior in isolation of the order book.
 *
 * You can tick in market data messages by creating new versions of createTick() (ex. createTick2, createTickMore etc..)
 *
 * You should then add behaviour to your algo to respond to that market data by creating or cancelling child orders.
 *
 * When you are comfortable you algo does what you expect, then you can move on to creating the MyAlgoBackTest.
 *
 */
public class MyAlgoTest extends AbstractAlgoTest {

    @Override
    public AlgoLogic createAlgoLogic() {
        //this adds your algo logic to the container classes
        return new MyAlgoLogic();
    }


    @Test
    public void testDispatchThroughSequencer() throws Exception {

        //create a sample market data tick....
        send(createTick());

        //simple assert to check we had 3 orders created
        //assertEquals(container.getState().getChildOrders().size(), 3);
    
           // Check that a buy order was created
        var childOrders = container.getState().getChildOrders();
        assertEquals(1, childOrders.size()); // Expect 1 order to be created

        var order = childOrders.get(0);
        assertEquals(Side.BUY, order.getSide());
        assertEquals(100L, order.getPrice());
        assertEquals(1, order.getSize());
    }

    @Test
    public void testNoActionWhenThresholdNotMet() throws Exception {
        // Simulate a market data tick with a high highest bid
        send(createTick2());

        // Check that no orders were created
        var childOrders = container.getState().getChildOrders();
        assertTrue(childOrders.isEmpty());
    }
}
