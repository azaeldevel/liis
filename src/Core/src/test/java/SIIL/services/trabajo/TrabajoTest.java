
package SIIL.services.trabajo;

import SAT.CatalogoTest;
import SIIL.services.Trabajo;
import SIIL.Server.Database;
import SIIL.Server.Person;
import SIIL.client.sales.Enterprise;
import SIIL.trace.Trace;
import database.mysql.sales.Remision;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
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
import session.User;
import stock.ItemTest;

/**
 *
 * @author Azael Reyes
 */
public class TrabajoTest 
{
    private static final boolean FL_COMMIT = true;    
    private static final String BACKWARD_BD = "bc.tj";
    
    public TrabajoTest() {
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
     * Test of insert method, of class Trabajo.
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
        
        Enterprise enterprise = new Enterprise();
        Trabajo.Sheet sheet = Trabajo.Sheet.CAMPO;
        User opertaor = new User();
        Trace contexTrace = null;
        try 
        {
            enterprise.selectRandom(dbserver);
            if(opertaor.selectRandom(dbserver) != true) fail("Fallo operator ID  = " + opertaor.getpID());
            if(opertaor.download(dbserver).isFail()) fail("Descarga de datos del operator " + opertaor.getpID());
        }
        catch(SQLException ex) 
        {
            fail(ex.getErrorCode() + " --> " + ex.getMessage());
            return;
        }
        
        Trabajo trabajo = null;
        try 
        {
            contexTrace = new Trace(BACKWARD_BD, opertaor, "Creacion de Trabajo(Testing)");
            contexTrace.insert(dbserver);
            trabajo = Trabajo.insert(dbserver, sheet, enterprise,opertaor,contexTrace);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(TrabajoTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
                 
        
        if(trabajo != null && FL_COMMIT)
        {            
            try 
            {
                dbserver.commit();
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        else if(trabajo != null && FL_COMMIT == false)
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
                fail("Falló RollBack " + ex.getMessage());
            }
        }
        dbserver.close();   
        
    }

    /**
     * Test of upSA method, of class Trabajo.
     */
    @Test
    public void testUpSA()
    {
        System.out.println("upSA");        
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
        
        Trabajo trabajo = new Trabajo(-1);
        Remision remision = new Remision(-1);
        User opertaor = new User();
        Trace contexTrace = null;
        
        try 
        {
            if(!remision.selectRandom(dbserver)) fail("No se encontro una remision para seleccionar");
            if(!trabajo.selectRandom(dbserver)) fail("No se encontro un trabajo en para seleccionar");            
            if(opertaor.selectRandom(dbserver) != true) fail("Fallo operator ID = " + opertaor.getpID());
            //if(opertaor.download(dbserver).isFail()) fail("Descarga de datos del operator " + opertaor.getpID());
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(TrabajoTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        Boolean ret = false;
        
        try 
        {            
            contexTrace = new Trace(BACKWARD_BD, opertaor, "Asignacion de SA(Testing)");
            contexTrace.insert(dbserver);
            ret = trabajo.upSA(dbserver, remision,contexTrace);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(TrabajoTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        
        if(ret && FL_COMMIT)
        {            
            try 
            {
                dbserver.commit();
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
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
                //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
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
                fail("Falló RollBack " + ex.getMessage());
            }
        }
        dbserver.close();  
        
    }

    /**
     * Test of upMechanic method, of class Trabajo.
     */
    @Test
    public void testUpMechanic()
    {
        System.out.println("upMechanic");       
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
        
        Trabajo trabajo = new Trabajo(-1);
        Person mechanic = new Person(-1);    
        User opertaor = new User();
        Trace contexTrace = null;    
        
        try 
        {
            if(mechanic.selectRandom(dbserver.getConnection()).isFail()) fail("No se encontro un mecanico para seleccionar para seleccionar");
            if(!trabajo.selectRandom(dbserver)) fail("No se encontro un trabajo en para seleccionar");
            if(opertaor.selectRandom(dbserver ) != true) fail("Fallo operator ID = " + opertaor.getpID());
            //if(opertaor.download(dbserver).isFail()) fail("Descarga de datos del operator " + opertaor.getpID());
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(TrabajoTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        Boolean ret = false;
        
        try 
        {
            contexTrace = new Trace(BACKWARD_BD, opertaor, "Creacion de Trabajo(Testing)");
            contexTrace.insert(dbserver);
            ret = trabajo.upMechanic(dbserver, mechanic,contexTrace);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(TrabajoTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        
        
        

        if(ret && FL_COMMIT)
        {            
            try 
            {
                dbserver.commit();
            }
            catch (SQLException ex) 
            {
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
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
                fail("Falló RollBack " + ex.getMessage());
            }
        }
        dbserver.close();  
    }

    /**
     * Test of selectRandom method, of class Trabajo.
     */
    @Test
    public void testSelectRandom(){
        System.out.println("selectRandom");
    }

    /**
     * Test of upfhToDo method, of class Trabajo.
     */
    @Test
    public void testUpfhToDo()
    {
        System.out.println("upfhToDo");       
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
        
        Trabajo trabajo = new Trabajo(-1);  
        User opertaor = new User();
        Trace contexTrace = null;
        try 
        {
            if(!trabajo.selectRandom(dbserver)) fail("No se encontro un trabajo en para seleccionar");
            if(opertaor.selectRandom(dbserver) != true) fail("Falló operator ID = " + opertaor.getpID());
            if(opertaor.download(dbserver).isFail()) fail("Falló descarga de datos del operator " + opertaor.getpID());
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(TrabajoTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        Boolean ret = false;
        
        try 
        {
            contexTrace = new Trace(BACKWARD_BD, opertaor, "Creacion de Trabajo(Testing)");
            contexTrace.insert(dbserver);
            ret = trabajo.upfhToDo(dbserver, new Date(),contexTrace);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(TrabajoTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        
        if(ret && FL_COMMIT)
        {            
            try 
            {
                dbserver.commit();
            }
            catch (SQLException ex) 
            {
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
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
                fail("Falló RollBack " + ex.getMessage());
            }
        }
        dbserver.close();  
    }

    /**
     * Test of upBrief method, of class Trabajo.
     */
    @Test
    public void testUpBrief()
    {
        System.out.println("upBrief");      
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
        
        Trabajo trabajo = new Trabajo(-1);    
        User opertaor = new User();
        Trace contexTrace = null;
        
        try 
        {
            if(!trabajo.selectRandom(dbserver)) fail("No se encontro un trabajo en para seleccionar");
            if(opertaor.selectRandom(dbserver) != true) fail("Fallo operator ID = " + opertaor.getpID());
            //if(opertaor.download(dbserver).isFail()) fail("Descarga de datos del operator " + opertaor.getpID());
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(TrabajoTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        Boolean ret = false;        
        try 
        {
            contexTrace = new Trace(BACKWARD_BD, opertaor, "Creacion de Trabajo(Testing)");
            contexTrace.insert(dbserver);
            ret = trabajo.upBrief(dbserver, "Pruebas ...",contexTrace);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(TrabajoTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }

        if(ret && FL_COMMIT)
        {            
            try 
            {
                dbserver.commit();
            }
            catch (SQLException ex) 
            {
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
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
                fail("Falló RollBack " + ex.getMessage());
            }
        }
        dbserver.close();  
    }

    /**
     * Test of getPriority method, of class Trabajo.
     */
    @Test
    public void testGetPriority() {
        System.out.println("getPriority");
    }

    /**
     * Test of downPriority method, of class Trabajo.
     */
    @Test
    public void testDownPriority()
    {
        System.out.println("downPriority");
    }

    /**
     * Test of getState method, of class Trabajo.
     */
    @Test
    public void testGetState() {
        System.out.println("getState");
    }

    /**
     * Test of getSheet method, of class Trabajo.
     */
    @Test
    public void testGetSheet() {
        System.out.println("getSheet");
    }

    /**
     * Test of getSA method, of class Trabajo.
     */
    @Test
    public void testGetSA() {
        System.out.println("getSA");
    }

    /**
     * Test of getCompany method, of class Trabajo.
     */
    @Test
    public void testGetCompany() {
        System.out.println("getCompany");
    }

    /**
     * Test of getMechanic method, of class Trabajo.
     */
    @Test
    public void testGetMechanic() {
        System.out.println("getMechanic");
    }

    /**
     * Test of getBrief method, of class Trabajo.
     */
    @Test
    public void testGetBrief() {
        System.out.println("getBrief");
    }

    /**
     * Test of getFhToDo method, of class Trabajo.
     */
    @Test
    public void testGetFhToDo() 
    {
        System.out.println("getFhToDo");
    }

    /**
     * Test of download method, of class Trabajo.
     */
    @Test
    public void testDownload() 
    {
        System.out.println("download");
    }

    /**
     * Test of downMechanic method, of class Trabajo.
     */
    @Test
    public void testDownMechanic() 
    {
        System.out.println("downMechanic");
    }

    /**
     * Test of downBrief method, of class Trabajo.
     */
    @Test
    public void testDownBrief() 
    {
        System.out.println("downBrief");
    }

    /**
     * Test of downDate method, of class Trabajo.
     */
    @Test
    public void testDownDate() throws Exception {
        System.out.println("downDate");
    }

    /**
     * Test of have method, of class Trabajo.
     */
    @Test
    public void testHave_Database_Date() throws Exception {
        System.out.println("have");
    }

    /**
     * Test of have method, of class Trabajo.
     */
    @Test
    public void testHave_3args() throws Exception {
        System.out.println("have");
    }

    /**
     * Test of delete method, of class Trabajo.
     */
    @Test
    public void testDelete() 
    {
        System.out.println("delete");
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
        
        boolean flRet = false;
        try 
        {
            Trabajo trabajo = new Trabajo(-1);            
            trabajo.selectRandom(dbserver);
            flRet = trabajo.delete(dbserver, null);
        }
        catch (SQLException ex) 
        {
            fail(ex.getMessage());
        }
        
        if(flRet && FL_COMMIT)
        {            
            try 
            {
                dbserver.commit();
                //System.out.println("Se genero el registro " + insertedID);
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        else if(flRet && FL_COMMIT == false)
        {            
            try 
            {
                dbserver.rollback();
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
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
            }
        }
        dbserver.close();
    }

    /**
     * Test of upSheet method, of class Trabajo.
     */
    @Test
    public void testUpSheet() 
    {
        System.out.println("upSheet");
    }

    /**
     * Test of checkClient method, of class Trabajo.
     */
    @Test
    public void testCheckClient() 
    {
        System.out.println("checkClient");
    }

    /**
     * Test of downState method, of class Trabajo.
     */
    @Test
    public void testDownState() 
    {
        System.out.println("downState");        
    }

    /**
     * Test of create method, of class Trabajo.
     */
    @Test
    public void testCreate() 
    {
        System.out.println("create");
    }

    /**
     * Test of downClient method, of class Trabajo.
     */
    @Test
    public void testDownClient() 
    {
        System.out.println("downClient");        
    }

    /**
     * Test of upClient method, of class Trabajo.
     */
    @Test
    public void testUpClient() 
    {
        System.out.println("upClient");
    }

    /**
     * Test of getId method, of class Trabajo.
     */
    @Test
    public void testGetId() 
    {
        System.out.println("getId");
    }

    /**
     * Test of downQuotedService method, of class Trabajo.
     */
    @Test
    public void testDownQuotedService() 
    {
        System.out.println("downQuotedService");
    }

    /**
     * Test of downSA method, of class Trabajo.
     */
    @Test
    public void testDownSA() 
    {
        System.out.println("downSA");
    }

    /**
     * Test of upQuotedService method, of class Trabajo.
     */
    @Test
    public void testUpQuotedService() 
    {
        System.out.println("upQuotedService");
    }

    /**
     * Test of getQuotedService method, of class Trabajo.
     */
    @Test
    public void testGetQuotedService() 
    {
        System.out.println("getQuotedService");
    }

    /**
     * Test of setQuotedService method, of class Trabajo.
     */
    @Test
    public void testSetQuotedService() 
    {
        System.out.println("setQuotedService");
    }

    /**
     * Test of upState method, of class Trabajo.
     */
    @Test
    public void testUpState() 
    {
        System.out.println("upState");
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
        
        boolean flRet = false;
        try 
        {
            Trabajo trabajo = new Trabajo(-1);
            trabajo.selectRandom(dbserver);            
            flRet = trabajo.upBrief(dbserver, "Test de comentario.", null);
        }
        catch (SQLException ex) 
        {
            fail(ex.getMessage());
        }
        
        if(flRet && FL_COMMIT)
        {            
            try 
            {
                dbserver.commit();
                //System.out.println("Se genero el registro " + insertedID);
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        else if(flRet && FL_COMMIT == false)
        {            
            try 
            {
                dbserver.rollback();
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
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
            }
        }
        dbserver.close();
    }
       
}
