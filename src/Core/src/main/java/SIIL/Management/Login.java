package SIIL.Management;

import SIIL.Server.MySQL;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * @version 2.1.0
 * @since Octubre 29, 2014
 * @author areyes
 */
public class Login extends javax.swing.JPanel 
{
    JPanel mod;
    Dimension dim;
    Policy policy;
    CardID cardid;
           
    public void check()
    {
        cardid.setBD("bc.tj");
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
        SIIL.Server.Login lg = new SIIL.Server.Login(txUser.getText(),passwd.getPassword(),conn,cardid.getBD());
        if(lg.valid())
        {
            cardid.getUser().setpID(lg.getuID());
            cardid.getUser().setAlias(lg.getAlias());
            //Optener datos adicionales del usuario
            String sql = "SELECT nameN1,nameAP FROM Users_Resolved WHERE alias = '" + txUser.getText() + "'";
            PreparedStatement stmt;
            try 
            {
                stmt = conn.getConnection().prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                if(rs.next())
                {
                    cardid.getUser().setN1(rs.getString("nameN1"));
                    cardid.getUser().setAP(rs.getString("nameAP"));
                }
                else
                {
                    ;
                }
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            cardid.setSuc(lg.getSuc());
            policy.setUser(cardid.getUser().getuID());
            /*if(policy.check(conn))
            {
                JFrame frm = new JFrame();
                frm.setContentPane(mod);
                frm.setSize(dim);
                frm.setVisible(true);
                frm.setResizable(false);
                frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ((Window)SwingUtilities.getWindowAncestor(this)).dispose();            
            }
            else
            {
                JOptionPane.showMessageDialog(this, "No teine permiso de acceso al recurso requerido", 
                        "Directiva de Seguridad", JOptionPane.WARNING_MESSAGE);
            }*/
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Usuario/Contrase??a incorectos", "Error Externo", JOptionPane.ERROR_MESSAGE);
        }
        conn.Close();
    }


    public Login(MySQL c,JPanel m, Dimension d, Policy police, CardID card) 
    {
        initComponents();
        mod = m;
        dim  = d;
        policy = police;
        cardid = card;
    }
    public Login(JPanel m, Dimension d, Policy police, CardID card) 
    {
        initComponents();
        mod = m;
        dim  = d;
        policy = police;
        cardid = card;
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
        jLabel2 = new javax.swing.JLabel();
        btLogin = new javax.swing.JToggleButton();
        passwd = new javax.swing.JPasswordField();
        txUser = new javax.swing.JTextField();

        jLabel1.setText("Usuario");

        jLabel2.setText("Contrasena");

        btLogin.setText("Entrar");
        btLogin.setToolTipText("");
        btLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btLoginActionPerformed(evt);
            }
        });
        btLogin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                btLoginKeyReleased(evt);
            }
        });

        passwd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                passwdKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(passwd, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                    .addComponent(txUser))
                .addContainerGap(163, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btLogin)
                .addGap(21, 21, 21))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(passwd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btLogin)
                .addContainerGap(18, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLoginActionPerformed
        check();
    }//GEN-LAST:event_btLoginActionPerformed

    private void btLoginKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btLoginKeyReleased
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            check();
        }
    }//GEN-LAST:event_btLoginKeyReleased

    private void passwdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwdKeyReleased
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            check();
        }
    }//GEN-LAST:event_passwdKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btLogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPasswordField passwd;
    private javax.swing.JTextField txUser;
    // End of variables declaration//GEN-END:variables

    private String convertBD(String item) 
    {
        switch(item)
        {
            case "Tijuana":
                return "bc.tj";
            case "Mexicali":
                return "bc.mx";
            case "Ensenada":
                return "bc.ens";
        }
        return "";
    }
}
