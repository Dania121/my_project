package application;

import java.awt.Dialog;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
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
import javafx.scene.control.ButtonBar.ButtonData;
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

public class ColorController implements Initializable {

	@FXML
	private TableColumn<Color, Integer> ColorCodeCoulmn;

	@FXML
	private Button DeleteColor;

	@FXML
	private Label ProCodeColbl;

	@FXML
	private Button back;

	@FXML
	private Button cancel;

	@FXML
	private Label clbl;

	@FXML
	private ComboBox<Product> cmb;

	@FXML
	private ComboBox<Product> cmbStrong;

	@FXML
	private TableColumn<Color, String> colorCoulmn;

	@FXML
	private AnchorPane colorPane;

	@FXML
	private TableView<Color> colortv;

	@FXML
	private Button insertColor;

	@FXML
	private TableColumn<Color, Double> percent;

	@FXML
	private Label proColbl;

	@FXML
	private TextField proCotxt;

	@FXML
	private Label strongColbl;

	@FXML
	private TableColumn<Color, String> strongColorCoulmn;

	@FXML
	private TableView<Color> strong_tv;

	@FXML
	private Button strongbtn;

	@FXML
	private Button updateColor;

	@FXML
	void Back(ActionEvent event) {
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
	void Cancel(ActionEvent event) {
		Node sourceNode = (Node) event.getSource();

		// Get the current stage
		Stage stage = (Stage) sourceNode.getScene().getWindow();

		// Close the stage
		stage.close();
	}

	@FXML
	void chooseProductCode(ActionEvent event) {

	}

	@FXML
	void chooseStrong(ActionEvent event) {

	}

	@FXML
	void deleteColor(ActionEvent event) throws SQLException {
		Color color = colortv.getSelectionModel().getSelectedItem();
		if (color == null) {
			// If no color is selected, show an alert and return
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Please select a color to delete.");
			alert.showAndWait();
			return;
		}

		String query = "DELETE FROM ProductColor WHERE product_code = ? AND Color = ?";
		try (Connection connection = DBconnetion.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			// Set parameters for the prepared statement
			statement.setInt(1, color.getProduct_code());
			statement.setString(2, color.getColor());

			// Execute the delete statement
			int rowsAffected = statement.executeUpdate();

			if (rowsAffected > 0) {
				// If deletion was successful, show success message and refresh the table
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Success");
				alert.setHeaderText(null);
				alert.setContentText("Color deleted successfully.");
				alert.showAndWait();
				showColors(); // Refresh the table
			} else {
				// If no rows were affected, show error message
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("Failed to delete color.");
				alert.showAndWait();
			}
		} catch (SQLException e) {
			// Handle SQL exception
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("An error occurred while deleting color.");
			alert.showAndWait();
		}
	}

	@FXML
	void insertColor(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		if (proCotxt.getText().isEmpty() || cmb.getSelectionModel().isEmpty()) {
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Please fill all the fields and select product");
			alert.showAndWait();
			return; // Exit the method early if any text field is empty
		}

		try (Connection connection = DBconnetion.getConnection();
				PreparedStatement statement = connection.prepareStatement("INSERT INTO ProductColor VALUES (?, ?)")) {

			Product selectedProduct = cmb.getSelectionModel().getSelectedItem();
			if (selectedProduct != null) {
				int categoryId = selectedProduct.getC_id();
				statement.setInt(1, categoryId);
			} else {
				// Handle case when no category is selected
				statement.setNull(1, java.sql.Types.INTEGER);
			}

			statement.setString(2, proCotxt.getText());

			statement.executeUpdate();
			showColors();

			alert.setTitle("Done");
			alert.setHeaderText(null);
			alert.setContentText("The ccolor inserted successfully");
			alert.showAndWait();
		} catch (SQLException e) {

			if (e.getMessage().contains("Duplicate entry")) {
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("The entered product ID already exists");
				alert.showAndWait();
			} else {
				e.printStackTrace();
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("An error occurred while inserting the product color");
				alert.showAndWait();

			}
		}

	}

	@FXML
	void showStrong(ActionEvent event) {

	}

	@FXML
	void updateColor(ActionEvent event) {
		Color selectedColor = colortv.getSelectionModel().getSelectedItem();
		if (selectedColor == null) {
			// If no color is selected, show an alert and return
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Please select a color to update.");
			alert.showAndWait();
			return;
		}

		// Create a dialog for updating color information
		javafx.scene.control.Dialog<Color> dialog = new javafx.scene.control.Dialog<>();
		dialog.setTitle("Update Color");
		dialog.setHeaderText(null);
		dialog.setResizable(false);

		// Set the buttons for the dialog
		ButtonType updateButtonType = new ButtonType("Update", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

		// Create the form fields
		TextField colorTextField = new TextField();
		colorTextField.setPromptText("Color");
		colorTextField.setText(selectedColor.getColor());

		// Add form fields to dialog pane
		dialog.getDialogPane().setContent(colorTextField);

		// Request focus on the color field by default
		Platform.runLater(() -> colorTextField.requestFocus());

		// Convert the result to a color when the update button is clicked
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == updateButtonType) {
				return new Color(selectedColor.getProduct_code(), colorTextField.getText());
			}
			return null;
		});

