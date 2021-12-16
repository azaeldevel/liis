
package stock;

import SAT.CatalogoTest;
import database.mysql.stock.Item;
import SIIL.Server.Database;
import database.mysql.sales.Quotation;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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

/**
 *
 * @author Azael Reyes
 */
public class FlowTest 
{
    private static final boolean FL_COMMIT = true;
    
    public FlowTest() 
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
    public void tearDown() {
    }

    /**
     * Test of getItem method, of class Flow.
     */
    @Test
    public void testGetItem() {
        System.out.println("getItem");
    }

    /**
     * Test of getFhIngreso method, of class Flow.
     */
    @Test
    public void testGetFhIngreso() {
        System.out.println("getFhIngreso");
    }

    /**
     * Test of setFhIngreso method, of class Flow.
     */
    @Test
    public void testSetFhIngreso() {
        System.out.println("setFhIngreso");
    }

    /**
     * Test of isActiveSerie method, of class Flow.
     */
    @Test
    public void testIsActiveSerie() {
        System.out.println("isActiveSerie");
    }

    /**
     * Test of getSerie method, of class Flow.
     */
    @Test
    public void testGetSerie() {
        System.out.println("getSerie...");
    }

    /**
     * Test of insert method, of class Flow.
     */
    @Test
    public void testInsert() 
    {
        System.out.println("insert...");
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
        java.util.Date date = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yymmddkkmmss");
        Random r = new Random();
        int Low = 1;
        int High = 99;
        int Result = r.nextInt(High-Low) + Low;   
        
        Item item = new Item(-1);
        try 
        {
            item.selectLast(connection);
        }  
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }

        Flow flow = new Flow(-1);
        Return result;
        try 
        {
            result = flow.insert(connection,date,true, "?", item);
        }  
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }

        int insertedID = 0;
        switch (result.getStatus()) 
        {
            case DONE:
                insertedID = (int)result.getParam();
                break;
            case FAIL:
                fail("Fallo la operacion.");
                break;
            default:
                fail("Resultado desconocido.");
                break;
        }
        if(insertedID > 0 && FL_COMMIT)
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
        else if(insertedID > 1 && FL_COMMIT == false)
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
            fail("Cant. Reg. incorrectos : " + insertedID);
        }
        dbserver.close();
    }

    /**
     * Test of getID method, of class Flow.
     */
    @Test
    public void testGetID() {
        System.out.println("getID...");
    }    

    /**
     * Test of upQuotation method, of class Flow.
     */
    @Test
    public void testUpQuotation() 
    {
        System.out.println("upQuotation");
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
        java.util.Date date = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yymmddkkmmss");
        Random r = new Random();
        int Low = 1;
        int High = 99;
        int Result = r.nextInt(High-Low) + Low;   
        
        Quotation quotation = new Quotation(-1);
        try 
        {
            quotation.select(connection,232);
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(FlowTest.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        Flow flow1 = new Flow(-1);
        Flow flow2 = new Flow(-1);
        Flow flow3 = new Flow(-1);
        boolean result;
        int updated = 0;
        try 
        {
            result = flow1.select(connection,822);
            updated += flow1.upQuotation(connection, quotation);
            result = flow2.select(connection,823);
            updated += flow2.upQuotation(connection, quotation);
            result = flow3.select(connection,824);
            updated += flow3.upQuotation(connection, quotation);
        }  
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }      

        if(updated == 3 && FL_COMMIT)
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
        else if(updated == 3 && FL_COMMIT == false)
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
     * Test of downPO method, of class Flow.
     */
    @Test
    public void testDownPO() throws Exception {
        System.out.println("downPO");
    }

    /**
     * Test of downQuotation method, of class Flow.
     */
    @Test
    public void testDownQuotation() throws Exception {
        System.out.println("downQuotation");
    }

    /**
     * Test of delete method, of class Flow.
     */
    @Test
    public void testDelete() throws Exception {
        System.out.println("delete");
    }

    /**
     * Test of selectRandom method, of class Flow.
     */
    @Test
    public void testSelectRandom() throws Exception {
        System.out.println("selectRandom");
    }

    /**
     * Test of upSerie method, of class Flow.
     */
    @Test
    public void testUpSerie() throws Exception {
        System.out.println("upSerie");
    }

    /**
     * Test of selectLast method, of class Flow.
     */
    @Test
    public void testSelectLast() throws Exception {
        System.out.println("selectLast");
    }

    /**
     * Test of downItem method, of class Flow.
     */
    @Test
    public void testDownItem() throws Exception {
        System.out.println("downItem");
    }

    /**
     * Test of getQuotation method, of class Flow.
     */
    @Test
    public void testGetQuotation() {
        System.out.println("getQuotation");
    }

    /**
     * Test of getPO method, of class Flow.
     */
    @Test
    public void testGetPO() {
        System.out.println("getPO");
    }

    /**
     * Test of upPO method, of class Flow.
     */
    @Test
    public void testUpPO() 
    {
        System.out.println("upPO");
    }

    /**
     * Test of select method, of class Flow.
     */
    @Test
    public void testSelect() 
    {
        System.out.println("select");
    }
}
