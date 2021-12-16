
package SIIL.CN.Engine;


/**
 *
 * @author Azael
 */
public class DBFSubrecord 
{ 
    static final int FIELD_WIDTH = 32;
        
    private final int offset;
    private final Table table;
    
    DBFSubrecord(int offset,Table table)
    {
        this.offset = offset;
        this.table = table;
    }
    
    /**
     * Nombre
     * @return 
     */
    public String getName()
    {//byte 0 - 10
        byte[] name = new byte[10];
        for(int i = 0; i < 10; i++)
        {
            name[i] = table.getBuffer().get(offset + i);
        }
        
        return new String(name);
    }
    
    /**
     * Tipo de dato
     * @return 
     */
    public char getType()
    {//byte 11
        return (char)table.getBuffer().get(offset + 11);
    }
    
    /**
     * Longitud en byte
     * @return 
     */
    public byte getLength()
    {//16
        if(getType() == 'M') return 10;
        return table.getBuffer().get(offset + 16);
    }
    
    
    /**
     * Calcula la addicion necesario para obtener los datos del campo indicado
     * @param table 
     * @param forField ordinal de coampo deseado
     * @return 
     */
    public static int getLocalOffset(Table table, int forField)
    {
        int offset = 1;
        for(int i = 0; i < forField; i++)
        {
            offset += table.getSubrecords().get(i).getLength();
        }
        return offset;
    }
}
