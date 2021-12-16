
package administracion;

import SAT.CatalogoTest;
import SIIL.Server.Database;
import SIIL.core.Office;
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
import stock.ItemTest;

/**
 *
 * @author Azael Reyes
 */
public class BuildingTest 
{
    private static final boolean FL_COMMIT = true;
    
    public BuildingTest() {
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
     * Test of insert method, of class Building.
     */
    @Test
    public void testInsertParent()
    {
        System.out.println("insert");
        /*SIIL.core.config.Server serverConfig = new SIIL.core.config.Server("server.xml");
        Database dbserver = null;
        try 
        {
            dbserver = new Database(serverConfig);
        } 
        catch (ClassNotFoundException | SQLException ex) 
        {
            //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        Connection connection = dbserver.getConnection();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyymmddhhmmss");
        Random r = new Random();
        int Low = 1;
        int High = 99;
        int Result = r.nextInt(High-Low) + Low;
        
        
        String name = "Build - " + sdf.format(date);
        Office office = new Office(dbserver, 1);
        Building parent = new Building(-1);
        try {
            parent.selectRandom(connection);
        }  
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        Building instance = new Building(-1);
        Return result = null;
        try {
            result = instance.insert(connection, name, office, parent);
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
        dbserver.Close();*/
    }
    
    /**
     * Test of insert method, of class Building.
     */
    @Test
    public void testInsertBase() 
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyymmddhhmmss");
        Random r = new Random();
        int Low = 1;
        int High = 99;
        int Result = r.nextInt(High-Low) + Low;
        
        
        String name = "Build - " + sdf.format(date);
        Office office = new Office(dbserver, 1);
        Building parent = null;
        Building instance = new Building(-1);
        Return result = null;
        try {
            result = instance.insert(connection, name, office, null);
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
    }

    /**
     * Test of getID method, of class Building.
     */
    @Test
    public void testGetID() {
        System.out.println("getID");
    }

    /**
     * Test of getOffice method, of class Building.
     */
    @Test
    public void testGetOffice() {
        System.out.println("getOffice");
    }

    /**
     * Test of getName method, of class Building.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
    }

    /**
     * Test of getParent method, of class Building.
     */
    @Test
    public void testGetParent() {
        System.out.println("getParent");
    }
    
}
