
package SIIL.CN.Tables;

import SIIL.CN.Engine.Clause;
import SIIL.CN.Engine.DBFRecord;
import SIIL.CN.Engine.Operator;
import SIIL.CN.Sucursal;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class COMPRASTest 
{
    
    public COMPRASTest() {
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
    public void testSomeMethod() 
    {
        /*COMPRAS renglones = null;
        try 
        {
            renglones = new COMPRAS(Sucursal.BC_Tijuana,true,true);
        }
        catch (IOException ex) 
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        
        List<DBFRecord> list = renglones.readWhere(new Clause(1,Operator.TRIMEQUAL,"727596"),0,'R');  
        if(list == null || list.size() == 0) fail("No se encontro registro.");
        for(DBFRecord rec : list)
        {
            System.out.println(rec.getString(0) + " |-| " + rec.getString(1) + " |-| " + rec.getString(2) + " |-| " + rec.getString(7) + " |-| " + rec.getString(8) + " |-| " + rec.getString(9));
        }*/
    }
    
}
