
package SIIL;

import SIIL.Server.Database;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author azael
 */
public class Tests {
    
    public static void main(String args[]) 
    {
        if(args[0].equals("tests"))
        {
            System.out.println("---------------------Tests---------------------");
            SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();        
            Database dbserver = null;
            try 
            {
                serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
                dbserver = new Database(serverConfig);
            } 
            catch (ClassNotFoundException | SQLException | IOException | ParserConfigurationException | SAXException ex) 
            {
                System.out.println(ex.getMessage());
                return;
            }
            if(args[1].compareTo("--login") == 0)
            {
                session.User user;
                try 
                {
                    user = session.User.checkSession(dbserver, "sistemas", "e77989ed21758e78331b20e477fc5582");
                    if(user == null)
                    {
                        System.out.println("Fallo La prueba de login.");
                        
                    }
                    else
                    {
                        System.out.println("Login acceptado.");
                    }
                } 
                catch (SQLException ex) 
                {
                    //Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Fallo La prueba de login.");
                }
            }
            dbserver.close();
        }
    }
}
