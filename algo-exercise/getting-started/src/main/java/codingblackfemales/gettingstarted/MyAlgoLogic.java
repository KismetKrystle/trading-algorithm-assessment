package codingblackfemales.gettingstarted;

import codingblackfemales.action.Action;
import codingblackfemales.action.NoAction;
import codingblackfemales.algo.AlgoLogic;
import codingblackfemales.sotw.SimpleAlgoState;
import codingblackfemales.util.Util;
import messages.order.Side;
import codingblackfemales.action.CreateChildOrder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyAlgoLogic implements AlgoLogic {

    private static final Logger logger = LoggerFactory.getLogger(MyAlgoLogic.class);
    private static final long BUY_THRESHOLD = 95L; // Example threshold for creating a buy order

    @Override
    public Action evaluate(SimpleAlgoState state) {

        var orderBookAsString = Util.orderBookToString(state);

        logger.info("[MYALGO] The state of the order book is:\n" + orderBookAsString);

         // Check the highest bid in the order book
        long highestBid = state.getHighestBid();

        if (highestBid < BUY_THRESHOLD) {
            logger.info("[MYALGO] Creating a buy order as highest bid is below the threshold.");
            return new CreateChildOrder(Side.BUY, 100L, 1); // Create a buy order at price 100 with size 1
        }

        logger.info("[MYALGO] No action taken.");
        return NoAction.NoAction;
    }
}