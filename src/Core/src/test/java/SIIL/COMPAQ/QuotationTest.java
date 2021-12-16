

package SIIL.COMPAQ;

import SAT.CatalogoTest;
import SIIL.Server.Database;
import SIIL.core.Office;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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

/**
 *
 * @author areyes
 */
public class QuotationTest {
    
    public QuotationTest() {
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
     * Test of setDocFolio method, of class Quotation.
     */
    @Test
    public void testSetDocFolio() throws Exception {
        System.out.println("setDocFolio");
    }

    /**
     * Test of getSerie method, of class Quotation.
     */
    @Test
    public void testGetSerie() {
        System.out.println("getSerie");
    }

    /**
     * Test of getNumber method, of class Quotation.
     */
    @Test
    public void testGetNumber() {
        System.out.println("getNumber");
    }

    /**
     * Test of search method, of class Quotation.
     */
    @Test
    public void testSearch() 
    {
        System.out.println("search");
        /*SIIL.core.config.Server serverConfig = new SIIL.core.config.Server();        
        Database dbserver = null;
        Database dbserverComercial = null;
        try 
        {
            serverConfig.loadFile(new java.io.File(".").getCanonicalPath());
            dbserver = new Database(serverConfig.getDataSource());
            dbserverComercial = new Database(serverConfig.getComercial());
        } 
        catch (ClassNotFoundException | SQLException | IOException | ParserConfigurationException | SAXException ex) 
        {
            Logger.getLogger(CatalogoTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        ArrayList<Quotation> lst = null;
        Office office = new Office(1);
        try 
        {
            office.download(dbserver.getConnection());
            lst = Quotation.search(dbserverComercial, office, 9139, 3);
        } 
        catch (Exception ex) 
        {
            //Logger.getLogger(QuotationTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        for(Quotation q : lst)
        {
            System.out.println(q.getSerie() + "-" + q.getNumber());
        }
        
        dbserver.close();*/
    }
    
}
