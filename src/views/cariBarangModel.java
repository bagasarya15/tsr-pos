/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import config.Koneksi;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import static views.Dashboard.trHarga;
import static views.Dashboard.trIdProduk;
import static views.Dashboard.trNamaBarang;
import static views.Dashboard.trQty;
import static views.Dashboard.trStok;
import static views.Dashboard.trTotal;

/**
 *
 * @author Bagas Arya Pradipta | 201843500707
 */
public class cariBarangModel extends javax.swing.JFrame {
    
    /**
     * Creates new form cariBarangModel
    */
    public cariBarangModel() {
        //Disable Max Size Frame
        setExtendedState(JFrame.MAXIMIZED_HORIZ); setVisible(true); setResizable(false);
        
        initComponents(); tampilDataBarang(); nullTableKeranjang();
    }
    
    private void nullTableKeranjang() {
        trIdProduk.setEditable(true);
        trIdProduk.setText(null);
        trNamaBarang.setText(null);
        trHarga.setText(null);
        trStok.setText(null);
        trQty.setText(null);
        trTotal.setText(null);
    }
    
    private void tampilDataBarang() {
       DefaultTableModel tblTransaksi = new DefaultTableModel();
       tblTransaksi.addColumn("No Produk");
       tblTransaksi.addColumn("Kode Barang");
       tblTransaksi.addColumn("Nama Barang");
       tblTransaksi.addColumn("Harga");
       tblTransaksi.addColumn("Stok");
        
        String cariTransaksi = txtCariTransaksi.getText();
        try {
           
            String sql = "SELECT * FROM produk WHERE kode_barang LIKE'%"+cariTransaksi+"%' OR nama_barang LIKE'%"+cariTransaksi+"%'";
            Connection conn = (Connection)Koneksi.getKoneksi();
            Statement stm = conn.createStatement();
            ResultSet rs  = stm.executeQuery(sql);
            
            while(rs.next()) {
                tblTransaksi.addRow(new Object[]{
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(6),
                    rs.getString(7)
                });
            }
            tabelTransaksi.setModel(tblTransaksi);
        }catch (SQLException e){
           JOptionPane.showMessageDialog(null, "Koneksi Database Gagal" + e.getMessage());
        } 
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        container = new javax.swing.JPanel();
        JptabelTransaksi = new javax.swing.JScrollPane();
        tabelTransaksi = new javax.swing.JTable();
        txtCariTransaksi = new javax.swing.JTextField();
        labelCariTransaksi = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnPilih = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        container.setBackground(new java.awt.Color(222, 222, 229));

        tabelTransaksi.setBackground(new java.awt.Color(244, 244, 250));
        tabelTransaksi.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        tabelTransaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Kode Barang", "Nama Barang", "Harga", "Stok"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelTransaksi.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tabelTransaksi.setGridColor(new java.awt.Color(222, 222, 229));
        tabelTransaksi.setName("dashboard"); // NOI18N
        tabelTransaksi.setOpaque(false);
        tabelTransaksi.setRequestFocusEnabled(false);
        tabelTransaksi.setRowHeight(25);
        tabelTransaksi.setSelectionBackground(new java.awt.Color(64, 64, 122));
        tabelTransaksi.setSelectionForeground(new java.awt.Color(245, 245, 250));
        tabelTransaksi.setShowVerticalLines(false);
        tabelTransaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelTransaksiMouseClicked(evt);
            }
        });
        JptabelTransaksi.setViewportView(tabelTransaksi);

        txtCariTransaksi.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtCariTransaksi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariTransaksiKeyReleased(evt);
            }
        });

        labelCariTransaksi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelCariTransaksi.setText("Cari Barang :");

        jPanel2.setBackground(new java.awt.Color(64, 64, 122));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("DATA BARANG");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(228, 228, 228)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnPilih.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnPilih.setForeground(new java.awt.Color(64, 64, 122));
        btnPilih.setText("PILIH");
        btnPilih.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPilihActionPerformed(evt);
            }
        });

        btnBatal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBatal.setForeground(new java.awt.Color(64, 64, 122));
        btnBatal.setText("BATAL");
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout containerLayout = new javax.swing.GroupLayout(container);
        container.setLayout(containerLayout);
        containerLayout.setHorizontalGroup(
            containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(containerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JptabelTransaksi, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, containerLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, containerLayout.createSequentialGroup()
                                .addComponent(btnPilih, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, containerLayout.createSequentialGroup()
                                .addComponent(labelCariTransaksi)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtCariTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        containerLayout.setVerticalGroup(
            containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, containerLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCariTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCariTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JptabelTransaksi, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPilih)
                    .addComponent(btnBatal))
                .addGap(19, 19, 19))
        );

        getContentPane().add(container, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 600, 340));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tabelTransaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelTransaksiMouseClicked
        // TODO add your handling code here:
        int baris = tabelTransaksi.rowAtPoint(evt.getPoint());

        String id_produk = tabelTransaksi.getValueAt(baris, 0).toString();
        trIdProduk.setText(id_produk);
        
        String nama_barang = tabelTransaksi.getValueAt(baris, 2).toString();
        trNamaBarang.setText(nama_barang);
        
        String harga_jual = tabelTransaksi.getValueAt(baris, 3).toString();
        trHarga.setText(harga_jual);

        String stok = tabelTransaksi.getValueAt(baris, 4).toString();
        trStok.setText(stok);
    }//GEN-LAST:event_tabelTransaksiMouseClicked

    private void txtCariTransaksiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariTransaksiKeyReleased
        // TODO add your handling code here:
        tampilDataBarang();
    }//GEN-LAST:event_txtCariTransaksiKeyReleased

    private void btnPilihActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPilihActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnPilihActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        // TODO add your handling code here:
        this.dispose(); nullTableKeranjang();
    }//GEN-LAST:event_btnBatalActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(cariBarangModel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(cariBarangModel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(cariBarangModel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(cariBarangModel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new cariBarangModel().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane JptabelTransaksi;
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnPilih;
    private javax.swing.JPanel container;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel labelCariTransaksi;
    private static javax.swing.JTable tabelTransaksi;
    private javax.swing.JTextField txtCariTransaksi;
    // End of variables declaration//GEN-END:variables
}
