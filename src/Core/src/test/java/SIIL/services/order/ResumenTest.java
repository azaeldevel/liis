
package SIIL.services.order;

import SAT.CatalogoTest;
import SIIL.Server.Company;
import SIIL.Server.Database;
import SIIL.Server.Person;
import SIIL.core.Office;
import SIIL.trace.Trace;
import core.FailResultOperationException;
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
import stock.Flow;
import stock.ItemTest;

/**
 *
 * @author Azael Reyes
 */
public class ResumenTest 
{
    private static final boolean FL_COMMIT = true;
    private static final String BACKWARD_BD = "bc.tj";
    
    public ResumenTest() {
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
     * Test of insert method, of class Resumen.
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
        Module module = new Module(1);//el uno esta programada para GM YALE
        Flow forklift = new Flow(-1);
        SIIL.service.quotation.ServiceQuotation quoteService = new SIIL.service.quotation.ServiceQuotation();
        Resumen resumen = new Resumen(-1);
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
        Boolean flResum = null;
        try 
        {
            opertaor.download(dbserver);
            contexTrace = new Trace(BACKWARD_BD, opertaor, "Captura de Orden de Servicio (Prueba)");
            contexTrace.insert(dbserver);
            flRet = order.insert(dbserver, new Date(), forklift, Order.Type.PREVENTIVE, 98765, "Mantenimiento de Prueba.", company, quoteService, sa,office,100,person,contexTrace);
            if(!flRet) fail("Falló la inserccion de la orden.");
            flResum = resumen.insert(dbserver, forklift, order, module,contexTrace);
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(OrderTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        
        if(flResum && FL_COMMIT)
        {
            try 
            {
                dbserver.commit();
                ///System.out.println("Se genero el registro " + insertedID);
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        else if(flResum && FL_COMMIT == false)
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
     * Test of getID method, of class Resumen.
     */
    @Test
    public void testGetID() {
        System.out.println("getID");
    }

    /**
     * Test of getTitem method, of class Resumen.
     */
    @Test
    public void testGetTitem() {
        System.out.println("getTitem");
    }

    /**
     * Test of getOrder method, of class Resumen.
     */
    @Test
    public void testGetOrder() {
        System.out.println("getOrder");
    }

    /**
     * Test of getModule method, of class Resumen.
     */
    @Test
    public void testGetModule() {
        System.out.println("getModule");
    }

    /**
     * Test of upOrder method, of class Resumen.
     */
    @Test
    public void testUpOrder() 
    {
        System.out.println("upOrder");
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
        Module module = new Module(1);//el uno esta programada para GM YALE
        Flow forklift = new Flow(-1);
        SIIL.service.quotation.ServiceQuotation quoteService = new SIIL.service.quotation.ServiceQuotation();
        Resumen resumen = new Resumen(-1);
        User opertaor = new User();
        Trace contexTrace = null;
        try 
        {
            Boolean ret = null;
            ret = forklift.selectTitemRamdom(dbserver);
            //ret = forklift.downNumber(dbserver.getConnection());
            //if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            //ret = forklift.downMake(dbserver.getConnection());
            quoteService.selectRandom(dbserver.getConnection());
            order.selectRandom(dbserver);
            opertaor.selectRandom(dbserver);
        }
        catch(FailResultOperationException ex)
        {
            fail(ex.getMessage());
            return;
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getErrorCode() + " --> " + ex.getMessage());
            return;
        }
        Boolean flResum = null;
        try 
        {
            opertaor.download(dbserver);
            contexTrace = new Trace(BACKWARD_BD, opertaor, "Captura de Orden de Servicio (Prueba)");
            contexTrace.insert(dbserver);
            flResum = resumen.insert(dbserver, forklift, order, module,contexTrace);
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(OrderTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        
        if(flResum && FL_COMMIT)
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
        else if(flResum && FL_COMMIT == false)
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
    
}
