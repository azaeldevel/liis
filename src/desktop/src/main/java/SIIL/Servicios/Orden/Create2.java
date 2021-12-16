
package SIIL.Servicios.Orden;

import SIIL.CN.Sucursal;
import SIIL.CN.Tables.CN60;
import SIIL.CN.Tables.COTIZACI;
import SIIL.Server.Database;
import SIIL.Server.MySQL;
import SIIL.Server.Person;
import SIIL.artifact.AmbiguosException;
import SIIL.artifact.DeployException;
import SIIL.client.sales.Enterprise;
import SIIL.core.Office;
import SIIL.trace.Trace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.internet.AddressException;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.xml.parsers.ParserConfigurationException;
import org.jdesktop.swingx.JXDialog;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Result;
import org.xml.sax.SAXException;
import process.State;

/**
 *
 * @author Azael Reyes
 */
public class Create2 extends javax.swing.JPanel {

    private OrdenCrear ord;
    private session.Credential cred;
    private Read screen;
    private JXDialog dialog;
    private Database dbserver;
    private core.MecanicoComboBoxModel mecModel;
    private Person mechanic;
    private COTIZACI tbCotiza;
    private SIIL.CN.Records.COTIZACI recCotiza;
    private Office office;
    private core.OwnresComboBoxModel ownModel;
    private Person owner1;
    
    
    private void configOwners() 
    {
        cbOwner1.getEditor().getEditorComponent().addKeyListener(new java.awt.event.KeyAdapter()
        {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                //cbMechanic.setSelectedIndex(-1);
                String search = ((JTextField)cbOwner1.getEditor().getEditorComponent()).getText();
                try
                {         
                    String[] toks = search.split(" ");
                    if(toks.length < 1)
                    {
                        ownModel.search(dbserver,SIIL.servApp.cred.getOffice(),10,search);
                    }
                    else
                    {
                        ownModel.search(dbserver,SIIL.servApp.cred.getOffice(),10,toks);
                    }
                    
                    if(evt.getKeyCode() == KeyEvent.VK_ENTER)
                    {
                        if(ownModel.getSize() == 0)// si no se encontro alguna remision
                        {
                            JOptionPane.showMessageDialog(
                                null,
                                "No hay elemento selecionado",
                                "Error Interno",
                                JOptionPane.ERROR_MESSAGE
                            );
                            return;
                        }
                        else if(ownModel.getSize() == 1)
                        {
                            
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null,
                                "Hay " + ownModel.getSize() + " Elementos en la lista, seleccione uno.",
                                "Error Interno",
                                JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                    else if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && evt.isControlDown() == true)
                    {
                        search = ""; 
                    }
                    else if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && search.length() == 0)
                    {
                        ;
                    }
                    ((JTextField)cbOwner1.getEditor().getEditorComponent()).setText(search);
                    
                    cbOwner1.setModel(ownModel);
                    if(cbOwner1.getSelectedItem() != null)
                    {
                        owner1 = new Person();
                        owner1 = (Person) cbOwner1.getSelectedItem();
                        //owner1.download(dbserver);
                        office = owner1.getOffice();
                        office.download(dbserver.getConnection());
                    }
                }
                catch (SQLException ex)
                {
                    JOptionPane.showMessageDialog(null,
                        ex.getMessage(),
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
                
            }              
        });
        
        cbOwner1.getEditor().getEditorComponent().addFocusListener(new java.awt.event.FocusAdapter()
        {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt)
            {
                if(cbOwner1.getSelectedItem() != null)
                {
                    owner1 = new Person();
                    owner1 = (Person) cbOwner1.getSelectedItem();
                    //owner1.download(dbserver);
                    office = owner1.getOffice();
                    office.download(dbserver.getConnection());
                }
            }
        });
        
        cbOwner1.addActionListener (new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(cbOwner1.getSelectedIndex() > -1)
                {
                    owner1 = (Person) cbOwner1.getSelectedItem();
                    Boolean ret = false;
                }
                
                if(cbOwner1.getSelectedItem() != null)
                {
                    owner1 = new Person();
                    owner1 = (Person) cbOwner1.getSelectedItem();
                    //owner1.download(dbserver);
                    office = owner1.getOffice();
                    office.download(dbserver.getConnection());
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
    
    private void configMechanic() 
    {
        cbTec.getEditor().getEditorComponent().addKeyListener(new java.awt.event.KeyAdapter()
        {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                //cbMechanic.setSelectedIndex(-1);
                String search = ((JTextField)cbTec.getEditor().getEditorComponent()).getText();
                try
                {            
                    String[] toks = search.split(" ");
                    if(toks.length <1)
                    {
                        mecModel.search(dbserver,SIIL.servApp.cred.getOffice(),10,search);
                    }
                    else
                    {
                        mecModel.search(dbserver,SIIL.servApp.cred.getOffice(),10,toks);
                    }
                    if(evt.getKeyCode() == KeyEvent.VK_ENTER)
                    {
                        if(mecModel.getSize() == 0)// si no se encontro alguna remision
                        {
                                JOptionPane.showMessageDialog(
                                    null,
                                    "No hay elemento selecionado",
                                    "Error Interno",
                                    JOptionPane.ERROR_MESSAGE
                                );
                                return;
                        }
                        else if(mecModel.getSize() == 1)
                        {
                            
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null,
                                "Hay " + mecModel.getSize() + " Elementos en la lista, seleccione uno.",
                                "Error Interno",
                                JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                    else if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && evt.isControlDown() == true)
                    {
                        search = ""; 
                    }
                    else if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && search.length() == 0)
                    {
                        
                    }
                    ((JTextField)cbTec.getEditor().getEditorComponent()).setText(search);
                    cbTec.setModel(mecModel);
                }
                catch (SQLException ex)
                {
                    //Logger.getLogger(Relacion.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null,
                            ex.getMessage(),
                            "Error Interno",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
                
            }              
        });
        cbTec.getEditor().getEditorComponent().addFocusListener(new java.awt.event.FocusAdapter()
        {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt)
            {                  
                
            }
        });
        
        cbTec.addActionListener (new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(cbTec.getSelectedIndex() > -1 )
                {
                    mechanic = (Person) cbTec.getSelectedItem();
                    Boolean ret = false;
                }
            }
        });
        
    }
    
