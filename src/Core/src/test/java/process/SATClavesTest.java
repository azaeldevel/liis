
package process;

import SAT.CatalogoTest;
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
public class SATClavesTest 
{
    private static final boolean FL_COMMIT = true;
    
    
    public SATClavesTest() 
    {
    }
    
    @BeforeClass
    public static void setUpClass() 
    {
    }
    
    @AfterClass
    public static void tearDownClass() 
    {
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
     * Test of insert method, of class SATClaves.
     */
    @Test
    public void testInsert() 
    {
        System.out.println("insert");
        /*SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();        
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
        int High = 1000;
        int Result = r.nextInt(High-Low) + Low;  
        
        String numero = "Test-" + Result;
        String nombre = "Test-" + Result;
        String descrip = "Description " + Result;
        SATClaves claves = new SATClaves();
        SATCatalogo catalogo = new SATCatalogo();   
        boolean ret = false;
        try 
        {
            catalogo.selectLast(dbserver);
            ret = claves.insert(dbserver,catalogo, numero, null, descrip);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(SATClavesTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
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
            fail("Cant. Reg. incorrectos :");
        }
        dbserver.close();*/
    }

    /**
     * Test of importFormaPago method, of class SATClaves.
     */
    @Test
    public void testImportFormaPago() 
    {
        System.out.println("importFormaPago");
    }

    /**
     * Test of getCatalog method, of class SATClaves.
     */
    @Test
    public void testGetCatalog() 
    {
        System.out.println("getCatalog");
    }

    /**
     * Test of getClave method, of class SATClaves.
     */
    @Test
    public void testGetClave() 
    {
        System.out.println("getClave");
        
    }

    /**
     * Test of getNombre method, of class SATClaves.
     */
    @Test
    public void testGetNombre() 
    {
        System.out.println("getNombre");
        
    }

    /**
     * Test of getDescription method, of class SATClaves.
     */
    @Test
    public void testGetDescription() 
    {
        System.out.println("getDescription");
        
    }
    
}
