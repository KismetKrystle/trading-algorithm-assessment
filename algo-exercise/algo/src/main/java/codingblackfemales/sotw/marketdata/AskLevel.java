package codingblackfemales.sotw.marketdata;

public class AskLevel extends AbstractLevel {
    public AskLevel(long askPrice, long askQuantity) {
        this.price = askPrice;
        this.quantity = askQuantity;
    }

    @Override
    public String toString() {
        return "ASK[" + getQuantity() + "@" + getPrice() + "]";
    }
}
