
package purchases.order;

import SIIL.Server.Database;
import core.Renglon;
import core.Words;
import database.mysql.purchases.Provider;
import database.mysql.purchases.order.PO;
import java.awt.HeadlessException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.parsers.ParserConfigurationException;
import org.jdesktop.swingx.JXDialog;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.xml.sax.SAXException;
import process.Module;
import process.Return;
import process.Row;
import process.State;

/**
 *
 * @author Azael Reyes
 */
public class List extends javax.swing.JPanel 
{
    private final String MYSQL_AVATAR_TABLE = "PurchasesOrder"; 
    private final String MYSQL_AVATAR_TABLE_RESOLVED = "PurchasesOrder_Resolved"; 
    private static final int DEFAULT_LENG = 10;
    private static final String DEFAULT_SERIE = "TTJ";  
    private Database dbserver;
    private JDialog dialog;
    public enum Mode
    {
        INIT,
        RELOAD,
        SEARCH
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
    
    private boolean generateCSV(Database dbserver, JTextArea textAre, PO po) throws SQLException 
    {
        java.util.List<Renglon> list = Renglon.select(dbserver, po, null);
        if(list == null) return false;
        for(Renglon r : list)
        {
            r.download(dbserver, po, false);
            textAre.append(r.getNumber() + "," + r.getCantidad() + "\n");
        }
        return true;
    }
    
    private PO getSelectItem() 
    {
        if(tbList.getSelectedRow()>-1)
        {
            TreeSelectionModel tsm = tbList.getTreeSelectionModel();
            TreeNode selectedNode = (TreeNode) tsm.getSelectionPath().getLastPathComponent();
            PONode poNode = (PONode) selectedNode;
            return poNode.getUserObject();
        }
        else
        {
            return null;
        }
    }
    
    public List() 
    {
        initComponents();    
        search(null,null,(int)txCount.getValue(),false);
    }
    
    
    /**
     * 
     * @param connection
     * @param mode
     * @param where
     * @param limit
     * @param state
     * @return 
     * @throws SQLException 
     */
    public POHeaderNode search(Database connection,Mode mode,String where, int limit,State state) throws SQLException 
    {
        if(connection == null)
        {
            return null;
        }
        
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE_RESOLVED;
        sql = sql + " WHERE  ";
        switch (SIIL.servApp.cred.getOffice().getType()) 
        {
            case "s":
                sql = sql + "(office = " + SIIL.servApp.cred.getOffice().getCode();
                break;
            case "m":
                sql = sql + "(";
                break;
            default:
                return null;
        }
        sql = sql + " type = '" + PO.TYPE + "' AND flag = 'A') AND state = " + state.getID();
        if(where != null)
        {
            sql = sql + " AND (id like '%" + where + "%' OR provNameShort like '%" + where + "%' AND provRazonSocial like '%"  + where + "%' AND folio like '%" + where + "%' AND strFolio like '%" + where + "%')";
        }
        sql = sql + " ORDER BY uxfhFolio DESC " ;
        if(limit > 0)
        {
            sql = sql + " LIMIT " + limit;
        }
        //System.out.println(sql);        
        Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        PO hnode = new PO(-1);
        POHeaderNode headerNode = new POHeaderNode(hnode);
        headerNode.title = state.getName();
        while(rs.next())
        {
            PO po = new PO(rs.getInt(1));
            if(po.download(connection).isFlag())
            {
                PONode ponode = new PONode(po);
                headerNode.add(ponode);
            }
        }
        if(headerNode.getChildCount() < 1) return null;
        return headerNode;
    }
    
