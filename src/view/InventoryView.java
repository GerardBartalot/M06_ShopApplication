package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import model.Product;

public class InventoryView {

    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;

    /**
     * Constructor que recibe la lista de productos.
     */
    public InventoryView(ArrayList<Product> inventory) {
        initialize(inventory);
    }

    /**
     * Inicializar la interfaz gráfica con los datos del inventario.
     */
    private void initialize(ArrayList<Product> inventory) {
        frame = new JFrame();
        frame.setTitle("");
        frame.setBounds(100, 100, 800, 450);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout(20, 20));  // Aumentar espacio entre componentes

        // Panel para el título con margen
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Espaciado superior e inferior
        JLabel titleLabel = new JLabel("SHOP INVENTORY", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        frame.add(titlePanel, BorderLayout.NORTH);

        // Definir columnas de la tabla
        String[] columnNames = {"ID", "Name", "Public Price", "Wholesaler Price", "Available", "Stock",};

        // Crear el modelo de la tabla
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);  // Aumentar el espacio entre filas

        // Aumentar el espacio entre columnas
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(50);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);

        // Centrando el contenido de las celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Llenar la tabla con los datos existentes
        populateTable(inventory);

        // Agregar la tabla a un JScrollPane con margen
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Añadir márgenes alrededor de la tabla
        frame.add(scrollPane, BorderLayout.CENTER);

        // Mostrar la ventana
        frame.setVisible(true);
    }

    /**
     * Método para poblar la tabla con los datos de los productos.
     */
    private void populateTable(ArrayList<Product> inventory) {
        for (Product product : inventory) {
            Object[] row = {
                product.getId(),
                product.getName(),
                (product.getPublicPrice() != null) ? product.getPublicPrice().toString() : "N/A",
                (product.getWholesalerPrice() != null) ? product.getWholesalerPrice().toString() : "N/A",
                product.isAvailable() ? "true" : "false",
                product.getStock(),              
            };
            tableModel.addRow(row);
        }
    }
}