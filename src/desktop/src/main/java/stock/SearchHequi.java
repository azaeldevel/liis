
package stock;

import SIIL.Server.Database;
import SIIL.client.sales.Enterprise;
import static SIIL.servApp.BACKWARD_BD;
import static SIIL.servApp.cred;
import SIIL.services.grua.Resumov;
import SIIL.services.grua.Uso;
import SIIL.trace.Trace;
import database.mysql.stock.Titem;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import process.Return;

/**
 *
 * @author Azael Reyes
 */
public class SearchHequi extends javax.swing.JPanel implements core.DialogContent
{
    private SearchHequiTableModel.Mode mode;
    private Database database;
    private SIIL.core.config.Server serverConfig;
    //private JXDialog dialog;
    private Flow[] flows;
    private core.Dialog dialog;
    
    
    /**
     * Creates new form SearchHequi
     * @param mode
     * @param database
     */
    public SearchHequi(SearchHequiTableModel.Mode mode,Database database) 
    {
        initComponents();
        this.mode = mode;
        SearchHequiTableModel list = (SearchHequiTableModel) tbList.getModel();
        if(database == null)
        {
            SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();
            try 
            {
                serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
            } 
            catch (IOException | ParserConfigurationException | SAXException ex) 
            {
                JOptionPane.showMessageDialog(this,
                    "Fallo importacion.",
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }
        list.search(database,mode,null);
        
        //
        if(cred.acces(database,"Servicios.Grua.Write"))
        {
            Update.setEnabled(true);
        }
        else
        {
            Update.setEnabled(false);
        }
    }
    
    public Boolean reload(Database database)
    {
        if(database != null)
        {
            try 
            {
                if(database.getConnection().isClosed()) 
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
                        JOptionPane.showMessageDialog(this,
                                ex.getMessage(),
                                "Error Interno",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            } 
            catch (SQLException ex) 
            {
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
        else
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
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
            SearchHequiTableModel model = (SearchHequiTableModel) tbList.getModel();
            //System.out.println(search.getText());
            model.search(database,mode,search.getText());
   
        return true;
    }
    
    public Flow[] getSelection()
    {
        return flows;
    }
    
    private void setSelectTitem()
    {
        if(tbList.getSelectedRowCount() > 0)
        {
            int[] ts = tbList.getSelectedRows();
            flows = new Flow[tbList.getSelectedRowCount()];
            SearchHequiTableModel model = (SearchHequiTableModel) tbList.getModel();
            for(int i = 0; i < ts.length; i++)
            {
                flows[i] = model.getValueAt(ts[i]);
            }
        }
    }
    
    /**
     * Creates new form SearchHequi
     * @param mode
     */
    public SearchHequi(SearchHequiTableModel.Mode mode) 
    {
        initComponents();
        this.mode = mode;
        SearchHequiTableModel list = (SearchHequiTableModel) tbList.getModel();
        
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
        database = dbserver;
        list.search(dbserver,mode,null);
        
        //
        if(cred.acces(dbserver,"Servicios.Grua.Write"))
        {
            Update.setEnabled(true);
        }
        else
        {
            Update.setEnabled(false);
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
        Update = new javax.swing.JMenuItem();
        mnAddResumov = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbList = new org.jdesktop.swingx.JXTable();
        search = new org.jdesktop.swingx.JXSearchField();
        btAddEquipos = new javax.swing.JButton();

        Update.setText("Actualizar Marca");
        Update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateActionPerformed(evt);
            }
        });
        mnMain.add(Update);

        mnAddResumov.setText("Agregar a hoja de Renta");
        mnAddResumov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAddResumovActionPerformed(evt);
            }
        });
        mnMain.add(mnAddResumov);

        tbList.setModel(new SearchHequiTableModel());
        tbList.setComponentPopupMenu(mnMain);
        tbList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbListMouseClicked(evt);
            }
        });
        tbList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbListKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbListKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tbList);

        search.setPrompt("Buscar...");
        search.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchFocusGained(evt);
            }
        });
        search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchActionPerformed(evt);
            }
        });

        btAddEquipos.setText("Agregar");
        btAddEquipos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddEquiposActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(search, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btAddEquipos)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btAddEquipos))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void searchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchFocusGained
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
        if(database != null) 
        {
            database.close();
            database = null;
        }
        database =  dbserver;
    }//GEN-LAST:event_searchFocusGained

    private void searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchActionPerformed
        reload(database);
    }//GEN-LAST:event_searchActionPerformed

    private void tbListKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbListKeyReleased
        evt.consume();
        if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER)
        {
            setSelectTitem();
            if(dialog != null && (mode == SearchHequiTableModel.Mode.SelectTitem || mode == SearchHequiTableModel.Mode.SelectForklift)) dialog.dispose();
        }
    }//GEN-LAST:event_tbListKeyReleased

    private void tbListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbListKeyPressed
        evt.consume();
    }//GEN-LAST:event_tbListKeyPressed

    private void UpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateActionPerformed
        getSelection();
        if(flows == null) 
        {
            return;
        }
        
        JInternalFrame inter = new JInternalFrame("Actualizar Marca",false,true);
        stock.UpdateMake up = new stock.UpdateMake(flows[0],inter,this);
        inter.setContentPane(up);
        inter.setSize(up.getPreferredSize());
        SIIL.servApp.getInstance().getDesktopPane().add(inter);
        int x = SIIL.servApp.getInstance().getDesktopPane().getSize().width/2 - up.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }//GEN-LAST:event_UpdateActionPerformed

    private void tbListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbListMouseClicked
        setSelectTitem();
        if(evt.getClickCount() == 2)
        {
            if(dialog != null) dialog.dispose();
        }
    }//GEN-LAST:event_tbListMouseClicked

    private void mnAddResumovActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAddResumovActionPerformed
    try 
    {
        getSelection();
        if(flows == null)
        {
            return;
        }
            
        if(flows.length > 1)
        {
            JOptionPane.showMessageDialog(this,
                        "Seleccion solo un registro",
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
            );
            return;
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
                return;
            }
            
            Resumov resumov = new Resumov(-1);
            Uso uso = new Uso(1);//disponible por default
            uso.download(database.getConnection());
            Enterprise company = new Enterprise(-1);
            //company.complete(database);
            company.fill(database, cred, "999");// 999 - ALMACEN SIIL
            Trace contexTrace = new Trace(BACKWARD_BD, cred.getUser(), "Inservion directa de equipo en Hoja de Renta");
            Boolean result = resumov.insert(database,SIIL.servApp.cred.getOffice(),uso,company,flows[0],new Date(),contexTrace);
            if(result)
            {
                database.commit();
                JOptionPane.showMessageDialog(this,
                        "Se completo la operación.",
                        "COnfirmado",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }
            else
            {
                database.rollback();
                JOptionPane.showMessageDialog(this,
                    "Fallo la operación.",
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(SearchHequi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnAddResumovActionPerformed

    private void btAddEquiposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddEquiposActionPerformed
        AddEquipo add = new AddEquipo();
        core.Dialog dialog = new core.Dialog(add); 
        dialog.setContent(add);
        dialog.dispose();
    }//GEN-LAST:event_btAddEquiposActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem Update;
    private javax.swing.JButton btAddEquipos;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem mnAddResumov;
    private javax.swing.JPopupMenu mnMain;
    private org.jdesktop.swingx.JXSearchField search;
    private org.jdesktop.swingx.JXTable tbList;
    // End of variables declaration//GEN-END:variables

    /**
     * @param dialog the dialog to set
     */
    public void setDialog(core.Dialog dialog) 
    {
        this.dialog = dialog;
    }

    /**
     * @return the titem
     */
    public Flow[] getTitem() 
    {
        return flows;
    }
}
