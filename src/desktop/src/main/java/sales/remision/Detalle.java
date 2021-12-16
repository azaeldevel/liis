
package sales.remision;

import SIIL.Server.Database;
import core.Renglon;
import database.mysql.sales.Remision;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import process.Moneda;

/**
 *
 * @author Azael Reyes
 */
public class Detalle extends javax.swing.JPanel 
{
    private Database dbserver;

    
    
    private int getSelectedRow() 
    {
        if(tbList.getSelectedRow() < 0) 
        {
            return tbList.getSelectedRow();
        }
        else
        {
            return tbList.convertRowIndexToModel(tbList.getSelectedRow());
        }
    }

    private void setCosto() 
    {
        int select = getSelectedRow();
        if (select < 0) 
        {
            JOptionPane.showMessageDialog
            (
                this,
                "Seleccione un registro.",
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        DetalleTableNumber model = (DetalleTableNumber) tbList.getModel();
        Renglon renglon = model.getValueAt(select);
        String costo = (String)JOptionPane.showInputDialog
        (
                this,
                "Indique el costo por unidad para '" + renglon.getNumber() +"'",
                "Captura de costo",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null
        );
        if(costo != null)
        {
            double co = 0;
            try
            {
                co = Double.parseDouble(costo);
            }
            catch(NumberFormatException ex)
            {
                //Logger.getLogger(FacturaRead.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog
                (
                    this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            
            if(convertMoneda() == null)
            {
                return;
            }
            
            openDatabase(true);
            
            try
            {
                renglon.upCostSale(dbserver, co, convertMoneda());
                renglon.downCostSale(dbserver);
            }
            catch (SQLException ex)
            {
                //Logger.getLogger(FacturaRead.class.getName()).log(Level.SEVERE, null, ex);
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
        try
        {
            dbserver.commit();
        }
        catch (SQLException ex)
        {
            //Logger.getLogger(FacturaRead.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog
            (
                this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            try
            {
                dbserver.rollback();
            }
            catch (SQLException ex1)
            {
                //Logger.getLogger(FacturaRead.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog
                    (
                            this,
                            ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            }
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
    
    
    private Moneda convertMoneda()
    {
        switch(cbMoney.getSelectedIndex())
        {
            case 1:
                return Moneda.MXN;
            case 2:
                return Moneda.USD;
            default:
                JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una moneda",
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                return null;
        }
    }
    
    /**
     * Creates new form Detalle
     * @param remision
     */
    public Detalle(Remision remision) 
    {
        initComponents();
        DetalleTableNumber detalle = (DetalleTableNumber) tbList.getModel();
        try 
        {
            openDatabase(true);
            detalle.download(dbserver, remision);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(Detalle.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog
            (
                this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
        }
        setTotales();
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
        tbList = new org.jdesktop.swingx.JXTable();
        cbMoney = new javax.swing.JComboBox();
        txSubTotal = new javax.swing.JTextField();
        txTotal = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btGuardar = new javax.swing.JButton();

        tbList.setModel(new DetalleTableNumber());
        tbList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbList);

        cbMoney.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Moneda...", "Pesos", "Dolar" }));
        cbMoney.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbMoneyActionPerformed(evt);
            }
        });

        txSubTotal.setEditable(false);
        txSubTotal.setText("$");

        txTotal.setEditable(false);
        txTotal.setText("$");

        jLabel1.setText("Subtotal");

        jLabel2.setText("Total");

        btGuardar.setText("Guardar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cbMoney, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel2))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btGuardar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 467, Short.MAX_VALUE)
                                .addComponent(jLabel1)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txSubTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                            .addComponent(txTotal))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbMoney, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(btGuardar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap(56, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cbMoneyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbMoneyActionPerformed
        convertMoneda();
    }//GEN-LAST:event_cbMoneyActionPerformed

    private void tbListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbListMouseClicked
        if(evt.getClickCount() == 2)
        {
            setCosto();
            setTotales();
        }
    }//GEN-LAST:event_tbListMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btGuardar;
    private javax.swing.JComboBox cbMoney;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private org.jdesktop.swingx.JXTable tbList;
    private javax.swing.JTextField txSubTotal;
    private javax.swing.JTextField txTotal;
    // End of variables declaration//GEN-END:variables

    private void setTotales() 
    {
        DetalleTableNumber detalle = (DetalleTableNumber) tbList.getModel();
        double subT = 0.0;
        int rCont = detalle.getRowCount();
        DecimalFormat df = new DecimalFormat("$ 0.00");
        for(int i = 0; i < rCont ; i++)
        {
            subT += detalle.monto(i);
        }
        txSubTotal.setText(df.format(subT));
        double total = subT * 1.15;        
        txTotal.setText(df.format(total));
    }
}
