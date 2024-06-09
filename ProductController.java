package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
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

public class ProductController implements Initializable {

	@FXML
	private TableColumn<Product, Integer> categoryCoulmn;

	@FXML
	private Label categoryidlbl;

	@FXML
	private ComboBox<Category> cmb;

	@FXML
	private TableColumn<Product, Integer> codeCoulmn;

	@FXML
	private TextField codetxt;

	@FXML
	private Button colorbtn;

	@FXML
	private TableColumn<Product, String> companyCoulmn;

	@FXML
	private Label companylbl;

	@FXML
	private TextField companytxt;

	@FXML
	private Button defektbtn;

	@FXML
	private Button deletebtn;

	@FXML
	private Button displaybtn;

	@FXML
	private Button insertbtn;

	@FXML
	private TableColumn<Product, Integer> priceCoulmn;

	@FXML
	private Label pricelbl;

	@FXML
	private TextField pricetxt;

	@FXML
	private Label proCodelbl;

	@FXML
	private Label pro_qlbl;

	@FXML
	private Label productNamelbl;

	@FXML
	private TextField productNametxt;

	@FXML
	private AnchorPane productPane;

	@FXML
	private TableView<Product> product_tv;

	@FXML
	private TableColumn<Product, Integer> profitCodeCoulmn;

	@FXML
	private TableColumn<Product, Double> profitCoulmn;

	@FXML
	private Label profitLbl;

	@FXML
	private TableView<ProfitRow> profit_tv;

	@FXML
	private Label prolabel;

	@FXML
	private TableColumn<Product, String> pronameCoulmn;

	@FXML
	private TextField quantitiytxt;

	@FXML
	private TableColumn<Product, Integer> quantityCoulmn;

	@FXML
	private Button stockedProductbtn;

	@FXML
	private Button updatebtn;

	@FXML
	private TableColumn<Product, Double> wholeCoulmn;

	@FXML
	private Label wholePricelbl;

	@FXML
	private TextField wholetxt;
	private CategoryController categoryController; // Assuming you have an instance of CategoryController

//	public ProductController(CategoryController categoryController) {
//		this.categoryController = categoryController;
//	}
//	public ProductController() {
//	}

	@FXML
	void DeleteProduct(ActionEvent event) throws SQLException {
		Alert alert = new Alert(AlertType.INFORMATION);
		// Check if the codetxt field is empty
		if (codetxt.getText().isEmpty()) {
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Please enter a product code.");
			alert.showAndWait();
			return; // Exit the method if the codetxt field is empty
		}

		int productCode = Integer.parseInt(codetxt.getText());
		// Check if the product with the given code exists
		if (!ProductExists(productCode)) {
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Product with ID " + productCode + " does not exist.");
			alert.showAndWait();
			return; // Exit the method if the product doesn't exist
		}

		String query = "DELETE FROM project.product WHERE product_code =" + productCode;
		executeQuery(query);
		showProduct();
	}

