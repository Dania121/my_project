package application;

public class Color {
	private int product_code;
	private String Color;

	public Color(int product_code, String color) {
		super();
		this.product_code = product_code;
		Color = color;
	}

	public int getProduct_code() {
		return product_code;
	}

	public void setProduct_code(int product_code) {
		this.product_code = product_code;
	}

	public String getColor() {
		return Color;
	}

	public void setColor(String color) {
		Color = color;
	}

}
