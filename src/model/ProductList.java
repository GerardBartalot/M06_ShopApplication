package model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.List;

@XmlRootElement(name = "products")
public class ProductList {

    private List<Product> products;

    public ProductList() {
    }

    public ProductList(List<Product> products) {
        this.products = products;
    }

    @XmlElement(name = "product")
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @XmlAttribute(name = "total")
    public int getTotalProducts() {
        return products != null ? products.size() : 0;
    }
}
