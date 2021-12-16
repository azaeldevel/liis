
package SIIL.services.trabajo;

import SIIL.Server.Database;
import SIIL.Server.Person;
import SIIL.client.sales.Enterprise;
import SIIL.service.quotation.ServiceQuotation;
import SIIL.core.config.Server;
import static SIIL.servApp.BACKWARD_BD;
import static SIIL.servApp.cred;
import SIIL.services.Trabajo;
import SIIL.trace.Trace;
import core.Dialog;
import core.Folio;
import database.mysql.sales.Remision;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.xml.parsers.ParserConfigurationException;
import org.jdesktop.swingx.JXDialog;
import org.xml.sax.SAXException;
import process.State;

/**
 *
 * @author Azael Reyes
 */
public class Create extends javax.swing.JPanel implements core.DialogContent
{
    private Font editedField;
    private final Trabajo.Sheet sheet;
    
    /**
     * @return the updated
     */
    public boolean isUpdated() 
    {
        return updated;
    }

    @Override
    public void setDialog(Dialog dialog) 
    {
        this.dialog = dialog;
    }

    private void enableControls(boolean active) 
    {
        txClient.setEditable(active);
        btClient.setEnabled(active);
        txQuoteService.setEditable(active);
        btQuoteService.setEnabled(active);
    }

    /**
     * @return the commitUpdate
     */
    public boolean isCommitUpdate() 
    {
        return commitUpdate;
    }

    private void configMechanic() 
    {
        cbMechanic.getEditor().getEditorComponent().addKeyListener(new java.awt.event.KeyAdapter()
        {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                //cbMechanic.setSelectedIndex(-1);
                String search = ((JTextField)cbMechanic.getEditor().getEditorComponent()).getText();
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
                            if(mode == Mode.UPDATE)
                            {
                                Boolean ret = activeAsigned(dbserver,trabajo,mecModel.getElementAt(0),contexTrace);
                                trabajo.upFhUpdate(dbserver, dbserver.getTimestamp(), contexTrace);
                                if(ret == null)
                                {
                                    updated = true;
                                }
                                else if(ret == true)
                                {
                                    updated = true;
                                }
                                else
                                {
                                    updated = false;
                                } 
                            }
                            else if(mode == Mode.CREATE)
                            {
                                mechanic = (Person) cbMechanic.getSelectedItem();
                            }
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
                        if(mode == Mode.UPDATE)
                        {
                            Boolean ret = activeAsigned(dbserver,trabajo,null,contexTrace);
                            trabajo.upFhUpdate(dbserver, dbserver.getTimestamp(), contexTrace);
                            if(ret == null)
                            {
                                updated = true;
                            }
                            else if(ret == true)
                            {
                                updated = true;
                            }
                            else
                            {
                                updated = false;
                            } 
                        }
                        else if(mode == Mode.CREATE)
                        {
                            mechanic = null;
                        }
                    }
                    else if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && search.length() == 0)
                    {
                        if(mode == Mode.UPDATE)
                        {
                            Boolean ret = activeAsigned(dbserver,trabajo,null,contexTrace);
                            trabajo.upFhUpdate(dbserver, dbserver.getTimestamp(), contexTrace);
                            if(ret == null)
                            {
                                updated = true;
                            }
                            else if(ret == true)
                            {
                                updated = true;
                            }
                            else
                            {
                                updated = false;
                            }
                        }
                        else if(mode == Mode.CREATE)
                        {
                            mechanic = null;
                        }
                    }
                    ((JTextField)cbMechanic.getEditor().getEditorComponent()).setText(search);
                    cbMechanic.setModel(mecModel);
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
        cbMechanic.getEditor().getEditorComponent().addFocusListener(new java.awt.event.FocusAdapter()
        {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt)
            {                 
                if(mode == Mode.UPDATE) cbMechanic.showPopup();  
                if(trabajo != null)
                {
                    if(trabajo.getMechanic() != null)
                    {
                        ((JTextField)cbMechanic.getEditor().getEditorComponent()).setText(trabajo.getMechanic().toString());
                    }
                }
            }
        });
        
