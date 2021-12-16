
package SIIL.CN.Engine;

import java.nio.MappedByteBuffer;

/**
 *
 * @author Azael Reyes
 */
public class DBFRecord 
{
    private final Table table;
    private final int offset;
    
    private int covertToInt32(byte[] bufftem) 
    {
        int number = 0;
        for (int shiftBy = 0 , i = 0; shiftBy < 32; shiftBy += 8, i++) 
        {
            number |= (bufftem[i] & 0xff) << shiftBy;
        }      
        return number;
    }
    
    public String getStringMemo(int column)
    {
        MappedByteBuffer bufferFile = table.getBuffer();
        byte[] tembuff = new byte[table.getSubrecords().get(column).getLength()];
        bufferFile.position(offset + DBFSubrecord.getLocalOffset(table, column));
        bufferFile.get(tembuff,0,table.getSubrecords().get(column).getLength());
        String buff = new String(tembuff);
        return buff;   
    }
    
    /**
     * 
     * @return 
     */
    public DBFSubrecord getSubrecord(int i)
    {
        return table.getSubrecord().get(i);
    }
    
    /**
     * 
     * @param record 
     */
    public DBFRecord(DBFRecord record)
    {
        this.table = record.table;
        this.offset = record.offset;
    }
    
    public DBFRecord(Table table, int offset) 
    {
        this.table = table;
        this.offset = offset;
    }
    
    public char getDeletedFlag()
    {
        MappedByteBuffer bufferFile = table.getBuffer();
        byte[] tembuff = new byte[1];
        bufferFile.position(offset);
        bufferFile.get(tembuff, 0, 1);
        String buff = new String(tembuff);
        return buff.charAt(0);  
    }
    
    /**
     * 
     * @param column
     * @return 
     */
    public String getString(int column)
    {
        MappedByteBuffer bufferFile = table.getBuffer();
        byte[] tembuff = new byte[table.getSubrecords().get(column).getLength()];
        bufferFile.position(offset + DBFSubrecord.getLocalOffset(table, column));
        bufferFile.get(tembuff,0,table.getSubrecords().get(column).getLength());
        String buff = new String(tembuff);
        return buff;     
    }

    /**
     * Retorna el registro completo en cadena
     * @return 
     */
    public String getString() 
    {
        MappedByteBuffer bufferFile = table.getBuffer();
        byte[] tembuff = new byte[table.getHeader().getRecordLength()+1];
        bufferFile.position(offset);
        bufferFile.get(tembuff, 0, table.getHeader().getRecordLength());
        String buff = new String(tembuff);
        return buff;
    }
}
