
package SIIL.Server;

import SAT.CatalogoTest;
import SIIL.core.Office;
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
public class PersonTest 
{
    private static final boolean FL_COMMIT = false;
    
    public PersonTest() {
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
     * Test of getSeudonimo method, of class Person.
     */
    @Test
    public void testGetSeudonimo() {
        System.out.println("getSeudonimo");
    }

    /**
     * Test of insert method, of class Person.
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
        
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddhhmmss");
        Random r = new Random();
        int Low = 1;
        int High = 9999;
        int Result = r.nextInt(High-Low) + Low;   
        
        Person person = new Person(-1);
        boolean result;
        try 
        {
            result = person.insert(dbserver,"PERSON-" + Result,"APELLIDO " + Result);
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
    }

    /**
     * Test of search method, of class Person.
     */
    @Test
    public void testSearch_6args_1() 
    {
        System.out.println("search");
    }

    /**
     * Test of search method, of class Person.
     */
    @Test
    public void testSearch_6args_2() 
    {
        System.out.println("search");
    }

    /**
     * Test of download method, of class Person.
     */
    @Test
    public void testDownload() 
    {
        System.out.println("download");
    }

    /**
     * Test of listing method, of class Person.
     */
    @Test
    public void testListing() 
    {
        System.out.println("listing");
        
    }

    /**
     * Test of selectRandom method, of class Person.
     */
    @Test
    public void testSelectRandom() 
    {
        System.out.println("selectRandom");        
    }

    /**
     * Test of toString method, of class Person.
     */
    @Test
    public void testToString_boolean() 
    {
        System.out.println("toString");
    }

    /**
     * Test of toString method, of class Person.
     */
    @Test
    public void testToString_0args() 
    {
        System.out.println("toString");
    }

    /**
     * Test of getAM method, of class Person.
     */
    @Test
    public void testGetAM() 
    {
        System.out.println("getAM");
    }

    /**
     * Test of setAM method, of class Person.
     */
    @Test
    public void testSetAM() {
        System.out.println("setAM");
    }

    /**
     * Test of getpID method, of class Person.
     */
    @Test
    public void testGetpID() 
    {
        System.out.println("getpID");
    }

    /**
     * Test of setpID method, of class Person.
     */
    @Test
    public void testSetpID() 
    {
        System.out.println("setpID");
    }

    /**
     * Test of getN1 method, of class Person.
     */
    @Test
    public void testGetN1() 
    {
        System.out.println("getN1");
    }

    /**
     * Test of setN1 method, of class Person.
     */
    @Test
    public void testSetN1() 
    {
        System.out.println("setN1");
    }

    /**
     * Test of getNs method, of class Person.
     */
    @Test
    public void testGetNs() 
    {
        System.out.println("getNs");
    }

    /**
     * Test of setNs method, of class Person.
     */
    @Test
    public void testSetNs() 
    {
        System.out.println("setNs");
    }

    /**
     * Test of getAP method, of class Person.
     */
    @Test
    public void testGetAP() 
    {
        System.out.println("getAP");
    }

    /**
     * Test of setAP method, of class Person.
     */
    @Test
    public void testSetAP() 
    {
        System.out.println("setAP");
    }

    /**
     * Test of getBD method, of class Person.
     */
    @Test
    public void testGetBD() 
    {
        System.out.println("getBD");
    }

    /**
     * Test of fill method, of class Person.
     */
    @Test
    public void testFill_3args() 
    {
        System.out.println("fill");
    }

    /**
     * Test of setBD method, of class Person.
     */
    @Test
    public void testSetBD() 
    {
        System.out.println("setBD");
    }

    /**
     * Test of fill method, of class Person.
     */
    @Test
    public void testFill_4args() 
    {
        System.out.println("fill");
    }

    /**
     * Test of getDepartment method, of class Person.
     */
    @Test
    public void testGetDepartment() 
    {
        System.out.println("getDepartment");
    }

    /**
     * Test of setDepartment method, of class Person.
     */
    @Test
    public void testSetDepartment() 
    {
        System.out.println("setDepartment");
    }

    /**
     * Test of getOffice method, of class Person.
     */
    @Test
    public void testGetOffice() 
    {
        System.out.println("getOffice");
    }

    /**
     * Test of downloadEmail method, of class Person.
     */
    @Test
    public void testDownloadEmail() 
    {
        System.out.println("downloadEmail");
    }

    /**
     * Test of getEmail method, of class Person.
     */
    @Test
    public void testGetEmail() 
    {
        System.out.println("getEmail");
    }

    /**
     * Test of upN1 method, of class Person.
     */
    @Test
    public void testUpN1() 
    {
        System.out.println("upN1");
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
        
        Person person = new Person(-1);
        boolean result;
        try 
        {
            person.selectRandom(dbserver.getConnection());
            result = person.upN1(dbserver, "P-" + Result);
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
    }

    /**
     * Test of upNS method, of class Person.
     */
    @Test
    public void testUpNS() 
    {
        System.out.println("upNS");
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
        
        Person person = new Person(-1);
        boolean result;
        try 
        {
            person.selectRandom(dbserver.getConnection());
            result = person.upNS(dbserver, "PS-" + Result);
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
    }

    /**
     * Test of upAP method, of class Person.
     */
    @Test
    public void testUpAP() 
    {
        System.out.println("upAP");
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
        
        Person person = new Person(-1);
        boolean result;
        try 
        {
            person.selectRandom(dbserver.getConnection());
            result = person.upAP(dbserver, "AP-" + Result);
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
    }

    /**
     * Test of upAM method, of class Person.
     */
    @Test
    public void testUpAM() 
    {
        System.out.println("upAM");
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
        
        Person person = new Person(-1);
        boolean result;
        try 
        {
            person.selectRandom(dbserver.getConnection());
            result = person.upAM(dbserver, "AM-" + Result);
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
    }

    /**
     * Test of upEmail method, of class Person.
     */
    @Test
    public void testUpEmail() 
    {
        System.out.println("upEmail");
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
        
        Person person = new Person(-1);
        boolean result;
        try 
        {
            person.selectRandom(dbserver.getConnection());
            result = person.upEmail(dbserver, "cinco.probador@gmail.com");
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
    }

    /**
     * Test of upOffice method, of class Person.
     */
    @Test
    public void testUpOffice() 
    {
        System.out.println("upOffice");
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
        
        Person person = new Person(-1);
        Office office = new Office(-1);
        boolean result;
        try 
        {
            person.selectRandom(dbserver.getConnection());
            office.selectRandom(dbserver.getConnection());
            office.download(dbserver.getConnection());
            result = person.upOffice(dbserver, office);
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
    }

    /**
     * Test of upSeudonimo method, of class Person.
     */
    @Test
    public void testUpSeudonimo() 
    {
        System.out.println("upSeudonimo");
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
        
        Person person = new Person(-1);
        boolean result;
        try 
        {
            person.selectRandom(dbserver.getConnection());
            result = person.upSeudonimo(dbserver, "SEU-" + Result);
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
    }

    /**
     * Test of upIsOrserOwner method, of class Person.
     */
    @Test
    public void testUpIsOrserOwner() 
    {
        System.out.println("upIsOrserOwner");
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
        
        Person person = new Person(-1);
        boolean result;
        try 
        {
            person.selectRandom(dbserver.getConnection());
            boolean acvtive = false;
            if(Result > 500)
            {
                acvtive = true;
            }
            else
            {
                acvtive = false;
            }
            result = person.upIsOrserOwner(dbserver, acvtive);
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
    }

    /**
     * Test of upIsOrserTec method, of class Person.
     */
    @Test
    public void testUpIsOrserTec() 
    {
        System.out.println("upIsOrserTec");
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
        
        Person person = new Person(-1);
        boolean result;
        try 
        {
            person.selectRandom(dbserver.getConnection());
            boolean acvtive = false;
            if(Result > 500)
            {
                acvtive = true;
            }
            else
            {
                acvtive = false;
            }
            result = person.upIsOrserTec(dbserver, acvtive);
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
    }

    /**
     * Test of upDepartment method, of class Person.
     */
    @Test
    public void testUpDepartment() 
    {
        System.out.println("upDepartment");
        
    }
    
}
