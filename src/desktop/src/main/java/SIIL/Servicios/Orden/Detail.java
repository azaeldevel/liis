
package SIIL.Servicios.Orden;

import SIIL.Server.Database;
import SIIL.service.quotation.Orcom;
import SIIL.Server.MySQL;
import SIIL.Server.User;
import SIIL.artifact.AmbiguosException;
import SIIL.artifact.DeployException;
import SIIL.service.quotation.ServiceQuotation;
import java.io.IOException;
import session.Credential;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Azael
 */
class Detail extends javax.swing.JInternalFrame 
{
    @Deprecated
    private Orcom ord;
    @Deprecated
    private String BD;
    
    /**
    *
    */
    Detail(ServiceQuotation ord, Credential cred) 
    {
        initComponents();
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
        Throwable fill = ord.fillDetail(db,cred,ord.getID());
        try 
        {
            if(ord.downPAutho(db))
            {
                ord.getPAutho().download(db);
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Detail.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
        }
        
        diplay(ord);
    }
    
    /**
    *
    */
    @Deprecated
    Detail(String bd, Orcom ord) 
    {
        initComponents();
        BD = bd;
        this.ord = ord;
        fillFields();
    }

    @Deprecated
    private void fillFields()  
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
        
        String sql = "SELECT estado,compBD,compNumber,compName,fhAutho,fhETA,fhArribo,fhSurtido,fhFin,folio,serie,ownerName,fhFolio,fhEdit,creator,fhETAfl FROM Orcom_Resolved WHERE BD = ? and folio = ? and ID = ?";
        
        PreparedStatement stmt;
        try 
        {
            stmt = dbserver.getConnection().prepareStatement(sql);
            stmt.setString(1, BD);
            stmt.setInt(2, ord.getFolio());
            stmt.setInt(3, ord.getID());
            
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                //estado
                if(rs.getString("estado").equals("docpen"))
                {                    
                    txEdo.setText("Cotizado");
                }
                else if(rs.getString("estado").equals("pedpen"))
                {
                    txEdo.setText( "Autorizado");                    
                }
                else if(rs.getString("estado").equals("pedtrans"))
                {
                    txEdo.setText("En Transito");                    
                }
                else if(rs.getString("estado").equals("pedArrb"))
                {
                    txEdo.setText("S.A.");                    
                }
                else if(rs.getString("estado").equals("pedsur"))
                {
                    txEdo.setText("Entregando");                    
                }
                else if(rs.getString("estado").equals("pedfin"))
                {
                    txEdo.setText("Final");                    
                }
                else if(rs.getString("estado").equals("docedit"))
                {
                    txEdo.setText("Edición");                    
                }
                else if(rs.getString("estado").equals("cancel"))
                {
                    txEdo.setText("Cancelación");                    
                }
                else
                {
                    txEdo.setText(rs.getString("estado"));
                }
                
                //CN Cot.
                txCot.setText(rs.getString("folio"));                
                
                //Cliente
                if(rs.getString("compNumber") != null)
                {
                    txClient.setText(rs.getString("compNumber") + " - " + rs.getString("compName"));
                }
                
                //Cliente
                if(rs.getString("creator") != null)
                {
                    User u = new User();
                    u.setAlias(rs.getString("creator"));
                    u.down(dbserver);
                    txCreator.setText(u.toString());
                }

                //Fecha de folio
                if(rs.getTimestamp("fhFolio") != null)
                {
                    Date dt = new Date(rs.getTimestamp("fhFolio").getTime());
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    txFh.setText(sdf.format(dt));
                }
                
                //Fecha de Autorizacion
                if(rs.getTimestamp("fhAutho") != null)
                {
                    Date dt = new Date(rs.getTimestamp("fhAutho").getTime());
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    txfhAutho.setText(sdf.format(dt));
                }
                
                //Fecha de Edición
                if(rs.getTimestamp("fhEdit") != null)
                {
                    Date dt = new Date(rs.getTimestamp("fhEdit").getTime());
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    txEdit.setText(sdf.format(dt));
                }
                
                //Fecha ETA
                if(rs.getDate("fhETA") != null)
                {
                    Date dt = new Date(rs.getDate("fhETA").getTime());
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");                    
                    txETA.setText(sdf.format(dt));
                }
                
                //Fecha ETAfl
                if(rs.getTimestamp("fhETAfl") != null)
                {
                    Date dt = new Date(rs.getTimestamp("fhETAfl").getTime());
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    txETAfl.setText(sdf.format(dt));
                }
                
                //Fecha Arribo
                if(rs.getTimestamp("fhArribo") != null)
                {
                    Date dt = new Date(rs.getTimestamp("fhArribo").getTime());
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    txfhArribo.setText(sdf.format(dt));
                }
                
                //Fecha Surtido
                if(rs.getTimestamp("fhSurtido") != null)
                {
                    Date dt = new Date(rs.getTimestamp("fhSurtido").getTime());
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    txfhSurtido.setText(sdf.format(dt));
                }

                //Fecha Finalizacion
                if(rs.getTimestamp("fhFin") != null)
                {
                    Date dt = new Date(rs.getTimestamp("fhFin").getTime());
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
                    txfhFin.setText(sdf.format(dt));
                }
                
                //Cliente
                if(rs.getString("ownerName") != null)
                {
                    txMang.setText(rs.getString("ownerName"));
                }                
            }
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(Detail.class.getName()).log(Level.SEVERE, null, ex);
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

        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txfhSurtido = new javax.swing.JTextField();
        txCot = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txClient = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txfhFin = new javax.swing.JTextField();
        txfhAutho = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txEdo = new javax.swing.JTextField();
        txETA = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txfhArribo = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txCreator = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txFh = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txEdit = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txMang = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txETAfl = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txTec = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txTerminal = new javax.swing.JTextArea();
        jLabel14 = new javax.swing.JLabel();
        txPAutho = new javax.swing.JTextField();

        setClosable(true);
        setTitle("Detalle");

        jLabel6.setText("Asignación S.A.");

        jLabel1.setText("CN Cotización");

        txfhSurtido.setEditable(false);
        txfhSurtido.setToolTipText("Fecha de Operación");

        txCot.setEditable(false);

        jLabel2.setText("Cliente");

        txClient.setEditable(false);

        jLabel8.setText("Finalizado");

        jLabel3.setText("Fecha de Autorización");

        txfhFin.setEditable(false);
        txfhFin.setToolTipText("Fecha de Operación para Ternimar y Cancelar");

        txfhAutho.setEditable(false);
        txfhAutho.setToolTipText("Fecha de Operación");

        jLabel9.setText("Estado");

        jLabel4.setText("ETA");

        txEdo.setEditable(false);

        txETA.setEditable(false);
        txETA.setToolTipText("Tiempo estimado de Arribo");

        jLabel5.setText("Confirm. Arribo");

        txfhArribo.setEditable(false);
        txfhArribo.setToolTipText("Fecha de Operación");

        jLabel7.setText("Creador");

        txCreator.setEditable(false);

        jLabel10.setText("Fecha de Creación");

        txFh.setEditable(false);
        txFh.setToolTipText("Fecha de Operación");

        jLabel11.setText("Fecha de Edición");

        txEdit.setEditable(false);
        txEdit.setToolTipText("Fecha de Operación");

        jLabel12.setText("Encargado");

        txMang.setEditable(false);

        jLabel13.setText(";");

        txETAfl.setEditable(false);
        txETAfl.setToolTipText("Fecha de Operación");

        jLabel15.setText("Técnico");

        txTec.setEditable(false);

        jLabel16.setText("Comentario Terminal");

        txTerminal.setEditable(false);
        txTerminal.setColumns(20);
        txTerminal.setRows(5);
        jScrollPane2.setViewportView(txTerminal);

        jLabel14.setText(";");

        txPAutho.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel9))
                                .addGap(41, 41, 41)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txEdo, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txCot, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txfhArribo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel15))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txfhSurtido)
                                    .addComponent(txfhFin)
                                    .addComponent(txCreator)
                                    .addComponent(txMang)
                                    .addComponent(txTec, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel16)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(63, 63, 63)
                                .addComponent(txETA, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txETAfl, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel3))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txClient)
                                        .addComponent(txFh)
                                        .addComponent(txEdit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txfhAutho, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txPAutho, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txEdo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txCot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txClient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txFh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txfhAutho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(txPAutho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txETA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(txETAfl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txfhArribo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txfhSurtido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txfhFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txCreator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txMang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txTec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField txClient;
    private javax.swing.JTextField txCot;
    private javax.swing.JTextField txCreator;
    private javax.swing.JTextField txETA;
    private javax.swing.JTextField txETAfl;
    private javax.swing.JTextField txEdit;
    private javax.swing.JTextField txEdo;
    private javax.swing.JTextField txFh;
    private javax.swing.JTextField txMang;
    private javax.swing.JTextField txPAutho;
    private javax.swing.JTextField txTec;
    private javax.swing.JTextArea txTerminal;
    private javax.swing.JTextField txfhArribo;
    private javax.swing.JTextField txfhAutho;
    private javax.swing.JTextField txfhFin;
    private javax.swing.JTextField txfhSurtido;
    // End of variables declaration//GEN-END:variables

    private void diplay(ServiceQuotation ord) 
    {
        txEdo.setText(ord.getState().getName());
        txCot.setText(ord.getFolio().toString());
        txClient.setText(ord.getCompany().toString());

        //Fecha de folio
        if(ord.getFhCretion() != null)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
            txFh.setText(sdf.format(ord.getFhCretion()));
        }
        
        //Fecha de Edición
        if(ord.getFhEdit() != null)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
            txEdit.setText(sdf.format(ord.getFhEdit()));
        }

        //Fecha de Autorizacion
        if(ord.getFhAutho() != null)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
            txfhAutho.setText(sdf.format(ord.getFhAutho()));
        }
        if(ord.getPAutho() != null)
        {
            txPAutho.setText(ord.getPAutho().toString());
        }

        //Fecha ETA
        if(ord.getFhETA() != null)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");                    
            txETA.setText(sdf.format(ord.getFhETA()));
        }
                
        //Fecha ETAfl
        if(ord.getFhETAfl() != null)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
            txETAfl.setText(sdf.format(ord.getFhETAfl()));
        }
        
        if(ord.getConfirmArribo() != null)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
            txfhArribo.setText(sdf.format(ord.getConfirmArribo()));            
        }
        
        if(ord.getSA() != null)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
            txfhArribo.setText(sdf.format(ord.getConfirmArribo())); 
        }
        
        //Fecha Surtido
        if(ord.getFhSurtir() != null)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
            txfhSurtido.setText(sdf.format(ord.getFhSurtir()));
        }
                
        if(ord.getFhEnd() != null)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");                    
            txfhFin.setText(sdf.format(ord.getFhEnd()));
        }
        
        if(ord.getCreator() != null )
        {
            txCreator.setText(ord.getCreator().toString());
        }
        
        if(ord.getOwner() != null)
        {
            txMang.setText(ord.getOwner().toString());
        }
        
        if(ord.getTechnical() != null)
        {
            txTec.setText(ord.getTechnical().toString());
        }
        
        txTerminal.setText(ord.getTerminalComment());        
    }
}
