package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.print.DocFlavor.INPUT_STREAM;

import com.mysql.cj.xdevapi.Statement;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
import javafx.stage.Stage;

public class SalesController implements Initializable {
	@FXML
	private Button back;

	@FXML
	private TableColumn<Display2sales, Integer> code1Coulmn;

	@FXML
	private DatePicker datePicker;

	@FXML
	private Button deleteBtn;

	@FXML
	private TableColumn<Display2sales, Date> displayDateCoulmn;

	@FXML
	private Button insertBtn;

	@FXML
	private Label label;

	@FXML
	private TableColumn<Display2sales, String> name1Coulmn;

	@FXML
	private TableColumn<Display2sales, Integer> priceCoulmn;

	@FXML
	private ComboBox<DisplayProducts> productCmb;

	@FXML
	private Label productLbl;

	@FXML
	private TableColumn<StrongRow, Double> profitCoulmn;

	@FXML
	private TableColumn<Display2sales, Integer> quantity1Coulmn;

	@FXML
	private Label quantityLbl;

	@FXML
	private TextField quantityTxt;

	@FXML
	private TableColumn<Display2sales, Date> saleDateCoulmn;

	@FXML
	private Label saleDateLbl;

	@FXML
	private TableColumn<Display2sales, Integer> saleIDCoulmn;

	@FXML
	private Label saleNumberLbl;

	@FXML
	private TextField saleNumberTxt;

	@FXML
	private Label salesLbl;

	@FXML
	private AnchorPane salesPane;

	@FXML
	private TableView<Display2sales> sales_tv;

	@FXML
	private ComboBox<Shop> shopCmb;

	@FXML
	private Label shopLbl;

	@FXML
	private TableColumn<Display2sales, String> shopNameCoulmn;

	@FXML
	private Button show;

	@FXML
	private Label slodoutLbl;

	@FXML
	private TableColumn<Sold, Integer> soldCodeCoulmn;

	@FXML
	private TableColumn<Sold, String> soldNameCoulmn;

	@FXML
	private TableView<Sold> soldOut_tv;

	@FXML
	private TableColumn<StrongRow, Integer> strongCodeCoulmn;

	@FXML
	private TableColumn<StrongRow, String> strongNameCoulmn;

	@FXML
	private TableColumn<StrongRow, Integer> strongQuantityCoulmn;

	@FXML
	private Label strongestLbl;

	@FXML
	private TableView<StrongRow> strongest_tv;

	@FXML
	private TableColumn<Display2sales, Integer> totalCoulmn;

	@FXML
	private Button updateBtn;

