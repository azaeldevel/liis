
package SIIL.COMPAQ;

import SAT.CatalogoTest;
import SIIL.Server.Database;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
 * @author areyes
 */
public class ClientTest {
    
    public ClientTest() {
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
     * Test of getNumber method, of class Client.
     */
    @Test
    public void testGetNumber() {
        System.out.println("getNumber");
    }

    /**
     * Test of getName method, of class Client.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
    }

    /**
     * Test of getCustomerID_Comercial method, of class Client.
     */
    @Test
    public void testGetCustomerID_Comercial() {
        System.out.println("getCustomerID_Comercial");
    }

    /**
     * Test of getBusinessEntityID_Comercial method, of class Client.
     */
    @Test
    public void testGetBusinessEntityID_Comercial() {
        System.out.println("getBusinessEntityID_Comercial");
    }

    /**
     * Test of search method, of class Client.
     */
    @Test
    public void testSearch() throws Exception {
        System.out.println("search");
        
        /*SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();        
        Database dbserver = null;
        try 
        {
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
            dbserver = new Database(serverConfig.getComercial());
        }
        catch (ClassNotFoundException | SQLException | IOException | ParserConfigurationException | SAXException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        
        ArrayList<Client> list = Client.search(dbserver, "a", 3);
        for(Client client : list)
        {
            System.out.println(client.getName());
        }
        
        dbserver.close();*/
    }
    
}
