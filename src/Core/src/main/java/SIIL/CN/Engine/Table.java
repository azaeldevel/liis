
package SIIL.CN.Engine;

import static SIIL.CN.Engine.DBFHeader.HEADER_TERMINATOR;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Azael
 */
public class Table
{
    private DBFHeader dbfHeader;
    private List<DBFSubrecord> subrecords;
    private MappedByteBuffer dbfFile;
    private int first,lenght,last;    
    private FPTHeader fptHeader;
    private FileChannel fileChannel;
    
    
    public void close()
    {
        try {
            fileChannel.close();
        } catch (IOException ex) {
            Logger.getLogger(Table.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * 
     * @param begin non-zero base 
     * @param last non-zero base
     * @return null sí count <= 0
     */
    public List<DBFRecord> readBlock(int begin,int last)
    {
        if(begin <= 0) return null;
        if(begin > last) return null;
        int count = last - begin;
        
        int lg = getHeader().getRecordLength();
        int offset = getHeader().getRecordFirst();
        offset = offset + (begin * lg);
        
        List<DBFRecord> list = new ArrayList<>();
        for(int i = 0; i < count; i++)
        {
            DBFRecord rec = new DBFRecord(this, offset);
            list.add(rec);
            offset += lg;
        }
        return list;
    }
        
    /**
     * 
     * @return 
     */
    public int getRecordsCount()
    {
        return getHeader().getRecordsCount();
    }
    
    /**
     * 
     * @return 
     */
    public List<DBFSubrecord> getSubrecord()
    {
        return subrecords;
    }
    
    /**
     * 
     * @return 
     */
    public boolean previous()
    {
        int offset = dbfFile.position() - getHeader().getRecordLength();
        /*
        first = getHeader().getRecordFirst();
        lenght = getHeader().getRecordLength();
        last = lenght * (getHeader().getRecordsCount() - 1);*/
        //verificar que el cambio este dentro del reango
        if(offset <  first | last < offset)
        {
            return false;
        }
        dbfFile.position(offset);
        return true;
    }
    
    /**
     * 
     * @return 
     */
    public boolean next()
    {
        int offset = dbfFile.position() + getHeader().getRecordLength();
        /*
        first = getHeader().getRecordFirst();
        lenght = getHeader().getRecordLength();
        last = lenght * (getHeader().getRecordsCount() - 1);*/
        //verificar que el cambio este dentro del reango
        if(offset <  first | last < offset)
        {
            return false;
        }
        dbfFile.position(offset);
        return true;
    }
    
    
    protected void setPosition(int offset)
    {
        dbfFile.position(offset);
    }
    
    public Table()
    {
        ;
    }
    
    /**
     * @param clause Condicion
     * @param limit numero maximo de registros solicitados, si es 0 hace una busqueda en toda la tabla.
     * @param direction orden de busqueda(no indexada)
     * @return 
     */
    public List<DBFRecord> readWhere(Clause clause,int limit,char direction)
    {
        int offset = 0;
        int lenght = getHeader().getRecordLength();
        int delta = 0;
        offset = getHeader().getRecordFirst();
        switch (direction) 
        {
            //progresivo
            case 'P':
                delta = 1;
                break;
            //retroceso
            case 'R':
                delta = -1;
                //cual es el offset del ultimo registro.
                offset = offset + (lenght * (getHeader().getRecordsCount() - 1));
                break;
            default:
                throw new UnsupportedOperationException("Bandera de direccion desconocida.");
        }
        int count = getHeader().getRecordsCount();
        List<DBFRecord> list = new ArrayList();               
        for(int steps = 0; steps < count; steps++)
        {
            //if(lenght == steps) break;
            //System.out.println("Step : " + steps);
            //limitar maximo numero de registros retornados
            if(list.size() >= limit && limit > 0) break;
            DBFRecord rec = new DBFRecord(this, offset);
            Boolean flag = null;
            if(clause.getValue() instanceof String)
            {
                if(checkClause(clause,rec))
                {
                    flag = true;
                }
                else
                {
                    flag = false;
                }
            }
            else if(clause.getValue() instanceof List)
            {
                List<Clause> clauses = (List<Clause>) clause.getValue();                
                for(Clause cl : clauses)
                {
                    if(clause.getOp() == Operator.AND)
                    {
                        if(checkClause(cl,rec))
                        {
                            flag = true;
                        }
                        else
                        {                            
                            flag = false;
                            break;
                        }
                    }
                    else if(clause.getOp() == Operator.OR)
                    {
                        if(checkClause(cl,rec))
                        {
                            flag = true;
                            break;
                        }
                        else
                        {
                            flag = false;
                        }
                    }
                }
            }
            else
            {
                throw new UnsupportedOperationException("Operador de Clausula no soportado");
            }
            if(flag)
            {
                list.add(rec);
            }
            
            offset += (delta * lenght);
            setPosition(offset);            
        }
        
        return list;
    }
    
    public List<DBFRecord> readLast(int last)
    {
        List<DBFRecord> list = new ArrayList<>();
        int offset = getHeader().getRecordFirst();
        int lenght = getHeader().getRecordLength();
        //¿cúal es el offset del ultimo registro?
        offset = offset + (lenght * (getHeader().getRecordsCount()-last));
        //int count = getHeader().getRecordsCount();
        for(int i = 0; i < last; i++)
        {
            DBFRecord rec = new DBFRecord(this, offset);
            offset += lenght;
            list.add(rec);
        }
        
        return list;
    }
    
    List<DBFSubrecord> getSubrecords()
    {        
        if(subrecords == null)
        {
            loadSubrecords();
        }
        return subrecords;
    }
    
    void loadSubrecords() 
    {
        subrecords = new ArrayList<>();
        int pos = 32;
        //Si el siguiente byte no es el terminador
        while(dbfFile.get(pos) != HEADER_TERMINATOR)
        {
            subrecords.add(new DBFSubrecord(pos,this));
            pos += DBFSubrecord.FIELD_WIDTH;
        }
    }
    
    public Table(String filename) throws FileNotFoundException, IOException
    {
        Load(filename);
        first = getHeader().getRecordFirst();
        lenght = getHeader().getRecordLength();
        last = lenght * (getHeader().getRecordsCount() - 1);
    }

    public void Load(String prefix,boolean activeDBF,boolean activeFPT) throws IOException, FileNotFoundException 
    {
        if(activeDBF)
        {
            File fileDBF = new File(prefix + ".dbf");
            FileChannel fileChannel = new RandomAccessFile(fileDBF, "r").getChannel();
            dbfFile = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());        
            dbfHeader = new DBFHeader(dbfFile);
        }
        if(activeFPT)
        {
            File fileFPT = new File(prefix + ".fpt");
            FileChannel fileChannel = new RandomAccessFile(fileFPT, "r").getChannel();
            MappedByteBuffer fptFile = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());  
            fptHeader = new FPTHeader(fptFile);
        }
    }
        
    public void Load(String filename) throws IOException, FileNotFoundException 
    {
        File file = new File(filename);
        fileChannel = new RandomAccessFile(file, "r").getChannel();
        dbfFile = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
        
        dbfHeader = new DBFHeader(dbfFile);
    }

    public DBFHeader getHeader() 
    {
        return dbfHeader;
    }

    MappedByteBuffer getBuffer() 
    {
        return dbfFile;
    }
     
    public List<DBFRecord> readAll()
    {
        List<DBFRecord> list = new ArrayList<>();
        int offset = getHeader().getRecordFirst();
        int count = getHeader().getRecordsCount();
        int lenght = getHeader().getRecordLength();
        for(int i = 0; i < count; i++)
        {
            DBFRecord rec = new DBFRecord(this, offset);
            offset += lenght;
            list.add(rec);
        }
        
        return list;
    }

    private boolean checkClause(Clause clause,DBFRecord rec) 
    {
        switch (clause.getOp()) 
        {
            case INCLUDE:
            {
                String tmpString = rec.getString(clause.getRecord());
                if(tmpString.contains((String)clause.getValue()))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            case TRIMEQUAL:
            {
                String tmpString = rec.getString(clause.getRecord());
                tmpString = tmpString.trim();
                String findValue = (String) clause.getValue();
                if(findValue == null)
                {
                    if(!tmpString.matches("^[a-zA-Z0-9]$") && rec.getDeletedFlag() != '*')
                    {
                        return true;
                    }  
                    else
                    {
                        return false;
                    }  
                }
                else
                {
                    if(tmpString.contains(findValue) && rec.getDeletedFlag() != '*')
                    {
                        return true;
                    }  
                    else
                    {
                        return false;
                    }  
                }
            }
            default:
                throw new UnsupportedOperationException("El operador aun no ha sido implementado.");
        }
    }

    /**
     * @return the fptHeader
     */
    public FPTHeader getFPTHeader() {
        return fptHeader;
    }
}
