
package SIIL.Server;


import com.mysql.cj.jdbc.MysqlDataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;
import process.Return;

/**
 *
 * @author Azael Reyes
 */
public class UserTest 
{
    private static final boolean FL_COMMIT = true;
    
    
    public UserTest() {
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
     * Test of md5 method, of class User.
     */
    @org.junit.Test
    public void testMd5() {
        System.out.println("md5");
    }

    /**
     * Test of valid method, of class User.
     */
    @org.junit.Test
    public void testValid()
    {
        System.out.println("valid");
        /*SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();
        Database dbserver = null;
        try 
        {
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
            dbserver = new Database(serverConfig.getMySQLDS());
        }
        catch (ClassNotFoundException ex) 
        {
            //Logger.getLogger(OperationalTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Fallo la conexion a la BD : " + ex.getMessage());
        }
        catch ( SQLException ex) 
        {
            //Logger.getLogger(OperationalTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Fallo la conexion a la BD : " + ex.getMessage());
        }
        catch (ParserConfigurationException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } 
        catch (SAXException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        
        User user = null;
        try 
        {
            user = User.valid(dbserver, "sistemas", "dev");
        }
        catch (SQLException ex) 
        {
            fail(ex.getMessage());
        }
        
        if(user == null)
        {
            fail("Usuario/Contrasña incorrectos.");
        }
        
        if(user != null && FL_COMMIT)
        {
            try 
            {
                dbserver.commit();
                //System.out.println("Se genero el registro " + insertedID);
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
                return;
            }
        }
        else if(user != null && FL_COMMIT == false)
        {
            try 
            {
                dbserver.rollback();
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
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
        }
        
        dbserver.close();*/
    }

    /**
     * Test of selectRandom method, of class User.
     */
    @org.junit.Test
    public void testSelectRandom() throws Exception {
        System.out.println("selectRandom");
    }

    /**
     * Test of download method, of class User.
     */
    @org.junit.Test
    public void testDownload_Database() throws Exception {
        System.out.println("download");
    }

    /**
     * Test of Load method, of class User.
     */
    @org.junit.Test
    public void testLoad() {
        System.out.println("Load");
    }

    /**
     * Test of listUsers method, of class User.
     */
    @org.junit.Test
    public void testListUsers() {
        System.out.println("listUsers");
    }

    /**
     * Test of getuID method, of class User.
     */
    @org.junit.Test
    public void testGetuID() {
        System.out.println("getuID");
    }

    /**
     * Test of getAlias method, of class User.
     */
    @org.junit.Test
    public void testGetAlias() {
        System.out.println("getAlias");
    }

    /**
     * Test of setAlias method, of class User.
     */
    @org.junit.Test
    public void testSetAlias() {
        System.out.println("setAlias");
    }

    /**
     * Test of toString method, of class User.
     */
    @org.junit.Test
    public void testToString() {
        System.out.println("toString");
    }

    /**
     * Test of down method, of class User.
     */
    @org.junit.Test
    public void testDown() throws Exception {
        System.out.println("down");
    }

    /**
     * Test of fill method, of class User.
     */
    @org.junit.Test
    public void testFill() {
        System.out.println("fill");
    }

    /**
     * Test of download method, of class User.
     */
    @org.junit.Test
    public void testDownload_3args() {
        System.out.println("download");
    }

    /**
     * Test of listAllUsers method, of class User.
     */
    @Test
    public void testListAllUsers()
    {
        System.out.println("listAllUsers");        
        SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();
        Database dbserver = null;
        try 
        {
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
            dbserver = new Database(serverConfig);
        } 
        catch (ClassNotFoundException ex) 
        {
            //Logger.getLogger(OperationalTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Fallo la conexion a la BD : " + ex.getMessage());
        }
        catch ( SQLException ex) 
        {
            //Logger.getLogger(OperationalTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Fallo la conexion a la BD : " + ex.getMessage());
        } 
        catch (ParserConfigurationException ex) 
        {
            Logger.getLogger(UserTest.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (SAXException ex) 
        {
            Logger.getLogger(UserTest.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(UserTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ArrayList<User> lst = User.listAllUsers(dbserver);
        if(lst == null)
        {
            fail("No hay usuario en la lista");
        }
        for(User user : lst)
        {
            try
            {
                user.down(dbserver);
                System.out.println(user);
            }
            catch(SQLException ex)
            {
            
            }
                   
            
        }
        if(lst != null && FL_COMMIT)
        {            
            try 
            {
                dbserver.commit();
                //System.out.println("Se genero el registro " + insertedID);
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
                return;
            }
        }
        else if(lst != null && FL_COMMIT == false)
        {            
            try 
            {
                dbserver.rollback();
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
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
        }
        
        dbserver.close();
    }    

    /**
     * Test of insert method, of class User.
     */
    @Test
    public void testInsert() 
    {
        System.out.println("insert");        
        SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();
        Database dbserver = null;
        String strurl = "";
        try 
        {  
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
            MysqlDataSource ds = serverConfig.getDataSource();
            //ds.setCharacterEncoding("ISO_8859_1");
            strurl = ds.getURL();
            dbserver = new Database(ds);
        } 
        catch (ClassNotFoundException ex) 
        {
            //Logger.getLogger(OperationalTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Fallo la conexion a la BD : " + ex.getMessage());
        }
        catch ( SQLException ex) 
        {
            //Logger.getLogger(OperationalTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Fallo la conexion a la BD : " + strurl + "  --  " + ex.getMessage());
        } 
        catch (ParserConfigurationException ex) 
        {
            Logger.getLogger(UserTest.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (SAXException ex) 
        {
            Logger.getLogger(UserTest.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(UserTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        Random rand = new Random();
        int randNumber = rand.nextInt(10000) + 1;
        
        boolean flP = false;
        boolean flU = false;
        
        try
        {
            Person person = new Person();
            flP = person.insert(dbserver, "ap" + randNumber, "am" + randNumber);
            if(person.download(dbserver).isFail())
            {
                //fail("fallo la descarga de datos.");
                return;
            }
            if(flP)
            {
                User user = new User();
                flU = user.insert(dbserver, person, "alias" + randNumber,"passs" + randNumber, false);
            }
        }
        catch(SQLException ex)
        {
            fail(ex.getMessage());
        }
        
        if(flP && flU && false)
        {            
            try 
            {
                dbserver.commit();
                //System.out.println("Se genero el registro " + insertedID);
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
                return;
            }
        }
        else if(flP && flU && false)
        {            
            try 
            {
                dbserver.rollback();
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
                return;
            }
        }
        else
        {
            fail("Fallo la insercion de los datos.");
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
     * Test of getActive method, of class User.
     */
    @Test
    public void testGetActive() {
        System.out.println("getActive");
    }

    /**
     * Test of getPasswdMD5 method, of class User.
     */
    @Test
    public void testGetPasswdMD5() {
        System.out.println("getPasswdMD5");
    }
    
    @Test 
    public void testDownloadedData()
    {                
        SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();
        Database dbserver = null;
        try 
        {
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
            MysqlDataSource ds = serverConfig.getDataSource();
            dbserver = new Database(ds);
        } 
        catch (ClassNotFoundException ex) 
        {
            //Logger.getLogger(OperationalTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Fallo la conexion a la BD : " + ex.getMessage());
        }
        catch ( SQLException ex) 
        {
            //Logger.getLogger(OperationalTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Fallo la conexion a la BD : " + ex.getMessage());
        } 
        catch (ParserConfigurationException ex) 
        {
            Logger.getLogger(UserTest.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (SAXException ex) 
        {
            Logger.getLogger(UserTest.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(UserTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        User user = new User("servicio2");
        try
        {
            Return ret = user.down(dbserver);
            if(ret.isFlag())
            {
                if(user.getActive())
                {
                    
                }
                else
                {
                    //fail("No esta activo el usuario '" + user.getAlias() + "'");
                }
            }
            else
            {
                fail(ret.getMessage());
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(UserTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        dbserver.close();
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
            MysqlDataSource ds = serverConfig.getDataSource();
            dbserver = new Database(ds);
        } 
        catch (ClassNotFoundException ex) 
        {
            //Logger.getLogger(OperationalTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Fallo la conexion a la BD : " + ex.getMessage());
        }
        catch ( SQLException ex) 
        {
            //Logger.getLogger(OperationalTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Fallo la conexion a la BD : " + ex.getMessage());
        } 
        catch (ParserConfigurationException ex) 
        {
            //Logger.getLogger(UserTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        } 
        catch (SAXException ex) 
        {
            //Logger.getLogger(UserTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        } 
        catch (IOException ex) 
        {
            //Logger.getLogger(UserTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        
        User user = new User();
        boolean flOp = false;
        try 
        {
            if(user.select(dbserver,"sistemas"))
            {
                flOp = user.upPasswdMD5(dbserver, "dev");
                if(flOp)
                {
                    
                }
                else
                {
                    fail("Actualizacion de contraseña.");
                }
            }
            else
            {
                fail("Usuario desconocido.");
            }
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(UserTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        
        
        if(flOp  && FL_COMMIT)
        {            
            try 
            {
                dbserver.commit();
                //System.out.println("Se genero el registro " + insertedID);
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
                return;
            }
        }
        else if(flOp  && FL_COMMIT)
        {            
            try 
            {
                dbserver.rollback();
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
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
        }
        
        dbserver.close();        
    }
}
