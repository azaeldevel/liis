
package database.mysql.purchases;

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
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;
import process.Return;
import stock.ItemTest;

/**
 *
 * @author Azael Reyes
 */
public class ProviderTest 
{
    private static boolean FL_COMMIT = true;
    
    public ProviderTest() 
    {
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
     * Test of importFromCN method, of class Provider.
     */
    @Test
    public void testImportFromCN() 
    {
        System.out.println("importFromCN");
    }

    /**
     * Test of search method, of class Provider.
     */
    @Test
    public void testSearch_Connection_String() 
    {
        System.out.println("search");
    }

    /**
     * Test of search method, of class Provider.
     */
    @Test
    public void testSearch_5args() 
    {
        System.out.println("search");
    }

    /**
     * Test of downRFC method, of class Provider.
     */
    @Test
    public void testDownRFC() 
    {
        System.out.println("downRFC");
    }

    /**
     * Test of downRazonSocial method, of class Provider.
     */
    @Test
    public void testDownRazonSocial() 
    {
        System.out.println("downRazonSocial");
    }

    /**
     * Test of downNameShort method, of class Provider.
     */
    @Test
    public void testDownNameShort() 
    {
        System.out.println("downNameShort");
    }

    /**
     * Test of insert method, of class Provider.
     */
    @Test
    public void testInsert()
    {
        System.out.println("Inserting Provider...");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yymmddhhmm");
        Random r = new Random();        
        int Low = 1;
        int High = 99;
        int Result = r.nextInt(High-Low) + Low;
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
        String nameShort = "TS - " + sdf.format(date);
        String nameRazonSocial = "Test Provider " + sdf.format(date);
        Provider instance = new Provider(-1);
        int expResult = 0;
        Return ret;
        try 
        {
            ret = instance.insert(connection, nameShort, nameRazonSocial);
        }  
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail( ex.getErrorCode() + " --> " + ex.getMessage());
            return;
        }
        
        int insertedID = 0;
        if(ret.isFlag() == false)
        {
            fail(ret.getMessage());
        }
        insertedID = (int) ret.getParam();
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
        dbserver.close();
    }

    /**
     * Test of getID method, of class Provider.
     */
    @Test
    public void testGetID() {
        System.out.println("getID");
    }

    /**
     * Test of getNameShort method, of class Provider.
     */
    @Test
    public void testGetNameShort() {
        System.out.println("getNameShort");
    }

    /**
     * Test of getNameRazonSocial method, of class Provider.
     */
    @Test
    public void testGetNameRazonSocial() {
        System.out.println("getNameRazonSocial");
    }

    /**
     * Test of getRFC method, of class Provider.
     */
    @Test
    public void testGetRFC() {
        System.out.println("getRFC");
    }

    /**
     * Test of selectLast method, of class Provider.
     */
    @Test
    public void testSelectLast() throws Exception {
        System.out.println("selectLast");
    }

    /**
     * Test of selectRandom method, of class Provider.
     */
    @Test
    public void testSelectRandom() throws Exception {
        System.out.println("selectRandom");
    }

    /**
     * Test of upRFC method, of class Provider.
     */
    @Test
    public void testUpRFC()
    {
        System.out.println("upRFC");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yymmddhhmm");
        Random r = new Random();        
        int Low = 1;
        int High = 99;
        int Result = r.nextInt(High-Low) + Low;
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
        
        Provider provider = new Provider(-1);
        boolean retS ;
        try 
        {
            retS = provider.selectLast(connection);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(ProviderTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        
        
        Return<Integer> ret = null;
        String rfc = "TEST0160602S07";
        try 
        {
            ret = provider.upRFC(connection, rfc);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(ProviderTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ret.getMessage());
        }
        
        int affected = 0;
        if(ret.isFail())
        {
            fail(ret.getMessage());
        }
        affected = ret.getParam();
        if(affected == 1 && FL_COMMIT)
        {
            try 
            {
                dbserver.commit();
                //System.out.println("Se afectaron registros " + affected);
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        else if(affected == 1 && FL_COMMIT == false)
        {            
            try 
            {
                dbserver.rollback();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
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
            fail("Cant. Reg. incorrectos : " + affected);
        }
    }

    
}
