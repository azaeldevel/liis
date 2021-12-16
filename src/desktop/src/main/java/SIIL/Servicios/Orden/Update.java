
package SIIL.Servicios.Orden;

import SIIL.Server.Database;
import SIIL.Server.MySQL;
import SIIL.Server.Person;
import SIIL.artifact.AmbiguosException;
import SIIL.artifact.DeployException;
import SIIL.service.quotation.OrdenUpdate;
import SIIL.core.Exceptions.DatabaseException;
import SIIL.core.Office;
import SIIL.trace.Trace;
import com.galaxies.andromeda.util.Texting.Message;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Result;
import org.xml.sax.SAXException;
import process.Return;

/**
 *
 * @author Azael
 */
public class Update extends javax.swing.JInternalFrame 
{
    public Update(OrdenUpdate orden, ModeEdit mode, session.Credential cred, Read screen) 
    {
        initComponents();
        this.cred = cred;
        this.screen = screen;
        this.orden = orden;
        this.mode = mode;
        switch(mode)
        {
            case FOLIO:
                txFolio.setEnabled(true);
                break;
            case OWNER:
                cbOwner.setEnabled(true);
                break;
            case CLIENT:
                txClient.setEnabled(true);
                btClient.setEnabled(true);
                break;
        }
        
        SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();        
        Database db = null;
        try 
        {
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
            db = new Database(serverConfig);
        } 
        catch (ClassNotFoundException | SQLException | IOException | ParserConfigurationException | SAXException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        try {
            loadCombo(db);
        } catch (SQLException ex) {
            //Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                    "Conexion a Servidor Invalida",
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
        }
        db.close();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbClient = new javax.swing.JLabel();
        jXLabel2 = new org.jdesktop.swingx.JXLabel();
        cbOwner = new org.jdesktop.swingx.JXComboBox();
        jXLabel3 = new org.jdesktop.swingx.JXLabel();
        cbTec = new org.jdesktop.swingx.JXComboBox();
        btCrear = new org.jdesktop.swingx.JXButton();
        jXLabel1 = new org.jdesktop.swingx.JXLabel();
        txFolio = new org.jdesktop.swingx.JXTextField();
        jLabel6 = new javax.swing.JLabel();
        txClient = new javax.swing.JTextField();
        btClient = new javax.swing.JButton();

        setClosable(true);

        lbClient.setText("##");

        jXLabel2.setText("Encargado");

        cbOwner.setToolTipText("La persona que le dara seguimiento directo con el cliente");
        cbOwner.setEnabled(false);

        jXLabel3.setText("Técnico");

        cbTec.setToolTipText("EL Técnico que evaluo en campo el servico");
        cbTec.setEnabled(false);

        btCrear.setText("Actualizar");
        btCrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCrearActionPerformed(evt);
            }
        });

        jXLabel1.setText("Folio");

        txFolio.setEnabled(false);

        jLabel6.setText("Cliente");

        txClient.setEnabled(false);
        txClient.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txClientKeyReleased(evt);
            }
        });

        btClient.setText("...");
        btClient.setEnabled(false);
        btClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btClientActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jXLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txFolio, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txClient, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btClient, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbClient))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btCrear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jXLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jXLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(cbOwner, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                                .addComponent(cbTec, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap(168, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jXLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txFolio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbClient)
                    .addComponent(btClient, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txClient))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jXLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbOwner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jXLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbTec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btCrear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCrearActionPerformed
        com.galaxies.andromeda.util.Progress progress;
        progress = SIIL.servApp.getInstance().getProgressObject();
        ((SIIL.servApp.Progress)SIIL.servApp.getInstance().getProgressObject()).On();
        
        progress.setProgress(1, "Iniciando actividad.");
        progress.setProgress(2, "Validando datos de insumo...");
        
        SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();        
        Database db = null;
        try 
        {
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
            db = new Database(serverConfig);
        } 
        catch (ClassNotFoundException | SQLException | IOException | ParserConfigurationException | SAXException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        
        progress.setProgress(30, "Validacion completa.");
        int fltrace = 0;
        orden.setTrace(new Trace(cred.getBD(), cred.getUser(), "Actualizacón de Encargado - Cotización " + orden.getFolio())); 
        try 
        {
            fltrace = orden.getTrace().insert(db);
        } 
        catch (SQLException ex) 
        {
            System.err.println("Fallo la creacion del registro de trace.");
            //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        Throwable up = null;
        boolean flMail = false;
        progress.setProgress(70, "Procensando peticion..."); 
        int res = 0,n;
        switch(mode)
        {
            case FOLIO:
                int newFolio = Integer.parseInt(txFolio.getText()); 
                n = JOptionPane.showOptionDialog(this,
                "Está cambiando el fólio de la cotización '" + orden.getFolio() + "' para asignar el nuevo " + newFolio + ". ¿Desea continuar?" ,
                "Confirme Operación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,null,null);
                if(n == JOptionPane.NO_OPTION)
                {
                    progress.clean();
                    return;
                }    
                Return ret = null;
                try 
                {
                    ret = orden.upFolio(db.getConnection(),newFolio);
                } 
                catch (SQLException ex) 
                {
                    //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                    progress.fail(ex);
                }    
                if(ret.getStatus() == Return.Status.DONE)
                {
                    flMail = orden.folioEmail(db);
                }
                break;
            case OWNER:
                n = JOptionPane.showOptionDialog(this,
                "Está cambiando el encargado de la cotización '" + orden.getFolio() + "'. ¿Desea continuar?" ,
                "Confirme Operación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,null,null);
                res = 0;
                if(n == JOptionPane.NO_OPTION)
                {
                    progress.clean();
                    return;
                }  
                orden.setOwner((Person) cbOwner.getSelectedItem());
                //System.out.println("Iniciando operacion de actualizacion updateOwner");
                up = orden.updateOwner(db);
                //Si la operacion en BD concluyo correctamente
                if(up == null)
                {//enviar correo
                    flMail = orden.ownerMail(db);
                }
                break;
            case CLIENT:
                n = JOptionPane.showOptionDialog(this,
                "Está cambiando el encargado de la cotización '" + orden.getFolio() + "'. ¿Desea continuar?" ,
                "Confirme Operación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,null,null);
                res = 0;
                if(n == JOptionPane.NO_OPTION)
                {
                    progress.clean();
                    return;
                }  
                if(flagChenged)
                {
                    //System.out.println("Se detecto cambio de cliente.");
                    try 
                    {
                        //System.out.println("Iniciando updateCompany...");
                        up = orden.updateCompany(db);
                        //Si la operacion en BD concluyo correctamente
                        if(up == null)
                        {//enviar correo
                            //System.out.println("Iniciando companyMail...");
                            flMail = orden.companyMail(db);
                        }
                    } 
                    catch (DatabaseException ex) 
                    {
                        System.err.println("Error en operaion en base de datos : " + ex.getMessage());
                    }
                }
                break;
        }            
        if(up == null && flMail)
        {
            try 
            {
                db.commit();
                screen.reloadTable();
                progress.finalized(new Message("Se modifico la cotizacion '" + orden.getFolio() + "'"));
                dispose();
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                progress.fail(new Exception("Fallo la confirmación de la operación."));
            }
        }
        else
        {
            try
            {
                db.rollback();
                progress.fail(new Exception("Fallo la operación, será cancelada.\nDevido a inconsistencia en la verificación de operación.",orden.getFail()));
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                progress.fail(ex);
            }
        }
    }//GEN-LAST:event_btCrearActionPerformed
        
    private void loadCombo(Database db) throws SQLException 
    {
        Throwable th = fillManager(db,cred);
        if( th != null)
        {
            JOptionPane.showMessageDialog(this,
                    th.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        th = fillTec(db,cred);
        if( th != null)
        {
            JOptionPane.showMessageDialog(this,
                    th.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
            );
        }        
    }
    
    public final Throwable fillManager(Database db, session.Credential cred) throws SQLException 
    {
        Field<?> ID = org.jooq.impl.DSL.field("pID",Integer.class);
        Field<?> BD = org.jooq.impl.DSL.field("BD",String.class);  
        Field<?> N1 = org.jooq.impl.DSL.field("nameN1",String.class);
        Field<?> AP = org.jooq.impl.DSL.field("nameAP",String.class);
        
        Office o = cred.getOffice();        
        String ostr = o.getFilter("office");
        if(ostr == null)
        {
            ostr = "";
        }
        else
        {
            ostr = ostr + " AND ";
        }
        Result<Record2<?,?>> result = (Result<Record2<?, ?>>) db.getContext().select(ID,BD).from("Persons").where(ostr + " BD = '" + cred.getBD() + "' AND email IS NOT NULL").orderBy(AP.asc(),N1.asc()).fetch();
        
        Person p = new Person();
        p.setpID(-1);
        p.setAP("...");
        p.setN1("Seleccione");
        cbOwner.addItem(p);
        int actualUserIndex = 0;
        for (Record r : result)
        {
            p = new Person();
            Throwable b = p.fill(db,(Integer)r.getValue(ID),(String)r.getValue(BD));
            if(b == null)
            {
                cbOwner.addItem(p);
                //buscar el usuario logeado.
                if(p.getpID() == cred.getUser().getpID())
                {
                    actualUserIndex = cbOwner.getItemCount();
                }
            }
            else
            {
                return b;
            }
        }
        cbOwner.setSelectedIndex(actualUserIndex - 1);        
        return null;
    }    
    
    public final Throwable fillTec(Database db, session.Credential cred) throws SQLException 
    {
        Field<?> ID = org.jooq.impl.DSL.field("pID",Integer.class);
        Field<?> BD = org.jooq.impl.DSL.field("BD",String.class);  
        Field<?> N1 = org.jooq.impl.DSL.field("nameN1",String.class);
        Field<?> AP = org.jooq.impl.DSL.field("nameAP",String.class);
        Office o = cred.getOffice();        
        String ostr = o.getFilter("office");
        if(ostr == null)
        {
            ostr = "";
        }
        else
        {
            ostr = ostr + " AND ";
        }
        Result<Record2<?,?>> result = (Result<Record2<?, ?>>) db.getContext().select(ID,BD).from("Persons").where( ostr + "BD = '" + cred.getBD() + "'").orderBy(AP.asc(),N1.asc()).fetch();
        
        Person p = new Person();
        p.setpID(-1);
        p.setAP("...");
        p.setN1("Seleccione");
        cbTec.addItem(p);
        for (Record r : result)
        {
            p = new Person();
            Throwable b = p.fill(db,(Integer)r.getValue(ID),(String)r.getValue(BD));
            if(b == null)
            {
                cbTec.addItem(p);
            }
            else
            {
                return b;
            }
        }
        
        return null;
    }   
    
    private void txClientKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txClientKeyReleased
        if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER)
        {
            SIIL.client.sales.Enterprise comp = new SIIL.client.sales.Enterprise();
            comp.setBD(cred.getBD());
            comp.setNumber(txClient.getText());
            orden.setCompany(comp);
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
                flagChenged = true;
                lbClient.setText(comp.getName());
                orden.setCompany(comp);
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

    private void btClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btClientActionPerformed
        SIIL.client.sales.Enterprise comp = new SIIL.client.sales.Enterprise();
        orden.setCompany(comp);
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
        SIIL.Clientes.ReadSelect read = new SIIL.Clientes.ReadSelect(comp,cred.getBD());
        dlg.setContentPane(read);
        dlg.setSize(460, 260);
        dlg.setVisible(true);
        txClient.setText(comp.getNumber());
        lbClient.setText(comp.getName());
        this.flagChenged = true;
    }//GEN-LAST:event_btClientActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btClient;
    private org.jdesktop.swingx.JXButton btCrear;
    private org.jdesktop.swingx.JXComboBox cbOwner;
    private org.jdesktop.swingx.JXComboBox cbTec;
    private javax.swing.JLabel jLabel6;
    private org.jdesktop.swingx.JXLabel jXLabel1;
    private org.jdesktop.swingx.JXLabel jXLabel2;
    private org.jdesktop.swingx.JXLabel jXLabel3;
    private javax.swing.JLabel lbClient;
    private javax.swing.JTextField txClient;
    private org.jdesktop.swingx.JXTextField txFolio;
    // End of variables declaration//GEN-END:variables

    session.Credential cred;
    Read screen;     
    OrdenUpdate orden;
    ModeEdit mode;
    private boolean flagChenged;
    
    public enum ModeEdit
    {
        FOLIO,
        CLIENT,
        OWNER,
        TECHNICAL,        
    }    
}
