
package process;

import SIIL.CN.Engine.Clause;
import SIIL.CN.Engine.DBFRecord;
import SIIL.CN.Engine.Operator;
import SIIL.CN.Sucursal;
import SIIL.CN.Tables.ARTICULO;
import SIIL.CN.Tables.CN60;
import SIIL.CN.Tables.COTIZACI;
import SIIL.CN.Tables.COTIZA_R;
import SIIL.CN.Tables.FACTURA;
import SIIL.CN.Tables.RENGLON;
import SIIL.Server.Database;
import SIIL.Server.Person;
import SIIL.core.Office;
import core.Folio;
import core.Renglon;
import database.mysql.sales.Quotation;
import database.mysql.stock.Item;
import database.mysql.stock.Service;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import stock.Flow;
import stock.Refection;

/**
 * @version 0.1
 * @author Azael Reyes
 */
public class Operational
{
    public static final String MYSQL_AVATAR_TABLE = "ProcessOperational";
    
    protected int id;
    private State state;
    private String serie;
    private int folio;
    private Date fhFolio;
    private String strFolio;
    private String type;
    private Office office;
    private char flag;
    private double total;
    private Moneda monedaLocal;
    
    public enum Type
    {
        PurchaseQuote,
        PurchaseOrder,
        PurchaseRemision,
        SalesQuote,
        SalesRemision,
        SalesInvoice
    }
    
    public enum TablaRenglon
    {
        REMISION,
        FACTURA,
        ORDENCONMPRA,
        COTIZACION
    }
    
