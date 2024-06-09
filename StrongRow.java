package application;

public class StrongRow {
	private int product_code;
	private String pname;
	private int quantity;
	private double profit;

	

	public StrongRow(int product_code, String pname, int quantity, double profit) {
		super();
		this.product_code = product_code;
		this.pname = pname;
		this.quantity = quantity;
		this.profit = profit;
	}

	public int getProduct_code() {
		return product_code;
	}

	public void setProduct_code(int product_code) {
		this.product_code = product_code;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getProfit() {
		return profit;
	}

	public void setProfit(double profit) {
		this.profit = profit;
	}
}