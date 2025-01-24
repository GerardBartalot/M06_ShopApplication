package dao;

import model.Amount;
import model.Product;
import model.ProductHistory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DaoImplHibernate implements Dao {

    private SessionFactory sessionFactory;

    public DaoImplHibernate() {
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al configurar Hibernate");
        }
    }

    @Override
    public void connect() {
        if (sessionFactory == null) {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        }
    }

    @Override
    public void disconnect() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }

    @Override
    public ArrayList<Product> getInventory() throws IOException {
        try (Session session = sessionFactory.openSession()) {
            List<Product> productList = session.createQuery("FROM Product", Product.class).list();

            if (productList.isEmpty()) {
                System.out.println("Inventario vacío");
                
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

                productList = session.createQuery("FROM Product", Product.class).list();               
            }

            return new ArrayList<>(productList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Error al obtener el inventario");
        }
    }


    @Override
    public boolean addProduct(Product product) throws IOException {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(product);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            throw new IOException("Error al agregar el producto");
        }
    }

    @Override
    public boolean updateProduct(String name, int stock) throws IOException {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Product product = session.createQuery("FROM Product WHERE name = :name", Product.class)
                                    .setParameter("name", name)
                                    .uniqueResult();
            if (product != null) {
                product.setStock(stock);
                session.update(product);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            throw new IOException("Error al actualizar el producto");
        }
    }

    @Override
    public boolean deleteProduct(Product product) throws IOException {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.delete(product);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            throw new IOException("Error al eliminar el producto");
        }
    }

    @Override
    public boolean writeInventory(ArrayList<Product> inventory) throws IOException {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            for (Product product : inventory) {
                ProductHistory history = new ProductHistory(
                    product.isAvailable(),
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getStock()
                );
                session.save(history);
            }
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error en writeInventory: " + e.getMessage());
            throw new IOException("Error al exportar el inventario", e);
        }
    }

    @Override
    public model.Employee getEmployee(int employeeId, String password) {
        return null;
    }
}