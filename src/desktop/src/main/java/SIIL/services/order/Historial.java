
package SIIL.services.order;

import SIIL.Server.Database;
import core.Dialog;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Azael Reyes
 */
public class Historial extends javax.swing.JPanel implements core.DialogContent
{
    public enum Mode
    {
        READ,
        SELECT
    }    
    
    private Mode mode;
    private Order selected;
    private Dialog dialog;
    private Database dbserver;

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
    
    @Override
    public void setDialog(Dialog dialog) 
    {
        this.dialog = dialog;
    }
    /**
     * @return the selected
     */
    public Order getSelected() 
    {
        return selected;
    }
    
    public void reloadTable() 
    {
        openDatabase(true);
        OrderTableModel model = (OrderTableModel) tbList.getModel();
        try 
        {
            model.search(dbserver,OrderTableModel.Mode.INITIAL,null,(int)spCant.getValue());
            //tbList.setComponentPopupMenu(mnMain);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(Read.class.getName()).log(Level.SEVERE, null, ex);            
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        closeDatabase();
    }
    
    public void viewOrderDetail()
    {
        if (tbList.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un registro primero por favor.",
                    "Operacion incompleta",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        openDatabase(true);
        ReadTableModel model = (ReadTableModel) tbList.getModel();
        Resumen resumen = model.getValueAt(tbList.getSelectedRow());
        JInternalFrame inter = new JInternalFrame("Capturar orden de Servicio",false,true);
        CreateUpdate ord = new SIIL.services.order.CreateUpdate(inter,CreateUpdate.Mode.DETAIL,resumen,this,dbserver);
        inter.setContentPane(ord);
        inter.setSize(ord.getPreferredSize());
        SIIL.servApp.getInstance().getDesktopPane().add(inter);
        int x = SIIL.servApp.getInstance().getDesktopPane().getSize().width/2 - ord.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
        closeDatabase();
    }
    
    /**
     * Creates new form Historial
     * @param mode
     */
    public Historial(Mode mode) 
    {
        initComponents();
        initialLoad();
        this.mode = mode;
    }

    private boolean initialLoad()
    {
        openDatabase(true);
        OrderTableModel model = (OrderTableModel) tbList.getModel();
        try {
            model.search(dbserver, OrderTableModel.Mode.INITIAL,null,(int)spCant.getValue());
        } catch (SQLException ex) {          
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        closeDatabase();
        return true;
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
        mnNew = new javax.swing.JMenuItem();
        btUpdate = new org.jdesktop.swingx.JXButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbList = new org.jdesktop.swingx.JXTable();
        spCant = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        search = new org.jdesktop.swingx.JXSearchField();

        mnNew.setText("Nueva orden");
        mnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnNewActionPerformed(evt);
            }
        });
        mnMain.add(mnNew);

        btUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/reload.png"))); // NOI18N
        btUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUpdateActionPerformed(evt);
            }
        });

        tbList.setModel(new OrderTableModel());
        tbList.setComponentPopupMenu(mnMain);
        tbList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbList);

        spCant.setToolTipText("Determina la cantidad de registro visualizados, si es 0 se viualizan toods los que hay en la sase de datos. Tambien afecta ha los reportes y exportaciones");
        spCant.setValue(50);
        spCant.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                spCantKeyReleased(evt);
            }
        });

        jLabel1.setText("Cant.");

        search.setPrompt("buscar rapida...");
        search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 833, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(search, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spCant, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spCant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUpdateActionPerformed
        reloadTable();
    }//GEN-LAST:event_btUpdateActionPerformed

    private void tbListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbListMouseClicked
        if(tbList.getSelectedRowCount() > 1)
        {
            SIIL.servApp.getInstance().setInformation("Row count = " + Integer.toString(tbList.getSelectedRowCount()));
        }
        else
        {
            SIIL.servApp.getInstance().setInformation("");
        }
        if (evt.getClickCount() == 2 && !evt.isConsumed() && mode == Mode.READ)
        {
            updateOrder();
        }
        else if (evt.getClickCount() == 2 && mode == Mode.READ)
        {
            viewOrderDetail();
        }
        else if (evt.getClickCount() == 2 && mode == Mode.SELECT)
        {
            openDatabase(true);
            if (tbList.getSelectedRow() < 0) 
            {
                JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un registro primero por favor.",
                    "Operacion incompleta",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            OrderTableModel model = (OrderTableModel) tbList.getModel();
            selected = model.getValueAt(tbList.getSelectedRow());
            try 
            {
                selected.download(dbserver);
                selected.downFolio(dbserver);
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(Historial.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(dialog != null) dialog.dispose();
        }
    }//GEN-LAST:event_tbListMouseClicked

    private void searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchActionPerformed
        search();
    }//GEN-LAST:event_searchActionPerformed

    private boolean search()
    {
        OrderTableModel model = (OrderTableModel) tbList.getModel();
        if (search.getText().length() < 1) {
            return true;
        }
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
            return false;
        }
        
        try
        {
            model.search(dbserver,OrderTableModel.Mode.SEARCH,search.getText(),(Integer)spCant.getValue());
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        dbserver.close();
        return true;
    }

    private void spCantKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_spCantKeyReleased
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            OrderTableModel model = (OrderTableModel) tbList.getModel();
            if(model.getModePrevius() == OrderTableModel.Mode.SEARCH)
            {
                search();
            }
            else if(model.getModePrevius() == OrderTableModel.Mode.INITIAL || model.getModePrevius() == OrderTableModel.Mode.LISTING || model.getModePrevius() == OrderTableModel.Mode.RELOAD)
            {
                initialLoad();
            }
        }
    }//GEN-LAST:event_spCantKeyReleased

    private void mnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnNewActionPerformed
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
        JInternalFrame inter = new JInternalFrame("Capturar orden de Servicio",false,true);
        SIIL.services.order.CreateUpdate ord = new SIIL.services.order.CreateUpdate(inter,CreateUpdate.Mode.CREATE,null, this, dbserver);
        inter.setContentPane(ord);
        inter.setSize(ord.getPreferredSize());
        SIIL.servApp.getInstance().getDesktopPane().add(inter);
        int x = SIIL.servApp.getInstance().getDesktopPane().getSize().width/2 - ord.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
        dbserver.close();
    }//GEN-LAST:event_mnNewActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jdesktop.swingx.JXButton btUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu mnMain;
    private javax.swing.JMenuItem mnNew;
    private org.jdesktop.swingx.JXSearchField search;
    private javax.swing.JSpinner spCant;
    private org.jdesktop.swingx.JXTable tbList;
    // End of variables declaration//GEN-END:variables

    private void updateOrder() 
    {        
        if (tbList.getSelectedRow() < 0) 
        {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un registro primero por favor.",
                    "Operacion incompleta",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        OrderTableModel model = (OrderTableModel) tbList.getModel();
        Order orden = model.getValueAt(tbList.getSelectedRow());
        JInternalFrame inter = new JInternalFrame("Capturar orden de Servicio",false,true);
        CreateUpdate ord = new SIIL.services.order.CreateUpdate(inter,CreateUpdate.Mode.UPDATE,this,orden);
        inter.setContentPane(ord);
        inter.setSize(ord.getPreferredSize());
        SIIL.servApp.getInstance().getDesktopPane().add(inter);
        int x = SIIL.servApp.getInstance().getDesktopPane().getSize().width/2 - ord.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }
}
