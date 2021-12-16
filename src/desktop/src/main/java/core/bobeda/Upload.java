
package core.bobeda;

import SIIL.Server.Database;
import SIIL.client.sales.Enterprise;
import SIIL.core.config.Server;
import SIIL.service.quotation.ServiceQuotation;
import SIIL.services.grua.Resumov;
import core.Dialog;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.ParserConfigurationException;
import org.jdesktop.swingx.JXDialog;
import org.xml.sax.SAXException;
import process.Moneda;
import process.Return;
import process.TipoCambio;

/**
 *
 * @author Azael Reyes
 */
public class Upload extends javax.swing.JPanel implements core.DialogContent
{
    private Enterprise comp;
    private Database dbserver;
    private FTP ftpServer;
    private FileInputStream in;
    private String nameFile;
    private JXDialog dialog;
    private core.bobeda.Business businesDocument;
    private CRUD crud;
    private ServiceQuotation serviceQuotation;
    private Resumov resumov;
    
    public Upload(Resumov resumov, CRUD crud) 
    {
        initComponents();
        initComponents2();
        
        txClient.setEditable(false);
        btClient.setEnabled(false);
        txClient.setText(resumov.getCompany().getNumber());
        lbClient.setText(resumov.getCompany().getName());
        comp = resumov.getCompany();
        this.resumov = resumov;
        this.crud = crud;
    }
    
    private void closeFTP() 
    {
        if(ftpServer != null)
        {
            ftpServer.close();
            ftpServer = null;
            
        }
    }    
    
