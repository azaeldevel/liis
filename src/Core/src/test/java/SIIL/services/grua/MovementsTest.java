
package SIIL.services.grua;

import SAT.CatalogoTest;
import SIIL.Server.Company;
import SIIL.Server.Database;
import SIIL.core.Office;
import core.PlainTitem;
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
import stock.Flow;
import stock.ItemTest;

/**
 *
 * @author Azael Reyes
 */
public class MovementsTest 
{
    private static final boolean FL_COMMIT = true;
    
    public MovementsTest() {
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
     * Test of insert method, of class Movements.
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
        
        
        Office office = new Office(-1);
        Uso uso = new Uso(-1);
        Company company = new Company();
        Tipo tmov = new Tipo(-1);        
        
        Movements mov = new Movements(-1);
        List<Flow> hesquis = new ArrayList<>();
        Flow forklift = new Flow(-1);
        hesquis.add(forklift);
        Remision sa = new Remision(-1);
        Boolean ret;
        try 
        {
            ret = forklift.selectForkliftRandom(dbserver);
            forklift.downItem(dbserver);
            forklift.getItem().downMake(dbserver.getConnection());
            forklift.getItem().downModel(dbserver);
            forklift.getItem().downNumber(dbserver.getConnection());
            forklift.downSerie(dbserver);
            tmov.selectRandom(dbserver.getConnection());
            tmov.download(dbserver.getConnection());
            uso.selectRandom(dbserver.getConnection());
            uso.download(dbserver.getConnection());
            company.selectRandom(dbserver);
            company.complete(dbserver);
            
            office.selectRandom(dbserver.getConnection());
            Throwable download = office.download(dbserver.getConnection());
            uso.selectRandom(dbserver.getConnection());
            uso.download(dbserver.getConnection());
            company.selectRandom(dbserver);
        } 
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getErrorCode() + " --> " + ex.getMessage());
            return;
        }
        Return result = null;
        Return<Integer> retUso = null;
        Return<Integer> rettmov = null;
        Return<Integer> retFirma = null;
        Return<Integer> retNote = null;
        Return<Integer> retSA = null;
        Return<Integer> retOwner = null;
        Return<Integer> retCompany = null;
        try 
        {
            result = mov.insert(dbserver, office,date,sdf.format(date),hesquis,new Flow(-1));
            retUso = mov.upUso(dbserver.getConnection(),uso);
            rettmov = mov.upTMov(dbserver.getConnection(),tmov);
            retFirma = mov.upFirma(dbserver.getConnection(),"Firma Prueba.");
            retNote = mov.upNote(dbserver.getConnection(),"Nota de Prueba.");
            retSA = mov.upSA(dbserver.getConnection(),sa);
            retOwner = mov.upOwner(dbserver.getConnection(),Captura.Owner.SIIL);
            retCompany = mov.upCompany(dbserver.getConnection(),company);
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
                insertedID = (int)result.getParam();
                break;
            case FAIL:
                fail(result.getMessage());
                break;
            default:
                fail("Resultado desconocido.");
                break;
        }
        if(retUso.isFlag() == false | rettmov.isFlag() == false | retCompany.isFlag() == false)
        {
            try 
            {
                dbserver.rollback();
            } 
            catch (SQLException ex) 
            {
                fail(ex.getMessage());
                return;
            }
            fail("Algunos cambios adjacente fallaron.");
            return;
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
                fail("FallÃ³ RollBack " + ex.getMessage());
                return;
            }
            fail("Cant. Reg. incorrectos : " + insertedID);
        }
        dbserver.close();
    }

    /**
     * Test of getHorometro method, of class Movements.
     */
    @Test
    public void testGetHorometro() {
        System.out.println("getHorometro");
    }

    /**
     * Test of getOffice method, of class Movements.
     */
    @Test
    public void testGetOffice() {
        System.out.println("getOffice");
    }

    /**
     * Test of getCreateTime method, of class Movements.
     */
    @Test
    public void testGetCreateTime() {
        System.out.println("getCreateTime");
    }

    /**
     * Test of getCompany method, of class Movements.
     */
    @Test
    public void testGetCompany() {
        System.out.println("getCompany");
    }

    /**
     * Test of getCreator method, of class Movements.
     */
    @Test
    public void testGetCreator() {
        System.out.println("getCreator");
    }

    /**
     * Test of getMovtitems method, of class Movements.
     */
    @Test
    public void testGetMovtitems() {
        System.out.println("getMovtitems");
    }

    /**
     * Test of insert method, of class Movements.
     */
    @Test
    public void testInsert_5args_1() 
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
        catch (SQLException | IOException | ParserConfigurationException | SAXException ex) 
        {
            Logger.getLogger(CatalogoTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MovementsTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("yymmddhhmmss");
        Random r = new Random();
        int Low = 1;
        int High = 100;
        int Result = r.nextInt(High - Low);  
        
        Office office = new Office(-1);
        Uso uso = new Uso(-1);
        Company company = new Company();
        Tipo tmov = new Tipo(-1);        
        Remision sa = new Remision(-1);
        
        Movements mov = new Movements(-1);
        List<PlainTitem> hesquis = new ArrayList<>();
        Boolean ret;
        try 
        {
            PlainTitem p1 = new PlainTitem();
            p1.number = "number1";
            p1.make = "make1";
            p1.model = "model1";
            p1.serie = "serie1";
            hesquis.add(p1);
            PlainTitem p2 = new PlainTitem();
            p2.number = "number2";
            p2.make = "make2";
            p2.model = "model2";
            p2.serie = "serie2";
            hesquis.add(p2);
            tmov.selectRandom(dbserver.getConnection());
            tmov.download(dbserver.getConnection());
            uso.selectRandom(dbserver.getConnection());
            uso.download(dbserver.getConnection());
            company.selectRandom(dbserver);
            company.complete(dbserver);
            
            office.selectRandom(dbserver.getConnection());
            Throwable download = office.download(dbserver.getConnection());
            uso.selectRandom(dbserver.getConnection());
            uso.download(dbserver.getConnection());
            company.selectRandom(dbserver);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(AllocatedTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getErrorCode() + " --> " + ex.getMessage());
            return;
        }
        Boolean result = null;
        Return<Integer> retUso = null;
        Return<Integer> rettmov = null;
        Return<Integer> retFirma = null;
        Return<Integer> retNote = null;
        Return<Integer> retSA = null;
        Return<Integer> retOwner = null;
        Return<Integer> retCompany = null;
        try 
        {
            result = mov.insert(dbserver, office,date,sdf.format(date),hesquis, new PlainTitem());
            retUso = mov.upUso(dbserver.getConnection(),uso);
            rettmov = mov.upTMov(dbserver.getConnection(),tmov);
            retFirma = mov.upFirma(dbserver.getConnection(),"Firma Prueba.");
            retNote = mov.upNote(dbserver.getConnection(),"Nota de Prueba.");
            retSA = mov.upSA(dbserver.getConnection(),sa);
            retOwner = mov.upOwner(dbserver.getConnection(),Captura.Owner.SIIL);
            retCompany = mov.upCompany(dbserver.getConnection(),company);
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
                //Logger.getLogger(ItemTest.class.getName()).log(Level.SEVERE, null, ex);
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
    }

    /**
     * Test of selectLast method, of class Movements.
     */
    @Test
    public void testSelectLast()
    {
        System.out.println("selectLast");
    }

    /**
     * Test of selectRandom method, of class Movements.
     */
    @Test
    public void testSelectRandom() 
    {
        System.out.println("selectRandom");
    }

    /**
     * Test of insert method, of class Movements.
     */
    @Test
    public void testInsert_5args_2() 
    {
        System.out.println("insert");        
    }

    /**
     * Test of getFolio method, of class Movements.
     */
    @Test
    public void testGetFolio() 
    {
        System.out.println("getFolio");
    }

    /**
     * Test of getFhMov method, of class Movements.
     */
    @Test
    public void testGetFhMov() 
    {
        System.out.println("getFhMov");
    }

    /**
     * Test of getTmov method, of class Movements.
     */
    @Test
    public void testGetTmov() 
    {
        System.out.println("getTmov");        
    }

    /**
     * Test of getUso method, of class Movements.
     */
    @Test
    public void testGetUso() 
    {
        System.out.println("getUso");
    }

    /**
     * Test of getFirma method, of class Movements.
     */
    @Test
    public void testGetFirma() 
    {
        System.out.println("getFirma");
    }

    /**
     * Test of getNote method, of class Movements.
     */
    @Test
    public void testGetNote() 
    {
        System.out.println("getNote");
    }

    /**
     * Test of getSa method, of class Movements.
     */
    @Test
    public void testGetSa() 
    {
        System.out.println("getSa");
    }

    /**
     * Test of getOwner method, of class Movements.
     */
    @Test
    public void testGetOwner() 
    {
        System.out.println("getOwner");
    }

    /**
     * Test of getID method, of class Movements.
     */
    @Test
    public void testGetID() 
    {
        System.out.println("getID");
    }

    /**
     * Test of upUso method, of class Movements.
     */
    @Test
    public void testUpUso() 
    {
        System.out.println("upUso");
    }

    /**
     * Test of upTMov method, of class Movements.
     */
    @Test
    public void testUpTMov() 
    {
        System.out.println("upTMov");
    }

    /**
     * Test of upFirma method, of class Movements.
     */
    @Test
    public void testUpFirma() 
    {
        System.out.println("upFirma");
    }

    /**
     * Test of upNote method, of class Movements.
     */
    @Test
    public void testUpNote() 
    {
        System.out.println("upNote");
    }

    /**
     * Test of upSA method, of class Movements.
     */
    @Test
    public void testUpSA() 
    {
        System.out.println("upSA");        
    }

    /**
     * Test of upOwner method, of class Movements.
     */
    @Test
    public void testUpOwner() 
    {
        System.out.println("upOwner");
    }

    /**
     * Test of upCompany method, of class Movements.
     */
    @Test
    public void testUpCompany() 
    {
        System.out.println("upCompany");
    }
    
}
