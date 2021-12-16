
package database.mysql.stock;

import SAT.CatalogoTest;
import SIIL.Server.Database;
import database.mysql.purchases.Provider;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
public class ItemTest {
    
    private static final boolean FL_COMMIT = true;
    private static final boolean FL_IGNORE = true;
    
    public ItemTest() {
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
     * Test of getNumber method, of class Item.
     */
    @Test
    public void testGetNumber() {
        System.out.println("getNumber...");
    }

    /**
     * Test of getType method, of class Item.
     */
    @Test
    public void testGetType() {
        System.out.println("getType...");
    }

    /**
     * Test of getMake method, of class Item.
     */
    @Test
    public void testGetMake() {
        System.out.println("getMake...");
    }

    /**
     * Test of getModel method, of class Item.
     */
    @Test
    public void testGetModel() {
        System.out.println("getModel...");
    }
    
    /**
     * Inserccion de numbero y tipo almenos.
     */
    @Test
    public void testInsert() 
    {
        System.out.println("catalogInsert...");
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
        int High = 99;
        int Result = r.nextInt(High-Low) + Low; 
        Connection connection = dbserver.getConnection();
        
        
        Item item = new Item(-1);   
        Return<Integer> result;
        try 
        {
            result = item.insert(connection,"TEST-" + sdf.format(date) + "-" + Result,"Articulo de Prueba " + sdf.format(date),Item.Type.ARTICULO,false,"");
        }  
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        
        int insertedID = 0;
        if(result.isFail())
        {
            fail(result.getMessage());
        }
        insertedID = result.getParam();
        if(insertedID > 0 && FL_COMMIT)
        {            
            try 
            {
                dbserver.commit();
                //System.out.println("Se genero el registro " + insertedID);
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(stock.ItemTest.class.getName()).log(Level.SEVERE, null, ex);
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
                fail("FallÃ³ RollBack " + ex.getMessage());
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
                fail("FallÃ³ RollBack " + ex.getMessage());
                return;
            }
            fail("Cant. Reg. incorrectos : " + insertedID);
        }
        dbserver.close();
    }

    /**
     * Test of getSerie method, of class Item.
     */
    @Test
    public void testGetSerie() {
        System.out.println("getSerie...");
    }

    /**
     * Test of getProvider method, of class Item.
     */
    @Test
    public void testGetProvider() {
        System.out.println("getProvider...");
    }

    /**
     * Test of upMake method, of class Item.
     */
    @Test
    public void testUpMake() 
    {
        System.out.println("upMake");
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
        
        
        Item instance = new Item(-1);
        try {
            instance.selectLast(connection);
        }  
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        String make = "Make - " + sdf.format(date);
        Return result;
        try {
            result = instance.upMake(connection, make);
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
                //System.out.println("Se afectaron registros " + affected);
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(stock.ItemTest.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(stock.ItemTest.class.getName()).log(Level.SEVERE, null, ex);
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
                fail("FallÃ³ RollBack " + ex.getMessage());
                return;
            }
            fail("Cant. Reg. incorrectos : " + affected);
        }
        dbserver.close();
    }

    /**
     * Test of upModel method, of class Item.
     */
    @Test
    public void testUpModel()
    {
        System.out.println("upModel");
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyymmddhhmmss");
        Random r = new Random();
        int Low = 1;
        int High = 99;
        int Result = r.nextInt(High-Low) + Low;
        
        
        String model = "Model - " + sdf.format(date);
        Item instance = new Item(-1);
        try 
        {
            instance.selectRandom(dbserver.getConnection());
        }  
        catch (SQLException ex) 
        {
            //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        Return result;
        try {
            result = instance.upModel(dbserver.getConnection(), model);
        }  
        catch (SQLException ex) 
        {
            //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
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
                //System.out.println("Se afectaron registros " + affected);
            } 
            catch (SQLException ex) 
            {
                fail("FallÃ³ Commit " + ex.getMessage());
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
                fail("FallÃ³ RollBack " + ex.getMessage());
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
                fail("FallÃ³ RollBack " + ex.getMessage());
                return;
            }
            fail("Cant. Reg. incorrectos : " + affected);
        }
        dbserver.close();
    }

    /**
     * Test of upProvider method, of class Item.
     */
    @Test
    public void testUpProvider() 
    {
        System.out.println("upProvider...");
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyymmddhhmmss");
        Random r = new Random();
        int Low = 1;
        int High = 99;
        int Result = r.nextInt(High-Low) + Low;
        
        
        database.mysql.purchases.Provider provider = new database.mysql.purchases.Provider(1);
        try 
        {
            provider.selectRandom(dbserver.getConnection());
        }  
        catch (SQLException ex) 
        {
            //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        Item instance = new Item(-1);
        try {
            instance.selectRandom(dbserver.getConnection());
        }   
        catch (SQLException ex) 
        {
            //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        Return result;
        try {
            result = instance.upProvider(dbserver.getConnection(), provider);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
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
                //System.out.println("Se afectaron registros " + affected);
            } 
            catch (SQLException ex) 
            {
                fail("FallÃ³ Commit " + ex.getMessage());
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
                fail("FallÃ³ RollBack " + ex.getMessage());
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
                fail("FallÃ³ RollBack " + ex.getMessage());
                return;
            }
            fail("Cant. Reg. incorrectos : " + affected);
        }
        dbserver.close();
    }

    /**
     * Test of getID method, of class Item.
     */
    @Test
    public void testGetID() {
        System.out.println("getID");
    }

    /**
     * Test of selectLast method, of class Item.
     */
    @Test
    public void testSelectLast() throws Exception {
        System.out.println("selectLast");
    }

    /**
     * Test of synchCN method, of class Item.
     */
    @Test
    public void testSynchCN()
    {
        System.out.println("synchCN");
        if(FL_IGNORE) return;
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
        
        Return ret = null;
        try 
        {
            ret = Item.synchCN(dbserver,null);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        
        if(ret.isFlag() && FL_COMMIT)
        {
            try 
            {
                dbserver.commit();
                //System.out.println("Se afectaron registros " + affected);
            }
            catch (SQLException ex) 
            {
                fail("Falló Commit " + ex.getMessage());
            }
        }
        else if(ret.isFlag() && FL_COMMIT == false)
        {            
            try 
            {
                dbserver.rollback();
            } 
            catch (SQLException ex) 
            {
                fail("Falló³ RollBack " + ex.getMessage());
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
            //fail("Cant. Reg. incorrectos : " + affected);
        }
        dbserver.close();
    }

    /**
     * Test of upUnidad method, of class Item.
     */
    @Test
    public void testUpUnidad()
    {
        System.out.println("upUnidad");
    }

    /**
     * Test of getUnidad method, of class Item.
     */
    @Test
    public void testGetUnidad() 
    {
        System.out.println("getUnidad");
    }

    /**
     * Test of downUnidad method, of class Item.
     */
    @Test
    public void testDownUnidad() throws Exception {
        System.out.println("downUnidad");
    }

    /**
     * Test of isService method, of class Item.
     */
    @Test
    public void testIsService() throws Exception {
        System.out.println("isService");
    }

    /**
     * Test of isRefection method, of class Item.
     */
    @Test
    public void testIsRefection() throws Exception {
        System.out.println("isRefection");
    }

    /**
     * Test of isItem method, of class Item.
     */
    @Test
    public void testIsItem() throws Exception {
        System.out.println("isItem");
    }

    /**
     * Test of toString method, of class Item.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
    }

    /**
     * Test of search method, of class Item.
     */
    @Test
    public void testSearch() throws Exception {
        System.out.println("search");
    }

    /**
     * Test of downModel method, of class Item.
     */
    @Test
    public void testDownModel() throws Exception {
        System.out.println("downModel");
    }

    /**
     * Test of selectNumber method, of class Item.
     */
    @Test
    public void testSelectNumber() throws Exception {
        System.out.println("selectNumber");
    }

    /**
     * Test of select method, of class Item.
     */
    @Test
    public void testSelect() throws Exception {
        System.out.println("select");
    }

    /**
     * Test of downDescription method, of class Item.
     */
    @Test
    public void testDownDescription() throws Exception {
        System.out.println("downDescription");
    }

    /**
     * Test of isExist method, of class Item.
     */
    @Test
    public void testIsExist() throws Exception {
        System.out.println("isExist");
    }

    /**
     * Test of downMake method, of class Item.
     */
    @Test
    public void testDownMake() throws Exception {
        System.out.println("downMake");
    }

    /**
     * Test of downNumber method, of class Item.
     */
    @Test
    public void testDownNumber() throws Exception {
        System.out.println("downNumber");
    }

    /**
     * Test of upExternalNumber method, of class Item.
     */
    @Test
    public void testUpExternalNumber() throws Exception {
        System.out.println("upExternalNumber");
    }
    /**
     * Test of getExternalNumber method, of class Item.
     */
    @Test
    public void testGetExternalNumber() {
        System.out.println("getExternalNumber");
    }

    /**
     * Test of selectRandom method, of class Item.
     */
    @Test
    public void testSelectRandom() throws Exception {
        System.out.println("selectRandom");
    }

    /**
     * Test of setID method, of class Item.
     */
    @Test
    public void testSetID() {
    }

    /**
     * Test of getDescription method, of class Item.
     */
    @Test
    public void testGetDescription() {
    }
    
}
