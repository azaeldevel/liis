
package stock;

import SAT.CatalogoTest;
import SIIL.Server.Database;
import java.io.IOException;
import java.sql.SQLException;
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
public class ClaveFiscalTest 
{
    private static final boolean FL_COMMIT = true;
    
    public ClaveFiscalTest() {
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
     * Test of upDescripcion method, of class ClaveFiscal.
     */
    @Test
    public void testUpDescripcion() 
    {
        System.out.println("upDescripcion");
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
        
        ClaveFiscal claveFiscal = new ClaveFiscal();
        boolean ret = false;
        try 
        {
            claveFiscal.selectLast(dbserver);
            ret = claveFiscal.upDescripcion(dbserver, "descripcion de 987654");
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(ClaveFiscalTest.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
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
        }
        
        dbserver.close();
    }

    /**
     * Test of upClave method, of class ClaveFiscal.
     */
    @Test
    public void testUpClave() 
    {
        System.out.println("upClave");
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
        
        ClaveFiscal claveFiscal = new ClaveFiscal();
        boolean ret = false;
        try 
        {
            claveFiscal.selectLast(dbserver);
            ret = claveFiscal.upClave(dbserver, "987654");
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(ClaveFiscalTest.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
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
        }
        
        dbserver.close();
    }

    /**
     * Test of insert method, of class ClaveFiscal.
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
        
        ClaveFiscal claveFiscal = new ClaveFiscal();
        boolean ret = false;
        try 
        {
            ret = claveFiscal.insert(dbserver, "123456", "descripcion de 123456");
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(ClaveFiscalTest.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
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
        }
        
        dbserver.close();
    }

    /**
     * Test of getID method, of class ClaveFiscal.
     */
    @Test
    public void testGetID() 
    {
        System.out.println("getID");
    }

    /**
     * Test of getClave method, of class ClaveFiscal.
     */
    @Test
    public void testGetClave() 
    {
        System.out.println("getClave");        
    }

    /**
     * Test of getDescripcion method, of class ClaveFiscal.
     */
    @Test
    public void testGetDescripcion() 
    {
        System.out.println("getDescripcion");
    }
    
}
