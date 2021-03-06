
package purchases.order;

import SIIL.Server.Database;
import SIIL.Servicios.Orden.Read;
import SIIL.service.quotation.ServiceQuotation;
import SIIL.servApp;
import static SIIL.servApp.cred;
import SIIL.service.quotation.OrdenAutorizar;
import core.Renglon;
import database.mysql.purchases.order.PO;
import database.mysql.sales.Quotation;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import org.jdesktop.swingx.JXDialog;
import org.xml.sax.SAXException;
import process.Return;
import process.Row;
import stock.Flow;
import stock.Refection;

/**
 *
 * @author Azael Reyes
 */
public class Renglones extends javax.swing.JPanel
{
    private JXDialog dlg;    
    private PO po;
    private RenglonesTableModel rengModel;
    private JDesktopPane desktopPane;
    private HashMap<Integer,Row> toAdd;
    private Database dbserver;
        
    private int getSelectRow() 
    {
        if(tbList.getSelectedRow() < 0 ) 
        {
            return tbList.getSelectedRow();
        }
        else
        {            
            return tbList.convertRowIndexToModel(tbList.getSelectedRow());
        }
    }
        
    /**
     * Creates new form Edit
     * @param desktopPane
     * @param po Orden de compra se procesa.
     */
    public Renglones(JDesktopPane desktopPane, PO po) 
    {
        initComponents();
        this.toAdd = new HashMap<>();
        this.desktopPane = desktopPane;
        this.po = po;
        rengModel = (RenglonesTableModel) tbList.getModel();                
        openDatabase(true);        
        try 
        {
            rengModel.download(dbserver, po);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(Renglones.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        tbList.setModel(rengModel);
        closeDatabase();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mnMain = new javax.swing.JPopupMenu();
        mnAddFromCuotation = new javax.swing.JMenuItem();
        mnAddManully = new javax.swing.JMenuItem();
        mnDelete = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbList = new org.jdesktop.swingx.JXTable();

        mnAddFromCuotation.setText("Agregar desde Cotizacion");
        mnAddFromCuotation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAddFromCuotationActionPerformed(evt);
            }
        });
        mnMain.add(mnAddFromCuotation);

