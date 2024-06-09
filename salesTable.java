package application;

public class salesTable {
	private DisplayProducts product_code;
	private DisplayProducts pname;
	private DisplayProducts price;
	private DisplayProducts display_date;
	private Sale sale_number;
	private Sale sale_date;
	private int quantity;
	private int total;
	private Shop shop_name;

	public salesTable(DisplayProducts product_code, DisplayProducts pname, DisplayProducts price,
			DisplayProducts display_date, Sale sale_number, Sale sale_date, int quantity, int total, Shop shop_name) {
		super();
		this.product_code = product_code;
		this.pname = pname;
		this.price = price;
		this.display_date = display_date;
		this.sale_number = sale_number;
		this.sale_date = sale_date;
		this.quantity = quantity;
		this.total = total;
		this.shop_name = shop_name;
	}

	public DisplayProducts getProduct_code() {
		return product_code;
	}

	public void setProduct_code(DisplayProducts product_code) {
		this.product_code = product_code;
	}

	public DisplayProducts getPname() {
		return pname;
	}

	public void setPname(DisplayProducts pname) {
		this.pname = pname;
	}

	public DisplayProducts getPrice() {
		return price;
	}

	public void setPrice(DisplayProducts price) {
		this.price = price;
	}

	public DisplayProducts getDisplay_date() {
		return display_date;
	}

	public void setDisplay_date(DisplayProducts display_date) {
		this.display_date = display_date;
	}

	public Sale getSale_number() {
		return sale_number;
	}

	public void setSale_number(Sale sale_number) {
		this.sale_number = sale_number;
	}

	public Sale getSale_date() {
		return sale_date;
	}

	public void setSale_date(Sale sale_date) {
		this.sale_date = sale_date;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Shop getShop_name() {
		return shop_name;
	}

	public void setShop_name(Shop shop_name) {
		this.shop_name = shop_name;
	}

}
