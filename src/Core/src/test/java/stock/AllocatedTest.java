
package stock;

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
import process.Return;

/**
 *
 * @author Azael Reyes
 */
public class AllocatedTest 
{
    private static final boolean FL_COMMIT = true;
    
    public AllocatedTest() 
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
     * Test of getId method, of class Allocated.
     */
    @Test
    public void testGetId() 
    {
        System.out.println("getId");
    }

    /**
     * Test of getContainer method, of class Allocated.
     */
    @Test
    public void testGetContainer() 
    {
        System.out.println("getContainer");
    }

    /**
     * Test of getItem method, of class Allocated.
     */
    @Test
    public void testGetItem() 
    {
        System.out.println("getItem");
    }

    /**
     * Test of insert method, of class Allocated.
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
        
        Connection connection = dbserver.getConnection();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddhhmmss");
        Random r = new Random();
        int Low = 1;
        int High = 99;
        int Result = r.nextInt(High-Low) + Low;
        
        
        Container container = new Container(-1);
        try {
            container.selectRandom(dbserver.getConnection());
        }  
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }

        Flow flow = new Flow(-1);
        try 
        {
            flow.selectLast(dbserver.getConnection());
        }  
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }

        try 
        {
            flow.downItem(dbserver.getConnection());
        }          
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }

        Allocated instance = new Allocated(-1);
        Return result;
        try 
        {
            result = instance.insert(connection, container, flow);
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
     * Test of upContainer method, of class Allocated.
     */
    @Test
    public void testUpContainer() 
    {
        System.out.println("upContainer");
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
        
        
        Container container = new Container(-1);
        try 
        {
            container.selectRandom(dbserver.getConnection());
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }

        Allocated instance = new Allocated(-1);
        try 
        {
            instance.selectRandom(dbserver.getConnection());
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }

        Return result = null;
        try 
        {
            result = instance.upContainer(connection, container);
        }  
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        
        
        int affected = 0;
        switch (result.getStatus()) 
        {
            case DONE:
                affected = (int)result.getParam();
                break;
            case FAIL:
                fail(result.getMessage());
                break;
            default:
                fail("Resultado desconocido.");
                break;
        }
        if(affected == 1 && FL_COMMIT)
        {            
            try 
            {
                dbserver.commit();
                //System.out.println("Se genero el registro " + affected);
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
        dbserver.close();
    }
    
}
