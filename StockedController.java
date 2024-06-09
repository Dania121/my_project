package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

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

public class StockedController implements Initializable {

	@FXML
	private Button DeleteStockedBtn;

	@FXML
	private Label StockedDateLbl;

	@FXML
	private Label StockedLbl;

	@FXML
	private Button UpdateStochedBtn;

	@FXML
	private TableColumn<StockedProducts, Integer> allQuantityCoulmn;

	@FXML
	private Button back;

	@FXML
	private TableColumn<StockedProducts, Integer> categotyCoulmn;

	@FXML
	private TableColumn<StockedProducts, String> companyCoulmn;

	@FXML
	private DatePicker datePicker;

	@FXML
	private Button displayBtn;

	@FXML
	private Button goToWarehouseBtn;

	@FXML
	private Button insertStockedBtn;

	@FXML
	private Label lll;

	@FXML
	private TableColumn<StockedProducts, Integer> priceCoulmn;

	@FXML
	private ComboBox<Product> proCmb;

	@FXML
	private Button productBtn;

	@FXML
	private TableColumn<StockedProducts, Integer> quantityCoulmn;

	@FXML
	private TextField quantityTxt;

	@FXML
	private TableColumn<StockedProducts, Integer> stockedCodeCoulmn;

	@FXML
	private Label stockedCodeLbl;

	@FXML
	private TableColumn<StockedProducts, Date> stockedDateCoulmn;

	@FXML
	private TableColumn<StockedProducts, String> stockedNameCoulmn;

	@FXML
	private AnchorPane stockedProductPane;

	@FXML
	private TableView<StockedProducts> stockedTv;

	@FXML
	private Label wareHouseLbl;

	@FXML
	private ComboBox<Warehouse> warehouseCmb;

	@FXML
	private TableColumn<StockedProducts, String> warehouseCoulmn;

	@FXML
	private TableColumn<StockedProducts, Double> wholeCoulmn;

