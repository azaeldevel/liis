
package SIIL.Servicios.Orden;

import SIIL.Server.Database;
import SIIL.Server.MySQL;
import SIIL.artifact.AmbiguosException;
import SIIL.artifact.DeployException;
import SIIL.client.Configuration;
import SIIL.client.Department;
import SIIL.client.sales.Enterprise;
import SIIL.service.quotation.ServiceQuotation;
import SIIL.service.quotation.OrdenAlmacenada;
import SIIL.service.quotation.OrdenAutorizar;
import SIIL.service.quotation.OrdenCancelar;
import SIIL.service.quotation.OrdenCotizada;
import SIIL.service.quotation.OrdenETA;
import SIIL.service.quotation.OrdenFinalizar;
import SIIL.service.quotation.OrdenTransito;
import SIIL.service.quotation.OrdenUpdate;
import SIIL.core.Office;
import SIIL.core.config.Server;
import static SIIL.servApp.BACKWARD_BD;
import SIIL.services.Trabajo;
import SIIL.services.Trabajo.Sheet;
import SIIL.trace.Trace;
import com.galaxies.andromeda.util.Texting.Confirmation;
import core.FailResultOperationException;
import core.bobeda.Archivo;
import core.bobeda.Business;
import core.bobeda.FTP;
import core.bobeda.Table;
import core.bobeda.Upload;
import core.bobeda.Vault;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.parsers.ParserConfigurationException;
import org.jdesktop.swingx.JXDialog;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.xml.sax.SAXException;
import process.Return;
import process.State;
import session.Credential;

/**
 *
 * @author Azael Reyes
 */
public class Read extends javax.swing.JPanel implements com.galaxies.andromeda.util.Upgradeable
{
    private static final int DEFAULT_LENGHT_LIST = 50; 
    private static final int DEFAULT_LENGHT_SEARCH_LIST = 10; 
    
    @Override
    public void clean() 
    {
        ;
    }

    @Override
    public void load() 
    {
        ;
    }

    @Override
    public void reload() 
    {
        reloadTable();
    }
    
    public enum Mode
    {
        LIST,
        SELECTION_AUTHO,
        SELECTION_ETA,
        SELECTION_ALMACEN
    }
    
