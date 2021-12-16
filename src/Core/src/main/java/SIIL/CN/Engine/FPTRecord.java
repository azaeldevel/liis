
package SIIL.CN.Engine;

import java.nio.MappedByteBuffer;

/**
 *
 * @author Azael Reyes
 */
public class FPTRecord 
{
    enum Signature 
    {
        PICTURE,
        TEXT        
    }    
    private FPTHeader header;
    private int offset;
    
    public FPTRecord(FPTHeader header,int offset)
    {
        this.header = header;
        this.offset = offset;
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
    
        
    public int getLength()
    {
        byte[] bufftem = new byte[4];
        bufftem[0] = header.getBuffer().get(offset + 4);
        bufftem[1] = header.getBuffer().get(offset + 5);
        bufftem[2] = header.getBuffer().get(offset + 6);
        bufftem[3] = header.getBuffer().get(offset + 7);
        int number = covertToInt32(bufftem);
        return number;
    }
    
    public String getText()
    {
        MappedByteBuffer bufferFile = header.getBuffer();
        int lenght = getLength();
        byte[] tembuff = new byte[lenght + 1];
        bufferFile.position(offset + 8);
        bufferFile.get(tembuff, 0, lenght);
        String buff = new String(tembuff);
        return buff;
    }
}
