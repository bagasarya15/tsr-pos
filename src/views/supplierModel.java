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
import static views.Dashboard.txNamaSupplier;
import static views.Dashboard.txtIdSupplier;


/**
 *
 * @author Bagas Arya Pradipta | 201843500707
 */
public class supplierModel extends javax.swing.JFrame {

    /**
     * Creates new form supplierModel
     */
    public supplierModel() {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_HORIZ); setVisible(true); setResizable(false);
        tampilDataSupplier();
        nullColumnSupplier();
    }
    
     public void  nullColumnSupplier() {
       txNamaSupplier.setEditable(true);
       txtIdSupplier.setText("1");
       txNamaSupplier.setText("Toko Umum");
    }
     
    private void tampilDataSupplier() {
        DefaultTableModel tblCustomer = new DefaultTableModel();
        tblCustomer.addColumn("No Supplier");
        tblCustomer.addColumn("Kode Customer");
        tblCustomer.addColumn("Nama Supplier");
        tblCustomer.addColumn("Deskripsi");
        
        String cariSupplier = txtCariSupplier.getText();
        try {
            String sql = "SELECT * FROM supplier WHERE nama_supplier LIKE'%"+cariSupplier+"%'";
            Connection conn = (Connection)Koneksi.getKoneksi();
            Statement stm = conn.createStatement();
            ResultSet rs  = stm.executeQuery(sql);
            
            while(rs.next()) {
                tblCustomer.addRow(new Object[]{
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(6)
                });
            }
            tbModelSupplier.setModel(tblCustomer);
        }catch (SQLException e){
           JOptionPane.showMessageDialog(null, "Koneksi Database Gagal" + e.getMessage());
        } 
    } 
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnPilih = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbModelSupplier = new javax.swing.JTable();
        txtCariSupplier = new javax.swing.JTextField();
        labelCariNewTransaksi = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(222, 222, 229));
        setMaximumSize(new java.awt.Dimension(421, 390));
        setMinimumSize(new java.awt.Dimension(421, 390));

        jPanel1.setBackground(new java.awt.Color(222, 222, 229));
        jPanel1.setMaximumSize(new java.awt.Dimension(421, 390));
        jPanel1.setMinimumSize(new java.awt.Dimension(421, 390));
        jPanel1.setPreferredSize(new java.awt.Dimension(421, 390));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnPilih.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnPilih.setForeground(new java.awt.Color(64, 64, 122));
        btnPilih.setText("PILIH");
        btnPilih.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPilihActionPerformed(evt);
            }
        });
        jPanel1.add(btnPilih, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 340, 70, -1));

        btnBatal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBatal.setForeground(new java.awt.Color(64, 64, 122));
        btnBatal.setText("BATAL");
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });
        jPanel1.add(btnBatal, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 340, -1, -1));

        tbModelSupplier.setForeground(new java.awt.Color(64, 64, 122));
        tbModelSupplier.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbModelSupplier.setGridColor(new java.awt.Color(0, 0, 0));
        tbModelSupplier.setSelectionBackground(new java.awt.Color(64, 64, 122));
        tbModelSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tbModelSupplierMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tbModelSupplier);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 125, 400, 200));

        txtCariSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCariSupplierActionPerformed(evt);
            }
        });
        txtCariSupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariSupplierKeyReleased(evt);
            }
        });
        jPanel1.add(txtCariSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 90, 230, 30));

        labelCariNewTransaksi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelCariNewTransaksi.setText("Cari Supplier :");
        jPanel1.add(labelCariNewTransaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 90, 110, 30));

        jPanel2.setBackground(new java.awt.Color(64, 64, 122));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("DATA SUPPLIER");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(148, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(135, 135, 135))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 420, 60));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 421, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 390, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnPilihActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPilihActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnPilihActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        // TODO add your handling code here:
        this.dispose(); nullColumnSupplier();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void tbModelSupplierMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbModelSupplierMousePressed
        // TODO add your handling code here:
        int baris = tbModelSupplier.rowAtPoint(evt.getPoint());
        String id_supplier = tbModelSupplier.getValueAt(baris, 0).toString();
        txtIdSupplier.setText(id_supplier);

        String nama_supplier = tbModelSupplier.getValueAt(baris, 2).toString();
        txNamaSupplier.setText(nama_supplier);
    }//GEN-LAST:event_tbModelSupplierMousePressed

    private void txtCariSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariSupplierActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCariSupplierActionPerformed

    private void txtCariSupplierKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariSupplierKeyReleased
        // TODO add your handling code here:
        tampilDataSupplier();
    }//GEN-LAST:event_txtCariSupplierKeyReleased

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
            java.util.logging.Logger.getLogger(supplierModel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(supplierModel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(supplierModel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(supplierModel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new supplierModel().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnPilih;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelCariNewTransaksi;
    private javax.swing.JTable tbModelSupplier;
    private javax.swing.JTextField txtCariSupplier;
    // End of variables declaration//GEN-END:variables
}
