
package SIIL.Clientes;

import SIIL.Server.Database;
import SIIL.client.sales.Enterprise;
import SIIL.servApp;
import core.Dialog;
import java.awt.Dimension;
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
 * @version 1.0
 * @since Octubre 29, 2014
 * @author Azael Reyes
 */
public class ReadSelect extends javax.swing.JPanel implements core.DialogContent
{
    private SIIL.Server.Company empresa;
    private Enterprise selected;
    private String BD;
    private Filter filter;
    private Database dbserver;  
    private core.Dialog dialog;
    
    
    public void seleccion(int sel)
    {
        ReadTableModel model = (ReadTableModel) tbClient.getModel();
        if(filter==Filter.SAERCH)
        {
            empresa.copy(model.getValueAt(sel));
            ((Window)SwingUtilities.getWindowAncestor(this)).dispose();
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
    
    public ReadSelect() 
    {
        initComponents();
        iniComponents2(); 
        txSearch.requestFocus();        
        tbClient.getColumnModel().getColumn(0).setPreferredWidth(20);
        tbClient.getColumnModel().getColumn(1).setPreferredWidth(20);
        BD = "bc.tj";
        filter = Filter.SAERCH;
    }
        
    public ReadSelect(SIIL.Server.Company e,String bd) 
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
    public ReadSelect(Filter field, String rfc) 
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
        txSearch = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(410, 220));
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

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

        txSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txSearchKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 126, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(txSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

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

    private void tbClientMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbClientMousePressed
        if(evt.getClickCount() == 2 && tbClient.getSelectedRow() > -1)
        {
            seleccion(tbClient.getSelectedRow());
        }
    }//GEN-LAST:event_tbClientMousePressed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER && tbClient.getRowCount() == 1)
        {
            seleccion(0);
        }     
    }//GEN-LAST:event_formKeyPressed

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



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbClient;
    private javax.swing.JTextField txSearch;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the selected
     */
    public Enterprise getSelected() 
    {
        return selected;
    }

    @Override
    public void setDialog(Dialog dialog) 
    {
        this.dialog = dialog;
    }
}
