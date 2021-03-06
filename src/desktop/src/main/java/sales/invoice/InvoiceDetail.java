
package sales.invoice;

import SIIL.Server.Database;
import SIIL.core.Office;
import core.OfficeComboBoxModel;
import core.bobeda.Archivo;
import core.bobeda.FTP;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import process.Operational;
import process.Return;
import sales.Invoice;

/**
 *
 * @author Azael Reyes
 */
public class InvoiceDetail extends javax.swing.JPanel {

    private Database dbserver;    
    private SIIL.core.config.Server serverConfig;
    private Office office;
    private Invoice invoice;
    private String serie;
    
    private void openDatabase(boolean  reclicleConextion)
    {
        try 
        {
            SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
            
            if(reclicleConextion)
            {
                if(dbserver != null)
                {
                    if(dbserver.getConnection().isValid(50))
                    {
                        return;
                    }
                    else
                    {
                        ;
                    }
                }
                else
                {
                    
                }
            }
            else
            {
                if(dbserver != null)
                {
                    if(!dbserver.getConnection().isClosed())dbserver.close();
                    dbserver = null;                    
                }
            }
            dbserver = null;
            dbserver = new Database(serverConfig);
        } 
        catch (IOException | ClassNotFoundException | SQLException | ParserConfigurationException | SAXException ex) 
        {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void closeDatabase() 
    {
        if(dbserver != null)
        {
            dbserver.close();
            dbserver = null;
        }
    }
    
    /**
     * Creates new form InvoiceDetail
     */
    public InvoiceDetail() {
        initComponents();
        openDatabase(true);
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
        txFolio = new javax.swing.JTextField();
        btOrdenes = new javax.swing.JButton();
        btPDF = new javax.swing.JButton();
        btXML = new javax.swing.JButton();
        btOrdenesDown = new javax.swing.JButton();
        btPDFDown = new javax.swing.JButton();
        btXMLDown = new javax.swing.JButton();
        cbSerie = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();

        jLabel1.setText("Folio");

        txFolio.setEnabled(false);
        txFolio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txFolioKeyPressed(evt);
            }
        });

