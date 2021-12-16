
package core;

import SIIL.Server.Database;
import database.mysql.purchases.order.PO;
import database.mysql.sales.Quotation;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import process.Moneda;
import process.Operational;
import process.Return;
import process.Row;
import stock.Allocated;
import stock.Flow;

/**
 * @version 0.1.0
 * @author Azael Reyes Martinez
 */
public class Renglon 
{    
    private static final String MYSQL_AVATAR_TABLE = "ProcessOperationalRows";
    
    private List<Row> rows;
    private double precioUnitario;
    private double importe;
    private Operational operational;
    private int grouprenglon;

    public enum RenglonExclusion
    {
        SIIL_Y_SEVICICOS
    }    
    
    public Renglon()
    {
        ;
    }
    
    
    public Renglon(Renglon renglon)
    {
        this.rows = renglon.rows;
        this.precioUnitario = renglon.precioUnitario;
        this.importe = renglon.importe;
        this.operational = renglon.operational;
        this.grouprenglon = renglon.grouprenglon;
    }
    
    
    public boolean downCuentaFiscal(Database dbserver) throws SQLException
    {
        for(Row row : rows)
        {
            row.getItem().getItem().downCuentaFiscal(dbserver);
        }
        return true;
    }
    
    public boolean upCuentaFiscal(Database dbserver, String cuenta) throws SQLException
    {
        for(Row row : rows)
        {
            row.getItem().getItem().upCuentaFiscal(dbserver, cuenta);
        }
        
        return true;
    }
    
    public boolean upPedimentoNumero(Database dbserver, String numero) throws SQLException
    {
        for(Row row : rows)
        {
            row.getItem().upPedimentoNumero(dbserver, numero);
        }
        
        return true;
    }
    
    public boolean upPedimentoAduana(Database dbserver, String aduna) throws SQLException
    {
        for(Row row : rows)
        {
            row.getItem().upPedimentoAduana(dbserver, aduna);
        }
        
        return true;
    }
    
    public boolean upPedimentoFecha(Database dbserver, Date fecha) throws SQLException
    {
        for(Row row : rows)
        {
            row.getItem().upPedimentoFecha(dbserver, fecha);
        }
        
        return true;
    }
    
    public boolean isArrived(Database dbserver) throws SQLException
    {
        for(Row row : rows)
        {
            if(row.getItem().downPurchaseArrival(dbserver))
            {
                continue;
            }
            else
            {
                return false;
            }
        }
        
        return true;
    }
        
    public boolean downCostPurchase(Database dbserver) throws SQLException
    {
        for(Row row : rows)
        {
            row.getItem().downCostPurchase(dbserver);
        }
        return true;
    }
        
        
    public boolean upPurchaseArrival(Database dbserver, java.sql.Date eta) throws SQLException
    {
        for(Row row : rows)
        {
            row.getItem().upPurchaseArrival(dbserver, eta);
        }
        return true;
    }
    
    public java.sql.Date getPurchaseArrival()
    {
        if(!rows.isEmpty())
        {
            return rows.get(0).getItem().getPurchaseArrival();
        }
        else
        {
            return null;
        }
    }
    
    public Moneda getCostPurchaseMoney()
    {
        if(!rows.isEmpty())
        {
            return rows.get(0).getItem().getCostPurchaseMoney();
        }
        else
        {
            return null;
        }
    }
        
    public Flow.Estado getEstado()
    {
        if(!rows.isEmpty())
        {
            return rows.get(0).getItem().getEstado();
        }
        else
        {
            return null;
        }
    }
        
        
    public boolean upEstado(Database dbserver, Flow.Estado estado) throws SQLException
    {
        for(Row row : rows)
        {
            row.getItem().upEstado(dbserver, estado);
        }
        return true;
    }
    
    public double getCostPurchase()
    {
        if(!rows.isEmpty())
        {
            return rows.get(0).getItem().getCostPurchase();
        }
        else
        {
            return 0.0;
        }
    }
        
    public boolean upCostPurchase(Database dbserver, double cost, Moneda moneda) throws SQLException
    {
        for(Row row : rows)
        {
            row.getItem().upCostPurchase(dbserver, cost, moneda);
        }
        return true;
    }
    
    public boolean upQuotation(Database dbserver, Quotation quo) throws SQLException
    {
        for(Row row : rows)
        {
            row.getItem().upQuotation(dbserver.getConnection(), quo);
        }
        
        return true;
    }
        
