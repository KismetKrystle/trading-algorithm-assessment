package codingblackfemales.sotw.marketdata;

public class BidLevel extends AbstractLevel{

    public BidLevel(double bidPrice, long bidQuantity) {
        //TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return "BID[" + getQuantity() + "@" + getPrice() + "]";
    }

}
