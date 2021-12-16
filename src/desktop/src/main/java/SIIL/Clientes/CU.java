
package SIIL.Clientes;

import SIIL.Server.Company;
import SIIL.Server.Database;
import SIIL.client.sales.Enterprise;
import core.Dialog;
import core.DialogContent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import process.Return;

/**
 *
 * @author Azael
 */
public class CU extends javax.swing.JPanel implements DialogContent
{
    private core.Dialog dialog;
    private JInternalFrame frame;
    private Enterprise selected;
    private ReadSelect2 readView;
          
    public void setReadView(ReadSelect2 readView)
    {
        this.readView = readView;
    }
    public void setFrame(JInternalFrame frame)            
    {
        this.frame = frame;
    }
    
    @Override
    public void setDialog(Dialog dialog) 
    {
        this.dialog = dialog;
    }

    private void updateClient() 
    {        
        if(txName.getText().length() == 0)
        {
            JOptionPane.showMessageDialog(this,
            "Capture el nombre del Cliente.",
            "Error",
            JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();
        try 
        {
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
        }
        catch (IOException | ParserConfigurationException | SAXException ex) 
        {
            //Logger.getLogger(CU.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
            ex.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
        }
        Database dbserver = null;
        try 
        {
            dbserver = new Database(serverConfig);
        }
        catch (ClassNotFoundException | SQLException ex) 
        {
            //Logger.getLogger(CU.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
            ex.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
        }
        
        Return<Integer> ret = null;
        try 
        {
            ret = selected.upName(dbserver.getConnection(), txName.getText());
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(CU.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
            ex.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
        }
        
        if(ret.isFlag())
        {
            try 
            {
                dbserver.commit();
                if(readView != null) readView.reload();
                if(frame != null) frame.dispose();
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(CU.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
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
                //Logger.getLogger(CU.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            }            
        }
        dbserver.close();
    }

    public enum Commnad
    {
        CREATE,
        UPDATE
    }
    
    Commnad commnad;
    
    private void createClient() 
    {
        if(txNumber.getText().length() == 0)
        {
            JOptionPane.showMessageDialog(this,
            "Capture el numero del Cliente.",
            "Error",
            JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(txName.getText().length() == 0)
        {
            JOptionPane.showMessageDialog(this,
            "Capture el nombre del Cliente.",
            "Error",
            JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();
        try 
        {
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
        } 
        catch (IOException | ParserConfigurationException | SAXException ex) 
        {
            //Logger.getLogger(CU.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
            ex.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
        }
        Database dbserver = null;
        try 
        {
            dbserver = new Database(serverConfig);
        } 
        catch (ClassNotFoundException | SQLException ex) 
        {
            //Logger.getLogger(CU.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
            ex.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
        }
        
        SIIL.Server.Company empresa = new SIIL.Server.Company();
        Return<Integer> ret = null;
        try 
        {
            ret = empresa.insert(dbserver.getConnection(), txNumber.getText(), txName.getText());
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(CU.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
            ex.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
        }
        
        if(ret.isFlag())
        {
            try 
            {
                dbserver.commit();
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(CU.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
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
                //Logger.getLogger(CU.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            }            
        }
        dbserver.close();
        if(dialog != null) dialog.dispose();
        if(frame != null) frame.dispose();
    }
    
    
    /**
     * Por default se pasa el comando CREATE
     */
    public CU() 
    {
        initComponents();        
        commnad = Commnad.CREATE;
    }
    
    public CU(Enterprise selected) 
    {
        initComponents();        
        commnad = Commnad.UPDATE;
        this.selected = selected;
        lbNumber.setEnabled(false);
        txNumber.setEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbNumber = new javax.swing.JLabel();
        txNumber = new javax.swing.JTextField();
        lbName = new javax.swing.JLabel();
        txName = new javax.swing.JTextField();
        btAcept = new javax.swing.JButton();

        lbNumber.setText("Numero");
        lbNumber.setToolTipText("");

        lbName.setText("Nombre");

        btAcept.setText("Aceptar");
        btAcept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAceptActionPerformed(evt);
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
                        .addComponent(lbNumber)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txName, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(86, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btAcept)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbNumber)
                    .addComponent(txNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbName)
                    .addComponent(txName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addComponent(btAcept)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btAceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAceptActionPerformed
        if(commnad == Commnad.CREATE) createClient();
        if(commnad == Commnad.UPDATE) updateClient();
    }//GEN-LAST:event_btAceptActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAcept;
    private javax.swing.JLabel lbName;
    private javax.swing.JLabel lbNumber;
    private javax.swing.JTextField txName;
    private javax.swing.JTextField txNumber;
    // End of variables declaration//GEN-END:variables
}
