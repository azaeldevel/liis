
package process;

import SAT.CatalogoTest;
import SIIL.Server.Database;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;

/**
 *
 * @author Azael Reyes
 */
public class ModuleTest {
    
    public ModuleTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getID method, of class Module.
     */
    @Test
    public void testGetID() 
    {
        System.out.println("getID");
    }

    /**
     * Test of getCode method, of class Module.
     */
    @Test
    public void testGetCode() {
        System.out.println("getCode");
    }

    /**
     * Test of getName method, of class Module.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
    }

    /**
     * Test of selectRandom method, of class Module.
     */
    @Test
    public void testSelectRandom() 
    {
        System.out.println("selectRandom");
        SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();        
        Database dbserver = null;
        try 
        {
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
            dbserver = new Database(serverConfig);
        } 
        catch (ClassNotFoundException | SQLException | IOException | ParserConfigurationException | SAXException ex) 
        {
            Logger.getLogger(CatalogoTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        
        Connection connection = dbserver.getConnection();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddhhmmss");
        Random r = new Random();
        int Low = 1;
        int High = 99;
        int Result = r.nextInt(High-Low) + Low;
        
        
        Module instance = new Module(-1);
        try 
        {
            instance.selectRandom(connection);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(ModuleTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Fallo SELECT BD : " + ex.getMessage());
        }
        
        if(instance.getID() < 1)
        {
            fail("No se asigno algun ID.");
        }
        dbserver.close();
    }

    /**
     * Test of select method, of class Module.
     */
    @Test
    public void testSelect()
    {
        System.out.println("select");       
                SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();        
        Database dbserver = null;
        try 
        {
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
            dbserver = new Database(serverConfig);
        } 
        catch (ClassNotFoundException | SQLException | IOException | ParserConfigurationException | SAXException ex) 
        {
            Logger.getLogger(CatalogoTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        
        Connection connection = dbserver.getConnection();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddhhmmss");
        Random r = new Random();
        int Low = 1;
        int High = 99;
        int Result = r.nextInt(High-Low) + Low;
        process.Module module = new process.Module(-1);
        try 
        {
            module.select(connection,database.mysql.purchases.order.PO.MODULE);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(ModuleTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Fallo SELECT BD : " + ex.getMessage());
        }
        
        if(module.getID() < 1)
        {
            fail("No se asigno algun ID.");
        }
        dbserver.close();
        
    }
    
}
