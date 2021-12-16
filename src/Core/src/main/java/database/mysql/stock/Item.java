
package database.mysql.stock;

import SIIL.CN.Engine.DBFRecord;
import SIIL.CN.Sucursal;
import SIIL.CN.Tables.ARTICULO;
import SIIL.Server.Database;
import static SIIL.services.grua.Battery.MYSQL_AVATAR_TABLE;
//import com.galaxies.andromeda.util.Texting.Message;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import process.Return;
import process.ErrorCodes;
import process.Moneda;
import stock.Refection;

/**
 * MySQL Avatar
 * @version 1.0
 * @author Azael Reyes
 */
public class Item
{
    public static final String MYSQL_AVATAR_TABLE = "StockItem";
    
    /**
     * @return the cuentaFiscal
     */
    public String getCuentaFiscal() 
    {
        return cuentaFiscal;
    }
    
    public enum Type
    {
        SERVICIO,
        ARTICULO,
        REFECTION,        
        ALL
    }
        
    //datos fijo
    protected int id;  
    private String number;
    private String type; 
    private String make;
    private String model;
    private String serie;
    private boolean  activeSerie;
    private String externalNumber;
    private String description;   
    private String unidad;
    
    //datos variable  
    private database.mysql.purchases.Provider provider;
    public double temporalCantidad;
    public Moneda temporaryMoneda;
    public double temporaryCost;
    private String cuentaFiscal;
    
    public String getSreie()
    {
        return serie;
    }
    
