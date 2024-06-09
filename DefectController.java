package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DefectController implements Initializable {

	@FXML
	private Button backBtn;

	@FXML
	private TableColumn<Defect, Integer> categoryCoulmn;

	@FXML
	private TableColumn<Defect, String> causeCoulmn;

	@FXML
	private TableColumn<Defect, String> companyCoulmn;

	@FXML
	private TableColumn<Defect, Integer> deectQuantityCoulmn;

	@FXML
	private Label defCauseLbl;

	@FXML
	private TextField defCauseTxt;

	@FXML
	private Button defectBackBtn;

	@FXML
	private TableColumn<Defect, Integer> defectCodeCoulmn;

	@FXML
	private AnchorPane defectPane;

	@FXML
	private TableView<Defect> defectTv;

	@FXML
	private Label defectlbl;

	@FXML
	private Button deleteDefBtn;

	@FXML
	private Button insertDefBtn;

	@FXML
	private Label label1;

	@FXML
	private TableColumn<Defect, String> nameCoulmn;

	@FXML
	private TableColumn<Defect, Integer> priceCoulmn;

	@FXML
	private ComboBox<Product> productCmb;

	@FXML
	private TableColumn<Defect, Integer> quantityCoulmn;

	@FXML
	private Label quantityLbl;

	@FXML
	private TextField quantityTxt;

	@FXML
	private Button updateDefBtn;

	@FXML
	private TableColumn<Defect, Double> wholeCoulmn;

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

		Defect selectedProduct = defectTv.getSelectionModel().getSelectedItem();

		// Check if an item is selected
		if (selectedProduct == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Please select a defect product to delete.");
			alert.showAndWait();
			return;
		}

		// Prompt the user for confirmation
		Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
		confirmationAlert.setTitle("Confirm Deletion");
		confirmationAlert.setHeaderText(null);
		confirmationAlert.setContentText("Are you sure you want to delete this defect product?");
		Optional<ButtonType> result = confirmationAlert.showAndWait();

		// If the user confirms the deletion
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try (Connection connection = DBconnetion.getConnection();
					PreparedStatement statement = connection
							.prepareStatement("DELETE FROM DefectProduct WHERE product_code = ?")) {
				statement.setInt(1, selectedProduct.getProduct_code());
				statement.executeUpdate();

				// Remove the deleted item from the TableView
				defectTv.getItems().remove(selectedProduct);

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
				errorAlert.setContentText("An error occurred while deleting the defect product.");
				errorAlert.showAndWait();
			}
		}

	}

	@FXML
	void insert(ActionEvent event) {

		Alert alert = new Alert(AlertType.INFORMATION);
		if (quantityTxt.getText().isEmpty() || productCmb.getSelectionModel().isEmpty()
				|| defCauseTxt.getText().isEmpty()) {
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Please fill all the fields and select from all the comboboxes");
			alert.showAndWait();
			return; // Exit the method early if any text field is empty
		}

		try (Connection connection = DBconnetion.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"INSERT INTO DefectProduct (product_code, defect_cause, quantitiy) VALUES (?, ?, ?)")) {
			statement.setInt(1, productCmb.getSelectionModel().getSelectedItem().getProduct_code());
			statement.setString(2, defCauseTxt.getText());

			statement.setInt(3, Integer.parseInt(quantityTxt.getText()));

			statement.executeUpdate();
			showDefect();
			alert.setTitle("Done");
			alert.setHeaderText(null);
			alert.setContentText("The product add to the defect products successfully");
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
				alert.setContentText("An error occurred while inserting the defect product");
				alert.showAndWait();
			}
		}

	}

	@FXML
	void productBack(ActionEvent event) {

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
		// Retrieve the selected DefectProduct
		Defect selectedProduct = defectTv.getSelectionModel().getSelectedItem();

		// Check if an item is selected
		if (selectedProduct == null) {
			showAlert(AlertType.ERROR, "Error", "No Product Selected", "Please select a defect product to update.");
			return;
		}

		// Validate input fields
		if (defCauseTxt.getText().isEmpty() || quantityTxt.getText().isEmpty()) {
			showAlert(AlertType.ERROR, "Error", "Missing Fields", "Please fill in all the fields to update.");
			return;
		}

		// Parse the quantity text to an integer
		int quantity;
		try {
			quantity = Integer.parseInt(quantityTxt.getText());
		} catch (NumberFormatException e) {
			showAlert(AlertType.ERROR, "Error", "Invalid Quantity", "Please enter a valid integer for quantity.");
			return;
		}

		// Prompt the user for confirmation
		Optional<ButtonType> result = showAlert(AlertType.CONFIRMATION, "Confirm Update", null,
				"Are you sure you want to update this defect product?");

		// If the user confirms the update
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try (Connection connection = DBconnetion.getConnection();
					PreparedStatement statement = connection.prepareStatement(
							"UPDATE DefectProduct SET defect_cause = ?, quantitiy = ? WHERE product_code = ?")) {
				statement.setString(1, defCauseTxt.getText());
				statement.setInt(2, quantity);
				statement.setInt(3, selectedProduct.getProduct_code());
				statement.executeUpdate();

				// Update the selected item in the TableView
				selectedProduct.setDefect_cause(defCauseTxt.getText());
				selectedProduct.setQuantitiy(quantity);

				// Show success message
				showAlert(AlertType.INFORMATION, "Success", null, "Defect product updated successfully.");
			} catch (SQLException e) {
				e.printStackTrace();
				showAlert(AlertType.ERROR, "Error", "Database Error",
						"An error occurred while updating the defect product.");
			}
		}

		// Refresh the TableView
		showDefect();
	}

	private Optional<ButtonType> showAlert(AlertType alertType, String title, String headerText, String contentText) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		return alert.showAndWait();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			showDefect();
			populateComboBox();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void showDefect() throws SQLException {
		String query = "SELECT dp.*, p.* FROM project.DefectProduct dp INNER JOIN project.Product p ON dp.product_code = p.product_code";

		ObservableList<Defect> productList = getDefect(query);
		defectCodeCoulmn.setCellValueFactory(new PropertyValueFactory<>("product_code"));
		nameCoulmn.setCellValueFactory(new PropertyValueFactory<>("pname"));
		wholeCoulmn.setCellValueFactory(new PropertyValueFactory<>("wholesale_price"));
		priceCoulmn.setCellValueFactory(new PropertyValueFactory<>("price"));
		companyCoulmn.setCellValueFactory(new PropertyValueFactory<>("product_company_name"));
		quantityCoulmn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		categoryCoulmn.setCellValueFactory(new PropertyValueFactory<>("c_id"));
		causeCoulmn.setCellValueFactory(new PropertyValueFactory<>("defect_cause"));
		deectQuantityCoulmn.setCellValueFactory(new PropertyValueFactory<>("quantitiy"));
		defectTv.setItems(productList);
	}

	public ObservableList<Defect> getDefect(String query) throws SQLException {
		ObservableList<Defect> defectList = FXCollections.observableArrayList();
		Connection conn = DBconnetion.getConnection();
		java.sql.Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(query);
			Defect defect;
			if (!rs.isBeforeFirst()) {
				System.out.println(" No records found for the query :" + query);
				return null;

			} else {
				while (rs.next()) {
					defect = new Defect(rs.getInt("product_code"), rs.getString("pname"), rs.getInt("price"),
							rs.getDouble("wholesale_price"), rs.getString("product_company_name"),
							rs.getInt("quantity"), rs.getInt("c_id"), rs.getString("defect_cause"),
							rs.getInt("quantitiy"));
					defectList.add(defect);
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
		return defectList;
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
		String query = "SELECT * FROM project.Product";
		List<Product> product = getProduct(query); // Adjust this method name to match your implementation
		ObservableList<Product> productList = FXCollections.observableArrayList(product);

		// Set the cell factory to display both code and name
		productCmb.setCellFactory(param -> new ListCell<Product>() {
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

		productCmb.setItems(productList);
	}

}