    /**
     * 
     * @throws HeadlessException 
     */
    private void cretePO() throws HeadlessException 
    {
        purchases.provider.List prov = new purchases.provider.List(SIIL.servApp.cred);
        JXDialog dlg = new JXDialog(prov);
        prov.setDialog(dlg);
        dlg.setContentPane(prov);
        dlg.setSize(prov.getPreferredSize());
        dlg.setModal(true);
        dlg.setVisible(true);
        Provider provider = prov.getSelection();    
        
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
        
        Return ret3 = null;
        Return ret4 = null;
        PO po = null;
        try
        {
            //modulo
            process.Module module = new process.Module(-1);
            Return ret1 = module.select(dbserver.getConnection(),PO.MODULE);
            //estado
            process.State state = new process.State(-1);
            Return ret2 = state.select(dbserver.getConnection(),module,1);
            po = new PO(-1);
            ret3 = po.insert(dbserver, SIIL.servApp.cred.getOffice(), state, SIIL.servApp.cred.getUser(), dbserver.getTimestamp(), provider, 0, DEFAULT_SERIE, PO.TYPE);
            ret4 = po.upFlag(dbserver.getConnection(), 'A');
            if(ret3.isFlag() && ret4.getStatus() == Return.Status.DONE)
            {
                int dlgOption = JOptionPane.showConfirmDialog(
                        this,
                        "Está creando un nuevo PO ¿desea continuar con la operación?",
                        "Confirmar operacón",
                        JOptionPane.YES_NO_OPTION
                );
                if(dlgOption == JOptionPane.NO_OPTION)
                {
                    dbserver.rollback();
                    return;
                }
            }
        }
        catch (SQLException ex)
        {
            //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog
            (
                this,
                ex.getMessage(),
                "Error Interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        if(ret3.isFlag()) 
        {
            try 
            {
                dbserver.commit();
                JOptionPane.showMessageDialog(this,
                    "PO Creado con Folio '" + po.getFolio() + "'",
                    "Error Interno",
                    JOptionPane.INFORMATION_MESSAGE
                );
                search(dbserver,txSearch.getText(),(int) txCount.getValue(),true);
            }
            catch (SQLException ex) 
            {
                JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
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
                JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * 
     * @param connection
     * @param poroot 
     */
    private void buildView(PORootNode poroot,boolean expand) 
    {    
        final String[] columnNames = {"Fólio", "Proveedor", "Fecha"};
        DefaultTreeTableModel treeModel = new DefaultTreeTableModel(poroot, Arrays.asList(columnNames));
        tbList = null;
        tbList = new org.jdesktop.swingx.JXTreeTable(treeModel);
        tbList.setAutoCreateRowSorter(true);
        jScrollPane3.setViewportView(tbList);
        tbList.setComponentPopupMenu(command);
        tbList.setRootVisible(false); 
        if(expand)
        {
            tbList.expandAll();
        }
        else
        {
            tbList.collapseAll();
        }
        tbList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbListMouseClicked(evt);
            }
        });
        //tbList.repaint();
        //repaint();
    }

    private void search(Database dbserver,String search,int lenght,boolean expand) 
    {
        if(dbserver == null)
        {
            SIIL.core.config.Server serverConfig = new SIIL.core.config.Server(); 
            try 
            {
                serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
            } 
            catch (IOException | ParserConfigurationException | SAXException ex) 
            {
                JOptionPane.showMessageDialog(this,
                    "Fallo importacion.",
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }
        
        Module module = new Module(-1);
        try 
        {
            module.select(dbserver.getConnection(), PO.MODULE);
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
        try 
        {
            java.util.List<State> states = State.selectAll(dbserver,module);
            if(states == null)
            {
                JOptionPane.showMessageDialog(this,
                        "Fallo al optener la lista de estados",
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            PORootNode rootNode = new PORootNode(new PO(-1));
            POHeaderNode retS = null;
            for(State state : states)
            {
                retS = search(dbserver,Mode.INIT, search, lenght,state);
                if(retS != null) 
                {
                    //Si se retorn null como parametro no se asigno header port lo tanto hay que ignora por que no hay elementos
                    rootNode.add(retS); 
                }              
            }
            buildView(rootNode,expand);
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        command = new javax.swing.JPopupMenu();
        mnCreate = new javax.swing.JMenuItem();
        mnAdd = new javax.swing.JMenuItem();
        mnPurching = new javax.swing.JMenuItem();
        mnRep = new javax.swing.JMenu();
        mnRepCSV = new javax.swing.JMenuItem();
        mnRepPrint = new javax.swing.JMenuItem();
        mnPurchased = new javax.swing.JMenuItem();
        mnArrive = new javax.swing.JMenuItem();
        txSearch = new org.jdesktop.swingx.JXSearchField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbList = new org.jdesktop.swingx.JXTreeTable();
        btUpdate = new org.jdesktop.swingx.JXButton();
        txCount = new javax.swing.JSpinner();

        mnCreate.setText("Crear");
        mnCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnCreateActionPerformed(evt);
            }
        });
        command.add(mnCreate);

        mnAdd.setText("Agregar Renglones");
        mnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAddActionPerformed(evt);
            }
        });
        command.add(mnAdd);

        mnPurching.setText("Comprar");
        mnPurching.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnPurchingActionPerformed(evt);
            }
        });
        command.add(mnPurching);

