
package SIIL.core;

import SIIL.Server.Database;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import process.Return;

/**
 * MySQL Avatar
 * @version 2.0
 * @author Azael
 */
public class Office implements Serializable
{
    private static final String MYSQL_AVATAR_TABLE = "Offices";
    private static final String SERIE_CN60_TIJUANA = "A";
    private static final String SERIE_CN60_MEXICALI = "B";
    private static final String SERIE_CN60_ENSENADA = "C";
    private static final String SERIE_CN60_TIJUANA_NC = "D";
    private static final String SERIE_TIJUANA = "CTJ";
    private static final String SERIE_MEXICALI = "CMX";
    private static final String SERIE_ENSENADA = "CEN";
    private static final String SERIE_TOOLS_TIJUANA = "TTJ";
    private static final String SERIE_TOOLS_MEXICALI = "TMX";
    private static final String SERIE_TOOLS_ENSENADA = "TEN";
    public static final String SERIE_COMERCIAL_TIJUANA = "CTT";
    public static final String SERIE_COMERCIAL_MEXICALI = "CTM";
    public static final String SERIE_COMERCIAL_ENSENADA = "CTE";
    
    private int id;
    private String code;
    private String name;
    private String type;
    private String BD;

    public static Office fromSerie(Database db,String serie)
    {
        Office office = null;
        if(serie.equals(SERIE_CN60_TIJUANA) || serie.equals(SERIE_TOOLS_TIJUANA) || serie.equals(SERIE_CN60_TIJUANA_NC) || serie.equals(SERIE_COMERCIAL_TIJUANA))
        {
            office = new Office(1);
        }
        else if(serie.equals(SERIE_CN60_MEXICALI) || serie.equals(SERIE_TOOLS_MEXICALI) || serie.equals(SERIE_COMERCIAL_MEXICALI))
        {
            office = new Office(2);
        }
        else if(serie.equals(SERIE_CN60_ENSENADA) || serie.equals(serie == SERIE_TOOLS_ENSENADA) || serie.equals(SERIE_COMERCIAL_ENSENADA))
        {
            office = new Office(3);
        }
        else
        {
            return null;
        }
        
        Throwable download = null;
        download = office.download(db.getConnection());
        return office;
    }
    public enum Platform
    {
        CN60,
        TOOLS,
        COMERCIAL
    }
    
    public String getSerieOfficeBackward(Platform platform)
    {
        switch(id)
        {
            case 1:
                if(platform == Platform.CN60)
                {
                    return SERIE_TIJUANA;
                }
                else if(platform == Platform.TOOLS)
                {
                    return SERIE_TOOLS_TIJUANA;
                }
                else
                {
                    return null;
                }
            case 2:
                if(platform == Platform.CN60)
                {
                    return SERIE_MEXICALI;
                }
                else if(platform == Platform.TOOLS)
                {
                    return SERIE_MEXICALI;
                }
                else
                {
                    return null;
                }
            case 3:
                if(platform == Platform.CN60)
                {
                    return SERIE_ENSENADA;
                }
                else if(platform == Platform.TOOLS)
                {
                    return SERIE_TOOLS_ENSENADA;
                }
                else
                {
                    return null;
                }
            default:
                return null;
        }
    }
    
    public String getSerieOffice(Platform platform)
    {
        switch(id)
        {
            case 1:
                if(platform == Platform.CN60)
                {
                    return SERIE_CN60_TIJUANA;
                }
                else if(platform == Platform.TOOLS)
                {
                    return SERIE_TOOLS_TIJUANA;
                }
                else if(platform == Platform.COMERCIAL)
                {
                    return SERIE_COMERCIAL_TIJUANA;
                }
                else
                {
                    return null;
                }
            case 2:
                if(platform == Platform.CN60)
                {
                    return SERIE_CN60_MEXICALI;
                }
                else if(platform == Platform.TOOLS)
                {
                    return SERIE_TOOLS_MEXICALI;
                }
                else if(platform == Platform.COMERCIAL)
                {
                    return SERIE_COMERCIAL_MEXICALI;
                }
                else
                {
                    return null;
                }
            case 3:
                if(platform == Platform.CN60)
                {
                    return SERIE_CN60_ENSENADA;
                }
                else if(platform == Platform.TOOLS)
                {
                    return SERIE_TOOLS_ENSENADA;
                }
                else if(platform == Platform.COMERCIAL)
                {
                    return SERIE_COMERCIAL_ENSENADA;
                }
                else
                {
                    return null;
                }
            default:
                return null;
        }
    }

