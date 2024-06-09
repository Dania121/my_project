package application;

public class ProfitRow {
	private int productCode;
	private double profit;

	public ProfitRow(int productCode, double profit) {
		this.productCode = productCode;
		this.profit = profit;
	}

	public int getProductCode() {
		return productCode;
	}

	public void setProductCode(int productCode) {
		this.productCode = productCode;
	}

	public double getProfit() {
		return profit;
	}

	public void setProfit(double profit) {
		this.profit = profit;
	}
}