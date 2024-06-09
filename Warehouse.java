package application;

public class Warehouse {
	private String store_name;
	private String address;

	public Warehouse(String store_name, String address) {
		super();
		this.store_name = store_name;
		this.address = address;
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
