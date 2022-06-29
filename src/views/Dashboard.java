/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import config.Koneksi;
import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Bagas Arya Pradipta | 201843500707 | bagasarya150800@gmail.com
 */
public class Dashboard extends javax.swing.JFrame {
    /**
     * Creates new form Dashboard
     */

public Dashboard() {
        initComponents();
        //Disable Max Size Frame
        setExtendedState(JFrame.MAXIMIZED_HORIZ); setVisible(true); setResizable(false);
        
        //Component Menu Dashboard        
        countData(); transaksiTerbaru(); 
       
        //Component Menu Produk
        tampilDataProduk(); nullTableProduk(); autoKodeProduk();
        
        //Component Menu Supplier
        tampilDataSupplier(); nullTableSupplier(); autoKodeSupplier();
        
        //Component Menu Transaksi Penjualan
        autoKodeTransaksi(); nullTableKeranjang(); 
        tampilInputTransaksi(); dateTransaksi(); nullTransaksi(); totalBiaya();
        
        //Component Menu Pengeluaran 
        tampilDataPengeluaran();  nullTablePengeluaran(); autoKodePengeluaran();
        
        //Component Menu Customer
        tampilDataCustomer(); nullTableCustomer(); autoKodeCustomer();
        
        //Component Menu Laporan Inventory
        tampilDataInventory(); sumTotalInventory();
        
        //Component Menu Laporan Pengeluaran
        tampilLaporanPengeluaran();
       
        //Component Menu Laporan Penjualan
        tampilDataPenjualan();
        
        //Component Menu Laporan Keuangan
        tampilLaporanKeuangan();
        
        //Component Menu Kelola Akun
        tampilDataUsers(); nullTableUsers(); autoKodeUser();
}
//** All Function    

    //Function Menu Dashboard    
    private void searchByDate() {
        
        String bulan = String.valueOf(JcBulan.getSelectedItem());
        String tahun = String.valueOf(JcTahun.getYear());
        
        String query [] = {
            "SELECT SUM(jumlah_beli) AS search FROM transaksi_detail WHERE MONTHNAME(tgl_transaksi)='"+bulan+"'"
            + " AND YEAR(tgl_transaksi)='"+tahun+"'",
            
            "SELECT COUNT(DISTINCT(no_transaksi)) AS search FROM transaksi_detail WHERE MONTHNAME(tgl_transaksi)='"+bulan+"'"
            + " AND YEAR(tgl_transaksi)='"+tahun+"'",
            
            "SELECT SUM(total_harga) AS search FROM transaksi_detail WHERE YEAR(tgl_transaksi)='"+tahun+"'",
            
            "SELECT SUM(total_harga) AS search FROM transaksi_detail WHERE MONTHNAME(tgl_transaksi)='"+bulan+"'"
            + " AND YEAR(tgl_transaksi)='"+tahun+"'",
            
            "SELECT SUM(total_pengeluaran) AS search FROM pengeluaran WHERE MONTHNAME(tgl_pengeluaran)='"+bulan+"'"
            + " AND YEAR(tgl_pengeluaran)='"+tahun+"'",
            
            "SELECT ( (SELECT SUM(total_harga) FROM transaksi_detail WHERE MONTHNAME(tgl_transaksi)='"+bulan+"'"
            + " AND YEAR(tgl_transaksi)='"+tahun+"' ) - (SELECT SUM(total_pengeluaran) FROM pengeluaran "
            + "WHERE MONTHNAME(tgl_pengeluaran)='"+bulan+"' AND YEAR(tgl_pengeluaran)='"+tahun+"' ) ) AS search",
            
            "SELECT ( (SELECT SUM(total_harga) FROM transaksi_detail WHERE YEAR(tgl_transaksi)='"+tahun+"' ) - "
            + "(SELECT SUM(total_pengeluaran) FROM pengeluaran WHERE YEAR(tgl_pengeluaran)='"+tahun+"' ) ) AS search"
            
        };
        JLabel labels[] = {totalProdukTerjual, totalTransaksi , totalPemasukan, dataTotalPemasukan, dataPengeluaran, dataKeuntungan, dataLabaRugi};
        Connection conn = (Connection)Koneksi.getKoneksi();
        for (int i=0; i<6; i++){
                try{
                Statement stmt = conn.createStatement();
                ResultSet res  = stmt.executeQuery(query[i]);
                if (res.next()){
                    labels[i].setText(res.getString("search"));
                }
            }catch (SQLException e){
                System.out.println("searchByDate" + e.getMessage());
            }
        }
    }
    
    private void countData() {
         String querys[] = {
            "SELECT SUM(stok) AS data FROM produk",
             
            "SELECT SUM(jumlah_beli) AS data FROM transaksi_detail",
            
            "SELECT COUNT(DISTINCT(no_transaksi)) AS data FROM transaksi_detail",
            
            "SELECT SUM(total_harga) AS data FROM transaksi_detail WHERE MONTH(tgl_transaksi)=MONTH(CURDATE()) "
            + "AND YEAR(tgl_transaksi)=YEAR(CURDATE())",
            
            "SELECT SUM(total_harga) AS data FROM transaksi_detail",
            
            "SELECT SUM(total_pengeluaran) AS data FROM pengeluaran WHERE MONTH(tgl_pengeluaran)=MONTH(CURDATE())"
            + " AND YEAR(tgl_pengeluaran) = YEAR(CURDATE())",
            
            "SELECT (SELECT SUM(total_harga) FROM transaksi_detail WHERE MONTH(tgl_transaksi)=MONTH(CURDATE()) AND "
            + "YEAR(tgl_transaksi)=YEAR(CURDATE()) ) - (SELECT SUM(total_pengeluaran) FROM pengeluaran"
            + " WHERE MONTH(tgl_pengeluaran)=MONTH(CURDATE()) AND YEAR(tgl_pengeluaran)=YEAR(CURDATE()) ) AS data",
            
            "SELECT ( (SELECT SUM(total_harga) FROM transaksi_detail WHERE YEAR(tgl_transaksi)=YEAR(CURDATE()) ) - "
            + "(SELECT SUM(total_pengeluaran) FROM pengeluaran WHERE YEAR(tgl_pengeluaran)=YEAR(CURDATE()) ) ) AS data"
        };
         
        JLabel labels[] = {totalStokProduk, totalProdukTerjual, totalTransaksi, dataTotalPemasukan, totalPemasukan, dataPengeluaran, dataKeuntungan, dataLabaRugi };
        
        Connection conn = (Connection)Koneksi.getKoneksi();
        for (int i=0; i<8; i++){
                try{
                Statement stmt = conn.createStatement();
                ResultSet res  = stmt.executeQuery(querys[i]);
                if (res.next()){
                    labels[i].setText(res.getString("data"));
                }
            }catch (SQLException e){
                System.out.println("countData" +e.getMessage());
            }
        }
       
    }
    
    private void transaksiTerbaru() {
    DefaultTableModel transaksiTerbaru = new DefaultTableModel();
    DecimalFormat DF = new DecimalFormat("Rp, #,###,###");
    transaksiTerbaru.addColumn("No");
    transaksiTerbaru.addColumn("No Transaksi");
    transaksiTerbaru.addColumn("Customer");
    transaksiTerbaru.addColumn("Kode Barang");
    transaksiTerbaru.addColumn("Nama Barang");
    transaksiTerbaru.addColumn("Jumlah Beli");
    transaksiTerbaru.addColumn("Dibayar");
    transaksiTerbaru.addColumn("Tgl-Transaksi");
    
    String cariDashboard = dashboardCariNewTransaksi.getText();
        try {
            int no = 1;
            String sql = "SELECT transaksi_detail.no_transaksi, customer.nama_customer, produk.kode_barang, "
                    + "produk.nama_barang, transaksi_detail.jumlah_beli, transaksi_detail.total_bayar, "
                    + "transaksi_detail.tgl_transaksi FROM customer JOIN transaksi_detail ON "
                    + "customer.id_customer = transaksi_detail.id_customer JOIN produk ON "
                    + "produk.id_produk = transaksi_detail.id_produk WHERE no_transaksi LIKE'%"+cariDashboard+"%' "
                    + "OR nama_customer LIKE'%"+cariDashboard+"%' ORDER BY no_transaksi DESC";
            Connection conn = (Connection)Koneksi.getKoneksi();
            Statement stm = conn.createStatement();
            ResultSet rs  = stm.executeQuery(sql);

            while(rs.next()) {
                 transaksiTerbaru.addRow(new Object[]{
                    no++,
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    DF.format(rs.getInt(6)),
                    rs.getString(7)
                });
            }
            newTransaksi.setModel(transaksiTerbaru);
        }catch (SQLException e){
           JOptionPane.showMessageDialog(null, "Transaksi Terbaru Koneksi Database Gagal" + e.getMessage());
        } 
    }
    // End Menu Dashboard    
    
