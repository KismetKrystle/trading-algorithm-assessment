package codingblackfemales.gettingstarted;

import codingblackfemales.action.Action;
import codingblackfemales.action.CreateChildOrder;
import codingblackfemales.action.NoAction;
import codingblackfemales.algo.AlgoLogic;
import codingblackfemales.sotw.SimpleAlgoState;
import codingblackfemales.sotw.marketdata.AskLevel;
import codingblackfemales.sotw.marketdata.BidLevel;
import codingblackfemales.util.Util;
import messages.order.Side;

import static codingblackfemales.action.NoAction.NoAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyAlgoLogic implements AlgoLogic {

    private static final Logger logger = LoggerFactory.getLogger(MyAlgoLogic.class);

    private double highPrice = 110.0; //arbitrary high price
    private double lowPrice = 90.0; //arbitrary low price

    @Override
    public Action evaluate(SimpleAlgoState state) {

        var orderBookAsString = Util.orderBookToString(state);

        logger.info("[MYALGO] The state of the order book is:\n" + orderBookAsString);

        // check if there are any Ask and Bid levels available in the book
        if (state.getAskLevels() == 0 || state.getBidLevels() == 0) {
            logger.info("[MYALGO] No Ask or Bid levels available. No action will be taken.");
            return NoAction;
        }

         // get the best Ask and Bid levels
        final AskLevel bestAsk = state.getAskAt(0);  // Best ask price (to buy)
        final BidLevel bestBid = state.getBidAt(0);  // Best bid price (to sell)

        // Log the best ask and bid prices
        logger.info("[MYALGO] Best ask price: " + bestAsk.price + " for quantity: " + bestAsk.quantity);
        logger.info("[MYALGO] Best bid price: " + bestBid.price + " for quantity: " + bestBid.quantity);

        // Update the high and low prices dynamically
        if (bestAsk.price > highPrice) {
            highPrice = bestAsk.price;  // Update the high price if the current ask is higher
            logger.info("[MYALGO] New high price set to: " + highPrice);
        }

        if (bestBid.price < lowPrice) {
            lowPrice = bestBid.price;  // Update the low price if the current bid is lower
            logger.info("[MYALGO] New low price set to: " + lowPrice);
        }

        // threshold for deciding when to buy or sell
        double threshold = 0.05; // 5% price difference threshold

        // Make a buy order if the best ask price is significantly lower than the high price
        if (bestAsk.price < highPrice * (1 - threshold) && state.getChildOrders().size() < 5) {
            return createBuyOrder(bestAsk.quantity, bestAsk.price);
        }

        // Make a sell order if the best bid price is significantly higher than the low price
        if (bestBid.price > lowPrice * (1 + threshold) && state.getChildOrders().size() < 5) {
            return createSellOrder(bestBid.quantity, bestBid.price);
        }

        logger.info("[MYALGO] No significant price movement, taking no action.");
        return NoAction;
    }

    // Private helper method to create a buy order
    private Action createBuyOrder(long quantity, long price) {
        logger.info("[MYALGO] Placing a BUY order for quantity: " + quantity + " at price: " + price);
        return new CreateChildOrder(Side.BUY, quantity, price);
    }

    // Private helper method to create a sell order
    private Action createSellOrder(long quantity, long price) {
        logger.info("[MYALGO] Placing a SELL order for quantity: " + quantity + " at price: " + price);
        return new CreateChildOrder(Side.SELL, quantity, price);
    }
}
