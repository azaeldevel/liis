
package SIIL.services.grua;

import SAT.CatalogoTest;
import SIIL.Server.Database;
import database.mysql.stock.Titem;
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
import stock.Flow;
import stock.ItemTest;

/**
 *
 * @author Azael Reyes
 */
public class MovitemsTest 
{
    private static final boolean FL_COMMIT = true;
    
    public MovitemsTest() {
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
     * Test of insert method, of class Movitems.
     */
    @Test
    public void testInsert()     
    {
        System.out.println("insert");        
        Timestamp date = null;
        SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();        
        Database dbserver = null;
        try 
        {
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
            dbserver = new Database(serverConfig);
            date = dbserver.getTimestamp();
        } 
        catch (ClassNotFoundException | SQLException | IOException | ParserConfigurationException | SAXException ex) 
        {
            Logger.getLogger(CatalogoTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        
        
        SimpleDateFormat sdf = new SimpleDateFormat("yymmddhhmmss");
        Random r = new Random();
        int Low = 1;
        int High = 99;
        int Result = r.nextInt(High-Low) + Low;  
        
        
        Movements mov = new Movements(-1);
        List<Flow> hesquis = new ArrayList<>();
        Flow forklift = new Flow(-1);
        hesquis.add(forklift);
        Flow battery = new Flow(-1);
        Flow charger = new Flow(-1);
        Flow charger2 = new Flow(-1);
        hesquis.add(charger2);
        //Flow mina = new Flow(-1);   
         
        try 
        {
            Return ret = null;
            ret = mov.selectLast(dbserver.getConnection());
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            forklift.selectForkliftRandom(dbserver);
            if(!forklift.downItem(dbserver))fail("Fallo la descar del Item desde el flow.");
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            ret = forklift.getItem().downNumber(dbserver.getConnection());
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            ret = forklift.getItem().downMake(dbserver.getConnection());
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            battery.selectBatteryRandom(dbserver);
            battery.downItem(dbserver);
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            ret = battery.getItem().downMake(dbserver.getConnection());
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            ret = battery.getItem().downNumber(dbserver.getConnection());
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            charger.selectChargerRandom(dbserver);
            charger.downItem(dbserver);
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            ret = charger.getItem().downMake(dbserver.getConnection());
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            ret = charger.getItem().downNumber(dbserver.getConnection());
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            /*mina.selectMinaRandom(dbserver);
            mina.downItem(dbserver);
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            ret = mina.getItem().downMake(dbserver.getConnection());
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            ret = mina.getItem().downNumber(dbserver.getConnection());*/
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            charger2.selectChargerRandom(dbserver);
            charger2.downItem(dbserver);
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            ret = charger2.getItem().downMake(dbserver.getConnection());
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            ret  = charger2.getItem().downNumber(dbserver.getConnection());
            if(ret.getStatus() == Return.Status.FAIL) fail(ret.getMessage());
            
            //((Forklift)forklift.getItem()).setBattery((Battery)battery.getItem());
            //((Forklift)forklift.getItem()).setCharger((Charger)charger.getItem());
            //((Forklift)forklift.getItem()).setMina((Mina)mina.getItem());
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getErrorCode() + " --> " + ex.getMessage());
            return;
        }      
        
        Return result = null;
        Movitems instance = new Movitems();
        try 
        {
            result = instance.insert(dbserver,hesquis,mov);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getErrorCode() + " --> " + ex.getMessage());
            return;
        }
        
        int insertedID = 0;
        switch (result.getStatus()) 
        {
            case DONE:
                insertedID = 1;//se generan vaior id.
                break;
            case FAIL:
                fail(result.getMessage());
                break;
            default:
                fail("Resultado desconocido.");
                break;
        }
        if(insertedID > 0 && FL_COMMIT)
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
        else if(insertedID > 0 && FL_COMMIT == false)
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
                return;
            }
            fail("Cant. Reg. incorrectos : " + insertedID);
        }
        dbserver.close();
    }
    
}