    // Function Menu Produk
    private void autoKodeProduk() {
        try{   
            String sql = "SELECT MAX(right(kode_barang, 2))  AS kode_barang FROM produk";        
            Connection conn = (Connection)Koneksi.getKoneksi();
            Statement stmt = conn.createStatement();
            ResultSet rs  = stmt.executeQuery(sql);
            while(rs.next()){
                if(rs.first()==false){
                    txtkodebarang.setText("PDK-001");
                }else{
                    rs.last();
                    int autoId = rs.getInt(1) + 1;
                    String nomor = String.valueOf(autoId);
                    int noLong = nomor.length();
                    
                    for(int i=0; i<3-noLong; i++){
                        nomor = "0" + nomor;
                    }
                    txtkodebarang.setText("PDK-" + nomor);
                }
            }
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void nullTableProduk() {
        txtnamabarang.setEditable(true);
        txtnamabarang.setText(null);
        cbkategori.setSelectedItem(this);
        txthargabeli.setText(null);
        txthargajual.setText(null);
        txtstok.setText(null);
        txtIdSupplier.setText("1");
        txNamaSupplier.setText("");
    }
    
    private void tampilDataProduk() {
        DefaultTableModel tblProduk = new DefaultTableModel();
        tblProduk.addColumn("No");
        tblProduk.addColumn("Kode Barang");
        tblProduk.addColumn("Nama Barang");
        tblProduk.addColumn("Kategori");
        tblProduk.addColumn("Harga Beli");
        tblProduk.addColumn("Harga Jual");
        tblProduk.addColumn("Stok");
        tblProduk.addColumn("Supplier");
        
        String cariProduk = txtcariProduk.getText();
        try {
            int no = 1;
            String sql = "SELECT produk.kode_barang, produk.nama_barang, produk.kategori, produk.harga_beli, produk.harga_jual,"
                    + " produk.stok, supplier.nama_supplier FROM supplier JOIN produk "
                    + " ON produk.id_supplier = supplier.id_supplier WHERE nama_barang LIKE'%"+cariProduk+"%' "
                    + " ORDER BY kode_barang DESC ";
            Connection conn = (Connection)Koneksi.getKoneksi();
            Statement stm = conn.createStatement();
            ResultSet rs  = stm.executeQuery(sql);
            while(rs.next()) {
                tblProduk.addRow(new Object[]{
                    no++,
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6),
                    rs.getString(7)
                });
            }
            tabelProduk.setModel(tblProduk);
        }catch (SQLException e){
           JOptionPane.showMessageDialog(null, "Tampil Data Produk Koneksi Database Gagal" + e.getMessage());
        } 
    }
    // End Menu Produk  
    
    //Function Menu Supplier
    private void autoKodeSupplier() {
        try{   
            String sql = "SELECT MAX(right(kode_supplier, 2))  AS kode_supplier FROM supplier";        
            Connection conn = (Connection)Koneksi.getKoneksi();
            Statement stmt = conn.createStatement();
            ResultSet rs  = stmt.executeQuery(sql);
            while(rs.next()){
                if(rs.first()==false){
                    txtKodeSupplier.setText("SPL-001");
                }else{
                    rs.last();
                    int supplierKode = rs.getInt(1) + 1;
                    String nomor = String.valueOf(supplierKode);
                    int noSup = nomor.length();
                    
                    for(int i=0; i<3-noSup; i++){
                        nomor = "0" + nomor;
                    }
                    txtKodeSupplier.setText("SPL-" + nomor);
                }
            }
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void nullTableSupplier() {
        txtNamaSupplier.setEditable(true);
        txtNamaSupplier.setText(null);
        txtAlamatSupplier.setText(null);
        txtTlpSupplier.setText(null);
        txtDeskripsi.setText(null);
    }
    
    private void tampilDataSupplier() {
        DefaultTableModel tblSupplier = new DefaultTableModel();
        tblSupplier.addColumn("No");
        tblSupplier.addColumn("Kode Supplier");
        tblSupplier.addColumn("Nama Supplier");
        tblSupplier.addColumn("Alamat");
        tblSupplier.addColumn("No Tlp");
        tblSupplier.addColumn("Deskripsi");
        
        String cariSupplier = txtcariSupplier.getText();
        try {
            int no = 1;
            String sql = "SELECT * FROM supplier WHERE nama_supplier LIKE'%"+cariSupplier+"%'";
            Connection conn = (Connection)Koneksi.getKoneksi();
            Statement stm = conn.createStatement();
            ResultSet rs  = stm.executeQuery(sql);
            
            while(rs.next()) {
                tblSupplier.addRow(new Object[]{
                    no++,
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6)
                });
            }
            tabelSupplier.setModel(tblSupplier);
        }catch (SQLException e){
           JOptionPane.showMessageDialog(null, "Koneksi Database Gagal" + e.getMessage());
        } 
    } 
    //End Menu Supplier
    
     //Function Menu Transaksi Penjualan
    private void dateTransaksi() {
        Date ys = new Date();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        txtTgl.setText(s.format(ys));
    }
    
    private void autoKodeTransaksi() {
        try{   
            String sql = "SELECT MAX(right(no_transaksi, 4))  AS no_transaksi FROM transaksi_detail";        
            Connection conn = (Connection)Koneksi.getKoneksi();
            Statement stmt = conn.createStatement();
            ResultSet rs  = stmt.executeQuery(sql);
            while(rs.next()){
                if(rs.first()==false){
                    transaksiKode.setText("INVOICE-0001");
                }else{
                    rs.last();
                    int autoId = rs.getInt(1) + 1;
                    String nomor = String.valueOf(autoId);
                    int noInvoice = nomor.length();
                    
                    for(int i=0; i<4-noInvoice; i++){
                        nomor = "0" + nomor;
                    }
                    transaksiKode.setText("INVOICE-" + nomor);
                }
            }
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
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
    
    private void nullTransaksi() {
        txtBayar.setEditable(true);
        txtBayar.setText(null);
        txtKembalian.setText(null);
        txIdCustomer.setText("1");
        txNamaCustomer.setText("Pembeli Umum");
    }
    
    //Perhitungan Transaksi
    private void totalBiaya() {
        int jumlahBaris = tabelInputTransaksi.getRowCount();
        int totalBiaya = 0;
        int jumlahBarang, hargaBarang;
        DecimalFormat DF = new DecimalFormat("#,###,###");
        for (int i = 0; i < jumlahBaris; i++) {
            hargaBarang = Integer.parseInt(tabelInputTransaksi.getValueAt(i, 3).toString());
            jumlahBarang = Integer.parseInt(tabelInputTransaksi.getValueAt(i, 4).toString());
            
            totalBiaya = totalBiaya + (hargaBarang*jumlahBarang);
        }
        txtTotalBayar.setText(String.valueOf(totalBiaya));
        txtTotalKeseluruhan.setText("Rp " + DF.format(totalBiaya));
    }
    
    private void hitungKembalian() {
        int jumlah, harga, total;
        jumlah = Integer.valueOf(trQty.getText());
        harga = Integer.valueOf(trHarga.getText());
        total = jumlah * harga;
        
        txtTotalBayar.setText(String.valueOf(total));
        totalBiaya();
    }
    
    private void hitungTotalKeranjang(){
        int qty, hargaBarang, hargaTotal;
        qty = Integer.valueOf(trQty.getText());
        hargaBarang = Integer.valueOf(trHarga.getText());
        hargaTotal = qty * hargaBarang;
        
        trTotal.setText(String.valueOf(hargaTotal));
        
    }
    
    private void Kembalian(){
       int totalBayar, bayar, kembali;
       totalBayar = Integer.valueOf(txtTotalBayar.getText());
       bayar = Integer.valueOf(txtBayar.getText());
       kembali = bayar - totalBayar;
       
       txtKembalian.setText(String.valueOf(kembali));
    }
    
    private void updateStok() {
        try {
            Integer stokBarang,jumlahJual, sisaStok;
            stokBarang = Integer.parseInt(trStok.getText());
            jumlahJual =  Integer.parseInt(trQty.getText());
            sisaStok = (stokBarang - jumlahJual);
            
            String updateStok = "UPDATE produk SET stok='"+sisaStok+"' WHERE id_produk = '"+trIdProduk.getText()+"'";
            Connection conn = (Connection)Koneksi.getKoneksi();
            PreparedStatement pst = conn.prepareStatement(updateStok);
            pst.execute();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Stok Gagal Diupdate !");
        }
    }
    
    private void returnStok(){       
        try{    
            Integer stokAwal;
            stokAwal = Integer.parseInt(trStok.getText());
            
            String updateStok = "UPDATE produk SET stok='"+stokAwal+"' WHERE id_produk = '"+trIdProduk.getText()+"'";
            Connection conn = (Connection)Koneksi.getKoneksi();
            PreparedStatement pst = conn.prepareStatement(updateStok);
            pst.execute();
            
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Return Stok Gagal !");
        }
    }
    //End Perhitungan Transaksi
    
    private void resetKeranjang() {
        try {   
            String sql = "DELETE FROM transaksi";
            Connection conn = (Connection)Koneksi.getKoneksi();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.execute();
            System.out.println("Keranjang Berhasil Dikosongkan !");
            autoKodeTransaksi(); nullTableKeranjang(); nullTransaksi(); tampilInputTransaksi(); totalBiaya();
        }catch(SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
     private void functionAddNota() {
       DefaultTableModel model = (DefaultTableModel) tabelInputTransaksi.getModel();
       Statement st;    
       int total, bayar;
       total = Integer.valueOf(txtTotalBayar.getText());
       bayar = Integer.valueOf(txtBayar.getText());
        
        try {
           Connection conn = (Connection)Koneksi.getKoneksi();
           st = conn.createStatement();
            if(txtBayar.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Isi Nominal Pembayaran !");
            } else if(total > bayar){
                JOptionPane.showMessageDialog(null, "Uang Tidak Cukup Untuk Melakukan Pembayaran !");
            } else {
                for (int i=0; i < model.getRowCount(); i++){
                  String noProduk = model.getValueAt(i,0).toString();
                  String qtyBarang = model.getValueAt(i,4).toString();
                  String totalHarga = model.getValueAt(i,6).toString();

                  String sqlQuery = "INSERT INTO `transaksi_detail` "
                          + "(`id_detail`,`no_transaksi`, `id_user`, `id_customer` , `id_produk`, `jumlah_beli`, "
                          + "`total_harga`, `total_bayar`, `total_kembalian`, `tgl_transaksi`)"
                          + " VALUES (default,'"+transaksiKode.getText()+"', '"+txtIdKasir.getText()+"',"
                          + "'"+txIdCustomer.getText()+"', '"+noProduk+"','"+qtyBarang+"','"+totalHarga+"', "
                          + "'"+txtBayar.getText()+"', '"+txtKembalian.getText()+"', '"+txtTgl.getText()+"')";
                  st.addBatch(sqlQuery);
            }
                int [] rowsInserted = st.executeBatch();
                JOptionPane.showMessageDialog(null, "Barang Berhasil Dibayar !");
                JOptionPane.showMessageDialog(null, "Tunggu Proses Cetak Struk !");
                strukTransaksi(); resetKeranjang();
           }           
        }catch(SQLException e) {
           JOptionPane.showMessageDialog(null, "Tambah Pembayaran Error !" + e.getMessage());
        }
        tampilDataPenjualan(); transaksiTerbaru();
        dateTransaksi(); countData(); nullTransaksi();
    }
     
    private void strukTransaksi() {
        try {      
            Connection conn = (Connection)Koneksi.getKoneksi();
            String report = "/reports/reportStruk.jasper";
            InputStream path = getClass().getResourceAsStream(report);
            HashMap hash = new HashMap();
            hash.put("kode_transaksi",transaksiKode.getText());
            JasperPrint JPrint = JasperFillManager.fillReport(path, hash, conn);
            JasperViewer.viewReport(JPrint, false);
        } catch (JRException ex) {
            JOptionPane.showMessageDialog(null, "Gagal Cetak Struk" + ex);
        }
    }
    
    private void tampilInputTransaksi() {
       DefaultTableModel tblInputTransaksi = new DefaultTableModel();
       tblInputTransaksi.addColumn("No Produk");
       tblInputTransaksi.addColumn("Kode Barang");
       tblInputTransaksi.addColumn("Nama Barang");
       tblInputTransaksi.addColumn("Harga");
       tblInputTransaksi.addColumn("Jumlah Beli");
       tblInputTransaksi.addColumn("Stok Awal");
       tblInputTransaksi.addColumn("Total");
        
        try {
            String sql = "SELECT produk.id_produk, produk.kode_barang, produk.nama_barang, produk.harga_jual,"
                    + " transaksi.qty, transaksi.stok_awal, transaksi.grand_total FROM produk JOIN transaksi "
                    + "ON produk.id_produk = transaksi.id_produk";
            Connection conn = (Connection)Koneksi.getKoneksi();
            Statement stm = conn.createStatement();
            ResultSet rs  = stm.executeQuery(sql);
            
            while(rs.next()) {
                tblInputTransaksi.addRow(new Object[]{
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6),
                    rs.getString(7),
                });
            }
           tabelInputTransaksi.setModel(tblInputTransaksi);
        }catch (SQLException e){
           JOptionPane.showMessageDialog(null, "Koneksi Database Gagal" + e.getMessage());
        } 
    }
   
    //End Menu Transaksi Penjualan
    
    //Menu Pengeluaran
    private void autoKodePengeluaran() {
        try{   
            String sql = "SELECT MAX(right(no_pengeluaran, 4))  AS no_pengeluaran FROM pengeluaran";        
            Connection conn = (Connection)Koneksi.getKoneksi();
            Statement stmt = conn.createStatement();
            ResultSet rs  = stmt.executeQuery(sql);
            while(rs.next()){
                if(rs.first()==false){
                    txtIdPengeluaran.setText("EXPNS-0001");
                }else{
                    rs.last();
                    int autoId = rs.getInt(1) + 1;
                    String nomor = String.valueOf(autoId);
                    int noPengeluaran = nomor.length();
                    
                    for(int i=0; i<4-noPengeluaran; i++){
                        nomor = "0" + nomor;
                    }
                    txtIdPengeluaran.setText("EXPNS-" + nomor);
                }
            }
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private static Date getTanggalPengeluaran(JTable table, int kolom) {
        JTable tabel = table;
        String str_tgl = String.valueOf(tabel.getValueAt(tabel.getSelectedRow(), kolom));
        Date tanggal = null;
        
        try{
            tanggal = new SimpleDateFormat("yyyy-MM-dd").parse(str_tgl);
        }catch(ParseException ex){
            System.out.println("getTanggalPengeluaran Error" + ex );
        }
        return tanggal;
    }
    
    private void nullTablePengeluaran() {
        txtIdPengeluaran.setEditable(true);
        txtIdPengeluaran.setText(null);
        txtKeteranganPengeluaran.setText(null);
        txtJumlahPengeluaran.setText(null);
        txtTotalPengeluaran.setText(null);
    }
  
    private void tampilDataPengeluaran() {
       DefaultTableModel tblPengeluaran = new DefaultTableModel();
       tblPengeluaran.addColumn("No");
       tblPengeluaran.addColumn("No Pengeluaran");
       tblPengeluaran.addColumn("Jenis Pengeluaran");
       tblPengeluaran.addColumn("Keterangan");
       tblPengeluaran.addColumn("Jumlah");
       tblPengeluaran.addColumn("Total Pengeluaran");
       tblPengeluaran.addColumn("Tgl-Pengeluaran");
       
       String cariPengeluaran = txtcariPengeluaran.getText();
        try {
            int no = 1;
            String sql = "SELECT * FROM pengeluaran WHERE no_pengeluaran  LIKE'%"+cariPengeluaran+"%' OR "
                    + "keterangan LIKE'%"+cariPengeluaran+"%' ORDER BY no_pengeluaran DESC ";
            Connection conn = (Connection)Koneksi.getKoneksi();
            Statement stm = conn.createStatement();
            ResultSet rs  = stm.executeQuery(sql);
            
            while(rs.next()) {
               tblPengeluaran.addRow(new Object[]{
                    no++,
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6),
                    rs.getString(7)
                });
            }
           tabelPengeluaran.setModel(tblPengeluaran);
        }catch (SQLException e){
           JOptionPane.showMessageDialog(null, "Koneksi Database Tabel Pengeluaran Gagal" + e.getMessage());
        } 
    }
    //End Menu Pengeluaran
    
    //Function Menu Customer
    private void autoKodeCustomer() {
        try{   
            String sql = "SELECT MAX(right(kode_customer, 2))  AS kode_customer FROM customer";        
            Connection conn = (Connection)Koneksi.getKoneksi();
            Statement stmt = conn.createStatement();
            ResultSet rs  = stmt.executeQuery(sql);
            while(rs.next()){
                if(rs.first()==false){
                    txtKodeCustomer.setText("CST-001");
                }else{
                    rs.last();
                    int customerKode = rs.getInt(1) + 1;
                    String nomor = String.valueOf(customerKode);
                    int noCus = nomor.length();
                    
                    for(int i=0; i<3-noCus; i++){
                        nomor = "0" + nomor;
                    }
                    txtKodeCustomer.setText("CST-" + nomor);
                }
            }
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void nullTableCustomer() {
        txtNamaCustomer.setEditable(true);
        txtNamaCustomer.setText(null);
        txtAlamatCustomer.setText(null);
        txtTlpCustomer.setText(null);
    }
    
    private void tampilDataCustomer() {
        DefaultTableModel tblCustomer = new DefaultTableModel();
        tblCustomer.addColumn("No");
        tblCustomer.addColumn("Kode Customer");
        tblCustomer.addColumn("Nama Customer");
        tblCustomer.addColumn("Alamat");
        tblCustomer.addColumn("No Tlp");
        
        String cariCustomer = txtcariCustomer.getText();
        try {
            int no = 1;
            String sql = "SELECT * FROM customer WHERE nama_customer LIKE'%"+cariCustomer+"%'";
            Connection conn = (Connection)Koneksi.getKoneksi();
            Statement stm = conn.createStatement();
            ResultSet rs  = stm.executeQuery(sql);
            
            while(rs.next()) {
                tblCustomer.addRow(new Object[]{
                    no++,
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5)
                });
            }
            tabelCustomer.setModel(tblCustomer);
        }catch (SQLException e){
           JOptionPane.showMessageDialog(null, "Koneksi Database Gagal" + e.getMessage());
        } 
    } 
    //End Menu Customer
    
    //Menu Laporan Inventory
     private void sumTotalInventory() {
        DecimalFormat DF = new DecimalFormat("#,###,###");
        String querys[] = {
            "SELECT SUM(harga_beli * stok) AS sumInven FROM produk",
            "SELECT SUM(harga_jual * stok) AS sumInven FROM produk",
            "SELECT SUM(stok) AS sumInven FROM produk"
        };
         
        JLabel labels[] = {sumHargaBeli, sumHargaJual, sumStok};
        
        Connection conn = (Connection)Koneksi.getKoneksi();
        for (int i=0; i<3; i++){
                try{
                Statement stmt = conn.createStatement();
                ResultSet res  = stmt.executeQuery(querys[i]);
                if (res.next()){
                    labels[i].setText(DF.format(res.getInt("sumInven")));
                }
            }catch (SQLException e){
                System.out.println("sumInventory" +e.getMessage());
            }
        }
    }
     
    private void tampilDataInventory() {
        DefaultTableModel tblProduk = new DefaultTableModel();
        DecimalFormat DF = new DecimalFormat("Rp #,###,###");
        tblProduk.addColumn("No");
        tblProduk.addColumn("Kode Barang");
        tblProduk.addColumn("Nama Barang");
        tblProduk.addColumn("Kategori");
        tblProduk.addColumn("Harga Beli");
        tblProduk.addColumn("Harga Jual");
        tblProduk.addColumn("Stok");
        tblProduk.addColumn("Supplier");
        
        String cariProduk = txtCariInventory.getText();
        try {
            int no = 1;
            String sql = "SELECT produk.kode_barang, produk.nama_barang, produk.kategori, produk.harga_beli, produk.harga_jual,"
                    + " produk.stok, supplier.nama_supplier FROM supplier JOIN produk "
                    + "ON produk.id_supplier = supplier.id_supplier WHERE kode_barang LIKE'%"+cariProduk+"%' "
                    + "OR nama_barang LIKE'%"+cariProduk+"%' ORDER BY kode_barang DESC";
            Connection conn = (Connection)Koneksi.getKoneksi();
            Statement stm = conn.createStatement();
            ResultSet rs  = stm.executeQuery(sql);
            while(rs.next()) {
                tblProduk.addRow(new Object[]{
                    no++,
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    DF.format(rs.getInt(4)),
                    DF.format(rs.getInt(5)),
                    rs.getString(6),
                    rs.getString(7)
                });
            }
            tabelInventory.setModel(tblProduk);
        }catch (SQLException e){
           JOptionPane.showMessageDialog(null, "Tabel Inventory Koneksi Database Gagal" + e.getMessage());
        } 
    }
    
      private void cetakDataProduk(){
        try {      
            Connection conn = (Connection)Koneksi.getKoneksi();
            String report = "/reports/reportInventory.jasper";
            InputStream path = getClass().getResourceAsStream(report);
            JasperPrint JPrint = JasperFillManager.fillReport(path, new HashMap(), conn);
            JasperViewer.viewReport(JPrint, false);
        } catch (JRException ex) {
            JOptionPane.showMessageDialog(null, "Gagal Cetak Data Produk" + ex);
        }
    }
    //End Menu Laporan Inventory
    
    //Menu Laporan Pengeluaran
    private void tampilLaporanPengeluaran() {
       DefaultTableModel tblPengeluaran = new DefaultTableModel();
       DecimalFormat DF = new DecimalFormat("Rp #,###,###");
       tblPengeluaran.addColumn("No Pengeluaran");
       tblPengeluaran.addColumn("Jenis Pengeluaran");
       tblPengeluaran.addColumn("Keterangan");
       tblPengeluaran.addColumn("Jumlah");
       tblPengeluaran.addColumn("Total Pengeluaran");
       tblPengeluaran.addColumn("Tgl-Pengeluaran");
       
       String cariPengeluaran = txtCariPengeluaran.getText();
        try {
           
            String sql = "SELECT * FROM pengeluaran WHERE no_pengeluaran  LIKE'%"+cariPengeluaran+"%' OR "
                    + "keterangan LIKE'%"+cariPengeluaran+"%' ORDER BY no_pengeluaran DESC ";
            Connection conn = (Connection)Koneksi.getKoneksi();
            Statement stm = conn.createStatement();
            ResultSet rs  = stm.executeQuery(sql);
            
            while(rs.next()) {
               tblPengeluaran.addRow(new Object[]{
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    DF.format(rs.getInt(6)),
                    rs.getString(7)
                });
            }
           tabelLaporanPengeluaran.setModel(tblPengeluaran);
        }catch (SQLException e){
           JOptionPane.showMessageDialog(null, "Koneksi Database Tabel Pengeluaran Gagal" + e.getMessage());
        }
    }
    
    private void cetakLaporanPengeluaran() {
        try {     
            Connection conn = (Connection)Koneksi.getKoneksi();
            String report = "/reports/reportPengeluaran.jasper";
            InputStream path = getClass().getResourceAsStream(report);
            JasperPrint JPrint = JasperFillManager.fillReport(path, new HashMap(), conn);
            JasperViewer.viewReport(JPrint, false);
        } catch (JRException ex) {
            JOptionPane.showMessageDialog(null, "Gagal Cetak Laporan Pengeluaran" + ex);
        }
    }
    
    private void cetakPengeluaranByMonthYear() {
        String bulan = String.valueOf(JcBulanPengeluaran.getSelectedIndex());
        String tahun = String.valueOf(JcTahunPengeluaran.getYear());
        try {      
            Connection conn = (Connection)Koneksi.getKoneksi();
            String report = "/reports/reportPengeluaranByMonthYear.jasper";
            InputStream path = getClass().getResourceAsStream(report);
            HashMap hash = new HashMap();
            hash.put("pengeluaranByMonth", bulan);
            hash.put("pengeluaranByYear", tahun);
            JasperPrint JPrint = JasperFillManager.fillReport(path, hash, conn);
            JasperViewer.viewReport(JPrint, false);
        } catch (JRException ex) {
            JOptionPane.showMessageDialog(null, "Gagal Cetak Laporan Keuangan" + ex);
        }
    }
    //End Menu Laporan Pengeluaran
    
    //Menu Laporan Penjualan
    private void tampilDataPenjualan() {
        DefaultTableModel tblPenjualan = new DefaultTableModel();
        tblPenjualan.addColumn("No");
        tblPenjualan.addColumn("No Transaksi");
        tblPenjualan.addColumn("Kasir");
        tblPenjualan.addColumn("Customer");
        tblPenjualan.addColumn("Kode Barang");
        tblPenjualan.addColumn("Nama Barang");
        tblPenjualan.addColumn("Harga");
        tblPenjualan.addColumn("Jumlah Beli");
        tblPenjualan.addColumn("Total");
        tblPenjualan.addColumn("Dibayar");
        tblPenjualan.addColumn("Kembali");
        tblPenjualan.addColumn("Tgl-Transaksi");

        String cariPenjualan = txtCariPenjualan.getText();
        try {
            int no = 1;
            String sql = "SELECT transaksi_detail.no_transaksi, users.name ,customer.nama_customer, produk.kode_barang, "
                    + "produk.nama_barang, produk.harga_jual, transaksi_detail.jumlah_beli, transaksi_detail.total_harga, "
                    + "transaksi_detail.total_bayar, transaksi_detail.total_kembalian, transaksi_detail.tgl_transaksi "
                    + "FROM customer JOIN transaksi_detail ON customer.id_customer = transaksi_detail.id_customer "
                    + "JOIN produk ON produk.id_produk = transaksi_detail.id_produk JOIN users "
                    + "ON users.id_user = transaksi_detail.id_user WHERE no_transaksi LIKE "
                    + "'%"+cariPenjualan+"%' OR nama_customer LIKE '%"+cariPenjualan+"%' ORDER BY no_transaksi DESC";
            
            Connection conn = (Connection)Koneksi.getKoneksi();
            Statement stm = conn.createStatement();
            ResultSet rs  = stm.executeQuery(sql);

            while(rs.next()) {
                 tblPenjualan.addRow(new Object[]{
                    no++,
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6),
                    rs.getString(7),
                    rs.getString(8),
                    rs.getString(9),
                    rs.getString(10),
                    rs.getString(11)
                });
            }
            tabelLapPenjualan.setModel(tblPenjualan);
        }catch (SQLException e){
           JOptionPane.showMessageDialog(null, "Laporan Penjualan Koneksi Database Gagal" + e.getMessage());
        } 
    }
    
    private void cetakDataByInvoice() {
        try {      
            Connection conn = (Connection)Koneksi.getKoneksi();
            String report = "/reports/reportStruk.jasper";
            InputStream path = getClass().getResourceAsStream(report);
            HashMap hash = new HashMap();
            hash.put("kode_transaksi", txtCariPenjualan.getText());
            JasperPrint JPrint = JasperFillManager.fillReport(path, hash, conn);
            JasperViewer.viewReport(JPrint, false);
        } catch (JRException ex) {
            JOptionPane.showMessageDialog(null, "Gagal Cetak Struk" + ex);
        }
    }
    
    private void cetakPenjualanByMonthYear() {
        String bulan = String.valueOf(JcBulanPenjualan.getSelectedIndex());
        String tahun = String.valueOf(JcTahunPenjualan.getYear());
        try {      
            Connection conn = (Connection)Koneksi.getKoneksi();
            String report = "/reports/reportPenjualanByMonthYear.jasper";
            InputStream path = getClass().getResourceAsStream(report);
            HashMap hash = new HashMap();
            hash.put("ByMonth", bulan);
            hash.put("ByYear", tahun);
            JasperPrint JPrint = JasperFillManager.fillReport(path, hash, conn);
            JasperViewer.viewReport(JPrint, false);
        } catch (JRException ex) {
            JOptionPane.showMessageDialog(null, "Gagal Cetak Laporan Penjualan" + ex);
        }
    }
    
    private void cetakDataPenjualan(){
        try {       
            Connection conn = (Connection)Koneksi.getKoneksi();
            String report = "/reports/reportPenjualan.jasper";
            InputStream path = getClass().getResourceAsStream(report);
            JasperPrint JPrint = JasperFillManager.fillReport(path, new HashMap(), conn);
            JasperViewer.viewReport(JPrint, false);
        } catch (JRException ex) {
            JOptionPane.showMessageDialog(null, "Gagal Cetak Data Penjualan" + ex);
        }
    }
    //End Menu Laporan Penjualan
    
    // Function Menu Kelola Akun
    private void autoKodeUser() {
        try{   
            String sql = "SELECT MAX(right(no_user, 4))  AS no_user FROM users";        
            Connection conn = (Connection)Koneksi.getKoneksi();
            Statement stmt = conn.createStatement();
            ResultSet rs  = stmt.executeQuery(sql);
            while(rs.next()){
                if(rs.first()==false){
                    txtidUser.setText("USR-0001");
                }else{
                    rs.last();
                    int autoId = rs.getInt(1) + 1;
                    String nomor = String.valueOf(autoId);
                    int noUser = nomor.length();
                    
                    for(int i=0; i<4-noUser; i++){
                        nomor = "0" + nomor;
                    }
                    txtidUser.setText("USR-" + nomor);
                }
            }
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void nullTableUsers() {
        txtidUser.setEditable(true);
        txtidUser.setText(null);
        txtusername.setText(null);
        txtpassword.setText(null);
        txtnama.setText(null);
        cbUsersKelola.setSelectedItem(this);
    }
    
    private void tampilDataUsers() {
        DefaultTableModel tblUsers = new DefaultTableModel();
        tblUsers.addColumn("No");
        tblUsers.addColumn("Kode User");
        tblUsers.addColumn("Username");
        tblUsers.addColumn("Password");
        tblUsers.addColumn("Nama");
        tblUsers.addColumn("Level Akses");
        
        String cariUsers = txtcariUsers.getText();
        try {
            int no = 1;
            String sql = "SELECT * FROM users WHERE username LIKE'%"+cariUsers+"%'";
            Connection conn = (Connection)Koneksi.getKoneksi();
            Statement stm = conn.createStatement();
            ResultSet rs  = stm.executeQuery(sql);
            
            while(rs.next()) {
                tblUsers.addRow(new Object[]{
                    no++,
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6)
                });
            }
            tabelKelolaAkun.setModel(tblUsers);
        }catch (SQLException e){
           JOptionPane.showMessageDialog(null, "Tampil Data User Koneksi Database Gagal" + e.getMessage());
        } 
    }
    //End Menu Kelola Akun
    
    //Menu Laporan Keuangan
    private void tampilLaporanKeuangan() {
        DecimalFormat DF = new DecimalFormat("Rp #,###,###");
        DefaultTableModel tblKeuangan = new DefaultTableModel();
        tblKeuangan.addColumn("No");
        tblKeuangan.addColumn("Tgl Dibuat");
        tblKeuangan.addColumn("Total Penjualan");
        tblKeuangan.addColumn("Beban Pengeluaran");
        tblKeuangan.addColumn("Laba Bersih");
        
        String cariKeuangan = txtCariKeuangan.getText();
        try {
            String sql = "SELECT * FROM laporan_keuangan WHERE tgl_dibuat LIKE'%"+cariKeuangan+"%' ORDER BY tgl_dibuat DESC";
            Connection conn = (Connection)Koneksi.getKoneksi();
            Statement stm = conn.createStatement();
            ResultSet rs  = stm.executeQuery(sql);
            int no = 1;
            while(rs.next()) {
                tblKeuangan.addRow(new Object[]{
                    no++,
                    rs.getString(2),
                    DF.format(rs.getInt(3)),
                    DF.format(rs.getInt(4)),
                    DF.format(rs.getInt(5)),
                });
            }
            tabelKeuangan.setModel(tblKeuangan);
        }catch (SQLException e){
           JOptionPane.showMessageDialog(null, "Tabel Laporan Keuangan Koneksi Database Gagal" + e.getMessage());
        } 
    }
    
    private void cetakLaporanKeuangan() {
        try {     
            Connection conn = (Connection)Koneksi.getKoneksi();
            String report = "/reports/reportKeuangan.jasper";
            InputStream path = getClass().getResourceAsStream(report);
            JasperPrint JPrint = JasperFillManager.fillReport(path, new HashMap(), conn);
            JasperViewer.viewReport(JPrint, false);
        } catch (JRException ex) {
            JOptionPane.showMessageDialog(null, "Gagal Cetak Laporan Keuangan" + ex);
        }
    }
    
    private void cetakKeuanganByMonthYear() {
        try {      
            Connection conn = (Connection)Koneksi.getKoneksi();
            String report = "/reports/reportKeuanganByMonthYear.jasper";
            InputStream path = getClass().getResourceAsStream(report);
            HashMap hash = new HashMap();
            hash.put("byMonth", txtCariKeuangan.getText());
            JasperPrint JPrint = JasperFillManager.fillReport(path , hash, conn);
            JasperViewer.viewReport(JPrint, false);
        } catch (JRException ex) {
            JOptionPane.showMessageDialog(null, "Gagal Cetak Laporan Keuangan" + ex);
        }
    }
     
    //End Menu Laporan Keuangan
//* End All Function    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        body = new javax.swing.JPanel();
        sidebar = new javax.swing.JPanel();
        separatorLogo1 = new javax.swing.JSeparator();
        separatorMenuUtama = new javax.swing.JSeparator();
        labelUtama = new javax.swing.JLabel();
        separatorMenuUtama1 = new javax.swing.JSeparator();
        PengaturanLabel = new javax.swing.JLabel();
        Logo = new javax.swing.JLabel();
        separatorMenuUtama2 = new javax.swing.JSeparator();
        PengaturanLabel1 = new javax.swing.JLabel();
        labelUtama1 = new javax.swing.JLabel();
        separatorMenuUtama3 = new javax.swing.JSeparator();
        container = new javax.swing.JPanel();
        topbar = new javax.swing.JPanel();
        userArea = new javax.swing.JPanel();
        iconLogout = new javax.swing.JLabel();
        iconUserLog = new javax.swing.JLabel();
        menuTitle = new javax.swing.JLabel();
        footer = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        mainPanel = new javax.swing.JLayeredPane();
        dashboardPanel = new javax.swing.JPanel();
        labelCariNewTransaksi = new javax.swing.JLabel();
        dashboardCariNewTransaksi = new javax.swing.JTextField();
        tableTitle = new javax.swing.JLabel();
        tableTransaksiTerbaru = new javax.swing.JScrollPane();
        newTransaksi = new javax.swing.JTable();
        cardTotalStok = new javax.swing.JPanel();
        cardTitle1 = new javax.swing.JLabel();
        card1Subtitle = new javax.swing.JLabel();
        card1Image = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cardProdukSale = new javax.swing.JPanel();
        card2Image = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        card2Title = new javax.swing.JLabel();
        card2Subtitle = new javax.swing.JLabel();
        totalProdukTerjual = new javax.swing.JLabel();
        cardTransaksi = new javax.swing.JPanel();
        card3Image2 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        card3Title2 = new javax.swing.JLabel();
        card3Subtitle2 = new javax.swing.JLabel();
        totalTransaksi = new javax.swing.JLabel();
        cardTransaksi3 = new javax.swing.JPanel();
        card3Image3 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        card3Title3 = new javax.swing.JLabel();
        card3Subtitle3 = new javax.swing.JLabel();
        dataTotalTransaksi3 = new javax.swing.JLabel();
        cardPemasukan = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        dataTotalPemasukan = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        cardTotalPemasukan = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        totalPemasukan = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        cardLabaBersih = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        dataLabaRugi = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        cardKeuntungan = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        dataKeuntungan = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        cardPengeluaran = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        dataPengeluaran = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        panelDateDashboard = new javax.swing.JPanel();
        JcTahun = new com.toedter.calendar.JYearChooser();
        JcBulan = new javax.swing.JComboBox<>();
        btnSearchByDate = new javax.swing.JButton();
        refreshDashboard = new javax.swing.JButton();
        produkPanel = new javax.swing.JPanel();
        crudProdukPanel = new javax.swing.JPanel();
        labelKodeBarang = new javax.swing.JLabel();
        labelNamaBarang = new javax.swing.JLabel();
        labelKategori = new javax.swing.JLabel();
        labelHargaBeli = new javax.swing.JLabel();
        labelSupplier = new javax.swing.JLabel();
        labelCariProduk = new javax.swing.JLabel();
        txthargabeli = new javax.swing.JTextField();
        txtnamabarang = new javax.swing.JTextField();
        cbkategori = new javax.swing.JComboBox<>();
        txthargajual = new javax.swing.JTextField();
        btnSimpanProduk = new javax.swing.JButton();
        btnEditProduk = new javax.swing.JButton();
        btnHapusProduk = new javax.swing.JButton();
        txtstok = new javax.swing.JTextField();
        btnResetProduk = new javax.swing.JButton();
        txtcariProduk = new javax.swing.JTextField();
        txtkodebarang = new javax.swing.JTextField();
        StokLabel = new javax.swing.JLabel();
        btnCariSupplier = new javax.swing.JButton();
        labelHargaJual1 = new javax.swing.JLabel();
        tabelProdukPanel = new javax.swing.JScrollPane();
        tabelProduk = new javax.swing.JTable();
        supplierPanel = new javax.swing.JPanel();
        crudSupplierPanel = new javax.swing.JPanel();
        labelKodeSupplier = new javax.swing.JLabel();
        labelNamaBarang1 = new javax.swing.JLabel();
        labelAlamatSupplier = new javax.swing.JLabel();
        labelTlpSupplier = new javax.swing.JLabel();
        labelCariSupplier = new javax.swing.JLabel();
        txtNamaSupplier = new javax.swing.JTextField();
        txtTlpSupplier = new javax.swing.JTextField();
        btnSimpanSupplier = new javax.swing.JButton();
        btnEditSupplier = new javax.swing.JButton();
        btnHapusSupplier = new javax.swing.JButton();
        btnResetSupplier = new javax.swing.JButton();
        txtcariSupplier = new javax.swing.JTextField();
        txtKodeSupplier = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAlamatSupplier = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDeskripsi = new javax.swing.JTextArea();
        labelDeskripsi = new javax.swing.JLabel();
        tabelSupplierPanel = new javax.swing.JScrollPane();
        tabelSupplier = new javax.swing.JTable();
        penjualanPanel = new javax.swing.JPanel();
        panelTransaksi = new javax.swing.JPanel();
        labelJumlah = new javax.swing.JLabel();
        tambahKeranjang = new javax.swing.JButton();
        labelIdProduk = new javax.swing.JLabel();
        labelStok = new javax.swing.JLabel();
        labelTotal = new javax.swing.JLabel();
        labelHarga = new javax.swing.JLabel();
        labelTotalBayar = new javax.swing.JLabel();
        labellBayar = new javax.swing.JLabel();
        labelKembalian = new javax.swing.JLabel();
        tambahNota = new javax.swing.JButton();
        txtTotalKeseluruhan = new javax.swing.JTextField();
        txtKembalian = new javax.swing.JTextField();
        txtBayar = new javax.swing.JTextField();
        txtTotalBayar = new javax.swing.JTextField();
        labelNmBarang = new javax.swing.JLabel();
        resetTransaksi = new javax.swing.JButton();
        panelInputTransaksi = new javax.swing.JPanel();
        labelAutoKodeTransaksi = new javax.swing.JLabel();
        labelDateTransaksi = new javax.swing.JLabel();
        txtTgl = new javax.swing.JTextField();
        labelCustomer = new javax.swing.JLabel();
        btnCariCustomer = new javax.swing.JButton();
        btnCariProduk = new javax.swing.JButton();
        JptabelInputTransaksi = new javax.swing.JScrollPane();
        tabelInputTransaksi = new javax.swing.JTable();
        pengeluaranPanel = new javax.swing.JPanel();
        crudPengeluaranPanel = new javax.swing.JPanel();
        labelIdPengeluaran = new javax.swing.JLabel();
        labelJenisPengeluaran = new javax.swing.JLabel();
        labelTotalPengeluaran = new javax.swing.JLabel();
        labelCariPengeluaran = new javax.swing.JLabel();
        txtTotalPengeluaran = new javax.swing.JTextField();
        btnSimpanPengeluaran = new javax.swing.JButton();
        btnEditPengeluaran = new javax.swing.JButton();
        btnHapusPengeluaran = new javax.swing.JButton();
        btnResetPengeluaran = new javax.swing.JButton();
        txtcariPengeluaran = new javax.swing.JTextField();
        txtIdPengeluaran = new javax.swing.JTextField();
        labelTglPengeluaran = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtKeteranganPengeluaran = new javax.swing.JTextArea();
        KeteranganLabel = new javax.swing.JLabel();
        cbPengeluaran = new javax.swing.JComboBox<>();
        txtJumlahPengeluaran = new javax.swing.JTextField();
        labelJumlahPengeluaran = new javax.swing.JLabel();
        txtTglPengeluaran = new com.toedter.calendar.JDateChooser();
        tabelPengeluaranPanel = new javax.swing.JScrollPane();
        tabelPengeluaran = new javax.swing.JTable();
        customerPanel = new javax.swing.JPanel();
        crudCustomerPanel = new javax.swing.JPanel();
        labelKodeCustomer = new javax.swing.JLabel();
        labelNamaCustomer = new javax.swing.JLabel();
        labelAlamatCustomer = new javax.swing.JLabel();
        labelTlpCustomer = new javax.swing.JLabel();
        labelCariCustomer = new javax.swing.JLabel();
        txtNamaCustomer = new javax.swing.JTextField();
        txtTlpCustomer = new javax.swing.JTextField();
        btnSimpanCustomer = new javax.swing.JButton();
        btnEditCustomer = new javax.swing.JButton();
        btnHapusCustomer = new javax.swing.JButton();
        btnResetCustomer = new javax.swing.JButton();
        txtcariCustomer = new javax.swing.JTextField();
        txtKodeCustomer = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtAlamatCustomer = new javax.swing.JTextArea();
        tabelProdukPanel1 = new javax.swing.JScrollPane();
        tabelCustomer = new javax.swing.JTable();
        lapInventoryPanel = new javax.swing.JPanel();
        JPtabelInventory = new javax.swing.JScrollPane();
        tabelInventory = new javax.swing.JTable();
        cetakDataInventory = new javax.swing.JButton();
        panelTotalInventory = new javax.swing.JPanel();
        txtCariInventory = new javax.swing.JTextField();
        labelCariInventory = new javax.swing.JLabel();
        lapPengeluaranPanel = new javax.swing.JPanel();
        cetakDataPengeluaran = new javax.swing.JButton();
        labelCariLapPengeluaran = new javax.swing.JLabel();
        txtCariPengeluaran = new javax.swing.JTextField();
        tabelPengeluaranPanel1 = new javax.swing.JScrollPane();
        tabelLaporanPengeluaran = new javax.swing.JTable();
        labelCetakKeuanganByMonth1 = new javax.swing.JLabel();
        CetakPengeluaranByMonth = new javax.swing.JButton();
        JcBulanPengeluaran = new javax.swing.JComboBox<>();
        JcTahunPengeluaran = new com.toedter.calendar.JYearChooser();
        lapPenjualanPanel = new javax.swing.JPanel();
        JpTbPenjualan = new javax.swing.JScrollPane();
        tabelLapPenjualan = new javax.swing.JTable();
        txtCariPenjualan = new javax.swing.JTextField();
        labelCetakByInvoice = new javax.swing.JLabel();
        cetakDataByNoInvoice = new javax.swing.JButton();
        labelCariPenjualan1 = new javax.swing.JLabel();
        cetakDataeByNoInvoice1 = new javax.swing.JButton();
        labelCetakPenjualanByMonthYear = new javax.swing.JLabel();
        CetakPenjualanByMonthYear = new javax.swing.JButton();
        JcBulanPenjualan = new javax.swing.JComboBox<>();
        JcTahunPenjualan = new com.toedter.calendar.JYearChooser();
        kelolaAkunPanel = new javax.swing.JPanel();
        tabelKelolaAkunPanel = new javax.swing.JScrollPane();
        tabelKelolaAkun = new javax.swing.JTable();
        crudKelolaAkunPanel = new javax.swing.JPanel();
        labelId = new javax.swing.JLabel();
        labelUsernameKelola = new javax.swing.JLabel();
        labelPassKelola = new javax.swing.JLabel();
        labelCariAkun = new javax.swing.JLabel();
        txtusername = new javax.swing.JTextField();
        cbUsersKelola = new javax.swing.JComboBox<>();
        txtpassword = new javax.swing.JTextField();
        btnSimpanUsers = new javax.swing.JButton();
        btnEditUsers = new javax.swing.JButton();
        btnHapusUsers = new javax.swing.JButton();
        txtnama = new javax.swing.JTextField();
        btnResetUsers = new javax.swing.JButton();
        txtcariUsers = new javax.swing.JTextField();
        labelNamaKelola = new javax.swing.JLabel();
        txtidUser = new javax.swing.JTextField();
        labelCbUser = new javax.swing.JLabel();
        lapKeuanganPanel = new javax.swing.JPanel();
        JPtabelInventory1 = new javax.swing.JScrollPane();
        tabelKeuangan = new javax.swing.JTable();
        cetakDataKeuangan = new javax.swing.JButton();
        txtCariKeuangan = new javax.swing.JTextField();
        labelCariKeuangan = new javax.swing.JLabel();
        labelCetakKeuanganByMonth = new javax.swing.JLabel();
        CetakKeuanganByMonth = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("POINT OF SALE - SUMBER REJEKI");
        setBackground(new java.awt.Color(0, 0, 0));
        setIconImages(null);
        setMaximumSize(new java.awt.Dimension(970, 569));
        setMinimumSize(new java.awt.Dimension(970, 569));
        setPreferredSize(new java.awt.Dimension(970, 569));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        body.setBackground(new java.awt.Color(255, 0, 102));
        body.setMaximumSize(new java.awt.Dimension(970, 548));
        body.setMinimumSize(new java.awt.Dimension(970, 548));
        body.setPreferredSize(new java.awt.Dimension(970, 548));
        body.setLayout(new javax.swing.BoxLayout(body, javax.swing.BoxLayout.LINE_AXIS));

        sidebar.setBackground(new java.awt.Color(64, 64, 122));
        sidebar.setMaximumSize(new java.awt.Dimension(220, 569));
        sidebar.setMinimumSize(new java.awt.Dimension(220, 569));
        sidebar.setPreferredSize(new java.awt.Dimension(220, 569));
        sidebar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dashboard.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        dashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/dashboard.png"))); // NOI18N
        dashboard.setText("Dashboard");
        dashboard.setFocusPainted(false);
        dashboard.setFocusable(false);
        dashboard.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        dashboard.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        dashboard.setIconTextGap(30);
        dashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dashboardActionPerformed(evt);
            }
        });
        sidebar.add(dashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 190, -1));

        separatorLogo1.setForeground(new java.awt.Color(255, 255, 255));
        separatorLogo1.setAlignmentX(0.8F);
        separatorLogo1.setAlignmentY(0.8F);
        sidebar.add(separatorLogo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 200, 10));

        separatorMenuUtama.setForeground(new java.awt.Color(255, 255, 255));
        sidebar.add(separatorMenuUtama, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 150, 110, 10));

        labelUtama.setBackground(new java.awt.Color(255, 255, 255));
        labelUtama.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        labelUtama.setForeground(new java.awt.Color(255, 255, 255));
        labelUtama.setText("Menu Utama");
        labelUtama.setToolTipText("");
        sidebar.add(labelUtama, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 80, -1));

