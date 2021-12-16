
package SIIL.CN.Engine;

import java.nio.MappedByteBuffer;

/**
 *
 * @author Azael Reyes
 */
public class FPTHeader 
{
    static final short HEADER_SIZE = 512; 
    private MappedByteBuffer bufferFile;
    
    public MappedByteBuffer getBuffer()
    {
        return bufferFile;
    }
    
    FPTHeader(MappedByteBuffer bufferFile)
    {
        this.bufferFile = bufferFile;
    }
    
    
    public short getSizeBlock()
    {
        int low = bufferFile.get(7) & 0xff;
        int high = bufferFile.get(6);
        return (short) (high << 8 | low);        
    }
      
    public FPTRecord getRecord(int i)
    {
        int offsetRecord = 512 + (getSizeBlock() * i);
        return new FPTRecord(this, offsetRecord);
    }
    
    private int covertToInt32(byte[] bufftem) 
    {
        int number = 0;
        for (int shiftBy = 0 , i = 0; shiftBy < 32; shiftBy += 8, i++) 
        {
            number |= (bufftem[i] & 0xff) << shiftBy;
        }      
        return number;
    }
    
    public int getNextFreeBlock()
    {
        byte[] bufftem = new byte[4];
        bufftem[0] = bufferFile.get(0);
        bufftem[1] = bufferFile.get(1);
        bufftem[2] = bufferFile.get(2);
        bufftem[3] = bufferFile.get(3);
        int number = covertToInt32(bufftem);
        return number;
    }
    
}
