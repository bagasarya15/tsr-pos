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
import static views.Dashboard.dashboard;
import static views.Dashboard.kelolaAkunMenu;
import static views.Dashboard.lapKeuanganMenu;
import static views.Dashboard.lapPengeluaranMenu;
import static views.Dashboard.lapPenjualanMenu;
import static views.Dashboard.lapProdukMenu;
import static views.Dashboard.produkMenu;
import static views.Dashboard.pengeluaranMenu;
import static views.Dashboard.supplierMenu;
import static views.Dashboard.txtIdKasir;
import static views.Dashboard.nameLog;
/**
 *
 * @author Bagas Arya
 */
public class Login extends javax.swing.JFrame {
    Dashboard dashboardView;
    /**
     * Creates new form Login
     */
    public Login() {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_HORIZ);
        setVisible(true);
        setResizable(false);
        
       dashboardView = new Dashboard();
       dashboardView.setVisible(false);
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        body = new javax.swing.JPanel();
        loginPanel = new javax.swing.JPanel();
        logoCard = new javax.swing.JPanel();
        creditText = new javax.swing.JLabel();
        iconLogoLogin = new javax.swing.JLabel();
        cardLogin = new javax.swing.JPanel();
        login = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        labelUsername = new javax.swing.JLabel();
        labelIconUser = new javax.swing.JLabel();
        txtuser = new javax.swing.JTextField();
        labelIconPass = new javax.swing.JLabel();
        txtpass = new javax.swing.JPasswordField();
        labelPass1 = new javax.swing.JLabel();
        cbuser = new javax.swing.JComboBox<>();
        loginButton = new javax.swing.JLabel();
        resetButton = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        labelAkses1 = new javax.swing.JLabel();
        warningText = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TSR POS - LOGIN");
        setMaximumSize(new java.awt.Dimension(930, 500));
        setMinimumSize(new java.awt.Dimension(930, 500));

