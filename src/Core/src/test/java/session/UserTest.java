
package session;

import SAT.CatalogoTest;
import SIIL.Server.Database;
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
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 *
 * @author Azael Reyes
 */
public class UserTest 
{
    private static final boolean FL_COMMIT = false;
    
    public UserTest() 
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
     * Test of insert method, of class User.
     */
    @Test
    public void testInsert() throws SQLException 
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
        
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddhhmmss");
        Random r = new Random();
        int Low = 1;
        int High = 9999;
        int Result = r.nextInt(High-Low) + Low;   
        
        User user = new User();
        boolean result;
        try 
        {
            result = user.insert(dbserver,"PERSON-" + Result,"APELLIDO-" + Result,"A" + Result);
        }  
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getErrorCode() + " --> " + ex.getMessage());
            return;
        }
        
        if(result && FL_COMMIT)
        {            
            try 
            {
                dbserver.commit();
                //System.out.println("Se genero el registro " + insertedID);
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        else if(result && FL_COMMIT == false)
        {            
            try 
            {
                dbserver.rollback();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
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
            }//fail("Cant. Reg. incorrectos : " + insertedID);
        }
        dbserver.close();
    }

    /**
     * Test of setPassword method, of class User.
     */
    @Test
    public void testSetPassword() 
    {
        System.out.println("setPassword");
    }

    /**
     * Test of getPassword method, of class User.
     */
    @Test
    public void testGetPassword() 
    {
        System.out.println("getPassword");
    }

    /**
     * Test of generate method, of class User.
     */
    @Test
    public void testGenerate() 
    {
        System.out.println("generate");
    }

    /**
     * Test of fill method, of class User.
     */
    @Test
    public void testFill() 
    {
        System.out.println("fill");
    }

    /**
     * Test of download method, of class User.
     */
    @Test
    public void testDownload() 
    {
        System.out.println("download");
    }

    /**
     * Test of downloadAlias method, of class User.
     */
    @Test
    public void testDownloadAlias() 
    {
        System.out.println("downloadAlias");
    }

    /**
     * Test of upPasswdMD5 method, of class User.
     */
    @Test
    public void testUpPasswdMD5() 
    {
        System.out.println("upPasswdMD5");
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
        
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddhhmmss");
        Random r = new Random();
        int Low = 1;
        int High = 9999;
        int Result = r.nextInt(High-Low) + Low;   
        
        User user = new User();
        boolean result;
        try 
        {
            user.selectRandom(dbserver.getConnection());
            result = user.upPasswdMD5(dbserver, "dev");
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getErrorCode() + " --> " + ex.getMessage());
            return;
        }
        
        if(result && FL_COMMIT)
        {
            try 
            {
                dbserver.commit();
                //System.out.println("Se genero el registro ");
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        else if(result && FL_COMMIT == false)
        {            
            try 
            {
                dbserver.rollback();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
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
            }//fail("Cant. Reg. incorrectos : " + insertedID);
        }
        dbserver.close();
    }

    /**
     * Test of upAlias method, of class User.
     */
    @Test
    public void testUpAlias() 
    {
        System.out.println("upAlias");
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
        
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddhhmmss");
        Random r = new Random();
        int Low = 1;
        int High = 9999;
        int Result = r.nextInt(High-Low) + Low;   
        
        User user = new User();
        boolean result;
        try 
        {
            user.selectRandom(dbserver.getConnection());
            result = false;
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getErrorCode() + " --> " + ex.getMessage());
            return;
        }
        
        if(result && FL_COMMIT)
        {
            try 
            {
                dbserver.commit();
                //System.out.println("Se genero el registro ");
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        else if(result && FL_COMMIT == false)
        {            
            try 
            {
                dbserver.rollback();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
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
            }//fail("Cant. Reg. incorrectos : " + insertedID);
        }    
        dbserver.close();    
    }    
}