    private boolean runCreate()
    {
        com.galaxies.andromeda.util.Progress progress;
        progress = SIIL.servApp.getInstance().getProgressObject();
        ((SIIL.servApp.Progress)SIIL.servApp.getInstance().getProgressObject()).On();
        
        progress.setProgress(1, "Iniciando actividad.");
        progress.setProgress(2, "Validando datos de insumo...");
        getOrden().setFolio(txFolio.getText().trim());        

        if(cbOwner1.getSelectedIndex() > 0) 
        {
            getOrden().setOwner(owner1);
        }
        else 
        {
            JOptionPane.showMessageDialog(this,
                "Indique el Encargado al menos",
                "Error Externo",
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        
        if(cbOwner2.getSelectedIndex() > 0) 
        {
            getOrden().setOwner2((Person)cbOwner2.getSelectedItem());
        }
        
        if (cbTec.getSelectedIndex() > -1) 
        {
            getOrden().setTechnical((Person)cbTec.getSelectedItem());
        }
        else 
        {
            JOptionPane.showMessageDialog
            (
                this,
                "Indique el Técnico",
                "Error Externo",
                JOptionPane.ERROR_MESSAGE
            );
            
            return true;
        }
        getOrden().setCreator(cred.getUser());
        getOrden().setTrace(new Trace(cred.getBD(), cred.getUser(), "Creación de Documento - Cotización " + getOrden().getFolio()));
        progress.setProgress(30, "Validacion completa.");
        progress.setProgress(50, "Conectando a base de datos.");
        
        openDatabase(false);
        int duplicateFolio = 0;
        if(OrdenCrear.isExist(Integer.valueOf(txFolio.getText()),dbserver))
        {
            duplicateFolio = JOptionPane.showConfirmDialog
            (
                this,
                "El Fólio '" + txFolio.getText() + "' ya existe, ¿Desea duplicar?",
                "Fólio duplicado",
                JOptionPane.YES_NO_OPTION
            );
        }
        
        
        if(duplicateFolio == JOptionPane.NO_OPTION)
        {
            progress.setProgress(0, "Operación cacelada por usuario(Folio duplicado).");
            return false;
        }
        
        if(getOrden().getCompany() == null)
        {
            JOptionPane.showMessageDialog(this,
                    "Cliente invalido",
                    "Error Exteno",
                    JOptionPane.ERROR_MESSAGE
            );
            dbserver.close();
            return false;
        }
        
        //Validacion de entradas
        Exception vlEx = getOrden().valid(dbserver);
        if( vlEx == null)
        {       
            getOrden().setServerDB(dbserver);
            progress.setProgress(60, "Conexión a base de datos realizada.");
            //Procesando
            try
            {
                btCrear.setEnabled(false);//si empiesa la operacion no se puede repetir
                progress.setProgress(70, "Enviando Datos...");                
                getOrden().getTrace().insert(dbserver);
                int flCreate = getOrden().create(dbserver);
                if(flCreate == 1)
                {
                    progress.setProgress(80, "Enviando correo electrónico...");
                    State estado = new State(-1);
                    estado.select(dbserver, State.Steps.CS_CREATED);
                    getOrden().setState(estado);
                    boolean flMail = getOrden().sendMail(dbserver);
                    progress.setProgress(90, "Correo enviado.");
                    if(flMail)
                    {
                        progress.setProgress(100, "Finalizado.");
                        dbserver.commit();
                        JOptionPane.showMessageDialog
                        (
                            this,
                            "La operacón de completo satisfactoriamente",
                            "Confirmación de Operación",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        if(this.screen  != null) this.screen.reloadTable();
                        dbserver.close();
                        if(this.dialog != null ) this.dialog.dispose();
                        return true;
                    }
                    else
                    {
                        progress.setProgress(20, "Falló el envio de correo electronico.");
                        dbserver.rollback();
                        JOptionPane.showMessageDialog
                        (
                            this,
                            "Falló el envio de correo electrónico, la operación será cancelada. Si el problema persiste consulte su administrador.",
                            "Error Interno",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog
                    (
                        this,
                        "Falló la inserción de los datos en la base de datos, nada se guardara",
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                    );
                    dbserver.rollback();
                }
            }
            catch(AddressException | SQLException ex)
            {
                //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(
                    this,
                    "Falló la inserción de los datos en la base de datos, nada se guardara.",
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
        else
        {
            JOptionPane.showMessageDialog(
                this,
                vlEx.getMessage(),
                "Error Externo",
                JOptionPane.ERROR_MESSAGE
            );
        }
        //processing.dispose();
        btCrear.setEnabled(true);
        dbserver.close();
        return false;
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
        Result<Record2<?,?>> result = (Result<Record2<?, ?>>) db.getContext().select(ID,BD).from("Persons").where(ostr + " BD = '" + cred.getBD() + "' AND email IS NOT NULL AND active='Y' AND isOrserOwner='Y'").orderBy(AP.asc(),N1.asc()).fetch();
        
        Person p = new Person();
        p.setpID(-1);
        p.setAP("...");
        p.setN1("Seleccione");
        //cbOwner1.addItem(p);
        cbOwner2.addItem(p);
        int actualUserIndex = 0;
        for (Record r : result)
        {
            p = new Person();
            Throwable b = p.fill(db,(Integer)r.getValue(ID),(String)r.getValue(BD));
            if(b == null)
            {
                //cbOwner1.addItem(p);
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
     * Creates new form Create2
     */
    public Create2(session.Credential cred,Read screen)
    {
        initComponents();
        this.cred = cred;
        ord = new OrdenCrear(cred);
        this.screen = screen;
        
        openDatabase(true);
        
        Throwable th = null;
        try 
        {
            th = fillManager(dbserver,cred);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(Create2.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
            );
        }
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
            mecModel.fill(dbserver, SIIL.servApp.cred.getOffice());
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(SIIL.services.trabajo.Create.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        cbTec.setModel(mecModel);
        configMechanic();
                
        ownModel = new core.OwnresComboBoxModel();
        try 
        {
            ownModel.fill(dbserver, null);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(Create2.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        cbOwner1.setModel(ownModel);
        configOwners();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btCrear = new org.jdesktop.swingx.JXButton();
        jXLabel4 = new org.jdesktop.swingx.JXLabel();
        cbOwner2 = new org.jdesktop.swingx.JXComboBox();
        jXLabel1 = new org.jdesktop.swingx.JXLabel();
        txFolio = new org.jdesktop.swingx.JXTextField();
        lbClient = new javax.swing.JLabel();
        jXLabel2 = new org.jdesktop.swingx.JXLabel();
        jXLabel3 = new org.jdesktop.swingx.JXLabel();
        cbTec = new org.jdesktop.swingx.JXComboBox();
        cbOwner1 = new org.jdesktop.swingx.JXComboBox();

        btCrear.setText("Crear");
        btCrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCrearActionPerformed(evt);
            }
        });

        jXLabel4.setText("Encargado 2");

        cbOwner2.setToolTipText("La persona que le dara seguimiento directo con el cliente");

        jXLabel1.setText("Folio");

        txFolio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txFolioActionPerformed(evt);
            }
        });

        lbClient.setText("##");

        jXLabel2.setText("Encargado 1");

        jXLabel3.setText("Técnico");

        cbTec.setEditable(true);

        cbOwner1.setToolTipText("La persona que le dara seguimiento directo con el cliente");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jXLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbOwner2, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                .addComponent(cbTec, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jXLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txFolio, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lbClient, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(89, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(213, Short.MAX_VALUE)
                .addComponent(btCrear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(188, 188, 188))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jXLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbOwner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jXLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txFolio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbClient))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbOwner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jXLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jXLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbTec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(btCrear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCrearActionPerformed
        runCreate();
    }//GEN-LAST:event_btCrearActionPerformed

    private void txFolioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txFolioActionPerformed
        if(txFolio.getText().length() > 0)
        {
            tbCotiza = null;
            Office office = ((Person)cbOwner1.getSelectedItem()).getOffice();            
            try 
            {
                switch (office.getCode()) 
                {
                    case "bc.tj":
                        tbCotiza = new COTIZACI(Sucursal.BC_Tijuana);
                        break;
                    case "bc.mx":
                        tbCotiza = new COTIZACI(Sucursal.BC_Mexicali);
                        break;
                    case "bc.ens":
                        tbCotiza = new COTIZACI(Sucursal.BC_Ensenada);
                        break;
                    default:
                        JOptionPane.showMessageDialog(null,
                        "Base de datos deconocida",
                        "Error Exteno",
                        JOptionPane.ERROR_MESSAGE
                        );
                        return;
                }
            }
            catch (IOException ex) 
            {
                JOptionPane.showMessageDialog(null,
                        ex.getMessage(),
                        "Error Exteno",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            
            recCotiza = null;
            try 
            {
                recCotiza = tbCotiza.readWhere(Integer.valueOf(txFolio.getText()));
                getOrden().importFromCN(dbserver, tbCotiza, office);
            }
            catch (SQLException | IOException ex) 
            {
                JOptionPane.showMessageDialog
                (
                    null,
                    ex.getMessage(),
                    "Error Exteno",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            
            if(recCotiza == null)
            {
                JOptionPane.showMessageDialog(
                    this,
                    "El Folio no existe, primero cree la cotizacion en el CN60",
                    "Error Exteno",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            else
            {
                String str = recCotiza.getClientNumber();
                Enterprise enterprise = new Enterprise(-1);
                enterprise.setBD("bc.tj");
                enterprise.setNumber(str);
                enterprise.complete(dbserver);
                getOrden().setCompany(enterprise);
                lbClient.setText(enterprise.getName());
            }
        }
    }//GEN-LAST:event_txFolioActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jdesktop.swingx.JXButton btCrear;
    private org.jdesktop.swingx.JXComboBox cbOwner1;
    private org.jdesktop.swingx.JXComboBox cbOwner2;
    private org.jdesktop.swingx.JXComboBox cbTec;
    private org.jdesktop.swingx.JXLabel jXLabel1;
    private org.jdesktop.swingx.JXLabel jXLabel2;
    private org.jdesktop.swingx.JXLabel jXLabel3;
    private org.jdesktop.swingx.JXLabel jXLabel4;
    private javax.swing.JLabel lbClient;
    private org.jdesktop.swingx.JXTextField txFolio;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the ord
     */
    public OrdenCrear getOrden() {
        return ord;
    }
    
    /**
     * @param dialog the dialog to set
     */
    public void setDialog(JXDialog dialog) {
        this.dialog = dialog;
    }
}