    public static Return copy(Database dbserver, Operational from, Operational to, boolean excludes) throws SQLException
    {
        List<Renglon> lsFrom = select(dbserver, from, null);
        
        for(Renglon renglon : lsFrom)        
        {
            if(excludes)
            {
                if(renglon.getNumber().equals("SIIL-999") || renglon.getNumber().matches("SER-*"))
                {
                    continue;
                }                
            }
            Return ret = insert(dbserver, to, renglon);
            if(ret.isFail()) return ret;
        }
        
        return new Return(true);
    }
    
    public boolean upPurchaseETA(Database dbserver, java.util.Date eta) throws SQLException
    {
        for(Row row : rows)
        {
            row.getItem().upPurchaseETA(dbserver, eta);
        }
        return true;
    }
    
    public Date getPurchaseETA()
    {
        if(!rows.isEmpty())
        {
            return rows.get(0).getItem().getPurchaseETA();
        }
        else
        {
            return null;
        }
    }
        
    public PO getPO()
    {
        if(!rows.isEmpty())
        {
            return rows.get(0).getItem().getPO();
        }
        else
        {
            return null;
        }
    }
    
    public Quotation getQuotation()
    {
        if(!rows.isEmpty())
        {
            return rows.get(0).getItem().getQuotation();
        }
        else
        {
            return null;
        }
    }
    
    public String getDescription()
    {
        if(!rows.isEmpty())
        {
            return rows.get(0).getItem().getItem().getDescription();
        }
        else
        {
            return null;
        }
    }
    
    public static Return clear(Database dbserver, Operational op) throws SQLException
    {
        String sql = "DELETE FROM " + MYSQL_AVATAR_TABLE + " WHERE op = " + op.getID();
        Statement stmt = dbserver.getConnection().createStatement();
        int afected = stmt.executeUpdate(sql);
        if(afected < 1)
        {
            return new Return(true, "No operable");
        }
                
        return new Return(true,"Operacion completa");
    }
    
    public Return delete(Database connection) throws SQLException
    {
        if(connection == null)
        {
            return new Return(false,"La conexion no es valida");
        }
        
        for(Row row : rows)  
        {
            Return ret = row.delete(connection);
            if(ret.isFail() && row.getId() > 0)
            {
                return new Return(false,"No se pudo eliminar : " + row.getItem().getItem().getNumber());
            }
        }
        
        return new Return(true);         
    }
    
    public static Return insert(Database connection, Operational operational, Renglon renglon) throws SQLException 
    {
        if(operational == null)
        {
            return new Return(false,"operational is NULL");
        }
        
        List<Row> rows = renglon.getRows();
        int groupRenglon = Row.getNextGroupRenglon(connection, operational);
        for(int j = 0; j < rows.size(); j++)
        {
            Return ret = rows.get(j).insert(connection,operational,groupRenglon);
            if(ret.isFail())
            {
                return ret;
            }
        }
        
        return new Return(true);
    }
    
    @Deprecated
    public Return insert(Database connection, Operational operational,int gropRenglon) throws SQLException 
    {
        if(operational == null)
        {
            return new Return(false,"operational is NULL");
        }
        
        for(int j = 0; j < rows.size(); j++)
        {
            Return ret = rows.get(j).insert(connection,operational,gropRenglon);
            if(ret.isFail()) 
            {
                return ret;
            }
        }
        
        return new Return(true);
    }
        
    public boolean upState(Database dbserver, stock.Flow.Estado estado) throws SQLException
    {
        for(Row row : rows)
        {
            row.getItem().upEstado(dbserver, estado);
        }
        return true;
    }
    
    /**
     * 
     * @param connection
     * @param operational
     * @param stored si es true return los renglones que estan actualmente en almacen, si es false returno los renglones que no estan en almacen
     * @param exclude
     * @return
     * @throws SQLException 
     */
    public static List<Renglon> selectStored(Database connection, Operational operational, boolean stored, RenglonExclusion exclude) throws SQLException
    {        
        if(stored)
        {
            String sql = "SELECT processoperationalrows.id FROM processoperationalrows,stockallocated WHERE processoperationalrows.item = stockallocated.item AND op = " + operational.getID();
            return  select(connection, operational, sql, exclude);
        }
        else
        {
            List<Renglon> listA = select(connection, operational, exclude);
            List<Renglon> listNS = new ArrayList<>();
            for(Renglon renglon : listA)
            {
                if(Allocated.isStored(connection, renglon) != true)
                {
                    listNS.add(renglon);
                }
            }
            
            return listNS;
        }
    }
    
