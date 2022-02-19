package SIIL;

import SIIL.Management.ImportCN;
import SIIL.Server.Database;
import SIIL.Servicios.Orden.Read;
import SIIL.client.sales.Enterprise;
import session.User;
import SIIL.desktop.auth.ToolLoginModule;
import SIIL.export.GruaMovements;
import SIIL.imports.LoteGrua;
import SIIL.services.grua.Resumov;
import SIIL.sockets.messages.ClosedApplication;
import SIIL.tools.ScreenLogger;
import com.galaxies.andromeda.util.Texting.Message;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FilenameUtils;
import org.xml.sax.SAXException;
import process.ImportsCN;


/**
 *
 * @author Azael
 */
public class servApp extends javax.swing.JFrame implements Runnable
{
    public com.galaxies.andromeda.util.Progress getProgressObject() 
    {
        return progress;
    }
        
    private static void closeDatabase(Database dbserver) 
    {
        if(dbserver != null)
        {
            dbserver.close();
            dbserver = null;
        }
    }

    private static Database openDatabase(boolean  reclicleConextion)
    {
        Database dbserver = null;
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
                        return dbserver;
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
            JOptionPane.showMessageDialog(null,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
        }
        return dbserver;
    }
    
    public void setInformation(String info)
    {
        lbInfo.setText(info);
    }
    
    public class Progress implements com.galaxies.andromeda.util.Progress
    {
        javax.swing.JProgressBar progBar;
        javax.swing.JLabel lbMensaje;
        java.awt.Component display;
        private com.galaxies.andromeda.util.Upgradeable view;
        
        public Progress(JProgressBar bar,JLabel tx,Component display)
        {
            progBar = bar;
            lbMensaje = tx;             
            this.display = display;   
            Off();
        }
        
        public void On()
        {
            progBar.setVisible(true);
            lbMensaje.setVisible(true);
        }
        
        public void Off()
        {
            progBar.setVisible(false);
            lbMensaje.setVisible(false);
        }
        
        @Override
        public void setProgress(int prog, String messge)
        {
            progBar.setValue(prog);
            lbMensaje.setText(messge);
        }

        @Override
        public void finalized(Message msg) 
        {
            JOptionPane.showMessageDialog(display,
                msg.getMessage(),
                "Confirmación de operación",
                JOptionPane.INFORMATION_MESSAGE
                );      
            if(view != null) view.reload();
            setProgress(100, msg.getMessage());
        }

        @Override
        public void fail(Throwable cause) 
        {
            setProgress(0, cause.getMessage());
            if(cause.getCause() != null)
            {
                JOptionPane.showMessageDialog(
                    display,
                    cause.getMessage()+ " [" + cause.getCause() + "]",
                    "Falló la operación",
                    JOptionPane.ERROR_MESSAGE
                );
            }
            else
            {
                JOptionPane.showMessageDialog(display,
                    cause.getMessage(),
                    "Falló la operación",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }

        /**
         * @param view the view to set
         */
        public void setView(com.galaxies.andromeda.util.Upgradeable view) 
        {
            this.view = view;
        }

        @Override
        public void clean() {
            setProgress(0, "");
        }
    }
    
    public void enableAcces()
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
                
        if(cred.acces(dbserver,"Servicios.Grua.Read"))
        {
            mnMainGruaRegmov.setVisible(true);
            mnMainGruaResumov.setVisible(true);
            mnMainGruaRegmov.setEnabled(true);
            mnMainGruaResumov.setEnabled(true);
            
            mnMainGruaEquipo.setVisible(true);
            mnMainGruaEquipo2.setVisible(true);
            mnMainGruaEquipo.setEnabled(true);
            mnMainGruaEquipo2.setEnabled(true);
        }
        else
        {
            mnMainGruaRegmov.setVisible(false);
            mnMainGruaResumov.setVisible(false);            
            mnMainGruaRegmov.setEnabled(false);
            mnMainGruaResumov.setEnabled(false);
            
            mnMainGruaEquipo.setVisible(false);
            mnMainGruaEquipo2.setVisible(false);            
            mnMainGruaEquipo.setEnabled(false);
            mnMainGruaEquipo2.setEnabled(false);
        }         
        if(cred.acces(dbserver,"Servicios.Grua.Write"))
        {
            mnMainGruaMove.setVisible(true);
            mnMainGruaMove.setEnabled(true);
            
            mnMainExport.setVisible(true);
            mnMainExport.setEnabled(true);
        }
        else
        {
            mnMainGruaMove.setVisible(false);
            mnMainGruaMove.setEnabled(false);
            
            mnMainExport.setVisible(false);
            mnMainExport.setEnabled(false);
        }
        if(cred.acces(dbserver,"Purchase.CR.R"))
        {
            mnComprasCR.setVisible(true);
            mnComprasCR.setEnabled(true);
        }
        else
        {
            mnComprasCR.setVisible(false);
            mnComprasCR.setEnabled(false);
        }        
        if(cred.acces(dbserver,"Purchase.Provider.Create"))
        {
            mnComprasProv.setVisible(true);
            mnComprasProv.setEnabled(true);
        }
        else
        {
            mnComprasProv.setVisible(false);
            mnComprasProv.setEnabled(false);
        }        
        if(cred.acces(dbserver,"Administracion.Cheques.Write") & cred.acces(dbserver,"Administracion.Cheques.Read"))
        {
            mnAdminCheque.setVisible(true);
            mnAdminCheque.setEnabled(true);
        }
        else
        {
            mnAdminCheque.setVisible(false);
            mnAdminCheque.setEnabled(false);
        }      
        if(cred.acces(dbserver,"Administracion.Panel.ImportCN") )
        {
            mnIMportCN.setVisible(true);
            mnIMportCN.setEnabled(true);
        }
        else
        {
            mnIMportCN.setVisible(false);
            mnIMportCN.setEnabled(false);
        }
        if(cred.acces(dbserver,"services.order.read"))
        {
            mnOrdenModule.setVisible(true);
            mnOrdenModule.setEnabled(true);
            mnHisOrdServ.setVisible(true);
            mnHisOrdServ.setEnabled(true);
        }
        else
        {
            mnOrdenModule.setVisible(false);
            mnOrdenModule.setEnabled(false);
            mnHisOrdServ.setVisible(false);
            mnHisOrdServ.setEnabled(false);
        }
        if(cred.acces(dbserver,"services.order.write"))
        {
            mnOrdenCreate.setVisible(true);
            mnOrdenCreate.setEnabled(true);
        }
        else
        {
            mnOrdenCreate.setVisible(false);
            mnOrdenCreate.setEnabled(false);
        }        
        if(cred.acces(dbserver,"services.rl.acces"))
        {
            mnRelacionTR.setVisible(true);
            mnRelacionTR.setEnabled(true);
        }
        else
        {
            mnRelacionTR.setVisible(false);
            mnRelacionTR.setEnabled(false);
        }
        if(cred.acces(dbserver, "sales.requirepo"))
        {
            mnSalesRequirePO.setVisible(true);
            mnSalesRequirePO.setEnabled(true);
        }
        else
        {
            mnSalesRequirePO.setVisible(false);
            mnSalesRequirePO.setEnabled(false);
        }
        if(cred.acces(dbserver, "process.init"))
        {
            mnAdminInit.setVisible(true);
            mnAdminInit.setEnabled(true);
        }
        else
        {
            mnAdminInit.setVisible(false);
            mnAdminInit.setEnabled(false);
        }
        if(cred.acces(dbserver, "process.control"))
        {
            mnInstances.setVisible(true);
            mnPanelBitacora.setVisible(true);
            mnFind.setVisible(true);
            mnInstances.setEnabled(true);
            mnPanelBitacora.setEnabled(true);
            mnFind.setEnabled(true);
            mnLoteGrua.setEnabled(true);
        }
        else
        {
            mnInstances.setVisible(false);
            mnPanelBitacora.setVisible(false);
            mnFind.setVisible(false);
            mnInstances.setEnabled(false);
            mnPanelBitacora.setEnabled(false);
            mnFind.setEnabled(false);
        }        
        if(cred.acces(dbserver, "administracion.email.envio.factura"))
        {
            mnMailFact.setVisible(true);
            mnMailFact.setEnabled(true);            
            mnClientEmail.setEnabled(true);
            mnClientEmail.setVisible(true);
        }
        else
        {
            mnMailFact.setVisible(false);
            mnMailFact.setEnabled(false);            
            mnClientEmail.setEnabled(false);
            mnClientEmail.setVisible(false);
        }                
        if(cred.acces(dbserver, "services.order.associate"))
        {
            mnAssociateOrderFile.setVisible(true);
            mnAssociateOrderFile.setEnabled(true); 
            mnAssociateOrderFileMass.setVisible(true);
            mnAssociateOrderFileMass.setEnabled(true);            
            mnAssociatefactura.setVisible(true);
            mnAssociatefactura.setEnabled(true);            
            mnAssociateFacturasFolios.setVisible(true);
            mnAssociateFacturasFolios.setEnabled(true);            
            mnAssociateFacturasFoliosSA.setVisible(true);
            mnAssociateFacturasFoliosSA.setEnabled(true); 
            mnAssociateFacturaOrden.setVisible(true);
            mnAssociateFacturaOrden.setEnabled(true);        
        }
        else
        {
            mnAssociateOrderFile.setVisible(false);
            mnAssociateOrderFile.setEnabled(false);
            mnAssociateOrderFileMass.setVisible(false);
            mnAssociateOrderFileMass.setEnabled(false);            
            mnAssociatefactura.setVisible(false);
            mnAssociatefactura.setEnabled(false);            
            mnAssociateFacturasFolios.setVisible(false);
            mnAssociateFacturasFolios.setEnabled(false);      
            mnAssociateFacturasFoliosSA.setVisible(false);
            mnAssociateFacturasFoliosSA.setEnabled(false);  
            mnAssociateFacturaOrden.setVisible(false);
            mnAssociateFacturaOrden.setEnabled(false);
        }
        if(cred.acces(dbserver, "Servicios.Orcom.acces"))
        {
            mnQuoatationAlmacen.setVisible(true);
            mnQuoatationAlmacen.setEnabled(true);            
            mnServCot.setVisible(true);
            mnServCot.setEnabled(true);                    
            mnServCotRef.setVisible(true);
            mnServCotRef.setEnabled(true);            
        }
        else
        {
            mnQuoatationAlmacen.setVisible(false);
            mnQuoatationAlmacen.setEnabled(false);            
            mnServCot.setVisible(false);
            mnServCot.setEnabled(false);            
            mnServCotRef.setVisible(false);
            mnServCotRef.setEnabled(false);
        }
        if(cred.acces(dbserver, "sales.invoice.cancel.stamp"))
        {           
            mnSalesCancel.setVisible(true);
            mnSalesCancel.setEnabled(true);
        }
        else
        {             
            mnSalesCancel.setVisible(false);
            mnSalesCancel.setEnabled(false);           
        }            
        if(cred.acces(dbserver, "sales.invoice.stamp"))
        {
            mnListFacturar.setVisible(true);
            mnListFacturar.setEnabled(true);  
        }
        else
        {
            mnListFacturar.setVisible(false);
            mnListFacturar.setEnabled(false);          
        }
        if(cred.acces(dbserver, "sales.invoice.read"))
        {
            mnSalesInvoiceVer.setVisible(true);
            mnSalesInvoiceVer.setEnabled(true); 
        }
        else
        {
            mnSalesInvoiceVer.setVisible(false);
            mnSalesInvoiceVer.setEnabled(false);      
        }
        if(cred.acces(dbserver, "sales.client.synch"))
        {
            mnSalesClientSynch.setVisible(true);
            mnSalesClientSynch.setEnabled(true); 
        }
        else
        {
            mnSalesClientSynch.setVisible(false);
            mnSalesClientSynch.setEnabled(false);       
        }
        if(cred.acces(dbserver, "sales.remision.read"))
        {
            mnSalesRemision.setVisible(true);
            mnSalesRemision.setEnabled(true); 
        }
        else
        {
            mnSalesRemision.setVisible(false);
            mnSalesRemision.setEnabled(false);       
        }
        
        dbserver.close();
    }
    /**
     * Creates new form servApp
     */
    public servApp(String args[])
    {
        initComponents();
        initComponents2();
        this.args = args;
        this.setVisible(true);
    }
    
