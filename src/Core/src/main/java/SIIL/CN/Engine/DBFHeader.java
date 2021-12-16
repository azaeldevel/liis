
package SIIL.CN.Engine;

import java.nio.MappedByteBuffer;

/**
 *
 * @author Azael
 */
public class DBFHeader 
{
    static final byte HEADER_TERMINATOR = 0x0d;    
    private MappedByteBuffer bufferFile;
        
    public DBFHeader()
    {
        
    }

    DBFHeader(MappedByteBuffer bufferFile)
    {
        this.bufferFile = bufferFile;
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
    
    /* 0     */
    public byte getSignature()
    {
        return bufferFile.get(0);
    }   
    
    /* 0     */
    public String getSignatureString()
    {
        byte sig = bufferFile.get(0);
        
        if(0x02 == sig)
        {
            return "FoxBASE";
        }
        else if(0x03 == sig)
        {
            return "FoxBASE+/Dbase III plus, no memo";
        }
        else if(0x30 == sig)
        {
            return "Visual FoxPro";
        }
        else if(0x31 == sig)
        {
            return "Visual FoxPro, autoincrement enabled";
        }
        else if(0x32 == sig)
        {
            return "Visual FoxPro, Varchar, Varbinary, or Blob-enabled";
        }
        else if(0x43 == sig)
        {
            return "dBASE IV SQL table files, no memo";
        }
        else if(0x63 == sig)
        {
            return "dBASE IV SQL system files, no memo";
        }
        else if(0x83 == sig)
        {
            return "dBASE IV with memo";
        }
        else if(0xCB == sig)
        {
            return "dBASE IV SQL table files, with memo";
        }
        else if(0xF5 == sig)
        {
            return "FoxPro 2.x (or earlier) with memo";
        }
        else if(0xFB == sig)
        {
            return "FoxBASE";
        }
        else
        {
            return "###Unkow, no soported###";
        }
    }
    
    /* 1     */
    public byte getYear()
    {
        return bufferFile.get(1);
    }
    
    /* 2     */
    public byte getMonth()
    {
        return bufferFile.get(2);
    }
    
    /* 3     */
    public byte getDay()
    {
        return bufferFile.get(3);
    }
    
    /* 4-7   */
    public int getRecordsCount()
    {
        byte[] bufftem = new byte[4];
        bufftem[0] = bufferFile.get(4);
        bufftem[1] = bufferFile.get(5);
        bufftem[2] = bufferFile.get(6);
        bufftem[3] = bufferFile.get(7);
        int number = covertToInt32(bufftem);
        return number;
    }
    
    /* 8-9   */
    public short getRecordFirst()
    {
        int low = bufferFile.get(8) & 0xff;
        int high = bufferFile.get(9);
        return (short) (high << 8 | low);
    }
    /* 10-11 */
    public short getRecordLength()
    {
        int low = bufferFile.get(10) & 0xff;
        int high = bufferFile.get(11);
        return (short) (high << 8 | low);        
    }
    /* 12-13 */
    public short getReserv1()
    {
        int low = bufferFile.get(12) & 0xff;
        int high = bufferFile.get(13);
        return (short) (high << 8 | low); 
    }
    /* 14    */
    public byte getIncompleteTransaction()
    {
        return bufferFile.get(14);
    }
    /* 15    */
    public byte getEncryptionFlag()
    {
        return bufferFile.get(15);
    }
    /* 16-19 */
    public int getFreeRecordThread()
    {
        byte[] bufftem = new byte[4];
        bufftem[0] = bufferFile.get(16);
        bufftem[1] = bufferFile.get(17);
        bufftem[2] = bufferFile.get(18);
        bufftem[3] = bufferFile.get(19);
        return covertToInt32(bufftem);
    }
    /* 20-23 */
    public int getReserv2()
    {
        byte[] bufftem = new byte[4];
        bufftem[0] = bufferFile.get(20);
        bufftem[1] = bufferFile.get(21);
        bufftem[2] = bufferFile.get(22);
        bufftem[3] = bufferFile.get(23);
        return covertToInt32(bufftem);
    }
    /* 24-27 */
    public int getReserv3()
    {
        byte[] bufftem = new byte[4];
        bufftem[0] = bufferFile.get(24);
        bufftem[1] = bufferFile.get(25);
        bufftem[2] = bufferFile.get(26);
        bufftem[3] = bufferFile.get(27);
        return covertToInt32(bufftem);
    }
    /* 28    */
    public byte getMDXFlag()
    {
        return bufferFile.get(28);
    }
    /* 29    */
    public byte getLanguageDriver()
    {
        return bufferFile.get(29);
    }
    /* 30-31 */
    public short getReserv4()
    {
        int low = bufferFile.get(30) & 0xff;
        int high = bufferFile.get(31);
        return (short) (high << 8 | low); 
    } 
}
