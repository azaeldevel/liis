
package SIIL.Servicios.Orden;

import SIIL.Server.Database;
import SIIL.Server.MySQL;
import SIIL.Server.Person;
import SIIL.artifact.AmbiguosException;
import SIIL.artifact.DeployException;
import SIIL.core.Office;
import SIIL.trace.Trace;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.internet.AddressException;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Result;
import org.xml.sax.SAXException;
import process.State;

/**
 *
 * @author Azael
 */
public class Create extends javax.swing.JInternalFrame implements Runnable
{
    private OrdenCrear ord;
    private session.Credential cred;
    private Read screen;
    private core.MecanicoComboBoxModel mecModel;
    
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
        Result<Record2<?,?>> result = (Result<Record2<?, ?>>) db.getContext().select(ID,BD).from("Persons").where(ostr + " BD = '" + cred.getBD() + "' AND email IS NOT NULL AND active='Y' AND isOrserOwner='Y'").orderBy(AP.asc(),N1.asc()).fetch();
        
        Person p = new Person();
        p.setpID(-1);
        p.setAP("...");
        p.setN1("Seleccione");
        cbOwner1.addItem(p);
        cbOwner2.addItem(p);
        int actualUserIndex = 0;
        for (Record r : result)
        {
            p = new Person();
            Throwable b = p.fill(db,(Integer)r.getValue(ID),(String)r.getValue(BD));
            if(b == null)
            {
                cbOwner1.addItem(p);
                cbOwner2.addItem(p);
                //buscar el usuario logeado.
                if(p.getpID() == cred.getUser().getpID())
                {
                    actualUserIndex = cbOwner1.getItemCount();
                }
            }
            else
            {
                return b;
            }
        }
        cbOwner1.setSelectedIndex(actualUserIndex - 1);        
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
        Result<Record2<?,?>> result = (Result<Record2<?, ?>>) db.getContext().select(ID,BD).from("Persons").where( ostr + "BD = '" + cred.getBD() + "' AND active='Y' AND isOrserTec='Y'").orderBy(AP.asc(),N1.asc()).fetch();
        
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
    
