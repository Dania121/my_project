package application;

import java.sql.Date;

public class DisplayProducts extends Product {

	private Date display_date;
	private String shop_name;
	private int display_quantity;

	public DisplayProducts(int product_code, String pname, int price, double wholesale_price,
			String product_company_name, int quantity, int c_id, Date display_date, String shop_name,
			int display_quantity) {
		super(product_code, pname, price, wholesale_price, product_company_name, quantity, c_id);
		this.display_date = display_date;
		this.shop_name = shop_name;
		this.display_quantity = display_quantity;
		// TODO Auto-generated constructor stub
	}

	public Date getDisplay_date() {
		return display_date;
	}

	public void setDisplay_date(Date display_date) {
		this.display_date = display_date;
	}

	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public int getDisplay_quantity() {
		return display_quantity;
	}

	public void setDisplay_quantity(int display_quantity) {
		this.display_quantity = display_quantity;
	}

}
