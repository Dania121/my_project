package application;

public class Sold {
	private int product_code;
	private String pname;

	public Sold(int product_code, String pname) {
		super();
		this.product_code = product_code;
		this.pname = pname;
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

	

}
