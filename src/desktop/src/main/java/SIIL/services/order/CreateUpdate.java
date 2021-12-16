
package SIIL.services.order;

import SIIL.CN.Tables.CN60;
import SIIL.Server.Company;
import SIIL.Server.Database;
import SIIL.Server.Person;
import SIIL.service.quotation.ServiceQuotation;
import static SIIL.servApp.cred;
import static SIIL.servApp.BACKWARD_BD;
import SIIL.services.order.Order.Type;
import SIIL.services.trabajo.Create;
import SIIL.trace.Trace;
import core.DefaultFocusTraversalPolicy;
import core.Dialog;
import core.Folio;
import core.IntegerInputVerifier;
import core.MecanicoComboBoxModel;
import core.bobeda.Archivo;
import core.bobeda.FTP;
import database.mysql.sales.Remision;
import database.mysql.stock.Item;
import database.mysql.stock.Titem;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.xml.parsers.ParserConfigurationException;
import org.jdesktop.swingx.JXDialog;
import org.xml.sax.SAXException;
import process.Return;
import stock.Flow;

/**
 * 
 * @author Azael Reyes
 */
public class CreateUpdate extends javax.swing.JPanel implements core.DialogContent
{
    public enum Mode
    {
        CREATE,
        UPDATE,
        DETAIL,
        CREATE_DETAIL,
    }
    private Company company;
    private Flow itemFlow;
    private Order.Type type;
    private SIIL.service.quotation.ServiceQuotation quoteService;
    private Date fechaService;
    private final JInternalFrame contenerdor;
    private Mode mode;
    private Resumen resumen;
    private javax.swing.JPanel read;
    //private Integer sa;
    private core.MecanicoComboBoxModel mecModel;
    private Person mechanic;
    private Database dbserver;
    private Order orden;
    private Font editedField;
    private Remision sa;
    private SIIL.core.config.Server serverConfig;
    private core.Dialog dialog;
    private Order orderCreated;

    

    /**
     * @return the order
     */
    public Order getCreated() {
        return orderCreated;
    }
    
