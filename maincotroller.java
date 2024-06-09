package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.sql.Date; // Import Date class
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class maincotroller implements Initializable {

	@FXML
	private TableView<Employee> tvName;
	@FXML
	private TableColumn<Employee, Integer> colEmpID;
	@FXML
	private TableColumn<Employee, String> colFirstName;
	@FXML
	private TableColumn<Employee, String> colLastName;
	@FXML
	private TableColumn<Employee, Integer> colAge;
	//@FXML
	//private TableColumn<Employee, Integer> colSalary;

	@FXML
	private TextField empIdField;
//	@FXML
//	private TextField ageField;
//	
	
	@FXML
	private TextField firstNameField;

	@FXML
	private TextField lastNameField;

	@FXML
	private DatePicker birthDatePicker; // Add @FXML annotation
	@FXML
	private TextField phoneField; // Add @FXML annotation
	@FXML
	private TextField addressField; // Add @FXML annotation
	@FXML
	private TextField shopNameField; // Add @FXML annotation

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		showEmployee();
	}

	private void showEmployee() {
		// Create ObservableList to hold employee data
		ObservableList<Employee> employeeList = FXCollections.observableArrayList();

		// Retrieve employee data from the database and populate the ObservableList
		try (Connection connection = DBconnetion.getConnection();
				PreparedStatement statement = connection.prepareStatement("SELECT * FROM employee");
				ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
				int empID = resultSet.getInt("emp_ID");
				int age = resultSet.getInt("age");
			//	int salary = resultSet.getInt("salary");
				Date Bdate = resultSet.getDate("Bdate");
				String firstName = resultSet.getString("fname");
				String lastName = resultSet.getString("lname");
				Long phone = resultSet.getLong("phone");
				String address = resultSet.getString("address");
				String shopName = resultSet.getString("shop_name");
				
                  

				// Create Employee object and add it to the list
				
					Employee employee = new Employee(empID, Bdate, firstName, lastName, phone, address,
						shopName);

				employeeList.add(employee);
			}

			// Populate TableView with employee data
			tvName.setItems(employeeList);

		} catch (SQLException e) {
			e.printStackTrace();
			showAlert("Error retrieving employee data: " + e.getMessage());
		}
	}

	@FXML
	private void insertEmployee() {
	    try (Connection connection = DBconnetion.getConnection();
	    		PreparedStatement statement = connection.prepareStatement("INSERT INTO employee VALUES ( ?, ?, ?, ?, ?, ?, ?)")) {
	        statement.setInt(1, Integer.parseInt(empIdField.getText()));

	        LocalDate localDate = birthDatePicker.getValue();
	        if (localDate != null) { // Check if birthDatePicker is not null
	            Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
	            java.sql.Date birthDate = java.sql.Date.valueOf(localDate);
	            statement.setDate(2, birthDate);
	        } else {
	            statement.setDate(2, null); // Set birthDate to null if birthDatePicker is null
	        }

	        statement.setString(3, firstNameField.getText());
	        statement.setString(4, lastNameField.getText());
	        statement.setLong(5, Long.parseLong(phoneField.getText()));
	        statement.setString(6, addressField.getText());
	        statement.setString(7, shopNameField.getText());

	        statement.executeUpdate();
	        showAlert("Employee inserted successfully!");
	    } catch (SQLException e) {
	        e.printStackTrace();
	        showAlert("Error inserting employee: " + e.getMessage());
	    }
	}




	@FXML
	private void updateEmployee() {
	    try (Connection connection = DBconnetion.getConnection();
	         PreparedStatement statement = connection.prepareStatement(
	                 "UPDATE employee SET age = ?,salary = ?, Bdate = ? , fname = ?,Lname = ?, phone = ?, address = ?, shop _name = ? WHERE emp_ID = ?")) {
	    //    statement.setInt(1, Integer.parseInt(ageField.getText())); // Use ageField.getText() instead of colAge.getText()
	    //    statement.setInt(2, Integer.parseInt(colSalary.getText())); // Use salaryField.getText() instead of colSalary.getText()
	        LocalDate localDate = birthDatePicker.getValue();
	        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
	        Date birthDate = (Date) Date.from(instant);

	        statement.setDate(3, birthDate);
	        statement.setString(4, firstNameField.getText());
	        statement.setString(5, lastNameField.getText());
	        statement.setLong(6, Long.parseLong(phoneField.getText()));
	        statement.setString(7, addressField.getText());
	        statement.setString(8, shopNameField.getText());

	        statement.setInt(9, Integer.parseInt(empIdField.getText()));
	        int rowsUpdated = statement.executeUpdate();
	        if (rowsUpdated > 0) {
	            showAlert("Employee updated successfully!");
	        } else {
	            showAlert("No employee found with the given ID.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        showAlert("Error updating employee: " + e.getMessage());
	    }
	}

	@FXML
	private void deleteEmployee() {
		try (Connection connection = DBconnetion.getConnection();
				PreparedStatement statement = connection.prepareStatement("DELETE FROM employee WHERE emp_ID = ?")) {
			statement.setInt(1, Integer.parseInt(empIdField.getText()));
			int rowsDeleted = statement.executeUpdate();
			if (rowsDeleted > 0) {
				showAlert("Employee deleted successfully!");
			} else {
				showAlert("No employee found with the given ID.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert("Error deleting employee: " + e.getMessage());
		}
	}

	private void showAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