    public static List<Renglon> selectArrival(Database connection, Operational operational, boolean arrival, RenglonExclusion exclude) throws SQLException
    {
        String sql = "SELECT processoperationalrows.id FROM processoperationalrows,stockflow WHERE processoperationalrows.item = stockflow.id AND " ;
        if(arrival)            
        {
            sql += " stockflow.purchaseArrival IS NOT NULL AND ";
        }
        else
        {
            sql += " stockflow.purchaseArrival IS NULL AND ";
        }
        sql += " stockflow.purchaseArrival IS NULL AND processoperationalrows.op = " + operational.getID();
        
        return select(connection, operational, sql, exclude);
    }
    
    public static List<Renglon> select(Database connection, Operational operational, RenglonExclusion exclude) throws SQLException
    {
        String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE op = " + operational.getID();
        return select(connection, operational, sql, exclude);
    }
    
    public static List<Renglon> select(Database connection,Operational operational,String select,RenglonExclusion exclude) throws SQLException
    {
        List<Renglon> renglones = new ArrayList<>();
        int max = Row.getMaximoGroupRenglon(connection, operational); 
        Renglon renglon = null;
        ResultSet rs = null;
        Flow flow = null;
        if(max == 0)// los rengones de las primeras versione no se enumeraban por lo tanto el max sera igual a cero
        {
            String sql = select + " ORDER BY groupRenglon ASC, renglon ASC ";
            System.out.println(sql);
            rs = connection.query(sql);                
            while(rs.next())
            {
                Row row = new Row(rs.getInt(1)); 
                renglon = new Renglon();
                renglon.add(row);
                renglon.download(connection, operational, false);
                renglones.add(renglon);
            }
            return renglones;
        }
        
        for(int i = 1; i <= max; i++)
        {
            renglon = new Renglon();
            int maxNumberRenglon = Row.getMaximoNumberRenglon(connection, operational, i);            
            for(int j = 1; j <= maxNumberRenglon; j++)
            {
                String sql = select + " AND renglon = " + j + " AND groupRenglon = " + i; 
                System.out.println(sql);
                rs = connection.query(sql);                
                if(rs.next())
                {
                    Row row = new Row(rs.getInt(1)); 
                    row.download(connection);
                    row.downItem(connection.getConnection());
                    row.getItem().download(connection);
                    row.getItem().getItem().downNumber(connection.getConnection());
                    if(exclude == RenglonExclusion.SIIL_Y_SEVICICOS)
                    {
                        // no agregar, simplemente sige con el ciclo
                        if(row.getItem().getItem().getNumber().equals("SIIL-999")) //excluir SIIL-999
                        {
                            continue;
                        }
                        else if(row.getItem().getItem().getNumber().matches("SER-*"))
                        {
                            continue;
                        }
                    }
                    row.getItem().getItem().downDescription(connection.getConnection());
                    renglon.add(row);
                }
            }
            if(renglon.getRows().size() > 0) renglones.add(renglon);
        }
        return renglones;        
    }
    
    
    public void add(List<Flow> flows)
    {
        if(rows == null) rows = new ArrayList<>();
        
        for(Flow flow : flows)
        {
            Row row = new Row(-1);
            row.add(flow);
            rows.add(row);
        }        
    }
        
    public void add(Row row)
    {
        if(rows == null) rows = new ArrayList<>();
        rows.add(row);
    }
        
    public void add(Flow flow)
    {
        if(rows == null) rows = new ArrayList<>();
        Row row = new Row(-1);
        row.add(flow);
        rows.add(row);
    }
    
    public void add(Flow[] flows)
    {
        if(rows == null) rows = new ArrayList<>();
        for(Flow flow : flows)
        {
            Row row = new Row(-1);
            row.add(flow);
            rows.add(row);
        }
    }
    
    public Return downRows(Database connection, Operational operational) throws SQLException
    {        
        if(operational == null)
        {
            return new Return(false,"La operacion es nula");
        }
        else if(operational.getID() < 1)
        {
            return new Return(false,"La operacion es nula");
        }
        rows = new ArrayList<>();
        this.operational = operational;
        int max = Row.getMaximoGroupRenglon(connection, operational);
        for(int i = 1; i <= max; i++)
        {
            String sql = "SELECT id FROM " + MYSQL_AVATAR_TABLE + " WHERE op = " + operational.getID() + " AND renglon = " + i;        
            Statement stmt = connection.getConnection().createStatement();
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                Row row = new Row(rs.getInt(1));
                row.downItem(connection.getConnection());
                rows.add(row);
            }
        }
        
