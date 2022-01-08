
package database.mysql.stock;

import SIIL.Server.Database;
import SIIL.services.grua.Battery;
import SIIL.services.grua.Charger;
import SIIL.services.grua.Forklift;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import process.Return;

/**
 * @version 0.1
 * @author Azael Reyes
 */
public class Titem extends Item
{
    public static final String MYSQL_AVATAR_TABLE = "StockHequi";
    private static final String MYSQL_AVATAR_TABLE_BACKWARD_OBJECT = "Object";
    private static final String MYSQL_AVATAR_TABLE_BACKWARD_ITEM = "Item";
    public static final String MYSQL_AVATAR_TABLE_BACKWARD_TITEM = "Titem";
    public static final String MYSQL_AVATAR_TABLE_BACKWARD_BD = "bc.tj"; 
    
    public enum Tipo
    {
        ELECTRICO,
        COMBUSTION_INTERNA
    }
    
    public Return upTipo(Connection connection, String make) throws SQLException
    {
        if(connection == null)
        {
            return new Return(false,"connection is null");
        }
        if(make == null)
        {
            return new Return(false,"make is null.");
        }
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE_BACKWARD_TITEM + " SET tipo='" + make + "' WHERE BD = '" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "' AND number ='" + getNumber() +"'" ;
        Statement stmt = connection.createStatement();
        int retUp = stmt.executeUpdate(sql);
        if(retUp == 1)
        {
            return new Return<>(true);
        }
        else
        {
            return new Return<>(false,"Affected row invalid : " + retUp);
        }
    }
    
