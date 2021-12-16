
package SIIL.services.trabajo;

import SIIL.Server.Database;
import SIIL.Server.Person;
import SIIL.client.sales.Enterprise;
import SIIL.service.quotation.ServiceQuotation;
import core.ClientComboBoxModel;
import core.MecanicoComboBoxModel;
import core.GeneralCellEditor;
import core.QuotedServiceComboBoxModel;
import core.RemisionComboBoxModel;
import database.mysql.sales.Remision;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;
import org.jdesktop.swingx.JXDialog;
import process.State;
import javax.mail.MessagingException;
import java.util.Date;
import java.io.UnsupportedEncodingException;
import SIIL.trace.Trace;
import SIIL.service.quotation.OrdenAlmacenada;
import static SIIL.servApp.BACKWARD_BD;
import SIIL.Servicios.Orden.OrdenCrear;
import SIIL.core.Office;
import SIIL.service.quotation.OrdenFinalizar;
import static SIIL.servApp.cred;
import SIIL.services.Trabajo;
import SIIL.services.Trabajo.Sheet;
import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;
import core.CSVFileFilter;
import core.Dialog;
import core.DialogContent;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.RowSorter;
import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.ParserConfigurationException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.jdesktop.swingx.JXTextField;
import org.xml.sax.SAXException;
import process.Operational;
import process.Return;
import sales.Invoice;



/**
 *
 * @author Azael Reyes
 */