        return new Return(true);
    }

    
    public static Return insert(Database connection, Operational operational, List<Renglon> renglones) throws SQLException 
    {
        if(operational == null)
        {
            return new Return(false,"operational is NULL");
        }
        if(renglones == null)
        {
            return new Return(false,"item is NULL");
        }
        
        for(int i = 0; i < renglones.size();i++)
        {
            renglones.get(i).insert(connection, operational, i + 1);
        }
        
        return new Return(true);
    }
    
    public boolean upUnidad(Database dbserver, String unidad) throws SQLException
    {
        for(Row row : rows)
        {
            row.getItem().getItem().upUnidad(dbserver, unidad);
        }
        return true;
    }
        
    public String getUnidad()
    {
        return rows.get(0).getItem().getItem().getUnidad();
    }
            
    public Date getPedimentoFecha()
    {
        return rows.get(0).getItem().getPedimentoFecha();
    }
    
    public String getPedimentoNumero()
    {
        return rows.get(0).getItem().getPedimentoNumero();
    }
    
    public String getPedimentoAduana()
    {
        return rows.get(0).getItem().getPedimentoAduana();
    }
    
    public boolean isAduanaActive()
    {
        for(Row row : rows)
        {
            if(row.getItem().isAduanaActive()) return true;
        }
        return false;
    }
    
    public boolean isRefection(Database dbserver) throws SQLException
    {
        for(Row row : rows)
        {
            if(row.getItem().getItem().isRefection(dbserver) == false) return false;
        }
        return true;
    }
    
    public boolean downQuotation(Database dbserver) throws SQLException
    {
        for(Row row : rows)
        {
            if(row.getItem().downQuotation(dbserver.getConnection()) == false)
            {
                return false;
            }
        }
        
        return true;
    }
        
    public boolean downCostSale(Database dbserver) throws SQLException
    {
        for(Row row : rows)
        {
            row.getItem().downCostSale(dbserver);
        }
        return true;
    }
     
    public Return download(Database dbserver, Operational operational,boolean load) throws SQLException
    {
        if(load) downRows(dbserver, operational);
        for(Row row : rows)
        {
            if(row.downItem(dbserver.getConnection()))
            {
                row.getItem().download(dbserver);
                row.getItem().downCostPurchase(dbserver);
                row.getItem().downEstado(dbserver);
                row.getItem().downPedimento(dbserver);
                if(row.getItem().downItem(dbserver.getConnection()))
                {
                    row.getItem().getItem().downNumber(dbserver.getConnection());
                    row.getItem().getItem().downDescription(dbserver.getConnection());
                    row.getItem().getItem().downUnidad(dbserver);
                    row.getItem().getItem().downCuentaFiscal(dbserver);
                }
            }
        }
        return new Return(true);
    }
     
    @Deprecated
    public Return download(Database dbserver,Operational operational) throws SQLException
    {
        downRows(dbserver, operational);
        for(Row row : rows)
        {
            row.getItem().download(dbserver);
            if(row.getItem().downItem(dbserver.getConnection()))
            {
                row.getItem().getItem().downNumber(dbserver.getConnection());
                row.getItem().getItem().downDescription(dbserver.getConnection());
                row.getItem().getItem().downUnidad(dbserver);
            }
        }        
        return new Return(true);
    }
    
    public boolean upPO(Database dbserver, PO po) throws SQLException
    {
        for(Row row : rows)
        {
            row.getItem().upPO(dbserver.getConnection(), po);
        }
        return true;
    }
    
    public boolean upCostSale(Database dbserver, double cost, Moneda moneda) throws SQLException
    {
        for(Row row : rows)
        {
            row.getItem().upCostSale(dbserver, cost, moneda);
        }
        return true;
    }

    private void validInput() 
    {
        if(this.rows.size() > 1)
        {
            for(int i = 1; i < this.rows.size(); i++)
            {
                if(this.rows.get(0).getItem().getID() != this.rows.get(i).getItem().getID())
                {
                    throw new FailResultOperationException("Los item de un mismo renglon deven ser iguales.");
                }
            }
        }
    }
    
    public double getCantidad()
    {
        return rows.size();
    }
    
    /**
     * @return the renglo
     */
    public List<Row> getRows() 
    {
        if(rows == null) rows = new ArrayList<>();
        
        return rows;
    }

    /**
     * @return the precioUnitario
     */
    public double getPrecioUnitario() 
    {
        return precioUnitario;
    }

    /**
     * @return the importe
     */
    public double getImporte() 
    {
        return importe;
    }

    /**
     * @return the number
     */
    public String getNumber() 
    {
        return rows.get(0).getItem().getItem().getNumber();
    }
    
    public String getDescripcion() 
    {
        return rows.get(0).getItem().getItem().getDescription();
    }
    
    
    public double getCostSales()
    {
        return rows.get(0).getItem().getCostSale();
    }
    
    public Moneda getCostSalesMoney()
    {
        return rows.get(0).getItem().getCostSaleMoney();
    }
    
    public double getTotal()
    {
        return getCostSales() * getCantidad();
    }
}
