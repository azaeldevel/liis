
package SIIL.Servicios.Grua.View.Resumov;

import SIIL.Server.Database;
import SIIL.Server.MySQL;
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
 * @author Azael
 */
public class Search extends javax.swing.JInternalFrame {

    /**
     * Creates new form Search
     */
    public Search(String bd,javax.swing.JDesktopPane dp,SIIL.Servicios.Grua.View.Resumov.ResumovList rm) 
    {
        initComponents();
        desktopPane = dp;
        BD = bd;
        resumov = rm; 
    }

    private String getWhere()
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
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        
        if(dbserver.getConnection() == null)
        {
            JOptionPane.showMessageDialog(this,
                "Conexion a Servidor Invalida",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
                );
            return null;
        }
        
        String sql = "";
        
        if(opdisp.isSelected())
        {
            if(sql.length()>0)
            {
                sql = sql + " or uso='disp'";
            }
            else
            {
                sql = sql + "uso='disp'";            
            }
        }
        if(oppres.isSelected())
        {
            if(sql.length()>0)
            {
                sql = sql + " or uso='pres'";
            }
            else
            {
                sql = sql + "uso='pres'";            
            }
        }
        if(oprep.isSelected())
        {
            if(sql.length()>0)
            {
                sql = sql + " or uso='rep'";
            }
            else
            {
                sql = sql + "uso='rep'";            
            }
        }
        if(oprtacp.isSelected())
        {
            if(sql.length()>0)
            {
                sql = sql + " or uso='rtacp'";
            }
            else
            {
                sql = sql + "uso='rtacp'";            
            }
        }
        if(oprtaoc.isSelected())
        {
            if(sql.length()>0)
            {
                sql = sql + " or uso='rtaoc'";
            }
            else
            {
                sql = sql + "uso='rtaoc'";            
            }
        }
        if(opmov.isSelected())
        {
            if(sql.length()>0)
            {
                sql = sql + " or uso='mov'";
            }
            else
            {
                sql = sql + "uso='mov'";            
            }
        }        
        if(opCurr.isSelected())
        {
            if(sql.length()>0)
            {
                sql = sql + " or uso='corr'";
            }
            else
            {
                sql = sql + "uso='corr'";            
            }
        }
        if(opVB.isSelected())
        {
            if(sql.length()>0)
            {
                sql = sql + " or uso='vta' or uso='baja'";
            }
            else
            {
                sql = sql + "uso='vta' or uso='baja'";            
            }
        }
        else
        {
            if(sql.length()>0)
            {
                sql = sql + " AND (uso!='vta' and uso!='baja')";
            }
            else
            {
                sql = sql + "(uso!='vta' and uso!='baja')";            
            }
        }
        
        
        if(oprtaoc.isSelected() || oprtacp.isSelected() || oprep.isSelected() || oppres.isSelected() || opdisp.isSelected() || opmov.isSelected() || opVB.isSelected() || opCurr.isSelected())
        {
            sql = "(" + sql + ")";
        }        
                      
        //cliente
        if(comp != null)
        {
            if(sql.length() > 0)
            {
                sql = sql + " and cBD='" + SIIL.servApp.cred.getBD() + "' and cNumber='" + comp.getNumber() + "'";
            }
            else
            {
                sql = sql + "cBD='" + SIIL.servApp.cred.getBD() + "' and cNumber='" + comp.getNumber() + "'";;
            }
        }
        
       
        
            
            String suc = "";
            
            if(opTj.isSelected())
            {
                suc = " suc = 'bc.tj'";
            }
            
            if(opMx.isSelected())
            {
                if(opTj.isSelected())
                {
                    suc = suc + " or suc = 'bc.mx'";
                }
                else
                {
                    suc = " suc = 'bc.mx'";
                }
            }
            
            if(opEns.isSelected())
            {
                if(opTj.isSelected() || opMx.isSelected())
                {
                    suc = suc + " or suc = 'bc.ens'";
                }
                else
                {
                    suc = " suc = 'bc.ens'";
                }
            }
            
            if(sql.length() > 0)
            {
                if(opTj.isSelected() || opMx.isSelected() || opEns.isSelected())
                {
                    sql = sql + " and (" + suc + ")";
                }
            }
            else
            {                
                if(opTj.isSelected() || opMx.isSelected() || opEns.isSelected())
                {
                    sql = "(" + suc + ")";
                }
            }
            
        
        if(!SIIL.servApp.cred.getSuc().equals("bc.tj"))
        {
            sql = "(" + sql + ") AND " + SIIL.servApp.cred.filterSucursal(dbserver);
        }
        

        dbserver.close();
        
        if(txBattery.getText().length() > 0)
        {
            if(sql.length() > 0)
            {
                sql = sql + " and batteryNumber = '" + txBattery.getText() + "'";
            }
            else
            {
                sql = " batteryNumber = '" + txBattery.getText() + "'";
            }
        }
        
        if(txCharger.getText().length() > 0)
        {
            if(sql.length() > 0)
            {
                sql = sql + " and chargerNumber = '" + txCharger.getText() + "'";
            }
            else
            {
                sql = " chargerNumber = '" + txCharger.getText() + "'";
            }
        }
        
        if(txForklift.getText().length() > 0)
        {
            if(sql.length() > 0)
            {
                sql = sql + " and titemNumber = '" + txForklift.getText() + "'";
            }
            else
            {
                sql = " titemNumber = '" + txForklift.getText() + "'";
            }
        }
        