public class Relacion extends javax.swing.JPanel 
{
    private ClientComboBoxModel clientModel;
    private org.jdesktop.swingx.JXComboBox cbClient;
    private org.jdesktop.swingx.JXComboBox cbMechanic;
    private MecanicoComboBoxModel mecModel;
    private org.jdesktop.swingx.JXComboBox cbQuoServ;
    private QuotedServiceComboBoxModel quoServModel;
    private org.jdesktop.swingx.JXComboBox cbSA;
    private RemisionComboBoxModel saModel;
    private RelacionTableModel model;
    private Database dbserver;
    private boolean editMode;
    private int rowselected;
    private JInternalFrame frame;
    private JTextField txBrief;
    //private TypeView typeView;
    private javax.swing.JPanel parent; 
    private Reload reload;
    private Thread reloadTh;

        
    private void genReportExcel() throws IOException 
    {
        final JFileChooser fc = new JFileChooser();
        //Visualiza el cuadro de guardar c para indicar el archivo.
        String nameFile;
        CSVFileFilter fcFilter = new CSVFileFilter();
        fc.setFileFilter(fcFilter);
        FileSystemView fw = fc.getFileSystemView();
        fc.setCurrentDirectory(fw.getDefaultDirectory());
        int returnVal = fc.showSaveDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            if(!fcFilter.accept(fc.getSelectedFile()))
            {
                fc.setSelectedFile(new File(fc.getSelectedFile().getAbsolutePath() + ".csv"));
            }
        }
        else
        {
            return ;
        }
        FileWriter file;
        try
        {
            file = new FileWriter(fc.getSelectedFile());
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error externo",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        catch (IOException ex)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error externo",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        if(getSelectRow() < 0)
        {
            JOptionPane.showMessageDialog(
                this,
                "Seleccion un registro.",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        openDatabase(true);
        RelacionTableModel model = (RelacionTableModel) tbList.getModel();
        Trabajo trabajo = model.getValueAt(getSelectRow());
                
        String row = "S.A.,Ser. Cot.,Número,Cliente,Mecánico,Descripción,Estado\n";
        file.append(row);        
        List<Trabajo> lst = model.getData();
        for(Trabajo trb : lst)
        {
            row = "";
            if(trb.getSA() != null)
            {
                row += trb.getSA().getFullFolio();
            }
            row += ",";
            if(trb.getQuotedService() != null)
            {
                row += trb.getQuotedService().getFullFolio();
            }
            row += ",";
            if(trb.getCompany() != null)
            {
                row += trb.getCompany().getNumber();
            }
            row += ",";
            if(trb.getCompany() != null)
            {
                row += trb.getCompany().getName();
            }
            row += ",";
            if(trb.getMechanic() != null)
            {
                row += trb.getMechanic().toString();
            }
            row += ",";
            if(trb.getBrief() != null)
            {
                String brief = trb.getBrief().replaceAll(",", " ");
                row += brief;
            }
            row += ",";  
            row += trb.getState().getName();            
            row += "\n";  
            
            file.append(row);
        }        
        file.flush();
        file.close();
    }
    
    private enum TypeView
    {
        SHEET,
        ASSISTANT,
        SEARCH,
        NOVIEW
    }
    
    private void update(int rowselected)
    {
        if(rowselected < 0)
        {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione una fila",
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
            );
            return;        
        }
        openDatabase(true);
        RelacionTableModel model = (RelacionTableModel) tbList.getModel();
        Create create = new Create(Create.Mode.UPDATE,model.getValueAt(rowselected),this,dbserver,getSheet());
        core.Dialog dlg = new core.Dialog(create);
        dlg.setTitle("Actualizar");
        create.setDialog(dlg);
        create.setRelacion(this);
        dlg.setContent((DialogContent) create);
        if(create.isUpdated() && create.isCommitUpdate())
        {
            try 
            {
                dbserver.commit();
                closeDatabase();
                reload(RelacionTableModel.ReadMode.REFRESH,true);
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(Relacion.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(
                    this,
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
                closeDatabase();
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(Relacion.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }        
    }
    
    public Relacion(JInternalFrame frame,String search) 
    {
        initComponents();       
        initComponents2(frame,RelacionTableModel.ReadMode.NOREAD,false);
        //reload.setEnable(false);
        fecha.setVisible(false);
        cbSheet.setVisible(false);
        btUpdate.setVisible(false);
        btFilter.setVisible(false);
        model = (RelacionTableModel) tbList.getModel();
        try 
        {
            model.search(dbserver, search);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(Relacion.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(
                this,
                ex.getMessage() + " ..<",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        //reload.setEnable(true);
    }
        
    public Relacion(JInternalFrame frame,List<Trabajo> list) 
    {
        initComponents();        
        initComponents2(frame,RelacionTableModel.ReadMode.NOREAD,false);        
        RelacionTableModel model = new RelacionTableModel(list);
        tbList.setModel(model);
        fecha.setEnabled(false);
        cbSheet.setEnabled(false);
        reload(RelacionTableModel.ReadMode.REFRESH,false);
        //reload.setEnable(true);
    }
    
        
    public void updateTable(RelacionTableModel.ReadMode mode) 
    {
        if(mode == RelacionTableModel.ReadMode.NOREAD) return;
        
        Sheet sheet = null;
        if(cbSheet.getSelectedIndex() == 0)
        {
            sheet = Sheet.CAMPO;
        }
        else if(cbSheet.getSelectedIndex() == 1)
        {
            sheet = Sheet.TALLER;
        }
        else if(cbSheet.getSelectedIndex() == 2)
        {
            sheet = Sheet.GRUA;
        }
        else if(cbSheet.getSelectedIndex() == 3)
        {
            sheet = Sheet.MTTO;
        }         
        try 
        {
            if(mode == RelacionTableModel.ReadMode.REFRESH)
            {
                model.refresh(dbserver);
            }
            else if(mode == RelacionTableModel.ReadMode.LOAD | mode == RelacionTableModel.ReadMode.RELOAD)
            {
                //System.out.println("Date : " + fecha.getCalendario().getCalendar().getTime());
                model = (RelacionTableModel) tbList.getModel();
                model.setNow(dbserver.getDateToday());
                model.load(dbserver,0,sheet,new java.sql.Date(fecha.getDate().getTime()));
            }
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

    @Deprecated    
    private void configTable() throws SQLException 
    {
        tbList.setDefaultRenderer(Object.class, new CellRenderer()); //colores de filas
        
        if(editMode)
        {
            switch(tbList.getSelectedColumn())
            {
                case 0:
                    configSA();
                    break;
                case 1:
                    configQuote(); 
                    break;
                case 2:
                    configClient();
                    break;
                case 3:
                    configMechanic();
                    break;
                case 4:
                    configBrief();
                    break;
            }
        }   
    }

    private void configBrief() 
    {
        //Bierf
        txBrief = new JXTextField();
        txBrief.setEditable(true);
        txBrief.setBorder(null);
        TableColumn colBrief = tbList.getColumnModel().getColumn(4);
        colBrief.setCellEditor(new GeneralCellEditor(txBrief)); 
        txBrief.addKeyListener(new java.awt.event.KeyAdapter()
        {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                openDatabase(true);         
                try
                {
                    if(evt.getKeyCode() == KeyEvent.VK_ENTER && getSelectRow()> -1)
                    {
                        Trabajo trb = model.getValueAt(getSelectRow());
                        Trace contexTrace = new Trace(BACKWARD_BD, SIIL.servApp.cred.getUser(), "Asignacion de comentario");
                        if(trb.upBrief(dbserver, txBrief.getText(), contexTrace))
                        {
                            dbserver.commit();
                        }
                        else
                        {
                            dbserver.rollback();
                        }
                        openDatabase(true);
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
        });        
    }

    private void configQuote() 
    {
        //Cotizacion de Servico
        cbQuoServ = new org.jdesktop.swingx.JXComboBox();
        cbQuoServ.setEditable(true);
        quoServModel = new QuotedServiceComboBoxModel();
        cbQuoServ.setModel(quoServModel);
        TableColumn colCotServ = tbList.getColumnModel().getColumn(1);
        colCotServ.setCellEditor(new GeneralCellEditor(cbQuoServ)); 
        cbQuoServ.getEditor().getEditorComponent().addKeyListener(new java.awt.event.KeyAdapter()
        {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                openDatabase(true);
                String search = ((JTextField)cbQuoServ.getEditor().getEditorComponent()).getText();                
                try
                {
                    quoServModel.search(dbserver,SIIL.servApp.cred.getOffice(), search,21);
                    ((JTextField)cbQuoServ.getEditor().getEditorComponent()).setText(search);
                    cbQuoServ.setModel(quoServModel);
                    if(evt.getKeyCode() == KeyEvent.VK_ENTER)
                    {
                        if(quoServModel.getSize() == 1)
                        {
                            ServiceQuotation orden = quoServModel.getElementAt(0);
                            Boolean ret = false;
                            try 
                            {
                                ret = saveQuotedService(tbList,orden);
                                if(ret == null)
                                {
                                    dbserver.rollback();
                                }
                                else if(ret == false)
                                {
                                    dbserver.rollback();
                                }
                                else
                                {
                                    dbserver.commit();
                                }
                                openDatabase(true);
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
                        else if(quoServModel.getSize() == 0)
                        {
                            JOptionPane.showMessageDialog(null,
                                    "Quiza la cotizacion existe en CN pero no existe en Tool." ,
                                    "Error Interno",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null,
                                    "Hay " + quoServModel.getSize() + " posibilidades, elija una de la lista." ,
                                    "Error Interno",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                    else if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE)
                    {
                        Boolean ret = false;
                        ret = saveQuotedService(tbList,null);
                        if(ret == null)
                        {
                            dbserver.rollback();
                        }
                        else if(ret == false)
                        {
                            dbserver.rollback();
                        }
                        else
                        {
                            dbserver.commit();
                            cbQuoServ.setSelectedIndex(-1);
                            ((JTextField)cbQuoServ.getEditor().getEditorComponent()).setText("");
                        }
                        openDatabase(true);                        
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
        cbQuoServ.getEditor().getEditorComponent().addFocusListener(new java.awt.event.FocusAdapter()
        {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt)
            {
                ((JTextField)cbQuoServ.getEditor().getEditorComponent()).setText(""); 
            }
        });
        cbQuoServ.addActionListener (new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(cbQuoServ.getSelectedIndex() > -1)
                {
                    ServiceQuotation orden = (ServiceQuotation)cbQuoServ.getSelectedItem();
                    Boolean ret = false;
                    try 
                    {
                        ret = saveQuotedService(tbList,orden);
                        if(ret == null)
                        {
                            dbserver.rollback();
                        }
                        else if(ret == false)
                        {
                            dbserver.rollback();
                        }
                        else
                        {
                            dbserver.commit();
                        }
                        openDatabase(true);
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

    private void configClient() 
    {
        //ComboBox para editar Cliente
        cbClient = new org.jdesktop.swingx.JXComboBox();
        cbClient.setEditable(true);
        clientModel = new ClientComboBoxModel();
        cbClient.setModel(clientModel);
        TableColumn colClient = tbList.getColumnModel().getColumn(2);
        colClient.setCellEditor(new GeneralCellEditor(cbClient));
        String search = null;
        cbClient.getEditor().getEditorComponent().addKeyListener(new java.awt.event.KeyAdapter()
        {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                openDatabase(true);                
                String search = ((JTextField)cbClient.getEditor().getEditorComponent()).getText();                
                try
                {
                    if( getSelectRow() < 0)
                    {
                        JOptionPane.showMessageDialog(null,
                                "Seleccion el registro",
                                "Error Interno",
                                JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                    Trabajo tb = model.getValueAt(getSelectRow());
                    clientModel.search(dbserver, search,tb.getCompany());
                    ((JTextField)cbClient.getEditor().getEditorComponent()).setText(search);
                    cbClient.setModel(clientModel);
                    if(evt.getKeyCode() == KeyEvent.VK_ENTER)
                    {
                        if(clientModel.getSize() == 1)
                        {
                            Boolean ret = saveClient(tbList,clientModel.getElementAt(0));
                            if(ret == true)
                            {
                                dbserver.commit();
                            }
                            else
                            {
                                dbserver.rollback();
                            }
                            openDatabase(true);
                        }
                        else if(clientModel.getSize() > 1)
                        {
                            JOptionPane.showMessageDialog(null,
                                    "Hay " + clientModel.getSize() + " posibilidades, elija una de la lista." ,
                                    "Dato ambiguo",
                                    JOptionPane.WARNING_MESSAGE
                            );
                        }
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
                    return;
                }
            }
        });
        cbClient.getEditor().getEditorComponent().addFocusListener(new java.awt.event.FocusAdapter()
        {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt)
            {
                ((JTextField)cbClient.getEditor().getEditorComponent()).setText("");
            }
        });
        
        cbClient.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(cbClient.getSelectedIndex() > -1)
                {
                    openDatabase(true);
                    Enterprise enterprise = (Enterprise)cbClient.getSelectedItem();
                    Boolean ret = false;
                    try
                    {
                        ret = saveClient(tbList,enterprise);
                        if(ret == true)
                        {
                            dbserver.commit();
                        }
                        else
                        {
                            dbserver.rollback();
                        }
                        openDatabase(true);
                    }
                    catch (SQLException ex)
                    {
                        //Logger.getLogger(Relacion.class.getName()).log(Level.SEVERE, null, ex);JOptionPane.showMessageDialog(null,
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

    private void configMechanic() throws SQLException 
    {
        //ComboBox para editar mecanicos
        cbMechanic = new org.jdesktop.swingx.JXComboBox();
        cbMechanic.setEditable(true);
        mecModel = new MecanicoComboBoxModel();
        mecModel.fill(dbserver, SIIL.servApp.cred.getOffice());
        cbMechanic.setModel(mecModel);
        TableColumn colMechanic = tbList.getColumnModel().getColumn(3);
        colMechanic.setCellEditor(new GeneralCellEditor(cbMechanic));
        cbMechanic.getEditor().getEditorComponent().addKeyListener(new java.awt.event.KeyAdapter()
        {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                openDatabase(true);
                String search = ((JTextField)cbMechanic.getEditor().getEditorComponent()).getText();
                try
                {
                    //System.out.println("Buscando remision " + evt.getKeyCode() + ":" + search);
                    mecModel.search(dbserver,SIIL.servApp.cred.getOffice(),21,search);
                    if(evt.getKeyCode() == KeyEvent.VK_ENTER)
                    {
                        //System.out.println("Inicio de Importacion para " + search);
                        if(mecModel.getSize() == 0)// si no se encontro alguna remision
                        {
                            ;
                        }
                        else if(mecModel.getSize() == 1)
                        {
                            if( getSelectRow() < 0)
                            {
                                JOptionPane.showMessageDialog(
                                        null,
                                        "Seleccion el registro",
                                        "Error Interno",
                                        JOptionPane.ERROR_MESSAGE
                                );
                                return;
                            }
                            Trabajo trabajo = model.getValueAt(getSelectRow());
                            Person mecanico = mecModel.getElementAt(0);
                            Boolean ret = activeAsigned(dbserver,trabajo,mecanico);
                            if(ret == null)
                            {
                                dbserver.commit();
                            }
                            else if(ret == true)
                            {
                                dbserver.commit();
                            }
                            else
                            {
                                dbserver.rollback();
                            }
                            openDatabase(true);
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
                    else if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE)
                    {
                        if( getSelectRow() < 0)
                        {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Seleccion el registro",
                                    "Error Interno",
                                    JOptionPane.ERROR_MESSAGE
                            );
                            return;
                        }
                        Trabajo trabajo = model.getValueAt(getSelectRow());
                        Person mecanico = mecModel.getElementAt(0);
                        Boolean ret = activeAsigned(dbserver,trabajo,null);
                        if(ret == null)
                        {
                            dbserver.commit();
                            ((JTextField)cbMechanic.getEditor().getEditorComponent()).setText("");
                            cbMechanic.setSelectedIndex(-1);
                        }
                        else if(ret == true)
                        {
                            dbserver.commit();
                            ((JTextField)cbMechanic.getEditor().getEditorComponent()).setText("");
                            cbMechanic.setSelectedIndex(-1);
                        }
                        else
                        {
                            dbserver.rollback();
                        }
                        openDatabase(true);
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
        cbMechanic.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if( getSelectRow() > -1 && cbMechanic.getSelectedIndex() > -1 )
                {
                    openDatabase(true);
                    Person mecanico = (Person) cbMechanic.getSelectedItem();
                    Trabajo trabajo = model.getValueAt(getSelectRow());
                    Boolean ret = false;
                    try
                    {
                        ret = activeAsigned(dbserver,trabajo,mecanico);
                        if(ret == null)
                        {
                            dbserver.rollback();
                        }
                        else if(ret == false)
                        {
                            dbserver.rollback();
                        }
                        else
                        {
                            dbserver.commit();
                        }
                        openDatabase(true);
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

    private void configSA() 
    {
        //SA
        cbSA = new org.jdesktop.swingx.JXComboBox();
        TableColumn colSA = tbList.getColumnModel().getColumn(0);
        colSA.setCellEditor(new GeneralCellEditor(cbSA));
        cbSA.setEditable(true);
        saModel = new RemisionComboBoxModel();
        cbSA.setModel(saModel);
        cbSA.getEditor().getEditorComponent().addKeyListener(new java.awt.event.KeyAdapter()
        {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                openDatabase(true);
                String search = ((JTextField)cbSA.getEditor().getEditorComponent()).getText();
                try
                {
                    //System.out.println("Buscando remision " + evt.getKeyCode() + ":" + search);
                    saModel.search(dbserver,SIIL.servApp.cred.getOffice(), search,21);
                    if(evt.getKeyCode() == KeyEvent.VK_ENTER)
                    {
                        System.out.println("Inicio de Importacion para " + search);
                        if(saModel.getSize() == 0)// si no se encontro alguna remision
                        {//importar
                            Remision remision = Remision.fromCN(dbserver,SIIL.servApp.cred.getOffice(),search,SIIL.servApp.cred.getUser());
                            if(remision != null)
                            {
                                System.out.println("Se encontro Remision en CN " + search);
                                if( getSelectRow() < 0)
                                {
                                    JOptionPane.showMessageDialog(
                                            null,
                                            "Seleccion el registro",
                                            "Error Interno",
                                            JOptionPane.ERROR_MESSAGE
                                    );
                                    return;
                                }
                                Trabajo tb = model.getValueAt(getSelectRow());
                                Boolean ret = saveSA(dbserver,tb,remision);
                                if(ret == true || ret == null)
                                {
                                    dbserver.commit();
                                }
                                else
                                {
                                    dbserver.rollback();
                                }
                                
                            }
                            else
                            {
                                //System.out.println("No se encontro Remision en CN " + search);
                            }
                        }
                        else if(saModel.getSize() == 1)
                        {
                            if( getSelectRow() < 0)
                            {
                                JOptionPane.showMessageDialog(
                                        null,
                                        "Seleccion el registro",
                                        "Error Interno",
                                        JOptionPane.ERROR_MESSAGE
                                );
                                return;
                            }
                            Trabajo tb = model.getValueAt(getSelectRow());
                            Remision remision = saModel.getElementAt(0);
                            Boolean ret = saveSA(dbserver,tb,remision);
                            if(ret == true || ret == null)
                            {
                                dbserver.commit();
                            }
                            else
                            {
                                dbserver.rollback();
                            }
                            openDatabase(true);
                        }
                        else if(saModel.getSize() > 1)
                        {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Hay " + saModel.getSize() + " seleccione un de la ista.",
                                    "Error Externo",
                                    JOptionPane.WARNING_MESSAGE
                            );
                        }
                    }
                    else if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE)
                    {
                        Trabajo tb = model.getValueAt(getSelectRow());
                        Remision remision = saModel.getElementAt(0);
                        Boolean ret = saveSA(dbserver,tb,null);
                        if(ret == null)
                        {
                            dbserver.commit();
                            ((JTextField)cbSA.getEditor().getEditorComponent()).setText("");
                            cbSA.setSelectedIndex(-1);
                        }
                        else if(ret == true)
                        {
                            dbserver.commit();
                            ((JTextField)cbSA.getEditor().getEditorComponent()).setText("");
                            cbSA.setSelectedIndex(-1);
                        }
                        else
                        {
                            dbserver.rollback();
                        }
                        openDatabase(true);
                    }
                    ((JTextField)cbSA.getEditor().getEditorComponent()).setText(search);
                    cbSA.setModel(saModel);
                }
                catch (IOException | ParseException | SQLException ex)
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
        cbSA.getEditor().getEditorComponent().addFocusListener(new java.awt.event.FocusAdapter()
        {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt)
            {
                ((JTextField)cbSA.getEditor().getEditorComponent()).setText("");
            }
        });
        cbSA.addActionListener (new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(cbSA.getSelectedIndex() > -1)
                {
                    openDatabase(true);
                    
                    Trabajo tb = model.getValueAt(getSelectRow());
                    Remision remision = (Remision)cbSA.getSelectedItem();
                    Boolean ret = saveSA(dbserver,tb,remision);
                    if(ret == true || ret == null)
                    {
                        try
                        {
                            dbserver.commit();
                            openDatabase(true);
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
                    else
                    {
                        try
                        {
                            dbserver.rollback();
                        }
                        catch (SQLException ex)
                        {
                            Logger.getLogger(Relacion.class.getName()).log(Level.SEVERE, null, ex);
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

    private int getSelectRow() 
    {
        if(tbList.getSelectedRow() < 0 ) 
        {
            return tbList.getSelectedRow();
        }
        else
        {
            RelacionTableModel model = (RelacionTableModel) tbList.getModel();
            Trabajo trabajo = model.getValueAt(tbList.convertRowIndexToModel(tbList.getSelectedRow()));
            return tbList.convertRowIndexToModel(tbList.getSelectedRow());
        }
    }
    
    public java.util.List<Trabajo> getSelectRows()
    {
        RowSorter sorter = tbList.getRowSorter();
        java.util.List<Trabajo>  val = new java.util.ArrayList<>();
        RelacionTableModel model = (RelacionTableModel) tbList.getModel();
        int[] rows = tbList.getSelectedRows();
        if(sorter != null) 
        {
            for(int row : rows)
            {
                val.add(model.getValueAt(tbList.convertRowIndexToModel(row)));
            }
        }
        else
        {
            for(int row : rows)
            {
                val.add(model.getValueAt(row));
            }
        }
        
        return val;
    }

    private void closeEdition(RelacionTableModel.ReadMode fast) 
    {
        closeDatabase();
        setEditMode(false);
        reload(fast,false);
    }

    private void closeDatabase() 
    {
        if(dbserver != null)
        {
            dbserver.close();
            dbserver = null;
        }
    }
    
    public void searchClient()
    {
        ;
    }
    
    public Boolean saveSA(Database database,Trabajo trb,Remision remision)
    {
        Boolean retQ = null,retSA = null;   
        try 
        {
            //if(remision == null) return false;
            //if(remision.getID() < 1) return false;
            Trace contexTrace = new Trace(BACKWARD_BD, SIIL.servApp.cred.getUser(), "Asignacion de SA");
            contexTrace.insert(dbserver);
            retSA = trb.upSA(dbserver,remision,contexTrace);
            if(retSA)
            {
                System.out.println("Se asigno el SA");
                retQ = sychQuotedServiceSA(dbserver,trb);
                if(retQ == null)
                {
                    //database.commit();
                    return true;
                }
                else if(retQ == true)
                {
                    return true;
                }
                else
                {
                    //System.err.println("No hay cambios en la cotizacion");
                    return false;
                }
            }
            else
            {
                //System.err.println("Fallo la asignacion del SA");
                //database.rollback();
                return false;
            }
        }
        catch (MessagingException | UnsupportedEncodingException | SQLException ex) 
        {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
        }
        return false;
    }
    
    public Boolean saveQuotedService(javax.swing.JTable table,ServiceQuotation orden) throws SQLException
    {
        if(getSelectRow() < 0) return false;
        
        RelacionTableModel model = (RelacionTableModel) table.getModel();
        Trabajo trb = model.getValueAt(getSelectRow());     
        Trace contexTrace = new Trace(BACKWARD_BD, SIIL.servApp.cred.getUser(), "Asignacion de Cotizacion");
        contexTrace.insert(dbserver);  
        contexTrace.addOperator(dbserver, "Relacion De Trabajo");
        return trb.upQuotedService(dbserver,orden,contexTrace);    
    }
    
    public Boolean saveClient(javax.swing.JTable table,Enterprise enterprise) throws SQLException
    {
        if(getSelectRow() < 0) return false;
        if(enterprise == null)  return false;
        
        RelacionTableModel model = (RelacionTableModel) table.getModel();
        Trabajo trb = model.getValueAt(getSelectRow());        
        Trace contexTrace = new Trace(BACKWARD_BD, SIIL.servApp.cred.getUser(), "Asignacion de Cliente");
        contexTrace.insert(dbserver);  
        contexTrace.addOperator(dbserver, "Relacion De Trabajo");
        return trb.upClient(dbserver,enterprise,contexTrace);    
    }
      
    /**
     * Esta funcion en llamada cuando se selecciona un persona en cbMechanic
     * @param table
     * @param cbMechanic 
     */
    public Boolean saveMechanic(javax.swing.JTable table,org.jdesktop.swingx.JXComboBox cbMechanic) throws SQLException
    {
        if(getSelectRow() < 0) return false;
        
        RelacionTableModel model = (RelacionTableModel) table.getModel();
        Trabajo trb = model.getValueAt(getSelectRow());
        
        Person person = (Person)cbMechanic.getSelectedItem();
        if(person == null) return false;
        if(person.getpID() < 1) return false;
        Trace contexTrace = new Trace(BACKWARD_BD, SIIL.servApp.cred.getUser(), "Asignacion de Mecanico");
        contexTrace.insert(dbserver);  
        contexTrace.addOperator(dbserver, "Relacion De Trabajo");
        return trb.upMechanic(dbserver,person,contexTrace);
    }
    
    /**
     * Creates new form Relacion
     */
    public Relacion(JInternalFrame frame) 
    {
        initComponents();
        initComponents2(frame,RelacionTableModel.ReadMode.LOAD,true);
        //reload.setEnable(false);
    }

    private void addMouseListenerRecrusively(final Container container)
    {        
        for (Component component: container.getComponents())
        {
            if (component instanceof Container) 
            {
                addMouseListenerRecrusively((Container) component);
            }
        }
        container.addMouseListener(new java.awt.event.MouseAdapter() 
        {
            public void mouseClicked(java.awt.event.MouseEvent evt) 
            {
                //System.out.println("Mouse Clicked : " + container);
                fechaMouseClicked(evt);
            }
        });
    }
    
    private void initComponents2(JInternalFrame frame1,RelacionTableModel.ReadMode fast,boolean autoLoad) 
    {
        fecha.setRelacion(this);
        tbList.setAutoCreateRowSorter(true);
        tbList.setDefaultRenderer(Object.class, new CellRenderer());
        TableRowUtilities.addNumberColumn(tbList, 1, true);
        tbList.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tbList.getColumnModel().getColumn(0).setPreferredWidth(80);
        tbList.getColumnModel().getColumn(3).setPreferredWidth(150); 
        tbList.getColumnModel().getColumn(4).setPreferredWidth(150);
        tbList.getColumnModel().getColumn(5).setPreferredWidth(500);
        //tbList.getColumnModel().getColumn(6).setPreferredWidth(500); 
        fecha.setDate(new Date());
        this.frame = frame1;   
        reload(fast,false);     
        //setEditMode(false);
        if(cred.acces(dbserver,"services.rl.write"))
        {
            mnAten.setEnabled(true);
            mnChangeSheetCampo.setEnabled(true);
            mnChangeSheetGrua.setEnabled(true);
            mnChangeSheetTaller.setEnabled(true);
            mnCotizar.setEnabled(true);
            mnCreate.setEnabled(true);
            mnDelete.setEnabled(true);
            mnEnd.setEnabled(true);
            mnUrge.setEnabled(true);
            mnMove.setEnabled(true);
            mnFacturar.setEnabled(true);
        }
        else
        {
            mnAten.setEnabled(false);
            mnChangeSheetCampo.setEnabled(false);
            mnChangeSheetGrua.setEnabled(false);
            mnChangeSheetTaller.setEnabled(false);
            mnCotizar.setEnabled(false);
            mnCreate.setEnabled(false);
            mnDelete.setEnabled(false);
            mnEnd.setEnabled(false);
            mnUrge.setEnabled(false);
            mnMove.setEnabled(false);
            mnFacturar.setEnabled(false);
        }
        /*
        reload = new Reload(this,autoLoad);
        reloadTh = new Thread(reload);        
        reloadTh.start();        
        addWindowListener(new WindowAdapter() 
        {
            @Override
            public void windowClosing(WindowEvent e) 
            {
                if(reload != null) reload.setRun(false);
                closeDatabase();
                reloadTh = null;
                reload = null;                   
            }
        });  
        */ 
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
        jXTable1 = new org.jdesktop.swingx.JXTable();
        mnMain = new javax.swing.JPopupMenu();
        mnCreate = new javax.swing.JMenuItem();
        mnAcept = new javax.swing.JMenuItem();
        mnUrge = new javax.swing.JMenuItem();
        mnAsigned = new javax.swing.JMenuItem();
        mnAten = new javax.swing.JMenuItem();
        mnCotizar = new javax.swing.JMenuItem();
        mnEnd = new javax.swing.JMenuItem();
        mnMove = new javax.swing.JMenuItem();
        mnPrintSA = new javax.swing.JMenuItem();
        mnChangeSheetCampo = new javax.swing.JMenuItem();
        mnChangeSheetTaller = new javax.swing.JMenuItem();
        mnChangeSheetGrua = new javax.swing.JMenuItem();
        mnChangeSheetManto = new javax.swing.JMenuItem();
        mnDelete = new javax.swing.JMenuItem();
        mnVerQouteRows = new javax.swing.JMenuItem();
        mnVerQuotedDetail = new javax.swing.JMenuItem();
        mnExcel = new javax.swing.JMenuItem();
        mnFacturar = new javax.swing.JMenuItem();
        cbSheet = new org.jdesktop.swingx.JXComboBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbList = new javax.swing.JTable();
        btUpdate = new org.jdesktop.swingx.JXButton();
        btFilter = new org.jdesktop.swingx.JXButton();
        fecha = new core.calendar.DateChooser();

        jXTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jXTable1);

        mnCreate.setText("Agregar");
        mnCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnCreateActionPerformed(evt);
            }
        });
        mnMain.add(mnCreate);

        mnAcept.setText("Aceptado");
        mnAcept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAceptActionPerformed(evt);
            }
        });
        mnMain.add(mnAcept);

        mnUrge.setText("Urgente");
        mnUrge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnUrgeActionPerformed(evt);
            }
        });
        mnMain.add(mnUrge);

        mnAsigned.setText("Asignado");
        mnAsigned.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAsignedActionPerformed(evt);
            }
        });
        mnMain.add(mnAsigned);

        mnAten.setText("Atendiendo");
        mnAten.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAtenActionPerformed(evt);
            }
        });
        mnMain.add(mnAten);

        mnCotizar.setText("Cotizar");
        mnCotizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnCotizarActionPerformed(evt);
            }
        });
        mnMain.add(mnCotizar);

        mnEnd.setText("Terminar");
        mnEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnEndActionPerformed(evt);
            }
        });
        mnMain.add(mnEnd);

        mnMove.setText("Mover a...");
        mnMove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnMoveActionPerformed(evt);
            }
        });
        mnMain.add(mnMove);

        mnPrintSA.setText("Imprimir SA");
        mnPrintSA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnPrintSAActionPerformed(evt);
            }
        });
        mnMain.add(mnPrintSA);

        mnChangeSheetCampo.setText("Hoja de Campo");
        mnChangeSheetCampo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnChangeSheetCampoActionPerformed(evt);
            }
        });
        mnMain.add(mnChangeSheetCampo);

        mnChangeSheetTaller.setText("Hoja de Taller");
        mnChangeSheetTaller.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnChangeSheetTallerActionPerformed(evt);
            }
        });
        mnMain.add(mnChangeSheetTaller);

        mnChangeSheetGrua.setText("Hoja de Grua");
        mnChangeSheetGrua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnChangeSheetGruaActionPerformed(evt);
            }
        });
        mnMain.add(mnChangeSheetGrua);

        mnChangeSheetManto.setText("Hoja Manto.");
        mnChangeSheetManto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnChangeSheetMantoActionPerformed(evt);
            }
        });
        mnMain.add(mnChangeSheetManto);

        mnDelete.setText("Eliminar");
        mnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnDeleteActionPerformed(evt);
            }
        });
        mnMain.add(mnDelete);

        mnVerQouteRows.setText("Ver Reglones de Cotizacion");
        mnVerQouteRows.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnVerQouteRowsActionPerformed(evt);
            }
        });
        mnMain.add(mnVerQouteRows);

        mnVerQuotedDetail.setText("Ver Detalles de Cotizacion");
        mnVerQuotedDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnVerQuotedDetailActionPerformed(evt);
            }
        });
        mnMain.add(mnVerQuotedDetail);

        mnExcel.setText("Excel");
        mnExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnExcelActionPerformed(evt);
            }
        });
        mnMain.add(mnExcel);

        mnFacturar.setText("Facturar");
        mnFacturar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnFacturarActionPerformed(evt);
            }
        });
        mnMain.add(mnFacturar);

        setComponentPopupMenu(mnMain);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        cbSheet.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Campo", "Taller", "Grua", "Mtto." }));
        cbSheet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSheetActionPerformed(evt);
            }
        });

        tbList.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        tbList.setModel(new RelacionTableModel());
        tbList.setComponentPopupMenu(mnMain);
        tbList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbListMouseClicked(evt);
            }
        });
        tbList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbListKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(tbList);

        btUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/reload.png"))); // NOI18N
        btUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUpdateActionPerformed(evt);
            }
        });

        btFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/search.png"))); // NOI18N
        btFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFilterActionPerformed(evt);
            }
        });

        fecha.setToolTipText("Fecha");
        fecha.setDateFormatString("dd/MMM/yyyy");
        fecha.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fechaMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cbSheet, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 698, Short.MAX_VALUE)
                .addComponent(btUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(cbSheet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btFilter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 512, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cbSheetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSheetActionPerformed
        reload(RelacionTableModel.ReadMode.LOAD,false);
    }//GEN-LAST:event_cbSheetActionPerformed

    private Sheet getSheet()
    {
        Sheet sheet = null;
        if(cbSheet.getSelectedIndex() == 0)
        {
            sheet = Sheet.CAMPO;
        }
        else if(cbSheet.getSelectedIndex() == 1)
        {
            sheet = Sheet.TALLER;
        }
        else if(cbSheet.getSelectedIndex() == 2)
        {
            sheet = Sheet.GRUA;
        }
        else if(cbSheet.getSelectedIndex() == 3)
        {
            sheet = Sheet.MTTO;
        } 
        return sheet;
    }
    
    private void mnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnCreateActionPerformed
        openDatabase(true);  
        Create create = new Create(Create.Mode.CREATE,null,null,null,getSheet());
        core.Dialog dlg = new core.Dialog(create);
        create.setDialog(dlg);
        create.setRelacion(this);
        dlg.setContent(create);
        reload(RelacionTableModel.ReadMode.RELOAD, false);
    }//GEN-LAST:event_mnCreateActionPerformed

    private void mnUrgeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnUrgeActionPerformed
        if(getSelectRow() < 0)
        {
            JOptionPane.showMessageDialog(this,
                "Seleccion un registro.",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        openDatabase(true);
        RelacionTableModel model = (RelacionTableModel) tbList.getModel();
        Trabajo trabajo = model.getValueAt(getSelectRow());
        State state;
        boolean ret = false;
        try
        {
            state = new State(-1);
            state.select(dbserver, State.Steps.RT_URGE);
            Trace contexTrace = new Trace(BACKWARD_BD, SIIL.servApp.cred.getUser(), "Cambio de Estado");
            contexTrace.insert(dbserver);
            contexTrace.addOperator(dbserver, "¿Quien hace?");
            if(state != null) ret = trabajo.upState(dbserver,state,contexTrace);            
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
        if(ret)
        {
            try
            {
                dbserver.commit();
                closeDatabase();
                reload(RelacionTableModel.ReadMode.REFRESH,false);
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(Relacion.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog
                (
                    this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }
        else
        {
            try 
            {
                dbserver.rollback();
                closeDatabase();
                reload(RelacionTableModel.ReadMode.REFRESH,false);
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(Relacion.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }
    }//GEN-LAST:event_mnUrgeActionPerformed

    private void mnCotizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnCotizarActionPerformed
        if(getSelectRow() < 0){
            JOptionPane.showMessageDialog(this,
                "Seleccion un registro.",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }       
        
        /*SIIL.core.config.Server serverConfig = new SIIL.core.config.Server("server.xml"); 
        try 
        {
            serverConfig = new SIIL.core.config.Server("server.xml");
            database = new Database(serverConfig);
        }
        catch (ClassNotFoundException | SQLException ex) 
        {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }*/

        openDatabase(true);
        RelacionTableModel model = (RelacionTableModel) tbList.getModel();
        Trabajo trabajo = model.getValueAt(getSelectRow());
        State state;
        boolean ret = false;
        
        
        Trace contexTrace = new Trace(BACKWARD_BD, SIIL.servApp.cred.getUser(), "Asignacion de Cotizacion");
        try 
        {  
            contexTrace.insert(dbserver);
            contexTrace.addOperator(dbserver, "¿Quien hace?");
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(Relacion.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
            );
        }
        ret = activeQuoting(dbserver, trabajo,contexTrace);
        
        
        //Crear cotizacion
        int option = JOptionPane.showConfirmDialog(
            this,
            "Desea crear un nueva cotizacion?",
            "Confirm operacion",
            JOptionPane.YES_NO_OPTION);
        if(option == JOptionPane.YES_OPTION)
        {
            SIIL.Servicios.Orden.Create2 qS = new SIIL.Servicios.Orden.Create2(SIIL.servApp.cred,null);
            JXDialog dlg = new JXDialog(qS);
            qS.setDialog(dlg);
            dlg.setContentPane(qS);
            dlg.setSize(qS.getPreferredSize());
            dlg.setModal(true);
            dlg.setVisible(true); 
            OrdenCrear newOrden = qS.getOrden();
            if(newOrden != null)
            {
                if(newOrden.getID() > 1)
                {
                    try 
                    {
                        //si falla la operacin se 
                        Boolean ret2 = trabajo.upQuotedService(dbserver, newOrden,contexTrace);
                        if(ret2 == null)
                        {
                            ret = false;
                        }
                        else if(ret2 == true)
                        {
                            ;
                        }
                        else
                        {
                            ret = false;
                        }
                    } 
                    catch (SQLException ex) 
                    {                        
                        //Logger.getLogger(Relacion.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(this,
                                ex.getMessage(),
                                "Error Interno",
                                JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                }
            } 
        }
        
        if(ret)
        {
            try 
            {
                dbserver.commit();
                closeDatabase();
                reload(RelacionTableModel.ReadMode.REFRESH,false);
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(Relacion.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }
        else
        {
            try 
            {
                dbserver.rollback();
                closeDatabase();
                reload(RelacionTableModel.ReadMode.REFRESH,false);
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(Relacion.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }
    }//GEN-LAST:event_mnCotizarActionPerformed

    private void mnEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnEndActionPerformed
        if(getSelectRow() < 0){
            JOptionPane.showMessageDialog(this,
                "Seleccion un registro.",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        openDatabase(true);
        RelacionTableModel model = (RelacionTableModel) tbList.getModel();
        Trabajo trabajo = model.getValueAt(getSelectRow());
        State state;
        boolean ret = false;
        ret = activeEnd(dbserver, trabajo);        
        if(ret)
        {
            try 
            {
                dbserver.commit();
                reload(RelacionTableModel.ReadMode.REFRESH,false);
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
                closeDatabase();
                reload(RelacionTableModel.ReadMode.REFRESH,false);
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
    }//GEN-LAST:event_mnEndActionPerformed

    private void btUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUpdateActionPerformed
        openDatabase(false);
        reload(RelacionTableModel.ReadMode.RELOAD,false);
    }//GEN-LAST:event_btUpdateActionPerformed

    private void mnMoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnMoveActionPerformed
        boolean flag = false;
        if(tbList.getSelectedRowCount() == 0)
        {            
            JOptionPane.showMessageDialog(this,
                "Seleccion un Registro.",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        else if(tbList.getSelectedRowCount() == 1)
        {
            RelacionTableModel model = (RelacionTableModel) tbList.getModel();
            Trabajo trb = model.getValueAt(getSelectRow());
            State state;
            boolean ret = false;     
            openDatabase(true);

            Move cal = new Move(trb);
            Dialog dlg = new Dialog(cal);      
            cal.setDialog(dlg);
            dlg.setContent(cal);                
            flag = cal.isReady();
            Trace contexTrace = new Trace(BACKWARD_BD, SIIL.servApp.cred.getUser(), "Moviendo Registro");            
            {
                try 
                {
                    contexTrace.insert(dbserver);
                    contexTrace.addOperator(dbserver, "¿Quien hace?");
                    if(cal.getDate() != null) 
                    {
                        if(!trb.upfhToDo(dbserver, cal.getDate(), contexTrace))
                        {                            
                            JOptionPane.showMessageDialog(this,
                                "Falló al asignar la Fehca para " + trb.toString(),
                                "Error Interno",
                                JOptionPane.ERROR_MESSAGE
                                );
                            return;
                        }
                    }
                    if(cal.getSheet() != null) 
                    {
                        if(!trb.upSheet(dbserver, cal.getSheet(), contexTrace))
                        {
                            JOptionPane.showMessageDialog(this,
                                "Falló al asignar la Hoja para " + trb.toString(),
                                "Error Interno",
                                JOptionPane.ERROR_MESSAGE
                                );
                            return;
                        }
                    }
                } 
                catch (SQLException ex) 
                {
                    //Logger.getLogger(Relacion.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            }
            flag = true;
        }
        else if(tbList.getSelectedRowCount() > 1)
        {
            Move cal = new Move();
            Dialog dlg = new Dialog(cal);      
            cal.setDialog(dlg);
            dlg.setContent(cal);
            List<Trabajo> rows = getSelectRows();
            Trace contexTrace = new Trace(BACKWARD_BD, SIIL.servApp.cred.getUser(), "Moviendo Registro masivo");
            openDatabase(true);
            for(Trabajo trb : rows)
            {
                try 
                {
                    if(cal.getDate() != null) 
                    {
                        if(!trb.upfhToDo(dbserver, cal.getDate(), contexTrace))
                        {                            
                            JOptionPane.showMessageDialog(this,
                                "Falló al asignar la Fehca para " + trb.toString(),
                                "Error Interno",
                                JOptionPane.ERROR_MESSAGE
                                );
                            return;
                        }
                    }
                    if(cal.getSheet() != null) 
                    {
                        if(!trb.upSheet(dbserver, cal.getSheet(), contexTrace))
                        {
                            JOptionPane.showMessageDialog(this,
                                "Falló al asignar la Hija para " + trb.toString(),
                                "Error Interno",
                                JOptionPane.ERROR_MESSAGE
                                );
                            return;
                        }
                    }
                } 
                catch (SQLException ex) 
                {
                    //Logger.getLogger(Relacion.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            }
            flag = true;
        }
        
        
        if(flag)
        {
            try 
            {
                dbserver.commit();
                closeDatabase();
                reload(RelacionTableModel.ReadMode.LOAD,false);
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(Relacion.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }
        else
        {
            try 
            {
                dbserver.rollback();
                closeDatabase();
                reload(RelacionTableModel.ReadMode.REFRESH,false);
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(Relacion.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }
    }//GEN-LAST:event_mnMoveActionPerformed

    private void tbListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbListMouseClicked
        if(evt.getClickCount() == 1)
        {
            int row = getSelectRow();
            //if(evt)
            mnMain.setEnabled(true);
            if(tbList.getSelectedRowCount() > 1)
            {
                SIIL.servApp.getInstance().setInformation("Row. Count. = " + Integer.toString(tbList.getSelectedRowCount()));
            }        
            else
            {
                SIIL.servApp.getInstance().setInformation("");
            }
        }
        else if(evt.getClickCount() == 2)
        {
            update(getSelectRow());
        }        
    }//GEN-LAST:event_tbListMouseClicked

    @Deprecated
    private void setEditMode(boolean  mode) 
    {
        editMode = mode;
        RelacionTableModel model = (RelacionTableModel) tbList.getModel();
        //model.setCellEditable(editMode);
    }

    private void mnPrintSAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnPrintSAActionPerformed
        displayReport();
    }//GEN-LAST:event_mnPrintSAActionPerformed

    private void displayReport()
    {        
        if(getSelectRow() < 0)
        {
            JOptionPane.showMessageDialog(this,
            "Seleccion un registro.",
            "Error Externo",
            JOptionPane.ERROR_MESSAGE
            );
        }
        
        RelacionTableModel model = (RelacionTableModel) tbList.getModel();
        Trabajo trabajo = model.getValueAt(getSelectRow());
        
        if(trabajo.getCompany() == null)
        {
            JOptionPane.showMessageDialog(this,
            "Asigne cliente primero.",
            "Error Externo",
            JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        if(trabajo.getSA() == null)
        {
            JOptionPane.showMessageDialog(this,
            "Asigne SA primero",
            "Error Externo",
            JOptionPane.ERROR_MESSAGE
            );
            return;
        }        
        openDatabase(true);
        JasperReport jr;
        try
        {
            HashMap map = new HashMap(); 
            map.put("client",trabajo.getCompany().toString());
            map.put("fecha",dbserver.getDateToday());
            map.put("sa", trabajo.getSA().getFullFolio());
            map.put("orden", "-");
            map.put("nota", trabajo.getBrief());
            jr = (JasperReport) JRLoader.loadObject(Relacion.class.getResourceAsStream("/SIIL/services/trabajo/SA.jasper"));
            JasperPrint jp = JasperFillManager.fillReport(jr, map, dbserver.getConnection());
            JasperViewer jv = new JasperViewer(jp,false);
            jv.setVisible(true);
            jv.setTitle("SAlida de Almacen");
        }
        catch (SQLException | JRException ex)
        {
            //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void tbListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbListKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            if(editMode == true)
            {
                setEditMode(false);
                reload(RelacionTableModel.ReadMode.REFRESH,false);
            }
            else
            {
                if(frame != null)frame.dispose();
            }
        }
    }//GEN-LAST:event_tbListKeyPressed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            if(editMode == true)
            {
                setEditMode(false);
                reload(RelacionTableModel.ReadMode.REFRESH,false);
            }
            else
            {
                if(frame != null)frame.dispose();
            }
        }
    }//GEN-LAST:event_formKeyPressed

    private void mnChangeSheetCampoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnChangeSheetCampoActionPerformed
        if(getSelectRow() > -1)
        {
            RelacionTableModel model = (RelacionTableModel) tbList.getModel();
            Trabajo trb = model.getValueAt(getSelectRow());
            try 
            {
                openDatabase(true);
                Boolean flag = null;
                Trace contexTrace = new Trace(BACKWARD_BD, SIIL.servApp.cred.getUser(), "Cambio de Hoja");
                contexTrace.insert(dbserver);
                contexTrace.addOperator(dbserver, "¿Quien hace?");
                flag = trb.upSheet(dbserver, Sheet.CAMPO, contexTrace);                
                if(flag == null)
                {
                    dbserver.rollback();
                }
                else if(flag == false)
                {
                    dbserver.rollback();
                }
                else if(flag == true)
                {
                    dbserver.commit();
                    reload(RelacionTableModel.ReadMode.RELOAD, false);
                }
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
            JOptionPane.showMessageDialog(
                this,
                "Selecione una fila",
                "Error Externo",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_mnChangeSheetCampoActionPerformed

    private void mnAtenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAtenActionPerformed
        if(getSelectRow() < 0)
        {
            JOptionPane.showMessageDialog(this,
                "Seleccion un registro.",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        openDatabase(true);
        RelacionTableModel model = (RelacionTableModel) tbList.getModel();
        Trabajo trabajo = model.getValueAt(getSelectRow());
        State state;
        boolean ret = false;
        try 
        {
            state = new State(-1);
            state.select(dbserver, State.Steps.RT_ATTENDING);
            Trace contexTrace = new Trace(BACKWARD_BD, SIIL.servApp.cred.getUser(), "Cambio de Estado");
            contexTrace.insert(dbserver);
            contexTrace.addOperator(dbserver, "¿Quien hace?");
            if(state != null) ret = trabajo.upState(dbserver,state,contexTrace);            
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
        if(ret)
        {
            try 
            {
                dbserver.commit();  
                reload(RelacionTableModel.ReadMode.REFRESH,false);
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(Relacion.class.getName()).log(Level.SEVERE, null, ex);
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
                closeDatabase();
                reload(RelacionTableModel.ReadMode.REFRESH,false);
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(Relacion.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }//GEN-LAST:event_mnAtenActionPerformed

    private void mnChangeSheetTallerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnChangeSheetTallerActionPerformed
        if(getSelectRow() > -1)
        {
            RelacionTableModel model = (RelacionTableModel) tbList.getModel();
            Trabajo trb = model.getValueAt(getSelectRow());
            try 
            {
                openDatabase(true);
                Boolean flag = null;                
                Trace contexTrace = new Trace(BACKWARD_BD, SIIL.servApp.cred.getUser(), "Cambio de Hoja");
                contexTrace.insert(dbserver);
                contexTrace.addOperator(dbserver, "¿Quien hace?");
                flag = trb.upSheet(dbserver, Sheet.TALLER, contexTrace);
                
                if(flag == null)
                {
                    dbserver.rollback();
                }
                else if(flag == false)
                {
                    dbserver.rollback();
                }
                else if(flag == true)
                {
                    dbserver.commit();
                    reload(RelacionTableModel.ReadMode.RELOAD, false);
                }                
            } 
            catch (SQLException ex)
            {
                //Logger.getLogger(Relacion.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
                );
            }
        }
        else
        {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione una fila",
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_mnChangeSheetTallerActionPerformed

    private void mnChangeSheetGruaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnChangeSheetGruaActionPerformed
        if(getSelectRow() > -1)
        {
            RelacionTableModel model = (RelacionTableModel) tbList.getModel();
            Trabajo trb = model.getValueAt(getSelectRow());
            try 
            {
                openDatabase(true);
                Boolean flag = null;
                Trace contexTrace = new Trace(BACKWARD_BD, SIIL.servApp.cred.getUser(), "Cambio de Hoja");
                contexTrace.insert(dbserver);
                contexTrace.addOperator(dbserver, "¿Quien hace?");
                flag = trb.upSheet(dbserver, Sheet.GRUA, contexTrace);
                
                if(flag == null)
                {
                    dbserver.rollback();
                }
                else if(flag == false)
                {
                    dbserver.rollback();
                }
                else if(flag == true)
                {
                    dbserver.commit();
                    reload(RelacionTableModel.ReadMode.RELOAD, false);
                }        
            } 
            catch (SQLException ex)
            {
                //Logger.getLogger(Relacion.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
                );
            }
        }
        else
        {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione una fila",
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_mnChangeSheetGruaActionPerformed

    private void mnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnDeleteActionPerformed
        if(getSelectRow() > -1)
        {
            int option = JOptionPane.showConfirmDialog(
                this,
                "Esta eliminando el registro selecionado, desea continuar?",
                "Confirme oprecion",
                JOptionPane.YES_NO_OPTION);
            if(option == JOptionPane.NO_OPTION)
            {
                return;
            }
            RelacionTableModel model = (RelacionTableModel) tbList.getModel();
            Trabajo trb = model.getValueAt(getSelectRow());
            try 
            {
                openDatabase(true);
                Boolean flag = null;
                Trace contexTrace = new Trace(BACKWARD_BD, SIIL.servApp.cred.getUser(), "Elimiacion de registro");
                contexTrace.insert(dbserver);
                contexTrace.addOperator(dbserver, "Relacion De Trabajo");
                flag = trb.delete(dbserver, null);
                
                if(flag == null)
                {
                    dbserver.rollback();
                }
                else if(flag == false)
                {
                    dbserver.rollback();
                }
                else if(flag == true)
                {
                    dbserver.commit();
                    reload(RelacionTableModel.ReadMode.RELOAD, false);
                }
            }
            catch (SQLException ex)
            {
                //Logger.getLogger(Relacion.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
        else
        {
            JOptionPane.showMessageDialog(
                this,
                "Selecione una fila",
                "Error Externo",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_mnDeleteActionPerformed

    private void mnAsignedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAsignedActionPerformed
        if(getSelectRow() < 0)
        {
            JOptionPane.showMessageDialog(this,
                "Seleccion un registro.",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        openDatabase(true);
        RelacionTableModel model = (RelacionTableModel) tbList.getModel();
        Trabajo trabajo = model.getValueAt(getSelectRow());
        State state;
        boolean ret = false;
        try 
        {
            state = new State(-1);
            state.select(dbserver, State.Steps.RT_ASIGNED);
            Trace contexTrace = new Trace(BACKWARD_BD, SIIL.servApp.cred.getUser(), "Cambio de Estado");
            contexTrace.insert(dbserver);
            contexTrace.addOperator(dbserver, "¿Quien hace?");
            if(state != null) ret = trabajo.upState(dbserver,state,contexTrace);            
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
        if(ret)
        {
            try 
            {
                dbserver.commit();
                reload(RelacionTableModel.ReadMode.REFRESH,false);
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(Relacion.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }
        else
        {
            try 
            {
                dbserver.rollback();
                closeDatabase();
                reload(RelacionTableModel.ReadMode.REFRESH,false);
            }
            catch (SQLException ex) 
            {
                JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }
    }//GEN-LAST:event_mnAsignedActionPerformed

    private void mnVerQouteRowsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnVerQouteRowsActionPerformed
        if(getSelectRow() < 0)
        {
            JOptionPane.showMessageDialog(this,
                "Seleccion un registro.",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        openDatabase(true);
        RelacionTableModel model = (RelacionTableModel) tbList.getModel();
        Trabajo trabajo = model.getValueAt(getSelectRow());
        if(trabajo.getQuotedService() == null)
        {
            JOptionPane.showMessageDialog(this,
                "Este registro no tiene cotizacion asignada.",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        SIIL.Servicios.Orden.Read.viewRows(dbserver, trabajo.getQuotedService());
    }//GEN-LAST:event_mnVerQouteRowsActionPerformed

    private void mnVerQuotedDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnVerQuotedDetailActionPerformed
        if(getSelectRow() < 0)
        {
            JOptionPane.showMessageDialog(this,
                "Seleccion un registro.",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        openDatabase(true);
        RelacionTableModel model = (RelacionTableModel) tbList.getModel();
        Trabajo trabajo = model.getValueAt(getSelectRow());
        if(trabajo.getQuotedService() == null)
        {
            JOptionPane.showMessageDialog(this,
                "Este registro no tiene cotizacion asignada.",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        SIIL.Servicios.Orden.Read.viewDetail(dbserver,trabajo.getQuotedService());
    }//GEN-LAST:event_mnVerQuotedDetailActionPerformed

    private void btFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFilterActionPerformed
        Filter filter = new Filter(this,null);
        core.Dialog dlg = new core.Dialog(filter);      
        filter.setDialog((Dialog) dlg);
        dlg.setContent(filter);
    }//GEN-LAST:event_btFilterActionPerformed

    private void mnChangeSheetMantoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnChangeSheetMantoActionPerformed
        if(getSelectRow() > -1)
        {
            RelacionTableModel model = (RelacionTableModel) tbList.getModel();
            Trabajo trb = model.getValueAt(getSelectRow());
            try 
            {
                openDatabase(true);
                Boolean flag = null;
                Trace contexTrace = new Trace(BACKWARD_BD, SIIL.servApp.cred.getUser(), "Cambio de Hoja");
                contexTrace.insert(dbserver);
                contexTrace.addOperator(dbserver, "¿Quien hace?");
                flag = trb.upSheet(dbserver, Sheet.MTTO, contexTrace);
                
                if(flag == null)
                {
                    dbserver.rollback();
                }
                else if(flag == false)
                {
                    dbserver.rollback();
                }
                else if(flag == true)
                {
                    dbserver.commit();
                    reload(RelacionTableModel.ReadMode.RELOAD, false);
                }                
            }
            catch (SQLException ex)
            {
                //Logger.getLogger(Relacion.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
                );
            }
        }
        else
        {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione una fila",
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_mnChangeSheetMantoActionPerformed

    private void mnAceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAceptActionPerformed
        if(getSelectRow() < 0)
        {
            JOptionPane.showMessageDialog(this,
                "Seleccion un registro.",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        openDatabase(true);
        RelacionTableModel model = (RelacionTableModel) tbList.getModel();
        Trabajo trabajo = model.getValueAt(getSelectRow());
        State state;
        boolean ret = false,retM = false;
        try 
        {
            state = new State(-1);
            state.select(dbserver, State.Steps.RT_ACCEPTED);
            Trace contexTrace = new Trace(BACKWARD_BD, SIIL.servApp.cred.getUser(), "Cambio de Estado");
            contexTrace.insert(dbserver);
            contexTrace.addOperator(dbserver, "¿Quien hace?");
            if(state != null) ret = trabajo.upState(dbserver,state,contexTrace); 
            retM = trabajo.upMechanic(dbserver, null, contexTrace);
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
        if(ret & retM)
        {
            try 
            {
                dbserver.commit();
                closeDatabase();
                reload(RelacionTableModel.ReadMode.REFRESH,false);
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(Relacion.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }
        else
        {
            try 
            {
                dbserver.rollback();
                closeDatabase();
                reload(RelacionTableModel.ReadMode.REFRESH,false);
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(Relacion.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }
    }//GEN-LAST:event_mnAceptActionPerformed

    private void fechaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fechaMouseClicked
        openDatabase(false);
        reload(RelacionTableModel.ReadMode.RELOAD,false);
    }//GEN-LAST:event_fechaMouseClicked

    private void mnExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnExcelActionPerformed
        try 
        {
            genReportExcel();
        }
        catch (IOException ex) 
        {
            Logger.getLogger(Relacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnExcelActionPerformed

    private void mnFacturarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnFacturarActionPerformed
        if(getSelectRow() < 0)
        {
            JOptionPane.showMessageDialog(this,
                "Seleccion un registro.",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        openDatabase(true);
        RelacionTableModel model = (RelacionTableModel) tbList.getModel();
        Trabajo trabajo = model.getValueAt(getSelectRow());
        State state;
        if(trabajo.getState().getOrdinal() != 6)
        {
            JOptionPane.showMessageDialog
            (
                this,
                "Deve estar terminado el trabajo para poder aplicar esta operacion.",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        Return retinvo = null;
        try 
        {
            Invoice invoice = new Invoice();
            Trace contexTrace = new Trace(BACKWARD_BD, SIIL.servApp.cred.getUser(), "Cambio de Estado");
            state = new State(-1);
            state.select(dbserver, State.Steps.IS_REVIEW);
            retinvo = invoice.insert(dbserver, SIIL.servApp.cred.getUser().getOffice(), state, SIIL.servApp.cred.getUser(), dbserver.getDateToday(), trabajo.getCompany(), -1, null, "SI");
            invoice.upTrabajo(dbserver, trabajo);            
            state.select(dbserver, State.Steps.RT_INVOICING);
            if(state != null) trabajo.upState(dbserver,state,contexTrace);
            if(trabajo.getSA() != null)
            {
                invoice.downOffice(dbserver);
                invoice.getOffice().download(dbserver.getConnection());
                invoice.fromCNRenglones(dbserver, Operational.TablaRenglon.REMISION);
            }
            else
            {
                closeDatabase();
                JOptionPane.showMessageDialog(this,
                "No hay SA asociado",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            contexTrace.insert(dbserver);
            contexTrace.addOperator(dbserver, "¿Quien hace?");           
        }
        catch (SQLException ex) 
        {
            
            closeDatabase();
            Logger.getLogger(Relacion.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        if(retinvo.isFlag())
        {
            try 
            {
                dbserver.commit();
                closeDatabase();
                reload(RelacionTableModel.ReadMode.REFRESH,false);
            }
            catch (SQLException ex) 
            {                
                closeDatabase();
                JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }
        else
        {
            try 
            {
                dbserver.rollback();
                closeDatabase();
                reload(RelacionTableModel.ReadMode.REFRESH,false);
            }
            catch (SQLException ex) 
            {
                JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }        
    }//GEN-LAST:event_mnFacturarActionPerformed

    /**
     * 
     * @param rowselected indical la fila selecionada.
     */
    private void read(int rowselected)
    {
        if(rowselected < 0)
        {
            JOptionPane.showMessageDialog
            (
                this,
                "Selecione una fila",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;        
        }
        
        RelacionTableModel model = (RelacionTableModel) tbList.getModel();
        Create create = new Create(Create.Mode.READ,model.getValueAt(rowselected),null,null,getSheet());
        core.Dialog dlg = new core.Dialog(create);
        create.setDialog(dlg);
        create.setRelacion(this);
        dlg.setContentPane(create);
        dlg.setSize(create.getPreferredSize());
        dlg.setModal(true);
        dlg.setVisible(true);
    }

    public void reload(RelacionTableModel.ReadMode mode,boolean recicle) 
    {        
        openDatabase(recicle);        
        updateTable(mode);
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
                    if(!dbserver.getConnection().isClosed()) dbserver.close();
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

    private Boolean activeAsigned(Database db, Trabajo trabajo, Person mechanic) throws SQLException
    {
        State state;
        boolean retM = false, retS = false;
        state = new State(-1);
        
        Trace contexTrace = new Trace(BACKWARD_BD, SIIL.servApp.cred.getUser(), "Asignacion de SA");
        
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

    /**
     * Solamente asigna el estado de cotizacion sin asignar la cotizacion
     * @param db
     * @param trabajo
     * @param contexTrace
     * @return 
     */
    private boolean activeQuoting(Database db, Trabajo trabajo,Trace contexTrace)
    {
        State state;
        boolean ret = false;
        try 
        {
            state = new State(-1);
            state.select(db, State.Steps.RT_QUOTING);
            if(state != null) ret = trabajo.upState(db,state,contexTrace);
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
            );
            return true;
        }
        return ret;
    }
    
    
    private boolean activeEnd(Database db, Trabajo trabajo)
    {
        State state;
        boolean ret = false,retO = true;
        try 
        {
            state = new State(-1);
            state.select(db, State.Steps.RT_END);
            Trace contexTrace = new Trace(BACKWARD_BD, SIIL.servApp.cred.getUser(), "Asignacion de SA");
            if(state != null) ret = trabajo.upState(db,state,contexTrace);
            if(trabajo.getQuotedService() != null)
            {
                OrdenFinalizar orden = new OrdenFinalizar(trabajo.getQuotedService());
                retO = orden.closeService(db, SIIL.servApp.getInstance().getProgressObject(), SIIL.servApp.cred);
            }
        } 
        catch (UnsupportedEncodingException | MessagingException | SQLException ex) 
        {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
            );
            return true;
        }
        return (ret && retO);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jdesktop.swingx.JXButton btFilter;
    private org.jdesktop.swingx.JXButton btUpdate;
    private org.jdesktop.swingx.JXComboBox cbSheet;
    private core.calendar.DateChooser fecha;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private org.jdesktop.swingx.JXTable jXTable1;
    private javax.swing.JMenuItem mnAcept;
    private javax.swing.JMenuItem mnAsigned;
    private javax.swing.JMenuItem mnAten;
    private javax.swing.JMenuItem mnChangeSheetCampo;
    private javax.swing.JMenuItem mnChangeSheetGrua;
    private javax.swing.JMenuItem mnChangeSheetManto;
    private javax.swing.JMenuItem mnChangeSheetTaller;
    private javax.swing.JMenuItem mnCotizar;
    private javax.swing.JMenuItem mnCreate;
    private javax.swing.JMenuItem mnDelete;
    private javax.swing.JMenuItem mnEnd;
    private javax.swing.JMenuItem mnExcel;
    private javax.swing.JMenuItem mnFacturar;
    private javax.swing.JPopupMenu mnMain;
    private javax.swing.JMenuItem mnMove;
    private javax.swing.JMenuItem mnPrintSA;
    private javax.swing.JMenuItem mnUrge;
    private javax.swing.JMenuItem mnVerQouteRows;
    private javax.swing.JMenuItem mnVerQuotedDetail;
    private javax.swing.JTable tbList;
    // End of variables declaration//GEN-END:variables

    /**
     * Evalua los campos de SA y Cotizacion para determinar si se actualiza el
     * esta de la cotizacio, en tal caso envia la solicitud de actualizacion al
     * modulo de Cotizacion.
     * @param database
     * @param trb
     * @return 
     */
    private Boolean sychQuotedServiceSA(Database database, Trabajo trb) throws SQLException, MessagingException, UnsupportedEncodingException 
    {
        if(trb.downSA(database))//hay SA
        {//Si
            if(trb.getQuotedService() != null)//hay una cotizacion asignada
            {//Si
                if(!trb.getQuotedService().downState(database))
                {
                    return false;
                }
                int ordinal = trb.getQuotedService().getState().getOrdinal();
                switch(ordinal)
                {
                    case 1:
                    case 2:
                        JOptionPane.showMessageDialog(this,
                        "No ha sido autrizada la cotizacion",
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                        );
                        return false;
                    case 3:
                    case 4:
                    case 5:
                        int option = JOptionPane.showConfirmDialog(
                        this,
                        "La cotizacion está en '" + trb.getQuotedService().getState().getName() + "', ¿Desea continuar?.",
                        "Confirme operación",
                        JOptionPane.YES_NO_OPTION);
                        if(option == JOptionPane.YES_OPTION)
                        {
                            OrdenAlmacenada ord = new OrdenAlmacenada();
                            ord.fill(database,SIIL.servApp.cred, trb.getQuotedService().getID());
                            SIIL.servApp.Progress progress = (SIIL.servApp.Progress) SIIL.servApp.getInstance().getProgressObject();
                            ((SIIL.servApp.Progress)SIIL.servApp.getInstance().getProgressObject()).On();
                            return ord.surtir(database, progress, trb.getSA().getFullFolio());
                        }
                        else
                        {
                            //System.err.println("Se eligio no acrualizar la cotizacion");
                            return null;
                        }
                    case 6: 
                    case 7:
                        JOptionPane.showMessageDialog(this,
                        "La cotizacion está en '" + trb.getQuotedService().getState().getName() + "'",
                        "Notificacion",
                        JOptionPane.WARNING_MESSAGE
                        );
                        return null;
                    default:
                        System.err.println("No se reconoce el estado indicado " + ordinal);
                        return false;
                }
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }
}