        mnRep.setText("Reportes");

        mnRepCSV.setText("Texto");
        mnRepCSV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnRepCSVActionPerformed(evt);
            }
        });
        mnRep.add(mnRepCSV);

        mnRepPrint.setText("Imprimible");
        mnRep.add(mnRepPrint);

        command.add(mnRep);

        mnPurchased.setText("Comprada");
        mnPurchased.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnPurchasedActionPerformed(evt);
            }
        });
        command.add(mnPurchased);

        mnArrive.setText("Arribar");
        mnArrive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnArriveActionPerformed(evt);
            }
        });
        command.add(mnArrive);

        txSearch.setPrompt("buscar...");
        txSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txSearchFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txSearchFocusLost(evt);
            }
        });
        txSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txSearchActionPerformed(evt);
            }
        });

        jLabel1.setText("Cant.");

        tbList.setComponentPopupMenu(command);
        tbList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbListMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tbList);

        btUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/reload.png"))); // NOI18N
        btUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUpdateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(txSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 439, Short.MAX_VALUE)
                .addComponent(btUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txCount, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jScrollPane3)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(btUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void mnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnCreateActionPerformed
        cretePO();
    }//GEN-LAST:event_mnCreateActionPerformed

    private void btUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUpdateActionPerformed
        closeDatabase();
        openDatabase(false);
        search(dbserver,txSearch.getText(),(int) txCount.getValue(),true);
    }//GEN-LAST:event_btUpdateActionPerformed

    private void mnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAddActionPerformed
        PO po = getSelectItem();
        if(po != null)
        {
            if(po.getState().getOrdinal() == 1)
            {
                renglos(po,true);
            }
            else
            {
                State state = new State(-1);
                try 
                {
                    openDatabase(true);
                    
                    if(!state.select(dbserver, State.Steps.PO_CREATED))
                    {
                        JOptionPane.showMessageDialog(
                            this,
                            "Fallo la descarga de datos de estado",
                            "Error Interno",
                            JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                } 
                catch (SQLException ex) 
                {
                    JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Error Externo",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                JOptionPane.showMessageDialog(
                    this,
                    "Esta operación solo se realiza en el estado '" + state.getName() + "'",
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
        else
        {
            JOptionPane.showMessageDialog(this,
                "Seleccione un registro",
                "Error Externo",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_mnAddActionPerformed

    private void tbListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbListMouseClicked
       if(evt.getClickCount() == 2) 
       {
            PO po = getSelectItem();
            if(po != null)
            {
                renglos(po,false);
            }
            else
            {
                JOptionPane.showMessageDialog(this,
                    "Seleccione un registro",
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
                );
            }
       }     
    }//GEN-LAST:event_tbListMouseClicked

    private void txSearchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txSearchFocusGained
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
    }//GEN-LAST:event_txSearchFocusGained

    private void txSearchFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txSearchFocusLost
        dbserver.close();
        dbserver = null;
    }//GEN-LAST:event_txSearchFocusLost

    private void txSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txSearchActionPerformed
        search(dbserver,txSearch.getText(),(int) txCount.getValue(),true);
    }//GEN-LAST:event_txSearchActionPerformed
    
    private void mnPurchingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnPurchingActionPerformed
        PO po = getSelectItem();
        if(po != null)
        {
            openDatabase(true);
            try 
            {
                po.getState().download(dbserver);
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(List.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog
                (
                    this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
            }
            
            if(po.getState().getOrdinal() == 1)
            {
                boolean ret;
                try 
                {
                    int option = JOptionPane.showConfirmDialog
                    (
                        this,
                        "Va iniciar la compra de la orden, ¿Deseá continuar?",
                        "Confirme",
                        JOptionPane.YES_NO_OPTION
                    );
                    if(option == JOptionPane.YES_OPTION)
                    {                                              
                        ret = purchase(dbserver,po); 
                    }
                    else
                    {
                        return;
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
                
                if( ret)
                {
                    try 
                    {
                        dbserver.commit();
                        JOptionPane.showMessageDialog(
                            this,
                            "Operación realizada satisfactoriamente",
                            "Confirmacion",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        search(dbserver,null,(int)txCount.getValue(),true);
                        return;                        
                    }
                    catch (SQLException ex) 
                    {
                        //Logger.getLogger(List.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog
                        (
                            this,
                            ex.getMessage(),
                            "Error Externo",
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
                        //Logger.getLogger(List.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog
                        (
                            this,
                            ex.getMessage(),
                            "Error Externo",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
            else
            {
                State state = new State(-1);
                try 
                {
                    state.select(dbserver, State.Steps.CS_CREATED);
                    state.download(dbserver);
                }
                catch (SQLException ex) 
                {
                    //Logger.getLogger(List.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog
                    (
                        this,
                        ex.getMessage(),
                        "Error Externo",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
                JOptionPane.showMessageDialog(
                    this,
                    "Esta operación solo se realiza en el estado '" + state.getName() + "'",
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
                ); 
            }
        }
        else
        {
            JOptionPane.showMessageDialog(this,
                "Seleccione un registro",
                "Error Externo",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_mnPurchingActionPerformed

    private void mnRepCSVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnRepCSVActionPerformed
        PO po = getSelectItem();
        if(po != null)
        {            
            if(po.getState().getOrdinal() != 2)
            {
                JOptionPane.showMessageDialog(this,
                    "Esta operaion no esta disponible para el PO indicado.",
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            
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
            
            Words words = new Words();
            JInternalFrame inter = new JInternalFrame("Texto CSV : " + po.getFullFolio(),false,true);
            inter.setContentPane(words);
            inter.setSize(words.getPreferredSize());
            SIIL.servApp.getInstance().getDesktopPane().add(inter);
            int x = SIIL.servApp.getInstance().getDesktopPane().getSize().width/2 - words.getSize().width/2;
            int y = 10;
            inter.setLocation(x, y);            
            boolean ret = false;
            try 
            {
                ret = generateCSV(dbserver,words.getTextAre(),po);
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
                inter.setVisible(true);
            }
            dbserver.close();
        }
    }//GEN-LAST:event_mnRepCSVActionPerformed

    private void mnPurchasedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnPurchasedActionPerformed
        PO po = getSelectItem();
        if(po != null)
        {
            openDatabase(true);
            
            try 
            {
                po.getState().download(dbserver);
            } 
            catch (SQLException ex) 
            {
                JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error Interno",
                    JOptionPane.ERROR_MESSAGE
                );
            }
            
            if(po.getState().getOrdinal() == 2)
            {
                boolean ret = false;
                boolean retQ = false;
                try 
                {
                    int option = JOptionPane.showConfirmDialog
                    (
                        this,
                        "Está confirmado la compra,¿Deseá continuar?",
                        "Confirme",
                        JOptionPane.YES_NO_OPTION
                    );
                    
                    if(option == JOptionPane.YES_OPTION)
                    {
                        State newState = po.getState().next(dbserver);
                        newState.download(dbserver);
                        ret = po.upState(dbserver, newState);
                        core.Calendario calendario = new core.Calendario();
                        core.Dialog dialog = new core.Dialog(calendario);
                        dialog.setContent(calendario);
                        Date eta = calendario.getDate();
                        if(eta != null)
                        {                            
                            if(po.upETA(dbserver, eta))
                            {
                                java.util.List<Renglon> renglones = Renglon.select(dbserver, po, null);
                                for(core.Renglon renglon: renglones)
                                {
                                    if(!renglon.upPurchaseETA(dbserver, eta))
                                    {
                                        JOptionPane.showMessageDialog
                                        (
                                            this,
                                            "Falló la asignacion de E.T.A.",
                                            "Error Interno",
                                            JOptionPane.ERROR_MESSAGE
                                        );
                                        closeDatabase();
                                        return;
                                    }
                                }
                            }                        
                        }
                        else
                        {
                            closeDatabase();
                            return;
                        }
                    }
                    else
                    {
                        return;
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
                
                if(ret)
                {
                    try 
                    {
                        dbserver.commit();
                        JOptionPane.showMessageDialog(
                            this,
                            "Operación realizada satisfactoriamente",
                            "Confirmacion",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        search(dbserver,null,(int)txCount.getValue(),true);
                        return;                        
                    }
                    catch (SQLException ex) 
                    {
                        //Logger.getLogger(List.class.getName()).log(Level.SEVERE, null, ex);
                        
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
                        Logger.getLogger(List.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            else
            {
                State state = new State(-1);
                try 
                {
                    state.select(dbserver, State.Steps.PO_PURCHASE);
                    state.download(dbserver);
                }
                catch (SQLException ex) 
                {
                    Logger.getLogger(List.class.getName()).log(Level.SEVERE, null, ex);
                }
                JOptionPane.showMessageDialog(
                    this,
                    "Esta operación solo se realiza en el estado '" + state.getName() + "'",
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
        else
        {
            JOptionPane.showMessageDialog(this,
                "Seleccione un registro",
                "Error Externo",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_mnPurchasedActionPerformed

    private void mnArriveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnArriveActionPerformed
        PO po = getSelectItem();
        if(po == null)
        {
            JOptionPane.showMessageDialog(this,
                "Seleccione un registro",
                "Error Externo",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        if(po.getState().getOrdinal() != 3)
        {
            JOptionPane.showMessageDialog(this,
                "La operacion no esta dispoble para este PO.",
                "Error Externo",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        JInternalFrame inter = new JInternalFrame("Factura de Compra",false,true);
        FacturaRead screen = new FacturaRead(po);
        inter.setContentPane(screen);
        inter.setSize(screen.getPreferredSize());
        inter.setMaximizable(true);
        SIIL.servApp.getInstance().getDesktopPane().add(inter);
        int x = SIIL.servApp.getInstance().getDesktopPane().getSize().width/2 - screen.getSize().width/2;
        int y = 10;
        inter.setLocation(x, y);
        inter.setVisible(true);
    }//GEN-LAST:event_mnArriveActionPerformed

    private boolean renglos(PO po,boolean edit) 
    {
        if(po != null)
        {
            purchases.order.Renglones reng = new purchases.order.Renglones(SIIL.servApp.getInstance().getDesktopPane(),po);
            openDatabase(true);
            
            try
            {
                po.downFolio(dbserver);
                po.downSerie(dbserver);
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(List.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error Interno",
                        JOptionPane.ERROR_MESSAGE
                );
                return true;
            }
            
            JInternalFrame inter = new JInternalFrame("Renglones de PO " + po.getFullFolio(),false,true);
            inter.setContentPane(reng);
            inter.setSize(reng.getPreferredSize());
            SIIL.servApp.getInstance().getDesktopPane().add(inter);
            int x = SIIL.servApp.getInstance().getDesktopPane().getSize().width/2 - reng.getSize().width/2;
            int y = 10;
            inter.setLocation(x, y);
            inter.setVisible(true);
            return true;
        }
        return false;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jdesktop.swingx.JXButton btUpdate;
    private javax.swing.JPopupMenu command;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JMenuItem mnAdd;
    private javax.swing.JMenuItem mnArrive;
    private javax.swing.JMenuItem mnCreate;
    private javax.swing.JMenuItem mnPurchased;
    private javax.swing.JMenuItem mnPurching;
    private javax.swing.JMenu mnRep;
    private javax.swing.JMenuItem mnRepCSV;
    private javax.swing.JMenuItem mnRepPrint;
    private org.jdesktop.swingx.JXTreeTable tbList;
    private javax.swing.JSpinner txCount;
    private org.jdesktop.swingx.JXSearchField txSearch;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the dialog
     */
    public JDialog getDialog() 
    {
        return dialog;
    }

    /**
     * @param dialog the dialog to set
     */
    public void setDialog(JDialog dialog) 
    {
        this.dialog = dialog;
    }

    private boolean purchase(Database dbserver,PO po) throws SQLException 
    {
        State nextpo = po.getState().next(dbserver);                       
        return po.upState(dbserver,nextpo);
    }
}
