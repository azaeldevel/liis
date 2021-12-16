
package SIIL.Management;

import SIIL.Server.Database;
import database.mysql.purchases.Provider;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import process.Return;

/**
 * @version 0.1.0
 * @author Azael Reyes Martinez
 */
public class ImportCN 
{
    private static final boolean FL_COMMIT = true;
    
    public static void main(String args[])  
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
                        
        }
        Connection connection = dbserver.getConnection();
        
        Return<Integer> ret = null;
        try 
        {
            ret = Provider.importFromCN(connection);
        }
        catch (IOException | SQLException ex) 
        {
            //Logger.getLogger(ImportCN.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
        }    
        
        int insertedCount = 0;
        if(ret.isFail())
        {
            //Logger.getLogger(ImportCN.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ret.getMessage());            
        }
        insertedCount = ret.getParam();
        if(insertedCount > 0 && FL_COMMIT)
        {
            try 
            {
                dbserver.commit();
                //System.out.println("Se genero el registro " + insertedID);
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(ImportCN.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println(ex.getMessage());
            }
        }
        else if(insertedCount > 0 && FL_COMMIT == false)
        {
            try 
            {
                dbserver.rollback();
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(ImportCN.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println(ex.getMessage());
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
                //Logger.getLogger(ImportCN.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println(ex.getMessage());
                return;
            }
            System.err.println(ret.getMessage());
        }
    }
}