	@FXML
	void OpenAddColor(ActionEvent event) {

		try {
			// Load the FXML file
			FXMLLoader loader = new FXMLLoader(getClass().getResource("color.fxml"));
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
	void OpenStockedProduct(ActionEvent event) {

	}

	@FXML
	void chooseCategory(ActionEvent event) {

	}

	@FXML
	void insertProduct(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		if (codetxt.getText().isEmpty() || productNametxt.getText().isEmpty() || wholetxt.getText().isEmpty()
				|| pricetxt.getText().isEmpty() || companytxt.getText().isEmpty() || quantitiytxt.getText().isEmpty()|| cmb.getSelectionModel().isEmpty()) {
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Please fill all the fields and select Category");
			alert.showAndWait();
			return; // Exit the method early if any text field is empty
		}
		try (Connection connection = DBconnetion.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("INSERT INTO product VALUES ( ?, ?, ?, ?, ?, ?, ?)")) {
			statement.setInt(1, Integer.parseInt(codetxt.getText()));
			statement.setString(2, productNametxt.getText());
			statement.setDouble(3, Double.parseDouble(wholetxt.getText()));
			statement.setInt(4, Integer.parseInt(pricetxt.getText()));
			statement.setString(5, companytxt.getText());
			statement.setInt(6, Integer.parseInt(quantitiytxt.getText()));
			Category selectedCategory = cmb.getSelectionModel().getSelectedItem();
			if (selectedCategory != null) {
				int categoryId = selectedCategory.getC_id();
				statement.setInt(7, categoryId);
			} else {
				// Handle case when no category is selected
				statement.setNull(7, java.sql.Types.INTEGER);
			}

			statement.executeUpdate();
			showProduct();

			alert.setTitle("Done");
			alert.setHeaderText(null);
			alert.setContentText("The product inserted successfully");
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
	void openDefektProuct(ActionEvent event) {

	}

	@FXML
	void openDisplayProduct(ActionEvent event) {

	}

	@FXML
	void updateProduct(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		// Check if any text field is empty or no category is selected
		if (codetxt.getText().isEmpty() || productNametxt.getText().isEmpty() || pricetxt.getText().isEmpty()
				|| wholetxt.getText().isEmpty() || companytxt.getText().isEmpty() || quantitiytxt.getText().isEmpty()
				|| cmb.getSelectionModel().isEmpty()) {
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Please fill all the fields and select a category.");
			alert.showAndWait();
			return; // Exit the method if any text field is empty or no category is selected
		}

		// Get the product code from the text field
		int productCode = Integer.parseInt(codetxt.getText());
		try {
			// Check if the product with the given code exists
			if (!ProductExists(productCode)) {
				// Show an alert if the product doesn't exist
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("Product with ID " + productCode + " does not exist.");
				alert.showAndWait();
				return; // Exit the method
			}

			// Construct the update query
			String query = "UPDATE project.product SET pname = '" + productNametxt.getText() + "', price = "
					+ pricetxt.getText() + ", wholesale_price = " + wholetxt.getText() + ", product_company_name = '"
					+ companytxt.getText() + "', quantity = " + quantitiytxt.getText() + ", c_id = "
					+ cmb.getSelectionModel().getSelectedItem().getC_id() + " WHERE product_code = " + productCode;

			// Execute the query
			executeQuery(query);

			// Show the updated product
			showProduct();

			alert.setTitle("Success");
			alert.setHeaderText(null);
			alert.setContentText("The product updated successfully");
			alert.showAndWait();
		} catch (NumberFormatException e) {
			// Handle number format exception
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Please enter valid numeric values for price and quantity.");
			alert.showAndWait();
		} catch (SQLException e) {
			// Handle SQL exception
			e.printStackTrace();
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("An error occurred while updating the product.");
			alert.showAndWait();
		}
	}

	public void initialize(URL arg0, ResourceBundle arg1) {

		try {
			showProduct();
			populateComboBox();
			// handleRowSelection(new MouseEvent());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	private void populateComboBox() throws SQLException {
		String query = "SELECT * FROM project.category";
		List<Category> categories = getCategory(query);
		ObservableList<Category> categoryList = FXCollections.observableArrayList(categories);

		// Set the cell factory to display both code and name
		cmb.setCellFactory(param -> new ListCell<Category>() {
			@Override
			protected void updateItem(Category item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					setText(item.getC_id() + " - " + item.getC_name());
				}
			}
		});

		cmb.setItems(categoryList);
	}

//	private void populateComboBox() throws SQLException {
//	    String query = "SELECT * FROM project.category";
//	    List<Category> categories = getCategory(query);
//	    ObservableList<String> categoryNames = FXCollections.observableArrayList();
//	    
//	    // Convert category objects to strings with code and name
//	    for (Category category : categories) {
//	        String categoryName = category.getC_id() + " - " + category.getC_name();
//	        categoryNames.add(categoryName);
//	    }
//	    
//	    cmb.setItems(categoryNames);
//	}

//	 private void populateComboBox() throws SQLException {
//		 String query = "SELECT * FROM project.category";
//	        List<Category> categories = getCategory(query);
//	        ObservableList<Category> categoryList = FXCollections.observableArrayList(categories);
//	       
//	        cmb.setItems(categoryList);
//	    }

//	public void showProduct() throws SQLException {
//		String query = "SELECT product_code, pname, price, wholesale_price FROM project.product";
//		ObservableList<Product> productList = getProduct(query);
//		codeCoulmn.setCellValueFactory(new PropertyValueFactory<>("product_code"));
//		pronameCoulmn.setCellValueFactory(new PropertyValueFactory<>("pname"));
//		priceCoulmn.setCellValueFactory(new PropertyValueFactory<>("price"));
//		wholeCoulmn.setCellValueFactory(new PropertyValueFactory<>("wholesale_price"));
//		companyCoulmn.setCellValueFactory(new PropertyValueFactory<>("product_company_name"));
//		quantityCoulmn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
//		categoryCoulmn.setCellValueFactory(new PropertyValueFactory<>("c_id"));
//
//		profit_tv.getItems().clear();
//
//		// Populate profitCodeCoulmn and profitCoulmn
//		for (Product product : productList) {
//			int productCode = product.getProduct_code();
//			double profit = calculateProfit(productCode);
//
//			// Create a new row data
//			Object rowData = new Object() {
//				public int getProduct_code() {
//					return productCode;
//				}
//
//				public double getProfit() {
//					return profit;
//				}
//			};
//
//		//	profit_tv.getItems().addAll(rowData);
//		}
//	}

	public void showProduct() throws SQLException {
		String query = "SELECT * FROM project.product";
		ObservableList<Product> productList = getProduct(query);
		codeCoulmn.setCellValueFactory(new PropertyValueFactory<>("product_code"));
		pronameCoulmn.setCellValueFactory(new PropertyValueFactory<>("pname"));
		wholeCoulmn.setCellValueFactory(new PropertyValueFactory<>("wholesale_price"));
		priceCoulmn.setCellValueFactory(new PropertyValueFactory<>("price"));
		companyCoulmn.setCellValueFactory(new PropertyValueFactory<>("product_company_name"));
		quantityCoulmn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		categoryCoulmn.setCellValueFactory(new PropertyValueFactory<>("c_id"));
		product_tv.setItems(productList);
		// Clear previous items in profit_tv
		profit_tv.getItems().clear();

		String query2 = "SELECT p.product_code, (p.price - p.wholesale_price) AS profit " + "FROM project.product p "
				+ "GROUP BY p.product_code";
		ObservableList<ProfitRow> profitList = getProfit(query2);

// Set profit data to the profit_tv TableView
		profitCodeCoulmn.setCellValueFactory(new PropertyValueFactory<>("productCode"));
		profitCoulmn.setCellValueFactory(new PropertyValueFactory<>("profit"));
		profit_tv.setItems(profitList);
	}

	private boolean ProductExists(int productCode) throws SQLException {
		String query = "SELECT COUNT(*) FROM project.product WHERE product_code = " + productCode;
		try (Connection connection = DBconnetion.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {
			resultSet.next();
			int count = resultSet.getInt(1);
			return count > 0;
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

	public double calculateProfit(int productCode) throws SQLException {
		String query = "SELECT price, wholesale_price FROM project.product WHERE product_code = ?";
		double profit = 0.0;

		try (Connection connection = DBconnetion.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, productCode);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					double price = resultSet.getDouble("price");
					double wholesalePrice = resultSet.getDouble("wholesale_price");

					// Calculate profit
					profit = price - wholesalePrice;
				}
			}
		} catch (SQLException e) {
			// Handle SQL exception
			e.printStackTrace();
			throw e;
		}

		return profit;
	}

	public ObservableList<ProfitRow> getProfit(String query) throws SQLException {
		ObservableList<ProfitRow> profitList = FXCollections.observableArrayList();
		Connection conn = DBconnetion.getConnection();
		java.sql.Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(query);
			ProfitRow profit;
			if (!rs.isBeforeFirst()) {
				System.out.println(" No records found for the query :" + query);
				return null;

			} else {
				while (rs.next()) {
					profit = new ProfitRow(rs.getInt("product_code"), rs.getDouble("profit"));
					profitList.add(profit);
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
		return profitList;
	}

}