    @Override
    public void setDialog(Dialog dialog) 
    {
        this.dialog = dialog;
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
        
    private void closeDatabase() 
    {
        if(dbserver != null)
        {
            dbserver.close();
            dbserver = null;
        }
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
                                mechanic = (Person) cbMechanic.getSelectedItem();
                                orden.upMechanic(dbserver, mechanic, null);
                                lbHoro.setFont(editedField);
                            }
                            else if(mode == Mode.CREATE || mode == Mode.CREATE_DETAIL) 
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
                            mechanic = null;
                            orden.upMechanic(dbserver, mechanic, null);
                            lbHoro.setFont(editedField);
                        }
                        else if(mode == Mode.CREATE || mode == Mode.CREATE_DETAIL)
                        {
                            mechanic = null;
                        }
                    }
                    else if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && search.length() == 0)
                    {
                        if(mode == Mode.UPDATE)
                        {
                            mechanic = null;
                            orden.upMechanic(dbserver, mechanic, null);
                            lbHoro.setFont(editedField);
                        }
                        else if(mode == Mode.CREATE || mode == Mode.CREATE_DETAIL)
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
                if(mode == Mode.UPDATE) 
                {
                    cbMechanic.showPopup();
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
                    if(mode == Mode.UPDATE)
                    {
                        try 
                        {
                            orden.upMechanic(dbserver, mechanic, null);
                            lbMechanical.setFont(editedField);
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
            }
        });
        
    }
    public CreateUpdate(JInternalFrame contenerdor,Mode mode,javax.swing.JPanel read,Order orden)
    {
        initComponents();
        initComponents2();
        this.contenerdor = contenerdor;
        this.orden = orden;
        this.read = read;
        openDatabase(true);
        if(mode == Mode.DETAIL)
        {
            enabledControls(false);
            download(orden, dbserver);            
            updateControls(orden, dbserver);
            this.mode = mode;
        }
        else if(mode == Mode.CREATE || mode == Mode.CREATE_DETAIL)
        {
            enabledControls(true); 
            MecanicoComboBoxModel cbModel = (MecanicoComboBoxModel) cbMechanic.getModel();
            try 
            {
                cbModel.fill(dbserver,SIIL.servApp.cred.getOffice());
            }
            catch (SQLException ex) 
            {
                Logger.getLogger(CreateUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.mode = mode;
        }
        else if(mode == Mode.UPDATE)
        {
            enabledControls(true);
            MecanicoComboBoxModel cbModel = (MecanicoComboBoxModel) cbMechanic.getModel();
            try 
            {
                cbModel.fill(dbserver,SIIL.servApp.cred.getOffice());
            }
            catch (SQLException ex) 
            {
                Logger.getLogger(CreateUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
            download(this.orden , dbserver);            
            updateControls(this.orden , dbserver);
            this.mode = mode;
        }
    }
    
    public CreateUpdate(JInternalFrame contenerdor,Mode mode,Resumen resumen,javax.swing.JPanel read,Database database)
    {
        initComponents();
        initComponents2();
        this.contenerdor = contenerdor;
        this.resumen = resumen;
        if(resumen != null) this.orden = resumen.getOrder();
        dbserver = database;
        this.read = read;
        if(mode == Mode.DETAIL)
        {
            enabledControls(false);
            download(resumen.getOrder(), database);            
            updateControls(resumen.getOrder(), database);
            this.mode = mode;
        }
        else if(mode == Mode.CREATE || mode == Mode.CREATE_DETAIL)
        {
            enabledControls(true); 
            btView.setEnabled(false);
            MecanicoComboBoxModel cbModel = (MecanicoComboBoxModel) cbMechanic.getModel();
            try 
            {
                cbModel.fill(database,SIIL.servApp.cred.getOffice());
            }
            catch (SQLException ex) 
            {
                Logger.getLogger(CreateUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.mode = mode;
        }
    }

    private void initComponents2() 
    {
        editedField = new Font("Tahoma", java.awt.Font.BOLD, 11);
        if(dbserver == null)
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
                    "Fallo importacion.",
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }
        mecModel = new core.MecanicoComboBoxModel();
        try 
        {
            mecModel.fill(dbserver, SIIL.servApp.cred.getOffice());
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Create.class.getName()).log(Level.SEVERE, null, ex);
        }
        cbMechanic.setModel(mecModel);
        orderFocusTraversalPolicy();
        groupingOptions();
        configMechanic();
    }

    private void orderFocusTraversalPolicy() {
        ArrayList<Component> comps = new ArrayList<>();
        comps.add(txClient);
        comps.add(txQuoteService);
        comps.add(txFolio);
        comps.add(txTitem);
        comps.add(opC);
        comps.add(opP);
        comps.add(txHorometro);
        comps.add(cbMechanic);
        comps.add(fecha);
        comps.add(txDescription);
        DefaultFocusTraversalPolicy policy = new DefaultFocusTraversalPolicy(comps);
        setFocusTraversalPolicy(policy);
        setFocusTraversalPolicyProvider(true);
        Set forwardKeys = getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
        Set newForwardKeys = new HashSet(forwardKeys);
        newForwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0));
        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
            newForwardKeys);
            }

    private void updateControls(Order ordr, Database database) 
    {
        lbClient.setText(ordr.getCompany().getName());
        txClient.setText(ordr.getCompany().getNumber());
        if(ordr.getQuoteService() != null)
        {
            txQuoteService.setText(ordr.getQuoteService().getFullFolio());            
        }
        lbTitem.setText(ordr.getItemFlow().getItem().getDescription());
        txTitem.setText(ordr.getItemFlow().getItem().getNumber());
        if (ordr.getType() == Order.Type.CORRECTIVE) 
        {
            opC.setSelected(true);
        }
        else if (ordr.getType() == Order.Type.PREVENTIVE) 
        {
            opP.setSelected(true);
        }
        else 
        {
            ;
        }
        fecha.setDate(ordr.getFhService());
        txHorometro.setText(Integer.toString(ordr.getHorometro()));
        txDescription.setText(ordr.getDescripcion());
        if(ordr.getTechnical() != null) mecModel.select(ordr.getTechnical());
        txFolio.setText(String.valueOf(ordr.getFolio()));
        txSA.setText(Integer.toString(ordr.getSA()));
        if(ordr.getArchivo()  != null)
        {
            btView.setEnabled(true);
        }
        else
        {
            btView.setEnabled(false);
        }
    }

    private boolean download(Order order, Database database) throws HeadlessException 
    {
        try 
        {
            order.downCompany(database);
            order.getCompany().download(database);
            order.downDescription(database);
            order.downFhService(database);
            order.downHorometro(database);
            order.downType(database);
            order.downTechnical(database);
            order.getTechnical().fill(database, order.getTechnical().getpID(), cred.getBD());
            order.downFolio(database);
            order.downSA(database);
            order.downQuoteService(database, cred);
            order.downArchivo(database);
            if(order.getQuoteService() != null)
            {
                if(order.getQuoteService().downCompany(database))
                {
                    order.getQuoteService().getCompany().download(database);                    
                }
                order.getQuoteService().downSerie(database.getConnection());
                order.getQuoteService().downFolio(database.getConnection());
            }
            if(order.getItemFlow() == null)
            {
                if(order.downItemFlow(database))
                {
                    if(order.getItemFlow().downItem(database.getConnection()))
                    {
                        order.getItemFlow().getItem().downNumber(database.getConnection());
                        order.getItemFlow().getItem().downDescription(database.getConnection());
                    }
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
            return true;
            //Logger.getLogger(CreateUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private void enabledControls(boolean  flag) 
    {
        btAceptar.setEnabled(flag);
        btClient.setEnabled(flag);
        btQuoteService.setEnabled(flag);
        btTitem.setEnabled(flag);
        //txClient.setEnabled(flag);
        //txDescription.setEnabled(flag);
        //txQuoteService.setEnabled(flag);
        //txTitem.setEnabled(flag);
        //fecha.setEnabled(false);
        //opC.setEnabled(flag);
        //opP.setEnabled(flag);
        //cbMecanico.setEnabled(flag);  
    }
    
    public CreateUpdate(Database database, Mode mode) 
    {
        initComponents();
        initComponents2();
        this.contenerdor = null;
        this.mode = mode;
        MecanicoComboBoxModel cbModel = (MecanicoComboBoxModel) cbMechanic.getModel();
        try 
        {
            cbModel.fill(dbserver,SIIL.servApp.cred.getOffice());
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(CreateUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    /**
     * Creates new form Create
     * @param contenerdor
     * @param parent
     */
    public CreateUpdate(JInternalFrame contenerdor,Database database) 
    {
        initComponents();
        initComponents2();
        this.contenerdor = contenerdor;
        this.mode = Mode.CREATE;
        MecanicoComboBoxModel cbModel = (MecanicoComboBoxModel) cbMechanic.getModel();
        try 
        {
            cbModel.fill(dbserver,SIIL.servApp.cred.getOffice());
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(CreateUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }

    private void groupingOptions() 
    {
        gopTipoServ.add(opC);
        gopTipoServ.add(opP);
        gopMetodo.add(opGM);
        gopMetodo.add(opGS);
        gopMetodo.add(opServ);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        gopTipoServ = new javax.swing.ButtonGroup();
        gopMetodo = new javax.swing.ButtonGroup();
        lbFlow = new javax.swing.JLabel();
        btClient = new javax.swing.JButton();
        lbClient = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txClient = new javax.swing.JTextField();
        txTitem = new javax.swing.JTextField();
        btTitem = new org.jdesktop.swingx.JXButton();
        lbTitem = new org.jdesktop.swingx.JXLabel();
        opP = new javax.swing.JRadioButton();
        lbTypeService = new javax.swing.JLabel();
        opC = new javax.swing.JRadioButton();
        lbBrief = new org.jdesktop.swingx.JXLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txDescription = new javax.swing.JTextArea();
        btAceptar = new org.jdesktop.swingx.JXButton();
        lbHoro = new javax.swing.JLabel();
        fecha = new org.jdesktop.swingx.JXDatePicker();
        lbFecha = new javax.swing.JLabel();
        lbMechanical = new javax.swing.JLabel();
        lbFolio = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        opGM = new javax.swing.JRadioButton();
        opGS = new javax.swing.JRadioButton();
        opServ = new javax.swing.JRadioButton();
        txHorometro = new javax.swing.JTextField();
        txFolio = new javax.swing.JTextField();
        cbMechanic = new org.jdesktop.swingx.JXComboBox();
        lbQuoted = new javax.swing.JLabel();
        txQuoteService = new javax.swing.JTextField();
        btQuoteService = new org.jdesktop.swingx.JXButton();
        lbSA = new javax.swing.JLabel();
        txSA = new javax.swing.JTextField();
        btFill = new javax.swing.JButton();
        btView = new javax.swing.JButton();

        lbFlow.setText("Montacargas");

        btClient.setText("...");
        btClient.setToolTipText("Localizacion Fisica de la operacion.");
        btClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btClientActionPerformed(evt);
            }
        });

        lbClient.setText("##");

        jLabel6.setText("Cliente");

        txClient.setToolTipText("Especifica el cliente fisico para el que se realiza la operación el cual es direfente al cliente para el cual se factura.");
        txClient.setDisabledTextColor(new java.awt.Color(204, 204, 204));
        txClient.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txClientFocusGained(evt);
            }
        });
        txClient.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txClientKeyReleased(evt);
            }
        });

        txTitem.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txTitemFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txTitemFocusLost(evt);
            }
        });
        txTitem.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                txTitemComponentShown(evt);
            }
        });
        txTitem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txTitemKeyPressed(evt);
            }
        });

        btTitem.setText("...");
        btTitem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTitemActionPerformed(evt);
            }
        });

        lbTitem.setText("##");

        opP.setText("Preventivo");
        opP.setToolTipText("");
        opP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opPMouseClicked(evt);
            }
        });

        lbTypeService.setText("Tipo de Servicio");

        opC.setText("Correctivo");
        opC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opCMouseClicked(evt);
            }
        });

        lbBrief.setText("Descripción");

        txDescription.setColumns(20);
        txDescription.setRows(5);
        txDescription.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txDescriptionFocusGained(evt);
            }
        });
        txDescription.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txDescriptionKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(txDescription);

        btAceptar.setText("Guardar");
        btAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAceptarActionPerformed(evt);
            }
        });

        lbHoro.setText("Horómetro");
        lbHoro.setToolTipText("");

        fecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechaActionPerformed(evt);
            }
        });

        lbFecha.setText("Fecha de Servicio");

        lbMechanical.setText("Mécanico");

        lbFolio.setText("Folio");

        jLabel10.setText("Método de Servicio");
        jLabel10.setToolTipText("");

        opGM.setText("GM");

        opGS.setText("Garantia de Servicio");

        opServ.setText("Servicio");

        txHorometro.setInputVerifier(new IntegerInputVerifier(true));
        txHorometro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txHorometroFocusGained(evt);
            }
        });
        txHorometro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txHorometroKeyPressed(evt);
            }
        });

        txFolio.setInputVerifier(new IntegerInputVerifier(false));
        txFolio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txFolioFocusGained(evt);
            }
        });
        txFolio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txFolioKeyReleased(evt);
            }
        });

        cbMechanic.setEditable(true);

        lbQuoted.setText("Cotización de Servicio");

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

        btQuoteService.setText("...");
        btQuoteService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btQuoteServiceActionPerformed(evt);
            }
        });

        lbSA.setText("SA");

        txSA.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txSAKeyPressed(evt);
            }
        });

        btFill.setText("...");
        btFill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFillActionPerformed(evt);
            }
        });

        btView.setText("Ver");
        btView.setEnabled(false);
        btView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btViewActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(txClient, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(btClient)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbClient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(txFolio, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(68, 68, 68))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lbFlow)
                                        .addGap(18, 18, 18)
                                        .addComponent(txTitem, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(32, 32, 32)
                                        .addComponent(btTitem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addComponent(lbTitem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lbTypeService)
                                        .addGap(132, 132, 132)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel10)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(opGM)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(opGS)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(opServ))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(69, 69, 69)
                                        .addComponent(cbMechanic, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(opP)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(opC))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lbHoro)
                                            .addComponent(lbMechanical))
                                        .addGap(18, 18, 18)
                                        .addComponent(txHorometro))
                                    .addComponent(lbFolio))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 210, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbFecha)
                                    .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(165, 165, 165))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbBrief, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btView))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(325, 325, 325)
                                .addComponent(btAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lbQuoted)
                                .addGap(18, 18, 18)
                                .addComponent(txQuoteService, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btQuoteService, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbSA)
                .addGap(27, 27, 27)
                .addComponent(txSA, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(btFill)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbClient)
                    .addComponent(btClient, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txClient))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbQuoted)
                    .addComponent(btQuoteService, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txQuoteService, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbSA)
                    .addComponent(txSA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btFill))
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbFlow)
                    .addComponent(txTitem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btTitem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbTitem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(lbTypeService))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(opP)
                    .addComponent(opC)
                    .addComponent(opGM)
                    .addComponent(opGS)
                    .addComponent(opServ))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbHoro)
                    .addComponent(txHorometro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbMechanical)
                            .addComponent(cbMechanic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbFolio)
                            .addComponent(txFolio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbFecha)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbBrief, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btView))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btClientActionPerformed
        sales.clients.Read cl = new sales.clients.Read(sales.clients.ReadTableModel.Mode.SELECT);        
        JXDialog dlg = new JXDialog(cl);
        cl.setDialog(dlg);
        dlg.setContentPane(cl);
        dlg.setSize(cl.getPreferredSize());
        dlg.setModal(true);
        dlg.setVisible(true);
        Company[] comp = cl.getSelection();  
        if(comp.length == 1)
        {
            company = comp[0];
            lbClient.setText(company.getName());
            txClient.setText(company.getNumber());
        }
        else if(comp.length > 1)
        {
            JOptionPane.showMessageDialog(this,
                "Demasiados elementos seleccionados, solo se reconocie uno a la vez.",
                "Error Externo",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        else if(comp.length == 0)
        {
            JOptionPane.showMessageDialog(this,
                "Seleccion un montacargas para continuar",
                "Error Externo",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
    }//GEN-LAST:event_btClientActionPerformed

    private void txClientKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txClientKeyReleased
        if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER)
        {
            SIIL.client.sales.Enterprise comp = new SIIL.client.sales.Enterprise();
            comp.setBD(cred.getBD());
            comp.setNumber(txClient.getText());
            openDatabase(true);
            if(comp.complete(dbserver))
            {
                this.company = comp;
                lbClient.setText(comp.getName());
            }
            else
            {
                if(comp == null)
                {
                    JOptionPane.showMessageDialog(this, "Número de cliente desconocido",  "Error externo", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    comp=null;
                    lbClient.setText("###");
                }
            }
            if(mode == Mode.UPDATE)
            {
                try 
                {
                    orden.upEnterprise(dbserver, comp, null);
                } 
                catch (SQLException ex) 
                {
                    //Logger.getLogger(CreateUpdate.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                        );
                    return;
                }
            }
        }
    }//GEN-LAST:event_txClientKeyReleased

    private void btTitemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTitemActionPerformed
        stock.SearchHequi srh = new stock.SearchHequi(stock.SearchHequiTableModel.Mode.SelectForklift);        
        /*JXDialog dlg = new JXDialog(srh);
        srh.setDialog(dlg);
        dlg.setContentPane(srh);
        dlg.setSize(srh.getPreferredSize());
        dlg.setModal(true);
        dlg.setVisible(true);*/
        core.Dialog dlg = new core.Dialog(srh);
        dlg.setContent(srh);        
        Flow[] fork = srh.getSelection();  
        if(fork != null & fork.length == 1)
        {
            lbTitem.setText(fork[0].getItem().getDescription());
            txTitem.setText(fork[0].getItem().getNumber());
            itemFlow = (Flow) fork[0];
        }
        else if(fork.length > 1)
        {
            JOptionPane.showMessageDialog(this,
                "Demasiados elementos seleccionados, solo se reconocie uno a la vez.",
                "Error Externo",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        else if(fork.length == 0)
        {
            JOptionPane.showMessageDialog(this,
                "Seleccion un montacargas para continuar",
                "Error Externo",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
    }//GEN-LAST:event_btTitemActionPerformed

    private void btAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAceptarActionPerformed
        if(mode == Mode.CREATE || mode == Mode.CREATE_DETAIL)
        {
            openDatabase(true);
            Order order = new Order(-1);   
            Boolean retFL = false;
            String desString = null;
            Resumen resumen = null;
            Module module = new Module(1);//por lo pronto usar la clave GM Yale.
            if(Integer.parseInt(txHorometro.getText()) < 0)
            {
                JOptionPane.showMessageDialog(this,
                    "El horometro deve ser mayor un numero positivo.",
                    "Dato necesesario",
                    JOptionPane.ERROR_MESSAGE
                );   
                return; 
            }
            /*if(txSA.getText().length() > 0)
            {
                sa = Integer.parseInt(txSA.getText());
            }
            else
            {
                sa = null;
            }*/
            if(txDescription.getText().length() > 1)
            {
                desString = txDescription.getText();
            }
            else
            {
                desString = null;
            }
            if(opC.isSelected())
            {
                type = Order.Type.CORRECTIVE;
            }
            else if(opP.isSelected())
            {
                type = Order.Type.PREVENTIVE;
            }
            else
            {
                JOptionPane.showMessageDialog(this,
                    "El tipo de matenimiento es obligatorio necesaria",
                    "Dato necesesario",
                    JOptionPane.ERROR_MESSAGE
                );   
                return; 
            }
            if(fecha.getDate() != null)
            {
                fechaService = fecha.getDate();
            }
            else
            {
                JOptionPane.showMessageDialog(this,
                    "La Fecha es necesaria",
                    "Dato necesesario",
                    JOptionPane.ERROR_MESSAGE
                );   
                return; 
            }
            if(txFolio.getText().length() < 1)
            {
                JOptionPane.showMessageDialog(this,
                    "El folio es necesario",
                    "Dato necesesario",
                    JOptionPane.ERROR_MESSAGE
                );   
                return; 
            }
            else if(Integer.parseInt(txFolio.getText()) < 1)
            {
                JOptionPane.showMessageDialog(this,
                    "El folio es necesario",
                    "Dato necesesario",
                    JOptionPane.ERROR_MESSAGE
                );   
                return; 
            }
            if(cbMechanic.getSelectedItem() != null && ((Person)cbMechanic.getSelectedItem()).getpID() > 0)
            {
                ;
            }
            else
            {
                JOptionPane.showMessageDialog(this,
                    "El mecanico es necesario, indique uno por favor",
                    "Dato necesesario",
                    JOptionPane.ERROR_MESSAGE
                );   
                return; 
            }
            if(company == null)
            {
                JOptionPane.showMessageDialog(this,
                    "Deve indicar un cliente",
                    "Dato necesesario",
                    JOptionPane.ERROR_MESSAGE
                );   
                return;
            }
            else if(company.getID() < 1)
            {
                JOptionPane.showMessageDialog(this,
                    "Deve indicar un cliente",
                    "Dato necesesario",
                    JOptionPane.ERROR_MESSAGE
                );   
                return;
            }
            try 
            {
                Trace contexTrace = new Trace(BACKWARD_BD, cred.getUser(), "Captura de Orden de Servicio");
                if(opGM.isSelected())
                {
                    retFL = order.insert(dbserver,fechaService, itemFlow, type, Integer.parseInt(txHorometro.getText()), desString, company, quoteService, sa,SIIL.servApp.cred.getOffice(),Integer.parseInt(txFolio.getText()),(Person)cbMechanic.getSelectedItem(),contexTrace);
                    resumen = Resumen.select(dbserver,itemFlow);
                    if(resumen == null)
                    {
                        resumen = new Resumen(-1);
                        resumen.insert(dbserver, itemFlow, order, module,contexTrace);
                    }
                    else
                    {
                        resumen.upOrder(dbserver, order, true,contexTrace);
                    }
                }
                else if(opGS.isSelected() | opServ.isSelected())
                {
                    retFL = order.insert(dbserver,fechaService, itemFlow, type, Integer.parseInt(txHorometro.getText()), desString, company, quoteService, sa,SIIL.servApp.cred.getOffice(),Integer.parseInt(txFolio.getText()),(Person)cbMechanic.getSelectedItem(),contexTrace);
                }
                else
                {
                    JOptionPane.showMessageDialog(this,
                        "El metodo de servicon seleccionado no esta implemente",
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                orderCreated = order;
                orderCreated.download(dbserver);
                if(dialog != null) dialog.dispose();
            }
            catch (SQLException ex) 
            {
                JOptionPane.showMessageDialog(this,
                    "Falló la operacion de guardar(on insert)",
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                try 
                {
                    dbserver.rollback();
                } 
                catch (SQLException ex1) 
                {
                    //Logger.getLogger(CreateUpdate.class.getName()).log(Level.SEVERE, null, ex1);
                    JOptionPane.showMessageDialog(this,
                        "Falló la operacion de cancelar(on rollback)",
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
                return;
            }

            if(retFL)
            {
                try 
                {        
                    int optionPane = JOptionPane.showConfirmDialog(
                    this,
                    "Desea guardar los cambios",
                    "Confirmar operación",
                    JOptionPane.YES_NO_OPTION);
                    if(optionPane == JOptionPane.YES_OPTION)
                    {
                        if(mode == Mode.CREATE) 
                        {
                            dbserver.commit();
                            reloadReferencedView();
                            closeDatabase();
                        }
                        if(this.contenerdor != null) this.contenerdor.dispose();                    
                    }
                    else
                    {
                        dbserver.rollback();
                    }
                } 
                catch (SQLException ex) 
                {
                    //Logger.getLogger(CreateUpdate.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this,
                        "Falló la operacion de guardar(on commit)",
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            }
            else
            {
                JOptionPane.showMessageDialog(this,
                    "Falló la operacion de guardar(retu value false)",
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }
        else if(mode == Mode.UPDATE)
        {
            try 
            {
                dbserver.commit();
                reloadReferencedView();
                closeDatabase();
                if(this.contenerdor != null) this.contenerdor.dispose();
            }
            catch (SQLException ex) 
            {
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }
    }//GEN-LAST:event_btAceptarActionPerformed

    private void reloadReferencedView() {
        if(read != null)
        {
            if(read instanceof Historial)
            {
                ((Historial)read).reloadTable();
            }
            else if(read instanceof Read)
            {
                ((Read)read).reloadTable();
            }
        }
    }

    private void txDescriptionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txDescriptionFocusGained
        txDescription.selectAll();
    }//GEN-LAST:event_txDescriptionFocusGained

    private void txClientFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txClientFocusGained
        txClient.selectAll();
    }//GEN-LAST:event_txClientFocusGained

    private void txTitemComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_txTitemComponentShown
        txTitem.selectAll();
    }//GEN-LAST:event_txTitemComponentShown

    private void txTitemFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txTitemFocusGained
        txTitem.selectAll(); 
    }//GEN-LAST:event_txTitemFocusGained

    private void txHorometroFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txHorometroFocusGained
        txHorometro.selectAll();
    }//GEN-LAST:event_txHorometroFocusGained

    private void txFolioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txFolioFocusGained
        txFolio.selectAll();
    }//GEN-LAST:event_txFolioFocusGained

    private void txTitemFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txTitemFocusLost

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
            try 
            {
                int lastVal  = Order.lastHorometro(dbserver,itemFlow);
                txHorometro.setText(String.valueOf(lastVal));
            } 
            catch (SQLException ex) 
            {
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            dbserver.close();
    }//GEN-LAST:event_txTitemFocusLost

    private void txQuoteServiceFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txQuoteServiceFocusGained
        txQuoteService.selectAll();
    }//GEN-LAST:event_txQuoteServiceFocusGained

    private void txQuoteServiceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txQuoteServiceKeyPressed
        if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER)
        {
            openDatabase(true);
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
                return;
            }
            if(this.mode == Mode.UPDATE)
            {
                try
                {
                    orden.upQuotedService(dbserver, quoteService, null);
                    //quoteService.downFolio(dbserver.getConnection());
                    //quoteService.downSerie(dbserver.getConnection());
                    txQuoteService.setText(quoteService.getFullFolio()); 
                    lbQuoted.setFont(editedField);
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
        if(this.mode == Mode.CREATE || mode == Mode.CREATE_DETAIL)
        {
            if(quoteService != null)
            {
                txQuoteService.setText(quoteService.getFullFolio());
            }            
        }
        else if(this.mode == Mode.UPDATE && quoteService != null)
        {
            try 
            {
                orden.upQuotedService(dbserver, quoteService, null);
                txQuoteService.setText(quoteService.getFullFolio());
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(CreateUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btQuoteServiceActionPerformed

    private void txSAKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txSAKeyPressed
        if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER)
        {
            fillField();
        }
    }                               

    private void fillField()
    {
        openDatabase(true);
        java.util.List<Remision> ls = null;
        try 
        {
            ls = Remision.search(dbserver,SIIL.servApp.cred.getOffice(),txSA.getText(),3);
            if (ls.size() == 1) 
            {
                sa = ls.get(0);
                sa.downFolio(dbserver);
                sa.downSerie(dbserver);
                txSA.setText(sa.getFullFolio());
                if(this.company == null)
                {
                    if(sa.downCompany(dbserver))
                    {
                        this.company = sa.getEnterprise();
                        this.company.download(dbserver);
                    }
                }
                if(this.company != null)
                {
                    lbClient.setText(company.getName());
                    txClient.setText(company.getNumber());
                    //¿es el numero de cliente un equipo?
                    Flow flow = new Flow(-1);
                    if(flow.selectTitemNumber(dbserver, company.getNumber()))
                    {
                        if(flow.download(dbserver))
                        {
                            Item item = flow.getItem();
                            item.downNumber(dbserver.getConnection());
                            item.downDescription(dbserver.getConnection());
                            lbTitem.setText(item.getDescription());
                            txTitem.setText(item.getNumber());
                        }
                        else
                        {
                            lbTitem.setText("");
                            txTitem.setText("");                              
                        }
                        itemFlow = flow;     
                    }
                    else
                    {
                        lbTitem.setText("");
                        txTitem.setText("");
                    }
                }
            } 
            else if (txSA.getText().length() == 0) 
            {
                sa = null;
                lbTitem.setText("");
                txTitem.setText("");
            } 
            else if (ls.size() < 1) 
            {
                if(!CN60.isWorking())
                {
                    JOptionPane.showMessageDialog(
                        this,
                        "Se intenta buscar '" + txSA.getText() + "' en la base de datos de CN60 pero no esta disponible.",
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                sa = Remision.fromCN(dbserver,SIIL.servApp.cred.getOffice(),txSA.getText(),SIIL.servApp.cred.getUser());
                if(sa != null)
                {
                    sa.downFolio(dbserver);
                    sa.downSerie(dbserver);
                    txSA.setText(sa.getFullFolio()); 
                    if(this.company == null)
                    {
                        if(sa.downCompany(dbserver))
                        {
                            this.company = sa.getEnterprise();
                            this.company.download(dbserver);
                        }
                    }
                    if(this.company != null)
                    {
                        lbClient.setText(company.getName());
                        txClient.setText(company.getNumber());
                        //¿es el numero de cliente un equipo?
                        Flow flow = new Flow(-1);
                        if(flow.selectTitemNumber(dbserver, company.getNumber()))
                        {
                            if(flow.download(dbserver))
                            {
                                Item item = flow.getItem();
                                item.downNumber(dbserver.getConnection());
                                item.downDescription(dbserver.getConnection());
                                lbTitem.setText(item.getDescription());
                                txTitem.setText(item.getNumber());
                            }
                            else
                            {
                                lbTitem.setText("");
                                txTitem.setText("");                                    
                            }
                            itemFlow = flow;
                        }
                        else
                        {
                            lbTitem.setText("");
                            txTitem.setText("");                                    
                        }
                    }
                }
            } 
            else if (ls.size() > 1) 
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
                orden.upSA(dbserver, sa, null);
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
    }//GEN-LAST:event_txSAKeyPressed
    
    private void opPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opPMouseClicked
        if(this.mode == Mode.UPDATE)
        {
            try 
            {
                orden.upType(dbserver, Type.PREVENTIVE, null);
                lbTypeService.setFont(editedField);
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(CreateUpdate.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }//GEN-LAST:event_opPMouseClicked

    private void opCMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opCMouseClicked
        if(this.mode == Mode.UPDATE)
        {
            try 
            {
                orden.upType(dbserver, Type.CORRECTIVE, null);
                lbTypeService.setFont(editedField);
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(CreateUpdate.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }//GEN-LAST:event_opCMouseClicked

    private void txHorometroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txHorometroKeyPressed
        //if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER)
        {
            if(this.mode == Mode.UPDATE)
            {
                try 
                {
                    orden.upHorometro(dbserver, Integer.valueOf(txHorometro.getText()), null);
                    lbHoro.setFont(editedField);
                } 
                catch (SQLException ex) 
                {
                    //Logger.getLogger(CreateUpdate.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }//GEN-LAST:event_txHorometroKeyPressed

    private void fechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fechaActionPerformed
        if(this.mode == Mode.UPDATE)
        {
            try 
            {
                orden.upFecha(dbserver, fecha.getDate(), null);
                lbFecha.setFont(editedField);
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(CreateUpdate.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }//GEN-LAST:event_fechaActionPerformed

    private void txFolioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txFolioKeyReleased
            if(this.mode == Mode.UPDATE)
            {
                try 
                {
                    orden.upFolio(dbserver, Integer.valueOf(txFolio.getText()), null);
                    lbFolio.setFont(editedField);
                }
                catch (SQLException ex) 
                {
                    //Logger.getLogger(CreateUpdate.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
    }//GEN-LAST:event_txFolioKeyReleased

    private void txDescriptionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txDescriptionKeyReleased
        //if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER)
        {
            if(this.mode == Mode.UPDATE)
            {
                try 
                {
                    orden.upDescripcion(dbserver, txDescription.getText(), null);
                    lbBrief.setFont(editedField);
                } 
                catch (SQLException ex) 
                {
                    //Logger.getLogger(CreateUpdate.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }            
        }
    }//GEN-LAST:event_txDescriptionKeyReleased

    private void btViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btViewActionPerformed
        try 
        {
            openDatabase(true);
            
            FTP ftpServer = new FTP();
            boolean expResult = false;
            try 
            {
                expResult = ftpServer.connect(serverConfig);
            } 
            catch (IOException ex) 
            {
                //Logger.getLogger(FTPTest.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error externo",
                JOptionPane.ERROR_MESSAGE
                );
            }
            File into = new File(System.getProperty("java.io.tmpdir"));
            Archivo archivo = orden.getArchivo();
            if(!archivo.download(dbserver,ftpServer,into))
            {
                JOptionPane.showMessageDialog(this,
                "Falló la descarga de archivo.",
                "Error externo",
                JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            archivo.getDownloadedFile().close();
            Desktop.getDesktop().open(new File(orden.getArchivo().getDownloadFileName()));
        } 
        catch (SQLException | IOException ex) 
        {
            //Logger.getLogger(CreateUpdate.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                            ex.getMessage(),
                            "Error Interno",
                            JOptionPane.ERROR_MESSAGE
                    );
        }        
    }//GEN-LAST:event_btViewActionPerformed

    private void txTitemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txTitemKeyPressed
        if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER)
        {
            try 
            {
                openDatabase(true);
                itemFlow = new Flow(-1);
                if(itemFlow.selectTitemNumber(dbserver, txTitem.getText()))                        
                {
                    itemFlow.downItem(dbserver.getConnection());
                    itemFlow.getItem().downDescription(dbserver.getConnection());
                    lbTitem.setText(itemFlow.getItem().getDescription());
                }
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(CreateUpdate.class.getName()).log(Level.SEVERE, null, ex);
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
                        orden.upFlow(dbserver, itemFlow, null);
                        lbFlow.setFont(editedField);
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
    }//GEN-LAST:event_txTitemKeyPressed

    private void btFillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFillActionPerformed
        fillField();
    }//GEN-LAST:event_btFillActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jdesktop.swingx.JXButton btAceptar;
    private javax.swing.JButton btClient;
    private javax.swing.JButton btFill;
    private org.jdesktop.swingx.JXButton btQuoteService;
    private org.jdesktop.swingx.JXButton btTitem;
    private javax.swing.JButton btView;
    private org.jdesktop.swingx.JXComboBox cbMechanic;
    private org.jdesktop.swingx.JXDatePicker fecha;
    private javax.swing.ButtonGroup gopMetodo;
    private javax.swing.ButtonGroup gopTipoServ;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private org.jdesktop.swingx.JXLabel lbBrief;
    private javax.swing.JLabel lbClient;
    private javax.swing.JLabel lbFecha;
    private javax.swing.JLabel lbFlow;
    private javax.swing.JLabel lbFolio;
    private javax.swing.JLabel lbHoro;
    private javax.swing.JLabel lbMechanical;
    private javax.swing.JLabel lbQuoted;
    private javax.swing.JLabel lbSA;
    private org.jdesktop.swingx.JXLabel lbTitem;
    private javax.swing.JLabel lbTypeService;
    private javax.swing.JRadioButton opC;
    private javax.swing.JRadioButton opGM;
    private javax.swing.JRadioButton opGS;
    private javax.swing.JRadioButton opP;
    private javax.swing.JRadioButton opServ;
    private javax.swing.JTextField txClient;
    private javax.swing.JTextArea txDescription;
    private javax.swing.JTextField txFolio;
    private javax.swing.JTextField txHorometro;
    private javax.swing.JTextField txQuoteService;
    private javax.swing.JTextField txSA;
    private javax.swing.JTextField txTitem;
    // End of variables declaration//GEN-END:variables
}