        cbMechanic.addActionListener (new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(cbMechanic.getSelectedIndex() > -1 )
                {
                    mechanic = (Person) cbMechanic.getSelectedItem();
                    Boolean ret = false;
                    try
                    {
                        if(mode == Mode.UPDATE)
                        {
                            ret = activeAsigned(dbserver,trabajo,mechanic,contexTrace);
                            trabajo.upFhUpdate(dbserver, dbserver.getTimestamp(), contexTrace);
                            if(ret == null)
                            {
                                updated = true;
                            }
                            else if(ret == true)
                            {
                                updated = true;
                            }
                            else
                            {
                                updated = false;
                            }
                        }
                        else if(mode == Mode.CREATE)
                        {
                            //mechanic = (Person) cbMechanic.getSelectedItem();
                        }
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
            }
        });
        
    }
    public enum Mode
    {
        CREATE,
        READ,
        UPDATE
    }
    private JXDialog dialog;
    private Relacion relacion;
    private Mode mode;
    private Enterprise comp;
    private Trabajo trabajo;
    private ServiceQuotation quoteService;
    private Database dbserver;
    private boolean updated;
    private Trace contexTrace;
    private boolean commitUpdate;
    private Remision sa;
    private core.MecanicoComboBoxModel mecModel;
    private Person mechanic;
    
    

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
    
    
    private void updateControls(Trabajo trabajo) 
    {
        if(trabajo.getSheet() == Trabajo.Sheet.CAMPO)
        {
            opCampo.setSelected(true);
        }
        else if(trabajo.getSheet() == Trabajo.Sheet.TALLER)
        {
            opTaller.setSelected(true);
        }
        
        if(trabajo.getCompany() != null)
        {
            comp = trabajo.getCompany();
            lbClient.setText(trabajo.getCompany().getName());
            txClient.setText(trabajo.getCompany().getNumber());
        }        
        
        if(trabajo.getQuotedService() != null)
        {
            quoteService = trabajo.getQuotedService();
            txQuoteService.setText(trabajo.getQuotedService().getFullFolio());
        }
        if(trabajo.getSA() != null)
        {
            sa = trabajo.getSA();
            txSA.setText(sa.getFullFolio());
        }
        
        if(trabajo.getMechanic() != null)
        {
            cbMechanic.setSelectedItem(trabajo.getMechanic());
        }
        if(trabajo.getBrief() != null)
        {
            if(trabajo.getBrief().length() > 0)
            {
                txaComment.setText(trabajo.getBrief());
            }
        }
        if(trabajo.getFhToDo() != null)
        {
            fecha.setDate(trabajo.getFhToDo());
        }
        enableControls(false);
    }
    
    /**
     * Creates new form Create
     * @param mode
     * @param trabajo
     * @param relacion
     * @param dbserver
     * @param sheet
     */
    public Create(Mode mode, Trabajo trabajo, Relacion relacion, Database dbserver, Trabajo.Sheet sheet) 
    {
        initComponents();
        editedField = new Font("Tahoma", java.awt.Font.BOLD, 11);
        mecModel = new core.MecanicoComboBoxModel();
        if(dbserver == null)
        {
            openDatabase(true);
        }
        else
        {
            this.dbserver = dbserver;
        }
        try 
        {
            mecModel.fill(this.dbserver, SIIL.servApp.cred.getOffice());
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Create.class.getName()).log(Level.SEVERE, null, ex);
        }
        cbMechanic.setModel(mecModel);
        gopSheet.add(opCampo);
        gopSheet.add(opTaller);
        gopSheet.add(opGru);
        gopSheet.add(opManto);       
        this.mode = mode;
        this.trabajo = trabajo;
        this.fecha.setDate(new Date());
        this.relacion = relacion;
        configMechanic();
        if(this.mode == Mode.CREATE)
        {
            opCampo.setSelected(true);
            btAceptar.setEnabled(true);   
        }
        else if(this.mode == Mode.READ)
        {
            btAceptar.setEnabled(false);
            updateControls(trabajo);
        }
        else if(this.mode == Mode.UPDATE)
        {
            btAceptar.setEnabled(true);            
            contexTrace = new Trace(BACKWARD_BD, SIIL.servApp.cred.getUser(), "Modificación de Registro");
            updateControls(trabajo);
            enableControls(true);
        }
            
        selectSheet(sheet);
        this.sheet = sheet; 
        
        if(cred.acces(this.dbserver,"services.rl.write"))
        {
            btAceptar.setEnabled(true);
        }
        else
        {
            btAceptar.setEnabled(false);
        }
    }

    private void selectSheet(Trabajo.Sheet sheet1) 
    {
        if (sheet1 == Trabajo.Sheet.CAMPO) 
        {
            opCampo.setSelected(true);
        }
        else if (sheet1 == Trabajo.Sheet.GRUA) 
        {
            opGru.setSelected(true);
        }
        else if (sheet1 == Trabajo.Sheet.MTTO) 
        {
            opManto.setSelected(true);
        }
        else if (sheet1 == Trabajo.Sheet.TALLER) 
        {
            opTaller.setSelected(true);
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

        gopSheet = new javax.swing.ButtonGroup();
        tagSheet = new javax.swing.JLabel();
        opCampo = new javax.swing.JRadioButton();
        opTaller = new javax.swing.JRadioButton();
        lbClient = new javax.swing.JLabel();
        btClient = new javax.swing.JButton();
        lbTagClient = new javax.swing.JLabel();
        txClient = new javax.swing.JTextField();
        txQuoteService = new javax.swing.JTextField();
        lbQuoted = new javax.swing.JLabel();
        btQuoteService = new org.jdesktop.swingx.JXButton();
        btAceptar = new javax.swing.JButton();
        lbSA = new javax.swing.JLabel();
        txSA = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        fecha = new org.jdesktop.swingx.JXDatePicker();
        lbFecha = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txaComment = new javax.swing.JTextArea();
        lbBrief = new javax.swing.JLabel();
        opGru = new javax.swing.JRadioButton();
        opManto = new javax.swing.JRadioButton();
        cbMechanic = new org.jdesktop.swingx.JXComboBox();
        jLabel8 = new javax.swing.JLabel();

        tagSheet.setText("Hoja*");

        opCampo.setText("Campo");
        opCampo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opCampoMouseClicked(evt);
            }
        });

        opTaller.setText("Taller");
        opTaller.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opTallerMouseClicked(evt);
            }
        });

        lbClient.setText("##");

        btClient.setText("...");
        btClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btClientActionPerformed(evt);
            }
        });

        lbTagClient.setText("Cliente*");

        txClient.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txClientKeyReleased(evt);
            }
        });

        txQuoteService.setToolTipText("");
        txQuoteService.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txQuoteServiceFocusGained(evt);
            }
        });
        txQuoteService.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txQuoteServiceKeyPressed(evt);
            }
        });

        lbQuoted.setText("Cotización de Servicio");

        btQuoteService.setText("...");
        btQuoteService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btQuoteServiceActionPerformed(evt);
            }
        });

        btAceptar.setText("Aceptar");
        btAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAceptarActionPerformed(evt);
            }
        });

        lbSA.setText("SA");

        txSA.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txSAKeyPressed(evt);
            }
        });

        jButton2.setText("...");
        jButton2.setEnabled(false);

        fecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechaActionPerformed(evt);
            }
        });

        lbFecha.setText("Fecha");

        txaComment.setColumns(20);
        txaComment.setRows(5);
        txaComment.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txaCommentKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txaCommentKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(txaComment);

        lbBrief.setText("Descripción");

        opGru.setText("Grua");
        opGru.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opGruMouseClicked(evt);
            }
        });

        opManto.setText("Manto.");
        opManto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opMantoMouseClicked(evt);
            }
        });

        cbMechanic.setEditable(true);

        jLabel8.setText("Mecánico");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tagSheet)
                                .addGap(18, 18, 18)
                                .addComponent(opCampo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(opTaller)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(opGru)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(opManto)
                                .addGap(82, 82, 82)
                                .addComponent(lbFecha)
                                .addGap(18, 18, 18)
                                .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lbBrief)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)
                                .addComponent(cbMechanic, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(240, 240, 240))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbSA)
                                .addGap(27, 27, 27)
                                .addComponent(txSA, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)
                                .addComponent(jButton2))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbQuoted)
                                .addGap(18, 18, 18)
                                .addComponent(txQuoteService, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btQuoteService, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbTagClient, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txClient, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btClient, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lbClient, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 56, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(264, 264, 264)
                .addComponent(btAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tagSheet)
                    .addComponent(opCampo)
                    .addComponent(opTaller)
                    .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbFecha)
                    .addComponent(opGru)
                    .addComponent(opManto))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btClient)
                        .addComponent(lbClient))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txClient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbTagClient)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbQuoted)
                    .addComponent(btQuoteService, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txQuoteService, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbSA)
                    .addComponent(txSA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbMechanic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addComponent(lbBrief)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(btAceptar)
                .addContainerGap(34, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btClientActionPerformed
        comp = new Enterprise();
        JFrame frm = new JFrame();
        JDialog dlg = new JDialog(frm,"Seleccionar cliente",true);
        
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
        
        SIIL.Clientes.ReadSelect read = new SIIL.Clientes.ReadSelect(comp,SIIL.servApp.cred.getBD());
        dlg.setContentPane(read);
        dlg.setSize(460, 260);
        dlg.setVisible(true);
        txClient.setText(comp.getNumber());
        lbClient.setText(comp.getName());
        comp.complete(dbserver);
        dbserver.close();        
        if(this.mode == Mode.UPDATE)
        {
            try 
            {
                this.updated = true;
                this.trabajo.upClient(dbserver, comp, contexTrace);
                this.trabajo.upFhUpdate(dbserver, dbserver.getTimestamp(), contexTrace);
                lbTagClient.setFont(editedField);
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(Create.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }//GEN-LAST:event_btClientActionPerformed

    private void txClientKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txClientKeyReleased
        if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER)
        {
            comp = new Enterprise();
            comp.setBD(SIIL.servApp.cred.getBD());
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
                    comp = null;
                    lbClient.setText("###");
                }
            }
            dbserver.close();
            if(this.mode == Mode.UPDATE)
            {
                try 
                {
                    updated = true;
                    trabajo.upClient(dbserver, comp, contexTrace);
                    trabajo.upFhUpdate(dbserver, dbserver.getTimestamp(), contexTrace);
                    lbTagClient.setFont(editedField);
                }
                catch (SQLException ex) 
                {
                    //Logger.getLogger(Create.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }//GEN-LAST:event_txClientKeyReleased

    private void txQuoteServiceFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txQuoteServiceFocusGained
        txQuoteService.selectAll();
    }//GEN-LAST:event_txQuoteServiceFocusGained

    private void btQuoteServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btQuoteServiceActionPerformed
        SIIL.Servicios.Orden.Read read = new SIIL.Servicios.Orden.Read(SIIL.servApp.cred,SIIL.Servicios.Orden.Read.Mode.SELECTION_ALMACEN,SIIL.servApp.getInstance().getDesktopPane());
        JXDialog dlg = new JXDialog(read);
        dlg.setSize(read.getPreferredSize());
        read.setDialog(dlg);
        dlg.setContentPane(read);
        int x = SIIL.servApp.getInstance().getDesktopPane().getSize().width/2 - dlg.getSize().width/2;
        int y = 10;
        dlg.setLocation(x, y);
        dlg.setLocationRelativeTo(null);
        dlg.setModal(true);
        dlg.setVisible(true);
        quoteService = read.getSelection();
        if(quoteService != null)
        {
            txQuoteService.setText(quoteService.getFolio().toString());
            lbQuoted.setFont(editedField);
        }
    }//GEN-LAST:event_btQuoteServiceActionPerformed

    private void btAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAceptarActionPerformed
        if(this.mode == Mode.CREATE)
        {
            Trabajo.Sheet sheet = null;
            if(opCampo.isSelected())
            {
                sheet = Trabajo.Sheet.CAMPO;
            }
            else if(opTaller.isSelected())
            {
                sheet = Trabajo.Sheet.TALLER;
            }
            else if(opGru.isSelected())
            {
                sheet = Trabajo.Sheet.GRUA;
            }
            else if(opManto.isSelected())
            {
                sheet = Trabajo.Sheet.MTTO;
            }
            else
            {
                JOptionPane.showMessageDialog(this,
                    "Selecione la hoja.",
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            if(comp == null)
            {
                JOptionPane.showMessageDialog(this,
                        "Seelcione un cliente.",
                        "Error Externo",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            else if(comp.getID() < 1)
            {
                JOptionPane.showMessageDialog(this,
                        "Seelcione un cliente.",
                        "Error Externo",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            String comment = null;
            if(txaComment.getText().length() > 0)
            {
                comment = txaComment.getText();
            }
            Trabajo trb = null;
            try 
            {
                Trace contexTrace = new Trace(BACKWARD_BD, SIIL.servApp.cred.getUser(), "Creacion de Trabajo");
                contexTrace.insert(dbserver);                
                contexTrace.addOperator(dbserver, "¿Quien hace?");
                trb = Trabajo.create(dbserver, sheet, comp, quoteService,fecha.getDate(),comment,SIIL.servApp.cred.getUser(),contexTrace);
                if(sa != null)
                {
                    trb.upSA(dbserver, sa, contexTrace);
                }
                if(mechanic != null)
                {
                    activeAsigned(dbserver, trb, mechanic, contexTrace);
                }
                trb.upFhUpdate(dbserver, dbserver.getTimestamp(), contexTrace);
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(Create.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if(trb != null)
            {
                try 
                {
                    dbserver.commit();
                    dbserver.close();
                    dbserver = null;
                    JOptionPane.showMessageDialog(
                        this,
                        "Registro creado",
                        "Confirmacion",
                        JOptionPane.INFORMATION_MESSAGE
                    );  
                    if(relacion != null) relacion.reload(RelacionTableModel.ReadMode.RELOAD,false);
                    if(dialog != null) dialog.dispose();
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
                try 
                {
                    dbserver.rollback();
                    dbserver.close();
                    dbserver = null;
                }
                catch (SQLException ex) 
                {
                    //Logger.getLogger(Create.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
        else
        {
            endUpdate();
        }
    }//GEN-LAST:event_btAceptarActionPerformed

    private void endUpdate() 
    {
        commitUpdate = true;
        if(dialog != null) dialog.dispose();
    }

    private void opCampoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opCampoMouseClicked
        if(this.mode == Mode.UPDATE)
        {
            try 
            {
                this.updated = true;
                this.trabajo.upSheet(dbserver, Trabajo.Sheet.CAMPO, contexTrace);
                this.trabajo.upFhUpdate(dbserver, dbserver.getTimestamp(), contexTrace);
                tagSheet.setFont(editedField);
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(Create.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }//GEN-LAST:event_opCampoMouseClicked

    private void opTallerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opTallerMouseClicked
        if(this.mode == Mode.UPDATE)
        {
            try 
            {
                this.updated = true;
                this.trabajo.upSheet(dbserver, Trabajo.Sheet.TALLER, contexTrace);
                this.trabajo.upFhUpdate(dbserver, dbserver.getTimestamp(), contexTrace);
                tagSheet.setFont(editedField);
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(Create.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }//GEN-LAST:event_opTallerMouseClicked

    private void opGruMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opGruMouseClicked
        if(this.mode == Mode.UPDATE)
        {
            try 
            {
                this.updated = true;
                this.trabajo.upSheet(dbserver, Trabajo.Sheet.GRUA, contexTrace);
                this.trabajo.upFhUpdate(dbserver, dbserver.getTimestamp(), contexTrace);
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(Create.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }//GEN-LAST:event_opGruMouseClicked

    private void opMantoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opMantoMouseClicked
        if(this.mode == Mode.UPDATE)
        {
            try 
            {
                this.updated = true;
                this.trabajo.upSheet(dbserver, Trabajo.Sheet.MTTO, contexTrace);
                this.trabajo.upFhUpdate(dbserver, dbserver.getTimestamp(), contexTrace);
                tagSheet.setFont(editedField);
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(Create.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }//GEN-LAST:event_opMantoMouseClicked

    private void txQuoteServiceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txQuoteServiceKeyPressed
            if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER)
            {
                java.util.List<ServiceQuotation> lis = null;
                try 
                {
                    lis = ServiceQuotation.searchByFolio(dbserver, SIIL.servApp.cred.getOffice(), new Folio(txQuoteService.getText()), 3);
                    if(lis.size() == 1)
                    {
                        quoteService = lis.get(0);
                        quoteService.downFolio(dbserver.getConnection());
                        quoteService.downSerie(dbserver.getConnection());
                        txQuoteService.setText(quoteService.getFullFolio());
                    }
                    else if(txQuoteService.getText().length() == 0)
                    {
                        quoteService = null;
                    }
                    else if(lis.size() > 1)
                    {
                        JOptionPane.showMessageDialog(this,
                        "Hay " + lis.size() + " coincidencias",
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                    else if(lis.size() < 1)
                    {
                        JOptionPane.showMessageDialog(this,
                        "No hay coincidencias",
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                } 
                catch (SQLException ex) 
                {
                    //Logger.getLogger(Create.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                    );
                }
                if(this.mode == Mode.UPDATE)
                {
                    try 
                    {
                        this.updated = true;
                        this.trabajo.upQuotedService(dbserver, quoteService, contexTrace);
                        lbQuoted.setFont(editedField);
                        this.trabajo.upFhUpdate(dbserver, dbserver.getTimestamp(), contexTrace);
                    }
                    catch (SQLException ex) 
                    {
                        //Logger.getLogger(Create.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(this,
                            ex.getMessage(),
                            "Error Interno",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        
    }//GEN-LAST:event_txQuoteServiceKeyPressed

    private void txSAKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txSAKeyPressed
            if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER)
            {
                java.util.List<Remision> ls = null;                
                try 
                {
                    ls = Remision.search(dbserver,SIIL.servApp.cred.getOffice(),txSA.getText(),3);
                    if(ls.size() == 1)
                    {
                        sa = ls.get(0);
                        sa.downFolio(dbserver);
                        sa.downSerie(dbserver);
                        txSA.setText(sa.getFullFolio());
                    }
                    else if(txSA.getText().length() == 0)
                    {
                        sa = null;
                    }
                    else if(ls.size() < 1)
                    {
                        sa = Remision.fromCN(dbserver,SIIL.servApp.cred.getOffice(),txSA.getText(),SIIL.servApp.cred.getUser());
                        sa.downFolio(dbserver);
                        sa.downSerie(dbserver);
                        txSA.setText(sa.getFullFolio());
                    }
                    else if(ls.size() > 1)
                    {
                        JOptionPane.showMessageDialog(this,
                        "Hay " + ls.size() + " coincidencias",
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                } 
                catch (ParseException | IOException | SQLException ex) 
                {
                    //Logger.getLogger(Create.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                    );
                }
                if(this.mode == Mode.UPDATE)
                {
                    try 
                    {
                        this.updated = true;
                        this.trabajo.upSA(dbserver, sa, contexTrace);
                        this.trabajo.upFhUpdate(dbserver, dbserver.getTimestamp(), contexTrace);
                        lbSA.setFont(editedField);
                    }
                    catch (SQLException ex) 
                    {
                        //Logger.getLogger(Create.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(this,
                            ex.getMessage(),
                            "Error Interno",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }        
    }//GEN-LAST:event_txSAKeyPressed

    private void fechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fechaActionPerformed
        if(mode == Mode.UPDATE)
        {
            try 
            {
                Boolean ret = trabajo.upfhToDo(dbserver, fecha.getDate(), contexTrace);
                this.trabajo.upFhUpdate(dbserver, dbserver.getTimestamp(), contexTrace);
                lbFecha.setFont(editedField);
                if(ret == null)
                {
                    updated = false;
                }
                else if(ret == true)
                {
                    updated = true;
                }
                else
                {
                    updated = false;
                }
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(Create.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }//GEN-LAST:event_fechaActionPerformed

    private void txaCommentKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txaCommentKeyPressed
        
    }//GEN-LAST:event_txaCommentKeyPressed

    private void txaCommentKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txaCommentKeyReleased
        if(mode == Mode.UPDATE)
        {
            lbBrief.setFont(editedField);//sombreado de campo editado
            try 
            {
                //if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER && evt.isControlDown() == true )//
                {
                    Boolean ret = trabajo.upBrief(dbserver, txaComment.getText(), null);  
                    this.trabajo.upFhUpdate(dbserver, dbserver.getTimestamp(), contexTrace);
                    //lbBrief.setFont(getFont());//fuente regular para campo guardado.
                    if(ret == null)
                    {
                        updated = false;
                    }
                    else if(ret == true)
                    {
                        updated = true;
                    }
                    else
                    {
                        updated = false;
                    }
                }
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(Create.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }//GEN-LAST:event_txaCommentKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAceptar;
    private javax.swing.JButton btClient;
    private org.jdesktop.swingx.JXButton btQuoteService;
    private org.jdesktop.swingx.JXComboBox cbMechanic;
    private org.jdesktop.swingx.JXDatePicker fecha;
    private javax.swing.ButtonGroup gopSheet;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbBrief;
    private javax.swing.JLabel lbClient;
    private javax.swing.JLabel lbFecha;
    private javax.swing.JLabel lbQuoted;
    private javax.swing.JLabel lbSA;
    private javax.swing.JLabel lbTagClient;
    private javax.swing.JRadioButton opCampo;
    private javax.swing.JRadioButton opGru;
    private javax.swing.JRadioButton opManto;
    private javax.swing.JRadioButton opTaller;
    private javax.swing.JLabel tagSheet;
    private javax.swing.JTextField txClient;
    private javax.swing.JTextField txQuoteService;
    private javax.swing.JTextField txSA;
    private javax.swing.JTextArea txaComment;
    // End of variables declaration//GEN-END:variables


    /**
     * @param relacion the relacion to set
     */
    public void setRelacion(Relacion relacion) {
        this.relacion = relacion;
    }
    

    private Boolean activeAsigned(Database db, Trabajo trabajo, Person mechanic,Trace contexTrace) throws SQLException
    {
        State state;
        boolean retM = false, retS = false;
        state = new State(-1);
        
        //Trace contexTrace = new Trace(BACKWARD_BD, SIIL.servApp.cred.getUser(), "Asignacion de SA");
        
        if(state != null) 
        {
            retM = trabajo.upMechanic(db,mechanic,contexTrace); 
            
            if(mechanic != null)
            {
                state.select(db, State.Steps.RT_ASIGNED);
                retS = trabajo.upState(db,state,contexTrace);
            }
            else
            {
                state.select(db, State.Steps.RT_ACCEPTED);
                retS = trabajo.upState(db,state,contexTrace);
            }
        }
        return (retM & retS);             
    }
    
}