	@FXML
	void back(ActionEvent event) {

		try {
			// Load the FXML file
			FXMLLoader loader = new FXMLLoader(getClass().getResource("window.fxml"));
			Parent root = loader.load();

			// Create a new scene with the loaded FXML
			Scene scene = new Scene(root);

			// Get the stage from the event source
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

			// Set the scene to the stage
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	void delete(ActionEvent event) {
		// Check if a sale is selected
		Display2sales selectedSale = sales_tv.getSelectionModel().getSelectedItem();
		if (selectedSale == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Please select a sale to delete.");
			alert.showAndWait();
			return;
		}

		Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
		confirmationAlert.setTitle("Confirm Deletion");
		confirmationAlert.setHeaderText(null);
		confirmationAlert.setContentText("Are you sure you want to delete this sale?");
		Optional<ButtonType> result = confirmationAlert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try (Connection connection = DBconnetion.getConnection()) {
				// Start transaction
				connection.setAutoCommit(false);

				// Delete the sale from the display2sales table
				String deleteDisplaySalesQuery = "DELETE FROM display2sales WHERE sale_number = ?";
				try (PreparedStatement deleteDisplaySalesStatement = connection
						.prepareStatement(deleteDisplaySalesQuery)) {
					deleteDisplaySalesStatement.setInt(1, selectedSale.getSale_number());
					deleteDisplaySalesStatement.executeUpdate();
				}

				// Delete the sale from the sales table
				String deleteSalesQuery = "DELETE FROM sales WHERE sale_number = ?";
				try (PreparedStatement deleteSalesStatement = connection.prepareStatement(deleteSalesQuery)) {
					deleteSalesStatement.setInt(1, selectedSale.getSale_number());
					deleteSalesStatement.executeUpdate();
				}

				// Commit transaction
				connection.commit();

				// Show success message
				Alert successAlert = new Alert(AlertType.INFORMATION);
				successAlert.setTitle("Success");
				successAlert.setHeaderText(null);
				successAlert.setContentText("Sale deleted successfully.");
				successAlert.showAndWait();

				// Refresh the sales table view
				showSales();
			} catch (SQLException e) {
				// Handle exceptions
				e.printStackTrace();
				Alert errorAlert = new Alert(AlertType.ERROR);
				errorAlert.setTitle("Error");
				errorAlert.setHeaderText(null);
				errorAlert.setContentText("An error occurred while deleting the sale.");
				errorAlert.showAndWait();
			}
		}
	}

	@FXML
	void getProducts(ActionEvent event) {

	}

	@FXML
	void getShop(ActionEvent event) {

	}

	@FXML
	void insert(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		if (productCmb.getSelectionModel().isEmpty() || shopCmb.getSelectionModel().isEmpty()
				|| quantityTxt.getText().isEmpty() || datePicker.getValue() == null) {
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Please fill all the fields and select the product and shop name");
			alert.showAndWait();
			return; // Exit the method early if any text field is empty
		}

		try (Connection connection = DBconnetion.getConnection()) {
			// Start transaction
			connection.setAutoCommit(false);

			// Insert into sales table with auto-incremented sale_number
			try (PreparedStatement salesStatement = connection.prepareStatement(
					"INSERT INTO sales (sale_date) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
				LocalDate selectedDate = datePicker.getValue();
				java.sql.Date sqlDate = null;
				if (selectedDate != null) {
					sqlDate = java.sql.Date.valueOf(selectedDate);
				}
				salesStatement.setDate(1, sqlDate);
				salesStatement.executeUpdate();

				// Get the generated sale_number
				try (ResultSet generatedKeys = salesStatement.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						int saleNumber = generatedKeys.getInt(1);

						// Insert into display2sales
						try (PreparedStatement displaySalesStatement = connection
								.prepareStatement("INSERT INTO display2sales VALUES (?, ?, ?)")) {
							displaySalesStatement.setInt(1, saleNumber);
							// Assuming you have a method to get the selected product code
							int productCode = productCmb.getSelectionModel().getSelectedItem().getProduct_code();
							displaySalesStatement.setInt(2, productCode);
							displaySalesStatement.setInt(3, Integer.parseInt(quantityTxt.getText()));
							displaySalesStatement.executeUpdate();
						}
					}
				}

				// Commit transaction
				connection.commit();

				// Show success message
				alert.setTitle("Done");
				alert.setHeaderText(null);
				alert.setContentText("The product inserted successfully");
				alert.showAndWait();

				// Refresh the sales table view
				showSales();
			}
		} catch (SQLException e) {
			// Handle exceptions
			e.printStackTrace();
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("An error occurred while inserting the product");
			alert.showAndWait();
		}
	}

	@FXML
	void show(ActionEvent event) {

		try {
			// Load the FXML file
			FXMLLoader loader = new FXMLLoader(getClass().getResource("displayProducts.fxml"));
			Parent root = loader.load();

			// Create a new scene with the loaded FXML
			Scene scene = new Scene(root);

			// Get the stage from the event source
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

			// Set the scene to the stage
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	void update(ActionEvent event) {
		// Check if a sale is selected
		Display2sales selectedSale = sales_tv.getSelectionModel().getSelectedItem();
		if (selectedSale == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Please select a sale to update.");
			alert.showAndWait();
			return;
		}

		// Validate input fields
		if (productCmb.getSelectionModel().isEmpty() || shopCmb.getSelectionModel().isEmpty()
				|| quantityTxt.getText().isEmpty() || datePicker.getValue() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Please fill all the fields and select the product, shop name, and sale date.");
			alert.showAndWait();
			return;
		}

		try (Connection connection = DBconnetion.getConnection()) {
			// Start transaction
			connection.setAutoCommit(false);

			// Update the sale in the display2sales table
			String updateQuery = "UPDATE display2sales SET product_code = ?, quantity = ? WHERE sale_number = ?";
			try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
				updateStatement.setInt(1, productCmb.getSelectionModel().getSelectedItem().getProduct_code());
				updateStatement.setInt(2, Integer.parseInt(quantityTxt.getText()));
				updateStatement.setInt(3, selectedSale.getSale_number());
				updateStatement.executeUpdate();
			}

			// Update the sale date in the sales table
			String updateSalesQuery = "UPDATE sales SET sale_date = ? WHERE sale_number = ?";
			try (PreparedStatement updateSalesStatement = connection.prepareStatement(updateSalesQuery)) {
				updateSalesStatement.setDate(1, java.sql.Date.valueOf(datePicker.getValue()));
				updateSalesStatement.setInt(2, selectedSale.getSale_number());
				updateSalesStatement.executeUpdate();
			}

			// Commit transaction
			connection.commit();

			// Show success message
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Success");
			alert.setHeaderText(null);
			alert.setContentText("Sale details updated successfully.");
			alert.showAndWait();

			// Refresh the sales table view
			showSales();
		} catch (SQLException e) {
			// Handle exceptions
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("An error occurred while updating the sale details.");
			alert.showAndWait();
		}
	}

	public void showSales() throws SQLException {
		String query = "SELECT " + "    p.product_code," + "    p.pname," + "    p.price," + "    dp.display_date,"
				+ "    dp.shop_name," + // Include the shop_name column
				"    d2s.sale_number," + "    s.sale_date," + "    d2s.quantity " + "FROM " + "    Product p " + "JOIN "
				+ "    DisplayProduct dp ON p.product_code = dp.product_code " + "JOIN "
				+ "    Display2Sales d2s ON p.product_code = d2s.product_code " + "JOIN "
				+ "    Sales s ON d2s.sale_number = s.sale_number " + "ORDER BY d2s.sale_number ASC;"; // Sort by sale
																										// number in
																										// ascending
																										// order

		ObservableList<Display2sales> salesList = getSales(query);

		// Group rows with the same sale number together
		List<Display2sales> groupedSalesList = new ArrayList<>();
		int currentSaleNumber = -1;
		Display2sales currentSale = null;
		for (Display2sales sale : salesList) {
			if (sale.getSale_number() != currentSaleNumber) {
				// Add the current sale to the list and update currentSale
				if (currentSale != null) {
					groupedSalesList.add(currentSale);
				}
				currentSale = sale;
				currentSaleNumber = sale.getSale_number();
			} else {
				// Append the product details to the current sale
				currentSale.setPname(currentSale.getPname() + ", " + sale.getPname());
				currentSale.setQuantity(currentSale.getQuantity() + sale.getQuantity());
				currentSale.setPrice(currentSale.getPrice() + sale.getPrice());
			}
		}
		// Add the last sale to the list
		if (currentSale != null) {
			groupedSalesList.add(currentSale);
		}

		ObservableList<Display2sales> sortedSalesList = FXCollections.observableArrayList(groupedSalesList);

		// Assuming sales_tv is your TableView
		code1Coulmn.setCellValueFactory(new PropertyValueFactory<>("product_code"));
		name1Coulmn.setCellValueFactory(new PropertyValueFactory<>("pname"));
		displayDateCoulmn.setCellValueFactory(new PropertyValueFactory<>("display_date"));
		priceCoulmn.setCellValueFactory(new PropertyValueFactory<>("price"));
		shopNameCoulmn.setCellValueFactory(new PropertyValueFactory<>("shop_name"));
		saleIDCoulmn.setCellValueFactory(new PropertyValueFactory<>("sale_number"));
		saleDateCoulmn.setCellValueFactory(new PropertyValueFactory<>("sale_date"));
		quantity1Coulmn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

		// Set the items to the TableView
		sales_tv.setItems(sortedSalesList);

		String selectQuery = "SELECT " + "d2s.product_code, " + "p.pname, " + "SUM(d2s.quantity) AS quantity, "
				+ "SUM((p.price * d2s.quantity) - (p.wholesale_price * d2s.quantity)) AS profit " + "FROM "
				+ "display2sales d2s " + "JOIN " + "Product p ON d2s.product_code = p.product_code " + "GROUP BY "
				+ "d2s.product_code, p.pname " + "ORDER BY " + "quantity DESC;";

		ObservableList<StrongRow> strongList = getStrong(selectQuery);
		strongCodeCoulmn.setCellValueFactory(new PropertyValueFactory<>("product_code"));
		strongNameCoulmn.setCellValueFactory(new PropertyValueFactory<>("pname"));
		strongQuantityCoulmn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

		profitCoulmn.setCellValueFactory(new PropertyValueFactory<>("profit"));
		strongest_tv.setItems(strongList);

		String selectQuery2 = "SELECT p.product_code, p.pname " + "FROM DisplayProduct dp, Product p "
				+ "WHERE p.product_code = dp.product_code " + "ORDER BY dp.display_quantity;";
		ObservableList<Sold> soldList = getSold(selectQuery2);

		soldCodeCoulmn.setCellValueFactory(new PropertyValueFactory<>("product_code"));
		soldNameCoulmn.setCellValueFactory(new PropertyValueFactory<>("pname"));
		soldOut_tv.setItems(soldList);

	}

	public ObservableList<StrongRow> getStrong(String query) throws SQLException {
		ObservableList<StrongRow> strongList = FXCollections.observableArrayList();
		Connection conn = DBconnetion.getConnection();
		java.sql.Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(query);
			StrongRow strong;
			if (!rs.isBeforeFirst()) {
				System.out.println(" No records found for the query :" + query);
				return null;

			} else {
				while (rs.next()) {
					strong = new StrongRow(rs.getInt("product_code"), rs.getString("pname"), rs.getInt("quantity"),
							rs.getDouble("profit"));
					strongList.add(strong);
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
		return strongList;
	}

	public ObservableList<Display2sales> getSales(String query) throws SQLException {
		ObservableList<Display2sales> salesList = FXCollections.observableArrayList();
		Connection conn = DBconnetion.getConnection();
		java.sql.Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(query);
			Display2sales sale;
			if (!rs.isBeforeFirst()) {
				System.out.println(" No records found for the query :" + query);
				return null;

			} else {
				while (rs.next()) {
					sale = new Display2sales(rs.getInt("sale_number"), rs.getInt("product_code"), rs.getInt("quantity"),
							rs.getString("pname"), rs.getInt("price"), rs.getDate("display_date"),
							rs.getDate("sale_date"), rs.getString("shop_name"));
					salesList.add(sale);
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
		return salesList;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

		totalCoulmn.setCellValueFactory(cellData -> {
			Display2sales sale = cellData.getValue();
			int totalPrice = sale.getPrice() * sale.getQuantity();
			return new SimpleIntegerProperty(totalPrice).asObject();
		});

		try {
			name1Coulmn.setPrefWidth(150);
			sales_tv.setPrefWidth(850);
			showSales();
			populateComboBox();
			populateComboBox2();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	private void populateComboBox() throws SQLException {
		String query = "SELECT * FROM project.shop";
		List<Shop> shops = getShop(query); // Adjust this method name to match your implementation
		ObservableList<Shop> shopList = FXCollections.observableArrayList(shops);

		// Set the cell factory to display both code and name
		shopCmb.setCellFactory(param -> new ListCell<Shop>() {
			@Override
			protected void updateItem(Shop item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					setText(item.getShop_name() + " - " + item.getAddress());
				}
			}
		});

		shopCmb.setItems(shopList);
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

	private void populateComboBox2() throws SQLException {
		String query = "SELECT p.*, dp.display_date, dp.display_quantity, dp.shop_name " + "FROM Product p "
				+ "JOIN DisplayProduct dp ON p.product_code = dp.product_code";

		List<DisplayProducts> products = getDisplay(query);
		ObservableList<DisplayProducts> productsList = FXCollections.observableArrayList(products);

		// Set the cell factory to display both code and name
		productCmb.setCellFactory(param -> new ListCell<DisplayProducts>() {
			@Override
			protected void updateItem(DisplayProducts item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					setText(item.getProduct_code() + " - " + item.getPname() + " - " + item.getPrice() + " - "
							+ item.getDisplay_date());
				}
			}
		});

		productCmb.setItems(productsList);
	}

	public ObservableList<Sold> getSold(String query) throws SQLException {
		ObservableList<Sold> soldList = FXCollections.observableArrayList();
		Connection conn = DBconnetion.getConnection();
		java.sql.Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(query);
			Sold sold;
			if (!rs.isBeforeFirst()) {
				System.out.println(" No records found for the query :" + query);
				return null;

			} else {
				while (rs.next()) {
					sold = new Sold(rs.getInt("product_code"), rs.getString("pname"));
					soldList.add(sold);
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
		return soldList;
	}

}
