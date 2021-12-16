
package purchases.order;

import SIIL.Server.Database;
import core.Renglon;
import database.mysql.purchases.order.PO;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import process.Moneda;
import process.State;
import core.GUIRenglon;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import stock.GuiItem;
import stock.Pedimento;

/**
 *
 * @author Azael Reyes
 */
public class FacturaRead extends javax.swing.JPanel 
{
    private Database dbserver;
    private PO po;
    
    public void reload()
    {
        FacturaReadTableModel model = (FacturaReadTableModel) tbList.getModel();
        try 
        {
            closeDatabase();
            openDatabase(true);
            model.download(dbserver, po);
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(FacturaRead.class.getName()).log(Level.SEVERE, null, ex);
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
                    if(!dbserver.getConnection().isClosed()) dbserver.close();
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
    
    private Moneda convertMoneda()
    {
        switch(cbMoney.getSelectedIndex())
        {
            case 1:
                return Moneda.MXN;
            case 2:
                return Moneda.USD;
            default:
                JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una moneda",
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                return null;
        }
    }
    
    
    /**
     * Creates new form FacturaCompra
     */
    public FacturaRead(PO po) 
    {
        initComponents(); 
        this.po = po;
        lbPO.setText("PO : " + po.getFullFolio());        
        openDatabase(true);        
        FacturaReadTableModel model = (FacturaReadTableModel) tbList.getModel();  
        
        try
        {
            model.download(dbserver, po);
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(FacturaRead.class.getName()).log(Level.SEVERE, null, ex);
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

        mnMain = new javax.swing.JPopupMenu();
        mnPedimento = new javax.swing.JMenuItem();
        mnCuentaFiscal = new javax.swing.JMenuItem();
        mnItem = new javax.swing.JMenuItem();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        lbPO = new javax.swing.JLabel();
        cbMoney = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbList = new org.jdesktop.swingx.JXTable();
        btArrive = new javax.swing.JButton();

        mnPedimento.setText("Pedimento");
        mnPedimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnPedimentoActionPerformed(evt);
            }
        });
        mnMain.add(mnPedimento);

        mnCuentaFiscal.setText("Cuenta fiscal");
        mnCuentaFiscal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnCuentaFiscalActionPerformed(evt);
            }
        });
        mnMain.add(mnCuentaFiscal);

        mnItem.setText("Articulo");
        mnItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnItemActionPerformed(evt);
            }
        });
        mnMain.add(mnItem);

        jTextField1.setEditable(false);

        jLabel1.setText("Total:");

        lbPO.setText("PO:");

        cbMoney.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Moneda...", "Pesos", "Dolar" }));
        cbMoney.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbMoneyActionPerformed(evt);
            }
        });

        tbList.setModel(new FacturaReadTableModel());
        tbList.setComponentPopupMenu(mnMain);
        tbList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbListMouseClicked(evt);
            }
        });
        tbList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbListKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tbList);

        btArrive.setText("Arribo");
        btArrive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btArriveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cbMoney, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btArrive, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lbPO, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 147, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbPO)
                    .addComponent(jLabel1)
                    .addComponent(cbMoney, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btArrive))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cbMoneyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbMoneyActionPerformed
        convertMoneda();
    }//GEN-LAST:event_cbMoneyActionPerformed

    private int getSelectedRow() 
    {
        if(tbList.getSelectedRow() < 0) 
        {
            return tbList.getSelectedRow();
        }
        else
        {
            return tbList.convertRowIndexToModel(tbList.getSelectedRow());
        }
    }
    
    private void tbListKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbListKeyReleased
        if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_DELETE)
        {
            FacturaReadTableModel model = (FacturaReadTableModel) tbList.getModel();
            Renglon ren = model.remove(getSelectedRow());
        }
        else if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_DELETE && evt.isControlDown())
        {
            setCostoPurchase();
        }
    }//GEN-LAST:event_tbListKeyReleased

    private void btArriveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btArriveActionPerformed
        FacturaReadTableModel model = (FacturaReadTableModel) tbList.getModel();
        List<Renglon> list = model.getList();
        openDatabase(true);
        
        State state = null;
        try 
        {
            if(!po.autoArrive(dbserver,SIIL.servApp.cred,list))
            {
                JOptionPane.showMessageDialog(
                    this,
                    "Fallo el registro en almacen",
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                closeDatabase();
                return;
            }
                           
            
            if(po.isFullArrive(dbserver)) 
            {
                state = po.getState().next(dbserver);                                
                if(!po.upState(dbserver, state))
                {
                    JOptionPane.showMessageDialog(
                            this,
                            "Fallo la transicion de estado",
                            "Error Interno",
                            JOptionPane.ERROR_MESSAGE
                    );
                    closeDatabase();
                    return;
                }
            }            
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(FacturaRead.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
        }
        int option = JOptionPane.showConfirmDialog
        (
            this,
            "Esta confirmando el arribo del PO '" + po.getFullFolio() + "',¿Desea continuar?",
            "Confirmar operación",
            JOptionPane.YES_NO_OPTION
        );
        
        try 
        {            
            if(option == JOptionPane.YES_OPTION)
            {
                dbserver.commit();
            }
            else
            {
                dbserver.rollback();
            }
            closeDatabase();
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(FacturaRead.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog
            (
                this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
        }        
    }//GEN-LAST:event_btArriveActionPerformed

    private void tbListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbListMouseClicked
        if(evt.getClickCount() == 2)
        {
            setCostoPurchase();
        }
    }//GEN-LAST:event_tbListMouseClicked

    private void mnPedimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnPedimentoActionPerformed
        if(getSelectedRow() < 0)
        {
            JOptionPane.showMessageDialog
            (
                this,
                "Seleccione un registro por favor.",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        FacturaReadTableModel model = (FacturaReadTableModel) tbList.getModel();
        Renglon renglon = model.getValueAt(getSelectedRow());        
        Pedimento ped = new Pedimento(renglon);
        core.Dialog dialog = new core.Dialog(ped);
        dialog.setContent(ped); 
        reload();
    }//GEN-LAST:event_mnPedimentoActionPerformed

    private void mnCuentaFiscalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnCuentaFiscalActionPerformed
        if(getSelectedRow() < 0)
        {
            JOptionPane.showMessageDialog
            (
                this,
                "Seleccione un registro por favor.",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        FacturaReadTableModel model = (FacturaReadTableModel) tbList.getModel();
        Renglon renglon = model.getValueAt(getSelectedRow());   
        GUIRenglon gRenglon = new GUIRenglon(renglon);
        boolean ret = false;
        try 
        {
            ret = gRenglon.upCuentaFiscal(dbserver);
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(FacturaRead.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(ret)
        {
            try 
            {
                dbserver.commit();
                reload();
                JOptionPane.showMessageDialog
                (
                    this,
                    "Operacion completa",
                    "Confirmacion",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(FacturaRead.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog
                (
                    this,
                    ex.getMessage(),
                    "Confirmacion",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
        else
        {
            try 
            {
                dbserver.rollback();
                JOptionPane.showMessageDialog
                (
                    this,
                    "Operacion cacelada",
                    "Confirmacion",
                    JOptionPane.WARNING_MESSAGE
                );
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(FacturaRead.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog
                (
                    this,
                    ex.getMessage(),
                    "Confirmacion",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }//GEN-LAST:event_mnCuentaFiscalActionPerformed

    private void mnItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnItemActionPerformed
        if(getSelectedRow() < 0)
        {
            JOptionPane.showMessageDialog
            (
                this,
                "Seleccione un registro por favor.",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        FacturaReadTableModel model = (FacturaReadTableModel) tbList.getModel();
        Renglon renglon = model.getValueAt(getSelectedRow());
        GuiItem gItem = new GuiItem(renglon);
        core.Dialog dialog = new core.Dialog(gItem);
        dialog.setContent(gItem);
        reload();
    }//GEN-LAST:event_mnItemActionPerformed

    
    private void setCostoPurchase() 
    {
        int select = getSelectedRow();
        if (select < 0) 
        {
            JOptionPane.showMessageDialog
            (
                this,
                "Seleccione un registro.",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        FacturaReadTableModel model = (FacturaReadTableModel) tbList.getModel();
        Renglon renglon = model.getValueAt(select);
        String costo = (String)JOptionPane.showInputDialog
        (
                this,
                "Indique el costo por unidad para '" + renglon.getNumber() +"'",
                "Captura de costo",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null
        );
        if(costo != null)
        {
            double co = 0;
            try
            {
                co = Double.parseDouble(costo);
            }
            catch(NumberFormatException ex)
            {
                //Logger.getLogger(FacturaRead.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog
                (
                    this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            
            if(convertMoneda() == null)
            {
                return;
            }
            
            openDatabase(true);
            
            try
            {
                renglon.upCostPurchase(dbserver, co, convertMoneda());
                renglon.downCostPurchase(dbserver);
            }
            catch (SQLException ex)
            {
                //Logger.getLogger(FacturaRead.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog
                    (
                            this,
                            ex.getMessage(),
                            "Error Interno",
                            JOptionPane.ERROR_MESSAGE
                    );
                return;
            }
        }
        try
        {
            dbserver.commit();
        }
        catch (SQLException ex)
        {
            //Logger.getLogger(FacturaRead.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog
            (
                this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            try
            {
                dbserver.rollback();
            }
            catch (SQLException ex1)
            {
                //Logger.getLogger(FacturaRead.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog
                    (
                            this,
                            ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            }
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btArrive;
    private javax.swing.JComboBox cbMoney;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lbPO;
    private javax.swing.JMenuItem mnCuentaFiscal;
    private javax.swing.JMenuItem mnItem;
    private javax.swing.JPopupMenu mnMain;
    private javax.swing.JMenuItem mnPedimento;
    private org.jdesktop.swingx.JXTable tbList;
    // End of variables declaration//GEN-END:variables
}
