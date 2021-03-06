
package SIIL.services.order;

import SIIL.Server.Database;
import SIIL.core.Office;
import core.bobeda.FTP;
import database.mysql.sales.Remision;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FilenameUtils;
import org.xml.sax.SAXException;
import process.Operational;
import process.Return;
import sales.Invoice;
import session.User;

/**
 *
 * @author Azael Reyes
 */
public class AssociateFacturaOrden extends javax.swing.JPanel 
{

    private FileInputStream in;
    private String nameFile;
    private Office office;
    private Database dbserver;
    private Invoice invoice;
    private User operator;
    private SIIL.core.config.Server serverConfig;
    private File file;
    
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
     * Creates new form AssociateFacturaOrden
     */
    public AssociateFacturaOrden() 
    {
        initComponents();
        office = SIIL.servApp.cred.getOffice();
        operator = SIIL.servApp.cred.getUser();
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
        btSelect = new javax.swing.JButton();
        lbFile = new javax.swing.JLabel();
        btSave = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txSA = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cbBD = new javax.swing.JComboBox();

        jLabel1.setText("Archivo");

        btSelect.setText("...");
        btSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSelectActionPerformed(evt);
            }
        });

        lbFile.setText("##");

        btSave.setText("Guardar");
        btSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSaveActionPerformed(evt);
            }
        });

        jLabel2.setText("SA");

        txSA.setEnabled(false);

        jLabel3.setText("Base de Datos");

        cbBD.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccione...", "Tijuana", "Mexicali", "Ensenada", " " }));
        cbBD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbBDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(477, Short.MAX_VALUE)
                .addComponent(btSave)
                .addGap(31, 31, 31))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btSelect)
                                .addGap(18, 18, 18)
                                .addComponent(lbFile, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txSA, javax.swing.GroupLayout.PREFERRED_SIZE, 498, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbBD, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cbBD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btSelect)
                    .addComponent(lbFile))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(txSA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addComponent(btSave)
                .addGap(33, 33, 33))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSelectActionPerformed
        final JFileChooser fc = new JFileChooser();
        //Visualiza el cuadro de guardar c para indicar el archivo.
        //String nameFile;
        //CSVFileFilter fcFilter = new CSVFileFilter();
        //fc.setFileFilter(fcFilter);
        FileSystemView fw = fc.getFileSystemView();
        try 
        {
            File file;
            String host = InetAddress.getLocalHost().getHostName();
            if(host.equals("Recepcion"))
            {
                file = new File("C:\\Users\\Recepcion\\Desktop\\ORDENES");
                fc.setCurrentDirectory(file);
            }
            else
            {
                fc.setCurrentDirectory(fw.getDefaultDirectory());
            }
        } 
        catch (UnknownHostException ex) 
        {
            Logger.getLogger(AsociacionMasivaFacturaOrdenes.class.getName()).log(Level.SEVERE, null, ex);
        }
        int returnVal = fc.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {

        }
        else
        {
            return ;
        }
        //FileWriter file;
        try
        {

            file = fc.getSelectedFile();
            in = new FileInputStream(fc.getSelectedFile());
            nameFile = fc.getSelectedFile().getName();
            lbFile.setText(fc.getSelectedFile().getAbsolutePath());
            String folio = FilenameUtils.removeExtension(fc.getSelectedFile().getName());
            try 
            {
                openDatabase(true);
                Return ret = Invoice.exist(dbserver, office, folio, Operational.Type.SalesInvoice);
                if(ret.isFlag())
                {
                    invoice = new Invoice((int)ret.getParam());
                    invoice.download(dbserver);
                    invoice.downCompany(dbserver);
                    invoice.upFlag(dbserver.getConnection(), 'A');
                }
                else
                {
                    invoice = new Invoice(-1);
                    try 
                    {
                        ret = invoice.fromCN(dbserver, office, folio, operator);
                        invoice.download(dbserver);
                        invoice.downCompany(dbserver);
                        invoice.upFlag(dbserver.getConnection(), 'A');
                        if(ret.isFail())
                        {
                            JOptionPane.showMessageDialog(this,
                                ret.getMessage(),
                                "Error externo",
                                JOptionPane.ERROR_MESSAGE
                            );
                            return;
                        }
                    }
                    catch (IOException | ParseException ex) 
                    {
                        //Logger.getLogger(AssociateFacturaOrden.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(this,
                            ex.getMessage(),
                            "Error externo",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
                txSA.setEnabled(true);
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(AssociateFacturaOrden.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error externo",
                JOptionPane.ERROR_MESSAGE
                );
            }           
        }
        catch (FileNotFoundException ex)
        {
            //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                "Fall?? la creacion del Archivo",
                "Error externo",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_btSelectActionPerformed

    private void btSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSaveActionPerformed
        SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();        
        Database dbserver = null;
        try 
        {
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
            dbserver = new Database(serverConfig);
        } 
        catch (ClassNotFoundException | SQLException | IOException | ParserConfigurationException | SAXException ex) 
        {
            JOptionPane.showMessageDialog(this,
                "Fallo importacion.",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
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
        
        Return ret = null, retNew = null;
        try 
        {
            ret = Remision.generate(dbserver, office, txSA.getText().split(","), operator);
            if(ret.isFlag())
            { 
                if(ret.isFail())
                {
                    JOptionPane.showMessageDialog(this,
                        ret.getMessage(),
                        "Error externo",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            }
            else
            {
                JOptionPane.showMessageDialog(this,
                ret.getMessage(),
                "Error externo",
                JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        } 
        catch (SQLException | IOException | ParseException ex) 
        {
            //Logger.getLogger(AssociateFacturaOrden.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error externo",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }        
        
        
        if(ret.isFlag())
        {
            try 
            {
                in.close();
                Files.delete(file.toPath());
                dbserver.commit();
                JOptionPane.showMessageDialog(this,
                    "Operaci??n Completa",
                    "Confirmacion",
                    JOptionPane.INFORMATION_MESSAGE
                );
                clean();
                return;
            } 
            catch (IOException | SQLException ex) 
            {
                //Logger.getLogger(AssociateFacturaOrden.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error externo",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
        else
        {
            try 
            {
                dbserver.rollback();
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(AssociateFacturaOrden.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error externo",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }        
    }//GEN-LAST:event_btSaveActionPerformed

    private void cbBDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbBDActionPerformed
        if(cbBD.getSelectedIndex() > 0)
        {
            openDatabase(true);
            office = (Office) cbBD.getSelectedItem();
        }
        else
        {
        }
    }//GEN-LAST:event_cbBDActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btSave;
    private javax.swing.JButton btSelect;
    private javax.swing.JComboBox cbBD;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lbFile;
    private javax.swing.JTextField txSA;
    // End of variables declaration//GEN-END:variables

    private void clean() 
    {
        invoice = null;
        lbFile.setText("");
        txSA.setText("");
        in = null;
        txSA.setEnabled(false);        
    }
}
