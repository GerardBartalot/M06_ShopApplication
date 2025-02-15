package dao;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import model.Amount;
import model.Employee;
import model.Product;
import model.ProductHistory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import java.util.Date;

public class DaoImplMongoDB implements Dao {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> inventoryCollection;
    private MongoCollection<Document> historicalInventoryCollection;
    private MongoCollection<Document> usersCollection;

    public DaoImplMongoDB() {
        connect();
    }

    @Override
    public void connect() {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("shop");
        inventoryCollection = database.getCollection("inventory");
        historicalInventoryCollection = database.getCollection("historical_inventory");
        usersCollection = database.getCollection("users");
    }

    @Override
    public void disconnect() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    @Override
    public ArrayList<Product> getInventory() throws IOException {
        ArrayList<Product> inventory = new ArrayList<>();
        for (Document doc : inventoryCollection.find()) {
            Document wholesalerPriceDoc = doc.get("wholesalerPrice", Document.class);
            double price = wholesalerPriceDoc != null ? wholesalerPriceDoc.getDouble("value") : 0.0;
            String currency = wholesalerPriceDoc != null ? wholesalerPriceDoc.getString("currency") : "€";
            
            Amount wholesalerPrice = new Amount(price, currency);
            Product product = new Product(
                    doc.getString("name"),
                    wholesalerPrice,
                    doc.getBoolean("available", true),
                    doc.getInteger("stock", 0)
                );
            product.setPublicPrice(new Amount(wholesalerPrice.getValue() * 2, currency));
            
            inventory.add(product);
        }

        if (inventory.isEmpty()) {
            System.out.println("Inventario vacío. Añadiendo productos por defecto.");
            addProduct(new Product(true, "Manzana", 10.00, 10));
            addProduct(new Product(true, "Pera", 20.00, 20));
            addProduct(new Product(true, "Hamburguesa", 30.00, 30));
            addProduct(new Product(true, "Fresa", 5.00, 20));
            addProduct(new Product(true, "Melocotón", 20.00, 10));
            addProduct(new Product(true, "Mango", 20.00, 20));
            addProduct(new Product(true, "Aguacate", 30.00, 30));
            addProduct(new Product(true, "Cereza", 15.00, 20));
            addProduct(new Product(true, "Naranja", 20.00, 10));
            addProduct(new Product(true, "Kiwi", 30.00, 20));
            return inventory;
        }

        return inventory;
    }

    @Override
    public boolean addProduct(Product product) throws IOException {
        Document wholesalerPriceDoc = new Document("value", product.getWholesalerPrice().getValue())
                .append("currency", product.getWholesalerPrice().getCurrency());
        
        Document doc = new Document()
                .append("name", product.getName())
                .append("wholesalerPrice", wholesalerPriceDoc)
                .append("available", product.isAvailable())
                .append("stock", product.getStock())
                .append("id", product.getId());
        
        inventoryCollection.insertOne(doc);
        return true;
    }

    @Override
    public boolean updateProduct(String name, int stock) throws IOException {
        Document updateResult = inventoryCollection.findOneAndUpdate(
            Filters.eq("name", name),
            Updates.set("stock", stock)
        );
        return updateResult != null;
    }

    @Override
    public boolean deleteProduct(Product product) throws IOException {
        Document deleteResult = inventoryCollection.findOneAndDelete(Filters.eq("name", product.getName()));
        return deleteResult != null;
    }

    @Override
    public boolean writeInventory(ArrayList<Product> inventory) throws IOException {
        if (inventory.isEmpty()) {
            return false;
        }
        
        // Verificar si el inventario ya ha sido exportado previamente
        long existingRecords = historicalInventoryCollection.countDocuments();
        if (existingRecords > 0) {
        	JOptionPane.showMessageDialog(null, "Error al exportar el inventario.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        List<Document> documents = new ArrayList<>();
        for (Product product : inventory) {
            Document wholesalerPriceDoc = new Document("value", product.getWholesalerPrice().getValue())
                    .append("currency", product.getWholesalerPrice().getCurrency());
            
            Document doc = new Document()
                    .append("id", product.getId())
                    .append("name", product.getName())
                    .append("wholesalerPrice", wholesalerPriceDoc)
                    .append("available", product.isAvailable())
                    .append("stock", product.getStock())                    
                    .append("created_at", new Date());
            
            documents.add(doc);
        }
        
        try {
            if (!documents.isEmpty()) {
                historicalInventoryCollection.insertMany(documents);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Error al exportar el inventario.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al exportar el inventario.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public Employee getEmployee(int employeeId, String password) {
        // Buscar el usuario en la base de datos verificando ID y contraseña al mismo tiempo
        Document userDoc = usersCollection.find(
            Filters.and(
                Filters.eq("employeeId", employeeId),
                Filters.eq("employeePassword", password)
            )
        ).first();

        // Si no se encuentra, significa que la combinación de usuario y contraseña es incorrecta
        if (userDoc == null) {
            return null;  // No mostramos el mensaje aquí, lo manejamos en LoginView
        }

        // Si el usuario existe y la contraseña es correcta, permitir el acceso
        return new Employee(
                userDoc.getInteger("employeeId"),
                userDoc.getString("employeeName"),
                userDoc.getString("employeePassword")
        );
    }
    
    private int getNextProductId() {
        Document lastProduct = inventoryCollection.find().sort(new Document("id", -1)).first();
        return (lastProduct != null) ? lastProduct.getInteger("id") + 1 : 1;
    }
 
    
}