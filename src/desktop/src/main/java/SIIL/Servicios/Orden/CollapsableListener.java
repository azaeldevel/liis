
package SIIL.Servicios.Orden;

import SIIL.Server.Database;
import SIIL.artifact.AmbiguosException;
import SIIL.artifact.DeployException;
import SIIL.client.Configuration;
import session.Credential;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.JXTreeTable;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Azael
 */
public class CollapsableListener implements TreeExpansionListener 
{
    JXTreeTable treeTb;
    Credential credential;
    CollapsableListener(JXTreeTable tbList,Credential cred) 
    {
        treeTb = tbList;
        credential = cred;
    }
    
    @Override
    public void treeExpanded(TreeExpansionEvent event) 
    {
        TreePath path = event.getPath();
        OrdenNode node = (OrdenNode) path.getLastPathComponent();
        String data = node.getData().getState().getName();
        //System.out.println("Expanded: " + data + " : treeExpanded ");
        Configuration conf = new Configuration();
        conf.setBD(credential.getBD());
        conf.setOffice(credential.getSuc());
        conf.setUser(credential.getUser());
        conf.setObject("SIIL.Servicios.Orden.Screen.Table");
        conf.setAttribute(node.getData().getState().getCode());
        conf.setValue("Expanded");
        
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
        
        int fl = 0;
        try 
        {
            fl = conf.update(db);
        } 
        catch (SQLException ex) 
        {
            System.err.print("No se registro el evento Expand en la base de datos.");
            Logger.getLogger(CollapsableListener.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        if(fl == 1)
        {
            try 
            {
                db.commit();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(CollapsableListener.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        else
        {
            try 
            {
                db.rollback();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(CollapsableListener.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }            
        }
        db.close();
    }

    @Override
    public void treeCollapsed(TreeExpansionEvent event) 
    {
        TreePath path = event.getPath();
        OrdenNode node = (OrdenNode) path.getLastPathComponent();
        String data = node.getData().getState().getName();
        //System.out.println("Collapsed: " + data + " : treeCollapsed ");
        Configuration conf = new Configuration();
        conf.setBD(credential.getBD());
        conf.setOffice(credential.getSuc());
        conf.setUser(credential.getUser());
        conf.setObject("SIIL.Servicios.Orden.Screen.Table");
        conf.setAttribute(node.getData().getState().getCode());
        conf.setValue("Collapsed");
        
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
        
        
        int fl = 0;
        try 
        {
            fl = conf.update(db);
        } 
        catch (SQLException ex) 
        {
            System.err.print("No se registro el evento Expand en la base de datos.");
            Logger.getLogger(CollapsableListener.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        if(fl == 1)
        {
            try 
            {
                db.commit();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(CollapsableListener.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        else
        {
            try 
            {
                db.rollback();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(CollapsableListener.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }            
        }
        db.close();
    }    
}