    /**
     * Creates new form servApp
     */
    public servApp(User user)
    {
        initComponents();
        initComponents2();        
    }
    
    /**
     * Creates new form servApp
     */
    public servApp()
    {
        initComponents();
        initComponents2();
    }

    private void initComponents2() throws HeadlessException
    {
        app = this;
        Dimension d = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setSize(d.width,d.height - ((int)(d.height*0.05)));
        //Se instacio el panel, lo asume updateNotif
        //rpanel = new SIIL.panel.notif.Panel(tasks);
        //titled.setTitle("Notificaciones");
        setResizable(true);
        progress = new Progress(prgMain,lbMain,this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        desktopPane = new javax.swing.JDesktopPane();
        stBar = new org.jdesktop.swingx.JXStatusBar();
        prgMain = new javax.swing.JProgressBar();
        lbMain = new org.jdesktop.swingx.JXLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(200, 0), new java.awt.Dimension(200, 0), new java.awt.Dimension(200, 32767));
        lbInfo = new javax.swing.JLabel();
        mnMain = new javax.swing.JMenuBar();
        mnGrua = new javax.swing.JMenu();
        mnMainGruaRegmov = new javax.swing.JMenuItem();
        mnMainGruaResumov = new javax.swing.JMenuItem();
        mnMainGruaMove = new javax.swing.JMenuItem();
        mnMainGruaEquipo = new javax.swing.JMenuItem();
        mnMainExport = new javax.swing.JMenuItem();
        mnRef = new javax.swing.JMenu();
        mnComprasOrden = new javax.swing.JMenuItem();
        mnServCotRef = new javax.swing.JMenuItem();
        mnServcios = new javax.swing.JMenu();
        mnMainGruaEquipo2 = new javax.swing.JMenuItem();
        mnHisOrdServ = new javax.swing.JMenuItem();
        mnOrdenModule = new javax.swing.JMenuItem();
        mnOrdenCreate = new javax.swing.JMenuItem();
        mnServCot = new javax.swing.JMenuItem();
        mnQuoatationAlmacen = new javax.swing.JMenuItem();
        mnRelacionTR = new javax.swing.JMenuItem();
        mnAssociateOrderFile = new javax.swing.JMenuItem();
        mnAssociateOrderFileMass = new javax.swing.JMenuItem();
        mnAssociateFacturasFoliosSA = new javax.swing.JMenuItem();
        mnAssociatefactura = new javax.swing.JMenuItem();
        mnAssociateFacturasFolios = new javax.swing.JMenuItem();
        mnAssociateFacturaOrden = new javax.swing.JMenuItem();
        mnVentas = new javax.swing.JMenu();
        mnVentasFactura = new javax.swing.JMenuItem();
        mnAdminCheque = new javax.swing.JMenuItem();
        mnSalesQuote = new javax.swing.JMenuItem();
        mnSaleRemision = new javax.swing.JMenuItem();
        mnSalesClientSynch = new javax.swing.JMenuItem();
        mnSalesClients = new javax.swing.JMenuItem();
        mnSalesRequirePO = new javax.swing.JMenuItem();
        mnSalesInvoice = new javax.swing.JMenuItem();
        mnSalesCancel = new javax.swing.JMenuItem();
        mnSalesInvoiceVer = new javax.swing.JMenuItem();
        mnSalesRemision = new javax.swing.JMenuItem();
        mnListFacturar = new javax.swing.JMenuItem();
        mnVentasClientes = new javax.swing.JMenuItem();
        mnCompras = new javax.swing.JMenu();
        mnComprasCR = new javax.swing.JMenuItem();
        mnComprasProv = new javax.swing.JMenuItem();
        mnComprasProviders2 = new javax.swing.JMenuItem();
        mnAlmacen = new javax.swing.JMenu();
        mnAlmacenLocacion = new javax.swing.JMenuItem();
        mnPanel = new javax.swing.JMenu();
        mnPanelBitacora = new javax.swing.JMenuItem();
        mnIMportCN = new javax.swing.JMenuItem();
        mnInstances = new javax.swing.JMenuItem();
        mnFind = new javax.swing.JMenuItem();
        mnAdminInit = new javax.swing.JMenu();
        mnImportCPO = new javax.swing.JMenuItem();
        mnUpdateClientes = new javax.swing.JMenuItem();
        mnUpdateItems = new javax.swing.JMenuItem();
        mnMailFact = new javax.swing.JMenuItem();
        mnClientEmail = new javax.swing.JMenuItem();
        mnMangUser = new javax.swing.JMenuItem();
        mnLoteGrua = new javax.swing.JMenuItem();
        mnHelp = new javax.swing.JMenu();
        mnHelpAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        desktopPane.setAutoscrolls(true);

        stBar.setLayout(new org.jdesktop.swingx.HorizontalLayout());
        stBar.add(prgMain);
        stBar.add(lbMain);
        stBar.add(filler1);
        stBar.add(lbInfo);

        mnGrua.setText("Movimiento de Equipo");

        mnMainGruaRegmov.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.ALT_DOWN_MASK));
        mnMainGruaRegmov.setText("Hoja de Movimientos");
        mnMainGruaRegmov.setEnabled(false);
        mnMainGruaRegmov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnMainGruaRegmovActionPerformed(evt);
            }
        });
        mnGrua.add(mnMainGruaRegmov);

        mnMainGruaResumov.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.ALT_DOWN_MASK));
        mnMainGruaResumov.setText("Hoja de Renta");
        mnMainGruaResumov.setEnabled(false);
        mnMainGruaResumov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnMainGruaResumovActionPerformed(evt);
            }
        });
        mnGrua.add(mnMainGruaResumov);

        mnMainGruaMove.setText("Alta de Movimiento");
        mnMainGruaMove.setEnabled(false);
        mnMainGruaMove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnMainGruaMoveActionPerformed(evt);
            }
        });
        mnGrua.add(mnMainGruaMove);

        mnMainGruaEquipo.setText("Equipos");
        mnMainGruaEquipo.setEnabled(false);
        mnMainGruaEquipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnMainGruaEquipoActionPerformed(evt);
            }
        });
        mnGrua.add(mnMainGruaEquipo);

        mnMainExport.setText("Exportar");
        mnMainExport.setEnabled(false);
        mnMainExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnMainExportActionPerformed(evt);
            }
        });
        mnGrua.add(mnMainExport);

        mnMain.add(mnGrua);

        mnRef.setText("Refacciones");

        mnComprasOrden.setText("Orden de Compra");
        mnComprasOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnComprasOrdenActionPerformed(evt);
            }
        });
        mnRef.add(mnComprasOrden);

        mnServCotRef.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_DOWN_MASK));
        mnServCotRef.setText("Cotizacion");
        mnServCotRef.setFocusable(true);
        mnServCotRef.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnServCotRefActionPerformed(evt);
            }
        });
        mnRef.add(mnServCotRef);

        mnMain.add(mnRef);

        mnServcios.setText("Servicios");
        mnServcios.setToolTipText("");

        mnMainGruaEquipo2.setText("Equipos");
        mnMainGruaEquipo2.setEnabled(false);
        mnMainGruaEquipo2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnMainGruaEquipo2ActionPerformed(evt);
            }
        });
        mnServcios.add(mnMainGruaEquipo2);

        mnHisOrdServ.setText("Historial de Ordenes de Servicio");
        mnHisOrdServ.setEnabled(false);
        mnHisOrdServ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnHisOrdServActionPerformed(evt);
            }
        });
        mnServcios.add(mnHisOrdServ);

        mnOrdenModule.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_DOWN_MASK));
        mnOrdenModule.setText("Resumen de GM");
        mnOrdenModule.setEnabled(false);
        mnOrdenModule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnOrdenModuleActionPerformed(evt);
            }
        });
        mnServcios.add(mnOrdenModule);

        mnOrdenCreate.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_DOWN_MASK));
        mnOrdenCreate.setText("Captura Orden de Servicio");
        mnOrdenCreate.setEnabled(false);
        mnOrdenCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnOrdenCreateActionPerformed(evt);
            }
        });
        mnServcios.add(mnOrdenCreate);

        mnServCot.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_DOWN_MASK));
        mnServCot.setText("Cotizacion");
        mnServCot.setEnabled(false);
        mnServCot.setFocusable(true);
        mnServCot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnServCotActionPerformed(evt);
            }
        });
        mnServcios.add(mnServCot);

        mnQuoatationAlmacen.setText("Cotizacion - Almacen");
        mnQuoatationAlmacen.setEnabled(false);
        mnQuoatationAlmacen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnQuoatationAlmacenActionPerformed(evt);
            }
        });
        mnServcios.add(mnQuoatationAlmacen);

        mnRelacionTR.setText("Relacion de Trabajo");
        mnRelacionTR.setEnabled(false);
        mnRelacionTR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnRelacionTRActionPerformed(evt);
            }
        });
        mnServcios.add(mnRelacionTR);

        mnAssociateOrderFile.setText("Asociar Orden - Indivudal");
        mnAssociateOrderFile.setEnabled(false);
        mnAssociateOrderFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAssociateOrderFileActionPerformed(evt);
            }
        });
        mnServcios.add(mnAssociateOrderFile);

        mnAssociateOrderFileMass.setText("Asociar Orden - Masivo");
        mnAssociateOrderFileMass.setEnabled(false);
        mnAssociateOrderFileMass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAssociateOrderFileMassActionPerformed(evt);
            }
        });
        mnServcios.add(mnAssociateOrderFileMass);

        mnAssociateFacturasFoliosSA.setText("Asociar Orden - Importar Folios");
        mnAssociateFacturasFoliosSA.setEnabled(false);
        mnAssociateFacturasFoliosSA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAssociateFacturasFoliosSAActionPerformed(evt);
            }
        });
        mnServcios.add(mnAssociateFacturasFoliosSA);

        mnAssociatefactura.setText("Asociar Facturas - Indiviual");
        mnAssociatefactura.setEnabled(false);
        mnAssociatefactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAssociatefacturaActionPerformed(evt);
            }
        });
        mnServcios.add(mnAssociatefactura);

        mnAssociateFacturasFolios.setText("Asociar Facturas - Importar Folios");
        mnAssociateFacturasFolios.setEnabled(false);
        mnAssociateFacturasFolios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAssociateFacturasFoliosActionPerformed(evt);
            }
        });
        mnServcios.add(mnAssociateFacturasFolios);

        mnAssociateFacturaOrden.setText("Asociar Facturas - Masivo");
        mnAssociateFacturaOrden.setEnabled(false);
        mnAssociateFacturaOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAssociateFacturaOrdenActionPerformed(evt);
            }
        });
        mnServcios.add(mnAssociateFacturaOrden);

        mnMain.add(mnServcios);

        mnVentas.setText("Ventas");

        mnVentasFactura.setText("Faturar");
        mnVentasFactura.setEnabled(false);
        mnVentasFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnVentasFacturaActionPerformed(evt);
            }
        });
        mnVentas.add(mnVentasFactura);

        mnAdminCheque.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.ALT_DOWN_MASK));
        mnAdminCheque.setText("Cheques");
        mnAdminCheque.setEnabled(false);
        mnAdminCheque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAdminChequeActionPerformed(evt);
            }
        });
        mnVentas.add(mnAdminCheque);

        mnSalesQuote.setText("Cotizaciones");
        mnSalesQuote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnSalesQuoteActionPerformed(evt);
            }
        });
        mnVentas.add(mnSalesQuote);

        mnSaleRemision.setText("Remisiones");
        mnSaleRemision.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnSaleRemisionActionPerformed(evt);
            }
        });
        mnVentas.add(mnSaleRemision);

        mnSalesClientSynch.setText("Sincronizar clientes...");
        mnSalesClientSynch.setEnabled(false);
        mnSalesClientSynch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnSalesClientSynchActionPerformed(evt);
            }
        });
        mnVentas.add(mnSalesClientSynch);

        mnSalesClients.setText("Clientes");
        mnSalesClients.setEnabled(false);
        mnSalesClients.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnSalesClientsActionPerformed(evt);
            }
        });
        mnVentas.add(mnSalesClients);

        mnSalesRequirePO.setText("¿Requiere PO?");
        mnSalesRequirePO.setEnabled(false);
        mnSalesRequirePO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnSalesRequirePOActionPerformed(evt);
            }
        });
        mnVentas.add(mnSalesRequirePO);

        mnSalesInvoice.setText("Facturar");
        mnSalesInvoice.setEnabled(false);
        mnSalesInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnSalesInvoiceActionPerformed(evt);
            }
        });
        mnVentas.add(mnSalesInvoice);

        mnSalesCancel.setText("Cancelar");
        mnSalesCancel.setEnabled(false);
        mnSalesCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnSalesCancelActionPerformed(evt);
            }
        });
        mnVentas.add(mnSalesCancel);

        mnSalesInvoiceVer.setText("Factura");
        mnSalesInvoiceVer.setEnabled(false);
        mnSalesInvoiceVer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnSalesInvoiceVerActionPerformed(evt);
            }
        });
        mnVentas.add(mnSalesInvoiceVer);

        mnSalesRemision.setText("Remision");
        mnSalesRemision.setEnabled(false);
        mnSalesRemision.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnSalesRemisionActionPerformed(evt);
            }
        });
        mnVentas.add(mnSalesRemision);

        mnListFacturar.setText("Lista de Facturacion");
        mnListFacturar.setEnabled(false);
        mnListFacturar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnListFacturarActionPerformed(evt);
            }
        });
        mnVentas.add(mnListFacturar);

        mnVentasClientes.setText("Clientes");
        mnVentasClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnVentasClientesActionPerformed(evt);
            }
        });
        mnVentas.add(mnVentasClientes);

        mnMain.add(mnVentas);

        mnCompras.setText("Compras");

        mnComprasCR.setText("Cotrarecibo");
        mnComprasCR.setEnabled(false);
        mnComprasCR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnComprasCRActionPerformed(evt);
            }
        });
        mnCompras.add(mnComprasCR);

        mnComprasProv.setText("Proveedores");
        mnComprasProv.setEnabled(false);
        mnComprasProv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnComprasProvActionPerformed(evt);
            }
        });
        mnCompras.add(mnComprasProv);

        mnComprasProviders2.setText("Proveedores v2");
        mnComprasProviders2.setEnabled(false);
        mnComprasProviders2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnComprasProviders2ActionPerformed(evt);
            }
        });
        mnCompras.add(mnComprasProviders2);

        mnMain.add(mnCompras);

        mnAlmacen.setText("Alamcen");

        mnAlmacenLocacion.setText("Locacion");
        mnAlmacenLocacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAlmacenLocacionActionPerformed(evt);
            }
        });
        mnAlmacen.add(mnAlmacenLocacion);

        mnMain.add(mnAlmacen);

        mnPanel.setText("Administración");

        mnPanelBitacora.setText("Bitacora");
        mnPanelBitacora.setEnabled(false);
        mnPanelBitacora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnPanelBitacoraActionPerformed(evt);
            }
        });
        mnPanel.add(mnPanelBitacora);

        mnIMportCN.setText("Importación CN");
        mnIMportCN.setEnabled(false);
        mnIMportCN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnIMportCNActionPerformed(evt);
            }
        });
        mnPanel.add(mnIMportCN);

        mnInstances.setText("Instancias");
        mnInstances.setEnabled(false);
        mnInstances.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnInstancesActionPerformed(evt);
            }
        });
        mnPanel.add(mnInstances);

        mnFind.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        mnFind.setText("Buscar");
        mnFind.setEnabled(false);
        mnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnFindActionPerformed(evt);
            }
        });
        mnPanel.add(mnFind);

        mnAdminInit.setText("Inicializacion");
        mnAdminInit.setEnabled(false);

        mnImportCPO.setText("importacion de PO Requerimento");
        mnImportCPO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnImportCPOActionPerformed(evt);
            }
        });
        mnAdminInit.add(mnImportCPO);

        mnUpdateClientes.setText("Actualizar informacion de clientes");
        mnUpdateClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnUpdateClientesActionPerformed(evt);
            }
        });
        mnAdminInit.add(mnUpdateClientes);

        mnUpdateItems.setText("Actualizar Items");
        mnUpdateItems.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnUpdateItemsActionPerformed(evt);
            }
        });
        mnAdminInit.add(mnUpdateItems);

        mnPanel.add(mnAdminInit);

        mnMailFact.setText("Correo de Factura");
        mnMailFact.setEnabled(false);
        mnMailFact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnMailFactActionPerformed(evt);
            }
        });
        mnPanel.add(mnMailFact);

        mnClientEmail.setText("Editar Email de Cliente");
        mnClientEmail.setEnabled(false);
        mnClientEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnClientEmailActionPerformed(evt);
            }
        });
        mnPanel.add(mnClientEmail);

        mnMangUser.setText("CRUD de Usuario");
        mnMangUser.setEnabled(false);
        mnMangUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnMangUserActionPerformed(evt);
            }
        });
        mnPanel.add(mnMangUser);

        mnLoteGrua.setText("Lote de Grua");
        mnLoteGrua.setEnabled(false);
        mnLoteGrua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnLoteGruaActionPerformed(evt);
            }
        });
        mnPanel.add(mnLoteGrua);

        mnMain.add(mnPanel);

        mnHelp.setText("Ayuda");

        mnHelpAbout.setText("Acerca de...");
        mnHelpAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnHelpAboutActionPerformed(evt);
            }
        });
        mnHelp.add(mnHelpAbout);

        mnMain.add(mnHelp);

        setJMenuBar(mnMain);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(desktopPane))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(stBar, javax.swing.GroupLayout.DEFAULT_SIZE, 986, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(desktopPane, javax.swing.GroupLayout.DEFAULT_SIZE, 659, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnMainGruaRegmovActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnMainGruaRegmovActionPerformed
        SIIL.Servicios.Grua.View.Regmov.RegmovList lRg = new SIIL.Servicios.Grua.View.Regmov.RegmovList(servApp.cred.getBD(), desktopPane);
        desktopPane.add(lRg);
        //ajustar a lo ancho
        lRg.setSize(desktopPane.getSize().width, lRg.getSize().height);
        lRg.setVisible(true);
    }//GEN-LAST:event_mnMainGruaRegmovActionPerformed

    private void mnMainGruaResumovActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnMainGruaResumovActionPerformed
        SIIL.Servicios.Grua.View.Resumov.ResumovList res = new SIIL.Servicios.Grua.View.Resumov.ResumovList(servApp.cred.getBD(),desktopPane);
        //ajustar a lo ancho
        desktopPane.add(res);
        //ajustar a lo ancho
        res.setSize(desktopPane.getSize().width, res.getSize().height);
        res.setVisible(true);
    }//GEN-LAST:event_mnMainGruaResumovActionPerformed

    private void mnAdminChequeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAdminChequeActionPerformed
        SIIL.Administracion.Cheques.Lista lor = new SIIL.Administracion.Cheques.Lista(servApp.cred.getBD(), desktopPane);
        desktopPane.add(lor);
        int x = desktopPane.getSize().width/2 - lor.getSize().width/2;
        int y = 10;
        lor.setLocation(x, y);
        lor.setVisible(true);
    }//GEN-LAST:event_mnAdminChequeActionPerformed

    private void mnVentasFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnVentasFacturaActionPerformed
    
    }//GEN-LAST:event_mnVentasFacturaActionPerformed

    private void mnComprasCRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnComprasCRActionPerformed
        SIIL.purchase.cr.List lscr = new SIIL.purchase.cr.List(servApp.cred.getBD(), desktopPane);
        desktopPane.add(lscr);
        int x = desktopPane.getSize().width/2 - lscr.getSize().width/2;
        int y = 10;
        lscr.setLocation(x, y);
        lscr.setVisible(true);
    }//GEN-LAST:event_mnComprasCRActionPerformed

    private void mnComprasProvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnComprasProvActionPerformed
        SIIL.purchase.privider.crud.Read provsR = new SIIL.purchase.privider.crud.Read(desktopPane);
        desktopPane.add(provsR);
        int x = desktopPane.getSize().width/2 - provsR.getSize().width/2;
        int y = 10;
        provsR.setLocation(x, y);
        provsR.setVisible(true);
    }//GEN-LAST:event_mnComprasProvActionPerformed

    private void mnPanelBitacoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnPanelBitacoraActionPerformed
        JInternalFrame inter = new JInternalFrame("Enviar correo de Factura",false,true);
        SIIL.trace.Overview2 trace = new SIIL.trace.Overview2(servApp.cred.getBD(), desktopPane);
        inter.setContentPane(trace);
        inter.setSize(trace.getPreferredSize());
        inter.setMaximizable(true);
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - trace.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);        
    }//GEN-LAST:event_mnPanelBitacoraActionPerformed

    private void mnComprasOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnComprasOrdenActionPerformed
        purchases.order.List prov = new purchases.order.List();
        JInternalFrame inter = new JInternalFrame("Ordenes de Compra",true,true);
        inter.setContentPane(prov);
        inter.setSize(prov.getPreferredSize());
        inter.setMaximizable(true);
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - prov.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }//GEN-LAST:event_mnComprasOrdenActionPerformed

    private void mnSalesQuoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnSalesQuoteActionPerformed
        sales.quotation.Read2 read = new sales.quotation.Read2();
        JInternalFrame inter = new JInternalFrame("Cotizaciones",true,true);
        inter.setContentPane(read);
        inter.setSize(read.getPreferredSize());
        inter.setMaximizable(true);
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - read.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }//GEN-LAST:event_mnSalesQuoteActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try 
        {
            if(SIIL.servApp.socket != null)
            {
                SIIL.servApp.socket.send(new ClosedApplication(SIIL.servApp.socket.getID()));
                synchronized(this)
                {
                    wait(1000);
                } 
                SIIL.servApp.socket.close();
            }
        } 
        catch (InterruptedException ex) 
        {
            Logger.getLogger(servApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowClosing

    private void mnHelpAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnHelpAboutActionPerformed
        SIIL.about.about frm = new SIIL.about.about();
        desktopPane.add(frm);
        int x = desktopPane.getSize().width/2 - frm.getSize().width/2;
        int y = 10;
        frm.setLocation(x, y);
        frm.setVisible(true);
    }//GEN-LAST:event_mnHelpAboutActionPerformed

    private void mnComprasProviders2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnComprasProviders2ActionPerformed
        purchases.provider.List prov = new purchases.provider.List(servApp.cred,purchases.provider.List.Mode.DISPLAY);
        JInternalFrame inter = new JInternalFrame("Lista de Proveedores",false,true);
        inter.setContentPane(prov);
        inter.setSize(prov.getPreferredSize());
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - prov.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }//GEN-LAST:event_mnComprasProviders2ActionPerformed

    private void mnIMportCNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnIMportCNActionPerformed
        int option = JOptionPane.showOptionDialog(this,
                "Inicio de importación gobal de CN, quiza se sobreescriban datos existentes, ¿Desea continuar?",
                "Confirme de solicitud",
                JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE,null,null,null);
        if(option == JOptionPane.NO_OPTION) 
        {
            return;
        }
        ImportCN.main(args);
    }//GEN-LAST:event_mnIMportCNActionPerformed

    private void mnMainGruaMoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnMainGruaMoveActionPerformed
        JInternalFrame inter = new JInternalFrame("Alta de movimiento de Grua",false,true);
        SIIL.Servicios.Grua.Move2 mov = new SIIL.Servicios.Grua.Move2(inter);
        inter.setContentPane(mov);
        inter.setSize(mov.getPreferredSize());
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - mov.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }//GEN-LAST:event_mnMainGruaMoveActionPerformed

    private void mnMainGruaEquipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnMainGruaEquipoActionPerformed
        stock.SearchHequi srh = new stock.SearchHequi(stock.SearchHequiTableModel.Mode.ListEquipos);
        JInternalFrame inter = new JInternalFrame("Lista de Equipos",false,true);
        inter.setContentPane(srh);
        inter.setSize(srh.getPreferredSize());
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - srh.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }//GEN-LAST:event_mnMainGruaEquipoActionPerformed

    private void mnSalesClientSynchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnSalesClientSynchActionPerformed
        SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();
        try 
        {
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
        } 
        catch (ParserConfigurationException ex) 
        {
            Logger.getLogger(servApp.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (SAXException ex) 
        {
            Logger.getLogger(servApp.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(servApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        Database dbserver1 = null;
        Database dbserver2 = null;
        Database dbserver3 = null;
        Database dbserver4 = null;
        try 
        {
            dbserver1 = new Database(serverConfig);
            dbserver2 = new Database(serverConfig);
            dbserver3 = new Database(serverConfig);
            dbserver4 = new Database(serverConfig);
        }
        catch (ClassNotFoundException | SQLException ex) 
        {
            //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        ArrayList<String> arr = new ArrayList<>();
        arr.add("Begin proess.");
        ImportsCN importcn = new ImportsCN();
        boolean flImpo = importcn.importClients(dbserver1,arr,600);  
        try 
        {
            if(flImpo) 
            {
                dbserver1.commit();
                arr.add("Importacion de cleintes Done.");
            }
            else
            {
                dbserver1.rollback();
                JOptionPane.showMessageDialog(this,
                    "Fallo importacion.",
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(servApp.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } 
        arr.add("----------------"); 
        boolean flFlowing = importcn.generateTitemFlow(dbserver3,arr,600);
        try 
        {
            if(flImpo) 
            {
                dbserver3.commit();
            }
            else
            {
                dbserver3.rollback();
                JOptionPane.showMessageDialog(this,
                    "Fallo importacion.2",
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(servApp.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }        
        arr.add("-----------------------------");        
        boolean flLk = false;
        try 
        {            
            flLk = importcn.importLinkTitems(dbserver2,arr,600);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(servApp.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        try 
        {
            if(flImpo) 
            {
                dbserver2.commit();
                arr.add("Importacion Limks Done.");
            }
            else
            {
                dbserver2.rollback();
                JOptionPane.showMessageDialog(this,
                    "Fallo importacion.2",
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(servApp.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        arr.add("-----------------------------");  
        Boolean result = null;
        try 
        {
            result = Resumov.emperejarV3(dbserver4);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        try 
        {
            if(result) 
            {
                dbserver4.commit();
                arr.add("Importando datos de Resumov");
            }
            else
            {
                dbserver4.rollback();
                JOptionPane.showMessageDialog(this,
                    "Falló importacion 2",
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return;
        }
                
        ScreenLogger scr = new ScreenLogger(arr);
        JInternalFrame inter = new JInternalFrame("Sincronizacion de clientes.",false,true);
        inter.setContentPane(scr);
        inter.setSize(scr.getPreferredSize());
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - scr.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
        dbserver1.close();
        dbserver2.close();
        dbserver3.close();
        dbserver4.close();
    }//GEN-LAST:event_mnSalesClientSynchActionPerformed

    private void mnOrdenCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnOrdenCreateActionPerformed
        JInternalFrame inter = new JInternalFrame("Captura de Orden de Servicio",false,true);
        SIIL.services.order.CreateUpdate ord = new SIIL.services.order.CreateUpdate(inter,null);
        inter.setContentPane(ord);
        inter.setSize(ord.getPreferredSize());
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - ord.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);        
    }//GEN-LAST:event_mnOrdenCreateActionPerformed

    private void mnOrdenModuleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnOrdenModuleActionPerformed
        SIIL.services.order.Read ord = new SIIL.services.order.Read();
        JInternalFrame inter = new JInternalFrame("Generar Reporte por Modulo",true,true);
        inter.setContentPane(ord);
        inter.setSize(ord.getPreferredSize());
        inter.setMaximizable(true);
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - ord.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }//GEN-LAST:event_mnOrdenModuleActionPerformed

    private void mnSalesClientsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnSalesClientsActionPerformed
        sales.clients.Read cl = new sales.clients.Read(sales.clients.ReadTableModel.Mode.DISPLAY);
        JInternalFrame inter = new JInternalFrame("Lista de Proveedores",false,true);
        inter.setContentPane(cl);
        inter.setSize(cl.getPreferredSize());
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - cl.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }//GEN-LAST:event_mnSalesClientsActionPerformed

    private void mnServCotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnServCotActionPerformed
        JInternalFrame inter = new JInternalFrame("Cotización",true,true);
        SIIL.Servicios.Orden.Read screen = new SIIL.Servicios.Orden.Read(cred, Read.Mode.LIST, desktopPane);
        inter.setContentPane(screen);
        inter.setSize(screen.getPreferredSize());
        inter.setMaximizable(true);
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - screen.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }//GEN-LAST:event_mnServCotActionPerformed

    private void mnHisOrdServActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnHisOrdServActionPerformed
        SIIL.services.order.Historial ord = new SIIL.services.order.Historial(SIIL.services.order.Historial.Mode.READ);
        JInternalFrame inter = new JInternalFrame("Historial de Ordenes de Servicio",true,true);
        inter.setContentPane(ord);
        inter.setSize(ord.getPreferredSize());
        inter.setMaximizable(true);
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - ord.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }//GEN-LAST:event_mnHisOrdServActionPerformed

    private void mnServCotRefActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnServCotRefActionPerformed
        JInternalFrame inter = new JInternalFrame("Cotización",true,true);
        SIIL.Servicios.Orden.Read screen = new SIIL.Servicios.Orden.Read(cred, Read.Mode.LIST, desktopPane);
        inter.setContentPane(screen);
        inter.setSize(screen.getPreferredSize());
        inter.setMaximizable(true);
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - screen.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }//GEN-LAST:event_mnServCotRefActionPerformed

    private void mnRelacionTRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnRelacionTRActionPerformed
        JInternalFrame inter = new JInternalFrame("Relacion de Trabajo",true,true);
        SIIL.services.trabajo.Relacion ord = new SIIL.services.trabajo.Relacion(inter);
        inter.setContentPane(ord);
        inter.setSize(ord.getPreferredSize());
        inter.setMaximizable(true);
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - ord.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }//GEN-LAST:event_mnRelacionTRActionPerformed

    private void mnMainGruaEquipo2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnMainGruaEquipo2ActionPerformed
        stock.SearchHequi srh = new stock.SearchHequi(stock.SearchHequiTableModel.Mode.ListEquipos);
        JInternalFrame inter = new JInternalFrame("Lista de Equipos",false,true);
        inter.setContentPane(srh);
        inter.setSize(srh.getPreferredSize());
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - srh.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }//GEN-LAST:event_mnMainGruaEquipo2ActionPerformed

    private void mnInstancesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnInstancesActionPerformed
        SIIL.Instances.Read read = new SIIL.Instances.Read();
        JInternalFrame inter = new JInternalFrame("Instancias",false,true);
        inter.setContentPane(read);
        inter.setSize(read.getPreferredSize());
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - read.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }//GEN-LAST:event_mnInstancesActionPerformed

    private void mnFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnFindActionPerformed
        JInternalFrame inter = new JInternalFrame("Buscar SA",true,true);
        core.search.Panel screen = new core.search.Panel();
        inter.setContentPane(screen);
        inter.setSize(screen.getPreferredSize());
        inter.setMaximizable(true);
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - screen.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }//GEN-LAST:event_mnFindActionPerformed

    private void mnSalesRequirePOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnSalesRequirePOActionPerformed
        JInternalFrame inter = new JInternalFrame("¿Requiere PO?",false,true);
        SIIL.Clientes.RequirePO screen = new SIIL.Clientes.RequirePO();
        inter.setContentPane(screen);
        inter.setSize(screen.getPreferredSize());
        inter.setMaximizable(true);
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - screen.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }//GEN-LAST:event_mnSalesRequirePOActionPerformed

    private void mnImportCPOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnImportCPOActionPerformed
        JInternalFrame inter = new JInternalFrame("Importar PO Requerimento",false,true);
        SIIL.imports.CPOA screen = new SIIL.imports.CPOA();
        inter.setContentPane(screen);
        inter.setSize(screen.getPreferredSize());
        inter.setMaximizable(true);
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - screen.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }//GEN-LAST:event_mnImportCPOActionPerformed

    private void mnUpdateClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnUpdateClientesActionPerformed
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
        
        List<String> logger = new ArrayList<>();        
        boolean ret = false;
        try 
        {        
            ret = Enterprise.updateFromCNAll(dbserver,logger);
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(servApp.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(servApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        ScreenLogger scr = new ScreenLogger(logger);
        JInternalFrame inter = new JInternalFrame("Sincronizacion de clientes.",false,true);
        inter.setContentPane(scr);
        inter.setSize(scr.getPreferredSize());
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - scr.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
        
        if(ret)
        {
            try 
            {
                dbserver.commit();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(servApp.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(servApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        dbserver.close();
    }//GEN-LAST:event_mnUpdateClientesActionPerformed

    private void mnMailFactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnMailFactActionPerformed
        /*JInternalFrame inter = new JInternalFrame("Enviar correo de Factura",false,true);
        SIIL.mail.CNMail screen = new SIIL.mail.CNMail();
        inter.setContentPane(screen);
        inter.setSize(screen.getPreferredSize());
        inter.setMaximizable(true);
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - screen.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true*/
    }//GEN-LAST:event_mnMailFactActionPerformed

    private void mnAssociateOrderFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAssociateOrderFileActionPerformed
        /*SIIL.services.order.AssociateFile ord = new SIIL.services.order.AssociateFile();
        JInternalFrame inter = new JInternalFrame("Asociar Orden - Indivudal",true,true);
        inter.setContentPane(ord);
        inter.setSize(ord.getPreferredSize());
        inter.setMaximizable(true);
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - ord.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);*/
    }//GEN-LAST:event_mnAssociateOrderFileActionPerformed

    private void mnAssociateOrderFileMassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAssociateOrderFileMassActionPerformed
        SIIL.services.order.AsociacionMasiva ord = new SIIL.services.order.AsociacionMasiva();
        JInternalFrame inter = new JInternalFrame("Asociar Orden - Masivo",true,true);
        inter.setContentPane(ord);
        inter.setSize(ord.getPreferredSize());
        inter.setMaximizable(true);
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - ord.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }//GEN-LAST:event_mnAssociateOrderFileMassActionPerformed

    private void mnSalesInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnSalesInvoiceActionPerformed
        /*JInternalFrame inter = new JInternalFrame("Factuar",false,true);
        SIIL.sales.invoice.Invoice screen = new SIIL.sales.invoice.Invoice();
        inter.setContentPane(screen);
        inter.setSize(screen.getPreferredSize());
        inter.setMaximizable(true);
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - screen.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);*/
    }//GEN-LAST:event_mnSalesInvoiceActionPerformed

    private void mnQuoatationAlmacenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnQuoatationAlmacenActionPerformed
        /*JInternalFrame inter = new JInternalFrame("Cotizaciones - Almacen",false,true);
        SIIL.Servicios.Orden.ReadAlmacen screen = new SIIL.Servicios.Orden.ReadAlmacen();
        inter.setContentPane(screen);
        inter.setSize(screen.getPreferredSize());
        inter.setMaximizable(true);
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - screen.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);*/
    }//GEN-LAST:event_mnQuoatationAlmacenActionPerformed

    private void mnClientEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnClientEmailActionPerformed
        JInternalFrame inter = new JInternalFrame("Enviar correo de Factura",false,true);
        SIIL.Clientes.UpdateMailClient screen = new SIIL.Clientes.UpdateMailClient();
        inter.setContentPane(screen);
        inter.setSize(screen.getPreferredSize());
        inter.setMaximizable(true);
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - screen.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }//GEN-LAST:event_mnClientEmailActionPerformed

    private void mnAssociatefacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAssociatefacturaActionPerformed
        SIIL.services.order.AssociateFacturaOrden screen = new SIIL.services.order.AssociateFacturaOrden();
        JInternalFrame inter = new JInternalFrame("Asociar Facturas - Indiviual",true,true);
        inter.setContentPane(screen);
        inter.setSize(screen.getPreferredSize());
        inter.setMaximizable(true);
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - screen.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }//GEN-LAST:event_mnAssociatefacturaActionPerformed

    private void mnSalesInvoiceVerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnSalesInvoiceVerActionPerformed
        sales.invoice.InvoiceDetail screen = new sales.invoice.InvoiceDetail();
        JInternalFrame inter = new JInternalFrame("Asociar de Ordenes a Factura",true,true);
        inter.setContentPane(screen);
        inter.setSize(screen.getPreferredSize());
        inter.setMaximizable(true);
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - screen.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }//GEN-LAST:event_mnSalesInvoiceVerActionPerformed

    private void mnAssociateFacturasFoliosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAssociateFacturasFoliosActionPerformed
        SIIL.services.order.AssociateImportFolios screen = new SIIL.services.order.AssociateImportFolios();
        JInternalFrame inter = new JInternalFrame("Asociar Facturas - Importar Folios",true,true);
        inter.setContentPane(screen);
        inter.setSize(screen.getPreferredSize());
        inter.setMaximizable(true);
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - screen.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }//GEN-LAST:event_mnAssociateFacturasFoliosActionPerformed

    private void mnAssociateFacturaOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAssociateFacturaOrdenActionPerformed
        SIIL.services.order.AsociacionMasivaFacturaOrdenes ord = new SIIL.services.order.AsociacionMasivaFacturaOrdenes();
        JInternalFrame inter = new JInternalFrame("Asociar Facturas - Masivo",true,true);
        inter.setContentPane(ord);
        inter.setSize(ord.getPreferredSize());
        inter.setMaximizable(true);
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - ord.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }//GEN-LAST:event_mnAssociateFacturaOrdenActionPerformed

    private void mnAssociateFacturasFoliosSAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAssociateFacturasFoliosSAActionPerformed
        SIIL.services.order.AssociateImportFoliosSA screen = new SIIL.services.order.AssociateImportFoliosSA();
        JInternalFrame inter = new JInternalFrame("Asociar Ordenes - Importar Folios",true,true);
        inter.setContentPane(screen);
        inter.setSize(screen.getPreferredSize());
        inter.setMaximizable(true);
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - screen.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }//GEN-LAST:event_mnAssociateFacturasFoliosSAActionPerformed

    private void mnSalesCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnSalesCancelActionPerformed
        /*sales.invoice.Cancel screen = new sales.invoice.Cancel();
        JInternalFrame inter = new JInternalFrame("Cancelar Factura",true,true);
        inter.setContentPane(screen);
        inter.setSize(screen.getPreferredSize());
        inter.setMaximizable(true);
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - screen.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);*/
    }//GEN-LAST:event_mnSalesCancelActionPerformed

    private void mnSalesRemisionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnSalesRemisionActionPerformed
        sales.invoice.RemisionDetail screen = new sales.invoice.RemisionDetail();
        JInternalFrame inter = new JInternalFrame("Asociar de Ordenes a Remision",true,true);
        inter.setContentPane(screen);
        inter.setSize(screen.getPreferredSize());
        inter.setMaximizable(true);
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - screen.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }//GEN-LAST:event_mnSalesRemisionActionPerformed

    private void mnUpdateItemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnUpdateItemsActionPerformed
        Thread t = new Thread(new Runnable() 
        {
            public void run() 
            {
                Database dbserver = openDatabase(true);
                progress.On();
                try 
                {
                    database.mysql.stock.Item.synchCN(dbserver, progress);
                } 
                catch (SQLException ex) 
                {
                    Logger.getLogger(servApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                closeDatabase(dbserver);
            }
        });
        t.start();
    }//GEN-LAST:event_mnUpdateItemsActionPerformed

    private void mnListFacturarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnListFacturarActionPerformed
        /*JInternalFrame inter = new JInternalFrame("Lista de Facturación",false,true);
        sales.invoice.ReadList screen = new sales.invoice.ReadList();
        inter.setContentPane(screen);
        inter.setSize(screen.getPreferredSize());
        inter.setMaximizable(true);
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - screen.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);*/
    }//GEN-LAST:event_mnListFacturarActionPerformed

    private void mnAlmacenLocacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAlmacenLocacionActionPerformed
        stock.Almacen prov = new stock.Almacen();
        JInternalFrame inter = new JInternalFrame("Almacen",true,true);
        inter.setContentPane(prov);
        inter.setSize(prov.getPreferredSize());
        inter.setMaximizable(true);
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - prov.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }//GEN-LAST:event_mnAlmacenLocacionActionPerformed

    private void mnSaleRemisionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnSaleRemisionActionPerformed
        sales.remision.Read2 read = new sales.remision.Read2();
        JInternalFrame inter = new JInternalFrame("Remision",true,true);
        inter.setContentPane(read);
        inter.setSize(read.getPreferredSize());
        inter.setMaximizable(true);
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - read.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }//GEN-LAST:event_mnSaleRemisionActionPerformed

    private void mnMangUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnMangUserActionPerformed
        JInternalFrame inter = new JInternalFrame("CRUD de Usuarios",false,true);
        SIIL.Management.User screen = new SIIL.Management.User();
        inter.setContentPane(screen);
        inter.setSize(screen.getPreferredSize());
        inter.setMaximizable(true);
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - screen.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }//GEN-LAST:event_mnMangUserActionPerformed

    private void mnVentasClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnVentasClientesActionPerformed
        SIIL.Clientes.ReadSelect2 select = new SIIL.Clientes.ReadSelect2(null);
        JInternalFrame inter = new JInternalFrame("CLientes",true,true);
        inter.setContentPane(select);
        inter.setSize(select.getPreferredSize());
        inter.setMaximizable(true);
        desktopPane.add(inter);
        int x = desktopPane.getSize().width/2 - select.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }//GEN-LAST:event_mnVentasClientesActionPerformed

    private void mnLoteGruaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnLoteGruaActionPerformed
        SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();        
        Database dbserver = null;
        try 
        {
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
            dbserver = new Database(serverConfig);
        }
        catch (ClassNotFoundException | SQLException | IOException | ParserConfigurationException | SAXException ex) 
        {
            Logger.getLogger(servApp.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,ex.getMessage());
            return;
        }
            
        LoteGrua lote = new LoteGrua(dbserver);
        
        String dir = System.getProperty("user.home");
        dir += "\\desktop";
        try 
        {
            lote.loadClients(dir+"\\lotegrua\\clients.csv");
        }
        catch (IOException | SQLException ex) 
        {
            Logger.getLogger(servApp.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,ex.getMessage());
        }
                
        try 
        {
            lote.loadActivos(dir+"\\lotegrua\\activos.csv");
        }
        catch (IOException | SQLException ex) 
        {
            Logger.getLogger(servApp.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,ex.getMessage());
        }        
           
        try 
        {
            lote.loadActivos2(dir+"\\lotegrua\\movements.csv");
        }
        catch (IOException | SQLException ex) 
        {
            Logger.getLogger(servApp.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,ex.getMessage());
        }
                
        try 
        {
            lote.loadMovements(dir + "\\lotegrua\\movements.csv");
        }
        catch (IOException | SQLException ex) 
        {
            Logger.getLogger(servApp.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,ex.getMessage());
        }
                
        try 
        {
            lote.loadResumov(dir+"\\lotegrua\\movements.csv");
        }
        catch (IOException | SQLException ex) 
        {
            Logger.getLogger(servApp.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,ex.getMessage());
        }
              
        try 
        {
            dbserver.commit();
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(servApp.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,ex.getMessage());
        }
    }//GEN-LAST:event_mnLoteGruaActionPerformed

    private void mnMainExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnMainExportActionPerformed
        SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();        
        Database dbserver = null;
        try 
        {
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
            dbserver = new Database(serverConfig);
        }
        catch (ClassNotFoundException | SQLException | IOException | ParserConfigurationException | SAXException ex) 
        {
            Logger.getLogger(servApp.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,ex.getMessage());
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");   
        GruaMovements export = new GruaMovements();
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) 
        {
            File fileToSave = fileChooser.getSelectedFile();
            //System.out.println("Save as file: " + fileToSave.getAbsolutePath());

            try 
            {
                export.generate(fileToSave,dbserver);
            } 
            catch (SQLException | IOException ex) 
            {
                Logger.getLogger(servApp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(servApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            JOptionPane.showMessageDialog(this,"Datos guardados.");
        }
    }//GEN-LAST:event_mnMainExportActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws ParserConfigurationException 
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(servApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
                
        Thread m = new Thread(new servApp(args));
        m.start();
    }
    
    public static servApp getInstance()
    {
        return app;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane desktopPane;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel lbInfo;
    private org.jdesktop.swingx.JXLabel lbMain;
    private javax.swing.JMenuItem mnAdminCheque;
    private javax.swing.JMenu mnAdminInit;
    private javax.swing.JMenu mnAlmacen;
    private javax.swing.JMenuItem mnAlmacenLocacion;
    private javax.swing.JMenuItem mnAssociateFacturaOrden;
    private javax.swing.JMenuItem mnAssociateFacturasFolios;
    private javax.swing.JMenuItem mnAssociateFacturasFoliosSA;
    private javax.swing.JMenuItem mnAssociateOrderFile;
    private javax.swing.JMenuItem mnAssociateOrderFileMass;
    private javax.swing.JMenuItem mnAssociatefactura;
    private javax.swing.JMenuItem mnClientEmail;
    private javax.swing.JMenu mnCompras;
    private javax.swing.JMenuItem mnComprasCR;
    private javax.swing.JMenuItem mnComprasOrden;
    private javax.swing.JMenuItem mnComprasProv;
    private javax.swing.JMenuItem mnComprasProviders2;
    private javax.swing.JMenuItem mnFind;
    private javax.swing.JMenu mnGrua;
    private javax.swing.JMenu mnHelp;
    private javax.swing.JMenuItem mnHelpAbout;
    private javax.swing.JMenuItem mnHisOrdServ;
    private javax.swing.JMenuItem mnIMportCN;
    private javax.swing.JMenuItem mnImportCPO;
    private javax.swing.JMenuItem mnInstances;
    private javax.swing.JMenuItem mnListFacturar;
    private javax.swing.JMenuItem mnLoteGrua;
    private javax.swing.JMenuItem mnMailFact;
    private javax.swing.JMenuBar mnMain;
    private javax.swing.JMenuItem mnMainExport;
    private javax.swing.JMenuItem mnMainGruaEquipo;
    private javax.swing.JMenuItem mnMainGruaEquipo2;
    private javax.swing.JMenuItem mnMainGruaMove;
    private javax.swing.JMenuItem mnMainGruaRegmov;
    private javax.swing.JMenuItem mnMainGruaResumov;
    private javax.swing.JMenuItem mnMangUser;
    private javax.swing.JMenuItem mnOrdenCreate;
    private javax.swing.JMenuItem mnOrdenModule;
    private javax.swing.JMenu mnPanel;
    private javax.swing.JMenuItem mnPanelBitacora;
    private javax.swing.JMenuItem mnQuoatationAlmacen;
    private javax.swing.JMenu mnRef;
    private javax.swing.JMenuItem mnRelacionTR;
    private javax.swing.JMenuItem mnSaleRemision;
    private javax.swing.JMenuItem mnSalesCancel;
    private javax.swing.JMenuItem mnSalesClientSynch;
    private javax.swing.JMenuItem mnSalesClients;
    private javax.swing.JMenuItem mnSalesInvoice;
    private javax.swing.JMenuItem mnSalesInvoiceVer;
    private javax.swing.JMenuItem mnSalesQuote;
    private javax.swing.JMenuItem mnSalesRemision;
    private javax.swing.JMenuItem mnSalesRequirePO;
    private javax.swing.JMenuItem mnServCot;
    private javax.swing.JMenuItem mnServCotRef;
    private javax.swing.JMenu mnServcios;
    private javax.swing.JMenuItem mnUpdateClientes;
    private javax.swing.JMenuItem mnUpdateItems;
    private javax.swing.JMenu mnVentas;
    private javax.swing.JMenuItem mnVentasClientes;
    private javax.swing.JMenuItem mnVentasFactura;
    private javax.swing.JProgressBar prgMain;
    private org.jdesktop.swingx.JXStatusBar stBar;
    // End of variables declaration//GEN-END:variables
    public static session.Credential cred;
    public static servApp app;
    SIIL.panel.notif.Panel rpanel;
    String args[];
    Progress progress;
    public static ToolLoginModule login;
    public static SIIL.sockets.ClientSideClient socket;
    public static Thread socketTh;
    public static final String BACKWARD_BD = "bc.tj";
    
    public javax.swing.JDesktopPane getDesktopPane()
    {
        return desktopPane;
    }
    
    @Override
    public void run() 
    {
        SIIL.servApp.cred  = new session.Credential();
        session.User user = null;
        System.out.println("Inicio de apliacacion.");
        //servApp servApp = new servApp();
        if(args.length > 0)
        {
            if(args[0].equals("-u"))
            {
                SIIL.desktop.auth.Login lg = new SIIL.desktop.auth.Login();
                desktopPane.add(lg);
                int x = desktopPane.getSize().width/2 - lg.getSize().width/2;
                int y = desktopPane.getSize().height/2 - lg.getSize().height/2;        
                lg.setLocation(x, y);
                lg.setVisible(true);
                lg.txUser.setText(args[1]);
                lg.pw.setText(args[2]);
            }
            else if(System.getProperty("tools.username") != null)
            {
                SIIL.desktop.auth.Login lg = new SIIL.desktop.auth.Login();
                desktopPane.add(lg);
                int x = desktopPane.getSize().width/2 - lg.getSize().width/2;
                int y = desktopPane.getSize().height/2 - lg.getSize().height/2;        
                lg.setLocation(x, y);
                lg.setVisible(true);
                lg.txUser.setText(System.getProperty("tools.username"));                
            }
            else
            {
                SIIL.desktop.auth.Login lg = new SIIL.desktop.auth.Login();
                desktopPane.add(lg);

                int x = desktopPane.getSize().width/2 - lg.getSize().width/2;
                int y = desktopPane.getSize().height/2 - lg.getSize().height/2;        
                lg.setLocation(x, y);
                lg.setVisible(true);
            }
        }
        else
        {
            System.out.println("Desplegando login.");
            SIIL.desktop.auth.Login lg = new SIIL.desktop.auth.Login();
            desktopPane.add(lg);
            
            int x = desktopPane.getSize().width/2 - lg.getSize().width/2;
            int y = desktopPane.getSize().height/2 - lg.getSize().height/2;        
            lg.setLocation(x, y);
            lg.setVisible(true);
        }  
    }
}
