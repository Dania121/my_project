package application;

public class Defect extends Product {
	private String defect_cause;
	private int quantitiy;

	public Defect(int product_code, String pname, int price, double wholesale_price, String product_company_name,
			int quantity, int c_id, String defect_cause, int quantitiy) {
		super(product_code, pname, price, wholesale_price, product_company_name, quantity, c_id);
		// TODO Auto-generated constructor stub
		this.defect_cause = defect_cause;
		this.quantitiy = quantitiy;
	}

	public String getDefect_cause() {
		return defect_cause;
	}

	public void setDefect_cause(String defect_cause) {
		this.defect_cause = defect_cause;
	}

	public int getQuantitiy() {
		return quantitiy;
	}

	public void setQuantitiy(int quantitiy) {
		this.quantitiy = quantitiy;
	}

}
