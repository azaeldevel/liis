
package SIIL.CN.Engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
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
public class FPTHeaderTest {
    
    public FPTHeaderTest() {
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
     * Test of getBuffer method, of class FPTHeader.
     */
    @Test
    public void testGetBuffer() 
    {
        System.out.println("getBuffer");
    }

    /**
     * Test of getNextFreeBlock method, of class FPTHeader.
     */
    @Test
    public void testGetNextFreeBlock() 
    {
        System.out.println("getNextFreeBlock");
    }

    /**
     * Test of getSizeBlock method, of class FPTHeader.
     */
    @Test
    public void testGetSizeBlock() 
    {
        System.out.println("getSizeBlock");
    }

    /**
     * Test of getRecord method, of class FPTHeader.
     */
    @Test
    public void testGetRecord() 
    {
        System.out.println("getRecord");
    }
    
    @Test
    public void testOpen() throws FileNotFoundException, IOException 
    {
        System.out.println("Open");
        /*File fileFPT = new File("C:\\CN60\\COTIZA_R.fpt");
        FileChannel fileChannel = new RandomAccessFile(fileFPT, "r").getChannel();
        MappedByteBuffer fptFile = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());  
        FPTHeader fptHeader = new FPTHeader(fptFile);
        System.out.println("SizeBlock : " + fptHeader.getSizeBlock());
        System.out.println("Next Free Block :" + fptHeader.getNextFreeBlock());
        int i = 100;
        FPTRecord rec = fptHeader.getRecord(i);        
        System.out.println("Record Lenght : " + rec.getLength());*/
        //System.out.println("Block " + i + ":" + rec.getText());
    }
}
