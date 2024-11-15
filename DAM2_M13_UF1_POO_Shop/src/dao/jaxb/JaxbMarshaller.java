package dao.jaxb;

import model.ProductList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JaxbMarshaller {

    public void writeInventory(ProductList products) throws IOException {
        
    	try {
            // Generar el nombre del archivo con la fecha actual
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String fileName = "jaxb/inventory_" + date + ".xml";
            File file = new File(fileName);

            // Verificar si el directorio de destino existe; si no, lanzar excepción
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                throw new IOException("El directorio de destino no existe: " + parentDir.getAbsolutePath());
            }

            // Crear el contexto JAXB y el marshaller
            JAXBContext jaxbContext = JAXBContext.newInstance(ProductList.class);
            Marshaller marshaller = jaxbContext.createMarshaller();

            // Configurar el marshaller para formato legible
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Marshal (Java a XML) y escribir en el archivo
            marshaller.marshal(products, file);

            System.out.println("Inventario exportado a: " + fileName);
        } catch (JAXBException e) {
            throw new IOException("Error al exportar el inventario a XML", e);
        }
    	
    }
    
}