
package SIIL.Clientes;

import SIIL.Server.Database;
import SIIL.client.sales.Enterprise;
import SIIL.servApp;
import core.Dialog;
import core.DialogContent;
import java.awt.Window;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Azael
 */
public class ReadSelect2 extends javax.swing.JPanel implements DialogContent {

    private SIIL.Server.Company empresa;
    private Enterprise selected;
    private String BD;
    private Filter filter;
    private Database dbserver;  
    private core.Dialog dialog;
    JInternalFrame inter;
    
    public void reload()
    {
        ReadTableModel model = (ReadTableModel) tbClient.getModel();
        openDatabase(true);
        try 
        {
            model.search(dbserver, txSearch.getText(), 7,filter);
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(ReadSelect2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void seleccion(int sel)
    {
        ReadTableModel model = (ReadTableModel) tbClient.getModel();
        if(filter==Filter.SAERCH)
        {
            empresa.copy(model.getValueAt(sel));
            //((Window)SwingUtilities.getWindowAncestor(this)).dispose();
        }
        else if(filter==Filter.RFC)
        {
            selected = model.getValueAt(sel);
            if(dialog != null) dialog.dispose();
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
    
    public ReadSelect2(SIIL.Server.Company e) 
    {
        initComponents();
        iniComponents2(); 
        if(e == null)empresa = new SIIL.Server.Company();
        else empresa = e;
        txSearch.requestFocus();        
        tbClient.getColumnModel().getColumn(0).setPreferredWidth(20);
        tbClient.getColumnModel().getColumn(1).setPreferredWidth(20);
        BD = SIIL.servApp.cred.getBD();
        filter = Filter.SAERCH;
    }
        
    public ReadSelect2(SIIL.Server.Company e,String bd) 
    {
        initComponents();
        iniComponents2(); 
        empresa = e;
        txSearch.requestFocus();        
        tbClient.getColumnModel().getColumn(0).setPreferredWidth(20);
        tbClient.getColumnModel().getColumn(1).setPreferredWidth(20);
        BD = bd;
        filter = Filter.SAERCH;
    }
    
    /**
     * Creates new form ReadSelect
     * @param field
     * @param rfc
     */
    public ReadSelect2(Filter field, String rfc) 
    {
        initComponents();
        iniComponents2();        
        txSearch.requestFocus();        
        tbClient.getColumnModel().getColumn(0).setPreferredWidth(20);
        tbClient.getColumnModel().getColumn(1).setPreferredWidth(20);
        BD = "bc.tj";
        filter = Filter.RFC;
        txSearch.setText(rfc);
        txSearch.setEnabled(false);
            try 
            {
                openDatabase(true);
                ReadTableModel model = (ReadTableModel) tbClient.getModel();
                if(filter == Filter.SAERCH)
                {
                    model.search(dbserver, txSearch.getText(), 7,filter);
                }
                else if(filter == Filter.RFC)
                {
                    model.search(dbserver, txSearch.getText(), 0,filter);
                }                
                closeDatabase();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(ReadSelect.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    private void iniComponents2() 
    {
        tbClient.setModel(new ReadTableModel());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tbClient = new javax.swing.JTable();
        btAdd = new javax.swing.JButton();
        txSearch = new javax.swing.JTextField();
        btUpdate = new javax.swing.JButton();
        btnImport = new javax.swing.JButton();

        tbClient.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Numero", "Nombre", "RFC"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbClient.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tbClientMousePressed(evt);
            }
        });
        tbClient.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbClientKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tbClient);

        btAdd.setText("Agregar");
        btAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddActionPerformed(evt);
            }
        });

        txSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txSearchKeyReleased(evt);
            }
        });

        btUpdate.setText("Modificar");
        btUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUpdateActionPerformed(evt);
            }
        });

        btnImport.setText("Importar");
        btnImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(txSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btUpdate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btAdd)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnImport)
                .addGap(5, 5, 5))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 535, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btAdd)
                    .addComponent(btUpdate)
                    .addComponent(btnImport))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tbClientMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbClientMousePressed
        if(evt.getClickCount() == 2 && tbClient.getSelectedRow() > -1)
        {
            seleccion(tbClient.getSelectedRow());
        }
    }//GEN-LAST:event_tbClientMousePressed

    private void tbClientKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbClientKeyPressed
        if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER && tbClient.getRowCount() == 1)
        {
            seleccion(0);
        }
        else if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER && tbClient.getSelectedRow() > -1)
        {
            seleccion(tbClient.getSelectedRow());
        }
    }//GEN-LAST:event_tbClientKeyPressed

    private void btAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddActionPerformed
        CU cu = new CU();
        JInternalFrame inter = new JInternalFrame("Agregar Cliente",true,true);
        inter.setContentPane(cu);
        inter.setSize(cu.getPreferredSize());
        inter.setMaximizable(true);
        servApp.getInstance().getDesktopPane().add(inter);
        int x = servApp.getInstance().getDesktopPane().getSize().width/2 - cu.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
        cu.setFrame(inter);
    }//GEN-LAST:event_btAddActionPerformed

    private void txSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txSearchKeyReleased
        if(evt.getKeyCode() != java.awt.event.KeyEvent.VK_ENTER)
        {
            try
            {
                openDatabase(true);
                ReadTableModel model = (ReadTableModel) tbClient.getModel();
                if(filter == Filter.SAERCH)
                {
                    model.search(dbserver, txSearch.getText(), 7,filter);
                }
                else if(filter == Filter.RFC)
                {
                    model.search(dbserver, txSearch.getText(), 0,filter);
                }
                closeDatabase();
            }
            catch (SQLException ex)
            {
                Logger.getLogger(ReadSelect.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER && tbClient.getRowCount() >= 0)
        {
            seleccion(0);
        }
    }//GEN-LAST:event_txSearchKeyReleased

    private void btUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUpdateActionPerformed
        if(tbClient.getSelectedRow() < 0)
        {
            JOptionPane.showMessageDialog(this,
                "Seleciono un registro",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        ReadTableModel model = (ReadTableModel) tbClient.getModel();
        selected = model.getValueAt(tbClient.getSelectedRow());
        CU cu = new CU(selected);
        JInternalFrame inter = new JInternalFrame("Actualizacion de Cliente",true,true);
        inter.setContentPane(cu);
        inter.setSize(cu.getPreferredSize());
        inter.setMaximizable(true);
        servApp.getInstance().getDesktopPane().add(inter);
        int x = servApp.getInstance().getDesktopPane().getSize().width/2 - cu.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
        cu.setFrame(inter);
        cu.setReadView(this);
    }//GEN-LAST:event_btUpdateActionPerformed

    private void btnImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportActionPerformed
        JInternalFrame inter = new JInternalFrame("Agregar Cliente",true,true);
        SIIL.COMPAQ.Clients client = new SIIL.COMPAQ.Clients(inter);
        inter.setContentPane(client);
        inter.setSize(client.getPreferredSize());
        inter.setMaximizable(true);
        servApp.getInstance().getDesktopPane().add(inter);
        int x = servApp.getInstance().getDesktopPane().getSize().width/2 - client.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
        //client.setFrame(inter);
    }//GEN-LAST:event_btnImportActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btUpdate;
    private javax.swing.JButton btnImport;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbClient;
    private javax.swing.JTextField txSearch;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setDialog(Dialog dialog) 
    {
        this.dialog = dialog;
    }
}
