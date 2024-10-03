package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Employee;
import model.Product;

public interface Dao {

	void connect() throws SQLException;
	
	ArrayList<Product> getInventory();
	
	Employee getEmployee (int employeeId, String password);
	
	boolean writeInventory(ArrayList<Product> inventory);
	
	void disconnect() throws SQLException;
	
}
