/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import dao.DaoImplFile;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;

import main.Shop;
import model.Product;
import utils.*;

public class ShopView extends javax.swing.JFrame implements KeyListener {
    // Tu código existente aquí

    Shop shop;
   
    
    /**
     * Creates new form ShopView
     * @throws IOException 
     */
    public ShopView() throws IOException {
        initComponents();
        addKeyListener(this); // Agrega el KeyListener al JFrame
        setFocusable(true); // Asegura que el JFrame pueda obtener el foco para recibir eventos de teclado
        requestFocusInWindow(); // Solicita el foco en la ventana para que pueda recibir eventos de teclado automáticamente
        this.shop = new Shop();
    }
    
    public void keyTyped(KeyEvent e) {
    // No es necesario implementar este método en este caso
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_ESCAPE) {
            dispose();
        } else if (key == KeyEvent.VK_0) {
            jButton0.doClick();
        } else if (key == KeyEvent.VK_1) {
            jButton1.doClick();
        } else if (key == KeyEvent.VK_2) {
            jButton2.doClick();
        } else if (key == KeyEvent.VK_3) {
            jButton3.doClick();
        } else if (key == KeyEvent.VK_4) {
            jButton4.doClick();
        } else if (key == KeyEvent.VK_5) {
            jButton5.doClick();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // No es necesario implementar este método en este caso
    } 

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Seleccione o pulse una opción:");

        jButton0 = new javax.swing.JButton();
        jButton0.setText("0. Exportar Inventario");
        jButton0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportInventory(evt);
            }
        });
        
        jButton1.setText("1. Contar caja");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("2. Añadir producto");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("3. Añadir stock");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        
        jButton4.setText("9. Eliminar producto");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        
        jButton5 = new javax.swing.JButton();
        jButton5.setText("5. Ver Inventario");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
        layout.setHorizontalGroup(
        	    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        	    .addGroup(layout.createSequentialGroup()
        	        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        	            .addGroup(layout.createSequentialGroup()
        	                .addGap(46, 46, 46)
        	                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
        	            .addGroup(layout.createSequentialGroup()
        	                .addGap(77, 77, 77)
        	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        	                    .addComponent(jButton2)
        	                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
        	                    .addComponent(jButton3)
        	                    .addComponent(jButton5)
        	                    .addComponent(jButton4)      	                    
        	                    .addComponent(jButton0))))
        	        .addContainerGap(173, Short.MAX_VALUE))
        	);

        layout.setVerticalGroup(
        	    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        	    .addGroup(layout.createSequentialGroup()
        	        .addGap(48, 48, 48)
        	        .addComponent(jLabel1)
        	        .addGap(24, 24, 24)
        	        .addComponent(jButton0)
        	        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        	        .addComponent(jButton1)
        	        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        	        .addComponent(jButton2)
        	        .addGap(12, 12, 12)
        	        .addComponent(jButton3)
        	        .addGap(12, 12, 12)
        	        .addComponent(jButton5)
        	        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        	        .addComponent(jButton4)
        	        .addContainerGap(84, Short.MAX_VALUE))
        	);


        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exportInventory(java.awt.event.ActionEvent evt) {
        try {
            boolean exportSuccess = shop.exportInventory();

            if (exportSuccess) {
                JOptionPane.showMessageDialog(this, "Inventario exportado correctamente.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al exportar el inventario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        int option = Constants.OPTION_ADD_PRODUCT;
        this.openProductView(shop, option);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.openCashView(shop);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        int option = Constants.OPTION_ADD_STOCK;
        this.openProductView(shop, option);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        int option = Constants.OPTION_REMOVE_PRODUCT;
        this.openProductView(shop, option);
    }//GEN-LAST:event_jButton4ActionPerformed
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        shop.showInventory();
    }

    public void openCashView(Shop Shop) {
        CashView cashView = new CashView(shop);
        cashView.setVisible(true);
    }
    
    public void openProductView (Shop shop, int option) {
        ProductView productView = new ProductView(shop, option);
        productView.setVisible(true);
    }
    
    /**
     * @param args the command line arguments
     */
        
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton0;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
