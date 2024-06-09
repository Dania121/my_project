package application;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.scene.Node;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CategoryController implements Initializable {

	@FXML
	private Button best;

	@FXML
	private TableColumn<Category, String> c_nameColumn;

	@FXML
	private AnchorPane categoryPane;

	@FXML
	private Label category_label;

	@FXML
	private TableView<Category> category_tv;

	@FXML
	private TableColumn<Category, Integer> cid_column;

	@FXML
	private Button delet_btn;

	@FXML
	private Label delete_label;

	@FXML
	private TextField idUpdate_txt;

	@FXML
	private Label idinsertlbl;

	@FXML
	private TextField idinserttxt;

	@FXML
	private Button insertCategoryBtn;

	@FXML
	private Label insertName_label;

	@FXML
	private TextField nameUpdate_txt;

	@FXML
	private TextField nametinsertxt;

	@FXML
	private Button showProbtn;

	@FXML
	private Button update_btn;

	@FXML
	private Label update_label;
	Alert alert = new Alert(AlertType.ERROR);
	

	@FXML
	void bestSelling(ActionEvent event) {

	}

	@FXML
	void deleteCategory(ActionEvent event) throws SQLException {

		String query = "DELETE FROM project.category WHERE c_id =" + idinserttxt.getText() + "";
		executeQuery(query);
		showCategory();

	}

	@FXML
	void goToProductScreen(ActionEvent event) {
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
	void insertCategory(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		if (idinserttxt.getText().isEmpty() || nametinsertxt.getText().isEmpty()) {
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Please fill all the fields");
			alert.showAndWait();
			return; // Exit the method early if any text field is empty
		}
		try (Connection connection = DBconnetion.getConnection();
				PreparedStatement statement = connection.prepareStatement("INSERT INTO category VALUES (?, ?)")) {
			statement.setInt(1, Integer.parseInt(idinserttxt.getText()));
			statement.setString(2, nametinsertxt.getText());

			statement.executeUpdate();
			showCategory();

			alert.setTitle("Done");
			alert.setHeaderText(null);
			alert.setContentText("The category inserted successfully");
			alert.showAndWait();
		} catch (SQLException e) {

			if (e.getMessage().contains("Duplicate entry")) {
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("The entered category ID already exists");
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
	void updateCategory(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		// Get the c_id from the text field
		int categoryId = Integer.parseInt(idinserttxt.getText());
		try {
			// Check if the category with the given c_id exists
			if (!categoryExists(categoryId)) {
				// Show an alert if the category doesn't exist
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("Category with ID " + categoryId + " does not exist.");
				alert.showAndWait();
				return; // Exit the method
			}

			// Construct the update query
			String query = "UPDATE project.category SET c_name = '" + nametinsertxt.getText() + "' WHERE c_id = "
					+ categoryId;

			// Execute the query
			executeQuery(query);

			// Show the updated category
			showCategory();
		} catch (SQLException e) {
			e.printStackTrace();
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("An error occurred while updating the category.");
			alert.showAndWait();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		try {
			showCategory();
			// handleRowSelection(new MouseEvent());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void showCategory() throws SQLException {
		String query = "SELECT * FROM project.category";
		ObservableList<Category> categoryList = getCategory(query);
		cid_column.setCellValueFactory(new PropertyValueFactory<>("c_id"));
		c_nameColumn.setCellValueFactory(new PropertyValueFactory<>("c_name"));
		category_tv.setItems(categoryList);

	}

	public ObservableList<Category> getCategory(String query) throws SQLException {
		ObservableList<Category> categoryList = FXCollections.observableArrayList();
		Connection conn = DBconnetion.getConnection();
		java.sql.Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(query);
			Category category;
			if (!rs.isBeforeFirst()) {
				System.out.println(" No records found for the query :" + query);
				return null;

			} else {
				while (rs.next()) {
					category = new Category(rs.getInt("c_id"), rs.getString("c_name"));
					categoryList.add(category);
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
		return categoryList;
	}

	public void handleRowSelection(MouseEvent event) {
		System.out.println("inside handleRowSelection");
		int index = category_tv.getSelectionModel().getSelectedIndex();
		System.out.println("index is " + index);
		if (index <= -1) {
			System.out.println("No row selected");
			return;
		}
		Category selectedCategory = category_tv.getSelectionModel().getSelectedItem();
		if (selectedCategory != null) {
			idinserttxt.setText(String.valueOf(selectedCategory.getC_id()));
			nametinsertxt.setText(selectedCategory.getC_name());
		}
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

	private boolean categoryExists(int categoryId) throws SQLException {
		String query = "SELECT COUNT(*) FROM project.category WHERE c_id = " + categoryId;
		try (Connection connection = DBconnetion.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {
			resultSet.next();
			int count = resultSet.getInt(1);
			return count > 0;
		}
	}
}
