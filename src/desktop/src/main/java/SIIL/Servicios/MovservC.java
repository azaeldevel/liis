
package SIIL.Servicios;

import SIIL.Server.Database;
import SIIL.Server.MySQL;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author areyes
 */
public class MovservC extends javax.swing.JPanel {

    /**
     * Creates new form Movserv
     */
    public MovservC(String bd) 
    {
        initComponents();
        BD = bd;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txCo = new javax.swing.JTextField();
        txCf = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txFolio = new javax.swing.JTextField();
        btChange = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txDate = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();

        jLabel1.setText("Cambiar");

        txCo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txCoFocusLost(evt);
            }
        });

        txCf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txCfFocusLost(evt);
            }
        });

        jLabel7.setText("Folio");

        btChange.setText("Cambiar");
        btChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btChangeActionPerformed(evt);
            }
        });

        jLabel8.setText("Fecha");

        jLabel9.setText("por");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btChange)
                .addGap(130, 130, 130))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txFolio, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(10, 10, 10)
                        .addComponent(txCo, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(txCf, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txDate, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txFolio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txCo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txCf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(btChange)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txCoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txCoFocusLost
        Co = new SIIL.Server.Charger();
        Co.setNumber(txCo.getText());
        Co.setBD(BD);
            SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();
            Database dbserver = null;
            try 
            {
                serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
                dbserver = new Database(serverConfig);
            } 
            catch (ClassNotFoundException | SQLException | IOException | ParserConfigurationException | SAXException ex) 
            {
                //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error externo",
                    JOptionPane.ERROR_MESSAGE
                    );
            }
        if(Co.checkExistCh(dbserver))
        {
           ;
        }
        else
        {
            JOptionPane.showMessageDialog(this, "El numero económico del cargador es desconocido", 
                            "Error Externo", JOptionPane.ERROR_MESSAGE);
        }
        dbserver.close();
    }//GEN-LAST:event_txCoFocusLost

    private void txCfFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txCfFocusLost
        Cf = new SIIL.Server.Charger();
        Cf.setNumber(txCf.getText());
        Cf.setBD(BD);
            SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();
            Database dbserver = null;
            try 
            {
                serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
                dbserver = new Database(serverConfig);
            } 
            catch (ClassNotFoundException | SQLException | IOException | ParserConfigurationException | SAXException ex) 
            {
                //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error externo",
                    JOptionPane.ERROR_MESSAGE
                    );
            }
        if(Cf.checkExistCh(dbserver))
        {
           ;
        }
        else
        {
            JOptionPane.showMessageDialog(this, "El numero económico de la bateria es desconocido", 
                            "Error Externo", JOptionPane.ERROR_MESSAGE);
        }
        dbserver.close();
    }//GEN-LAST:event_txCfFocusLost
    private boolean validFhMov() 
    {
        if(txDate.getText().matches("^(0?[1-9]|[12][0-9]|3[01])[\\/](0?[1-9]|1[012])[\\/]\\d{4}$"))
        {
            return true;
        }
        else
        {
            JOptionPane.showMessageDialog(this, "La fecha es incorrecta, el formato correcto es dd/mm/yyyy", "Error Externo", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    private boolean validFolio() 
    {
        if(txFolio.getText().matches("^[1-9]|[1-9][0-9]|[12][0-9][0-9]|300$"))// 
        {
            return true;
        }
        else
        {
            JOptionPane.showMessageDialog(this, "En el folio es un valor numerico entre 1 y 300.", "Error Externo", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    private void btChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btChangeActionPerformed
        SIIL.Server.Movimiento movser = new SIIL.Server.Movimiento();
        movser.setItemClass("serv");
        movser.setBD(BD);
        if(validFolio())
        {
            movser.setFolio(txFolio.getText());
        }
        else
        {
            return;
        }
        if(validFhMov())
        {
            try 
            {
                movser.setFhMov(txDate.getText());
            } 
            catch (ParseException ex) 
            {
                Logger.getLogger(MovCreate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            return;
        }  
        //movser.setCharger(Cf);
        MySQL conn = new MySQL();
        if(conn.Create() == null)
        {
            JOptionPane.showMessageDialog(this,
                "Conexion a Servidor Invalida",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
                );
            return;
        }

    }//GEN-LAST:event_btChangeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btChange;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField txCf;
    private javax.swing.JTextField txCo;
    private javax.swing.JTextField txDate;
    private javax.swing.JTextField txFolio;
    // End of variables declaration//GEN-END:variables
    SIIL.Server.Charger Co,Cf;
    String BD;
}
