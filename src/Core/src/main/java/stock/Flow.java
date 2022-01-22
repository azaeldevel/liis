
package stock;

import SIIL.Server.Database;
import SIIL.services.grua.Battery;
import SIIL.services.grua.Charger;
import SIIL.services.grua.Forklift;
import SIIL.services.grua.Mina;
import core.FailResultOperationException;
import database.mysql.purchases.order.PO;
import database.mysql.sales.Quotation;
import database.mysql.stock.Item;
import database.mysql.stock.Titem;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import process.Moneda;
import process.Return;

/**
 * MySQL Avatar
 * @version 0.1
 * @author Azael Reyes
 */
public class Flow 
{
    private static final String MYSQL_AVATAR_TABLE = "StockFlow";    
    private static final String MYSQL_AVATAR_TABLE_TITEM = "Titem_Resolved";
    private static final String MYSQL_AVATAR_TABLE_FORKLIFT = "Forklift_Resolved";
    private static final String MYSQL_AVATAR_TABLE_BATTERY = "Battery_Resolved";
    private static final String MYSQL_AVATAR_TABLE_CHARGER = "Charger_Resolved";
    private static final String MYSQL_AVATAR_TABLE_MINA = "Minas_Resolved";
    private static final String MYSQL_AVATAR_TABLE_ITEM = "Item_resolved"; 
          
    public enum Estado
    {
        LIBRE,
        APARTADO,
        VENDIDO,
    }
    private int id;
    private Item item;
    
    /**
     * Indica cuando ingresa al flujo de mercancia.
     */
    private Date fhIngreso;
    private boolean activeSerie;
    private String serie;
    private Quotation quotation;
    private PO po;   
    private double costSale;
    private Moneda costSaleMoney;
    private String pedimentoAduana;
    private String pedimentoNumero;
    private Date pedimentoFecha;
    private Estado estado;
    private Date purchaseETA;
    private double costPurchase;
    private Moneda costPurchaseMoney;    
    private Date purchaseArrival;
        
    public boolean downPedimento(Database dbserver) throws SQLException
    {
        String sql = "SELECT pedimentoFecha,pedimentoAduana,pedimentoNumero FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        ResultSet rs = dbserver.query(sql);
        if(rs.next())
        {
            pedimentoFecha = rs.getDate(1);
            pedimentoAduana = rs.getString(2);
            pedimentoNumero = rs.getString(3);
            return true;
        }
        else
        {
            return false;
        }
    }
    public int upPedimentoFecha(Database dbserver, java.util.Date fecha) throws SQLException
    {
        if(id < 1)
        {
            return -1; 
        }
        if(dbserver == null)
        {
            return -1;
        }
        
        this.pedimentoFecha= null;
        SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET pedimentoFecha= '" + formateador.format(fecha) + "' WHERE id=" + id;
        Statement stmt = dbserver.getConnection().createStatement();
        return stmt.executeUpdate(sql);
    }
    
    public int upPedimentoAduana(Database dbserver, String aduana) throws SQLException
    {
        if(id < 1)
        {
            return -1; 
        }
        if(dbserver == null)
        {
            return -1;
        }
        this.pedimentoAduana = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET pedimentoAduana= '" + aduana + "' WHERE id=" + id;
        Statement stmt = dbserver.getConnection().createStatement();
        return stmt.executeUpdate(sql);
    }
    
    public int upPedimentoNumero(Database dbserver, String numero) throws SQLException
    {
        if(id < 1)
        {
            return -1; 
        }
        if(dbserver == null)
        {
            return -1;
        }
        
        this.pedimentoNumero = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET pedimentoNumero= '" + numero + "' WHERE id=" + id;
        Statement stmt = dbserver.getConnection().createStatement();
        return stmt.executeUpdate(sql);
    }
        
