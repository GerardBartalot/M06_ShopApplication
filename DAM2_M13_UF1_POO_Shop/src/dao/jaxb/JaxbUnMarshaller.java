package dao.jaxb;

import model.ProductList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class JaxbUnMarshaller {

    // Método para leer el inventario desde un archivo XML
    public ProductList getInventory() {
        try {
            // Archivo de entrada XML
            File file = new File("jaxb/inputInventory.xml");

            // Crear contexto JAXB y unmarshaller
            JAXBContext jaxbContext = JAXBContext.newInstance(ProductList.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            // Unmarshal (XML a Java)
            return (ProductList) unmarshaller.unmarshal(file);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }
}
