
package core.bobeda;

import SIIL.Server.Database;
import SIIL.client.sales.Enterprise;
import SIIL.service.quotation.ServiceQuotation;
import SIIL.services.grua.Resumov;
import core.Dialog;
import core.DialogContent;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.ParserConfigurationException;
import org.jdesktop.swingx.JXDialog;
import org.xml.sax.SAXException;

/**
 *
 * @author Azael Reyes
 */
public class CRUD extends javax.swing.JPanel implements DialogContent
{    
    private Database dbserver;
    private JXDialog dialog;    
    private InVault inVault;
    private Enterprise enterprise;
    private core.Mode mode;
    private ServiceQuotation serviceQuotation;
    private Table table;
    private Resumov resumovs;
    private SIIL.core.config.Server serverConfig;
    
    /**
     * @return the businesDocument
     */
    public Business getBusinesDocument() {
        
        if(inVault == null)return null;
        return inVault.businessDocument;
    }
        
    private void closeDatabase() 
    {
        if(dbserver != null)
        {
            dbserver.close();
            dbserver = null;
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
    
    public CRUD(core.Mode mode) 
    {
        initComponents();   
        searchClient.setEnabled(true);         
        searchBobeda.setEnabled(true);
        searchTable.setEnabled(true);   
        this.mode = mode;
        initComponents2();
        openDatabase(true);
        CRUDTableModel model = (CRUDTableModel) tbList.getModel();
        try 
        {
            model.searchTriple(dbserver, mode, table, null, null, null);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(CRUD.class.getName()).log(Level.SEVERE, null, ex);            
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
        }
        closeDatabase();
    }
    
    public CRUD(core.Mode mode,Table table) 
    {
        initComponents();   
        searchClient.setEnabled(true);         
        searchBobeda.setEnabled(true);
        searchTable.setEnabled(true);   
        this.mode = mode;
        this.table = table;
        initComponents2();
        openDatabase(true);
        CRUDTableModel model = (CRUDTableModel) tbList.getModel();
        try 
        {
            model.searchTriple(dbserver, mode, table, null, null, null);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(CRUD.class.getName()).log(Level.SEVERE, null, ex);            
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
        }
        closeDatabase();
    }

    private void initComponents2() 
    {
        tbList.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tbList.getColumnModel().getColumn(0).setPreferredWidth(80);
        tbList.getColumnModel().getColumn(1).setPreferredWidth(20); 
        tbList.getColumnModel().getColumn(2).setPreferredWidth(15);
        tbList.getColumnModel().getColumn(3).setPreferredWidth(75);        
    }
        
    public CRUD(core.Mode mode,Resumov resumovs) 
    {
        initComponents();          
        searchClient.setEnabled(false);
        searchClient.setText(resumovs.getCompany().getNumber());
        searchBobeda.setEnabled(true);
        searchTable.setEnabled(false); 
        this.mode = mode;
        this.table = Table.RESUMOV;
        this.resumovs = resumovs;
        openDatabase(true);
        CRUDTableModel model = (CRUDTableModel) tbList.getModel();
        try 
        {
            model.searchTriple(dbserver,mode, table, null, null,resumovs.getCompany().getNumber());
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(CRUD.class.getName()).log(Level.SEVERE, null, ex);            
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
        }
        closeDatabase();
        initComponents2();        
    }
            
    /**
     * Creates new form CRUD
     * @param mode
     * @param serviceQuotation
     */
    public CRUD(core.Mode mode,ServiceQuotation serviceQuotation) 
    {
        initComponents();          
        searchClient.setEnabled(false);
        searchClient.setText(serviceQuotation.getEntreprise().getNumber());
        searchBobeda.setEnabled(true);
        searchTable.setEnabled(false); 
        this.mode = mode;
        this.table = Table.SERVICEQUOTATION;
        this.serviceQuotation = serviceQuotation;
        openDatabase(true);
        CRUDTableModel model = (CRUDTableModel) tbList.getModel();
        try 
        {
            model.searchTriple(dbserver, mode, table, null, null,serviceQuotation.getEntreprise().getNumber());
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(CRUD.class.getName()).log(Level.SEVERE, null, ex);            
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
        }
        closeDatabase();
        initComponents2();        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mnMain = new javax.swing.JPopupMenu();
        mnPOView = new javax.swing.JMenuItem();
        mnPODown = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbList = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btReload = new javax.swing.JButton();
        searchTable = new org.jdesktop.swingx.JXSearchField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        searchClient = new org.jdesktop.swingx.JXSearchField();
        searchBobeda = new org.jdesktop.swingx.JXSearchField();

        mnPOView.setText("Ver PO");
        mnPOView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnPOViewActionPerformed(evt);
            }
        });
        mnMain.add(mnPOView);