        produkMenu.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        produkMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/produk.png"))); // NOI18N
        produkMenu.setText("Produk");
        produkMenu.setFocusPainted(false);
        produkMenu.setFocusable(false);
        produkMenu.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        produkMenu.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        produkMenu.setIconTextGap(30);
        produkMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                produkMenuActionPerformed(evt);
            }
        });
        sidebar.add(produkMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 190, -1));

        separatorMenuUtama1.setForeground(new java.awt.Color(255, 255, 255));
        sidebar.add(separatorMenuUtama1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 460, 120, 10));

        PengaturanLabel.setBackground(new java.awt.Color(255, 255, 255));
        PengaturanLabel.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        PengaturanLabel.setForeground(new java.awt.Color(255, 255, 255));
        PengaturanLabel.setText("Pengaturan");
        PengaturanLabel.setToolTipText("");
        sidebar.add(PengaturanLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 450, 80, -1));

        Logo.setBackground(new java.awt.Color(64, 64, 122));
        Logo.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        Logo.setForeground(new java.awt.Color(245, 245, 250));
        Logo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/img/tsr-dashboard.png"))); // NOI18N
        Logo.setText("TSR - POS");
        Logo.setIconTextGap(10);
        Logo.setOpaque(true);
        sidebar.add(Logo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 200, 50));

        separatorMenuUtama2.setForeground(new java.awt.Color(255, 255, 255));
        sidebar.add(separatorMenuUtama2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 320, 140, 10));

        lapKeuanganMenu.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lapKeuanganMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/bars.png"))); // NOI18N
        lapKeuanganMenu.setText("Laporan Keuangan");
        lapKeuanganMenu.setFocusPainted(false);
        lapKeuanganMenu.setFocusable(false);
        lapKeuanganMenu.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lapKeuanganMenu.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lapKeuanganMenu.setIconTextGap(25);
        lapKeuanganMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lapKeuanganMenuActionPerformed(evt);
            }
        });
        sidebar.add(lapKeuanganMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 420, 190, -1));

        lapPenjualanMenu.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lapPenjualanMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/lapPenjualan.png"))); // NOI18N
        lapPenjualanMenu.setText("Laporan Penjualan");
        lapPenjualanMenu.setFocusPainted(false);
        lapPenjualanMenu.setFocusable(false);
        lapPenjualanMenu.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lapPenjualanMenu.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lapPenjualanMenu.setIconTextGap(25);
        lapPenjualanMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lapPenjualanMenuActionPerformed(evt);
            }
        });
        sidebar.add(lapPenjualanMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, 190, -1));

        PengaturanLabel1.setBackground(new java.awt.Color(255, 255, 255));
        PengaturanLabel1.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        PengaturanLabel1.setForeground(new java.awt.Color(255, 255, 255));
        PengaturanLabel1.setText("Laporan");
        PengaturanLabel1.setToolTipText("");
        sidebar.add(PengaturanLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 80, -1));

        customerMenu.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        customerMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/customer.png"))); // NOI18N
        customerMenu.setText("Customer");
        customerMenu.setFocusPainted(false);
        customerMenu.setFocusable(false);
        customerMenu.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        customerMenu.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        customerMenu.setIconTextGap(30);
        customerMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerMenuActionPerformed(evt);
            }
        });
        sidebar.add(customerMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 190, -1));

        supplierMenu.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        supplierMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/delivery.png"))); // NOI18N
        supplierMenu.setText("Supplier");
        supplierMenu.setFocusPainted(false);
        supplierMenu.setFocusable(false);
        supplierMenu.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        supplierMenu.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        supplierMenu.setIconTextGap(30);
        supplierMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierMenuActionPerformed(evt);
            }
        });
        sidebar.add(supplierMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 190, -1));

        penjualanMenu.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        penjualanMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/sale.png"))); // NOI18N
        penjualanMenu.setText("Penjualan");
        penjualanMenu.setFocusPainted(false);
        penjualanMenu.setFocusable(false);
        penjualanMenu.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        penjualanMenu.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        penjualanMenu.setIconTextGap(30);
        penjualanMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                penjualanMenuActionPerformed(evt);
            }
        });
        sidebar.add(penjualanMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 190, -1));

        pengeluaranMenu.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        pengeluaranMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/pengeluaran.png"))); // NOI18N
        pengeluaranMenu.setText("Pengeluaran");
        pengeluaranMenu.setFocusPainted(false);
        pengeluaranMenu.setFocusable(false);
        pengeluaranMenu.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        pengeluaranMenu.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        pengeluaranMenu.setIconTextGap(30);
        pengeluaranMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pengeluaranMenuActionPerformed(evt);
            }
        });
        sidebar.add(pengeluaranMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 190, -1));

        labelUtama1.setBackground(new java.awt.Color(255, 255, 255));
        labelUtama1.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        labelUtama1.setForeground(new java.awt.Color(255, 255, 255));
        labelUtama1.setText("Overview");
        labelUtama1.setToolTipText("");
        sidebar.add(labelUtama1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 80, -1));

        separatorMenuUtama3.setForeground(new java.awt.Color(255, 255, 255));
        sidebar.add(separatorMenuUtama3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 90, 130, 10));

        lapPengeluaranMenu.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lapPengeluaranMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/lapPengeluaran.png"))); // NOI18N
        lapPengeluaranMenu.setText("Laporan Pengeluaran");
        lapPengeluaranMenu.setFocusPainted(false);
        lapPengeluaranMenu.setFocusable(false);
        lapPengeluaranMenu.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lapPengeluaranMenu.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lapPengeluaranMenu.setIconTextGap(25);
        lapPengeluaranMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lapPengeluaranMenuActionPerformed(evt);
            }
        });
        sidebar.add(lapPengeluaranMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, 190, -1));

        lapProdukMenu.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lapProdukMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/lapProduk.png"))); // NOI18N
        lapProdukMenu.setText("Laporan Inventory");
        lapProdukMenu.setFocusPainted(false);
        lapProdukMenu.setFocusable(false);
        lapProdukMenu.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lapProdukMenu.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lapProdukMenu.setIconTextGap(25);
        lapProdukMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lapProdukMenuActionPerformed(evt);
            }
        });
        sidebar.add(lapProdukMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 190, -1));

        kelolaAkunMenu.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        kelolaAkunMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/userSet.png"))); // NOI18N
        kelolaAkunMenu.setText("Kelola Akun");
        kelolaAkunMenu.setFocusPainted(false);
        kelolaAkunMenu.setFocusable(false);
        kelolaAkunMenu.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        kelolaAkunMenu.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        kelolaAkunMenu.setIconTextGap(25);
        kelolaAkunMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kelolaAkunMenuActionPerformed(evt);
            }
        });
        sidebar.add(kelolaAkunMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 470, 190, -1));

        body.add(sidebar);

        container.setBackground(new java.awt.Color(204, 204, 204));
        container.setMaximumSize(new java.awt.Dimension(970, 548));
        container.setMinimumSize(new java.awt.Dimension(970, 548));
        container.setPreferredSize(new java.awt.Dimension(970, 548));
        container.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        topbar.setBackground(new java.awt.Color(245, 246, 250));
        topbar.setMaximumSize(new java.awt.Dimension(200, 60));
        topbar.setMinimumSize(new java.awt.Dimension(200, 60));
        topbar.setPreferredSize(new java.awt.Dimension(200, 60));
        topbar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        userArea.setBackground(new java.awt.Color(245, 246, 250));
        userArea.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconLogout.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        iconLogout.setForeground(new java.awt.Color(64, 64, 122));
        iconLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/logout-top.png"))); // NOI18N
        iconLogout.setText("Logout");
        iconLogout.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        iconLogout.setIconTextGap(7);
        iconLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconLogoutMouseClicked(evt);
            }
        });
        userArea.add(iconLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(274, 0, 80, 60));

        iconUserLog.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/userLogLabel.png"))); // NOI18N
        iconUserLog.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconUserLogMouseClicked(evt);
            }
        });
        userArea.add(iconUserLog, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, -1, 60));

        nameLog.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        nameLog.setForeground(new java.awt.Color(64, 64, 122));
        nameLog.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nameLog.setText("Selamat Datang :)");
        nameLog.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        nameLog.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                nameLogMouseClicked(evt);
            }
        });
        userArea.add(nameLog, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 210, 20));

        divide.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        divide.setForeground(new java.awt.Color(64, 64, 122));
        divide.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        divide.setText("|");
        divide.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        divide.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                divideMouseClicked(evt);
            }
        });
        userArea.add(divide, new org.netbeans.lib.awtextra.AbsoluteConstraints(255, 20, -1, -1));

        topbar.add(userArea, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 0, 380, 60));

        menuTitle.setBackground(new java.awt.Color(64, 64, 122));
        menuTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        menuTitle.setForeground(new java.awt.Color(64, 64, 122));
        menuTitle.setText("Overview | Dashboard");
        menuTitle.setMaximumSize(new java.awt.Dimension(103, 25));
        menuTitle.setMinimumSize(new java.awt.Dimension(103, 25));
        menuTitle.setPreferredSize(new java.awt.Dimension(103, 25));
        topbar.add(menuTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, 310, 30));

        container.add(topbar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 750, -1));

        footer.setBackground(new java.awt.Color(245, 246, 250));
        footer.setMaximumSize(new java.awt.Dimension(750, 15));
        footer.setMinimumSize(new java.awt.Dimension(750, 15));
        footer.setPreferredSize(new java.awt.Dimension(750, 15));
        footer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(64, 64, 122));
        jLabel1.setText("Bagas Arya Pradipta | 201843500707");
        footer.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 10, 190, -1));

        container.add(footer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 500, 770, 50));
        footer.getAccessibleContext().setAccessibleParent(body);

        mainPanel.setBackground(new java.awt.Color(222, 222, 229));
        mainPanel.setMaximumSize(new java.awt.Dimension(974, 569));
        mainPanel.setMinimumSize(new java.awt.Dimension(974, 569));
        mainPanel.setLayout(new java.awt.CardLayout());

        dashboardPanel.setBackground(new java.awt.Color(222, 222, 229));
        dashboardPanel.setMaximumSize(new java.awt.Dimension(974, 569));
        dashboardPanel.setMinimumSize(new java.awt.Dimension(974, 569));
        dashboardPanel.setPreferredSize(new java.awt.Dimension(974, 569));
        dashboardPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelCariNewTransaksi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelCariNewTransaksi.setText("Cari :");
        dashboardPanel.add(labelCariNewTransaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 240, 40, 30));

        dashboardCariNewTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dashboardCariNewTransaksiActionPerformed(evt);
            }
        });
        dashboardCariNewTransaksi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                dashboardCariNewTransaksiKeyReleased(evt);
            }
        });
        dashboardPanel.add(dashboardCariNewTransaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 240, 180, 30));

        tableTitle.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        tableTitle.setForeground(new java.awt.Color(64, 64, 122));
        tableTitle.setText("Transaksi Terbaru");
        dashboardPanel.add(tableTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 240, -1, -1));

        newTransaksi.setBackground(new java.awt.Color(244, 244, 250));
        newTransaksi.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        newTransaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "No Transaksi", "Customer", "Kode Barang", "Nama Barang", "Jumlah", "Dibayar", "Tgl-Transaksi"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false, false, false, true, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        newTransaksi.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        newTransaksi.setGridColor(new java.awt.Color(222, 222, 229));
        newTransaksi.setName("dashboard"); // NOI18N
        newTransaksi.setOpaque(false);
        newTransaksi.setRequestFocusEnabled(false);
        newTransaksi.setRowHeight(25);
        newTransaksi.setSelectionBackground(new java.awt.Color(64, 64, 122));
        newTransaksi.setSelectionForeground(new java.awt.Color(245, 245, 250));
        newTransaksi.setShowVerticalLines(false);
        tableTransaksiTerbaru.setViewportView(newTransaksi);

        dashboardPanel.add(tableTransaksiTerbaru, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 275, 690, 160));

        cardTotalStok.setBackground(new java.awt.Color(245, 245, 250));
        cardTotalStok.setPreferredSize(new java.awt.Dimension(150, 85));
        cardTotalStok.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cardTitle1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        cardTitle1.setForeground(new java.awt.Color(64, 64, 122));
        cardTitle1.setText("Total");
        cardTitle1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        cardTotalStok.add(cardTitle1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 13, 88, 20));

        card1Subtitle.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        card1Subtitle.setForeground(new java.awt.Color(64, 64, 122));
        card1Subtitle.setText("Stok Produk");
        card1Subtitle.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        cardTotalStok.add(card1Subtitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 28, 88, 20));

        card1Image.setBackground(new java.awt.Color(64, 64, 122));
        card1Image.setPreferredSize(new java.awt.Dimension(50, 84));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/package.png"))); // NOI18N

        javax.swing.GroupLayout card1ImageLayout = new javax.swing.GroupLayout(card1Image);
        card1Image.setLayout(card1ImageLayout);
        card1ImageLayout.setHorizontalGroup(
            card1ImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card1ImageLayout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        card1ImageLayout.setVerticalGroup(
            card1ImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        cardTotalStok.add(card1Image, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 85));

        totalStokProduk.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        totalStokProduk.setForeground(new java.awt.Color(35, 36, 54));
        totalStokProduk.setText("99");
        totalStokProduk.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        totalStokProduk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                totalStokProdukKeyReleased(evt);
            }
        });
        cardTotalStok.add(totalStokProduk, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 49, 88, 30));

        dashboardPanel.add(cardTotalStok, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 47, -1, -1));

        cardProdukSale.setBackground(new java.awt.Color(245, 245, 250));
        cardProdukSale.setMaximumSize(new java.awt.Dimension(148, 85));
        cardProdukSale.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        card2Image.setBackground(new java.awt.Color(64, 64, 122));

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/shopping-bag.png"))); // NOI18N

        javax.swing.GroupLayout card2ImageLayout = new javax.swing.GroupLayout(card2Image);
        card2Image.setLayout(card2ImageLayout);
        card2ImageLayout.setHorizontalGroup(
            card2ImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );
        card2ImageLayout.setVerticalGroup(
            card2ImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
        );

        cardProdukSale.add(card2Image, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 85));

        card2Title.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        card2Title.setForeground(new java.awt.Color(64, 64, 122));
        card2Title.setText("Produk");
        card2Title.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        cardProdukSale.add(card2Title, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 13, 88, 20));

        card2Subtitle.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        card2Subtitle.setForeground(new java.awt.Color(64, 64, 122));
        card2Subtitle.setText("Terjual");
        card2Subtitle.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        cardProdukSale.add(card2Subtitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 28, 88, 20));

        totalProdukTerjual.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        totalProdukTerjual.setForeground(new java.awt.Color(35, 36, 54));
        totalProdukTerjual.setText("99");
        totalProdukTerjual.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        cardProdukSale.add(totalProdukTerjual, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 49, 88, 30));

        dashboardPanel.add(cardProdukSale, new org.netbeans.lib.awtextra.AbsoluteConstraints(203, 47, 150, -1));

        cardTransaksi.setBackground(new java.awt.Color(245, 245, 250));
        cardTransaksi.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        card3Image2.setBackground(new java.awt.Color(235, 77, 75));

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/sales.png"))); // NOI18N

        javax.swing.GroupLayout card3Image2Layout = new javax.swing.GroupLayout(card3Image2);
        card3Image2.setLayout(card3Image2Layout);
        card3Image2Layout.setHorizontalGroup(
            card3Image2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );
        card3Image2Layout.setVerticalGroup(
            card3Image2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
        );

        cardTransaksi.add(card3Image2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        card3Title2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        card3Title2.setForeground(new java.awt.Color(235, 77, 75));
        card3Title2.setText("Total");
        card3Title2.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        cardTransaksi.add(card3Title2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 13, 88, 20));

        card3Subtitle2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        card3Subtitle2.setForeground(new java.awt.Color(235, 77, 75));
        card3Subtitle2.setText("Transaksi");
        card3Subtitle2.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        cardTransaksi.add(card3Subtitle2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 28, 88, 20));

        totalTransaksi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        totalTransaksi.setForeground(new java.awt.Color(35, 36, 54));
        totalTransaksi.setText("99");
        totalTransaksi.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        cardTransaksi.add(totalTransaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 49, 88, 30));

        cardTransaksi3.setBackground(new java.awt.Color(245, 245, 250));
        cardTransaksi3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        card3Image3.setBackground(new java.awt.Color(54, 123, 225));

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout card3Image3Layout = new javax.swing.GroupLayout(card3Image3);
        card3Image3.setLayout(card3Image3Layout);
        card3Image3Layout.setHorizontalGroup(
            card3Image3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );
        card3Image3Layout.setVerticalGroup(
            card3Image3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
        );

        cardTransaksi3.add(card3Image3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        card3Title3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        card3Title3.setForeground(new java.awt.Color(54, 123, 225));
        card3Title3.setText("Total");
        card3Title3.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        cardTransaksi3.add(card3Title3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 13, 88, 20));

        card3Subtitle3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        card3Subtitle3.setForeground(new java.awt.Color(54, 123, 225));
        card3Subtitle3.setText("Transaksi");
        card3Subtitle3.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        cardTransaksi3.add(card3Subtitle3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 28, 88, 20));

        dataTotalTransaksi3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        dataTotalTransaksi3.setForeground(new java.awt.Color(35, 36, 54));
        dataTotalTransaksi3.setText("0");
        dataTotalTransaksi3.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        cardTransaksi3.add(dataTotalTransaksi3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 49, 88, 30));

        cardTransaksi.add(cardTransaksi3, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 60, 150, 85));

        dashboardPanel.add(cardTransaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 47, 150, 85));

        cardPemasukan.setBackground(new java.awt.Color(245, 245, 250));
        cardPemasukan.setMaximumSize(new java.awt.Dimension(148, 85));
        cardPemasukan.setMinimumSize(new java.awt.Dimension(148, 85));
        cardPemasukan.setPreferredSize(new java.awt.Dimension(148, 85));
        cardPemasukan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(95, 39, 205));

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/growth.png"))); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
        );

        cardPemasukan.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        dataTotalPemasukan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        dataTotalPemasukan.setForeground(new java.awt.Color(35, 36, 54));
        dataTotalPemasukan.setText("999999");
        dataTotalPemasukan.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        cardPemasukan.add(dataTotalPemasukan, new org.netbeans.lib.awtextra.AbsoluteConstraints(76, 49, 95, 30));

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(95, 39, 205));
        jLabel29.setText("Pemasukan");
        jLabel29.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        cardPemasukan.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 13, 88, 20));

        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(95, 39, 205));
        jLabel30.setText("Bulan Ini");
        jLabel30.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        cardPemasukan.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 28, 88, 20));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(35, 36, 54));
        jLabel21.setText("Rp");
        cardPemasukan.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 20, -1));

        dashboardPanel.add(cardPemasukan, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 145, 150, -1));

        cardTotalPemasukan.setBackground(new java.awt.Color(245, 245, 250));
        cardTotalPemasukan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.setBackground(new java.awt.Color(95, 39, 205));

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/banknote.png"))); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
        );

        cardTotalPemasukan.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        totalPemasukan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        totalPemasukan.setForeground(new java.awt.Color(35, 36, 54));
        totalPemasukan.setText("999999");
        totalPemasukan.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        cardTotalPemasukan.add(totalPemasukan, new org.netbeans.lib.awtextra.AbsoluteConstraints(76, 49, 80, 30));

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(95, 39, 205));
        jLabel33.setText("Total");
        jLabel33.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        cardTotalPemasukan.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 13, 88, 20));

        jLabel34.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(95, 39, 205));
        jLabel34.setText("Pemasukan");
        jLabel34.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        cardTotalPemasukan.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 28, 88, 20));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(35, 36, 54));
        jLabel25.setText("Rp");
        cardTotalPemasukan.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 20, -1));

        dashboardPanel.add(cardTotalPemasukan, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 145, 150, -1));

        cardLabaBersih.setBackground(new java.awt.Color(245, 245, 250));
        cardLabaBersih.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel8.setBackground(new java.awt.Color(34, 166, 179));

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/pie-chart.png"))); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
        );

        cardLabaBersih.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        dataLabaRugi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        dataLabaRugi.setForeground(new java.awt.Color(35, 36, 54));
        dataLabaRugi.setText("999999");
        dataLabaRugi.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        cardLabaBersih.add(dataLabaRugi, new org.netbeans.lib.awtextra.AbsoluteConstraints(76, 49, 100, 30));

        jLabel35.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(34, 166, 179));
        jLabel35.setText("Total");
        jLabel35.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        cardLabaBersih.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 13, 88, 20));

        jLabel36.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(34, 166, 179));
        jLabel36.setText("Laba Bersih");
        jLabel36.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        cardLabaBersih.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 28, 88, 20));

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(35, 36, 54));
        jLabel27.setText("Rp");
        cardLabaBersih.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 20, -1));

        dashboardPanel.add(cardLabaBersih, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 145, 180, -1));

        cardKeuntungan.setBackground(new java.awt.Color(245, 245, 250));
        cardKeuntungan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel10.setBackground(new java.awt.Color(34, 166, 179));

        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/money-bag.png"))); // NOI18N

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
        );

        cardKeuntungan.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        dataKeuntungan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        dataKeuntungan.setForeground(new java.awt.Color(35, 36, 54));
        dataKeuntungan.setText("999999");
        dataKeuntungan.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        cardKeuntungan.add(dataKeuntungan, new org.netbeans.lib.awtextra.AbsoluteConstraints(76, 49, 80, 30));

        jLabel39.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(34, 166, 179));
        jLabel39.setText("Keuntungan");
        jLabel39.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        cardKeuntungan.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 13, 88, 20));

        jLabel40.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(34, 166, 179));
        jLabel40.setText("Bulan Ini");
        jLabel40.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        cardKeuntungan.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 28, 88, 20));

        jLabel41.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(35, 36, 54));
        jLabel41.setText("Rp");
        cardKeuntungan.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 20, -1));

        dashboardPanel.add(cardKeuntungan, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 145, 150, -1));

        cardPengeluaran.setBackground(new java.awt.Color(245, 245, 250));
        cardPengeluaran.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel9.setBackground(new java.awt.Color(235, 77, 75));

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/outcome.png"))); // NOI18N

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
        );

        cardPengeluaran.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        dataPengeluaran.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        dataPengeluaran.setForeground(new java.awt.Color(35, 36, 54));
        dataPengeluaran.setText("999999");
        dataPengeluaran.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        cardPengeluaran.add(dataPengeluaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(76, 49, 70, 30));

        jLabel37.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(235, 77, 75));
        jLabel37.setText("Pengeluaran");
        jLabel37.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        cardPengeluaran.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 13, 88, 20));

        jLabel38.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(235, 77, 75));
        jLabel38.setText("Bulan Ini");
        jLabel38.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        cardPengeluaran.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 28, 88, 20));

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(35, 36, 54));
        jLabel31.setText("Rp");
        cardPengeluaran.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 20, -1));

        dashboardPanel.add(cardPengeluaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 47, 180, 85));

        panelDateDashboard.setBackground(new java.awt.Color(222, 222, 229));

        JcBulan.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        JcBulan.setForeground(new java.awt.Color(64, 64, 122));
        JcBulan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December", " " }));

        btnSearchByDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/search.png"))); // NOI18N
        btnSearchByDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchByDateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelDateDashboardLayout = new javax.swing.GroupLayout(panelDateDashboard);
        panelDateDashboard.setLayout(panelDateDashboardLayout);
        panelDateDashboardLayout.setHorizontalGroup(
            panelDateDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDateDashboardLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSearchByDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(JcBulan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JcTahun, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(118, 118, 118))
        );
        panelDateDashboardLayout.setVerticalGroup(
            panelDateDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDateDashboardLayout.createSequentialGroup()
                .addGroup(panelDateDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelDateDashboardLayout.createSequentialGroup()
                        .addComponent(JcTahun, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(JcBulan, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSearchByDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        dashboardPanel.add(panelDateDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 5, 220, 30));

        refreshDashboard.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        refreshDashboard.setForeground(new java.awt.Color(64, 64, 122));
        refreshDashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/undo-circular-arrow.png"))); // NOI18N
        refreshDashboard.setText("REFRESH");
        refreshDashboard.setIconTextGap(8);
        refreshDashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshDashboardActionPerformed(evt);
            }
        });
        dashboardPanel.add(refreshDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(615, 3, -1, -1));

        mainPanel.add(dashboardPanel, "card2");

        produkPanel.setBackground(new java.awt.Color(222, 222, 229));
        produkPanel.setMaximumSize(new java.awt.Dimension(974, 569));
        produkPanel.setMinimumSize(new java.awt.Dimension(974, 569));
        produkPanel.setPreferredSize(new java.awt.Dimension(974, 569));
        produkPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        crudProdukPanel.setBackground(new java.awt.Color(222, 222, 229));
        crudProdukPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelKodeBarang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelKodeBarang.setText("Kode Barang");
        crudProdukPanel.add(labelKodeBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        labelNamaBarang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelNamaBarang.setText("Nama Barang");
        crudProdukPanel.add(labelNamaBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, -1));

        labelKategori.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelKategori.setText("Kategori");
        crudProdukPanel.add(labelKategori, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, -1));

        labelHargaBeli.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelHargaBeli.setText("Harga Beli");
        crudProdukPanel.add(labelHargaBeli, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, -1, -1));

        labelSupplier.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelSupplier.setText("Supplier");
        crudProdukPanel.add(labelSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 140, -1, -1));

        labelCariProduk.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelCariProduk.setText("Cari :");
        crudProdukPanel.add(labelCariProduk, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 170, -1, 30));

        txthargabeli.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txthargabeli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txthargabeliKeyTyped(evt);
            }
        });
        crudProdukPanel.add(txthargabeli, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 40, 150, 30));

        txtnamabarang.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txtnamabarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnamabarangActionPerformed(evt);
            }
        });
        crudProdukPanel.add(txtnamabarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 150, 30));

        cbkategori.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        cbkategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Makanan", "Minuman", "ATK", "Sembako", "Lainnya" }));
        cbkategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbkategoriActionPerformed(evt);
            }
        });
        crudProdukPanel.add(cbkategori, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 150, 30));

        txthargajual.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txthargajual.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txthargajualKeyTyped(evt);
            }
        });
        crudProdukPanel.add(txthargajual, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 100, 150, 30));

        btnSimpanProduk.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnSimpanProduk.setForeground(new java.awt.Color(64, 64, 122));
        btnSimpanProduk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/save.png"))); // NOI18N
        btnSimpanProduk.setText("SIMPAN");
        btnSimpanProduk.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSimpanProduk.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSimpanProduk.setIconTextGap(10);
        btnSimpanProduk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanProdukActionPerformed(evt);
            }
        });
        crudProdukPanel.add(btnSimpanProduk, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 30, 120, -1));

        btnEditProduk.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnEditProduk.setForeground(new java.awt.Color(64, 64, 122));
        btnEditProduk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/editing.png"))); // NOI18N
        btnEditProduk.setText("UBAH");
        btnEditProduk.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnEditProduk.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnEditProduk.setIconTextGap(10);
        btnEditProduk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditProdukActionPerformed(evt);
            }
        });
        crudProdukPanel.add(btnEditProduk, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 80, 120, -1));

        btnHapusProduk.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnHapusProduk.setForeground(new java.awt.Color(64, 64, 122));
        btnHapusProduk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/trash.png"))); // NOI18N
        btnHapusProduk.setText("HAPUS");
        btnHapusProduk.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnHapusProduk.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnHapusProduk.setIconTextGap(8);
        btnHapusProduk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusProdukActionPerformed(evt);
            }
        });
        crudProdukPanel.add(btnHapusProduk, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 30, 110, -1));

        txtstok.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txtstok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtstokActionPerformed(evt);
            }
        });
        txtstok.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtstokKeyTyped(evt);
            }
        });
        crudProdukPanel.add(txtstok, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 40, 120, 30));

        btnResetProduk.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnResetProduk.setForeground(new java.awt.Color(64, 64, 122));
        btnResetProduk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/undo-circular-arrow.png"))); // NOI18N
        btnResetProduk.setText("RESET");
        btnResetProduk.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnResetProduk.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnResetProduk.setIconTextGap(10);
        btnResetProduk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetProdukActionPerformed(evt);
            }
        });
        crudProdukPanel.add(btnResetProduk, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 80, 110, -1));

        txtcariProduk.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txtcariProduk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtcariProdukKeyReleased(evt);
            }
        });
        crudProdukPanel.add(txtcariProduk, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 170, 240, 30));

        txtkodebarang.setEditable(false);
        txtkodebarang.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        txtkodebarang.setDisabledTextColor(new java.awt.Color(64, 64, 122));
        txtkodebarang.setEnabled(false);
        txtkodebarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtkodebarangActionPerformed(evt);
            }
        });
        crudProdukPanel.add(txtkodebarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 150, 30));

        StokLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        StokLabel.setText("Stok");
        crudProdukPanel.add(StokLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 20, -1, -1));

        txNamaSupplier.setEditable(false);
        txNamaSupplier.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txNamaSupplier.setDisabledTextColor(new java.awt.Color(64, 64, 122));
        txNamaSupplier.setEnabled(false);
        txNamaSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txNamaSupplierActionPerformed(evt);
            }
        });
        crudProdukPanel.add(txNamaSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 160, 110, 30));

        txtIdSupplier.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txtIdSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdSupplierActionPerformed(evt);
            }
        });
        crudProdukPanel.add(txtIdSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 100, 0, -1));

        btnCariSupplier.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCariSupplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/search.png"))); // NOI18N
        btnCariSupplier.setIconTextGap(10);
        btnCariSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariSupplierActionPerformed(evt);
            }
        });
        crudProdukPanel.add(btnCariSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 160, 30, 30));

        labelHargaJual1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelHargaJual1.setText("Harga Jual");
        crudProdukPanel.add(labelHargaJual1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 80, -1, -1));

        produkPanel.add(crudProdukPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 750, 210));

        tabelProduk.setBackground(new java.awt.Color(244, 244, 250));
        tabelProduk.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        tabelProduk.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Kode Barang", "Nama Barang", "Kategori", "Harga Beli", "Harga Jual", "Satuan", "Stok"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelProduk.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tabelProduk.setGridColor(new java.awt.Color(222, 222, 229));
        tabelProduk.setName("dashboard"); // NOI18N
        tabelProduk.setOpaque(false);
        tabelProduk.setRequestFocusEnabled(false);
        tabelProduk.setRowHeight(25);
        tabelProduk.setSelectionBackground(new java.awt.Color(64, 64, 122));
        tabelProduk.setSelectionForeground(new java.awt.Color(245, 245, 250));
        tabelProduk.setShowVerticalLines(false);
        tabelProduk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelProdukMouseClicked(evt);
            }
        });
        tabelProdukPanel.setViewportView(tabelProduk);
        tabelProduk.getAccessibleContext().setAccessibleParent(tabelProduk);

        produkPanel.add(tabelProdukPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, 700, 210));

        mainPanel.add(produkPanel, "card3");

        supplierPanel.setBackground(new java.awt.Color(222, 222, 229));
        supplierPanel.setMaximumSize(new java.awt.Dimension(974, 569));
        supplierPanel.setMinimumSize(new java.awt.Dimension(974, 569));
        supplierPanel.setPreferredSize(new java.awt.Dimension(974, 569));
        supplierPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        crudSupplierPanel.setBackground(new java.awt.Color(222, 222, 229));
        crudSupplierPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelKodeSupplier.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelKodeSupplier.setText("Kode Supplier");
        crudSupplierPanel.add(labelKodeSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        labelNamaBarang1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelNamaBarang1.setText("Nama Supplier");
        crudSupplierPanel.add(labelNamaBarang1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, -1));

        labelAlamatSupplier.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelAlamatSupplier.setText("Alamat Supplier");
        crudSupplierPanel.add(labelAlamatSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, -1, -1));

        labelTlpSupplier.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelTlpSupplier.setText("No Tlp");
        crudSupplierPanel.add(labelTlpSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, -1));

        labelCariSupplier.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelCariSupplier.setText("Cari :");
        crudSupplierPanel.add(labelCariSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 210, -1, 30));

        txtNamaSupplier.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txtNamaSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaSupplierActionPerformed(evt);
            }
        });
        crudSupplierPanel.add(txtNamaSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 150, 30));

        txtTlpSupplier.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txtTlpSupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTlpSupplierKeyTyped(evt);
            }
        });
        crudSupplierPanel.add(txtTlpSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 150, 30));

        btnSimpanSupplier.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnSimpanSupplier.setForeground(new java.awt.Color(64, 64, 122));
        btnSimpanSupplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/save.png"))); // NOI18N
        btnSimpanSupplier.setText("SIMPAN");
        btnSimpanSupplier.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSimpanSupplier.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSimpanSupplier.setIconTextGap(10);
        btnSimpanSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanSupplierActionPerformed(evt);
            }
        });
        crudSupplierPanel.add(btnSimpanSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 30, 120, -1));

        btnEditSupplier.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnEditSupplier.setForeground(new java.awt.Color(64, 64, 122));
        btnEditSupplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/editing.png"))); // NOI18N
        btnEditSupplier.setText("UBAH");
        btnEditSupplier.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnEditSupplier.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnEditSupplier.setIconTextGap(10);
        btnEditSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditSupplierActionPerformed(evt);
            }
        });
        crudSupplierPanel.add(btnEditSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 80, 120, -1));

        btnHapusSupplier.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnHapusSupplier.setForeground(new java.awt.Color(64, 64, 122));
        btnHapusSupplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/trash.png"))); // NOI18N
        btnHapusSupplier.setText("HAPUS");
        btnHapusSupplier.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnHapusSupplier.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnHapusSupplier.setIconTextGap(8);
        btnHapusSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusSupplierActionPerformed(evt);
            }
        });
        crudSupplierPanel.add(btnHapusSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 30, 110, -1));

        btnResetSupplier.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnResetSupplier.setForeground(new java.awt.Color(64, 64, 122));
        btnResetSupplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/undo-circular-arrow.png"))); // NOI18N
        btnResetSupplier.setText("RESET");
        btnResetSupplier.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnResetSupplier.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnResetSupplier.setIconTextGap(10);
        btnResetSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetSupplierActionPerformed(evt);
            }
        });
        crudSupplierPanel.add(btnResetSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 80, 110, -1));

        txtcariSupplier.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txtcariSupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtcariSupplierKeyReleased(evt);
            }
        });
        crudSupplierPanel.add(txtcariSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 210, 230, 30));

        txtKodeSupplier.setEditable(false);
        txtKodeSupplier.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        txtKodeSupplier.setDisabledTextColor(new java.awt.Color(64, 64, 122));
        txtKodeSupplier.setEnabled(false);
        txtKodeSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKodeSupplierActionPerformed(evt);
            }
        });
        crudSupplierPanel.add(txtKodeSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 150, 30));

        txtAlamatSupplier.setColumns(20);
        txtAlamatSupplier.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txtAlamatSupplier.setRows(5);
        jScrollPane1.setViewportView(txtAlamatSupplier);

        crudSupplierPanel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 40, 240, 60));

        txtDeskripsi.setColumns(20);
        txtDeskripsi.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txtDeskripsi.setRows(5);
        jScrollPane2.setViewportView(txtDeskripsi);

        crudSupplierPanel.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 130, 240, 60));

        labelDeskripsi.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelDeskripsi.setText("Deksripsi");
        crudSupplierPanel.add(labelDeskripsi, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 110, -1, -1));

        supplierPanel.add(crudSupplierPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 750, 250));

        tabelSupplier.setBackground(new java.awt.Color(244, 244, 250));
        tabelSupplier.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        tabelSupplier.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Kode Supplier", "Nama Supplier", "Alamat", "No Tlp", "Deskripsi"
            }
        ));
        tabelSupplier.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tabelSupplier.setGridColor(new java.awt.Color(222, 222, 229));
        tabelSupplier.setName("dashboard"); // NOI18N
        tabelSupplier.setOpaque(false);
        tabelSupplier.setRequestFocusEnabled(false);
        tabelSupplier.setRowHeight(25);
        tabelSupplier.setSelectionBackground(new java.awt.Color(64, 64, 122));
        tabelSupplier.setSelectionForeground(new java.awt.Color(245, 245, 250));
        tabelSupplier.setShowVerticalLines(false);
        tabelSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelSupplierMouseClicked(evt);
            }
        });
        tabelSupplierPanel.setViewportView(tabelSupplier);

        supplierPanel.add(tabelSupplierPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, 690, 180));

        mainPanel.add(supplierPanel, "card5");

        penjualanPanel.setBackground(new java.awt.Color(222, 222, 229));
        penjualanPanel.setMaximumSize(new java.awt.Dimension(974, 569));
        penjualanPanel.setMinimumSize(new java.awt.Dimension(974, 569));
        penjualanPanel.setPreferredSize(new java.awt.Dimension(974, 569));
        penjualanPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelTransaksi.setBackground(new java.awt.Color(222, 222, 229));

        trIdProduk.setEditable(false);
        trIdProduk.setBackground(new java.awt.Color(222, 222, 229));
        trIdProduk.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        trIdProduk.setForeground(new java.awt.Color(222, 222, 229));
        trIdProduk.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        trIdProduk.setDisabledTextColor(new java.awt.Color(64, 64, 122));
        trIdProduk.setEnabled(false);
        trIdProduk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trIdProdukActionPerformed(evt);
            }
        });

        trHarga.setEditable(false);
        trHarga.setBackground(new java.awt.Color(222, 222, 229));
        trHarga.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        trHarga.setForeground(new java.awt.Color(222, 222, 229));
        trHarga.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        trHarga.setDisabledTextColor(new java.awt.Color(64, 64, 122));
        trHarga.setEnabled(false);
        trHarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trHargaActionPerformed(evt);
            }
        });

        trStok.setEditable(false);
        trStok.setBackground(new java.awt.Color(222, 222, 229));
        trStok.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        trStok.setForeground(new java.awt.Color(222, 222, 229));
        trStok.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        trStok.setDisabledTextColor(new java.awt.Color(64, 64, 122));
        trStok.setEnabled(false);
        trStok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trStokActionPerformed(evt);
            }
        });

        trQty.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        trQty.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        trQty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trQtyActionPerformed(evt);
            }
        });
        trQty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                trQtyKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                trQtyKeyTyped(evt);
            }
        });

        labelJumlah.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelJumlah.setText("Jumlah");

        tambahKeranjang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tambahKeranjang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/add-to-cart.png"))); // NOI18N
        tambahKeranjang.setText("TAMBAH KERANJANG");
        tambahKeranjang.setIconTextGap(7);
        tambahKeranjang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahKeranjangActionPerformed(evt);
            }
        });

        trTotal.setBackground(new java.awt.Color(222, 222, 229));
        trTotal.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        trTotal.setForeground(new java.awt.Color(222, 222, 229));
        trTotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        trTotal.setDisabledTextColor(new java.awt.Color(64, 64, 122));
        trTotal.setEnabled(false);
        trTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trTotalActionPerformed(evt);
            }
        });

        labelIdProduk.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelIdProduk.setText("No Produk");

        labelStok.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelStok.setText("Stok");

        labelTotal.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelTotal.setText("Total");

        labelHarga.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelHarga.setText("Harga");

        labelTotalBayar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelTotalBayar.setText("TOTAL BAYAR   :");

        labellBayar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labellBayar.setText("BAYAR                :");

        labelKembalian.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelKembalian.setText("KEMBALIAN      :");

        tambahNota.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tambahNota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/credit-cards-payment.png"))); // NOI18N
        tambahNota.setText("BAYAR");
        tambahNota.setIconTextGap(8);
        tambahNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahNotaActionPerformed(evt);
            }
        });

        txtTotalKeseluruhan.setEditable(false);
        txtTotalKeseluruhan.setBackground(new java.awt.Color(64, 64, 122));
        txtTotalKeseluruhan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtTotalKeseluruhan.setForeground(new java.awt.Color(255, 255, 255));
        txtTotalKeseluruhan.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTotalKeseluruhan.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        txtTotalKeseluruhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalKeseluruhanActionPerformed(evt);
            }
        });

        txtKembalian.setEditable(false);
        txtKembalian.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtKembalian.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtKembalian.setDisabledTextColor(new java.awt.Color(64, 64, 122));
        txtKembalian.setEnabled(false);
        txtKembalian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKembalianActionPerformed(evt);
            }
        });

        txtBayar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtBayar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBayarActionPerformed(evt);
            }
        });
        txtBayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBayarKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBayarKeyTyped(evt);
            }
        });

        txtTotalBayar.setEditable(false);
        txtTotalBayar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtTotalBayar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTotalBayar.setDisabledTextColor(new java.awt.Color(64, 64, 122));
        txtTotalBayar.setEnabled(false);
        txtTotalBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalBayarActionPerformed(evt);
            }
        });

        trNamaBarang.setEditable(false);
        trNamaBarang.setBackground(new java.awt.Color(222, 222, 229));
        trNamaBarang.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        trNamaBarang.setForeground(new java.awt.Color(222, 222, 229));
        trNamaBarang.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        trNamaBarang.setDisabledTextColor(new java.awt.Color(64, 64, 122));
        trNamaBarang.setEnabled(false);
        trNamaBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trNamaBarangActionPerformed(evt);
            }
        });

        labelNmBarang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelNmBarang.setText("Nama Produk");

        resetTransaksi.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        resetTransaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/undo-circular-arrow.png"))); // NOI18N
        resetTransaksi.setText("RESET");
        resetTransaksi.setIconTextGap(7);
        resetTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetTransaksiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelTransaksiLayout = new javax.swing.GroupLayout(panelTransaksi);
        panelTransaksi.setLayout(panelTransaksiLayout);
        panelTransaksiLayout.setHorizontalGroup(
            panelTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTransaksiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelTransaksiLayout.createSequentialGroup()
                        .addComponent(labelIdProduk, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                        .addGap(181, 181, 181)
                        .addComponent(trHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelTransaksiLayout.createSequentialGroup()
                        .addGroup(panelTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelTransaksiLayout.createSequentialGroup()
                                .addComponent(labelStok, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(trStok, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(labelTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(11, 11, 11))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTransaksiLayout.createSequentialGroup()
                                .addComponent(labelNmBarang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(trNamaBarang)
                                    .addComponent(trIdProduk, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(9, 9, 9))
                            .addGroup(panelTransaksiLayout.createSequentialGroup()
                                .addComponent(tambahKeranjang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(panelTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(trTotal)
                            .addComponent(resetTransaksi, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                            .addComponent(trQty))))
                .addGap(48, 48, 48)
                .addGroup(panelTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelKembalian)
                    .addComponent(labellBayar)
                    .addComponent(labelTotalBayar)
                    .addComponent(tambahNota, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtBayar, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(txtTotalBayar, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTotalKeseluruhan, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtKembalian))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        panelTransaksiLayout.setVerticalGroup(
            panelTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTransaksiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTransaksiLayout.createSequentialGroup()
                        .addGroup(panelTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTotalBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelTotalBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labellBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addGroup(panelTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtKembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelKembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelTransaksiLayout.createSequentialGroup()
                        .addGroup(panelTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(trIdProduk, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(trHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelIdProduk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelHarga, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(trQty, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelJumlah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(trNamaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelNmBarang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(trTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(trStok, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelStok, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtTotalKeseluruhan, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tambahNota))
                            .addGroup(panelTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(tambahKeranjang)
                                .addComponent(resetTransaksi)))
                        .addGap(55, 55, 55))))
        );

        penjualanPanel.add(panelTransaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 750, 190));

        panelInputTransaksi.setBackground(new java.awt.Color(222, 222, 229));

        labelAutoKodeTransaksi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelAutoKodeTransaksi.setText("No Transaksi  :");

        transaksiKode.setEditable(false);
        transaksiKode.setBackground(new java.awt.Color(222, 222, 229));
        transaksiKode.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        transaksiKode.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        transaksiKode.setDisabledTextColor(new java.awt.Color(64, 64, 122));
        transaksiKode.setEnabled(false);
        transaksiKode.setSelectedTextColor(new java.awt.Color(222, 222, 229));
        transaksiKode.setSelectionColor(new java.awt.Color(0, 0, 0));
        transaksiKode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transaksiKodeActionPerformed(evt);
            }
        });

        labelDateTransaksi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelDateTransaksi.setText("Tanggal Transaksi : ");

        txtTgl.setEditable(false);
        txtTgl.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        txtTgl.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTgl.setDisabledTextColor(new java.awt.Color(64, 64, 122));
        txtTgl.setEnabled(false);
        txtTgl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTglActionPerformed(evt);
            }
        });

        labelCustomer.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelCustomer.setText("Customer       :");

        btnCariCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/search.png"))); // NOI18N
        btnCariCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariCustomerActionPerformed(evt);
            }
        });

        txNamaCustomer.setEditable(false);
        txNamaCustomer.setBackground(new java.awt.Color(222, 222, 229));
        txNamaCustomer.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        txNamaCustomer.setForeground(new java.awt.Color(222, 222, 229));
        txNamaCustomer.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txNamaCustomer.setText("Pembeli Umum");
        txNamaCustomer.setDisabledTextColor(new java.awt.Color(64, 64, 122));
        txNamaCustomer.setEnabled(false);
        txNamaCustomer.setSelectedTextColor(new java.awt.Color(222, 222, 229));
        txNamaCustomer.setSelectionColor(new java.awt.Color(0, 0, 0));
        txNamaCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txNamaCustomerActionPerformed(evt);
            }
        });

        txIdCustomer.setEditable(false);
        txIdCustomer.setBackground(new java.awt.Color(222, 222, 229));
        txIdCustomer.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        txIdCustomer.setForeground(new java.awt.Color(222, 222, 229));
        txIdCustomer.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txIdCustomer.setText("1");
        txIdCustomer.setDisabledTextColor(new java.awt.Color(64, 64, 122));
        txIdCustomer.setEnabled(false);
        txIdCustomer.setOpaque(false);
        txIdCustomer.setSelectedTextColor(new java.awt.Color(222, 222, 229));
        txIdCustomer.setSelectionColor(new java.awt.Color(0, 0, 0));
        txIdCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txIdCustomerActionPerformed(evt);
            }
        });

        btnCariProduk.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCariProduk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/search.png"))); // NOI18N
        btnCariProduk.setText("PILIH BARANG");
        btnCariProduk.setIconTextGap(10);
        btnCariProduk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariProdukActionPerformed(evt);
            }
        });

        tabelInputTransaksi.setBackground(new java.awt.Color(244, 244, 250));
        tabelInputTransaksi.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        tabelInputTransaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No Produk", "Kode Barang", "Nama Barang", "Harga", "Jumlah Beli", "Stok Awal", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelInputTransaksi.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tabelInputTransaksi.setGridColor(new java.awt.Color(222, 222, 229));
        tabelInputTransaksi.setName("dashboard"); // NOI18N
        tabelInputTransaksi.setOpaque(false);
        tabelInputTransaksi.setRequestFocusEnabled(false);
        tabelInputTransaksi.setRowHeight(25);
        tabelInputTransaksi.setSelectionBackground(new java.awt.Color(64, 64, 122));
        tabelInputTransaksi.setSelectionForeground(new java.awt.Color(245, 245, 250));
        tabelInputTransaksi.setShowVerticalLines(false);
        tabelInputTransaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelInputTransaksiMouseClicked(evt);
            }
        });
        JptabelInputTransaksi.setViewportView(tabelInputTransaksi);

        txtIdKasir.setEditable(false);
        txtIdKasir.setBackground(new java.awt.Color(222, 222, 229));
        txtIdKasir.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        txtIdKasir.setForeground(new java.awt.Color(222, 222, 229));
        txtIdKasir.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtIdKasir.setDisabledTextColor(new java.awt.Color(64, 64, 122));
        txtIdKasir.setEnabled(false);
        txtIdKasir.setMaximumSize(new java.awt.Dimension(0, 0));
        txtIdKasir.setMinimumSize(new java.awt.Dimension(0, 0));
        txtIdKasir.setPreferredSize(new java.awt.Dimension(0, 0));
        txtIdKasir.setSelectedTextColor(new java.awt.Color(222, 222, 229));
        txtIdKasir.setSelectionColor(new java.awt.Color(0, 0, 0));
        txtIdKasir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdKasirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelInputTransaksiLayout = new javax.swing.GroupLayout(panelInputTransaksi);
        panelInputTransaksi.setLayout(panelInputTransaksiLayout);
        panelInputTransaksiLayout.setHorizontalGroup(
            panelInputTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelInputTransaksiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelInputTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(JptabelInputTransaksi)
                    .addGroup(panelInputTransaksiLayout.createSequentialGroup()
                        .addGroup(panelInputTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(panelInputTransaksiLayout.createSequentialGroup()
                                .addComponent(labelAutoKodeTransaksi)
                                .addGap(0, 27, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelInputTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelInputTransaksiLayout.createSequentialGroup()
                                .addComponent(txNamaCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCariCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(transaksiKode))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelInputTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelDateTransaksi)
                            .addGroup(panelInputTransaksiLayout.createSequentialGroup()
                                .addComponent(txIdCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(130, 130, 130)
                                .addComponent(txtIdKasir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(53, 53, 53)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelInputTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnCariProduk, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                            .addComponent(txtTgl))))
                .addGap(19, 19, 19))
        );
        panelInputTransaksiLayout.setVerticalGroup(
            panelInputTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInputTransaksiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelInputTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelInputTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(labelAutoKodeTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(transaksiKode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelInputTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTgl, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(labelDateTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelInputTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelInputTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(panelInputTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelCustomer)
                            .addComponent(txNamaCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(btnCariCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelInputTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCariProduk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txIdCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(txtIdKasir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(JptabelInputTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        penjualanPanel.add(panelInputTransaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 750, 250));

        mainPanel.add(penjualanPanel, "card7");

        pengeluaranPanel.setBackground(new java.awt.Color(222, 222, 229));
        pengeluaranPanel.setMaximumSize(new java.awt.Dimension(974, 569));
        pengeluaranPanel.setMinimumSize(new java.awt.Dimension(974, 569));
        pengeluaranPanel.setPreferredSize(new java.awt.Dimension(974, 569));
        pengeluaranPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        crudPengeluaranPanel.setBackground(new java.awt.Color(222, 222, 229));
        crudPengeluaranPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelIdPengeluaran.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelIdPengeluaran.setText("No Pengeluaran");
        crudPengeluaranPanel.add(labelIdPengeluaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        labelJenisPengeluaran.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelJenisPengeluaran.setText("Jenis Pengeluaran");
        crudPengeluaranPanel.add(labelJenisPengeluaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, -1));

        labelTotalPengeluaran.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelTotalPengeluaran.setText("Total Pengeluaran");
        crudPengeluaranPanel.add(labelTotalPengeluaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, -1, -1));

        labelCariPengeluaran.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelCariPengeluaran.setText("Cari :");
        crudPengeluaranPanel.add(labelCariPengeluaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 170, -1, 30));

        txtTotalPengeluaran.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txtTotalPengeluaran.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTotalPengeluaranKeyTyped(evt);
            }
        });
        crudPengeluaranPanel.add(txtTotalPengeluaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 40, 150, 30));

        btnSimpanPengeluaran.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnSimpanPengeluaran.setForeground(new java.awt.Color(64, 64, 122));
        btnSimpanPengeluaran.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/save.png"))); // NOI18N
        btnSimpanPengeluaran.setText("SIMPAN");
        btnSimpanPengeluaran.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSimpanPengeluaran.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSimpanPengeluaran.setIconTextGap(10);
        btnSimpanPengeluaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanPengeluaranActionPerformed(evt);
            }
        });
        crudPengeluaranPanel.add(btnSimpanPengeluaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 30, 120, -1));

        btnEditPengeluaran.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnEditPengeluaran.setForeground(new java.awt.Color(64, 64, 122));
        btnEditPengeluaran.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/editing.png"))); // NOI18N
        btnEditPengeluaran.setText("UBAH");
        btnEditPengeluaran.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnEditPengeluaran.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnEditPengeluaran.setIconTextGap(10);
        btnEditPengeluaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditPengeluaranActionPerformed(evt);
            }
        });
        crudPengeluaranPanel.add(btnEditPengeluaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 80, 120, -1));

        btnHapusPengeluaran.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnHapusPengeluaran.setForeground(new java.awt.Color(64, 64, 122));
        btnHapusPengeluaran.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/trash.png"))); // NOI18N
        btnHapusPengeluaran.setText("HAPUS");
        btnHapusPengeluaran.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnHapusPengeluaran.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnHapusPengeluaran.setIconTextGap(8);
        btnHapusPengeluaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusPengeluaranActionPerformed(evt);
            }
        });
        crudPengeluaranPanel.add(btnHapusPengeluaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 30, 110, -1));

        btnResetPengeluaran.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnResetPengeluaran.setForeground(new java.awt.Color(64, 64, 122));
        btnResetPengeluaran.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/undo-circular-arrow.png"))); // NOI18N
        btnResetPengeluaran.setText("RESET");
        btnResetPengeluaran.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnResetPengeluaran.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnResetPengeluaran.setIconTextGap(10);
        btnResetPengeluaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetPengeluaranActionPerformed(evt);
            }
        });
        crudPengeluaranPanel.add(btnResetPengeluaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 80, 110, -1));

        txtcariPengeluaran.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txtcariPengeluaran.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtcariPengeluaranKeyReleased(evt);
            }
        });
        crudPengeluaranPanel.add(txtcariPengeluaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 170, 240, 30));

        txtIdPengeluaran.setEditable(false);
        txtIdPengeluaran.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        txtIdPengeluaran.setDisabledTextColor(new java.awt.Color(64, 64, 122));
        txtIdPengeluaran.setEnabled(false);
        txtIdPengeluaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdPengeluaranActionPerformed(evt);
            }
        });
        crudPengeluaranPanel.add(txtIdPengeluaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 110, 30));

        labelTglPengeluaran.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelTglPengeluaran.setText("Tanggal Pengeluaran");
        crudPengeluaranPanel.add(labelTglPengeluaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 20, -1, -1));

        txtKeteranganPengeluaran.setColumns(20);
        txtKeteranganPengeluaran.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txtKeteranganPengeluaran.setRows(5);
        jScrollPane4.setViewportView(txtKeteranganPengeluaran);

        crudPengeluaranPanel.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 280, 90));

        KeteranganLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        KeteranganLabel.setText("Keterangan");
        crudPengeluaranPanel.add(KeteranganLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 80, 120, -1));

        cbPengeluaran.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        cbPengeluaran.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Operasional", "Non-Operasional" }));
        cbPengeluaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbPengeluaranActionPerformed(evt);
            }
        });
        crudPengeluaranPanel.add(cbPengeluaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 110, 30));

        txtJumlahPengeluaran.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txtJumlahPengeluaran.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtJumlahPengeluaranKeyTyped(evt);
            }
        });
        crudPengeluaranPanel.add(txtJumlahPengeluaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 110, 30));

        labelJumlahPengeluaran.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelJumlahPengeluaran.setText("Jumlah ");
        crudPengeluaranPanel.add(labelJumlahPengeluaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 70, -1));
        crudPengeluaranPanel.add(txtTglPengeluaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 40, 140, 30));

        pengeluaranPanel.add(crudPengeluaranPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 750, 200));

        tabelPengeluaran.setBackground(new java.awt.Color(244, 244, 250));
        tabelPengeluaran.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        tabelPengeluaran.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "No Pengeluaran", "Jenis Pengeluaran", "Keterangan", "Jumlah", "Total Pengeluaran", "Tgl-Pengeluaran"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelPengeluaran.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tabelPengeluaran.setGridColor(new java.awt.Color(222, 222, 229));
        tabelPengeluaran.setName("dashboard"); // NOI18N
        tabelPengeluaran.setOpaque(false);
        tabelPengeluaran.setRequestFocusEnabled(false);
        tabelPengeluaran.setRowHeight(25);
        tabelPengeluaran.setSelectionBackground(new java.awt.Color(64, 64, 122));
        tabelPengeluaran.setSelectionForeground(new java.awt.Color(245, 245, 250));
        tabelPengeluaran.setShowVerticalLines(false);
        tabelPengeluaran.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelPengeluaranMouseClicked(evt);
            }
        });
        tabelPengeluaranPanel.setViewportView(tabelPengeluaran);

        pengeluaranPanel.add(tabelPengeluaranPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 700, 230));

        mainPanel.add(pengeluaranPanel, "card10");

        customerPanel.setBackground(new java.awt.Color(222, 222, 229));
        customerPanel.setMaximumSize(new java.awt.Dimension(974, 569));
        customerPanel.setMinimumSize(new java.awt.Dimension(974, 569));
        customerPanel.setPreferredSize(new java.awt.Dimension(974, 569));
        customerPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        crudCustomerPanel.setBackground(new java.awt.Color(222, 222, 229));
        crudCustomerPanel.setMaximumSize(new java.awt.Dimension(730, 200));
        crudCustomerPanel.setMinimumSize(new java.awt.Dimension(730, 200));
        crudCustomerPanel.setPreferredSize(new java.awt.Dimension(730, 200));
        crudCustomerPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelKodeCustomer.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelKodeCustomer.setText("Kode Customer");
        crudCustomerPanel.add(labelKodeCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        labelNamaCustomer.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelNamaCustomer.setText("Nama Customer");
        crudCustomerPanel.add(labelNamaCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, -1));

        labelAlamatCustomer.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelAlamatCustomer.setText("Alamat Customer");
        crudCustomerPanel.add(labelAlamatCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, -1, -1));

        labelTlpCustomer.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelTlpCustomer.setText("No Tlp");
        crudCustomerPanel.add(labelTlpCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, -1));

        labelCariCustomer.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelCariCustomer.setText("Cari :");
        crudCustomerPanel.add(labelCariCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 160, -1, 30));

        txtNamaCustomer.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txtNamaCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaCustomerActionPerformed(evt);
            }
        });
        crudCustomerPanel.add(txtNamaCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 150, 30));

        txtTlpCustomer.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txtTlpCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTlpCustomerActionPerformed(evt);
            }
        });
        txtTlpCustomer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTlpCustomerKeyTyped(evt);
            }
        });
        crudCustomerPanel.add(txtTlpCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 150, 30));

        btnSimpanCustomer.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnSimpanCustomer.setForeground(new java.awt.Color(64, 64, 122));
        btnSimpanCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/save.png"))); // NOI18N
        btnSimpanCustomer.setText("SIMPAN");
        btnSimpanCustomer.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSimpanCustomer.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSimpanCustomer.setIconTextGap(10);
        btnSimpanCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanCustomerActionPerformed(evt);
            }
        });
        crudCustomerPanel.add(btnSimpanCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 30, 120, -1));

        btnEditCustomer.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnEditCustomer.setForeground(new java.awt.Color(64, 64, 122));
        btnEditCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/editing.png"))); // NOI18N
        btnEditCustomer.setText("UBAH");
        btnEditCustomer.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnEditCustomer.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnEditCustomer.setIconTextGap(10);
        btnEditCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditCustomerActionPerformed(evt);
            }
        });
        crudCustomerPanel.add(btnEditCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 80, 120, -1));

        btnHapusCustomer.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnHapusCustomer.setForeground(new java.awt.Color(64, 64, 122));
        btnHapusCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/trash.png"))); // NOI18N
        btnHapusCustomer.setText("HAPUS");
        btnHapusCustomer.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnHapusCustomer.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnHapusCustomer.setIconTextGap(8);
        btnHapusCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusCustomerActionPerformed(evt);
            }
        });
        crudCustomerPanel.add(btnHapusCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 30, 110, -1));

        btnResetCustomer.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnResetCustomer.setForeground(new java.awt.Color(64, 64, 122));
        btnResetCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/undo-circular-arrow.png"))); // NOI18N
        btnResetCustomer.setText("RESET");
        btnResetCustomer.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnResetCustomer.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnResetCustomer.setIconTextGap(10);
        btnResetCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetCustomerActionPerformed(evt);
            }
        });
        crudCustomerPanel.add(btnResetCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 80, 110, -1));

        txtcariCustomer.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txtcariCustomer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtcariCustomerKeyReleased(evt);
            }
        });
        crudCustomerPanel.add(txtcariCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 160, 240, 30));

        txtKodeCustomer.setEditable(false);
        txtKodeCustomer.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        txtKodeCustomer.setDisabledTextColor(new java.awt.Color(64, 64, 122));
        txtKodeCustomer.setEnabled(false);
        txtKodeCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKodeCustomerActionPerformed(evt);
            }
        });
        crudCustomerPanel.add(txtKodeCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 150, 30));

        txtAlamatCustomer.setColumns(20);
        txtAlamatCustomer.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txtAlamatCustomer.setRows(5);
        jScrollPane3.setViewportView(txtAlamatCustomer);

        crudCustomerPanel.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 40, 240, 60));

        customerPanel.add(crudCustomerPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 750, 200));

        tabelCustomer.setBackground(new java.awt.Color(244, 244, 250));
        tabelCustomer.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        tabelCustomer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Kode Customer", "Nama Customer", "Alamat", "No Tlp"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelCustomer.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tabelCustomer.setGridColor(new java.awt.Color(222, 222, 229));
        tabelCustomer.setName("dashboard"); // NOI18N
        tabelCustomer.setOpaque(false);
        tabelCustomer.setRequestFocusEnabled(false);
        tabelCustomer.setRowHeight(25);
        tabelCustomer.setSelectionBackground(new java.awt.Color(64, 64, 122));
        tabelCustomer.setSelectionForeground(new java.awt.Color(245, 245, 250));
        tabelCustomer.setShowVerticalLines(false);
        tabelCustomer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelCustomerMouseClicked(evt);
            }
        });
        tabelProdukPanel1.setViewportView(tabelCustomer);

        customerPanel.add(tabelProdukPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 700, 230));

        mainPanel.add(customerPanel, "card6");

        lapInventoryPanel.setBackground(new java.awt.Color(222, 222, 229));
        lapInventoryPanel.setMaximumSize(new java.awt.Dimension(974, 569));
        lapInventoryPanel.setMinimumSize(new java.awt.Dimension(974, 569));
        lapInventoryPanel.setPreferredSize(new java.awt.Dimension(974, 569));
        lapInventoryPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabelInventory.setBackground(new java.awt.Color(244, 244, 250));
        tabelInventory.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        tabelInventory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Kode Barang", "Nama Barang", "Kategori", "Harga Beli", "Harga Jual", "Stok", "Supplier"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelInventory.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tabelInventory.setGridColor(new java.awt.Color(222, 222, 229));
        tabelInventory.setName("dashboard"); // NOI18N
        tabelInventory.setOpaque(false);
        tabelInventory.setRequestFocusEnabled(false);
        tabelInventory.setRowHeight(25);
        tabelInventory.setSelectionBackground(new java.awt.Color(64, 64, 122));
        tabelInventory.setSelectionForeground(new java.awt.Color(245, 245, 250));
        tabelInventory.setShowVerticalLines(false);
        tabelInventory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelInventoryMouseClicked(evt);
            }
        });
        JPtabelInventory.setViewportView(tabelInventory);
        if (tabelInventory.getColumnModel().getColumnCount() > 0) {
            tabelInventory.getColumnModel().getColumn(2).setResizable(false);
        }

        lapInventoryPanel.add(JPtabelInventory, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 710, 330));

        cetakDataInventory.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cetakDataInventory.setForeground(new java.awt.Color(64, 64, 122));
        cetakDataInventory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/printer.png"))); // NOI18N
        cetakDataInventory.setText("CETAK DATA PRODUK");
        cetakDataInventory.setIconTextGap(8);
        cetakDataInventory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cetakDataInventoryActionPerformed(evt);
            }
        });
        lapInventoryPanel.add(cetakDataInventory, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 240, -1));

        panelTotalInventory.setBackground(new java.awt.Color(222, 222, 229));
        panelTotalInventory.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        sumHargaBeli.setBackground(new java.awt.Color(64, 64, 122));
        sumHargaBeli.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        sumHargaBeli.setForeground(new java.awt.Color(64, 64, 122));
        sumHargaBeli.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        sumHargaBeli.setText("sumHargaBeli");
        sumHargaBeli.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        sumHargaBeli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sumHargaBeliMouseClicked(evt);
            }
        });
        panelTotalInventory.add(sumHargaBeli, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 6, -1, -1));

        sumStok.setBackground(new java.awt.Color(64, 64, 122));
        sumStok.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        sumStok.setForeground(new java.awt.Color(64, 64, 122));
        sumStok.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        sumStok.setText("sumStok");
        sumStok.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        sumStok.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sumStokMouseClicked(evt);
            }
        });
        panelTotalInventory.add(sumStok, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 6, -1, -1));

        sumHargaJual.setBackground(new java.awt.Color(64, 64, 122));
        sumHargaJual.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        sumHargaJual.setForeground(new java.awt.Color(64, 64, 122));
        sumHargaJual.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        sumHargaJual.setText("sumHargaJual");
        sumHargaJual.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        sumHargaJual.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sumHargaJualMouseClicked(evt);
            }
        });
        panelTotalInventory.add(sumHargaJual, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 6, -1, -1));

        TotalLabelInven.setBackground(new java.awt.Color(64, 64, 122));
        TotalLabelInven.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        TotalLabelInven.setForeground(new java.awt.Color(64, 64, 122));
        TotalLabelInven.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        TotalLabelInven.setText("Total :");
        TotalLabelInven.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        TotalLabelInven.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TotalLabelInvenMouseClicked(evt);
            }
        });
        panelTotalInventory.add(TotalLabelInven, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 6, -1, 20));

        lapInventoryPanel.add(panelTotalInventory, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 405, 710, 30));

        txtCariInventory.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txtCariInventory.setMaximumSize(new java.awt.Dimension(189, 33));
        txtCariInventory.setMinimumSize(new java.awt.Dimension(189, 33));
        txtCariInventory.setPreferredSize(new java.awt.Dimension(189, 33));
        txtCariInventory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariInventoryKeyReleased(evt);
            }
        });
        lapInventoryPanel.add(txtCariInventory, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 30, 240, -1));

        labelCariInventory.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelCariInventory.setText("Cari :");
        lapInventoryPanel.add(labelCariInventory, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 30, -1, 30));

        mainPanel.add(lapInventoryPanel, "card12");

        lapPengeluaranPanel.setBackground(new java.awt.Color(222, 222, 229));
        lapPengeluaranPanel.setMaximumSize(new java.awt.Dimension(974, 569));
        lapPengeluaranPanel.setMinimumSize(new java.awt.Dimension(974, 569));
        lapPengeluaranPanel.setPreferredSize(new java.awt.Dimension(974, 569));
        lapPengeluaranPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cetakDataPengeluaran.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cetakDataPengeluaran.setForeground(new java.awt.Color(64, 64, 122));
        cetakDataPengeluaran.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/printer.png"))); // NOI18N
        cetakDataPengeluaran.setText("CETAK SELURUH DATA PENGELUARAN");
        cetakDataPengeluaran.setIconTextGap(8);
        cetakDataPengeluaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cetakDataPengeluaranActionPerformed(evt);
            }
        });
        lapPengeluaranPanel.add(cetakDataPengeluaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 397, -1, -1));

        labelCariLapPengeluaran.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelCariLapPengeluaran.setText("Cari :");
        lapPengeluaranPanel.add(labelCariLapPengeluaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 70, -1, 30));

        txtCariPengeluaran.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txtCariPengeluaran.setMaximumSize(new java.awt.Dimension(189, 33));
        txtCariPengeluaran.setMinimumSize(new java.awt.Dimension(189, 33));
        txtCariPengeluaran.setPreferredSize(new java.awt.Dimension(189, 33));
        txtCariPengeluaran.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariPengeluaranKeyReleased(evt);
            }
        });
        lapPengeluaranPanel.add(txtCariPengeluaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 70, 220, -1));

        tabelLaporanPengeluaran.setBackground(new java.awt.Color(244, 244, 250));
        tabelLaporanPengeluaran.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        tabelLaporanPengeluaran.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "No Pengeluaran", "Jenis Pengeluaran", "Keterangan", "Jumlah", "Total Pengeluaran", "Tgl-Pengeluaran"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelLaporanPengeluaran.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tabelLaporanPengeluaran.setGridColor(new java.awt.Color(222, 222, 229));
        tabelLaporanPengeluaran.setName("dashboard"); // NOI18N
        tabelLaporanPengeluaran.setOpaque(false);
        tabelLaporanPengeluaran.setRequestFocusEnabled(false);
        tabelLaporanPengeluaran.setRowHeight(25);
        tabelLaporanPengeluaran.setSelectionBackground(new java.awt.Color(64, 64, 122));
        tabelLaporanPengeluaran.setSelectionForeground(new java.awt.Color(245, 245, 250));
        tabelLaporanPengeluaran.setShowVerticalLines(false);
        tabelLaporanPengeluaran.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelLaporanPengeluaranMouseClicked(evt);
            }
        });
        tabelPengeluaranPanel1.setViewportView(tabelLaporanPengeluaran);

        lapPengeluaranPanel.add(tabelPengeluaranPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 730, 280));

        labelCetakKeuanganByMonth1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelCetakKeuanganByMonth1.setForeground(new java.awt.Color(64, 64, 122));
        labelCetakKeuanganByMonth1.setText("Cetak Berdasarkan Bulan & Tahun :");
        lapPengeluaranPanel.add(labelCetakKeuanganByMonth1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 250, 20));

        CetakPengeluaranByMonth.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        CetakPengeluaranByMonth.setForeground(new java.awt.Color(64, 64, 122));
        CetakPengeluaranByMonth.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/printer.png"))); // NOI18N
        CetakPengeluaranByMonth.setIconTextGap(8);
        CetakPengeluaranByMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CetakPengeluaranByMonthActionPerformed(evt);
            }
        });
        lapPengeluaranPanel.add(CetakPengeluaranByMonth, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 15, 40, -1));

        JcBulanPengeluaran.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        JcBulanPengeluaran.setForeground(new java.awt.Color(64, 64, 122));
        JcBulanPengeluaran.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-PILIH BULAN-", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December", " " }));
        JcBulanPengeluaran.setToolTipText("");
        lapPengeluaranPanel.add(JcBulanPengeluaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 15, -1, 33));
        lapPengeluaranPanel.add(JcTahunPengeluaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 15, 100, 33));

        mainPanel.add(lapPengeluaranPanel, "card11");

        lapPenjualanPanel.setBackground(new java.awt.Color(222, 222, 229));
        lapPenjualanPanel.setMaximumSize(new java.awt.Dimension(974, 569));
        lapPenjualanPanel.setMinimumSize(new java.awt.Dimension(974, 569));
        lapPenjualanPanel.setPreferredSize(new java.awt.Dimension(974, 569));
        lapPenjualanPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabelLapPenjualan.setBackground(new java.awt.Color(244, 244, 250));
        tabelLapPenjualan.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        tabelLapPenjualan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "No Transaksi", "Customer", "Kode Barang", "Nama Barang", "Jumlah", "Total", "Dibayar", "Kembali", "Tgl-Transaksi"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false, false, false, true, false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelLapPenjualan.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tabelLapPenjualan.setGridColor(new java.awt.Color(222, 222, 229));
        tabelLapPenjualan.setName("dashboard"); // NOI18N
        tabelLapPenjualan.setOpaque(false);
        tabelLapPenjualan.setRequestFocusEnabled(false);
        tabelLapPenjualan.setRowHeight(25);
        tabelLapPenjualan.setSelectionBackground(new java.awt.Color(64, 64, 122));
        tabelLapPenjualan.setSelectionForeground(new java.awt.Color(245, 245, 250));
        tabelLapPenjualan.setShowVerticalLines(false);
        tabelLapPenjualan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelLapPenjualanMouseClicked(evt);
            }
        });
        JpTbPenjualan.setViewportView(tabelLapPenjualan);

        lapPenjualanPanel.add(JpTbPenjualan, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 730, 270));

        txtCariPenjualan.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txtCariPenjualan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariPenjualanKeyReleased(evt);
            }
        });
        lapPenjualanPanel.add(txtCariPenjualan, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 60, 220, 30));

        labelCetakByInvoice.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelCetakByInvoice.setForeground(new java.awt.Color(64, 64, 122));
        labelCetakByInvoice.setText("Cetak Berdasarkan No Transaksi    :");
        lapPenjualanPanel.add(labelCetakByInvoice, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 63, 240, 20));

        cetakDataByNoInvoice.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cetakDataByNoInvoice.setForeground(new java.awt.Color(64, 64, 122));
        cetakDataByNoInvoice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/printer.png"))); // NOI18N
        cetakDataByNoInvoice.setIconTextGap(8);
        cetakDataByNoInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cetakDataByNoInvoiceActionPerformed(evt);
            }
        });
        lapPenjualanPanel.add(cetakDataByNoInvoice, new org.netbeans.lib.awtextra.AbsoluteConstraints(265, 60, 40, -1));

        labelCariPenjualan1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelCariPenjualan1.setText("Cari :");
        lapPenjualanPanel.add(labelCariPenjualan1, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 60, 40, 30));

        cetakDataeByNoInvoice1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cetakDataeByNoInvoice1.setForeground(new java.awt.Color(64, 64, 122));
        cetakDataeByNoInvoice1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/printer.png"))); // NOI18N
        cetakDataeByNoInvoice1.setText("CETAK SELURUH DATA PENJUALAN");
        cetakDataeByNoInvoice1.setIconTextGap(8);
        cetakDataeByNoInvoice1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cetakDataeByNoInvoice1ActionPerformed(evt);
            }
        });
        lapPenjualanPanel.add(cetakDataeByNoInvoice1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, 270, -1));

        labelCetakPenjualanByMonthYear.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelCetakPenjualanByMonthYear.setForeground(new java.awt.Color(64, 64, 122));
        labelCetakPenjualanByMonthYear.setText("Cetak Berdasarkan Bulan & Tahun :");
        lapPenjualanPanel.add(labelCetakPenjualanByMonthYear, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 250, 20));

        CetakPenjualanByMonthYear.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        CetakPenjualanByMonthYear.setForeground(new java.awt.Color(64, 64, 122));
        CetakPenjualanByMonthYear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/printer.png"))); // NOI18N
        CetakPenjualanByMonthYear.setIconTextGap(8);
        CetakPenjualanByMonthYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CetakPenjualanByMonthYearActionPerformed(evt);
            }
        });
        lapPenjualanPanel.add(CetakPenjualanByMonthYear, new org.netbeans.lib.awtextra.AbsoluteConstraints(265, 15, 40, -1));

        JcBulanPenjualan.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        JcBulanPenjualan.setForeground(new java.awt.Color(64, 64, 122));
        JcBulanPenjualan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-PILIH BULAN-", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December", " " }));
        JcBulanPenjualan.setToolTipText("");
        lapPenjualanPanel.add(JcBulanPenjualan, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 15, -1, 33));
        lapPenjualanPanel.add(JcTahunPenjualan, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 15, 100, 33));

        mainPanel.add(lapPenjualanPanel, "card8");

        kelolaAkunPanel.setBackground(new java.awt.Color(222, 222, 229));
        kelolaAkunPanel.setMaximumSize(new java.awt.Dimension(974, 569));
        kelolaAkunPanel.setMinimumSize(new java.awt.Dimension(974, 569));
        kelolaAkunPanel.setPreferredSize(new java.awt.Dimension(974, 569));
        kelolaAkunPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabelKelolaAkun.setBackground(new java.awt.Color(244, 244, 250));
        tabelKelolaAkun.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        tabelKelolaAkun.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "ID User", "Username", "Password", "Nama", "Level Akses"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelKelolaAkun.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tabelKelolaAkun.setGridColor(new java.awt.Color(222, 222, 229));
        tabelKelolaAkun.setName("dashboard"); // NOI18N
        tabelKelolaAkun.setOpaque(false);
        tabelKelolaAkun.setRequestFocusEnabled(false);
        tabelKelolaAkun.setRowHeight(25);
        tabelKelolaAkun.setSelectionBackground(new java.awt.Color(64, 64, 122));
        tabelKelolaAkun.setSelectionForeground(new java.awt.Color(245, 245, 250));
        tabelKelolaAkun.setShowVerticalLines(false);
        tabelKelolaAkun.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelKelolaAkunMouseClicked(evt);
            }
        });
        tabelKelolaAkunPanel.setViewportView(tabelKelolaAkun);
        if (tabelKelolaAkun.getColumnModel().getColumnCount() > 0) {
            tabelKelolaAkun.getColumnModel().getColumn(0).setHeaderValue("No");
            tabelKelolaAkun.getColumnModel().getColumn(1).setHeaderValue("ID User");
            tabelKelolaAkun.getColumnModel().getColumn(2).setHeaderValue("Username");
            tabelKelolaAkun.getColumnModel().getColumn(3).setHeaderValue("Password");
            tabelKelolaAkun.getColumnModel().getColumn(4).setHeaderValue("Nama");
            tabelKelolaAkun.getColumnModel().getColumn(5).setHeaderValue("Level Akses");
        }

        kelolaAkunPanel.add(tabelKelolaAkunPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 690, 220));

        crudKelolaAkunPanel.setBackground(new java.awt.Color(222, 222, 229));
        crudKelolaAkunPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelId.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelId.setText("ID User");
        crudKelolaAkunPanel.add(labelId, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 90, -1));

        labelUsernameKelola.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelUsernameKelola.setText("Username");
        crudKelolaAkunPanel.add(labelUsernameKelola, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, -1));

        labelPassKelola.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelPassKelola.setText("Password");
        crudKelolaAkunPanel.add(labelPassKelola, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, -1));

        labelCariAkun.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelCariAkun.setText("Cari :");
        crudKelolaAkunPanel.add(labelCariAkun, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 176, -1, 20));

        txtusername.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        crudKelolaAkunPanel.add(txtusername, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 150, 30));

        cbUsersKelola.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        cbUsersKelola.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kasir", "Admin" }));
        cbUsersKelola.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbUsersKelolaActionPerformed(evt);
            }
        });
        crudKelolaAkunPanel.add(cbUsersKelola, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 40, 150, 30));

        txtpassword.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        crudKelolaAkunPanel.add(txtpassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 150, 30));

        btnSimpanUsers.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnSimpanUsers.setForeground(new java.awt.Color(64, 64, 122));
        btnSimpanUsers.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/save.png"))); // NOI18N
        btnSimpanUsers.setText("SIMPAN");
        btnSimpanUsers.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSimpanUsers.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSimpanUsers.setIconTextGap(10);
        btnSimpanUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanUsersActionPerformed(evt);
            }
        });
        crudKelolaAkunPanel.add(btnSimpanUsers, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 30, 120, -1));

        btnEditUsers.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnEditUsers.setForeground(new java.awt.Color(64, 64, 122));
        btnEditUsers.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/editing.png"))); // NOI18N
        btnEditUsers.setText("UBAH");
        btnEditUsers.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnEditUsers.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnEditUsers.setIconTextGap(10);
        btnEditUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditUsersActionPerformed(evt);
            }
        });
        crudKelolaAkunPanel.add(btnEditUsers, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 80, 120, -1));

        btnHapusUsers.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnHapusUsers.setForeground(new java.awt.Color(64, 64, 122));
        btnHapusUsers.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/trash.png"))); // NOI18N
        btnHapusUsers.setText("HAPUS");
        btnHapusUsers.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnHapusUsers.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnHapusUsers.setIconTextGap(8);
        btnHapusUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusUsersActionPerformed(evt);
            }
        });
        crudKelolaAkunPanel.add(btnHapusUsers, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 30, 110, -1));

        txtnama.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        crudKelolaAkunPanel.add(txtnama, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, 150, 30));

        btnResetUsers.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnResetUsers.setForeground(new java.awt.Color(64, 64, 122));
        btnResetUsers.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/undo-circular-arrow.png"))); // NOI18N
        btnResetUsers.setText("RESET");
        btnResetUsers.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnResetUsers.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnResetUsers.setIconTextGap(10);
        btnResetUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetUsersActionPerformed(evt);
            }
        });
        crudKelolaAkunPanel.add(btnResetUsers, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 80, 110, -1));

        txtcariUsers.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txtcariUsers.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtcariUsersKeyReleased(evt);
            }
        });
        crudKelolaAkunPanel.add(txtcariUsers, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 170, 240, 30));

        labelNamaKelola.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelNamaKelola.setText("Nama");
        crudKelolaAkunPanel.add(labelNamaKelola, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, -1, -1));

        txtidUser.setEditable(false);
        txtidUser.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        txtidUser.setDisabledTextColor(new java.awt.Color(64, 64, 122));
        txtidUser.setEnabled(false);
        crudKelolaAkunPanel.add(txtidUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 150, 30));

        labelCbUser.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelCbUser.setText("Akses Level");
        crudKelolaAkunPanel.add(labelCbUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, -1, -1));

        kelolaAkunPanel.add(crudKelolaAkunPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 750, 210));

        mainPanel.add(kelolaAkunPanel, "card4");

        lapKeuanganPanel.setBackground(new java.awt.Color(222, 222, 229));
        lapKeuanganPanel.setMaximumSize(new java.awt.Dimension(974, 569));
        lapKeuanganPanel.setMinimumSize(new java.awt.Dimension(974, 569));
        lapKeuanganPanel.setPreferredSize(new java.awt.Dimension(974, 569));
        lapKeuanganPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabelKeuangan.setBackground(new java.awt.Color(244, 244, 250));
        tabelKeuangan.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        tabelKeuangan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tgl Dibuat", "Total Penjualan", "Beban Pengeluaran", "Laba Bersih"
            }
        ));
        tabelKeuangan.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tabelKeuangan.setGridColor(new java.awt.Color(222, 222, 229));
        tabelKeuangan.setName("dashboard"); // NOI18N
        tabelKeuangan.setOpaque(false);
        tabelKeuangan.setRequestFocusEnabled(false);
        tabelKeuangan.setRowHeight(25);
        tabelKeuangan.setSelectionBackground(new java.awt.Color(64, 64, 122));
        tabelKeuangan.setSelectionForeground(new java.awt.Color(245, 245, 250));
        tabelKeuangan.setShowVerticalLines(false);
        tabelKeuangan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelKeuanganMouseClicked(evt);
            }
        });
        JPtabelInventory1.setViewportView(tabelKeuangan);
        if (tabelKeuangan.getColumnModel().getColumnCount() > 0) {
            tabelKeuangan.getColumnModel().getColumn(1).setResizable(false);
        }

        lapKeuanganPanel.add(JPtabelInventory1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 710, 320));

        cetakDataKeuangan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cetakDataKeuangan.setForeground(new java.awt.Color(64, 64, 122));
        cetakDataKeuangan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/printer.png"))); // NOI18N
        cetakDataKeuangan.setText("CETAK SELURUH LAPORAN KEUANGAN");
        cetakDataKeuangan.setIconTextGap(8);
        cetakDataKeuangan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cetakDataKeuanganActionPerformed(evt);
            }
        });
        lapKeuanganPanel.add(cetakDataKeuangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 397, 290, -1));

        txtCariKeuangan.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txtCariKeuangan.setMaximumSize(new java.awt.Dimension(189, 33));
        txtCariKeuangan.setMinimumSize(new java.awt.Dimension(189, 33));
        txtCariKeuangan.setPreferredSize(new java.awt.Dimension(189, 33));
        txtCariKeuangan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariKeuanganKeyReleased(evt);
            }
        });
        lapKeuanganPanel.add(txtCariKeuangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 30, 240, -1));

        labelCariKeuangan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelCariKeuangan.setText("Cari :");
        lapKeuanganPanel.add(labelCariKeuangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 30, -1, 30));

        labelCetakKeuanganByMonth.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelCetakKeuanganByMonth.setForeground(new java.awt.Color(64, 64, 122));
        labelCetakKeuanganByMonth.setText("Cetak Berdasarkan Data Yang Dipilih :");
        lapKeuanganPanel.add(labelCetakKeuanganByMonth, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 260, 30));

        CetakKeuanganByMonth.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        CetakKeuanganByMonth.setForeground(new java.awt.Color(64, 64, 122));
        CetakKeuanganByMonth.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/printer.png"))); // NOI18N
        CetakKeuanganByMonth.setIconTextGap(8);
        CetakKeuanganByMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CetakKeuanganByMonthActionPerformed(evt);
            }
        });
        lapKeuanganPanel.add(CetakKeuanganByMonth, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 30, 40, -1));

        mainPanel.add(lapKeuanganPanel, "card12");

        container.add(mainPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, -1, -1));

        body.add(container);

        getContentPane().add(body, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void dashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashboardActionPerformed
       
//        Remove Panel
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();
//        Add Panel
        mainPanel.add(dashboardPanel);
        menuTitle.setText("Overview | Dashboard");  
        mainPanel.repaint();
        mainPanel.revalidate();
    }//GEN-LAST:event_dashboardActionPerformed

    private void produkMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_produkMenuActionPerformed
        
