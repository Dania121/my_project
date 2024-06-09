package application;

import java.sql.Date;

import javafx.scene.control.DatePicker;

import java.sql.Connection;

public class Employee {
	private int empID;
	// private int age;
	private Date birthDate;
	private String firstName;
	private String lastName;
	private long phone;
	private String address;
	private String shopName;

	// Constructor
	public Employee(int empID, Date birthDate, String firstName, String lastName, long phone,
			String address, String shopName) {
		this.empID = empID;
		//this.age = age;
		this.birthDate = birthDate;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.address = address;
		this.shopName = shopName;
	}

	// Getters and setters for each field
	public int getEmpID() {
		return empID;
	}

	public void setEmpID(int empID) {
		this.empID = empID;
	}


	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public long getPhone() {
		return phone;
	}

	public void setPhone(long phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
}