
package process;

import SIIL.Server.Database;
import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 *
 * @author Azael Reyes
 */
public class SATCatalogo 
{
    private static final String MYSQL_AVATAR_TABLE = "SATCatalogo";
    
    public boolean selectLast(Database connection) throws SQLException 
    {
        if(connection == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " ORDER BY id DESC LIMIT 0, 1";
        Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        if(rs.next())
        {
            this.id = rs.getInt(1);
            return true;
        }
        else
        {
            this.id = -1;
            return false;
        }
    }
    
    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return the clave
     */
    public String getClave() {
        return clave;
    }

    /**
     * @return the id
     */
    public int getID() {
        return id;
    }
    public enum Imports
    {
        ALL,
        FormaPago       
    }
    private String nombre;
    private String clave;
    private int id;
    
    
    public boolean insert(Database database, String nombre, String clave) throws SQLException
    {
        if(database == null)
        {
            return false;
        }
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + " (nombre,clave) VALUES('" + nombre + "','" + clave + "')";
        Statement stmt = database.getConnection().createStatement();
        int affected = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        if(affected != 1)
        {
            return false;
        }
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next())
        {
            id = rs.getInt(1);
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public static boolean importXLS(Database database, File file, Imports imports) throws IOException, BiffException, SQLException
    {
        if(!file.isFile()) return false;
        if(!file.getName().equals("catCFDI.xls"))
        {
            throw new IOException("El nombre de archivo no es correcto, se espera 'catCFDI.xls'");
        }
        
        Workbook workbook = Workbook.getWorkbook(file);       
        switch(imports)
        {
            case FormaPago:  
                String clave = "c_FormaPago";
                Sheet sheet = workbook.getSheet(clave);
                String nombre = sheet.getCell(1, 2).getContents();
                SATCatalogo catalogo = new SATCatalogo();
                if(catalogo.insert(database, nombre, clave))
                {
                    return SATClaves.importFormaPago(database,catalogo,sheet);
                }
                else
                {
                    return false;
                }
            default:
                return false;
        }
    }
}
