package application;

import java.util.Date;

public class StockedProducts extends Product {

	private Date stocked_date;
	private int stocked_quantity;
	private String store_name;

	public StockedProducts(int product_code, String pname, int price, double wholesale_price,
			String product_company_name, int quantity, int c_id, Date stocked_date, int stocked_quantity,
			String store_name) {
		super(product_code, pname, price, wholesale_price, product_company_name, quantity, c_id);
		// TODO Auto-generated constructor stub
		this.stocked_date = stocked_date;
		this.stocked_quantity = stocked_quantity;
		this.store_name = store_name;

	}

	public Date getStocked_date() {
		return stocked_date;
	}

	public void setStocked_date(Date stocked_date) {
		this.stocked_date = stocked_date;
	}

	public int getStocked_quantity() {
		return stocked_quantity;
	}

	public void setStocked_quantity(int stocked_quantity) {
		this.stocked_quantity = stocked_quantity;
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

}
