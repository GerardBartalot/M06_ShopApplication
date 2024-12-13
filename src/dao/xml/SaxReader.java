package dao.xml;

import model.Product;
import model.Amount;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SaxReader extends DefaultHandler {
    
    private List<Product> products = null;
    private Product product = null;
    private boolean wholesalerPrice = false;
    private boolean stock = false;

    // Método para iniciar el documento XML
    @Override
    public void startDocument() throws SAXException {
        System.out.println("Iniciando la lectura del documento XML.");
    }

    // Método para empezar a leer un elemento
    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        if (name.equalsIgnoreCase("product")) {
            String productName = attributes.getValue("name");
            product = new Product(productName, new Amount(0, "Euro"), true, 0);
            if (products == null) {
                products = new ArrayList<>();
            }
        } else if (name.equalsIgnoreCase("wholesalerPrice")) {
            wholesalerPrice = true;
            String currency = attributes.getValue("currency");
            product.setWholesalerPrice(new Amount(0, currency != null ? currency : "Euro")); // Configura la moneda
        } else if (name.equalsIgnoreCase("stock")) {
            stock = true;
        }
    }

    // Método para capturar el contenido entre las etiquetas
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String content = new String(ch, start, length).trim();
        if (wholesalerPrice) {
            try {
                double priceValue = Double.parseDouble(content);
                Amount amount = product.getWholesalerPrice(); // Recupera el objeto Amount existente
                amount.setValue(priceValue); // Actualiza el valor del precio mayorista
                product.setWholesalerPrice(amount);
            } catch (NumberFormatException e) {
                System.out.println("Error: Valor de precio mayorista no válido. Se asignará 0.");
                product.setWholesalerPrice(new Amount(0, "Euro"));
            }
            wholesalerPrice = false;
        } else if (stock) {
            try {
                product.setStock(Integer.parseInt(content));
            } catch (NumberFormatException e) {
                System.out.println("Error: Stock no válido. Se asignará 0.");
                product.setStock(0);
            }
            stock = false;
        }
    }

    // Método para finalizar un elemento
    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        if (name.equalsIgnoreCase("product")) {
            products.add(product);
        }
    }

    // Método para leer el archivo XML
    public List<Product> readInventoryFromXML(String filePath) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(new File(filePath), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }
}