        mnAddManully.setText("Capturar Manual");
        mnAddManully.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAddManullyActionPerformed(evt);
            }
        });
        mnMain.add(mnAddManully);

        mnDelete.setText("Eliminar");
        mnDelete.setToolTipText("");
        mnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnDeleteActionPerformed(evt);
            }
        });
        mnMain.add(mnDelete);

        tbList.setModel(new RenglonesTableModel());
        tbList.setComponentPopupMenu(mnMain);
        jScrollPane1.setViewportView(tbList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void mnAddFromCuotationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAddFromCuotationActionPerformed
        SIIL.Servicios.Orden.Read read = new SIIL.Servicios.Orden.Read(cred,Read.Mode.SELECTION_AUTHO,desktopPane);
        JXDialog dlg = new JXDialog(read);
        dlg.setSize(read.getPreferredSize());
        read.setDialog(dlg);
        dlg.setContentPane(read);
        int x = desktopPane.getSize().width/2 - dlg.getSize().width/2;
        int y = 10;
        dlg.setLocation(x, y);
        dlg.setLocationRelativeTo(null);
        dlg.setModal(true);
        dlg.setVisible(true);
        ServiceQuotation orden = read.getSelection();
        
        if(orden == null)
        {
            JOptionPane.showMessageDialog(
                this,
                "Selecciona una orden antes de continuar",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        if(!(orden instanceof OrdenAutorizar))
        {
            JOptionPane.showMessageDialog(
                this,
                "Deve seleccionar un arden que ya ha sido autorizada y aun no tiene E.T.A.",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        openDatabase(true);
        try 
        {
            orden.downQuotation(dbserver.getConnection());
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(Renglones.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        Quotation quotation = orden.getQuotation();
        if(quotation == null)
        {
            JOptionPane.showMessageDialog
            (
                this,
                "No hay cotizacion para la orden de servicio.",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        RenglonesTableModel model = (RenglonesTableModel) tbList.getModel();        
        List<core.Renglon> selections = null;
        try 
        {
            selections = Renglon.select(dbserver, quotation, Renglon.RenglonExclusion.SIIL_Y_SEVICICOS);
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
        if(selections == null) 
        {
            JOptionPane.showMessageDialog(this,
                "No hay elementos",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        int countAdd = 0;
        for(core.Renglon r : selections)
        {
            if(r.getPO() != null)
            {
                try 
                {
                    r.getPO().downFolio(dbserver);
                    r.getPO().downSerie(dbserver);
                    JOptionPane.showMessageDialog(this,
                            "El renglon '" + r.getNumber() + "' ya esta asignado al PO '" + r.getPO().getFullFolio() + "' por lo tanto ser?? ignorado.",
                            "Error Interno",
                            JOptionPane.ERROR_MESSAGE
                    );
                    continue;
                } 
                catch (SQLException ex) 
                {
                    Logger.getLogger(Renglones.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            try 
            {
                //El rengonlon optenido es de una cotizacion, hay que crear el correpondinete para la PO.
                //core.Renglon renglonOP = new core.Renglon();
                r.download(dbserver, quotation, false);
                if(r.isRefection(dbserver)) //es una refaccion y no esta en un PO?
                {
                    Return retInsert = core.Renglon.insert(dbserver, po, r);                    
                    if(retInsert.isFail())
                    {
                        JOptionPane.showMessageDialog(this,
                        retInsert.getMessage(),
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                    r.upPO(dbserver, po);
                    if(r.downQuotation(dbserver))
                    {
                        r.getQuotation().download(dbserver);
                    }
                    else
                    {
                        r.upQuotation(dbserver, quotation);
                    }
                    model.add(r);
                    countAdd++;
                }
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
        model.reload();//se cargaron varios datos hay que actualizar la vista.
        if(countAdd < 1)
        {
            closeDatabase();
            return;
        }
        String msg = "Se agregaron '" + countAdd + "' elemento(s) a el PO '" + po.getFullFolio() + "'";
        int option = JOptionPane.showOptionDialog(
                this,
                msg + ". ??Desea guardar esta infomaci??n?" ,
                "Confirmaci??n de solicitud",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,null,null);
        servApp.Progress msgs = (servApp.Progress)SIIL.servApp.getInstance().getProgressObject();
        if(option == JOptionPane.YES_OPTION)
        {
            try 
            {
                dbserver.commit();
                msgs.setProgress(100, msg);
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
        else
        {
            try 
            {
                dbserver.rollback();
                msgs.setProgress(0, "Operaci??n <<cancelada>>: " + msg);
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
        
        closeDatabase();
    }//GEN-LAST:event_mnAddFromCuotationActionPerformed

    private void mnAddManullyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAddManullyActionPerformed
        
        
        openDatabase(true);
        String strnumber = (String) JOptionPane.showInputDialog(
            this,
            "Indique el numero",
            "Captura",
            JOptionPane.PLAIN_MESSAGE,
            null,
            null,
        "");
        
        Refection refection = new Refection(-1);
        try 
        {
            if(!refection.selectNumber(dbserver.getConnection(), strnumber))
            {
                JOptionPane.showMessageDialog(this,
                "N??mero desconocido",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            Flow flow = new Flow(-1);
            flow.insert(dbserver.getConnection(), new Date(), false, "?", refection);
            flow.downItem(dbserver);
            flow.getItem().downNumber(dbserver.getConnection());
            flow.getItem().downDescription(dbserver.getConnection());
            flow.upCantidad(dbserver, 1);
            Row rowOP = new Row(-1);
            rowOP.insert(dbserver.getConnection(), po, flow);            
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(Renglones.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
                );
                return;
        }       
        
        int option = JOptionPane.showConfirmDialog(
        this,
        "Est?? agregando '" + strnumber + "' a la orden de compra, ??Desea continar la operacion?",
        "Confirmar operaci??",
        JOptionPane.YES_NO_OPTION);
        if(option == JOptionPane.YES_OPTION)
        {
            try 
            {
                dbserver.commit();
                RenglonesTableModel model = (RenglonesTableModel) tbList.getModel();
                model.download(dbserver, po);
                model.reload();
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(Renglones.class.getName()).log(Level.SEVERE, null, ex);
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
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(Renglones.class.getName()).log(Level.SEVERE, null, ex);
                //Logger.getLogger(Renglones.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }
        closeDatabase();
    }//GEN-LAST:event_mnAddManullyActionPerformed

    private void mnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnDeleteActionPerformed
        int row = getSelectRow();
        if(row < 0)
        {
            JOptionPane.showMessageDialog(this,
                "Seleccione un registro",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
                );
                return;
        }
        openDatabase(true);
        RenglonesTableModel model = (RenglonesTableModel) tbList.getModel();
        Return ret = null;
        try 
        {
            ret = model.remove(dbserver , row);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(Renglones.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(ret.isFlag())
        {
            try 
            {
                dbserver.commit();
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
        else
        {
            try 
            {
                dbserver.rollback();
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
        closeDatabase();
    }//GEN-LAST:event_mnDeleteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem mnAddFromCuotation;
    private javax.swing.JMenuItem mnAddManully;
    private javax.swing.JMenuItem mnDelete;
    private javax.swing.JPopupMenu mnMain;
    private org.jdesktop.swingx.JXTable tbList;
    // End of variables declaration//GEN-END:variables

    /**
     * @param dlg the dlg to set
     */
    public void setDialog(JXDialog dlg) 
    {
        this.dlg = dlg;
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
}