    public static List<Office> listing(Database db) throws SQLException
    {
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " ORDER BY id ASC";
        Office offic;
        List<Office> ls = new ArrayList<Office>();
        ResultSet rs = db.query(sql);
        while(rs.next())
        {
            offic = new Office(rs.getInt(1));
            ls.add(offic);
        }
        return ls;
    }
    
    public Boolean selectCode(Database db, String code) throws SQLException 
    {
        clean();
        if(db == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        String sql = "SELECT id FROM  " + MYSQL_AVATAR_TABLE + " WHERE code = '" + code + "'";        
        ResultSet rs = db.query(sql);
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
    
    public Office(int id) 
    {
        this.id = id;
    }
    
    /**
     * 
     * @return 
     */
    public int getID()
    {
        return id;
    }
    

    @Override
    public String toString()
    {
        if(id > 0)
        {            
            return name;
        }
        else if(id == -1000)
        {
            return "Seleccione...";//usado en el combobox
        }
        else
        {
            return null;
        }
    }
    
    public Office(Database db,String code)
    {
        this.code = code;
        id = -1;
        download(db.getConnection());
    }
    
    public Office(String code,String name)
    {
        this.code = code;
        this.name = name;
        id = -1;
    }
    
    public Office(Database db,Integer id)
    {
        this.id = id;
        download(db.getConnection());
    }

    /**
     * Inicializa los campos code,name,type.
     * @param connection
     * @return null si todo es correcto y Exception con informacion del la falla.
     */
    public Exception download(Connection connection) 
    {
        if(getID() > 0)
        {
            String sql = "SELECT code,name,type,BD FROM Offices WHERE id = " + getID() + " ORDER BY id DESC";
            try 
            {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if(rs.next())
                {
                    this.code = rs.getString(1);
                    this.name = rs.getString(2);
                    this.type = rs.getString(3);
                    this.BD = rs.getString(4);
                    return null;
                }            
                return new Exception("Falló la descarga de atributos para la oficina con id " + getID());
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(Office.class.getName()).log(Level.SEVERE, null, ex);
                return new Exception("Falló la descarga de atributos para la oficina con id " + getID(), ex);
            }  
        }
        else if (getCode() != null)
        {
            String sql = "SELECT id,name,type,BD FROM Offices WHERE code = '" + getCode() + "' ORDER BY id DESC";
            try 
            {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if(rs.next())
                {
                    this.id = rs.getInt(1);
                    this.name = rs.getString(2);
                    this.type = rs.getString(3);
                    this.BD = rs.getString(4);
                    return null;
                }            
                return new Exception("Falló la descarga de atributos para la oficina con code " + getCode());
            }
            catch (SQLException ex) 
            {
                //Logger.getLogger(Office.class.getName()).log(Level.SEVERE, null, ex);
                return new Exception("Falló la descarga de atributos para la oficina con code " + getCode(), ex);
            }  
        }
        else
        {
            return new Exception("Falló la descarga de atributos para la oficina ya que no tiene id ni code valido");
        }
    }

    public String getFilter(String officeField) 
    {
        String where;
        if(getType().equals("m"))
        {
            return null;
        }
        else if(getType().equals("s"))
        {
            where = "(" + officeField + " = '" + getCode() + "')";
            return where;
        }   
        else
        {
            return null;
        }
    }
    
    /**
     * Asigna un ID al azar
     * @param connection
     * @return
     * @throws SQLException 
     */
    public Return selectRandom(Connection connection) throws SQLException 
    {
        clean();
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        String sql = "SELECT id FROM  " + MYSQL_AVATAR_TABLE + " ORDER BY RAND() LIMIT 1";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            this.id = rs.getInt(1);
            return new Return(Return.Status.DONE);
        }
        else
        {
            this.id = -1;
            return new Return(Return.Status.DONE);
        }
    }

    private void clean() 
    {
        id = -1;
        BD = null;
        code = null;
        name = null;
        type = null;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the BD
     */
    public String getBD() {
        return BD;
    }
}