    private void openFTP(boolean  reclicleConextion)
    {
        try 
        {
            SIIL.core.config.Server serverConfig = new SIIL.core.config.Server(); 
            Database dbserver = null;
            try 
            {
                serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
                dbserver = new Database(serverConfig);
            }
            catch (ClassNotFoundException | SQLException | IOException | ParserConfigurationException | SAXException ex) 
            {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                //fail(ex.getMessage());
            }
            
            if(reclicleConextion)
            {
                if(ftpServer != null)
                {
                    if(ftpServer.isConnected())
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
                if(ftpServer != null)
                {
                    if(ftpServer.isConnected())dbserver.close();
                    ftpServer = null;                    
                }
            }
            ftpServer = null;
            ftpServer = new FTP();
            ftpServer.connect(serverConfig);
        } 
        catch (IOException ex) 
        {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
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
        
    public Upload(ServiceQuotation serviceQuotation, CRUD crud) 
    {
        initComponents();
        initComponents2();
        
        txClient.setEditable(false);
        btClient.setEnabled(false);
        txClient.setText(serviceQuotation.getEntreprise().getNumber());
        lbClient.setText(serviceQuotation.getEntreprise().getName());
        comp = serviceQuotation.getEntreprise();
        this.serviceQuotation = serviceQuotation;
        this.crud = crud;
    }
    
    /**
     * Creates new form Upload
     * @param enterprise
     * @param crud
     */
    public Upload(Enterprise enterprise, CRUD crud) 
    {
        initComponents();
        initComponents2();
        
        txClient.setEditable(false);
        btClient.setEnabled(false);
        txClient.setText(enterprise.getNumber());
        lbClient.setText(enterprise.getName());
        comp = enterprise;
        
        this.crud = crud;
    }
    
    /**
     * Creates new form Upload
     * @param mode
     * @param business
     */
    public Upload(core.Mode mode, Business business) 
    {
        initComponents();
        initComponents2();       
        
        editableControls(false);
        updateControls(business);
    }

    private void editableControls(boolean  edittable) 
    {
        txClient.setEditable(edittable);
        txFolio.setEditable(edittable);
        txMount.setEditable(edittable); 
        txTC.setEditable(edittable);
        txaBrief.setEditable(edittable);
    }

    private void updateControls(Business business) 
    {
        comp = business.getEnterprise();
        lbClient.setText(business.getEnterprise().getName());
        txClient.setText(business.getEnterprise().getNumber());
        lbFile.setText(business.getBobeda().getNombre());
        txFolio.setText(business.getFolio());
        txMount.setText(String.valueOf(business.getMonto()));
        if(business.getMonedaForeign() != Moneda.MXN)
        {
            if(business.getMonedaForeign() == Moneda.USD)
            {
                opUSD.setSelected(true);
            }
            txTC.setText(String.valueOf(business.getMonedaForeignValor()));
        }
        txaBrief.setText(business.getBobeda().getBrief());
    }

    private void initComponents2() 
    {
        grMonedas.add(opMXN);
        grMonedas.add(opUSD);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        gopMonedas = new javax.swing.JRadioButton();
        grMonedas = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        btSelect = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txFolio = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        lbClient = new javax.swing.JLabel();
        btClient = new javax.swing.JButton();
        lbTagClient = new javax.swing.JLabel();
        txClient = new javax.swing.JTextField();
        btUpload = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txaBrief = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        txMount = new javax.swing.JTextField();
        lbFile = new javax.swing.JLabel();
        opMXN = new javax.swing.JRadioButton();
        opUSD = new javax.swing.JRadioButton();
        txTC = new javax.swing.JTextField();
        lbTC = new javax.swing.JLabel();

        gopMonedas.setText("jRadioButton2");

        jLabel1.setText("Archivo");

        btSelect.setText("...");
        btSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSelectActionPerformed(evt);
            }
        });

        jLabel2.setText("Folio");

        jLabel3.setText("Descripción");

        lbClient.setText("##");

        btClient.setText("...");
        btClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btClientActionPerformed(evt);
            }
        });

        lbTagClient.setText("Cliente");

        txClient.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txClientKeyReleased(evt);
            }
        });

        btUpload.setText("Subir");
        btUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUploadActionPerformed(evt);
            }
        });

        txaBrief.setColumns(20);
        txaBrief.setRows(5);
        jScrollPane1.setViewportView(txaBrief);

        jLabel4.setText("Monto");

        lbFile.setText("#");

        opMXN.setText("MXN");
        opMXN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opMXNActionPerformed(evt);
            }
        });

        opUSD.setText("USD");
        opUSD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opUSDActionPerformed(evt);
            }
        });

        lbTC.setText("Tipo de Cambio");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txFolio, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                                        .addComponent(jLabel4)
                                        .addGap(90, 90, 90))
                                    .addComponent(jLabel1)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel3)
                                                .addGap(154, 154, 154)
                                                .addComponent(lbTC))
                                            .addComponent(jLabel2))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txMount)
                                            .addComponent(txTC, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE))))
                                .addComponent(opUSD)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(opMXN))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbTagClient, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txClient, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btClient, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lbClient, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btSelect)
                                .addGap(18, 18, 18)
                                .addComponent(lbFile, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 28, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(220, 220, 220)
                .addComponent(btUpload)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btClient)
                        .addComponent(lbClient))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txClient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbTagClient)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btSelect)
                            .addComponent(lbFile))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txFolio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txMount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(opMXN)
                            .addComponent(opUSD)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txTC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbTC)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btUpload)
                .addContainerGap(31, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btClientActionPerformed
        comp = new Enterprise();
        JFrame frm = new JFrame();
        JDialog dlg = new JDialog(frm,"Seleccionar cliente",true);
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
        
        SIIL.Clientes.ReadSelect read = new SIIL.Clientes.ReadSelect(comp,SIIL.servApp.cred.getBD());
        dlg.setContentPane(read);
        dlg.setSize(460, 260);
        dlg.setVisible(true);
        txClient.setText(comp.getNumber());
        lbClient.setText(comp.getName());
        comp.complete(dbserver);
        dbserver.close();
    }//GEN-LAST:event_btClientActionPerformed

    private void txClientKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txClientKeyReleased
        if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER)
        {
            comp = new Enterprise();
            comp.setBD(SIIL.servApp.cred.getBD());
            comp.setNumber(txClient.getText());
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
            if(comp.complete(dbserver))
            {
                lbClient.setText(comp.getName());
            }
            else
            {
                if(comp == null)
                {
                    JOptionPane.showMessageDialog(this, "Numero de cliente desconocido",
                        "Error externo", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    comp = null;
                    lbClient.setText("###");
                }
            }
            dbserver.close();
        }
    }//GEN-LAST:event_txClientKeyReleased

    private void btSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSelectActionPerformed
        final JFileChooser fc = new JFileChooser();
        //Visualiza el cuadro de guardar c para indicar el archivo.
        //String nameFile;
        //CSVFileFilter fcFilter = new CSVFileFilter();
        //fc.setFileFilter(fcFilter);
        FileSystemView fw = fc.getFileSystemView();
        fc.setCurrentDirectory(fw.getDefaultDirectory());
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
            
            in = new FileInputStream(fc.getSelectedFile());
            nameFile = fc.getSelectedFile().getName();
            lbFile.setText(fc.getSelectedFile().getAbsolutePath());
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                "Falló la creacion del Archivo",
                "Error externo",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        catch (IOException ex)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                "Falló la creacion del Archivo",
                "Error externo",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
    }//GEN-LAST:event_btSelectActionPerformed

    private void btUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUploadActionPerformed
        if(txFolio.getText().length() == 0)
        {
            JOptionPane.showMessageDialog(this,
                "El Folio es obligatorio",
                "Error externo",
                JOptionPane.ERROR_MESSAGE
            );
            return;            
        }
        if(in == null)
        {
            JOptionPane.showMessageDialog(this,
                "El archivo  es obligatorio",
                "Error externo",
                JOptionPane.ERROR_MESSAGE
            );
            return;             
        }
        if(comp == null)
        {
            JOptionPane.showMessageDialog(this,
                "El cliente  es obligatorio",
                "Error externo",
                JOptionPane.ERROR_MESSAGE
            );
            return; 
        }
        
        
        openDatabase(true);
        openFTP(true);        
        Return ret = null;                
        try 
        {
            if(!ftpServer.isExist(Vault.Type.PO,Vault.Origen.CLIENTE,comp.getNumber()))
            {
                if(!ftpServer.addSubdirectory(Vault.Type.PO,Vault.Origen.CLIENTE, comp.getNumber()))
                {
                    JOptionPane.showMessageDialog(this,
                    "Fallo al crear el subdirecotio de cliente",
                    "Error externo",
                    JOptionPane.ERROR_MESSAGE
                    );                    
                }
            }
            if(opUSD.isSelected() && txTC.getText().isEmpty())
            {
                JOptionPane.showMessageDialog(this,
                    "Indique tipo de cambio por favor",
                    "Error externo",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            TipoCambio tipocambio = null;
            double tc = -1.0;
            if(txMount.getText().length() > 0)
            {
                if(opMXN.isSelected())
                {
                    tipocambio = new TipoCambio(Moneda.MXN,Moneda.MXN,1.0);
                }
                else if(opUSD.isSelected())
                {
                    tc = Double.parseDouble(txTC.getText());
                    tipocambio = new TipoCambio(Moneda.MXN,Moneda.USD,tc);
                }
            }
            
            String strBrief = txaBrief.getText();
            if(txaBrief.getText().length() > 0)
            {
                strBrief = txaBrief.getText();
            }
            else
            {
                strBrief = null;
            }
            
            
            ret = Business.add(dbserver, ftpServer, nameFile, in, comp, tc, strBrief,Vault.Type.PO, Vault.Origen.CLIENTE,txFolio.getText(),tipocambio);            
            businesDocument = (Business) ret.getParam();
            if(serviceQuotation != null) 
            {
                serviceQuotation.upPOFile(dbserver, businesDocument);
            }
            else if(resumov != null)
            {
                resumov.upPOFile(dbserver, businesDocument);
            }
            else
            {
                JOptionPane.showMessageDialog(
                    this,
                    "No se ha referido una tabla para subir el documento",
                    "Error externo",
                    JOptionPane.ERROR_MESSAGE
                ); 
            }
            
            if(ret.isFlag())
            {
                dbserver.commit();
                JOptionPane.showMessageDialog(this,
                    "Operacion completada satisfactoriamente",
                    "Confirmación",
                    JOptionPane.INFORMATION_MESSAGE
                );                
                if(crud != null) crud.reload();
                if(dialog != null) dialog.dispose();
            }
            else
            {
                dbserver.rollback();
                JOptionPane.showMessageDialog(
                    this,
                    "Error desconocido en la etapa fianlde la operacion guardar: " + ret.getMessage(),
                    "Error externo",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } 
        catch (IOException | SQLException ex) 
        {
            //Logger.getLogger(Upload.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error externo",
                JOptionPane.ERROR_MESSAGE
            );
        }        
    }//GEN-LAST:event_btUploadActionPerformed

    private void opUSDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opUSDActionPerformed
        txTC.setEditable(true);
    }//GEN-LAST:event_opUSDActionPerformed

    private void opMXNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opMXNActionPerformed
         txTC.setEditable(false);
    }//GEN-LAST:event_opMXNActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btClient;
    private javax.swing.JButton btSelect;
    private javax.swing.JButton btUpload;
    private javax.swing.JRadioButton gopMonedas;
    private javax.swing.ButtonGroup grMonedas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbClient;
    private javax.swing.JLabel lbFile;
    private javax.swing.JLabel lbTC;
    private javax.swing.JLabel lbTagClient;
    private javax.swing.JRadioButton opMXN;
    private javax.swing.JRadioButton opUSD;
    private javax.swing.JTextField txClient;
    private javax.swing.JTextField txFolio;
    private javax.swing.JTextField txMount;
    private javax.swing.JTextField txTC;
    private javax.swing.JTextArea txaBrief;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setDialog(Dialog dialog) 
    {
        this.dialog = dialog;
    }

    /**
     * @return the businesDocument
     */
    public core.bobeda.Business getBusinesDocument() {
        return businesDocument;
    }
}
