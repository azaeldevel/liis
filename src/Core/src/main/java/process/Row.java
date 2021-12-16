
package process;

import SIIL.Server.Database;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import stock.Flow;

/**
 * @version 0.1.0
 * @author Azael Reyes Martinez
 */
public class Row 
{
    private static final String MYSQL_AVATAR_TABLE = "ProcessOperationalRows";
    
    private int id;
    private Operational operational;//solo para relacionar los renglones con el documento
    private Flow item; 
    private int number;
    private int groupRenglon;
        
    public Return delete(Database connection) throws SQLException
    {
        if(connection == null)
        {
            return new Return(false,"Conexion nula");
        }
        if(getId() < 0)
        {
            return new Return(false,"No ID");
        }
        
        String sql = "DELETE FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + getId();
        Statement stmt = connection.getConnection().createStatement();
        int afected = stmt.executeUpdate(sql);
        if(afected < 1)
        {
            return new Return(false,"No operable");
        }
                
        return new Return(true,"Operacion completa");       
    }
    
    public Boolean download(Database database) throws SQLException
    {
        if(database == null)
        {
            return false;
        }
        String sql = "SELECT item,op,renglon,groupRenglon FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        Statement stmt = database.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            this.item = new Flow(rs.getInt(1));
            this.item.download(database);
            this.operational = new Operational(rs.getInt(2));
            this.operational.downSerie(database);
            this.operational.downFolio(database);            
            this.number = rs.getInt(3);
            this.groupRenglon = rs.getInt(4);
            return true;
        }
        else
        {
            this.operational = null;
            return false;
        }
    }
          
    
    public void add(Flow flow)
    {
        item = flow;
    }
    
    public Boolean downOperational(Database database) throws SQLException
    {
        if(database == null)
        {
            return false;
        }
        String sql = "SELECT op FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + getId();
        Statement stmt = database.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            this.operational = new Operational(rs.getInt(1));
            this.operational.downSerie(database);
            this.operational.downFolio(database);
            return true;
        }
        else
        {
            this.operational = null;
            return false;
        }
    }
    
    public static List<Row> select(Connection connection,Operational op) throws SQLException
    {      
        List<Row> list = new ArrayList<>();
        if(op == null) return list;
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE op = " + op.getID();        
        Statement stmt = connection.createStatement();
        //System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);
            
        if(rs == null) return list;
        while(rs.next())
        {
            list.add(new Row(rs.getInt(1)));
        }
        return list;
    }
    
    public boolean delete(Connection connection) throws SQLException
    {
        item.downItem(connection);        
        return item.delete(connection);        
    }
    
    public static boolean exist(Connection connection,Operational operational,Flow item) throws SQLException
    {
        item.downItem(connection);
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE op=" + operational.getID() + " AND item = " + item.getID();
        Statement stmt = connection.createStatement();
        //System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            return true;
        }
        return false; 
    }
    
    public Boolean downItem(Connection connection) throws SQLException
    {
        if(connection == null)
        {
            return false;
        }
        String sql = "SELECT item FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + getId();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            this.item = new Flow(rs.getInt(1));
            return true;
        }
        else
        {
            this.item = null;
            return false;
        } 
    }
    
    public static List<Row> select(Connection connection,String serie,int folio) throws SQLException
    {
        //opeter el ID de la operacion.
        Integer op = Operational.select(connection,serie,folio);
        
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE op = " + op;        
        Statement stmt = connection.createStatement();
        //System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);
        List<Row> list = new ArrayList<>();    
        if(rs == null) return list;
        while(rs.next())
        {
            list.add(new Row(rs.getInt(1)));
        }
        return list;
    }
    
    /**
     * 
     * @param id 
     */
    public Row(int id)
    {
        this.id = id;
    }
    
    /**
     * @return the operational
     */
    public Operational getOperational() 
    {
        return operational;
    }

    /**
     * @return the items
     */
    public Flow getItem() 
    {
        return item;
    }
    
    public static int getMaximoNumberRenglon(Database dbserver,Operational operational,int groupRenglon) throws SQLException
    {
        String sql = "SELECT MAX(renglon) FROM " + MYSQL_AVATAR_TABLE  + " WHERE op = " + operational.getID() + " AND groupRenglon = " + groupRenglon;
        ResultSet rs = dbserver.query(sql);
        if(rs.next())
        {
            return rs.getInt(1);
        }
        else
        {
            return 0;
        }
    }
        
        
    public static int getMaximoGroupRenglon(Database dbserver,Operational operational) throws SQLException
    {
        String sql = "SELECT MAX(groupRenglon) FROM " + MYSQL_AVATAR_TABLE  + " WHERE op = " + operational.getID();
        ResultSet rs = dbserver.query(sql);
        if(rs.next())
        {
            return rs.getInt(1);
        }
        else
        {
            return 0;
        }
    }
        
    public static int getNextNumberRenglon(Database dbserver,Operational operational, int groupRenglon) throws SQLException
    {
        String sql = "SELECT MAX(renglon) FROM " + MYSQL_AVATAR_TABLE  + " WHERE op = " + operational.getID() + " AND groupRenglon = " + groupRenglon;
        ResultSet rs = dbserver.query(sql);
        if(rs.next())
        {
            return rs.getInt(1) + 1;
        }
        else
        {
            return 1;
        }
    }
    
    public static int getNextGroupRenglon(Database dbserver,Operational operational) throws SQLException
    {
        String sql = "SELECT MAX(groupRenglon) FROM " + MYSQL_AVATAR_TABLE  + " WHERE op = " + operational.getID();
        ResultSet rs = dbserver.query(sql);
        if(rs.next())
        {
            return rs.getInt(1) + 1;
        }
        else
        {
            return 1;
        }
    }
        
    public Return insert(Database connection,Operational operational, int groupRenglon) throws SQLException 
    {
        if(operational == null)
        {
            return new Return(false,"operational is NULL");
        }
        if(item == null)
        {
            return new Return(false,"item is NULL");
        }
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(item,op,renglon,groupRenglon)  VALUES(" + item.getID() + "," + operational.getID()+ "," + getNextNumberRenglon(connection, operational, groupRenglon) + "," + groupRenglon + ")";
        //System.out.println(sql);
        Statement stmt = connection.getConnection().createStatement();
        int affected = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        if(affected != 1)
        {
            return new Return(false,"Se afectaron '" + affected + "' registro(s) lo cual es incorrecto.");
        }
        ResultSet rs = stmt.getGeneratedKeys();
        int generated = -1;        
        if (rs.next())
        {
            id = rs.getInt(1);
            return new Return(true);
        }
        else
        {
            id = -1;
            return new Return(false,"No genero ID para el registro.");
        }
    }
    
    /**
     * 
     * @param connection
     * @param operational
     * @param item
     * @return
     * @throws SQLException 
     */
    public Return<Integer> insert(Connection connection, Operational operational, Flow item) throws SQLException 
    {
        clean();
        if(operational == null)
        {
            return new Return(false,"operational is NULL");
        }
        if(item == null)
        {
            return new Return(false,"item is NULL");
        }
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(item,op)  VALUES(" + item.getID() + "," + operational.getID() + ")";
        //System.out.print(sql);
        Statement stmt = connection.createStatement();
        int affected = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        if(affected != 1)
        {
            return new Return<>(false,"Se afectaron '" + affected + "' registro(s) lo cual es incorrecto.");
        }
        ResultSet rs = stmt.getGeneratedKeys();
        int generated = -1;        
        if (rs.next())
        {
            id = rs.getInt(1);
            return new Return(true, rs.getInt(1));
        }
        else
        {
            id = -1;
            return new Return(false,"No genero ID para el registro.");
        }
    }

    private void clean() 
    {
        id = -1;
        item = null;
        operational = null;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
}