//      Remove Panel
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();
//      Add Panel
        mainPanel.add(produkPanel);
        menuTitle.setText("Menu Utama | Data Produk");  
        mainPanel.repaint();
        mainPanel.revalidate();
    }//GEN-LAST:event_produkMenuActionPerformed

    private void btnEditProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditProdukActionPerformed
        
        try{
            String sql = "UPDATE produk SET kode_barang='"+txtkodebarang.getText()+"',"
                    + " nama_barang='"+txtnamabarang.getText()+"', kategori='"+cbkategori.getSelectedItem()+"', "
                    + "harga_beli='"+txthargabeli.getText()+"', harga_jual='"+txthargajual.getText()+"', "
                    + "stok='"+txtstok.getText()+"', id_supplier='"+txtIdSupplier.getText()+"' WHERE kode_barang='"+txtkodebarang.getText()+"'";
            Connection conn = (Connection)Koneksi.getKoneksi();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.execute();
            tampilDataProduk(); tampilDataPenjualan(); nullTableProduk(); countData(); autoKodeProduk();
            sumTotalInventory(); tampilDataInventory();
            JOptionPane.showMessageDialog(null, "Data Produk Berhasil Diubah !");
        }catch(HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_btnEditProdukActionPerformed

    private void btnHapusProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusProdukActionPerformed
        
        try {   
            int warningProduk;
                if((warningProduk = JOptionPane.showConfirmDialog(null, "Hapus barang yang dipilih ?", "Konfirmasi", 
                        JOptionPane.YES_NO_OPTION)) == 0){
                    String sql = "DELETE FROM produk WHERE kode_barang='"+txtkodebarang.getText()+"'";
                    Connection conn = (Connection)Koneksi.getKoneksi();
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.execute();
                    tampilDataProduk(); nullTableProduk(); countData(); autoKodeProduk();
                    sumTotalInventory(); tampilDataInventory();
                    JOptionPane.showMessageDialog(null, "Data Produk Berhasil Dihapus !");
                }
        }catch(SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_btnHapusProdukActionPerformed

    private void txtnamabarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnamabarangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnamabarangActionPerformed

    private void cbkategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbkategoriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbkategoriActionPerformed

    private void btnSimpanProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanProdukActionPerformed
        
        try {
            if (txtkodebarang.getText().equals("") ||txtnamabarang.getText().equals("") ||
                txthargabeli.getText().equals("") || txthargajual.getText().equals("") || 
                txtstok.getText().equals("")  ) {
                JOptionPane.showMessageDialog(null, "Isi field terlebih dahulu !");
            } else {
                String sql = "INSERT INTO produk VALUES (null,'"+txtkodebarang.getText()+"','"+txtnamabarang.getText()+"',"
                        + " '"+cbkategori.getSelectedItem()+"', '"+txthargabeli.getText()+"', '"+txthargajual.getText()+"',"
                        + " '"+txtstok.getText()+"', '"+txtIdSupplier.getText()+"')";
                Connection conn = (Connection)Koneksi.getKoneksi();
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.execute();
                tampilDataProduk(); tampilDataPenjualan(); autoKodeProduk(); nullTableProduk(); countData();
                sumTotalInventory(); tampilDataInventory();
                JOptionPane.showMessageDialog(null, "Data Produk Berhasil Ditambah !");
            }
        }catch(HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_btnSimpanProdukActionPerformed

    private void btnResetProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetProdukActionPerformed
        
        nullTableProduk(); autoKodeProduk();  
    }//GEN-LAST:event_btnResetProdukActionPerformed

    private void tabelProdukMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelProdukMouseClicked
        
        int baris = tabelProduk.rowAtPoint(evt.getPoint());
        
        String kode_barang = tabelProduk.getValueAt(baris, 1).toString();
        txtkodebarang.setText(kode_barang);

        String nama_barang = tabelProduk.getValueAt(baris, 2).toString();
        txtnamabarang.setText(nama_barang);

        String kategori = tabelProduk.getValueAt(baris, 3).toString();
        cbkategori.setSelectedItem(kategori);

        String harga_beli = tabelProduk.getValueAt(baris, 4).toString();
        txthargabeli.setText(harga_beli);

        String harga_jual = tabelProduk.getValueAt(baris, 5).toString();
        txthargajual.setText(harga_jual);

        String stok = tabelProduk.getValueAt(baris, 6).toString();
        txtstok.setText(stok);
        
        String nama_supplier = tabelProduk.getValueAt(baris, 7).toString();
        txNamaSupplier.setText(nama_supplier);
    }//GEN-LAST:event_tabelProdukMouseClicked

    private void txtcariProdukKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcariProdukKeyReleased
      
        tampilDataProduk();
    }//GEN-LAST:event_txtcariProdukKeyReleased

    private void txtkodebarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtkodebarangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtkodebarangActionPerformed

    private void lapKeuanganMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lapKeuanganMenuActionPerformed
      
