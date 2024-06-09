package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class WarehouseController implements Initializable {

	@FXML
	private TableColumn<Warehouse, String> addressCoulmn;

	@FXML
	private Button backToStockedBtn;

	@FXML
	private Button deleteStoreBtn;

	@FXML
	private Button insertStoreBtn;

	@FXML
	private Button mainBtn;

	@FXML
	private TableColumn<Warehouse, String> nameCoulmn;

	@FXML
	private Label storeAddressLbl;

	@FXML
	private TextField storeAddressTxt;

	@FXML
	private Label storeNameLbl;

	@FXML
	private TextField storeNameTxt;

	@FXML
	private Label storelbl;

	@FXML
	private AnchorPane storepane;

	@FXML
	private TableView<Warehouse> storetv;

	@FXML
	private Button updateStoreBtn;

	@FXML
	void delete(ActionEvent event) {
		Warehouse selectedWarehouse = storetv.getSelectionModel().getSelectedItem();
		if (selectedWarehouse == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Please select a warehouse to delete.");
			alert.showAndWait();
			return;
		}

		Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
		confirmationAlert.setTitle("Confirm Deletion");
		confirmationAlert.setHeaderText(null);
		confirmationAlert.setContentText("Are you sure you want to delete this warehouse?");
		Optional<ButtonType> result = confirmationAlert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try (Connection connection = DBconnetion.getConnection()) {
				// Start transaction
				connection.setAutoCommit(false);

				// Delete the warehouse
				String deleteWarehouseQuery = "DELETE FROM store WHERE store_name = ?";
				try (PreparedStatement deleteWarehouseStatement = connection.prepareStatement(deleteWarehouseQuery)) {
					deleteWarehouseStatement.setString(1, selectedWarehouse.getStore_name());
					deleteWarehouseStatement.executeUpdate();
				}

				// Commit transaction
				connection.commit();
				showWarehouse();
				// Show success message
				Alert successAlert = new Alert(AlertType.INFORMATION);
				successAlert.setTitle("Success");
				successAlert.setHeaderText(null);
				successAlert.setContentText("Warehouse deleted successfully.");
				successAlert.showAndWait();

				// Refresh the warehouse table view if needed
				// Replace this with your refresh logic
				// refreshTableView();

			} catch (SQLException e) {
				// Handle exceptions
				e.printStackTrace();
				Alert errorAlert = new Alert(AlertType.ERROR);
				errorAlert.setTitle("Error");
				errorAlert.setHeaderText(null);
				errorAlert.setContentText("An error occurred while deleting the warehouse.");
				errorAlert.showAndWait();
			}
		}
	}

	@FXML
	void insert(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		if (storeNameTxt.getText().isEmpty() || storeAddressTxt.getText().isEmpty()) {
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Please fill all the fields");
			alert.showAndWait();
			return; // Exit the method early if any text field is empty
		}
		try (Connection connection = DBconnetion.getConnection();
				PreparedStatement statement = connection.prepareStatement("INSERT INTO store VALUES (?, ?)")) {
			statement.setString(1, storeNameTxt.getText());
			statement.setString(2, storeAddressTxt.getText());

			statement.executeUpdate();
			showWarehouse();

			alert.setTitle("Done");
			alert.setHeaderText(null);
			alert.setContentText("The warehouse inserted successfully");
			alert.showAndWait();
		} catch (SQLException e) {

			if (e.getMessage().contains("Duplicate entry")) {
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("The entered warehouse name already exists");
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
	void main(ActionEvent event) {

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
	void stock(ActionEvent event) {

	}

	@FXML
	void update(ActionEvent event) throws SQLException {
		// Check if a warehouse is selected
		Warehouse selectedWarehouse = storetv.getSelectionModel().getSelectedItem();
		if (selectedWarehouse == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Please select a warehouse to update.");
			alert.showAndWait();
			return;
		}

		// Validate input fields
		if (storeAddressTxt.getText() == null || storeAddressTxt.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Please fill in an address to update.");
			alert.showAndWait();
			return;
		}

		try (Connection connection = DBconnetion.getConnection()) {
			// Start transaction
			connection.setAutoCommit(false);

			// Update the warehouse in the store table
			String updateQuery = "UPDATE store SET address = ? WHERE store_name = ?";
			try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
				updateStatement.setString(1, storeAddressTxt.getText());
				updateStatement.setString(2, selectedWarehouse.getStore_name());
				updateStatement.executeUpdate();
			}

			// Commit transaction
			connection.commit();

			// Show success message
			Alert successAlert = new Alert(AlertType.INFORMATION);
			successAlert.setTitle("Success");
			successAlert.setHeaderText(null);
			successAlert.setContentText("Warehouse updated successfully.");
			successAlert.showAndWait();

			// Refresh the warehouse table view if needed
			// Replace this with your refresh logic
			// refreshTableView();

		} catch (SQLException e) {
			// Handle exceptions
			e.printStackTrace();
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setTitle("Error");
			errorAlert.setHeaderText(null);
			errorAlert.setContentText("An error occurred while updating the warehouse.");
			errorAlert.showAndWait();
		}
		showWarehouse();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			showWarehouse();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void showWarehouse() throws SQLException {
		String query = "SELECT * FROM project.store";
		ObservableList<Warehouse> warehouseList = getWarehouse(query);
		nameCoulmn.setCellValueFactory(new PropertyValueFactory<>("store_name"));
		addressCoulmn.setCellValueFactory(new PropertyValueFactory<>("address"));
		storetv.setItems(warehouseList);

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

}
