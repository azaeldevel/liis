
package SIIL.Clientes;

import SIIL.Server.Database;
import SIIL.client.sales.Enterprise;
import SIIL.core.config.Server;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Azael Reyes
 */
public class RequirePO extends javax.swing.JPanel 
{
    private Enterprise comp;    
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
    
    /**
     * Creates new form RequirePO
     */
    public RequirePO() 
    {
        initComponents();
        grOption.add(opA);
        grOption.add(opP);
        grOption.add(opO);
        grOption.add(opNo);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grOption = new javax.swing.ButtonGroup();
        lbClient = new javax.swing.JLabel();
        btClient = new javax.swing.JButton();
        lbTagClient = new javax.swing.JLabel();
        txClient = new javax.swing.JTextField();
        opA = new javax.swing.JRadioButton();
        opP = new javax.swing.JRadioButton();
        opO = new javax.swing.JRadioButton();
        btSave = new javax.swing.JButton();
        opNo = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();

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

        opA.setText("Anticipada");

        opP.setText("Posterior");

        opO.setText("Opcional");

        btSave.setText("Guardar");
        btSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSaveActionPerformed(evt);
            }
        });

        opNo.setText("No");

        jLabel1.setText("??Se Requiere Orden de Compra?");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbTagClient, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txClient, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btClient, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbClient, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(opNo)
                        .addGap(18, 18, 18)
                        .addComponent(opA)
                        .addGap(18, 18, 18)
                        .addComponent(opO)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btSave)
                            .addComponent(opP))))
                .addContainerGap(100, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btClient)
                        .addComponent(lbClient))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txClient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbTagClient)))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(opA)
                    .addComponent(opP)
                    .addComponent(opNo)
                    .addComponent(opO))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(btSave)
                .addGap(21, 21, 21))
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
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        
        SIIL.Clientes.ReadSelect read = new SIIL.Clientes.ReadSelect(comp,SIIL.servApp.cred.getBD());
        dlg.setContentPane(read);
        dlg.setSize(460, 260);
        dlg.setVisible(true);
        txClient.setText(comp.getNumber());
        lbClient.setText(comp.getName());
        comp.complete(dbserver);
        //>>
        try 
        {
            comp.downRequirePO(dbserver);
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(RequirePO.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(comp.getRequirePO() == Enterprise.RequirePO.ANTERIOR)
        {
            opA.setSelected(true);
        }
        else if(comp.getRequirePO() == Enterprise.RequirePO.POSTERIOR)
        {
            opP.setSelected(true);
        }
        else if(comp.getRequirePO() == Enterprise.RequirePO.OPCIONAL)
        {
            opO.setSelected(true);
        }
        else if(comp.getRequirePO() == Enterprise.RequirePO.NO)
        {
            opNo.setSelected(true);
        }
        else
        {
            
        }
        //<<
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
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }
            Database db=null;
            try
            {
                db = new Database(serverConfig);
            }
            catch (ClassNotFoundException | SQLException ex)
            {
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            if(comp.complete(db))
            {
                lbClient.setText(comp.getName());
                //>>
                try 
                {
                    comp.downRequirePO(db);
                    if(comp.getRequirePO() == Enterprise.RequirePO.ANTERIOR)
                    {
                        opA.setSelected(true);
                    }
                    else if(comp.getRequirePO() == Enterprise.RequirePO.POSTERIOR)
                    {
                        opP.setSelected(true);
                    }
                    else if(comp.getRequirePO() == Enterprise.RequirePO.OPCIONAL)
                    {
                        opO.setSelected(true);
                    }
                    else if(comp.getRequirePO() == Enterprise.RequirePO.NO)
                    {
                        opNo.setSelected(true);
                    }
                    else
                    {
                        
                    }
                } 
                catch (SQLException ex) 
                {
                    //Logger.getLogger(RequirePO.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                }
                //<<
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
            db.close();
        }
    }//GEN-LAST:event_txClientKeyReleased

    private void btSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSaveActionPerformed

        openDatabase(true);
        
        try
        {
            boolean ret = false;
            if(opA.isSelected())
            {
                ret = comp.upRequirePO(dbserver, Enterprise.RequirePO.ANTERIOR);
            }
            else if(opO.isSelected())
            {
                ret = comp.upRequirePO(dbserver, Enterprise.RequirePO.OPCIONAL);
            }
            else if(opP.isSelected())
            {
                ret = comp.upRequirePO(dbserver, Enterprise.RequirePO.POSTERIOR);
            }
            else if(opNo.isSelected())
            {
                ret = comp.upRequirePO(dbserver, Enterprise.RequirePO.NO);
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Selecione un opcion por favor.",
                        "Error externo", JOptionPane.ERROR_MESSAGE);
                closeDatabase();
                return;
            }
            
            if(ret)
            {
                dbserver.commit();
                JOptionPane.showMessageDialog(this, "Operaci??n completada.","Completada", JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                dbserver.rollback();
                JOptionPane.showMessageDialog(this, "Ocurrio un error inesperado en la etapa final de guardar la operacion se cancelo","Error externo", JOptionPane.ERROR_MESSAGE);
                closeDatabase();
            }
            closeDatabase();
        }
        catch(SQLException ex)
        {
            //Logger.getLogger(RequirePO.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
        }
    }//GEN-LAST:event_btSaveActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btClient;
    private javax.swing.JButton btSave;
    private javax.swing.ButtonGroup grOption;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lbClient;
    private javax.swing.JLabel lbTagClient;
    private javax.swing.JRadioButton opA;
    private javax.swing.JRadioButton opNo;
    private javax.swing.JRadioButton opO;
    private javax.swing.JRadioButton opP;
    private javax.swing.JTextField txClient;
    // End of variables declaration//GEN-END:variables
}
