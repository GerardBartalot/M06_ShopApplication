package main;

import java.awt.EventQueue;
import model.Amount;
import model.Client;
import model.Employee;
import model.Product;
import model.Sale;
import view.LoginView;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import dao.Dao;
import dao.DaoImplFile;
import dao.DaoImplHibernate;
import dao.DaoImplJDBC;
import dao.DaoImplJaxb;
import dao.DaoImplXml;
import utils.*;
import view.CashView;
import view.InventoryView;
import view.ProductView;

public class Shop {
    
    private Amount cash = new Amount(100.00, "€");
    private ArrayList<Product> inventory;
    private ArrayList<Sale> sales;
    int sale_num = 0;
    public DaoImplHibernate dao;

    final static double TAX_RATE = 1.04;

    public Shop() throws IOException {
        cash = new Amount(50.0, "€");
        inventory = new ArrayList<>();
        sales = new ArrayList<>();
        dao = new DaoImplHibernate();
        dao.connect();
        loadInventory();
        readInventory();
        
    }

    public static void main(String[] args) throws IOException {
        
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoginView frame = new LoginView();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        
        boolean loggedIn = initSession();

        if (loggedIn) {
            Shop shop = new Shop();

            Scanner scanner = new Scanner(System.in);
            int opcion = 0;
            boolean exit = false;

            do {
                System.out.println("\n");
                System.out.println("===========================");
                System.out.println("Menu principal miTienda.com");
                System.out.println("===========================");
                System.out.println("0) Exportar inventario");
                System.out.println("1) Contar caja");
                System.out.println("2) Añadir producto");
                System.out.println("3) Añadir stock");
                System.out.println("4) Marcar producto proxima caducidad");
                System.out.println("5) Ver inventario");
                System.out.println("6) Venta");
                System.out.println("7) Ver ventas");
                System.out.println("8) Salir del programa");
                System.out.println("9) Eliminar producto");
                System.out.println("10) Ventas totales");
                System.out.print("Seleccione una opción: ");
                opcion = scanner.nextInt();

                switch (opcion) {
                    
                	/* case 1:
                        shop.getCash(shop);
                        break; */

                    case 2:
                        ProductView addProductView = new ProductView(shop, Constants.OPTION_ADD_PRODUCT);
                        addProductView.setVisible(true);
                        break;

                    case 3:
                        ProductView addStockView = new ProductView(shop, Constants.OPTION_ADD_STOCK);
                        addStockView.setVisible(true);
                        break;

                    case 4:
                        shop.setExpired();
                        break;

                    case 5:
                        shop.showInventory();
                        break;

                    case 6:
                        shop.sale();
                        break;

                    case 7:
                        shop.showSales();
                        Scanner exportScanner = new Scanner(System.in);
                        System.out.print("¿Desea exportar las ventas a un archivo? (Sí/No): ");
                        String exportChoice = exportScanner.nextLine();
                        if (exportChoice.equalsIgnoreCase("Sí") || exportChoice.equalsIgnoreCase("Si")) {
                            shop.writeInventory();
                        }
                        break;

                    case 8:
                        System.out.println("Saliendo del programa.");
                        exit = true;
                        break;

                    case 9:
                        ProductView removeProductView = new ProductView(shop, Constants.OPTION_REMOVE_PRODUCT);
                        removeProductView.setVisible(true);
                        break;

                    case 10:
                        shop.totalAmountSales();
                        break;
                }

            } while (!exit);
        } else {
            System.out.println("Inicio de sesión fallido. Por favor, inténtelo de nuevo más tarde.");
        }
    }

