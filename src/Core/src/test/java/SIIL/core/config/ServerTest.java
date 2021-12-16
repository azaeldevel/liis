
package SIIL.core.config;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class ServerTest 
{
    
    Server server;
    
    public ServerTest()
    {       
        server = new Server();
        try 
        {
            server.loadFile(new java.io.File(".").getCanonicalPath());
        }
        catch (IOException | ParserConfigurationException | SAXException ex) 
        {
            //Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
    }
    
    @BeforeClass
    public static void setUpClass() 
    {
    }
    
    @AfterClass
    public static void tearDownClass() 
    {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getDataSource method, of class Server.
     */
    @Test
    public void testGetDataSource() 
    {        
        assert(server.getDataSource() != null);
        //System.out.println("MySQL Data Source Server name: " + server.getDataSource().getServerName());
    }

    /**
     * Test of getSpeechPort method, of class Server.
     */
    @Test
    public void testGetSpeechPort() 
    {
        assert(server.getSpeechPort() != 0);
        //System.out.println("Speech port: " + server.getSpeechPort());
    }

    /**
     * Test of getSpeechHost method, of class Server.
     */
    @Test
    public void testGetSpeechHost() 
    {
        assert(server.getSpeechHost() != null);
        //System.out.println("Speech Host: " + server.getSpeechHost());        
    }    

    /**
     * Test of isFlag method, of class Server.
     */
    @Test
    public void testIsFlag() {
        System.out.println("isFlag");
    }

    /**
     * Test of getFtpHost method, of class Server.
     */
    @Test
    public void testGetFtpHost() {
        System.out.println("getFtpHost");
    }

    /**
     * Test of getFtpPort method, of class Server.
     */
    @Test
    public void testGetFtpPort() {
        System.out.println("getFtpPort");
    }

    /**
     * Test of getFtpUser method, of class Server.
     */
    @Test
    public void testGetFtpUser() {
        System.out.println("getFtpUser");
    }

    /**
     * Test of getFtpPasss method, of class Server.
     */
    @Test
    public void testGetFtpPasss() {
        System.out.println("getFtpPasss");
    }

    /**
     * Test of getFtpBase method, of class Server.
     */
    @Test
    public void testGetFtpBase() {
        System.out.println("getFtpBase");
    }

    /**
     * Test of getWorkspace method, of class Server.
     */
    @Test
    public void testGetWorkspace() {
        System.out.println("getWorkspace");
    }

    /**
     * Test of getTokenDiversa method, of class Server.
     */
    @Test
    public void testGetTokenDiversa() {
        System.out.println("getTokenDiversa");
    }

    /**
     * Test of getPrefiFileDiversa method, of class Server.
     */
    @Test
    public void testGetPrefiFileDiversa() {
        System.out.println("getPrefiFileDiversa");
    }

    /**
     * Test of getPasswordDiversa method, of class Server.
     */
    @Test
    public void testGetPasswordDiversa() {
        System.out.println("getPasswordDiversa");
    }

    /**
     * Test of getStampDiversa32 method, of class Server.
     */
    @Test
    public void testGetStampDiversa32() {
        System.out.println("getStampDiversa32");
    }

    /**
     * Test of getCancelDiversa32 method, of class Server.
     */
    @Test
    public void testGetCancelDiversa32() {
        System.out.println("getCancelDiversa32");
    }

    /**
     * Test of getStampDiversa33 method, of class Server.
     */
    @Test
    public void testGetStampDiversa33() {
        System.out.println("getStampDiversa33");
    }

    /**
     * Test of getCancelDiversa33 method, of class Server.
     */
    @Test
    public void testGetCancelDiversa33() {
        System.out.println("getCancelDiversa33");
    }
}