	@FXML
	void backBtn(ActionEvent event) {

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
		// Retrieve the selected StockedProduct
		StockedProducts selectedProduct = stockedTv.getSelectionModel().getSelectedItem();

		// Check if an item is selected
		if (selectedProduct == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Please select a stocked product to delete.");
			alert.showAndWait();
			return;
		}

		// Prompt the user for confirmation
		Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
		confirmationAlert.setTitle("Confirm Deletion");
		confirmationAlert.setHeaderText(null);
		confirmationAlert.setContentText("Are you sure you want to delete this stocked product?");
		Optional<ButtonType> result = confirmationAlert.showAndWait();

		// If the user confirms the deletion
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try (Connection connection = DBconnetion.getConnection();
					PreparedStatement statement = connection
							.prepareStatement("DELETE FROM StockedProduct WHERE product_code = ?")) {
				statement.setInt(1, selectedProduct.getProduct_code());
				statement.executeUpdate();

				// Remove the deleted item from the TableView
				stockedTv.getItems().remove(selectedProduct);

				// Show success message
				Alert successAlert = new Alert(AlertType.INFORMATION);
				successAlert.setTitle("Success");
				successAlert.setHeaderText(null);
				successAlert.setContentText("Stocked product deleted successfully.");
				successAlert.showAndWait();
			} catch (SQLException e) {
				e.printStackTrace();
				Alert errorAlert = new Alert(AlertType.ERROR);
				errorAlert.setTitle("Error");
				errorAlert.setHeaderText(null);
				errorAlert.setContentText("An error occurred while deleting the stocked product.");
				errorAlert.showAndWait();
			}
		}
	}

	@FXML
	void display(ActionEvent event) {


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
	void goToWarehouse(ActionEvent event) {


		try {
			// Load the FXML file
			FXMLLoader loader = new FXMLLoader(getClass().getResource("warehouse.fxml"));
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
	void insert(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		if (quantityTxt.getText().isEmpty() || proCmb.getSelectionModel().isEmpty()
				|| warehouseCmb.getSelectionModel().isEmpty() || datePicker.getValue() == null) {
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Please fill all the fields and select from all the comboboxes");
			alert.showAndWait();
			return; // Exit the method early if any text field is empty
		}

		try (Connection connection = DBconnetion.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"INSERT INTO StockedProduct (product_code, stocked_date, stocked_quantity, store_name) VALUES (?, ?, ?, ?)")) {
			statement.setInt(1, proCmb.getSelectionModel().getSelectedItem().getProduct_code());
			statement.setDate(2, java.sql.Date.valueOf(datePicker.getValue()));

			statement.setInt(3, Integer.parseInt(quantityTxt.getText()));
			statement.setString(4, warehouseCmb.getSelectionModel().getSelectedItem().getStore_name());

			statement.executeUpdate();
			showStockedProduct();

			alert.setTitle("Done");
			alert.setHeaderText(null);
			alert.setContentText("The product stocked successfully");
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
				alert.setContentText("An error occurred while inserting the stocked product");
				alert.showAndWait();
			}
		}
	}

	@FXML
	void product(ActionEvent event) {


		try {
			// Load the FXML file
			FXMLLoader loader = new FXMLLoader(getClass().getResource("product.fxml"));
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
	void update(ActionEvent event) throws SQLException {
		// Retrieve the selected StockedProduct
		StockedProducts selectedProduct = stockedTv.getSelectionModel().getSelectedItem();

		// Check if an item is selected
		if (selectedProduct == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Please select a stocked product to update.");
			alert.showAndWait();
			return;
		}

		// Validate input fields
		if (quantityTxt.getText() == null || datePicker.getValue() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Please fill in all the fields to update.");
			alert.showAndWait();
			return;
		}

		// Prompt the user for confirmation
		Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
		confirmationAlert.setTitle("Confirm Update");
		confirmationAlert.setHeaderText(null);
		confirmationAlert.setContentText("Are you sure you want to update this stocked product?");
		Optional<ButtonType> result = confirmationAlert.showAndWait();

		// If the user confirms the update
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try (Connection connection = DBconnetion.getConnection();
					PreparedStatement statement = connection.prepareStatement(
							"UPDATE StockedProduct SET stocked_date = ?, stocked_quantity = ? , store_name = ? WHERE product_code = ?")) {
				statement.setDate(1, java.sql.Date.valueOf(datePicker.getValue()));
				statement.setInt(2, Integer.parseInt(quantityTxt.getText()));
				statement.setString(3, warehouseCmb.getSelectionModel().getSelectedItem().getStore_name());
				statement.setInt(4, selectedProduct.getProduct_code());
				statement.executeUpdate();

				// Update the selected item in the TableView
				selectedProduct.setStocked_date(java.sql.Date.valueOf(datePicker.getValue()));
				selectedProduct.setStocked_quantity(Integer.parseInt(quantityTxt.getText()));

				// Show success message
				Alert successAlert = new Alert(AlertType.INFORMATION);
				successAlert.setTitle("Success");
				successAlert.setHeaderText(null);
				successAlert.setContentText("Stocked product updated successfully.");
				successAlert.showAndWait();
			} catch (SQLException e) {
				e.printStackTrace();
				Alert errorAlert = new Alert(AlertType.ERROR);
				errorAlert.setTitle("Error");
				errorAlert.setHeaderText(null);
				errorAlert.setContentText("An error occurred while updating the stocked product.");
				errorAlert.showAndWait();
			}
		}
		showStockedProduct();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {

			showStockedProduct();
			populateComboBox();
			populateComboBox2();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ObservableList<StockedProducts> getStockedProduct(String query) throws SQLException {
		ObservableList<StockedProducts> stockedList = FXCollections.observableArrayList();
		Connection conn = DBconnetion.getConnection();
		java.sql.Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(query);
			StockedProducts stockedProduct;
			if (!rs.isBeforeFirst()) {
				System.out.println(" No records found for the query :" + query);
				return null;

			} else {
				while (rs.next()) {
					stockedProduct = new StockedProducts(rs.getInt("product_code"), rs.getString("pname"),
							rs.getInt("price"), rs.getDouble("wholesale_price"), rs.getString("product_company_name"),
							rs.getInt("quantity"), rs.getInt("c_id"), rs.getDate("stocked_date"),
							rs.getInt("stocked_quantity"), rs.getString("store_name"));
					stockedList.add(stockedProduct);
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
		return stockedList;
	}

	public void showStockedProduct() throws SQLException {
		String query = "SELECT sp.*, p.* " + "FROM project.StockedProduct sp "
				+ "INNER JOIN project.Product p ON sp.product_code = p.product_code";

		ObservableList<StockedProducts> productList = getStockedProduct(query);
		stockedCodeCoulmn.setCellValueFactory(new PropertyValueFactory<>("product_code"));
		stockedNameCoulmn.setCellValueFactory(new PropertyValueFactory<>("pname"));
		wholeCoulmn.setCellValueFactory(new PropertyValueFactory<>("wholesale_price"));
		priceCoulmn.setCellValueFactory(new PropertyValueFactory<>("price"));
		companyCoulmn.setCellValueFactory(new PropertyValueFactory<>("product_company_name"));
		allQuantityCoulmn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		categotyCoulmn.setCellValueFactory(new PropertyValueFactory<>("c_id"));
		stockedDateCoulmn.setCellValueFactory(new PropertyValueFactory<>("stocked_date"));
		quantityCoulmn.setCellValueFactory(new PropertyValueFactory<>("stocked_quantity"));
		warehouseCoulmn.setCellValueFactory(new PropertyValueFactory<>("store_name"));
		stockedTv.setItems(productList);
	}

	private void populateComboBox() throws SQLException {
		String query = "SELECT * FROM project.Product";
		List<Product> product = getProduct(query); // Adjust this method name to match your implementation
		ObservableList<Product> productList = FXCollections.observableArrayList(product);

		// Set the cell factory to display both code and name
		proCmb.setCellFactory(param -> new ListCell<Product>() {
			@Override
			protected void updateItem(Product item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					setText(item.getProduct_code() + " - " + item.getPname() + " - " + item.getWholesale_price() + " - "
							+ item.getPrice() + " - " + item.getProduct_company_name() + " - " + item.getQuantity()
							+ " - " + item.getC_id());
				}
			}
		});

		proCmb.setItems(productList);
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

	public ObservableList<Warehouse> getWarehouse(String query) throws SQLException {
		ObservableList<Warehouse> warehouseList = FXCollections.observableArrayList();
		Connection conn = DBconnetion.getConnection();
		java.sql.Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(query);
			Warehouse warehouse;
			if (!rs.isBeforeFirst()) {
				System.out.println(" No records found for the query :" + query);
				return null;

			} else {
				while (rs.next()) {
					warehouse = new Warehouse(rs.getString("store_name"), rs.getString("address"));
					warehouseList.add(warehouse);
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
		return warehouseList;
	}

	private void populateComboBox2() throws SQLException {
		String query = "SELECT * FROM project.store";
		List<Warehouse> warehouse = getWarehouse(query); // Adjust this method name to match your implementation
		ObservableList<Warehouse> wareList = FXCollections.observableArrayList(warehouse);

		// Set the cell factory to display both code and name
		warehouseCmb.setCellFactory(param -> new ListCell<Warehouse>() {
			@Override
			protected void updateItem(Warehouse item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					setText(item.getStore_name() + " - " + item.getAddress());
				}
			}
		});

		warehouseCmb.setItems(wareList);
	}

}
