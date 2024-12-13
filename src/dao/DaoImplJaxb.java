package dao;

import dao.jaxb.JaxbMarshaller;
import dao.jaxb.JaxbUnMarshaller;
import model.Employee;
import model.Product;
import model.ProductList;

import java.io.IOException;
import java.util.ArrayList;

public class DaoImplJaxb implements Dao {

    private final JaxbUnMarshaller unmarshaller = new JaxbUnMarshaller();
    private final JaxbMarshaller marshaller = new JaxbMarshaller();

    @Override
    public ArrayList<Product> getInventory() {
        ArrayList<Product> products = new ArrayList<>();
        ProductList productList = unmarshaller.getInventory();

        if (productList != null) {
            products = new ArrayList<>(productList.getProducts());
            System.out.println("Inventario cargado desde: jaxb/inputInventory.xml.");
        } else {
            System.out.println("El archivo de inventario está vacío o no se pudo leer.");
        }
        return products;
    }

    @Override
    public boolean writeInventory(ArrayList<Product> inventory) throws IOException {
        ProductList productList = new ProductList(inventory);

        try {
            marshaller.writeInventory(productList);
            return true;
        } catch (Exception e) {
            throw new IOException("Error al intentar exportar el inventario en la ruta especificada.", e);
        }
    }

    @Override
    public void connect() {
    }

    @Override
    public void disconnect() {
    }

    @Override
    public Employee getEmployee(int employeeId, String password) {
        return null;
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