    public String describeType(Database database) throws SQLException
    {
        if(Forklift.isForklift(database,this))
        {
            String sql = "SELECT tipo FROM " + Forklift.MYSQL_AVATAR_TABLE + " WHERE id = " + id;
            Statement stmt = database.getConnection().createStatement();
            //System.out.println(sql);
            ResultSet rs = database.query(sql);
            if(rs.next())
            {
                if(rs.getString("tipo").equals("E"))
                {
                    return "Eletrico";
                }
                else if(rs.getString("tipo").equals("CI"))
                {
                    return "Combustion Interna";
                }
                else if(rs.getString("tipo").equals("AD"))
                {
                    return "Aditamento";
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
        else if(Battery.isBattery(database,this))
        {
            return "Bateria";
        }
        else if(Charger.isCharger(database,this))
        {
            return "Cargador";
        }
        return null;
        
    }
    
    public enum Import
    {
        CreateSotckUpdateTitem,
        UpdateTitemFromStocK,
        NoImport
    }
    
    public enum Type
    {
        FORKLIFT,
        BATTERY,
        CHARGER,
        MINA,
        UNKNOW
    }
    
    public Type getType(Database database) throws SQLException
    {
        if(Forklift.isForklift(database,this))
        {
            return Type.FORKLIFT;
        }
        else if(Battery.isBattery(database,this))
        {
            return Type.BATTERY;
        }
        else if(Charger.isCharger(database,this))
        {
            return Type.CHARGER;
        }
        return Type.UNKNOW;
    }
    
    public static Import importType(Database database,String number) throws SQLException
    {
        boolean flTitem,flStockItem;
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE_BACKWARD_TITEM + " WHERE BD = '" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "' AND number = '" + number + "'";
        Statement stmt = database.getConnection().createStatement();
        //System.out.println(sql);
        ResultSet rs = database.query(sql);
        if(rs.next())
        {
            flTitem = true;
        }
        else
        {
            flTitem = false;
        }
                
        sql = "SELECT id FROM " + Item.MYSQL_AVATAR_TABLE + " WHERE number = '" + number + "'";
        stmt = database.getConnection().createStatement();
        //System.out.println(sql);
        rs = database.query(sql);
        if(rs.next())
        {
            flStockItem = true;
        }
        else
        {
            flStockItem = false;
        }
        
        if(flTitem == true && flStockItem == false)
        {
            return Import.CreateSotckUpdateTitem;
        }
        else if(flTitem == true && flStockItem == true)
        {
            return Import.UpdateTitemFromStocK;
        }
        else
        {
            return Import.NoImport;
        }
    }
    
    public static int isExist(Database database,String number) throws SQLException
    {
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE_BACKWARD_TITEM + " WHERE BD = '" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "' AND number = '" + number + "'";
        Statement stmt = database.getConnection().createStatement();
        //System.out.println(sql);
        ResultSet rs = database.query(sql);
        if(rs.next())
        {
            return rs.getInt(1);
        }
        else
        {
            return -1;
        }
    }
    
    /**
     * Actualiza el campo StockItem.make
     * @param connection
     * @param make
     * @return
     * @throws SQLException 
     */
    @Override
    public Return<Integer> upMake(Connection connection, String make) throws SQLException
    {
        if(connection == null)
        {
            return new Return(false,"connection is null");
        }
        if(make == null)
        {
            return new Return(false,"make is null.");
        }
        Return<Integer> ret = super.upMake(connection, make);
        if(ret.getStatus() == Return.Status.FAIL) return new Return(false,"fallo la actualizacionde item is null.");
        Return retDow = downNumber(connection);
        if(retDow.getStatus() == Return.Status.FAIL) return new Return(false,"fallo la descarga del number.");
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE_BACKWARD_TITEM + " SET marca='" + make + "' WHERE BD = '" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "' AND number ='" + getNumber() +"'" ;
        Statement stmt = connection.createStatement();
        int retUp = stmt.executeUpdate(sql);
        if(retUp == 1)
        {
            return new Return<>(true);
        }
        else
        {
            return new Return<>(false,"Affected row invalid : " + retUp);
        }
    }
    
    /**
     * Actualiza el campo StockItem.mode
     * @param connection
     * @param model
     * @return
     * @throws SQLException 
     */
    @Override
    public Return<Integer> upModel(Connection connection, String model) throws SQLException
    {
        if(connection == null)
        {
            return new Return<>(false,"connection is null");
        }
        if(model == null)
        {
            return new Return<>(false,"model is null");
        }
        Return<Integer> ret = super.upModel(connection, model);
        if(ret.getStatus() == Return.Status.FAIL) return new Return(false,"fallo la actualizacionde item is null.");
        Return retDow = downNumber(connection);
        if(retDow.getStatus() == Return.Status.FAIL) return new Return(false,"fallo la descarga del number.");
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE_BACKWARD_TITEM + " SET modelo='" + model + "' WHERE BD = '" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "' AND number ='" + getNumber() +"'" ;
        Statement stmt = connection.createStatement();
        int retUp = stmt.executeUpdate(sql);
        if(retUp == 1)
        {
            return new Return<>(true);
        }
        else
        {
            return new Return<>(false,"Affected row invalid : " + retUp);
        }
    }
    
    /**
     * Asigna un ID al azar
     * @param connection
     * @return
     * @throws SQLException 
     */
    @Override
    public Return selectRandom(Connection connection) throws SQLException 
    {
        clean();
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        
        String sql = "SELECT id FROM  " + MYSQL_AVATAR_TABLE_BACKWARD_TITEM + " WHERE id > 0 ORDER BY RAND() LIMIT 1";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            super.id = rs.getInt(1);
            return new Return(Return.Status.DONE);
        }
        else
        {
            super.id = -1;
            return new Return(Return.Status.DONE);
        }
    }
    
    /**
     * Se agrega para dar compatibiliidad hacia atras ya que resumov no reconoce
     * los IDs de el nuevo modulo de stock.
     * @param database
     * @param number
     * @throws java.sql.SQLException
     * @deprecated
     */
    @Deprecated
    public Titem(Database database,String number) throws SQLException 
    {
        super(database,number);
    }
        
    /**
     * @param idItem id en tabla -1 si se deconoce.
     */
    public Titem(int idItem) 
    {
        super(idItem);
    }
    
    /**
     * Inserta un nuevo nímero de parte en el catalogo.
     * @param connection
     * @param number
     * @param description
     * @param backward activa la compatibilidad con la version anterior de la Base de datos.
     * @param activeSerie tru se se activa la serie
     * @param serie la serie
     * @return Id de registro creado.
     * @throws java.sql.SQLException Lanzada desde el engine
     */
    public Return<Integer> insert(Connection connection, String number,String description,Import backward,boolean activeSerie,String serie) throws SQLException
    {
        if(connection == null)
        {
            return new Return<>(false,"Connection is null.");
        }
        Return ret = null;
        if(backward == Import.UpdateTitemFromStocK)
        {            
            if(!super.selectNumber(connection, number))
            {
                return new Return<>(false,"No se encontro ID para " + number);
            }
            ret = new Return<>(true, super.getID());
        }
        else
        {
            ret = super.insert(connection, number,description,Item.Type.ARTICULO,activeSerie,serie);
        }
        if(ret.isFail()) return ret;
        Statement stmt = connection.createStatement();
        if(backward == Import.CreateSotckUpdateTitem || backward == Import.UpdateTitemFromStocK)
        {
            String sql = "UPDATE " + MYSQL_AVATAR_TABLE_BACKWARD_TITEM + " SET serie = '" + serie + "'" ;
            sql += " WHERE id = " + super.getID();
            //System.out.println(sql);
            int upTitem = stmt.executeUpdate(sql);
        }
        else
        {
            String sqlObject = "INSERT INTO " + MYSQL_AVATAR_TABLE_BACKWARD_OBJECT + "(BD,number) VALUES('" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "','" + number + "')";
            String sqlItem = "INSERT INTO " + MYSQL_AVATAR_TABLE_BACKWARD_ITEM + "(BD,number) VALUES('" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "','" + number + "')";
            String sqlTitem = "";
            if(activeSerie)
            {
                sqlTitem = "INSERT INTO " + MYSQL_AVATAR_TABLE_BACKWARD_TITEM + "(BD,number,id,serie) VALUES('" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "','" + number + "'," + super.getID()+ ",'" + serie + "')";
            }
            else
            {
                sqlTitem = "INSERT INTO " + MYSQL_AVATAR_TABLE_BACKWARD_TITEM + "(BD,number,id) VALUES('" + MYSQL_AVATAR_TABLE_BACKWARD_BD + "','" + number + "'," + super.getID() + ")";
            }
            int affectedObject = stmt.executeUpdate(sqlObject);
            int affectedItem = stmt.executeUpdate(sqlItem);
            //System.out.println(sqlTitem);
            int affectedTitem = stmt.executeUpdate(sqlTitem);
            if(affectedObject != 1 | affectedItem != 1 | affectedTitem != 1)
            {
                return new Return<>(false,"Se afectaron un cantidad incorrecta de registros en las tabalas de backward compatible");
            }
        }
        return new Return<>(true, super.getID());
    }

    /**
     * 
     */
    private void clean() 
    {
    }
    
    public static Type checkType(String titem) throws SQLException
    {        
        if(titem.isEmpty() || titem.isBlank())
        {
            return Type.UNKNOW;
        }
        else if(titem.startsWith("N/A"))
        {
            return Type.UNKNOW;
        }
        else if(titem.startsWith("B-"))
        {
            return Type.BATTERY;
        }
        else if(titem.startsWith("C-"))
        {
            return Type.CHARGER;
        }
        else if(titem.matches("[+-]?\\d*(\\.\\d+)?"))
        {
            int number = Integer.valueOf(titem);
            if(number < 700) return Type.UNKNOW;
            return Type.FORKLIFT;
        }
        
        return Type.UNKNOW;
    }
}
