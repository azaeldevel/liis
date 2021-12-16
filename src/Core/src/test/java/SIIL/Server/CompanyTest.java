
package SIIL.Server;

import SAT.CatalogoTest;
import java.io.IOException;
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
import process.Return;
import static process.Return.Status.DONE;
import static process.Return.Status.FAIL;
import stock.ItemTest;

/**
 *
 * @author Azael Reyes
 */
public class CompanyTest 
{
    private static final boolean FL_COMMIT = true;
    
    public CompanyTest() {
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
     * Test of insert method, of class Company.
     */
    @Test
    public void testInsert_3args()
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
            //fail(ex.getMessage());
        }
        
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yymmddhhmmss");
        Random r = new Random();
        int Low = 1;
        int High = 99;
        int Result = r.nextInt(High-Low) + Low;   
        
        Company company = new Company();
        Return<Integer>  ret = null;
        try 
        {
            ret = company.insert(dbserver.getConnection(),sdf.format(date), sdf.format(date));
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getErrorCode() + " --> " + ex.getMessage());
            return;
        }
        
 
        int insertedID = 0;
        if(ret.isFail())
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ret.getMessage());
            return;
        }
        insertedID = ret.getParam();
        if(insertedID > 0 && FL_COMMIT)
        {            
            try 
            {
                dbserver.commit();
                //System.out.println("Se genero el registro " + insertedID);
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        else if(insertedID > 0 && FL_COMMIT == false)
        {            
            try 
            {
                dbserver.rollback();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
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
            fail("Cant. Reg. incorrectos : " + insertedID);
        }
    }

    /**
     * Test of selectLast method, of class Company.
     */
    @Test
    public void testSelectLast() throws Exception {
        System.out.println("selectLast");
    }

    /**
     * Test of selectRandom method, of class Company.
     */
    @Test
    public void testSelectRandom() throws Exception {
        System.out.println("selectRandom");
    }

    /**
     * Test of fillCB method, of class Company.
     */
    @Test
    public void testFillCB_4args() {
        System.out.println("fillCB");
    }

    /**
     * Test of fillCB method, of class Company.
     */
    @Test
    public void testFillCB_JComboBox_MySQL() {
        System.out.println("fillCB");
    }

    /**
     * Test of insert method, of class Company.
     */
    @Test
    public void testInsert_MySQL() throws Exception {
        System.out.println("insert");
    }

    /**
     * Test of search method, of class Company.
     */
    @Test
    public void testSearch() {
        System.out.println("search");
    }

    /**
     * Test of check method, of class Company.
     */
    @Test
    public void testCheck() {
        System.out.println("check");
    }

    /**
     * Test of complete method, of class Company.
     */
    @Test
    public void testComplete_Database() {
        System.out.println("complete");
    }

    /**
     * Test of complete method, of class Company.
     */
    @Test
    public void testComplete_MySQL() {
        System.out.println("complete");
    }

    /**
     * Test of valid method, of class Company.
     */
    @Test
    public void testValid() {
        System.out.println("valid");
    }

    /**
     * Test of fill method, of class Company.
     */
    @Test
    public void testFill() {
        System.out.println("fill");
    }

    /**
     * Test of getName method, of class Company.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
    }

    /**
     * Test of setName method, of class Company.
     */
    @Test
    public void testSetName() {
        System.out.println("setName");
    }

    /**
     * Test of getID method, of class Company.
     */
    @Test
    public void testGetID() {
        System.out.println("getID");
    }
    
}