    public Boolean downOffice(Database database) throws SQLException 
    {
        if(database == null)
        {
            return false;
        }
        String sql = "SELECT office FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        Statement stmt = database.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            office = new Office(rs.getInt(1));
            return true;
        }
        else
        {
            return false;
        }
    }
    
    protected SIIL.CN.Records.ARTICULO getCN60Descripcion(ARTICULO tbArticulo, String number) 
    {
        List<DBFRecord> list = tbArticulo.readWhere(new Clause(0,Operator.TRIMEQUAL,number),1,'R');
        if(list.size() > 0)
        {
            return new SIIL.CN.Records.ARTICULO(list.get(0));
        }
        else
        {
            return null;
        }
    }
        
    public Return fromCNRenglones(Database dbserver, TablaRenglon tablaRenglon) throws SQLException
    {
        if(!CN60.isWorking())
        {
            return new Return(false,"No hay CN instalado en la maquina actual");
        }
        
        CN60 tbMain = null;
        try 
        {
            switch (getOffice().getCode()) 
            {
                case "bc.tj":
                    if(tablaRenglon == TablaRenglon.REMISION)
                    {
                        tbMain = new FACTURA(Sucursal.BC_Tijuana);
                    }
                    else if(tablaRenglon == TablaRenglon.COTIZACION)
                    {
                        tbMain = new COTIZACI(Sucursal.BC_Tijuana);
                    }
                    else
                    {
                        return new Return(false);
                    }
                    break;
                case "bc.mx":
                    if(tablaRenglon == TablaRenglon.REMISION)
                    {
                        tbMain = new FACTURA(Sucursal.BC_Mexicali);
                    }
                    else if(tablaRenglon == TablaRenglon.COTIZACION)
                    {
                        tbMain = new COTIZACI(Sucursal.BC_Mexicali);
                    }
                    else
                    {
                        return new Return(false);
                    }
                    break;
                case "bc.ens":
                    if(tablaRenglon == TablaRenglon.REMISION)
                    {
                        tbMain = new FACTURA(Sucursal.BC_Ensenada);
                    }
                    else if(tablaRenglon == TablaRenglon.COTIZACION)
                    {
                        tbMain = new COTIZACI(Sucursal.BC_Ensenada);
                    }
                    else
                    {
                        return new Return(false);
                    }
                    break;
                default:
                    return new Return(false,"No CN para la base de datos '" + getOffice().getCode() + "'");
            }
        }
        catch (IOException ex) 
        {
            return new Return(false,ex.getMessage());
        }   
        
        
        //Importacion de renglones desde en CN
        CN60 tbRenglones = null;
        try 
        {
            switch (getOffice().getCode()) 
            {
                case "bc.tj":
                    if(tablaRenglon == TablaRenglon.REMISION)
                    {
                        tbRenglones = new RENGLON(Sucursal.BC_Tijuana);
                    }
                    else if(tablaRenglon == TablaRenglon.COTIZACION)
                    {
                        tbRenglones = new COTIZA_R(Sucursal.BC_Tijuana);
                    }
                    else
                    {
                        return new Return(false);
                    }
                    break;
                case "bc.mx":
                    if(tablaRenglon == TablaRenglon.REMISION)
                    {
                        tbRenglones = new RENGLON(Sucursal.BC_Mexicali);
                    }
                    else if(tablaRenglon == TablaRenglon.COTIZACION)
                    {
                        tbRenglones = new COTIZA_R(Sucursal.BC_Mexicali);
                    }
                    else
                    {
                        return new Return(false);
                    }
                    break;
                case "bc.ens":
                    if(tablaRenglon == TablaRenglon.REMISION)
                    {
                        tbRenglones = new RENGLON(Sucursal.BC_Ensenada);
                    }
                    else if(tablaRenglon == TablaRenglon.COTIZACION)
                    {
                        tbRenglones = new COTIZA_R(Sucursal.BC_Ensenada);
                    }
                    else
                    {
                        return new Return(false);
                    }
                    break;
                default:
                    return new Return(false,"No CN para la base de datos '" + getOffice().getCode() + "'");
            }
        }
        catch (IOException ex) 
        {
            return new Return(false,ex.getMessage());
        }        
        
        ARTICULO tbArticulo = null;
        try 
        {
            switch (getOffice().getCode()) 
            {
                case "bc.tj":
                    tbArticulo = new ARTICULO(Sucursal.BC_Tijuana);
                    break;
                case "bc.mx":
                    tbArticulo = new ARTICULO(Sucursal.BC_Mexicali);
                    break;
                case "bc.ens":
                    tbArticulo = new ARTICULO(Sucursal.BC_Ensenada);
                    break;
                default:
                    return new Return(false,"No CN para la base de datos '" + getOffice().getCode() + "'");
            }
        }
        catch (IOException ex) 
        {
            return new Return(false,ex.getMessage());
        }
        
        //
        List<DBFRecord> renglones = null;
        List<DBFRecord> mainreng = null;
        if(tablaRenglon == TablaRenglon.REMISION)
        {
            mainreng = tbMain.readWhere(new Clause(0,Operator.TRIMEQUAL,String.valueOf(folio)), 0, 'R');
            renglones = tbRenglones.readWhere(new Clause(0,Operator.TRIMEQUAL,String.valueOf(folio)), 0, 'R');
        }
        else if(tablaRenglon == TablaRenglon.COTIZACION)
        {
            mainreng = tbMain.readWhere(new Clause(0,Operator.TRIMEQUAL,String.valueOf(folio)), 0, 'R');
            renglones = tbRenglones.readWhere(new Clause(0,Operator.TRIMEQUAL,String.valueOf(folio)), 0, 'R');
        }
        else
        {
            return new Return(false);
        }
        if(mainreng.size() == 0)
        {
            return new Return(false,"El folio '" + folio + "' no existe en la tal principal.");
        }
        if(renglones.size() == 0)
        {
            return new Return(false,"No hay renglones para este folio.");
        }
        Item item = null;
        List<Item> items = new ArrayList<>();
        
        int gropRenglon = Row.getMaximoGroupRenglon(dbserver, this);
        if(gropRenglon == 0)
        {
            Return ret = Renglon.clear(dbserver,this);
            if(ret.isFail()) return ret;
        }
        
        for(DBFRecord rec: renglones)
        {
            String number = null;
            if(tablaRenglon == TablaRenglon.REMISION)
            {
                number = rec.getString(3).trim();
            }
            else if(tablaRenglon == TablaRenglon.COTIZACION)
            {
                number = rec.getString(1).trim();
            }
            
            Return<Integer> retExist = Item.isExist(dbserver.getConnection(),number);
            if(retExist.isFail())
            {
                //si no existe enconces
                //Descargar la descripcion desde el CN60
                SIIL.CN.Records.ARTICULO art = getCN60Descripcion(tbArticulo,number);
                String description = art.getDescription();
                String unidad = art.getReciben();
                unidad = unidad.trim().toLowerCase();                
                if(unidad.equals("servicio"))
                {
                    item = new Service(-1);
                    Return<Integer> retIse = ((Service)item).insert(dbserver.getConnection(),number, description,Item.Type.SERVICIO,false,null); 
                }
                else if(unidad.equals("pieza") | unidad.equals("piezas") | unidad.equals("pza.") | unidad.equals("pieza.") | unidad.equals("piesa")  | unidad.equals("piesas") | unidad.equals("piezas.") | unidad.equals("pza") | unidad.equals("cajas")  | unidad.equals("caja") | unidad.equals("cubeta") | unidad.equals("cubeta."))
                {
                    item = new Refection(-1);
                    Return<Integer> retIse = ((Refection)item).insert(dbserver.getConnection(),number, description,false,null);
                }
                else
                {
                    throw new InvalidParameterException("Tipo de elemento desconocido : " + number + " - " + unidad);
                }
                
                if(tablaRenglon == TablaRenglon.REMISION)
                {
                    item.temporalCantidad = Double.parseDouble(rec.getString(6));
                }
                else if(tablaRenglon == TablaRenglon.COTIZACION)
                {
                    item.temporalCantidad = Double.parseDouble(rec.getString(2));
                    item.temporaryCost = Double.parseDouble(rec.getString(6).trim());
                    if(mainreng.get(0).getString(5).trim().equals("D"))
                    {
                        item.temporaryMoneda = Moneda.USD;
                    }
                    else if(mainreng.get(0).getString(5).trim().equals("P"))
                    {
                        item.temporaryMoneda = Moneda.MXN;
                    }                    
                }                
                unidad = art.getReciben();
                item.upUnidad(dbserver, unidad);
                items.add(item);                
            }
            else
            {
                //ya existe.
                Return<Integer> retnmew = Refection.isExist(dbserver.getConnection(), number);
                if(retnmew.isFlag())
                {
                    item = new Refection(retnmew.getParam());
                }
                else if(Service.isExist(dbserver.getConnection(), number).isFlag())
                {
                    item = new Service(Service.isExist(dbserver.getConnection(),number).getParam());
                }
                else
                {
                    item = new Item(Item.isExist(dbserver.getConnection(),number).getParam());
                }
                if(tablaRenglon == TablaRenglon.REMISION)
                {
                    item.temporalCantidad = Double.parseDouble(rec.getString(6));
                }
                else if(tablaRenglon == TablaRenglon.COTIZACION)
                {
                    item.temporalCantidad = Double.parseDouble(rec.getString(2));
                    item.temporaryCost = Double.parseDouble(rec.getString(6).trim());
                    if(rec.getString(5).trim().equals("D"))
                    {
                        item.temporaryMoneda = Moneda.USD;
                    }
                    else if(rec.getString(5).trim().equals("P"))
                    {
                        item.temporaryMoneda = Moneda.MXN;
                    }
                }
                items.add(item);
                continue;
            }
        }
        
        //Comproposito de saber cuales estan en la base de datos actualmente
        Map<String,Renglon> actualRows = new HashMap<>();
        List<Renglon> rengs = Renglon.select(dbserver, this, null);
        for(Renglon ren : rengs)
        {
            actualRows.put(ren.getNumber(), ren);
        }
        
        //Agregar las piezas que faltan al flow
        Flow flow = null;
        for(int k = 0; k < items.size(); k++)
        {
            items.get(k).downNumber(dbserver.getConnection());
            if(!actualRows.containsKey(items.get(k).getNumber()))//verificar si existe el item correspondiente al flow.
            {
                flow = null;
                Renglon renglon = new Renglon();
                for(int i = 0; i < items.get(k).temporalCantidad; i++)
                {
                    flow = new Flow(-1);
                    Return<Integer> retFlow = flow.insert(dbserver.getConnection(), dbserver.getTimestamp(), false, null, items.get(k));                                        
                    flow.upCostSale(dbserver, item.temporaryCost, item.temporaryMoneda);
                    renglon.add(flow);
                    if(this instanceof Quotation)
                    {
                        flow.upQuotation(dbserver.getConnection(), (Quotation) this);
                    }
                }
                Renglon.insert(dbserver, this, renglon);
                items.get(k).downNumber(dbserver.getConnection());
                actualRows.put(items.get(k).getNumber(), renglon) ;//se registran las que se estan agregando.
            }
        }
        
        //Ya que se cuales son las actuales elimino el todas las que estan en el CN, las que quedan son las que hay que eliminar del tool.
        for(DBFRecord rec : renglones)
        {
            String number = null;
            if(tablaRenglon == TablaRenglon.REMISION)
            {
                number = rec.getString(3).trim();
            }
            else if(tablaRenglon == TablaRenglon.COTIZACION)
            {
                number = rec.getString(1).trim();
            }
            actualRows.remove(number);//eliminar todas las que estan en el CN
        }
        
        //las que quedan son las que sobran y se deven eliminar
        Collection<Renglon> deletedRows = actualRows.values();
        for(Renglon ren : deletedRows)
        {
            ren.delete(dbserver);
        }
        
        tbRenglones.close();
        tbArticulo.close();        
        return new Return(true);
    }
        
    public Return upFolio(Database dbserver,int folio) throws SQLException
    {
        if(id < 1)
        {
            return new Return(false,"ID invalido."); 
        }
        if(dbserver == null)
        {
            return new Return(false,"No se permite valores nulos en los parametros");
        }
        this.folio = -1;
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET folio=" + folio + " WHERE id=" + id;
        //System.out.println(sql);
        Statement stmt = dbserver.getConnection().createStatement();
        return new Return(true, stmt.executeUpdate(sql));
    }
    
    public Return upSerie(Database dbserver,String serie) throws SQLException
    {
        if(id < 1)
        {
            return new Return(false,"ID invalido."); 
        }
        if(dbserver == null)
        {
            return new Return(false,"No se permite valores nulos en los parametros");
        }
        this.serie = null;
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET serie='" + serie + "' WHERE id=" + id;
        //System.out.println(sql);
        Statement stmt = dbserver.getConnection().createStatement();
        return new Return(true, stmt.executeUpdate(sql));
    }
        
    public Boolean select(Database dbserver, Office office, String folio, Type type) throws SQLException
    {
        clean();        
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE flag='A' AND  serie = '" + office.getSerieOffice(Office.Platform.CN60) + "' AND folio = " + folio + " AND type = '";
        if(type == Type.PurchaseQuote)
        {
            sql += "PQ'";
        }
        else if(type == Type.PurchaseOrder)
        {
            sql += "PO'";
        }
        else if(type == Type.PurchaseRemision)
        {
            sql += "PR'";
        }
        else if(type == Type.SalesQuote)
        {
            sql += "SQ'";
        }
        else if(type == Type.SalesRemision)
        {
            sql += "SR'";
        }
        else if(type == Type.SalesInvoice)
        {
            sql += "SI'";
        }
        sql += " AND office = " + office.getID();
        ResultSet rs = dbserver.query(sql);
        //System.out.println(sql);
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
        
    public static Return exist(Database dbserver, String serie, String folio, Type type) throws SQLException
    {
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE flag='A' AND folio = " + folio + " AND type = '";
        if(type == Type.PurchaseQuote)
        {
            sql += "PQ'";
        }
        else if(type == Type.PurchaseOrder)
        {
            sql += "PO'";
        }
        else if(type == Type.PurchaseRemision)
        {
            sql += "PR'";
        }
        else if(type == Type.SalesQuote)
        {
            sql += "SQ'";
        }
        else if(type == Type.SalesRemision)
        {
            sql += "SR'";
        }
        else if(type == Type.SalesInvoice)
        {
            sql += "SI'";
        }
        sql += " AND serie = '" + serie + "' ORDER BY id DESC";
        ResultSet rs = dbserver.query(sql);
        //System.out.println(sql);
        if(rs.next())
        {
            return new Return(true,rs.getInt(1));
        }
        return new Return(false,"No existe el folio con folio " + folio);
    }
    
    public static Return exist(Database dbserver, Office office, String folio, Type type) throws SQLException
    {
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE flag='A' AND folio = " + folio + " AND type = '";
        if(type == Type.PurchaseQuote)
        {
            sql += "PQ'";
        }
        else if(type == Type.PurchaseOrder)
        {
            sql += "PO'";
        }
        else if(type == Type.PurchaseRemision)
        {
            sql += "PR'";
        }
        else if(type == Type.SalesQuote)
        {
            sql += "SQ'";
        }
        else if(type == Type.SalesRemision)
        {
            sql += "SR'";
        }
        else if(type == Type.SalesInvoice)
        {
            sql += "SI'";
        }
        sql += " AND office = " + office.getID() + " ORDER BY id DESC";
        ResultSet rs = dbserver.query(sql);
        //System.out.println(sql);
        if(rs.next())
        {
            return new Return(true,rs.getInt(1));
        }
        return new Return(false,"No existe el folio con folio " + folio);
    }
    
    
    /**
     * @return the total
     */
    public double getTotal() {
        return total;
    }
    
    public Return downTotal(Database connection) throws SQLException 
    {
        if(connection == null)
        {
            return new Return<>(false,"Connection is null.");
        }
        String sql = "SELECT total,monedaLocal FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            total = rs.getDouble(1);
            if(rs.getString(2) == null)
            {
                monedaLocal = null;
                return new Return(false,"La moneda '" + rs.getString(2) + "' es desconocida.");                
            }
            if(rs.getString(2).equals("USD"))
            {
                monedaLocal = Moneda.USD;
            }
            else if(rs.getString(2).equals("MXN"))
            {
                monedaLocal = Moneda.MXN;
            }
            else
            {
                monedaLocal = null;
                return new Return(false,"La moneda '" + rs.getString(2) + "' es desconocida.");
            }
            return new Return(true);
        }
        else
        {
            return new Return(false);
        }
    }
    
    public Return upTotal(Database connection,double total) throws SQLException
    {
        if(id < 1)
        {
            return new Return(false,"ID invalido."); 
        }
        if(connection == null)
        {
            return new Return(false,"No se permite valores nulos en los parametros");
        }
        this.total = -1.0;
        String sql = "UPDATE  " + MYSQL_AVATAR_TABLE + " SET total=" + total + " WHERE id=" + id;
        //System.out.println(sql);
        Statement stmt = connection.getConnection().createStatement();
        return new Return(true, stmt.executeUpdate(sql));
    }
    
    public static Folio splitFolio(String str)
    {
        return new Folio(str);
    }
       
    
    @Override
    public String toString()
    {
        return getFullFolio();
    }
    
    /**
     * 
     * @param db
     * @param state
     * @return
     * @throws SQLException 
     */
    public boolean upState(Database db, State state) throws SQLException
    {
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET state = " + state.getID() + " WHERE id = " + getID();
        Statement stmt = db.getConnection().createStatement();
        int affected = stmt.executeUpdate(sql);
        if(affected == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
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
    
    public String getFullFolio()
    {        
        String fullfolio = "";
        if(getSerie() != null)
        {
            fullfolio = getSerie();
        }        
        fullfolio += getFolio();
        return fullfolio;
    }
    
    
    public static boolean exist(Connection connection,String serie,int folio) throws SQLException
    {
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE flag='A' AND ";
        if(serie != null)
        {
            sql = sql + " serie = '" + serie + "' AND " ;
        }
        else
        {
            sql = sql + " serie = NULL AND ";
        }
        sql = sql + " folio = " + folio;
        Statement stmt = connection.createStatement();
        //System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            return true;
        }
        return false; 
    }
    
    public static Integer select(Connection connection,String serie,int folio) throws SQLException
    {
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE flag='A' AND ";
        if(serie != null)
        {
            sql = sql + " serie = '" + serie + "' AND " ;
        }
        else
        {
            sql = sql + " serie = NULL AND ";
        }
        sql = sql + " folio = " + folio;
        Statement stmt = connection.createStatement();
        //System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            return rs.getInt(1);
        }
        
        return 0;
    }
    
    /**
     * 
     * @param connection
     * @param serie
     * @param folio
     * @param office
     * @param type
     * @return
     * @throws SQLException 
     */
    public Return<Integer> select(Connection connection,String serie,String folio,Office office,String type) throws SQLException 
    {
        clean();
        if(connection == null)
        {
            throw new InvalidParameterException("Connection is null.");
        }
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE serie = ";
        if(serie == null)
        {
            sql = sql + " NULL AND ";
        }
        sql = sql + " folio = " + folio + " AND office = " + office.getID() + " AND type = " + type;
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
            return new Return<>(true);
        }
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
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " ORDER BY id DESC LIMIT 0, 1";
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
     * 
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
    
    
    public Return upFlag(Connection connection, char flag) throws SQLException
    {
        if(id < 1)
        {
            return new Return(Return.Status.FAIL,"ID invalido."); 
        }
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }            
        String sql = "UPDATE " + MYSQL_AVATAR_TABLE + " SET flag = '" + flag + "' WHERE id = " + id;
        Statement stmt = connection.createStatement();
        return new Return(Return.Status.DONE, stmt.executeUpdate(sql));
    }
    
    public Return nextFolio(Database connection,String type,String serie) throws SQLException
    {        
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        if(type == null)
        {
            return new Return(Return.Status.FAIL,"type is null.");
        }
        
        String sql = "SELECT folio FROM " + MYSQL_AVATAR_TABLE + " WHERE type = '" + type + "' " ;
        if(serie != null)
        {
            sql = sql + " AND serie = '" + serie + "'";
        }
        sql += " ORDER BY folio DESC LIMIT 1 ";
        Statement stmt = connection.getConnection().createStatement();
        //System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            folio = rs.getInt(1) + 1;
            upFolio(connection, folio);
            upSerie(connection, serie);
            return new Return(Return.Status.DONE, folio);
        }
        else
        {
            folio = 1;
            upFolio(connection, folio);
            upSerie(connection, serie);
            return new Return(Return.Status.FAIL, folio);
        }
    }
    
    /**
     * 
     * @param connection
     * @return 
     * @throws java.sql.SQLException 
     */
    public Return download(Database connection) throws SQLException
    {
        if(connection == null)
        {
            return new Return(Return.Status.FAIL,"Connection is null.");
        }
        String sql = "SELECT folio,fhFolio,state,serie FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        Statement stmt = connection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            folio = rs.getInt(1);
            fhFolio = rs.getDate(2);
            state = new State(rs.getInt(3));
            state.download(connection);
            serie = rs.getString(4);
            return new Return(Return.Status.DONE);
        }
        else
        {
            clean();
            return new Return(Return.Status.FAIL,"No se encontro nigun registro para el ID = " + id);
        }    
    }
    
    //public abstract Document execute();
    /**
     * 
     * @param id 
     */
    public Operational(int id)
    {
        clean();
        this.id = id;
    }
    
    /**
     * @return the id
     */
    public int getID() {
        return id;
    }

    /**
     * @return the state
     */
    public State getState() {
        return state;
    }

    /**
     * 
     * @param connection
     * @param office
     * @param state
     * @param operator
     * @param date
     * @param folio Si es mayo que 0 se utiliza este valor, si es igual a cero se genera uno nuevo en otro caso provaca error.
     * @param serie
     * @param type
     * @return
     * @throws SQLException 
     */
    public Return insert(Database connection,Office office,State state,Person operator,Date date,int folio,String serie,String type) throws SQLException
    {      
        clean();
        if(id > 0)
        {
            return new Return(false, "Bab parameter."); 
        }
        if(connection == null)
        {
            return new Return(false, "Bab parameter."); 
        }
        if(state == null)
        {
            return new Return(false, "Bab parameter."); 
        }
        else if(state.getID() < 1)
        {
            return new Return(false, "Bab parameter."); 
        }
        /*if(operator == null)
        {
            return new Return(false, "Bab parameter."); 
        }
        else if(operator.getpID() < 1)
        {
            return new Return(false, "Bab parameter."); 
        }*/
        if(type == null)
        {
            return new Return(false, "Bab parameter."); 
        }
        if(office == null)
        {
            return new Return(false, "Bab parameter."); 
        }
        this.folio = folio;
        if(folio < 0 && serie == null)
        {
            ;//continauar operacion sin crear folio
        }
        else if(this.folio < 0)
        {
            return new Return(false, "Bab parameter."); 
        }
        else if(this.folio == 0)
        {//solicita que se genere uno
            nextFolio(connection, type, serie);
        }
        if(date == null)
        {
            date = connection.getTimestamp();
        }
              
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Random r = new Random();
        int Low = 1;
        int High = 99;
        int Result = r.nextInt(High-Low) + Low;
        String sql = "INSERT INTO " + MYSQL_AVATAR_TABLE + " (state,operator,fhFolio,strFolio,folio,type,office,fhInit";
        sql += ",serie";
        sql = sql + ") VALUES(" + state.getID() + "," ;
        if(operator != null)
        {            
            sql += operator.getpID();
        }
        else
        {
            sql += "NULL";
        }
        sql += ",'" + sdf.format(date) + "',md5(" + date.getTime() + ")";
        if(this.folio > 0)
        {
            sql += "," + this.folio;            
        }
        else
        {
            sql += ",NULL";
        }
        sql += ",'" + type + "'," + office.getID() + ",'" + sdf.format(date) + "'" ;
        if(serie != null)
        {
            sql = sql + ",'" + serie + "'";
        }
        else
        {
            sql = sql + ",NULL";
        }
        sql = sql + ")";
        //System.out.println(sql);
        Statement stmt = connection.getConnection().createStatement();
        int affected = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        if(affected != 1)
        {
            return new Return(false, "Deamasiados registros insertados"); 
        }
        ResultSet rs = stmt.getGeneratedKeys();
        if(rs.next())
        {
            id = rs.getInt(1);
            return new Return(true, id);
        }
        else
        {
            id = -1;
            return new Return(false, "No se genero ID");
        }
    }

    private void clean() 
    {
        id = -1;
        state = null;
        folio = -1;
        serie = null;
        type = null;
        office = null;
    }

    /**
     * @return the serie
     */
    public String getSerie() 
    {
        return serie;
    }

    /**
     * @return the folio
     */
    public String getFolio() 
    {
        return String.valueOf(folio);
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the office
     */
    public Office getOffice() {
        return office;
    }

    /**
     * @return the fhFolio
     */
    public Date getFhFolio() {
        return fhFolio;
    }

    /**
     * @return the strFolio
     */
    public String getStrFolio() {
        return strFolio;
    }

    /**
     * @return the flag
     */
    public char getFlag() {
        return flag;
    }

    /**
     * 
     * @param database
     * @return
     * @throws SQLException 
     */
    public Boolean downSerie(Database database) throws SQLException 
    {
        if(database == null)
        {
            return false;
        }
        String sql = "SELECT serie FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        Statement stmt = database.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            serie = rs.getString(1);
            return true;
        }
        else
        {
            serie = null;
            return false;
        } 
    }
    

    /**
     * 
     * @param database
     * @return
     * @throws SQLException 
     */
    public Boolean downFolio(Database database) throws SQLException 
    {
        if(database == null)
        {
            return false;
        }
        String sql = "SELECT folio FROM " + MYSQL_AVATAR_TABLE + " WHERE id = " + id;
        Statement stmt = database.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next())
        {
            folio = rs.getInt(1);
            return true;
        }
        else
        {
            folio = -1;
            return false;
        }
    }

    /**
     * @return the moneda
     */
    public Moneda getMonedaLocal() 
    {
        return monedaLocal;
    }
}
