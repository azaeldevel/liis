
package stock;

import SAT.CatalogoTest;
import SIIL.Server.Database;
import administracion.Building;
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
public class ContainerTest 
{
    private static final boolean FL_COMMIT = true;
    
    public ContainerTest() {
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
     * Test of getID method, of class Container.
     */
    @Test
    public void testGetID() {
        System.out.println("getID");
    }

    /**
     * Test of getName method, of class Container.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
    }

    /**
     * Test of getItem method, of class Container.
     */
    @Test
    public void testGetItem() {
        System.out.println("getItem");
    }

    /**
     * Test of getParent method, of class Container.
     */
    @Test
    public void testGetParent() {
        System.out.println("getParent");
    }

    @Test
    public void testInsertSimple() 
    {
        System.out.println("insert simple");
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
        
        
        String name = "Container - " + sdf.format(date);
        Building build = new Building(-1);
        try 
        {
            build.selectRandom(connection);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(ContainerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        Container instance = new Container(-1);
        Return result = null;
        try 
        {
            result = instance.insert(connection, name, build, null);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(ContainerTest.class.getName()).log(Level.SEVERE, null, ex);
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
                fail(result.getMessage());
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
     * Test of insert method, of class Container.
     */
    @Test
    public void testInsertParent()
    {
        System.out.println("insert with parent");
        /*SIIL.core.config.Server serverConfig = new SIIL.core.config.Server("server.xml");
        Database dbserver = null;
        try 
        {
            dbserver = new Database(serverConfig);
        } 
        catch (ClassNotFoundException | SQLException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        Connection connection = dbserver.getConnection();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddhhmmss");
        Random r = new Random();
        int Low = 1;
        int High = 99;
        int Result = r.nextInt(High-Low) + Low;
        
        
        String name = "Container - " + sdf.format(date);
        Building build = new Building(-1);
        try {
            build.selectRandom(connection);
        } catch (SQLException ex) {
            //Logger.getLogger(ContainerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        Container parent = new Container(-1);
        Return<Integer> retparent = null;
        try 
        {
            retparent = parent.selectRandom(connection);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(ContainerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        if(retparent.isFail())
        {
            //Logger.getLogger(ContainerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(retparent.getMessage());
            return;
        }
        Container instance = new Container(-1);
        System.out.println("instance.insert ..");
        Return result = null;
        try {
            result = instance.insert(connection, name, build, parent);
        } catch (SQLException ex) {
            //Logger.getLogger(ContainerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        System.out.println("instance.insert .. return;");
        
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
        dbserver.Close();*/
    }

    /**
     * Test of getBuilding method, of class Container.
     */
    @Test
    public void testGetBuilding() {
        System.out.println("getBuilding");
    }

    /**
     * Test of upBuilding method, of class Container.
     */
    @Test
    public void testUpBuilding() 
    {
        System.out.println("upBuilding");
        /*SIIL.core.config.Server serverConfig = new SIIL.core.config.Server("server.xml");
        Database dbserver = null;
        try 
        {
            dbserver = new Database(serverConfig);
        } 
        catch (ClassNotFoundException | SQLException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        Connection connection = dbserver.getConnection();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddhhmmss");
        Random r = new Random();
        int Low = 1;
        int High = 99;
        int Result = r.nextInt(High-Low) + Low;
        
        
        Building building = new Building(-1);
        try {
            building.selectRandom(connection);
        } catch (SQLException ex) {
            Logger.getLogger(ContainerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        Container container = new Container(-1);
        try {
            container.selectRandom(connection);
        } catch (SQLException ex) {
            Logger.getLogger(ContainerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Return result;
        try 
        {
            result = container.upBuilding(connection, building);
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
                fail("Fallo la operacion.");
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
        dbserver.Close();*/
    }

    /**
     * Test of upParent method, of class Container.
     */
    @Test
    public void testUpParent() 
    {
        System.out.println("upParent");
        System.out.println("upBuilding");
        /*
        SIIL.core.config.Server serverConfig = new SIIL.core.config.Server("server.xml");
        Database dbserver = null;
        try 
        {
            dbserver = new Database(serverConfig);
        } 
        catch (ClassNotFoundException | SQLException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
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
            container.selectRandom(connection);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(ContainerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        Container parent = new Container(-1);
        try 
        {
            parent.selectRandom(connection);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(ContainerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        Return result = null;
        try 
        {
            result = container.upParent(connection, parent);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(ContainerTest.class.getName()).log(Level.SEVERE, null, ex);
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
                fail("Fallo la operacion.");
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
        dbserver.Close();
        */
    }    
}
