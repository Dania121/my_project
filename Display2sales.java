package application;

import java.util.Date;

public class Display2sales {
	private int sale_number;
	private int product_code;
	private int quantity;
	// private int total;
	private String pname;
	private int price;
	private Date display_date;
	private Date sale_date;
	private String shop_name;
	private DisplayProducts displayProduct;

	public Display2sales(int sale_number, int product_code, int quantity, String pname, int price, Date display_date,
			Date sale_date, String shop_name) {
		super();
		this.sale_number = sale_number;
		this.product_code = product_code;
		this.quantity = quantity;
		// this.total = total;
		this.pname = pname;
		this.price = price;
		this.display_date = display_date;
		this.sale_date = sale_date;
		this.shop_name = shop_name;
	}

	public int getSale_number() {
		return sale_number;
	}

	public void setSale_number(int sale_number) {
		this.sale_number = sale_number;
	}

	public int getProduct_code() {
		return product_code;
	}

	public void setProduct_code(int product_code) {
		this.product_code = product_code;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

//	public int getTotal() {
//		return total;
//	}
//
//	public void setTotal(int total) {
//		this.total = total;
//	}

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

	public Date getDisplay_date() {
		return display_date;
	}

	public void setDisplay_date(Date display_date) {
		this.display_date = display_date;
	}

	public Date getSale_date() {
		return sale_date;
	}

	public void setSale_date(Date sale_date) {
		this.sale_date = sale_date;
	}

	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public DisplayProducts getDisplayProduct() {
		return displayProduct;
	}

	public void setDisplayProduct(DisplayProducts displayProduct) {
		this.displayProduct = displayProduct;
	}

}
