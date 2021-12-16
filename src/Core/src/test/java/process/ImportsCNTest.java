
package process;

import SAT.CatalogoTest;
import SIIL.Server.Database;
import SIIL.core.Office;
import SIIL.service.quotation.OrdenCotizada;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import session.Credential;
import session.User;
import stock.ItemTest;

/**
 *
 * @author Azael Reyes
 */
public class ImportsCNTest 
{
    private static final boolean FL_COMMIT = true;
    
    public ImportsCNTest() {
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
    public void tearDown() 
    {
    }

    /**
     * Test of getCN60Descripcion method, of class ImportsCN.
     */
    @Test
    public void testGetCN60Descripcion() {
        System.out.println("getCN60Descripcion");
    }

    /**
     * Test of importCNQuotation method, of class ImportsCN.
     */
    @Test
    public void testImportCNQuotation() 
    {
        System.out.println("importCNQuotation");
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
        
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddhhmmss");
        Random r = new Random();
        int Low = 1;
        int High = 99;
        int Result = r.nextInt(High-Low) + Low;   
        
        Return<Integer> retLas = null;
        Return<Integer> retImport = null;
        Return<Integer> retFolio = null;
        OrdenCotizada orden = new OrdenCotizada();
        orden.setServerDB(dbserver);
        Office office = new Office(dbserver, 1);
        ImportsCN omp = new ImportsCN();
        boolean retimport = false;
        try 
        {
            User user = new User();
            user.selectRandom(dbserver.getConnection());
            Credential cred = new Credential();
            cred.setUser(user);
            orden.setCredential(cred);
            retLas = orden.selectRandom(dbserver.getConnection(),office);
            retFolio = orden.downFolio(dbserver.getConnection());
            orden.downSerie(dbserver.getConnection());
            retimport = omp.importCNQuotation(orden, dbserver);
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            fail(ex.getErrorCode() + " --> " + ex.getMessage());
            return;
        }
        catch (IOException ex) 
        {
            Logger.getLogger(ImportsCNTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(retimport && FL_COMMIT)
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
        else if(retimport && FL_COMMIT == false)
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
            //fail("Cant. Reg. incorrectos : " + insertedID);
        }
        dbserver.close();*/
    }

    /**
     * Test of importClients method, of class ImportsCN.
     */
    @Test
    public void testImportClients() 
    {
        System.out.println("importClients");
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
        
        ArrayList<String> arr = new ArrayList<>();
        ImportsCN importcn = new ImportsCN();
        if(importcn.importClients(dbserver,arr,500)) 
        {
            if(FL_COMMIT)
            {
                try 
                {
                    dbserver.commit();
                }
                catch (SQLException ex) 
                {
                    //Logger.getLogger(ImportsCNTest.class.getName()).log(Level.SEVERE, null, ex);
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
                    //Logger.getLogger(ImportsCNTest.class.getName()).log(Level.SEVERE, null, ex);
                }
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
                //Logger.getLogger(ImportsCNTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            fail("Fallo la importación ");
        }
        
        for(String lg : arr)
        {
            System.out.println(lg);
        }*/
    }

    /**
     * Test of getCountCNQuote method, of class ImportsCN.
     */
    @Test
    public void testGetCountCNQuote() 
    {
        System.out.println("getCountCNQuote");
    }

    /**
     * Test of importLinkTitems method, of class ImportsCN.
     */
    @Test
    public void testImportLinkTitems() 
    {
        System.out.println("importLinkTitems");
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
        
        ArrayList<String> arr = new ArrayList<>();
        ImportsCN importcn = new ImportsCN();
        boolean fl;
        try 
        {
            fl = importcn.importLinkTitems(dbserver,arr,80);
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(ImportsCNTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        if(fl) 
        {
            if(FL_COMMIT)
            {
                try 
                {
                    dbserver.commit();
                }
                catch (SQLException ex) 
                {
                    Logger.getLogger(ImportsCNTest.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(ImportsCNTest.class.getName()).log(Level.SEVERE, null, ex);
                }
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
                Logger.getLogger(ImportsCNTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            fail("Fallo la importacion");
        }
        
        for(String lg : arr)
        {
            System.out.println(lg);
        }*/
    }

    /**
     * Test of importProvider method, of class ImportsCN.
     */
    @Test
    public void testImportProvider() 
    {
        System.out.println("importProvider");
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
        
        ArrayList<String> arr = new ArrayList<>();
        ImportsCN importcn = new ImportsCN();
        if(importcn.importProvider(dbserver,arr,80)) 
        { 
            if(FL_COMMIT)
            {
                try 
                {
                    dbserver.commit();
                }
                catch (SQLException ex) 
                {
                    Logger.getLogger(ImportsCNTest.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(ImportsCNTest.class.getName()).log(Level.SEVERE, null, ex);
                }
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
                Logger.getLogger(ImportsCNTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            fail("Falló la importacion " );
        }
        
        for(String lg : arr)
        {
            System.out.println(lg);
        }*/
    }

    /**
     * Test of generateTitemFlow method, of class ImportsCN.
     */
    @Test
    public void testGenerateTitemFlow() 
    {
        System.out.println("generateTitemFlow");
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
        
        ArrayList<String> arr = new ArrayList<>();
        ImportsCN importcn = new ImportsCN();
        if(importcn.generateTitemFlow(dbserver,arr,80))
        {
            if(FL_COMMIT)
            {
                try 
                {
                    dbserver.commit();
                }
                catch (SQLException ex) 
                {
                    Logger.getLogger(ImportsCNTest.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(ImportsCNTest.class.getName()).log(Level.SEVERE, null, ex);
                }
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
                Logger.getLogger(ImportsCNTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            fail("Fallo la importacion");
        }
        
        for(String lg : arr)
        {
            System.out.println(lg);
        }*/
    }
}
