package dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import dao.xml.DomWriter;
import dao.xml.SaxReader;
import model.Employee;
import model.Product;

public class DaoImplXml implements Dao {

    private SaxReader saxReader;
    private DomWriter domWriter;

    public DaoImplXml() {  
    	saxReader = new SaxReader();
        domWriter = new DomWriter();   
    }
    
    @Override
    public ArrayList<Product> getInventory() {        
    	
    	String filePath = System.getProperty("user.dir") + "/xml/inputInventory.xml";
        List<Product> inventory = saxReader.readInventoryFromXML(filePath);
        
        if (inventory != null && !inventory.isEmpty()) {
            System.out.println("Inventario cargado correctamente.");
        } else {
            System.out.println("El inventario está vacío o no se pudo cargar.");
        }
        
        return (ArrayList<Product>) inventory; 
    }
    
    @Override
    public boolean writeInventory(ArrayList<Product> inventory) {
        
    	boolean exportSuccess = domWriter.generateXml(inventory);
        
    	if (exportSuccess) {
            System.out.println("Inventario exportado correctamente.");
            return true;
        } else {
            System.out.println("Error al exportar el inventario.");
            return false;
        }
    }
    
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