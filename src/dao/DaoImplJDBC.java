package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import model.Amount;
import model.Employee;
import model.Product; 

public class DaoImplJDBC implements Dao {

	private Connection connection;

	// Método para Connectar con la Base de Datos
	@Override
	public void connect() {
		// Define connection parameters
		String url = "jdbc:mysql://localhost:3306/shop";
		String user = "root";
		String pass = "";
		try {
			this.connection = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Override
	public Employee getEmployee(int employeeId, String password) {
		Employee employee = null;
		// prepare query
		String query = "select * from employee where employeeId = ? ";
		
		try (PreparedStatement ps = connection.prepareStatement(query)) { 
			// set id to search for
			ps.setInt(1,employeeId);
		  	//System.out.println(ps.toString());
	        try (ResultSet rs = ps.executeQuery()) {
	        	if (rs.next()) {
	        		employee =  new Employee(rs.getInt(1), rs.getString(3));            		            				
	        	}
	        }
	    } catch (SQLException e) {
			// in case error in SQL
			e.printStackTrace();
		}
		return employee;
	}

	@Override
	public void disconnect() throws SQLException {
		if (connection != null) {
			connection.close();
		}
	}

	@Override
	public ArrayList<Product> getInventory() throws IOException {
		ArrayList<Product> productList = new ArrayList<>();
		Amount wholesalerPrice = new Amount();
		String query = "select * from inventory";
		try (Statement ps = connection.createStatement()) {
			try (ResultSet rs = ps.executeQuery(query)) {
				// for each result add to list
				while (rs.next()) {
					// get data for each person from column
					wholesalerPrice = new Amount(rs.getInt(3), "€");
					Product product = new Product(rs.getString(2), wholesalerPrice,rs.getBoolean(4), rs.getInt(5));
					product.setId(rs.getInt(1));
					productList.add(product);
					
					
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return productList;

	}
	@Override
	public boolean writeInventory(ArrayList<Product> getInventory) {
 String query = "INSERT INTO historical_inventory (id_producto, name, wholesalerPrice, available, stock, created_at) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";		try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			for (Product product : getInventory) {
				LocalDateTime currentDateTime = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 																				
				String formattedDateTime = currentDateTime.format(formatter);
				
				preparedStatement.setInt(1, product.getId());
				preparedStatement.setString(2, product.getName());
				preparedStatement.setDouble(3, product.getWholesalerPrice().getValue());
				preparedStatement.setBoolean(4, product.isAvailable());
				preparedStatement.setInt(5, product.getStock());
				preparedStatement.setString(6, formattedDateTime);
				preparedStatement.executeUpdate();
			}
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}


	@Override
	public boolean addProduct(Product producto) throws IOException {
		String query = "INSERT INTO inventory (id, name, wholesalerPrice, available, stock) "
				+ "VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, producto.getId());			
			preparedStatement.setString(2, producto.getName());
			preparedStatement.setDouble(3, producto.getWholesalerPrice().getValue());
			preparedStatement.setBoolean(4, producto.isAvailable());
			preparedStatement.setInt(5, producto.getStock());
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return false;
	}


	@Override
	public boolean updateProduct(String name, int stock) throws IOException {
		String query = "UPDATE inventory SET stock = ? WHERE name = ?";

		try (PreparedStatement updateStatement = connection.prepareStatement(query)) {
			updateStatement.setInt(1,stock);
			updateStatement.setString(2,name);
			updateStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	
	}


	@Override
	public boolean deleteProduct(Product producto) throws IOException {
	String query= "DELETE FROM inventory WHERE name = ?";
		try (PreparedStatement deleteStatement = connection.prepareStatement(query)) {
			deleteStatement.setString(1, producto.getName()); 
			deleteStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return false;	}

	
}