        body.setBackground(new java.awt.Color(64, 64, 122));
        body.setMaximumSize(new java.awt.Dimension(930, 500));
        body.setMinimumSize(new java.awt.Dimension(930, 500));
        body.setPreferredSize(new java.awt.Dimension(930, 500));
        body.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        loginPanel.setBackground(new java.awt.Color(64, 64, 122));
        loginPanel.setMaximumSize(new java.awt.Dimension(480, 460));
        loginPanel.setMinimumSize(new java.awt.Dimension(480, 460));
        loginPanel.setPreferredSize(new java.awt.Dimension(480, 460));
        loginPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logoCard.setBackground(new java.awt.Color(255, 255, 255));
        logoCard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        creditText.setBackground(new java.awt.Color(64, 64, 122));
        creditText.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        creditText.setForeground(new java.awt.Color(64, 64, 122));
        creditText.setText("TSR - POS V.1.0");
        logoCard.add(creditText, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 400, -1, -1));

        iconLogoLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/img/tsr-logo.png"))); // NOI18N
        logoCard.add(iconLogoLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, -1, -1));

        loginPanel.add(logoCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 388, 440));

        cardLogin.setBackground(new java.awt.Color(255, 255, 255));
        cardLogin.setMaximumSize(new java.awt.Dimension(400, 440));
        cardLogin.setMinimumSize(new java.awt.Dimension(400, 440));
        cardLogin.setPreferredSize(new java.awt.Dimension(420, 440));
        cardLogin.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        login.setBackground(new java.awt.Color(64, 64, 122));
        login.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        login.setForeground(new java.awt.Color(64, 64, 122));
        login.setText("TSR - LOGIN");
        cardLogin.add(login, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 15, -1, -1));

        jSeparator2.setBackground(new java.awt.Color(64, 64, 122));
        jSeparator2.setForeground(new java.awt.Color(64, 64, 122));
        jSeparator2.setAlignmentX(1.0F);
        jSeparator2.setAlignmentY(1.0F);
        jSeparator2.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        cardLogin.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 380, 360, 20));

        labelUsername.setBackground(new java.awt.Color(64, 64, 122));
        labelUsername.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelUsername.setForeground(new java.awt.Color(64, 64, 122));
        labelUsername.setText("Username");
        cardLogin.add(labelUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 90, -1, -1));

        labelIconUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/userblack.png"))); // NOI18N
        cardLogin.add(labelIconUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, -1, 30));

        txtuser.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        txtuser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtuserActionPerformed(evt);
            }
        });
        cardLogin.add(txtuser, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 120, 270, 30));

        labelIconPass.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/key.png"))); // NOI18N
        cardLogin.add(labelIconPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, -1, 30));

        txtpass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtpassActionPerformed(evt);
            }
        });
        cardLogin.add(txtpass, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 190, 270, 30));

        labelPass1.setBackground(new java.awt.Color(64, 64, 122));
        labelPass1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelPass1.setForeground(new java.awt.Color(64, 64, 122));
        labelPass1.setText("Password");
        cardLogin.add(labelPass1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 160, -1, -1));

        cbuser.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        cbuser.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Level", "Admin", "Kasir" }));
        cardLogin.add(cbuser, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 260, 190, 30));

        loginButton.setBackground(new java.awt.Color(64, 64, 122));
        loginButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        loginButton.setForeground(new java.awt.Color(244, 244, 250));
        loginButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loginButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/enter.png"))); // NOI18N
        loginButton.setText(" LOGIN");
        loginButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        loginButton.setOpaque(true);
        loginButton.setPreferredSize(new java.awt.Dimension(100, 35));
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loginButtonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButtonMouseExited(evt);
            }
        });
        cardLogin.add(loginButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 320, 90, -1));

        resetButton.setBackground(new java.awt.Color(64, 64, 122));
        resetButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        resetButton.setForeground(new java.awt.Color(244, 244, 250));
        resetButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        resetButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/redo.png"))); // NOI18N
        resetButton.setText("RESET");
        resetButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        resetButton.setOpaque(true);
        resetButton.setPreferredSize(new java.awt.Dimension(100, 35));
        resetButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                resetButtonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                resetButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                resetButtonMouseExited(evt);
            }
        });
        cardLogin.add(resetButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 320, 90, -1));

        jSeparator3.setBackground(new java.awt.Color(64, 64, 122));
        jSeparator3.setForeground(new java.awt.Color(64, 64, 122));
        jSeparator3.setAlignmentX(1.0F);
        jSeparator3.setAlignmentY(1.0F);
        jSeparator3.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        cardLogin.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 360, 20));

        labelAkses1.setBackground(new java.awt.Color(64, 64, 122));
        labelAkses1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelAkses1.setForeground(new java.awt.Color(64, 64, 122));
        labelAkses1.setText("Akses Level");
        cardLogin.add(labelAkses1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 230, -1, -1));

        warningText.setBackground(new java.awt.Color(64, 64, 122));
        warningText.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        warningText.setForeground(new java.awt.Color(64, 64, 122));
        warningText.setText("Lupa Password ? Hubungi Admin !");
        cardLogin.add(warningText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 400, -1, -1));

        loginPanel.add(cardLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 0, -1, -1));

        body.add(loginPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(65, 32, 800, 440));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtuserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtuserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtuserActionPerformed

    private void txtpassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtpassActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtpassActionPerformed

    private void loginButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loginButtonMouseClicked
       try{
            Connection koneksi = (Connection)Koneksi.getKoneksi();
            Statement stat = koneksi.createStatement();
            ResultSet rs = stat.executeQuery(
                "SELECT * FROM users WHERE username='" + txtuser.getText() + "'"
                + "AND password='" + txtpass.getText()+ ""
                + "'AND role='" + cbuser.getSelectedItem() + "'"
            );
            
            if (rs.next()){
                txtIdKasir.setText(rs.getString(1));
                nameLog.setText(rs.getString(5));
            }
            
            rs.last();
            if(txtuser.getText().equals("username") && txtpass.getText().equals("password") && 
                cbuser.getSelectedItem().equals("role")){
                dashboardView.setVisible(true);
                dashboard.setEnabled(true);
                txtuser.setText("");
                txtpass.setText("");
                cbuser.setSelectedItem("Admin");
                this.dispose();
            } else if (rs.getRow() >=1) {
                if(cbuser.getSelectedItem().equals("Admin")){
                    dashboardView.setVisible(true);
                    dashboard.setEnabled(true);
                    produkMenu.setEnabled(true);
                    supplierMenu.setEnabled(true);
                    pengeluaranMenu.setEnabled(true);                    
                    lapProdukMenu.setEnabled(true);
                    lapPengeluaranMenu.setEnabled(true);
                    lapPenjualanMenu.setEnabled(true);
                    kelolaAkunMenu.setEnabled(true);
                    lapKeuanganMenu.setEnabled(true);
                    txtuser.setText("");
                    txtpass.setText("");
                    cbuser.setSelectedItem("Admin");
                    JOptionPane.showMessageDialog(null, "Anda Login Sebagai Admin");
                    this.dispose();
                }else if(cbuser.getSelectedItem().equals("Kasir")) {
                    dashboardView.setVisible(true);
                    dashboard.setEnabled(true);
                    produkMenu.setEnabled(false);
                    supplierMenu.setEnabled(false);
                    pengeluaranMenu.setEnabled(false);                    
                    lapProdukMenu.setEnabled(false);
                    lapPengeluaranMenu.setEnabled(false);
                    lapPenjualanMenu.setEnabled(false);
                    kelolaAkunMenu.setEnabled(false);
                    lapKeuanganMenu.setEnabled(false);
                    txtuser.setText("");
                    txtpass.setText("");
                    cbuser.setSelectedItem("Kasir");
                    JOptionPane.showMessageDialog(null, "Anda Login Sebagai Kasir");
                    this.dispose();
                }
                stat.close();
                this.dispose();
            }else{
                JOptionPane.showMessageDialog(this, "Login Gagal ! Username, Password / Hak Akses Salah !");
                JOptionPane.showMessageDialog(this, "Username, Password / Hak Akses Salah !");
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Koneksi gagal : "+e.getMessage());
        }
    }//GEN-LAST:event_loginButtonMouseClicked

    private void loginButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loginButtonMouseEntered
 
    }//GEN-LAST:event_loginButtonMouseEntered

    private void loginButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loginButtonMouseExited
       
    }//GEN-LAST:event_loginButtonMouseExited

    private void resetButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_resetButtonMouseClicked

        txtuser.setText("");
        txtpass.setText("");
        cbuser.setSelectedItem("Pilih Level");
        txtuser.requestFocus();
    }//GEN-LAST:event_resetButtonMouseClicked

    private void resetButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_resetButtonMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_resetButtonMouseEntered

    private void resetButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_resetButtonMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_resetButtonMouseExited

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
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel body;
    private javax.swing.JPanel cardLogin;
    private javax.swing.JComboBox<String> cbuser;
    private javax.swing.JLabel creditText;
    private javax.swing.JLabel iconLogoLogin;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel labelAkses1;
    private javax.swing.JLabel labelIconPass;
    private javax.swing.JLabel labelIconUser;
    private javax.swing.JLabel labelPass1;
    private javax.swing.JLabel labelUsername;
    private javax.swing.JLabel login;
    private javax.swing.JLabel loginButton;
    private javax.swing.JPanel loginPanel;
    private javax.swing.JPanel logoCard;
    private javax.swing.JLabel resetButton;
    private javax.swing.JPasswordField txtpass;
    private javax.swing.JTextField txtuser;
    private javax.swing.JLabel warningText;
    // End of variables declaration//GEN-END:variables
}