//      Remove Panel
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();
//      Add Panel
        mainPanel.add(lapKeuanganPanel);
        menuTitle.setText("Laporan | Laporan Keuangan");  
        mainPanel.repaint();
        mainPanel.revalidate();
    }//GEN-LAST:event_lapKeuanganMenuActionPerformed

    private void lapPenjualanMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lapPenjualanMenuActionPerformed
     
//      Remove Panel
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();
//      Add Panel
        mainPanel.add(lapPenjualanPanel);
        menuTitle.setText("Laporan | Laporan Penjualan");  
        mainPanel.repaint();
        mainPanel.revalidate();
    }//GEN-LAST:event_lapPenjualanMenuActionPerformed

    private void tabelKelolaAkunMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelKelolaAkunMouseClicked
        
        int baris = tabelKelolaAkun.rowAtPoint(evt.getPoint());
        String id_user = tabelKelolaAkun.getValueAt(baris, 1).toString();
        txtidUser.setText(id_user);
        
        String username = tabelKelolaAkun.getValueAt(baris, 2).toString();
        txtusername.setText(username);
        
        String password = tabelKelolaAkun.getValueAt(baris, 3).toString();
        txtpassword.setText(password);
        
        String name = tabelKelolaAkun.getValueAt(baris, 4).toString();
        txtnama.setText(name);
        
        String role = tabelKelolaAkun.getValueAt(baris, 5).toString();
        cbUsersKelola.setSelectedItem(role);
    }//GEN-LAST:event_tabelKelolaAkunMouseClicked

    private void customerMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerMenuActionPerformed
       