    public boolean downPurchaseArrival(Database db) throws SQLException 
    {
        String sql = "SELECT purchaseArrival FROM " + MYSQL_AVATAR_TABLE + " WHERE purchaseArrival IS NOT NULL AND id = " + id;
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            purchaseArrival = rs.getDate(1);
            return true;
        }
        else
        {
            purchaseArrival =  null;
            return false;
        }
    }
    
    public Date getPurchaseArrival() 
    {
        return purchaseArrival;
    }
        
    public boolean upCostPurchase(Database db, double costo, Moneda moneda) throws SQLException
    {
        if(id < 1)
        {
            return false; 
        }
        else if(db == null)
        {
            return false;
        }
        
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET costPurchase = " + costo;
        if(moneda == Moneda.MXN)
        {
            sql +=  ",costPurchaseMoney='MXN'";
        }
        else if(moneda == Moneda.USD)
        {
            sql +=  ",costPurchaseMoney='USD'";
        }
        
        sql += " WHERE id = " + id;
        this.costPurchase = costo;
        this.costPurchaseMoney = moneda;
        Statement stmt = db.getConnection().createStatement();
        //System.out.println(sql);
        if(stmt.executeUpdate(sql) == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * @return the estado
     */
    public Estado getEstado() 
    {
        return estado;
    }

    /**
     * @return the purchaseETA
     */
    public Date getPurchaseETA() 
    {
        return purchaseETA;
    }

    /**
     * @return the costPurchase
     */
    public double getCostPurchase() 
    {
        return costPurchase;
    }

    /**
     * @return the costPurchaseMoney
     */
    public Moneda getCostPurchaseMoney() 
    {
        return costPurchaseMoney;
    }
    
    public int upPurchaseArrival(Database db, java.util.Date eta) throws SQLException
    {
        if(id < 1)
        {
            return -1; 
        }
        if(db == null)
        {
            return -1;
        }
        
        this.purchaseArrival = null;
        SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET purchaseArrival = '" + formateador.format(eta) + "' WHERE id=" + id;
        Statement stmt = db.getConnection().createStatement();
        
        return stmt.executeUpdate(sql);
    }
        
        
    public boolean downCostPurchase(Database database) throws SQLException 
    {
        if(database == null)
        {
            return false;
        }
        String sql = "SELECT costPurchase,costPurchaseMoney FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        //System.out.println(sql);
        ResultSet rs = database.query(sql);
        if(rs.next())
        {
            this.costPurchase = rs.getDouble(1);
            if(rs.getString(2) == null)
            {
                costPurchaseMoney = null;
                return false;
            }
            else if(rs.getString(2).equals("MXN"))
            {
                costPurchaseMoney = Moneda.MXN;
            }
            else if(rs.getString(2).equals("USD"))
            {
                costPurchaseMoney = Moneda.USD;
            }
            return true;
        }
        else
        {
            //this.cantidad = -1;
            return false;
        }
    }
        
        
    public int upPurchaseETA(Database db, java.util.Date eta) throws SQLException
    {
        if(id < 1)
        {
            return -1; 
        }        
        if(db == null)
        {
            return -1;
        }
        
        this.purchaseETA = null;
        SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET purchaseETA='" + formateador.format(eta) + "' WHERE id=" + id;
        Statement stmt = db.getConnection().createStatement();
        
        return stmt.executeUpdate(sql);
    }
    
    
    public Boolean nextItemFree(Database db,String number) throws SQLException 
    {
        clean();
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT idFlow FROM "  + MYSQL_AVATAR_TABLE_ITEM + " WHERE number='" + number + "'";
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            this.id = rs.getInt(1);
            upEstado(db, Estado.VENDIDO);
            return true;
        }
        else
        {
            this.id = -1;
            return false;
        }
    }
        
    public Return downEstado(Database database) throws SQLException 
    {
        if(database == null)
        {
            return new Return(false,"No se encontro el ID '" + id + "'");
        }
        String sql = "SELECT estado FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;        
        ResultSet rs = database.query(sql);
        if(rs.next())
        {
            String str = rs.getString(1);
            if(str == null)
            {
                estado = null;
            }
            else if(str.equals("V"))
            {
               estado = Estado.VENDIDO;
            }
            else if(str.equals("L"))
            {
               estado = Estado.LIBRE;
            }
            else if(str.equals("A"))
            {
               estado = Estado.APARTADO;
            }
            else
            {
                estado = null;
                return new Return(false,"No se el codigo de estado '" + str +"'");
            }
            return new Return(true);
        }
        else
        {
            this.estado = null;
            return new Return(false,"No se encontro el ID '" + id + "'");
        }
    }
    
    public Return upEstado(Database connection, Estado estado) throws SQLException
    {
        if(id < 1)
        {
            return new Return(false,"ID invalido."); 
        }
        if(connection == null)
        {
            return new Return(false,"connection is null");
        }
        if(estado == null)
        {
            return new Return(false,"serie is null");
        }
        this.estado = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET estado=";
        if(estado == Estado.VENDIDO)
        {
            sql += "'V'";
        }
        else if(estado == Estado.LIBRE)
        {
            sql += "'L'";
        }
        else if(estado == Estado.APARTADO)
        {
             sql += "'A'";
        } 
        else
        {
            return new Return(false,"Estado '" + estado + "' es desconocido");
        }
        sql +=" WHERE id=" + id;
        
        Statement stmt = connection.getConnection().createStatement();
        
        return new Return(true, stmt.executeUpdate(sql));
    }
    
    public boolean isAduanaActive()
    {
        return false;
    }
    
    public boolean downCostSale(Database database) throws SQLException 
    {
        if(database == null)
        {
            return false;
        }
        String sql = "SELECT costSale,costSaleMoney FROM " + MYSQL_AVATAR_TABLE + " WHERE costSale IS NOT NULL AND costSaleMoney IS NOT NULL AND id = " + id;
        //System.out.println(sql);
        ResultSet rs = database.query(sql);
        if(rs.next())
        {
            this.costSale = rs.getDouble(1);
            if(rs.getString(2).equals("MXN"))
            {
                costSaleMoney = Moneda.MXN;
            }
            else if(rs.getString(2).equals("USD"))
            {
                costSaleMoney = Moneda.USD;
            }
            return true;
        }
        else
        {
            //this.cantidad = -1;
            return false;
        }
    }
    
    public boolean upCostSale(Database db, double costo, Moneda moneda) throws SQLException
    {
        if(id < 1)
        {
            return false; 
        }
        else if(db == null)
        {
            return false;
        }
        
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET costSale = " + costo;
        if(moneda == Moneda.MXN)
        {
            sql +=  ",costSaleMoney='MXN'";
        }
        else if(moneda == Moneda.USD)
        {
            sql +=  ",costSaleMoney='USD'";
        }
        
        sql += " WHERE id = " + id;
        this.costSale = costo;
        this.costSaleMoney = moneda;
        Statement stmt = db.getConnection().createStatement();
        //System.out.println(sql);
        if(stmt.executeUpdate(sql) == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public static List<Flow> getCantidad(Database dbserver,String number,int cantidad,Estado status) throws SQLException
    {
        if(cantidad < 1) return null;
        
        String sql = "SELECT idFlow FROM Item_Resolved WHERE number = '" + number + "' ";
        if(Estado.LIBRE == status)
        {
            sql += " AND estado = 'L'";
        }        
        sql += " LIMIT " + cantidad;        
        ResultSet rs = dbserver.query(sql); 
        //System.out.println(sql);
        List<Flow> ls = new ArrayList<>();
        while(rs.next())
        {
            ls.add(new Flow(rs.getInt(1)));
        }
        return ls;
    }
    
    /**
     * @return the cantidad
     */
    /*public short getCantidad() 
    {
        return cantidad;
    }*/
    
    public boolean downCantidad(Database database) throws SQLException 
    {
        if(database == null)
        {
            return false;
        }
        String sql = "SELECT cantidad FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        //System.out.println(sql);
        ResultSet rs = database.query(sql);
        if(rs.next())
        {
            //this.cantidad = rs.getShort(1);
            return true;
        }
        else
        {
            //this.cantidad = -1;
            return false;
        }
    }
    
    public boolean upCantidad(Database db, int cantidad) throws SQLException
    {
        if(id < 1)
        {
            return false; 
        }
        if(db == null)
        {
            return false;
        }
        //this.cantidad = 0;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET cantidad=" + cantidad + " WHERE id=" + id;
        Statement stmt = db.getConnection().createStatement();
        if(stmt.executeUpdate(sql) == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    } 
       
    
    public Flow() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Descar una instancia convertible hacia Forklift,Batttery,etc.
     * @param connection
     * @return
     * @throws SQLException 
     */
    public boolean downItem(Database connection) throws SQLException 
    {
        if(connection == null)
        {
            throw new FailResultOperationException("Connection is null.");
        }
        String sql = "SELECT idItem FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        //System.out.println(sql);
        ResultSet rs = connection.query(sql);
        if(rs.next())
        {
            Titem tt = new Titem(rs.getInt(1));
            if(Forklift.isForklift(connection, tt))
            {
                this.item = new Forklift(rs.getInt(1));
            }
            else if(Battery.isBattery(connection, tt))
            {
                this.item = new Battery(rs.getInt(1));
            }
            else if(Charger.isCharger(connection, tt))
            {
                this.item = new Charger(rs.getInt(1));
            }
            else if(Mina.isMina(connection, tt))
            {
                this.item = new Mina(rs.getInt(1));
            }
            else if(Item.isItem(connection, tt))
            {
                this.item = new Item(rs.getInt(1));
            }
            else
            {
                //System.out.println("Not found Flow Item = " + id);
                return false;
            }
            //System.out.println("Found Flow Item = " + id);
            return true;
        }
        else
        {
            this.item = null;
            throw new FailResultOperationException("No se encontro ningun registro para el ID = " + id + " [" + sql + "]");
        }        
    }

    /**
     * Asigna un ID al azar
     * @param db
     * @return
     * @throws SQLException 
     */
    public Boolean selectMinaRandom(Database db) throws SQLException 
    {
        clean();
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT idFlow FROM  " + MYSQL_AVATAR_TABLE_MINA + " ORDER BY RAND() LIMIT 1";
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
    
    /**
     * Asigna un ID al azar
     * @return
     * @throws SQLException 
     */
    public Boolean selectChargerRandom(Database db) throws SQLException 
    {
        clean();
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT idFlow FROM  " + MYSQL_AVATAR_TABLE_CHARGER + " ORDER BY RAND() LIMIT 1";
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
    
    /**
     * Asigna un ID al azar
     * @return
     * @throws SQLException 
     */
    public Boolean selectBatteryRandom(Database db) throws SQLException 
    {
        clean();
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT idFlow FROM  " + MYSQL_AVATAR_TABLE_BATTERY + " ORDER BY RAND() LIMIT 1";
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
    
    /**
     * Asigna un ID al azar
     * @return
     * @throws SQLException 
     */
    public Boolean selectForkliftRandom(Database db) throws SQLException 
    {
        clean();
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT idFlow FROM  " + MYSQL_AVATAR_TABLE_FORKLIFT + " ORDER BY RAND() LIMIT 1";
        //System.out.println(sql);
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

        /**
     * Asigna un ID al azar
     * @param db
     * @param number
     * @return
     * @throws SQLException 
     */
    public Boolean selectForkliftNumber(Database db,String number) throws SQLException 
    {
        clean();
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT idFlow FROM "  + MYSQL_AVATAR_TABLE_FORKLIFT + " WHERE idT > 0 AND number='" + number + "'";
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
    
    /**
     * Asigna un ID al azar
     * @param db
     * @param number
     * @return
     * @throws SQLException 
     */
    public Boolean selectTitemNumber(Database db,String number) throws SQLException 
    {
        clean();
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT idFlow FROM "  + MYSQL_AVATAR_TABLE_TITEM + " WHERE idT > 0  and number='" + number + "'";
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
    public Boolean selectItemNumber(Database db,String number) throws SQLException 
    {
        clean();
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT idFlow FROM "  + MYSQL_AVATAR_TABLE_ITEM + " WHERE number='" + number + "'";
        System.out.println(sql);
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
    
    /**
     * Asigna un ID al azar
     * @return
     * @throws SQLException 
     */
    public Boolean selectTitemRamdom(Database db) throws SQLException 
    {
        clean();
        if(db == null)
        {
            return false;
        }
        String sql = "SELECT idFlow FROM  " + MYSQL_AVATAR_TABLE_TITEM + " ORDER BY RAND() LIMIT 1";
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
    
    /**
     * 
     * @param database
     * @return
     * @throws SQLException 
     */
    public boolean downSerie(Database database) throws SQLException 
    {
        if(database == null)
        {
            return false;
        }
        String sql = "SELECT activeSerie,serie FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        //System.out.println(sql);
        ResultSet rs = database.query(sql);
        if(rs.next())
        {
            if(rs.getString(1).equalsIgnoreCase("Y")) 
            {
                this.activeSerie = true;    
                this.serie = rs.getString(2);            
            }
            else
            {
                this.activeSerie = false;
                this.serie = null;
            }
            //System.out.println("Sereie : " + this.serie);
            return true;
        }
        else
        {
            this.serie = null;
            this.activeSerie = false;
            //System.out.println("Sereie : N/A");
            return false;
        }
    }
    
    /**
     * Actualiza el campo StockItem.mode
     * @param connection
     * @param po
     * @return
     * @throws SQLException 
     */
    public int upPO(Connection connection, PO po) throws SQLException
    {
        if(id < 1)
        {
            return -1; 
        }
        
        if(connection == null)
        {
            return -1;
        }
        
        if(po == null)
        {
            return -1;
        }
        
        this.po = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET po=" + po.getID() + " WHERE id=" + id;
        Statement stmt = connection.createStatement();
        return stmt.executeUpdate(sql);
    }
    
    /**
     * Asigna un ID al azar
     * @param connection
     * @param id
     * @return
     * @throws SQLException 
     */
    public Boolean select(Connection connection, int id) throws SQLException 
    {
        clean();
        if(connection == null)
        {
            return false;
        }
        String sql = "SELECT id FROM  " + MYSQL_AVATAR_TABLE + " WHERE ID = " + id;
        Statement stmt = connection.createStatement();
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
     * Actualiza el campo StockItem.mode
     * @param connection
     * @param quotation
     * @return
     * @throws SQLException 
     */
    public int upQuotation(Connection connection, Quotation quotation) throws SQLException
    {
        if(id < 1)
        {
            return -1; 
        }
        if(connection == null)
        {
            return -1;
        }
        if(quotation == null)
        {
            return -1;
        }
        this.quotation = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET quotation=" + quotation.getID() + " WHERE id=" + id;
        Statement stmt = connection.createStatement();
        return stmt.executeUpdate(sql);
    }
    
    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public boolean downPO(Connection connection) throws SQLException 
    {
        if(connection == null)
        {
            return false;
        }
        String sql = "SELECT po FROM " + MYSQL_AVATAR_TABLE + " WHERE po IS NOT NULL AND id = " + id;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            this.po = new PO(rs.getInt(1));
            return true;
        }
        else
        {
            this.po = null;
            return false;
        }        
    }
    
    /**
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public boolean downQuotation(Connection connection) throws SQLException 
    {
        if(connection == null)
        {
            return false;
        }
        String sql = "SELECT quotation FROM " + MYSQL_AVATAR_TABLE + " WHERE quotation IS NOT NULL AND id = " + id;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            this.quotation = new Quotation(rs.getInt(1));
            return true;
        }
        else
        {
            this.quotation = null;
            return false;
        }        
    }
    
    
    public boolean delete(Connection connection) throws SQLException
    {
        if(connection == null)
        {
            return false;
        }
        if(item == null)
        {
            return false;
        }
        
        String sql = "DELETE  FROM " + MYSQL_AVATAR_TABLE + " WHERE id=" + id;
        Statement stmt = connection.createStatement();
        //System.out.println(sql);
        int afected = stmt.executeUpdate(sql);
        if(afected == 1)
        {
            return true;
        }
        return false;         
    }

    /**
     * Asigna un ID al azar
     * @param connection
     * @return
     * @throws SQLException 
     */
    public Return<Integer> selectRandom(Connection connection) throws SQLException 
    {
        clean();
        if(connection == null)
        {
            return new Return<>(false,"Connection is null.");
        }
        String sql = "SELECT id FROM  " + MYSQL_AVATAR_TABLE + " ORDER BY RAND() LIMIT 1";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            this.id = rs.getInt(1);
            return new Return<>(true);
        }
        else
        {
            this.id = -1;
            return new Return<>(false);
        }
    }


    /**
     * Actualiza el campo StockItem.mode
     * @param connection
     * @param serie
     * @return
     * @throws SQLException 
     */
    public Return<Integer> upSerie(Connection connection, String serie) throws SQLException
    {
        if(id < 1)
        {
            return new Return(false,"ID invalido."); 
        }
        if(connection == null)
        {
            return new Return(false,"connection is null");
        }
        if(serie == null)
        {
            return new Return(false,"serie is null");
        }
        this.serie = null;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET serie='" + serie + "', activeSerie = 'Y' WHERE id=" + id;
        Statement stmt = connection.createStatement();
        return new Return<>(true, stmt.executeUpdate(sql));
    }
    
    /**
     * 
     * @param id 
     */
    public Flow(int id)
    {
        this.id = id;
    }
    
    /**
     * @return the item
     */
    public Item getItem() {
        return item;
    }

    /**
     * @return the fhIngreso
     */
    public Date getFhIngreso() {
        return fhIngreso;
    }

    /**
     * @param fhIngreso the fhIngreso to set
     */
    public void setFhIngreso(Date fhIngreso) {
        this.fhIngreso = fhIngreso;
    }

    
    /**
     * @return the activeSerie
     */
    public boolean isActiveSerie() {
        return activeSerie;
    }

    /**
     * @return the serie
     */
    public String getSerie() 
    {
        return serie;
    }
        
    /**
     * Inserta el item indicado en el constructor.
     * @param connection
     * @param ingreso
     * @param activeSerie
     * @param serie
     * @param item
     * @return
     * @throws SQLException 
     */
    public Return insert(Connection connection,java.util.Date ingreso,boolean activeSerie,String serie,Item item) throws SQLException
    {
        clean();
        if(id > 0)
        {
            return new Return(Return.Status.FAIL,"Connection is NULL");
        }
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is NULL");
        }
        if(item == null)
        {
            return new Return(Return.Status.FAIL,"Item is NULL");
        }
        else if(item.getID() < 1)
        {
            return new Return(Return.Status.FAIL,"Item tiene ID invalido.");
        }                    
        if(activeSerie == true)
        {
            if(serie == null)
            {
                throw new InvalidParameterException("serie no puede ser null.");
            }
        }
        
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + "(idItem,fhIngreso";
        //if(activeSerie)
        //{
            sql = sql + ",activeSerie,serie";
        //}
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sql = sql + ") VALUES("  + item.getID() + ",'" + sdf.format(ingreso);
        if(activeSerie)
        {
            sql = sql + "','Y','" + serie + "')";
        }
        else
        {
            sql = sql + "','N',NULL)";
        }
        //System.out.println(sql);
        Statement stmt = connection.createStatement();
        int affected = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        if(affected != 1)
        {
            return new Return(Return.Status.FAIL,"Se afectaron '" + affected + "' registro(s) lo cual es incorrecto.");
        }
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next())
        {
            id = rs.getInt(1);
            return new Return(Return.Status.DONE, rs.getInt(1));
        }
        else
        {
            id = -1;
            return new Return(Return.Status.FAIL,"No genero ID para el registro.");
        }
    }

    /**
     * @return the id
     */
    public int getID() {
        return id;
    }

    private void clean() 
    {
        id = 0;
        item = null;
        fhIngreso = null;
        activeSerie = false;
        serie = null;
    }

    public boolean selectLast(Connection connection) throws SQLException 
    {
        clean();
        if(connection == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " ORDER BY id DESC LIMIT 0, 1";
        Statement stmt = connection.createStatement();
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
     * 
     * @param connection
     * @return
     * @throws SQLException 
     */
    public boolean downItem(Connection connection) throws SQLException 
    {
        if(connection == null)
        {
            return false;
        }
        String sql = "SELECT idItem FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            this.item = new Item(rs.getInt(1));
            return true;
        }
        else
        {
            this.item = null;
            return false;
        }        
    }

    /**
     * @return the quotation
     */
    public Quotation getQuotation() {
        return quotation;
    }

    /**
     * @return the po
     */
    public PO getPO() {
        return po;
    }

    public boolean download(Database db) throws SQLException 
    {
        String sql = "SELECT quotation, purchaseETA FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        ResultSet rs = db.query(sql);
        if(rs.next())
        {
            this.downItem(db);
            this.downPO(db.getConnection());
            if(rs.getInt(1) != 0)
            {
                this.quotation = new Quotation(rs.getInt(1));
                this.quotation.download(db);                
            }
            this.downSerie(db);
            this.purchaseETA = rs.getDate(2);
            return true;
        }
        else
        {
            return false;
        }
       
    }

    /**
     * @return the costSale
     */
    public double getCostSale() 
    {
        return costSale;
    }

    /**
     * @return the costSaleMoney
     */
    public Moneda getCostSaleMoney() 
    {
        return costSaleMoney;
    }

    /**
     * @return the adunanaAduana
     */
    public String getPedimentoAduana() 
    {
        return pedimentoAduana;
    }

    /**
     * @return the adunaNumero
     */
    public String getPedimentoNumero() 
    {
        return pedimentoNumero;
    }

    /**
     * @return the aduanaFecha
     */
    public Date getPedimentoFecha() 
    {
        return pedimentoFecha;
    }
}