        btOrdenes.setText("Ver Ordenes");
        btOrdenes.setEnabled(false);
        btOrdenes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btOrdenesActionPerformed(evt);
            }
        });

        btPDF.setText("Ver PDF");
        btPDF.setEnabled(false);
        btPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btPDFActionPerformed(evt);
            }
        });

        btXML.setText("Ver XML");
        btXML.setEnabled(false);
        btXML.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btXMLActionPerformed(evt);
            }
        });

        btOrdenesDown.setText("Download  Ordenes");
        btOrdenesDown.setEnabled(false);
        btOrdenesDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btOrdenesDownActionPerformed(evt);
            }
        });

        btPDFDown.setText("Download PDF");
        btPDFDown.setEnabled(false);
        btPDFDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btPDFDownActionPerformed(evt);
            }
        });

        btXMLDown.setText("Download XML");
        btXMLDown.setEnabled(false);
        btXMLDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btXMLDownActionPerformed(evt);
            }
        });

        cbSerie.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccione...", "A", "B", "C", "D", "TA", "TD", "CTJ", "CMX", "CEN" }));
        cbSerie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSerieActionPerformed(evt);
            }
        });

        jLabel4.setText("Serie");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btOrdenes)
                            .addComponent(btOrdenesDown))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btPDF)
                            .addComponent(btPDFDown))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btXMLDown, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btXML, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbSerie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txFolio, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txFolio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(cbSerie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btOrdenes)
                    .addComponent(btPDF)
                    .addComponent(btXML))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btOrdenesDown)
                    .addComponent(btPDFDown)
                    .addComponent(btXMLDown))
                .addContainerGap(69, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btOrdenesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btOrdenesActionPerformed
        try 
        {
            openDatabase(true);            
            FTP ftpServer = new FTP();
            boolean expResult = false;
            try 
            {
                expResult = ftpServer.connect(serverConfig);
            } 
            catch (IOException ex) 
            {
                //Logger.getLogger(FTPTest.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error externo",
                JOptionPane.ERROR_MESSAGE
                );
            }            
            File into = new File(System.getProperty("java.io.tmpdir"));                 
            Archivo archivo = invoice.getOrdenesArchivo();
            if(!archivo.download(dbserver,ftpServer,into))
            {
                JOptionPane.showMessageDialog(this,
                "Fall?? la descarga de archivo.",
                "Error externo",
                JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            archivo.getDownloadedFile().close();
            Desktop.getDesktop().open(new File(archivo.getDownloadFileName()));
        } 
        catch (SQLException | IOException ex) 
        {
            //Logger.getLogger(CreateUpdate.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
        }  
    }//GEN-LAST:event_btOrdenesActionPerformed

    private void txFolioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txFolioKeyPressed
        if(java.awt.event.KeyEvent.VK_ENTER == evt.getKeyCode())
        {
            try 
            {
                Return ret = Invoice.exist(dbserver, serie, txFolio.getText(), Operational.Type.SalesInvoice);
                if(ret.isFlag())
                {
                    invoice = new Invoice((int)ret.getParam());
                    invoice.download(dbserver);
                    if(invoice.downOrdenesArchivo(dbserver))
                    {
                        btOrdenes.setEnabled(true);
                        btOrdenesDown.setEnabled(true);
                    }
                    else
                    {
                        btOrdenes.setEnabled(false);
                        btOrdenesDown.setEnabled(false);
                    }
                    if(invoice.downPDFArchivo(dbserver))
                    {
                        btPDF.setEnabled(true);
                        btPDFDown.setEnabled(true);
                    }
                    else
                    {
                        btPDF.setEnabled(false);
                        btPDFDown.setEnabled(false);
                    }
                    if(invoice.downXMLArchivo(dbserver))
                    {
                        btXML.setEnabled(true);
                        btXMLDown.setEnabled(true);
                    }
                    else
                    {
                        btXML.setEnabled(false);
                        btXMLDown.setEnabled(false);
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(
                        this,
                        ret.getMessage(),
                        "Error externo",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(InvoiceDetail.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Error externo",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }//GEN-LAST:event_txFolioKeyPressed

    private void btPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btPDFActionPerformed
        try 
        {
            openDatabase(true);            
            FTP ftpServer = new FTP();
            boolean expResult = false;
            try 
            {
                expResult = ftpServer.connect(serverConfig);
            } 
            catch (IOException ex) 
            {
                //Logger.getLogger(FTPTest.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error externo",
                JOptionPane.ERROR_MESSAGE
                );
            }            
            File into = new File(System.getProperty("java.io.tmpdir"));                 
            Archivo archivo = invoice.getPDFArchivo();
            if(!archivo.download(dbserver,ftpServer,into))
            {
                JOptionPane.showMessageDialog(this,
                "Fall?? la descarga de archivo.",
                "Error externo",
                JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            archivo.getDownloadedFile().close();
            Desktop.getDesktop().open(new File(archivo.getDownloadFileName()));
        } 
        catch (SQLException | IOException ex) 
        {
            //Logger.getLogger(CreateUpdate.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_btPDFActionPerformed

    private void btOrdenesDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btOrdenesDownActionPerformed
        try 
        {
            openDatabase(true);            
            FTP ftpServer = new FTP();
            boolean expResult = false;
            try 
            {
                expResult = ftpServer.connect(serverConfig);
            } 
            catch (IOException ex) 
            {
                //Logger.getLogger(FTPTest.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error externo",
                JOptionPane.ERROR_MESSAGE
                );
            }       
            final JFileChooser fc = new JFileChooser();   
            FileSystemView fw = fc.getFileSystemView();
            fc.setCurrentDirectory(fw.getDefaultDirectory());
            fc.setSelectedFile(new File(invoice.getOrdenesArchivo().getNombre()));
            int returnVal = fc.showSaveDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION)
            {

            }
            else
            {
                return ;
            }
            File into = new File(fc.getSelectedFile().getAbsolutePath());                 
            Archivo archivo = invoice.getPDFArchivo();
            if(!archivo.download(dbserver,ftpServer,into))
            {
                JOptionPane.showMessageDialog(this,
                "Fall?? la descarga de archivo.",
                "Error externo",
                JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            archivo.getDownloadedFile().close();
        } 
        catch (SQLException | IOException ex) 
        {
            //Logger.getLogger(CreateUpdate.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
        }  
    }//GEN-LAST:event_btOrdenesDownActionPerformed

    private void btPDFDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btPDFDownActionPerformed
        try 
        {
            openDatabase(true);            
            FTP ftpServer = new FTP();
            boolean expResult = false;
            try 
            {
                expResult = ftpServer.connect(serverConfig);
            } 
            catch (IOException ex) 
            {
                //Logger.getLogger(FTPTest.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error externo",
                JOptionPane.ERROR_MESSAGE
                );
            }       
            final JFileChooser fc = new JFileChooser();   
            FileSystemView fw = fc.getFileSystemView();
            fc.setCurrentDirectory(fw.getDefaultDirectory());
            fc.setSelectedFile(new File(invoice.getPDFArchivo().getNombre()));
            int returnVal = fc.showSaveDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION)
            {

            }
            else
            {
                return ;
            }
            File into = new File(fc.getSelectedFile().getAbsolutePath());                 
            Archivo archivo = invoice.getPDFArchivo();
            if(!archivo.download(dbserver,ftpServer,into))
            {
                JOptionPane.showMessageDialog(this,
                "Fall?? la descarga de archivo.",
                "Error externo",
                JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            archivo.getDownloadedFile().close();
        } 
        catch (SQLException | IOException ex) 
        {
            //Logger.getLogger(CreateUpdate.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
        } 
    }//GEN-LAST:event_btPDFDownActionPerformed

    private void btXMLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btXMLActionPerformed
        try 
        {
            openDatabase(true);            
            FTP ftpServer = new FTP();
            boolean expResult = false;
            try 
            {
                expResult = ftpServer.connect(serverConfig);
            } 
            catch (IOException ex) 
            {
                //Logger.getLogger(FTPTest.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error externo",
                JOptionPane.ERROR_MESSAGE
                );
            }            
            File into = new File(System.getProperty("java.io.tmpdir"));                 
            Archivo archivo = invoice.getXMLArchivo();
            if(!archivo.download(dbserver,ftpServer,into))
            {
                JOptionPane.showMessageDialog(this,
                "Fall?? la descarga de archivo.",
                "Error externo",
                JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            archivo.getDownloadedFile().close();
            Desktop.getDesktop().open(new File(archivo.getDownloadFileName()));
        } 
        catch (SQLException | IOException ex) 
        {
            //Logger.getLogger(CreateUpdate.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
        }  
    }//GEN-LAST:event_btXMLActionPerformed

    private void btXMLDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btXMLDownActionPerformed
        try 
        {
            openDatabase(true);            
            FTP ftpServer = new FTP();
            boolean expResult = false;
            try 
            {
                expResult = ftpServer.connect(serverConfig);
            } 
            catch (IOException ex) 
            {
                //Logger.getLogger(FTPTest.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error externo",
                JOptionPane.ERROR_MESSAGE
                );
            }       
            final JFileChooser fc = new JFileChooser();   
            FileSystemView fw = fc.getFileSystemView();
            fc.setCurrentDirectory(fw.getDefaultDirectory());
            fc.setSelectedFile(new File(invoice.getXMLArchivo().getNombre()));
            int returnVal = fc.showSaveDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION)
            {

            }
            else
            {
                return ;
            }
            File into = new File(fc.getSelectedFile().getAbsolutePath());                 
            Archivo archivo = invoice.getXMLArchivo();
            if(!archivo.download(dbserver,ftpServer,into))
            {
                JOptionPane.showMessageDialog(this,
                "Fall?? la descarga de archivo.",
                "Error externo",
                JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            archivo.getDownloadedFile().close();
        } 
        catch (SQLException | IOException ex) 
        {
            //Logger.getLogger(CreateUpdate.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
        } 
    }//GEN-LAST:event_btXMLDownActionPerformed

    private void cbSerieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSerieActionPerformed
        if(cbSerie.getSelectedIndex() > 0)
        {
            openDatabase(true);
            serie = (String) cbSerie.getSelectedItem();
            office = Office.fromSerie(dbserver,serie);
            txFolio.setEnabled(true);
        }
        else
        {
            txFolio.setEnabled(false);
        }
    }//GEN-LAST:event_cbSerieActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btOrdenes;
    private javax.swing.JButton btOrdenesDown;
    private javax.swing.JButton btPDF;
    private javax.swing.JButton btPDFDown;
    private javax.swing.JButton btXML;
    private javax.swing.JButton btXMLDown;
    private javax.swing.JComboBox cbSerie;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField txFolio;
    // End of variables declaration//GEN-END:variables
}
