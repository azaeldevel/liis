
package stock;

import SIIL.Server.Database;
import core.GeneralCellEditor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.TableColumn;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Azael Reyes
 */
public class Almacen extends javax.swing.JPanel 
{
    private Database dbserver;
    private org.jdesktop.swingx.JXComboBox cbContainerTemporal;
    private ContainerComboBoxModel containerModelTemporal;
    
    private int getSelectRow() 
    {
        return tbList.getSelectedRow();
        //return tbList.convertRowIndexToModel(tbList.getSelectedRow());
    }
        
    private void configContainer() throws SQLException 
    {
        //ComboBox para editar mecanicos
        cbContainerTemporal = new org.jdesktop.swingx.JXComboBox();
        cbContainerTemporal.setEditable(false);
        containerModelTemporal = new ContainerComboBoxModel();
        containerModelTemporal.fill(dbserver);
        cbContainerTemporal.setModel(containerModelTemporal);
        TableColumn colContainer = tbList.getColumnModel().getColumn(2);
        colContainer.setCellEditor(new GeneralCellEditor(cbContainerTemporal));        
        cbContainerTemporal.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(getSelectRow() > -1 && cbContainerTemporal.getSelectedIndex() > -1)
                {
                    AlmacenTableModel model = (AlmacenTableModel) tbList.getModel();
                    Allocated allocated = model.getValueAt(getSelectRow());
                    openDatabase(true);
                    try 
                    {
                        allocated.upContainer(dbserver.getConnection(), ((Container)cbContainerTemporal.getSelectedItem()));
                        dbserver.commit();                        
                        model.download(dbserver, ((Container)cbContainer.getSelectedItem()));                        
                    }
                    catch (SQLException ex) 
                    {
                        //Logger.getLogger(Almacen.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog
                        (
                            null,
                            "Seleccione un registro",
                            "Error Interno",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }                    
                }
            }
        });
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
    
    /**
     * Creates new form Almacen
     */
    public Almacen() 
    {
        initComponents();
        
        openDatabase(true);        
        ContainerComboBoxModel containerModel = (ContainerComboBoxModel) cbContainer.getModel();
        try 
        {
            containerModel.fill(dbserver);
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Almacen.class.getName()).log(Level.SEVERE, null, ex);
        }
        loaddata();
        try 
        {
            configContainer();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Almacen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loaddata() 
    {
        AlmacenTableModel model = (AlmacenTableModel) tbList.getModel();
        ContainerComboBoxModel containerComboBoxModel = (ContainerComboBoxModel) cbContainer.getModel();
        try 
        {
            openDatabase(true);
            if(cbContainer.getSelectedIndex() > 0)
            {
                model.download(dbserver, (Container) cbContainer.getSelectedItem());                
            }
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(Almacen.class.getName()).log(Level.SEVERE, null, ex);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jXTable1 = new org.jdesktop.swingx.JXTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbList = new org.jdesktop.swingx.JXTable();
        cbContainer = new javax.swing.JComboBox();

        jXTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jXTable1);

        tbList.setModel(new AlmacenTableModel());
        jScrollPane2.setViewportView(tbList);

        cbContainer.setModel(new ContainerComboBoxModel());
        cbContainer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbContainerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 682, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(cbContainer, javax.swing.GroupLayout.PREFERRED_SIZE, 561, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cbContainerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbContainerActionPerformed
        loaddata();
    }//GEN-LAST:event_cbContainerActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cbContainer;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private org.jdesktop.swingx.JXTable jXTable1;
    private org.jdesktop.swingx.JXTable tbList;
    // End of variables declaration//GEN-END:variables
}