//      Remove Panel
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();
//      Add Panel
        mainPanel.add(customerPanel);
        menuTitle.setText("Menu Utama | Data Customer");  
        mainPanel.repaint();
        mainPanel.revalidate();
    }//GEN-LAST:event_customerMenuActionPerformed

    private void supplierMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierMenuActionPerformed
       
//      Remove Panel
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();
//      Add Panel
        mainPanel.add(supplierPanel);
        menuTitle.setText("Menu Utama | Data Supplier");  
        mainPanel.repaint();
        mainPanel.revalidate();
    }//GEN-LAST:event_supplierMenuActionPerformed

    private void penjualanMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_penjualanMenuActionPerformed

//      Remove Panel
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();
//      Add Panel
        mainPanel.add(penjualanPanel);
        menuTitle.setText("Menu Utama | Transaksi Penjualan");  
        mainPanel.repaint();
        mainPanel.revalidate();
    }//GEN-LAST:event_penjualanMenuActionPerformed

    private void pengeluaranMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pengeluaranMenuActionPerformed

//      Remove Panel
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();
//      Add Panel
        mainPanel.add(pengeluaranPanel);
        menuTitle.setText("Menu Utama | Pengeluaran");  
        mainPanel.repaint();
        mainPanel.revalidate();
    }//GEN-LAST:event_pengeluaranMenuActionPerformed

    private void cbUsersKelolaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbUsersKelolaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbUsersKelolaActionPerformed

    private void btnSimpanUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanUsersActionPerformed

        try {
            if (txtnama.getText().equals("") ||txtpassword.getText().equals("") || txtnama.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Isi field terlebih dahulu !");
            } else {
                String sql = "INSERT INTO users VALUES (default ,'"+txtidUser.getText()+"', '"+txtusername.getText()+"',"
                    + " '"+txtpassword.getText()+"', '"+txtnama.getText()+"', '"+cbUsersKelola.getSelectedItem()+"')";
                Connection conn = (Connection)Koneksi.getKoneksi();
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.execute();
                tampilDataUsers(); nullTableUsers(); autoKodeUser();
                JOptionPane.showMessageDialog(null, "Data Users Berhasil Ditambah !");
            }
        }catch(HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_btnSimpanUsersActionPerformed

    private void btnEditUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditUsersActionPerformed

        try{
            String sql = "UPDATE users SET no_user"
                    + "='"+txtidUser.getText()+"', username='"+txtusername.getText()+"',"
                + " password='"+txtpassword.getText()+"', name='"+txtnama.getText()+"',"
                + " role='"+cbUsersKelola.getSelectedItem()+"' WHERE no_user='"+txtidUser.getText()+"'";
            Connection conn = (Connection)Koneksi.getKoneksi();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.execute();
            tampilDataUsers(); nullTableUsers(); autoKodeUser();
            JOptionPane.showMessageDialog(null, "Data Users Berhasil Diubah !");
        }catch(HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_btnEditUsersActionPerformed

    private void btnHapusUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusUsersActionPerformed
  
        try {   
            int warningUsers;
                if((warningUsers = JOptionPane.showConfirmDialog(null, "Hapus users yang dipilih ?", "Konfirmasi", 
                        JOptionPane.YES_NO_OPTION)) == 0){
                    String sql = "DELETE FROM users WHERE no_user='"+txtidUser.getText()+"'";
                    Connection conn = (Connection)Koneksi.getKoneksi();
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.execute();
                    tampilDataUsers(); nullTableUsers();
                    JOptionPane.showMessageDialog(null, "Data Users Berhasil Dihapus !");
                }
        }catch(SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_btnHapusUsersActionPerformed

    private void btnResetUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetUsersActionPerformed
   
        nullTableUsers(); autoKodeUser();
    }//GEN-LAST:event_btnResetUsersActionPerformed

    private void txtcariUsersKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcariUsersKeyReleased
     
        tampilDataUsers();
    }//GEN-LAST:event_txtcariUsersKeyReleased

    private void txtstokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtstokActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtstokActionPerformed

    private void iconLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconLogoutMouseClicked
        int confirmLogout = JOptionPane.showConfirmDialog(null, "Anda Yakin Ingin Logout ?", "Konfirmasi Keluar", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirmLogout == JOptionPane.YES_OPTION){
            this.dispose();
            new Login().setVisible(true);
            JOptionPane.showMessageDialog(null, "Berhasil Logout !");
        }
    }//GEN-LAST:event_iconLogoutMouseClicked

    private void txtNamaSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaSupplierActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaSupplierActionPerformed

    private void btnSimpanSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanSupplierActionPerformed

        try {
            if (txtKodeSupplier.getText().equals("") ||txtNamaSupplier.getText().equals("") ||
                txtTlpSupplier.getText().equals("") ||txtAlamatSupplier.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Isi field terlebih dahulu !");
            } else {
                String sql = "INSERT INTO supplier VALUES (null,'"+txtKodeSupplier.getText()+"',"
                + " '"+txtNamaSupplier.getText()+"', '"+txtAlamatSupplier.getText()+"', "
                + "'"+txtTlpSupplier.getText()+"','"+txtDeskripsi.getText()+"')";
                Connection conn = (Connection)Koneksi.getKoneksi();
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.execute();
                tampilDataSupplier(); autoKodeSupplier(); nullTableSupplier(); countData();
                 
                JOptionPane.showMessageDialog(null, "Data Supplier Berhasil Ditambah !");
            }
        }catch(HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_btnSimpanSupplierActionPerformed

    private void btnEditSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditSupplierActionPerformed
       
        try{
            String sql = "UPDATE supplier SET kode_supplier='"+txtKodeSupplier.getText()+"',"
            + " nama_supplier='"+txtNamaSupplier.getText()+"', alamat_supplier='"+txtAlamatSupplier.getText()+"',"
            + " tlp_supplier='"+txtTlpSupplier.getText()+"', deskripsi='"+txtDeskripsi.getText()+"'"
            + " WHERE kode_supplier='"+txtKodeSupplier.getText()+"'";
            Connection conn = (Connection)Koneksi.getKoneksi();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.execute();
            tampilDataSupplier(); nullTableSupplier(); countData();
            JOptionPane.showMessageDialog(null, "Data Supplier Berhasil Diubah !");
        }catch(HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_btnEditSupplierActionPerformed

    private void btnHapusSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusSupplierActionPerformed
   
         try {   
            int warningSupplier;
                if((warningSupplier = JOptionPane.showConfirmDialog(null, "Hapus data yang dipilih ?", "Konfirmasi", 
                        JOptionPane.YES_NO_OPTION)) == 0){
                    String sql = "DELETE FROM supplier WHERE kode_supplier='"+txtKodeSupplier.getText()+"'";
                    Connection conn = (Connection)Koneksi.getKoneksi();
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.execute();
                    tampilDataSupplier(); nullTableSupplier(); countData(); autoKodeSupplier();
                    JOptionPane.showMessageDialog(null, "Data Supplier Berhasil Dihapus !");
                }
        }catch(SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_btnHapusSupplierActionPerformed

    private void btnResetSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetSupplierActionPerformed
     
        nullTableSupplier(); autoKodeSupplier();
    }//GEN-LAST:event_btnResetSupplierActionPerformed

    private void txtcariSupplierKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcariSupplierKeyReleased
      
        tampilDataSupplier();
    }//GEN-LAST:event_txtcariSupplierKeyReleased

    private void txtKodeSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKodeSupplierActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKodeSupplierActionPerformed

    private void tabelSupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelSupplierMouseClicked
       
        int baris = tabelSupplier.rowAtPoint(evt.getPoint());
        
        String kode_supplier = tabelSupplier.getValueAt(baris, 1).toString();
        txtKodeSupplier.setText(kode_supplier);
        
        String nama_supplier = tabelSupplier.getValueAt(baris, 2).toString();
        txtNamaSupplier.setText(nama_supplier);
        
        String alamat_supplier = tabelSupplier.getValueAt(baris, 3).toString();
        txtAlamatSupplier.setText(alamat_supplier);
        
        String tlp_supplier = tabelSupplier.getValueAt(baris, 4).toString();
        txtTlpSupplier.setText(tlp_supplier);
        
        String deskripsi = tabelSupplier.getValueAt(baris, 5).toString();
        txtDeskripsi.setText(deskripsi);
        
      
    }//GEN-LAST:event_tabelSupplierMouseClicked

    private void totalStokProdukKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_totalStokProdukKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_totalStokProdukKeyReleased

    private void txtNamaCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaCustomerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaCustomerActionPerformed

    private void btnSimpanCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanCustomerActionPerformed
        
        try {
            if (txtKodeCustomer.getText().equals("") ||txtNamaCustomer.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Isi field terlebih dahulu !");
            } else {
                String sql = "INSERT INTO customer VALUES (default, '"+txtKodeCustomer.getText()+"', "
                + "'"+txtNamaCustomer.getText()+"', '"+txtAlamatCustomer.getText()+"', '"+txtTlpCustomer.getText()+"')";
                Connection conn = (Connection)Koneksi.getKoneksi();
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.execute();
                tampilDataCustomer(); tampilDataPenjualan(); transaksiTerbaru();
                autoKodeCustomer(); nullTableCustomer();
                 
                JOptionPane.showMessageDialog(null, "Data Customer Berhasil Ditambah !");
            }
        }catch(HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_btnSimpanCustomerActionPerformed

    private void btnEditCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditCustomerActionPerformed
     
        try{
            String sql = "UPDATE customer SET kode_customer='"+txtKodeCustomer.getText()+"',"
            + " nama_customer='"+txtNamaCustomer.getText()+"', alamat_customer='"+txtAlamatCustomer.getText()+"',"
            + " tlp_customer='"+txtTlpCustomer.getText()+"' WHERE kode_customer='"+txtKodeCustomer.getText()+"'";
            Connection conn = (Connection)Koneksi.getKoneksi();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.execute();
            tampilDataCustomer(); tampilDataPenjualan(); transaksiTerbaru(); nullTableCustomer();
            JOptionPane.showMessageDialog(null, "Data Customer Berhasil Diubah !");
        }catch(HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_btnEditCustomerActionPerformed

    private void btnHapusCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusCustomerActionPerformed
      
        try {   
            int warningCustomer;
                if((warningCustomer = JOptionPane.showConfirmDialog(null, "Menghapus Data Customer Dapat Membuat Data Pada Laporan Penjualan Hilang !",
                    "Konfirmasi Hapus Data Customer", JOptionPane.YES_NO_OPTION)) == 0){
                    String sql = "DELETE FROM customer WHERE kode_customer='"+txtKodeCustomer.getText()+"'";
                    Connection conn = (Connection)Koneksi.getKoneksi();
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.execute();
                    tampilDataCustomer(); nullTableCustomer(); autoKodeCustomer();
                    JOptionPane.showMessageDialog(null, "Data Customer Berhasil Dihapus !");
                }
        }catch(SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_btnHapusCustomerActionPerformed

    private void btnResetCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetCustomerActionPerformed
      
        nullTableCustomer(); autoKodeCustomer();
    }//GEN-LAST:event_btnResetCustomerActionPerformed

    private void txtcariCustomerKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcariCustomerKeyReleased
        
        tampilDataCustomer();
    }//GEN-LAST:event_txtcariCustomerKeyReleased

    private void txtKodeCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKodeCustomerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKodeCustomerActionPerformed

    private void tabelCustomerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelCustomerMouseClicked
     
        int baris = tabelCustomer.rowAtPoint(evt.getPoint());
        
        String kode_customer = tabelCustomer.getValueAt(baris, 1).toString();
        txtKodeCustomer.setText(kode_customer);
        
        String nama_customer = tabelCustomer.getValueAt(baris, 2).toString();
        txtNamaCustomer.setText(nama_customer);
        
        String alamat_customer = tabelCustomer.getValueAt(baris, 3).toString();
        txtAlamatCustomer.setText(alamat_customer);
        
        String tlp_customer = tabelCustomer.getValueAt(baris, 4).toString();
        txtTlpCustomer.setText(tlp_customer);
    }//GEN-LAST:event_tabelCustomerMouseClicked

    private void transaksiKodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transaksiKodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_transaksiKodeActionPerformed

    private void trHargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trHargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_trHargaActionPerformed

    private void trStokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trStokActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_trStokActionPerformed

    private void trQtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trQtyActionPerformed
        // TODO add your handling code here:
       
    }//GEN-LAST:event_trQtyActionPerformed
    
    private void tambahKeranjangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahKeranjangActionPerformed
        
        try {
            if (trQty.getText().equals("") | trIdProduk.getText().equals("")||trTotal.getText().equals("") ){
                JOptionPane.showMessageDialog(null, "Gagal Menambahkan Barang Ke Keranjang, Isi Jumlah Beli !");
            } else {
              String sql = "INSERT INTO transaksi (`id_transaksi`,`id_produk`,`qty`,`stok_awal`,`grand_total`)VALUES "
              + "(default ,'"+trIdProduk.getText()+"', '"+trQty.getText()+"', '"+trStok.getText()+"','"+trTotal.getText()+"')";
              Connection conn = (Connection)Koneksi.getKoneksi();
              PreparedStatement pst = conn.prepareStatement(sql);
              pst.execute();
              updateStok();  tampilInputTransaksi(); tampilDataProduk();
              totalBiaya(); nullTableKeranjang();

              JOptionPane.showMessageDialog(null, "Barang Berhasil Ditambah Ke Keranjang !");
            } 
        }catch(HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_tambahKeranjangActionPerformed

    private void resetTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetTransaksiActionPerformed
        
        nullTableKeranjang();
    }//GEN-LAST:event_resetTransaksiActionPerformed

    private void txtTglActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTglActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTglActionPerformed
    
    private void tambahNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahNotaActionPerformed
      
       functionAddNota(); 
    }//GEN-LAST:event_tambahNotaActionPerformed
    
    private void txtTotalBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalBayarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalBayarActionPerformed

    private void txtBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBayarActionPerformed
        // TODO add your handling code here:
        functionAddNota();
    }//GEN-LAST:event_txtBayarActionPerformed

    private void txtKembalianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKembalianActionPerformed
  
        hitungKembalian();
    }//GEN-LAST:event_txtKembalianActionPerformed

    private void txtTotalKeseluruhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalKeseluruhanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalKeseluruhanActionPerformed

    private void trTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_trTotalActionPerformed

    private void trQtyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_trQtyKeyReleased
   
        hitungTotalKeranjang();
    }//GEN-LAST:event_trQtyKeyReleased

    private void txtBayarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBayarKeyReleased
        // TODO add your handling code here:
        Kembalian();
    }//GEN-LAST:event_txtBayarKeyReleased

    private void dashboardCariNewTransaksiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dashboardCariNewTransaksiKeyReleased
 
        transaksiTerbaru();
    }//GEN-LAST:event_dashboardCariNewTransaksiKeyReleased

    private void txtCariPenjualanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariPenjualanKeyReleased
        
        tampilDataPenjualan();
    }//GEN-LAST:event_txtCariPenjualanKeyReleased

    private void tabelLapPenjualanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelLapPenjualanMouseClicked
      
        int baris = tabelLapPenjualan.rowAtPoint(evt.getPoint());
        
        String kode_barang = tabelLapPenjualan.getValueAt(baris, 1).toString();
        txtCariPenjualan.setText(kode_barang);
    }//GEN-LAST:event_tabelLapPenjualanMouseClicked

    private void btnSearchByDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchByDateActionPerformed
     
        searchByDate();
    }//GEN-LAST:event_btnSearchByDateActionPerformed

    private void dashboardCariNewTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashboardCariNewTransaksiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dashboardCariNewTransaksiActionPerformed

    private void trQtyKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_trQtyKeyTyped
      
        char QtyTyped=evt.getKeyChar();
        if(!(Character.isDigit(QtyTyped))){
            evt.consume();
        }
    }//GEN-LAST:event_trQtyKeyTyped

    private void txtBayarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBayarKeyTyped
      
        char BayarKeyTyped=evt.getKeyChar();
        if(!(Character.isDigit(BayarKeyTyped))){
            evt.consume();
        }
    }//GEN-LAST:event_txtBayarKeyTyped

    private void txtTlpCustomerKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTlpCustomerKeyTyped
      
        char TlpCustomerKeyTyped=evt.getKeyChar();
        if(!(Character.isDigit(TlpCustomerKeyTyped))){
            evt.consume();
        }
    }//GEN-LAST:event_txtTlpCustomerKeyTyped

    private void txtTlpSupplierKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTlpSupplierKeyTyped
      
        char TlpSupplierKeyTyped=evt.getKeyChar();
        if(!(Character.isDigit(TlpSupplierKeyTyped))){
            evt.consume();
        }
    }//GEN-LAST:event_txtTlpSupplierKeyTyped

    private void txthargabeliKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txthargabeliKeyTyped
      
        char hargabeliKeyTyped=evt.getKeyChar();
        if(!(Character.isDigit(hargabeliKeyTyped))){
            evt.consume();
        }
    }//GEN-LAST:event_txthargabeliKeyTyped

    private void txthargajualKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txthargajualKeyTyped
      
        char hargajualKeyTyped=evt.getKeyChar();
        if(!(Character.isDigit(hargajualKeyTyped))){
            evt.consume();
        }
    }//GEN-LAST:event_txthargajualKeyTyped

    private void txtstokKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtstokKeyTyped
      
        char stokKeyTyped=evt.getKeyChar();
        if(!(Character.isDigit(stokKeyTyped))){
            evt.consume();
        }
    }//GEN-LAST:event_txtstokKeyTyped

    private void refreshDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshDashboardActionPerformed
      
        countData();
    }//GEN-LAST:event_refreshDashboardActionPerformed

    private void txtTotalPengeluaranKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTotalPengeluaranKeyTyped
      
        char TotalPengeluaranKeyTyped=evt.getKeyChar();
        if(!(Character.isDigit(TotalPengeluaranKeyTyped))){
            evt.consume();
        }
    }//GEN-LAST:event_txtTotalPengeluaranKeyTyped

    private void btnSimpanPengeluaranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanPengeluaranActionPerformed
      
        String datePengeluaran = "yyyy-MM-dd";
        SimpleDateFormat dP = new SimpleDateFormat(datePengeluaran);
        String tglPengeluaran = String.valueOf(dP.format(txtTglPengeluaran.getDate()));
        try {
            if (txtKeteranganPengeluaran.getText().equals("") | txtTotalPengeluaran.getText().equals("") ){
                JOptionPane.showMessageDialog(null, "Field Tidak Boleh Kosong !");
            } else {
              String sql = "INSERT INTO pengeluaran (`id_pengeluaran`,`no_pengeluaran`,`jenis_pengeluaran`,`keterangan`,`jumlah`,"
                    + "`total_pengeluaran`,`tgl_pengeluaran`) VALUES (default, '"+txtIdPengeluaran.getText()+"' ,'"+cbPengeluaran.getSelectedItem()+"', "
                    + "'"+txtKeteranganPengeluaran.getText()+"', '"+txtJumlahPengeluaran.getText()+"',"
                    + "'"+txtTotalPengeluaran.getText()+"','"+tglPengeluaran+"')";
              Connection conn = (Connection)Koneksi.getKoneksi();
              PreparedStatement pst = conn.prepareStatement(sql);
              pst.execute();
              tampilDataPengeluaran(); tampilLaporanPengeluaran();  nullTablePengeluaran(); 
              countData(); autoKodePengeluaran();

              JOptionPane.showMessageDialog(null, "Data Pengeluaran Berhasil Ditambah !");
            } 
        }catch(HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_btnSimpanPengeluaranActionPerformed

    private void btnEditPengeluaranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditPengeluaranActionPerformed
      
        String datePengeluaran = "yyyy-MM-dd";
        SimpleDateFormat dP = new SimpleDateFormat(datePengeluaran);
        String tglPengeluaran = String.valueOf(dP.format(txtTglPengeluaran.getDate()));
        try{
            String sql = "UPDATE pengeluaran SET no_pengeluaran='"+txtIdPengeluaran.getText()+"',jenis_pengeluaran='"+cbPengeluaran.getSelectedItem()+"',"
                + " keterangan='"+txtKeteranganPengeluaran.getText()+"', jumlah='"+txtJumlahPengeluaran.getText()+"',"
                + " total_pengeluaran='"+txtTotalPengeluaran.getText()+"',tgl_pengeluaran='"+tglPengeluaran+"' WHERE no_pengeluaran='"+txtIdPengeluaran.getText()+"'";
            Connection conn = (Connection)Koneksi.getKoneksi();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.execute();
            tampilDataPengeluaran(); tampilLaporanPengeluaran(); nullTablePengeluaran();
            countData(); autoKodePengeluaran();
            JOptionPane.showMessageDialog(null, "Data Pengeluaran Berhasil Diubah !");
        }catch(HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_btnEditPengeluaranActionPerformed

    private void btnHapusPengeluaranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusPengeluaranActionPerformed
      
        try {   
            int warningPengeluaran;
                if((warningPengeluaran = JOptionPane.showConfirmDialog(null, "Hapus data yang dipilih ?", "Konfirmasi", 
                        JOptionPane.YES_NO_OPTION)) == 0){
                    String sql = "DELETE FROM pengeluaran WHERE no_pengeluaran='"+txtIdPengeluaran.getText()+"'";
                    Connection conn = (Connection)Koneksi.getKoneksi();
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.execute();
                    tampilDataPengeluaran(); tampilLaporanPengeluaran();
                    nullTablePengeluaran(); countData(); autoKodePengeluaran();
                    JOptionPane.showMessageDialog(null, "Data Pengeluaran Yang Dipilih Berhasil Dihapus !");
                }
        }catch(SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_btnHapusPengeluaranActionPerformed

    private void btnResetPengeluaranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetPengeluaranActionPerformed
      
        nullTablePengeluaran(); autoKodePengeluaran();
    }//GEN-LAST:event_btnResetPengeluaranActionPerformed

    private void txtcariPengeluaranKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcariPengeluaranKeyReleased
      
        tampilDataPengeluaran();
    }//GEN-LAST:event_txtcariPengeluaranKeyReleased

    private void txtIdPengeluaranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdPengeluaranActionPerformed
      
    }//GEN-LAST:event_txtIdPengeluaranActionPerformed

    private void tabelPengeluaranMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelPengeluaranMouseClicked
      
        
        int baris = tabelPengeluaran.rowAtPoint(evt.getPoint());
        
        String id_pengeluaran= tabelPengeluaran.getValueAt(baris, 1).toString();
        txtIdPengeluaran.setText(id_pengeluaran);
        
        String jenis_pengeluaran= tabelPengeluaran.getValueAt(baris, 2).toString();
        cbPengeluaran.setSelectedItem(jenis_pengeluaran);
        
        String keterangan= tabelPengeluaran.getValueAt(baris, 3).toString();
        txtKeteranganPengeluaran.setText(keterangan);
        
        String jumlah = tabelPengeluaran.getValueAt(baris, 4).toString();
        txtJumlahPengeluaran.setText(jumlah);
        
        String total_pengeluaran = tabelPengeluaran.getValueAt(baris, 5).toString();
        txtTotalPengeluaran.setText(total_pengeluaran);
        
        txtTglPengeluaran.setDate(getTanggalPengeluaran(tabelPengeluaran, 6));   
      
    }//GEN-LAST:event_tabelPengeluaranMouseClicked

    private void cbPengeluaranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbPengeluaranActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbPengeluaranActionPerformed

    private void txtJumlahPengeluaranKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJumlahPengeluaranKeyTyped
      
        char JumlahPengeluaranKeyTyped=evt.getKeyChar();
        if(!(Character.isDigit(JumlahPengeluaranKeyTyped))){
            evt.consume();
            System.out.println("Field Hanya Berupa Angka !");
        }
    }//GEN-LAST:event_txtJumlahPengeluaranKeyTyped

    private void lapPengeluaranMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lapPengeluaranMenuActionPerformed
      
