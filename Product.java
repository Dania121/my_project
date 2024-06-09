package application;

public class Product {
	private int product_code;
	private String pname;
	private int price;
	private double wholesale_price;
	private String product_company_name;
	private int quantity;
	private int c_id;

	public Product(int product_code, String pname, int price, double wholesale_price, String product_company_name,
			int quantity, int c_id) {
		super();
		this.product_code = product_code;
		this.pname = pname;
		this.price = price;
		this.wholesale_price = wholesale_price;
		this.product_company_name = product_company_name;
		this.quantity = quantity;
		this.c_id = c_id;
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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public double getWholesale_price() {
		return wholesale_price;
	}

	public void setWholesale_price(double wholesale_price) {
		this.wholesale_price = wholesale_price;
	}

	public String getProduct_company_name() {
		return product_company_name;
	}

	public void setProduct_company_name(String product_company_name) {
		this.product_company_name = product_company_name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getC_id() {
		return c_id;
	}

	public void setC_id(int c_id) {
		this.c_id = c_id;
	}

}
