
package SIIL.services.grua;

import SAT.CatalogoTest;
import SIIL.Server.Company;
import SIIL.Server.Database;
import SIIL.core.Office;
import SIIL.trace.Trace;
import database.mysql.purchases.Provider;
import database.mysql.stock.Titem;
import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
import static process.Return.Status.DONE;
import static process.Return.Status.FAIL;
import session.User;
import stock.Flow;
import stock.ItemTest;

/**
 *
 * @author Azael Reyes
 */
public class ResumovTest 
{
    private static final boolean FL_COMMIT = true;
    private static final String BACKWARD_BD = "bc.tj";
    
    public ResumovTest() {
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
     * Test of getBD method, of class Resumov.
     */
    @Test
    public void testGetBD() {
        System.out.println("getBD");
    }

    /**
     * Test of getOffice method, of class Resumov.
     */
    @Test
    public void testGetOffice() {
        System.out.println("getOffice");
    }

    /**
     * Test of getUso method, of class Resumov.
     */
    @Test
    public void testGetUso() {
        System.out.println("getUso");
    }

    /**
     * Test of getNote method, of class Resumov.
     */
    @Test
    public void testGetNote() {
        System.out.println("getNote");
    }

    /**
     * Test of getCompany method, of class Resumov.
     */
    @Test
    public void testGetCompany() {
        System.out.println("getCompany");
    }

    /**
     * Test of getFhmov method, of class Resumov.
     */
    @Test
    public void testGetFhmov() {
        System.out.println("getFhmov");
    }

    /**
     * Test of getTitem method, of class Resumov.
     */
    @Test
    public void testGetTitem() {
        System.out.println("getTitem");
    }

    /**
     * Test of getBattery method, of class Resumov.
     */
    @Test
    public void testGetBattery() {
        System.out.println("getBattery");
    }

    /**
     * Test of getCharger method, of class Resumov.
     */
    @Test
    public void testGetCharger() {
        System.out.println("getCharger");
    }

    /**
     * Test of getTitem1 method, of class Resumov.
     */
    @Test
    public void testGetTitem1() {
        System.out.println("getTitem1");
    }

    /**
     * Test of getTitem2 method, of class Resumov.
     */
    @Test
    public void testGetTitem2() {
        System.out.println("getTitem2");
    }


    /**
     * Test of insert method, of class Resumov.
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
        
        Titem item = new Titem(-1);
        Provider prov = new Provider(-1); 
        User opertaor = new User();
        Trace contexTrace = null;   
        try 
        {
            prov.selectRandom(dbserver.getConnection());
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getErrorCode() + " --> " + ex.getMessage());
            return;
        }
        
        Office office = new Office(-1);
        try 
        {
            office.selectRandom(dbserver.getConnection());
            office.download(dbserver.getConnection());
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
        
        Flow fork = new Flow(-1);
        Flow battery = new Flow(-1);
        //Flow mina = new Flow(-1);
        try 
        {
            fork.selectForkliftRandom(dbserver);
            fork.downItem(dbserver);
            fork.getItem().downNumber(dbserver.getConnection());
            battery.selectBatteryRandom(dbserver);
            battery.downItem(dbserver);
            battery.getItem().downNumber(dbserver.getConnection());
            //((Forklift)fork.getItem()).setBattery((Battery)battery.getItem());
            /*mina.selectMinaRandom(dbserver);
            mina.downItem(dbserver);
            mina.getItem().downNumber(dbserver.getConnection());*/
            opertaor.selectRandom(dbserver);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getErrorCode() + " --> " + ex.getMessage());
            return;
        }
        
        //Insersion simple
        Resumov resumov = new Resumov(-1);
        Boolean result = null;
        try 
        {
            opertaor.download(dbserver);
            contexTrace = new Trace(BACKWARD_BD, opertaor, "Movimiento directo de hoja de Equipos.");
            if(!resumov.find(dbserver, fork))
            {
                result = resumov.insert(dbserver, office, uso, company, fork, new Date(),contexTrace);  
                //resumov.insert(dbserver, office, uso, company, battery, new Date(),contexTrace,null);
                //resumov.insert(dbserver, office, uso, company, mina, new Date(),contexTrace,null);
            }
            else
            {
                return;
            }
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getErrorCode() + " -+-> " + ex.getMessage());
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
                fail("Falló RollBack " + ex.getMessage());
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
                fail("Falló RollBack " + ex.getMessage());
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
            fail("Cant. Reg. incorrectos : ");
        }
        dbserver.close();
    }

    /**
     * Test of getHequi method, of class Resumov.
     */
    @Test
    public void testGetHequi() {
        System.out.println("getHequi");
    }

    /**
     * Test of getHequi1 method, of class Resumov.
     */
    @Test
    public void testGetHequi1() {
        System.out.println("getHequi1");
    }

    /**
     * Test of getHequi2 method, of class Resumov.
     */
    @Test
    public void testGetHequi2() {
        System.out.println("getHequi2");
    }

    /**
     * Test of selectLast method, of class Resumov.
     */
    @Test
    public void testSelectLast() throws Exception {
        System.out.println("selectLast");
    }

    /**
     * Test of upMina method, of class Resumov.
     */
    @Test
    public void testUpMina()
    {
        System.out.println("upMina");
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
        }
        catch (ClassNotFoundException ex) 
        {
            Logger.getLogger(ResumovTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Connection connection = dbserver.getConnection();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyymmddhhmmss");
        Random r = new Random();
        int Low = 1;
        int High = 99;
        int Result = r.nextInt(High-Low) + Low;
        
        Mina mina = new Mina(-1);
        Return ret = null;
        User opertaor = new User();
        Trace contexTrace = null;
        try 
        {
            mina.selectRandom(connection);
            ret = mina.downNumber(connection);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getErrorCode() + " --> " + ex.getMessage());
            return;
        }
        if(ret.getStatus() == Return.Status.FAIL)
        {
            fail(ret.getMessage());
        }
        Resumov instance = new Resumov(-1);
        try 
        {
            instance.selectLast(connection);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        Return result;
        try 
        {
            opertaor.download(dbserver);
            contexTrace = new Trace(BACKWARD_BD, opertaor, "Movimiento directo de hoja de Equipos.");
            result = instance.upMina(connection, mina,contexTrace);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getErrorCode() + " --> " + ex.getMessage());
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
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
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
            fail("Cant. Reg. incorrectos : " + affected);
        }
        dbserver.close();        
    }

    /**
     * Test of upBattery method, of class Resumov.
     */
    @Test
    public void testUpBattery() 
    {
        System.out.println("upBattery");
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
        
        Connection connection = dbserver.getConnection();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyymmddhhmmss");
        Random r = new Random();
        int Low = 1;
        int High = 99;
        int Result = r.nextInt(High-Low) + Low;
        
        Battery battery = new Battery(-1);;
        User opertaor = new User();
        Trace contexTrace = null;
        Return ret = null;
        try 
        {
            battery.selectRandom(connection);
            ret = battery.downNumber(connection);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getErrorCode() + " --> " + ex.getMessage());
            return;
        }
        if(ret.getStatus() == Return.Status.FAIL)
        {
            fail(ret.getMessage());
        }
        Resumov instance = new Resumov(-1);
        try 
        {
            instance.selectLast(connection);
            opertaor.selectRandom(dbserver);
        }  
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        /*
        Return result;
        try 
        {
            opertaor.download(dbserver);
            contexTrace = new Trace(BACKWARD_BD, opertaor, "Movimiento directo de hoja de Equipos.");
            result = instance.upBattery(connection, battery,contexTrace,null);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getErrorCode() + " --> " + ex.getMessage());
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
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
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
            fail("Cant. Reg. incorrectos : " + affected);
        }*/
        dbserver.close();
    }

    /**
     * Test of upCharger method, of class Resumov.
     */
    @Test
    public void testUpCharger() 
    {
        System.out.println("upCharger");
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
        
        Charger charger = new Charger(-1);
        User opertaor = new User();
        Trace contexTrace = null;
        Return ret = null;
        try 
        {
            charger.selectRandom(connection);
            ret = charger.downNumber(connection);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getErrorCode() + " --> " + ex.getMessage());
            return;
        }
        if(ret.getStatus() == Return.Status.FAIL)
        {
            fail(ret.getMessage());
        }
        Resumov instance = new Resumov(-1);
        try 
        {
            instance.selectLast(connection);
            opertaor.selectRandom(dbserver);
        }  
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        /*
        Return result;
        try 
        {
            opertaor.download(dbserver);
            contexTrace = new Trace(BACKWARD_BD, opertaor, "Captura de Orden de Servicio.");
            result = instance.upCharger(connection, charger,contexTrace,null);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getErrorCode() + " --> " + ex.getMessage());
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
                Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
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
            fail("Cant. Reg. incorrectos : " + affected);
        }*/
        dbserver.close();
    }

    /**
     * Test of getMina method, of class Resumov.
     */
    @Test
    public void testGetMina() {
        System.out.println("getMina");
    }

    /**
     * Test of upNote method, of class Resumov.
     */
    @Test
    public void testUpNote()
    {
        System.out.println("upNote");
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
        
        Resumov instance = new Resumov(-1);
        User opertaor = new User();
        Trace contexTrace = null;
        try 
        {
            instance.selectLast(connection);
            opertaor.selectRandom(dbserver);
        }  
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        Return result;
        try 
        {
            opertaor.download(dbserver);
            contexTrace = new Trace(BACKWARD_BD, opertaor, "Captura de Orden de Servicio.");
            result = instance.upNote(connection, "Nota de Prueba.",contexTrace);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getErrorCode() + " --> " + ex.getMessage());
            return;
        }

        
        int affected = 0;
        if(result.isFail())
        {
            fail(result.getMessage());
        }
        affected = (int) result.getParam();
        if(affected == 1 && FL_COMMIT)
        {            
            try 
            {
                dbserver.commit();
                //System.out.println("Se afectaron registros " + affected);
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
                fail(ex.getErrorCode() + " --> " + ex.getMessage());
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
            fail("Cant. Reg. incorrectos : " + affected);
        }
        dbserver.close();
    }

    /**
     * Test of downForklift method, of class Resumov.
     */
    @Test
    public void testDownForklift()
    {
        System.out.println("downForklift");
    }

    /**
     * Test of getHequi3 method, of class Resumov.
     */
    @Test
    public void testGetHequi3() {
        System.out.println("getHequi3");
        
    }

    /**
     * Test of getForklift method, of class Resumov.
     */
    @Test
    public void testGetForklift() {
        System.out.println("getForklift");
    }

    /**
     * Test of find method, of class Resumov.
     */
    @Test
    public void testFind() {
        System.out.println("find");
    }

    /**
     * Test of upOffice method, of class Resumov.
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
        
        Connection connection = dbserver.getConnection();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddhhmmss");
        Random r = new Random();
        int Low = 1;
        int High = 99;
        int Result = r.nextInt(High-Low) + Low;
        
        Resumov instance = new Resumov(-1);
        Office office = new Office(-1);
        SIIL.Server.User opertaor = new SIIL.Server.User();
        Trace contexTrace = null;
        try 
        {
            instance.selectLast(connection);
            office.selectRandom(connection);
            office.download(connection);
            opertaor.selectRandom(connection);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        Boolean result;
        try 
        {
            contexTrace = new Trace(BACKWARD_BD, opertaor, "Captura de Orden de Servicio.");
            result = instance.upOffice(dbserver,office,contexTrace);
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
                //System.out.println("Se afectaron registros " + affected);
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
                fail(ex.getErrorCode() + " --> " + ex.getMessage());
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
        dbserver.close();
    }

    /**
     * Test of upCompany method, of class Resumov.
     */
    @Test
    public void testUpCompany() 
    {
        System.out.println("upCompany");
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
        
        Resumov instance = new Resumov(-1);
        Company company = new Company(-1);
        SIIL.Server.User opertaor = new SIIL.Server.User();
        Trace contexTrace = null;
        try 
        {
            instance.selectLast(connection);
            company.selectRandom(dbserver);
            company.download(dbserver);
            opertaor.selectRandom(connection);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        Boolean result;
        try 
        {
            contexTrace = new Trace(BACKWARD_BD, opertaor, "Captura de Orden de Servicio.");
            result = instance.upCompany(dbserver,company,contexTrace);
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
                //System.out.println("Se afectaron registros " + affected);
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
                fail(ex.getErrorCode() + " --> " + ex.getMessage());
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
        dbserver.close();
    }

    /**
     * Test of upFecha method, of class Resumov.
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
        
        Connection connection = dbserver.getConnection();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddhhmmss");
        Random r = new Random();
        int Low = 1;
        int High = 99;
        int Result = r.nextInt(High-Low) + Low;
        
        Resumov instance = new Resumov(-1);
        Date fecha = new Date();
        SIIL.Server.User opertaor = new SIIL.Server.User();
        Trace contexTrace = null;
        try 
        {
            instance.selectLast(connection);
            opertaor.selectRandom(connection);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        Boolean result;
        try 
        {
            contexTrace = new Trace(BACKWARD_BD, opertaor, "Captura de Orden de Servicio.");
            result = instance.upFecha(dbserver,fecha,contexTrace);
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
                //System.out.println("Se afectaron registros " + affected);
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
                fail(ex.getErrorCode() + " --> " + ex.getMessage());
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
        dbserver.close();
    }

    /**
     * Test of upUso method, of class Resumov.
     */
    @Test
    public void testUpUso() 
    {
        System.out.println("upUso");
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
        
        Resumov instance = new Resumov(-1);
        Uso uso = new Uso(-1);
        SIIL.Server.User opertaor = new SIIL.Server.User();
        Trace contexTrace = null;
        try 
        {
            instance.selectLast(connection);
            if(uso.selectRandom(connection).isFlag())
            {
                if(uso.download(connection).isFail())
                {
                    fail("Fallo la descarga de Uso");
                }
            }
            else
            {
                fail("Fallo mla seleccion del uso");
            }
            opertaor.selectRandom(connection);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        Boolean result;
        try 
        {
            contexTrace = new Trace(BACKWARD_BD, opertaor, "Captura de Orden de Servicio.");
            result = instance.upUso(dbserver,uso,contexTrace);
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
                //System.out.println("Se afectaron registros " + affected);
            } 
            catch (SQLException ex) 
            {
                //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
                fail(ex.getErrorCode() + " --> " + ex.getMessage());
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
        dbserver.close();
    }

    /**
     * Test of upTitem method, of class Resumov.
     */
    @Test
    public void testUpTitem()
    {
        System.out.println("upTitem");
    }

    /**
     * Test of emperejarV3 method, of class Resumov.
     */
    @Test
    public void testEmperejarV3() 
    {
        System.out.println("emperejarV3");
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
        
        Boolean result = null;
        try 
        {
            result = Resumov.emperejarV3(dbserver);
        }
        catch (SQLException ex) 
        {
            fail(ex.getErrorCode() + " --> " + ex.getMessage());
            return;
        }

        if(result && FL_COMMIT)
        {
            try 
            {
                dbserver.commit();
                //System.out.println("Se afectaron registros " + affected);
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
                fail(ex.getErrorCode() + " --> " + ex.getMessage());
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
        dbserver.close();
    }

    /**
     * Test of getUso method, of class Resumov.
     */
    @Test
    public void testGetUso_Database() 
    {
        System.out.println("getUso");
    }

    /**
     * Test of getCompany method, of class Resumov.
     */
    @Test
    public void testGetCompany_Database() 
    {
        System.out.println("getCompany");
    }

    /**
     * Test of getUso method, of class Resumov.
     */
    @Test
    public void testGetUso_0args() 
    {
        System.out.println("getUso");
    }

    /**
     * Test of getCompany method, of class Resumov.
     */
    @Test
    public void testGetCompany_0args() 
    {
        System.out.println("getCompany");
    }    

    /**
     * Test of removeNotThis method, of class Resumov.
     */
    @Test
    public void testRemoveNotThis() 
    {
        System.out.println("removeNotThis");
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
        
        Resumov resumov = new Resumov(-1);
        Resumov resumovtemp = new Resumov(-1);
        Flow flow = null;
        try 
        {
            resumov.selectLast(dbserver.getConnection());
            resumovtemp.selectRandom(dbserver);
            resumovtemp.downTitem(dbserver);
            flow = resumovtemp.getTitem();
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(ResumovTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        int ret = 0;
        try 
        {
            ret = resumov.removeNotThis(dbserver, flow);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(ResumovTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }

        if(ret > 1 && FL_COMMIT)
        {
            try 
            {
                dbserver.commit();
                //System.out.println("Se afectaron registros " + affected);
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
                fail(ex.getErrorCode() + " --> " + ex.getMessage());
                return;
            }
        }
        else if(ret > 1 && FL_COMMIT == false)
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
        dbserver.close();
    }

    /**
     * Test of removeAllNotThis method, of class Resumov.
     */
    @Test
    public void testRemoveAllNotThis() 
    {
        System.out.println("removeAllNotThis");
    }

    /**
     * Test of removeAllReferences method, of class Resumov.
     */
    @Test
    public void testRemoveAllReferences() 
    {
        System.out.println("removeAllReferences");
    }

    /**
     * Test of counterRefences method, of class Resumov.
     */
    @Test
    public void testCounterRefences() 
    {
        System.out.println("counterRefences");
    }

    /**
     * Test of removeAditamento method, of class Resumov.
     */
    @Test
    public void testRemoveAditamento() 
    {
        System.out.println("removeAditamento");
    }

    /**
     * Test of upAditamento method, of class Resumov.
     */
    @Test
    public void testUpAditamento() 
    {
        System.out.println("upAditamento");
    }

    /**
     * Test of downTitem method, of class Resumov.
     */
    @Test
    public void testDownTitem() 
    {
        System.out.println("downTitem");
    }
}