        mnPODown.setText("Descargar PO");
        mnPODown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnPODownActionPerformed(evt);
            }
        });
        mnMain.add(mnPODown);

        tbList.setModel(new CRUDTableModel());
        tbList.setComponentPopupMenu(mnMain);
        tbList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbListMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tbListMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tbList);

        jLabel1.setText("Empresa");

        btReload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/reload.png"))); // NOI18N
        btReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btReloadActionPerformed(evt);
            }
        });

        searchTable.setPrompt("Buscar");
        searchTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTableActionPerformed(evt);
            }
        });

        jLabel3.setText("Folio en Tabla");

        jLabel4.setText("Folio en Bobeda");

        searchClient.setPrompt("Buscar");
        searchClient.setEnabled(false);
        searchClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchClientActionPerformed(evt);
            }
        });

        searchBobeda.setPrompt("Buscar");
        searchBobeda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBobedaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(searchClient, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(searchBobeda, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(searchTable, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(138, 138, 138)
                .addComponent(btReload))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 777, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(searchClient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btReload)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(searchTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(searchBobeda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tbListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbListMousePressed
        if(evt.getClickCount() == 2)
        {
            if(tbList.getSelectedRow() > -1)
            {
                CRUDTableModel model = (CRUDTableModel) tbList.getModel();
                InVault val = (InVault) model.getValueAt(tbList.getSelectedRow());                
                inVault = val;
                if(dialog != null && mode == core.Mode.SELECTION) 
                {
                    dialog.dispose();
                }                
                if(mode == core.Mode.VIEW)
                {
                    Business business = val.businessDocument;
                    core.bobeda.Upload screen = new core.bobeda.Upload(core.Mode.READ,business);
                    core.Dialog dialog = new core.Dialog(screen);
                    dialog.setContent(screen);
                }
            }
        }
    }//GEN-LAST:event_tbListMousePressed

    private void btReloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btReloadActionPerformed
        reload();
    }//GEN-LAST:event_btReloadActionPerformed

    private void searchBobedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBobedaActionPerformed
        searchTriple();
    }//GEN-LAST:event_searchBobedaActionPerformed

    private void searchTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTableActionPerformed
        searchTriple();
    }//GEN-LAST:event_searchTableActionPerformed

    private void searchClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchClientActionPerformed
        searchTriple();
    }//GEN-LAST:event_searchClientActionPerformed

    private void tbListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbListMouseClicked
        if(evt.getClickCount() == 2)
        {
            if(tbList.getSelectedRow() < 0)
            {
                JOptionPane.showMessageDialog(this,
                "Selecione un registro",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
                );
            }
            
           
        }
    }//GEN-LAST:event_tbListMouseClicked

    private void mnPOViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnPOViewActionPerformed
        InVault val = null;        
        if(tbList.getSelectedRow() > -1)
        {
            CRUDTableModel model = (CRUDTableModel) tbList.getModel();
            val = (InVault) model.getValueAt(tbList.getSelectedRow());            
        }
        else
        {
            JOptionPane.showMessageDialog(this,
                "Selecione un registro",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
                );
            return;
        }
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
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            File into = new File(System.getProperty("java.io.tmpdir"));
            Business archivo = val.businessDocument;
            if(!archivo.getBobeda().download(dbserver,ftpServer,into))
            {
                JOptionPane.showMessageDialog(this,
                    "Falló la descarga de archivo.",
                    "Error externo",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            archivo.getBobeda().getDownloadedFile().close();
            Desktop.getDesktop().open(new File(archivo.getBobeda().getDownloadFileName()));
        }
        catch (SQLException | IOException ex)
        {
            //Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error externo",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_mnPOViewActionPerformed

    private void mnPODownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnPODownActionPerformed
        InVault val = null;        
        if(tbList.getSelectedRow() > -1)
        {
            CRUDTableModel model = (CRUDTableModel) tbList.getModel();
            val = (InVault) model.getValueAt(tbList.getSelectedRow());            
        }
        else
        {
            JOptionPane.showMessageDialog(this,
                "Selecione un registro",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
                );
            return;
        }
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
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            
            File into = new File(System.getProperty("java.io.tmpdir"));
            Business archivo = val.businessDocument;            

            final JFileChooser fc = new JFileChooser();   
            FileSystemView fw = fc.getFileSystemView();
            fc.setCurrentDirectory(fw.getDefaultDirectory());
            fc.setSelectedFile(new File(archivo.getBobeda().getNombre()));
            int returnVal = fc.showSaveDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION)
            {

            }
            else
            {
                return ;
            }
            
            if(!archivo.getBobeda().download(dbserver,ftpServer,into))
            {
                JOptionPane.showMessageDialog(this,
                    "Falló la descarga de archivo.",
                    "Error externo",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            archivo.getBobeda().download(dbserver, ftpServer, new File(fc.getSelectedFile().getAbsolutePath()));
            OutputStream file = archivo.getBobeda().getDownloadedFile();
            file.close();
        }
        catch (SQLException | IOException ex)
        {
            //Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error externo",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_mnPODownActionPerformed

    private void searchTriple() 
    {
        CRUDTableModel model = (CRUDTableModel) tbList.getModel();
        openDatabase(true);
        try
        {
            String searchT = null, searchB = null, searchC = null;
            if(searchTable.getText().length() > 0) searchT = searchTable.getText();
            if(searchBobeda.getText().length() > 0) searchB = searchBobeda.getText();
            if(searchClient.getText().length() > 0 )searchC = searchClient.getText();
            model.searchTriple(dbserver,mode, table, searchT , searchB, searchC);
        }
        catch (SQLException ex)
        {
            //Logger.getLogger(CRUD.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
        }
        closeDatabase();
    }

    public void reload() 
    {
        openDatabase(true);
        CRUDTableModel model = (CRUDTableModel) tbList.getModel();
        try
        {
            model.reload(dbserver);
        }
        catch (SQLException ex)
        {
            //Logger.getLogger(CRUD.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            ); 
        }
        closeDatabase();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btReload;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu mnMain;
    private javax.swing.JMenuItem mnPODown;
    private javax.swing.JMenuItem mnPOView;
    private org.jdesktop.swingx.JXSearchField searchBobeda;
    private org.jdesktop.swingx.JXSearchField searchClient;
    private org.jdesktop.swingx.JXSearchField searchTable;
    private javax.swing.JTable tbList;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setDialog(Dialog dialog) 
    {
        this.dialog = dialog;
    }
}
