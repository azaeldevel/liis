
package sales.invoice;

import SIIL.Server.Database;
import SIIL.core.Office;
import core.OfficeComboBoxModel;
import core.bobeda.Archivo;
import core.bobeda.FTP;
import database.mysql.sales.Remision;
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

/**
 *
 * @author Azael Reyes
 */
public class RemisionDetail extends javax.swing.JPanel {

    private Database dbserver;    
    private SIIL.core.config.Server serverConfig;
    private Office office;
    private Remision remision;
    
    
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
    public RemisionDetail() {
        initComponents();
        openDatabase(true);
        OfficeComboBoxModel offices = new OfficeComboBoxModel();
        try 
        {
            offices.fill(dbserver);
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        cbBD.setModel(offices);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        cbBD = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        txFolio = new javax.swing.JTextField();
        btOrdenes = new javax.swing.JButton();
        btOrdenesDown = new javax.swing.JButton();

        jLabel3.setText("Base de Datos");

        cbBD.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccione...", "Tijuana", "Mexicali", "Ensenada", " " }));
        cbBD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbBDActionPerformed(evt);
            }
        });

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

        btOrdenesDown.setText("Download  Ordenes");
        btOrdenesDown.setEnabled(false);
        btOrdenesDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btOrdenesDownActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbBD, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txFolio, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btOrdenes)
                    .addComponent(btOrdenesDown))
                .addGap(49, 49, 49))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cbBD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(txFolio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(56, 56, 56)
                .addComponent(btOrdenes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btOrdenesDown)
                .addContainerGap(65, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cbBDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbBDActionPerformed
        if(cbBD.getSelectedIndex() > 0)
        {
            if(cbBD.getSelectedIndex() > 0)
            {
                office = (Office) cbBD.getSelectedItem();
                txFolio.setEnabled(true);
            }
            else
            {     
            }
        }
        else
        {
            
        }
    }//GEN-LAST:event_cbBDActionPerformed

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
            Archivo archivo = remision.getArchivoOS();
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
                Return ret = Remision.exist(dbserver, office, txFolio.getText(), Operational.Type.SalesRemision);
                if(ret.isFlag())
                {
                    remision = new Remision((int)ret.getParam());
                    remision.download(dbserver);
                    if(remision.downOrdenesArchivo(dbserver))
                    {
                        btOrdenes.setEnabled(true);
                        btOrdenesDown.setEnabled(true);
                        
                    }
                    else
                    {
                        btOrdenes.setEnabled(false);
                        btOrdenesDown.setEnabled(false);
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
            fc.setSelectedFile(new File(remision.getArchivoOS().getNombre()));
            int returnVal = fc.showSaveDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION)
            {

            }
            else
            {
                return ;
            }
            File into = new File(fc.getSelectedFile().getAbsolutePath());                 
            Archivo archivo = remision.getArchivoOS();
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btOrdenes;
    private javax.swing.JButton btOrdenesDown;
    private javax.swing.JComboBox cbBD;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField txFolio;
    // End of variables declaration//GEN-END:variables
}
