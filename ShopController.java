package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class ShopController implements Initializable {

	@FXML
	private TableColumn<Shop, String> addressCoulmn;

	@FXML
	private Button deleteBtn;

	@FXML
	private Button insertBtn;

	@FXML
	private TableColumn<Shop, String> nameCoulmn;

	@FXML
	private Label shopAddressLbl;

	@FXML
	private TextField shopAdressTxt;

	@FXML
	private Label shopLbl;

	@FXML
	private Label shopNameLbl;

	@FXML
	private TextField shopNameTxt;

	@FXML
	private AnchorPane shop_pane;

	@FXML
	private TableView<Shop> shop_tv;

	@FXML
	private Button updateBtn;

	@FXML
	void deleteShop(ActionEvent event) {

	}

	@FXML
	void insertShop(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		if (shopNameTxt.getText().isEmpty() || shopAdressTxt.getText().isEmpty()) {
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Please fill all the fields");
			alert.showAndWait();
			return; // Exit the method early if any text field is empty

		}
		try (Connection connection = DBconnetion.getConnection();
				PreparedStatement statement = connection.prepareStatement("INSERT INTO shop VALUES (?, ?)")) {
			statement.setString(1, shopNameTxt.getText());
			statement.setString(2, shopAdressTxt.getText());

			statement.executeUpdate();
			showShops();

			alert.setTitle("Done");
			alert.setHeaderText(null);
			alert.setContentText("The shop inserted successfully");
			alert.showAndWait();
		} catch (SQLException e) {

			if (e.getMessage().contains("Duplicate entry")) {
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("The entered shop name already exists");
				alert.showAndWait();
			} else {
				e.printStackTrace();
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("An error occurred while inserting the shop");
				alert.showAndWait();

			}
		}

	}

	@FXML
	void updateShop(ActionEvent event) {

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

	public void showShops() throws SQLException {
		String query = "SELECT * FROM project.shop";
		ObservableList<Shop> shopList = getShop(query);
		nameCoulmn.setCellValueFactory(new PropertyValueFactory<>("shop_name"));
		addressCoulmn.setCellValueFactory(new PropertyValueFactory<>("address"));
		shop_tv.setItems(shopList);

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			showShops();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
