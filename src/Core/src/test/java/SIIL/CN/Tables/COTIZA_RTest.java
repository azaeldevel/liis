
package SIIL.CN.Tables;

import SIIL.CN.Engine.Clause;
import SIIL.CN.Engine.Operator;
import SIIL.CN.Engine.DBFRecord;
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
public class COTIZA_RTest {
    
    public COTIZA_RTest() {
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
        /*COTIZA_R renglones = null;
        try 
        {
            renglones = new COTIZA_R(Sucursal.BC_Tijuana,true,true);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(COTIZA_RTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        List<DBFRecord> list = renglones.readWhere(new Clause(0,Operator.TRIMEQUAL,"70709"),0,'R');
        //System.out.println("Se encontraron : " + list.size());
        if(list.size() != 19) fail("Deve de encontrar exactamente 17 renglones pero encontro " + list.size());        
        for(DBFRecord rec : list)
        {
            System.out.println(rec.getString(0) + "|-|" + rec.getString(1)  + "|-|" + rec.getString(2) + "|-|" + rec.getString(12) + "|-|" );
            //System.out.println(rec.getString());
        }*/
    }    
}
