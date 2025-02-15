package model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@Entity
@Table(name = "inventory")
public class Product {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
	@Column(name = "available")
	private boolean available = true;
   
	@Column(name = "name", nullable = false, unique = true)
	private String name;
    
	@Transient
	private double price;

	@Column(name = "stock")
	private int stock;
	
	@Transient
	private Amount publicPrice;
    
	@Column(name = "wholesaler Price")
	@Embedded
    private Amount wholesalerPrice;
    
	@Transient
	public static int totalProducts = 0;

    private static final double EXPIRATION_RATE = 0.60;

    public Product(String name, Amount wholesalerPrice, boolean available, int stock) {
    	this.id = ++totalProducts;
    	this.name = name; 
        this.wholesalerPrice = wholesalerPrice;
        this.available = available; 
        this.stock = stock;
    }
    
    public Product(boolean available, String name, double price, int stock) {
        this.id = ++totalProducts;
        this.name = name;
        this.price = price;
        this.wholesalerPrice = new Amount(price, "€");
        this.publicPrice = new Amount(price * 2, "€");
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
    
    public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
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
        return "Product --> [Id = " + id + ", Name = " + name + ", Public Price = " + publicPrice + ", Wholesaler Price = " + wholesalerPrice
                + ", Available = " + available + ", Stock = " + stock + "]";
    }

	public String getCreatedAt() {
		// TODO Auto-generated method stub
		return null;
	}

	public double getProductId() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Embeddable
	class WholesalerPrice {
	    private double value;
	    private String currency;
	    
	    public WholesalerPrice() {}
	    
	    public WholesalerPrice(double value, String currency) {
	        this.value = value;
	        this.currency = currency;
	    }
	    
	    @XmlElement
	    public double getValue() {
	        return value;
	    }
	    
	    public void setValue(double value) {
	        this.value = value;
	    }
	    
	    @XmlElement
	    public String getCurrency() {
	        return currency;
	    }
	    
	    public void setCurrency(String currency) {
	        this.currency = currency;
	    }
	    
	    @Override
	    public String toString() {
	        return "WholesalerPrice [value=" + value + ", currency=" + currency + "]";
	    }
	}
    
}