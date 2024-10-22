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
    private boolean WholesalerPrice = false;
    private boolean Stock = false;

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
            WholesalerPrice = true;
        } else if (name.equalsIgnoreCase("stock")) {
            Stock = true;
        }
    }

    // Método para capturar el contenido entre las etiquetas
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String content = new String(ch, start, length).trim();
        if (WholesalerPrice) {
            product.setWholesalerPrice(new Amount(Double.parseDouble(content), "Euro"));
            WholesalerPrice = false;
        } else if (Stock) {
            product.setStock(Integer.parseInt(content));
            Stock = false;
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