		// Show the dialog and wait for user input
		Optional<Color> result = dialog.showAndWait();

		// Process the result
		result.ifPresent(updatedColor -> {
			try {
				// Update the color in the database
				String query = "UPDATE ProductColor SET Color = ? WHERE product_code = ? AND Color = ?";
				try (Connection connection = DBconnetion.getConnection();
						PreparedStatement statement = connection.prepareStatement(query)) {
					statement.setString(1, updatedColor.getColor());
					statement.setInt(2, updatedColor.getProduct_code());
					statement.setString(3, selectedColor.getColor());

					int rowsAffected = statement.executeUpdate();

					if (rowsAffected > 0) {
						// If update successful, show success message and refresh the table
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Success");
						alert.setHeaderText(null);
						alert.setContentText("Color updated successfully.");
						alert.showAndWait();
						showColors(); // Refresh the table
					} else {
						// If no rows were affected, show error message
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error");
						alert.setHeaderText(null);
						alert.setContentText("Failed to update color.");
						alert.showAndWait();
					}
				}
			} catch (SQLException e) {
				// Handle SQL exception
				e.printStackTrace();
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("An error occurred while updating color.");
				alert.showAndWait();
			}
		});
	}

	public void initialize(URL arg0, ResourceBundle arg1) {

		try {
			showColors();
			populateComboBox();
			// populateComboBox();
			// handleRowSelection(new MouseEvent());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ObservableList<Color> getColor(String query) throws SQLException {
		ObservableList<Color> ColorList = FXCollections.observableArrayList();
		Connection conn = DBconnetion.getConnection();
		java.sql.Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(query);
			Color color;
			if (!rs.isBeforeFirst()) {
				System.out.println(" No records found for the query :" + query);
				return null;

			} else {
				while (rs.next()) {
					color = new Color(rs.getInt("product_code"), rs.getString("Color"));
					ColorList.add(color);
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
		return ColorList;
	}

	public void showColors() throws SQLException {
		String query = "SELECT * FROM project.ProductColor";
		ObservableList<Color> colorList = getColor(query);
		ColorCodeCoulmn.setCellValueFactory(new PropertyValueFactory<>("product_code"));
		colorCoulmn.setCellValueFactory(new PropertyValueFactory<>("Color"));

		colortv.setItems(colorList);

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
		ObservableList<Product> categoryList = FXCollections.observableArrayList(products);

		// Set the cell factory to display both code and name
		cmb.setCellFactory(param -> new ListCell<Product>() {
			@Override
			protected void updateItem(Product item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					setText(item.getProduct_code() + " - " + item.getPname());
				}
			}
		});
		cmbStrong.setCellFactory(param -> new ListCell<Product>() {
			@Override
			protected void updateItem(Product item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					setText(item.getProduct_code() + " - " + item.getPname());
				}
			}
		});

		cmb.setItems(categoryList);
		cmbStrong.setItems(categoryList);
	}

	public void executeQuery(String query) throws SQLException {
		Connection conn = DBconnetion.getConnection();
		Statement st;
		try {
			st = conn.createStatement();
			st.executeUpdate(query);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
