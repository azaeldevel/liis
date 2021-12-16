
package process;

import SAT.CatalogoTest;
import SIIL.Server.Database;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import jxl.read.biff.BiffException;
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
public class SATCatalogoTest 
{
    private static final boolean FL_COMMIT = true;
    
    
    public SATCatalogoTest() {
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
     * Test of getNombre method, of class SATCatalogo.
     */
    @Test
    public void testGetNombre() 
    {
        System.out.println("getNombre");
    }

    /**
     * Test of getClave method, of class SATCatalogo.
     */
    @Test
    public void testGetClave() 
    {
        System.out.println("getClave");
    }

    /**
     * Test of getID method, of class SATCatalogo.
     */
    @Test
    public void testGetID() 
    {
        System.out.println("getID");
    }

    /**
     * Test of insert method, of class SATCatalogo.
     */
    @Test
    public void testInsert() 
    {
        System.out.println("insert");
    }

    /**
     * Test of importXLS method, of class SATCatalogo.
     */
    @Test
    public void testImportXLS() 
    {
        System.out.println("importXLS");
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
        
        boolean ret = false;
        try 
        {
            ret = SATCatalogo.importXLS(dbserver, new File("C:\\Users\\Azael Reyes\\Desktop\\CGDF v33\\catCFDI.xls"), SATCatalogo.Imports.FormaPago);
        }
        catch (IOException | BiffException | SQLException ex) 
        {
            fail(ex.getMessage());
        }
        
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
                fail("No Operation");
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
        dbserver.Close(); */
    }
    
}
