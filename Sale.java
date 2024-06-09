package application;

import java.util.Date;

public class Sale {
	private int sale_number;
	private Date sale_date;

	public Sale(int sale_number, Date sale_date) {
		super();
		this.sale_number = sale_number;
		this.sale_date = sale_date;
	}

	public int getSale_number() {
		return sale_number;
	}

	public void setSale_number(int sale_number) {
		this.sale_number = sale_number;
	}

	public Date getSale_date() {
		return sale_date;
	}

	public void setSale_date(Date sale_date) {
		this.sale_date = sale_date;
	}

}
