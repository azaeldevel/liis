
package SIIL.service.quotation;

import SAT.CatalogoTest;
import SIIL.Server.Database;
import SIIL.Server.Person;
import core.bobeda.Business;
import java.io.IOException;
import java.sql.SQLException;
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
import session.Credential;

/**
 *
 * @author Azael Reyes
 */
public class SenderTest 
{
    private static boolean FL_COMMIT = true;
    
    public SenderTest() {
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
     * Test of generateTO method, of class Sender.
     */
    @Test
    public void testGenerateTO_Database_ServiceQuotation() throws Exception {
        System.out.println("generateTO");
    }

    /**
     * Test of addDocMensaje method, of class Sender.
     */
    @Test
    public void testAddDocMensaje() throws Exception {
        System.out.println("addDocMensaje");
    }

    /**
     * Test of addDocSubject method, of class Sender.
     */
    @Test
    public void testAddDocSubject() throws Exception {
        System.out.println("addDocSubject");
    }

    /**
     * Test of addDoc method, of class Sender.
     */
    @Test
    public void testAddDoc() throws Exception {
        System.out.println("addDoc");
    }

    /**
     * Test of confirmBuild method, of class Sender.
     */
    @Test
    public void testConfirmBuild() throws Exception {
        System.out.println("confirmBuild");
    }

    /**
     * Test of pedMensaje method, of class Sender.
     */
    @Test
    public void testPedMensaje() throws Exception {
        System.out.println("pedMensaje");
    }

    /**
     * Test of pedSubject method, of class Sender.
     */
    @Test
    public void testPedSubject() throws Exception {
        System.out.println("pedSubject");
    }

    /**
     * Test of pedBuild method, of class Sender.
     */
    @Test
    public void testPedBuild() throws Exception {
        System.out.println("pedBuild");
    }

    /**
     * Test of authoMensaje method, of class Sender.
     */
    @Test
    public void testAuthoMensaje() throws Exception {
        System.out.println("authoMensaje");
    }

    /**
     * Test of authoSubject method, of class Sender.
     */
    @Test
    public void testAuthoSubject() throws Exception {
        System.out.println("authoSubject");
    }

    /**
     * Test of authoBuild method, of class Sender.
     */
    @Test
    public void testAuthoBuild() throws Exception {
        System.out.println("authoBuild");
    }

    /**
     * Test of editMensaje method, of class Sender.
     */
    @Test
    public void testEditMensaje() throws Exception {
        System.out.println("editMensaje");
    }

    /**
     * Test of editSubject method, of class Sender.
     */
    @Test
    public void testEditSubject() throws Exception {
        System.out.println("editSubject");
    }

    /**
     * Test of Edit method, of class Sender.
     */
    @Test
    public void testEdit() throws Exception {
        System.out.println("Edit");
    }

    /**
     * Test of confirmMensaje method, of class Sender.
     */
    @Test
    public void testConfirmMensaje() throws Exception {
        System.out.println("confirmMensaje");
    }

    /**
     * Test of confirmSubject method, of class Sender.
     */
    @Test
    public void testConfirmSubject() throws Exception {
        System.out.println("confirmSubject");
    }

    /**
     * Test of confirm method, of class Sender.
     */
    @Test
    public void testConfirm() throws Exception {
        System.out.println("confirm");
    }

    /**
     * Test of cancel method, of class Sender.
     */
    @Test
    public void testCancel() throws Exception {
        System.out.println("cancel");
    }

    /**
     * Test of surtirMensaje method, of class Sender.
     */
    @Test
    public void testSurtirMensaje() throws Exception {
        System.out.println("surtirMensaje");
    }

    /**
     * Test of surtirSubject method, of class Sender.
     */
    @Test
    public void testSurtirSubject() throws Exception {
        System.out.println("surtirSubject");
    }

    /**
     * Test of surtir method, of class Sender.
     */
    @Test
    public void testSurtir() throws Exception {
        System.out.println("surtir");
    }

    /**
     * Test of termMensaje method, of class Sender.
     */
    @Test
    public void testTermMensaje() throws Exception {
        System.out.println("termMensaje");
    }

    /**
     * Test of termSubject method, of class Sender.
     */
    @Test
    public void testTermSubject() throws Exception {
        System.out.println("termSubject");
    }

    /**
     * Test of term method, of class Sender.
     */
    @Test
    public void testTerm() throws Exception {
        System.out.println("term");
    }

    /**
     * Test of generateTO method, of class Sender.
     */
    @Test
    public void testGenerateTO_3args() {
        System.out.println("generateTO");
    }

    /**
     * Test of generateTO method, of class Sender.
     */
    @Test
    public void testGenerateTO_Database_Person() {
        System.out.println("generateTO");
    }

    /**
     * Test of updateOwner method, of class Sender.
     */
    @Test
    public void testUpdateOwner() throws Exception {
        System.out.println("updateOwner");
    }

    /**
     * Test of generateCC method, of class Sender.
     */
    @Test
    public void testGenerateCC() {
        System.out.println("generateCC");
    }

    /**
     * Test of generateBCC method, of class Sender.
     */
    @Test
    public void testGenerateBCC() {
        System.out.println("generateBCC");
    }

    /**
     * Test of updateCompany method, of class Sender.
     */
    @Test
    public void testUpdateCompany() throws Exception {
        System.out.println("updateCompany");
    }

    /**
     * Test of updateFolio method, of class Sender.
     */
    @Test
    public void testUpdateFolio() throws Exception {
        System.out.println("updateFolio");
    }

    /**
     * Test of getRowsDescriptions method, of class Sender.
     */
    @Test
    public void testGetRowsDescriptions()
    {
        System.out.println("getRowsDescriptions");
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
        
        ServiceQuotation serviceQuotation = new ServiceQuotation();
        serviceQuotation.select(dbserver, 9602);
        Return ret = null;
        try 
        {
            serviceQuotation.download(dbserver);
            serviceQuotation.downQuotation(dbserver.getConnection());
            String str = Sender.getRowsDescriptions(dbserver, serviceQuotation);    
            System.out.println("Mail : " + str);
        }
        catch (SQLException ex) 
        {
            //Logger.getLogger(ServiceQuotationTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }

        if(FL_COMMIT)
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
        else if(FL_COMMIT == false)
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
                return;
            }
            //fail("Cant. Reg. incorrectos : ");
        }
        dbserver.close();
    }
    
}
