
package SIIL.client.sales;

import SAT.CatalogoTest;
import SIIL.Server.Database;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 *
 * @author Azael Reyes
 */
public class EnterpriseTest 
{    
    
    private static boolean FL_COMMIT = true;
    
    public EnterpriseTest() 
    {
    }
    
    @BeforeClass
    public static void setUpClass() 
    {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() 
    {
    }
    
    @After
    public void tearDown() 
    {
    }

    /**
     * Test of updateFromCNAll method, of class Enterprise.
     */
    @Test
    public void testUpdateFromCNAll()
    {
        System.out.println("updateFromCNAll");
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
        
        boolean ret = true;
        /*try 
        {
            //ret = Enterprise.updateFromCNAll(dbserver);
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(EnterpriseTest.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(EnterpriseTest.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        if(ret && FL_COMMIT)
        {            
            try 
            {
                dbserver.commit();
                //System.out.println("Se genero el registro " + insertedID);
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        else if(ret && FL_COMMIT == false)
        {            
            try 
            {
                dbserver.rollback();
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
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
                //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail("Falló RollBack " + ex.getMessage());
                return;
            }
            //fail("Cant. Reg. incorrectos : ");
        }
        dbserver.close();
    }

    /**
     * Test of downMailFact method, of class Enterprise.
     */
    @Test
    public void testDownMailFact() 
    {
        System.out.println("downMailFact");
    }

    /**
     * Test of download method, of class Enterprise.
     */
    @Test
    public void testDownload() 
    {
        System.out.println("download");
    }

    /**
     * Test of getEmailFactura method, of class Enterprise.
     */
    @Test
    public void testGetEmailFactura() 
    {
        System.out.println("getEmailFactura");
    }

    /**
     * Test of upRFC method, of class Enterprise.
     */
    @Test
    public void testUpRFC() 
    {
        System.out.println("upRFC");
    }

    /**
     * Test of getRFC method, of class Enterprise.
     */
    @Test
    public void testGetRFC() 
    {
        System.out.println("getRFC");
    }

    /**
     * Test of updateCN_RFC method, of class Enterprise.
     */
    @Test
    public void testUpdateCN_RFC() 
    {
        System.out.println("updateCN_RFC");        
    }

    /**
     * Test of getRequirePO method, of class Enterprise.
     */
    @Test
    public void testGetRequirePO() 
    {
        System.out.println("getRequirePO");
    }

    /**
     * Test of upRequirePO method, of class Enterprise.
     */
    @Test
    public void testUpRequirePO() 
    {
        System.out.println("upRequirePO");        
    }

    /**
     * Test of downRequirePO method, of class Enterprise.
     */
    @Test
    public void testDownRequirePO() 
    {
        System.out.println("downRequirePO");
    }

    /**
     * Test of findByRFC method, of class Enterprise.
     */
    @Test
    public void testFindByRFC() 
    {
        System.out.println("findByRFC");
    }

    /**
     * Test of find method, of class Enterprise.
     */
    @Test
    public void testFind() 
    {
        System.out.println("find");
    }

    /**
     * Test of listing method, of class Enterprise.
     */
    @Test
    public void testListing() 
    {
        System.out.println("listing");        
    }

    /**
     * Test of valid method, of class Enterprise.
     */
    @Test
    public void testValid() 
    {
        System.out.println("valid");        
    }

    /**
     * Test of fill method, of class Enterprise.
     */
    @Test
    public void testFill() 
    {
        System.out.println("fill");        
    }    
}
