
package SIIL.services.grua;

import SAT.CatalogoTest;
import SIIL.Server.Company;
import SIIL.Server.Database;
import SIIL.core.Office;
import SIIL.trace.Trace;
import database.mysql.sales.Remision;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import session.User;
import stock.Flow;

/**
 *
 * @author Azael Reyes
 */
public class CapturaTest 
{
    private static final boolean FL_COMMIT = true;
    private static final String BACKWARD_BD = "bc.tj";
    
    public CapturaTest() {
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
     * Test of getOwner method, of class Captura.
     */
    @Test
    public void testGetOwner() {
        System.out.println("getOwner");
    }

    /**
     * Test of setOwner method, of class Captura.
     */
    @Test
    public void testSetOwner() {
        System.out.println("setOwner");
    }

    /**
     * Test of getFolio method, of class Captura.
     */
    @Test
    public void testGetFolio() {
        System.out.println("getFolio");
    }

    /**
     * Test of setFolio method, of class Captura.
     */
    @Test
    public void testSetFolio() {
        System.out.println("setFolio");
    }

    /**
     * Test of getFecha method, of class Captura.
     */
    @Test
    public void testGetFecha() {
        System.out.println("getFecha");
    }

    /**
     * Test of setFecha method, of class Captura.
     */
    @Test
    public void testSetFecha() {
        System.out.println("setFecha");
    }

    /**
     * Test of getTipo method, of class Captura.
     */
    @Test
    public void testGetTipo() {
        System.out.println("getTipo");
    }

    /**
     * Test of setTipo method, of class Captura.
     */
    @Test
    public void testSetTipo_Tipo() {
        System.out.println("setTipo");
    }

    /**
     * Test of setTipo method, of class Captura.
     */
    @Test
    public void testSetTipo_String() {
        System.out.println("setTipo");
    }

    /**
     * Test of getUso method, of class Captura.
     */
    @Test
    public void testGetUso() {
        System.out.println("getUso");
    }

    /**
     * Test of setUso method, of class Captura.
     */
    @Test
    public void testSetUso_Uso() {
        System.out.println("setUso");
    }

    /**
     * Test of setUso method, of class Captura.
     */
    @Test
    public void testSetUso_String() {
        System.out.println("setUso");
    }

    /**
     * Test of getSa method, of class Captura.
     */
    @Test
    public void testGetSa() {
        System.out.println("getSa");
    }

    /**
     * Test of setSa method, of class Captura.
     */
    @Test
    public void testSetSa() 
    {
        System.out.println("setSa");
    }

    /**
     * Test of getCompany method, of class Captura.
     */
    @Test
    public void testGetCompany() 
    {
        System.out.println("getCompany");
    }

    /**
     * Test of setCompany method, of class Captura.
     */
    @Test
    public void testSetCompany() {
        System.out.println("setCompany");
    }

    /**
     * Test of getFirma method, of class Captura.
     */
    @Test
    public void testGetFirma() {
        System.out.println("getFirma");
    }

    /**
     * Test of setFirma method, of class Captura.
     */
    @Test
    public void testSetFirma() {
        System.out.println("setFirma");
    }

    /**
     * Test of getOficina method, of class Captura.
     */
    @Test
    public void testGetOficina() {
        System.out.println("getOficina");
    }

    /**
     * Test of setOficina method, of class Captura.
     */
    @Test
    public void testSetOficina() {
        System.out.println("setOficina");
    }

    /**
     * Test of getTitems method, of class Captura.
     */
    @Test
    public void testGetTitems() {
        System.out.println("getTitems");
    }

    /**
     * Test of setTitems method, of class Captura.
     */
    @Test
    public void testSetTitems() {
        System.out.println("setTitems");
    }

    /**
     * Test of getNote method, of class Captura.
     */
    @Test
    public void testGetNote() {
        System.out.println("getNote");
        Captura instance = new Captura();
    }

    /**
     * Test of setNote method, of class Captura.
     */
    @Test
    public void testSetNote() {
        System.out.println("setNote");
    }

    /**
     * Test of getCredentail method, of class Captura.
     */
    @Test
    public void testGetCredentail() {
        System.out.println("getCredentail");
    }

    /**
     * Test of setCredentail method, of class Captura.
     */
    @Test
    public void testSetCredentail() {
        System.out.println("setCredentail");
    }

    /**
     * Test of create method, of class Captura.
     */
    @Test
    public void testCreate()
    {
        System.out.println("create");
        Timestamp date = null;
        SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();        
        Database dbserver = null;
        try 
        {
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
            dbserver = new Database(serverConfig);
            date = dbserver.getTimestamp();
        } 
        catch (SQLException | IOException | ParserConfigurationException | SAXException ex) 
        {
            Logger.getLogger(CatalogoTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CapturaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("yymmddhhmmss");
        Random r = new Random();
        int Low = 1;
        int High = 99;
        int Result = r.nextInt(High-Low) + Low;  
        
        
        String firma;
        try 
        {
            firma = "Captura de Movimiento - " + sdf.format(dbserver.getTimestamp());
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getErrorCode() + " --> " + ex.getMessage());
            return;
        }
        Office officeTitem = new Office(-1);
        Office officeOperator = new Office(-1);
        try 
        {
            officeTitem.selectRandom(dbserver.getConnection());
            Throwable download = officeTitem.download(dbserver.getConnection());
            officeOperator.selectRandom(dbserver.getConnection());
            officeOperator.download(dbserver.getConnection());
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getErrorCode() + " --> " + ex.getMessage());
            return;
        }
        Uso uso = new Uso(-1);
        try 
        {
            uso.selectRandom(dbserver.getConnection());
            uso.download(dbserver.getConnection());
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getErrorCode() + " --> " + ex.getMessage());
            return;
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
        Tipo tmov = new Tipo(-1);        
        
        Movements mov = new Movements(-1);
        List<Flow> hesquis = new ArrayList<>();
        Flow forklift = new Flow(-1);
        hesquis.add(forklift);
        Flow battery = new Flow(-1);
        Flow charger = new Flow(-1);
        Flow charger2 = new Flow(-1);
        hesquis.add(charger2);
        //Flow mina = new Flow(-1);   
        
        User opertaor = new User(); 
        Trace contexTrace = null;
        
        Remision sa = new Remision(-1);
        
        try 
        {
            Return ret = null;
            ret = mov.selectLast(dbserver.getConnection());
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            forklift.selectForkliftRandom(dbserver);
            if(!forklift.downItem(dbserver)) fail("Fallo la descarga de titem desde el Flow");
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            ret = forklift.getItem().downNumber(dbserver.getConnection());
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            ret = forklift.getItem().downMake(dbserver.getConnection());
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            if(!battery.selectBatteryRandom(dbserver))fail("Fallo la deacar de titem desde el Flow");
            battery.downItem(dbserver);
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            ret = battery.getItem().downMake(dbserver.getConnection());
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            ret = battery.getItem().downNumber(dbserver.getConnection());
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            if(!charger.selectChargerRandom(dbserver))fail("Fallo la deacar de titem desde el Flow");
            charger.downItem(dbserver);
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            ret = charger.getItem().downMake(dbserver.getConnection());
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            ret = charger.getItem().downNumber(dbserver.getConnection());
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            /*if(!mina.selectMinaRandom(dbserver))fail("Fallo la deacar de titem desde el Flow");
            mina.downItem(dbserver);
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            ret = mina.getItem().downMake(dbserver.getConnection());
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            ret = mina.getItem().downNumber(dbserver.getConnection());*/
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            if(!charger2.selectChargerRandom(dbserver))fail("Fallo la deacar de titem desde el Flow");
            charger2.downItem(dbserver);
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            ret = charger2.getItem().downMake(dbserver.getConnection());
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            ret  = charger2.getItem().downNumber(dbserver.getConnection());
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            
            //((Forklift)forklift.getItem()).setBattery((Battery)battery.getItem());
            //((Forklift)forklift.getItem()).setCharger((Charger)charger.getItem());
            //((Forklift)forklift.getItem()).setMina((Mina)mina.getItem());   
            tmov.selectRandom(dbserver.getConnection());
            tmov.download(dbserver.getConnection());
            
            opertaor.selectRandom(dbserver);
            sa.selectRandom(dbserver);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getErrorCode() + " --> " + ex.getMessage());
            return;
        }
        
        Captura captura = new Captura();
        captura.setCompany(company);
        try 
        {
            captura.setFecha(dbserver.getDateToday());
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getErrorCode() + " --> " + ex.getMessage());
            return;
        }
        
        
        
        captura.setOwner(Captura.Owner.SIIL);
        captura.setFolio(sdf.format(date));
        captura.setFecha(date);
        captura.setTipo(tmov);
        captura.setUso(uso);
        captura.setSA(sa);
        captura.setCompany(company);
        captura.setFirma(firma);
        captura.setOficina(officeTitem);
        captura.setNote("Prueba de captura de movimiento ###",true,true);
        captura.setHequis(hesquis);
        
        Boolean ret = null;
        /*
        try 
        {
            opertaor.download(dbserver);
            contexTrace = new Trace(BACKWARD_BD, opertaor, "Captura de Orden de Servicio.");
            ret = captura.create(dbserver,contexTrace, null, officeOperator);
        }
        catch (SQLException ex) 
        {
            fail(ex.getErrorCode() + " -+-> " + ex.getMessage());
            return;
        }

        if(ret == false)
        {
            fail("error desconocido.");
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
                fail(ex.getErrorCode() + " --> " + ex.getMessage());
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
                fail(ex.getErrorCode() + " --> " + ex.getMessage());
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
        }     */  
        dbserver.close();       
    }
    
}
