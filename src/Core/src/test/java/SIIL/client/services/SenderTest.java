
package SIIL.client.services;

import SAT.CatalogoTest;
import SIIL.Server.Database;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 *
 * @author Azael Reyes
 */
public class SenderTest 
{
    static boolean active = false;

    /**
     * Test of updateOwner method, of class Sender.
     */
    @Test
    public void testUpdateOwner() 
    {       
        System.out.println("UpdateOwner mail...");
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
        
        //Creando credencia
        System.out.println("Creando credencia...");
        String userName = "sistemas";
        char[] password = {'d','e','v'};
        session.User user = new session.User();
        user.setBD("bc.tj");
        if (userName.length() > 0) {
            user.setAlias(userName.toLowerCase());
        } else {
            JOptionPane.showMessageDialog(null,
                    "Indique el usuario",
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        if (password.length > 0) {
            user.setPassword(password);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Indique la contraseña",
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        session.Credential cred  = new session.Credential();
        if(cred.check(user,"alpha",serverConfig.getDataSource()))
        {
            ;
        }
        else
        {
            JOptionPane.showMessageDialog(null,
                    "usuario/Contraseña Incorrecto",
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        
        //Creando orden
        System.out.println("Creando orden...");
        SIIL.service.quotation.OrdenUpdate update = new SIIL.service.quotation.OrdenUpdate();
        Throwable th = update.fill(dbserver, cred, 251);          
        if(th != null)
        {
            System.err.println("Error al rellenar los datos de la orden : " + th.getMessage());
        }
        //Envian correo
        System.out.println("Envian correo...");
        SIIL.service.quotation.Sender senderMail = new SIIL.service.quotation.Sender();
        senderMail.init();
        try {
            if(!senderMail.updateOwner(dbserver, cred, update))
            {
                fail("Fallo al entregar el correo electronico.");
            }
            else
            {
                if(active)senderMail.send();
            }
        } catch (SQLException ex) {
            Logger.getLogger(SenderTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        dbserver.close();*/
    }
    
    /**
     * Test of updateCompany method, of class Sender.
     */
    @Test
    public void testUpdateCompany() 
    {       
        System.out.println("UpdateCompany mail...");
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
        
        //Creando credencia
        System.out.println("Creando credencia...");
        String userName = "sistemas";
        char[] password = {'d','e','v'};
        session.User user = new session.User();
        user.setBD("bc.tj");
        if (userName.length() > 0) {
            user.setAlias(userName.toLowerCase());
        } else {
            JOptionPane.showMessageDialog(null,
                    "Indique el usuario",
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        if (password.length > 0) {
            user.setPassword(password);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Indique la contraseña",
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        session.Credential cred  = new session.Credential();
        if(cred.check(user,"alpha",serverConfig.getDataSource()))
        {
            ;
        }
        else
        {
            JOptionPane.showMessageDialog(null,
                    "usuario/Contraseña Incorrecto",
                    "Error Externo",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        
        //Creando orden
        System.out.println("Creando orden...");
        SIIL.service.quotation.OrdenUpdate update = new SIIL.service.quotation.OrdenUpdate();
        Throwable th = update.fill(dbserver, cred, 2);
        if(th != null)
        {
            System.err.println("Error al rellenar los datos de la orden : " + th.getMessage());
            fail("No se rellenaron los datos : " + th.getMessage());
        }
        //Envian correo
        System.out.println("Envian correo...");
        SIIL.service.quotation.Sender senderMail = new SIIL.service.quotation.Sender();
        senderMail.init();
        try {
            senderMail.updateCompany(dbserver, cred, update);
        } catch (SQLException ex) {
            Logger.getLogger(SenderTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(active)senderMail.send();
        dbserver.close();*/
    }
    
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
     * Test of addDoc method, of class Sender.
     */
    @Test
    public void testAddDoc() {
        System.out.println("addDoc");
    }

    /**
     * Test of confirmBuild method, of class Sender.
     */
    @Test
    public void testConfirmBuild_3args() {
        System.out.println("confirmBuild");
    }

    /**
     * Test of pedBuild method, of class Sender.
     */
    @Test
    public void testPedBuild_4args() {
        System.out.println("pedBuild");
    }

    /**
     * Test of authoBuild method, of class Sender.
     */
    @Test
    public void testAuthoBuild_3args() {
        System.out.println("authoBuild");
    }

    /**
     * Test of Edit method, of class Sender.
     */
    @Test
    public void testEdit_3args() {
        System.out.println("Edit");
    }

    /**
     * Test of confirm method, of class Sender.
     */
    @Test
    public void testConfirm_3args() {
        System.out.println("confirm");
    }

    /**
     * Test of cancel method, of class Sender.
     */
    @Test
    public void testCancel_3args() {
        System.out.println("cancel");
    }

    /**
     * Test of surtir method, of class Sender.
     */
    @Test
    public void testSurtir_4args() {
        System.out.println("surtir");
    }

    /**
     * Test of term method, of class Sender.
     */
    @Test
    public void testTerm_3args() {
        System.out.println("term");
    }

    /**
     * Test of confirmBuild method, of class Sender.
     */
    @Test
    public void testConfirmBuild_6args() throws Exception {
        System.out.println("confirmBuild");
    }

    /**
     * Test of pedBuild method, of class Sender.
     */
    @Test
    public void testPedBuild_7args() throws Exception {
        System.out.println("pedBuild");
    }

    /**
     * Test of authoBuild method, of class Sender.
     */
    @Test
    public void testAuthoBuild_6args() throws Exception {
        System.out.println("authoBuild");
    }

    /**
     * Test of Edit method, of class Sender.
     */
    @Test
    public void testEdit_6args() throws Exception {
        System.out.println("Edit");
    }

    /**
     * Test of confirm method, of class Sender.
     */
    @Test
    public void testConfirm_6args() throws Exception {
        System.out.println("confirm");
    }

    /**
     * Test of cancel method, of class Sender.
     */
    @Test
    public void testCancel_6args() throws Exception {
        System.out.println("cancel");
    }

    /**
     * Test of surtir method, of class Sender.
     */
    @Test
    public void testSurtir_7args() throws Exception {
        System.out.println("surtir");
    }
    
    /**
     * Test of term method, of class Sender.
     */
    @Test
    public void testTerm_6args() throws Exception {
        System.out.println("term");
    }

    
}