    public static boolean initSession() {
        Scanner scanner = new Scanner(System.in);
        DaoImplHibernate dao = new DaoImplHibernate(); // Crear instancia de DaoImplHibernate
        dao.connect(); // Conectar a la base de datos

        boolean loggedIn = false;
        try {
            while (!loggedIn) {
                int employeeId = scanner.nextInt();
                String password = scanner.next();

                Employee employee = dao.getEmployee(employeeId, password);

                if (employee != null) {
                    loggedIn = true;
                    System.out.println("Inicio de sesión exitoso. Bienvenido, empleado " + employeeId + "!");
                } else {
                    System.out.println("Credenciales incorrectas. Por favor, inténtelo de nuevo.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.disconnect();
        }

        return loggedIn;
    }

    public double getCashValue(){
        return cash.getValue();
    }

    /**
     * load initial inventory to shop
     */
	/**
	 * load initial inventory to shop
	 */
	/* public void loadInventory() {

		// Aquí Buscamos el Archivo en su Ubicación
		
	    File inventoryFile = new File(System.getProperty("user.dir") + File.separator + "files2/inputInventory.txt");
	    File newFolder = new File("resources");

	    if (!newFolder.exists()) {
	        newFolder.mkdir();
	    }

	    if (!inventoryFile.exists()) {
	        try {
	            inventoryFile.createNewFile();
	        } catch (IOException e) {
	            System.err.println("Error al crear el archivo de inventario.");
	            e.printStackTrace();
	            return;
	        }
	    }

	    if (inventoryFile.canRead()) {
	        readInventory();
	    } else {
	        System.out.println("NO SE PUEDE LEER EL ARCHIVO");
	    }

	} */

	
    /**
     * Cargar inventario inicial desde el archivo inputInventory.txt.
     * @throws IOException 
     */
    
	// Aquí Leemos el Fichero
	
    public void readInventory() throws IOException {
        this.setInventory(dao.getInventory());      
        }

	private void setInventory(ArrayList<Product> inventory) {
		this.inventory=inventory;		
	}

	/**
     * Exporta los datos de las ventas a un archivo con el formato especificado.
	 * @throws IOException 
     */
	
	// Aquí Escribimos las Ventas en el Fichero
	
	public boolean writeInventory() throws IOException {
        return dao.writeInventory(this.inventory);
    }
	
	

	/**
	 * add a new product to inventory getting data from console
	 */
	public boolean addProduct(String name, int stock, double wholeSalerPrice) {
        if (isInventoryFull()) {
            return false; // Indicar que no se puede añadir más productos
        }

        if (findProduct(name) != null) {
            return false; // El producto ya existe
        }

        inventory.add(new Product(name, new Amount(wholeSalerPrice, "€"), true, stock));
        return true; // Producto añadido correctamente
    }

	/**
	 * add stock for a specific product
	 */
	public boolean addStock(String name, int stock) {
            Product product = findProduct(name);
            if (product != null) {
                product.setStock(product.getStock() + stock);
                return true; // Stock añadido correctamente
            }
            return false; // Producto no encontrado
        }

	/**
	 * set a product as expired
	 */
	private void setExpired() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Seleccione un nombre de producto: ");
		String name = scanner.next();

		Product product = findProduct(name);

	    if (product != null) {
	        Amount precioInicial = product.getPublicPrice();
	        product.expire();
	        Amount precioActualizado = product.getPublicPrice();
	        System.out.println("El precio del producto " + name + " ha sido actualizado de " + precioInicial + " a " + precioActualizado);
	    } else {
	        System.out.println("Producto no encontrado.");
	    }
	}

	/**
	 * show all inventory
	 */
	public void showInventory() {
	    SwingUtilities.invokeLater(() -> {
	        new InventoryView(inventory);
	    });
	}



	/**
	 * make a sale of products to a client
	 */

	public void sale() {
	    // Pedir el nombre del cliente
	    Scanner sc = new Scanner(System.in);
	    System.out.println("Realizar venta, escribir nombre cliente:");
	    String clientName = sc.nextLine();

	    // Crear un objeto Client con los valores predeterminados
	    Client client = new Client(clientName);

	    // Sale de productos hasta que el nombre ingresado no sea "0"
	    double totalAmount = 0.0;
	    String productName = "";
	    int productNum = 0;
	    LocalDateTime saleDateTime = LocalDateTime.now();

	    Product[] products = new Product[10];

	    while (!productName.equals("0")) {
	        System.out.println("Introduce el nombre del producto, escribir 0 para terminar:");
	        productName = sc.nextLine();

	        if (productName.equals("0")) {
	            break;
	        }

	        Product product = findProduct(productName);
	        boolean productAvailable = false;

	        if (product != null && product.isAvailable()) {
	            productAvailable = true;
	            products[productNum] = product;
	            productNum++;
	            totalAmount += product.getPublicPrice().getValue();
	            product.setStock(product.getStock() - 1);
	            // Si no hay más stock, establecer como no disponible para la venta
	            if (product.getStock() == 0) {
	                product.setAvailable(false);
	            }
	            System.out.println("Producto añadido con éxito");
	        }

	        if (!productAvailable) {
	            System.out.println("Producto no encontrado o sin stock");
	        }
	    }

	    // Mostrar el costo total
	    if (totalAmount > 0) {
	        totalAmount = totalAmount * TAX_RATE;

	        // Realizar el pago al cliente
	        boolean paymentStatus = client.pay(new Amount(totalAmount, "EUR"));

	        sales.add(new Sale(client, new ArrayList<>(Arrays.asList(products)), new Amount(totalAmount, "EUR"), saleDateTime));
	        sale_num++;
	        cash.setValue(cash.getValue() + totalAmount);
	        System.out.println("Venta realizada con éxito, total: " + totalAmount + "€.");
	        System.out.println("Fecha y hora de la venta: " + saleDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))); // Mostrar la fecha y hora de la venta
	        if (paymentStatus) {
	            // Si el pago es exitoso, continuar con la venta
	        	//public Sale(String client, ArrayList<Product> products, double amount) {
	        	//public Sale(Client client, ArrayList<Product> products, Amount amount) {
	        } else {
	            // Si el pago no es exitoso, mostrar el mensaje de deuda	        	
	            System.out.println("El cliente debe " + client.getBalance());
	        }
	    } else {
	        System.out.println("No se ha realizado ninguna venta");
	    }
	}




