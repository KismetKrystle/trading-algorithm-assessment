package codingblackfemales.gettingstarted;

import codingblackfemales.action.Action;
import codingblackfemales.action.NoAction;
import codingblackfemales.algo.AlgoLogic;
import codingblackfemales.sotw.SimpleAlgoState;
import codingblackfemales.sotw.marketdata.AskLevel;
import codingblackfemales.sotw.marketdata.BidLevel;
import codingblackfemales.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyAlgoLogic implements AlgoLogic {

    private static final Logger logger = LoggerFactory.getLogger(MyAlgoLogic.class);

    @Override
    public Action evaluate(SimpleAlgoState state) {

        var orderBookAsString = Util.orderBookToString(state);

        logger.info("[MYALGO] The state of the order book is:\n" + orderBookAsString);

         // Fetch the best Ask and Bid levels
        final AskLevel bestAsk = state.getAskAt(0);  // Best ask price (to buy)
        final BidLevel bestBid = state.getBidAt(0);  // Best bid price (to sell)

        // Log the best ask and bid prices
        logger.info("[MYALGO] Best ask price: " + bestAsk.price + " for quantity: " + bestAsk.quantity);
        logger.info("[MYALGO] Best bid price: " + bestBid.price + " for quantity: " + bestBid.quantity);

        // Define a threshold to determine if a price is "cheap" or "expensive"
        double threshold = 0.05; // 5% price difference threshold
        double historicalPrice = state.getHistoricalPrice(); // Assuming this exists

        // Check for a buy opportunity (current price is significantly lower than the historical price)
        if (currentPrice < historicalPrice * (1 - threshold)) {
          logger.info("[MYALGO] Buying shares, current price: " + currentPrice);
          return createBuyOrder(state);
        }

        // Check for a sell opportunity (current price is significantly higher than the historical price)
        if (currentPrice > historicalPrice * (1 + threshold)) {
          logger.info("[MYALGO] Selling shares, current price: " + currentPrice);
          return createSellOrder(state);
        }

        // If no significant price movement, do nothing
            logger.info("[MYALGO] No significant price movement, taking no action.");
            return NoAction.NoAction;
    }

    // Example method for placing a buy order
    private Action createBuyOrder(SimpleAlgoState state) {
        // Implement order creation logic here
        return new BuyOrderAction(state.getInstrument(), state.getQuantity());
}

    // Example method for placing a sell order
    private Action createSellOrder(SimpleAlgoState state) {
        // Implement order creation logic here
        return new SellOrderAction(state.getInstrument(), state.getQuantity());
}
        /********
         *
         * Add your logic here....
         *
         */

        return NoAction.NoAction;
    }
}
