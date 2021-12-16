
package process;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Azael Reyes
 */
public class ConvinatorTest 
{
    public ConvinatorTest() 
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
     * Test of print method, of class Convinator.
     */
    @Test
    public void testPrint() 
    {
        System.out.println("print");
    }

    /**
     * Test of convine method, of class Convinator.
     */
    @Test
    public void testConvine() 
    {
        System.out.println("convine");
        ArrayList<Double> numbers = new ArrayList<>();
        numbers.add(98.36);
        numbers.add(6.0);
        numbers.add(5.36);
        numbers.add(1.5);
        numbers.add(3.0);
        numbers.add(13.0);
        numbers.add(43.0);
        numbers.add(1.5);
        numbers.add(30.0);
        numbers.add(37.0);
        numbers.add(5.38);
        numbers.add(5.36);
        Convinator instance = new Convinator(numbers, 3);
        instance.generarCombinacion();        
        System.out.println("Calculo de combinacion Total : " + instance.calcularPermutacion());
        instance.print();        
    }

    /**
     * Test of getMaximoCombination method, of class Convinator.
     */
    @Test
    public void testGetMaximoCombination() 
    {
        System.out.println("getMaximoCombination");
    }

    /**
     * Test of getCombinaciones method, of class Convinator.
     */
    @Test
    public void testGetCombinaciones() 
    {
        System.out.println("getCombinaciones");
    }

    
}
