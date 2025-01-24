package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import model.Amount;
import model.Employee;
import model.Product;
import model.Sale;

public class DaoImplFile implements Dao{

	private Connection connection;

	@Override
	public void connect() {
	}
	
	@Override
	public Employee getEmployee(int employeeId, String password) {
		return null;
	}

	@Override
	public void disconnect() throws SQLException {
	}

	@Override
	public ArrayList<Product> getInventory() {
			
		File file = new File(System.getProperty("user.dir") + File.separator + "files2/inputInventory.txt");
		ArrayList<Product>productos=new ArrayList<>();
		
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;

			while ((line = br.readLine()) != null) {
			     String[] parts = line.split(";");
			     String name = parts[0], priceString = parts[1].trim(), stockString = parts[2].trim();   

			     // Verificar que las cadenas no estén vacías antes de convertirlas
			        
			     if (!priceString.isEmpty() && !stockString.isEmpty()) {
			         double price = Double.parseDouble(priceString);
			         int stock = Integer.parseInt(stockString);
			         productos.add(new Product(name, new Amount(price, "€"), true, stock));
			     } else {
			         // Tratar el caso de que falten datos necesarios
			         System.err.println("Faltan datos para el producto " + name);
			     }
			 }

			 br.close();
			 fr.close();
			
		} catch (IOException e) {
			 System.err.println("Error al cargar el inventario desde el archivo.");
			 e.printStackTrace();
		}
	return productos;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {

	    // Establecer la ruta de la carpeta por defecto
	    File newFolder = new File(System.getProperty("user.dir") + File.separator + "files");

	    // Si la carpeta no existe, solicitar el nombre de una nueva carpeta al usuario
	    if (!newFolder.exists()) {
	        String folderName = JOptionPane.showInputDialog(null, "La carpeta por defecto no existe. Introduzca el nombre de la nueva carpeta:");
	        
	        // Si el usuario introduce un nombre, creamos la nueva carpeta
	        if (folderName != null && !folderName.trim().isEmpty()) {
	            newFolder = new File(System.getProperty("user.dir") + File.separator + folderName);
	            if (!newFolder.exists()) {
	                // Usar mkdirs para crear todos los directorios necesarios
	                if (!newFolder.mkdirs()) {
	                    JOptionPane.showMessageDialog(null, "Error al crear la carpeta. Operación cancelada.");
	                    return false;
	                }
	            }
	        } else {
	            // Si el usuario cancela o no introduce un nombre válido, salir del método
	            JOptionPane.showMessageDialog(null, "No se ha proporcionado un nombre de carpeta válido. Cancelando operación.");
	            return false;
	        }
	    }

	    // Obtener la fecha actual y formatearla como yyyy-mm-dd
	    LocalDate currentDate = LocalDate.now();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    String formattedDate = currentDate.format(formatter);

	    // Establecer el nombre del archivo de inventario
	    File inventoryFile = new File(newFolder, "inventory_" + formattedDate + ".txt");  

	    // Verificar si el archivo no existe y crearlo
	    if (!inventoryFile.exists()) {
	        try {
	            inventoryFile.createNewFile();
	        } catch (IOException e) {
	            e.printStackTrace();
	            JOptionPane.showMessageDialog(null, "Error al crear el archivo de inventario.");
	            return false;
	        }
	    }

	    // Escribir el inventario en el archivo
	    try (FileWriter fw = new FileWriter(inventoryFile, false);
	         PrintWriter pw = new PrintWriter(fw)) {

	        // Iterar sobre el inventario y escribir en el archivo
	        for (Product product : inventory) {
	            pw.println("Product:" + product.getName() +
	                ";Stock:" + product.getStock() + ";");
	        }      
	        pw.println("\n" + "Número total de productos:" + inventory.size());

	        // Si tiene éxito, devolver true
	        return true;

	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	@Override
	public boolean addProduct(Product producto) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateProduct(String name, int stock) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteProduct(Product producto) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}


}