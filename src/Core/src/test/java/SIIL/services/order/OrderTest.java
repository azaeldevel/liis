    
package SIIL.services.order;

import SAT.CatalogoTest;
import SIIL.Server.Company;
import SIIL.Server.Database;
import SIIL.Server.Person;
import SIIL.client.sales.Enterprise;
import SIIL.core.Office;
import SIIL.service.quotation.ServiceQuotation;
import SIIL.services.order.Order.Type;
import SIIL.trace.Trace;
import core.bobeda.Archivo;
import core.bobeda.FTP;
import database.mysql.sales.Remision;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import session.User;
import stock.Flow;
import stock.ItemTest;

/**
 *
 * @author Azael Reyes
 */
public class OrderTest 
{
    
    private static final boolean FL_COMMIT = true;
    private static final String BACKWARD_BD = "bc.tj";
    
    public OrderTest() {
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
     * Test of getSA method, of class Order.
     */
    @Test
    public void testGetSA() {
        System.out.println("getSA");
    }

    /**
     * Test of getCompany method, of class Order.
     */
    @Test
    public void testGetCompany() {
        System.out.println("getCompany");
    }

    /**
     * Test of getTitem method, of class Order.
     */
    @Test
    public void testGetTitem() {
        System.out.println("getTitem");
    }

    /**
     * Test of getType method, of class Order.
     */
    @Test
    public void testGetType() {
        System.out.println("getType");
    }

    /**
     * Test of getHorometro method, of class Order.
     */
    @Test
    public void testGetHorometro() 
    {
        System.out.println("getHorometro");
    }

    /**
     * Test of getDescripcion method, of class Order.
     */
    @Test
    public void testGetDescripcion() 
    {
        System.out.println("getDescripcion");
    }

    /**
     * Test of insert method, of class Order.
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
        
        Company company = new Company();
        try 
        {
            company.selectRandom(dbserver);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getErrorCode() + " --> " + ex.getMessage());
            return;
        }
        Order order = new Order(-1);
        Flow forklift = new Flow(-1);
        SIIL.service.quotation.ServiceQuotation quoteService = new SIIL.service.quotation.ServiceQuotation();
        Office office = new Office(-1);
        Person person = new Person(-1);
        User opertaor = new User();
        Trace contexTrace = null;
        Remision sa = new Remision(-1);
        try 
        {
            Boolean ret = null;
            ret = forklift.selectTitemRamdom(dbserver);
            //if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            //ret = forklift.downNumber(dbserver.getConnection());
            //if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            //ret = forklift.downMake(dbserver.getConnection());
            quoteService.selectRandom(dbserver.getConnection());
            office.selectRandom(dbserver.getConnection());
            office.download(dbserver.getConnection());
            person.selectRandom(dbserver.getConnection());
            person.fill(dbserver, person.getpID(), BACKWARD_BD);
            opertaor.selectRandom(dbserver);
            sa.selectRandom(dbserver);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getErrorCode() + " --> " + ex.getMessage());
            return;
        }
        Boolean flRet = null;
        try 
        {
            opertaor.download(dbserver);
            contexTrace = new Trace(BACKWARD_BD, opertaor, "Captura de Orden de Servicio.");
            contexTrace.insert(dbserver);
            flRet = order.insert(dbserver, new Date(), forklift, Order.Type.PREVENTIVE, 98765, "Mantenimiento de Prueba.", company, quoteService, sa,office,100,person,contexTrace);
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(OrderTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
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
                //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail("Falló RollBack " + ex.getMessage());
            }
        }
        dbserver.close();   
    }

    /**
     * Test of upSA method, of class Order.
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
        
        boolean flRet = false;
        try 
        {
            Order order = new Order(-1);
            Remision sa = new Remision(-1);
                        
            order.selectRandom(dbserver);
            sa.selectRandom(dbserver);   
            
            flRet = order.upSA(dbserver, sa, null);
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
                //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail("Falló RollBack " + ex.getMessage());
            }
        }
        dbserver.close();
    }

    /**
     * Test of upQuotedService method, of class Order.
     */
    @Test
    public void testUpQuotedService() throws Exception {
        System.out.println("upQuotedService");
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
            Order order = new Order(-1);
            ServiceQuotation serviceQuotation = new ServiceQuotation();
                        
            order.selectRandom(dbserver);
            serviceQuotation.selectRandom(dbserver.getConnection());   
            
            flRet = order.upQuotedService(dbserver, serviceQuotation, null);
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
                //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail("Falló RollBack " + ex.getMessage());
            }
        }
        dbserver.close();
    }

    /**
     * Test of upEnterprise method, of class Order.
     */
    @Test
    public void testUpEnterprise()
    {
        System.out.println("upEnterprise");                
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
            Order order = new Order(-1);
            Enterprise enterprise = new Enterprise();
                        
            order.selectRandom(dbserver);
            enterprise.selectRandom(dbserver);   
            
            flRet = order.upEnterprise(dbserver, enterprise, null);
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
                //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail("Falló RollBack " + ex.getMessage());
            }
        }
        dbserver.close();
    }

    /**
     * Test of lastHorometro method, of class Order.
     */
    @Test
    public void testLastHorometro()
    {
        System.out.println("lastHorometro");
    }

    /**
     * Test of downSA method, of class Order.
     */
    @Test
    public void testDownSA()
    {
        System.out.println("downSA");
    }

    /**
     * Test of downFolio method, of class Order.
     */
    @Test
    public void testDownFolio() 
    {
        System.out.println("downFolio");
    }

    /**
     * Test of downTechnical method, of class Order.
     */
    @Test
    public void testDownTechnical() 
    {
        System.out.println("downTechnical");
    }

    /**
     * Test of downQuoteService method, of class Order.
     */
    @Test
    public void testDownQuoteService() 
    {
        System.out.println("downQuoteService");
    }

    /**
     * Test of downType method, of class Order.
     */
    @Test
    public void testDownType() throws Exception {
        System.out.println("downType");
    }

    /**
     * Test of downItemFlow method, of class Order.
     */
    @Test
    public void testDownItemFlow() 
    {
        System.out.println("downItemFlow");
    }

    /**
     * Test of downFhService method, of class Order.
     */
    @Test
    public void testDownFhService() 
    {
        System.out.println("downFhService");
    }

    /**
     * Test of downDescription method, of class Order.
     */
    @Test
    public void testDownDescription() 
    {
        System.out.println("downDescription");        
    }

    /**
     * Test of downHorometro method, of class Order.
     */
    @Test
    public void testDownHorometro() 
    {
        System.out.println("downHorometro");
    }

    /**
     * Test of downCompany method, of class Order.
     */
    @Test
    public void testDownCompany() 
    {
        System.out.println("downCompany");        
    }

    /**
     * Test of getFhService method, of class Order.
     */
    @Test
    public void testGetFhService() 
    {
        System.out.println("getFhService");        
    }

    /**
     * Test of getID method, of class Order.
     */
    @Test
    public void testGetID() 
    {
        System.out.println("getID");        
    }

    /**
     * Test of getItemFlow method, of class Order.
     */
    @Test
    public void testGetItemFlow() 
    {
        System.out.println("getItemFlow");        
    }

    /**
     * Test of getQuoteService method, of class Order.
     */
    @Test
    public void testGetQuoteService() 
    {
        System.out.println("getQuoteService");
    }

    /**
     * Test of getTechnical method, of class Order.
     */
    @Test
    public void testGetTechnical() 
    {
        System.out.println("getTechnical");
    }

    /**
     * Test of getFolio method, of class Order.
     */
    @Test
    public void testGetFolio() 
    {
        System.out.println("getFolio");
    }

    /**
     * Test of selectRandom method, of class Order.
     */
    @Test
    public void testSelectRandom() 
    {
        System.out.println("selectRandom");
    }

    /**
     * Test of upFlow method, of class Order.
     */
    @Test
    public void testUpFlow() 
    {
        System.out.println("upFlow");              
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
            Order order = new Order(-1);
            Flow flow = new Flow(-1);
                        
            order.selectRandom(dbserver);
            flow.selectRandom(dbserver.getConnection());   
            
            flRet = order.upFlow(dbserver, flow, null);
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
                //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail("Falló RollBack " + ex.getMessage());
            }
        }
        dbserver.close();
    }

    /**
     * Test of upType method, of class Order.
     */
    @Test
    public void testUpType() 
    {
        System.out.println("upType");                              
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
            Order order = new Order(-1);
            Type type = Type.PREVENTIVE;
                        
            order.selectRandom(dbserver);            
            flRet = order.upType(dbserver, type, null);
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
                //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail("Falló RollBack " + ex.getMessage());
            }
        }
        dbserver.close();        
    }

    /**
     * Test of upHorometro method, of class Order.
     */
    @Test
    public void testUpHorometro() 
    {
        System.out.println("upHorometro");    
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
            Order order = new Order(-1);
            int ho = 1000;            
            order.selectRandom(dbserver);            
            flRet = order.upHorometro(dbserver, ho, null);
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
                //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                fail("Falló RollBack " + ex.getMessage());
            }
        }
        dbserver.close();
    }

    /**
     * Test of upMechanic method, of class Order.
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
        
        boolean flRet = false;
        try 
        {
            Order order = new Order(-1);
            Person person = new Person();
            person.selectRandom(dbserver.getConnection());
            order.selectRandom(dbserver);            
            flRet = order.upMechanic(dbserver, person, null);
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
     * Test of upFolio method, of class Order.
     */
    @Test
    public void testUpFolio() 
    {
        System.out.println("upFolio"); 
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
            Order order = new Order(-1);
            int folio = 1001;
            order.selectRandom(dbserver);            
            flRet = order.upFolio(dbserver, 1001, null);
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
     * Test of upFecha method, of class Order.
     */
    @Test
    public void testUpFecha() 
    {
        System.out.println("upFecha");
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
            Order order = new Order(-1);
            order.selectRandom(dbserver);            
            flRet = order.upFecha(dbserver, new Date(), null);
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
     * Test of upDescripcion method, of class Order.
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
        
        boolean flRet = false;
        try 
        {
            Order order = new Order(-1);
            order.selectRandom(dbserver);            
            flRet = order.upDescripcion(dbserver, "Test de comentario.", null);
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
     * Test of upArchivo method, of class Order.
     */
    @Test
    public void testUpArchivo() 
    {
        System.out.println("upArchivo");
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
        
        Return flRet = null;
        try 
        {
            Order order = new Order(-1);
            order.selectRandom(dbserver);   
            Archivo archivo = new Archivo(-1);
            archivo.selectRandom(dbserver);
            flRet = order.upArchivo(dbserver, archivo, null);
        }
        catch (SQLException ex) 
        {
            fail(ex.getMessage());
        }
        
        if(flRet.isFlag() && FL_COMMIT)
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
        else if(flRet.isFlag() && FL_COMMIT == false)
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
     * Test of download method, of class Order.
     */
    @Test
    public void testDownload() 
    {
        System.out.println("download");
    }

    /**
     * Test of search method, of class Order.
     */
    @Test
    public void testSearch() 
    {
        System.out.println("search");
    }

    /**
     * Test of getIdentificator method, of class Order.
     */
    @Test
    public void testGetIdentificator() 
    {
        System.out.println("getIdentificator");
    }

    /**
     * Test of getBrief method, of class Order.
     */
    @Test
    public void testGetBrief() {
        System.out.println("getBrief");
    }

    /**
     * Test of getArchivo method, of class Order.
     */
    @Test
    public void testGetArchivo() 
    {
        System.out.println("getArchivo");
    }

    /**
     * Test of massAssociation method, of class Order.
     */
    @Test
    public void testMassAssociation()
    {
        System.out.println("massAssociation");
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
        
        FTP ftpServer = new FTP();
        boolean expResult = false;
        try 
        {
            expResult = ftpServer.connect(serverConfig);
        } 
        catch (IOException ex) 
        {
            //Logger.getLogger(FTPTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        File directory = new File("C:\\Users\\Azael Reyes\\Documents\\Proyectos\\SIIL\\trunk\\System\\Core\\SAs");
        List<String> logger = new ArrayList<>();
        Return ret = null;
        try 
        {
            Office office = new Office(1);
            office.download(dbserver.getConnection());
            List<Association<Order>> associations = new ArrayList<>();
            Order.massAssociationReadDirectory(dbserver, directory, associations, logger,office);
            ret = Order.massAssociation(dbserver, ftpServer, directory, logger, associations, null,office);
            for(String str : logger)
            {
                //System.out.println(str);
            }
            //Order.massAssociationCleanDirectory(associations);
        }
        catch (IOException | SQLException ex) 
        {
            //Logger.getLogger(OrderTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        
        if(ret.isFlag() && FL_COMMIT)
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
        else if(ret.isFlag() && FL_COMMIT == false)
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
            //fail("Cant. Reg. incorrectos : " + affected);
        }
        dbserver.close();*/
    }

    /**
     * Test of downArchivo method, of class Order.
     */
    @Test
    public void testDownArchivo() 
    {
        System.out.println("downArchivo");
    }

    /**
     * Test of getEnterprise method, of class Order.
     */
    @Test
    public void testGetEnterprise() 
    {
        System.out.println("getEnterprise");
    }
    
}