//      Remove Panel
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();
//      Add Panel
        mainPanel.add(lapPengeluaranPanel);
        menuTitle.setText("Laporan | Laporan Pengeluaran");  
        mainPanel.repaint();
        mainPanel.revalidate();
    }//GEN-LAST:event_lapPengeluaranMenuActionPerformed

    private void lapProdukMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lapProdukMenuActionPerformed
      
//      Remove Panel
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();
//      Add Panel
        mainPanel.add(lapInventoryPanel);
        menuTitle.setText("Laporan | Laporan Inventory");  
        mainPanel.repaint();
        mainPanel.revalidate();
    }//GEN-LAST:event_lapProdukMenuActionPerformed

    private void kelolaAkunMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kelolaAkunMenuActionPerformed
      
//      Remove Panel
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();
//      Add Panel
        mainPanel.add(kelolaAkunPanel);
        menuTitle.setText("Pengaturan | Kelola Akun");  
        mainPanel.repaint();
        mainPanel.revalidate();
    }//GEN-LAST:event_kelolaAkunMenuActionPerformed

    private void txtTlpCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTlpCustomerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTlpCustomerActionPerformed

    private void tabelInventoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelInventoryMouseClicked
      
    }//GEN-LAST:event_tabelInventoryMouseClicked

    private void cetakDataInventoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cetakDataInventoryActionPerformed
      
        int confirmCetakInven = JOptionPane.showConfirmDialog(null, "Laporan Inventory Yang Dicetak Berdasarkan Data Yang Ada Saat Ini !",
            "Cetak Data Inventory", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirmCetakInven == JOptionPane.YES_OPTION){
            cetakDataProduk();
            JOptionPane.showMessageDialog(null, "Laporan Inventory Berhasil Dicetak !");
        } 
    }//GEN-LAST:event_cetakDataInventoryActionPerformed

    private void btnCariCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariCustomerActionPerformed
      
        new customerModel().setVisible(true);
    }//GEN-LAST:event_btnCariCustomerActionPerformed

    private void txNamaCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txNamaCustomerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txNamaCustomerActionPerformed

    private void sumHargaBeliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sumHargaBeliMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_sumHargaBeliMouseClicked

    private void sumStokMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sumStokMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_sumStokMouseClicked

    private void sumHargaJualMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sumHargaJualMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_sumHargaJualMouseClicked

    private void TotalLabelInvenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TotalLabelInvenMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_TotalLabelInvenMouseClicked

    private void txtCariInventoryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariInventoryKeyReleased
      
        tampilDataInventory();
    }//GEN-LAST:event_txtCariInventoryKeyReleased

    private void trIdProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trIdProdukActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_trIdProdukActionPerformed

    private void btnCariProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariProdukActionPerformed
      
        new cariBarangModel().setVisible(true);
    }//GEN-LAST:event_btnCariProdukActionPerformed

    private void tabelInputTransaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelInputTransaksiMouseClicked
      
        int baris = tabelInputTransaksi.rowAtPoint(evt.getPoint());

        String id_transaksi = tabelInputTransaksi.getValueAt(baris, 0).toString();
        trIdProduk.setText(id_transaksi);
        String stok = tabelInputTransaksi.getValueAt(baris, 5).toString();
        trStok.setText(stok);
            try {
                int warningTrInput;
                if((warningTrInput = JOptionPane.showConfirmDialog(null, "Hapus barang yang dipilih ?", "Konfirmasi",
                    JOptionPane.YES_NO_OPTION)) == 0){
                    String sql = "DELETE FROM transaksi WHERE id_produk='"+trIdProduk.getText()+"'";
                    Connection conn = (Connection)Koneksi.getKoneksi();
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.execute();
                    returnStok(); tampilInputTransaksi();
                    nullTableKeranjang(); nullTransaksi(); totalBiaya(); tampilDataProduk();
                    JOptionPane.showMessageDialog(null, "Barang Yang Dipilih Berhasil Dihapus !");
                }
            }catch(SQLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
    }//GEN-LAST:event_tabelInputTransaksiMouseClicked

    private void trNamaBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trNamaBarangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_trNamaBarangActionPerformed

    private void txIdCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txIdCustomerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txIdCustomerActionPerformed

    private void cetakDataByNoInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cetakDataByNoInvoiceActionPerformed
       int confirmByNoInvoice = JOptionPane.showConfirmDialog(null, "Cetak Laporan Berdasarkan No Invoice  ?", "Laporan Penjualan", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirmByNoInvoice == JOptionPane.YES_OPTION){
            cetakDataByInvoice();
        }         
    }//GEN-LAST:event_cetakDataByNoInvoiceActionPerformed

    private void cetakDataeByNoInvoice1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cetakDataeByNoInvoice1ActionPerformed
      
        int confirmCetakPenjualan = JOptionPane.showConfirmDialog(null, "Laporan Penjualan Yang Dicetak Berdasarkan Data Yang Ada Saat Ini !",
            "Cetak Data Penjualan", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirmCetakPenjualan == JOptionPane.YES_OPTION){
            cetakDataPenjualan();
            JOptionPane.showMessageDialog(null, "Laporan Penjualan Berhasil Dicetak !");
        } 
    }//GEN-LAST:event_cetakDataeByNoInvoice1ActionPerformed

    private void cetakDataPengeluaranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cetakDataPengeluaranActionPerformed
      
        int confirmCetakPengeluaran = JOptionPane.showConfirmDialog(null, "Laporan Pengeluaran Yang Dicetak Berdasarkan Data Yang Ada Saat Ini !",
            "Cetak Laporan Pengeluaran", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirmCetakPengeluaran == JOptionPane.YES_OPTION){
            cetakLaporanPengeluaran();
            JOptionPane.showMessageDialog(null, "Laporan Pengeluaran Berhasil Dicetak !");
        } 
    }//GEN-LAST:event_cetakDataPengeluaranActionPerformed

    private void txtCariPengeluaranKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariPengeluaranKeyReleased
         tampilLaporanPengeluaran();
    }//GEN-LAST:event_txtCariPengeluaranKeyReleased

    private void tabelLaporanPengeluaranMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelLaporanPengeluaranMouseClicked

    }//GEN-LAST:event_tabelLaporanPengeluaranMouseClicked

    private void tabelKeuanganMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelKeuanganMouseClicked
        int baris = tabelKeuangan.rowAtPoint(evt.getPoint());
        
        String tgl_dibuat= tabelKeuangan.getValueAt(baris, 1).toString();
        txtCariKeuangan.setText(tgl_dibuat);
        
    }//GEN-LAST:event_tabelKeuanganMouseClicked

    private void cetakDataKeuanganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cetakDataKeuanganActionPerformed
        int confirmCetakKeuangan = JOptionPane.showConfirmDialog(null, "Laporan Keuangan Yang Dicetak Berdasarkan Data Yang Ada Saat Ini !",
            "Cetak Laporan Keuangan", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirmCetakKeuangan == JOptionPane.YES_OPTION){
            try {
            
              String sql = "INSERT INTO laporan_keuangan (id_keuangan, tgl_dibuat, total_penjualan, "
                      +"beban_pengeluaran, laba_bersih) VALUES "
                      +"(default, CURDATE(), "
                      +"(SELECT SUM(total_harga) FROM transaksi_detail), "
                      +"(SELECT SUM(total_pengeluaran) FROM pengeluaran), "
                      +"(SELECT SUM(total_harga) FROM transaksi_detail) - "
                      +"(SELECT SUM(total_pengeluaran) FROM pengeluaran))";
              Connection conn = (Connection)Koneksi.getKoneksi();
              PreparedStatement pst = conn.prepareStatement(sql);
              pst.execute();
              tampilLaporanKeuangan(); cetakLaporanKeuangan();
              JOptionPane.showMessageDialog(null, "Laporan Keuangan Berhasil Ditambah !");
            
        }catch(HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
            
        }
    }//GEN-LAST:event_cetakDataKeuanganActionPerformed

    private void txtIdKasirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdKasirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdKasirActionPerformed

    private void txNamaSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txNamaSupplierActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txNamaSupplierActionPerformed

    private void txtIdSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdSupplierActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdSupplierActionPerformed

    private void btnCariSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariSupplierActionPerformed
        // TODO add your handling code here:
        new supplierModel().setVisible(true);
    }//GEN-LAST:event_btnCariSupplierActionPerformed

    private void txtCariKeuanganKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeuanganKeyReleased

        tampilLaporanKeuangan();
    }//GEN-LAST:event_txtCariKeuanganKeyReleased

    private void CetakKeuanganByMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CetakKeuanganByMonthActionPerformed
        int confirmKeuanganByMonthYear = JOptionPane.showConfirmDialog(null, "Cetak Laporan Berdasarkan Bulan & Tahun  ?", "Laporan Keuangan", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirmKeuanganByMonthYear == JOptionPane.YES_OPTION){
            cetakKeuanganByMonthYear();
        }     
    }//GEN-LAST:event_CetakKeuanganByMonthActionPerformed

    private void CetakPengeluaranByMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CetakPengeluaranByMonthActionPerformed
        int confirmPengeluaranByMonth = JOptionPane.showConfirmDialog(null, "Cetak Laporan Berdasarkan Bulan & Tahun  ?", "Laporan Pengeluaran", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirmPengeluaranByMonth == JOptionPane.YES_OPTION){
            cetakPengeluaranByMonthYear();
        }    
    }//GEN-LAST:event_CetakPengeluaranByMonthActionPerformed

    private void iconUserLogMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconUserLogMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_iconUserLogMouseClicked

    private void nameLogMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nameLogMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_nameLogMouseClicked

    private void divideMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_divideMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_divideMouseClicked

    private void CetakPenjualanByMonthYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CetakPenjualanByMonthYearActionPerformed
       int confirmPenjualanByMonthYear = JOptionPane.showConfirmDialog(null, "Cetak Laporan Berdasarkan Bulan & Tahun  ?", "Laporan Penjualan", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirmPenjualanByMonthYear == JOptionPane.YES_OPTION){
            cetakPenjualanByMonthYear();
        } 
    }//GEN-LAST:event_CetakPenjualanByMonthYearActionPerformed

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
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CetakKeuanganByMonth;
    private javax.swing.JButton CetakPengeluaranByMonth;
    private javax.swing.JButton CetakPenjualanByMonthYear;
    private javax.swing.JScrollPane JPtabelInventory;
    private javax.swing.JScrollPane JPtabelInventory1;
    private javax.swing.JComboBox<String> JcBulan;
    private javax.swing.JComboBox<String> JcBulanPengeluaran;
    private javax.swing.JComboBox<String> JcBulanPenjualan;
    private com.toedter.calendar.JYearChooser JcTahun;
    private com.toedter.calendar.JYearChooser JcTahunPengeluaran;
    private com.toedter.calendar.JYearChooser JcTahunPenjualan;
    private javax.swing.JScrollPane JpTbPenjualan;
    private javax.swing.JScrollPane JptabelInputTransaksi;
    private javax.swing.JLabel KeteranganLabel;
    private javax.swing.JLabel Logo;
    private javax.swing.JLabel PengaturanLabel;
    private javax.swing.JLabel PengaturanLabel1;
    private javax.swing.JLabel StokLabel;
    public static final javax.swing.JLabel TotalLabelInven = new javax.swing.JLabel();
    private javax.swing.JPanel body;
    private javax.swing.JButton btnCariCustomer;
    private javax.swing.JButton btnCariProduk;
    private javax.swing.JButton btnCariSupplier;
    private javax.swing.JButton btnEditCustomer;
    private javax.swing.JButton btnEditPengeluaran;
    private javax.swing.JButton btnEditProduk;
    private javax.swing.JButton btnEditSupplier;
    private javax.swing.JButton btnEditUsers;
    private javax.swing.JButton btnHapusCustomer;
    private javax.swing.JButton btnHapusPengeluaran;
    private javax.swing.JButton btnHapusProduk;
    private javax.swing.JButton btnHapusSupplier;
    private javax.swing.JButton btnHapusUsers;
    private javax.swing.JButton btnResetCustomer;
    private javax.swing.JButton btnResetPengeluaran;
    private javax.swing.JButton btnResetProduk;
    private javax.swing.JButton btnResetSupplier;
    private javax.swing.JButton btnResetUsers;
    private javax.swing.JButton btnSearchByDate;
    private javax.swing.JButton btnSimpanCustomer;
    private javax.swing.JButton btnSimpanPengeluaran;
    private javax.swing.JButton btnSimpanProduk;
    private javax.swing.JButton btnSimpanSupplier;
    private javax.swing.JButton btnSimpanUsers;
    private javax.swing.JPanel card1Image;
    private javax.swing.JLabel card1Subtitle;
    private javax.swing.JPanel card2Image;
    private javax.swing.JLabel card2Subtitle;
    private javax.swing.JLabel card2Title;
    private javax.swing.JPanel card3Image2;
    private javax.swing.JPanel card3Image3;
    private javax.swing.JLabel card3Subtitle2;
    private javax.swing.JLabel card3Subtitle3;
    private javax.swing.JLabel card3Title2;
    private javax.swing.JLabel card3Title3;
    private javax.swing.JPanel cardKeuntungan;
    private javax.swing.JPanel cardLabaBersih;
    private javax.swing.JPanel cardPemasukan;
    private javax.swing.JPanel cardPengeluaran;
    private javax.swing.JPanel cardProdukSale;
    private javax.swing.JLabel cardTitle1;
    private javax.swing.JPanel cardTotalPemasukan;
    private javax.swing.JPanel cardTotalStok;
    private javax.swing.JPanel cardTransaksi;
    private javax.swing.JPanel cardTransaksi3;
    private javax.swing.JComboBox<String> cbPengeluaran;
    private javax.swing.JComboBox<String> cbUsersKelola;
    private javax.swing.JComboBox<String> cbkategori;
    private javax.swing.JButton cetakDataByNoInvoice;
    private javax.swing.JButton cetakDataInventory;
    private javax.swing.JButton cetakDataKeuangan;
    private javax.swing.JButton cetakDataPengeluaran;
    private javax.swing.JButton cetakDataeByNoInvoice1;
    private javax.swing.JPanel container;
    private javax.swing.JPanel crudCustomerPanel;
    private javax.swing.JPanel crudKelolaAkunPanel;
    private javax.swing.JPanel crudPengeluaranPanel;
    private javax.swing.JPanel crudProdukPanel;
    private javax.swing.JPanel crudSupplierPanel;
    public static final javax.swing.JButton customerMenu = new javax.swing.JButton();
    private javax.swing.JPanel customerPanel;
    public static final javax.swing.JButton dashboard = new javax.swing.JButton();
    private javax.swing.JTextField dashboardCariNewTransaksi;
    private javax.swing.JPanel dashboardPanel;
    private static javax.swing.JLabel dataKeuntungan;
    private static javax.swing.JLabel dataLabaRugi;
    private static javax.swing.JLabel dataPengeluaran;
    private static javax.swing.JLabel dataTotalPemasukan;
    private static javax.swing.JLabel dataTotalTransaksi3;
    public static final javax.swing.JLabel divide = new javax.swing.JLabel();
    private javax.swing.JPanel footer;
    private javax.swing.JLabel iconLogout;
    private javax.swing.JLabel iconUserLog;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    public static final javax.swing.JButton kelolaAkunMenu = new javax.swing.JButton();
    private javax.swing.JPanel kelolaAkunPanel;
    private javax.swing.JLabel labelAlamatCustomer;
    private javax.swing.JLabel labelAlamatSupplier;
    private javax.swing.JLabel labelAutoKodeTransaksi;
    private javax.swing.JLabel labelCariAkun;
    private javax.swing.JLabel labelCariCustomer;
    private javax.swing.JLabel labelCariInventory;
    private javax.swing.JLabel labelCariKeuangan;
    private javax.swing.JLabel labelCariLapPengeluaran;
    private javax.swing.JLabel labelCariNewTransaksi;
    private javax.swing.JLabel labelCariPengeluaran;
    private javax.swing.JLabel labelCariPenjualan1;
    private javax.swing.JLabel labelCariProduk;
    private javax.swing.JLabel labelCariSupplier;
    private javax.swing.JLabel labelCbUser;
    private javax.swing.JLabel labelCetakByInvoice;
    private javax.swing.JLabel labelCetakKeuanganByMonth;
    private javax.swing.JLabel labelCetakKeuanganByMonth1;
    private javax.swing.JLabel labelCetakPenjualanByMonthYear;
    private javax.swing.JLabel labelCustomer;
    private javax.swing.JLabel labelDateTransaksi;
    private javax.swing.JLabel labelDeskripsi;
    private javax.swing.JLabel labelHarga;
    private javax.swing.JLabel labelHargaBeli;
    private javax.swing.JLabel labelHargaJual1;
    private javax.swing.JLabel labelId;
    private javax.swing.JLabel labelIdPengeluaran;
    private javax.swing.JLabel labelIdProduk;
    private javax.swing.JLabel labelJenisPengeluaran;
    private javax.swing.JLabel labelJumlah;
    private javax.swing.JLabel labelJumlahPengeluaran;
    private javax.swing.JLabel labelKategori;
    private javax.swing.JLabel labelKembalian;
    private javax.swing.JLabel labelKodeBarang;
    private javax.swing.JLabel labelKodeCustomer;
    private javax.swing.JLabel labelKodeSupplier;
    private javax.swing.JLabel labelNamaBarang;
    private javax.swing.JLabel labelNamaBarang1;
    private javax.swing.JLabel labelNamaCustomer;
    private javax.swing.JLabel labelNamaKelola;
    private javax.swing.JLabel labelNmBarang;
    private javax.swing.JLabel labelPassKelola;
    private javax.swing.JLabel labelStok;
    private javax.swing.JLabel labelSupplier;
    private javax.swing.JLabel labelTglPengeluaran;
    private javax.swing.JLabel labelTlpCustomer;
    private javax.swing.JLabel labelTlpSupplier;
    private javax.swing.JLabel labelTotal;
    private javax.swing.JLabel labelTotalBayar;
    private javax.swing.JLabel labelTotalPengeluaran;
    private javax.swing.JLabel labelUsernameKelola;
    private javax.swing.JLabel labelUtama;
    private javax.swing.JLabel labelUtama1;
    private javax.swing.JLabel labellBayar;
    private javax.swing.JPanel lapInventoryPanel;
    public static final javax.swing.JButton lapKeuanganMenu = new javax.swing.JButton();
    private javax.swing.JPanel lapKeuanganPanel;
    public static final javax.swing.JButton lapPengeluaranMenu = new javax.swing.JButton();
    private javax.swing.JPanel lapPengeluaranPanel;
    public static final javax.swing.JButton lapPenjualanMenu = new javax.swing.JButton();
    private javax.swing.JPanel lapPenjualanPanel;
    public static final javax.swing.JButton lapProdukMenu = new javax.swing.JButton();
    private javax.swing.JLayeredPane mainPanel;
    private javax.swing.JLabel menuTitle;
    public static final javax.swing.JLabel nameLog = new javax.swing.JLabel();
    private static javax.swing.JTable newTransaksi;
    private javax.swing.JPanel panelDateDashboard;
    private javax.swing.JPanel panelInputTransaksi;
    private javax.swing.JPanel panelTotalInventory;
    private javax.swing.JPanel panelTransaksi;
    public static final javax.swing.JButton pengeluaranMenu = new javax.swing.JButton();
    private javax.swing.JPanel pengeluaranPanel;
    public static final javax.swing.JButton penjualanMenu = new javax.swing.JButton();
    private javax.swing.JPanel penjualanPanel;
    public static final javax.swing.JButton produkMenu = new javax.swing.JButton();
    private javax.swing.JPanel produkPanel;
    private javax.swing.JButton refreshDashboard;
    private javax.swing.JButton resetTransaksi;
    private javax.swing.JSeparator separatorLogo1;
    private javax.swing.JSeparator separatorMenuUtama;
    private javax.swing.JSeparator separatorMenuUtama1;
    private javax.swing.JSeparator separatorMenuUtama2;
    private javax.swing.JSeparator separatorMenuUtama3;
    private javax.swing.JPanel sidebar;
    public static final javax.swing.JLabel sumHargaBeli = new javax.swing.JLabel();
    public static final javax.swing.JLabel sumHargaJual = new javax.swing.JLabel();
    public static final javax.swing.JLabel sumStok = new javax.swing.JLabel();
    public static final javax.swing.JButton supplierMenu = new javax.swing.JButton();
    private javax.swing.JPanel supplierPanel;
    private static javax.swing.JTable tabelCustomer;
    private static javax.swing.JTable tabelInputTransaksi;
    private static javax.swing.JTable tabelInventory;
    private static javax.swing.JTable tabelKelolaAkun;
    private javax.swing.JScrollPane tabelKelolaAkunPanel;
    private static javax.swing.JTable tabelKeuangan;
    private static javax.swing.JTable tabelLapPenjualan;
    private static javax.swing.JTable tabelLaporanPengeluaran;
    private static javax.swing.JTable tabelPengeluaran;
    private javax.swing.JScrollPane tabelPengeluaranPanel;
    private javax.swing.JScrollPane tabelPengeluaranPanel1;
    private static javax.swing.JTable tabelProduk;
    private javax.swing.JScrollPane tabelProdukPanel;
    private javax.swing.JScrollPane tabelProdukPanel1;
    private static javax.swing.JTable tabelSupplier;
    private javax.swing.JScrollPane tabelSupplierPanel;
    private javax.swing.JLabel tableTitle;
    private javax.swing.JScrollPane tableTransaksiTerbaru;
    private javax.swing.JButton tambahKeranjang;
    private javax.swing.JButton tambahNota;
    private javax.swing.JPanel topbar;
    private static javax.swing.JLabel totalPemasukan;
    private static javax.swing.JLabel totalProdukTerjual;
    public static final javax.swing.JLabel totalStokProduk = new javax.swing.JLabel();
    private static javax.swing.JLabel totalTransaksi;
    public static final javax.swing.JTextField trHarga = new javax.swing.JTextField();
    public static final javax.swing.JTextField trIdProduk = new javax.swing.JTextField();
    public static final javax.swing.JTextField trNamaBarang = new javax.swing.JTextField();
    public static final javax.swing.JTextField trQty = new javax.swing.JTextField();
    public static final javax.swing.JTextField trStok = new javax.swing.JTextField();
    public static final javax.swing.JTextField trTotal = new javax.swing.JTextField();
    public static final javax.swing.JTextField transaksiKode = new javax.swing.JTextField();
    public static final javax.swing.JTextField txIdCustomer = new javax.swing.JTextField();
    public static final javax.swing.JTextField txNamaCustomer = new javax.swing.JTextField();
    public static final javax.swing.JTextField txNamaSupplier = new javax.swing.JTextField();
    private javax.swing.JTextArea txtAlamatCustomer;
    private javax.swing.JTextArea txtAlamatSupplier;
    private javax.swing.JTextField txtBayar;
    private javax.swing.JTextField txtCariInventory;
    private javax.swing.JTextField txtCariKeuangan;
    private javax.swing.JTextField txtCariPengeluaran;
    private javax.swing.JTextField txtCariPenjualan;
    private javax.swing.JTextArea txtDeskripsi;
    public static final javax.swing.JTextField txtIdKasir = new javax.swing.JTextField();
    private javax.swing.JTextField txtIdPengeluaran;
    public static final javax.swing.JTextField txtIdSupplier = new javax.swing.JTextField();
    private javax.swing.JTextField txtJumlahPengeluaran;
    private javax.swing.JTextField txtKembalian;
    private javax.swing.JTextArea txtKeteranganPengeluaran;
    private javax.swing.JTextField txtKodeCustomer;
    private javax.swing.JTextField txtKodeSupplier;
    private javax.swing.JTextField txtNamaCustomer;
    private javax.swing.JTextField txtNamaSupplier;
    private javax.swing.JTextField txtTgl;
    private com.toedter.calendar.JDateChooser txtTglPengeluaran;
    private javax.swing.JTextField txtTlpCustomer;
    private javax.swing.JTextField txtTlpSupplier;
    private javax.swing.JTextField txtTotalBayar;
    private javax.swing.JTextField txtTotalKeseluruhan;
    private javax.swing.JTextField txtTotalPengeluaran;
    private javax.swing.JTextField txtcariCustomer;
    private javax.swing.JTextField txtcariPengeluaran;
    private javax.swing.JTextField txtcariProduk;
    private javax.swing.JTextField txtcariSupplier;
    private javax.swing.JTextField txtcariUsers;
    private javax.swing.JTextField txthargabeli;
    private javax.swing.JTextField txthargajual;
    private javax.swing.JTextField txtidUser;
    private javax.swing.JTextField txtkodebarang;
    private javax.swing.JTextField txtnama;
    private javax.swing.JTextField txtnamabarang;
    private javax.swing.JTextField txtpassword;
    private javax.swing.JTextField txtstok;
    private javax.swing.JTextField txtusername;
    private javax.swing.JPanel userArea;
    // End of variables declaration//GEN-END:variables
}
