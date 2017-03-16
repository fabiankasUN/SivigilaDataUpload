/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Fabian
 */
public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        properties();
    }
    public void properties(){
        BackGround fondo=new BackGround(359, 307, "Fondo4.png", 0, 0);
        setResizable(false);
        setLocationRelativeTo(null);
        add(fondo);
        

                
     }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        butttonLoadData = new javax.swing.JButton();
        butttonLoadData1 = new javax.swing.JButton();
        butttonLoadData2 = new javax.swing.JButton();
        butttonLoadData3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        butttonLoadData.setText("Cargar eventos futuros");
        butttonLoadData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butttonLoadDataActionPerformed(evt);
            }
        });

        butttonLoadData1.setText("Generar reportes");
        butttonLoadData1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butttonLoadData1ActionPerformed(evt);
            }
        });

        butttonLoadData2.setText("Cargar Datos sivigila");
        butttonLoadData2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butttonLoadData2ActionPerformed(evt);
            }
        });

        butttonLoadData3.setText("Salir");
        butttonLoadData3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butttonLoadData3ActionPerformed(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(26, 46, 88));
        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("    Cargar datos");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel1.setOpaque(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(butttonLoadData3, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(butttonLoadData, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(butttonLoadData1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(butttonLoadData2, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(99, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(butttonLoadData2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(butttonLoadData1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(butttonLoadData, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(butttonLoadData3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 32, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void butttonLoadDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butttonLoadDataActionPerformed
        
        this.dispose();
        FutureEventsFrame frame = new FutureEventsFrame();
        frame.setVisible(true);
    }//GEN-LAST:event_butttonLoadDataActionPerformed

    private void butttonLoadData1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butttonLoadData1ActionPerformed
        
        this.dispose();
        GenerateReportsFrame frame = new GenerateReportsFrame();
        frame.setVisible(true);
    }//GEN-LAST:event_butttonLoadData1ActionPerformed

    private void butttonLoadData2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butttonLoadData2ActionPerformed
        this.dispose();
        LoadDataFrame frame = new LoadDataFrame();
        frame.setVisible(true);
    }//GEN-LAST:event_butttonLoadData2ActionPerformed

    private void butttonLoadData3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butttonLoadData3ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_butttonLoadData3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton butttonLoadData;
    private javax.swing.JButton butttonLoadData1;
    private javax.swing.JButton butttonLoadData2;
    private javax.swing.JButton butttonLoadData3;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
