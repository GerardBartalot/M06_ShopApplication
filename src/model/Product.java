package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "product")
@XmlType(propOrder = {"id", "name", "available", "wholesalerPrice", "publicPrice", "stock"})
public class Product {

    private int id;
    private String name;
    private Amount publicPrice;
    private Amount wholesalerPrice;
    private boolean available = true;
    private int stock;
    public static int totalProducts = 0;

    private static final double EXPIRATION_RATE = 0.60;

    public Product(String name, Amount wholesalerPrice, boolean available, int stock) {
        this.id = ++totalProducts; // Autoincremento del ID
        this.name = name;
        setWholesalerPrice(wholesalerPrice); // Configura wholesalerPrice y actualiza publicPrice
        this.available = available;
        this.stock = stock;
    }

    // Constructor vacío requerido para JAXB
    public Product() {
        this.id = ++totalProducts; // Autoincremento del ID incluso al leer desde XML
    }

    @XmlAttribute
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public Amount getPublicPrice() {
        return publicPrice;
    }

    public void setPublicPrice(Amount publicPrice) {
        this.publicPrice = publicPrice;
    }

    @XmlElement
    public Amount getWholesalerPrice() {
        return wholesalerPrice;
    }

    public void setWholesalerPrice(Amount wholesalerPrice) {
        this.wholesalerPrice = wholesalerPrice;
        this.publicPrice = new Amount(wholesalerPrice.getValue() * 2, wholesalerPrice.getCurrency()); // Calcula publicPrice como el doble de wholesalerPrice
    }

    @XmlElement
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @XmlElement
    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void expire() {
        double newValue = this.publicPrice.getValue() * EXPIRATION_RATE;
        this.publicPrice = new Amount(newValue, this.publicPrice.getCurrency());
    }

    @Override
    public String toString() {
        return "Product --> [Name = " + name + ", Public Price = " + publicPrice + ", Wholesaler Price = " + wholesalerPrice
                + ", Available = " + available + ", Stock = " + stock + "]";
    }
    
}