	/**
	 * show all sales
	 */
	private void showSales() {
	    System.out.println("\nLISTADO DE VENTAS");
	    for (Sale sale : sales) {
	        if (sale != null) {
	            System.out.println(sale);
	            System.out.println("Fecha y hora: " + sale.getFormattedSaleDateTime()); // Mostrar la fecha y hora de la venta
	        }
	    }
	}

	/**
	 * add a product to inventory
	 * 
	 * @param product
	 * @throws IOException 
	 */
	public void addProduct(Product product) throws IOException {
		int idToAdd = 0;
	    if (isInventoryFull()) {
	        return;
	    }
	    for(Product producto : inventory){
        	idToAdd = producto.getId() + 1;
        }
	    product.setId(idToAdd);
	    inventory.add(product);
	    dao.addProduct(product);
	    
	}


	/**
	 * check if inventory is full or not
	 */
	public boolean isInventoryFull() {
	    return inventory.size() >= 20;
	}

	/**
	 * find product by name
	 * 
	 * @param product name
	 */
	public Product findProduct(String name) {
	    for (Product product : inventory) {
	        if (product != null && product.getName().equalsIgnoreCase(name)) {
	            return product;
	        }
	    }
	    return null;
	}

	
	/**
	 * Remove a product from inventory.
	 * @throws IOException 
	 */
	public boolean removeProduct(String name) throws IOException {
	    Product product = findProduct(name);
            if (product != null) {
                inventory.remove(product);
                dao.deleteProduct(product);
                return true;
            }
            return false;
	}

	    
	 /**
	  * new method: totalAmountSales
	  */
	    
	public void totalAmountSales() {
	    double totalSales = 0.0;
	    for (Sale sale : sales) {
	        if (sale != null && sale.getAmount() != null) {
	            totalSales += sale.getAmount().getValue();
	        }
	    }
	    System.out.println("\nImporte total de las ventas: " + totalSales + "€");
	}

    public void openCashView(Shop shop) {
       CashView cashView = new CashView(shop);
       //cashView.setDefaultCloseOperation(JDialog.DISPONE_ON_CLOSE);
       cashView.setVisible(true);
    }
    
    private void loadInventory() throws IOException {
        if (inventory == null) { // Cargar solo si no está cargado
            inventory = dao.getInventory();
        }
    }

    public boolean exportInventory() throws IOException {
        loadInventory();
        return dao.writeInventory(inventory);
    }  

}