    /**
     * Creates new form Create
     * @param cred
     * @param screen
     */
    public Create(session.Credential cred,Read screen)
    {
        initComponents();
        this.cred = cred;
        ord = new OrdenCrear(cred);
        this.screen = screen;
        
        @SuppressWarnings("UnusedAssignment")
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
        if(!db.valid())
        {
                JOptionPane.showMessageDialog(this,
                    "Conexion a Servidor Invalida",
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
        }
        Throwable th = null;
        /*try {
            th = fillManager(db,cred);
        } catch (SQLException ex) {
            Logger.getLogger(Create.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        if( th != null)
        {
            JOptionPane.showMessageDialog(this,
                    th.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        mecModel = new core.MecanicoComboBoxModel();
        try 
        {
            mecModel.fill(db, SIIL.servApp.cred.getOffice());
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Create.class.getName()).log(Level.SEVERE, null, ex);
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

        jLabel1 = new javax.swing.JLabel();
        jXLabel1 = new org.jdesktop.swingx.JXLabel();
        txFolio = new org.jdesktop.swingx.JXTextField();
        jLabel6 = new javax.swing.JLabel();
        txClient = new javax.swing.JTextField();
        btClient = new javax.swing.JButton();
        lbClient = new javax.swing.JLabel();
        jXLabel2 = new org.jdesktop.swingx.JXLabel();
        jXLabel3 = new org.jdesktop.swingx.JXLabel();
        btCrear = new org.jdesktop.swingx.JXButton();
        jXLabel4 = new org.jdesktop.swingx.JXLabel();
        cbOwner2 = new org.jdesktop.swingx.JXComboBox();
        cbOwner1 = new org.jdesktop.swingx.JXComboBox();
        cbTec = new org.jdesktop.swingx.JXComboBox();

        jLabel1.setText("jLabel1");

        setClosable(true);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jXLabel1.setText("Folio");

        jLabel6.setText("Cliente");

        txClient.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txClientKeyReleased(evt);
            }
        });

        btClient.setText("...");
        btClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btClientActionPerformed(evt);
            }
        });

        lbClient.setText("##");

        jXLabel2.setText("Encargado 1");

        jXLabel3.setText("Técnico");

        btCrear.setText("Crear");
        btCrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCrearActionPerformed(evt);
            }
        });

        jXLabel4.setText("Encargado 2");

        cbOwner2.setToolTipText("La persona que le dara seguimiento directo con el cliente");

        cbOwner1.setEditable(true);

        cbTec.setEditable(true);

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
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jXLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jXLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbOwner1, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(cbTec, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jXLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbOwner2, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btCrear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(126, 126, 126))
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
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jXLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbOwner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jXLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbOwner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jXLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbTec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btCrear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txClientKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txClientKeyReleased
        if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER)
        {
            SIIL.client.sales.Enterprise comp = new SIIL.client.sales.Enterprise();
            comp.setBD(cred.getBD());
            comp.setNumber(txClient.getText());
            ord.setCompany(comp);
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
                ord.setCompany(comp);
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
        ord.setCompany(comp);
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
    }//GEN-LAST:event_btClientActionPerformed

    private void btCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCrearActionPerformed
       Thread th = new Thread(this);
       th.start();       
    }//GEN-LAST:event_btCrearActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        if(evt.getClickCount() == 2)
        {
            btCrear.setEnabled(true);
        }
    }//GEN-LAST:event_formMouseClicked

    private boolean runCreate()
    {
        com.galaxies.andromeda.util.Progress progress;
        progress = SIIL.servApp.getInstance().getProgressObject();
        ((SIIL.servApp.Progress)SIIL.servApp.getInstance().getProgressObject()).On();
        
        progress.setProgress(1, "Iniciando actividad.");
        progress.setProgress(2, "Validando datos de insumo...");
        ord.setFolio(txFolio.getText().trim());        

        if (cbOwner1.getSelectedIndex() > 0) 
        {
            ord.setOwner((Person)cbOwner1.getSelectedItem());
        } 
        else 
        {
            JOptionPane.showMessageDialog(this,
                    "Indique el Encargado1 al menos",
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        if(cbOwner2.getSelectedIndex() > 0) 
        {
            ord.setOwner2((Person)cbOwner2.getSelectedItem());
        }
        
        if (cbTec.getSelectedIndex() > 0) 
        {
            ord.setTechnical((Person)cbTec.getSelectedItem());
        }
        else 
        {
            JOptionPane.showMessageDialog(this,
                    "Indique el Técnico",
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
            );
            //processing.dispose();
            return true;
        }
        ord.setCreator(cred.getUser());
        ord.setTrace(new Trace(cred.getBD(), cred.getUser(), "Creación de Documento - Cotización " + ord.getFolio()));
        progress.setProgress(30, "Validacion completa.");
        progress.setProgress(50, "Conectando a base de datos...");
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
        int duplicateFolio = 0;
        if(OrdenCrear.isExist(Integer.valueOf(txFolio.getText()),db))
        {
            duplicateFolio = JOptionPane.showConfirmDialog(
            this,
            "El Fólio '" + txFolio.getText() + "' ya existe, ¿Desea duplicar?",
            "Fólio duplicado",
            JOptionPane.YES_NO_OPTION);
        }
        if(duplicateFolio == JOptionPane.NO_OPTION)
        {
            progress.setProgress(0, "Operación cacelada por usuario(Folio duplicado).");
            return false;
        }
                
        if(ord.getCompany() == null)
        {
            JOptionPane.showMessageDialog(this,
                    "Cliente invalido",
                    "Error Exteno",
                    JOptionPane.ERROR_MESSAGE
            );
            db.close();
            return false;
        }
        //Validacion de entradas
        Exception vlEx = ord.valid(db);
        if( vlEx == null)
        {       
            ord.setServerDB(db);
            progress.setProgress(60, "Conexión a base de datos realizada.");
            //Procesando
            try
            {
                btCrear.setEnabled(false);//si empiesa la operacion no se puede repetir
                progress.setProgress(70, "Enviando Datos...");                
                ord.getTrace().insert(db);
                int flCreate = ord.create(db);
                if(flCreate == 1)
                {
                    progress.setProgress(80, "Enviando correo electrónico...");
                    State estado = new State(-1);
                    estado.select(db, State.Steps.CS_CREATED);
                    ord.setState(estado);
                    boolean flMail = ord.sendMail(db);
                    progress.setProgress(90, "Correo enviado.");
                    if(flMail)
                    {
                        progress.setProgress(100, "Finalizado ...");
                        db.commit();
                        JOptionPane.showMessageDialog(this,
                                "La operacón de completo satisfactoriamente",
                                "Confirmación de Operación",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        this.screen.reloadTable();
                        db.close();
                        dispose();
                        return true;
                    }
                    else
                    {
                        progress.setProgress(20, "Falló el envio de correo electronico.");
                        db.rollback();
                        JOptionPane.showMessageDialog(this,
                                "Falló el envio de correo electrónico, la operación será cancelada. Si el problema persiste consulte su administrador.",
                                "Error Interno",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(this,
                            "Falló la inserción de los datos en la base de datos, nada se guardara",
                            "Error Interno",
                            JOptionPane.ERROR_MESSAGE
                    );
                    db.rollback();
                }
            }
            catch(AddressException | SQLException ex)
            {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                        "Falló la inserción de los datos en la base de datos, nada se guardara.",
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
        else
        {
            JOptionPane.showMessageDialog(this,
                    vlEx.getMessage(),
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        //processing.dispose();
        btCrear.setEnabled(true);
        db.close();
        return false;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btClient;
    private org.jdesktop.swingx.JXButton btCrear;
    private org.jdesktop.swingx.JXComboBox cbOwner1;
    private org.jdesktop.swingx.JXComboBox cbOwner2;
    private org.jdesktop.swingx.JXComboBox cbTec;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel6;
    private org.jdesktop.swingx.JXLabel jXLabel1;
    private org.jdesktop.swingx.JXLabel jXLabel2;
    private org.jdesktop.swingx.JXLabel jXLabel3;
    private org.jdesktop.swingx.JXLabel jXLabel4;
    private javax.swing.JLabel lbClient;
    private javax.swing.JTextField txClient;
    private org.jdesktop.swingx.JXTextField txFolio;
    // End of variables declaration//GEN-END:variables
    
    @Override
    public void run() 
    {
        runCreate();
    }
}