        return sql;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        oprtaoc = new javax.swing.JCheckBox();
        opmov = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        opdisp = new javax.swing.JCheckBox();
        opVB = new javax.swing.JCheckBox();
        oprtacp = new javax.swing.JCheckBox();
        oppres = new javax.swing.JCheckBox();
        oprep = new javax.swing.JCheckBox();
        opEns = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        txBattery = new javax.swing.JTextField();
        txCharger = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txForklift = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        opTj = new javax.swing.JCheckBox();
        opMx = new javax.swing.JCheckBox();
        lbClient = new javax.swing.JLabel();
        btClient = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txClient = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        opCurr = new javax.swing.JCheckBox();

        setClosable(true);

        oprtaoc.setText("Renta Opcion de Compra");

        opmov.setText("Movimiento");

        jLabel1.setText("Uso:");

        opdisp.setText("Disponible");
        opdisp.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        opVB.setText("Incluir V & B");

        oprtacp.setText("Renta Corto Plazo");

        oppres.setText("Prestamo");

        oprep.setText("Reparacion");

        opEns.setText("Ensenada");

        jLabel3.setText("Bateria");

        jLabel4.setText("Cargador");

        jLabel5.setText("Montacargas");

        jLabel6.setText("Sucursal:");

        opTj.setText("Tijuana");

        opMx.setText("Mexicali");

        lbClient.setText("##");

        btClient.setText("...");
        btClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btClientActionPerformed(evt);
            }
        });

        jLabel7.setText("Cliente");

        txClient.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txClientKeyReleased(evt);
            }
        });

        jButton1.setText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        opCurr.setText("Cubriendo");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txBattery, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txCharger, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txForklift, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(53, 53, 53)
                                .addComponent(jLabel5)))
                        .addGap(34, 34, 34)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(txClient, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btClient, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lbClient, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(opTj)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(opMx)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(opEns))
                    .addComponent(jButton1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(opdisp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(oprtacp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(oppres)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(oprtaoc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(oprep)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(opmov)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(opCurr)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(opVB)))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(opdisp)
                    .addComponent(oprtacp)
                    .addComponent(oppres)
                    .addComponent(oprep)
                    .addComponent(oprtaoc)
                    .addComponent(jLabel1)
                    .addComponent(opmov)
                    .addComponent(opVB)
                    .addComponent(opCurr))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(opTj)
                            .addComponent(opMx)
                            .addComponent(opEns))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txBattery, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txCharger, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txForklift, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbClient)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btClient, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txClient)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btClientActionPerformed
        comp = new SIIL.Server.Company();
        JFrame frm = new JFrame();
        JDialog dlg = new JDialog(frm,"Seleccionar cliente",true);
        MySQL conn = new MySQL();
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
        SIIL.Clientes.ReadSelect read = new SIIL.Clientes.ReadSelect(comp,BD);
        dlg.setContentPane(read);
        dlg.setSize(460, 260);
        dlg.setVisible(true);
        txClient.setText(comp.getNumber());
        lbClient.setText(comp.getName());
    }//GEN-LAST:event_btClientActionPerformed

    private void txClientKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txClientKeyReleased
        if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER)
        {
            comp = new SIIL.Server.Company();
            comp.setBD(BD);
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
                JOptionPane.showMessageDialog(this,
                    "Fallo importacion.",
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            if(comp.complete(dbserver))
            {
                lbClient.setText(comp.getName());
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
                    comp=null;
                    lbClient.setText("###");
                }
            }
            dbserver.close();
        }
    }//GEN-LAST:event_txClientKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        resumov.reloadTable(getWhere());
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private String filtroInterno(String sql) 
    {
        //Filtro interno de Sucursal.
        switch(SIIL.servApp.cred.getSuc())
        {
            case "bc.tj":
                break;
            case "bc.mx":
                if(sql.length() > 0)
                {
                    sql = sql + " and suc='bc.mx'";
                }
                else
                {
                    sql = " suc='bc.mx' ";
                }
                break;
            case "bc.ens":
                if(sql.length() > 0)
                {
                    sql = sql + " and suc='bc.ens' ";
                }
                else
                {
                    sql = " suc='bc.ens' ";
                }
                break;
        }
        return sql;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btClient;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel lbClient;
    private javax.swing.JCheckBox opCurr;
    private javax.swing.JCheckBox opEns;
    private javax.swing.JCheckBox opMx;
    private javax.swing.JCheckBox opTj;
    private javax.swing.JCheckBox opVB;
    private javax.swing.JCheckBox opdisp;
    private javax.swing.JCheckBox opmov;
    private javax.swing.JCheckBox oppres;
    private javax.swing.JCheckBox oprep;
    private javax.swing.JCheckBox oprtacp;
    private javax.swing.JCheckBox oprtaoc;
    private javax.swing.JTextField txBattery;
    private javax.swing.JTextField txCharger;
    private javax.swing.JTextField txClient;
    private javax.swing.JTextField txForklift;
    // End of variables declaration//GEN-END:variables

    SIIL.Server.Company comp;
    String BD;
    javax.swing.JDesktopPane desktopPane;
    SIIL.Servicios.Grua.View.Resumov.ResumovList resumov;
}
