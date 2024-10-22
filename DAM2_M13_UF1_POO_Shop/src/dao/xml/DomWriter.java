package dao.xml;

import model.Product;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DomWriter {

    // Método para generar el documento XML (estructura en memoria)
    public Document generateDocument(List<Product> productList) {
        Document doc = null;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.newDocument();

            Element rootElement = doc.createElement("products");
            rootElement.setAttribute("total", String.valueOf(productList.size())); // Atributo total
            doc.appendChild(rootElement);

            int id = 1;
            for (Product product : productList) {
                // Crear el elemento con el nombre del producto
                Element productElement = doc.createElement("product");
                productElement.setAttribute("id", String.valueOf(id++));
                rootElement.appendChild(productElement);

                // Nombre del producto dentro del elemento <name>
                Element nameElement = doc.createElement("name");
                nameElement.appendChild(doc.createTextNode(product.getName()));
                productElement.appendChild(nameElement);

                // Precio del producto con el atributo currency
                Element priceElement = doc.createElement("price");
                priceElement.setAttribute("currency", "€");
                priceElement.appendChild(doc.createTextNode(String.valueOf(product.getWholesalerPrice().getValue())));
                productElement.appendChild(priceElement);

                // Stock del producto
                Element stockElement = doc.createElement("stock");
                stockElement.appendChild(doc.createTextNode(String.valueOf(product.getStock())));
                productElement.appendChild(stockElement);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return doc;
    }

    // Método para generar el archivo XML
    public boolean generateXml(List<Product> productList) {
    	
    	 try {
            Document doc = generateDocument(productList);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("C:\\\\Users\\\\gerar\\\\git\\\\repository2\\\\DAM2_M13_UF1_POO_Shop\\\\files\\\\inventory_" + currentDate + ".xml"));

            transformer.transform(source, result);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    
}