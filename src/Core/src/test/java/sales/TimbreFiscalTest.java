
package sales;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Azael Reyes
 */
public class TimbreFiscalTest 
{
    
    
    public TimbreFiscalTest() 
    {
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
    public void setUp() 
    {
    }
    
    @After
    public void tearDown() 
    {
    }

    /**
     * Test of cancelar method, of class TimbreFiscal.
     */
    @Test
    public void testCancelar() throws Exception 
    {
        System.out.println("cancelar");
    }

    /**
     * Test of timbrar method, of class TimbreFiscal.
     */
    @Test
    public void testTimbrar()
    {
        System.out.println("timbrar");
        /*
        File file = new File("C:\\Users\\Azael Reyes\\Desktop\\Kit_CFDI3.3_2\\Kit_CFDI3.3\\Ejemplos\\XML\\Timbrado\\Archivo_Entrada_Timbre.xml");
        TimbreFiscal timbrar = new TimbreFiscal();
        try 
        {
            if(!timbrar.timbrar(file,TimbreFiscal.XML.v33)) 
            {
                fail(timbrar.getResponseCode() + " : " +  timbrar.getResponseMessage() + " -- " + timbrar.getStrURLStamp() + " TOKEN : " + timbrar.getToken());
                return;
            }
        }
        catch (MalformedURLException ex) 
        {
            //Logger.getLogger(InvoiceTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(timbrar.getResponseCode() + " : " +  timbrar.getResponseMessage() + " -- " + timbrar.getStrURLStamp() + " TOKEN : " + timbrar.getToken());
            return;
        }
        catch (IOException ex) 
        {
            //Logger.getLogger(InvoiceTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(timbrar.getResponseCode() + " : " +  timbrar.getResponseMessage() + " -- " + timbrar.getStrURLStamp() + " TOKEN : " + timbrar.getToken());            
            return;
        }
        */      
    }

    /**
     * Test of getResponseCode method, of class TimbreFiscal.
     */
    @Test
    public void testGetResponseCode() 
    {
        System.out.println("getResponseCode");
    }

    /**
     * Test of getResponseMessage method, of class TimbreFiscal.
     */
    @Test
    public void testGetResponseMessage() 
    {
        System.out.println("getResponseMessage");
    }
    
}
