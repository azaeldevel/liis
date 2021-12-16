
package SAT;

import SIIL.Server.Database;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Random;
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
public class CatalogoTest 
{
    private static final boolean FL_COMMIT = true;
    
    public CatalogoTest() {
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
     * Test of listing method, of class Catalogo.
     */
    @Test
    public void testListing() 
    {
        System.out.println("listing");
    }

    /**
     * Test of download method, of class Catalogo.
     */
    @Test
    public void testDownload()
    {
        System.out.println("download");
    }

    /**
     * Test of insert method, of class Catalogo.
     */
    @Test
    public void testInsert()
    {
        System.out.println("insert");
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
        
        java.util.Date date = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yymmddkkmmss");
        Random r = new Random();
        int Low = 1;
        int High = 10000;
        int Result = r.nextInt(High-Low) + Low;
        
        Catalogo catalogo = new Catalogo(-1);
        boolean ret = false;
        try 
        {
            ret = catalogo.insert(dbserver, String.valueOf(Result) , "TEST-" + String.valueOf(Result));
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(CatalogoTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(ret && FL_COMMIT)
        {
            try 
            {
                dbserver.commit();
                //System.out.println("Se afectaron registros " + insertedID);
            }
            catch (SQLException ex) 
            {
                fail("Falló Commit " + ex.getMessage());
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
                fail("Falló RollBack " + ex.getMessage());
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
        }
        dbserver.close();        
    }

    /**
     * Test of getId method, of class Catalogo.
     */
    @Test
    public void testGetId() 
    {
        System.out.println("getId");
    }

    /**
     * Test of getClave method, of class Catalogo.
     */
    @Test
    public void testGetClave() 
    {
        System.out.println("getClave");
    }

    /**
     * Test of getNombre method, of class Catalogo.
     */
    @Test
    public void testGetNombre() 
    {
        System.out.println("getNombre");
    }
    
}
