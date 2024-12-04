package codingblackfemales.gettingstarted;

import codingblackfemales.algo.AlgoLogic;
import codingblackfemales.sotw.SimpleAlgoState;
import codingblackfemales.sotw.marketdata.AskLevel;
import codingblackfemales.sotw.marketdata.BidLevel;
import messages.order.Side;

import org.agrona.concurrent.UnsafeBuffer;

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

    protected UnsafeBuffer createTick2() {

        final MessageHeaderEncoder headerEncoder = new MessageHeaderEncoder();
        final BookUpdateEncoder encoder = new BookUpdateEncoder();
    
        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        final UnsafeBuffer directBuffer = new UnsafeBuffer(byteBuffer);
    
        // Write the encoded output to the direct buffer
        encoder.wrapAndApplyHeader(directBuffer, 0, headerEncoder);
    
        // Set the fields to desired values
        encoder.venue(Venue.XLON);
        encoder.instrumentId(123L);
    
        encoder.askBookCount(3)
                .next().price(120L).size(101L) // Higher ask prices
                .next().price(125L).size(200L)
                .next().price(130L).size(500L);
    
        encoder.bidBookCount(3)
                .next().price(90L).size(100L)  // Lower bid prices
                .next().price(85L).size(200L)
                .next().price(80L).size(300L);
    
        encoder.instrumentStatus(InstrumentStatus.CONTINUOUS);
        encoder.source(Source.STREAM);
    
        return directBuffer;
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
        assertEquals(1, order.getQuantity());
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