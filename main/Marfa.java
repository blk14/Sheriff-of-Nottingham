package main;

public class Marfa {
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int TEN = 10;
    public static final int ILEVEN = 11;
    public static final int TWELVE = 12;
    private String asset;
    private boolean type;   // 0 - ilegal, 1 - legal
    private int id;
    private int profit;
    private int penalty;

    // La constructor initializez toate campurile in
    // functie de id
    public Marfa(final int id) {
        this.id = id;
		if (id == ZERO) {
			this.asset = "Apple";
			this.type = true;
			this.profit = 2;
			this.penalty = 2;
		}
		if (id == ONE) {
			this.asset = "Chesse";
			this.type = true;
			this.profit = THREE;
			this.penalty = 2;
		}
		if (id == TWO) {
			this.asset = "Bread";
			this.type = true;
			this.profit = 2 * TWO;
			this.penalty = 2;
		}
		if (id == THREE) {
			this.asset = "Chicken";
			this.type = true;
			this.profit = 2 * TWO;
			this.penalty = 2;
		}
		if (id == TEN) {
			this.asset = "Silk";
			this.type = false;
			this.profit = THREE * THREE;
			this.penalty = 2 * TWO;
		}
		if (id == ILEVEN) {
			this.asset = "Pepper";
			this.type = false;
			this.profit = THREE * TWO + TWO;
			this.penalty = 2 * TWO;
		}
		if (id == TWELVE) {
			this.asset = "Barrel";
			this.type = false;
			this.profit = 2 * THREE + 1;
			this.penalty = 2 * TWO;
		}

	}
    public final int getId() {
		return this.id;
	}
    public final int getProfit() {
		return this.profit;
	}
    public final int getPenalty() {
		return this.penalty;
	}
    public final boolean getType() {
		return this.type;
	}
}
