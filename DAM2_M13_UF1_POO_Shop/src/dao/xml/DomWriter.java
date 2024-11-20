package dao.xml;

import model.Product;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
            rootElement.setAttribute("total", String.valueOf(Product.totalProducts));
            doc.appendChild(rootElement);

            for (Product product : productList) {
                // Crear el elemento con el nombre del producto
                Element productElement = doc.createElement("product");

                // Usar el id del producto en lugar de un contador local
                productElement.setAttribute("id", String.valueOf(product.getId())); 
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

            // Crear la ruta dinámica para el archivo
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String filePath = System.getProperty("user.dir") + "/xml/inventory_" + currentDate + ".xml";

            // Asegurarse de que el directorio xml exista
            File xmlDir = new File(System.getProperty("user.dir") + "/xml");
            if (!xmlDir.exists()) {
                xmlDir.mkdirs();
            }

            // Escribir el archivo
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));

            transformer.transform(source, result);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