    public boolean downCuentaFiscal(Database connection) throws SQLException 
    {
        if(connection == null)
        {
            return false;
        }
        String sql = "SELECT cuentaFiscal FROM " + MYSQL_AVATAR_TABLE + " WHERE cuentaFiscal IS NOT NULL AND id = " + id;
        Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            cuentaFiscal = rs.getString(1);
            return true;
        }
        else
        {
            cuentaFiscal = null;
            return false;
        }        
    }
    
    public boolean download(Database dbserver) throws SQLException
    {
        String sql = "SELECT number,make,model,description,unidad,cuentaFiscal FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        ResultSet rs = dbserver.query(sql);
        if(rs.next())
        {
            number = rs.getString(1);
            make = rs.getString(2);
            model = rs.getString(3);
            description = rs.getString(4);
            unidad = rs.getString(5);
            cuentaFiscal = rs.getString(6);
            return true;
        }
        
        return false;
    }
    public Return upCuentaFiscal(Database connection, String cuenta) throws SQLException
    {
        if(id < 1)
        {
            return new Return(false,"ID invalido."); 
        }
        if(connection == null)
        {
            return new Return(false,"connection is null");
        }
        if(cuenta == null)
        {
            return new Return(false,"desc is null.");
        }
        this.cuentaFiscal = null;
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET cuentaFiscal='" + cuenta + "' WHERE id=" + id;
        Statement stmt = connection.getConnection().createStatement();
        System.out.println(sql);
        return new Return(true, stmt.executeUpdate(sql));
    }
    
    public Return upDescription(Database connection, String desc) throws SQLException
    {
        if(id < 1)
        {
            return new Return(Return.Status.FAIL,"ID invalido."); 
        }
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"connection is null");
        }
        if(desc == null)
        {
            return new Return(Return.Status.FAIL,"desc is null.");
        }
        this.description = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET description='" + desc + "' WHERE id=" + id;
        Statement stmt = connection.getConnection().createStatement();
        //System.out.println(sql);
        return new Return(Return.Status.DONE, stmt.executeUpdate(sql));
    }

    public static Return synchCN(Database dbserver,Object progress) throws SQLException
    {
        ARTICULO tbArticulo = null;
        try 
        {
            tbArticulo = new ARTICULO(Sucursal.BC_Tijuana);
        } 
        catch (IOException ex) 
        {
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "Error Exteno",
                    JOptionPane.ERROR_MESSAGE
            );
            return new Return(false,ex.getMessage());
        }
                
        List<DBFRecord> list = tbArticulo.readAll();
        System.out.println("Total de Items " + list.size());
        double lenghtStep = 100/list.size();
        double lenghtWay = 0;
        int countUpdates = 0;
        for(DBFRecord rec : list)
        {
            lenghtWay += lenghtStep;
            Item item = new Item(-1);
            SIIL.CN.Records.ARTICULO articulo = new SIIL.CN.Records.ARTICULO(rec);
            String number = articulo.getNumber();
            Boolean ret = item.selectNumber(dbserver.getConnection(), number);
            //if(progress != null) progress.setProgress((int) lenghtWay, "Presesando " + number + "...");
            if(ret)
            {                
                Return retUnidad = item.upUnidad(dbserver, articulo.getReciben());
                if(retUnidad.isFail()) System.err.println(retUnidad.getMessage());
                countUpdates++;
                Return retDesc = item.upDescription(dbserver, articulo.getDescription());
                if(retDesc.isFail()) System.err.println(retDesc.getMessage());
            }
            else
            {
                
            }
        }
        System.out.println("Total de Items Update " + countUpdates);  
        tbArticulo.close();
        //if(progress != null) progress.finalized(new Message("Actualizacion de items terminada."));        
        return new Return(true);
    }
    
    
    public Return upUnidad(Database connection, String unidad) throws SQLException
    {
        if(id < 1)
        {
            return new Return(Return.Status.FAIL,"ID invalido."); 
        }
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"connection is null");
        }
        if(unidad == null)
        {
            return new Return(Return.Status.FAIL,"unidad is null.");
        }
        this.unidad = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET unidad='" + unidad + "' WHERE id=" + id;
        Statement stmt = connection.getConnection().createStatement();
        //System.out.println(sql);
        return new Return(Return.Status.DONE, stmt.executeUpdate(sql));
    }
    
    public String getUnidad() 
    {
        return unidad;
    }
    
    public Return downUnidad(Database connection) throws SQLException 
    {
        if(connection == null)
        {
            return new Return(false,"Connection is null.");
        }
        String sql = "SELECT unidad FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            unidad = rs.getString(1);
            return new Return(true);
        }
        else
        {
            unidad = null;
            return new Return(false,"No se encontro ningún registro para el ID = " + id);
        }
    }    

    public boolean isService(Database database) throws SQLException
    {
        String sql = "SELECT id FROM " + Service.MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        ResultSet rs = database.query(sql);
        if(rs.next())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public boolean isRefection(Database database) throws SQLException
    {
        String sql = "SELECT idItem FROM " + Refection.MYSQL_AVATAR_TABLE + " WHERE idItem = " + id;
        ResultSet rs = database.query(sql);
        if(rs.next())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public static boolean isItem(Database database, Item item) throws SQLException 
    {
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + item.getID();
        Statement stmt = database.getConnection().createStatement();
        //System.out.println(sql);
        ResultSet rs = database.query(sql);
        if(rs.next())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    
    @Override
    public String toString()
    {
        return number;
    }

    public static List<Item> search(Database database, String search)throws SQLException
    {
        String sql;
    
        if(search.length() > 0)
        {
            sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE number like '%" + search + "%'";
        }
        else
        {
            sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " ORDER BY id DESC LIMIT 20";
        }
        Statement stmt = database.getConnection().createStatement();
        System.out.println(sql);
        ResultSet  rs =stmt.executeQuery(sql);
        ArrayList<Item> list = new ArrayList<>();
        while(rs.next())
        {
            Item item = new Item(rs.getInt(1));
            list.add(item);            
        }
        return list;
    }
    
    public Boolean downSerie(Database database) throws SQLException 
    {
        if(database == null)
        {
            return false;
        }
        String sql = "SELECT serie FROM " + MYSQL_AVATAR_TABLE + " WHERE serie IS NOT NULL AND id = " + id;
        ResultSet rs = database.query(sql);
        if(rs.next())
        {
            this.serie = rs.getString(1);
            this.activeSerie = true;
            return true;
        }
        else
        {
            this.model = null;
            activeSerie = false;
            return false;
        }
    }
        
    /**
     * 
     * @param database
     * @return
     * @throws SQLException 
     */
    public Boolean downModel(Database database) throws SQLException 
    {
        if(database == null)
        {
            return false;
        }
        String sql = "SELECT model FROM " + MYSQL_AVATAR_TABLE + " WHERE model IS NOT NULL AND id = " + id;
        ResultSet rs = database.query(sql);
        if(rs.next())
        {
            this.model = rs.getString(1);
            return true;
        }
        else
        {
            this.model = null;
            return false;
        }
    }
    
    /**
     * Asigna un ID al azar
     * @param connection
     * @param number
     * @return
     * @throws SQLException 
     */
    public Boolean selectNumber(Connection connection,String number) throws SQLException 
    {
        if(connection == null)
        {
            id = -1;
            return false;
        }
        String sql = "SELECT id FROM  " + MYSQL_AVATAR_TABLE + " WHERE number = '" + number + "'";
        Statement stmt = connection.createStatement();
        //System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            id = rs.getInt(1);
            return true;            
        }
        else
        {
            id = -1;
            return false;
        }
    }
    
    /**
     * Asigna un ID al azar
     * @param connection
     * @param number
     * @return
     * @throws SQLException 
     */
    public static Integer select(Connection connection,String number) throws SQLException 
    {
        if(connection == null)
        {
            return -1;
        }
        String sql = "SELECT id FROM  " + MYSQL_AVATAR_TABLE + " WHERE number = '" + number + "'";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
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
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public Return<Integer> downDescription(Connection connection) throws SQLException 
    {
        if(connection == null)
        {
            return new Return<>(false,"Connection is null.");
        }
        String sql = "SELECT description FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            description = rs.getString(1);
            return new Return<>(true);
        }
        else
        {
            description = null;
            return new Return<>(false,"No se encontro nigun registro para el ID = " + id);
        }
    }

    public static Return<Integer> isExist(Connection connection, String number)throws SQLException
    {
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE number = '" + number + "'";
        Statement stmt = connection.createStatement();
        ResultSet  rs =stmt.executeQuery(sql);
        if(rs.next())
        {
            //System.out.println("item.isExist " + number);
            return new Return<>(true,rs.getInt(1));
        }
        else
        {
            //System.out.println("item.isExist no " + number);
            return new Return<>(false,-1);
        }
    }
    
    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public Return downMake(Connection connection) throws SQLException 
    {
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        String sql = "SELECT make FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            make = rs.getString(1);
            return new Return(Return.Status.DONE);
        }
        else
        {
            make = null;
            return new Return(Return.Status.FAIL,"No se encontro nigun registro para el ID = " + id);
        }        
    }
    
    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public Return downNumber(Connection connection) throws SQLException 
    {
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        String sql = "SELECT number FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            number = rs.getString(1);
            return new Return(Return.Status.DONE);
        }
        else
        {
            number = null;
            return new Return(Return.Status.FAIL,"No se encontro nigun registro para el ID = " + id);
        }        
    }
    
    /**
     * @return the number
     */
    public String getNumber() 
    {
        return number;
    }

    /**
     * @return the type
     */
    public String getType() 
    {
        return type;
    }

    /**
     * @return the make
     */
    public String getMake() 
    {
        return make;
    }

    /**
     * @return the model
     */
    public String getModel() 
    {
        return model;
    }    
    
    /**
     * Inserta un nuevo numero de parte en el catalogo.
     * @param connection
     * @param number
     * @param description
     * @param type
     * @return Id de registro creado.
     * @throws java.sql.SQLException Lanzada desde el engine
     */
    public Return<Integer> insert(Connection connection, String number,String description,Type type,boolean activeSerie,String serie) throws SQLException
    {
        clean();
        if(id > 0)
        {
            return new Return<>(false,"ID tiene valor > 0 en Item : " + id);
        }
        if(connection == null || number == null)
        {
            return new Return<>(false,"No se permite valores nulos en los parametros");
        }
        
        if(number.length() < 5 && number.length() > 30)
        {
            return new Return<>(false,"Deve indicar un número deve estar entre 5 y 30 caracteres.");
        }
        if(number.length() < 3 && number.length() > 10)
        {
            return new Return<>(false,"El tipo de articulo deve ser de entre 3 y 10 caracteres.");
        }
        if(description == null)
        {
            return new Return<>(false,"La descripcion no puede ser null.");
        }
        else if(description.length() < 1)
        {
            return new Return<>(false,"La descripcion no puede una cadena vacia.");
        }
        if(number.contains("'"))
        {
            return new Return<>(false,"El caracter \"'\" es no deve formar parte del valor.",ErrorCodes.ERR_MYSQL_APOSTROPHE);   
        }
        if(description.contains("'"))
        {
            return new Return<>(false,"El caracter \"'\" es no deve formar parte del valor.",ErrorCodes.ERR_MYSQL_APOSTROPHE);   
        } 
        
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(number,description,type) VALUES('" + number + "','" + description + "'";        
        switch(type)
        {
            case ARTICULO:
                sql = sql + ",'I'";
                break;
            case SERVICIO:
                sql = sql + ",'S'";
                break;
            case REFECTION:
                sql = sql + ",'R'";
                break;
        }
         sql = sql + ")";
        //System.out.println(sql);
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
            //connection.commit();
            id = rs.getInt(1);
            return new Return<>(true, rs.getInt(1));
        }
        else
        {
            id = -1;
            //connection.rollback();
            return new Return<>(false,"No genero ID para el registro.");
        }
    }


    /**
     * @return the provider
     */
    public database.mysql.purchases.Provider getProvider() {
        return provider;
    }

    /**
     * @return the idItem
     */
    public int getID() 
    {
        return id;
    }
    
    /**
     * Se agrega para dar compatibiliidad hacia atras ya que resumov no reconoce
     * los IDs de el nuevo modulo de stock.
     * @param databse
     * @param number
     * @throws java.sql.SQLException
     * @deprecated
     */
    public Item(Database databse,String number) throws SQLException
    {
        select(databse.getConnection(), number);
    }
    
    /**
     * 
     * @param idItem 
     */
    public Item(int idItem)
    {
        this.id = idItem;
    }
    
    /**
     * Actualiza el campo StockItem.make
     * @param connection
     * @param make
     * @return
     * @throws SQLException 
     */
    public Return upMake(Connection connection, String make) throws SQLException
    {
        if(id < 1)
        {
            return new Return(Return.Status.FAIL,"ID invalido."); 
        }
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"connection is null");
        }
        if(make == null)
        {
            return new Return(Return.Status.FAIL,"make is null.");
        }
        this.make = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET make='" + make + "' WHERE id=" + id;
        Statement stmt = connection.createStatement();
        //System.out.println(sql);
        return new Return(Return.Status.DONE, stmt.executeUpdate(sql));
    }
    
    /**
     * Actualiza el campo StockItem.mode
     * @param connection
     * @param model
     * @return
     * @throws SQLException 
     */
    public Return upModel(Connection connection, String model) throws SQLException
    {
        if(id < 1)
        {
            return new Return(Return.Status.FAIL,"ID invalido."); 
        }
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"connection is null");
        }
        if(model == null)
        {
            return new Return(Return.Status.FAIL,"model is null");
        }
        this.model = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET model='" + model + "' WHERE id=" + id;
        Statement stmt = connection.createStatement();
        //System.out.println(sql);
        return new Return(Return.Status.DONE, stmt.executeUpdate(sql));
    }  
    
    
    /**
     * Actualiza el campo StockItem.mode
     * @param connection
     * @param serie
     * @return
     * @throws SQLException 
     */
    public Return upSerie(Connection connection, String serie) throws SQLException
    {
        if(id < 1)
        {
            return new Return(Return.Status.FAIL,"ID invalido."); 
        }
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"connection is null");
        }
        if(serie == null)
        {
            return new Return(Return.Status.FAIL,"model is null");
        }
        this.serie = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET serie='" + serie + "' WHERE id=" + id;
        Statement stmt = connection.createStatement();
        //System.out.println(sql);
        return new Return(Return.Status.DONE, stmt.executeUpdate(sql));
    }
    
    /**
     * Actualiza el campo StockItem.externalNumber
     * @param connection
     * @param externalNumber
     * @return
     * @throws SQLException 
     */
    public Return upExternalNumber(Connection connection, String externalNumber) throws SQLException
    {
        if(id < 1)
        {
            return new Return(Return.Status.FAIL,"ID invalido."); 
        }
        if(connection == null || externalNumber == null)
        {
            return new Return(Return.Status.FAIL,"No se permite valores nulos en los parametros");
        }
        this.externalNumber = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET externalNumber='" + externalNumber + "' WHERE id=" + id;
        Statement stmt = connection.createStatement();
        return new Return(Return.Status.DONE, stmt.executeUpdate(sql));
    }   
    
    /**
     * @return the externalNumber
     */
    public String getExternalNumber() {
        return externalNumber;
    }
    
    /**
     * Actualiza el campo StockItem.provider
     * @param connection
     * @param provider
     * @return
     * @throws SQLException 
     */
    public Return upProvider(Connection connection, database.mysql.purchases.Provider provider) throws SQLException
    {
        if(id < 1)
        {
            return new Return(Return.Status.FAIL,"ID invalido."); 
        }
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"connection is null");
        }
        if(provider == null)
        {
            throw new InvalidParameterException("provider is null");
        }
        this.provider = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET provider='" + provider.getID() + "' WHERE id=" + id;
        Statement stmt = connection.createStatement();
        //System.out.println(sql);
        return new Return(Return.Status.DONE, stmt.executeUpdate(sql));
    }

    /**
     *
     */
    private void clean() 
    {    
        //datos fijo
        id = -1;  
        number = null;
        type = null; 
        make = null;
        model = null;
        externalNumber = null;

        //datos variable  
        provider = null;
    }

    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public Return selectLast(Connection connection) throws SQLException 
    {
        clean();
        if(connection == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        String sql = "SELECT id FROM  " + MYSQL_AVATAR_TABLE + " ORDER BY id DESC LIMIT 0, 1";
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
    
    protected void setID(int id)
    {
        this.id = id;
    }    

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
}
