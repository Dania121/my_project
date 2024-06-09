package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class DisplayController implements Initializable {

	@FXML
	private DatePicker datePicker;

	@FXML
	private TableColumn<DisplayProducts, Integer> displayCategoryCoulmn;

	@FXML
	private TableColumn<DisplayProducts, Integer> displayCodeCoulmn;

	@FXML
	private TableColumn<DisplayProducts, String> displayCompanyCoulmn;

	@FXML
	private TableColumn<DisplayProducts, Date> displayDateCoulmn;

	@FXML
	private Button displayDeleteBtn;

	@FXML
	private Button displayInsertBtn;

	@FXML
	private Label displayLbl;

	@FXML
	private TableColumn<DisplayProducts, String> displayNameCoulmn;

	@FXML
	private AnchorPane displayPane;

	@FXML
	private TableColumn<DisplayProducts, Integer> displayPriceCoulmn;

	@FXML
	private TableColumn<DisplayProducts, Integer> displayQuantityCoulmn;

	@FXML
	private TableColumn<DisplayProducts, String> displayShopCoulmn;

	@FXML
	private TableView<DisplayProducts> displayTv;

	@FXML
	private Button displayUpdateBtn;

	@FXML
	private TableColumn<DisplayProducts, Double> displayWholeCoulmn;

	@FXML
	private Label label1;

	@FXML
	private Label label2;

	@FXML
	private Label label3;

	@FXML
	private Button productsBtn;

	@FXML
	private ComboBox<Product> productsCmb;

	@FXML
	private ComboBox<Shop> shopsCmb;
	@FXML
	private TableColumn<DisplayProducts, Integer> displaQuantityCoulmn;
	@FXML
	private Label ll;
	@FXML
	private TextField quantityTxt;

	@FXML
	void addProduct(ActionEvent event) {

	}

	@FXML
	void chooseProduct(ActionEvent event) {

	}

	@FXML
	void chooseShop(ActionEvent event) {

	}

	@FXML
	void delete(ActionEvent event) {

	}

	@FXML
	void insert(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		if (quantityTxt.getText().isEmpty() || productsCmb.getSelectionModel().isEmpty()
				|| shopsCmb.getSelectionModel().isEmpty() || datePicker.getValue() == null) {
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Please fill all the fields and select from all the comboboxes");
			alert.showAndWait();
			return; // Exit the method early if any text field is empty
		}

		try (Connection connection = DBconnetion.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"INSERT INTO DisplayProduct (product_code, display_date, display_quantity, shop_name) VALUES (?, ?, ?, ?)")) {
			statement.setInt(1, productsCmb.getSelectionModel().getSelectedItem().getProduct_code());
			statement.setDate(2, Date.valueOf(datePicker.getValue()));
			statement.setInt(3, Integer.parseInt(quantityTxt.getText()));
			statement.setString(4, shopsCmb.getSelectionModel().getSelectedItem().getShop_name());

			statement.executeUpdate();
			showProduct();

			alert.setTitle("Done");
			alert.setHeaderText(null);
			alert.setContentText("The product displayed successfully");
			alert.showAndWait();
		} catch (SQLException e) {
			if (e.getMessage().contains("Duplicate entry")) {
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("The entered product code already exists");
				alert.showAndWait();
			} else {
				e.printStackTrace();
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("An error occurred while inserting the category");
				alert.showAndWait();
			}
		}
	}

	@FXML
	void update(ActionEvent event) {

	}

	public ObservableList<Product> getProduct(String query) throws SQLException {
		ObservableList<Product> productList = FXCollections.observableArrayList();
		Connection conn = DBconnetion.getConnection();
		java.sql.Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(query);
			Product product;
			if (!rs.isBeforeFirst()) {
				System.out.println(" No records found for the query :" + query);
				return null;

			} else {
				while (rs.next()) {
					product = new Product(rs.getInt("product_code"), rs.getString("pname"), rs.getInt("price"),
							rs.getDouble("wholesale_price"), rs.getString("product_company_name"),
							rs.getInt("quantity"), rs.getInt("c_id"));
					productList.add(product);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (st != null)
					st.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return productList;
	}

	private void populateComboBox() throws SQLException {
		String query = "SELECT * FROM project.product";
		List<Product> products = getProduct(query);
		ObservableList<Product> productList = FXCollections.observableArrayList(products);

		// Set the cell factory to display both code and name
		productsCmb.setCellFactory(param -> new ListCell<Product>() {
			@Override
			protected void updateItem(Product item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					setText(item.getProduct_code() + " - " + item.getPname() + "-" + item.getWholesale_price() + "-"
							+ item.getPrice() + "-" + item.getProduct_company_name() + "-" + item.getQuantity() + "-"
							+ item.getC_id());
				}
			}
		});
		productsCmb.setItems(productList);
	}

	public void showProduct() throws SQLException {
		String query = "SELECT dp.*, p.* " + "FROM project.DisplayProduct dp "
				+ "INNER JOIN project.Product p ON dp.product_code = p.product_code";
		ObservableList<DisplayProducts> productList = getDisplay(query);
		displayCodeCoulmn.setCellValueFactory(new PropertyValueFactory<>("product_code"));
		displayNameCoulmn.setCellValueFactory(new PropertyValueFactory<>("pname"));
		displayWholeCoulmn.setCellValueFactory(new PropertyValueFactory<>("wholesale_price"));
		displayPriceCoulmn.setCellValueFactory(new PropertyValueFactory<>("price"));
		displayCompanyCoulmn.setCellValueFactory(new PropertyValueFactory<>("product_company_name"));
		displayQuantityCoulmn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		displayCategoryCoulmn.setCellValueFactory(new PropertyValueFactory<>("c_id"));
		displayDateCoulmn.setCellValueFactory(new PropertyValueFactory<>("display_date"));
		displayShopCoulmn.setCellValueFactory(new PropertyValueFactory<>("shop_name"));
		displaQuantityCoulmn.setCellValueFactory(new PropertyValueFactory<>("display_quantity"));
		displayTv.setItems(productList);
	}

	public ObservableList<Shop> getShop(String query) throws SQLException {
		ObservableList<Shop> shopList = FXCollections.observableArrayList();
		Connection conn = DBconnetion.getConnection();
		java.sql.Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(query);
			Shop shop;
			if (!rs.isBeforeFirst()) {
				System.out.println(" No records found for the query :" + query);
				return null;

			} else {
				while (rs.next()) {
					shop = new Shop(rs.getString("shop_name"), rs.getString("address"));
					shopList.add(shop);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (st != null)
					st.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return shopList;
	}

	private void populateComboBoxShop() throws SQLException {
		String query = "SELECT * FROM project.shop";
		List<Shop> shops = getShop(query);
		ObservableList<Shop> shopsList = FXCollections.observableArrayList(shops);

		// Set the cell factory to display both code and name
		shopsCmb.setCellFactory(param -> new ListCell<Shop>() {
			@Override
			protected void updateItem(Shop item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					setText(item.getShop_name() + "-" + item.getAddress());
				}
			}
		});
		shopsCmb.setItems(shopsList);
	}

	public ObservableList<DisplayProducts> getDisplay(String query) throws SQLException {
		ObservableList<DisplayProducts> productList = FXCollections.observableArrayList();
		Connection conn = DBconnetion.getConnection();
		java.sql.Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(query);
			DisplayProducts product;
			if (!rs.isBeforeFirst()) {
				System.out.println(" No records found for the query :" + query);
				return null;

			} else {
				while (rs.next()) {
					product = new DisplayProducts(rs.getInt("product_code"), rs.getString("pname"), rs.getInt("price"),
							rs.getDouble("wholesale_price"), rs.getString("product_company_name"),
							rs.getInt("quantity"), rs.getInt("c_id"), rs.getDate("display_date"),
							rs.getString("shop_name"), rs.getInt("display_quantity"));
					productList.add(product);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (st != null)
					st.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return productList;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			showProduct();
			populateComboBox();
			populateComboBoxShop();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
