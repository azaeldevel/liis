
package SIIL.Servicios.Titem;

import SIIL.Management.Policy;
import SIIL.Server.Database;
import SIIL.Server.MySQL;
import java.awt.Window;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author areyes
 */
public class Read extends javax.swing.JPanel 
{
    String BD;
    int limitSQL;
    /**
     * Creates new form Read
     * @param bd
     */
    public Read(String bd) 
    {
        initComponents();
        BD = bd;
        limitSQL = 7;
        /*MySQL conn = new MySQL();
        conn.Create();
        if(conn.getConnection() == null)
        {
            JOptionPane.showMessageDialog(this,
                "Conexion a Servidor Invalida",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
                );
            return;
        }
        //where = sqlwhere;
        fillTable(tbList, conn);
        conn.Close();*/
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
        tbList = new javax.swing.JTable();
        btUpdate = new javax.swing.JButton();
        txNumEco = new javax.swing.JTextField();
        txName = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txLength = new javax.swing.JTextField();
        btReload = new javax.swing.JButton();
        btClose = new javax.swing.JButton();

        tbList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "N??mero", "Marca", "Modelo", "Serie", "Nombre"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tbList);

        btUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/SIIL/resources/editclear.png"))); // NOI18N
        btUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUpdateActionPerformed(evt);
            }
        });

        txNumEco.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txNumEcoKeyReleased(evt);
            }
        });

        txName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txNameKeyReleased(evt);
            }
        });

        jLabel1.setText("Cant.");

        txLength.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txLengthFocusLost(evt);
            }
        });

        btReload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/SIIL/resources/reload.png"))); // NOI18N
        btReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btReloadActionPerformed(evt);
            }
        });

        btClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/SIIL/resources/dialog-close.png"))); // NOI18N
        btClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txLength, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btReload)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btUpdate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btClose))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txNumEco, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txName, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txNumEco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btReload)
                    .addComponent(btUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txLength)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(btClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUpdateActionPerformed
        SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();        
        Database dbserver = null;
        try 
        {
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
            dbserver = new Database(serverConfig);
        } 
        catch (ClassNotFoundException | SQLException | IOException | ParserConfigurationException | SAXException ex) 
        {;
            JOptionPane.showMessageDialog(this,
                "Fallo importacion.",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        Policy pol = new Policy();
        pol.setPolicy("Servicios.Write");
        pol.setPred("Y");
        //pol.setUser(SIIL.servApp.card.getUser().getuID());
        /*if(pol.check(conn))
        {
            if(tbList.getSelectedRow()<0)
            {
                JOptionPane.showMessageDialog(
                this,
                "Debe selecionar un equipo para la operaci??n",
                "Error externo",
                JOptionPane.ERROR_MESSAGE);
                return;
        }        */
        SIIL.Server.Titem t = new SIIL.Server.Titem();
        t.setNumber(((DefaultTableModel)tbList.getModel()).getValueAt(tbList.getSelectedRow(),0).toString());
        t.setBD(BD);
        boolean complete = t.complete(dbserver,false);
        dbserver.close();
        if(complete)
        {
            SIIL.Servicios.Titem.Update rd = new SIIL.Servicios.Titem.Update(t);
            JDialog dlg = new JDialog(new JFrame(),"Lista de Equipos",true);
            dlg.setResizable(false);
            dlg.setContentPane(rd);
            dlg.setSize(550, 200);
            dlg.setVisible(true);
            fillTb();
        }
        else
        {
                JOptionPane.showMessageDialog(
                this,
                "No se encontr?? el elemento seleccionado",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE);
                return;
        }
    }//GEN-LAST:event_btUpdateActionPerformed

    private void txNumEcoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txNumEcoKeyReleased
        fillTb();
    }//GEN-LAST:event_txNumEcoKeyReleased

    private void txNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txNameKeyReleased
        fillTb();
    }//GEN-LAST:event_txNameKeyReleased

    private void txLengthFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txLengthFocusLost
        if(txLength.getText().matches("^[0-9]+$"))
        {
            limitSQL = Integer.parseInt(txLength.getText());
            fillTb();
        }
        else
        {
            JOptionPane.showMessageDialog(this, "El n??mero tiene formato incorrecto"
                    , "Error Externo", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_txLengthFocusLost

    private void btReloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btReloadActionPerformed
        fillTb();
    }//GEN-LAST:event_btReloadActionPerformed

    private void btCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCloseActionPerformed
        ((Window)SwingUtilities.getWindowAncestor(this)).dispose();
    }//GEN-LAST:event_btCloseActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btClose;
    private javax.swing.JButton btReload;
    private javax.swing.JButton btUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbList;
    private javax.swing.JTextField txLength;
    private javax.swing.JTextField txName;
    private javax.swing.JTextField txNumEco;
    // End of variables declaration//GEN-END:variables

    private void fillTable(JTable tbList,MySQL conn) 
    {
        DefaultTableModel dm = ((DefaultTableModel)tbList.getModel());
        int rows = dm.getRowCount();
        for(int i = 0; i < rows; ++i)
        {
            dm.removeRow(0);
        }
        String sql = "SELECT * FROM Titem_Resolved WHERE BD = '" + BD + "'";
        //if(where.length()>0)
        {
            //sql = sql + " WHERE " + where;
            
        }
        Statement stmt = null;
        try 
        {
            stmt = (Statement) conn.getConnection().createStatement();
            //System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            Object[] row;
            while(rs.next())
            {
                row = new Object[5];
                row[0] = rs.getString("number");
                row[1] = rs.getString("serie");
                row[2] = rs.getString("modelo");
                row[3] = rs.getString("marca");
                row[4] = rs.getString("name");
                dm.addRow(row);
            }
            rs.close();
            stmt.close();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void fillTb() 
    {
        DefaultTableModel dm = ((DefaultTableModel)tbList.getModel());
        int rows = dm.getRowCount();
        for(int i = 0; i < rows; ++i)
        {
            dm.removeRow(0);
        }
        String sql = "SELECT * FROM Titem_Resolved WHERE BD = '" + BD + "'";
        String where = strWhere();
        if(where.length()>0)
        {
            sql = sql + " and " + where;            
        }
        sql = sql + " LIMIT " + limitSQL;
        MySQL conn = new MySQL();
        conn.Create();
        Statement stmt = null;
        try 
        {
            stmt = (Statement) conn.getConnection().createStatement();
            //System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            Object[] row;
            while(rs.next())
            {
                row = new Object[5];
                row[0] = rs.getString("number");
                row[1] = rs.getString("marca");
                row[2] = rs.getString("modelo");
                row[3] = rs.getString("serie");
                row[4] = rs.getString("name");
                dm.addRow(row);
            }
            rs.close();
            stmt.close();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        conn.Close();
    }

    private String strWhere() 
    {
        String strSQL = "";
        
        if(txNumEco.getText().length()>0)
        {
            strSQL = strSQL + " number LIKE '" + txNumEco.getText() + "'";
        }
        
        if(txName.getText().length()>0)
        {
            strSQL = strSQL + " name LIKE '" + txName.getText() + "'";
        }
        
        return strSQL;
    }
}
