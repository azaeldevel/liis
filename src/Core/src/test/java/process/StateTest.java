
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
public class StateTest {
    
    public StateTest() {
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
     * Test of getID method, of class State.
     */
    @Test
    public void testGetID() {
        System.out.println("getID");
    }

    /**
     * Test of getCode method, of class State.
     */
    @Test
    public void testGetCode() {
        System.out.println("getCode");
    }

    /**
     * Test of getName method, of class State.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
    }

    /**
     * Test of getOrdinal method, of class State.
     */
    @Test
    public void testGetOrdinal() {
        System.out.println("getOrdinal");
    }

    /**
     * Test of download method, of class State.
     */
    @Test
    public void testDownload() 
    {
        System.out.println("download");
    }

    /**
     * Test of selectRandom method, of class State.
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
        
        
        State instance = new State(-1);
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
    }

    /**
     * Test of select method, of class State.
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
        process.State state = new process.State(-1);
        process.Module module = new process.Module(-1);
        try 
        {
            module.select(connection,database.mysql.purchases.order.PO.MODULE);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(ModuleTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Fallo SELECT BD : " + ex.getMessage());
            return;
        }
        try 
        {
            Return ret = state.select(dbserver.getConnection(),module,1);
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(ModuleTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Fallo SELECT BD : " + ex.getMessage());
            return;
        }
        
        if(state.getID() < 1)
        {
            fail("No se asigno algun ID.");
        }
    }

    /**
     * Test of getModule method, of class State.
     */
    @Test
    public void testGetModule() {
        System.out.println("getModule");
    }

    /**
     * Test of download method, of class State.
     */
    @Test
    public void testDownload_Database() throws Exception {
        System.out.println("download");
    }

    /**
     * Test of next method, of class State.
     */
    @Test
    public void testNext() throws Exception {
        System.out.println("next");
    }

    /**
     * Test of select method, of class State.
     */
    @Test
    public void testSelect_Database_StateSteps() throws Exception {
        System.out.println("select");
    }

    /**
     * Test of getStep method, of class State.
     */
    @Test
    public void testGetStep() {
        System.out.println("getStep");
    }

    /**
     * Test of fill method, of class State.
     */
    @Test
    public void testFill() {
        System.out.println("fill");
    }

    /**
     * Test of selectAll method, of class State.
     */
    @Test
    public void testSelectAll() throws Exception {
        System.out.println("selectAll");
    }

    /**
     * Test of select method, of class State.
     */
    @Test
    public void testSelect_3args() throws Exception {
        System.out.println("select");
    }

    /**
     * Test of download method, of class State.
     */
    @Test
    public void testDownload_Connection() throws Exception {
        System.out.println("download");
    }
    
    @Test
    public void testinIDStatic()
    {       
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
        
        //Para el modulo de Cotizacion de Servicio
        State state = new State(-1);
        try 
        {
            state.select(dbserver, State.Steps.CS_CREATED);
            if(state.getID() != 3)
            {
                fail("El State.ID tiene un enlace estatico entre la BD y el Codigo fuente.");
            }
            State nextState = state.next(dbserver);
            if(nextState.getID() != 4)
            {
                fail("El State.ID tiene un enlace estatico entre la BD y el Codigo fuente.");
            }
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(StateTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
    }
}
