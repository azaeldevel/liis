
package SIIL.sales.rows;

import SIIL.CN.Sucursal;
import SIIL.CN.Tables.COTIZACI;
import SIIL.Server.Database;
import SIIL.core.Office;
import SIIL.service.quotation.ServiceQuotation;
import database.mysql.sales.Operational;
import database.mysql.sales.Quotation;
import java.awt.HeadlessException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import process.Flag;
import process.Return;

/**
 * @version 0.1.0
 * @author Azael Reyes
 */
public class Rows extends javax.swing.JPanel 
{
    public enum Filter
    {
        NO_ONE,
        NOT_QOUTATION,
        NOT_PO        
    }
    private JDialog dialog;
    private ServiceQuotation orden;
    private Database dbserver;
    private Operational operational;
    private Flag mode;
    private List<core.Renglon> selection;
    Filter filter;
    
    
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
    
    private void closeDatabase() 
    {
        if(dbserver != null)
        {
            dbserver.close();
            dbserver = null;
        }
    }
        
    /**
     * 
     * Creates new form Rows
     * @param mode
     * @param quotation
     * @param filter
     */
    public Rows(Flag mode, Quotation quotation,Filter filter) 
    {
        initComponents(); 
        this.filter = filter;
        this.mode = mode;
        operational = quotation;
        openDatabase(true);
        
        RowsTableModel model = (RowsTableModel) tbList.getModel();
        try 
        {
            model.setQuotation(dbserver,quotation);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(Rows.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        tbList.setModel(model);
    }
    
    /**
     * 
     * Creates new form Rows
     * @param orden
     */
    public Rows(ServiceQuotation orden)
    {
        initComponents();
        this.orden = orden;
        this.filter = Filter.NO_ONE;
        this.mode = Flag.WORKING_MODE;
        openDatabase(false);       
        
        try 
        {
            if(orden.downQuotation(dbserver.getConnection()).isFlag())
            {
                operational = orden.getQuotation();
                operational.downTotal(dbserver);
                operational.downFolio(dbserver);
                operational.downOffice(dbserver);
                operational.getOffice().download(dbserver.getConnection());
            }
            else
            {
                COTIZACI tbCotiza = null;
                Office office = null;
                try 
                {
                    switch (orden.getOffice()) 
                    {
                        case "bc.tj":
                            tbCotiza = new COTIZACI(Sucursal.BC_Tijuana);
                            office = new Office(1);
                            break;
                        case "bc.mx":
                            tbCotiza = new COTIZACI(Sucursal.BC_Mexicali);
                            office = new Office(2);
                            break;
                        case "bc.ens":
                            tbCotiza = new COTIZACI(Sucursal.BC_Ensenada);
                            office = new Office(3);
                            break;
                        default:
                            JOptionPane.showMessageDialog(
                            this,
                            "Base de datos deconocida",
                            "Error Exteno",
                             JOptionPane.ERROR_MESSAGE
                            );
                            return;
                    }
                    office.download(dbserver.getConnection());
                    Return ret = orden.importFromCN(dbserver, tbCotiza, office);   
                    if(ret.isFail())
                    {
                        JOptionPane.showMessageDialog(
                            this,
                            ret.getMessage(),
                            "Error Exteno",
                             JOptionPane.ERROR_MESSAGE
                            );
                            return;
                    }
                    dbserver.commit();
                    tbCotiza.close();
                    openDatabase(false);
                }
                catch (IOException ex) 
                {
                    JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Error Exteno",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
                                
                if(orden.getQuotation() != null)
                {
                    operational = orden.getQuotation();
                    operational.downTotal(dbserver);
                    if(operational.downOffice(dbserver)) 
                    {
                        operational.getOffice().download(dbserver.getConnection());
                    }
                    else
                    {
                        JOptionPane.showMessageDialog
                        (
                            this,
                            "No se a asignado la oficina de la cotización",
                            "Error Interno",
                            JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                }
            }
        }
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog
            (
                this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        Return ret = null;
        try 
        {
            ret = generate();
            if(ret.isFail())
            {
                JOptionPane.showMessageDialog(this,
                "Al generar renglones :" + ret.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
                );
            }
            openDatabase(false);
        }
        catch (IOException | SQLException ex) 
        {
            //Logger.getLogger(Rows.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        openDatabase(false);        
        RowsTableModel model = (RowsTableModel) tbList.getModel();        
        try 
        {
            model.setQuotation(dbserver,orden.getQuotation());
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
        
        tbList.setModel(model);
        tbList.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tbList.getColumnModel().getColumn(4).setPreferredWidth(10);
        tbList.getColumnModel().getColumn(3).setPreferredWidth(10);
        tbList.getColumnModel().getColumn(2).setPreferredWidth(10);
        tbList.getColumnModel().getColumn(1).setPreferredWidth(250);
    }

    private Return generate() throws HeadlessException, SQLException, IOException 
    {
        Return ret = operational.fromCNRenglones(dbserver, process.Operational.TablaRenglon.COTIZACION);        
        if (ret.isFlag()) 
        {
            dbserver.commit();
            if(operational.getTotal() > 0)
            {
                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                String moneyString = formatter.format(operational.getTotal());
                txMonto.setText(moneyString);
            }
            else
            {
                return ret;
            }
        }
        else 
        {
            return ret;
        }
        
        return new Return(true);
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
        tbList = new javax.swing.JTable();
        txMonto = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        tbList.setModel(new RowsTableModel());
        tbList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbListKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tbList);

        txMonto.setEditable(false);

        jLabel1.setText("Total");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 667, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txMonto, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txMonto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tbListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbListKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER)
        {
            int[] sel = tbList.getSelectedRows();
            RowsTableModel model = (RowsTableModel) tbList.getModel();
            selection = new ArrayList<>();
            core.Renglon renglon = null;
            for(int i : sel)
            {       
                renglon = model.getRenglon(i);
                switch(filter)
                {
                    case NOT_PO:
                        if(renglon.getPO() != null)
                        {
                            JOptionPane.showMessageDialog(this,
                                "El elemento '" + renglon.getNumber() + "' ya está comprado en '" + renglon.getPO().getFullFolio() + "'" ,
                                "Error Interno",
                                JOptionPane.ERROR_MESSAGE
                            );
                            selection = null;
                            return;
                        }
                        break;
                }
                selection.add(renglon);
            }
            evt.consume();
            dialog.dispose();
        }        
    }//GEN-LAST:event_tbListKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbList;
    private javax.swing.JTextField txMonto;
    // End of variables declaration//GEN-END:variables

    /**
     * @param dialog the dialog to set
     */
    public void setDialog(JDialog dialog) 
    {
        this.dialog = dialog;          
    }

    /**
     * @return the selection
     */
    public List<core.Renglon> getSelection() 
    {
        return selection;
    }
}
