
package process;

import SAT.CatalogoTest;
import SIIL.Server.Database;
import SIIL.Server.Person;
import SIIL.core.Office;
import SIIL.services.Trabajo;
import database.mysql.sales.Quotation;
import database.mysql.sales.Remision;
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
import stock.ItemTest;

/**
 *
 * @author Azael Reyes
 */
public class OperationalTest 
{
    private static boolean FL_COMMIT = true;
    
    public OperationalTest() {
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
     * Test of getID method, of class Operational.
     */
    @Test
    public void testGetID() {
        System.out.println("getID");
    }

    /**
     * Test of getState method, of class Operational.
     */
    @Test
    public void testGetState() {
        System.out.println("getState");
    }

    /**
     * Test of insert method, of class Operational.
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyymmddhhmmss");
        Random r = new Random();
        int Low = 1;
        int High = 9999;
        int Result = r.nextInt(High-Low) + Low; 
        Connection connection = dbserver.getConnection();
        
        State state = new State(-1);
        try 
        {
            state.selectRandom(connection);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(OperationalTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Fallo la selecion de stado : " + ex.getMessage());
        }
        Person operator = new Person();
        try 
        {
            operator.selectRandom(connection);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(OperationalTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Fallo la seleccion de operador : " + ex.getMessage());
        }
        Office office = new Office(-1);
        try {
            office.selectRandom(connection);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(OperationalTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        Operational instance = new Operational(-1);
        Return result = null;
        try 
        {
            result = instance.insert(dbserver,office, state,operator,dbserver.getTimestamp(),0,"DEV","TEST");
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(OperationalTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Fallo la inserccion de la operacion : " + ex.getMessage());
        }
        
        if(result.isFlag() && FL_COMMIT)
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
        else if(result.isFlag() && FL_COMMIT == false)
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
            fail("Cant. Reg. incorrectos : ");
        }
        dbserver.close();
    }

    /**
     * Test of nextFolio method, of class Operational.
     */
    @Test
    public void testNextFolio() 
    {
        System.out.println("nextFolio");
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
        int High = 9999;
        int Result = r.nextInt(High-Low) + Low; 
        Connection connection = dbserver.getConnection();
        
        String type = "TEST";
        String serie = "DEV";
        Operational instance = new Operational(-1);
        try 
        {
            Return result = instance.nextFolio(dbserver, type, serie);
        } 
        catch (SQLException ex) 
        {
            fail(ex.getMessage());
            return;
        }
        if(instance.getFolio() == null)
        {
            fail("Se genero el folio " + instance.getFolio());
        }
        else
        {
            System.out.println("Se genero el folio " + instance.getFolio());
        }
    }

    /**
     * Test of download method, of class Operational.
     */
    @Test
    public void testDownload() throws Exception {
        System.out.println("download");
    }

    /**
     * Test of getSerie method, of class Operational.
     */
    @Test
    public void testGetSerie() {
        System.out.println("getSerie");
    }

    /**
     * Test of getFolio method, of class Operational.
     */
    @Test
    public void testGetFolio() {
        System.out.println("getFolio");
    }

    /**
     * Test of getType method, of class Operational.
     */
    @Test
    public void testGetType() {
        System.out.println("getType");
    }

    /**
     * Test of getOffice method, of class Operational.
     */
    @Test
    public void testGetOffice() {
        System.out.println("getOffice");
    }

    /**
     * Test of getFhFolio method, of class Operational.
     */
    @Test
    public void testGetFhFolio() {
        System.out.println("getFhFolio");
    }

    /**
     * Test of getStrFolio method, of class Operational.
     */
    @Test
    public void testGetStrFolio() {
        System.out.println("getStrFolio");
    }

    /**
     * Test of upFlag method, of class Operational.
     */
    @Test
    public void testUpFlag()
    {
        System.out.println("upFlag");
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
        
        Operational instance = new Operational(-1);
        try 
        {
            instance.selectRandom(connection);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(OperationalTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        Return result = null;
        try 
        {
            result = instance.upFlag(connection, 'A');
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(OperationalTest.class.getName()).log(Level.SEVERE, null, ex);
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
                //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
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
                //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
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

    /**
     * Test of getFlag method, of class Operational.
     */
    @Test
    public void testGetFlag() 
    {
        System.out.println("getFlag");
    }

    /**
     * Test of getCN60Descripcion method, of class Operational.
     */
    @Test
    public void testGetCN60Descripcion() {
        System.out.println("getCN60Descripcion");
        
    }

    /**
     * Test of fromCNRenglones method, of class Operational.
     */
    @Test
    public void testFromCNRenglones() 
    {
        System.out.println("fromCNRenglones");
        /*SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();        
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
        
        Return ret = null, retQ = null;
        try 
        {
            Trabajo trabajo = new Trabajo(8);
            trabajo.downSA(dbserver);
            Remision instance = trabajo.getSA();
            instance.download(dbserver);
            instance.downOffice(dbserver);
            instance.getOffice().download(dbserver.getConnection());
            ret = instance.fromCNRenglones(dbserver, Operational.TablaRenglon.REMISION); 
            
            Quotation quotation = new Quotation(-1);
            quotation.select(dbserver.getConnection(), 13804);
            quotation.downFolio(dbserver);
            quotation.downOffice(dbserver);
            quotation.getOffice().download(dbserver.getConnection());
            retQ = quotation.fromCNRenglones(dbserver, Operational.TablaRenglon.COTIZACION);
            
        }
        catch (SQLException ex) 
        {
            fail(ex.getMessage());
        }        
        
        if(retQ.isFlag() && FL_COMMIT)
        {            
            try 
            {
                dbserver.commit();
                //System.out.println("Se genero el registro " + affected);
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
                return;
            }
        }
        else if(retQ.isFlag() && FL_COMMIT == false)
        {            
            try 
            {
                dbserver.rollback();
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                fail(ex.getMessage());
            }
        }
        else
        {
            try 
            {
                fail("Falló : " + ret.getMessage());
                dbserver.rollback();
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail("Falló RollBack " + ret.getMessage());
                return;
            }
            
        } 
        dbserver.close();*/
        
    }

    /**
     * Test of upFolio method, of class Operational.
     */
    @Test
    public void testUpFolio() 
    {
        System.out.println("upFolio");
        
    }

    /**
     * Test of upSerie method, of class Operational.
     */
    @Test
    public void testUpSerie() 
    {
        System.out.println("upSerie");
        
    }

    /**
     * Test of select method, of class Operational.
     */
    @Test
    public void testSelect_4args() 
    {
        System.out.println("select");
        
    }

    /**
     * Test of exist method, of class Operational.
     */
    @Test
    public void testExist_4args_1() 
    {
        System.out.println("exist");
        
    }

    /**
     * Test of exist method, of class Operational.
     */
    @Test
    public void testExist_4args_2() 
    {
        System.out.println("exist");
        
    }

    /**
     * Test of getTotal method, of class Operational.
     */
    @Test
    public void testGetTotal() {
        System.out.println("getTotal");
        
    }

    /**
     * Test of downTotal method, of class Operational.
     */
    @Test
    public void testDownTotal() 
    {
        System.out.println("downTotal");
        
    }

    /**
     * Test of upTotal method, of class Operational.
     */
    @Test
    public void testUpTotal() 
    {
        System.out.println("upTotal");
        
    }

    /**
     * Test of splitFolio method, of class Operational.
     */
    @Test
    public void testSplitFolio() {
        System.out.println("splitFolio");
        
    }

    /**
     * Test of toString method, of class Operational.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        
    }

    /**
     * Test of upState method, of class Operational.
     */
    @Test
    public void testUpState() 
    {
        System.out.println("upState");
        
    }

    /**
     * Test of select method, of class Operational.
     */
    @Test
    public void testSelect_Connection_int() 
    {
        System.out.println("select");
        
    }

    /**
     * Test of getFullFolio method, of class Operational.
     */
    @Test
    public void testGetFullFolio() {
        System.out.println("getFullFolio");
        
    }

    /**
     * Test of exist method, of class Operational.
     */
    @Test
    public void testExist_3args() 
    {
        System.out.println("exist");
        
    }

    /**
     * Test of select method, of class Operational.
     */
    @Test
    public void testSelect_3args() 
    {
        System.out.println("select");
        
    }

    /**
     * Test of select method, of class Operational.
     */
    @Test
    public void testSelect_5args() 
    {
        System.out.println("select");
        
    }

    /**
     * Test of selectLast method, of class Operational.
     */
    @Test
    public void testSelectLast() 
    {
        System.out.println("selectLast");
        
    }

    /**
     * Test of selectRandom method, of class Operational.
     */
    @Test
    public void testSelectRandom() 
    {
        System.out.println("selectRandom");
        
    }

    /**
     * Test of downSerie method, of class Operational.
     */
    @Test
    public void testDownSerie()
    {
        System.out.println("downSerie");
    }

    /**
     * Test of downFolio method, of class Operational.
     */
    @Test
    public void testDownFolio() throws Exception {
        System.out.println("downFolio");
    }

    /**
     * Test of getMonedaLocal method, of class Operational.
     */
    @Test
    public void testGetMonedaLocal() {
        System.out.println("getMonedaLocal");
    }
    
}