    session.Credential cred;
    private boolean dataValid;
    private ListTask uatoReload;
    private Mode mode;
    private ServiceQuotation selection;
    private JXDialog dialog;
    private JDesktopPane desktopPane;
    
    
    private boolean orderEnd(ServiceQuotation o) 
    {
        SIIL.servApp.Progress progress = (SIIL.servApp.Progress) SIIL.servApp.getInstance().getProgressObject();
        ((SIIL.servApp.Progress)SIIL.servApp.getInstance().getProgressObject()).On();
        progress.setProgress(1, "Iniciando actividad.");
        progress.setProgress(2, "Validando datos de insumo...");
        OrdenFinalizar ord;
        if(o instanceof OrdenFinalizar)
        {
            ord = (OrdenFinalizar)o;
        }
        else
        {
            ord = new OrdenFinalizar(o);
        }
        int n = JOptionPane.showOptionDialog(this,
                "Está terminando el Documento '" + ord.getFolio() + "'. ¿Desea continuar?" ,
                "Confirme Operación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,null,null);
        int res = 0;
        if (n == JOptionPane.NO_OPTION) {
            progress.clean();
            return true;
        }
        progress.setProgress(10, "Conectando a Base de Datos...");
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
        //Si ya esta en transito
        if (ord.getState().getOrdinal() > 3 && ord.getState().getOrdinal() < 6) 
        {
            int opSel = JOptionPane.showOptionDialog(this,
                    "El Documento '" + ord.getFolio() + "' está como '" + ord.getState().getName() + "' implica que ya se realizo la compra, ¿Desea continuar con la operación?" ,
                    "Confirmación de solicitud",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,null,null,null);
            if (opSel == JOptionPane.NO_OPTION) {
                return true;
            }
        }
        ord.setServerDB(db);
        ord.setProgressObject(SIIL.servApp.getInstance().getProgressObject());
        ((SIIL.servApp.Progress)ord.getProgressObject()).setView(this);
        dataValid = false;
        Thread th = new Thread(ord);
        th.start();
        return false;
    }
    
    private boolean orderSurtir(ServiceQuotation o) throws HeadlessException {
        SIIL.servApp.Progress progress = (SIIL.servApp.Progress) SIIL.servApp.getInstance().getProgressObject();
        ((SIIL.servApp.Progress)SIIL.servApp.getInstance().getProgressObject()).On();
        progress.setProgress(1, "Iniciando actividad.");
        progress.setProgress(2, "Validando datos de insumo...");
        OrdenAlmacenada ord;
        if (o instanceof OrdenAlmacenada) {
            ord = (OrdenAlmacenada)o;
        } else {
            JOptionPane.showMessageDialog(this,
                    "La operación no se aplica para el registro seleccionado.",
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
            );
            progress.clean();
            return true;
        }
        String sa = "";
        sa = (String)JOptionPane.showInputDialog(
                this,
                "Introduzca el S.A.",
                "Captura de S.A.",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "");
        if (!sa.matches("^[0-9]+$")) {
            JOptionPane.showMessageDialog(this,
                    "El formato de del SA es incorrecto solo se aceptan números",
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
            );
            progress.clean();
            return true;
        }
        ord.setSA(sa);
        int n = JOptionPane.showOptionDialog(this,
                "Se va asignar el SA '" + sa + "' al Documento '" + ord.getFolio().toString() + "'. ¿Desea continuar?" ,
                "Confirme Operación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,null,null);
        int res = 0;
        if (n == JOptionPane.NO_OPTION) {
            ord.setSA(null);
            progress.clean();
            return true;
        }
        progress.setProgress(10, "Conectando a Base de Datos...");
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
        ord.setServerDB(db);
        ord.setProgressObject(SIIL.servApp.getInstance().getProgressObject());
        ((SIIL.servApp.Progress)ord.getProgressObject()).setView(this);
        dataValid = false;
        Thread th = new Thread(ord);
        th.start();
        return false;
    }
    
    private boolean orderTransito(ServiceQuotation o) throws HeadlessException 
    {
        SIIL.servApp.Progress progress = (SIIL.servApp.Progress) SIIL.servApp.getInstance().getProgressObject();
        ((SIIL.servApp.Progress)SIIL.servApp.getInstance().getProgressObject()).On();
        progress.setProgress(1, "Iniciando actividad.");
        progress.setProgress(2, "Validando datos de insumo...");
        OrdenTransito ord;
        if (o instanceof OrdenTransito) {
            ord = (OrdenTransito)o;
        } else {
            JOptionPane.showMessageDialog(this,
                    "La operación no se aplica para el registro seleccionado.",
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
            );
            progress.clean();
            return true;
        }
        int n = JOptionPane.showOptionDialog(this,
                "Está confirmando el Documento '" + ord.getFolio() + "'. ¿Desea continuar?" ,
                "Confirme operación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,null,null);
        int res = 0;
        if (n == JOptionPane.NO_OPTION) {
            progress.clean();
            return true;
        }
        progress.setProgress(10, "Conectando a Base de Datos...");
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
        ord.setServerDB(db);
        ord.setProgressObject(SIIL.servApp.getInstance().getProgressObject());
        ((SIIL.servApp.Progress)ord.getProgressObject()).setView(this);
        dataValid = false;
        Thread th = new Thread(ord);
        th.start();
        return false;
    }
    
    /**
     * Creates new form Screen
     * @param cred
     * @param mode
     * @param desktopPane
     */
    public Read(session.Credential cred,Mode mode,JDesktopPane desktopPane) 
    {
        initComponents();
        this.desktopPane = desktopPane;
        this.mode = mode;
        this.cred = cred;
        dataValid = true;
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
        loadCombo(db);
        String search = null;
        String lenght = null;
        buildTable(db, cred, lenght, search,mode); 
        tbList.requestFocus();
        enableAcces(); 
        updateMenuList(db);      
    }
    
    public Throwable fillModel(Database db,session.Credential cred, int length, String search,OrdenRootNode root,boolean  pers,Office offi,Department dep,Mode mode) 
    {
        String sql = "SELECT code,ordinal FROM ProcessStates ";
        //>>RQEXSR015: filtra solo la lista de ETA
        if(mode == Mode.SELECTION_AUTHO)
        {
            sql += " WHERE code = 'docpen' AND module = 6";
        } 
        else if(mode == Mode.SELECTION_ETA)
        {//si esta en modo de mostrar el ETA 
            sql += " WHERE code = 'pedpen' AND module = 6";
        }
        else if(mode == Mode.SELECTION_ALMACEN)
        {
            sql += " WHERE code = 'pedArrb' AND module = 6";
        }
        else
        {
            sql += " WHERE module = 6";
        }
        
        //<<RQEXSR015
        sql += " ORDER BY ordinal ASC";
        try 
        {
            java.sql.Statement pstmt = db.getConnection().createStatement();
            ResultSet rs = pstmt.executeQuery(sql);            
            Throwable ret = null;
            while(rs.next())
            {
                State edo = new State(-1);
                String code = rs.getString(1);
                ret = edo.fill(db,code);
                if(ret != null) return ret;
                //>>RQEXSR015: filtra solo la lista de ETA
                if(mode == Mode.LIST)
                {
                    //check if enable list
                    Configuration conf = new Configuration();
                    conf.setBD(cred.getBD());
                    conf.setOffice(cred.getSuc());
                    conf.setUser(cred.getUser());
                    conf.setObject("SIIL.Servicios.Orden.Screen.List");
                    conf.setAttribute(edo.getCode());
                    conf.setValue("enable");
                    try 
                    {
                        if(!conf.check(db) && !(search != null & length > 0)) 
                        {
                            continue;
                        }
                    }
                    catch (SQLException ex) 
                    {
                        //Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                        return ex;
                    }       
                }
                ServiceQuotation ord = new ServiceQuotation();
                ord.setState(edo);
                OrdenChildNode chlNode = new OrdenChildNode(ord);
                root.add(chlNode);
                ret = fillModelByEstado(db,cred,length,search,edo,chlNode,pers,offi,dep);
                if(ret != null) return ret;            
            }        
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
        }
        return null;
    }
            
    private Throwable fillModelByEstado(Database db, Credential cred, int lengh, String search, State status,OrdenChildNode parent,boolean  pers,Office offi,Department dep) 
    {        
        String sql = "SELECT ID FROM Orcom_Resolved";
        
        Where clause = new Where(db, status, cred);
        if(search != null)
        {
            clause.setQuick(lengh, search);
        }
        //Deshabilitada para no restrigier el acceso a las viejas.
        clause.setFilter(pers,dep,offi);        
        //System.out.println("Orden Where " + clause.getString());
        
        try
        {
            String clString = clause.getString();
        }        
        catch(Exception ex) 
        {
            JOptionPane.showMessageDialog(this,
                "Falló la generación de la clausula WHERE: " + ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            //Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            //return ex;
        }
        
        //where = select.where(clause.getString());        
        //Result<Record5<?, ?, ?, ?, ?>> result = where.orderBy(ID.desc()).limit(lengh).offset(0).fetch();//limit(lengh).fetch()  
        //Asignado clausula where
        sql += " WHERE " + clause.getString();
        //Ordendan por fecha de operación. Se usa la fecha de la operacion anterior para ordenar.
        if(status.getStep() == State.Steps.CS_CREATED)
        {
            sql += " ORDER BY fhFolio DESC ";
        }
        else if(status.getStep() == State.Steps.CS_QUOTED)
        {
            sql += " ORDER BY fhEdit DESC ";                
        }
        else if(status.getStep() == State.Steps.CS_ETA)
        {
            sql += " ORDER BY fhAutho DESC ";
        }
        else if(status.getStep() == State.Steps.CS_TRANS)
        {
            sql += " ORDER BY fhETAfl DESC ";
        }
        else if(status.getStep() == State.Steps.CS_ALMACEN)
        {
            sql += " ORDER BY fhArribo DESC";
        }
        else if(status.getStep() == State.Steps.CS_ENTREGANDO)
        {
            sql += " ORDER BY fhSurtido DESC ";
        }
        else
        {
            sql += " ORDER BY ID DESC ";
        }
        //limit clause
        sql += " LIMIT " + lengh + " OFFSET 0 ";
        ResultSet rs = null;
        try 
        {
            java.sql.Statement stmt = db.getConnection().createStatement();
            //System.out.println(sql);
            rs = stmt.executeQuery(sql);
            Throwable ret;
            while(rs.next()) 
            {
                ServiceQuotation orden;
                if(status.getStep() == State.Steps.CS_CREATED)
                {
                    orden = new OrdenCotizada();
                }
                else if(status.getStep() == State.Steps.CS_QUOTED)
                {
                    orden = new OrdenAutorizar();           
                }
                else if(status.getStep() == State.Steps.CS_ETA)
                {
                    orden = new OrdenETA();
                }
                else if(status.getStep() == State.Steps.CS_TRANS)
                {
                    orden = new OrdenTransito();
                }
                else if(status.getStep() == State.Steps.CS_ALMACEN)
                {
                    orden = new OrdenAlmacenada();
                }
                else if(status.getStep() == State.Steps.CS_ENTREGANDO)
                {
                    orden = new OrdenFinalizar();
                }
                else
                {
                    orden = new ServiceQuotation();
                }

                orden.setState(status);
                OrdenChildNode ordChild = new OrdenChildNode(orden);
                ret = orden.fill(db,cred,rs.getInt(1));
                if(ret != null) return ret;
                parent.add((MutableTreeTableNode) ordChild); 
            }
        }
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(this,
                "Falló la generación de los elemento de la lista.",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            //Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }   

    private void buildTable(Database db, Credential cred1, String lengthtString, String search,Mode mode)
    {        
        int length;        
        if(lengthtString != null)
        {
            if(lengthtString.matches("^[0-9]+$"))
            {
                length = Integer.valueOf(lengthtString);
            }
            else
            {
                length = DEFAULT_LENGHT_LIST;
            }      
        }
        else
        {
            length = DEFAULT_LENGHT_LIST;
        }
        
        boolean QS_Actived = false,Filter_Active=false;
        Department dep;
        if(cbDepartments.getSelectedIndex() > 0)
        {
            dep = (Department)cbDepartments.getSelectedItem();
            Filter_Active = true;
        }
        else
        {
            dep = null;
        }        
        Office offi;
        if(cbOffices.getSelectedIndex() > 0)
        {
            offi = (Office)cbOffices.getSelectedItem();
            Filter_Active = true;
        }
        else
        {
            offi = null;
        }        
        boolean pers = chPersonal.isSelected();        
        if(pers)
        {
            Filter_Active = true;
        }
        
        if(search != null && length > 0)
        {
            QS_Actived = true;
        }
        
        ServiceQuotation ordRoot = new ServiceQuotation();
        OrdenRootNode root = new OrdenRootNode(ordRoot);     
        final String[] columnNames = {"Estado", "Folio", "Encargado", "Cliente","ETA","S.A.","PO"};
        DefaultTreeTableModel newModel = new DefaultTreeTableModel(root, Arrays.asList(columnNames));
        Throwable ret = fillModel(db, cred1, length, search, root,pers,offi,dep,mode);
        if (ret != null) {
            JOptionPane.showMessageDialog(this,
                    ret.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }  
        tbList = new JXTreeTable(newModel);
        tbList.setRowSelectionAllowed(true);
        tbList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tbList.setAutoCreateRowSorter(true);
        tbList.setComponentPopupMenu(mnCommand);
        jScrollPane1.setViewportView(tbList);
        tbList.setRootVisible(false); 
        tbList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbListMouseClicked(evt);
            }
        });
        //por default todos estan collapsados
        
        //tbList.collapseAll();
        
        if(QS_Actived)
        {
            tbList.expandAll();
        }
        else
        {
            CollapsableListener collasable = new CollapsableListener(tbList,cred);
            tbList.addTreeExpansionListener(collasable);
            
            String sql = "SELECT attribute FROM Configuration WHERE BD = ? and office = ? and user = ? and object = ? and value = ?";
            try 
            {
                //Lee la confufuracion dekl componete de la base de datos
                PreparedStatement pstmt = db.getConnection().prepareStatement(sql);
                pstmt.setString(1, cred.getBD());
                pstmt.setString(2, cred.getSuc());
                pstmt.setString(3, cred.getUser().getAlias());
                pstmt.setString(4, "SIIL.Servicios.Orden.Screen.Table");
                pstmt.setString(5, "Expanded");
                //todas las carpetas expandidas por el usuario
                ResultSet rs = pstmt.executeQuery();
                TreePath path;
                OrdenNode node; 
                while(rs.next())
                {
                    int count = tbList.getModel().getRowCount();
                    for(int i = 0; i < count; i++)
                    {
                        path = tbList.getPathForRow(i);
                        node = (OrdenNode) path.getLastPathComponent();
                        if(node.getData().getState().getCode().equals(rs.getString(1)))
                        {
                            tbList.expandRow(i);
                        }
                    }
                }
            }
            catch (SQLException ex) 
            {
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * 
     * @return 
     */
    public ServiceQuotation getSelection()
    {
        return selection;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mnCommand = new javax.swing.JPopupMenu();
        mnDetail = new javax.swing.JMenuItem();
        mnRows = new javax.swing.JMenuItem();
        mnCreate = new javax.swing.JMenuItem();
        mnEdit = new javax.swing.JMenuItem();
        mnAutho = new javax.swing.JMenuItem();
        mnETA = new javax.swing.JMenuItem();
        mnETAs = new javax.swing.JMenuItem();
        mnTransito = new javax.swing.JMenuItem();
        mnSurtir = new javax.swing.JMenuItem();
        mnEnd = new javax.swing.JMenuItem();
        mnCancel = new javax.swing.JMenuItem();
        mnInStock = new javax.swing.JMenuItem();
        mnChange = new javax.swing.JMenu();
        mnChangeFolio = new javax.swing.JMenuItem();
        mnChangeOwner = new javax.swing.JMenuItem();
        mnChangeClient = new javax.swing.JMenuItem();
        mnRollbackEdition = new javax.swing.JMenuItem();
        mnRollbackAutho = new javax.swing.JMenuItem();
        mnRollbackETA = new javax.swing.JMenuItem();
        mnRollbackArrival = new javax.swing.JMenuItem();
        mnRollbackSA = new javax.swing.JMenuItem();
        mnRollbackEnd = new javax.swing.JMenuItem();
        mnExport = new javax.swing.JMenuItem();
        mnConfig = new javax.swing.JMenu();
        mnConfigLists = new javax.swing.JMenu();
        mnConfigListsCreado = new javax.swing.JCheckBoxMenuItem();
        mnConfigListsCotizado = new javax.swing.JCheckBoxMenuItem();
        mnConfigListsETA = new javax.swing.JCheckBoxMenuItem();
        mnConfigListsTransito = new javax.swing.JCheckBoxMenuItem();
        mnConfigListsAlmacen = new javax.swing.JCheckBoxMenuItem();
        mnConfigListsEntregando = new javax.swing.JCheckBoxMenuItem();
        mnConfigListsTerminada = new javax.swing.JCheckBoxMenuItem();
        mnConfigListsCancelada = new javax.swing.JCheckBoxMenuItem();
        mnSelect = new javax.swing.JMenuItem();
        mnAddWork = new javax.swing.JMenuItem();
        mnPOView = new javax.swing.JMenuItem();
        mnPODown = new javax.swing.JMenuItem();
        mnPOUp = new javax.swing.JMenuItem();
        mnPOFind = new javax.swing.JMenuItem();
        mnPORead = new javax.swing.JMenuItem();
        mnPOUnLink = new javax.swing.JMenuItem();
        txSearch = new org.jdesktop.swingx.JXSearchField();
        chPersonal = new javax.swing.JCheckBox();
        cbDepartments = new org.jdesktop.swingx.JXComboBox();
        cbOffices = new org.jdesktop.swingx.JXComboBox();
        btUpdate = new org.jdesktop.swingx.JXButton();
        jXLabel1 = new org.jdesktop.swingx.JXLabel();
        txCount = new org.jdesktop.swingx.JXTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbList = new org.jdesktop.swingx.JXTreeTable();

        mnDetail.setText("Detalle");
        mnDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnDetailActionPerformed(evt);
            }
        });
        mnCommand.add(mnDetail);

        mnRows.setText("Renglones");
        mnRows.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnRowsActionPerformed(evt);
            }
        });
        mnCommand.add(mnRows);

        mnCreate.setText("Crear");
        mnCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnCreateActionPerformed(evt);
            }
        });
        mnCommand.add(mnCreate);

        mnEdit.setText("Editar");
        mnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnEditActionPerformed(evt);
            }
        });
        mnCommand.add(mnEdit);

        mnAutho.setText("Autorizar");
        mnAutho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAuthoActionPerformed(evt);
            }
        });
        mnCommand.add(mnAutho);

        mnETA.setText("Asignar ETA");
        mnETA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnETAActionPerformed(evt);
            }
        });
        mnCommand.add(mnETA);

        mnETAs.setText("Asignacion masiva de ETA");
        mnETAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnETAsActionPerformed(evt);
            }
        });
        mnCommand.add(mnETAs);

        mnTransito.setText("Confrimar Arribo");
        mnTransito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnTransitoActionPerformed(evt);
            }
        });
        mnCommand.add(mnTransito);

        mnSurtir.setText("Asignar S.A.");
        mnSurtir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnSurtirActionPerformed(evt);
            }
        });
        mnCommand.add(mnSurtir);

        mnEnd.setText("Terminar");
        mnEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnEndActionPerformed(evt);
            }
        });
        mnCommand.add(mnEnd);

        mnCancel.setText("Cancelar");
        mnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnCancelActionPerformed(evt);
            }
        });
        mnCommand.add(mnCancel);

        mnInStock.setText("En Stock");
        mnInStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnInStockActionPerformed(evt);
            }
        });
        mnCommand.add(mnInStock);

        mnChange.setText("Cambiar");

        mnChangeFolio.setText("Folio");
        mnChangeFolio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnChangeFolioActionPerformed(evt);
            }
        });
        mnChange.add(mnChangeFolio);

        mnChangeOwner.setText("Encargado");
        mnChangeOwner.setEnabled(false);
        mnChangeOwner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnChangeOwnerActionPerformed(evt);
            }
        });
        mnChange.add(mnChangeOwner);

        mnChangeClient.setText("Cambiar Cliente");
        mnChangeClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnChangeClientActionPerformed(evt);
            }
        });
        mnChange.add(mnChangeClient);

        mnRollbackEdition.setText("Revertir Edicón");
        mnRollbackEdition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnRollbackEditionActionPerformed(evt);
            }
        });
        mnChange.add(mnRollbackEdition);

        mnRollbackAutho.setText("Revertir Autorizacion");
        mnRollbackAutho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnRollbackAuthoActionPerformed(evt);
            }
        });
        mnChange.add(mnRollbackAutho);

        mnRollbackETA.setText("Revertir ETA");
        mnRollbackETA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnRollbackETAActionPerformed(evt);
            }
        });
        mnChange.add(mnRollbackETA);

        mnRollbackArrival.setText("Revertir Arribo");
        mnRollbackArrival.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnRollbackArrivalActionPerformed(evt);
            }
        });
        mnChange.add(mnRollbackArrival);

        mnRollbackSA.setText("Revertir S.A.");
        mnRollbackSA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnRollbackSAActionPerformed(evt);
            }
        });
        mnChange.add(mnRollbackSA);

        mnRollbackEnd.setText("Revertir Terminar");
        mnRollbackEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnRollbackEndActionPerformed(evt);
            }
        });
        mnChange.add(mnRollbackEnd);

        mnCommand.add(mnChange);

        mnExport.setText("Exportar");
        mnExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnExportActionPerformed(evt);
            }
        });
        mnCommand.add(mnExport);

        mnConfig.setText("Configuracion");

        mnConfigLists.setText("Listas");

        mnConfigListsCreado.setText("Creado");
        mnConfigListsCreado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnConfigListsCreadoActionPerformed(evt);
            }
        });
        mnConfigLists.add(mnConfigListsCreado);

        mnConfigListsCotizado.setText("Cotizado");
        mnConfigListsCotizado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnConfigListsCotizadoActionPerformed(evt);
            }
        });
        mnConfigLists.add(mnConfigListsCotizado);

        mnConfigListsETA.setText("ETA");
        mnConfigListsETA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnConfigListsETAActionPerformed(evt);
            }
        });
        mnConfigLists.add(mnConfigListsETA);

        mnConfigListsTransito.setText("En Transito");
        mnConfigListsTransito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnConfigListsTransitoActionPerformed(evt);
            }
        });
        mnConfigLists.add(mnConfigListsTransito);

        mnConfigListsAlmacen.setText("Almacen");
        mnConfigListsAlmacen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnConfigListsAlmacenActionPerformed(evt);
            }
        });
        mnConfigLists.add(mnConfigListsAlmacen);

        mnConfigListsEntregando.setText("Entregando a cliente");
        mnConfigListsEntregando.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnConfigListsEntregandoActionPerformed(evt);
            }
        });
        mnConfigLists.add(mnConfigListsEntregando);

        mnConfigListsTerminada.setText("Terminada");
        mnConfigListsTerminada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnConfigListsTerminadaActionPerformed(evt);
            }
        });
        mnConfigLists.add(mnConfigListsTerminada);

        mnConfigListsCancelada.setText("Canceladas");
        mnConfigListsCancelada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnConfigListsCanceladaActionPerformed(evt);
            }
        });
        mnConfigLists.add(mnConfigListsCancelada);

        mnConfig.add(mnConfigLists);

        mnCommand.add(mnConfig);

        mnSelect.setText("Seleccionar");
        mnSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnSelectActionPerformed(evt);
            }
        });
        mnCommand.add(mnSelect);

        mnAddWork.setText("Agregar a relacion de Trabajo");
        mnAddWork.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAddWorkActionPerformed(evt);
            }
        });
        mnCommand.add(mnAddWork);

        mnPOView.setText("Ver PO");
        mnPOView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnPOViewActionPerformed(evt);
            }
        });
        mnCommand.add(mnPOView);

        mnPODown.setText("Descargar PO");
        mnPODown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnPODownActionPerformed(evt);
            }
        });
        mnCommand.add(mnPODown);

        mnPOUp.setText("Subir y Asociar PO");
        mnPOUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnPOUpActionPerformed(evt);
            }
        });
        mnCommand.add(mnPOUp);

        mnPOFind.setText("Buscar y Asociar PO");
        mnPOFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnPOFindActionPerformed(evt);
            }
        });
        mnCommand.add(mnPOFind);

        mnPORead.setText("Lista de PO");
        mnPORead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnPOReadActionPerformed(evt);
            }
        });
        mnCommand.add(mnPORead);

        mnPOUnLink.setText("Desasociar PO");
        mnPOUnLink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnPOUnLinkActionPerformed(evt);
            }
        });
        mnCommand.add(mnPOUnLink);

        txSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txSearchActionPerformed(evt);
            }
        });

        chPersonal.setText("Personal");
        chPersonal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chPersonalMouseClicked(evt);
            }
        });

        cbDepartments.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Departamento..." }));
        cbDepartments.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbDepartmentsActionPerformed(evt);
            }
        });

        cbOffices.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Oficina..." }));
        cbOffices.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbOfficesActionPerformed(evt);
            }
        });

        btUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/reload.png"))); // NOI18N
        btUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUpdateActionPerformed(evt);
            }
        });

        jXLabel1.setText("Cant.");

        txCount.setText("20");
        txCount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txCountActionPerformed(evt);
            }
        });

        tbList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(txSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chPersonal)
                .addGap(18, 18, 18)
                .addComponent(cbDepartments, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cbOffices, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 170, Short.MAX_VALUE)
                .addComponent(btUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jXLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txCount, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jXLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(chPersonal)
                        .addComponent(cbDepartments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbOffices, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txSearchActionPerformed
        reloadTable();
    }//GEN-LAST:event_txSearchActionPerformed

    private void chPersonalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chPersonalMouseClicked
        reloadTable();
    }//GEN-LAST:event_chPersonalMouseClicked

    private void cbDepartmentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbDepartmentsActionPerformed
        if(cbDepartments.getSelectedIndex() > 0) reloadTable();
    }//GEN-LAST:event_cbDepartmentsActionPerformed

    private void cbOfficesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbOfficesActionPerformed
        if(cbOffices.getSelectedIndex() > 0) reloadTable();
    }//GEN-LAST:event_cbOfficesActionPerformed

    private void btUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUpdateActionPerformed
        reloadTable();
    }//GEN-LAST:event_btUpdateActionPerformed

    private void txCountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txCountActionPerformed
        reloadTable();
    }//GEN-LAST:event_txCountActionPerformed

    private void mnDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnDetailActionPerformed
        ServiceQuotation ord = getSelectItem();
        viewDetail(null,ord);
    }//GEN-LAST:event_mnDetailActionPerformed

    /**
     * 
     * @param dbserver si es null no se intera descargas los datos de la oren
     * @param orden 
     */
    public static void viewDetail(Database dbserver,ServiceQuotation orden) 
    {
        if(dbserver != null)    
        {
            try 
            {
                orden.fill(dbserver, SIIL.servApp.cred, orden.getID());
                orden.downQuotation(dbserver.getConnection());
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null,
                        ex.getMessage(),
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
        SIIL.Servicios.Orden.Detail lor = new SIIL.Servicios.Orden.Detail(orden,SIIL.servApp.cred);
        SIIL.servApp.getInstance().getDesktopPane().add(lor);
        int x = SIIL.servApp.getInstance().getDesktopPane().getSize().width/2 - lor.getSize().width/2;
        int y = 10;
        lor.setLocation(x, y);
        lor.setVisible(true);
    }

    private void mnRowsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnRowsActionPerformed
        viewRows();
    }//GEN-LAST:event_mnRowsActionPerformed

    public static boolean viewRows(Database dbserver,ServiceQuotation orden)
    {        
        try 
        {
            orden.fill(dbserver, SIIL.servApp.cred, orden.getID());
            orden.downQuotation(dbserver.getConnection());
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
            );
            return true;
        }
        
        SIIL.sales.rows.Rows rows = new SIIL.sales.rows.Rows(orden);
        JDialog dlg = new JDialog();
        dlg.setSize(rows.getPreferredSize());
        dlg.setTitle("Reglones de cotización " + orden.getFullFolio());
        rows.setDialog(dlg);
        dlg.setContentPane(rows);
        int x = SIIL.servApp.getInstance().getDesktopPane().getSize().width/2 - dlg.getSize().width/2;
        int y = 10;
        dlg.setLocation(x, y);
        dlg.setLocationRelativeTo(null);
        dlg.setModal(true);
        dlg.setVisible(true);
        dbserver.close();
        return false;
    }
    
    private boolean viewRows()
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
            return false;
        }
        
        ServiceQuotation orden = getSelectItem();
        
        SIIL.sales.rows.Rows rows = new SIIL.sales.rows.Rows(orden);
        JDialog dlg = new JDialog();
        dlg.setSize(rows.getPreferredSize());
        dlg.setTitle("Reglones de cotización " + orden.getFullFolio());
        rows.setDialog(dlg);
        dlg.setContentPane(rows);
        int x = SIIL.servApp.getInstance().getDesktopPane().getSize().width/2 - dlg.getSize().width/2;
        int y = 10;
        dlg.setLocation(x, y);
        dlg.setLocationRelativeTo(null);
        dlg.setModal(true);
        dlg.setVisible(true);
        dbserver.close();
        return false;
    }

    private void mnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnCreateActionPerformed
        SIIL.Servicios.Orden.Create2 prov = new SIIL.Servicios.Orden.Create2(cred,this);
        JXDialog dlg = new JXDialog(prov);
        prov.setDialog(dlg);
        dlg.setContentPane(prov);
        dlg.setSize(prov.getPreferredSize());
        dlg.setModal(true);
        dlg.setVisible(true); 
    }//GEN-LAST:event_mnCreateActionPerformed

    private void mnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnEditActionPerformed
        ServiceQuotation o = getSelectItem();
        orderEdit(o);
    }//GEN-LAST:event_mnEditActionPerformed

    private boolean orderEdit(ServiceQuotation o) throws HeadlessException 
    {
        SIIL.servApp.Progress progress = (SIIL.servApp.Progress) SIIL.servApp.getInstance().getProgressObject();
        ((SIIL.servApp.Progress)SIIL.servApp.getInstance().getProgressObject()).On();
        progress.setProgress(1, "Iniciando actividad.");
        progress.setProgress(2, "Validando datos de insumo...");
        OrdenCotizada ord;
        if (o instanceof OrdenCotizada) 
        {
            ord = (OrdenCotizada)o;
        } 
        else if (o instanceof OrdenAutorizar) 
        {
            ord = new OrdenCotizada(o);
        } 
        else 
        {
            JOptionPane.showMessageDialog(this,
                    "La operación no se aplica para el registro seleccionado.",
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
            );
            progress.clean();
            return true;
        }
        int n = JOptionPane.showOptionDialog(this,
                "Está editando el Documento '" + ord.getFolio() + "'. ¿Desea continuar?" ,
                "Confirme de solicitud",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,null,null);
        int res = 0;
        if (n == JOptionPane.NO_OPTION) {
            progress.clean();
            return true;
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
        ord.setServerDB(db);
        ord.setProgressObject(SIIL.servApp.getInstance().getProgressObject());
        ((SIIL.servApp.Progress)ord.getProgressObject()).setView(this);
        dataValid = false;
        Thread th = new Thread(ord);
        th.start();
        return false;
    }
    
    private void mnAuthoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAuthoActionPerformed
        SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();        
        Database dbServer = null;
        try 
        {
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
            dbServer = new Database(serverConfig);
        } 
        catch (ClassNotFoundException | SQLException | IOException | ParserConfigurationException | SAXException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }        
        ServiceQuotation ord = getSelectItem();
        try 
        {
            ord.getEntreprise().downRequirePO(dbServer);
            if(ord.getEntreprise().getRequirePO() == Enterprise.RequirePO.ANTERIOR && (SIIL.servApp.cred.getOffice().getCode().equals("bc.tj") || SIIL.servApp.cred.getOffice().getCode().equals("bc.mx")))//si es anterior al servicio
            {
                if(ord.getPOFile() == null)
                {
                    if(cred.acces(dbServer,"Servicios.Orcom.PO.Jump"))
                    {
                        int option = JOptionPane.showConfirmDialog(
                            this,
                            "Se requiere tener PO previo a la realizacion del servicio. ¿Desea ignorar este requisito?",
                            "Autorización",
                            JOptionPane.YES_NO_OPTION);
                        if(option == JOptionPane.NO_OPTION)
                        {
                            dbServer.rollback();
                            dbServer.close();
                            return;
                        }
                        else if(option == JOptionPane.YES_OPTION)
                        {
                            ;
                        }
                        else
                        {
                            dbServer.rollback();
                            dbServer.close();
                            return;
                        }
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(this,
                        "Se requiere tener PO pervio a la realizacion del servicio.",
                        "Error Externo",
                        JOptionPane.ERROR_MESSAGE
                        );
                        dbServer.rollback();
                        dbServer.close();
                        return;
                    }
                }
            }
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(Read.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        orderAutho(dbServer,ord);
    }//GEN-LAST:event_mnAuthoActionPerformed

    private boolean orderAutho(Database dbServer,ServiceQuotation o)
    {
        SIIL.servApp.Progress progress = (SIIL.servApp.Progress) SIIL.servApp.getInstance().getProgressObject();
        ((SIIL.servApp.Progress)SIIL.servApp.getInstance().getProgressObject()).On();
        progress.setProgress(1, "Iniciando actividad.");
        progress.setProgress(2, "Validando datos de insumo...");
        OrdenAutorizar ord;
        if (o instanceof OrdenAutorizar) 
        {
            ord = (OrdenAutorizar)o;
        } 
        else 
        {
            JOptionPane.showMessageDialog(this,
                    "La esta operación no se aplica para el registro seleccionado.",
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
            );
            progress.clean();
            return true;
        }
        int n = JOptionPane.showOptionDialog(this,
                "Está autorizando el Documento '" + ord.getFolio().toString() + "'. ¿Desea continuar?" ,
                "Confirme operación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,null,null);
        int res = 0;
        if (n == JOptionPane.NO_OPTION) 
        {
            progress.clean();
            return true;
        }
        progress.setProgress(10, "Conectando a Base de Datos...");
        
        ord.setServerDB(dbServer);
        ord.setProgressObject(SIIL.servApp.getInstance().getProgressObject());
        ((SIIL.servApp.Progress)ord.getProgressObject()).setView(this);
        dataValid = false;
        Thread th = new Thread(ord);
        th.start();
        return false;
    }
    
    private void mnETAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnETAActionPerformed
        ServiceQuotation o = getSelectItem();
        orderETA(o);
    }//GEN-LAST:event_mnETAActionPerformed

    private boolean orderETA(ServiceQuotation o) throws NumberFormatException {
        SIIL.servApp.Progress progress = (SIIL.servApp.Progress) SIIL.servApp.getInstance().getProgressObject();
        ((SIIL.servApp.Progress)SIIL.servApp.getInstance().getProgressObject()).On();
        progress.setProgress(1, "Iniciando actividad.");
        progress.setProgress(2, "Validando datos de insumo...");
        OrdenETA ord;
        if (o instanceof OrdenETA) {
            ord = (OrdenETA)o;
        } else if (o instanceof OrdenTransito) {
            ord = new OrdenETA(o);
        } else {
            JOptionPane.showMessageDialog(this,
                    "La esta operación no se aplica para el registro seleccionado.",
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
            );
            progress.clean();
            return true;
        }
        int n = JOptionPane.showOptionDialog(this,
                "Está asignando ETA a el Documento '" + ord.getFolio() + "'. ¿Desea continuar?" ,
                "Confirme Operación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,null,null);
        int res = 0;
        if (n == JOptionPane.NO_OPTION) 
        {
            progress.clean();
            return true;
        }
        progress.setProgress(5, "Validando insumo de datos...");
        progress.setProgress(10, "Conectando a Base de Datos...");
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
        String fecha = "";
        fecha = (String)JOptionPane.showInputDialog(
                this,
                "Introduzca Fecha(dd/mm/yyyy)",
                "Captura de Fecha",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "");
        if (fecha == null) {
            progress.clean();
            return true;
        }
        try {
            if (fecha.matches("^[0-9]{2,2}$")) {
                Calendar c = new GregorianCalendar(db.getDateYear(), db.getDateMonth()-1,Integer.valueOf(fecha));
                ord.setFhETA(c.getTime());
            } else if (fecha.matches("^[0-9]{4,4}$")) {
                Integer dia,mes;
                String substr = fecha.substring(0, 2);
                dia = Integer.valueOf(substr);
                substr = fecha.substring(2, 4);
                mes = Integer.valueOf(substr) - 1;
                Calendar c = new GregorianCalendar(db.getDateYear(),mes,dia);
                ord.setFhETA(c.getTime());
            } 
            else 
            {
                SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    ord.setFhETA(dt.parse(fecha));
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Formato incorrecto use 'dd/mm/yyyy' ",
                            "Error Externo",
                            JOptionPane.ERROR_MESSAGE
                    );
                    progress.clean();
                    return true;
                }
            }
            long eta,now;
            eta = ord.getFhETA().getTime();
            now = db.getDateToday().getTime();
            if (eta < now) {
                JOptionPane.showMessageDialog(this,
                        "La Fecha de E.T.A es anterior a la fecha de hoy.",
                        "Error Externo",
                        JOptionPane.ERROR_MESSAGE
                );
                progress.clean();
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            progress.clean();
            return true;
        }
        ord.setServerDB(db);
        ord.setProgressObject(SIIL.servApp.getInstance().getProgressObject());
        ((SIIL.servApp.Progress)ord.getProgressObject()).setView(this);
        dataValid = false;
        Thread th = new Thread(ord);
        th.start();
        return false;
    }
    
    private void mnTransitoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnTransitoActionPerformed
        ServiceQuotation o = getSelectItem();
        orderTransito(o);
    }//GEN-LAST:event_mnTransitoActionPerformed

    private void mnSurtirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnSurtirActionPerformed
        ServiceQuotation o=getSelectItem();
        orderSurtir(o);
    }//GEN-LAST:event_mnSurtirActionPerformed

    private void mnEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnEndActionPerformed
        ServiceQuotation o = getSelectItem();
        orderEnd(o);
    }//GEN-LAST:event_mnEndActionPerformed

    private void mnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnCancelActionPerformed
        SIIL.servApp.Progress progress = (SIIL.servApp.Progress) SIIL.servApp.getInstance().getProgressObject();
        ((SIIL.servApp.Progress)SIIL.servApp.getInstance().getProgressObject()).On();

        progress.setProgress(1, "Iniciando actividad.");
        progress.setProgress(2, "Validando datos de insumo...");

        OrdenCancelar ord = new OrdenCancelar((ServiceQuotation)getSelectItem());

        int n = JOptionPane.showOptionDialog(this,
            "Está cancelando el Documento '" + ord.getFolio() + "'. ¿Desea continuar?" ,
            "Confirme de solicitud",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,null,null,null);
        int res = 0;
        if(n == JOptionPane.NO_OPTION)
        {
            return;
        }
        progress.setProgress(20, "Conectando a base de datos...");
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
        
        //Si ya esta en transito
        if(ord.getState().getOrdinal() > 3 && ord.getState().getOrdinal() < 8)
        {
            int opSel = JOptionPane.showOptionDialog(this,
                "El Documento '" + ord.getFolio() + "' está como '" + ord.getState().getName() + "' implica que ya se realizo la compra, ¿Desea continaur con la operación?" ,
                "Confirme de solicitud",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,null,null);
            if(opSel == JOptionPane.NO_OPTION)
            {
                return;
            }
        }
        
        //
        ord.setServerDB(db);
        ord.setProgressObject(progress);
        
        //((SIIL.servApp.Progress)ord.getProgressObject()).setView(this);
        dataValid = false;
        String comment;
        comment = (String)JOptionPane.showInputDialog(
            this,
            "¿Cual es la razón por la que se realiza la cancelación?",
            "Comentario",
            JOptionPane.PLAIN_MESSAGE,
            null,
            null,
            "");
        progress.setProgress(50, "Iniciando operacón...");
        ord.setTerminalComment(comment);
        Thread th = new Thread((Runnable) ord);
        th.start();
    }//GEN-LAST:event_mnCancelActionPerformed

    private void mnInStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnInStockActionPerformed
        JOptionPane.showMessageDialog(this,
            "Pensado en la comodidad de usted el equipo de Tool ha iniciado la implementación de está operación, en muy poco tiempo estará disponible.",
            "En Desarrollo...",
            JOptionPane.WARNING_MESSAGE
        );
    }//GEN-LAST:event_mnInStockActionPerformed

    private void mnChangeFolioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnChangeFolioActionPerformed
        OrdenUpdate orden = new OrdenUpdate(getSelectItem());
        Update update = new Update(orden,Update.ModeEdit.FOLIO,cred,this);
        SIIL.servApp.getInstance().getDesktopPane().add(update);
        int x = SIIL.servApp.getInstance().getDesktopPane().getSize().width/2 - update.getSize().width/2;
        int y = 10;
        update.setLocation(x, y);
        update.setVisible(true);
    }//GEN-LAST:event_mnChangeFolioActionPerformed

    private void mnChangeOwnerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnChangeOwnerActionPerformed
        OrdenUpdate orden = new OrdenUpdate(getSelectItem());
        Update update = new Update(orden,Update.ModeEdit.OWNER,cred,this);
        SIIL.servApp.getInstance().getDesktopPane().add(update);
        int x = SIIL.servApp.getInstance().getDesktopPane().getSize().width/2 - update.getSize().width/2;
        int y = 10;
        update.setLocation(x, y);
        update.setVisible(true);
    }//GEN-LAST:event_mnChangeOwnerActionPerformed

    private void mnChangeClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnChangeClientActionPerformed
        OrdenUpdate orden = new OrdenUpdate(getSelectItem());
        Update update = new Update(orden,Update.ModeEdit.CLIENT,cred,this);
        SIIL.servApp.getInstance().getDesktopPane().add(update);
        int x = SIIL.servApp.getInstance().getDesktopPane().getSize().width/2 - update.getSize().width/2;
        int y = 10;
        update.setLocation(x, y);
        update.setVisible(true);
    }//GEN-LAST:event_mnChangeClientActionPerformed

    private void mnRollbackEditionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnRollbackEditionActionPerformed
        JOptionPane.showMessageDialog(this,
            "Pensado en la comodidad de usted el equipo de Tool ha iniciado la implementación de está operación, en muy poco tiempo estará disponible.",
            "En Desarrollo...",
            JOptionPane.WARNING_MESSAGE
        );
    }//GEN-LAST:event_mnRollbackEditionActionPerformed

    private void mnRollbackAuthoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnRollbackAuthoActionPerformed
        JOptionPane.showMessageDialog(this,
            "Pensado en la comodidad de usted el equipo de Tool ha iniciado la implementación de está operación, en muy poco tiempo estará disponible.",
            "En Desarrollo...",
            JOptionPane.WARNING_MESSAGE
        );
    }//GEN-LAST:event_mnRollbackAuthoActionPerformed

    private void mnRollbackETAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnRollbackETAActionPerformed
        JOptionPane.showMessageDialog(this,
            "Pensado en la comodidad de usted el equipo de Tool ha iniciado la implementación de está operación, en muy poco tiempo estará disponible.",
            "En Desarrollo...",
            JOptionPane.WARNING_MESSAGE
        );
    }//GEN-LAST:event_mnRollbackETAActionPerformed

    private void mnRollbackArrivalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnRollbackArrivalActionPerformed
        JOptionPane.showMessageDialog(this,
            "Pensado en la comodidad de usted el equipo de Tool ha iniciado la implementación de está operación, en muy poco tiempo estará disponible.",
            "En Desarrollo...",
            JOptionPane.WARNING_MESSAGE
        );
    }//GEN-LAST:event_mnRollbackArrivalActionPerformed

    private void mnRollbackSAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnRollbackSAActionPerformed
        JOptionPane.showMessageDialog(this,
            "Pensado en la comodidad de usted el equipo de Tool ha iniciado la implementación de está operación, en muy poco tiempo estará disponible.",
            "En Desarrollo...",
            JOptionPane.WARNING_MESSAGE
        );
    }//GEN-LAST:event_mnRollbackSAActionPerformed

    private void mnRollbackEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnRollbackEndActionPerformed
        JOptionPane.showMessageDialog(this,
            "Pensado en la comodidad de usted el equipo de Tool ha iniciado la implementación de está operación, en muy poco tiempo estará disponible.",
            "En Desarrollo...",
            JOptionPane.WARNING_MESSAGE
        );
    }//GEN-LAST:event_mnRollbackEndActionPerformed

    private void mnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnExportActionPerformed
        final JFileChooser fc = new JFileChooser();
        //Visualiza el cuadro de guardar c para indicar el archivo.
        String nameFile;
        FileSystemView fw = fc.getFileSystemView();
        fc.setCurrentDirectory(fw.getDefaultDirectory());
        int returnVal = fc.showSaveDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            if(fc.getSelectedFile().getAbsolutePath().endsWith(".csv"))
            {
                nameFile = fc.getSelectedFile().getAbsolutePath();
            }
            else
            {
                nameFile = fc.getSelectedFile().getAbsolutePath() + ".csv";
            }
        }
        else
        {
            return ;
        }
        FileWriter file;
        try
        {
            file = new FileWriter(nameFile);
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                "Falló la creacion del Archivo",
                "Error externo",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        catch (IOException ex)
        {
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                "Falló la creacion del Archivo",
                "Error externo",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        TreeTableModel tree = tbList.getTreeTableModel();
        OrdenNode root = (OrdenNode) tree.getRoot();
        try
        {
            file.append("Estado,Folio,Encargado,Compañia,E.T.A,S.A.\n");
        }
        catch (IOException ex)
        {
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                "Falló la genración del Archivo, quizá este en uso por algún programa.",
                "Error externo",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        generateExport(root,file);
        try
        {
            file.flush();
            file.close();
            JOptionPane.showMessageDialog(this,
                "Archivo generado correctamente.",
                "Operación completa",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
        catch (IOException ex)
        {
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                "Falló al guardar el Archivo",
                "Error externo",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
    }//GEN-LAST:event_mnExportActionPerformed

    private boolean setListFlag(String estado, boolean flag) 
    {
        boolean flCreate = flag;
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
        //Estado estado = new Estado();
        //estado.fill(db, cred, "docedit");
        String strObject = "SIIL.Servicios.Orden.Screen.List";
        Configuration conf = new Configuration();
        conf.setBD(cred.getBD());
        conf.setOffice(cred.getSuc());
        conf.setUser(cred.getUser());
        conf.setObject(strObject);
        conf.setAttribute(estado);
        String val = flCreate ? "enable" : "disable";
        conf.setValue(val);
        int fl = 0;
        try 
        {
            fl = conf.update(db);
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return true;
        }
        if (fl == 1) {
            try {
                db.commit();
            } catch (SQLException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return true;
            }
        } else {
            try {
                db.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return true;
            }
        }
        db.close();
        return false;
    }
    
    private void mnConfigListsCreadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnConfigListsCreadoActionPerformed
        setListFlag("docedit",mnConfigListsCreado.isSelected());
        reloadTable();
    }//GEN-LAST:event_mnConfigListsCreadoActionPerformed

    private void mnConfigListsCotizadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnConfigListsCotizadoActionPerformed
        setListFlag("docpen",mnConfigListsCotizado.isSelected());
        reloadTable();
    }//GEN-LAST:event_mnConfigListsCotizadoActionPerformed

    private void mnConfigListsETAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnConfigListsETAActionPerformed
        setListFlag("pedpen",mnConfigListsETA.isSelected());
        reloadTable();
    }//GEN-LAST:event_mnConfigListsETAActionPerformed

    private void mnConfigListsTransitoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnConfigListsTransitoActionPerformed
        setListFlag("pedtrans",mnConfigListsTransito.isSelected());
        reloadTable();
    }//GEN-LAST:event_mnConfigListsTransitoActionPerformed

    private void mnConfigListsAlmacenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnConfigListsAlmacenActionPerformed
        setListFlag("pedArrb",mnConfigListsAlmacen.isSelected());
        reloadTable();
    }//GEN-LAST:event_mnConfigListsAlmacenActionPerformed

    private void mnConfigListsEntregandoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnConfigListsEntregandoActionPerformed
        setListFlag("pedsur",mnConfigListsEntregando.isSelected());
        reloadTable();
    }//GEN-LAST:event_mnConfigListsEntregandoActionPerformed

    private void mnConfigListsTerminadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnConfigListsTerminadaActionPerformed
        setListFlag("pedfin",mnConfigListsTerminada.isSelected());
        reloadTable();
    }//GEN-LAST:event_mnConfigListsTerminadaActionPerformed

    private void mnConfigListsCanceladaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnConfigListsCanceladaActionPerformed
        setListFlag("cancel",mnConfigListsCancelada.isSelected());
        reloadTable();
    }//GEN-LAST:event_mnConfigListsCanceladaActionPerformed

    private void tbListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbListMouseClicked
        if(evt.getClickCount() == 2)
        {
            if(mode == Mode.SELECTION_AUTHO | mode == Mode.SELECTION_ALMACEN)//Modeo de selecion de una
            {//se cierra una vez que ha selecionado la orden
                this.selection = getSelectItem();
                dialog.dispose();
            }
            else
            {
                viewRows();
            }
        }
    }//GEN-LAST:event_tbListMouseClicked

    private void mnSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnSelectActionPerformed
        if(mode == Mode.SELECTION_ETA | mode == Mode.SELECTION_ALMACEN)//Modeo de selecion de una
        {//se cierra una vez que ha selecionado la orden
                this.selection = getSelectItem();
                dialog.dispose();
        }       
    }//GEN-LAST:event_mnSelectActionPerformed

    private void mnAddWorkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAddWorkActionPerformed
        if(tbList.getSelectedRowCount() > 0)
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
            
        
            List<ServiceQuotation> rows = getSelectItems();
            Trabajo flcreate = null;
            Trace contexTrace = new Trace(BACKWARD_BD, SIIL.servApp.cred.getUser(), "Importacin de cotizaciones hacia Relacion de Trabajo");
            try 
            {
                Date dt = new Date();
                Calendar c = Calendar.getInstance(); 
                c.setTime(dt); 
                c.add(Calendar.DATE, 1);
                dt = c.getTime();
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String strRet = (String) JOptionPane.showInputDialog(
                            this,
                            "Indique la fecha(dia/mes/año)",
                            "Confirme operación",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            df.format(dt));
                if(strRet == null) 
                {
                    dbserver.rollback();
                    dbserver.close();
                    return ;
                }
                dt = df.parse(strRet);
        
                for(ServiceQuotation orden : rows)
                {
                    flcreate = Trabajo.create(dbserver, Sheet.CAMPO, orden.getEntreprise(), orden,dt,"Importada desde Cotizacion por " + SIIL.servApp.cred.getUser(),SIIL.servApp.cred.getUser(),contexTrace);
                    if(flcreate == null) 
                    {
                        dbserver.rollback();
                        dbserver = null;
                        throw new FailResultOperationException("Fallo la importacion de Cotizaciones");
                    }
                }
                if(flcreate != null)
                {
                    dbserver.commit();
                    dbserver.close();
                    dbserver = null;
                    JOptionPane.showMessageDialog(this,
                        "Se importaron " + rows.size() + " Ordnes",
                        "Confirmacion",
                        JOptionPane.INFORMATION_MESSAGE
                    ); 
                }
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(Read.class.getName()).log(Level.SEVERE, null, ex);                
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                );
            } catch (ParseException ex) {
                Logger.getLogger(Read.class.getName()).log(Level.SEVERE, null, ex);
            }                        
        }
    }//GEN-LAST:event_mnAddWorkActionPerformed

    private void mnETAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnETAsActionPerformed
        if(tbList.getSelectedRowCount() > 0)
        {
            SIIL.servApp.Progress progress = (SIIL.servApp.Progress) SIIL.servApp.getInstance().getProgressObject();
            ((SIIL.servApp.Progress)SIIL.servApp.getInstance().getProgressObject()).On();
            progress.setProgress(1, "Iniciando actividad.");
            progress.setProgress(2, "Validando datos de insumo...");
            
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
            
            
            List<ServiceQuotation> rows = getSelectItems();            
            int opcion = JOptionPane.showOptionDialog(this,
                    "Está asignando ETA masivamente a  '" + rows.size() + "' elementos. ¿Desea continuar?" ,
                    "Confirme Operación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,null,null,null);
            int res = 0;
            if (opcion == JOptionPane.NO_OPTION) 
            {
                progress.clean();
                return;
            }
            
            String strFecha = "";
            strFecha = (String)JOptionPane.showInputDialog(
                    this,
                    "Indique la Fecha(dd/mm/yyyy)",
                    "Captura de Fecha",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "");
            if (strFecha == null) 
            {
                progress.clean();
            }
            SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yy");
            java.util.Date fhEta = null;
            try 
            {
                fhEta = dt.parse(strFecha);
            } 
            catch (ParseException ex) 
            {
                JOptionPane.showMessageDialog(this,
                            "Formato de fecha incorrecto, use 'dd/mm/yy' ",
                            "Error Externo",
                            JOptionPane.ERROR_MESSAGE
                );
                progress.clean();
                return ;
            }
                
            Boolean flChange = null;            
            Trace contexTrace = new Trace(BACKWARD_BD, SIIL.servApp.cred.getUser(), "Importacin de cotizaciones hacia Relacion de Trabajo");
            try 
            {
                for(ServiceQuotation orden : rows)
                {                    
                    OrdenETA ord = null;
                    if (orden instanceof OrdenETA) 
                    {
                        ord = (OrdenETA)orden;
                    } 
                    else if (orden instanceof OrdenTransito) 
                    {
                        ord = new OrdenETA(orden);
                    } 
                    else 
                    {
                        JOptionPane.showMessageDialog(this,
                                "La esta operación no se aplica para el registro seleccionado.",
                                "Error Externo",
                                JOptionPane.ERROR_MESSAGE
                        );
                        progress.clean();
                        return;
                    }
                    
                    flChange = ord.eta(dbserver,fhEta,progress,contexTrace);
                    if(flChange == false) throw new FailResultOperationException("Fallo la asignacion de ETA para '" + ord.getFullFolio() + "'");
                }
                
                int mailCount = process.Mail.getCount(dbserver);
                String comment;
                if(mailCount < 10)
                {
                    comment = "Operacion completada.";
                }
                else
                {
                    comment = "Operacion completada. Hay " + mailCount + " correos espera.";
                }
                
                if(flChange)
                {                    
                    dbserver.commit();
                    dbserver.close();
                    progress.finalized(new Confirmation(comment)); 
                }
            } 
            catch (UnsupportedEncodingException | MessagingException | SQLException ex) 
            {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                );
            }                      
        }
    }//GEN-LAST:event_mnETAsActionPerformed

    private void mnPOUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnPOUpActionPerformed
        ServiceQuotation ord = getSelectItem();
        if(ord == null) return;
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
        Upload screen = new Upload(ord,null);
        core.Dialog dialog = new core.Dialog(screen);
        dialog.setContent(screen);
        if(screen.getBusinesDocument() != null)
        {
            try 
            {
                Return ret = ord.upPOFile(dbserver, screen.getBusinesDocument());
                if(ret.isFlag())
                {
                    dbserver.commit();
                    reloadTable();
                    JOptionPane.showMessageDialog(this,
                        "Operacion completada",
                        "Confirmación",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
                else
                {
                    dbserver.rollback();
                }
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(Read.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } 
        dbserver.close();
    }//GEN-LAST:event_mnPOUpActionPerformed

    private void mnPODownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnPODownActionPerformed
        ServiceQuotation ord = getSelectItem();
        if(ord == null) return;
        
        
        if(ord.getPOFile() == null)
        {
            JOptionPane.showMessageDialog(this,
                "No hay PO asociado",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        final JFileChooser fc = new JFileChooser();   
        FileSystemView fw = fc.getFileSystemView();
        fc.setCurrentDirectory(fw.getDefaultDirectory());
        fc.setSelectedFile(new File(ord.getPOFile().getBobeda().getNombre()));
        int returnVal = fc.showSaveDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            
        }
        else
        {
            return ;
        }
        try
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
            FTP ftpServer = new FTP();
            boolean expResult = false;
            try 
            {
                expResult = ftpServer.connect(serverConfig);
            } 
            catch (IOException ex)
            {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                );
                return;           
            }
            
            //OutputStream file = Vault.getFile(dbserver, ftpServer, ord.getPOFile().getBobeda(), fc.getSelectedFile().getAbsolutePath());
            Archivo archivo = ord.getPOFile().getBobeda();
            archivo.download(dbserver, ftpServer, new File(fc.getSelectedFile().getAbsolutePath()));
            OutputStream file = archivo.getDownloadedFile();
            file.close();
        }
        catch (SQLException | IOException ex)
        {
            //Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error externo",
                JOptionPane.ERROR_MESSAGE
            );
        }       
    }//GEN-LAST:event_mnPODownActionPerformed

    private void mnPOReadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnPOReadActionPerformed
        core.bobeda.CRUD screen = new core.bobeda.CRUD(core.Mode.VIEW,Table.SERVICEQUOTATION);
        core.Dialog dialog = new core.Dialog(screen);
        dialog.setContent(screen);
    }//GEN-LAST:event_mnPOReadActionPerformed

    private void mnPOFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnPOFindActionPerformed
        ServiceQuotation ord = getSelectItem();
        if(ord == null) return;
        
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
        
        core.bobeda.CRUD screen = new core.bobeda.CRUD(core.Mode.SELECTION,ord);
        core.Dialog dialog = new core.Dialog(screen);
        dialog.setContent(screen);
        Return ret = null;
        if(screen.getBusinesDocument() != null)
        {
            try 
            {
                ret = ord.upPOFile(dbserver, screen.getBusinesDocument());                
                if(ret.isFlag())
                {
                    dbserver.commit();
                    reloadTable();
                    JOptionPane.showMessageDialog(this,
                        "Operacion completada",
                        "Cofirmacion",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
                else
                {
                    dbserver.rollback();
                    reloadTable();
                    JOptionPane.showMessageDialog(this,
                        "Operacion Fallida",
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(Read.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }//GEN-LAST:event_mnPOFindActionPerformed

    private void mnPOUnLinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnPOUnLinkActionPerformed
        ServiceQuotation ord = getSelectItem();
        if(ord == null) return;
        
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
        
        Return ret = null;
        try 
        {
            ret = ord.upPOFile(dbserver, null);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(Read.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
        }  
        if(ret.isFlag())
        {
            try 
            {
                dbserver.commit();
                reloadTable();
                JOptionPane.showMessageDialog(this,
                "Operacion completa",
                "Confirmación",
                JOptionPane.INFORMATION_MESSAGE
            );
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(Read.class.getName()).log(Level.SEVERE, null, ex);
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
                JOptionPane.showMessageDialog(this,
                    ret.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(Read.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }
    }//GEN-LAST:event_mnPOUnLinkActionPerformed

    private void mnPOViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnPOViewActionPerformed
        ServiceQuotation ord = getSelectItem();
        if(ord == null) return;
        
        
        if(ord.getPOFile() == null)
        {
            JOptionPane.showMessageDialog(this,
                "No hay PO asociado",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        try
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
            
            FTP ftpServer = new FTP();
            boolean expResult = false;
            try 
            {
                expResult = ftpServer.connect(serverConfig);
            } 
            catch (IOException ex)
            {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                );
                return;           
            }
                        
            File into = new File(System.getProperty("java.io.tmpdir"));
            Business archivo = ord.getPOFile();
            if(!archivo.getBobeda().download(dbserver,ftpServer,into))
            {
                JOptionPane.showMessageDialog(this,
                "Falló la descarga de archivo.",
                "Error externo",
                JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            archivo.getBobeda().getDownloadedFile().close();
            Desktop.getDesktop().open(new File(archivo.getBobeda().getDownloadFileName()));            
        }
        catch (SQLException | IOException ex)
        {
            //Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Error externo",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_mnPOViewActionPerformed

    public void reloadTable() 
    {
        if(txSearch.getText().length() > 0)
        {
            quickSearch(txSearch.getText(),txCount.getText());
        }
        else
        {
            update(txCount.getText());
        }
    }
    
    /**
     * Escribe en el archvo indicado una linea con formato CSV para nodo 
     * pasado como parámetro.
     * @param node
     * @param file 
     */
    private void generateExport(OrdenNode node,FileWriter file) 
    {
        int count = node.getChildCount();
        ServiceQuotation ord;
        //Para cada nodo hijo del node indicado
        for(int i = 0; i < count; i++)
        {
            ord = ((OrdenNode)node.getChildAt(i)).getData();            
            if(ord.getFolio() != null & node.getParent() != null)
            {
                try 
                {
                    String row = "";
                    row += ord.getState().getName();
                    row += ",";
                    row += ord.getFolio();
                    row += ",";
                    row += ord.getOwner().toString();
                    row += ",";
                    row += ord.getCompany().toString().replaceFirst(",", " ");
                    row += ",";
                    if(ord.getFhETA() != null)
                    {
                        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                        row += df.format(ord.getFhETA());
                        row += ",";
                    }
                    else
                    {
                        row += "";
                        row += ",";
                    }
                    if(ord.getSA() != null)
                    {
                        row += ord.getSA();
                        row += "\n";
                    }
                    else
                    {
                        row += "";
                        row += "\n";
                    }
                    file.append(row);
                }
                catch (IOException ex) 
                {
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);            
                    JOptionPane.showMessageDialog(this,
                        "Falló la genración del Arcivo",
                        "Error externo",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            }           
            //Si tiene hijos se hace una llamada recursiva.
            if(node.getChildAt(i).getChildCount() > 0)
            {
                generateExport((OrdenNode)node.getChildAt(i),file);
            }
        }
    }
    
    private void quickSearch(String text, String lenght) 
    {
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
        buildTable(db, cred, lenght, text,Mode.LIST);        
    }

    private void update(String lenght) 
    {
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
        buildTable(db, cred, lenght, null,Mode.LIST);
    }

    private ServiceQuotation getSelectItem() 
    {
        if(tbList.getSelectedRow()>-1)
        {
            TreeSelectionModel tsm = tbList.getTreeSelectionModel();
            TreeNode selectedNode = (TreeNode) tsm.getSelectionPath().getLastPathComponent();
            OrdenNode ordNode = (OrdenNode) selectedNode;
            return ordNode.getData();
        }
        else
        {
            JOptionPane.showMessageDialog(this,
                "Seleccione un registro",
                "Error Externo",
                JOptionPane.ERROR_MESSAGE
                );
            return null;
        } 
    }
    
    private List<ServiceQuotation> getSelectItems() 
    {
        if(tbList.getSelectedRow() > -1)
        {
            tbList.getTreeSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
            TreeSelectionModel tsm = tbList.getTreeSelectionModel();
            TreePath[] selectedNodes = tsm.getSelectionPaths();            
            ArrayList<ServiceQuotation> ordenes = new ArrayList<>();
            for(TreePath path : selectedNodes)
            {
                OrdenNode ordNode = (OrdenNode) path.getLastPathComponent();
                ordenes.add(ordNode.getData());
            }
            return ordenes;
        }
        else
        {
            JOptionPane.showMessageDialog(this,
                "Seleccione un registro",
                "Error Externo",
                JOptionPane.ERROR_MESSAGE
                );
            return null;
        } 
    }

    private void enableAcces() 
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

        if(cred.acces(dbserver,"Servicios.Orcom.Edit"))
        {
            mnEdit.setVisible(true);  
        }
        else
        {
            mnEdit.setVisible(false); 
        }    
        if(cred.acces(dbserver,"Servicios.Orcom.Autho"))
        {
            mnAutho.setVisible(true);
        }
        else
        {
            mnAutho.setVisible(false);
        }
        if(cred.acces(dbserver,"Servicios.Orcom.ETA"))
        {
            mnETA.setVisible(true);
            mnETAs.setVisible(true);
        }
        else
        {
            mnETA.setVisible(false);
            mnETAs.setVisible(false);
        }
        if(cred.acces(dbserver,"Servicios.Orcom.Arribo"))
        {
            mnTransito.setVisible(true);
        }
        else
        {
            mnTransito.setVisible(false);
        }
        if(cred.acces(dbserver,"Servicios.Orcom.Surtido"))
        {
            mnSurtir.setVisible(true);
        }
        else
        {
            mnSurtir.setVisible(false);
        }
        if(cred.acces(dbserver,"Servicios.Orcom.Finalizado"))
        {
            mnEnd.setVisible(true);
        }
        else
        {
            mnEnd.setVisible(false);
        }
        if(cred.acces(dbserver,"Servicios.Orcom.AddDoc"))
        {
            mnCreate.setVisible(true);
        }
        else
        {
            mnCreate.setVisible(false);
        }
        if(cred.acces(dbserver,"Servicios.Orcom.Cancel"))
        {
            mnCancel.setVisible(true);
        }
        else
        {
            mnCancel.setVisible(false);
        }
        if(cred.acces(dbserver, "Servicios.Orcom.change.owner"))
        {
            mnChangeOwner.setVisible(true);
        }
        else
        {
            mnChangeOwner.setVisible(false);            
        }
        if(cred.acces(dbserver, "Servicios.Orcom.change.client"))
        {
            mnChangeClient.setVisible(true);
        }
        else
        {
            mnChangeClient.setVisible(false);            
        }
        if(cred.acces(dbserver, "Servicios.Orcom.change.folio"))
        {
            mnChangeFolio.setVisible(true);
        }
        else
        {
            mnChangeFolio.setVisible(false);            
        }
        if(cred.acces(dbserver,"services.rl.write") )
        {
            mnAddWork.setVisible(true);
        }
        else
        {
            mnAddWork.setVisible(false);
        }        
        if(cred.acces(dbserver,"bobeda.asociar") )
        {
            mnPOUp.setVisible(true);
        }
        else
        {
            mnPOUp.setVisible(false);
        }        
        if(cred.acces(dbserver,"bobeda.download") )
        {
            mnPODown.setVisible(true);
        }
        else
        {
            mnPODown.setVisible(false);
        }           
        if(cred.acces(dbserver,"Servicios.Orcom.PO.Unlink"))
        {
            mnPOUnLink.setVisible(true);
        }
        else
        {
            mnPOUnLink.setVisible(false);
        }      
    }

    private void loadCombo(Database db) 
    {
        Throwable th = loadComboDepartments(db);
        if(th != null) 
        {
            JOptionPane.showMessageDialog(this,
                "Falló la visualización de Departamentos",
                "Error Externo",
                JOptionPane.ERROR_MESSAGE
                );
        }
        th = loadComboOffices(db);
        if(th != null) 
        {
            JOptionPane.showMessageDialog(this,
                "Falló la visualización de Oficinas",
                "Error Externo",
                JOptionPane.ERROR_MESSAGE);
        }         
    }

    private Throwable loadComboOffices(Database db) 
    {
        String sql = "SELECT id FROM Offices";
        try 
        {
            Statement stmt = (Statement) db.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            cbOffices.removeAllItems();
            Office office;
            office = new Office("Oficinas...","Oficinas...");
            cbOffices.addItem(office);
            while(rs.next())
            {
                office = new Office(db,rs.getInt(1));
                cbOffices.addItem(office);
            }
            
            return null;
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            return new java.lang.Exception("Falló la visualización de Oficinas",ex);
        }       
    }

    private Throwable loadComboDepartments(Database db) 
    {
        String sql = "SELECT id FROM Departments";
        try 
        {
            Statement stmt = (Statement) db.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            cbDepartments.removeAllItems();
            Department dept;
            dept = new Department("Departments...","Departments...");
            cbDepartments.addItem(dept);
            while(rs.next())
            {
                dept = new Department(db,rs.getInt(1));
                cbDepartments.addItem(dept);
            }
            
            return null;
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            return new java.lang.Exception("Falló la visualización de Oficinas",ex);
        } 
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jdesktop.swingx.JXButton btUpdate;
    private org.jdesktop.swingx.JXComboBox cbDepartments;
    private org.jdesktop.swingx.JXComboBox cbOffices;
    private javax.swing.JCheckBox chPersonal;
    private javax.swing.JScrollPane jScrollPane1;
    private org.jdesktop.swingx.JXLabel jXLabel1;
    private javax.swing.JMenuItem mnAddWork;
    private javax.swing.JMenuItem mnAutho;
    private javax.swing.JMenuItem mnCancel;
    private javax.swing.JMenu mnChange;
    private javax.swing.JMenuItem mnChangeClient;
    private javax.swing.JMenuItem mnChangeFolio;
    private javax.swing.JMenuItem mnChangeOwner;
    private javax.swing.JPopupMenu mnCommand;
    private javax.swing.JMenu mnConfig;
    private javax.swing.JMenu mnConfigLists;
    private javax.swing.JCheckBoxMenuItem mnConfigListsAlmacen;
    private javax.swing.JCheckBoxMenuItem mnConfigListsCancelada;
    private javax.swing.JCheckBoxMenuItem mnConfigListsCotizado;
    private javax.swing.JCheckBoxMenuItem mnConfigListsCreado;
    private javax.swing.JCheckBoxMenuItem mnConfigListsETA;
    private javax.swing.JCheckBoxMenuItem mnConfigListsEntregando;
    private javax.swing.JCheckBoxMenuItem mnConfigListsTerminada;
    private javax.swing.JCheckBoxMenuItem mnConfigListsTransito;
    private javax.swing.JMenuItem mnCreate;
    private javax.swing.JMenuItem mnDetail;
    private javax.swing.JMenuItem mnETA;
    private javax.swing.JMenuItem mnETAs;
    private javax.swing.JMenuItem mnEdit;
    private javax.swing.JMenuItem mnEnd;
    private javax.swing.JMenuItem mnExport;
    private javax.swing.JMenuItem mnInStock;
    private javax.swing.JMenuItem mnPODown;
    private javax.swing.JMenuItem mnPOFind;
    private javax.swing.JMenuItem mnPORead;
    private javax.swing.JMenuItem mnPOUnLink;
    private javax.swing.JMenuItem mnPOUp;
    private javax.swing.JMenuItem mnPOView;
    private javax.swing.JMenuItem mnRollbackArrival;
    private javax.swing.JMenuItem mnRollbackAutho;
    private javax.swing.JMenuItem mnRollbackETA;
    private javax.swing.JMenuItem mnRollbackEdition;
    private javax.swing.JMenuItem mnRollbackEnd;
    private javax.swing.JMenuItem mnRollbackSA;
    private javax.swing.JMenuItem mnRows;
    private javax.swing.JMenuItem mnSelect;
    private javax.swing.JMenuItem mnSurtir;
    private javax.swing.JMenuItem mnTransito;
    private org.jdesktop.swingx.JXTreeTable tbList;
    private org.jdesktop.swingx.JXTextField txCount;
    private org.jdesktop.swingx.JXSearchField txSearch;
    // End of variables declaration//GEN-END:variables

    private void updateMenuList(Database db) 
    {
        String sql = "SELECT attribute,value FROM Configuration WHERE BD = ? and office = ? and user = ? and object = ? ";
        try 
        {
            //Lee la confufuracion dekl componete de la base de datos
            PreparedStatement pstmt = db.getConnection().prepareStatement(sql);
            pstmt.setString(1, cred.getBD());
            pstmt.setString(2, cred.getSuc());
            pstmt.setString(3, cred.getUser().getAlias());
            pstmt.setString(4, "SIIL.Servicios.Orden.Screen.List");
            ResultSet rs = pstmt.executeQuery();
            while(rs.next())
            {
                State edo = new State(-1);
                //el atributo describe el estado de la operacion
                Throwable fill = edo.fill(db, rs.getString(1));
                boolean flag = rs.getString(2).equals("enable");
                switch(rs.getString(1))
                {
                    case "docedit":
                        mnConfigListsCreado.setSelected(flag);
                        break;
                    case "docpen":
                        mnConfigListsCotizado.setSelected(flag);
                        break;
                    case "pedpen":
                        mnConfigListsETA.setSelected(flag);
                        break;
                    case "pedtrans":
                        mnConfigListsTransito.setSelected(flag);
                        break;
                    case "pedArrb":
                        mnConfigListsAlmacen.setSelected(flag);
                        break;
                    case "pedsur":
                        mnConfigListsEntregando.setSelected(flag);
                        break;
                    case "pedfin":
                        mnConfigListsTerminada.setSelected(flag);
                        break;
                    case "cancel":
                        mnConfigListsCancelada.setSelected(flag);
                        break;
                }
            }
        } 
        catch (SQLException ex) 
        {
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * @param dialog the dialog to set
     */
    public void setDialog(JXDialog dialog) {
        this.dialog = dialog;
    }
}
