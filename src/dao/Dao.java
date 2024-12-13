package dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Employee;
import model.Product;

public interface Dao {

	void connect();

	ArrayList<Product> getInventory() throws IOException;

	Employee getEmployee(int employeeId, String password);

	boolean writeInventory(ArrayList<Product> inventory) throws IOException;

	boolean addProduct(Product producto) throws IOException;

	boolean updateProduct(String name, int stock) throws IOException;

	boolean deleteProduct(Product producto) throws IOException;

	void disconnect() throws SQLException;

}
