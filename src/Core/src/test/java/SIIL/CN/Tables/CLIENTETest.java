
package SIIL.CN.Tables;

import SIIL.CN.Engine.DBFRecord;
import SIIL.CN.Sucursal;
import java.io.IOException;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Azael Reyes
 */
public class CLIENTETest {
    
    public CLIENTETest() {
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

    @Test
    public void testImport() 
    {
        /*SIIL.CN.Tables.CLIENTE cliente = null;
        try 
        {
            cliente = new SIIL.CN.Tables.CLIENTE(Sucursal.BC_Tijuana);
        } 
        catch (IOException ex) 
        {
            //Logger.getLogger(TableTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        
        List<DBFRecord> list = cliente.readBlock(cliente.getRecordsCount() - 100, cliente.getRecordsCount());
        for(DBFRecord r : list)
        {
            System.out.println(r.getString(0).trim());
        }
        if(list.size() != 100) fail("Cantidad incorrecta de registos deven ser 100");*/
    }    
}
