package application;

public class Shop {
	private String shop_name;
	private String address;

	public Shop(String shop_name, String address) {
		super();
		this.shop_name = shop_name;
		this.address = address;
	}

	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
